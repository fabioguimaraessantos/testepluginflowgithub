package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ciandt.pms.business.service.*;
import com.ciandt.pms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.vo.ChargebackCell;
import com.ciandt.pms.model.vo.ChargebackRow;
import com.ciandt.pms.model.vo.LicensaEmailRow;
import com.ciandt.pms.persistence.dao.IChargebackPessDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.MailSenderUtil;


/**
 * 
 * A classe ChargebackPessService proporciona as funcionalidades de serviço
 * referente a entidade ChargebackPessoa.
 * 
 * @since 01/07/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class ChargebackPessService implements IChargebackPessService {

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** Intancia de mailSender. */
    private MailSenderUtil mailSender;

    /** Instancia do dao. */
    @Autowired
    private IChargebackPessDao chbackPessDao;

    /** Instancia do serviço da Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do serviço do TiRecurso. */
    @Autowired
    private ITiRecursoService tiRecursoService;

    /** Instancia do serviço do Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /** Instancia do serviço do CustoTiRecurso. */
    @Autowired
    private ICustoTiRecursoService custoTiRecursoService;

    /** Instancia do servico Modulo. */
    @Autowired
    private IModuloService moduloService;

    @Autowired
    private IRateioLicencaSwService rateioLicencaSwService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    public Boolean createChbackPess(final ChargebackPessoa entity) {
        // somente cria a entidade se o numero de unidades for
        // diferente de zero.
        if (BigDecimal.valueOf(0.0).compareTo(entity.getNumeroUnidades()) != 0) {
            if (this.existsChbackPess(entity.getPessoa(),entity.getTiRecurso(), entity.getDataMes())) {
                return Boolean.valueOf(false);
            }
            chbackPessDao.create(entity);

            return Boolean.valueOf(true);
        } else {
            return Boolean.valueOf(false);
        }
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que sera atualizada.
     * @param isDifferentReg
     *            indicador se registros são diferentes ou não
     * 
     * @return true se atualizado com sucesso, caso contrario retorna false
     */
    public Boolean updateChbackPess(final ChargebackPessoa entity,
            final Boolean isDifferentReg) {
        // se os logins forem diferentes, quer dizer que foi editado o campo
        // login e precisa verificar se já existe a relação login/mês na base.
        // Caso for o mesmo login não faz a verificação e atualiza normalmente.
        if (isDifferentReg
                && this.existsChbackPess(entity.getPessoa(), entity
                        .getTiRecurso(), entity.getDataMes())) {
            return Boolean.valueOf(false);
        }

        chbackPessDao.update(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que sera apagada.
     * 
     */
    public void removeChbackPess(final ChargebackPessoa entity) {
        ChargebackPessoa chbackPess =
                this.findChbackPessById(entity.getCodigoChargebackPess());
        if (chbackPess != null) {
            chbackPessDao.remove(chbackPess);
        }
    }

    /**
     * Remove as entidades pela data e tipo.
     * 
     * @param dataMes
     *            - data da vigencia.
     * @param indicadorTipo
     *            - tipo MN ou SY.
     * 
     * @return true se a remoção ocorrou corretamente. False caso contrário.
     */
    private Boolean removeChbackPess(final Date dataMes,
            final String indicadorTipo) {
        return chbackPessDao.removeByDateAndType(dataMes, indicadorTipo);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public ChargebackPessoa findChbackPessById(final Long id) {
        return chbackPessDao.findById(id);
    }

    /**
     * Busca e prepara a o Mapa de Chargeback para o gerenciamento, referente a
     * um recurso.
     * 
     * @param tiRecurso
     *            - entidade do tipo ItRecurso.
     * @param startDate
     *            - data inicio do periodo
     * @param endDate
     *            - data fim do periodo
     * 
     * @return returna um lista de ChargebackRow referente ao mapa de
     *         Chargeback.
     */
    public List<ChargebackRow> findChbackPessAndPrepareManagePerItResource(
            final TiRecurso tiRecurso, final Date startDate, final Date endDate) {

        List<ChargebackPessoa> chargebackList =
                chbackPessDao.findByTiRecursoAndPeriod(tiRecurso, startDate,
                        endDate);

        return this.generateRowListByTiRecurso(tiRecurso, chargebackList,
                startDate, endDate);

    }

    /**
     * Envia um email para os usuarios que possuem licença de software alocadas.
     */
    public void sendSoftwareLicenseMail() {
        Date dataMes = new Date();
        GregorianCalendar gcInicial = new GregorianCalendar();
        gcInicial.setTime(dataMes);
        gcInicial.set(GregorianCalendar.DAY_OF_MONTH, 1);
        gcInicial.add(GregorianCalendar.MONTH, -1);

        List<Long> chargeBackPessoaList =
                chbackPessDao.findByMonth(gcInicial.getTime());
        
        List<LicensaEmailRow> licencaEmailRowList = null;

        BigDecimal total = null;

        CustoTiRecurso custoUnitario = new CustoTiRecurso();

        Moeda moeda = new Moeda();

        for (Long cp : chargeBackPessoaList) {

            licencaEmailRowList = new ArrayList<LicensaEmailRow>();
            total = new BigDecimal(0);

            Pessoa pessoa = pessoaService.findPessoaById(cp);

            ChargebackPessoa chargebackPessoa = new ChargebackPessoa();

            String to = pessoa.getTextoEmail();
            String subject =
                    systemProperties
                            .getProperty("software_license.mail.subject");
            String link = systemProperties.getProperty("jira_web_link");

            int ultimoDiaMes = DateUtil.getLastDayOfMonth(dataMes);

            GregorianCalendar gcFinal = new GregorianCalendar();
            gcFinal.setTime(dataMes);
            gcFinal.set(GregorianCalendar.DAY_OF_MONTH, ultimoDiaMes);

            List<TiRecurso> trList =
                    tiRecursoService.findTiRecursoByPessoaAndPeriodAndType(
                            pessoa, gcInicial.getTime(), gcFinal.getTime(),
                            "SU");

            Boolean sendMail = Boolean.valueOf(false);

            List<TiRecurso> trListMail = new ArrayList<TiRecurso>();

            for (TiRecurso tr : trList) {
                LicensaEmailRow licensaEmailRow = new LicensaEmailRow();

                tr =
                        tiRecursoService.findTiRecursoById(tr
                                .getCodigoTiRecurso());

                tr.setMoeda(moedaService.findMoedaByTiRecurso(tr));

                List<CustoTiRecurso> custoList =
                        new ArrayList<CustoTiRecurso>();

                List<CustoTiRecurso> custoTiRecursoList =
                        custoTiRecursoService.findCustoTiRecursoByTiRecurso(tr);

                for (CustoTiRecurso custo : custoTiRecursoList) {

                    custo =
                            custoTiRecursoService.findCustoTiRecById(custo
                                    .getCodigoCustoTiRecurso());

                    if (custo.getDataFim() == null) {
                        custo.setValorCustoTiRecursoString(custo
                                .getValorCustoTiRecurso().setScale(2,
                                        BigDecimal.ROUND_UP).toString());
                        custoList.add(custo);
                        custoUnitario = custo;
                        sendMail = Boolean.valueOf(true);
                        break;
                    } else {
                        int ultimoDiaMesFim =
                                DateUtil.getLastDayOfMonth(custo.getDataFim());
                        GregorianCalendar gcDataFim = new GregorianCalendar();
                        gcDataFim.setTime(custo.getDataFim());
                        gcDataFim.set(GregorianCalendar.DAY_OF_MONTH,
                                ultimoDiaMesFim);

                        Date dataAtual = new Date();

                        if (custo.getDataInicio().before(dataAtual)
                                && gcDataFim.getTime().after(dataAtual)) {

                            custo.setValorCustoTiRecursoString(custo
                                    .getValorCustoTiRecurso().setScale(2,
                                            BigDecimal.ROUND_UP).toString());
                            custoList.add(custo);
                            custoUnitario = custo;
                            sendMail = Boolean.valueOf(true);
                            break;
                        }
                    }
                }

                chargebackPessoa =
                        this.findChbackPessByUniqueKey(tr, pessoa, gcInicial
                                .getTime());

                trListMail.add(tr);

                moeda = tr.getMoeda();

                licensaEmailRow.setTiRecurso(tr);
                licensaEmailRow.setNumeroUnidades(chargebackPessoa
                        .getNumeroUnidades());
                licensaEmailRow.setCustoUnitario(custoUnitario
                        .getValorCustoTiRecurso().setScale(2,
                                RoundingMode.HALF_UP));
                licensaEmailRow.setValorTotal(licensaEmailRow
                        .getNumeroUnidades().multiply(
                                custoUnitario.getValorCustoTiRecurso())
                        .setScale(2, RoundingMode.HALF_UP));

                total = total.add(licensaEmailRow.getValorTotal());

                licencaEmailRowList.add(licensaEmailRow);

            }

            if (sendMail) {
                Format formatter;

                formatter = new SimpleDateFormat("MMM/yyyy");

                Map<String, Object> dataSource = new HashMap<String, Object>();
                dataSource.put("licencaEmailRowList", licencaEmailRowList);
                dataSource.put("total", total);
                dataSource.put("moeda", moeda);
                dataSource.put("softwareLicenseList", trListMail);
                dataSource.put("login", pessoa.getCodigoLogin());
                dataSource
                        .put("dataMes", formatter.format(gcInicial.getTime()));
                dataSource.put("link", link);

                // pega o tamplete de email e insere os dados
                String message =
                        mailSender.getTemplateMailMessage("softwareLicense.vm",
                                dataSource);

                // envia o email
                mailSender.sendHtmlMail(to, subject, message);
            }
        }
    }

    /**
     * Busca e prepara a o Mapa de Chargeback para o gerenciamento, referente a
     * uma Pessoa.
     * 
     * @param pessoa
     *            - entidade do tipo Pessoa.
     * @param startDate
     *            - data inicio do periodo
     * @param endDate
     *            - data fim do periodo
     * 
     * @return returna um lista de ChargebackRow referente ao mapa de
     *         Chargeback.
     */
    public List<ChargebackRow> findChbackPessAndPrepareManagePerPessoa(
            final Pessoa pessoa, final Date startDate, final Date endDate) {

        List<ChargebackPessoa> chbackPessList =
                chbackPessDao.findByPessoaAndPeriod(pessoa, startDate, endDate);

        return this.generateRowListByPessoa(pessoa, chbackPessList, startDate,
                endDate);
    }

    /**
     * Realiza uma busca por ChargebackPessoa por Pessoa, TiRecurso e Data.
     * Estes tres compoem uma cahva unica.
     * 
     * @param tiRecurso
     *            do tipo TiRecurso.
     * 
     * @param pessoa
     *            do tipo Pessoa.
     * 
     * @param monthDate
     *            data mes.
     * 
     * @return lista de ChargebackPessoa que atendem ao criterio de filtro
     */
    public ChargebackPessoa findChbackPessByUniqueKey(
            final TiRecurso tiRecurso, final Pessoa pessoa, final Date monthDate) {
        return chbackPessDao.findByUniqueKey(tiRecurso, pessoa, monthDate);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param multCodTiRecMap
     *            multiplos codigos de TiRecurso.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<ChargebackPessoa> findChbackPessByFilter(
            final ChargebackPessoa filter,
            final Map<Long, String> multCodTiRecMap) {
        return chbackPessDao.findByFilter(filter, multCodTiRecMap);
    }

    /**
     * Busca uma lista de entidades pelo filtro e com algum valor nulo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<ChargebackPessoa> findChbackPessByFilterMissBlank(
            final ChargebackPessoa filter) {
        return chbackPessDao.findByFilterMissBlank(filter);
    }

    /**
     * Gera a lista de Pessoa do mapa pelo TiRecurso.
     * 
     * @param tiRecurso
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
    private List<ChargebackRow> generateRowListByTiRecurso(
            final TiRecurso tiRecurso,
            final List<ChargebackPessoa> chargebackList, final Date startDate,
            final Date endDate) {

        List<ChargebackRow> rowList = new ArrayList<ChargebackRow>();

        Map<String, ChargebackPessoa> chbackPessMap =
                this.translateChargebackListToMap(chargebackList);

        List<Date> dateList = DateUtil.getValidityDateList(startDate, endDate);
        List<Pessoa> pessList =
                pessoaService.findPessByTiRecursoAndPeriod(tiRecurso,
                        startDate, endDate);

        ChargebackRow row;
        List<ChargebackCell> cellList;
        ChargebackCell cell;
        for (Pessoa pessoa : pessList) {
            row = new ChargebackRow();
            cellList = new ArrayList<ChargebackCell>();

            for (Date monthDate : dateList) {

                cell = new ChargebackCell();

                ChargebackPessoa chbackPess =
                        chbackPessMap.get(this.generateChargebackKey(monthDate,
                                pessoa, tiRecurso));

                if (chbackPess == null) {
                    chbackPess = new ChargebackPessoa();
                    chbackPess.setPessoa(pessoa);
                    chbackPess.setTiRecurso(tiRecurso);
                    chbackPess.setDataMes(monthDate);
                    chbackPess.setNumeroUnidades(BigDecimal.valueOf(0));
                }

                cell.setChargebackPess(chbackPess);

                if (!DateUtil.after(monthDate, moduloService
                        .getClosingDateChargeBack())) {
                    cell.setReadonly(Boolean.valueOf(true));
                }

                cellList.add(cell);
            }

            row.setCellList(cellList);
            row.setPessoa(pessoa);
            row.setTiRecurso(tiRecurso);

            rowList.add(row);
        }

        return rowList;
    }

    /**
     * Gera a lista de TiRecurso do mapa pela Pessoa.
     * 
     * @param pessoa
     *            - entidade do tipo Pessoa
     * @param chbackPessList
     *            - Lista de chargeback Pessoa
     * @param startDate
     *            - Data inicio da vigencia
     * @param endDate
     *            - data fim da vigeancia
     * 
     * @return retorna uma lista ChargebackRow que representa o mapa de alocação
     */
    private List<ChargebackRow> generateRowListByPessoa(final Pessoa pessoa,
            final List<ChargebackPessoa> chbackPessList, final Date startDate,
            final Date endDate) {

        List<ChargebackRow> rowList = new ArrayList<ChargebackRow>();

        Map<String, ChargebackPessoa> chargebackMap =
                this.translateChargebackListToMap(chbackPessList);

        List<Date> dateList = DateUtil.getValidityDateList(startDate, endDate);
        List<TiRecurso> tiRecursoList =
                tiRecursoService.findTiRecursoByPessoaAndPeriod(pessoa,
                        startDate, endDate);

        ChargebackRow row;
        List<ChargebackCell> cellList;
        ChargebackCell cell;
        for (TiRecurso tiRec : tiRecursoList) {
            row = new ChargebackRow();
            cellList = new ArrayList<ChargebackCell>();

            for (Date monthDate : dateList) {

                cell = new ChargebackCell();

                ChargebackPessoa chbackPess =
                        chargebackMap.get(this.generateChargebackKey(monthDate,
                                pessoa, tiRec));

                if (chbackPess == null) {
                    chbackPess = new ChargebackPessoa();
                    chbackPess.setPessoa(pessoa);
                    chbackPess.setTiRecurso(tiRec);
                    chbackPess.setDataMes(monthDate);
                    chbackPess.setNumeroUnidades(BigDecimal.valueOf(0));
                }

                cell.setChargebackPess(chbackPess);

                if (!DateUtil.after(monthDate, moduloService
                        .getClosingDateChargeBack())) {
                    cell.setReadonly(Boolean.valueOf(true));
                }

                cellList.add(cell);
            }

            row.setCellList(cellList);
            row.setPessoa(pessoa);
            row.setTiRecurso(tiRec);

            rowList.add(row);
        }

        return rowList;
    }

    /**
     * Transforma a LIST de chargeback em um MAP. Onde a chave deste map é:
     * 
     * KEY: [Data Mes]_[Cod. Pessoa]_[Codigo TiRecurso]
     * 
     * @param chargebackList
     *            - lista de chargeback
     * 
     * @return retorna o Map referente a lista passado por parametro
     */
    private Map<String, ChargebackPessoa> translateChargebackListToMap(
            final List<ChargebackPessoa> chargebackList) {

        Map<String, ChargebackPessoa> chargebackRet =
                new HashMap<String, ChargebackPessoa>();

        for (ChargebackPessoa chp : chargebackList) {
            String key =
                    this.generateChargebackKey(chp.getDataMes(), chp
                            .getPessoa(), chp.getTiRecurso());

            chargebackRet.put(key, chp);
        }

        return chargebackRet;
    }

    /**
     * Gera uma string que representa a chave de um TiChargeback. Onde a chave
     * deste map é:
     * 
     * KEY: [Data Mes]_[Cod. Pessoa]_[Codigo TiRecurso]
     * 
     * @param monthDate
     *            - Data mes
     * @param pessoa
     *            - entidade Pessoa
     * @param tiRecurso
     *            entidade TiRecurso
     * 
     * @return retorna a chave em formato de string
     */
    private String generateChargebackKey(final Date monthDate,
            final Pessoa pessoa, final TiRecurso tiRecurso) {
        return monthDate.getTime() + "_" + pessoa.getCodigoPessoa() + "_"
                + tiRecurso.getCodigoTiRecurso();
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
    public Boolean updateManageChargebackPess(
            final List<ChargebackRow> chargebackRowList) {
        ChargebackPessoa chbackPess;

        for (ChargebackRow row : chargebackRowList) {
            List<ChargebackCell> cellList = row.getCellList();

            for (ChargebackCell cell : cellList) {
                chbackPess = cell.getChargebackPess();

                BigDecimal numUnidades = chbackPess.getNumeroUnidades();

                if (numUnidades == null
                        && BigDecimal.valueOf(0.0).compareTo(numUnidades) == 0) {
                    if (chbackPess.getCodigoChargebackPess() != null) {
                        this.removeChbackPess(chbackPess);
                    }
                } else {
                    chbackPess.setPessoa(pessoaService
                            .findPessoaById(chbackPess.getPessoa()
                                    .getCodigoPessoa()));
                    chbackPess.setTiRecurso(tiRecursoService
                            .findTiRecursoById(chbackPess.getTiRecurso()
                                    .getCodigoTiRecurso()));

                    if (chbackPess.getCodigoChargebackPess() != null) {
                        this.updateChbackPess(chbackPess, Boolean
                                .valueOf(false));
                    } else {
                        this.createChbackPess(chbackPess);
                    }
                }

            }
        }

        return Boolean.valueOf(true);
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
    public Boolean updateAllNumUnitsPess(final BigDecimal numUnits,
            final List<ChargebackRow> rowList) {

        for (ChargebackRow row : rowList) {
            if (row.getIsSelected()) {

                List<ChargebackCell> cellList = row.getCellList();
                for (ChargebackCell cell : cellList) {
                    if (!cell.getReadonly()) {
                        ChargebackPessoa chbackPess = cell.getChargebackPess();
                        // se id diferente de null, então deve ser atualizado ou
                        // removido
                        if (chbackPess.getCodigoChargebackPess() != null) {
                            ChargebackPessoa cb =
                                    this.findChbackPessById(chbackPess
                                            .getCodigoChargebackPess());
                            // verificação de segurança
                            if (cb != null) {
                                cb.setNumeroUnidades(numUnits);
                                // se numero unidades diferente de zero realiza
                                // o update
                                if (new BigDecimal(0.0).compareTo(cb
                                        .getNumeroUnidades()) != 0) {
                                    this.updateChbackPess(cb, Boolean
                                            .valueOf(false));
                                    // se numero unidades igual a zero realiza a
                                    // remoção
                                } else {
                                    this.removeChbackPess(cb);
                                }
                            }
                            // se id igual a null e numero de unidades diferente
                            // de zero insere.
                        } else {
                            chbackPess.setNumeroUnidades(numUnits);
                            if (new BigDecimal(0.0).compareTo(chbackPess
                                    .getNumeroUnidades()) != 0) {
                                chbackPess.setPessoa(pessoaService
                                        .findPessoaById(chbackPess.getPessoa()
                                                .getCodigoPessoa()));
                                chbackPess.setTiRecurso(tiRecursoService
                                        .findTiRecursoById(chbackPess
                                                .getTiRecurso()
                                                .getCodigoTiRecurso()));

                                this.createChbackPess(chbackPess);
                            }
                        }
                    }
                }
            }
        }

        return Boolean.valueOf(true);
    }

    /**
     * Cria a linha do Mapa.
     * 
     * @param chbackPess
     *            - entidade do tipo ChargebackPessoa
     * @param startDate
     *            - data inicio do peridod
     * @param endDate
     *            = data fim do periodo
     * 
     * @return a linha criada, entidade do tipo ChargebackRow.
     */
    public ChargebackRow createRow(final ChargebackPessoa chbackPess,
            final Date startDate, final Date endDate) {

        boolean isClosed;
        List<Date> dateList = DateUtil.getValidityDateList(startDate, endDate);

        ChargebackPessoa cbp;
        ChargebackCell cell;
        List<ChargebackCell> cellList = new ArrayList<ChargebackCell>();
        for (Date monthDate : dateList) {
            // verifica se o mes está fechado
            isClosed =
                    !DateUtil.after(monthDate, moduloService
                            .getClosingDateChargeBack());

            // realiza uma busca pela chave unica
            cbp =
                    chbackPessDao.findByUniqueKey(chbackPess.getTiRecurso(),
                            chbackPess.getPessoa(), monthDate);

            // se não existe alocação para este mês.
            if (cbp == null) {
                // cria a celula e a alocação do mapa
                cell = new ChargebackCell();
                cbp = new ChargebackPessoa();
                cbp.setPessoa(chbackPess.getPessoa());
                cbp.setTiRecurso(chbackPess.getTiRecurso());
                cbp.setNumeroUnidades(chbackPess.getNumeroUnidades());
                cbp.setDataMes(monthDate);

                // se mês fechado cria alocação com zero e não persiste
                if (isClosed) {
                    cbp.setNumeroUnidades(BigDecimal.valueOf(0.0));
                    cell.setReadonly(Boolean.TRUE);
                    // se mes Nâo esta fechado cria uma nova aloação.
                } else {
                    cbp.setNumeroUnidades(chbackPess.getNumeroUnidades());
                    this.createChbackPess(cbp);
                }

                cell.setChargebackPess(cbp);

                cellList.add(cell);
                // se existe e mes NÃO está fechado
                // soma o valor existente ao novo valor inserido
            } else if (!isClosed) {
                BigDecimal numeroUnidades = cbp.getNumeroUnidades();
                cbp.setNumeroUnidades(numeroUnidades.add(chbackPess
                        .getNumeroUnidades()));

                this.updateChbackPess(cbp, Boolean.valueOf(false));
            }
        }

        ChargebackRow row = new ChargebackRow();
        row.setPessoa(chbackPess.getPessoa());
        row.setTiRecurso(chbackPess.getTiRecurso());
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
                        ChargebackPessoa chbackPess = cell.getChargebackPess();

                        if (chbackPess.getCodigoChargebackPess() != null) {
                            ChargebackPessoa cbp =
                                    this.findChbackPessById(chbackPess
                                            .getCodigoChargebackPess());
                            // verificação de segurança
                            if (cbp != null) {
                                this.removeChbackPess(cbp);
                            }
                        }
                    }
                }
            }
        }

        return Boolean.valueOf(true);
    }

    /**
     * Executa a sincronização do ChargebackPessoa completa: apaga tudo e grava
     * novamente.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return true se a sincronização ocorrou corretamente. False caso
     *         contrário.
     * 
     * @throws IntegrityConstraintException
     *             - exceção caso ocorra algum erro na remoção
     */
    public Boolean syncChbackPessFull(final Date dataMes)
            throws IntegrityConstraintException {
        Boolean isProcessOk = this.removeChbackPess(dataMes, "");

        if (!isProcessOk) {
            throw new IntegrityConstraintException();
        }

        return isProcessOk;
    }

    /**
     * Executa a sincronização do ChargebackPessoa parcial: mantém os registros
     * do tipo Manual, apaga somente os do tipo Sincronizado e grava novamente.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return true se a sincronização ocorrou corretamente. False caso
     *         contrário.
     * 
     * @throws IntegrityConstraintException
     *             - exceção caso ocorra algum erro na remoção
     */
    public Boolean syncChbackPessPartial(final Date dataMes)
            throws IntegrityConstraintException {
        Boolean isProcessOk =
                this.removeChbackPess(dataMes, Constants.TYPE_SYNC);

        if (!isProcessOk) {
            throw new IntegrityConstraintException();
        }

        return isProcessOk;
    }

    /**
     * Verifica se existe ComposicaoTce pela Pessoa e dataMes.
     * 
     * @param pessoa
     *            - entidade Pessoa
     * @param tiRecurso
     *            - entidade TiRecurso
     * @param dataMes
     *            - data mês do registro
     * 
     * @return retorna true se existe, caso contrario false.
     */
    private Boolean existsChbackPess(final Pessoa pessoa,
            final TiRecurso tiRecurso, final Date dataMes) {
        ChargebackPessoa chbackPess =
                this.findChbackPessByUniqueKey(tiRecurso, pessoa, dataMes);

        if (chbackPess == null) {
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    /**
     * Validação do HistoryLock. Caso startDate (data de início da vigência)
     * seja maior do que a data History Lock, a vigência é válida e pode ser
     * adicionada ou removida. Caso contrário, a vigência não pode ser
     * adicionada nem removida.
     * 
     * @param startDate
     *            - data de início da vigência
     * @param showMsgError
     *            - mostra mensagem de erro caso startDate não seja válido
     * 
     * @return true se startDate for maior do que HistoryLock. false caso
     *         contrário
     */
    public Boolean verifyChbackClosingDate(final Date startDate,
            final Boolean showMsgError) {
        Date chbackClosingDate = moduloService.getClosingDateChargeBack();
        if (startDate.compareTo(chbackClosingDate) > 0) {
            return Boolean.valueOf(true);
        } else {
            if (showMsgError) {
                Messages.showError("verifyChbackClosingDate",
                        Constants.MSG_ERROR_MODULO_CHARGEBACK, DateUtil
                                .formatDate(chbackClosingDate,
                                        Constants.DEFAULT_DATE_PATTERN_SIMPLE,
                                        Constants.DEFAULT_CALENDAR_LOCALE));
            }

            return Boolean.valueOf(false);
        }
    }

    /**
     * @return the mailSender
     */
    public MailSenderUtil getMailSender() {
        return mailSender;
    }

    /**
     * @param mailSender
     *            the mailSender to set
     */
    public void setMailSender(MailSenderUtil mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean verifyChbackMonthApproved(Date startDate, Long codigoTiRecurso) {

        List<RateioLicencaSw> rateioLicencaSws = rateioLicencaSwService.findByRecursoTIAndMonth(startDate, codigoTiRecurso);
        if (null != rateioLicencaSws && !rateioLicencaSws.isEmpty()) {
            for (RateioLicencaSw rateio : rateioLicencaSws) {
                if (!rateio.getStatus().equalsIgnoreCase(Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ChargebackPessoa findByPessoaAndTiRecursoAndEndDate(ChargebackPessoa chbackPessoa) {
        return chbackPessDao.findByPessoaAndTiRecursoAndEndDateBefore(chbackPessoa.getPessoa(), chbackPessoa.getTiRecurso(), chbackPessoa.getDataMes());
    }
}