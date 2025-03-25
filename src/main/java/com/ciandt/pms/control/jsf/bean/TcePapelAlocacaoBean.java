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
import com.ciandt.pms.model.TcePapelAlocacao;


/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 28/01/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TcePapelAlocacaoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;
    
    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** to do backingBean. */
    private TcePapelAlocacao to = new TcePapelAlocacao();

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Indicador do padrão para exibição de valores de moeda. */
    private String patternCurrency = "";
    
    /** Lista dos possiveis valores de meses. */
    private List<String> validityMonthList = Arrays.asList("01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12");

    /** Lista dos possiveis valores de anos. */
    private List<String> validityYearList = new ArrayList<String>();

    /** Mes vigencia - inicio. */
    private String validityMonthBeg = null;

    /** Ano vigencia - inicio. */
    private String validityYearBeg = null;
    
    /**
     * @return the validityMonthBeg
     */
    public String getValidityMonthBeg() {
        return validityMonthBeg;
    }

    /**
     * @param validityMonthBeg the validityMonthBeg to set
     */
    public void setValidityMonthBeg(final String validityMonthBeg) {
        this.validityMonthBeg = validityMonthBeg;
    }

    /**
     * @return the validityYearBeg
     */
    public String getValidityYearBeg() {
        return validityYearBeg;
    }

    /**
     * @param validityYearBeg the validityYearBeg to set
     */
    public void setValidityYearBeg(final String validityYearBeg) {
        this.validityYearBeg = validityYearBeg;
    }

    /**
     * @return the validityMonthList
     */
    public List<String> getValidityMonthList() {
        return validityMonthList;
    }

    /**
     * @return lista de anos da vigencia
     */
    public List<String> getValidityYearList() {

        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yearList.add("" + i);
        }

        validityYearList = yearList;

        return validityYearList;
    }

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
     * @return the to
     */
    public TcePapelAlocacao getTo() {
        if (to == null) {
            to = new TcePapelAlocacao();
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final TcePapelAlocacao to) {
        this.to = to;
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
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetValidityDate();
        this.isUpdate = Boolean.valueOf(false);
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new TcePapelAlocacao();
    }
    
    /**
     * Reseta a data de vigencia.
     */
    public void resetValidityDate() {
        this.validityMonthBeg = "";
        this.validityYearBeg = "";
    }

}