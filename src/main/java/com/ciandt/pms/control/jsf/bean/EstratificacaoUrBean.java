package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.EstratificacaoUr;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 08/06/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class EstratificacaoUrBean extends Thread implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private EstratificacaoUr to = new EstratificacaoUr();

    /** representa o mes selecionado. */
    private String monthBeg;

    /** representa o ano selecionado. */
    private String yearBeg;

    /** Indicador do valor total da barra de progresso. */
    private float totalValue;

    /** Indicador do valor da barra de progresso. */
    private float currentValue;

    /**
     * @return the totalValue
     */
    public float getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue
     *            the totalValue to set
     */
    public void setTotalValue(final float totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return the currentValue
     */
    public float getCurrentValue() {
        return currentValue;
    }

    /**
     * @return the currentValueRounded
     */
    public float getValueRounded() {
        return Math.round(currentValue);
    }

    /**
     * @param currentValue
     *            the currentValue to set
     */
    public void setCurrentValue(final float currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * @return the to
     */
    public EstratificacaoUr getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final EstratificacaoUr to) {
        this.to = to;
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetValidityDate();
        this.currentValue = 0;
        this.totalValue = 0;
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new EstratificacaoUr();
    }

    /**
     * Reseta a data do periodo da vigencia.
     */
    public void resetValidityDate() {
        this.monthBeg = null;
        this.yearBeg = null;
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

}