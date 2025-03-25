/*
 * @(#) VwFiscalBalance.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ciandt.pms.model.vo.DealFiscalFiscalBalanceRow;

/**
 * Entity gerado a partir da tabela VW_FISCAL_BALANCE.
 * 
 * @author Generated by Hibernate Tools 3.4.0.CR1
 * @since Jun 26, 2014 6:22:36 PM
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "VW_PMS_CONTRATO_PROFIT_CENTER")
@NamedQueries({
		@NamedQuery(name = "VwPmsContratoProfitCenter.findAll", query = "SELECT t FROM VwPmsContratoProfitCenter t"),

})

public class VwPmsContratoProfitCenter implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "VwFiscalBalanceAcumulado.findAll";

	// CLIE_NM_CLIENTE, C.COPR_CD_CONTRATO_PRATICA, C.COPR_NM_CONTRATO_PRATICA, C.PRAT_NM_PRATICA, C.UMKT, C.SSO, C.BUSINESS_MANAGER
	@Id
	@Column(name = "CD_CONTRATO_PROFIT_CENTER", precision = 22, scale = 0)
	private Long codigoContratoProfitCenter;

	@Column(name = "CLIE_NM_CLIENTE")
	private String cliente;
	
	@Column(name = "COPR_NM_CONTRATO_PRATICA")
	private String nomeContratoPratica;
	
	@Column(name = "PRAT_NM_PRATICA")
	private String nomePratica;
	
	@Column(name = "UMKT")
	private String nomeUmkt;
	
	@Column(name = "SSO")
	private String nomeSso;
	
	@Column(name = "BUSINESS_MANAGER")
	private String loginBusinessManager;

	@Column(name = "DT_VALOR", length = 7)
	private Date dtValor;
	
	@Column(name = "MOED_SG_MOEDA")
	private String siglaMoeda;
	
	@Column(name = "VL_MAPA")
	private Double valorMapa;
	
	@Column(name = "VL_RECEITA")
	private Double valorReceita;

	@Column(name = "TIPO_RECEITA")
	private String tipoReceita;

	/**
	 * @return the codigoContratoProfitCenter
	 */
	public Long getCodigoContratoProfitCenter() {
		return codigoContratoProfitCenter;
	}

	/**
	 * @param codigoContratoProfitCenter the codigoContratoProfitCenter to set
	 */
	public void setCodigoContratoProfitCenter(Long codigoContratoProfitCenter) {
		this.codigoContratoProfitCenter = codigoContratoProfitCenter;
	}

	/**
	 * @return the cliente
	 */
	public String getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the nomeContratoPratica
	 */
	public String getNomeContratoPratica() {
		return nomeContratoPratica;
	}

	/**
	 * @param nomeContratoPratica the nomeContratoPratica to set
	 */
	public void setNomeContratoPratica(String nomeContratoPratica) {
		this.nomeContratoPratica = nomeContratoPratica;
	}

	/**
	 * @return the nomePratica
	 */
	public String getNomePratica() {
		return nomePratica;
	}

	/**
	 * @param nomePratica the nomePratica to set
	 */
	public void setNomePratica(String nomePratica) {
		this.nomePratica = nomePratica;
	}

	/**
	 * @return the nomeUmkt
	 */
	public String getNomeUmkt() {
		return nomeUmkt;
	}

	/**
	 * @param nomeUmkt the nomeUmkt to set
	 */
	public void setNomeUmkt(String nomeUmkt) {
		this.nomeUmkt = nomeUmkt;
	}

	/**
	 * @return the nomeSso
	 */
	public String getNomeSso() {
		return nomeSso;
	}

	/**
	 * @param nomeSso the nomeSso to set
	 */
	public void setNomeSso(String nomeSso) {
		this.nomeSso = nomeSso;
	}

	/**
	 * @return the loginBusinessManager
	 */
	public String getLoginBusinessManager() {
		return loginBusinessManager;
	}

	/**
	 * @param loginBusinessManager the loginBusinessManager to set
	 */
	public void setLoginBusinessManager(String loginBusinessManager) {
		this.loginBusinessManager = loginBusinessManager;
	}

	/**
	 * @return the data
	 */
	public Date getDtValor() {
		return dtValor;
	}

	/**
	 * @param data the data to set
	 */
	public void setDtValor(Date dtValor) {
		this.dtValor = dtValor;
	}

	/**
	 * @return the siglaMoeda
	 */
	public String getSiglaMoeda() {
		return siglaMoeda;
	}

	/**
	 * @param siglaMoeda the siglaMoeda to set
	 */
	public void setSiglaMoeda(String siglaMoeda) {
		this.siglaMoeda = siglaMoeda;
	}

	/**
	 * @return the valorMapa
	 */
	public Double getValorMapa() {
		return valorMapa;
	}

	/**
	 * @param valorMapa the valorMapa to set
	 */
	public void setValorMapa(Double valorMapa) {
		this.valorMapa = valorMapa;
	}

	/**
	 * @return the valorReceita
	 */
	public Double getValorReceita() {
		return valorReceita;
	}

	/**
	 * @param valorReceita the valorReceita to set
	 */
	public void setValorReceita(Double valorReceita) {
		this.valorReceita = valorReceita;
	}

	/**
	 * @return the tipoReceita
	 */
	public String getTipoReceita() {
		return tipoReceita;
	}

	/**
	 * @param tipoReceita the tipoReceita to set
	 */
	public void setTipoReceita(String tipoReceita) {
		this.tipoReceita = tipoReceita;
	}

}