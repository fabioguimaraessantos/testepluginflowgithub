package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.Empresa;

/**
 * Define o BackingBean da entidade.
 * 
 * @since 15/09/2010
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class EmpresaBean implements Serializable {
    
    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private Empresa to = new Empresa();

    /** filter do backingBean. */
    private Empresa filter = new Empresa();

    /** lista de resultados da pesquisa. */
    private List<Empresa> resultList = null;

    /** Lista para o combobox com as Empresas pais. */
    private List<String> empresaPaiList = new ArrayList<String>();

    /** Lista para o combobox com as Empresas pais. */
    private Map<String, Long> empresaPaiMap = new HashMap<String, Long>();

    /** Valor selecionado no combo de Cliente pai. */
    private String empresaPaiSelected = null;

    /** Valor do filtro selecionado no combo de Empresa pai. */
    private String empresaPaiSelectedFilter = null;

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0L);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Lista para o combobox com as sugestoes - indicadores Ativo/Inativo. */
    private List<String> suggestionsListInAtivo = new ArrayList<String>();

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Lista para o combobox com as Empresas pais. */
    private List<String> moedaList = new ArrayList<String>();

    /** Lista para o combobox com as Empresas pais. */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();

    /** Valor selecionado no combo de Cliente pai. */
    private String moedaSelected = null;

    private String empresaPaiSelectedAnterior = null;

    private Boolean isParentFieldsFulfilled = Boolean.FALSE;

    private Boolean isBranchFieldsFulfilled = Boolean.FALSE;

    /**
     * @return the to
     */
    public Empresa getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(final Empresa to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public Empresa getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(final Empresa filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<Empresa> getResultList() {
        return resultList;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(final List<Empresa> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the empresaPaiList
     */
    public List<String> getEmpresaPaiList() {
        return empresaPaiList;
    }

    /**
     * @param empresaPaiList the empresaPaiList to set
     */
    public void setEmpresaPaiList(final List<String> empresaPaiList) {
        this.empresaPaiList = empresaPaiList;
    }

    /**
     * @return the empresaPaiMap
     */
    public Map<String, Long> getEmpresaPaiMap() {
        return empresaPaiMap;
    }

    /**
     * @param empresaPaiMap the empresaPaiMap to set
     */
    public void setEmpresaPaiMap(final Map<String, Long> empresaPaiMap) {
        this.empresaPaiMap = empresaPaiMap;
    }

    /**
     * @return the empresaPaiSelected
     */
    public String getEmpresaPaiSelected() {
        return empresaPaiSelected;
    }

    /**
     * @param empresaPaiSelected the empresaPaiSelected to set
     */
    public void setEmpresaPaiSelected(final String empresaPaiSelected) {
        this.empresaPaiSelected = empresaPaiSelected;
    }

    /**
     * @return the empresaPaiSelectedFilter
     */
    public String getEmpresaPaiSelectedFilter() {
        return empresaPaiSelectedFilter;
    }

    /**
     * @param empresaPaiSelectedFilter the empresaPaiSelectedFilter to set
     */
    public void setEmpresaPaiSelectedFilter(final String empresaPaiSelectedFilter) {
        this.empresaPaiSelectedFilter = empresaPaiSelectedFilter;
    }

    /**
     * @return the currentRowId
     */
    public Long getCurrentRowId() {
        return currentRowId;
    }

    /**
     * @param currentRowId the currentRowId to set
     */
    public void setCurrentRowId(final Long currentRowId) {
        this.currentRowId = currentRowId;
    }

    /**
     * @return the currentPageId
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @param currentPageId the currentPageId to set
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the suggestionsListInAtivo
     */
    public List<String> getSuggestionsListInAtivo() {
        return suggestionsListInAtivo;
    }

    /**
     * @param suggestionsListInAtivo the suggestionsListInAtivo to set
     */
    public void setSuggestionsListInAtivo(final List<String> suggestionsListInAtivo) {
        this.suggestionsListInAtivo = suggestionsListInAtivo;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @param isUpdate the isUpdate to set
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public List<String> getMoedaList() {
        return moedaList;
    }

    public void setMoedaList(List<String> moedaList) {
        this.moedaList = moedaList;
    }

    public Map<String, Long> getMoedaMap() {
        return moedaMap;
    }

    public void setMoedaMap(Map<String, Long> moedaMap) {
        this.moedaMap = moedaMap;
    }

    public String getMoedaSelected() {
        return moedaSelected;
    }

    public void setMoedaSelected(String moedaSelected) {
        this.moedaSelected = moedaSelected;
    }

    public String getEmpresaPaiSelectedAnterior() {
        return empresaPaiSelectedAnterior;
    }

    public void setEmpresaPaiSelectedAnterior(String empresaPaiSelectedAnterior) {
        this.empresaPaiSelectedAnterior = empresaPaiSelectedAnterior;
    }

    public Boolean getIsParentFieldsFulfilled() {
        return isParentFieldsFulfilled;
    }

    public void setIsParentFieldsFulfilled(Boolean isParentFieldsFulfilled) {
        this.isParentFieldsFulfilled = isParentFieldsFulfilled;
    }

    public Boolean getIsBranchFieldsFulfilled() {
        return isBranchFieldsFulfilled;
    }

    public void setIsBranchFieldsFulfilled(Boolean isBranchFieldsFulfilled) {
        this.isBranchFieldsFulfilled = isBranchFieldsFulfilled;
    }

    public void prepareParentFieldsFulfilled() {
        isParentFieldsFulfilled = Boolean.valueOf(getTo().getCodigoEmpresaERP() != null
                || getTo().getCodigoErpProjetoPadrao() != null);
    }

    public void prepareBranchFieldsFulfilled() {
        isBranchFieldsFulfilled = Boolean.valueOf(getTo().getCodigoErpFilial() != null
                || getTo().getCodigoErpCcusto() != null
                || getTo().getCodigoErpProjIde() != null
                || getTo().getIndicadorExibicaoMsaContrato() != null);
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
        resetSuggestionsListInAtivo();
        resetBranchAndParentCompany();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new Empresa();
        this.empresaPaiSelected = null;
        this.moedaSelected = null;
        this.isParentFieldsFulfilled = Boolean.FALSE;
        this.isBranchFieldsFulfilled = Boolean.FALSE;

    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new Empresa();
        this.empresaPaiSelectedFilter = null;
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<Empresa>();
    }

    /**
     * Reseta a lista de sugestoes - indicadores Ativo/Inativo.
     */
    public void resetSuggestionsListInAtivo() {
        this.suggestionsListInAtivo = new ArrayList<String>();
    }

    public void resetBranchAndParentCompany() {
        this.isBranchFieldsFulfilled = Boolean.FALSE;
        this.isParentFieldsFulfilled = Boolean.FALSE;
    }
}
