/*
 * @(#) OrcDespesaMes.java
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

/**
 * Entity gerado a partir da tabela ORC_DESPESA_MES.
 * 
 * @author Generated by Hibernate Tools 3.4.0.CR1
 * @since 10/10/2013 16:33:17
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "ORC_DESPESA_MES")
@NamedQueries({
		@NamedQuery(name = "OrcDespesaMes.findAll", query = "SELECT t FROM OrcDespesaMes t"),
		@NamedQuery(name = "OrcDespesaMes.sumPercMesByClobAndPeriod", query = "SELECT odm FROM OrcDespesaMes odm "
				+ "INNER JOIN odm.orcDespesa od "
				+ "INNER JOIN od.orcDespesaCls odc "
				+ "INNER JOIN odc.contratoPraticaOrcDespCls cpodc "
				+ "WHERE cpodc.contratoPratica.codigoContratoPratica = ? "
				+ "AND od.indicadorAtivo = 'S' "
				+ "AND od.indicadorDeleteLogico = 'N' "
				+ "AND odc.indicadorReembolsavel = 'NM' "
				+ "AND odm.dataMes >= to_date(?, 'mm/yyyy') "
				+ "AND odm.dataMes <= to_date(?, 'mm/yyyy') ")
		})
public class OrcDespesaMes implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "OrcDespesaMes.findAll".
	 */
	public static final String FIND_ALL = "OrcDespesaMes.findAll";

	/**
	 * Constante para NamedQuery "OrcDespesaMes.sumPercMesByClobAndPeriod".
	 */
	public static final String FIND_SUM_PERC_MES_BY_CLOB_AND_PERIOD = "OrcDespesaMes.sumPercMesByClobAndPeriod";

	/**
	 * Atributo gerado a partir da coluna ORDM_CD_ORC_DESPESA_MES.
	 */
	@Id
	@GeneratedValue(generator = "OrcDespesaMesSeq")
	@SequenceGenerator(name = "OrcDespesaMesSeq", sequenceName = "SQ_ORDM_CD_ORC_DESPESA_MES", allocationSize = 1)
	@Column(name = "ORDM_CD_ORC_DESPESA_MES", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoOrcDespesaMes;

	/**
	 * Atributo gerado a partir da coluna ORDE_CD_ORC_DESPESA.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDE_CD_ORC_DESPESA")
	private OrcamentoDespesa orcDespesa;

	/**
	 * Atributo gerado a partir da coluna ORDM_DT_MES.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ORDM_DT_MES", length = 7)
	private Date dataMes;

	/**
	 * Atributo gerado a partir da coluna ORDM_PR_MES.
	 */

	@Column(name = "ORDM_PR_MES", precision = 22, scale = 0)
	private BigDecimal percentualMes;

	/**
	 * Construtor default.
	 */
	public OrcDespesaMes() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoOrcDespesaMes
	 *            Valor do atributo codigoOrcDespesaMes;
	 */
	public OrcDespesaMes(Long codigoOrcDespesaMes) {
		this.codigoOrcDespesaMes = codigoOrcDespesaMes;
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoOrcDespesaMes
	 *            Valor do atributo codigoOrcDespesaMes;
	 */
	public OrcDespesaMes(Long codigoOrcDespesaMes, OrcamentoDespesa orcDespesa,
			Date dataMes, BigDecimal percentualMes) {
		this.codigoOrcDespesaMes = codigoOrcDespesaMes;
		this.orcDespesa = orcDespesa;
		this.dataMes = dataMes;
		this.percentualMes = percentualMes;
	}

	/**
	 * Obtem o valor do atributo codigoOrcDespesaMes.<BR>
	 * Atributo gerado a partir da coluna ORDM_CD_ORC_DESPESA_MES.
	 * 
	 * @return Valor do atributo codigoOrcDespesaMes.
	 */
	public Long getCodigoOrcDespesaMes() {
		return this.codigoOrcDespesaMes;
	}

	/**
	 * Atualiza o valor do atributo codigoOrcDespesaMes.<BR>
	 * Atributo gerado a partir da coluna ORDM_CD_ORC_DESPESA_MES.
	 * 
	 * @param codigoOrcDespesaMes
	 *            Novo valor para o atributo codigoOrcDespesaMes.
	 */
	public void setCodigoOrcDespesaMes(Long codigoOrcDespesaMes) {
		this.codigoOrcDespesaMes = codigoOrcDespesaMes;
	}

	/**
	 * Obtem o valor do atributo orcDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_CD_ORC_DESPESA.
	 * 
	 * @return Valor do atributo orcDespesa.
	 */
	public OrcamentoDespesa getOrcDespesa() {
		return this.orcDespesa;
	}

	/**
	 * Atualiza o valor do atributo orcDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_CD_ORC_DESPESA.
	 * 
	 * @param orcDespesa
	 *            Novo valor para o atributo orcDespesa.
	 */
	public void setOrcDespesa(OrcamentoDespesa orcDespesa) {
		this.orcDespesa = orcDespesa;
	}

	/**
	 * Obtem o valor do atributo dataMes.<BR>
	 * Atributo gerado a partir da coluna ORDM_DT_MES.
	 * 
	 * @return Valor do atributo dataMes.
	 */
	public Date getDataMes() {
		return this.dataMes;
	}

	/**
	 * Atualiza o valor do atributo dataMes.<BR>
	 * Atributo gerado a partir da coluna ORDM_DT_MES.
	 * 
	 * @param dataMes
	 *            Novo valor para o atributo dataMes.
	 */
	public void setDataMes(Date dataMes) {
		this.dataMes = dataMes;
	}

	/**
	 * Obtem o valor do atributo percentualMes.<BR>
	 * Atributo gerado a partir da coluna ORDM_PR_MES.
	 * 
	 * @return Valor do atributo percentualMes.
	 */
	public BigDecimal getPercentualMes() {
		return this.percentualMes;
	}

	/**
	 * Atualiza o valor do atributo percentualMes.<BR>
	 * Atributo gerado a partir da coluna ORDM_PR_MES.
	 * 
	 * @param percentualMes
	 *            Novo valor para o atributo percentualMes.
	 */
	public void setPercentualMes(BigDecimal percentualMes) {
		this.percentualMes = percentualMes;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoOrcDespesaMes").append("='")
				.append(getCodigoOrcDespesaMes()).append("' ");
		buffer.append("orcDespesa").append("='").append(getOrcDespesa())
				.append("' ");
		buffer.append("dataMes").append("='").append(getDataMes()).append("' ");
		buffer.append("percentualMes").append("='").append(getPercentualMes())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}
