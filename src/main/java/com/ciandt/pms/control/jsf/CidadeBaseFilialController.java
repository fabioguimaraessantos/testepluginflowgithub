package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICidadeBaseFilialService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.control.jsf.bean.CidadeBaseFilialBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CidadeBaseFilial;
import com.ciandt.pms.model.CidadeBaseFilialId;
import com.ciandt.pms.model.Empresa;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.CITY_BASE_CO:VW", "BOF.CITY_BASE_CO:CR", "BOF.CITY_BASE_CO:ED", "BOF.CITY_BASE_CO:DE"})
public class CidadeBaseFilialController {

    private static final Log logger = LogFactory.getLog(CidadeBaseFilialController.class.getName());
    private static final String OUTCOME_CIDADE_BASE_FILIAL_SEARCH = "cidadeBaseFilial_search";
    private static final String OUTCOME_CIDADE_BASE_FILIAL_ADD = "cidadeBaseFilial_add";
    private static final String OUTCOME_CIDADE_BASE_FILIAL_EDIT = "cidadeBaseFilial_edit";

    @Autowired
    private ICidadeBaseFilialService cidadeBaseFilialService;

    @Autowired
    private ICidadeBaseService cidadeBaseService;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private CidadeBaseFilialBean bean;

    public CidadeBaseFilialBean getBean() {
        return bean;
    }

    public void setBean(final CidadeBaseFilialBean bean) {
        this.bean = bean;
    }

    public String prepareSearch() {
        bean.reset();
        loadCidadeBaseListAndEmpresaMatrizListForCombos();
        loadEmpresaListAndEmpresaFilialListForCombosAll();
        return OUTCOME_CIDADE_BASE_FILIAL_SEARCH;
    }

    public void search() {
        findByFilter();
    }

    public String prepareCreate() {
        bean.reset();
        loadCidadeBaseListAndEmpresaMatrizListForCombos();
        return OUTCOME_CIDADE_BASE_FILIAL_ADD;
    }

    public String create() {
        setFilter();
        final CidadeBaseFilial newCidadeBaseFilial = bean.getFilter();

        try {
            cidadeBaseFilialService.create(newCidadeBaseFilial);

        } catch (final DataIntegrityViolationException e) {
            Messages.showError("create", Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS, Constants.ENTITY_NAME_CIDADE_BASE_FILIAL);
            return null;
        }

        Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE, Constants.ENTITY_NAME_CIDADE_BASE_FILIAL);
        bean.resetFilter();

        return OUTCOME_CIDADE_BASE_FILIAL_ADD;
    }

    public String prepareUpdate() {
        final CidadeBaseFilial to = bean.getTo();
        if (to == null) return null;

        if (to.getId() != null && to.getId().getCidadeBase() != null) {
            bean.setCidadeBaseSelected(to.getId().getCidadeBase().getNomeCidadeBase());
        }

        if (to.getId() != null && to.getId().getEmpresa() != null) {
            bean.setEmpresaSelected(to.getId().getEmpresa().getNomeEmpresa());
        }

        if (to.getEmpresaFilial() != null) {
            bean.setEmpresaFilialSelected(to.getEmpresaFilial().getNomeEmpresa());
        }

        if (to.getEmpresaMatriz() != null) {
            bean.setEmpresaMatrizSelected(to.getEmpresaMatriz().getNomeEmpresa());
        }

        loadEmpresaListAndEmpresaFilialListForCombosByEmpresaMatrizSelected();
        bean.setUpdate(true);
        return OUTCOME_CIDADE_BASE_FILIAL_EDIT;
    }

    public void toggleEditModalOpen() {
        if (!bean.isEditModalOpen()) {
            findById();
            setFilter();
        }
        bean.setEditModalOpen(!bean.isEditModalOpen());
    }

    public String update() {
        final CidadeBaseFilial oldCidadeBaseFilial = findById();
        final CidadeBaseFilial newCidadeBaseFilial = bean.getFilter();

        try {
            cidadeBaseFilialService.update(oldCidadeBaseFilial, newCidadeBaseFilial);

        } catch (final DataIntegrityViolationException e) {
            toggleEditModalOpen();
            Messages.showError("update", Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS, Constants.ENTITY_NAME_CIDADE_BASE_FILIAL);
            return null;
        }

        toggleEditModalOpen();
        restoreFilledForm();
        findByFilter();
        bean.setUpdate(false);
        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE, Constants.ENTITY_NAME_CIDADE_BASE_FILIAL);

        return OUTCOME_CIDADE_BASE_FILIAL_SEARCH;
    }

    public String cancelUpdate() {
        bean.resetFilter();
        loadEmpresaListAndEmpresaFilialListForCombosAll();
        bean.setUpdate(false);
        return OUTCOME_CIDADE_BASE_FILIAL_SEARCH;
    }

    public void toggleDeleteModalOpen() {
        if (!bean.isDeleteModalOpen()) findById();
        bean.setDeleteModalOpen(!bean.isDeleteModalOpen());
        bean.setUpdate(false);
    }

    public String remove() {
        final CidadeBaseFilial cidadeBaseFilial = findById();
        if (cidadeBaseFilial == null) return null;

        try {
            cidadeBaseFilialService.remove(cidadeBaseFilial);

        } catch (final DataIntegrityViolationException e) {
            toggleDeleteModalOpen();
            Messages.showError("remove", Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE, Constants.ENTITY_NAME_CIDADE_BASE_FILIAL);
            return null;
        }

        toggleDeleteModalOpen();
        bean.resetTo();
        bean.resetFilter();
        prepareSearch();
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE, Constants.ENTITY_NAME_CIDADE_BASE_FILIAL);

        return OUTCOME_CIDADE_BASE_FILIAL_SEARCH;
    }

    public void clearForm() {
        bean.setCidadeBaseSelected(null);
        bean.setEmpresaSelected(null);
        bean.setEmpresaFilialSelected(null);
        bean.setEmpresaMatrizSelected(null);
        setFilter();

        loadCidadeBaseListAndEmpresaMatrizListForCombos();
    }

    public void clearSearchPage() {
        clearForm();
        loadEmpresaListAndEmpresaFilialListForCombosAll();
    }

    private CidadeBaseFilial findById() {
        final CidadeBaseFilial cidadeBaseFilial = cidadeBaseFilialService.findById(bean.getCurrentRowId());
        if (cidadeBaseFilial == null) {
            Messages.showWarning("findById", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            return null;
        }
        bean.setTo(cidadeBaseFilial);
        return cidadeBaseFilial;
    }

    private void findByFilter() {
        setFilter();

        try {
            bean.setResultList(cidadeBaseFilialService.findByFilter(bean.getFilter()));
            bean.setCurrentPageId(1L);

        } catch (final BusinessException e) {
            bean.setResultList(Collections.emptyList());
            Messages.showWarning("findByFilter", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
            logger.warn(Constants.MSG_NOT_FOUND_CIDADE_BASE_FILIAL);
        }
    }

    private void setFilter() {
         CidadeBase cidadeBase =  new CidadeBase();
         if(bean.getCidadeBaseMap() != null && bean.getCidadeBaseSelected() != null){
             cidadeBase =  bean.getCidadeBaseMap().get(bean.getCidadeBaseSelected());
         }

         Empresa empresa = new Empresa();
         if(bean.getEmpresaSelected() != null){
             empresa = bean.getEmpresaMap().getOrDefault(bean.getEmpresaSelected(),
                     bean.getEmpresaMatrizMap().get(bean.getEmpresaSelected()));
         }

         Empresa filial = new Empresa();
         if(bean.getEmpresaFilialSelected() != null){
             filial = bean.getEmpresaFilialMap().getOrDefault(bean.getEmpresaFilialSelected(),
                     bean.getEmpresaFilialMap().get(bean.getEmpresaFilialSelected()));
         }

         Empresa matriz = new Empresa();
         if(bean.getEmpresaMatrizSelected() != null){
            matriz = bean.getEmpresaMatrizMap().getOrDefault(bean.getEmpresaMatrizSelected(),
                    bean.getEmpresaMatrizMap().get(bean.getEmpresaMatrizSelected()));
         }

        bean.setFilter(new CidadeBaseFilial(new CidadeBaseFilialId(cidadeBase, empresa), filial, matriz));
    }

    private void restoreFilledForm() {
        bean.setTo(bean.getFilter());

        if (bean.getTo() != null) {
            if (bean.getTo().getId() != null && bean.getTo().getId().getCidadeBase() != null) {
                bean.setCidadeBaseSelected(bean.getTo().getId().getCidadeBase().getNomeCidadeBase());
            }

            if (bean.getTo().getId() != null && bean.getTo().getId().getEmpresa() != null) {
                bean.setEmpresaSelected(bean.getTo().getId().getEmpresa().getNomeEmpresa());
            }

            if (bean.getTo().getEmpresaFilial() != null) {
                bean.setEmpresaFilialSelected(bean.getTo().getEmpresaFilial().getNomeEmpresa());
            }

            if (bean.getTo().getEmpresaMatriz() != null) {
                bean.setEmpresaMatrizSelected(bean.getTo().getEmpresaMatriz().getNomeEmpresa());
            }
        }
    }

    private void loadCidadeBaseListAndEmpresaMatrizListForCombos() {
        loadCidadeBaseList(cidadeBaseService.findCidadeBaseAll());
        loadEmpresaMatrizList(empresaService.findEmpresaAllParentCompany());
    }

    private void loadEmpresaListAndEmpresaFilialListForCombosAll() {
        final List<Empresa> filiais = empresaService.findEmpresaAllSubsidiary();
        loadEmpresaList(filiais);
        loadEmpresaFilialList(filiais);
    }

    private void loadEmpresaListAndEmpresaFilialListForCombosByEmpresaMatrizSelected() {
        setFilter();

        final Empresa filterEmpresa = new Empresa();
        if (bean.getEmpresaFilialMap() != null && bean.getEmpresaFilialSelected() != null &&
                  bean.getEmpresaFilialMap().get(bean.getEmpresaFilialSelected()).getIndicadorAtivo().equals("A")) {
            filterEmpresa.setIndicadorAtivo("A");
        }
        filterEmpresa.setEmpresa(bean.getFilter().getEmpresaMatriz());

        final List<Empresa> filiais = empresaService.findEmpresaByFilter(filterEmpresa);
        if (CollectionUtils.isEmpty(filiais)) filiais.addAll(empresaService.findEmpresaAllSubsidiary());

        loadEmpresaList(filiais);
        loadEmpresaFilialList(filiais);
        setFilter();
    }

    public void loadEmpresaListAndEmpresaFilialListForCombosByOnSelectEmpresaMatrizEvent(final ValueChangeEvent event) {
        bean.setEmpresaMatrizSelected(String.valueOf(event.getNewValue()));
        loadEmpresaListAndEmpresaFilialListForCombosByEmpresaMatrizSelected();
        bean.setEmpresaSelected(null);
        bean.setEmpresaFilialSelected(null);
        bean.setComboDisabled(false);

        loadCidadeBaseListAndEmpresaMatrizListForCombos();
    }

    private void loadCidadeBaseList(final List<CidadeBase> cidadesBase) {
        final Map<String, CidadeBase> cidadeBaseMap = new HashMap<>();
        final List<String> cidadeBaseList = new ArrayList<>();

        for (final CidadeBase cidadeBase : cidadesBase) {
            cidadeBaseMap.put(cidadeBase.getNomeCidadeBase(), cidadeBase);
            cidadeBaseList.add(cidadeBase.getNomeCidadeBase());
        }

        bean.setCidadeBaseMap(cidadeBaseMap);
        bean.setCidadeBaseList(cidadeBaseList);
    }

    private void loadEmpresaList(final List<Empresa> empresas) {
        final Map<String, Empresa> empresaMap = new HashMap<>();
        final List<String> empresaList = new ArrayList<>();

        for (final Empresa empresa : empresas) {
            empresaMap.put(empresa.getNomeEmpresa(), empresa);
            empresaList.add(empresa.getNomeEmpresa());
        }

        bean.setEmpresaMap(empresaMap);
        bean.setEmpresaList(empresaList);
    }

    private void loadEmpresaFilialList(final List<Empresa> empresasFilial) {
        final Map<String, Empresa> empresaFilialMap = new HashMap<>();
        final List<String> empresaFilialList = new ArrayList<>();

        for (final Empresa empresaFilial : empresasFilial) {
            empresaFilialMap.put(empresaFilial.getNomeEmpresa(), empresaFilial);
            empresaFilialList.add(empresaFilial.getNomeEmpresa());
        }

        bean.setEmpresaFilialMap(empresaFilialMap);
        bean.setEmpresaFilialList(empresaFilialList);
    }

    private void loadEmpresaMatrizList(final List<Empresa> empresasMatriz) {
        final Map<String, Empresa> empresaMatrizMap = new HashMap<>();
        final List<String> empresaMatrizList = new ArrayList<>();

        for (final Empresa empresaMatriz : empresasMatriz) {
            empresaMatrizMap.put(empresaMatriz.getNomeEmpresa(), empresaMatriz);
            empresaMatrizList.add(empresaMatriz.getNomeEmpresa());
        }

        bean.setEmpresaMatrizMap(empresaMatrizMap);
        bean.setEmpresaMatrizList(empresaMatrizList);
    }
}
