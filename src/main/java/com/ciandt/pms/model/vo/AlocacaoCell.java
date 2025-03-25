/*
 * @(#) AlocacaoCell.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.AlocacaoPeriodo;

/**
 * Classe que representa a celula da matriz de Alocacao.
 * 
 */
public class AlocacaoCell implements java.io.Serializable {

    /** Valor do serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Index da celula. */
    private Integer cellId = Integer.valueOf(0);

    /** Entidade AlocacaoPeriodo que a celula representa. */
    private AlocacaoPeriodo alocacaoPeriodo = new AlocacaoPeriodo();

    /** Indicador habilitado/desabilitado. */
    private Boolean rendered = Boolean.valueOf(true);
    
    /** Total alocacao do recurso no mes.  */
    private Float totalAlocacaoMes = Float.valueOf(1);

    /** Percentual alocavel de um recurso.  */
    private Float percentualAlocavel = Float.valueOf(1);
    
    /**
     * Flag que indica se deve indicar a mensagem de que o recurso foi admitido
     * depois da data do Periodo Alocacao.
     */
    private Boolean showMessageError = Boolean.valueOf(false);
    
    /** 
     * Flag que indica Sub/Super alocacação. Possiveis valores:
     * "-1" - para Sub Alocados
     *  "0" - para OK 
     *  "1" - para super alocado
     * */
    private int flagIndicadorAlocacao = 0;
    
    /** Flag que indica se a celula foi alterada. */
    private Boolean wasChanged = Boolean.TRUE;
    
    /** Flag para indicar se o recurso esta ou nao ativo no mes. */
    private Boolean flagIndicadorAtivo = Boolean.FALSE;
    
    /**
     * Construtor padrão.
     */
    public AlocacaoCell() {
        
    }
    
    /**
     * Construtor padrao.
     * 
     * @param alocacaoPeriodo
     *            - AlocacaoPeriodo da célula corrente da matriz
     * @param cellId
     *            - id da célula da matriz
     */
    public AlocacaoCell(final AlocacaoPeriodo alocacaoPeriodo,
            final Integer cellId) {
        this.alocacaoPeriodo = alocacaoPeriodo;
        this.cellId = cellId;
    }

    /**
     * @return the cellId
     */
    public Integer getCellId() {
        return cellId;
    }

    /**
     * @param cellId
     *            the cellId to set
     */
    public void setCellId(final Integer cellId) {
        this.cellId = cellId;
    }

    /**
     * @return the alocacaoPeriodo
     */
    public AlocacaoPeriodo getAlocacaoPeriodo() {
        return alocacaoPeriodo;
    }

    /**
     * @param alocacaoPeriodo
     *            the alocacaoPeriodo to set
     */
    public void setAlocacaoPeriodo(final AlocacaoPeriodo alocacaoPeriodo) {
        this.alocacaoPeriodo = alocacaoPeriodo;
    }

    /**
     * @return the rendered
     */
    public Boolean getRendered() {
        return rendered;
    }

    /**
     * @param rendered
     *            the rendered to set
     */
    public void setRendered(final Boolean rendered) {
        this.rendered = rendered;
    }

    /**
     * @param totalAlocacaoMes the totalAlocacaoMes to set
     */
    public void setTotalAlocacaoMes(final Float totalAlocacaoMes) {
        this.totalAlocacaoMes = totalAlocacaoMes;
    }

    /**
     * @return the totalAlocacaoMes
     */
    public Float getTotalAlocacaoMes() {
        return totalAlocacaoMes;
    }

    /**
     * @param percentualAlocavel the percentualAlocavel to set
     */
    public void setPercentualAlocavel(final Float percentualAlocavel) {
        this.percentualAlocavel = percentualAlocavel;
    }

    /**
     * @return the percentualAlocavel
     */
    public Float getPercentualAlocavel() {
        return percentualAlocavel;
    }

    /**
     * @param flagIndicadorAlocacao the flagIndicadorAlocacao to set
     */
    public void setFlagIndicadorAlocacao(final int flagIndicadorAlocacao) {
        this.flagIndicadorAlocacao = flagIndicadorAlocacao;
    }

    /**
     * @return the flagIndicadorAlocacao
     */
    public int getFlagIndicadorAlocacao() {
        return flagIndicadorAlocacao;
    }

    /**
     * @param wasChanged the wasChanged to set
     */
    public void setWasChanged(final Boolean wasChanged) {
        this.wasChanged = wasChanged;
    }

    /**
     * @return the wasChanged
     */
    public Boolean getWasChanged() {
        return wasChanged;
    }

    /** 
     * @return the showMessageError
     */
    public Boolean getShowMessageError() {
        return showMessageError;
    }

    /**
     * @param showMessageError
     *            the showMessageError to set
     */
    public void setShowMessageError(final Boolean showMessageError) {
        this.showMessageError = showMessageError;
    }

    /**
     * @return the flagIndicadorAtivo
     */
    public Boolean getFlagIndicadorAtivo() {
        return flagIndicadorAtivo;
    }

    /**
     * @param flagIndicadorAtivo the flagIndicadorAtivo to set
     */
    public void setFlagIndicadorAtivo(final Boolean flagIndicadorAtivo) {
        this.flagIndicadorAtivo = flagIndicadorAtivo;
    }
    
    

}