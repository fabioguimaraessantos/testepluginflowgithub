/*
 * @(#) HedgeMoedaMes.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity gerado a partir da tabela HEDGE_MOEDA_MES.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 24/08/2010 14:05:04
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "HEDGE_MOEDA_MES")
@NamedQueries({
    @NamedQuery(name = "HedgeMoedaMes.findAll", query = "SELECT t FROM HedgeMoedaMes t"),
    
    @NamedQuery(name = "HedgeMoedaMes.findUnique", query = "SELECT t FROM HedgeMoedaMes t " 
        + " WHERE TRUNC(t.dataMes) = TRUNC(?1) AND t.moeda.codigoMoeda = ?2"),
    
    @NamedQuery(name = "HedgeMoedaMes.findByFilter", query = "SELECT hedge FROM HedgeMoedaMes hedge "
        + " LEFT JOIN FETCH hedge.moeda "
        + " WHERE (TRUNC(hedge.dataMes) = TRUNC(?1) OR to_date('01/01/1900','dd/MM/yyyy') = TRUNC(?1)) " 
        + " AND ( (hedge.moeda.codigoMoeda = ?2) OR (?2 = 0L) ) " 
        + " ORDER BY hedge.dataMes, hedge.moeda ")
})
public class HedgeMoedaMes implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** Constante para NamedQuery "HedgeMoedaMes.findAll". */
    public static final String FIND_ALL = "HedgeMoedaMes.findAll";

    /** Constante para NamedQuery "HedgeMoedaMes.findByFilter". */
    public static final String FIND_BY_FILTER = "HedgeMoedaMes.findByFilter";
    
    /** Constante para NamedQuery "HedgeMoedaMes.findUnique". */
    public static final String FIND_UNIQUE = "HedgeMoedaMes.findUnique";
    
    /**
     * Atributo gerado a partir da coluna HEMM_CD_HEDGE_MOEDA_MES.
     */
    @Id
    @GeneratedValue(generator = "HedgeMoedaMesSeq")
    @SequenceGenerator(name = "HedgeMoedaMesSeq", sequenceName = "SQ_HEMM_CD_HEDGE_MOEDA_MES", allocationSize = 1)
    @Column(name = "HEMM_CD_HEDGE_MOEDA_MES", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoHedgeMoedaMes;

    /**
     * Atributo gerado a partir da coluna MOED_CD_MOEDA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOED_CD_MOEDA", nullable = false)
    private Moeda moeda;

    /**
     * Atributo gerado a partir da coluna HEMM_DT_MES.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "HEMM_DT_MES", nullable = false, length = 7)
    private Date dataMes;

    /**
     * Atributo gerado a partir da coluna HEMM_VL_HEDGE.
     */

    @Column(name = "HEMM_VL_HEDGE", precision = 22, scale = 0)
    private BigDecimal valorHedge;

    /**
     * Construtor default.
     */
    public HedgeMoedaMes() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoHedgeMoedaMes Valor do atributo codigoHedgeMoedaMes;
     * @param moeda Valor do atributo moeda;
     * @param dataMes Valor do atributo dataMes;
     */
    public HedgeMoedaMes(final Long codigoHedgeMoedaMes, 
            final Moeda moeda, final Date dataMes) {
        this.codigoHedgeMoedaMes = codigoHedgeMoedaMes;
        this.moeda = moeda;
        this.dataMes = dataMes;
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoHedgeMoedaMes Valor do atributo codigoHedgeMoedaMes;
     * @param moeda Valor do atributo moeda;
     * @param dataMes Valor do atributo dataMes;
     * @param valorHedge Valor do atributo valorHedge;
     */
    public HedgeMoedaMes(final Long codigoHedgeMoedaMes, final Moeda moeda,
            final Date dataMes, final BigDecimal valorHedge) {
        this.codigoHedgeMoedaMes = codigoHedgeMoedaMes;
        this.moeda = moeda;
        this.dataMes = dataMes;
        this.valorHedge = valorHedge;
    }

    /**
     * Obtem o valor do atributo codigoHedgeMoedaMes.<BR>
     * Atributo gerado a partir da coluna HEMM_CD_HEDGE_MOEDA_MES.
     * @return Valor do atributo codigoHedgeMoedaMes.
     */
    public Long getCodigoHedgeMoedaMes() {
        return this.codigoHedgeMoedaMes;
    }

    /**
     * Atualiza o valor do atributo codigoHedgeMoedaMes.<BR>
     * Atributo gerado a partir da coluna HEMM_CD_HEDGE_MOEDA_MES.
     * @param codigoHedgeMoedaMes Novo valor para o atributo codigoHedgeMoedaMes.
     */
    public void setCodigoHedgeMoedaMes(final Long codigoHedgeMoedaMes) {
        this.codigoHedgeMoedaMes = codigoHedgeMoedaMes;
    }

    /**
     * Obtem o valor do atributo moeda.<BR>
     * Atributo gerado a partir da coluna MOED_CD_MOEDA.
     * @return Valor do atributo moeda.
     */
    public Moeda getMoeda() {
        return this.moeda;
    }

    /**
     * Atualiza o valor do atributo moeda.<BR>
     * Atributo gerado a partir da coluna MOED_CD_MOEDA.
     * @param moeda Novo valor para o atributo moeda.
     */
    public void setMoeda(final Moeda moeda) {
        this.moeda = moeda;
    }

    /**
     * Obtem o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna HEMM_DT_MES.
     * @return Valor do atributo dataMes.
     */
    public Date getDataMes() {
        return this.dataMes;
    }

    /**
     * Atualiza o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna HEMM_DT_MES.
     * @param dataMes Novo valor para o atributo dataMes.
     */
    public void setDataMes(final Date dataMes) {
        this.dataMes = dataMes;
    }

    /**
     * Obtem o valor do atributo valorHedge.<BR>
     * Atributo gerado a partir da coluna HEMM_VL_HEDGE.
     * @return Valor do atributo valorHedge.
     */
    public BigDecimal getValorHedge() {
        return this.valorHedge;
    }

    /**
     * Atualiza o valor do atributo valorHedge.<BR>
     * Atributo gerado a partir da coluna HEMM_VL_HEDGE.
     * @param valorHedge Novo valor para o atributo valorHedge.
     */
    public void setValorHedge(final BigDecimal valorHedge) {
        this.valorHedge = valorHedge;
    }

    /**
     * @see Object#toString()
     * 
     * @return retorna a entidade no formato de string
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(
                Integer.toHexString(hashCode())).append(" [");
        buffer.append("codigoHedgeMoedaMes").append("='").append(
                getCodigoHedgeMoedaMes()).append("' ");
        buffer.append("moeda").append("='").append(getMoeda()).append("' ");
        buffer.append("dataMes").append("='").append(getDataMes()).append("' ");
        buffer.append("valorHedge").append("='").append(getValorHedge())
                .append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
