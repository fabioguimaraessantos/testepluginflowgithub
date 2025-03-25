package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.control.jsf.bean.CidadeBaseBean;
import com.ciandt.pms.control.jsf.util.CurrencyUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CidadeBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import java.util.Collections;

@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.CITY_BASE:VW", "BOF.CITY_BASE:CR", "BOF.CITY_BASE:ED", "BOF.CITY_BASE:DE"})
public class CidadeBaseController {

    private static final Log logger = LogFactory.getLog(IndustryTypeController.class.getName());
    private static final String OUTCOME_CIDADE_BASE_ADD = "cidadeBase_add";
    private static final String OUTCOME_CIDADE_BASE_SEARCH = "cidadeBase_search";

    @Autowired
    private ICidadeBaseService cidadeBaseService;

    @Autowired
    private IMoedaService moedaService;

    @Autowired
    private CidadeBaseBean bean;

    public CidadeBaseBean getBean() {
        return bean;
    }

    public void setBean(CidadeBaseBean bean) {
        this.bean = bean;
    }

    public String prepareSearch() {
        bean.reset();

        bean.setMoedaMap(CurrencyUtil.loadMoedaMap(moedaService.findMoedaAll()));
        bean.setMoedaList(CurrencyUtil.loadMoedaList(moedaService.findMoedaAll()));
        bean.setSearch(Boolean.TRUE);

        return OUTCOME_CIDADE_BASE_SEARCH;
    }

    public void findByFilter() {
        try {
            CidadeBase filter = bean.getTo();
            bean.setFilter(filter);

            Long moedaCodigo = bean.getMoedaMap().get(bean.getMoedaSelected());
            filter.setMoeda(moedaCodigo != null ? moedaService.findMoedaById(moedaCodigo) : null);

            bean.setResultList(cidadeBaseService.findByFilter(filter));
            bean.setCurrentPageId(0L);
        } catch (BusinessException e) {
            bean.setResultList(Collections.emptyList());
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.error("Erro ao tentar buscar Cidade Base.");
        }
    }

    public String prepareCreate() {
        bean.resetTo();

        bean.setMoedaMap(CurrencyUtil.loadMoedaMap(moedaService.findMoedaAll()));
        bean.setMoedaList(CurrencyUtil.loadMoedaList(moedaService.findMoedaAll()));
        bean.setSearch(Boolean.FALSE);

        return OUTCOME_CIDADE_BASE_ADD;
    }

    public String create() {
        CidadeBase cidadeBase = bean.getTo();
        cidadeBase.setIndicadorAtivo(Constants.ACTIVE);

        Long moeda = bean.getMoedaMap().get(bean.getMoedaSelected());

        if (moeda != null) {
            cidadeBase.setMoeda(moedaService.findMoedaById(moeda));
        }

        try {
            cidadeBaseService.createCidadeBase(cidadeBase);

        } catch (IntegrityConstraintException ice) {
            Messages.showError("create", ice.getMessage());

            return null;
        }

        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                Constants.ENTITY_NAME_CIDADE_BASE);
        bean.resetTo();

        return OUTCOME_CIDADE_BASE_ADD;
    }

    public void prepareUpdate() {
        bean.setStatusList(bean.getStatusList());
        findById(bean.getCurrentRowId());
        bean.setUpdate(Boolean.TRUE);
    }

    public String update() {
        CidadeBase cityBase = bean.getTo();

        Long currencyId = bean.getMoedaMap().get(bean.getMoedaSelected());

        if (currencyId != null) {
            cityBase.setMoeda(moedaService.findMoedaById(currencyId));
        }

        try{
            cidadeBaseService.updateCidadeBase(cityBase);
        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage(), new Object[] {
                    Constants.ENTITY_NAME_CIDADE_BASE,
                    Constants.ENTITY_NAME_CIDADE_BASE
            });
            return null;
        }

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_CIDADE_BASE);

        restoreFilledForm();

        findByFilter();
        bean.setUpdate(Boolean.FALSE);
        return OUTCOME_CIDADE_BASE_SEARCH;
    }

    public String cancelUpdate () {
        restoreFilledForm();
        findByFilter();
        bean.setUpdate(Boolean.FALSE);
        return OUTCOME_CIDADE_BASE_SEARCH;
    }

    public void openDeleteModal() {
        bean.setDeleteModalOpen(true);
    }

    public void closeDeleteModal() {
        bean.setDeleteModalOpen(false);
    }

    public String remove() {
        CidadeBase cidadeBase = cidadeBaseService.findCidadeBaseById(bean.getTo().getCodigoCidadeBase());

        try {
            cidadeBaseService.removeCidadeBase(cidadeBase);
        } catch (IntegrityConstraintException ice) {
            Messages.showError("remove", ice.getMessage());
            closeDeleteModal();
            return null;
        }

        closeDeleteModal();

        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_CIDADE_BASE);

        bean.resetTo();
        findByFilter();

        return OUTCOME_CIDADE_BASE_SEARCH;
    }

    public void openInactivateModal() {
        bean.setInactivateModalOpen(true);
    }

    public void confirmInactivateModal() {
        CidadeBase cidadeBase = bean.getTo();
        cidadeBase.toggleIndicadorAtivoBetweenActiveAndInactive();

        try {
            cidadeBaseService.updateCidadeBaseIndicadorAtivo(cidadeBase);
            Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE);
        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage());
        } finally {
            closeInactivateModal();
            restoreFilledForm();
            findByFilter();
        }
    }

    public void closeInactivateModal() {
        bean.setInactivateModalOpen(false);
    }

    public void clearForm() {
        bean.resetFilter();
    }

    private void findById(final Long id) {
        CidadeBase to = cidadeBaseService.findCidadeBaseById(id);
        bean.setTo(to);

        if (to != null) {
            if (to.getMoeda() != null) {
                bean.setMoedaSelected(to.getMoeda().getNomeMoeda());
            }
            if (to.getIndicadorAtivo() != null) {
                bean.setStatusSelected(to.getIndicadorAtivo());
            }
        } else {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }
    private void restoreFilledForm() {
        CidadeBase filter = bean.getFilter();
        bean.resetTo();
        bean.setTo(filter);

        if (filter.getMoeda() != null) {
            bean.setMoedaSelected(filter.getMoeda().getNomeMoeda());
        }
    }
}
