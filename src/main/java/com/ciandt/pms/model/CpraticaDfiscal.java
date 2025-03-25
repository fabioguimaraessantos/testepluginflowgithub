/*
 * @(#) CpraticaDfiscal.java
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

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * Entity gerado a partir da tabela CPRATICA_DFISCAL.
 * 
 * @author Generated by Hibernate Tools 3.6.0
 * @since Sep 28, 2012 11:39:11 AM
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "CPRATICA_DFISCAL")
@Audited
@AuditTable(value="CPRATICA_DFISCAL_AUD")
@NamedQueries({
		@NamedQuery(name = "CpraticaDfiscal.findAll", query = "SELECT t FROM CpraticaDfiscal t"),
		@NamedQuery(name = "CpraticaDfiscal.findById", query = "SELECT t FROM CpraticaDfiscal t "
				+ " WHERE t.id.codigoDealFiscal = ?"
				+ " AND t.id.codigoContratoPratica = ?"),
		@NamedQuery(name = CpraticaDfiscal.FIND_BY_CONTRATO_PRATICA, query = "SELECT t FROM CpraticaDfiscal t"
				+ " WHERE t.id.codigoContratoPratica = :codigoContratoPratica")
})
public class CpraticaDfiscal implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "CpraticaDfiscal.findAll".
	 */
	public static final String FIND_ALL = "CpraticaDfiscal.findAll";

	/**
	 * Constante para NamedQuery "CpraticaDfiscal.findById".
	 */
	public static final String FIND_BY_ID = "CpraticaDfiscal.findById";
	
	/**
	 * Constante para NamedQuery "CpraticaDfiscal.findByContratoPratica".
	 */
	public static final String FIND_BY_CONTRATO_PRATICA = "CpraticaDfiscal.findByContratoPratica";

	/**
	 * Atributo gerado a partir da coluna DEFI_CD_DEAL_FISCAL.
	 */
	@EmbeddedId
	@GeneratedValue(generator = "CpraticaDfiscalSeq")
	@SequenceGenerator(name = "CpraticaDfiscalSeq", sequenceName = "SQ_COPR_CD_CONTRATO_PRATICA", allocationSize = 1)
	@AttributeOverrides({
			@AttributeOverride(name = "codigoContratoPratica", column = @Column(name = "COPR_CD_CONTRATO_PRATICA", nullable = false, precision = 18, scale = 0)),
			@AttributeOverride(name = "codigoDealFiscal", column = @Column(name = "DEFI_CD_DEAL_FISCAL", nullable = false, precision = 18, scale = 0)) })
	private CpraticaDfiscalId id;

	/**
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@JoinColumn(name = "COPR_CD_CONTRATO_PRATICA", nullable = false, insertable = false, updatable = false)
	private ContratoPratica contratoPratica;

	/**
	 * Atributo gerado a partir da coluna DEFI_CD_DEAL_FISCAL.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@JoinColumn(name = "DEFI_CD_DEAL_FISCAL", nullable = false, insertable = false, updatable = false)
	private DealFiscal dealFiscal;

	/**
	 * Construtor default.
	 */
	public CpraticaDfiscal() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param id
	 *            Valor do atributo id;
	 * @param contratoPratica
	 *            Valor do atributo contratoPratica;
	 * @param dealFiscal
	 *            Valor do atributo dealFiscal;
	 */
	public CpraticaDfiscal(CpraticaDfiscalId id,
			ContratoPratica contratoPratica, DealFiscal dealFiscal) {
		this.id = id;
		this.contratoPratica = contratoPratica;
		this.dealFiscal = dealFiscal;
	}

	/**
	 * Obtem o valor do atributo id.<BR>
	 * Atributo gerado a partir da coluna DEFI_CD_DEAL_FISCAL.
	 * 
	 * @return Valor do atributo id.
	 */
	public CpraticaDfiscalId getId() {
		return this.id;
	}

	/**
	 * Atualiza o valor do atributo id.<BR>
	 * Atributo gerado a partir da coluna DEFI_CD_DEAL_FISCAL.
	 * 
	 * @param id
	 *            Novo valor para o atributo id.
	 */
	public void setId(CpraticaDfiscalId id) {
		this.id = id;
	}

	/**
	 * Obtem o valor do atributo contratoPratica.<BR>
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 * 
	 * @return Valor do atributo contratoPratica.
	 */
	public ContratoPratica getContratoPratica() {
		return this.contratoPratica;
	}

	/**
	 * Atualiza o valor do atributo contratoPratica.<BR>
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 * 
	 * @param contratoPratica
	 *            Novo valor para o atributo contratoPratica.
	 */
	public void setContratoPratica(ContratoPratica contratoPratica) {
		this.contratoPratica = contratoPratica;
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
		buffer.append("contratoPratica").append("='")
				.append(getContratoPratica()).append("' ");
		buffer.append("dealFiscal").append("='").append(getDealFiscal())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}
