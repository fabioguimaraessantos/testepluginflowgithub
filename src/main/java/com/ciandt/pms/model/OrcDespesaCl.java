/*
 * @(#) OrcDespesaCl.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

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

import org.hibernate.annotations.Where;

import com.ciandt.pms.Constants;

/**
 * Entity gerado a partir da tabela ORC_DESPESA_CL.
 * 
 * @author Generated by Hibernate Tools 3.6.0
 * @since 09/04/2013 17:15:47
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "ORC_DESPESA_CL")
@Where(clause = " ORDC_IN_DELETE_LOGICO = 'N' ")
@NamedQueries({
		@NamedQuery(name = "OrcDespesaCl.findAll", query = "SELECT t FROM OrcDespesaCl t"),
		@NamedQuery(name = "OrcDespesaCl.findByCliente", query = "SELECT t FROM OrcDespesaCl t "
				+ " WHERE t.cliente.codigoCliente = ? "),
		@NamedQuery(name = "OrcDespesaCl.findByOrcamentoDespesa", query = "SELECT t FROM OrcDespesaCl t "
				+ " WHERE t.orcamentoDespesa.codigoOrcaDespesa = ? "),
		@NamedQuery(name = "OrcDespesaCl.findByClientAndActive", query = "SELECT t FROM OrcDespesaCl t "
				+ " WHERE t.cliente.codigoCliente = ? "
				+ " AND t.orcamentoDespesa.indicadorAtivo = 'S' "),
		@NamedQuery(name = "OrcDespesaCl.findByNameAndActive", query = "SELECT t FROM OrcDespesaCl t "
				+ " WHERE t.orcamentoDespesa.nomeOrcDespesa = ? "
				+ " AND t.orcamentoDespesa.indicadorDeleteLogico = 'N' "
				+ " AND t.orcamentoDespesa.indicadorAtivo = 'S' "),
		@NamedQuery(name = "OrcDespesaCl.findByNameAndClienteAndActive", query = "SELECT t FROM OrcDespesaCl t "
				+ " WHERE UPPER(t.orcamentoDespesa.nomeOrcDespesa) = UPPER(?) "
				+ " AND t.cliente.codigoCliente = ? "
				+ " AND t.orcamentoDespesa.indicadorAtivo = 'S'"),
		@NamedQuery(name = "OrcDespesaCl.findOnlyValidByClient", query = "SELECT t FROM OrcDespesaCl t "
				+ " WHERE trunc(t.orcamentoDespesa.dataInicio) <= trunc(SYSDATE) "
				+ " AND trunc(t.orcamentoDespesa.dataFim)  >= trunc(SYSDATE) "
				+ " AND t.cliente.codigoCliente = ? "
				+ " AND t.orcamentoDespesa.indicadorAtivo = 'S'")		
})
public class OrcDespesaCl implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "OrcDespesaCl.findAll".
	 */
	public static final String FIND_ALL = "OrcDespesaCl.findAll";

	/**
	 * Constante para NamedQuery "OrcDespesaCl.findByCliente".
	 */
	public static final String FIND_BY_CLIENTE = "OrcDespesaCl.findByCliente";

	/**
	 * Constante para NamedQuery "OrcDespesaCl.findByClientAndActive".
	 */
	public static final String FIND_BY_CLIENT_AND_ACTIVE = "OrcDespesaCl.findByClientAndActive";

	/**
	 * Constante para NamedQuery "OrcDespesaCl.findByClientAndName".
	 */
	public static final String FIND_BY_CLIENT_AND_NAME = "OrcDespesaCl.findByClientAndName";

	/**
	 * Constante para NamedQuery "OrcDespesaCl.findByNameAndActive".
	 */
	public static final String FIND_BY_NAME_AND_ACTIVE = "OrcDespesaCl.findByNameAndActive";
	
	/**
	 * Constante para NamedQuery "OrcDespesaCl.findByOrcamentoDespesa".
	 */
	public static final String FIND_BY_ORCAMENTO_DESPESA = "OrcDespesaCl.findByOrcamentoDespesa";
	
	/**
	 * Constante para NamedQuery "OrcDespesaCl.findByNameAndClienteAndActive".
	 */
	public static final String FIND_BY_NAME_AND_CLIENT_AND_ACTIVE = "OrcDespesaCl.findByNameAndClienteAndActive";

	/**
	 * Constante para NamedQuery "OrcDespesaCl.findOnlyValidByClient".
	 */
	public static final String FIND_ONLY_VALID_BY_CLIENT = "OrcDespesaCl.findOnlyValidByClient";

	/**
	 * Atributo gerado a partir da coluna ORDC_CD_ORCA_DESP_CL.
	 */
	@Id
	@GeneratedValue(generator = "OrcDespesaClSeq")
	@SequenceGenerator(name = "OrcDespesaClSeq", sequenceName = "SQ_ORDC_CD_ORCA_DESP_CL", allocationSize = 1)
	@Column(name = "ORDC_CD_ORCA_DESP_CL", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoOrcaDespCl;

	/**
	 * Atributo gerado a partir da coluna CLIE_CD_CLIENTE.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CLIE_CD_CLIENTE", nullable = false)
	private Cliente cliente;

	/**
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORDE_CD_ORCA_DESPESA", nullable = false)
	private OrcamentoDespesa orcamentoDespesa;

	/**
	 * Atributo gerado a partir da coluna ORDC_IN_REEMBOLSAVEL.
	 */
	@Column(name = "ORDC_IN_REEMBOLSAVEL", length = 2)
	private String indicadorReembolsavel;

	/**
	 * Atributo gerado a partir do relacionamento ContratoPratica com
	 * OrcDespesaCl.
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "orcDespesaCl")
	private List<ContratoPraticaOrcDespCl> contratoPraticaOrcDespCls = new ArrayList<ContratoPraticaOrcDespCl>();

	/**
	 * Atributo gerado a partir da coluna ORDC_IN_DELETE_LOGICO.
	 */
	@Column(name = "ORDC_IN_DELETE_LOGICO", length = 1, nullable = false)
	private String indicadorDeleteLogico;
	
	/**
	 * Atributo gerado a partir da coluna ORDC_IN_TIPO_FATURA.
	 */
	@Column(name = "ORDC_IN_TIPO_FATURA", length = 2)
	private String indicadorTipoFatura;
	
	/**
	 * Atributo gerado a partir da coluna ORDC_IN_TIPO_FATURA.
	 */
	@Column(name = "ORDC_IN_ORCAMENTO_EXTRA", length = 2)
	private String indicadorOrcamentoExtra;

	/**
	 * Construtor default.
	 */
	public OrcDespesaCl() {
	}

	/**
	 * Obtem o valor do atributo codigoOrcaDespCl.<BR>
	 * Atributo gerado a partir da coluna ORDC_CD_ORCA_DESP_CL.
	 * 
	 * @return Valor do atributo codigoOrcaDespCl.
	 */
	public Long getCodigoOrcaDespCl() {
		return this.codigoOrcaDespCl;
	}

	/**
	 * Atualiza o valor do atributo codigoOrcaDespCl.<BR>
	 * Atributo gerado a partir da coluna ORDC_CD_ORCA_DESP_CL.
	 * 
	 * @param codigoOrcaDespCl
	 *            Novo valor para o atributo codigoOrcaDespCl.
	 */
	public void setCodigoOrcaDespCl(final Long codigoOrcaDespCl) {
		this.codigoOrcaDespCl = codigoOrcaDespCl;
	}

	/**
	 * Obtem o valor do atributo cliente.<BR>
	 * Atributo gerado a partir da coluna CLIE_CD_CLIENTE.
	 * 
	 * @return Valor do atributo cliente.
	 */
	public Cliente getCliente() {
		return this.cliente;
	}

	/**
	 * Atualiza o valor do atributo cliente.<BR>
	 * Atributo gerado a partir da coluna CLIE_CD_CLIENTE.
	 * 
	 * @param cliente
	 *            Novo valor para o atributo cliente.
	 */
	public void setCliente(final Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Obtem o valor do atributo orcDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 * 
	 * @return Valor do atributo orcDespesa.
	 */
	public OrcamentoDespesa getOrcamentoDespesa() {
		return this.orcamentoDespesa;
	}

	/**
	 * Atualiza o valor do atributo orcDespesa.<BR>
	 * Atributo gerado a partir da coluna ORDE_CD_ORCA_DESPESA.
	 * 
	 * @param orcDespesa
	 *            Novo valor para o atributo orcDespesa.
	 */
	public void setOrcamentoDespesa(final OrcamentoDespesa orcDespesa) {
		this.orcamentoDespesa = orcDespesa;
	}

	/**
	 * Obtem o valor do atributo indicadorReembolsavel.<BR>
	 * Atributo gerado a partir da coluna ORDC_IN_REEMBOLSAVEL.
	 * 
	 * @return Valor do atributo indicadorReembolsavel.
	 */
	public String getIndicadorReembolsavel() {
		return this.indicadorReembolsavel;
	}

	/**
	 * Atualiza o valor do atributo indicadorReembolsavel.<BR>
	 * Atributo gerado a partir da coluna ORDC_IN_REEMBOLSAVEL.
	 * 
	 * @param indicadorReembolsavel
	 *            Novo valor para o atributo indicadorReembolsavel.
	 */
	public void setIndicadorReembolsavel(final String indicadorReembolsavel) {
		this.indicadorReembolsavel = indicadorReembolsavel;
	}

	/**
	 * Obtem o valor do atributo contratoPraticaOrcDespCls.<BR>
	 * 
	 * @return Valor do atributo contratoPraticaOrcDespCls.
	 */
	public List<ContratoPraticaOrcDespCl> getContratoPraticaOrcDespCls() {
		return this.contratoPraticaOrcDespCls;
	}

	/**
	 * Atualiza o valor do atributo contratoPraticaOrcDespCls.<BR>
	 * 
	 * @param contratoPraticaOrcDespCls
	 *            Novo valor para o atributo contratoPraticaOrcDespCls.
	 */
	public void setContratoPraticaOrcDespCls(
			final List<ContratoPraticaOrcDespCl> contratoPraticaOrcDespCls) {
		this.contratoPraticaOrcDespCls = contratoPraticaOrcDespCls;
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
	 * @return the indicadorTipoFatura
	 */
	public String getIndicadorTipoFatura() {
		return indicadorTipoFatura;
	}

	/**
	 * @param indicadorTipoFatura 
	 * 			the indicadorTipoFatura to set
	 */
	public void setIndicadorTipoFatura(String indicadorTipoFatura) {
		this.indicadorTipoFatura = indicadorTipoFatura;
	}

	/**
	 * @return the indicadorOrcamentoExtra
	 */
	public String getIndicadorOrcamentoExtra() {
		return indicadorOrcamentoExtra;
	}

	/**
	 * @param indicadorOrcamentoExtra
	 *            the indicadorOrcamentoExtra to set
	 */
	public void setIndicadorOrcamentoExtra(String indicadorOrcamentoExtra) {
		this.indicadorOrcamentoExtra = indicadorOrcamentoExtra;
	}

	/**
	 * Retorna true caso o {@link OrcDespesaCl} seja um orcamento reembolsavel.
	 * 
	 * @return boolean
	 * 
	 */
	public boolean getIsReembolsavel() {
		if (Constants.REEMBOLSAVEL.equals(this.getIndicadorReembolsavel())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoOrcaDespCl").append("='")
				.append(getCodigoOrcaDespCl()).append("' ");
		buffer.append("cliente").append("='").append(getCliente()).append("' ");
		buffer.append("orcDespesa").append("='").append(getOrcamentoDespesa())
				.append("' ");
		buffer.append("indicadorReembolsavel").append("='")
				.append(getIndicadorReembolsavel()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}