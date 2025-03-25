/*
 * @(#) AlocacaoRow.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.MsaSaldoMoeda;


/**
 * Classe que representa um MsaSaldoMoeda para checkagem.
 * 
 */
public class MsaSaldoMoedaRow implements java.io.Serializable {

    /** Valor do serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Entidade Fatura que a linha representa. */
    private MsaSaldoMoeda msaSalMoe = new MsaSaldoMoeda();

    /** Indicador input selecionado / nao selecionado. */
    private Boolean isSelected = Boolean.valueOf(false);
    
    /** Indicador checkbox selecionado / nao selecionado. */
    private Boolean isCheckBoxSelected = Boolean.valueOf(false);

    /**
     * default constructor
     */
    public MsaSaldoMoedaRow() {
    }

    /**
     * @return the msaSalMoe
     */
    public MsaSaldoMoeda getMsaSalMoe() {
        return msaSalMoe;
    }

    /**
     * @param msaSalMoe the msaSalMoe to set
     */
    public void setMsaSalMoe(MsaSaldoMoeda msaSalMoe) {
        this.msaSalMoe = msaSalMoe;
    }

    /**
     * @return the isSelected
     */
    public Boolean getIsSelected() {
        return isSelected;
    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

	/**
	 * @return the isCheckBoxSelected
	 */
	public Boolean getIsCheckBoxSelected() {
		return isCheckBoxSelected;
	}

	/**
	 * @param isCheckBoxSelected the isCheckBoxSelected to set
	 */
	public void setIsCheckBoxSelected(Boolean isCheckBoxSelected) {
		this.isCheckBoxSelected = isCheckBoxSelected;
	}
    
    

}