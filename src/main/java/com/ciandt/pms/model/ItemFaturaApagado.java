/*
 * @(#) ItemFatura.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Entity gerado a partir da tabela ITEM_FATURA_APAGADO.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 30/10/2009 10:42:58
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "ITEM_FATURA_APAGADO")
@NamedQueries({
	@NamedQuery(name = ItemFaturaApagado.FIND_ALL, query = "SELECT t FROM ItemFaturaApagado t"),
	@NamedQuery(name = ItemFaturaApagado.FIND_BY_FATURA_APAGADA, query = "SELECT t FROM ItemFaturaApagado t"
			+ " WHERE t.faturaApagada.codigoFaturaApagada = :faturaApagadaId")
})
public class ItemFaturaApagado implements java.io.Serializable, Cloneable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "ItemFatura.findAll".
	 */
	public static final String FIND_ALL = "ItemFaturaApagado.findAll";
	
	/**
	 * Constante para NamedQuery "ItemFatura.findAll".
	 */
	public static final String FIND_BY_FATURA_APAGADA = "ItemFaturaApagado.findByFaturaApagada";

	/**
	 * Atributo gerado a partir da coluna IFAA_CD_ITEM_FATURA_APAGADO.
	 */
	@Id
	@Column(name = "IFAA_CD_ITEM_FATURA_APAGADO", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoItemFatura;
	
	/**
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COPR_CD_CONTRATO_PRATICA", nullable = true)
	private ContratoPratica contratoPratica;

	/**
	 * Atributo gerado a partir da coluna FAAP_CD_FATURA_APAGADA.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FAAP_CD_FATURA_APAGADA", nullable = false)
	private FaturaApagada faturaApagada;

	/**
	 * Atributo gerado a partir da coluna TISE_CD_TIPO_SERVICO.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TISE_CD_TIPO_SERVICO", nullable = false)
	private TipoServico tipoServico;

	/**
	 * Atributo gerado a partir da coluna IFAA_TX_OBSERVACAO.
	 */

	@Column(name = "IFAA_TX_OBSERVACAO", length = 200)
	private String textoObservacao;

	/**
	 * Atributo gerado a partir da coluna IFAA_VL_ITEM.
	 */

	@Column(name = "IFAA_VL_ITEM", precision = 22, scale = 0)
	private BigDecimal valorItem;
	
	/**
	 * Atributo gerado a partir da coluna FORE_CD_FONTE_RECEITA.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FORE_CD_FONTE_RECEITA")
	private FonteReceita fonteReceita;
	
	/**
	 * Atributo gerado a partir da coluna IFAA_NR_NOTA_FISCAL.
	 */
	@Column(name = "IFAA_NR_NOTA_FISCAL", length = 100)
	private String numeroNotaFiscal;

	/**
	 * Atributo gerado a partir da coluna IFAA_NR_PEDIDO.
	 */
	@Column(name = "IFAA_NR_PEDIDO", length = 100)
	private String numeroPedido;

	/**
	 * Atributo gerado a partir da coluna IFAA_DT_PAGAMENTO.
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "IFAA_DT_PAGAMENTO", length = 7)
	private Date dataPagamento;

	/**
	 * Atributo transiente para status do item.
	 */
	@Transient
	private String statusItem;

	/**
	 * Atributo transiente para guardar o status da comissao por itemFatura.
	 */
	@Transient
	private String statusComissao;

	/**
	 * Construtor default.
	 */
	public ItemFaturaApagado() {
	}

	public ItemFaturaApagado(ItemFatura itemFatura, FaturaApagada faturaApagada) {

		this.codigoItemFatura = itemFatura.getCodigoItemFatura();
		this.contratoPratica = itemFatura.getContratoPratica();
		this.faturaApagada = faturaApagada;
		this.tipoServico = itemFatura.getTipoServico();
		this.textoObservacao = itemFatura.getTextoObservacao();
		this.valorItem = itemFatura.getValorItem();
		this.fonteReceita = itemFatura.getFonteReceita();
		this.numeroNotaFiscal = itemFatura.getNumeroNotaFiscal();
		this.dataPagamento = itemFatura.getDataPagamento();
	}

	/**
	 * @return the codigoItemFatura
	 */
	public Long getCodigoItemFatura() {
		return codigoItemFatura;
	}

	/**
	 * @param codigoItemFatura
	 *            the codigoItemFatura to set
	 */
	public void setCodigoItemFatura(final Long codigoItemFatura) {
		this.codigoItemFatura = codigoItemFatura;
	}

	/**
	 * @return the fatura
	 */
	public FaturaApagada getFaturaApagada() {
		return this.faturaApagada;
	}

	/**
	 * @param fatura
	 *            the fatura to set
	 */
	public void setFaturaApagada(final FaturaApagada faturaApagada) {
		this.faturaApagada = faturaApagada;
	}

	/**
	 * @return the contratoPratica
	 */
	public ContratoPratica getContratoPratica() {
		return contratoPratica;
	}

	/**
	 * @param contratoPratica
	 *            the contratoPratica to set
	 */
	public void setContratoPratica(final ContratoPratica contratoPratica) {
		this.contratoPratica = contratoPratica;
	}

	/**
	 * @return the tipoServico
	 */
	public TipoServico getTipoServico() {
		return tipoServico;
	}

	/**
	 * @param tipoServico
	 *            the tipoServico to set
	 */
	public void setTipoServico(final TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	/**
	 * @return the textoObservacao
	 */
	public String getTextoObservacao() {
		return textoObservacao;
	}

	/**
	 * @param textoObservacao
	 *            the textoObservacao to set
	 */
	public void setTextoObservacao(final String textoObservacao) {
		this.textoObservacao = textoObservacao;
	}

	/**
	 * @return the valorItem
	 */
	public BigDecimal getValorItem() {
		return valorItem;
	}

	/**
	 * @param valorItem
	 *            the valorItem to set
	 */
	public void setValorItem(final BigDecimal valorItem) {
		this.valorItem = valorItem;
	}

	/**
	 * @return the fonteReceita
	 */
	public FonteReceita getFonteReceita() {
		return fonteReceita;
	}

	/**
	 * @param fonteReceita
	 *            the fonteReceita to set
	 */
	public void setFonteReceita(final FonteReceita fonteReceita) {
		this.fonteReceita = fonteReceita;
	}

	/**
	 * @return the numeroNotaFiscal
	 */
	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	/**
	 * @param numeroNotaFiscal
	 *            the numeroNotaFiscal to set
	 */
	public void setNumeroNotaFiscal(final String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	/**
	 * @return the numeroPedido
	 */
	public String getNumeroPedido() {
		return numeroPedido;
	}

	/**
	 * @param numeroPedido
	 *            the numeroPedido to set
	 */
	public void setNumeroPedido(final String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @param dataPagamento
	 *            the dataPagamento to set
	 */
	public void setDataPagamento(final Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the statusItem
	 */
	public String getStatusItem() {
		return statusItem;
	}

	/**
	 * @param statusItem the statusItem to set
	 */
	public void setStatusItem(String statusItem) {
		this.statusItem = statusItem;
	}

	/**
	 * @return the statusComissao
	 */
	public String getStatusComissao() {
		return statusComissao;
	}

	/**
	 * @param statusComissao the statusComissao to set
	 */
	public void setStatusComissao(String statusComissao) {
		this.statusComissao = statusComissao;
	}

}