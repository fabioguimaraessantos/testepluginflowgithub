package com.ciandt.pms.business.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IContratoPraticaCentroLucroService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.ICotacaoMoedaService;
import com.ciandt.pms.business.service.ICustoInfraBaseService;
import com.ciandt.pms.business.service.IDreLogFechamentoService;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.business.service.IPapelAlocacaoService;
import com.ciandt.pms.business.service.IReceitaMoedaService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.CustoInfraBase;
import com.ciandt.pms.model.DreLogFechamento;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.persistence.dao.IDreLogFechamentoDao;
import com.ciandt.pms.persistence.dao.IFaturaDao;
import com.ciandt.pms.util.LoginUtil;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 07/06/2010
 */
@Service
public class DreLogFechamentoService implements IDreLogFechamentoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IDreLogFechamentoDao dreLogFechamentoDao;

    /** Instancia do Servico da entidade Fatura. */
    @Autowired
    private IModuloService moduloService;

    /** Instancia do Servico da entidade Receita. */
    @Autowired
    private IReceitaService receitaService;

    /** Instancia do Servico da entidade Fatura. */
    @Autowired
    private IFaturaService faturaService;

    /** Instancia do Servico da entidade PapelAlocacao. */
    @Autowired
    private IPapelAlocacaoService papelAlocacaoService;

    /** Instancia do Servico da entidade CidadeBase. */
    @Autowired
    private ICidadeBaseService cidadeBaseService;

    /** Instancia do Servico da entidade CustoInfraBase. */
    @Autowired
    private ICustoInfraBaseService custoInfraBaseService;

    /** Instancia do Servico da entidade ContratoPratica. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /** Instancia do Servico da entidade ContratoPraticaCentroLucro. */
    @Autowired
    private IContratoPraticaCentroLucroService cpclService;

    /** Instancia do Servico da entidade NaturezaCentroLucro. */
    @Autowired
    private INaturezaCentroLucroService naturezaService;

    /** Instancia do Servico da entidade MapaAlocacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    /** Instancia do Servico da entidade Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /** Instancia do Servico da entidade CotacaoMoeda. */
    @Autowired
    private ICotacaoMoedaService cotacaoMoedaService;
    
    /** Instancia do Servico da entidade ReceitaMoeda. */
    @Autowired
    private IReceitaMoedaService receitaMoedaService;

    /** Instancia do Dao da entidade Fatura. */
    @Autowired
    private IFaturaDao faturaDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createDreLogFechamento(final DreLogFechamento entity) {
        dreLogFechamentoDao.create(entity);
    }

    /**
     * Altera uma entidade.
     * 
     * @param entity
     *            entidade a ser alterada.
     */
    public void updateDreLogFechamento(final DreLogFechamento entity) {
        dreLogFechamentoDao.update(entity);
    }

    /**
     * Valida o fechamento da DRE.
     * 
     * @param dataMes
     *            - mes que será validado o fechamento da DRE.
     * @return true se validacao com sucesso, caso contrario retorna false
     */
    public DreLogFechamento validateDreLogFechamento(final Date dataMes) {
        final String titleLine = "********** ";
        final String breakLine = "\n";
        final String logError = " ERROR ";
        final String logWarn = " WARN ";
        final String msgLogNotClosed = " is not closed.";
        final String msgLogNotPublished = " is not published.";
        final String msgLogInvoiceId = "Invoice ID #";
        final String msgLogNotSubmited = " is not submited.";
        final String msgLogNotTceAssociated =
                " does not have TCE value associated.";
        final String msgLogNotCustoInfBaseAssociated =
                " does not have Site Costs associated.";
        final String msgLogNotNCLFilled =
                " has Profit Center Type Mandatory to be filled.";
        final String msgLogMissing = " is missing.";
        final String msgLogNotValue =
                " has no value to the last day of the month.";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuffer textoLog = new StringBuffer();
        Boolean processSuccess = Boolean.valueOf(true);

        // recupera todas as listas para compor o log e fazer as validacoes
        // ---------------------------- logs
        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.ENTITY_NAME_MODULO)
                + breakLine);
        List<Modulo> moduloList = moduloService.findModuloaAll();
        for (Modulo modulo : moduloList) {
            if (dataMes.compareTo(modulo.getDataFechamento()) > 0) {
                textoLog.append(sdf.format(new Date()) + logError
                        + modulo.getNomeModulo() + msgLogNotClosed + breakLine);
                processSuccess = Boolean.valueOf(false);
            }
        }

        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.ENTITY_NAME_RECEITA)
                + breakLine);
        List<Receita> receitaList =
                receitaService.findReceitaAllNotClosed(dataMes);
        if (receitaList.size() > 0) {
            processSuccess = Boolean.valueOf(false);
        }
        for (Receita receita : receitaList) {
            textoLog.append(sdf.format(new Date()) + logError
                    + receita.getContratoPratica().getNomeContratoPratica()
                    + msgLogNotPublished + breakLine);
        }

        List<MapaAlocacao> mapaList =
                mapaAlocacaoService.findMapaAlocacaoByFilterWithoutRevenue(
                        null, null, null, null, dataMes, null);
        if (mapaList.size() > 0) {
            processSuccess = Boolean.valueOf(false);
        }
        for (MapaAlocacao mapaAlocacao : mapaList) {
            ContratoPratica contratoPratica = mapaAlocacao.getContratoPratica();
            textoLog.append(sdf.format(new Date()) + logWarn
                    + contratoPratica.getNomeContratoPratica() + msgLogMissing
                    + breakLine);
        }

        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.ENTITY_NAME_FATURA)
                + breakLine);
        List<Fatura> faturaList = faturaService.findFaturaAllNotClosed(dataMes);
        if (faturaList.size() > 0) {
            processSuccess = Boolean.valueOf(false);
        }
        for (Fatura fatura : faturaList) {
            textoLog.append(sdf.format(new Date()) + logError + msgLogInvoiceId
                    + fatura.getCodigoFatura() + msgLogNotSubmited + breakLine);
        }

        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.ENTITY_NAME_PAPEL_ALOCACAO)
                + breakLine);
        List<PapelAlocacao> papelAlocList =
                papelAlocacaoService.findPapelAlocAllNotTceAssociated(dataMes);
        if (papelAlocList.size() > 0) {
            processSuccess = Boolean.valueOf(false);
        }
        for (PapelAlocacao papelAlocacao : papelAlocList) {
            textoLog.append(sdf.format(new Date()) + logError
                    + papelAlocacao.getNomePapelAlocacao()
                    + msgLogNotTceAssociated + breakLine);
        }

        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.ENTITY_NAME_CUSTO_INFRA_BASE)
                + breakLine);
        List<CidadeBase> cidadeBaseList = cidadeBaseService.findCidadeBaseAllActive();
        for (CidadeBase cidadeBase : cidadeBaseList) {
            CustoInfraBase custoInfraBase =
                    custoInfraBaseService.findCustoInfBaseByDateAndCidadeBase(
                            dataMes, cidadeBase);
            if (custoInfraBase == null) {
                textoLog.append(sdf.format(new Date()) + logError
                        + cidadeBase.getNomeCidadeBase()
                        + msgLogNotCustoInfBaseAssociated + breakLine);
                processSuccess = Boolean.valueOf(false);
            }
        }

        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.ENTITY_NAME_CONTRATO_PRATICA)
                + breakLine);
        List<ContratoPratica> contratoPraticaList =
                contratoPraticaService.findContPratAllWithMapaAlocacao();
        NaturezaCentroLucro naturezaFilter = new NaturezaCentroLucro();
        naturezaFilter.setIndicadorAtivo(Constants.ACTIVE);
        naturezaFilter.setIndicadorTipo(Constants.NATUREZA_TYPE_MANDATORY);
        List<NaturezaCentroLucro> naturezaMandatoryList =
                naturezaService.findNaturezaCentroLucroByFilter(naturezaFilter);
        for (ContratoPratica contratoPratica : contratoPraticaList) {
            for (NaturezaCentroLucro natureza : naturezaMandatoryList) {
                ContratoPraticaCentroLucro cpcl =
                        cpclService.findCPCLByContPratAndNatAndDate(
                                contratoPratica, natureza, dataMes);
                if (cpcl == null) {
                    textoLog.append(sdf.format(new Date()) + logError
                            + contratoPratica.getNomeContratoPratica()
                            + msgLogNotNCLFilled + breakLine);
                    processSuccess = Boolean.valueOf(false);
                    break;
                }
            }
        }

        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.ENTITY_NAME_COTACAO_MOEDA)
                + breakLine);
        List<Moeda> moedaList = moedaService.findMoedaAll();
        for (Moeda moeda : moedaList) {
            if (moeda.getSiglaMoeda().equals(
                    Constants.PROPERTY_CURRENCY_BRL_CODE)) {
                CotacaoMoeda cotacaoMoeda =
                        cotacaoMoedaService.findCotMoedaByMoedaAndLastDayMonth(
                                moeda, dataMes);
                if (cotacaoMoeda == null) {
                    textoLog
                            .append(sdf.format(new Date()) + logError
                                    + moeda.getNomeMoeda() + msgLogNotValue
                                    + breakLine);
                    processSuccess = Boolean.valueOf(false);
                }
            }
        }
        // ---------------------------- END logs
        // fim das validacoes

        // cria o objeto DreLogFechamento (log do fechamento)
        DreLogFechamento dreLogFechamento = new DreLogFechamento();
        dreLogFechamento.setDataMes(dataMes);
        dreLogFechamento.setDataExecucao(new Date());
        dreLogFechamento.setTextoLog(textoLog.toString());
        dreLogFechamento.setCodigoAutor(LoginUtil.getLoggedUsername());

        // se o processo for sucesso, grava o log de sucesso e chama a procedure
        // para consolidar a DRE
        if (processSuccess) {
            dreLogFechamento.setIndicadorSucesso(Constants.SUCCESS);
            this.createDreLogFechamento(dreLogFechamento);
            // senao grava o log de erro e volta para exibir a tela com o log
        } else {
            dreLogFechamento.setIndicadorSucesso(Constants.ERROR);
            this.createDreLogFechamento(dreLogFechamento);
        }

        // se o processo NAO for sucesso, ou seja, for erro, retorna o objeto do
        // log para exibir na tela
        if (!processSuccess) {
            return dreLogFechamento;
            // caso contrario, se for sucesso, retorna null e dá mensagem de
            // sucesso
        } else {
            return null;
        }
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<DreLogFechamento> findDreLogFechamentoAll() {
        return dreLogFechamentoDao.findAll();
    }

    /**
     * Busca a entidade filtrando pelos parametros.
     * 
     * @param dataMes
     *            do DreLogFechamento.
     * 
     * @return lista de entidades que atendem ao criterio da CidadeBase e
     *         dataMes.
     */
    public DreLogFechamento findDreLogFechByDataMes(final Date dataMes) {
        return dreLogFechamentoDao.findByDataMes(dataMes);
    }

    /**
     * Realiza a consolidação da DRE.
     * 
     * @param dataMes
     *            mes do fechamento.
     * 
     * @return retorna o status da execução da consolidacao. Se retorno igual a
     *         zero erro, caso contrario sucesso.
     */
    @Deprecated
    public Boolean consolidateDre(final Date dataMes) {
        // pega todas as receitas do mês
        List<Receita> receitaList =
                receitaService.findReceitaByMonthDate(dataMes);
        // atualiza as cotações das receita
        for (Receita receita : receitaList) {
            List<ReceitaMoeda> receitaMoedaList = receita.getReceitaMoedas();

            for (ReceitaMoeda receitaMoeda : receitaMoedaList) {
                receitaMoeda.setCotacaoMoeda(cotacaoMoedaService
						.findCotacaoMoedaByMonth(receitaMoeda.getMoeda(),
								receita.getDataMes()));

                receitaMoedaService.updateReceitaMoeda(receitaMoeda);
            }
        }

        // pega todas as cotações do mês
        List<Fatura> faturaList = faturaService.findFaturaByMonthDate(dataMes);
        // atualiza as cotações das faturas
        for (Fatura fatura : faturaList) {
            faturaService.setCotacaoMoeda(fatura);

            faturaDao.update(fatura);
        }

        // sucesso na integração
        return Boolean.valueOf(true);
    }

}