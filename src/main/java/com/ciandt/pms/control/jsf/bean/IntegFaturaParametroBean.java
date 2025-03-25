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
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.model.IntegFaturaParametro;
import com.ciandt.pms.model.TipoServico;


/**
 * 
 * A classe IntegFaturaParametroBean é utilizado como backingBean para a
 * entidade IntegFaturaParametro.
 * 
 * @since 23/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntegFaturaParametroBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private IntegFaturaParametro to = new IntegFaturaParametro();

    /** to antigo, usado para comparacao antes de ser atualizado. */
    private IntegFaturaParametro toOld = new IntegFaturaParametro();

    /** filter do backingBean. */
    private IntegFaturaParametro filter = new IntegFaturaParametro();

    /** lista de resultados da pesquisa. */
    private List<IntegFaturaParametro> resultList =
            new ArrayList<IntegFaturaParametro>();

    /** Lista para o combobox Empresa. */
    private List<String> empresaList = new ArrayList<String>();

    /** Lista para o combobox Empresa. */
    private Map<String, Long> empresaMap = new HashMap<String, Long>();

    /** Lista para o combobox TipoServico. */
    private List<String> tipoServicoList = new ArrayList<String>();

    /** Lista para o combobox TipoServico. */
    private Map<String, Long> tipoServicoMap = new HashMap<String, Long>();

    /** Lista para o combobox FonteReceita. */
    private List<String> fonteReceitaList = new ArrayList<String>();

    /** Lista para o combobox FonteReceita. */
    private Map<String, Long> fonteReceitaMap = new HashMap<String, Long>();

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /**
     * @return the toOld
     */
    public IntegFaturaParametro getToOld() {
        return toOld;
    }

    /**
     * @param toOld
     *            the toOld to set
     */
    public void setToOld(final IntegFaturaParametro toOld) {
        this.toOld = toOld;
    }

    /**
     * @return the tipoServicoList
     */
    public List<String> getTipoServicoList() {
        return tipoServicoList;
    }

    /**
     * @param tipoServicoList
     *            the tipoServicoList to set
     */
    public void setTipoServicoList(final List<String> tipoServicoList) {
        this.tipoServicoList = tipoServicoList;
    }

    /**
     * @return the tipoServicoMap
     */
    public Map<String, Long> getTipoServicoMap() {
        return tipoServicoMap;
    }

    /**
     * @param tipoServicoMap
     *            the tipoServicoMap to set
     */
    public void setTipoServicoMap(final Map<String, Long> tipoServicoMap) {
        this.tipoServicoMap = tipoServicoMap;
    }

    /**
     * @return the fonteReceitaList
     */
    public List<String> getFonteReceitaList() {
        return fonteReceitaList;
    }

    /**
     * @param fonteReceitaList
     *            the fonteReceitaList to set
     */
    public void setFonteReceitaList(final List<String> fonteReceitaList) {
        this.fonteReceitaList = fonteReceitaList;
    }

    /**
     * @return the fonteReceitaMap
     */
    public Map<String, Long> getFonteReceitaMap() {
        return fonteReceitaMap;
    }

    /**
     * @param fonteReceitaMap
     *            the fonteReceitaMap to set
     */
    public void setFonteReceitaMap(final Map<String, Long> fonteReceitaMap) {
        this.fonteReceitaMap = fonteReceitaMap;
    }

    /**
     * @return the empresaList
     */
    public List<String> getEmpresaList() {
        return empresaList;
    }

    /**
     * @param empresaList
     *            the empresaList to set
     */
    public void setEmpresaList(final List<String> empresaList) {
        this.empresaList = empresaList;
    }

    /**
     * @return the empresaMap
     */
    public Map<String, Long> getEmpresaMap() {
        return empresaMap;
    }

    /**
     * @param empresaMap
     *            the empresaMap to set
     */
    public void setEmpresaMap(final Map<String, Long> empresaMap) {
        this.empresaMap = empresaMap;
    }

    /**
     * @return the resultList
     */
    public List<IntegFaturaParametro> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<IntegFaturaParametro> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @param isUpdate
     *            the isUpdate to set
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final IntegFaturaParametro to) {
        this.to = to;
    }

    /**
     * 
     * @return retorna o TO
     */
    public IntegFaturaParametro getTo() {
        // Verifica se as entidades de relacionamento sao nulas,
        // se verdade instancia as novas entidades
        // Isto é necessario na criacao de uma entidade, pois as referencias
        // não podem ser nulas
        if (to != null) {
            if (to.getEmpresa() == null) {
                to.setEmpresa(new Empresa());
            }
            if (to.getTipoServico() == null) {
                to.setTipoServico(new TipoServico());
            }
            if (to.getFonteReceita() == null) {
                to.setFonteReceita(new FonteReceita());
            }
        }
        return to;
    }

    /**
     * @return the filter
     */
    public IntegFaturaParametro getFilter() {
        // Verifica se as entidades de relacionamento sao nulas,
        // se verdade instancia as novas entidades
        // Isto é necessario na criacao de uma entidade, pois as referencias
        // não podem ser nulas
        if (filter != null) {
            if (filter.getEmpresa() == null) {
                filter.setEmpresa(new Empresa());
            }
            if (filter.getTipoServico() == null) {
                filter.setTipoServico(new TipoServico());
            }
            if (filter.getFonteReceita() == null) {
                filter.setFonteReceita(new FonteReceita());
            }
        }

        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final IntegFaturaParametro filter) {
        this.filter = filter;
    }

    /**
     * Reseta o backinBean.
     */
    public void resetFilter() {
        this.filter = new IntegFaturaParametro();
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
        this.isUpdate = Boolean.valueOf(false);
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new IntegFaturaParametro();
        this.toOld = new IntegFaturaParametro();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<IntegFaturaParametro>();
    }

    /**
     * @return the currentPageId
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @param currentPageId
     *            the currentPageId to set
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the currentRowId
     */
    public Long getCurrentRowId() {
        return currentRowId;
    }

    /**
     * @param currentRowId
     *            the currentRowId to set
     */
    public void setCurrentRowId(final Long currentRowId) {
        this.currentRowId = currentRowId;
    }

}