/*
 * @(#) FollowOrcamentoDesp.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.ArrayList;
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

import org.hibernate.annotations.Where;

import com.ciandt.pms.Constants;

/**
 * Entity gerado a partir da tabela FOLLOW_ORCAMENTO_DESP.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 26/07/2012 16:07:51
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "FOLLOW_ORCAMENTO_DESP")
@Where(clause = "ORDE_CD_ORCA_DESPESA = (SELECT O.ORDE_CD_ORCA_DESPESA FROM PMS20.ORC_DESPESA O "
		+ "WHERE O.ORDE_CD_ORCA_DESPESA = ORDE_CD_ORCA_DESPESA AND O.ORDE_IN_DELETE_LOGICO = 'N')")
@NamedQueries({
		@NamedQuery(name = "FollowOrcamentoDesp.findAll", query = "SELECT t FROM FollowOrcamentoDesp t"),
		@NamedQuery(name = "FollowOrcamentoDesp.findByOrcamentoDespesaAndLogin", query = "SELECT t FROM FollowOrcamentoDesp t "
				+ "WHERE t.orcamentoDespesa.codigoOrcaDespesa = ? "
				+ "AND t.codigoLogin = ? "),
		@NamedQuery(name = "FollowOrcamentoDesp.findByLogin", query = "SELECT t FROM FollowOrcamentoDesp t "
				+ "WHERE t.codigoLogin = ? "),
		@NamedQuery(name = "FollowOrcamentoDesp.findByOrcDespesa", query = "SELECT t FROM FollowOrcamentoDesp t "
				+ "WHERE t.orcamentoDespesa.codigoOrcaDespesa = ? "),
		@NamedQuery(name = "FollowOrcamentoDesp.findByPessoaAndDateOfOpenVoucher", query = "SELECT f FROM FollowOrcamentoDesp f"
				+ " JOIN f.orcamentoDespesa od"
				+ " JOIN f.pessoas p"
				+ " JOIN od.vouchers v"
				+ " WHERE v.dataCreation < :date"
				+ " AND v.indicadorStatus = '" + Constants.STATUS_VOUCHER_OPEN + "'"
				+ " AND p.codigoPessoa = :codigoPessoa")
})
public class FollowOrcamentoDesp implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "FollowOrcamentoDesp.findAll".
	 */
	public static final String FIND_ALL = "FollowOrcamentoDesp.findAll";

	/**
	 * Constante para NamedQuery
	 * "FollowOrcamentoDesp.findByOrcamentoDespesaAndLogin".
	 */
	public static final String FIND_BY_ORC_DESPESA_AND_LOGIN = "FollowOrcamentoDesp.findByOrcamentoDespesaAndLogin";

	/**
	 * Constante para NamedQuery "FollowOrcamentoDesp.findByLogin".
	 */
	public static final String FIND_BY_LOGIN = "FollowOrcamentoDesp.findByLogin";

	/**
	 * Constante para NamedQuery "FollowOrcamentoDesp.findByOrcDespesa".
	 */
	public static final String FIND_BY_ORC_DESPESA = "FollowOrcamentoDesp.findByOrcDespesa";

	/**
	 * Constante para NamedQuery "FollowOrcamentoDesp.findByPessoaAndDateOfOpenVoucher".
	 */
	public static final String FIND_BY_PESSOA_AND_DATE_OF_OPEN_VOUCHER = "FollowOrcamentoDesp.findByPessoaAndDateOfOpenVoucher";

	/**
	 * Atributo gerado a partir da coluna FOOD_CD_FOLLOW_ORC_DESP.
	 */
	@Id
	@GeneratedValue(generator = "FollowOrcamentoDespSeq")
	@SequenceGenerator(name = "FollowOrcamentoDespSeq", sequenceName = "SQ_FOOD_CD_FOLLOW_ORC_DESP", allocationSize = 1)
	@Column(name = "FOOD_CD_FOLLOW_ORC_DESP", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoFollowOrcDesp;

	/**
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORDE_CD_ORCA_DESPESA", nullable = false)
	private OrcamentoDespesa orcamentoDespesa;

	/**
	 * Atributo gerado a partir da coluna PESS_CD_LOGIN.
	 */

	@Column(name = "PESS_CD_LOGIN", length = 100)
	private String codigoLogin;

	/**
	 * Atributo gerado a partir da tabela PESSOA.
	 */
	@OneToMany
	@JoinColumn(name = "PESS_CD_LOGIN", nullable = false)
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

	/**
	 * Construtor default.
	 */
	public FollowOrcamentoDesp() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 *
	 * @param codigoFollowOrcDesp
	 *            Valor do atributo codigoFollowOrcDesp;
	 * @param orcamentoDespesa
	 *            Valor do atributo orcamentoDespesa;
	 */
	public FollowOrcamentoDesp(final Long codigoFollowOrcDesp,
							   final OrcamentoDespesa orcamentoDespesa) {
		this.codigoFollowOrcDesp = codigoFollowOrcDesp;
		this.orcamentoDespesa = orcamentoDespesa;
	}

	/**
	 * Construtor com preenchimento da entidade.
	 *
	 * @param codigoFollowOrcDesp
	 *            Valor do atributo codigoFollowOrcDesp;
	 * @param orcamentoDespesa
	 *            Valor do atributo orcamentoDespesa;
	 * @param codigoLogin
	 *            Valor do atributo codigoLogin;
	 */
	public FollowOrcamentoDesp(final Long codigoFollowOrcDesp,
							   final OrcamentoDespesa orcamentoDespesa, final String codigoLogin) {
		this.codigoFollowOrcDesp = codigoFollowOrcDesp;
		this.orcamentoDespesa = orcamentoDespesa;
		this.codigoLogin = codigoLogin;
	}

	/**
	 * Obtem o valor do atributo codigoFollowOrcDesp.<BR>
	 * Atributo gerado a partir da coluna FOOD_CD_FOLLOW_ORC_DESP.
	 *
	 * @return Valor do atributo codigoFollowOrcDesp.
	 */
	public Long getCodigoFollowOrcDesp() {
		return this.codigoFollowOrcDesp;
	}

	/**
	 * Atualiza o valor do atributo codigoFollowOrcDesp.<BR>
	 * Atributo gerado a partir da coluna FOOD_CD_FOLLOW_ORC_DESP.
	 *
	 * @param codigoFollowOrcDesp
	 *            Novo valor para o atributo codigoFollowOrcDesp.
	 */
	public void setCodigoFollowOrcDesp(final Long codigoFollowOrcDesp) {
		this.codigoFollowOrcDesp = codigoFollowOrcDesp;
	}

	/**
	 * Obtem o valor do atributo orcamentoDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 *
	 * @return Valor do atributo orcamentoDespesa.
	 */
	public OrcamentoDespesa getOrcamentoDespesa() {
		return this.orcamentoDespesa;
	}

	/**
	 * Atualiza o valor do atributo orcamentoDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 *
	 * @param orcamentoDespesa
	 *            Novo valor para o atributo orcamentoDespesa.
	 */
	public void setOrcamentoDespesa(final OrcamentoDespesa orcamentoDespesa) {
		this.orcamentoDespesa = orcamentoDespesa;
	}

	/**
	 * Obtem o valor do atributo codigoLogin.<BR>
	 * Atributo gerado a partir da coluna PESS_CD_LOGIN.
	 *
	 * @return Valor do atributo codigoLogin.
	 */
	public String getCodigoLogin() {
		return this.codigoLogin;
	}

	/**
	 * Atualiza o valor do atributo codigoLogin.<BR>
	 * Atributo gerado a partir da coluna PESS_CD_LOGIN.
	 *
	 * @param codigoLogin
	 *            Novo valor para o atributo codigoLogin.
	 */
	public void setCodigoLogin(final String codigoLogin) {
		this.codigoLogin = codigoLogin;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(final List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	/**
	 * @see Object#toString()
	 * @return buffer.
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoFollowOrcDesp").append("='")
				.append(getCodigoFollowOrcDesp()).append("' ");
		buffer.append("orcamentoDespesa").append("='")
				.append(getOrcamentoDespesa()).append("' ");
		buffer.append("codigoLogin").append("='").append(getCodigoLogin())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}
