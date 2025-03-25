package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IChargebackCPService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.ITiRecursoService;
import com.ciandt.pms.model.ChargebackContratoPratica;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.vo.ChargebackCell;
import com.ciandt.pms.model.vo.ChargebackRow;
import com.ciandt.pms.persistence.dao.IChargebackCPDao;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe ChargebackCPService proporciona as funcionalidades de serviço
 * referente a entidade ChargebackContratoPratica.
 * 
 * @since 16/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ChargebackCPService implements IChargebackCPService {

    /** Instancia do dao. */
    @Autowired
    private IChargebackCPDao chargebackDao;

    /** Instancia do serviço do ContratoPraticaService. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /** Instancia do serviço do ContratoPraticaService. */
    @Autowired
    private ITiRecursoService tiRecursoService;

    /** Instancia do servico Modulo. */
    @Autowired
    private IModuloService moduloService;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Busca e prepara a o Mapa de Chargeback para o gerenciamento, referente a
     * um recurso.
     * 
     * @param recurso
     *            - entidade do tipo ItRecurso.
     * @param startDate
     *            - data inicio do periodo
     * @param endDate
     *            - data fim do periodo
     * 
     * @return returna um lista de ChargebackRow referente ao mapa de
     *         Chargeback.
     */
    public List<ChargebackRow> findAndPrepareManagePerResource(
            final TiRecurso recurso, final Date startDate, final Date endDate) {

        List<ChargebackContratoPratica> chargebackList = chargebackDao
                .findByRecursoAndPeriod(recurso, startDate, endDate);

        return generateRowListByRecurso(recurso, chargebackList, startDate,
                endDate);

    }

    /**
     * Busca e prepara a o Mapa de Chargeback para o gerenciamento, referente a
     * um contrato-prática.
     * 
     * @param cp
     *            - entidade do tipo ContratoPratica.
     * @param startDate
     *            - data inicio do periodo
     * @param endDate
     *            - data fim do periodo
     * 
     * @return returna um lista de ChargebackRow referente ao mapa de
     *         Chargeback.
     */
    public List<ChargebackRow> findAndPrepareManagePerContract(
            final ContratoPratica cp, final Date startDate, final Date endDate) {

        List<ChargebackContratoPratica> chargebackList = chargebackDao
                .findByContractAndPeriod(cp, startDate, endDate);

        return generateRowListByContract(cp, chargebackList, startDate, endDate);

    }

    /**
     * Cria a linha do Mapa.
     * 
     * @param chargeback
     *            - entidade do tipo ChargebackContratoPratica.
     * @param startDate
     *            - data inicio do peridod
     * @param endDate
     *            = data fim do periodo
     * 
     * @return a linha criada, entidade do tipo ChargebackRow.
     */
    public ChargebackRow createRow(final ChargebackContratoPratica chargeback,
            final Date startDate, final Date endDate) {

        boolean isClosed;
        List<Date> dateList = DateUtil.getValidityDateList(startDate, endDate);

        ChargebackContratoPratica c;
        ChargebackCell cell;
        List<ChargebackCell> cellList = new ArrayList<ChargebackCell>();
        for (Date monthDate : dateList) {
            // verifica se o mes está fechado
            isClosed = !DateUtil.after(monthDate, getClosingDateChargeback());

            // realiza uma busca pela chave unica
            c = chargebackDao.findByUniqueKey(chargeback.getTiRecurso(),
                    chargeback.getContratoPratica(), monthDate);

            // se não existe alocação para este mês.
            if (c == null) {
                // cria a celula e a alocação do mapa
                cell = new ChargebackCell();
                c = new ChargebackContratoPratica();
                c.setContratoPratica(chargeback.getContratoPratica());
                c.setTiRecurso(chargeback.getTiRecurso());
                c.setNumeroUnidades(chargeback.getNumeroUnidades());
                c.setDataMes(monthDate);

                // se mês fechado cria alocação com zero e não persiste
                if (isClosed) {
                    c.setNumeroUnidades(BigDecimal.valueOf(0.0));
                    cell.setReadonly(Boolean.TRUE);
                    // se mes Nâo esta fechado cria uma nova aloação.
                } else {
                    c.setNumeroUnidades(chargeback.getNumeroUnidades());
                    this.createChargeback(c);
                }

                cell.setChargebackCP(c);

                cellList.add(cell);
                // se existe e mes NÃO está fechado
                // soma o valor existente ao novo valor inserido
            } else if (!isClosed) {
                BigDecimal numeroUnidades = c.getNumeroUnidades();
                c.setNumeroUnidades(numeroUnidades.add(chargeback
                        .getNumeroUnidades()));

                this.chargebackDao.update(c);
            }
        }

        ChargebackRow row = new ChargebackRow();
        row.setCp(chargeback.getContratoPratica());
        row.setTiRecurso(chargeback.getTiRecurso());
        row.setCellList(cellList);

        return row;
    }

    /**
     * Remove todas as linhas selecionadas.
     * 
     * @param rowList
     *            - Lista com as linas
     * 
     * @return retorna true se removido com sucesso, caso contrario false.
     */
    public Boolean removeAllSelectedRow(final List<ChargebackRow> rowList) {

        for (ChargebackRow row : rowList) {
            if (row.getIsSelected()) {

                List<ChargebackCell> cellList = row.getCellList();
                for (ChargebackCell cell : cellList) {
                    if (!cell.getReadonly()) {
                        ChargebackContratoPratica chargeback = cell
                                .getChargebackCP();

                        if (chargeback.getCodigoChargebackCp() != null) {
                            ChargebackContratoPratica cb = findChargebackById(chargeback
                                    .getCodigoChargebackCp());
                            // verificação de segurança
                            if (cb != null) {
                                this.removeChargeback(cb);
                            }
                        }
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

    /**
     * Remove todas as linhas selecionadas.
     * 
     * @param numUnits
     *            - Numero de unidades
     * @param rowList
     *            - Lista com as linas
     * 
     * @return retorna true se removido com sucesso, caso contrario false.
     */
    public Boolean updateAllNumUnits(final BigDecimal numUnits,
            final List<ChargebackRow> rowList) {

        for (ChargebackRow row : rowList) {
            if (row.getIsSelected()) {

                List<ChargebackCell> cellList = row.getCellList();
                for (ChargebackCell cell : cellList) {
                    if (!cell.getReadonly()) {
                        ChargebackContratoPratica chargeback = cell
                                .getChargebackCP();
                        // se id diferente de null, então deve ser atualizado ou
                        // removido
                        if (chargeback.getCodigoChargebackCp() != null) {
                            ChargebackContratoPratica cb = findChargebackById(chargeback
                                    .getCodigoChargebackCp());
                            // verificação de segurança
                            if (cb != null) {
                                cb.setNumeroUnidades(numUnits);
                                // se numero unidades diferente de zero realiza
                                // o update
                                if (new BigDecimal(0.0).compareTo(cb
                                        .getNumeroUnidades()) != 0) {
                                    this.updateChargeback(cb);
                                    // se numero unidades igual a zero realiza a
                                    // remoção
                                } else {
                                    this.removeChargeback(cb);
                                }
                            }
                            // se id igual a null e numero de unidades diferente
                            // de zero insere.
                        } else {
                            chargeback.setNumeroUnidades(numUnits);
                            if (new BigDecimal(0.0).compareTo(chargeback
                                    .getNumeroUnidades()) != 0) {
                                initChargebackSession(chargeback);

                                this.createChargeback(chargeback);
                            }
                        }
                    }
                }
            }
        }

        return Boolean.TRUE;
    }

    /**
     * Gera a lista de contratrato-praticas do mapa.
     * 
     * @param recurso
     *            - entidade do tipo TiRecurso
     * @param chargebackList
     *            - Lista de chargeback
     * @param startDate
     *            - Data inicio da vigencia
     * @param endDate
     *            - data fim da vigeancia
     * 
     * @return retorna uma lista ChargebackRow que representa o mapa de alocação
     */
    private List<ChargebackRow> generateRowListByRecurso(
            final TiRecurso recurso,
            final List<ChargebackContratoPratica> chargebackList,
            final Date startDate, final Date endDate) {

        List<ChargebackRow> rowList = new ArrayList<ChargebackRow>();

        Map<String, ChargebackContratoPratica> chargebackMap = translateChargebackListToMap(chargebackList);

        List<Date> dateList = DateUtil.getValidityDateList(startDate, endDate);
        List<ContratoPratica> cpList = contratoPraticaService
                .findContratoPraticaByTiRecursoAndPeriod(recurso, startDate,
                        endDate);

        ChargebackRow row;
        List<ChargebackCell> cellList;
        ChargebackCell cell;
        for (ContratoPratica contratoPratica : cpList) {
            row = new ChargebackRow();
            cellList = new ArrayList<ChargebackCell>();

            for (Date monthDate : dateList) {

                cell = new ChargebackCell();

                ChargebackContratoPratica chargeback = chargebackMap
                        .get(generateChargebackKey(monthDate, contratoPratica,
                                recurso));

                if (chargeback == null) {
                    chargeback = new ChargebackContratoPratica();
                    chargeback.setContratoPratica(contratoPratica);
                    chargeback.setTiRecurso(recurso);
                    chargeback.setDataMes(monthDate);
                    chargeback.setNumeroUnidades(BigDecimal.valueOf(0.0));
                }

                cell.setChargebackCP(chargeback);

                if (!DateUtil.after(monthDate, getClosingDateChargeback())) {
                    cell.setReadonly(Boolean.TRUE);
                }

                cellList.add(cell);
            }

            row.setCellList(cellList);
            row.setCp(contratoPratica);
            row.setTiRecurso(recurso);

            rowList.add(row);
        }

        return rowList;
    }

    /**
     * Gera a lista de contratrato-praticas do mapa.
     * 
     * @param contratoPratica
     *            - entidade do tipo ContratoPratica
     * @param chargebackList
     *            - Lista de chargeback
     * @param startDate
     *            - Data inicio da vigencia
     * @param endDate
     *            - data fim da vigeancia
     * 
     * @return retorna uma lista ChargebackRow que representa o mapa de alocação
     */
    private List<ChargebackRow> generateRowListByContract(
            final ContratoPratica contratoPratica,
            final List<ChargebackContratoPratica> chargebackList,
            final Date startDate, final Date endDate) {

        List<ChargebackRow> rowList = new ArrayList<ChargebackRow>();

        Map<String, ChargebackContratoPratica> chargebackMap = translateChargebackListToMap(chargebackList);

        List<Date> dateList = DateUtil.getValidityDateList(startDate, endDate);
        List<TiRecurso> recursoList = tiRecursoService
                .findTiRecursoByContractAndPeriod(contratoPratica, startDate,
                        endDate);

        ChargebackRow row;
        List<ChargebackCell> cellList;
        ChargebackCell cell;
        for (TiRecurso tiRec : recursoList) {
            row = new ChargebackRow();
            cellList = new ArrayList<ChargebackCell>();

            for (Date monthDate : dateList) {

                cell = new ChargebackCell();

                ChargebackContratoPratica chargeback = chargebackMap
                        .get(generateChargebackKey(monthDate, contratoPratica,
                                tiRec));

                if (chargeback == null) {
                    chargeback = new ChargebackContratoPratica();
                    chargeback.setContratoPratica(contratoPratica);
                    chargeback.setTiRecurso(tiRec);
                    chargeback.setDataMes(monthDate);
                    chargeback.setNumeroUnidades(BigDecimal.valueOf(0.0));
                }

                cell.setChargebackCP(chargeback);

                if (!DateUtil.after(monthDate, getClosingDateChargeback())) {
                    cell.setReadonly(Boolean.TRUE);
                }

                cellList.add(cell);
            }

            row.setCellList(cellList);
            row.setCp(contratoPratica);
            row.setTiRecurso(tiRec);

            rowList.add(row);
        }

        return rowList;
    }

    /**
     * Transforma a LIST de chargeback em um MAP. Onde a chave deste map é:
     * 
     * KEY: [Data Mes]_[Cod. Contrato Pratica]_[Codigo TiRecurso]
     * 
     * @param chargebackList
     *            - lista de chargeback
     * 
     * @return retorna o Map referente a lista passado por parametro
     */
    private Map<String, ChargebackContratoPratica> translateChargebackListToMap(
            final List<ChargebackContratoPratica> chargebackList) {

        Map<String, ChargebackContratoPratica> chargebackRet = new HashMap<String, ChargebackContratoPratica>();

        for (ChargebackContratoPratica c : chargebackList) {
            String key = generateChargebackKey(c.getDataMes(), c
                    .getContratoPratica(), c.getTiRecurso());

            chargebackRet.put(key, c);
        }

        return chargebackRet;
    }

    /**
     * Gera uma string que representa a chave de um TiChargeback. Onde a chave
     * deste map é:
     * 
     * KEY: [Data Mes]_[Cod. Contrato Pratica]_[Codigo TiRecurso]
     * 
     * @param monthDate
     *            - Data mes
     * @param cp
     *            - entidade ContratoPratica
     * @param recurso
     *            entidade TiRecurso
     * 
     * @return retorna a chave em formato de string
     */
    private String generateChargebackKey(final Date monthDate,
            final ContratoPratica cp, final TiRecurso recurso) {
        return monthDate.getTime() + "_" + cp.getCodigoContratoPratica() + "_"
                + recurso.getCodigoTiRecurso();
    }

    /**
     * Realiza o update do 'Mapa' do chargeback.
     * 
     * @param chargebackRowList
     *            - Lista do Mapa do chargeback.
     * 
     * @return retorna true se update realizado com sucesso, caso contrario
     *         false.
     */
    public Boolean updateManageChargeback(
            final List<ChargebackRow> chargebackRowList) {
        ChargebackContratoPratica chargeback;

        for (ChargebackRow row : chargebackRowList) {
            List<ChargebackCell> cellList = row.getCellList();

            for (ChargebackCell cell : cellList) {
                chargeback = cell.getChargebackCP();

                BigDecimal numUnidades = chargeback.getNumeroUnidades();

                if (numUnidades == null
                        && BigDecimal.valueOf(0.0).compareTo(numUnidades) == 0) {
                    if (chargeback.getCodigoChargebackCp() != null) {
                        this.removeChargeback(chargeback);
                    }
                } else {
                    initChargebackSession(chargeback);

                    if (chargeback.getCodigoChargebackCp() != null) {
                        this.updateChargeback(chargeback);
                    } else {
                        this.createChargeback(chargeback);
                    }
                }

            }
        }

        return Boolean.TRUE;
    }

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createChargeback(final ChargebackContratoPratica entity) {
        // somente cria a entidade se o numero de unidades for
        // diferente de zero.
        if (BigDecimal.valueOf(0.0).compareTo(entity.getNumeroUnidades()) != 0) {
            chargebackDao.create(entity);
        }
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que sera atualizada.
     * 
     */
    public void updateChargeback(final ChargebackContratoPratica entity) {
        chargebackDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que sera apagada.
     * 
     */
    public void removeChargeback(final ChargebackContratoPratica entity) {
        ChargebackContratoPratica c = findChargebackById(entity
                .getCodigoChargebackCp());
        if (c != null) {
            chargebackDao.remove(c);
        }
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public ChargebackContratoPratica findChargebackById(final Long id) {
        return chargebackDao.findById(id);
    }

    /**
     * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente o
     * atributo 'naturezaCentroLucro'.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<ChargebackContratoPratica> findChargebackByFilter(
            final ChargebackContratoPratica filter) {
        if (1 == 1) {
            throw new NotYetImplementedException(
                    "Método 'findChargebackByFilter' não implementado");
        }

        return null;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<ChargebackContratoPratica> findChargebackAll() {
        if (1 == 1) {
            throw new NotYetImplementedException(
                    "Método 'findChargebackByFilter' não implementado");
        }

        return null;
    }

    /**
     * Pega a data inicio default para o filtro.
     * 
     * @return retorna a data inicio
     */
    public Date getDefaultStartDate() {
        return DateUtils.addMonths(new Date(), -2);
    }

    /**
     * Pega a data fim default para o filtro.
     * 
     * @return retorna a data fim
     */
    public Date getDefaultEndDate() {
        return DateUtils.addMonths(new Date(), 3);
    }

    /**
     * Pega a data de fechamento do MapaAlocacao.
     * 
     * @return retorna a Data de Fechamento.
     */
    private Date getClosingDateChargeback() {
        // pega a data de fechamento do modulo do MapaAlocacao
        Modulo moduloChargeback = moduloService
                .findModuloById(new Long(systemProperties
                        .getProperty(Constants.MODULO_CHARGEBACK_CODE)));

        return DateUtils.truncate(moduloChargeback.getDataFechamento(),
                Calendar.MONTH);
    }

    /**
     * WORKAROUND para inicializar a sessao do hiberante.
     * 
     * @param chargeback
     *            - entidade do tipo ChargebackContratoPratica.
     */
    private void initChargebackSession(
            final ChargebackContratoPratica chargeback) {
        // ****** Inicializa a sessão **********
        chargeback.setContratoPratica(contratoPraticaService
                .findContratoPraticaById(chargeback.getContratoPratica()
                        .getCodigoContratoPratica()));
        chargeback.setTiRecurso(tiRecursoService.findTiRecursoById(chargeback
                .getTiRecurso().getCodigoTiRecurso()));
        // *************************************
    }

}
