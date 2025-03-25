package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IIndustryTypeService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.control.jsf.bean.IndustryTypeBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.BusinessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.INDUSTRY:VW", "BOF.INDUSTRY:ED", "BOF.INDUSTRY:DE", "BOF.INDUSTRY:CR"})
public class IndustryTypeController {

    /* Logger */
    private static final Log logger = LogFactory.getLog(IndustryTypeController.class.getName());

    /**
     * outcome tela inclusao da entidade.
     */
    private static final String OUTCOME_INDUSTRY_TYPE_RESEARCH = "industry_type_research";
    private static final String OUTCOME_INDUSTRY_TYPE_ADD = "industry_type_add";
    private static final String OUTCOME_INDUSTRY_TYPE_DELETE = "industry_type_delete";
    private static final String OUTCOME_INDUSTRY_TYPE_UPDATE = "industry_type_edit";

    /**
     * Bean from Industry Type.
     */
    @Autowired
    private IndustryTypeBean bean;

    /**
     * Service from Industry Type.
     */
    @Autowired
    private IIndustryTypeService service;

    /**
     * Service from MSA.
     */
    @Autowired
    private IMsaService msaService;

    /**
     * @return route to research screen
     */
    public String prepareResearch() {
        bean.reset();
        bean.setSuggestionsListInAtivo(getOptionsActiveInactive(true));
        return OUTCOME_INDUSTRY_TYPE_RESEARCH;
    }

    public String prepareCreate() {
        bean.reset();
        bean.setUpdate(Boolean.FALSE);
        bean.setSuggestionsListInAtivo(getOptionsActiveInactive(false));
        return OUTCOME_INDUSTRY_TYPE_ADD;
    }

    public String prepareUpdate() {

        try {
            findById(bean.getCurrentRowId());
        } catch (BusinessException e) {
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.error("Erro ao tentar buscar industry type by ID: " + e.getMessage());
            return null;
        }

        bean.setUpdate(Boolean.TRUE);
        bean.setSuggestionsListInAtivo(getOptionsActiveInactive(false));
        findMsas(bean.getCurrentRowId());
        return OUTCOME_INDUSTRY_TYPE_UPDATE;
    }

    public String prepareDelete() {
        try {
            findById(bean.getCurrentRowId());
            findMsas(bean.getCurrentRowId());
        } catch (BusinessException e) {
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.error("Erro ao tentar buscar industry type by ID: " + e.getMessage());
            return null;
        }
        return OUTCOME_INDUSTRY_TYPE_DELETE;
    }

    /**
     *
     */
    public void filter() {
        try {
            bean.setListIndustryType(service.findByFilter(bean.getFilter()));
            bean.setCurrentPageId(0);
            bean.setSuggestionsListInAtivo(getOptionsActiveInactive(true));
        } catch (BusinessException e) {
            bean.setListIndustryType(Collections.EMPTY_LIST);
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.error("Erro ao tentar buscar industries type: " + e.getMessage());
        }
    }

    /**
     * Deleta uma entidade.
     *
     * @return pagina de destino
     */
    public String delete() {
        try {
            service.remove(bean.getIndustryType().getCode());
        } catch (Exception e) {
            logger.error("Erro ao tentar remover industry type: " + e.getMessage());
            return null;
        }

        filter();
        bean.resetForm();

        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE, Constants.ENTITY_INDUSTRY_TYPE);
        return OUTCOME_INDUSTRY_TYPE_RESEARCH;
    }

    /**
     *
     */
    public void create() {
        try {
            service.create(bean.getIndustryType());
            bean.reset();
        } catch (BusinessException e) {
            Messages.showError("create", e.getMessage(), Constants.ENTITY_INDUSTRY_TYPE);
            return;
        }

        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE, Constants.ENTITY_INDUSTRY_TYPE);
    }

    /**
     * @return Route to Research
     */
    public String update() {

        try {
            service.update(bean.getIndustryType());
        } catch (BusinessException e) {
            Messages.showError("update", e.getMessage(), Constants.ENTITY_INDUSTRY_TYPE);
            return null;
        }

        filter();
        bean.resetForm();

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE, Constants.ENTITY_INDUSTRY_TYPE);
        return OUTCOME_INDUSTRY_TYPE_RESEARCH;
    }

    /**
     * @param industryTypeCode - Industry Type code to Research MSAs
     */
    private void findMsas(final Long industryTypeCode) {
        try {
            this.bean.setMsas(msaService.findMsaByIndustryType(industryTypeCode));
            this.bean.setMsasAssociated(Boolean.TRUE);
        } catch (BusinessException e) {
            this.bean.setMsas(Collections.EMPTY_LIST);
            this.bean.setMsasAssociated(Boolean.FALSE);
            logger.warn("Msas Not Found by Industry Type: " + e.getMessage());
        }
    }

    /* Methods */
    private void findById(final Long id) throws BusinessException {
        bean.setIndustryType(service.findById(id));
    }

    private List<String> getOptionsActiveInactive(boolean all) {
        return (all) ? Arrays.asList(Constants.ALL, Constants.ACTIVE, Constants.INACTIVE)
                : Arrays.asList(Constants.ACTIVE, Constants.INACTIVE);
    }

    /* Getters and Setters */
    public IndustryTypeBean getBean() {
        return bean;
    }

    public void setBean(IndustryTypeBean bean) {
        this.bean = bean;
    }
}