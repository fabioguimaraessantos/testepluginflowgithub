package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.model.CostCenterHierarchy;
import com.ciandt.pms.model.GrupoCusto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CostCenterHierarchyBean implements Serializable {

    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Entity From Backing Bean
     **/
    private CostCenterHierarchy costCenterHierarchy;

    /**
     * FILTER From Backing Bean
     **/
    private CostCenterHierarchy filter;

    /**
     * LIST Entity From Backing Bean To Search
     **/
    private List<CostCenterHierarchy> listCostCenterHierarchy;

    /**
     * Id From Current Page In Research.
     */
    private Integer currentPageId = Integer.valueOf(1);

    /**
     * Id From Entity Selected In Research.
     */
    private Long currentRowId = Long.valueOf(0);

    /**
     * Number From Row Selected .
     */
    private Integer rowNumber = Integer.valueOf(0);

    /**
     * List of Values to combobox Active / Inactive.
     */
    private List<String> suggestionsListInAtivo = new ArrayList<String>();

    /**
     * Indicates that Operation is a Create / Update.
     */
    private Boolean isUpdate = Boolean.FALSE;

    /**
     * Flag that indicates if a status can be updated
     */
    private boolean updateStatus;

    /**
     * LIST of Cost Centers associated to a specific Cost Center Hierarchy.
     */
    private List<GrupoCusto> costCenters;

    /**
     * Flag that indicates if a specific Cost Center Hierarchy has Cost Centers associated
     */
    private boolean hasAssociatedCostCenters;
    /**
     *
     */
    public void reset() {
        this.costCenters = Collections.emptyList();
        this.setListCostCenterHierarchy(Collections.EMPTY_LIST);
        this.filter = new CostCenterHierarchy();
        this.costCenterHierarchy = new CostCenterHierarchy();
        this.isUpdate = Boolean.FALSE;
        this.hasAssociatedCostCenters = Boolean.FALSE;
    }

    public void resetForm() {
        this.costCenterHierarchy = new CostCenterHierarchy();
        this.updateStatus = Boolean.FALSE;
    }

    /* Getters and Setters */

    public CostCenterHierarchy getCostCenterHierarchy() {
        return this.costCenterHierarchy;
    }

    public void setCostCenterHierarchy(CostCenterHierarchy costCenterHierarchy) {
        this.costCenterHierarchy = costCenterHierarchy;
    }

    public CostCenterHierarchy getFilter() {
        return this.filter;
    }

    public void setFilter(CostCenterHierarchy filter) {
        this.filter = filter;
    }

    public List<CostCenterHierarchy> getListCostCenterHierarchy() {
        return this.listCostCenterHierarchy;
    }

    public void setListCostCenterHierarchy(List<CostCenterHierarchy> listCostCenterHierarchy) {
        this.listCostCenterHierarchy = listCostCenterHierarchy;
    }

    public Integer getCurrentPageId() {
        return this.currentPageId;
    }

    public void setCurrentPageId(Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    public Long getCurrentRowId() {
        return this.currentRowId;
    }

    public void setCurrentRowId(Long currentRowId) {
        this.currentRowId = currentRowId;
    }

    public Integer getRowNumber() {
        return this.rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public List<String> getSuggestionsListInAtivo() {
        return this.suggestionsListInAtivo;
    }

    public void setSuggestionsListInAtivo(List<String> suggestionsListInAtivo) {
        this.suggestionsListInAtivo = suggestionsListInAtivo;
    }

    public Boolean getIsUpdate() {
        return this.isUpdate;
    }

    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public List<GrupoCusto> getCostCenters() {
        return this.costCenters;
    }

    public void setCostCenters(List<GrupoCusto> costCenters) {
        this.costCenters = costCenters;
    }

    public boolean getHasAssociatedCostCenters() {
        return this.hasAssociatedCostCenters;
    }

    public void setHasAssociatedCostCenters(boolean hasAssociatedCostCenters) {
        this.hasAssociatedCostCenters = hasAssociatedCostCenters;
    }
}