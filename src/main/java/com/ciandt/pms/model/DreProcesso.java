/*
 * @(#) DreProcesso.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity gerado a partir da tabela DRE_PROCESSO.
 * 
 * @author Generated by Hibernate Tools 3.6.0
 * @since 10/10/2013 16:22:55
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "DRE_PROCESSO")
@NamedQueries({
		@NamedQuery(name = "DreProcesso.findAll", query = "SELECT t FROM DreProcesso t"),

		@NamedQuery(name = "DreProcesso.findLastByCdProcessoDataAndIndPorLogin", query = "SELECT dp FROM"
				+ " DreProcesso dp"
				+ " WHERE"
				+ " dp.processo.codigoProcesso = :codigoProcesso"
				+ " AND dp.indicadorPorLogin = :indicadorPorLogin"
				+ " AND dp.dreMes.codigoDreMes = ("
				+ " 	SELECT m.codigoDreMes FROM DreMes m WHERE to_char(m.dataMes, 'MM/yyyy') = :monthYear)"
				+ " AND dp.dataInicio = ("
				+ "		SELECT MAX(t.dataInicio) FROM"
				+ " 	DreProcesso t"
				+ " 	JOIN t.dreMes dm"
				+ " 	WHERE to_char(dm.dataMes, 'MM/yyyy') = :monthYear"
				+ " 	AND t.processo.codigoProcesso = :codigoProcesso"
				+ "     AND t.indicadorPorLogin = :indicadorPorLogin)"),

		@NamedQuery(name = "DreProcesso.findAllByDreMes", query = "SELECT t FROM DreProcesso t WHERE t.dreMes.codigoDreMes = :codigoDreMes") })
public class DreProcesso implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "DreProcesso.findAll".
	 */
	public static final String FIND_ALL = "DreProcesso.findAll";

	/**
	 * Constante para NamedQuery "DreProcesso.findLastByCdProcessoDataAndIndPorLogin".
	 */
	public static final String FIND_lAST_BY_PROCESSO_DATA_AND_IND_POR_LOGIN = "DreProcesso.findLastByCdProcessoDataAndIndPorLogin";

	/**
	 * Constante para NamedQuery "DreProcesso.findAllByDreMes".
	 */
	public static final String FIND_ALL_BY_DRE_MES = "DreProcesso.findAllByDreMes";

	/**
	 * Atributo gerado a partir da coluna DREP_CD_DRE_PROCESSO.
	 */
	@Id
	@GeneratedValue(generator = "DreProcessoSeq")
	@SequenceGenerator(name = "DreProcessoSeq", sequenceName = "SQ_DREP_CD_DRE_PROCESSO", allocationSize = 1)
	@Column(name = "DREP_CD_DRE_PROCESSO", nullable = false, precision = 18, scale = 0)
	private Long codigoDreProcesso;

	/**
	 * Atributo gerado a partir da coluna DREM_CD_DRE_MES.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DREM_CD_DRE_MES", nullable = false)
	private DreMes dreMes;

	/**
	 * Atributo gerado a partir da coluna PROC_CD_PROCESSO.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PROC_CD_PROCESSO", nullable = false)
	private Processo processo;

	/**
	 * Atributo gerado a partir da coluna DREP_CD_LOGIN.
	 */

	@Column(name = "DREP_CD_LOGIN", nullable = false, length = 100)
	private String codigoLogin;

	/**
	 * Atributo gerado a partir da coluna DREP_DT_INICIO.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DREP_DT_INICIO", nullable = false, length = 7)
	private Date dataInicio;

	/**
	 * Atributo gerado a partir da coluna DREP_IN_STATUS.
	 * 
	 * <li>"PE" - Performed</li> <li>"IP" - In Progress</li> <li>"IN" -
	 * Invalidated</li> <li>"ER" - Error</li> <li>"LK" - Locked</li> <li>"NP" -
	 * Not Performed</li>
	 */

	@Column(name = "DREP_IN_STATUS", nullable = false, length = 1)
	private String indicadorStatus;

	/**
	 * Atributo gerado a partir da coluna DREP_IN_POR_LOGIN.
	 * 
	 * <li>"S" - Processo executado para um mes/ano, especificando login</li>
	 * <li>
	 * "N" - Processo executado para um mes/ano, sem especificar um login</li>
	 */

	@Column(name = "DREP_IN_POR_LOGIN", nullable = false, length = 1)
	private String indicadorPorLogin;

	/**
	 * Atributo gerado a partir da coluna DREP_TX_LOG.
	 */

	@Lob
	@Column(name = "DREP_TX_LOG")
	@Basic(fetch = FetchType.LAZY)
	private String textoLog;

	/**
	 * Construtor default.
	 */
	public DreProcesso() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoDreProcesso
	 *            Valor do atributo codigoDreProcesso;
	 * @param dreMes
	 *            Valor do atributo dreMes;
	 * @param processo
	 *            Valor do atributo processo;
	 * @param codigoLogin
	 *            Valor do atributo codigoLogin;
	 * @param dataInicio
	 *            Valor do atributo dataInicio;
	 * @param indicadorStatus
	 *            Valor do atributo indicadorStatus;
	 * @param indicadorPorLogin
	 *            Valor do atributo indicadorPorLogin;
	 */
	public DreProcesso(Long codigoDreProcesso, DreMes dreMes,
			Processo processo, String codigoLogin, Date dataInicio,
			String indicadorStatus, String indicadorPorLogin) {
		this.codigoDreProcesso = codigoDreProcesso;
		this.dreMes = dreMes;
		this.processo = processo;
		this.codigoLogin = codigoLogin;
		this.dataInicio = dataInicio;
		this.indicadorStatus = indicadorStatus;
		this.indicadorPorLogin = indicadorPorLogin;
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * 
	 * @param codigoDreProcesso
	 *            Valor do atributo codigoDreProcesso;
	 * @param dreMes
	 *            Valor do atributo dreMes;
	 * @param processo
	 *            Valor do atributo processo;
	 * @param codigoLogin
	 *            Valor do atributo codigoLogin;
	 * @param dataInicio
	 *            Valor do atributo dataInicio;
	 * @param indicadorStatus
	 *            Valor do atributo indicadorStatus;
	 * @param indicadorPorLogin
	 *            Valor do atributo indicadorPorLogin;
	 */
	public DreProcesso(Long codigoDreProcesso, DreMes dreMes,
			Processo processo, String codigoLogin, Date dataInicio,
			String indicadorStatus, String textoLog, String indicadorPorLogin) {
		this.codigoDreProcesso = codigoDreProcesso;
		this.dreMes = dreMes;
		this.processo = processo;
		this.codigoLogin = codigoLogin;
		this.dataInicio = dataInicio;
		this.indicadorStatus = indicadorStatus;
		this.indicadorPorLogin = indicadorPorLogin;
		this.textoLog = textoLog;
	}

	/**
	 * Obtem o valor do atributo codigoDreProcesso.<BR>
	 * Atributo gerado a partir da coluna DREP_CD_DRE_PROCESSO.
	 * 
	 * @return Valor do atributo codigoDreProcesso.
	 */
	public Long getCodigoDreProcesso() {
		return this.codigoDreProcesso;
	}

	/**
	 * Atualiza o valor do atributo codigoDreProcesso.<BR>
	 * Atributo gerado a partir da coluna DREP_CD_DRE_PROCESSO.
	 * 
	 * @param codigoDreProcesso
	 *            Novo valor para o atributo codigoDreProcesso.
	 */
	public void setCodigoDreProcesso(Long codigoDreProcesso) {
		this.codigoDreProcesso = codigoDreProcesso;
	}

	/**
	 * Obtem o valor do atributo dreMes.<BR>
	 * Atributo gerado a partir da coluna DREM_CD_DRE_MES.
	 * 
	 * @return Valor do atributo dreMes.
	 */
	public DreMes getDreMes() {
		return this.dreMes;
	}

	/**
	 * Atualiza o valor do atributo dreMes.<BR>
	 * Atributo gerado a partir da coluna DREM_CD_DRE_MES.
	 * 
	 * @param dreMes
	 *            Novo valor para o atributo dreMes.
	 */
	public void setDreMes(DreMes dreMes) {
		this.dreMes = dreMes;
	}

	/**
	 * Obtem o valor do atributo processo.<BR>
	 * Atributo gerado a partir da coluna PROC_CD_PROCESSO.
	 * 
	 * @return Valor do atributo processo.
	 */
	public Processo getProcesso() {
		return this.processo;
	}

	/**
	 * Atualiza o valor do atributo processo.<BR>
	 * Atributo gerado a partir da coluna PROC_CD_PROCESSO.
	 * 
	 * @param processo
	 *            Novo valor para o atributo processo.
	 */
	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	/**
	 * Obtem o valor do atributo codigoLogin.<BR>
	 * Atributo gerado a partir da coluna DREP_CD_LOGIN.
	 * 
	 * @return Valor do atributo codigoLogin.
	 */
	public String getCodigoLogin() {
		return this.codigoLogin;
	}

	/**
	 * Atualiza o valor do atributo codigoLogin.<BR>
	 * Atributo gerado a partir da coluna DREP_CD_LOGIN.
	 * 
	 * @param codigoLogin
	 *            Novo valor para o atributo codigoLogin.
	 */
	public void setCodigoLogin(String codigoLogin) {
		this.codigoLogin = codigoLogin;
	}

	/**
	 * Obtem o valor do atributo dataInicio.<BR>
	 * Atributo gerado a partir da coluna DREP_DT_INICIO.
	 * 
	 * @return Valor do atributo dataInicio.
	 */
	public Date getDataInicio() {
		return this.dataInicio;
	}

	/**
	 * Atualiza o valor do atributo dataInicio.<BR>
	 * Atributo gerado a partir da coluna DREP_DT_INICIO.
	 * 
	 * @param dataInicio
	 *            Novo valor para o atributo dataInicio.
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * Obtem o valor do atributo indicadorStatus.<BR>
	 * Atributo gerado a partir da coluna DREP_IN_STATUS.
	 * 
	 * @return Valor do atributo indicadorStatus: <li>"PE" - Performed</li> <li>
	 *         "IP" - In Progress</li> <li>"IN" - Invalidated</li> <li>"ER" -
	 *         Error</li> <li>"LK" - Locked</li> <li>"NP" - Not Performed</li>
	 */
	public String getIndicadorStatus() {
		return this.indicadorStatus;
	}

	/**
	 * Atualiza o valor do atributo indicadorStatus.<BR>
	 * Atributo gerado a partir da coluna DREP_IN_STATUS.
	 * 
	 * @param indicadorStatus
	 *            Novo valor para o atributo indicadorStatus: <li>"PE" -
	 *            Performed</li> <li>
	 *            "IP" - In Progress</li> <li>"IN" - Invalidated</li> <li>"ER" -
	 *            Error</li> <li>"LK" - Locked</li> <li>"NP" - Not Performed</li>
	 */
	public void setIndicadorStatus(String indicadorStatus) {
		this.indicadorStatus = indicadorStatus;
	}

	/**
	 * Obtem o valor do atributo indicadorPorLogin.<BR>
	 * Atributo gerado a partir da coluna DREP_IN_POR_LOGIN.
	 * 
	 * @return Valor do atributo indicadorPorLogin: <li>"S" - Processo executado
	 *         para um mes/ano, especificando login</li> <li>
	 *         "N" - Processo executado para um mes/ano, sem especificar um
	 *         login</li>
	 */
	public String getIndicadorPorLogin() {
		return indicadorPorLogin;
	}

	/**
	 * Atualiza o valor do atributo indicadorPorLogin.<BR>
	 * Atributo gerado a partir da coluna DREP_IN_POR_LOGIN.
	 * 
	 * @param indicadorPorLogin
	 *            Novo valor para o atributo indicadorPorLogin: <li>"S" -
	 *            Processo executado para um mes/ano, especificando login</li>
	 *            <li>
	 *            "N" - Processo executado para um mes/ano, sem especificar um
	 *            login</li>
	 */
	public void setIndicadorPorLogin(String indicadorPorLogin) {
		this.indicadorPorLogin = indicadorPorLogin;
	}

	/**
	 * Obtem o valor do atributo textoLog.<BR>
	 * Atributo gerado a partir da coluna DREP_TX_LOG.
	 * 
	 * @return Valor do atributo textoLog.
	 */
	public String getTextoLog() {
		return this.textoLog;
	}

	/**
	 * Atualiza o valor do atributo textoLog.<BR>
	 * Atributo gerado a partir da coluna DREP_TX_LOG.
	 * 
	 * @param textoLog
	 *            Novo valor para o atributo textoLog.
	 */
	public void setTextoLog(String textoLog) {
		this.textoLog = textoLog;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoDreProcesso").append("='")
				.append(getCodigoDreProcesso()).append("' ");
		buffer.append("dreMes").append("='").append(getDreMes()).append("' ");
		buffer.append("processo").append("='").append(getProcesso())
				.append("' ");
		buffer.append("codigoLogin").append("='").append(getCodigoLogin())
				.append("' ");
		buffer.append("dataInicio").append("='").append(getDataInicio())
				.append("' ");
		buffer.append("indicadorStatus").append("='")
				.append(getIndicadorStatus()).append("' ");
		buffer.append("textoLog").append("='").append(getTextoLog())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}
