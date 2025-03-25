package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.TiRecursoBean;
import com.ciandt.pms.control.jsf.components.impl.CompanySelect;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.*;
import com.ciandt.pms.util.DateUtil;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.event.ValueChangeEvent;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * A classe TiRecursoController proporciona as funcionalidades da
 * camada de apresenta��o referente a entidade TiRecurso.
 *
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ICB.IT_RESOURCE:VW", "ICB.IT_RESOURCE:CR" })
public class TiRecursoController {

    /*********** OUTCOMES **************************/

    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_ADD = "tiRecurso_add";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_RESEARCH = "tiRecurso_research";

    /** outcome tela manage da entidade. */
    private static final String OUTCOME_EDIT = "tiRecurso_edit";

    /** outcome tela view da entidade. */
    private static final String OUTCOME_MANAGE_VIEW = "tiRecurso_view";

    /*********** SERVICES **************************/

    /** Instancia do servico - TiRecurso. */
    @Autowired
    private ITiRecursoService tiRecursoService;

    /** Instancia do servico - CustoTiRecursoService. */
    @Autowired
    private ICustoTiRecursoService custoTiRecursoService;

    /** Instancia do servi�o da entidade Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /** Instancia do servi�o da entidade Modulo. */
    @Autowired
    private IModuloService moduloService;

    /** Instancia do serviço da entidade Empresa. */
    @Autowired
    private IEmpresaService empresaService;

    /** Instancia do serviço da entidade RateioLicencaSw. */
    @Autowired
    private IRateioLicencaSwService rateioService;

    /*********** BEANS **************************/

    /** Instancia do bean. */
    @Autowired
    private TiRecursoBean bean;

    /********************************************/

    /**
     * @return the bean
     */
    public TiRecursoBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final TiRecursoBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     *
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();

        this.loadMoedaList(moedaService.findMoedaAll());

        prepareCompanyList();
        bean.setTipoAlocacaoList(getTipoAlocacaoList(null, Boolean.TRUE));
        bean.getFilter().setIndicadorTipoAlocacao(Constants.ALL);
        return OUTCOME_RESEARCH;
    }

    /**
     * Prepara a tela de insercao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareCreate() {
        // limpa os dados do formul�rio
        bean.reset();
        // marca como modo de inser��o
        bean.setIsUpdate(Boolean.FALSE);

        // carrega o combo de Moeda
        this.loadMoedaList(moedaService.findMoedaAll());

        prepareCompanyList();
        bean.setTipoAlocacaoList(getTipoAlocacaoList(null, Boolean.FALSE));
        return OUTCOME_ADD;
    }

    /**
     * Insere uma entidade.
     *
     * @return retorna a pagina de inser��o.
     */
    public String create() {
        String month = bean.getMonthBeg();
        String year = bean.getYearBeg();
        Date startDate = DateUtil.getDate(month, year);

        // verifica o History Lock. Se startDate n�o for v�lido, d� mensagem de
        // erro
        if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
            return null;
        }

        // popula o TiRecurso
        TiRecurso tiRecurso = bean.getTo();
        tiRecurso.getEmpresa().setCodigoEmpresa(bean.getCompanySelect().value());

        // busca pela Moeda
        Long codigoMoeda = bean.getMoedaMap().get(
                tiRecurso.getMoeda().getNomeMoeda());
        if (codigoMoeda != null) {
            Moeda moeda = moedaService.findMoedaById(codigoMoeda);
            tiRecurso.setMoeda(moeda);
        } else {
            tiRecurso.setMoeda(null);
        }

        // codigoMnemonico upperCase
        String codigoMnemonico = tiRecurso.getCodigoMnemonico();
        if (!Strings.isNullOrEmpty(codigoMnemonico)) {
            tiRecurso.setcodigoMnemonico(codigoMnemonico
                    .toUpperCase());
        }

        // popula o CustoTiRecurso
        CustoTiRecurso custoTiRecurso = bean.getCustoTiRecurso();
        custoTiRecurso.setDataInicio(startDate);

        custoTiRecurso.setTiRecurso(tiRecurso);
        tiRecurso.setNomeTiRecurso(this.removeWhiteSpaceFromTheEnd(tiRecurso.getNomeTiRecurso()));

        tiRecursoService.createTiRecurso(tiRecurso, custoTiRecurso);

        // mensagem de sucesso
        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                Constants.ENTITY_NAME_TI_RECURSO);

        return this.prepareCreate();
    }

    /**
     * Prepara a tela de edicao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareUpdate() {
        // pega a entidade PapelAlocacao
        TiRecurso tiRecurso = bean.getTo();

        // limpa os dados do formul�rio do TcePapelAlocacao
        bean.reset();

        // marca como modo de alter��o
        bean.setIsUpdate(Boolean.TRUE);

        // se existir o PapelAlocacao redireciona para a
        // tela de gerenciamento
        if (tiRecurso != null) {
            bean.setTo(tiRecursoService.findTiRecursoById(
                    tiRecurso.getCodigoTiRecurso()));

            // carrega o combo de Moeda
            this.loadMoedaList(moedaService.findMoedaAll());

            // guarda a data do HistoryLock
            bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());

            prepareCompanyList();
            Long companyCode = tiRecurso.getEmpresa() == null || tiRecurso.getEmpresa().getCodigoEmpresa() == null ? 1l : tiRecurso.getEmpresa().getCodigoEmpresa();
            bean.getCompanySelect().select(companyCode);
            bean.setTipoAlocacaoList(getTipoAlocacaoList(companyCode, Boolean.FALSE));

            return OUTCOME_EDIT;
            // se nao existir na base de dados
        } else {
            Messages.showError("prepareManage",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_TI_RECURSO);

            // retorna null caso n�o seja validado
            return null;
        }
    }

    /**
     * Executa um update da entidade.
     *
     * @return pagina de destino
     *
     */
    public String update() {
        TiRecurso tiRecurso = bean.getTo();
        tiRecurso.setEmpresa(getCompany());

        // busca pela Moeda
        Long codigoMoeda = bean.getMoedaMap().get(
                tiRecurso.getMoeda().getNomeMoeda());
        Moeda moeda = null;
        if (codigoMoeda != null) {
            moeda = moedaService.findMoedaById(codigoMoeda);
        }

        // se existir a Moeda atualiza o TiRecurso
        if (moeda != null) {
            tiRecurso.setMoeda(moeda);

            // codigoMnemonico upperCase
            String codigoMnemonico = tiRecurso.getCodigoMnemonico();
            if (!Strings.isNullOrEmpty(codigoMnemonico)) {
                tiRecurso.setcodigoMnemonico(codigoMnemonico
                        .toUpperCase());
            }

            tiRecurso.setNomeTiRecurso(this.removeWhiteSpaceFromTheEnd(tiRecurso.getNomeTiRecurso()));
            tiRecursoService.updateTiRecurso(tiRecurso);

            Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_TI_RECURSO);

            bean.setCurrentRowId(tiRecurso.getCodigoTiRecurso());

            return prepareUpdate();
        } else {
            Messages.showError("update",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_MOEDA);

            return null;
        }
    }

    /**
     * Prepara a tela de remocao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareRemove() {
        // marca o modo de remo��o como true
        //para que a tela n�o mostre o bot�o de excluir
        bean.setIsRemove(Boolean.TRUE);

        // carrega o TiRecurso em modo de visualiza��o
        if (loadTiRecursoView()) {
            return OUTCOME_MANAGE_VIEW;
        }

        return null;
    }

    /**
     * Deleta uma entidade.
     *
     * @return pagina de destino
     *
     */
    public String remove() {
        try {
            tiRecursoService.removeTiRecurso(
                    tiRecursoService.findTiRecursoById(
                            bean.getTo().getCodigoTiRecurso()));

        } catch (DataIntegrityViolationException e) {
            Messages.showError("remove",
                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_TI_RECURSO);
            return null;
        }

        // mensagem de sucesso
        Messages.showSucess("remove",
                Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_TI_RECURSO);

        bean.resetTo();
        findByFilter();

        return OUTCOME_RESEARCH;
    }

    /**
     * Prepara a tela de visualiza��o da entidade.
     *
     * @return retorna a pagina de visualiza��o do PapelAlocacao
     */
    public String prepareView() {
        // marca o modo de remo��o como false (view) para que a tela n�o mostre
        // o bot�o de excluir
        bean.setIsRemove(Boolean.FALSE);

        // carrega o PapelAlocacao em modo de visualiza��o
        if (loadTiRecursoView()) {
            return OUTCOME_MANAGE_VIEW;
        }

        return null;
    }

    /**
     * Carrega os conteudo de um PapelAlocacao para exibir na tela.
     *
     * @return retorna true se encontrou o PapelAlocacao corretamente
     */
    private Boolean loadTiRecursoView() {
        // pega a entidade TiRecurso
        TiRecurso tiRecurso = bean.getTo();

        // se existir o PapelAlocacao atribui a Moeda
        if (tiRecurso.getCodigoTiRecurso() != null) {
            //isso � feito para abrir a sess�o com o banco
            tiRecurso = tiRecursoService.findTiRecursoById(
                    tiRecurso.getCodigoTiRecurso());

            bean.setTo(tiRecurso);

            return Boolean.TRUE;
            // se nao existir na base de dados, mostra msg
        } else {
            Messages.showError("loadPapelAlocacaoView",
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_TI_RECURSO);

            return Boolean.TRUE;
        }
    }

    /**
     * Cancela a atualiza��o de um PapelAlocacao.
     *
     * @return pagina de destino
     */
    public String backToSearch() {
        findByFilter();

        return OUTCOME_RESEARCH;
    }

    /**
     * Cria uma entidade do tipo TcePapelAlocacao.
     */
    public void createCustoTiRecurso() {
        String validityMonth = bean.getMonthBeg();
        String validityYear = bean.getYearBeg();

        int startYear = Integer.parseInt(bean.getYearBeg());
        int startMonth = Integer.parseInt(bean.getMonthBeg());
        Date startDate = DateUtil.getDate(validityMonth, validityYear);

        // verifica o History Lock. Se startDate n�o for v�lido, d� mensagem de
        // erro
        if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
            return;
        }

        if(rateioService.isMonthApprovedByTiRecurso(startDate, bean.getTo().getCodigoTiRecurso())){
                Messages.showError("verifyDateApproved",
                                    Constants.MSG_ERROR_ADD_CUSTO_TI_RECURSO_SELECTED_DATE_APPROVED,
                                    DateUtil.formatDate(startDate, Constants.DEFAULT_DATE_PATTERN_SIMPLE, Constants.DEFAULT_CALENDAR_LOCALE));
            return;
        }

        List<CustoTiRecurso>  custoTiRecursoList = custoTiRecursoService.findCustoTiRecursoByTiRecurso(bean.getTo());
        for(CustoTiRecurso custo : custoTiRecursoList) {

            Calendar dataInicio = Calendar.getInstance();
            dataInicio.setTime(custo.getDataInicio());

            //Validando inicio de vigência menor que a vigência atual
            if(custo.getDataFim()== null && ((dataInicio.get(Calendar.YEAR) > startYear) || (dataInicio.get(Calendar.YEAR) == startYear && dataInicio.get(Calendar.MONTH) > startMonth))){
                Messages.showError("update",
                        Constants.DEFAULT_MSG_ERROR_VIGENCY_LICENSE_USER_ALREADY_EXISTS,
                        custo.getTiRecurso().getNomeTiRecurso() );
                return;
            }

            if(custo.getDataFim()== null && (dataInicio.get(Calendar.YEAR) < startYear || (dataInicio.get(Calendar.YEAR) == startYear && dataInicio.get(Calendar.MONTH) < startMonth-1))) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
                Date utilDate = null;
                Date finalDate = null;
                try {
                    utilDate = formatter.parse(startYear + "/" + startMonth);
                } catch (ParseException e) {
                    Messages.showError("update",
                            Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                            Constants.ENTITY_NAME_CUSTO_TI_RECURSO);
                    return;
                }
                finalDate = new DateTime(utilDate).minusDays(1).toDate();
                custo.setDataFim(finalDate);

                // encerra vigencia atual
                custoTiRecursoService.updateCustoTiRec(custo);


            }
        }

        CustoTiRecurso custoTiRecurso = bean.getCustoTiRecurso();
        custoTiRecurso.setDataInicio(startDate);
        //seta o TiRecurso
        custoTiRecurso.setTiRecurso(bean.getTo());

        custoTiRecursoService.createCustoTiRec(custoTiRecurso);

        // mensagem de sucesso
        Messages.showSucess("createTcePapelAlocacao",
                Constants.DEFAULT_MSG_SUCCESS_CREATE,
                Constants.ENTITY_NAME_CUSTO_TI_RECURSO);

        // limpa o TcePapelAlocacao no bean
        bean.resetCustoTiRecurso();

        // atualiza a lista de TcePapelAlocacao no PapelAlocacao corrente
        findById(bean.getTo().getCodigoTiRecurso());
    }

    /**
     * Remove uma entidade do tipo TcePapelAlocacao.
     */
    public void removeCustoTiRecurso() {
        CustoTiRecurso custoTiRec = bean.getCustoTiRecurso();

        try {

            if(rateioService.isMonthApprovedByTiRecurso(custoTiRec.getDataInicio(), bean.getTo().getCodigoTiRecurso())){
                Messages.showError("verifyDateApproved",
                        Constants.MSG_ERROR_REMOVE_CUSTO_TI_RECURSO_SELECTED_DATE_APPROVED,
                        DateUtil.formatDate(custoTiRec.getDataInicio(), Constants.DEFAULT_DATE_PATTERN_SIMPLE, Constants.DEFAULT_CALENDAR_LOCALE));

            }else{

                // remove o CustoTiRecurso
                custoTiRecursoService.removeCustoTiRec(
                        custoTiRecursoService.findCustoTiRecById(
                                custoTiRec.getCodigoCustoTiRecurso()));

                // mensagem de sucesso
                Messages.showSucess("removeTcePapelAlocacao",
                        Constants.ENTITY_NAME_CUSTO_TI_RECURSO,
                        Constants.ENTITY_NAME_TCE_PAPEL_ALOCACAO);
            }

        } catch (DataIntegrityViolationException e) {
            Messages.showError("removeCustoTiRecurso",
                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    Constants.ENTITY_NAME_CUSTO_TI_RECURSO);
        }

        // limpa o bean do CustoTiRecurso
        bean.resetCustoTiRecurso();
        // atualiza a lista de CustoTiRecurso no TiRecurso corrente
        findById(bean.getTo().getCodigoTiRecurso());
    }

    /**
     * Cancela a atualiza��o de um TcePapelAlocacao.
     */
    public void cancelCustoTiRecurso() {
        // limpa o bean do CustoTiRecurso
        bean.resetCustoTiRecurso();
    }

    /**
     * Busca uma entidade pelo id.
     *
     * @param id da entidade.
     *
     */
    private void findById(final Long id) {
        bean.setTo(tiRecursoService.findTiRecursoById(id));

        if (bean.getTo() == null
                || bean.getTo().getCodigoTiRecurso() == null) {

            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     */
    public void findByFilter() {
        TiRecurso filter = bean.getFilter();
        filter.getEmpresa().setCodigoEmpresa(bean.getCompanySelect().filter());

        Integer total = 0;
        List<TiRecurso> trList = tiRecursoService.findTiRecursoByFilter(filter);

        for(TiRecurso tire :trList){
            List<ChargebackPessoa> lsFiltered = new ArrayList<ChargebackPessoa>();
            for(ChargebackPessoa cbp : tire.getChargebackPessoas()){
                if(cbp.getDataFimVigencia() == null || cbp.getDataFimVigencia().equals(DateUtil.getDateFirstDayOfMonth(DateUtil.getNextMonth(new Date())))){
                    lsFiltered.add(cbp);
                }
            }

            tire.setChargebackPessoas(lsFiltered);
            total = tire.getChargebackPessoas().size();
            tire.setActiveUsers(total.toString());
        }

        bean.setResultList(trList);

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
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


    public void prepareTipoAlocacaoCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();
        bean.getTo().setIndicadorTipoAlocacao(value);

        // Resetar campos que ficam desabilitados
        bean.getCustoTiRecurso().setValorCustoTiRecurso(BigDecimal.ZERO);
    }

    /**
     * @param e
     */
    public void prepareTipoAlocacaoList(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();
        bean.getCustoTiRecurso().setValorCustoTiRecurso(BigDecimal.ZERO);

        if (value != null) {
            Long code = bean.getCompanySelect().map().get(value);

            if(code != null){
                bean.setTipoAlocacaoList(getTipoAlocacaoList(code, Boolean.FALSE));
                if(code.equals(Constants.CD_EMPRESA_INC)) bean.getTo().setIndicadorTipoAlocacao(Constants.TI_RECURSO_TYPE_SOFTWARE_USER);

                return;
            }
        }

        bean.getTo().setIndicadorTipoAlocacao(Constants.TI_RECURSO_TYPE_CONTRCT_SERVICE);
        bean.setTipoAlocacaoList(getTipoAlocacaoList(null, Boolean.FALSE));
    }

    /**
     * @param e
     */
    public void prepareTipoAlocacaoListResearch(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();
        if (value != null) {
            Long code = bean.getCompanySelect().map().get(value);

            if(code != null){
                bean.setTipoAlocacaoList(getTipoAlocacaoList(code, Boolean.TRUE));
                if(code.equals(Constants.CD_EMPRESA_INC)) bean.getFilter().setIndicadorTipoAlocacao(Constants.TI_RECURSO_TYPE_SOFTWARE_USER);

                return;
            }
        }

        bean.getFilter().setIndicadorTipoAlocacao(Constants.ALL);
        bean.setTipoAlocacaoList(getTipoAlocacaoList(null, Boolean.TRUE));
    }

    private String removeWhiteSpaceFromTheEnd(String nomeTiRecurso) {
        if (' ' == nomeTiRecurso.charAt(nomeTiRecurso.length() - 1)) {
            nomeTiRecurso = this.removeWhiteSpaceFromTheEnd(nomeTiRecurso.substring(0, nomeTiRecurso.length() - 1));
        }
        return nomeTiRecurso;
    }

    /**
     * Prepara a lista de empresas para a Inserção/Edição.
     */
    public void prepareCompanyList() {

        List<Empresa> empresas = new ArrayList<>();
        empresas.add(empresaService.findEmpresaById(1l));
        empresas.add(empresaService.findEmpresaById(Constants.CD_EMPRESA_INC));

        bean.setCompanySelect(new CompanySelect(empresas));
        bean.getCompanySelect().select(1l);
    }

    /**
     * @param company - Company to Compare
     * @param isFilter - Indicate if is filter
     * @return Lista com os Tipos de Alocação
     */
    public List<String> getTipoAlocacaoList(Long company, boolean isFilter){

        List<String> result = new ArrayList<>();
        if(company == null || !company.equals(Constants.CD_EMPRESA_INC)){

            if(isFilter)
                result.add(Constants.ALL);

            result.add(Constants.TI_RECURSO_TYPE_CONTRCT_SERVICE);
            result.add(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);
        }

        result.add(Constants.TI_RECURSO_TYPE_SOFTWARE_USER);
        return result;
    }

    /**
     * @return Empresa
     */
    private Empresa getCompany(){
        Empresa empresa = null;
        try {
            if(bean.getCompanySelect().value() != null)
                empresa = empresaService.findEmpresaById(bean.getCompanySelect().value());

        }catch (Exception e){
            e.printStackTrace();
        }

        return empresa;
    }
}
