/**
 * CentroLucroBean - BackingBean do Centrolucro
 */
package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.NaturezaCentroLucro;


/**
 * 
 * A classe CentroLucroBean proporciona as funcionalidades de backingBean para
 * para o CentroLucro.
 * 
 * @since 04/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CentroLucroBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private CentroLucro to = new CentroLucro();

    /** filter do backingBean. */
    private CentroLucro filter = new CentroLucro();

    /** lista de resultados da pesquisa. */
    private List<CentroLucro> resultList = new ArrayList<CentroLucro>();

    /** lista de Centro Lucro. */
    private List<CentroLucro> centroLucroList = new ArrayList<CentroLucro>();

    /** Lista para o combobox com as NaturezaCentroLucro. */
    private List<String> naturezaCentroLucroList = new ArrayList<String>();

    /** Lista para o combobox com as NaturezaCentroLucro. */
    private Map<String, Long> naturezaCentroLucroMap = new HashMap<String, Long>();

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indica se esta atualizando. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /**
     * @return the naturezaCentroLucroList
     */
    public List<String> getNaturezaCentroLucroList() {
        return naturezaCentroLucroList;
    }

    /**
     * @param naturezaCentroLucroList
     *            the naturezaCentroLucroList to set
     */
    public void setNaturezaCentroLucroList(
            final List<String> naturezaCentroLucroList) {
        this.naturezaCentroLucroList = naturezaCentroLucroList;
    }

    /**
     * @return the naturezaCentroLucroMap
     */
    public Map<String, Long> getNaturezaCentroLucroMap() {
        return naturezaCentroLucroMap;
    }

    /**
     * @param naturezaCentroLucroMap
     *            the naturezaCentroLucroMap to set
     */
    public void setNaturezaCentroLucroMap(
            final Map<String, Long> naturezaCentroLucroMap) {
        this.naturezaCentroLucroMap = naturezaCentroLucroMap;
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
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new CentroLucro();
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new CentroLucro();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<CentroLucro>();
    }

    /**
     * @return the to
     */
    public CentroLucro getTo() {
        // Verifica se a natureza ne nula, se verdade instancia uma nova
        // natureza.
        // Isto é necessario na criacao de uma entidade, pois o a referencia da
        // natureza não pode ser nula
        if (to != null && to.getNaturezaCentroLucro() == null) {
            to.setNaturezaCentroLucro(new NaturezaCentroLucro());
        }
        // to.setNaturezaCentroLucro(natureza);
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final CentroLucro to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public CentroLucro getFilter() {
        if (filter != null && filter.getNaturezaCentroLucro() == null) {
            filter.setNaturezaCentroLucro(new NaturezaCentroLucro());
        }
        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final CentroLucro filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<CentroLucro> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<CentroLucro> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the centroLucroList
     */
    public List<CentroLucro> getCentroLucroList() {
        return centroLucroList;
    }

    /**
     * @param centroLucroList
     *            the centroLucroList to set
     */
    public void setCentroLucroList(final List<CentroLucro> centroLucroList) {
        this.centroLucroList = centroLucroList;
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

}