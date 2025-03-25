/*
 * @(#) AlocacaoFotoCell.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe que representa a celula da matriz de AlocacaoFoto.
 * 
 */
public class AlocacaoFotoCell implements java.io.Serializable {

    /** Valor do serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Atributo gerado a partir da coluna SNAP_DT_ALOC. */
    private Date dataAloc;

    /** Atributo gerado a partir da coluna SNAP_PR_ALOC_PV. */
    private BigDecimal percentualAlocPv;

    /** Atributo gerado a partir da coluna SNAP_PR_ALOC_CR. */
    private BigDecimal percentualAlocCr;

    /**
     * @return the dataAloc
     */
    public Date getDataAloc() {
        return dataAloc;
    }

    /**
     * @param dataAloc
     *            the dataAloc to set
     */
    public void setDataAloc(final Date dataAloc) {
        this.dataAloc = dataAloc;
    }

    /**
     * @return the percentualAlocPv
     */
    public BigDecimal getPercentualAlocPv() {
        return percentualAlocPv;
    }

    /**
     * @param percentualAlocPv
     *            the percentualAlocPv to set
     */
    public void setPercentualAlocPv(final BigDecimal percentualAlocPv) {
        this.percentualAlocPv = percentualAlocPv;
    }

    /**
     * @return the percentualAlocCr
     */
    public BigDecimal getPercentualAlocCr() {
        return percentualAlocCr;
    }

    /**
     * @param percentualAlocCr
     *            the percentualAlocCr to set
     */
    public void setPercentualAlocCr(final BigDecimal percentualAlocCr) {
        this.percentualAlocCr = percentualAlocCr;
    }

}