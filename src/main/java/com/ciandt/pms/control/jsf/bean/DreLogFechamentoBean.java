package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.DreLogFechamento;
import com.ciandt.pms.model.vo.ValidacaoPessoaRow;
import com.ciandt.pms.util.TempoConstrucao;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 08/06/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class DreLogFechamentoBean extends Thread implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private DreLogFechamento to = new DreLogFechamento();

    /** representa o mes selecionado. */
    private String monthBeg;

    /** representa o ano selecionado. */
    private String yearBeg;

    /** StringBuffer do log da validação. */
    private String logValidate = new Date()
            + " - Starting the Validation Process... \n";

    /** Lista de resultados da entidade Pessoa. */
    private List<ValidacaoPessoaRow> pessoaRowList = new ArrayList<ValidacaoPessoaRow>();

    /** Indicador / numero da tela a ser exibida. */
    private Integer screenNumber = Integer.valueOf(1);

    /** Indicador se o botao ficara desabilidato. */
    private Boolean btnRegisterDisable = Boolean.valueOf(false);

    /** Indicador do valor da barra de progresso. */
    private float currentValue;

    /** Thread de controle do tempo de construcao. */
    private TempoConstrucao tc;

    /** Indicador do tempo da consulta. */
    private int consultingTime = 0;

    /**
     * @return the tc
     */
    public TempoConstrucao getTc() {
        return tc;
    }

    /**
     * @param tc
     *            the tc to set
     */
    public void setTc(final TempoConstrucao tc) {
        this.tc = tc;
    }

    /**
     * @return the consultingTime
     */
    public int getConsultingTime() {
        return consultingTime;
    }

    /**
     * @param consultingTime
     *            the consultingTime to set
     */
    public void setConsultingTime(final int consultingTime) {
        this.consultingTime = consultingTime;
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
     * @return the screenNumber
     */
    public Integer getScreenNumber() {
        return screenNumber;
    }

    /**
     * @param screenNumber
     *            the screenNumber to set
     */
    public void setScreenNumber(final Integer screenNumber) {
        this.screenNumber = screenNumber;
    }

    /**
     * @return the pessoaRowList
     */
    public List<ValidacaoPessoaRow> getPessoaRowList() {
        return pessoaRowList;
    }

    /**
     * @param pessoaRowList
     *            the pessoaRowList to set
     */
    public void setPessoaRowList(final List<ValidacaoPessoaRow> pessoaRowList) {
        this.pessoaRowList = pessoaRowList;
    }

    /**
     * @return the to
     */
    public DreLogFechamento getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final DreLogFechamento to) {
        this.to = to;
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetValidityDate();
        this.screenNumber = Integer.valueOf(1);
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new DreLogFechamento();
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

    /**
     * @return the logValidate
     */
    public String getLogValidate() {
        return logValidate;
    }

    /**
     * @param logValidate
     *            the logValidate to set
     */
    public void setLogValidate(final String logValidate) {
        this.logValidate += (new Date() + " - Validating - " + logValidate + "\n");
    }

    /**
     * @param btnRegisterDisable
     *            the btnRegisterDisable to set
     */
    public void setBtnRegisterDisable(final Boolean btnRegisterDisable) {
        this.btnRegisterDisable = btnRegisterDisable;
    }

    /**
     * @return the btnRegisterDisable
     */
    public Boolean getBtnRegisterDisable() {
        return btnRegisterDisable;
    }

}