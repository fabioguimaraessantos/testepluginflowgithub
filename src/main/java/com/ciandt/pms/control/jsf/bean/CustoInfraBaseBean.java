package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CustoInfraBase;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CustoInfraBaseBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private CustoInfraBase to = new CustoInfraBase();

    /** filter do backingBean. */
    private CustoInfraBase filter = new CustoInfraBase();

    /** lista de resultados da pesquisa. */
    private List<CustoInfraBase> resultList = new ArrayList<CustoInfraBase>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Lista para o combobox com os CidadeBase. */
    private List<String> cidadeBaseList = new ArrayList<String>();

    /** Map para o combobox com os CidadeBase. */
    private Map<String, Long> cidadeBaseMap = new HashMap<String, Long>();

    /** representa o mes selecionado. */
    private String monthBeg;

    /** representa o ano selecionado. */
    private String yearBeg;

    /** Indicador do padrão para exibição de valores de moeda. */
    private String patternCurrency = "";
    
    /** Data do HistoryLock. */
    private Date historyLockDate;

    /**
     * @return the patternCurrency
     */
    public String getPatternCurrency() {
        return patternCurrency + " ";
    }

    /**
     * @param patternCurrency
     *            the patternCurrency to set
     */
    public void setPatternCurrency(final String patternCurrency) {
        this.patternCurrency = patternCurrency;
    }

    /**
     * @return the cidadeBaseList
     */
    public List<String> getCidadeBaseList() {
        return cidadeBaseList;
    }

    /**
     * @param cidadeBaseList
     *            the cidadeBaseList to set
     */
    public void setCidadeBaseList(final List<String> cidadeBaseList) {
        this.cidadeBaseList = cidadeBaseList;
    }

    /**
     * @return the cidadeBaseMap
     */
    public Map<String, Long> getCidadeBaseMap() {
        return cidadeBaseMap;
    }

    /**
     * @param cidadeBaseMap
     *            the cidadeBaseMap to set
     */
    public void setCidadeBaseMap(final Map<String, Long> cidadeBaseMap) {
        this.cidadeBaseMap = cidadeBaseMap;
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
        resetValidityDate();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new CustoInfraBase();
        this.monthBeg = "";
        this.yearBeg = "";
        this.patternCurrency = "";
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new CustoInfraBase();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<CustoInfraBase>();
    }

    /**
     * Reseta a data do periodo da vigencia.
     */
    public void resetValidityDate() {
        this.monthBeg = null;
        this.yearBeg = null;
    }

    /**
     * @return the to
     */
    public CustoInfraBase getTo() {
        if (to == null) {
            to = new CustoInfraBase();
        }
        if (to.getCidadeBase() == null) {
            to.setCidadeBase(new CidadeBase());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final CustoInfraBase to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public CustoInfraBase getFilter() {
        if (filter == null) {
            filter = new CustoInfraBase();
        }
        if (filter.getCidadeBase() == null) {
            filter.setCidadeBase(new CidadeBase());
        }

        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final CustoInfraBase filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<CustoInfraBase> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<CustoInfraBase> resultList) {
        this.resultList = resultList;
    }

    /**
     * @param monthBeg
     *            the monthBeg to set
     */
    public void setMonthBeg(final String monthBeg) {
        this.monthBeg = monthBeg;
    }

    /**
     * @return the monthBeg
     */
    public String getMonthBeg() {
        return monthBeg;
    }

    /**
     * @param yearBeg
     *            the yearBeg to set
     */
    public void setYearBeg(final String yearBeg) {
        this.yearBeg = yearBeg;
    }

    /**
     * @return the yearBeg
     */
    public String getYearBeg() {
        return yearBeg;
    }

    /**
     * @return the historyLockDate
     */
    public Date getHistoryLockDate() {
        return historyLockDate;
    }

    /**
     * @param historyLockDate the historyLockDate to set
     */
    public void setHistoryLockDate(final Date historyLockDate) {
        this.historyLockDate = historyLockDate;
    }

}