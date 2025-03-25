package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.HedgeMoedaMes;
import com.ciandt.pms.model.Moeda;


/**
 * 
 * A classe HedgeBean é utilizado como backingBean para a entidade Hedge.
 * 
 * @since 03/08/2010
 * @author <a href="mailto:alisson@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class HedgeBean implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /** Instancia do TO. */
    private HedgeMoedaMes to = new HedgeMoedaMes();

    /** Instancia do Filtro. */
    private HedgeMoedaMes filter = new HedgeMoedaMes();
    
    /** Lista de resultados da busca. */
    private List<HedgeMoedaMes> resultList = new ArrayList<HedgeMoedaMes>();
    
    /** Mês selecionado. */
    private String month;
    
    /** Ano selecionado. */
    private String year;
    
    /** Lista para o combobox Moeda. */
    private List<String> moedaList = new ArrayList<String>();

    /** Lista para o combobox Moeda. */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();
    
    /** Página corrente. */
    private int currentPage = 0;
    
    /**
     * @param to the to to set
     */
    public void setTo(final HedgeMoedaMes to) {
        this.to = to;
    }

    /**
     * @return the TO
     */
    public HedgeMoedaMes getTo() {
        if (to == null) {
            to = new HedgeMoedaMes();
        }
        if (to.getMoeda() == null) {
            to.setMoeda(new Moeda());
        }
        
        return to;
    }
    
    /**
     * Reseta o bean.
     */
    public void reset() {
        resetTo();
        month = null;
        year = null;
    }
    
    /**
     * Reseta o TO.
     */
    public void resetTo() {
        to = new HedgeMoedaMes();
    }
    
    /**
     * Reseta o filter.
     */
    public void resultFilter() {
        filter = new HedgeMoedaMes();
        resultList = new ArrayList<HedgeMoedaMes>();
    }

    /**
     * @param month the month to set
     */
    public void setMonth(final String month) {
        this.month = month;
    }

    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param year the year to set
     */
    public void setYear(final String year) {
        this.year = year;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param moedaList the moedaList to set
     */
    public void setMoedaList(final List<String> moedaList) {
        this.moedaList = moedaList;
    }

    /**
     * @return the moedaList
     */
    public List<String> getMoedaList() {
        return moedaList;
    }

    /**
     * @param moedaMap the moedaMap to set
     */
    public void setMoedaMap(final Map<String, Long> moedaMap) {
        this.moedaMap = moedaMap;
    }

    /**
     * @return the moedaMap
     */
    public Map<String, Long> getMoedaMap() {
        return moedaMap;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(final HedgeMoedaMes filter) {
        this.filter = filter;
    }

    /**
     * @return the filter
     */
    public HedgeMoedaMes getFilter() {
        if (filter == null) {
            filter = new HedgeMoedaMes();
        }
        if (filter.getMoeda() == null) {
            filter.setMoeda(new Moeda());
        }
        
        return filter;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(final List<HedgeMoedaMes> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the resultList
     */
    public List<HedgeMoedaMes> getResultList() {
        return resultList;
    }
}
