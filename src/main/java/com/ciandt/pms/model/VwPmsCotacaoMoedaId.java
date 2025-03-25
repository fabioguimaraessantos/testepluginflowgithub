/*
 * @(#) VwPmsVoucherCotacaoMoedaId.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * VwPmsVoucherCotacaoMoedaId generated by hbm2java
 * 
 * @author Generated by Hibernate Tools 3.4.0.CR1
 * @since 05/12/2013 11:05:43
 * @version 09/01/19 1.1 10:08:00
 */
@Embeddable
public class VwPmsCotacaoMoedaId implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "VwPmsVoucherCotacaoMoedaId.findAll".
	 */
	public static final String FIND_ALL = "VwPmsVoucherCotacaoMoedaId.findAll";

	@Column(name = "GAE_KEY", length = 58)
	private String key;

	@Column(name = "MOED_CD_MOEDA", precision = 18, scale = 0)
	private Long codigoMoeda;

	@Column(name = "VAL_DT_INDICE", length = 7)
	private Date dataIndice;

	@Column(name = "VAL_RE_VALOR", precision = 18, scale = 8)
	private BigDecimal valorTaxa;

	@Column(name = "IND_ST_SIGLA", length = 10)
	private String siglaMoeda;

	@Column(name = "ATUALIZACAO", length = 8)
	private Date dataAtualizacao;

	@Column(name = "MAX_DT", length = 7)
	private Date data;

	/**
	 * Construtor default.
	 */
	public VwPmsCotacaoMoedaId() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 */
	public VwPmsCotacaoMoedaId(String key, Long codigoMoeda, Date dataIndice,
			BigDecimal reValor, String siglaMoeda, Date dataAtualizacao,
			Date data) {
		this.key = key;
		this.codigoMoeda = codigoMoeda;
		this.dataIndice = dataIndice;
		this.valorTaxa = reValor;
		this.siglaMoeda = siglaMoeda;
		this.dataAtualizacao = dataAtualizacao;
		this.data = data;
	}

	/**
	 * Obtem o valor do atributo key.<BR>
	 * 
	 * @return Valor do atributo key.
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Atualiza o valor do atributo key.<BR>
	 * 
	 * @param key
	 *            Novo valor para o atributo key.
	 */
	public void setKey(String key) {
		this.key = key;
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
	 * Obtem o valor do atributo dataIndice.<BR>
	 * 
	 * @return Valor do atributo dataIndice.
	 */
	public Date getDataIndice() {
		return this.dataIndice;
	}

	/**
	 * Atualiza o valor do atributo dataIndice.<BR>
	 * 
	 * @param dataIndice
	 *            Novo valor para o atributo dataIndice.
	 */
	public void setDataIndice(Date dataIndice) {
		this.dataIndice = dataIndice;
	}

	/**
	 * Obtem o valor do atributo reValor.<BR>
	 * 
	 * @return Valor do atributo reValor.
	 */
	public BigDecimal getValorTaxa() {
		return this.valorTaxa;
	}

	/**
	 * Atualiza o valor do atributo reValor.<BR>
	 * 
	 * @param reValor
	 *            Novo valor para o atributo reValor.
	 */
	public void setValorTaxa(BigDecimal valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	/**
	 * Obtem o valor do atributo stSigla.<BR>
	 * 
	 * @return Valor do atributo stSigla.
	 */
	public String getSiglaMoeda() {
		return this.siglaMoeda;
	}

	/**
	 * Atualiza o valor do atributo stSigla.<BR>
	 * 
	 * @param stSigla
	 *            Novo valor para o atributo stSigla.
	 */
	public void setSiglaSigla(String siglaMoeda) {
		this.siglaMoeda = siglaMoeda;
	}

	/**
	 * Obtem o valor do atributo .<BR>
	 * 
	 * @return Valor do atributo .
	 */
	public Date getDataAtualizacao() {
		return this.dataAtualizacao;
	}

	/**
	 * Atualiza o valor do atributo .<BR>
	 * 
	 * @param Novo
	 *            valor para o atributo .
	 */
	public void set(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	/**
	 * Obtem o valor do atributo data.<BR>
	 * 
	 * @return Valor do atributo data.
	 */
	public Date getData() {
		return this.data;
	}

	/**
	 * Atualiza o valor do atributo data.<BR>
	 * 
	 * @param data
	 *            Novo valor para o atributo data.
	 */
	public void setData(Date data) {
		this.data = data;
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
		if (!(other instanceof VwPmsCotacaoMoedaId))
			return false;
		VwPmsCotacaoMoedaId castOther = (VwPmsCotacaoMoedaId) other;

		return ((this.getKey() == castOther.getKey()) || (this.getKey() != null
				&& castOther.getKey() != null && this.getKey().equals(
				castOther.getKey())))
				&& ((this.getCodigoMoeda() == castOther.getCodigoMoeda()) || (this
						.getCodigoMoeda() != null
						&& castOther.getCodigoMoeda() != null && this
						.getCodigoMoeda().equals(castOther.getCodigoMoeda())))
				&& ((this.getDataIndice() == castOther.getDataIndice()) || (this
						.getDataIndice() != null
						&& castOther.getDataIndice() != null && this
						.getDataIndice().equals(castOther.getDataIndice())))
				&& ((this.getValorTaxa() == castOther.getValorTaxa()) || (this
						.getValorTaxa() != null
						&& castOther.getValorTaxa() != null && this
						.getValorTaxa().equals(castOther.getValorTaxa())))
				&& ((this.getSiglaMoeda() == castOther.getSiglaMoeda()) || (this
						.getSiglaMoeda() != null
						&& castOther.getSiglaMoeda() != null && this
						.getSiglaMoeda().equals(castOther.getSiglaMoeda())))
				&& ((this.getDataAtualizacao() == castOther
						.getDataAtualizacao()) || (this.getDataAtualizacao() != null
						&& castOther.getDataAtualizacao() != null && this
						.getDataAtualizacao().equals(
								castOther.getDataAtualizacao())))
				&& ((this.getData() == castOther.getData()) || (this.getData() != null
						&& castOther.getData() != null && this.getData()
						.equals(castOther.getData())));
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getKey() == null ? 0 : this.getKey().hashCode());
		result = 37
				* result
				+ (getCodigoMoeda() == null ? 0 : this.getCodigoMoeda()
						.hashCode());
		result = 37
				* result
				+ (getDataIndice() == null ? 0 : this.getDataIndice()
						.hashCode());
		result = 37 * result
				+ (getValorTaxa() == null ? 0 : this.getValorTaxa().hashCode());
		result = 37
				* result
				+ (getSiglaMoeda() == null ? 0 : this.getSiglaMoeda()
						.hashCode());
		result = 37
				* result
				+ (getDataAtualizacao() == null ? 0 : this.getDataAtualizacao()
						.hashCode());
		result = 37 * result
				+ (getData() == null ? 0 : this.getData().hashCode());
		return result;
	}

}
