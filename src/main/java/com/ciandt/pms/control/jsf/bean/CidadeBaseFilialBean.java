package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CidadeBaseFilial;
import com.ciandt.pms.model.CidadeBaseFilialId;
import com.ciandt.pms.model.Empresa;
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
public class CidadeBaseFilialBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private CidadeBaseFilial to = new CidadeBaseFilial();
    private CidadeBaseFilialId currentRowId = new CidadeBaseFilialId();
    private Long currentPageId = 1L;
    private CidadeBaseFilial filter = new CidadeBaseFilial();
    private List<CidadeBaseFilial> resultList = new ArrayList<>();

    private boolean update = false;
    private boolean isComboDisabled = true;
    private boolean isEditModalOpen = false;
    private boolean isDeleteModalOpen = false;

    private List<String> cidadeBaseList = new ArrayList<>();
    private Map<String, CidadeBase> cidadeBaseMap = new HashMap<>();

    private List<String> empresaList = new ArrayList<>();
    private Map<String, Empresa> empresaMap = new HashMap<>();

    private List<String> empresaFilialList = new ArrayList<>();
    private Map<String, Empresa> empresaFilialMap = new HashMap<>();

    private List<String> empresaMatrizList = new ArrayList<>();
    private Map<String, Empresa> empresaMatrizMap = new HashMap<>();

    private String cidadeBaseSelected = null;
    private String empresaSelected = null;
    private String empresaFilialSelected = null;
    private String empresaMatrizSelected = null;

    public void reset() {
        this.resetTo();
        this.resetFilter();

        this.resultList = new ArrayList<>();
        this.currentPageId = 1L;

        this.cidadeBaseList = new ArrayList<>();
        this.empresaList = new ArrayList<>();
        this.empresaFilialList = new ArrayList<>();
        this.empresaMatrizList = new ArrayList<>();

        this.update = false;
        this.isComboDisabled = true;
        this.isEditModalOpen = false;
        this.isDeleteModalOpen = false;
    }

    public void resetTo() {
        this.to = new CidadeBaseFilial();
        this.currentRowId = new CidadeBaseFilialId();
    }

    public void resetFilter() {
        this.filter = new CidadeBaseFilial();
        this.cidadeBaseSelected = null;
        this.empresaSelected = null;
        this.empresaFilialSelected = null;
        this.empresaMatrizSelected = null;
    }
}
