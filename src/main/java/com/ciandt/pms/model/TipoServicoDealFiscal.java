/*
 * @(#) TipoServicoDealFiscal.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity gerado a partir da tabela TIPO_SERVICO_DEAL_FISCAL.
 *
 * @author Generated by Hibernate Tools 3.6.0
 * @since Sep 28, 2012 11:39:11 AM
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "TIPO_SERVICO_DEAL_FISCAL")
@NamedQueries({
		@NamedQuery(name = "TipoServicoDealFiscal.findAll", query = "SELECT t FROM TipoServicoDealFiscal t"),
		@NamedQuery(name = "TipoServicoDealFiscal.findByDealFiscal", query = "SELECT t FROM TipoServicoDealFiscal t "
				+ "WHERE t.dealFiscal.codigoDealFiscal = ?") })
public class TipoServicoDealFiscal implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Constante para NamedQuery "TipoServicoDealFiscal.findAll".
     */
    public static final String FIND_ALL = "TipoServicoDealFiscal.findAll";

    /**
	 * Constante para NamedQuery "TipoServicoDealFiscal.findByDealFiscal".
	 */
	public static final String FIND_BY_DEAL_FISCAL = "TipoServicoDealFiscal.findByDealFiscal";

	/**
     * Atributo gerado a partir da coluna TISE_CD_TIPO_SERVICO.
     */
    @EmbeddedId
    @GeneratedValue(generator = "TipoServicoDealFiscalSeq")
    @SequenceGenerator(name = "TipoServicoDealFiscalSeq", sequenceName = "SQ_DEFI_CD_DEAL_FISCAL", allocationSize = 1)
    @AttributeOverrides({
            @AttributeOverride(name = "codigoDealFiscal", column = @Column(name = "DEFI_CD_DEAL_FISCAL", precision = 18, scale = 0)),
            @AttributeOverride(name = "codigoTipoServico", column = @Column(name = "TISE_CD_TIPO_SERVICO", precision = 18, scale = 0))})
    private TipoServicoDealFiscalId id;

    /**
     * Atributo gerado a partir da coluna TISE_CD_TIPO_SERVICO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TISE_CD_TIPO_SERVICO", insertable = false, updatable = false)
    private TipoServico tipoServico;

    /**
     * Atributo gerado a partir da coluna DEFI_CD_DEAL_FISCAL.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFI_CD_DEAL_FISCAL", insertable = false, updatable = false)
    private DealFiscal dealFiscal;

    /**
     * Construtor default.
     */
    public TipoServicoDealFiscal() {
    }

    /**
     * Construtor com preenchimento da entidade.
	 * 
	 * @param id
	 *            Valor do atributo id;
     */
    public TipoServicoDealFiscal(TipoServicoDealFiscalId id) {
        this.id = id;
    }

    /**
     * Construtor com preenchimento da entidade.
	 * 
	 * @param id
	 *            Valor do atributo id;
     */
    public TipoServicoDealFiscal(TipoServicoDealFiscalId id,
            TipoServico tipoServico, DealFiscal dealFiscal) {
        this.id = id;
        this.tipoServico = tipoServico;
        this.dealFiscal = dealFiscal;
    }

    /**
     * Obtem o valor do atributo id.<BR>
     * Atributo gerado a partir da coluna TISE_CD_TIPO_SERVICO.
	 * 
     * @return Valor do atributo id.
     */
    public TipoServicoDealFiscalId getId() {
        return this.id;
    }

    /**
     * Atualiza o valor do atributo id.<BR>
     * Atributo gerado a partir da coluna TISE_CD_TIPO_SERVICO.
	 * 
	 * @param id
	 *            Novo valor para o atributo id.
     */
    public void setId(TipoServicoDealFiscalId id) {
        this.id = id;
    }

    /**
     * Obtem o valor do atributo tipoServico.<BR>
     * Atributo gerado a partir da coluna TISE_CD_TIPO_SERVICO.
	 * 
     * @return Valor do atributo tipoServico.
     */
    public TipoServico getTipoServico() {
        return this.tipoServico;
    }

    /**
     * Atualiza o valor do atributo tipoServico.<BR>
     * Atributo gerado a partir da coluna TISE_CD_TIPO_SERVICO.
	 * 
	 * @param tipoServico
	 *            Novo valor para o atributo tipoServico.
     */
    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    /**
     * Obtem o valor do atributo dealFiscal.<BR>
     * Atributo gerado a partir da coluna DEFI_CD_DEAL_FISCAL.
	 * 
     * @return Valor do atributo dealFiscal.
     */
    public DealFiscal getDealFiscal() {
        return this.dealFiscal;
    }

    /**
     * Atualiza o valor do atributo dealFiscal.<BR>
     * Atributo gerado a partir da coluna DEFI_CD_DEAL_FISCAL.
	 * 
	 * @param dealFiscal
	 *            Novo valor para o atributo dealFiscal.
     */
    public void setDealFiscal(DealFiscal dealFiscal) {
        this.dealFiscal = dealFiscal;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@")
                .append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("id").append("='").append(getId()).append("' ");
        buffer.append("tipoServico").append("='").append(getTipoServico())
                .append("' ");
        buffer.append("dealFiscal").append("='").append(getDealFiscal())
                .append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
