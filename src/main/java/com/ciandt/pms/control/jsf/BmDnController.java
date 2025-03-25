package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IBmDnService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.control.jsf.bean.BmDnBean;
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
 * @since 01/07/2022
 * @author <a href="mailto:pricaldeira@ciandt.com">Priscilla Caldeira</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.BM_DN:VW", "BOF.BM_DN:ED", "BOF.BM_DN:DE", "BOF.BM_DN:CR" })
public class BmDnController {


    /** outcome tela inclusao da entidade. */
    private static final String OUTCOME_BMDN_ADD = "bmdn_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_BMDN_EDIT = "bmdn_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_BMDN_DELETE = "bmdn_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_BMDN_RESEARCH = "bmdn_research";

    /**outcome tela pesquisa da entidade.*/
    private static final String OUTCOME_BMDN_VIEW = "bmdn_view";

    /**
     * Instancia do servico.
     */
    @Autowired
    private IBmDnService service;

    /**
     * Instancia do servico.
     */
    @Autowired
    private IMsaService msaService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private BmDnBean bean = null;

    /* Logger */
    private static final Log logger = LogFactory.getLog(BmDnController.class.getName());

    /**
     * @return route to research screen
     */
    public String prepareResearch() {
        bean.reset();
        return OUTCOME_BMDN_RESEARCH;
    }

    public String prepareCreate() {
        bean.reset();
        bean.setUpdate(Boolean.FALSE);
        return OUTCOME_BMDN_ADD;
    }

    public String prepareUpdate() {

        try{
            findById(bean.getCurrentRowId());
        }catch (BusinessException e){
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.error("Erro ao tentar buscar BM DN by ID: "+ e.getMessage());
            return null;
        }

        bean.setUpdate(Boolean.TRUE);
        findMsas(bean.getCurrentRowId());
        return OUTCOME_BMDN_EDIT;
    }

    public String prepareRemove() {
        try{
            findById(bean.getCurrentRowId());
            findMsas(bean.getCurrentRowId());
        }catch (BusinessException e){
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.error("Erro ao tentar buscar BM DN by ID: "+ e.getMessage());
            return null;
        }
        return OUTCOME_BMDN_DELETE;
    }

    /**
     *
     */
    public void findByFilter() {
        try{
            bean.setListBmDn(service.findByFilter(bean.getFilter()));
            bean.setCurrentPageId(0);
        }catch (BusinessException e){
            bean.setListBmDn(Collections.EMPTY_LIST);
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.error("Erro ao tentar buscar BM DN: "+ e.getMessage());
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
            service.remove(bean.getBmDn().getCode());
        } catch (Exception e) {
            logger.error("Erro ao tentar remover BM DN: "+ e.getMessage());
            return null;
        }

        findByFilter();
        bean.resetForm();

        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE, Constants.ENTITY_NAME_BMDN);
        return OUTCOME_BMDN_RESEARCH;
    }

    /**
     *
     */
    public void create() {
        try {
            service.create(bean.getBmDn());
            bean.reset();
        } catch (BusinessException e) {
            Messages.showError("create", e.getMessage(), Constants.ENTITY_NAME_BMDN);
            return;
        }

        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE, Constants.ENTITY_NAME_BMDN);
    }

    /**
     * @return Route to Research
     */
    public String update() {

        try {
            service.update(bean.getBmDn());
        } catch (BusinessException e) {
            Messages.showError("update", e.getMessage(), Constants.ENTITY_NAME_BMDN);
            return null;
        }

        findByFilter();
        bean.resetForm();

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE, Constants.ENTITY_NAME_BMDN);
        return OUTCOME_BMDN_RESEARCH;
    }

    /**
     * @param bmdnCode - Bmdn code to Research MSAs
     */
    private void findMsas(final Long bmdnCode){
        try{
            this.bean.setMsas(msaService.findMsaByBmDn(bmdnCode));
            this.bean.setMsasAssociated(Boolean.TRUE);
        }catch (BusinessException e){
            this.bean.setMsas(Collections.EMPTY_LIST);
            this.bean.setMsasAssociated(Boolean.FALSE);
            logger.warn("Msas Not Found by Bmdn Code: "+ e.getMessage());
        }
    }

    /* Methods */
    private void findById(final Long id) throws  BusinessException{
        bean.setBmDn(service.findById(id));
    }

    private List<String> getOptionsActiveInactive(){
        return Arrays.asList(Constants.ACTIVE, Constants.INACTIVE);
    }

    /* Getters and Setters */
    public BmDnBean getBean() {
        return bean;
    }
    public void setBean(BmDnBean bean) {
        this.bean = bean;
    }
}
