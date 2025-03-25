/*
 * @(#) FollowGrupoCusto.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

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

/**
 * Entity gerado a partir da tabela FOLLOW_GRUPO_CUSTO.
 * 
 * @author Generated by Hibernate Tools 3.6.0
 * @since 23/04/2013 10:36:37
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "FOLLOW_GRUPO_CUSTO")
@NamedQueries({
		@NamedQuery(name = "FollowGrupoCusto.findAll", query = "SELECT t FROM FollowGrupoCusto t"),
		@NamedQuery(name = "FollowGrupoCusto.findByGrupoCusto", query = "SELECT t FROM FollowGrupoCusto t "
				+ " WHERE t.grupoCusto.codigoGrupoCusto = ?"),
		@NamedQuery(name = "FollowGrupoCusto.findByLogin", query = "SELECT t FROM FollowGrupoCusto t "
				+ " WHERE t.codigoLogin = ?"),
		@NamedQuery(name = "FollowGrupoCusto.findByGrupoCustoAndLogin", query = "SELECT t FROM FollowGrupoCusto t "
				+ "WHERE t.grupoCusto.codigoGrupoCusto = ? "
				+ "AND t.codigoLogin = ? ") })
public class FollowGrupoCusto implements java.io.Serializable {

	
	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "FollowGrupoCusto.findAll".
	 */
	public static final String FIND_ALL = "FollowGrupoCusto.findAll";

	/**
	 * Constante para NamedQuery "FollowGrupoCusto.findByGrupoCusto".
	 */
	public static final String FIND_BY_GRUPO_CUSTO = "FollowGrupoCusto.findByGrupoCusto";

	/**
	 * Constante para NamedQuery "FollowGrupoCusto.findByLogin".
	 */
	public static final String FIND_BY_LOGIN = "FollowGrupoCusto.findByLogin";
	
	/**
	 * Constante para NamedQuery "FollowGrupoCusto.findByGrupoCustoAndLogin".
	 */
	public static final String FIND_BY_GRUPO_CUSTO_AND_LOGIN = "FollowGrupoCusto.findByGrupoCustoAndLogin";

	/**
	 * Atributo gerado a partir da coluna FOCL_CD_FOLLOW_GRUPO_CUSTO.
	 */
	@Id
	@GeneratedValue(generator = "FollowGrupoCustoSeq")
	@SequenceGenerator(name = "FollowGrupoCustoSeq", sequenceName = "SQ_FOGC_CD_FOLLOW_GRUPO_CUSTO", allocationSize = 1)
	@Column(name = "FOGC_CD_FOLLOW_GRUPO_CUSTO", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoFollowGrupoCusto;

	/**
	 * Atributo gerado a partir da coluna GRCU_CD_GRUPO_CUSTO.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRCU_CD_GRUPO_CUSTO", nullable = false)
	private GrupoCusto grupoCusto;

	/**
	 * Atributo gerado a partir da coluna PESS_CD_LOGIN.
	 */

	@Column(name = "PESS_CD_LOGIN", length = 100)
	private String codigoLogin;

	/**
	 * Construtor default.
	 */
	public FollowGrupoCusto() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoFollowGrupoCusto
	 *            Valor do atributo codigoFollowGrupoCusto;
	 * @param grupoCusto
	 *            Valor do atributo grupoCusto;
	 */
	public FollowGrupoCusto(Long codigoFollowGrupoCusto, GrupoCusto grupoCusto) {
		this.codigoFollowGrupoCusto = codigoFollowGrupoCusto;
		this.grupoCusto = grupoCusto;
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoFollowGrupoCusto
	 *            Valor do atributo codigoFollowGrupoCusto;
	 * @param grupoCusto
	 *            Valor do atributo grupoCusto;
	 */
	public FollowGrupoCusto(Long codigoFollowGrupoCusto, GrupoCusto grupoCusto,
			String codigoLogin) {
		this.codigoFollowGrupoCusto = codigoFollowGrupoCusto;
		this.grupoCusto = grupoCusto;
		this.codigoLogin = codigoLogin;
	}

	/**
	 * Obtem o valor do atributo codigoFollowGrupoCusto.<BR>
	 * Atributo gerado a partir da coluna FOCL_CD_FOLLOW_GRUPO_CUSTO.
	 * 
	 * @return Valor do atributo codigoFollowGrupoCusto.
	 */
	public Long getCodigoFollowGrupoCusto() {
		return this.codigoFollowGrupoCusto;
	}

	/**
	 * Atualiza o valor do atributo codigoFollowGrupoCusto.<BR>
	 * Atributo gerado a partir da coluna FOCL_CD_FOLLOW_GRUPO_CUSTO.
	 * 
	 * @param codigoFollowGrupoCusto
	 *            Novo valor para o atributo codigoFollowGrupoCusto.
	 */
	public void setCodigoFollowGrupoCusto(Long codigoFollowGrupoCusto) {
		this.codigoFollowGrupoCusto = codigoFollowGrupoCusto;
	}

	/**
	 * Obtem o valor do atributo grupoCusto.<BR>
	 * Atributo gerado a partir da coluna GRCU_CD_GRUPO_CUSTO.
	 * 
	 * @return Valor do atributo grupoCusto.
	 */
	public GrupoCusto getGrupoCusto() {
		return this.grupoCusto;
	}

	/**
	 * Atualiza o valor do atributo grupoCusto.<BR>
	 * Atributo gerado a partir da coluna GRCU_CD_GRUPO_CUSTO.
	 * 
	 * @param grupoCusto
	 *            Novo valor para o atributo grupoCusto.
	 */
	public void setGrupoCusto(GrupoCusto grupoCusto) {
		this.grupoCusto = grupoCusto;
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
	public void setCodigoLogin(String codigoLogin) {
		this.codigoLogin = codigoLogin;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoFollowGrupoCusto").append("='")
				.append(getCodigoFollowGrupoCusto()).append("' ");
		buffer.append("grupoCusto").append("='").append(getGrupoCusto())
				.append("' ");
		buffer.append("codigoLogin").append("='").append(getCodigoLogin())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}
