/**
 * 
 */
package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.security.RolesAllowed;
import javax.faces.event.ValueChangeEvent;

import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Moeda;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.control.jsf.bean.EmpresaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Empresa;


/**
 * Define o Controller da Entidade.
 *
 * @since 15/09/2010
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.COMPANY:DE", "BOF.COMPANY:CR", "BOF.COMPANY:ED", "BOF.COMPANY:VW" })
public class EmpresaController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    /** outcome tela inclusao da entidade. */
    private static final String OUTCOME_EMPRESA_ADD = "empresa_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_EMPRESA_EDIT = "empresa_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_EMPRESA_DELETE = "empresa_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_EMPRESA_RESEARCH = "empresa_research";
    
    /**
     * Instancia do servico.
     */
    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IMoedaService moedaService;
    
    /**
     * Instancia do bean.
     */
    @Autowired
    private EmpresaBean bean = null;

    /**
     * @return the bean
     */
    public EmpresaBean getBean() {
        return bean;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(final EmpresaBean bean) {
        this.bean = bean;
    }
    
    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareResearch() {
        bean.reset();
        loadEmpresaPaiList();
        loadMoedaList();
        bean.setSuggestionsListInAtivo(Constants.ACTIVE_INACTIVE_VALUES);
        return OUTCOME_EMPRESA_RESEARCH;
    }
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        Empresa filter = bean.getFilter();

        Long empresaPai = bean.getEmpresaPaiMap().get(
                bean.getEmpresaPaiSelectedFilter());
        if ((empresaPai != null)) {
            filter.setEmpresa(empresaService.findEmpresaById(empresaPai));
        } else {
            filter.setEmpresa(null);
        }

        bean.setResultList(empresaService.findEmpresaByFilter(filter));
        if (bean.getResultList().isEmpty()) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }
    
    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de criação de empresa
     */
    public String prepareCreate() {
        bean.resetTo();
        loadEmpresaPaiList();
        loadMoedaList();
        bean.setIsUpdate(Boolean.FALSE);
        return OUTCOME_EMPRESA_ADD;
    }
    
    /**
     * Ação que cria uma empresa.
     * 
     * @return retorna a pagina de destino.
     */
    public String create() {
        if (isEmpresaPaiInvalid()) {
            Messages.showError("create", Constants.MSG_ERROR_EMPRESA_PAI_NOT_FOUND);

            return null;
        }

        Empresa empresa = bean.getTo();
        empresa.setIndicadorAtivo(Constants.ACTIVE);
        Long empresaPai = bean.getEmpresaPaiMap().get(bean.getEmpresaPaiSelected());
        Long moeda = bean.getMoedaMap().get(bean.getMoedaSelected());
        
        if (empresaPai != null) {
            empresa.setEmpresa(empresaService.findEmpresaById(empresaPai));
        }

        if (moeda != null) {
            empresa.setMoeda(moedaService.findMoedaById(moeda));
        }

        if (isInvalidCodigoTaxpayerCompany()) {
            Messages.showError("create",
                    Constants.DEFAULT_MSG_ERROR_HAS_INVALID_ORACLE_TAXPAYER);
            return null;
        }

        if (isExistingCodigoTaxpayerCompany(empresa, "create")) {
            return null;
        }

        try {
            empresaService.createEmpresa(empresa);

        } catch (IntegrityConstraintException ice) {
            Messages.showError("create", ice.getMessage());

            return null;
        }
        
        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                Constants.ENTITY_NAME_EMPRESA);
        bean.resetTo();
        bean.resetBranchAndParentCompany();

        // atualiza o combo de empresas Pai
        loadEmpresaPaiList();
        return OUTCOME_EMPRESA_ADD;
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareUpdate() {
        loadEmpresaPaiList();
        loadMoedaList();
        bean.setSuggestionsListInAtivo(Constants.ACTIVE_INACTIVE_VALUES);
        findById(bean.getCurrentRowId());
        
        bean.setIsUpdate(Boolean.TRUE);
        return OUTCOME_EMPRESA_EDIT;
    }
    
    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        if (isEmpresaPaiInvalid()) {
            Messages.showError("update", Constants.MSG_ERROR_EMPRESA_PAI_NOT_FOUND);

            return null;
        }

        Empresa empresa = bean.getTo();

        Long empresaPai = bean.getEmpresaPaiMap().get(
                bean.getEmpresaPaiSelected());

        // se empresaPai não for null, atribui o EmpresaPai e essa Empresa se torna uma EmpresaFilho
        if ((empresaPai != null)) {
            empresa.setEmpresa(empresaService.findEmpresaById(empresaPai));
        } else {
            empresa.setEmpresa(null);
        }

        Long moeda = bean.getMoedaMap().get(bean.getMoedaSelected());

        if (moeda != null) {
            empresa.setMoeda(moedaService.findMoedaById(moeda));
        }

        if (isInvalidCodigoTaxpayerCompany()) {
            Messages.showError("update",
                    Constants.DEFAULT_MSG_ERROR_HAS_INVALID_ORACLE_TAXPAYER);
            return null;
        }

        if (isExistingCodigoTaxpayerCompany(empresa, "update")) {
            return null;
        }

        try {
            empresaService.updateEmpresa(empresa);

        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage(), new Object[] {
                    Constants.ENTITY_NAME_EMPRESA,
                    Constants.ENTITY_NAME_EMPRESA});

            return null;
        }

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_EMPRESA);

        bean.resetTo();
        bean.resetBranchAndParentCompany();
        findByFilter();

        // atualiza o combo de EmpresaPai
        loadEmpresaPaiList();

        return OUTCOME_EMPRESA_RESEARCH;
    }

    /**
     * Prepara a tela de remocao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareRemove() {
        findById(bean.getCurrentRowId());
        return OUTCOME_EMPRESA_DELETE;
    }
    
    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {

        Empresa empresa = empresaService.findEmpresaById(bean.getTo().getCodigoEmpresa());

        try {
            empresaService.removeEmpresa(empresa);
        } catch (IntegrityConstraintException ice) {    
            Messages.showError("remove", ice.getMessage(), new Object[] {
                Constants.ENTITY_NAME_EMPRESA,
                Constants.ENTITY_NAME_EMPRESA });
            
            return null;
        }

        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_EMPRESA);

        bean.resetTo();
        bean.resetBranchAndParentCompany();
        findByFilter();

        // atualiza o combo de EmpresaPai
        loadEmpresaPaiList();

        return OUTCOME_EMPRESA_RESEARCH;
    }
    
    /**
     * Popula a lista de Empresas para combobox da Empresa pai.
     */
    private void loadEmpresaPaiList() {

        List<Empresa> empresas = empresaService.findEmpresaAllParentCompany();
        Map<String, Long> empresaPaiMap = new HashMap<String, Long>();
        List<String> empresaPaiList = new ArrayList<String>();

        for (Empresa empresa : empresas) {
            empresaPaiMap.put(empresa.getNomeEmpresa(), empresa.getCodigoEmpresa());
            empresaPaiList.add(empresa.getNomeEmpresa());
        }

        bean.setEmpresaPaiMap(empresaPaiMap);
        bean.setEmpresaPaiList(empresaPaiList);
    }

    /**
     * Popula a lista de Moedas para combobox de Moedas.
     */
    private void loadMoedaList() {
        List<Moeda> moedas = moedaService.findMoedaAll();
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
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        bean.setTo(empresaService.findEmpresaById(id));
        Empresa to = bean.getTo();
        if (to != null) {
            if (to.getEmpresa() != null) {
                bean.setEmpresaPaiSelected(to.getEmpresa().getNomeEmpresa());
            } else {
                bean.setEmpresaPaiSelected("");
            }

            if (to.getMoeda() != null) {
                bean.setMoedaSelected(to.getMoeda().getNomeMoeda());
            }
        } else {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    public void handleEmpresaPaiSelected(final ValueChangeEvent e) {
        bean.setEmpresaPaiSelectedAnterior((String) e.getOldValue());
        bean.setEmpresaPaiSelected((String) e.getNewValue());

        bean.prepareParentFieldsFulfilled();
        bean.prepareBranchFieldsFulfilled();
    }

    private boolean isInvalidCodigoTaxpayerCompany() {
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = pattern.matcher(bean.getTo().getCodigoTaxpayerCompany());
        return matcher.find();
    }

    private boolean isExistingCodigoTaxpayerCompany(Empresa empresa, String methodName) {
        String taxpayerCode = empresa.getCodigoTaxpayerCompany();
        if (taxpayerCode != null && !taxpayerCode.isEmpty()) {
            List<Empresa> empresaByCodigoTaxpayerCompany = empresaService.findEmpresaByCodigoTaxpayerCompany(empresa.getCodigoTaxpayerCompany());
            for (Empresa empresaFound : empresaByCodigoTaxpayerCompany) {
                log.info("Encontrada empresa com taxpayer '" + empresa.getCodigoTaxpayerCompany() + "': " + empresaFound.getNomeEmpresa());
            }
            if (!empresaByCodigoTaxpayerCompany.isEmpty() && !empresaByCodigoTaxpayerCompany.get(0).getCodigoEmpresa().equals(empresa.getCodigoEmpresa())) {
                Messages.showError(methodName,
                        Constants.DEFAULT_MSG_ERROR_HAS_EXISTING_ORACLE_TAXPAYER_COMPANY,
                        empresaByCodigoTaxpayerCompany.get(0).getNomeEmpresa());
                return true;
            }
        }
        return false;
    }

    public boolean isChangedParentToBranch() {
        return(
            (bean.getEmpresaPaiSelected() != null || !bean.getEmpresaPaiSelected().isEmpty())
            &&
            (bean.getEmpresaPaiSelectedAnterior() == null || bean.getEmpresaPaiSelectedAnterior().isEmpty())
        );
    }

    public boolean isChangedBranchToParent() {
        return (
            (bean.getEmpresaPaiSelected() == null || bean.getEmpresaPaiSelected().isEmpty())
            &&
            (bean.getEmpresaPaiSelectedAnterior() != null || !bean.getEmpresaPaiSelectedAnterior().isEmpty())
        );
    }

    public boolean isChangedBranchToBranch() {
        return (
            (bean.getEmpresaPaiSelected() != null || !bean.getEmpresaPaiSelected().isEmpty())
            &&
            (bean.getEmpresaPaiSelectedAnterior()!= null || !bean.getEmpresaPaiSelectedAnterior().isEmpty())
        );
    }

    private boolean isEmpresaPaiInvalid() {
        String empresaPaiName = bean.getEmpresaPaiSelected();

        return !empresaPaiName.isEmpty() && !bean.getEmpresaPaiList().contains(empresaPaiName);
    }

    public void continueEmpresaPaiSelected() {
        if (isChangedParentToBranch()) {
            bean.setIsParentFieldsFulfilled(Boolean.FALSE);

            bean.getTo().setCodigoEmpresaERP(null);
            bean.getTo().setCodigoErpProjetoPadrao(null);
        } else if (isChangedBranchToParent() || isChangedBranchToBranch()) {
            bean.setIsBranchFieldsFulfilled(Boolean.FALSE);

            bean.getTo().setCodigoErpFilial(null);
            bean.getTo().setCodigoErpCcusto(null);
            bean.getTo().setCodigoErpProjIde(null);
            bean.getTo().setIndicadorExibicaoMsaContrato(null);
        }
    }

    public void cancelEmpresaPaiSelected() {
        bean.setEmpresaPaiSelected(bean.getEmpresaPaiSelectedAnterior());
    }
}
