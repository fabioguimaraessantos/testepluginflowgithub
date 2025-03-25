package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.IndustryType;
import com.ciandt.pms.model.Msa;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IndustryTypeBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** DTO From Backing Bean **/
    private IndustryType industryType;

    /** FILTER From Backing Bean **/
    private IndustryType filter;

    /** LIST DTO From Backing Bean To Search **/
    private List<IndustryType> listIndustryType;

    /** Id From Entity Selected In Research. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Number From Row Selected . */
    private Integer rowNumber = Integer.valueOf(0);

    /** List of Values to combobox Active / Inactive. */
    private List<String> suggestionsListInAtivo = new ArrayList<String>();

    /** Indicates that Operation is a Create / Update. */
    private Boolean isUpdate = Boolean.FALSE;

    /** LIST of MSAs from a specific industry type. */
    private List<Msa> msas;

    /** Flag that indicates if a specific Industry Type has Msas */
    private boolean msasAssociated;

    /** Flag that indicates if a status can be updated */
    private boolean updateStatus;

    /**
     *
     */
    public void reset() {
        this.setListIndustryType(Collections.EMPTY_LIST);
        this.setMsas(Collections.EMPTY_LIST);
        this.filter = new IndustryType();
        this.isUpdate = Boolean.FALSE;
        this.msasAssociated = Boolean.FALSE;
        resetForm();
    }

    public void resetForm() {
        this.industryType = new IndustryType();
        this.updateStatus = Boolean.FALSE;
    }

    /* Getters and Setters */
    public IndustryType getIndustryType() {
        return industryType;
    }
    public void setIndustryType(IndustryType industryType) {
        this.industryType = industryType;
    }
    public List<IndustryType> getListIndustryType() {
        return listIndustryType;
    }
    public void setListIndustryType(List<IndustryType> listIndustryType) {
        this.listIndustryType = listIndustryType;
    }
    public Long getCurrentRowId() {
        return currentRowId;
    }
    public void setCurrentRowId(Long currentRowId) {
        this.currentRowId = currentRowId;
    }
    public Integer getRowNumber() {
        return rowNumber;
    }
    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }
    public IndustryType getFilter() {
        return filter;
    }
    public void setFilter(IndustryType filter) {
        this.filter = filter;
    }
    public List<String> getSuggestionsListInAtivo() {
        return suggestionsListInAtivo;
    }
    public void setSuggestionsListInAtivo(List<String> suggestionsListInAtivo) {
        this.suggestionsListInAtivo = suggestionsListInAtivo;
    }
    public Boolean getUpdate() {
        return isUpdate;
    }
    public void setUpdate(Boolean update) {
        isUpdate = update;
    }
    public List<Msa> getMsas() {
        return msas;
    }
    public void setMsas(List<Msa> msas) {
        this.msas = msas;
    }
    public boolean isMsasAssociated() {
        return msasAssociated;
    }
    public void setMsasAssociated(boolean msasAssociated) {
        this.msasAssociated = msasAssociated;
    }
    public Integer getCurrentPageId() {
        return currentPageId;
    }
    public void setCurrentPageId(Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    public boolean isUpdateStatus() {

        if(this.getIndustryType().getInActive() == Constants.ACTIVE &&
            this.isUpdate && this.msasAssociated)
            return Boolean.FALSE;

        return Boolean.TRUE;
    }
}