/*
 * @(#) ReceitaMoeda.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * Entity gerado a partir da tabela RECEITA_MOEDA.
 * 
 * @author Generated by Hibernate Tools 3.6.0
 * @since Sep 28, 2012 11:39:11 AM
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "RECEITA_MOEDA")
@NamedQueries({
		@NamedQuery(name = "ReceitaMoeda.findAll", query = "SELECT t FROM ReceitaMoeda t"),

		@NamedQuery(name = "ReceitaMoeda.findByPeriodoAndContratoPratica", query = "SELECT t FROM ReceitaMoeda t "
				+ " WHERE t.receita.dataMes BETWEEN :dataInicio AND :dataFim "
				+ " AND t.receita.contratoPratica.codigoContratoPratica = :codigoContratoPratica"),

		@NamedQuery(name = "ReceitaMoeda.findByReceita", query = "SELECT t FROM ReceitaMoeda t "
				+ "WHERE t.receita.codigoReceita = ? "),

		@NamedQuery(name = "ReceitaMoeda.findByClobAndMoedaAndDataMes", query = "SELECT t FROM ReceitaMoeda t "
				+ " WHERE t.receita.contratoPratica.codigoContratoPratica = :codigoContratoPratica "
				+ " AND t.receita.dataMes = :dataMes "
				+ " AND t.moeda.codigoMoeda = :codigoMoeda "
				+ " AND t.receita.indicadorVersao != 'FC' ")
})
public class ReceitaMoeda implements java.io.Serializable {

	// ========================================================================
	// BEGIN - Coloque aqui o codigo manual
	// ========================================================================

	/**
	 * Constante para NamedQuery "ReceitaMoeda.findByPeriodoAndContratoPratica".
	 */
	public static final String FIND_BY_PERIODO_AND_CONTRATO_PRATICA = "ReceitaMoeda.findByPeriodoAndContratoPratica";

	/** Constante para NamedQuery "ReceitaMoeda.findByReceita". */
	public static final String FIND_BY_RECEITA = "ReceitaMoeda.findByReceita";

	/** Consntante para NamedQuery "ReceitaMoeda.findByClobAndMoedaAndDataMes". */
	public static final String FIND_BY_CLOB_AND_MOEDA_AND_DATAMES = "ReceitaMoeda.findByClobAndMoedaAndDataMes";

	/**
	 * Lista de Itens de Receita.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "receitaMoeda", cascade = CascadeType.ALL)
	private List<ItemReceita> itemReceitas = new ArrayList<ItemReceita>(0);

	/**
	 * Relacionamento um pra muitos.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "receitaMoeda", cascade = CascadeType.ALL)
	private List<ReceitaDealFiscal> receitaDealFiscals = new ArrayList<ReceitaDealFiscal>(
			0);

	/** Ajusta da ReceitaMoeda. */
	@Transient
	private Double totalAjuste;

	/**  */
	@Transient
	private Double totalReceitaPlantao;

	// ========================================================================
	// END
	// ========================================================================

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "ReceitaMoeda.findAll".
	 */
	public static final String FIND_ALL = "ReceitaMoeda.findAll";

	/**
	 * Atributo gerado a partir da coluna REMO_CD_RECEITA_MOEDA.
	 */
	@Id
	@GeneratedValue(generator = "ReceitaMoedaSeq")
	@SequenceGenerator(name = "ReceitaMoedaSeq", sequenceName = "SQ_REMO_CD_RECEITA_MOEDA", allocationSize = 1)
	@Column(name = "REMO_CD_RECEITA_MOEDA", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoReceitaMoeda;

	/**
	 * Atributo gerado a partir da coluna MOED_CD_MOEDA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOED_CD_MOEDA", nullable = false)
	private Moeda moeda;

	/**
	 * Atributo gerado a partir da coluna RECE_CD_RECEITA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECE_CD_RECEITA", nullable = false)
	private Receita receita;

	/**
	 * Atributo gerado a partir da coluna REMO_VL_TOTAL_MOEDA.
	 */
	@Audited
	@Column(name = "REMO_VL_TOTAL_MOEDA", precision = 22, scale = 0)
	private BigDecimal valorTotalMoeda = BigDecimal.ZERO;

	/**
	 * Atributo gerado a partir da coluna REMO_PR_IMPOSTO.
	 */
	@Audited
	@Column(name = "REMO_PR_IMPOSTO", precision = 22, scale = 0)
	private BigDecimal percentualImposto = BigDecimal.ZERO;

	/**
	 * Atributo gerado a partir da coluna COMO_CD_COTACAO_MOEDA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMO_CD_COTACAO_MOEDA")
	private CotacaoMoeda cotacaoMoeda;

	/**
	 * Construtor default.
	 */
	public ReceitaMoeda() {
	}

	/**
	 * Obtem o valor do atributo codigoReceitaMoeda.<BR>
	 * Atributo gerado a partir da coluna REMO_CD_RECEITA_MOEDA.
	 * 
	 * @return Valor do atributo codigoReceitaMoeda.
	 */
	public Long getCodigoReceitaMoeda() {
		return this.codigoReceitaMoeda;
	}

	/**
	 * Atualiza o valor do atributo codigoReceitaMoeda.<BR>
	 * Atributo gerado a partir da coluna REMO_CD_RECEITA_MOEDA.
	 * 
	 * @param codigoReceitaMoeda
	 *            Novo valor para o atributo codigoReceitaMoeda.
	 */
	public void setCodigoReceitaMoeda(final Long codigoReceitaMoeda) {
		this.codigoReceitaMoeda = codigoReceitaMoeda;
	}

	/**
	 * Obtem o valor do atributo moeda.<BR>
	 * Atributo gerado a partir da coluna MOED_CD_MOEDA.
	 * 
	 * @return Valor do atributo moeda.
	 */
	public Moeda getMoeda() {
		return this.moeda;
	}

	/**
	 * Atualiza o valor do atributo moeda.<BR>
	 * Atributo gerado a partir da coluna MOED_CD_MOEDA.
	 * 
	 * @param moeda
	 *            Novo valor para o atributo moeda.
	 */
	public void setMoeda(final Moeda moeda) {
		this.moeda = moeda;
	}

	/**
	 * Obtem o valor do atributo receita.<BR>
	 * Atributo gerado a partir da coluna RECE_CD_RECEITA.
	 * 
	 * @return Valor do atributo receita.
	 */
	public Receita getReceita() {
		return this.receita;
	}

	/**
	 * Atualiza o valor do atributo receita.<BR>
	 * Atributo gerado a partir da coluna RECE_CD_RECEITA.
	 * 
	 * @param receita
	 *            Novo valor para o atributo receita.
	 */
	public void setReceita(final Receita receita) {
		this.receita = receita;
	}

	/**
	 * Obtem o valor do atributo valorTotalMoeda.<BR>
	 * Atributo gerado a partir da coluna REMO_VL_TOTAL_MOEDA.
	 * 
	 * @return Valor do atributo valorTotalMoeda.
	 */
	public BigDecimal getValorTotalMoeda() {
		return this.valorTotalMoeda;
	}

	/**
	 * Atualiza o valor do atributo valorTotalMoeda.<BR>
	 * Atributo gerado a partir da coluna REMO_VL_TOTAL_MOEDA.
	 * 
	 * @param valorTotalMoeda
	 *            Novo valor para o atributo valorTotalMoeda.
	 */
	public void setValorTotalMoeda(final BigDecimal valorTotalMoeda) {
		this.valorTotalMoeda = valorTotalMoeda;
	}

	/**
	 * Obtem o valor do atributo percentualImposto.<BR>
	 * Atributo gerado a partir da coluna REMO_PR_IMPOSTO.
	 * 
	 * @return Valor do atributo percentualImposto.
	 */
	public BigDecimal getPercentualImposto() {
		return this.percentualImposto;
	}

	/**
	 * Atualiza o valor do atributo percentualImposto.<BR>
	 * Atributo gerado a partir da coluna REMO_PR_IMPOSTO.
	 * 
	 * @param percentualImposto
	 *            Novo valor para o atributo percentualImposto.
	 */
	public void setPercentualImposto(final BigDecimal percentualImposto) {
		this.percentualImposto = percentualImposto;
	}

	/**
	 * Obtem o valor do atributo cotacaoMoeda.<BR>
	 * Atributo gerado a partir da coluna COMO_CD_COTACAO_MOEDA.
	 * 
	 * @return Valor do atributo cotacaoMoeda.
	 */
	public CotacaoMoeda getCotacaoMoeda() {
		return this.cotacaoMoeda;
	}

	/**
	 * Atualiza o valor do atributo cotacaoMoeda.<BR>
	 * Atributo gerado a partir da coluna COMO_CD_COTACAO_MOEDA.
	 * 
	 * @param cotacaoMoeda
	 *            Novo valor para o atributo cotacaoMoeda.
	 */
	public void setCotacaoMoeda(final CotacaoMoeda cotacaoMoeda) {
		this.cotacaoMoeda = cotacaoMoeda;
	}

	/**
	 * Obtem o valor do atributo itemReceitas.<BR>
	 * 
	 * @return Valor do atributo itemReceitas.
	 */
	public List<ItemReceita> getItemReceitas() {
		return this.itemReceitas;
	}

	/**
	 * Atualiza o valor do atributo itemReceitas.<BR>
	 * 
	 * @param itemReceitas
	 *            Novo valor para o atributo itemReceitas.
	 */
	public void setItemReceitas(final List<ItemReceita> itemReceitas) {
		this.itemReceitas = itemReceitas;
	}

	/**
	 * @return the receitaDealFiscals
	 */
	public List<ReceitaDealFiscal> getReceitaDealFiscals() {
		return receitaDealFiscals;
	}

	/**
	 * @param receitaDealFiscals
	 *            the receitaDealFiscals to set
	 */
	public void setReceitaDealFiscals(
			final List<ReceitaDealFiscal> receitaDealFiscals) {
		this.receitaDealFiscals = receitaDealFiscals;
	}

	/**
	 * @see Object#toString()
	 * @return string
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoReceitaMoeda").append("='")
				.append(getCodigoReceitaMoeda()).append("' ");
		buffer.append("moeda").append("='").append(getMoeda()).append("' ");
		buffer.append("receita").append("='").append(getReceita()).append("' ");
		buffer.append("valorTotalMoeda").append("='")
				.append(getValorTotalMoeda()).append("' ");
		buffer.append("percentualImposto").append("='")
				.append(getPercentualImposto()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

	/**
	 * @return the totalAjuste
	 */
	public Double getTotalAjuste() {
		return totalAjuste;
	}

	/**
	 * @param totalAjuste
	 *            the totalAjuste to set
	 */
	public void setTotalAjuste(final Double totalAjuste) {
		this.totalAjuste = totalAjuste;
	}

	/**
	 * @return the totalReceitaPlantao
	 */
	public Double getTotalReceitaPlantao() {
		return totalReceitaPlantao;
	}

	/**
	 * @param totalReceitaPlantao the totalReceitaPlantao to set
	 */
	public void setTotalReceitaPlantao(Double totalReceitaPlantao) {
		this.totalReceitaPlantao = totalReceitaPlantao;
	}

}
