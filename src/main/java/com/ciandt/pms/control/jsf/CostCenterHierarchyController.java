package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICostCenterHierarchyService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.control.jsf.bean.CostCenterHierarchyBean;
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


/**
 * Define o Controller da entidade.
 *
 * @since 15/07/2024
 * @author <a href="mailto:daimomsr@ciandt.com">Daimom Rosa</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.COST_CENTER.HIER:CR", "BOF.COST_CENTER.HIER:ED", "BOF.COST_CENTER.HIER:VW", "BOF.COST_CENTER.HIER:DE" })
public class CostCenterHierarchyController {


    /** outcome tela inclusao da entidade. */
    private static final String OUTCOME_COST_CENTER_HIERARCHY_ADD = "cost_center_hierarchy_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_COST_CENTER_HIERARCHY_EDIT = "cost_center_hierarchy_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_COST_CENTER_HIERARCHY_DELETE = "cost_center_hierarchy_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_COST_CENTER_HIERARCHY_SEARCH = "cost_center_hierarchy_search";

    /**
     * Instancia do servico.
     */
    @Autowired
    private ICostCenterHierarchyService service;

    /**
     * Instancia do servico.
     */
    @Autowired
    private IGrupoCustoService costCenterService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private CostCenterHierarchyBean bean = null;

    /* Logger */
    private static final Log logger = LogFactory.getLog(CostCenterHierarchyController.class.getName());

    /**
     * @return route to research screen
     */
    public String prepareSearch() {
        bean.reset();
        return OUTCOME_COST_CENTER_HIERARCHY_SEARCH;
    }

    public String prepareCreate() {
        bean.reset();
        bean.setIsUpdate(Boolean.FALSE);
        return OUTCOME_COST_CENTER_HIERARCHY_ADD;
    }

    public String prepareUpdate() {
        bean.setIsUpdate(Boolean.TRUE);
        return OUTCOME_COST_CENTER_HIERARCHY_EDIT;
    }

    public String prepareRemove() {
        try {
            findById(bean.getCurrentRowId());
            findCostCenters(bean.getCurrentRowId());
        } catch (BusinessException e){
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.error("An error occurred while trying to find Cost Center Hierarchy by id: "+ e.getMessage());
            return null;
        }
        return OUTCOME_COST_CENTER_HIERARCHY_DELETE;
    }

    /**
     *
     */
    public void findByFilter() {
        try {
            bean.setListCostCenterHierarchy(service.findByFilter(bean.getFilter()));
            bean.setCurrentPageId(0);
        } catch (BusinessException e) {
            bean.setListCostCenterHierarchy(Collections.EMPTY_LIST);
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.info("Error searching cost center hierarchy: "+ e.getMessage());
        }
    }

    /**
     * Deleta uma entidade.
     *
     * @return pagina de destino
     *
     */
    public String delete() {
        try {
            service.remove(bean.getCostCenterHierarchy().getCode());
        } catch (Exception e) {
            logger.error("An error occurred while trying to remove the Cost Center Hierarchy: "+ e.getMessage());
            return null;
        }

        findByFilter();
        bean.resetForm();

        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE, Constants.ENTITY_NAME_COST_CENTER_HIERARCHY);
            return OUTCOME_COST_CENTER_HIERARCHY_SEARCH;
    }

    private void findCostCenters(final Long code) {
        try {
            bean.setCostCenters(costCenterService.findCostCentersByCostCenterHierarchy(code));
            bean.setHasAssociatedCostCenters(Boolean.TRUE);
        } catch (BusinessException e) {
            bean.setCostCenters(Collections.EMPTY_LIST);
            bean.setHasAssociatedCostCenters(Boolean.FALSE);
            logger.info("An error occurred while trying to find Cost Centers by Cost Center Hierarchy: "+ e.getMessage());
        }
    }
    /**
     *
     */
    public void create() {
        try {
            service.create(bean.getCostCenterHierarchy());
            bean.reset();
        } catch (BusinessException e) {
            Messages.showError("create", e.getMessage(), Constants.ENTITY_NAME_COST_CENTER_HIERARCHY);
            logger.info("Error creating Cost Center Hierarchy: "+ e.getMessage());
            return;
        }
        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE, Constants.ENTITY_NAME_COST_CENTER_HIERARCHY);
    }

    public String update() {

        try {
            service.update(bean.getCostCenterHierarchy());

        } catch (final BusinessException e) {
            Messages.showError("costCenterHierarchyController.update", e.getMessage(), Constants.ENTITY_NAME_COST_CENTER_HIERARCHY);
            logger.info("Error updating Cost Center Hierarchy: "+ e.getMessage());
            return OUTCOME_COST_CENTER_HIERARCHY_EDIT;
        }

        bean.resetForm();
        findByFilter();

        Messages.showSucess("costCenterHierarchyController.update", Constants.DEFAULT_MSG_SUCCESS_UPDATE, Constants.ENTITY_NAME_COST_CENTER_HIERARCHY);
        return OUTCOME_COST_CENTER_HIERARCHY_SEARCH;
    }

    public String cancelUpdate() {
        bean.resetForm();
        findByFilter();
        return OUTCOME_COST_CENTER_HIERARCHY_SEARCH;
    }

    /* Methods */
    private void findById(final Long id) throws  BusinessException {
        bean.setCostCenterHierarchy(service.findByCode(id));
    }

    private List<String> getOptionsActiveInactive(){
        return Arrays.asList(Constants.ACTIVE, Constants.INACTIVE);
    }

    /* Getters and Setters */
    public CostCenterHierarchyBean getBean() {
        return bean;
    }
    public void setBean(CostCenterHierarchyBean bean) {
        this.bean = bean;
    }
}
