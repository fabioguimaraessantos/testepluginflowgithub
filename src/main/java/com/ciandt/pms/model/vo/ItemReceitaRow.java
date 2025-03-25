/*
 * @(#) ItemReceitaRow.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.EtiquetaItemReceita;
import com.ciandt.pms.model.ItemReceita;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa a linha da lista de ItemReceita.
 * 
 */
public class ItemReceitaRow implements java.io.Serializable,
		Comparable<ItemReceitaRow> {

	/** Valor do serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Indica por qual compo se deseja fazer a ordenção. */
	private static Integer comparableField = 8;

	/** Salva qual foi a ultima ordenção realizada. */
	private static Integer lastComparableField = 8;

	/** Index da linha. */
	private Integer rowId = Integer.valueOf(0);

	/** Entidade ItemReceita que a linha representa. */
	private ItemReceita itemReceita = new ItemReceita();

	/** Indicador do modo em tempo de execucao - remove. */
	private Boolean isRemove = Boolean.valueOf(false);

	/** Titulo do MapaAlocacao. */
	private String mapaAlocacaoTitle = "";

	/** Valor do número de horas faturadas - tela. */
	private BigDecimal numberHours = BigDecimal.valueOf(0);

	/** Valor do número de horas faturadas - real, escondido. */
	private BigDecimal numberHoursHidden = BigDecimal.valueOf(0);

	/** Valor do número de FTE - real, escondido. */
	private BigDecimal numeroFteHidden = BigDecimal.valueOf(0);

	/** Valor do preço do PerfilVendido em horas. */
	private BigDecimal hrsPrice = BigDecimal.valueOf(0);

	/** Valor do preço do PerfilVendido em fte. */
	private BigDecimal ftePrice = BigDecimal.valueOf(0);

	/** Valor do subtotal da receita. */
	private BigDecimal amountValue = BigDecimal.valueOf(0);

	/** Indica se o item esta selecionado. */
	private Boolean isSelected = Boolean.valueOf(false);

	/** Nomes das Etiqueta (tags). */
	private String etiquetaNames = "";

	/** Nomes das Etiqueta (tags) para exibir na tela. */
	private String etiquetaNamesPart = "";

	/** Indicador do modo em tempo de execucao - view (tags). */
	private Boolean isView = Boolean.TRUE;

	/** Nome da cidade base do login. */
	private String nomeCidadeBase = "";

	/** Valor da coluna, para ordenção. */
	private static final int COLUMN_LOGIN = 1;
	/** Valor da coluna, para ordenção. */
	private static final int COLUMN_SALE_PROFILE = 2;
	/** Valor da coluna, para ordenção. */
	private static final int COLUMN_NUM_HOURS = 3;
	/** Valor da coluna, para ordenção. */
	private static final int COLUMN_FTE = 4;
	/** Valor da coluna, para ordenção. */
	private static final int COLUMN_HRS_PRICE = 5;
	/** Valor da coluna, para ordenção. */
	private static final int COLUMN_FTE_PRICE = 6;
	/** Valor da coluna, para ordenção. */
	private static final int COLUMN_TOTAL = 7;
	/** Valor da coluna, para ordenação. */
	public static final int COLUMN_SITE = 8;
	/** Valor da coluna, para ordenação. */
	public static final int COLUMN_SALE_PROFILE_SITE = 9;

	/** Lista de EtiquetaItemReceita do ItemReceita corrente. */
	private List<EtiquetaItemReceita> etiquetaItemReceitaList = new ArrayList<EtiquetaItemReceita>(
			0);

	/**
	 * Construtor padrao.
	 * 
	 * @param itemReceita
	 *            - ItemReceita da linha corrente da lista
	 */
	public ItemReceitaRow(final ItemReceita itemReceita) {
		this.itemReceita = itemReceita;
	}

	/**
	 * Construtor padrao.
	 * 
	 * @param itemReceita
	 *            - ItemReceita da linha corrente da lista
	 * @param rowId
	 *            - id da linha da matriz
	 */
	public ItemReceitaRow(final ItemReceita itemReceita, final Integer rowId) {
		this.itemReceita = itemReceita;
		this.rowId = rowId;
	}

	/**
	 * @return the numberHoursHidden
	 */
	public BigDecimal getNumberHoursHidden() {
		return numberHoursHidden;
	}

	/**
	 * @param numberHoursHidden
	 *            the numberHoursHidden to set
	 */
	public void setNumberHoursHidden(final BigDecimal numberHoursHidden) {
		this.numberHoursHidden = numberHoursHidden;
	}

	/**
	 * @return the numeroFteHidden
	 */
	public BigDecimal getNumeroFteHidden() {
		return numeroFteHidden;
	}

	/**
	 * @param numeroFteHidden
	 *            the numeroFteHidden to set
	 */
	public void setNumeroFteHidden(final BigDecimal numeroFteHidden) {
		this.numeroFteHidden = numeroFteHidden;
	}

	/**
	 * @return the etiquetaItemReceitaList
	 */
	public List<EtiquetaItemReceita> getEtiquetaItemReceitaList() {
		return etiquetaItemReceitaList;
	}

	/**
	 * @param etiquetaItemReceitaList
	 *            the etiquetaItemReceitaList to set
	 */
	public void setEtiquetaItemReceitaList(
			final List<EtiquetaItemReceita> etiquetaItemReceitaList) {
		this.etiquetaItemReceitaList = etiquetaItemReceitaList;
	}

	/**
	 * @return the isView
	 */
	public Boolean getIsView() {
		return isView;
	}

	/**
	 * @param isView
	 *            the isView to set
	 */
	public void setIsView(final Boolean isView) {
		this.isView = isView;
	}

	/**
	 * @return the etiquetaNames
	 */
	public String getEtiquetaNames() {
		return etiquetaNames;
	}

	/**
	 * @param etiquetaNames
	 *            the etiquetaNames to set
	 */
	public void setEtiquetaNames(final String etiquetaNames) {
		this.etiquetaNames = etiquetaNames;
	}

	/**
	 * @return the etiquetaNamesPart
	 */
	public String getEtiquetaNamesPart() {
		return etiquetaNamesPart;
	}

	/**
	 * @param etiquetaNamesPart
	 *            the etiquetaNamesPart to set
	 */
	public void setEtiquetaNamesPart(final String etiquetaNamesPart) {
		this.etiquetaNamesPart = etiquetaNamesPart;
	}

	/**
	 * Construtor padrao.
	 * 
	 */
	public ItemReceitaRow() {
	}

	/**
	 * @return the hrsPrice
	 */
	public BigDecimal getHrsPrice() {
		return hrsPrice;
	}

	/**
	 * @param hrsPrice
	 *            the hrsPrice to set
	 */
	public void setHrsPrice(final BigDecimal hrsPrice) {
		this.hrsPrice = hrsPrice;
	}

	/**
	 * @return the ftePrice
	 */
	public BigDecimal getFtePrice() {
		return ftePrice;
	}

	/**
	 * @param ftePrice
	 *            the ftePrice to set
	 */
	public void setFtePrice(final BigDecimal ftePrice) {
		this.ftePrice = ftePrice;
	}

	/**
	 * @return the mapaAlocacaoTitle
	 */
	public String getMapaAlocacaoTitle() {
		return mapaAlocacaoTitle;
	}

	/**
	 * @param mapaAlocacaoTitle
	 *            the mapaAlocacaoTitle to set
	 */
	public void setMapaAlocacaoTitle(final String mapaAlocacaoTitle) {
		this.mapaAlocacaoTitle = mapaAlocacaoTitle;
	}

	/**
	 * @return the numberHours
	 */
	public BigDecimal getNumberHours() {
		return numberHours;
	}

	/**
	 * @param numberHours
	 *            the numberHours to set
	 */
	public void setNumberHours(final BigDecimal numberHours) {
		this.numberHours = numberHours;
	}

	/**
	 * @return the amountValue
	 */
	public BigDecimal getAmountValue() {
		return amountValue;
	}

	/**
	 * @param amountValue
	 *            the amountValue to set
	 */
	public void setAmountValue(final BigDecimal amountValue) {
		this.amountValue = amountValue;
	}

	/**
	 * @return the rowId
	 */
	public Integer getRowId() {
		return rowId;
	}

	/**
	 * @param rowId
	 *            the rowId to set
	 */
	public void setRowId(final Integer rowId) {
		this.rowId = rowId;
	}

	/**
	 * @return the itemReceita
	 */
	public ItemReceita getItemReceita() {
		return itemReceita;
	}

	/**
	 * @param itemReceita
	 *            the itemReceita to set
	 */
	public void setItemReceita(final ItemReceita itemReceita) {
		this.itemReceita = itemReceita;
	}

	/**
	 * @return the isRemove
	 */
	public Boolean getIsRemove() {
		return isRemove;
	}

	/**
	 * @param isRemove
	 *            the isRemove to set
	 */
	public void setIsRemove(final Boolean isRemove) {
		this.isRemove = isRemove;
	}

	/**
	 * @param isSelected
	 *            the isSelected to set
	 */
	public void setIsSelected(final Boolean isSelected) {
		this.isSelected = isSelected;
	}

	/**
	 * @return the isSelected
	 */
	public Boolean getIsSelected() {
		return isSelected;
	}

	/**
	 * Implamentacao do metodo de comparação.
	 * 
	 * @param row
	 *            - entidade ItemReceitaRow
	 * 
	 * @return retorna o valor da comparação.
	 */
	@Override
	public int compareTo(final ItemReceitaRow row) {
		setLastComparableField(comparableField);
		switch (comparableField) {
		case COLUMN_LOGIN:
			return this.itemReceita.getPessoa().getCodigoLogin()
					.compareTo(row.itemReceita.getPessoa().getCodigoLogin());
		case COLUMN_SALE_PROFILE:
			return this.itemReceita
					.getPerfilVendido()
					.getNomePerfilVendido()
					.compareTo(
							row.itemReceita.getPerfilVendido()
									.getNomePerfilVendido());
		case COLUMN_NUM_HOURS:
			return this.numberHours.compareTo(row.numberHours);
		case COLUMN_FTE:
			return this.itemReceita.getNumeroFte().compareTo(
					row.itemReceita.getNumeroFte());
		case COLUMN_HRS_PRICE:
			return this.hrsPrice.compareTo(row.hrsPrice);
		case COLUMN_FTE_PRICE:
			return this.ftePrice.compareTo(row.ftePrice);
		case COLUMN_TOTAL:
			return this.amountValue.compareTo(row.amountValue);
		case COLUMN_SITE:
			int i = this.nomeCidadeBase.compareTo(row.getNomeCidadeBase());
			if (i != 0) return i;
			return this.itemReceita.getPessoa().getCodigoLogin().compareTo(row.itemReceita.getPessoa().getCodigoLogin());
		case COLUMN_SALE_PROFILE_SITE:
			return this.itemReceita
					.getPerfilVendido()
					.getPerfilPadrao()
					.getCidadeBase()
					.getSiglaCidadeBase()
					.compareTo(row.itemReceita
							.getPerfilVendido()
							.getPerfilPadrao()
							.getCidadeBase()
							.getSiglaCidadeBase());
		default:
			return 0;
		}
	}

	/**
	 * @param lastComparableField
	 *            the lastComparableField to set
	 */
	public static void setLastComparableField(final Integer lastComparableField) {
		ItemReceitaRow.lastComparableField = lastComparableField;
	}

	/**
	 * @return the lastComparableField
	 */
	public static Integer getLastComparableField() {
		return lastComparableField;
	}

	/**
	 * @return the comparableField
	 */
	public Integer getComparableField() {
		return comparableField;
	}

	/**
	 * @return the comparableField
	 */
	public static Integer getStaticComparableField() {
		return comparableField;
	}

	/**
	 * @param comparableField
	 *            the comparableField to set
	 */
	public void setComparableField(final Integer comparableField) {
		ItemReceitaRow.comparableField = comparableField;
	}

	/**
	 * @param nomeCidadeBase
	 *            the nomeCidadeBase to set
	 */
	public void setNomeCidadeBase(final String nomeCidadeBase) {
		this.nomeCidadeBase = nomeCidadeBase;
	}

	/**
	 * @return the nomeCidadeBase field
	 */
	public String getNomeCidadeBase() {
		return nomeCidadeBase;
	}
}