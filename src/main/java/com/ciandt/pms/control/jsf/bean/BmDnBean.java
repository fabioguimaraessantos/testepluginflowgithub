package com.ciandt.pms.control.jsf.bean;


import com.ciandt.pms.Constants;
import com.ciandt.pms.model.BmDn;
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
public class BmDnBean implements Serializable {

    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Entity From Backing Bean
     **/
    private BmDn bmDn;

    /**
     * FILTER From Backing Bean
     **/
    private BmDn filter;

    /**
     * LIST Entity From Backing Bean To Search
     **/
    private List<BmDn> listBmDn;

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
     * LIST of MSAs from a specific industry type.
     */
    private List<Msa> msas;

    /**
     * Flag that indicates if a specific Industry Type has Msas
     */
    private boolean msasAssociated;

    /**
     * Flag that indicates if a status can be updated
     */
    private boolean updateStatus;

    /**
     *
     */
    public void reset() {
        this.setListBmDn(Collections.EMPTY_LIST);
        this.setMsas(Collections.EMPTY_LIST);
        this.filter = new BmDn();
        this.bmDn = new BmDn();
        this.isUpdate = Boolean.FALSE;
        this.msasAssociated = Boolean.FALSE;
    }

    public void resetForm() {
        this.bmDn = new BmDn();
        this.updateStatus = Boolean.FALSE;
    }

    /* Getters and Setters */
    public BmDn getBmDn() {
        return bmDn;
    }

    public void setBmDn(BmDn bmDn) {
        this.bmDn = bmDn;
    }

    public List<BmDn> getListBmDn() {
        return listBmDn;
    }

    public void setListBmDn(List<BmDn> listBmDn) {
        this.listBmDn = listBmDn;
    }

    public Integer getCurrentPageId() {
        return currentPageId;
    }

    public void setCurrentPageId(Integer currentPageId) {
        this.currentPageId = currentPageId;
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

    public BmDn getFilter() {
        return filter;
    }

    public void setFilter(BmDn filter) {
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

    public boolean isUpdateStatus() {

        if (this.getBmDn().getInActive() == Constants.ACTIVE &&
                this.isUpdate && this.msasAssociated)
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

}