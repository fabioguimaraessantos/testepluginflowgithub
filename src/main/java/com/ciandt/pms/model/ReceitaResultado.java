/*
 * @(#) ReceitaResultado.java
 * Copyright (c) 2008 Ci&T Software S/A.
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
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * Entity gerado a partir da tabela RECEITA_RESULTADO.
 * 
 * @author Generated by Hibernate Tools 3.6.0
 * @since 29/10/2012 13:37:15
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "RECEITA_RESULTADO", uniqueConstraints = @UniqueConstraint(columnNames = {
		"COPR_CD_CONTRATO_PRATICA", "MOED_CD_MOEDA", "RERE_DT_MES" }))
@NamedQueries({
		@NamedQuery(name = "ReceitaResultado.findAll", query = "SELECT t FROM ReceitaResultado t"),
		@NamedQuery(name = "ReceitaResultado.findByContratoAndMoedaAndDate", query = "SELECT t FROM ReceitaResultado t "
				+ " WHERE t.contratoPratica.codigoContratoPratica = ? AND t.dataMes = ? "
				+ "  AND t.moeda.codigoMoeda = ? ") })
public class ReceitaResultado implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "ReceitaResultado.findAll".
	 */
	public static final String FIND_ALL = "ReceitaResultado.findAll";

	/**
	 * Constante para NamedQuery
	 * "ReceitaResultado.findByContratoAndMoedaAndDate".
	 */
	public static final String FIND_BY_CONTRATO_AND_MOEDA_AND_DATE = "ReceitaResultado.findByContratoAndMoedaAndDate";

	/**
	 * Atributo gerado a partir da coluna RERE_CD_RECEITA_RESULTADO.
	 */
	@Id
	@GeneratedValue(generator = "ReceitaResultadoSeq")
	@SequenceGenerator(name = "ReceitaResultadoSeq", sequenceName = "SQ_RERE_CD_RECEITA_RESULTADO", allocationSize = 1)
	@Column(name = "RERE_CD_RECEITA_RESULTADO", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoReceitaResultado;

	/**
	 * Atributo gerado a partir da coluna MOED_CD_MOEDA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOED_CD_MOEDA", nullable = false)
	private Moeda moeda;

	/**
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COPR_CD_CONTRATO_PRATICA", nullable = false)
	private ContratoPratica contratoPratica;

	/**
	 * Atributo gerado a partir da coluna MORE_CD_MOTIVO_RESULTADO.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MORE_CD_MOTIVO_RESULTADO")
	private MotivoResultado motivoResultado;

	/**
	 * Atributo gerado a partir da coluna RERE_DT_MES.
	 */
	@Audited
	@Temporal(TemporalType.DATE)
	@Column(name = "RERE_DT_MES", length = 7)
	private Date dataMes;

	/**
	 * Atributo gerado a partir da coluna RERE_TX_OBSERVACAO.
	 */
	@Audited
	@Column(name = "RERE_TX_OBSERVACAO", length = 200)
	private String textoObservacao;

	/**
	 * Atributo gerado a partir da coluna RERE_NR_FTE_PLANEJADO.
	 */
	@Audited
	@Column(name = "RERE_NR_FTE_PLANEJADO", precision = 22, scale = 0)
	private BigDecimal numeroFtePlanejado;

	/**
	 * Atributo gerado a partir da coluna RERE_VL_RECEITA_PLANEJADA.
	 */
	@Audited
	@Column(name = "RERE_VL_RECEITA_PLANEJADA", precision = 22, scale = 0)
	private BigDecimal valorReceitaPlanejada;

	/**
	 * Atributo gerado a partir da coluna RERE_VL_RECEITA_REALIZADA.
	 */
	@Audited
	@Column(name = "RERE_VL_RECEITA_REALIZADA", precision = 22, scale = 0)
	private BigDecimal valorReceitaRealizada;

	/**
	 * Atributo gerado a partir da coluna RERE_DT_FOTO.
	 */
	@Audited
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RERE_DT_FOTO", length = 7)
	private Date dataFoto;

	/**
	 * Atributo gerado a partir da coluna RERE_DT_ATUALIZACAO.
	 */
	@Audited
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RERE_DT_ATUALIZACAO", length = 7)
	private Date dataAtualizacao;

	/**
	 * Construtor default.
	 */
	public ReceitaResultado() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoReceitaResultado
	 *            Valor do atributo codigoReceitaResultado;
	 * @param moeda
	 *            Valor do atributo moeda;
	 * @param contratoPratica
	 *            Valor do atributo contratoPratica;
	 * @param motivoResultado
	 *            Valor do atributo motivoResultado;
	 */
	public ReceitaResultado(final Long codigoReceitaResultado,
			final Moeda moeda, final ContratoPratica contratoPratica,
			final MotivoResultado motivoResultado) {
		this.codigoReceitaResultado = codigoReceitaResultado;
		this.moeda = moeda;
		this.contratoPratica = contratoPratica;
		this.motivoResultado = motivoResultado;
	}

	/**
	 * Obtem o valor do atributo codigoReceitaResultado.<BR>
	 * Atributo gerado a partir da coluna RERE_CD_RECEITA_RESULTADO.
	 * 
	 * @return Valor do atributo codigoReceitaResultado.
	 */
	public Long getCodigoReceitaResultado() {
		return this.codigoReceitaResultado;
	}

	/**
	 * Atualiza o valor do atributo codigoReceitaResultado.<BR>
	 * Atributo gerado a partir da coluna RERE_CD_RECEITA_RESULTADO.
	 * 
	 * @param codigoReceitaResultado
	 *            Novo valor para o atributo codigoReceitaResultado.
	 */
	public void setCodigoReceitaResultado(final Long codigoReceitaResultado) {
		this.codigoReceitaResultado = codigoReceitaResultado;
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
	public void setContratoPratica(final ContratoPratica contratoPratica) {
		this.contratoPratica = contratoPratica;
	}

	/**
	 * Obtem o valor do atributo motivoResultado.<BR>
	 * Atributo gerado a partir da coluna MORE_CD_MOTIVO_RESULTADO.
	 * 
	 * @return Valor do atributo motivoResultado.
	 */
	public MotivoResultado getMotivoResultado() {
		return this.motivoResultado;
	}

	/**
	 * Atualiza o valor do atributo motivoResultado.<BR>
	 * Atributo gerado a partir da coluna MORE_CD_MOTIVO_RESULTADO.
	 * 
	 * @param motivoResultado
	 *            Novo valor para o atributo motivoResultado.
	 */
	public void setMotivoResultado(final MotivoResultado motivoResultado) {
		this.motivoResultado = motivoResultado;
	}

	/**
	 * Obtem o valor do atributo dataMes.<BR>
	 * Atributo gerado a partir da coluna RERE_DT_MES.
	 * 
	 * @return Valor do atributo dataMes.
	 */
	public Date getDataMes() {
		return this.dataMes;
	}

	/**
	 * Atualiza o valor do atributo dataMes.<BR>
	 * Atributo gerado a partir da coluna RERE_DT_MES.
	 * 
	 * @param dataMes
	 *            Novo valor para o atributo dataMes.
	 */
	public void setDataMes(final Date dataMes) {
		this.dataMes = dataMes;
	}

	/**
	 * Obtem o valor do atributo textoObservacao.<BR>
	 * Atributo gerado a partir da coluna RERE_TX_OBSERVACAO.
	 * 
	 * @return Valor do atributo textoObservacao.
	 */
	public String getTextoObservacao() {
		return this.textoObservacao;
	}

	/**
	 * Atualiza o valor do atributo textoObservacao.<BR>
	 * Atributo gerado a partir da coluna RERE_TX_OBSERVACAO.
	 * 
	 * @param textoObservacao
	 *            Novo valor para o atributo textoObservacao.
	 */
	public void setTextoObservacao(final String textoObservacao) {
		this.textoObservacao = textoObservacao;
	}

	/**
	 * Obtem o valor do atributo numeroFtePlanejado.<BR>
	 * Atributo gerado a partir da coluna RERE_NR_FTE_PLANEJADO.
	 * 
	 * @return Valor do atributo numeroFtePlanejado.
	 */
	public BigDecimal getNumeroFtePlanejado() {
		return this.numeroFtePlanejado;
	}

	/**
	 * Atualiza o valor do atributo numeroFtePlanejado.<BR>
	 * Atributo gerado a partir da coluna RERE_NR_FTE_PLANEJADO.
	 * 
	 * @param numeroFtePlanejado
	 *            Novo valor para o atributo numeroFtePlanejado.
	 */
	public void setNumeroFtePlanejado(final BigDecimal numeroFtePlanejado) {
		this.numeroFtePlanejado = numeroFtePlanejado;
	}

	/**
	 * Obtem o valor do atributo valorReceitaPlanejada.<BR>
	 * Atributo gerado a partir da coluna RERE_VL_RECEITA_PLANEJADA.
	 * 
	 * @return Valor do atributo valorReceitaPlanejada.
	 */
	public BigDecimal getValorReceitaPlanejada() {
		return this.valorReceitaPlanejada;
	}

	/**
	 * Atualiza o valor do atributo valorReceitaPlanejada.<BR>
	 * Atributo gerado a partir da coluna RERE_VL_RECEITA_PLANEJADA.
	 * 
	 * @param valorReceitaPlanejada
	 *            Novo valor para o atributo valorReceitaPlanejada.
	 */
	public void setValorReceitaPlanejada(final BigDecimal valorReceitaPlanejada) {
		this.valorReceitaPlanejada = valorReceitaPlanejada;
	}

	/**
	 * Obtem o valor do atributo valorReceitaRealizada.<BR>
	 * Atributo gerado a partir da coluna RERE_VL_RECEITA_REALIZADA.
	 * 
	 * @return Valor do atributo valorReceitaRealizada.
	 */
	public BigDecimal getValorReceitaRealizada() {
		return this.valorReceitaRealizada;
	}

	/**
	 * Atualiza o valor do atributo valorReceitaRealizada.<BR>
	 * Atributo gerado a partir da coluna RERE_VL_RECEITA_REALIZADA.
	 * 
	 * @param valorReceitaRealizada
	 *            Novo valor para o atributo valorReceitaRealizada.
	 */
	public void setValorReceitaRealizada(final BigDecimal valorReceitaRealizada) {
		this.valorReceitaRealizada = valorReceitaRealizada;
	}

	/**
	 * Obtem o valor do atributo dataFoto.<BR>
	 * Atributo gerado a partir da coluna RERE_DT_FOTO.
	 * 
	 * @return Valor do atributo dataFoto.
	 */
	public Date getDataFoto() {
		return this.dataFoto;
	}

	/**
	 * Atualiza o valor do atributo dataFoto.<BR>
	 * Atributo gerado a partir da coluna RERE_DT_FOTO.
	 * 
	 * @param dataFoto
	 *            Novo valor para o atributo dataFoto.
	 */
	public void setDataFoto(final Date dataFoto) {
		this.dataFoto = dataFoto;
	}

	/**
	 * Obtem o valor do atributo dataAtualizacao.<BR>
	 * Atributo gerado a partir da coluna RERE_DT_ATUALIZACAO.
	 * 
	 * @return Valor do atributo dataAtualizacao.
	 */
	public Date getDataAtualizacao() {
		return this.dataAtualizacao;
	}

	/**
	 * Atualiza o valor do atributo dataAtualizacao.<BR>
	 * Atributo gerado a partir da coluna RERE_DT_ATUALIZACAO.
	 * 
	 * @param dataAtualizacao
	 *            Novo valor para o atributo dataAtualizacao.
	 */
	public void setDataAtualizacao(final Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoReceitaResultado").append("='")
				.append(getCodigoReceitaResultado()).append("' ");
		buffer.append("moeda").append("='").append(getMoeda()).append("' ");
		buffer.append("motivoResultado").append("='")
				.append(getMotivoResultado()).append("' ");
		buffer.append("dataMes").append("='").append(getDataMes()).append("' ");
		buffer.append("textoObservacao").append("='")
				.append(getTextoObservacao()).append("' ");
		buffer.append("numeroFtePlanejado").append("='")
				.append(getNumeroFtePlanejado()).append("' ");
		buffer.append("valorReceitaPlanejada").append("='")
				.append(getValorReceitaPlanejada()).append("' ");
		buffer.append("valorReceitaRealizada").append("='")
				.append(getValorReceitaRealizada()).append("' ");
		buffer.append("dataFoto").append("='").append(getDataFoto())
				.append("' ");
		buffer.append("dataAtualizacao").append("='")
				.append(getDataAtualizacao()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}
