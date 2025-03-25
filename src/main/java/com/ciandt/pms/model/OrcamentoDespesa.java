/*
 * @(#) OrcamentoDespesa.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ciandt.pms.Constants;

/**
 * Entity gerado a partir da tabela ORCAMENTO_DESPESA.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 14/08/2012 08:22:13
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "ORC_DESPESA")
@NamedQueries({
		@NamedQuery(name = "OrcamentoDespesa.findAll", query = "SELECT t FROM OrcamentoDespesa t"),
		@NamedQuery(name = "OrcamentoDespesa.findByGrupoCustoAndActive", query = "SELECT t FROM OrcamentoDespesa t "
				+ " JOIN FETCH t.orcDespesaGcs ogc "
				+ " WHERE ogc.grupoCusto.codigoGrupoCusto = ? "
				+ " AND t.indicadorAtivo = 'S' AND ORDE_IN_DELETE_LOGICO = 'N'"
				+ " ORDER BY t.nomeOrcDespesa "),
		@NamedQuery(name = "OrcamentoDespesa.findByNameAndTipoOrcDesp", query = "SELECT t FROM OrcamentoDespesa t "
				+ " WHERE t.nomeOrcDespesa = ? AND t.tpOrcDesp = ? "
				+ " AND ORDE_IN_DELETE_LOGICO = 'N'") })
public class OrcamentoDespesa implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "OrcamentoDespesa.findAll".
	 */
	public static final String FIND_ALL = "OrcamentoDespesa.findAll";

	/**
	 * Constante para NamedQuery "OrcamentoDespesa.findByGrupoCustoAndActive".
	 */
	public static final String FIND_BY_GRUPO_CUSTO_AND_ACTIVE = "OrcamentoDespesa.findByGrupoCustoAndActive";

	/** Constante para NamedQuery "OrcamentoDespesa.findByNameAndTipoOrcDesp". */
	public static final String FIND_BY_NAME_AND_TIPO_ORC_DESP = "OrcamentoDespesa.findByNameAndTipoOrcDesp";

	/**
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 */
	@Id
	@GeneratedValue(generator = "OrcDespesaSeq")
	@SequenceGenerator(name = "OrcDespesaSeq", sequenceName = "SQ_ORDE_CD_ORCA_DESPESA", allocationSize = 1)
	@Column(name = "ORDE_CD_ORCA_DESPESA", nullable = false, precision = 18, scale = 0)
	private Long codigoOrcaDespesa;

	/**
	 * Atributo gerado a partir da coluna MOED_CD_MOEDA.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MOED_CD_MOEDA")
	private Moeda moeda;

	/**
	 * Atributo gerado a partir da coluna ORDE_NM_ORC_DESPESA.
	 */
	@Column(name = "ORDE_NM_ORC_DESPESA", length = 100)
	private String nomeOrcDespesa;

	/**
	 * Atributo gerado a partir da coluna ORDE_DT_INICIO.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ORDE_DT_INICIO", length = 7)
	private Date dataInicio;

	/**
	 * Atributo gerado a partir da coluna ORDE_DT_FIM.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ORDE_DT_FIM", length = 7)
	private Date dataFim;

	/**
	 * Atributo gerado a partir da coluna ORDE_VL_ORCADO.
	 */
	@Column(name = "ORDE_VL_ORCADO", precision = 22, scale = 0)
	private BigDecimal valorOrcado;

	/**
	 * Atributo gerado a partir da coluna ORDE_TP_ORC_DESP.
	 */
	@Column(name = "ORDE_TP_ORC_DESP", length = 2)
	private String tpOrcDesp;

	/**
	 * Atributo gerado a partir da coluna ORDE_IN_ATIVO.
	 */
	@Column(name = "ORDE_IN_ATIVO", length = 1)
	private String indicadorAtivo;

	/**
	 * Atributo gerado a partir da coluna ORDE_LOGIN_CRIADOR.
	 */
	@Column(name = "ORDE_LOGIN_CRIADOR", length = 100)
	private String loginCriador;

	/**
	 * Atributo gerado a partir da coluna ORDE_DT_SYNC.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ORDE_DT_SYNC", length = 7)
	private Date dataSync;

	/**
	 * Atributo gerado a partir da coluna ORDE_IN_SYNC.
	 */
	@Column(name = "ORDE_IN_SYNC", length = 1)
	private String indicadorSync;

	/**
	 * Atributo gerado a partir da coluna ORDE_IN_DELETE_LOGICO.
	 */
	@Column(name = "ORDE_IN_DELETE_LOGICO", length = 1, nullable = false)
	private String indicadorDeleteLogico;

	@Column(name = "ORDE_TB_PURPOSE", length = 2)
	private String tbPurpose;

	/**
	 * The followOrcamentoDesps.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orcamentoDespesa")
	private List<FollowOrcamentoDesp> followOrcamentoDesps = new ArrayList<FollowOrcamentoDesp>();

	/**
	 * The orcDespWhiteLists.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orcamentoDespesa")
	private List<OrcDespWhiteList> orcDespWhiteLists = new ArrayList<OrcDespWhiteList>();

	/**
	 * THe orcDespesaGcs.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orcamentoDespesa")
	private List<OrcDespesaGc> orcDespesaGcs = new ArrayList<OrcDespesaGc>();

	/**
	 * The ordcDespesaCls.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orcamentoDespesa")
	private List<OrcDespesaCl> orcDespesaCls = new ArrayList<OrcDespesaCl>();

	/**
	 * The vouchers.
	 */
	@OneToMany
	@JoinColumn(name = "ORDE_CD_ORCA_DESPESA", referencedColumnName = "ORDE_CD_ORCA_DESPESA")
	private List<Voucher> vouchers = new ArrayList<Voucher>();

	/**
	 * Construtor default.
	 */
	public OrcamentoDespesa() {


	}

	public OrcamentoDespesa(String tbPurpose) {
		this.tbPurpose = tbPurpose;
	}

	/**
	 * Obtem o valor do atributo codigoOrcDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 * 
	 * @return Valor do atributo codigoOrcDespesa.
	 */
	public Long getCodigoOrcaDespesa() {
		return this.codigoOrcaDespesa;
	}

	/**
	 * Atualiza o valor do atributo codigoOrcaDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 * 
	 * @param codigoOrcaDespesa
	 *            Novo valor para o atributo codigoOrcaDespesa.
	 */
	public void setCodigoOrcaDespesa(final Long codigoOrcaDespesa) {
		this.codigoOrcaDespesa = codigoOrcaDespesa;
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
	 * Obtem o valor do atributo nomeOrcDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_NM_ORC_DESPESA.
	 * 
	 * @return Valor do atributo nomeOrcDespesa.
	 */
	public String getNomeOrcDespesa() {
		return this.nomeOrcDespesa;
	}

	/**
	 * Atualiza o valor do atributo nomeOrcDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_NM_ORC_DESPESA.
	 * 
	 * @param nomeOrcDespesa
	 *            Novo valor para o atributo nomeOrcDespesa.
	 */
	public void setNomeOrcDespesa(final String nomeOrcDespesa) {
		this.nomeOrcDespesa = nomeOrcDespesa;
	}

	/**
	 * Obtem o valor do atributo dataInicio.<BR>
	 * Atributo gerado a partir da coluna ORDE_DT_INICIO.
	 * 
	 * @return Valor do atributo dataInicio.
	 */
	public Date getDataInicio() {
		return this.dataInicio;
	}

	/**
	 * Atualiza o valor do atributo dataInicio.<BR>
	 * Atributo gerado a partir da coluna ORDE_DT_INICIO.
	 * 
	 * @param dataInicio
	 *            Novo valor para o atributo dataInicio.
	 */
	public void setDataInicio(final Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * Obtem o valor do atributo dataFim.<BR>
	 * Atributo gerado a partir da coluna ORDE_DT_FIM.
	 * 
	 * @return Valor do atributo dataFim.
	 */
	public Date getDataFim() {
		return this.dataFim;
	}

	/**
	 * Atualiza o valor do atributo dataFim.<BR>
	 * Atributo gerado a partir da coluna ORDE_DT_FIM.
	 * 
	 * @param dataFim
	 *            Novo valor para o atributo dataFim.
	 */
	public void setDataFim(final Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * Obtem o valor do atributo valorOrcado.<BR>
	 * Atributo gerado a partir da coluna ORDE_VL_ORCADO.
	 * 
	 * @return Valor do atributo valorOrcado.
	 */
	public BigDecimal getValorOrcado() {
		return this.valorOrcado;
	}

	/**
	 * Atualiza o valor do atributo valorOrcado.<BR>
	 * Atributo gerado a partir da coluna ORDE_VL_ORCADO.
	 * 
	 * @param valorOrcado
	 *            Novo valor para o atributo valorOrcado.
	 */
	public void setValorOrcado(final BigDecimal valorOrcado) {
		this.valorOrcado = valorOrcado;
	}

	/**
	 * Obtem o valor do atributo indicadorAtivo.<BR>
	 * Atributo gerado a partir da coluna ORDE_IN_ATIVO.
	 * 
	 * @return Valor do atributo indicadorAtivo.
	 */
	public String getIndicadorAtivo() {
		return this.indicadorAtivo;
	}

	/**
	 * Atualiza o valor do atributo indicadorAtivo.<BR>
	 * Atributo gerado a partir da coluna ORDE_IN_ATIVO.
	 * 
	 * @param indicadorAtivo
	 *            Novo valor para o atributo indicadorAtivo.
	 */
	public void setIndicadorAtivo(final String indicadorAtivo) {
		this.indicadorAtivo = indicadorAtivo;
	}

	/**
	 * Obtem o valor do atributo loginCriador.<BR>
	 * Atributo gerado a partir da coluna ORDE_LOGIN_CRIADOR.
	 * 
	 * @return Valor do atributo loginCriador.
	 */
	public String getLoginCriador() {
		return this.loginCriador;
	}

	/**
	 * Atualiza o valor do atributo loginCriador.<BR>
	 * Atributo gerado a partir da coluna ORDE_LOGIN_CRIADOR.
	 * 
	 * @param loginCriador
	 *            Novo valor para o atributo loginCriador.
	 */
	public void setLoginCriador(final String loginCriador) {
		this.loginCriador = loginCriador;
	}

	/**
	 * Obtem o valor do atributo dataSync.<BR>
	 * Atributo gerado a partir da coluna ORDE_DT_SYNC.
	 * 
	 * @return Valor do atributo dataSync.
	 */
	public Date getDataSync() {
		return this.dataSync;
	}

	/**
	 * Atualiza o valor do atributo dataSync.<BR>
	 * Atributo gerado a partir da coluna ORDE_DT_SYNC.
	 * 
	 * @param dataSync
	 *            Novo valor para o atributo dataSync.
	 */
	public void setDataSync(final Date dataSync) {
		this.dataSync = dataSync;
	}

	/**
	 * Obtem o valor do atributo indicadorSync.<BR>
	 * Atributo gerado a partir da coluna ORDE_IN_SYNC.
	 * 
	 * @return Valor do atributo indicadorSync.
	 */
	public String getIndicadorSync() {
		return this.indicadorSync;
	}

	/**
	 * Atualiza o valor do atributo indicadorSync.<BR>
	 * Atributo gerado a partir da coluna ORDE_IN_SYNC.
	 * 
	 * @param indicadorSync
	 *            Novo valor para o atributo indicadorSync.
	 */
	public void setIndicadorSync(final String indicadorSync) {
		this.indicadorSync = indicadorSync;
	}

	/**
	 * @return the indicadorDeleteLogico
	 */
	public String getIndicadorDeleteLogico() {
		return indicadorDeleteLogico;
	}

	/**
	 * @param indicadorDeleteLogico
	 *            the indicadorDeleteLogico to set
	 */
	public void setIndicadorDeleteLogico(final String indicadorDeleteLogico) {
		this.indicadorDeleteLogico = indicadorDeleteLogico;
	}

	/**
	 * @return the tbPurpose
	 */
	public String getTbPurpose() {
		return tbPurpose;
	}

	/**
	 * @param tbPurpose
	 *            the tbPurpose to set
	 */
	public void setTbPurpose(final String tbPurpose) {
		this.tbPurpose = tbPurpose;
	}

	/**
	 * Obtem o valor do atributo followOrcamentoDesps.<BR>
	 * 
	 * @return Valor do atributo followOrcamentoDesps.
	 */
	public List<FollowOrcamentoDesp> getFollowOrcamentoDesps() {
		return this.followOrcamentoDesps;
	}

	/**
	 * Atualiza o valor do atributo followOrcamentoDesps.<BR>
	 * 
	 * @param followOrcamentoDesps
	 *            Novo valor para o atributo followOrcamentoDesps.
	 */
	public void setFollowOrcamentoDesps(
			final List<FollowOrcamentoDesp> followOrcamentoDesps) {
		this.followOrcamentoDesps = followOrcamentoDesps;
	}

	/**
	 * Obtem o valor do atributo orcDespWhiteLists.<BR>
	 * 
	 * @return Valor do atributo orcDespWhiteLists.
	 */
	public List<OrcDespWhiteList> getOrcDespWhiteLists() {
		return this.orcDespWhiteLists;
	}

	/**
	 * Atualiza o valor do atributo orcDespWhiteLists.<BR>
	 * 
	 * @param orcDespWhiteLists
	 *            Novo valor para o atributo orcDespWhiteLists.
	 */
	public void setOrcDespWhiteLists(
			final List<OrcDespWhiteList> orcDespWhiteLists) {
		this.orcDespWhiteLists = orcDespWhiteLists;
	}

	/**
	 * Obtem o valor do atributo orcDespesaGcs.<BR>
	 * 
	 * @return Valor do atributo orcDespesaGcs.
	 */
	public List<OrcDespesaGc> getOrcDespesaGcs() {
		return this.orcDespesaGcs;
	}

	/**
	 * Atualiza o valor do atributo orcDespesaGcs.<BR>
	 * 
	 * @param orcDespesaGcs
	 *            Novo valor para o atributo orcDespesaGcs.
	 */
	public void setOrcDespesaGcs(final List<OrcDespesaGc> orcDespesaGcs) {
		this.orcDespesaGcs = orcDespesaGcs;
	}

	/**
	 * Obtem o valor do atributo tpOrcDesp.<BR>
	 * Atributo gerado a partir da coluna ORDE_TP_ORC_DESP.
	 * 
	 * @return Valor do atributo tpOrcDesp.
	 */
	public String getTpOrcDesp() {
		return this.tpOrcDesp;
	}

	/**
	 * Atualiza o valor do atributo tpOrcDesp.<BR>
	 * Atributo gerado a partir da coluna ORDE_TP_ORC_DESP.
	 * 
	 * @param tpOrcDesp
	 *            Novo valor para o atributo tpOrcDesp.
	 */
	public void setTpOrcDesp(final String tpOrcDesp) {
		this.tpOrcDesp = tpOrcDesp;
	}

	/**
	 * Obtem o valor do atributo ordcDespesaCls.<BR>
	 * 
	 * @return Valor do atributo ordcDespesaCls.
	 */
	public List<OrcDespesaCl> getOrcDespesaCls() {
		return this.orcDespesaCls;
	}

	/**
	 * Atualiza o valor do atributo ordcDespesaCls.<BR>
	 * 
	 * @param ordcDespesaCls
	 *            Novo valor para o atributo ordcDespesaCls.
	 */
	public void setOrcDespesaCls(final List<OrcDespesaCl> ordcDespesaCls) {
		this.orcDespesaCls = ordcDespesaCls;
	}

	/**
	 * @return the vouchers
	 */
	public List<Voucher> getVouchers() {
		return vouchers;
	}

	/**
	 * @param vouchers
	 *            the vouchers to set
	 */
	public void setVouchers(final List<Voucher> vouchers) {
		this.vouchers = vouchers;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoOrcaDespesa").append("='")
				.append(getCodigoOrcaDespesa()).append("' ");
		buffer.append("moeda").append("='").append(getMoeda()).append("' ");
		buffer.append("nomeOrcDespesa").append("='")
				.append(getNomeOrcDespesa()).append("' ");
		buffer.append("dataInicio").append("='").append(getDataInicio())
				.append("' ");
		buffer.append("dataFim").append("='").append(getDataFim()).append("' ");
		buffer.append("valorOrcado").append("='").append(getValorOrcado())
				.append("' ");
		buffer.append("indicadorAtivo").append("='")
				.append(getIndicadorAtivo()).append("' ");
		buffer.append("loginCriador").append("='").append(getLoginCriador())
				.append("' ");
		buffer.append("dataSync").append("='").append(getDataSync())
				.append("' ");
		buffer.append("indicadorSync").append("='").append(getIndicadorSync())
				.append("' ");
		buffer.append("tbPurpose").append("='").append(getTbPurpose())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

	/**
	 * Obtem o {@link Cliente} do {@link OrcamentoDespesa}. Caso seja area de
	 * apoio, retorna null (area de apoio nao possui cliente).
	 * 
	 * @return {@link Cliente}
	 */
	public Cliente getCliente() {
		if (Constants.TRAVEL_BUDGET_CLIENT_TYPE.equals(this.getTpOrcDesp())) {
			return new ArrayList<OrcDespesaCl>(this.getOrcDespesaCls()).get(0)
					.getCliente();
		}
		return null;
	}

	/**
	 * Obtem a somatoria dos vouchers sacados.
	 * 
	 * @return somatoria
	 * 
	 */
	public BigDecimal getWithdrawnValue() {
		BigDecimal withdrawn = new BigDecimal(0);


		
		for (Voucher voucher : this.vouchers) {

			if (Constants.STATUS_VOUCHER_CLOSED.equals(voucher
					.getIndicadorStatus())
					&& voucher.getValorUsedTbCurrency() != null) {
				withdrawn = withdrawn.add(voucher.getValorUsedTbCurrency());

			} else if (Constants.STATUS_VOUCHER_USED.equals(voucher
					.getIndicadorStatus())
					&& voucher.getValorUsedTbCurrency() != null) {

				withdrawn = withdrawn.add(voucher.getValorUsedTbCurrency());

			} else if (Constants.STATUS_VOUCHER_OPEN.equals(voucher
					.getIndicadorStatus())
					&& voucher.getValorIssuedTbCurrency() != null) {
				withdrawn = withdrawn.add(voucher.getValorIssuedTbCurrency());
			} else {
				withdrawn = withdrawn.add(new BigDecimal(0));
			}
		}

		return withdrawn;
	}
}