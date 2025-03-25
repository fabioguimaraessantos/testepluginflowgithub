package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.CidadeBase;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CidadeBaseBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // TO is an object to transfer data between layers. In this case it makes a copy of the object from database
    // and uses this object to create, update and remove data
    private CidadeBase to = new CidadeBase();
    private Long currentRowId = 0L;
    private Long currentPageId = 0L;
    private CidadeBase filter = new CidadeBase();
    private List<CidadeBase> resultList = new ArrayList<>();
    private List<String> moedaList = new ArrayList<>();
    private Map<String, Long> moedaMap = new HashMap<>();
    private List<String> statusList = new ArrayList<>();
    private boolean update = false;
    private boolean search = false;
    private String finalCharStatus = null;
    private boolean isDeleteModalOpen = false;
    private boolean isInactivateModalOpen = false;
    private String moedaSelected = null;
    private String moedaFilter = null;
    private String statusSelected = null;

    public void resetTo() {
        this.to = new CidadeBase();
        this.currentRowId = 0L;
        this.update = Boolean.FALSE;
        this.moedaSelected = null;
    }

    public void resetFilter() {
        this.filter = new CidadeBase();
        this.moedaFilter = null;
    }

    public void reset() {
        this.resetTo();
        this.resetFilter();

        this.resultList = new ArrayList<>();
        this.currentPageId = 0L;
        this.moedaList = new ArrayList<>();
        this.statusList = new ArrayList<>();
        this.isDeleteModalOpen = false;
        this.isInactivateModalOpen = false;
    }

    public List<String> getStatusList() {
        if (this.update) {
            this.statusList.clear();
            this.statusList.addAll(Constants.ACTIVE_INACTIVE_VALUES_ONLY);
        }
        else {
            this.statusList.clear();
            this.statusList.addAll(Constants.ALL_ACTIVE_INACTIVE_VALUES);
        }

        return this.statusList;
    }

    public String getFinalCharStatus() {
        if (this.update) {
            this.finalCharStatus = "*";
        }
        else {
            this.finalCharStatus =  "";
        }

        return this.finalCharStatus;
    }
}
