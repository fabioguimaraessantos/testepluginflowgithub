package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.NaturezaCentroLucro;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class NaturezaCentroLucroBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private NaturezaCentroLucro to = new NaturezaCentroLucro();

    /** filter do backingBean. */
    private NaturezaCentroLucro filter = new NaturezaCentroLucro();

    /** lista de resultados da pesquisa. */
    private List<NaturezaCentroLucro> resultList = new ArrayList<NaturezaCentroLucro>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Lista para o combobox com as sugestoes - indicadores Ativo/Inativo. */
    private List<String> suggestionsListInAtivo = new ArrayList<String>();

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

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
     * @return the suggestionsListInAtivo
     */
    public List<String> getSuggestionsListInAtivo() {
        return suggestionsListInAtivo;
    }

    /**
     * @param suggestionsListInAtivo
     *            the suggestionsListInAtivo to set
     */
    public void setSuggestionsListInAtivo(
            final List<String> suggestionsListInAtivo) {
        this.suggestionsListInAtivo = suggestionsListInAtivo;
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

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
        resetSuggestionsListInAtivo();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new NaturezaCentroLucro();
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new NaturezaCentroLucro();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<NaturezaCentroLucro>();
    }

    /**
     * Reseta a lista de sugestoes - indicadores Ativo/Inativo.
     */
    public void resetSuggestionsListInAtivo() {
        this.suggestionsListInAtivo = new ArrayList<String>();
    }

    /**
     * @return the to
     */
    public NaturezaCentroLucro getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final NaturezaCentroLucro to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public NaturezaCentroLucro getFilter() {
        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final NaturezaCentroLucro filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<NaturezaCentroLucro> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<NaturezaCentroLucro> resultList) {
        this.resultList = resultList;
    }

}