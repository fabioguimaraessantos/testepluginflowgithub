/*
 * @(#) AlocacaoRow.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import com.ciandt.pms.enums.XeroLineIntegration;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.util.NumberUtil;

import java.math.BigDecimal;


/**
 * Classe que representa a linha da consulta da Fatura.
 * 
 */
public class FaturaRow implements java.io.Serializable {

    /** Valor do serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Index da linha. */
    private Integer rowId = Integer.valueOf(0);

    /** Entidade Fatura que a linha representa. */
    private Fatura fatura = new Fatura();

    /** Mostra os ItemFatura da Fatura corrente. */
    private Boolean showItemFatura = Boolean.valueOf(false);

    /** Soma dos valores dos ItemFatura da Fatura corrente. */
    private BigDecimal totalFatura = BigDecimal.valueOf(0);

    /** Tipo da linha: C (cabecalho), T (total) e TG (total geral). */
    private String lineType = "";

    /** Indicador do padrão para exibição de valores de moeda. */
    private String patternCurrency = "";

    /** Indicador selecionado / nao selecionado. */
    private Boolean isSelected = Boolean.valueOf(false);

    /** Indicador de status da fatura (pago, parcialmente pago, não pago). */
    private String statusPagamento = "";

    /** Status item pagamento. */
    private String statusItemPagamento;

    /** Comissao. */
    private String comissao;

    /** Flag para identificar se a fatura esta inteiramente comissionada. */
    private boolean isCommission = Boolean.valueOf(false);

    private XeroLineIntegration xeroLineIntegration;

    /**
     * @return the statusItemPagamento
     */
    public String getStatusItemPagamento() {
        return statusItemPagamento;
    }

    /**
     * @param statusItemPagamento
     *            the statusItemPagamento to set
     */
    public void setStatusItemPagamento(final String statusItemPagamento) {
        this.statusItemPagamento = statusItemPagamento;
    }

    /**
     * @return the comissao
     */
    public String getComissao() {
        return comissao;
    }

    /**
     * @param comissao
     *            the comissao to set
     */
    public void setComissao(final String comissao) {
        this.comissao = comissao;
    }

    /**
     * @return the statusPagamento
     */
    public String getStatusPagamento() {
        return statusPagamento;
    }

    /**
     * @param statusPagamento
     *            the statusPagamento to set
     */
    public void setStatusPagamento(final String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    /**
     * @return the isSelected
     */
    public Boolean getIsSelected() {
        return isSelected;
    }

    /**
     * @param isSelected
     *            the isSelected to set
     */
    public void setIsSelected(final Boolean isSelected) {
        this.isSelected = isSelected;
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
     * @return the rowId
     */
    public Integer getRowId() {
        return rowId;
    }

    /**
     * @param rowId
     *            the rowId to set
     */
    public void setRowId(final Integer rowId) {
        this.rowId = rowId;
    }

    /**
     * @return the fatura
     */
    public Fatura getFatura() {
        return fatura;
    }

    /**
     * @param fatura
     *            the fatura to set
     */
    public void setFatura(final Fatura fatura) {
        this.fatura = fatura;
    }

    /**
     * @return the showItemFatura
     */
    public Boolean getShowItemFatura() {
        return showItemFatura;
    }

    /**
     * @param showItemFatura
     *            the showItemFatura to set
     */
    public void setShowItemFatura(final Boolean showItemFatura) {
        this.showItemFatura = showItemFatura;
    }

    /**
     * @return the totalFatura
     */
    public BigDecimal getTotalFatura() {
        if (totalFatura != null) {
            return NumberUtil.round(totalFatura);
        }

        return totalFatura;
    }

    /**
     * @param totalFatura
     *            the totalFatura to set
     */
    public void setTotalFatura(final BigDecimal totalFatura) {
        this.totalFatura = totalFatura;
    }

    /**
     * @return the lineType
     */
    public String getLineType() {
        return lineType;
    }

    /**
     * @param lineType
     *            the lineType to set
     */
    public void setLineType(final String lineType) {
        this.lineType = lineType;
    }

    /**
     * Construtor padrao.
     * 
     */
    public FaturaRow() {
    }

    /**
     * Construtor padrao.
     * 
     * @param fatura
     *            - Fatura da linha corrente
     */
    public FaturaRow(final Fatura fatura) {
        this.fatura = fatura;
    }

    /**
     * Construtor padrao.
     * 
     * @param fatura
     *            - Fatura da linha corrente
     * @param rowId
     *            - id da linha da matriz
     */
    public FaturaRow(final Fatura fatura, final Integer rowId) {
        this.fatura = fatura;
        this.rowId = rowId;
    }

    public FaturaRow(final FaturaApagada fa, final Integer rowId) {
		Fatura fatura = new Fatura();
		fatura.setCodigoFatura(fa.getCodigoFatura());
		fatura.setMoeda(fa.getMoeda());
		fatura.setDealFiscal(fa.getDealFiscal());
		fatura.setDataPrevisao(fa.getDataPrevisao());
		fatura.setNumeroDoc(fa.getNumeroDoc());
		fatura.setTextoObservacao(fa.getTextoObservacao());
		fatura.setIndicadorStatus(fa.getIndicadorStatus());
		fatura.setCodigoLoginAe(fa.getCodigoLoginAe());
		fatura.setCodigoErpPedido(fa.getCodigoErpPedido());

		this.fatura = fatura;
		this.rowId = rowId;
	}

    /**
     * @return the isCommission
     */
    public boolean getIsCommission() {
        return isCommission;
    }

    /**
     * @param isCommission
     *            the isCommission to set
     */
    public void setIsCommission(final boolean isCommission) {
        this.isCommission = isCommission;
    }

    public XeroLineIntegration getXeroLineIntegration() {
        return this.xeroLineIntegration;
    }

    public void setXeroLineIntegration(final XeroLineIntegration xeroLineIntegration) {
        this.xeroLineIntegration = xeroLineIntegration;
    }
}