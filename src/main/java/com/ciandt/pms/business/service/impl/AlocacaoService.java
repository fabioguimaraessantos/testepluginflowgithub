package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoPeriodoService;
import com.ciandt.pms.business.service.IAlocacaoService;
import com.ciandt.pms.business.service.IEtiquetaAlocacaoService;
import com.ciandt.pms.business.service.IEtiquetaService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.EtiquetaAlocacao;
import com.ciandt.pms.model.EtiquetaAlocacaoId;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.model.vo.AlocacaoRow;
import com.ciandt.pms.persistence.dao.IAlocacaoDao;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * A classe AlocacaoService proporciona as funcionalidades de serviço para a
 * entidade alocação.
 * 
 * @since 14/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class AlocacaoService implements IAlocacaoService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IAlocacaoDao alocacaoDao;

    /** Instancia do servico EtiquetaAlocacao. */
    @Autowired
    private IEtiquetaAlocacaoService etiquetaAlocacaoService;

    /** Instancia do servico PerfilVendido. */
    @Autowired
    private IPerfilVendidoService perfilVendidoService;

    /** Instancia do servico AlocacaoPeriodo. */
    @Autowired
    private IAlocacaoPeriodoService alocacaoPeriodoService;

    /** Instancia do servico MapaAlocacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    /** Instancia do servico Etiqueta. */
    @Autowired
    private IEtiquetaService etiquetaService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createAlocacao(final Alocacao entity) {
        entity.setCodigoAlocacao(null);

        alocacaoDao.create(entity);
    }

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateAlocacao(final Alocacao entity) {
        alocacaoDao.update(entity);
    }

    /**
     * Remove a entidade passado por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    public void removeAlocacao(final Alocacao entity) {
        List<EtiquetaAlocacao> etiquetaAlocacaoList =
                entity.getEtiquetaAlocacaos();

        for (EtiquetaAlocacao etiquetaAlocacao : etiquetaAlocacaoList) {
            // remove EtiquetaAlocacao
            etiquetaAlocacaoService
                    .removeEtiquetaAlocacao(etiquetaAlocacaoService
                            .findEtiquetaAlocacaoById(etiquetaAlocacao.getId()));
        }

        alocacaoDao.remove(entity);
    }

    /**
     * Realiza o update a data inicio e fim.
     * 
     * @param alocacao
     *            - alocação a ser editada
     * @param startDate
     *            - data inicio
     * @param endDate
     *            - data fim
     * 
     * @return retorna true se inserido com sucesso caso contrario false.
     */
    public Boolean updateAlocacaoValidity(final Alocacao alocacao,
            final Date startDate, final Date endDate) {

        if (endDate.before(startDate)) {
            Messages.showError("updateAlocacaoValidity",
                    Constants.DEFAULT_MSG_ERROR_DATE_BEG_GT_DATE_END);

            return Boolean.FALSE;
        }
        alocacao.setDataInicio(startDate);
        alocacao.setDataFim(endDate);

        if (alocacao.getCodigoAlocacao() != null) {
            Alocacao aloc = this.findAlocacaoById(alocacao.getCodigoAlocacao());

            aloc.setDataInicio(startDate);
            aloc.setDataFim(endDate);

            updateAlocacao(aloc);
        }

        return Boolean.TRUE;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public Alocacao findAlocacaoById(final Long id) {
        return alocacaoDao.findById(id);
    }

    /**
     * Busca por alocações de uma mapa.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @return uma lista de Alocações do Mapa.
     */
    public List<Alocacao> findAlocacaoByMapaAlocacao(final MapaAlocacao mapa) {
        return alocacaoDao.findByMapaAlocacao(mapa);
    }

    /**
     * Metodo que realiza a cópia de Alocacao. Cada cópia o PerfilVendido
     * 
     * @param alocacaoRowList
     *            - lista de Alocacao a serem copiadas
     * @param perfilVendido
     *            - PerfilVendido das Alocacao clonadas
     * @param indicadorEstagioClone
     *            - indicadorEstagio das Alocacao clonadas
     * @param startMonthClone
     *            - mes da data inicial para fazer o clone
     * @param startYearClone
     *            - ano da data inicial para fazer o clone
     * @param validityMonthEnd
     *            - mes da data final da vigencia do MapaAlocacao
     * @param validityYearEnd
     *            - ano da data final da vigencia do MapaAlocacao
     * 
     * @return true se copiou normalmente false caso tenha ocorrido algum erro
     */
    public Boolean copyAlocacao(final List<AlocacaoRow> alocacaoRowList,
            final PerfilVendido perfilVendido,
            final String indicadorEstagioClone, final String startMonthClone,
            final String startYearClone, final String validityMonthEnd,
            final String validityYearEnd) {
        Alocacao alocacaoClone = null;
        Set<AlocacaoPeriodo> alocacaoPeriodoList =
                new HashSet<AlocacaoPeriodo>();

        // pega o startDate do periodo a ser clonado
        Date startDateClone = DateUtil.getDate(startMonthClone, startYearClone);

        String initMonthClone = startMonthClone;
        String initYearClone = startYearClone;

        // nao permite que o mes de intencao de copia seja closing date
        if (!mapaAlocacaoService.verifyClosingDateMapaAlocacao(startDateClone,
                false)) {
            // se o startDate escolhido for closing date atribui um mes valido
            startDateClone = mapaAlocacaoService.getDateAfterClosingDate();
            initMonthClone = DateUtil.getMonthString(startDateClone);
            initYearClone = DateUtil.getYearString(startDateClone);
        }

        // recupera a data do mes anterior
        Date previousStartDateClone = DateUtils.addMonths(startDateClone, -1);
        // cria uma lista (periodo) de datas entre o startDate selecionado e a
        // data final da vigencia do MapaAlocacao
        List<Date> validityDateList =
                DateUtil.getValidityDateList(initMonthClone, initYearClone,
                        validityMonthEnd, validityYearEnd);

        Boolean success = Boolean.valueOf(true);

        // itera a lista de AlocacaoRow
        for (AlocacaoRow alocacaoRow : alocacaoRowList) {
            // verifica qual linha está selecionada
            if (alocacaoRow.getIsSelected()) {
                // verifica se a Alocacao na matrix tem código, ou seja,
                // se já foi gravada no banco. Se não tem,
                // dá mensagem de erro para essa linha e continua na próxima
                Alocacao alocacao = alocacaoRow.getAlocacao();
                if (alocacao.getCodigoAlocacao() == null) {
                    Messages.showError("copyAlocacao",
                            Constants.MSG_ERROR_INVAL_ALLOCATION_COPY, alocacao
                                    .getRecurso().getCodigoMnemonico());

                    success = Boolean.valueOf(false);
                    continue;
                }

                // ----- cria a Alocacao clone -----
                alocacaoClone =
                        this.findAlocacaoById(alocacao.getCodigoAlocacao())
                                .getClone();

                // recupera a AlocacaoPeriodo do mes anterior ao mes selecionado
                // no startDate
                AlocacaoPeriodo alocPerPreviousMonth =
                        alocacaoPeriodoService
                                .findAlocacaoPeriodoByAlocacaoAndDate(
                                        alocacaoClone, previousStartDateClone);
                // atribui o percentual da AlocacaoPeriodo
                BigDecimal percAlocPerPrevMonth = BigDecimal.valueOf(0);
                if (alocPerPreviousMonth != null) {
                    percAlocPerPrevMonth =
                            alocPerPreviousMonth.getPercentualAlocacaoPeriodo();
                }
                // se nao existir AlocacaoPeriodo no mes anterior ou o
                // percentual alocado for zero, nao duplica a Alocacao e mostra
                // mensagem de erro
                if (percAlocPerPrevMonth == BigDecimal.valueOf(0)) {
                    Messages
                            .showError(
                                    "copyAlocacao",
                                    Constants.MSG_ERROR_INVAL_ALLOCATION_COPY_NO_PERCENTAGE,
                                    alocacao.getRecurso().getCodigoMnemonico());
                    success = Boolean.valueOf(false);
                    continue;
                }

                alocacaoClone.setPerfilVendido(perfilVendidoService
                        .findPerfilVendidoById(perfilVendido
                                .getCodigoPerfilVendido()));

                alocacaoClone.setIndicadorEstagio(indicadorEstagioClone);
                alocacaoClone.setValorUr(alocacao.getValorUr());
                // edita a Alocacao clone
                alocacaoClone.setCodigoAlocacao(null);
                alocacaoClone.setAlocacaoPeriodos(null);

                // itera a lista (periodo) de datas para criar as
                // AlocacaoPeriodo
                for (Date validityDate : validityDateList) {
                    AlocacaoPeriodo alocacaoPeriodo = new AlocacaoPeriodo();
                    alocacaoPeriodo.setAlocacao(alocacaoClone);
                    alocacaoPeriodo.setDataAlocacaoPeriodo(validityDate);
                    alocacaoPeriodo
                            .setPercentualAlocacaoPeriodo(percAlocPerPrevMonth);

                    alocacaoPeriodoList.add(alocacaoPeriodo);
                }
                alocacaoClone.setAlocacaoPeriodos(alocacaoPeriodoList);

                // guarda a lista de EtiquetaAlocacao para cada AlocacaoClone
                // criada
                List<EtiquetaAlocacao> etiquetaAlocacaoList =
                        alocacao.getEtiquetaAlocacaos();

                // atribui uma nova lista pq nesse momento as AlocacaoClone não
                // tem código
                alocacaoClone
                        .setEtiquetaAlocacaos(new ArrayList<EtiquetaAlocacao>());

                this.createAlocacao(alocacaoClone);

                // ----- fim criacao da Alocacao clone -----

                // ----- cria a lista de AlocacaoPeriodo clone -----
                // verifica se o percentual foi preenchido corretamente e se
                // é maior do que 0 (zero), pois se for 0, não será persistido
                if (percAlocPerPrevMonth.doubleValue() > 0) {
                    for (AlocacaoPeriodo alocacaoPeriodoClone : alocacaoPeriodoList) {
                        alocacaoPeriodoService
                                .createAlocacaoPeriodo(alocacaoPeriodoClone);
                    }
                }
                // ----- fim criacao da lista de AlocacaoPeriodo clone -----

                // ----- cria a Etiqueta clone -----
                EtiquetaAlocacao etiquetaAlocacaoClone = null;
                List<EtiquetaAlocacao> etiquetaAlocacaoCloneList =
                        new ArrayList<EtiquetaAlocacao>();
                for (EtiquetaAlocacao etiquetaAlocacao : etiquetaAlocacaoList) {
                    // realiza uma cópia de uma EtiquetaAlocacao
                    etiquetaAlocacaoClone = etiquetaAlocacao.getClone();
                    // seta os novos atributos
                    etiquetaAlocacaoClone.setAlocacao(alocacaoClone);
                    etiquetaAlocacaoClone.setEtiqueta(etiquetaService
                            .findEtiquetaById(
                                    etiquetaAlocacao.getEtiqueta()
                                            .getCodigoEtiqueta()).getClone());

                    etiquetaAlocacaoClone.setId(new EtiquetaAlocacaoId(
                            alocacaoClone.getCodigoAlocacao(),
                            etiquetaAlocacaoClone.getEtiqueta()
                                    .getCodigoEtiqueta()));

                    etiquetaAlocacaoService
                            .createEtiquetaAlocacao(etiquetaAlocacaoClone);
                    etiquetaAlocacaoCloneList.add(etiquetaAlocacaoClone);
                }

                // atribui a nova lista de EtiquetaAlocacao à Alocacao clone
                // corrente
                alocacaoClone.setEtiquetaAlocacaos(etiquetaAlocacaoCloneList);
                // ----- fim criacao da Etiqueta clone -----
            }
        }

        return success;
    }

    /**
     * Busca por alocações de uma mapa dentro de um periodo.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @return uma lista de Alocações do Mapa.
     */
    public List<Alocacao> findAlocacaoByMapaAlocacaoAndPeriod(
            final MapaAlocacao mapa) {

        return findAlocacaoByMapaAlocacaoAndPeriod(mapa, mapa
                .getDataInicioJanela(), mapaAlocacaoService
                .getMapaAlocacaoEndDateWindow(mapa));
    }

    /**
     * Busca por alocações de uma mapa dentro de um periodo.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * @param startDate
     *            - data inicio do perido
     * @param endDate
     *            - data fim do periodo
     * 
     * @return uma lista de Alocações do Mapa.
     */
    public List<Alocacao> findAlocacaoByMapaAlocacaoAndPeriod(
            final MapaAlocacao mapa, final Date startDate, final Date endDate) {

        return alocacaoDao
                .findByMapaAlocacaoAndPeriod(mapa, startDate, endDate);
    }

    /**
     * Busca a alocacao de uma mapa de acordo com o recurso e perfilvendido.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @param recurso
     *            - entidade do tipo Recurso.
     * 
     * @param perfil
     *            - entidade do tipo PerfilVendido.
     * 
     * 
     * @return uma Alocacao.
     */
    @Override
    public Alocacao findByMapaAlocacaoAndRecursoAndPerfil(
            final MapaAlocacao mapa, final Recurso recurso,
            final PerfilVendido perfil) {

        return alocacaoDao.findByMapaAlocacaoAndRecursoAndPerfil(mapa, recurso,
                perfil);
    }

    @Override
    public List<Alocacao> findVigentesByContratoPratica(Long contratoPraticaCode, Date closingMapDate) {
        return alocacaoDao.findVigentesByContratoPratica(contratoPraticaCode, closingMapDate);
    }


}