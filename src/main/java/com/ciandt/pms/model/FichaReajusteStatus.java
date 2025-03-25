/*
 * @(#) FichaReajusteStatus.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity gerado a partir da tabela FICHA_REAJUSTE_STATUS.
 *
 * @author Generated by Hibernate Tools 3.6.0
 * @since 06/12/2013 16:56:59
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "FICHA_REAJUSTE_STATUS")
@NamedQueries({
	@NamedQuery(name = "FichaReajusteStatus.findAll", query = "SELECT t FROM FichaReajusteStatus t"),
	@NamedQuery(name = "FichaReajusteStatus.findAllActive", query = "SELECT t FROM FichaReajusteStatus t"
			+ " WHERE t.indicadorAtivo = :indicadorAtivo"),
	@NamedQuery(name = "FichaReajusteStatus.findBySiglaFichaReajusteStatus", query = "SELECT t FROM FichaReajusteStatus t"
			+ " WHERE t.siglaFichaReajusteStatus = :siglaFichaReajusteStatus")
})
public class FichaReajusteStatus implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * Constante para NamedQuery "FichaReajusteStatus.findAll".
	 */
	public static final String FIND_ALL = "FichaReajusteStatus.findAll";
	
	/** 
	 * Constante para NamedQuery "FichaReajusteStatus.findAllActive".
	 */
	public static final String FIND_ALL_ACTIVE = "FichaReajusteStatus.findAllActive";
	
	/** 
	 * Constante para NamedQuery "FichaReajusteStatus.findBySiglaFichaReajusteStatus".
	 */
	public static final String FIND_BY_SIGLA_FICHA_REAJUSTE_STATUS = "FichaReajusteStatus.findBySiglaFichaReajusteStatus";

	/**
	 * Atributo gerado a partir da coluna FIRS_CD_FICHA_REAJUSTE_STATUS.
	 */
	@Id
	@GeneratedValue(generator = "FichaReajusteStatusSeq")
	@SequenceGenerator(name = "FichaReajusteStatusSeq", sequenceName = "SQ_FIRS_CD_FICHA_REAJUSTE_STATUS", allocationSize = 1)
	@Column(name = "FIRS_CD_FICHA_REAJUSTE_STATUS", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoFichaReajusteStatus;

	/**
	 * Atributo gerado a partir da coluna FIRS_NM_FICHA_REAJUSTES_STATUS.
	 */

	@Column(name = "FIRS_NM_FICHA_REAJUSTES_STATUS", length = 200)
	private String nomeFichaReajustesStatus;

	/**
	 * Atributo gerado a partir da coluna FIRS_SG_FICHA_REAJUSTE_STATUS.
	 */

	@Column(name = "FIRS_SG_FICHA_REAJUSTE_STATUS", length = 2)
	private String siglaFichaReajusteStatus;

	/**
	 * Atributo gerado a partir da coluna FIRS_IN_ATIVO.
	 */

	@Column(name = "FIRS_IN_ATIVO", length = 1)
	private String indicadorAtivo;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "fichaReajusteStatus")
	private Set<FichaReajuste> fichaReajustes = new HashSet<FichaReajuste>(0);

	/**
	 * Construtor default.
	 */
	public FichaReajusteStatus() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * @param codigoFichaReajusteStatus Valor do atributo codigoFichaReajusteStatus;
	 */
	public FichaReajusteStatus(Long codigoFichaReajusteStatus) {
		this.codigoFichaReajusteStatus = codigoFichaReajusteStatus;
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * @param codigoFichaReajusteStatus Valor do atributo codigoFichaReajusteStatus;
	 */
	public FichaReajusteStatus(Long codigoFichaReajusteStatus,
			String nomeFichaReajustesStatus, String siglaFichaReajusteStatus,
			String indicadorAtivo, Set<FichaReajuste> fichaReajustes) {
		this.codigoFichaReajusteStatus = codigoFichaReajusteStatus;
		this.nomeFichaReajustesStatus = nomeFichaReajustesStatus;
		this.siglaFichaReajusteStatus = siglaFichaReajusteStatus;
		this.indicadorAtivo = indicadorAtivo;
		this.fichaReajustes = fichaReajustes;
	}

	/**
	 * Obtem o valor do atributo codigoFichaReajusteStatus.<BR>
	 * Atributo gerado a partir da coluna FIRS_CD_FICHA_REAJUSTE_STATUS.
	 * @return Valor do atributo codigoFichaReajusteStatus.
	 */
	public Long getCodigoFichaReajusteStatus() {
		return this.codigoFichaReajusteStatus;
	}

	/**
	 * Atualiza o valor do atributo codigoFichaReajusteStatus.<BR>
	 * Atributo gerado a partir da coluna FIRS_CD_FICHA_REAJUSTE_STATUS.
	 * @param codigoFichaReajusteStatus Novo valor para o atributo codigoFichaReajusteStatus.
	 */
	public void setCodigoFichaReajusteStatus(Long codigoFichaReajusteStatus) {
		this.codigoFichaReajusteStatus = codigoFichaReajusteStatus;
	}

	/**
	 * Obtem o valor do atributo nomeFichaReajustesStatus.<BR>
	 * Atributo gerado a partir da coluna FIRS_NM_FICHA_REAJUSTES_STATUS.
	 * @return Valor do atributo nomeFichaReajustesStatus.
	 */
	public String getNomeFichaReajustesStatus() {
		return this.nomeFichaReajustesStatus;
	}

	/**
	 * Atualiza o valor do atributo nomeFichaReajustesStatus.<BR>
	 * Atributo gerado a partir da coluna FIRS_NM_FICHA_REAJUSTES_STATUS.
	 * @param nomeFichaReajustesStatus Novo valor para o atributo nomeFichaReajustesStatus.
	 */
	public void setNomeFichaReajustesStatus(String nomeFichaReajustesStatus) {
		this.nomeFichaReajustesStatus = nomeFichaReajustesStatus;
	}

	/**
	 * Obtem o valor do atributo siglaFichaReajusteStatus.<BR>
	 * Atributo gerado a partir da coluna FIRS_SG_FICHA_REAJUSTE_STATUS.
	 * @return Valor do atributo siglaFichaReajusteStatus.
	 */
	public String getSiglaFichaReajusteStatus() {
		return this.siglaFichaReajusteStatus;
	}

	/**
	 * Atualiza o valor do atributo siglaFichaReajusteStatus.<BR>
	 * Atributo gerado a partir da coluna FIRS_SG_FICHA_REAJUSTE_STATUS.
	 * @param siglaFichaReajusteStatus Novo valor para o atributo siglaFichaReajusteStatus.
	 */
	public void setSiglaFichaReajusteStatus(String siglaFichaReajusteStatus) {
		this.siglaFichaReajusteStatus = siglaFichaReajusteStatus;
	}

	/**
	 * Obtem o valor do atributo indicadorAtivo.<BR>
	 * Atributo gerado a partir da coluna FIRS_IN_ATIVO.
	 * @return Valor do atributo indicadorAtivo.
	 */
	public String getIndicadorAtivo() {
		return this.indicadorAtivo;
	}

	/**
	 * Atualiza o valor do atributo indicadorAtivo.<BR>
	 * Atributo gerado a partir da coluna FIRS_IN_ATIVO.
	 * @param indicadorAtivo Novo valor para o atributo indicadorAtivo.
	 */
	public void setIndicadorAtivo(String indicadorAtivo) {
		this.indicadorAtivo = indicadorAtivo;
	}

	/**
	 * Obtem o valor do atributo fichaReajustes.<BR>

	 * @return Valor do atributo fichaReajustes.
	 */
	public Set<FichaReajuste> getFichaReajustes() {
		return this.fichaReajustes;
	}

	/**
	 * Atualiza o valor do atributo fichaReajustes.<BR>

	 * @param fichaReajustes Novo valor para o atributo fichaReajustes.
	 */
	public void setFichaReajustes(Set<FichaReajuste> fichaReajustes) {
		this.fichaReajustes = fichaReajustes;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoFichaReajusteStatus").append("='")
				.append(getCodigoFichaReajusteStatus()).append("' ");
		buffer.append("nomeFichaReajustesStatus").append("='")
				.append(getNomeFichaReajustesStatus()).append("' ");
		buffer.append("siglaFichaReajusteStatus").append("='")
				.append(getSiglaFichaReajusteStatus()).append("' ");
		buffer.append("indicadorAtivo").append("='")
				.append(getIndicadorAtivo()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}
