/**
 * Classe IntegFaturaParametroController - Classe da camada de apresentação do IntegFaturaParametro
 */
package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IFonteReceitaService;
import com.ciandt.pms.business.service.IIntegFaturaParametroService;
import com.ciandt.pms.business.service.ITipoServicoService;
import com.ciandt.pms.control.jsf.bean.IntegFaturaParametroBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.model.IntegFaturaParametro;
import com.ciandt.pms.model.TipoServico;


/**
 * 
 * A classe IntegFaturaParametroController proporciona as funcionalidades da
 * camada de apresentação para a entidade IntegFaturaParametro.
 * 
 * @since 24/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.INVOICE_PARAMETERS:CR", "BOF.INVOICE_PARAMETERS:VW", "BOF.INVOICE_PARAMETERS:ED",
"BOF.INVOICE_PARAMETERS:DE"})
public class IntegFaturaParametroController {

    /*********** OUTCOMES **************************/

    /** Outcome da tela de pesquisa da entidade. */
    private static final String OUTCOME_INTEG_FATURA_PARAMETRO_RESEARCH =
            "integFaturaParametro_research";

    /** Outcome da tela de criação da entidade. */
    private static final String OUTCOME_INTEG_FATURA_PARAMETRO_ADD =
            "integFaturaParametro_add";

    /** Outcome da tela de remoção da entidade. */
    private static final String OUTCOME_INTEG_FATURA_PARAMETRO_REMOVE =
            "integFaturaParametro_remove";

    /** Outcome da tela de remoção da entidade. */
    private static final String OUTCOME_INTEG_FATURA_PARAMETRO_EDIT =
            "integFaturaParametro_edit";

    /*********** SERVICES **************************/

    /** Instancia do servico. */
    @Autowired
    private IIntegFaturaParametroService integFaturaParametroService;

    /** Instancia do servico Empresa. */
    @Autowired
    private IEmpresaService empresaService;

    /** Instancia do servico TipoServico. */
    @Autowired
    private ITipoServicoService tipoServicoService;

    /** Instancia do servico FonteReceita. */
    @Autowired
    private IFonteReceitaService fonteReceitaService;

    /*********** BEANS **************************/

    /** Instancia do bean. */
    @Autowired
    private IntegFaturaParametroBean bean = null;

    /********************************************/

    /**
     * @return the bean
     */
    public IntegFaturaParametroBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final IntegFaturaParametroBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela de Criacao.
     * 
     * @return retorna o outcome da pagina de criação.
     */
    public String prepareCreate() {
        bean.reset();

        // carrega as listas de entidades
        loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
        loadTipoServicoList(tipoServicoService.findTipoServicoAll());
        loadFonteReceitaList(fonteReceitaService.findFonteReceitaAll());

        return OUTCOME_INTEG_FATURA_PARAMETRO_ADD;
    }

    /**
     * Cria uma entidade.
     */
    public void create() {
        IntegFaturaParametro intFatParam = verifyCreateOrUpdateIntFatParam();

        // cria o IntegFaturaParametro
        try {
            integFaturaParametroService.createIntegFaturaParametro(intFatParam);

            // mensagem de sucesso
            Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_INTEG_FATURA_PARAMETRO);

            // limpa o bean do IntegFaturaParametro
            bean.reset();

        } catch (IntegrityConstraintException ice) {
            Messages.showError("create", ice.getMessage(),
                    new Object[]{Constants.ENTITY_NAME_INTEG_FATURA_PARAMETRO});
        }

    }

    /**
     * Prepara a tela para update de um IntegFaturaParametro.
     * 
     * @return retorna o outcome para a pagina.
     */
    public String prepareUpdate() {
        findById(bean.getCurrentRowId());
        return OUTCOME_INTEG_FATURA_PARAMETRO_EDIT;
    }

    /**
     * Atualiza uma entidade do tipo IntegFaturaParametro.
     * 
     * @return retorna o outcome da pagina de Search
     */
    public String update() {
        IntegFaturaParametro intFatParam = verifyCreateOrUpdateIntFatParam();

        // atualiza o IntegFaturaParametro
        try {
            integFaturaParametroService.updateIntegFaturaParametro(intFatParam);

            // mensagem de sucesso
            Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_INTEG_FATURA_PARAMETRO);

            // limpa o bean do IntegFaturaParametro
            bean.resetTo();
            // atualiza a lista de IntegFaturaParametro
            findByFilter();
        } catch (IntegrityConstraintException ice) {
            Messages.showError("create", ice.getMessage(),
                    new Object[]{Constants.ENTITY_NAME_INTEG_FATURA_PARAMETRO});
        }
        return OUTCOME_INTEG_FATURA_PARAMETRO_RESEARCH;
    }

    /**
     * Prepara a tela de Remoção.
     * 
     * @return retorna o outcome da pagina de remoção.
     */
    public String prepareRemove() {
        findById(bean.getCurrentRowId());
        return OUTCOME_INTEG_FATURA_PARAMETRO_REMOVE;
    }

    /**
     * Remove uma entidade do tipo IntegFaturaParametro.
     * 
     * @return retorna o outcome da pagina.
     */
    public String remove() {
        IntegFaturaParametro intFatParam = bean.getTo();

        // remove o IntegFaturaParametro
        integFaturaParametroService
                .removeIntegFaturaParametro(integFaturaParametroService
                        .findIntFatParamById(intFatParam
                                .getCodigoIntegFaturaParam()));

        // mensagem de sucesso
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_INTEG_FATURA_PARAMETRO);

        // limpa o bean do IntegFaturaParametro
        bean.resetTo();

        // atualiza a lista de IntegFaturaParametro
        findByFilter();

        return OUTCOME_INTEG_FATURA_PARAMETRO_RESEARCH;
    }

    /**
     * Cancela a atualização de um IntegFaturaParametro.
     * 
     * @return retorna o outcome de research.
     */
    public String cancel() {
        bean.resetTo();
        findByFilter();

        // carrega as listas de entidades
        loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
        loadTipoServicoList(tipoServicoService.findTipoServicoAll());
        loadFonteReceitaList(fonteReceitaService.findFonteReceitaAll());

        return OUTCOME_INTEG_FATURA_PARAMETRO_RESEARCH;
    }

    /**
     * Prepara a tela de Busca.
     * 
     * @return retorna o outcome da pagina de busca.
     */
    public String prepareResearch() {

        bean.reset();
        // marca como modo de inserção
        bean.setIsUpdate(Boolean.valueOf(false));

        // carrega as listas de entidades
        loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
        loadTipoServicoList(tipoServicoService.findTipoServicoAll());
        loadFonteReceitaList(fonteReceitaService.findFonteReceitaAll());

        return OUTCOME_INTEG_FATURA_PARAMETRO_RESEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        IntegFaturaParametro intFatParam =
                integFaturaParametroService.findIntFatParamById(id);
        bean.setTo(intFatParam);

        if (bean.getTo() == null
                || bean.getTo().getCodigoIntegFaturaParam() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca a lista de entidades.
     * 
     */
    public void findAll() {
        // realiza a busca pelo IntegFaturaParametro
        bean.setResultList(integFaturaParametroService.findIntFatParamAll());
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     */
    public void findByFilter() {

        IntegFaturaParametro filter = new IntegFaturaParametro();

        // Setta a Branch
        String nomeEmpresa = bean.getFilter().getEmpresa().getNomeEmpresa();
        if (nomeEmpresa != null && !nomeEmpresa.isEmpty()) {
            filter.setEmpresa(empresaService.findEmpresaById(bean
                    .getEmpresaMap().get(
                            bean.getFilter().getEmpresa().getNomeEmpresa())));
        }

        // Setta o Service Type
        String nomeTipoServico =
                bean.getFilter().getTipoServico().getNomeTipoServico();
        if (nomeTipoServico != null && !nomeTipoServico.isEmpty()) {
            filter.setTipoServico(tipoServicoService.findTipoServicoById(bean
                    .getTipoServicoMap().get(nomeTipoServico)));
        }

        // Setta Revanue Source
        String nomeFonteReceita =
                bean.getFilter().getFonteReceita().getNomeFonteReceita();
        if (nomeFonteReceita != null && !nomeFonteReceita.isEmpty()) {
            filter.setFonteReceita(fonteReceitaService
                    .findFonteReceitaById(bean.getFonteReceitaMap().get(
                            nomeFonteReceita)));
        }

        bean.setResultList(integFaturaParametroService
                .findIntFatParamByFilter(filter));

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    /**
     * Faz a verificação das entidades para criar ou editar um
     * IntegFaturaParametro.
     * 
     * @return IntegFaturaParametro - retorna o IntegFaturaParametro
     */
    private IntegFaturaParametro verifyCreateOrUpdateIntFatParam() {
        IntegFaturaParametro intFatParam = bean.getTo();

        // atualiza a referencia das entidades
        intFatParam.setEmpresa(empresaService
                .findEmpresaById(bean.getEmpresaMap().get(
                        intFatParam.getEmpresa().getNomeEmpresa())));

        intFatParam.setTipoServico(tipoServicoService.findTipoServicoById(bean
                .getTipoServicoMap().get(
                        intFatParam.getTipoServico().getNomeTipoServico())));

        intFatParam.setFonteReceita(fonteReceitaService
                .findFonteReceitaById(bean.getFonteReceitaMap().get(
                        intFatParam.getFonteReceita().getNomeFonteReceita())));

        return intFatParam;
    }

    /**
     * Popula a lista de Empresa para combobox.
     * 
     * @param pEmpresaList
     *            lista de Empresa.
     * 
     */
    private void loadEmpresaList(final List<Empresa> pEmpresaList) {

        Map<String, Long> empresaMap = new HashMap<String, Long>();
        List<String> empresaList = new ArrayList<String>();

        for (Empresa empresa : pEmpresaList) {
            empresaMap
                    .put(empresa.getNomeEmpresa(), empresa.getCodigoEmpresa());
            empresaList.add(empresa.getNomeEmpresa());
        }
        bean.setEmpresaMap(empresaMap);
        bean.setEmpresaList(empresaList);
    }

    /**
     * Verifica se o valor do atributo Empresa é valido. Se o valor não é nulo e
     * existe no empresaMap, então o valor é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateEmpresa(final FacesContext context,
            final UIComponent component, final Object value) {

        Long codigoEmpresa = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            codigoEmpresa = bean.getEmpresaMap().get(strValue);
            if (codigoEmpresa == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * Popula a lista de TipoServico para combobox.
     * 
     * @param pTipoServicoList
     *            lista de TipoServico.
     * 
     */
    private void loadTipoServicoList(final List<TipoServico> pTipoServicoList) {

        Map<String, Long> tipoServicoMap = new HashMap<String, Long>();
        List<String> tipoServicoList = new ArrayList<String>();

        for (TipoServico tipoServico : pTipoServicoList) {
            tipoServicoMap.put(tipoServico.getNomeTipoServico(), tipoServico
                    .getCodigoTipoServico());
            tipoServicoList.add(tipoServico.getNomeTipoServico());
        }
        bean.setTipoServicoMap(tipoServicoMap);
        bean.setTipoServicoList(tipoServicoList);
    }

    /**
     * Verifica se o valor do atributo TipoServico é valido. Se o valor não é
     * nulo e existe no tipoServicoMap, então o valor é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateTipoServico(final FacesContext context,
            final UIComponent component, final Object value) {

        Long codigoTipoServico = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            codigoTipoServico = bean.getTipoServicoMap().get(strValue);
            if (codigoTipoServico == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * Popula a lista de FonteReceita para combobox.
     * 
     * @param pFonteReceitaList
     *            lista de FonteReceita.
     * 
     */
    private void loadFonteReceitaList(final List<FonteReceita> pFonteReceitaList) {

        Map<String, Long> fonteReceitaMap = new HashMap<String, Long>();
        List<String> fonteReceitaList = new ArrayList<String>();

        for (FonteReceita fonteReceita : pFonteReceitaList) {
            fonteReceitaMap.put(fonteReceita.getNomeFonteReceita(),
                    fonteReceita.getCodigoFonteReceita());
            fonteReceitaList.add(fonteReceita.getNomeFonteReceita());
        }

        bean.setFonteReceitaMap(fonteReceitaMap);
        bean.setFonteReceitaList(fonteReceitaList);
    }

    /**
     * Verifica se o valor do atributo FonteReceita é valido. Se o valor não é
     * nulo e existe no fonteReceitaMap, então o valor é valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validateFonteReceita(final FacesContext context,
            final UIComponent component, final Object value) {

        Long codigoFonteReceita = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            codigoFonteReceita = bean.getFonteReceitaMap().get(strValue);
            if (codigoFonteReceita == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

}