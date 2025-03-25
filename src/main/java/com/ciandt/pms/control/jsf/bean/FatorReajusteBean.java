package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.FatorReajuste;
import com.ciandt.pms.model.TipoContrato;


/**
 * 
 * Define o BackingBean da entidade.
 *
 * @since 25/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class FatorReajusteBean implements Serializable {

    /** Default serail version UID. */
    private static final long serialVersionUID = 1L;
    
    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;
    
    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;

    /** Lista dos possiveis valores de anos. */
    private List<String> yearList = new ArrayList<String>();

    /** Entidade FatorReajuste. */
    private FatorReajuste to = new FatorReajuste();
    
    /** Contém a lista de resultados do filtro de busca. */
    private List<FatorReajuste> resultList = new ArrayList<FatorReajuste>();
    
    /** Map de ID de tipos de contratos. */
    private Map<String, Long> tipoContratoMap = new HashMap<String, Long>();
    
    /** Contém a lista com os nomes dos tipos de contratos, utilizando no combobox. */
    private List<String> tipoContratoList = new ArrayList<String>();
    
    /** Mes selecionado. */
    private String month;
    
    /** Ano selecionado. */
    private String year;
    
    /** Armazena o valor da linha selecionada. */
    private Integer selectedRow; 
    
    /** Reseta o bean. */
    public void reset() {
        resetTo();
        resetFilter();
    }
    
    /** Reseta o TO do bean. */
    public void resetTo() {
        this.to = new FatorReajuste();
        this.month = "";
        this.year = "";
    }
    
    /** Reseta o resultado do filtro. */
    public void resetFilter() {
        this.resultList = new ArrayList<FatorReajuste>();
    }
    
    /**
     * @param monthList the monthList to set
     */
    public void setMonthList(final List<String> monthList) {
        this.monthList = monthList;
    }

    /**
     * @return the monthList
     */
    public List<String> getMonthList() {
        return monthList;
    }
    
    /**
     * @return the yearList
     */
    public List<String> getYearList() {
        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> years = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            years.add("" + i);
        }

        yearList = years;

        return yearList;
    }

    /**
     * @param to the to to set
     */
    public void setTo(final FatorReajuste to) {
        this.to = to;
    }

    /**
     * @return the to
     */
    public FatorReajuste getTo() {
        if (to == null) {
            to = new FatorReajuste();
        }
        if (to.getTipoContrato() == null) {
            to.setTipoContrato(new TipoContrato());
        }
        
        return to;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(final List<FatorReajuste> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the resultList
     */
    public List<FatorReajuste> getResultList() {
        return resultList;
    }

    /**
     * @param tipoContratoMap the tipoContratoMap to set
     */
    public void setTipoContratoMap(final Map<String, Long> tipoContratoMap) {
        this.tipoContratoMap = tipoContratoMap;
    }

    /**
     * @return the tipoContratoMap
     */
    public Map<String, Long> getTipoContratoMap() {
        return tipoContratoMap;
    }

    /**
     * @param tipoContratoList the tipoContratoList to set
     */
    public void setTipoContratoList(final List<String> tipoContratoList) {
        this.tipoContratoList = tipoContratoList;
    }

    /**
     * @return the tipoContratoList
     */
    public List<String> getTipoContratoList() {
        return tipoContratoList;
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
     * @param selectedRow the selectedRow to set
     */
    public void setSelectedRow(final Integer selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     * @return the selectedRow
     */
    public Integer getSelectedRow() {
        return selectedRow;
    }

}
