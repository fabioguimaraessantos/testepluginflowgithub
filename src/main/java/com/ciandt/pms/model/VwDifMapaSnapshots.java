/*
 * @(#) VwDifMapaSnapshots.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity gerado a partir da tabela VW_PMS_DIF_MAPA_SNAPSHOTS.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 08/03/2011 14:03:02
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "VW_PMS_DIF_MAPA_SNAPS_FINAL")
@NamedQueries({ 
    @NamedQuery(name = "VwDifMapaSnapshots.findAllByRangeMonthsClob", 
            query = "SELECT t FROM VwDifMapaSnapshots t "
        + " WHERE ( TRUNC(t.id.dataAloc,'MONTH') "
        + "         BETWEEN TRUNC(?,'MONTH') "
        + "             AND ADD_MONTHS(TRUNC(?,'MONTH'),?-1) ) "
        + "   AND (t.indicadorFlag = 'FLWALL') "
        + " ORDER BY t.id.nomeContratoPratica ASC, t.id.nomeRecurso ASC, "
        + "          t.id.codigoAlocacao ASC, t.id.dataAloc ASC"),
        
    @NamedQuery(name = "VwDifMapaSnapshots.findAllByRangeMonthsPeople", 
            query = "SELECT t FROM VwDifMapaSnapshots t "
        + " WHERE ( TRUNC(t.id.dataAloc,'MONTH') "
        + "         BETWEEN TRUNC(?,'MONTH') "
        + "             AND ADD_MONTHS(TRUNC(?,'MONTH'),?-1) ) "
        + " ORDER BY t.id.nomeRecurso ASC, t.id.nomeContratoPratica ASC, "
        + "          t.id.codigoAlocacao ASC, t.id.dataAloc ASC")
})
public class VwDifMapaSnapshots implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "VwDifMapaSnapshots.findAllByRangeMonthsClob".
     */
    public static final String FIND_ALL_BY_RANGE_MONTHS_CLOB = "VwDifMapaSnapshots.findAllByRangeMonthsClob";
    
    /**
     * Constante para NamedQuery "VwDifMapaSnapshots.findAllByRangeMonthsPeople".
     */
    public static final String FIND_ALL_BY_RANGE_MONTHS_PEOPLE = "VwDifMapaSnapshots.findAllByRangeMonthsPeople";

    /** Atributo com o id. */
    @EmbeddedId
    @GeneratedValue(generator = "VwDifMapaSnapshotsSeq")
    @SequenceGenerator(name = "VwDifMapaSnapshotsSeq", 
            sequenceName = "SQ_SNAP_NM_CONTRATO_PRATICA", allocationSize = 1)
    @AttributeOverrides({
            @AttributeOverride(name = "codigoMapaAlocacao", 
                    column = @Column(name = "SNAP_CD_MAPA_ALOCACAO", precision = 18, scale = 0)),
            @AttributeOverride(name = "nomeContratoPratica", 
                    column = @Column(name = "SNAP_NM_CONTRATO_PRATICA", length = 240)),
            @AttributeOverride(name = "codigoRecurso", 
                    column = @Column(name = "SNAP_CD_RECURSO", precision = 18, scale = 0)),
            @AttributeOverride(name = "nomeRecurso", column = @Column(name = "SNAP_NM_RECURSO", length = 50)),
            @AttributeOverride(name = "codigoAlocacao", 
                    column = @Column(name = "SNAP_CD_ALOCACAO", precision = 18, scale = 0)),
            @AttributeOverride(name = "dataAloc", column = @Column(name = "SNAP_DT_ALOC", length = 7)) })
    private VwDifMapaSnapshotsId id;

    /** Atributo gerado a partir da coluna SNAP_NM_PERFIL_VENDIDO_PV. */
    @Column(name = "SNAP_NM_PERFIL_VENDIDO_PV", length = 200)
    private String nomePerfilVendidoPv;

    /** Atributo gerado a partir da coluna SNAP_NM_PERFIL_VENDIDO_CR. */
    @Column(name = "SNAP_NM_PERFIL_VENDIDO_CR", length = 200)
    private String nomePerfilVendidoCr;

    /** Atributo gerado a partir da coluna SNAP_IN_ESTAGIO_PV. */
    @Column(name = "SNAP_IN_ESTAGIO_PV", length = 3)
    private String indicadorEstagioPv;

    /** Atributo gerado a partir da coluna SNAP_IN_ESTAGIO_CR. */
    @Column(name = "SNAP_IN_ESTAGIO_CR", length = 3)
    private String indicadorEstagioCr;

    /** Atributo gerado a partir da coluna SNAP_VL_UR_PV. */
    @Column(name = "SNAP_VL_UR_PV", precision = 22, scale = 0)
    private BigDecimal valorUrPv;

    /** Atributo gerado a partir da coluna SNAP_VL_UR_CR. */
    @Column(name = "SNAP_VL_UR_CR", precision = 22, scale = 0)
    private BigDecimal valorUrCr;

    /** Atributo gerado a partir da coluna SNAP_PR_ALOC_PV. */
    @Column(name = "SNAP_PR_ALOC_PV", precision = 22, scale = 0)
    private BigDecimal percentualAlocPv;

    /** Atributo gerado a partir da coluna SNAP_PR_ALOC_CR. */
    @Column(name = "SNAP_PR_ALOC_CR", precision = 22, scale = 0)
    private BigDecimal percentualAlocCr;

    /** Atributo gerado a partir da coluna SNAP_IN_TIPO_OP. */
    @Column(name = "SNAP_IN_TIPO_OP", length = 1)
    private String indicadorTipoOp;
    
    /** Atributo gerado a partir da coluna SNAP_IN_FLAG. */
    @Column(name = "SNAP_IN_FLAG", length = 1)
    private String indicadorFlag;

    /**
     * Construtor default.
     */
    public VwDifMapaSnapshots() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param id
     *            Valor do atributo id;
     */
    public VwDifMapaSnapshots(final VwDifMapaSnapshotsId id) {
        this.id = id;
    }

    /**
     * Obtem o valor do atributo id.<BR>
     * 
     * @return Valor do atributo id.
     */
    public VwDifMapaSnapshotsId getId() {
        return this.id;
    }

    /**
     * Atualiza o valor do atributo id.<BR>
     * 
     * @param id
     *            Novo valor para o atributo id.
     */
    public void setId(final VwDifMapaSnapshotsId id) {
        this.id = id;
    }

    /**
     * Obtem o valor do atributo nomePerfilVendidoPv.<BR>
     * 
     * @return Valor do atributo nomePerfilVendidoPv.
     */
    public String getNomePerfilVendidoPv() {
        return this.nomePerfilVendidoPv;
    }

    /**
     * Atualiza o valor do atributo nomePerfilVendidoPv.<BR>
     * 
     * @param nomePerfilVendidoPv
     *            Novo valor para o atributo nomePerfilVendidoPv.
     */
    public void setNomePerfilVendidoPv(final String nomePerfilVendidoPv) {
        this.nomePerfilVendidoPv = nomePerfilVendidoPv;
    }

    /**
     * Obtem o valor do atributo nomePerfilVendidoCr.<BR>
     * 
     * @return Valor do atributo nomePerfilVendidoCr.
     */
    public String getNomePerfilVendidoCr() {
        return this.nomePerfilVendidoCr;
    }

    /**
     * Atualiza o valor do atributo nomePerfilVendidoCr.<BR>
     * 
     * @param nomePerfilVendidoCr
     *            Novo valor para o atributo nomePerfilVendidoCr.
     */
    public void setNomePerfilVendidoCr(final String nomePerfilVendidoCr) {
        this.nomePerfilVendidoCr = nomePerfilVendidoCr;
    }

    /**
     * Obtem o valor do atributo indicadorEstagioPv.<BR>
     * 
     * @return Valor do atributo indicadorEstagioPv.
     */
    public String getIndicadorEstagioPv() {
        return this.indicadorEstagioPv;
    }

    /**
     * Atualiza o valor do atributo indicadorEstagioPv.<BR>
     * 
     * @param indicadorEstagioPv
     *            Novo valor para o atributo indicadorEstagioPv.
     */
    public void setIndicadorEstagioPv(final String indicadorEstagioPv) {
        this.indicadorEstagioPv = indicadorEstagioPv;
    }

    /**
     * Obtem o valor do atributo indicadorEstagioCr.<BR>
     * 
     * @return Valor do atributo indicadorEstagioCr.
     */
    public String getIndicadorEstagioCr() {
        return this.indicadorEstagioCr;
    }

    /**
     * Atualiza o valor do atributo indicadorEstagioCr.<BR>
     * 
     * @param indicadorEstagioCr
     *            Novo valor para o atributo indicadorEstagioCr.
     */
    public void setIndicadorEstagioCr(final String indicadorEstagioCr) {
        this.indicadorEstagioCr = indicadorEstagioCr;
    }

    /**
     * Obtem o valor do atributo valorUrPv.<BR>
     * 
     * @return Valor do atributo valorUrPv.
     */
    public BigDecimal getValorUrPv() {
        return this.valorUrPv;
    }

    /**
     * Atualiza o valor do atributo valorUrPv.<BR>
     * 
     * @param valorUrPv
     *            Novo valor para o atributo valorUrPv.
     */
    public void setValorUrPv(final BigDecimal valorUrPv) {
        this.valorUrPv = valorUrPv;
    }

    /**
     * Obtem o valor do atributo valorUrCr.<BR>
     * 
     * @return Valor do atributo valorUrCr.
     */
    public BigDecimal getValorUrCr() {
        return this.valorUrCr;
    }

    /**
     * Atualiza o valor do atributo valorUrCr.<BR>
     * 
     * @param valorUrCr
     *            Novo valor para o atributo valorUrCr.
     */
    public void setValorUrCr(final BigDecimal valorUrCr) {
        this.valorUrCr = valorUrCr;
    }

    /**
     * Obtem o valor do atributo percentualAlocPv.<BR>
     * 
     * @return Valor do atributo percentualAlocPv.
     */
    public BigDecimal getPercentualAlocPv() {
        return this.percentualAlocPv;
    }

    /**
     * Atualiza o valor do atributo percentualAlocPv.<BR>
     * 
     * @param percentualAlocPv
     *            Novo valor para o atributo percentualAlocPv.
     */
    public void setPercentualAlocPv(final BigDecimal percentualAlocPv) {
        this.percentualAlocPv = percentualAlocPv;
    }

    /**
     * Obtem o valor do atributo percentualAlocCr.<BR>
     * 
     * @return Valor do atributo percentualAlocCr.
     */
    public BigDecimal getPercentualAlocCr() {
        return this.percentualAlocCr;
    }

    /**
     * Atualiza o valor do atributo percentualAlocCr.<BR>
     * 
     * @param percentualAlocCr
     *            Novo valor para o atributo percentualAlocCr.
     */
    public void setPercentualAlocCr(final BigDecimal percentualAlocCr) {
        this.percentualAlocCr = percentualAlocCr;
    }

    /**
     * Obtem o valor do atributo indicadorTipoOp.<BR>
     * 
     * @return Valor do atributo indicadorTipoOp.
     */
    public String getIndicadorTipoOp() {
        return this.indicadorTipoOp;
    }

    /**
     * Atualiza o valor do atributo indicadorTipoOp.<BR>
     * 
     * @param indicadorTipoOp
     *            Novo valor para o atributo indicadorTipoOp.
     */
    public void setIndicadorTipoOp(final String indicadorTipoOp) {
        this.indicadorTipoOp = indicadorTipoOp;
    }
    
    /**
     * Obtem o valor do atributo indicadorFlag.<BR>
     * 
     * @return Valor do atributo indicadorFlag.
     */
    public String getIndicadorFlag() {
        return this.indicadorFlag;
    }

    /**
     * Atualiza o valor do atributo indicadorFlag.<BR>
     * 
     * @param indicadorFlag
     *            Novo valor para o atributo indicadorFlag.
     */
    public void setIndicadorFlag(final String indicadorFlag) {
        this.indicadorFlag = indicadorFlag;
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
        buffer.append("id").append("='").append(getId()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}