/*
 * @(#) VwPmsCustoEmbutidoPvId.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * VwPmsCustoEmbutidoPvId generated by hbm2java
 * 
 * @author Generated by Hibernate Tools 3.4.0.CR1
 * @since 16/10/2013 15:43:56
 * @version 09/01/19 1.1 10:08:00
 */
@Embeddable
public class VwPmsCustoEmbutidoPvId implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "VwPmsCustoEmbutidoPvId.findAll".
	 */
	public static final String FIND_ALL = "VwPmsCustoEmbutidoPvId.findAll";

	@Column(name = "COPR_CD_CONTRATO_PRATICA", precision = 18, scale = 0)
	private Long codigoContratoPratica;

	@Column(name = "ALPE_DT_ALOCACAO_PERIODO", length = 7)
	private Date dataAlocacaoPeriodo;

	@Column(name = "MOED_CD_MOEDA", nullable = false, precision = 18, scale = 0)
	private Long codigoMoeda;

	@Column(name = "TOTAL", precision = 22, scale = 0)
	private BigDecimal total;

	/**
	 * Construtor default.
	 */
	public VwPmsCustoEmbutidoPvId() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoMoeda
	 *            Valor do atributo codigoMoeda;
	 */
	public VwPmsCustoEmbutidoPvId(Long codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoMoeda
	 *            Valor do atributo codigoMoeda;
	 */
	public VwPmsCustoEmbutidoPvId(Long codigoContratoPratica,
			Date dataAlocacaoPeriodo, Long codigoMoeda, BigDecimal total) {
		this.codigoContratoPratica = codigoContratoPratica;
		this.dataAlocacaoPeriodo = dataAlocacaoPeriodo;
		this.codigoMoeda = codigoMoeda;
		this.total = total;
	}

	/**
	 * Obtem o valor do atributo codigoContratoPratica.<BR>
	 * 
	 * @return Valor do atributo codigoContratoPratica.
	 */
	public Long getCodigoContratoPratica() {
		return this.codigoContratoPratica;
	}

	/**
	 * Atualiza o valor do atributo codigoContratoPratica.<BR>
	 * 
	 * @param codigoContratoPratica
	 *            Novo valor para o atributo codigoContratoPratica.
	 */
	public void setCodigoContratoPratica(Long codigoContratoPratica) {
		this.codigoContratoPratica = codigoContratoPratica;
	}

	/**
	 * Obtem o valor do atributo dataAlocacaoPeriodo.<BR>
	 * 
	 * @return Valor do atributo dataAlocacaoPeriodo.
	 */
	public Date getDataAlocacaoPeriodo() {
		return this.dataAlocacaoPeriodo;
	}

	/**
	 * Atualiza o valor do atributo dataAlocacaoPeriodo.<BR>
	 * 
	 * @param dataAlocacaoPeriodo
	 *            Novo valor para o atributo dataAlocacaoPeriodo.
	 */
	public void setDataAlocacaoPeriodo(Date dataAlocacaoPeriodo) {
		this.dataAlocacaoPeriodo = dataAlocacaoPeriodo;
	}

	/**
	 * Obtem o valor do atributo codigoMoeda.<BR>
	 * 
	 * @return Valor do atributo codigoMoeda.
	 */
	public Long getCodigoMoeda() {
		return this.codigoMoeda;
	}

	/**
	 * Atualiza o valor do atributo codigoMoeda.<BR>
	 * 
	 * @param codigoMoeda
	 *            Novo valor para o atributo codigoMoeda.
	 */
	public void setCodigoMoeda(Long codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}

	/**
	 * Obtem o valor do atributo .<BR>
	 * 
	 * @return Valor do atributo .
	 */
	public BigDecimal getTotal() {
		return this.total;
	}

	/**
	 * Atualiza o valor do atributo .<BR>
	 * 
	 * @param Novo
	 *            valor para o atributo .
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VwPmsCustoEmbutidoPvId))
			return false;
		VwPmsCustoEmbutidoPvId castOther = (VwPmsCustoEmbutidoPvId) other;

		return ((this.getCodigoContratoPratica() == castOther
				.getCodigoContratoPratica()) || (this
				.getCodigoContratoPratica() != null
				&& castOther.getCodigoContratoPratica() != null && this
				.getCodigoContratoPratica().equals(
						castOther.getCodigoContratoPratica())))
				&& ((this.getDataAlocacaoPeriodo() == castOther
						.getDataAlocacaoPeriodo()) || (this
						.getDataAlocacaoPeriodo() != null
						&& castOther.getDataAlocacaoPeriodo() != null && this
						.getDataAlocacaoPeriodo().equals(
								castOther.getDataAlocacaoPeriodo())))
				&& ((this.getCodigoMoeda() == castOther.getCodigoMoeda()) || (this
						.getCodigoMoeda() != null
						&& castOther.getCodigoMoeda() != null && this
						.getCodigoMoeda().equals(castOther.getCodigoMoeda())))
				&& ((this.getTotal() == castOther.getTotal()) || (this
						.getTotal() != null && castOther.getTotal() != null && this
						.getTotal().equals(castOther.getTotal())));
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getCodigoContratoPratica() == null ? 0 : this
						.getCodigoContratoPratica().hashCode());
		result = 37
				* result
				+ (getDataAlocacaoPeriodo() == null ? 0 : this
						.getDataAlocacaoPeriodo().hashCode());
		result = 37
				* result
				+ (getCodigoMoeda() == null ? 0 : this.getCodigoMoeda()
						.hashCode());
		result = 37 * result
				+ (getTotal() == null ? 0 : this.getTotal().hashCode());
		return result;
	}

}
