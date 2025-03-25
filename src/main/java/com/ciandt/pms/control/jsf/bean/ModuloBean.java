package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.vo.ModuloRow;


/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 14/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ModuloBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;
    
    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;
    
    /** to do backingBean. */
    private Modulo to = new Modulo();

    /** Lista de ModuloRow. */
    private List<ModuloRow> moduloRowList = new ArrayList<ModuloRow>();
    
    /** Linha do modulo. */
    private ModuloRow row = new ModuloRow();
    
    /** Lista de resultado. */
    private List<Modulo> resultList = new ArrayList<Modulo>();
    
    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Arrays.asList("01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12");

    /** Lista dos possiveis valores de anos. */
    private List<String> yearList = new ArrayList<String>();
    
    /** Mes vigencia. */
    private String month = null;

    /** Ano vigencia. */
    private String year = null;
    
    /**
     * @param to the to to set
     */
    public void setTo(final Modulo to) {
        this.to = to;
    }

    /**
     * @return the to
     */
    public Modulo getTo() {
        return to;
    }

    /**
     * @param monthList the validityMonthList to set
     */
    public void setMonthList(final List<String> monthList) {
        this.monthList = monthList;
    }

    /**
     * @return the validityMonthList
     */
    public List<String> getMonthList() {
        return monthList;
    }

    /**
     * @param yearList the validityYearList to set
     */
    public void setYearList(final List<String> yearList) {
        this.yearList = yearList;
    }

    /**
     * @return the validityYearList
     */
    public List<String> getYearList() {
        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yearList.add("" + i);
        }

        this.yearList = yearList;

        return this.yearList;
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
     * @param resultList the resultList to set
     */
    public void setResultList(final List<Modulo> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the resultList
     */
    public List<Modulo> getResultList() {
        return resultList;
    }

    /**
     * @param moduloRowList the moduloRowlist to set
     */
    public void setModuloRowList(final List<ModuloRow> moduloRowList) {
        this.moduloRowList = moduloRowList;
    }

    /**
     * @return the moduloRowlist
     */
    public List<ModuloRow> getModuloRowList() {
        return moduloRowList;
    }

    /**
     * @param row the row to set
     */
    public void setRow(final ModuloRow row) {
        this.row = row;
    }

    /**
     * @return the row
     */
    public ModuloRow getRow() {
        return row;
    }
}
