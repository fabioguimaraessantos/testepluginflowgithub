package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.Modulo;

import java.io.Serializable;


/**
 * 
 * A classe MapDashboard representa a linha da lista de modulos.
 *
 * @since 19/10/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie do Nascimento</a>
 *
 */
public class ModuloRow implements Serializable {

    /** Default serial Version UID. */
    private static final long serialVersionUID = 1L;
    
    /** Instancia do Modulo. */
    private Modulo modulo;
    
    /** Mes. */
    private String month;
    
    /** Ano. */
    private String year;
    
    /**
     * @return the modulo
     */
    public Modulo getModulo() {
        return modulo;
    }

    /**
     * @param modulo the modulo to set
     */
    public void setModulo(final Modulo modulo) {
        this.modulo = modulo;
    }

    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(final String month) {
        this.month = month;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(final String year) {
        this.year = year;
    }
    
}
