/*
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity gerado a partir da tabela RECEITA_TIPO.
 *
 * @author Generated by Hibernate Tools 3.4.0.CR1
 * @since 11/08/2014 14:55:05
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "RECEITA_TIPO")
@NamedQueries({
	@NamedQuery(name = "ReceitaTipo.findAll", query = "SELECT t FROM ReceitaTipo t"),
	@NamedQuery(name = "ReceitaTipo.findByNomeReceitaTipo", query = "SELECT rt FROM ReceitaTipo rt WHERE rt.nomeReceitaTipo = :nomeReceitaTipo"),
	@NamedQuery(name = "ReceitaTipo.findBySiglaReceitaTipo", query = "SELECT rt FROM ReceitaTipo rt WHERE rt.siglaReceitaTipo = :siglaReceitaTipo")
})
public class ReceitaTipo implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * Constante para NamedQuery "ReceitaTipo.findAll".
	 */
	public static final String FIND_ALL = "ReceitaTipo.findAll";
	
	/** 
	 * Constante para NamedQuery "ReceitaTipo.findAll".
	 */
	public static final String FIND_BY_NOME_RECEITA_TIPO = "ReceitaTipo.findByNomeReceitaTipo";
	
	/** 
	 * Constante para NamedQuery "ReceitaTipo.findAll".
	 */
	public static final String FIND_BY_SIGLA_RECEITA_TIPO = "ReceitaTipo.findBySiglaReceitaTipo";

	/**
	 * Atributo gerado a partir da coluna RETI_CD_TIPO_RECEITA.
	 */
	@Id
	@GeneratedValue(generator = "ReceitaTipoSeq")
	@SequenceGenerator(name = "ReceitaTipoSeq", sequenceName = "SQ_RETI_CD_RECEITA_TIPO", allocationSize = 1)
	@Column(name = "RETI_CD_TIPO_RECEITA", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoReceitaTipo;

	/**
	 * Atributo gerado a partir da coluna RETI_NM_TIPO_RECEITA.
	 */
	@Column(name = "RETI_NM_TIPO_RECEITA", length = 100)
	private String nomeReceitaTipo;

	/**
	 * Atributo gerado a partir da coluna RETI_SG_TIPO_RECEITA.
	 */
	@Column(name = "RETI_SG_TIPO_RECEITA", length = 2)
	private String siglaReceitaTipo;

	public ReceitaTipo() {
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * @param codigoTipoDocumentoLegal Valor do atributo codigoTipoDocumentoLegal;
	 */
	public ReceitaTipo(Long codigoTipoDocumentoLegal) {
		this.codigoReceitaTipo = codigoTipoDocumentoLegal;
	}

	/**
	 * Construtor com preenchimento da entidade.
	 * @param codigoReceitaTipo Valor do atributo codigoTipoDocumentoLegal;
	 */
	public ReceitaTipo(Long codigoReceitaTipo,
			String nomeReceitaTipo, String siglaReceitaTipo,
			String indicadorAtivo, Set<Receita> receitas) {
		this.codigoReceitaTipo = codigoReceitaTipo;
		this.nomeReceitaTipo = nomeReceitaTipo;
		this.siglaReceitaTipo = siglaReceitaTipo;
	}

	/**
	 * @return the codigoReceitaTipo
	 */
	public Long getCodigoReceitaTipo() {
		return codigoReceitaTipo;
	}

	/**
	 * @param codigoReceitaTipo the codigoReceitaTipo to set
	 */
	public void setCodigoReceitaTipo(Long codigoReceitaTipo) {
		this.codigoReceitaTipo = codigoReceitaTipo;
	}

	/**
	 * @return the nomeReceitaTipo
	 */
	public String getNomeReceitaTipo() {
		return nomeReceitaTipo;
	}

	/**
	 * @param nomeReceitaTipo the nomeReceitaTipo to set
	 */
	public void setNomeReceitaTipo(String nomeReceitaTipo) {
		this.nomeReceitaTipo = nomeReceitaTipo;
	}

	/**
	 * @return the siglaReceitaTipo
	 */
	public String getSiglaReceitaTipo() {
		return siglaReceitaTipo;
	}

	/**
	 * @param siglaReceitaTipo the siglaReceitaTipo to set
	 */
	public void setSiglaReceitaTipo(String siglaReceitaTipo) {
		this.siglaReceitaTipo = siglaReceitaTipo;
	}

}
