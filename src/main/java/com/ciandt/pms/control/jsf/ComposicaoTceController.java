package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IComposicaoTceService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IPessoaTipoContratoService;
import com.ciandt.pms.business.service.ITceLogSincronizacaoService;
import com.ciandt.pms.business.service.ITipoContratoService;
import com.ciandt.pms.business.service.IVwCompTceFuncionarioService;
import com.ciandt.pms.control.jsf.bean.ComposicaoTceBean;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.ComposicaoTce;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaTipoContrato;
import com.ciandt.pms.model.TceLogSincronizacao;
import com.ciandt.pms.model.TipoContrato;
import com.ciandt.pms.model.VwCompTceFuncionario;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;


/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 08/06/2011
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"PER.TCE:CR", "PER.TCE:VW", "PER.TCE:ED"})
public class ComposicaoTceController {

    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_COMPOSICAO_TCE_ADD = "composicaoTce_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_COMPOSICAO_TCE_EDIT = "composicaoTce_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_COMPOSICAO_TCE_DELETE = "composicaoTce_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_COMPOSICAO_TCE_SEARCH = "composicaoTce_search";

    /** outcome tela sincronização da entidade. */
    private static final String OUTCOME_COMPOSICAO_TCE_SYNC = "composicaoTce_sync";

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** Instancia do servico. */
    @Autowired
    private IComposicaoTceService compTceService;

    /** Instancia do servico Pessoa. */
    @Autowired
    private IPessoaService pessoaService;
    
    /** Instancia do servico TipoContrato. */
    @Autowired
    private ITipoContratoService tipoContratoService;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /** Instância do serviço de VwCompTceFuncionario. */
    @Autowired
    private IVwCompTceFuncionarioService vwCompTceService;

    /** Instância do serviço de TceLogSincronizacao. */
    @Autowired
    private ITceLogSincronizacaoService tceLogSincService;
    
    /** Instancia do servico PessoaTipoContrato. */
    @Autowired
    private IPessoaTipoContratoService pessTipoContService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private ComposicaoTceBean bean = null;

    /**
     * @return the bean
     */
    public ComposicaoTceBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final ComposicaoTceBean bean) {
        this.bean = bean;
    }

    /**
     * Reseta os valores das progressBars.
     */
    public void resetBar() {
        bean.resetBar();
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareSearch() {
        bean.reset();

        this.loadMoedaList(moedaService.findMoedaAll());

        return OUTCOME_COMPOSICAO_TCE_SEARCH;
    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareCreate() {
        bean.reset();
        bean.setIsUpdate(Boolean.valueOf(false));

        this.loadMoedaList(moedaService.findMoedaAll());

        return OUTCOME_COMPOSICAO_TCE_ADD;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void create() {
        ComposicaoTce to = bean.getTo();

        to.setPessoa(pessoaService.findPessoaByLogin(to.getPessoa()
                .getCodigoLogin()));
        Long codigoMoeda = bean.getMoedaMap().get(to.getMoeda().getNomeMoeda());
        if (codigoMoeda != null) {
            to.setMoeda(moedaService.findMoedaById(codigoMoeda));
        }
        to.setDataMes(DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg()));
        to.setIndicadorTipo(Constants.TYPE_MANUAL);
        Long codTipoContrato = this.getValidCodTipoContrato(to.getPessoa(), to
                .getDataMes());
        if (codTipoContrato != null) {
            to.setTipoContrato(tipoContratoService
                    .findTipoContratoById(codTipoContrato));
        }

        if (compTceService.createComposicaoTce(to)) {
            bean.resetTo();
            bean.resetDataMes();
            Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_COMPOSICAO_TCE);
        } else {
            Messages.showError("create",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_COMPOSICAO_TCE);
        }
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareUpdate() {
        findById(bean.getCurrentRowId());
        bean.setIsUpdate(Boolean.valueOf(true));

        // carrega o combo de Moeda
        this.loadMoedaList(moedaService.findMoedaAll());

        Date dataMes = bean.getTo().getDataMes();
        bean.setMonthBeg(DateUtil.getMonthString(dataMes));
        bean.setYearBeg(DateUtil.getYearString(dataMes));

        // guarda o código do login que está sendo atualizado para caso trocar,
        // deve validar na base se o novo login/mês já existe ou não
        bean.setCodigoLoginUpdate(bean.getTo().getPessoa().getCodigoLogin());

        return OUTCOME_COMPOSICAO_TCE_EDIT;
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        ComposicaoTce to = bean.getTo();
        String codigoLoginNew = to.getPessoa().getCodigoLogin();
        to.setPessoa(pessoaService.findPessoaByLogin(codigoLoginNew));
        Long codigoMoeda = bean.getMoedaMap().get(to.getMoeda().getNomeMoeda());
        if (codigoMoeda != null) {
            to.setMoeda(moedaService.findMoedaById(codigoMoeda));
        }
        to.setDataMes(DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg()));
        to.setIndicadorTipo(Constants.TYPE_MANUAL);
        Long codTipoContrato = this.getValidCodTipoContrato(to.getPessoa(), to
                .getDataMes());
        if (codTipoContrato != null) {
            to.setTipoContrato(tipoContratoService
                    .findTipoContratoById(codTipoContrato));
        }

        Boolean isDifferentCodigoLogin = !codigoLoginNew.equals(bean
                .getCodigoLoginUpdate());

        if (compTceService.updateComposicaoTce(to, isDifferentCodigoLogin)) {
            bean.resetTo();
            bean.resetDataMes();
            Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_COMPOSICAO_TCE);
            findByFilter();
            return OUTCOME_COMPOSICAO_TCE_SEARCH;
        } else {
            Messages.showError("update",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_COMPOSICAO_TCE);
            return null;
        }
    }

    /**
     * Prepara a tela de remocao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareRemove() {
        findById(bean.getCurrentRowId());
        return OUTCOME_COMPOSICAO_TCE_DELETE;
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {
        compTceService.removeComposicaoTce(compTceService.findCompTceById(bean
                .getTo().getCodigoComposicaoTce()));
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_COMPOSICAO_TCE);
        bean.resetTo();
        bean.resetDataMes();
        findByFilter();
        return OUTCOME_COMPOSICAO_TCE_SEARCH;
    }

    /**
     * Faz a cópia do registro.
     * 
     */
    public void copy() {
        ComposicaoTce compTce = compTceService.findCompTceById(bean
                .getCurrentRowId());

        if (compTceService.copyCompTce(compTce)) {
            Messages.showSucess("copy",
                    Constants.COMPOSICAO_TCE_MSG_SUCCESS_COPY);

            this.findByFilter();
        } else {
            Messages.showError("copy", Constants.COMPOSICAO_TCE_MSG_ERROR_COPY,
                    DateUtil.formatDate(compTce.getDataMes(),
                            Constants.DEFAULT_DATE_PATTERN_SIMPLE,
                            Constants.LOCALE_DEFAULT_BUNDLE));
        }
    }

    /**
     * Prepara a tela de sincronização do TCE.
     * 
     * @return pagina de destino
     */
    public String prepareSync() {
        bean.reset();

        return OUTCOME_COMPOSICAO_TCE_SYNC;
    }

    /**
     * Executa a sincronização do TCE completa: apaga tudo e grava novamente.
     */
    public void syncFull() {
        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        try {
            if (compTceService.syncCompTceFull(dataMes)) {
                if (this.syncCompTce(dataMes)) {
                    Messages.showSucess("syncFull",
                            Constants.COMPOSICAO_TCE_MSG_SUCCESS_SYNC_FULL);
                } else {
                    Messages.showWarning("syncFull",
                            Constants.COMPOSICAO_TCE_MSG_WARNG_SYNC);
                }
            }
        } catch (IntegrityConstraintException e) {
            Messages.showError("syncFull",
                    Constants.COMPOSICAO_TCE_MSG_ERROR_SYNC);
        }
    }

    /**
     * Executa a sincronização do TCE parcial: mantém os registros do tipo
     * Manual, apaga somente os do tipo Sincronizado e grava novamente.
     */
    public void syncPartial() {
        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        try {
            if (compTceService.syncCompTcePartial(dataMes)) {
                if (this.syncCompTce(dataMes)) {
                    Messages.showSucess("syncPartial",
                            Constants.COMPOSICAO_TCE_MSG_SUCCESS_SYNC_PARTIAL);
                } else {
                    Messages.showWarning("syncPartial",
                            Constants.COMPOSICAO_TCE_MSG_WARNG_SYNC);
                }
            }
        } catch (IntegrityConstraintException e) {
            Messages.showError("syncPartial",
                    Constants.COMPOSICAO_TCE_MSG_ERROR_SYNC);
        }
    }

    /**
     * Executa a sincronização do TCE.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return true se a sincronização ocorrou corretamente. False caso
     *         contrário.
     */
    private Boolean syncCompTce(final Date dataMes) {
        // variáveis do log
        final String titleLine = "********** ";
        final String breakLine = "\n";
        final String logError = " ERROR ";
        StringBuffer textoLog = new StringBuffer();
        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.LABEL_TCE_SYNCHRONIZATION)
                + breakLine);

        // flag indicador processo realizado com sucesso ou não.
        Boolean isSyncOk = Boolean.valueOf(true);

        // itera a lista de Pessoas e verifica a lista de registros TCE da view
        // para depois gravar na ComposicaoTce. Caso ocorra erros/duplicação,
        // guarda no textoLog e depois grava na TceLogSincronizacao

        // busca a lista de registros da view
        List<VwCompTceFuncionario> vwCompTceFuncList = vwCompTceService
                .findAllByDate(dataMes);

        // guarda em um map e já guarda em uma variável o log dos registros
        // duplicados
        Map<Long, VwCompTceFuncionario> vwCompTceFuncMap = new HashMap<Long, VwCompTceFuncionario>();
        for (VwCompTceFuncionario vwCompTceFunc : vwCompTceFuncList) {
            Long codigoPessoa = vwCompTceFunc.getId().getCodigoPessoa();

            if (!vwCompTceFuncMap.containsKey(codigoPessoa)) {
                vwCompTceFuncMap.put(codigoPessoa, vwCompTceFunc);
            } else {
                isSyncOk = Boolean.valueOf(false);
                String codigoLogin = vwCompTceFunc.getId().getCodigoLogin();
                Messages.showError("syncCompTce",
                        Constants.COMPOSICAO_TCE_MSG_LOGIN_ERROR_SYNC,
                        codigoLogin);

                textoLog
                        .append(DateUtil.formatDate(new Date(),
                                Constants.DEFAULT_DATE_PATTERN_FULL,
                                Constants.DEFAULT_CALENDAR_LOCALE)
                                + logError
                                + (BundleUtil
                                        .getBundle(Constants.COMPOSICAO_TCE_MSG_LOGIN_ERROR_SYNC))
                                        .replace("{0}", codigoLogin)
                                + breakLine);
            }
        }

        // busca a lista de registros que estão na ComposicaoTce, para
        // considerar os registros do tipo Manual que não são removidos na
        // sincronização do TCE (quando a escolha é "No")
        ComposicaoTce filter = bean.getFilter();
        filter.setDataMes(dataMes);
        filter.setIndicadorTipo(Constants.TYPE_MANUAL);
        List<ComposicaoTce> compTceMNList = compTceService
                .findCompTceByFilter(filter);

        // guarda em um map
        Map<Long, ComposicaoTce> compTceMNMap = new HashMap<Long, ComposicaoTce>();
        for (ComposicaoTce compTceMN : compTceMNList) {
            compTceMNMap.put(compTceMN.getPessoa().getCodigoPessoa(), compTceMN);
        }

        // busca a lista de todas as pessoas ativas NA ÉPOCA do dataMes
        List<Pessoa> pessoaList = pessoaService
                .findPessoaAllActiveByDate(dataMes);

        // variáveis auxiliares
        double cont = 0;
        double totalPessoas = pessoaList.size();
        Boolean isPersistEnable = null;
        ComposicaoTce compTce = null;

        // itera a lista de Pessoas. Caso o código tenha sido retornado na lista
        // da view, utiliza estes valores. Caso contrário, cria um novo registro
        // para aquela Pessoa com os valores nulos. A idéia é ter como base a
        // lista de Pessoas para identificar qual recurso não está sendo
        // retornado na view do TCE, afinal a lista de Pessoas do PMS é
        // teoricamente completa. É também considerado o map gerado a partir da
        // ComposicaoTce pois se existem e estão sendo considerados os registros
        // tipo Manual, estes não podem ser inseridos novamente. Portanto na
        // iteração devem ser desconsiderados.
        for (Pessoa pessoa : pessoaList) {
            isPersistEnable = Boolean.valueOf(true);
            compTce = new ComposicaoTce();

            VwCompTceFuncionario vwCompTceFunc = vwCompTceFuncMap.get(pessoa
                    .getCodigoPessoa());
            if (vwCompTceFunc != null) {
                compTce.setPessoa(pessoaService.findPessoaById(vwCompTceFunc
                        .getId().getCodigoPessoa()));
                compTce.setDataMes(vwCompTceFunc.getId().getDataMes());
                compTce.setTipoContrato(tipoContratoService
                        .findTipoContratoById(vwCompTceFunc
                                .getCodigoTipoContrato()));
                compTce.setMoeda(moedaService.findMoedaByAcronym(vwCompTceFunc
                        .getSiglaMoeda()));
                compTce.setValorSalario(vwCompTceFunc.getValorSalario());
                compTce.setValorBeneficios(vwCompTceFunc.getValorBeneficios());
                compTce.setNumeroHorasJornada(vwCompTceFunc
                        .getNumeroHorasJornada());
                // desconsidera os registros da ComposicaoTce do tipo Manual
            } else if (compTceMNMap.get(pessoa.getCodigoPessoa()) != null) {
                isPersistEnable = Boolean.valueOf(false);
                // caso não encontre na view e nem na ComposicaoTce, grava com
                // os valores nulos
            } else {
                compTce.setPessoa(pessoa);
                compTce.setDataMes(dataMes);
                Long codigoTipoContrato = this.getValidCodTipoContrato(pessoa,
                        dataMes);
                if (codigoTipoContrato != null) {
                    compTce.setTipoContrato(tipoContratoService
                            .findTipoContratoById(codigoTipoContrato));
                }
                Long defaultCodigoMoeda = Long.valueOf(systemProperties
                        .getProperty(Constants.DEFAULT_PROPERTY_CURRENCY_CODE));
                compTce
                        .setMoeda(moedaService
                                .findMoedaById(defaultCodigoMoeda));
                compTce.setValorSalario(null);
                compTce.setValorBeneficios(null);
                compTce.setNumeroHorasJornada(null);
            }

            // será false somente quando existir o registro na ComposicaoTce com
            // o tipo Manual. Nesse caso não grava no banco e desconsidera o
            // registro dessa Pessoa
            if (isPersistEnable) {
                compTce.setIndicadorTipo(Constants.TYPE_SYNC);

                compTceService.createComposicaoTce(compTce);
            }

            // faz o cálculo da porcentagem do progresso do processo para
            // mostrar na tela
            cont++;
            bean.setProgressPercent((cont / totalPessoas) * 100);
        }

        // grava o log
        TceLogSincronizacao tceLogSinc = new TceLogSincronizacao();
        tceLogSinc.setDataMes(dataMes);
        tceLogSinc.setDataExecucao(new Date());
        tceLogSinc.setCodigoAutor(LoginUtil.getLoggedUsername());
        if (!isSyncOk) {
            tceLogSinc.setTextoLog(textoLog.toString());
        } else {
            textoLog.append("SUCCESS");
            tceLogSinc.setTextoLog(textoLog.toString());
        }
        tceLogSincService.createTceLogSinc(tceLogSinc);

        bean.setIsProgressFinished(Boolean.valueOf(true));

        return isSyncOk;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        bean.setTo(compTceService.findCompTceById(id));
        if (bean.getTo() == null
                || bean.getTo().getCodigoComposicaoTce() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        ComposicaoTce filter = bean.getFilter();
        filter.setDataMes(DateUtil.getDate(bean.getMonthBegFilter(), bean
                .getYearBegFilter()));

        if (bean.getIsMissingBlankValues()) {
            bean.setResultList(compTceService
                    .findCompTceByFilterMissBlank(filter));
        } else {
            bean.setResultList(compTceService.findCompTceByFilter(filter));
        }

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    /**
     * Ao finalizar a sincronização, clica em Acknowledge e redireciona para
     * tela de busca.
     * 
     * @return página de destino
     * 
     */
    public String acknowledge() {
        bean.resetFilter();
        ComposicaoTce filter = bean.getFilter();
        filter.setDataMes(DateUtil.getDate(bean.getMonthBeg(), bean
                .getYearBeg()));
        filter.setIndicadorTipo(Constants.ALL);
        bean.setIsMissingBlankValues(Boolean.valueOf(true));

        bean.setResultList(compTceService.findCompTceByFilterMissBlank(filter));

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("acknowledge",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);

        // atualiza os combos dos filtros da tela
        bean.setMonthBegFilter(bean.getMonthBeg());
        bean.setYearBegFilter(bean.getYearBeg());

        return OUTCOME_COMPOSICAO_TCE_SEARCH;
    }

    /**
     * Realiza a validaçao do campo Login.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validatePessoa(final FacesContext context,
            final UIComponent component, final Object value) {

        String login = (String) value;

        if ((login != null) && (!"".equals(login))) {
            Pessoa pessoa = pessoaService.findPessoaByLogin(login);

            if (pessoa == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }

    }

    /**
     * Ação utilizada no autocomplete da Pessoa. Retorna uma lista de Pessoas.
     * 
     * @param value
     *            - valor (login) utilizado na busca das Pessoas
     * 
     * @return retorna uma lista de recurso
     */
    public List<Pessoa> autoCompletePessoa(final Object value) {
        return pessoaService.findPessoaByLikeLogin((String) value);
    }

    /**
     * Popula a lista de moedas para combobox.
     * 
     * @param moedas
     *            lista de moedas.
     * 
     */
    private void loadMoedaList(final List<Moeda> moedas) {

        Map<String, Long> moedaMap = new HashMap<String, Long>();
        List<String> moedaList = new ArrayList<String>();

        for (Moeda moeda : moedas) {
            moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            moedaList.add(moeda.getNomeMoeda());
        }

        bean.setMoedaMap(moedaMap);
        bean.setMoedaList(moedaList);
    }
    
    /**
     * Recupera o codigo TipoContrato válido da Pessoa e Mês corrente.
     * 
     * @param pessoa
     *            - Pessoa corrente
     * @param dataMes
     *            - dataMes corrente
     * @return Long com o codigo do TipoContrato valido
     */
    private Long getValidCodTipoContrato(final Pessoa pessoa, final Date dataMes) {
        Long codigoTipoContrato = null;
        PessoaTipoContrato pessTC = pessTipoContService
                .findPessTCByPessoaAndDate(pessoa, dataMes);
        if (pessTC != null) {
            TipoContrato tc = pessTC.getTipoContrato();
            if (tc != null) {
                codigoTipoContrato = tc.getCodigoTipoContrato();
            }
        }
        if (codigoTipoContrato == null) {
            TipoContrato tc = pessoa.getTipoContrato();
            if (tc != null) {
                codigoTipoContrato = tc.getCodigoTipoContrato();
            }
        }

        return codigoTipoContrato;
    }

}