package com.ciandt.pms.model.vo;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.model.ReceitaPlantao;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A classe ReceitaMoedaRow representa as receitas por moeda exibida na tela de
 * Receita.
 * 
 * @author mvidolin
 * @since 17/10/2012
 */
@Component
public class ReceitaMoedaRow implements java.io.Serializable {

	/** The serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** Instancia da {@link ReceitaMoeda}. */
	private ReceitaMoeda receitaMoeda;

	/**
	 * Lista de {@link ReceitaDealFiscalRow} (Tabela onde é distribuido os
	 * valores da receita).
	 */
	private List<ReceitaDealFiscalRow> recDealFiscalRowList = new ArrayList<ReceitaDealFiscalRow>();

	/** Lista de {@link ItemReceitaRow} (Resource List). */
	private List<ItemReceitaRow> itemReceitaRowList = new ArrayList<ItemReceitaRow>();

	/** Flag que indica se panel dessa ReceitaMoeda está aberta. */
	private Boolean isOpened = Boolean.FALSE;

	/** Instancia de um {@link ShortTermRevenueRow}. */
	private ShortTermRevenueRow shortTermRevenueRow;

	/** Flag para mostrar ou nao o form de Ajuste Receita. */
	private Boolean showFormAjusteReceita = Boolean.FALSE;

	/**
	 * Inidica se a mensagem deve ser mostrada dentro da caixa da Receita Moeda.
	 */
	private Boolean showMessage = Boolean.FALSE;

	/** Lista para o combobox com os dealFiscal. */
	private Map<String, Long> dealFiscalMap = new HashMap<String, Long>();

	/** Lista para o combobox com os dealFiscal. */
	private List<String> listaDealFiscal = new ArrayList<String>();

	/** Lista para o combobox com os dealFiscal Filtrados (To). */
	private List<String> listaDealFiscalFiltradaTo = new ArrayList<String>();

	/** Lista para o combobox To com os dealFiscal filtrados. */
	private Map<String, Long> dealFiscalMapTo = new HashMap<String, Long>();

	/** Lista de {@link AjusteReceita}. */
	private List<AjusteReceita> ajusteReceitaList = new ArrayList<AjusteReceita>();

	private List<AjusteReceitaRow> ajusteReceitaRowList = new ArrayList<AjusteReceitaRow>();

	private BigDecimal valorReceitaAjustadoForecast = BigDecimal.ZERO;

	public BigDecimal getValorReceitaAjustadoForecast() {
		if (BigDecimal.ZERO.equals(valorReceitaAjustadoForecast)) {
			valorReceitaAjustadoForecast = getTotalReceitaDealFiscal();
		}
		return valorReceitaAjustadoForecast;
	}

	public void setValorReceitaAjustadoForecast(BigDecimal valorReceitaAjustadoForecast) {
		this.valorReceitaAjustadoForecast = valorReceitaAjustadoForecast;
	}

	/**
	 * @return the ajusteReceitaRowList
	 */
	public List<AjusteReceitaRow> getAjusteReceitaRowList() {
		return ajusteReceitaRowList;
	}

	/**
	 * @param ajusteReceitaRowList
	 *            the ajusteReceitaRowList to set
	 */
	public void setAjusteReceitaRowList(
			List<AjusteReceitaRow> ajusteReceitaRowList) {
		this.ajusteReceitaRowList = ajusteReceitaRowList;
	}
	/**
	 * @return the ajusteReceitaList
	 */
	public List<AjusteReceita> getAjusteReceitaList() {
		return ajusteReceitaList;
	}

	/**
	 * @param ajusteReceitaList
	 *            the ajusteReceitaList to set
	 */
	public void setAjusteReceitaList(final List<AjusteReceita> ajusteReceitaList) {
		this.ajusteReceitaList = ajusteReceitaList;
	}

	/**
	 * @return the dealFiscalMap
	 */
	public Map<String, Long> getDealFiscalMap() {
		return dealFiscalMap;
	}

	/**
	 * @param dealFiscalMap
	 *            the dealFiscalMap to set
	 */
	public void setDealFiscalMap(final Map<String, Long> dealFiscalMap) {
		this.dealFiscalMap = dealFiscalMap;
	}

	/**
	 * @return the receitaMoeda
	 */
	public ReceitaMoeda getReceitaMoeda() {
		return receitaMoeda;
	}

	/**
	 * @param receitaMoeda
	 *            the receitaMoeda to set
	 */
	public void setReceitaMoeda(final ReceitaMoeda receitaMoeda) {
		this.receitaMoeda = receitaMoeda;
	}

	/**
	 * @return the receitaRowList.
	 */
	public List<ReceitaDealFiscalRow> getRecDealFiscalRowList() {
		return recDealFiscalRowList;
	}

	/**
	 * @param recDealFiscalRowList
	 *            the recDealFiscalRowList.
	 */
	public void setRecDealFiscalRowList(
			final List<ReceitaDealFiscalRow> recDealFiscalRowList) {
		this.recDealFiscalRowList = recDealFiscalRowList;
	}

	/**
	 * @return the itemReceitaRowList
	 */
	public List<ItemReceitaRow> getItemReceitaRowList() {
		return itemReceitaRowList;
	}

	/**
	 * @param itemReceitaRowList
	 *            the itemReceitaRowList.
	 */
	public void setItemReceitaRowList(
			final List<ItemReceitaRow> itemReceitaRowList) {
		this.itemReceitaRowList = itemReceitaRowList;
	}

	/**
	 * Calcula o valor total de Horas dos itens da receita da moeda corrente.
	 * 
	 * @return the totalHrs
	 */
	public BigDecimal getTotalHrs() {
		double totalHrs = 0.0;
		// itera por todos os itens da receita da moeda atual para totalizar as
		// Horas
		for (ItemReceitaRow item : this.itemReceitaRowList) {
			if (!item.getIsRemove() && item.getIsView()) {
				// calcula com o "Hidden" e nao com o valor exibido na tela por
				// motivos de precisao.
				totalHrs += item.getNumberHoursHidden()
						.setScale(2, RoundingMode.HALF_UP).doubleValue();
			}
		}
		return BigDecimal.valueOf(totalHrs);
	}

	/**
	 * Calcula o valor total de FTEs dos itens da receita da moeda corrente.
	 * 
	 * @return the totalFte
	 */
	public BigDecimal getTotalFte() {
		double totalFte = 0.0;
		// itera por todos os itens da receita da moeda atual para totalizar o
		// FTE
		for (ItemReceitaRow item : this.itemReceitaRowList) {
			if (!item.getIsRemove() && item.getIsView()) {
				// calcula com o "Hidden" e nao com o valor exibido na tela por
				// motivos de precisao.
				totalFte += item.getNumeroFteHidden()
						.setScale(2, RoundingMode.HALF_UP).doubleValue();
			}
		}
		return BigDecimal.valueOf(totalFte);
	}

	/**
	 * Calcula o valor total de Amount dos itens da receita da moeda corrente.
	 * 
	 * @return the totalAmount
	 */
	public BigDecimal getTotalAmount() {
		BigDecimal totalAmount = new BigDecimal(0);
		// itera por todos os itens da receita da moeda atual para totalizar a
		// Amount
		for (ItemReceitaRow item : this.itemReceitaRowList) {
			if (!item.getIsRemove() && item.getIsView()) {
				totalAmount = totalAmount.add(item.getAmountValue());
			}
		}
		return totalAmount.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Calcula o valor total de Amount dos itens da receita da moeda corrente.
	 *
	 * @return the totalAmount
	 */
	public BigDecimal getUnselectedRowsTotalAmount() {
		BigDecimal totalAmount = new BigDecimal(0);
		// itera por todos os itens da receita da moeda atual para totalizar a
		// Amount
		for (ItemReceitaRow item : this.itemReceitaRowList) {
			if (!item.getIsRemove() && !item.getIsSelected()) {
				totalAmount = totalAmount.add(item.getAmountValue());
			}
		}
		return totalAmount.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Obtem o valor total da Receita Deal Fiscal.
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getTotalReceitaDealFiscal() {
		BigDecimal totalResult = BigDecimal.valueOf(0);

		for (ReceitaDealFiscalRow receitaRow : this.recDealFiscalRowList) {
			totalResult = totalResult.add(receitaRow.getTo().getValorReceita() != null ? receitaRow.getTo().getValorReceita() : BigDecimal.ZERO);
		}

		return totalResult.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Obtem o valor total do Ajuste Receita.
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getTotalAjusteDealFiscal() {
		BigDecimal totalResult = BigDecimal.valueOf(0);

		for (ReceitaDealFiscalRow receitaRow : this.recDealFiscalRowList) {
			totalResult = totalResult.add(receitaRow.getAjusteFiscalDeal());
		}

		return totalResult.setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * Obtem o valor total do Receita Plantao.
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getTotalReceitaPlantao() {
		BigDecimal totalResult = BigDecimal.valueOf(0);
		
		for (ReceitaDealFiscalRow receitaRow : this.recDealFiscalRowList) {
			if (receitaRow.getTo().getReceitaPlantao() != null && receitaRow.getTo().getReceitaPlantao().getValorReceitaPlantao() != null) {
				
				totalResult = totalResult.add(receitaRow.getTo().getReceitaPlantao().getValorReceitaPlantao());
			} else {
				receitaRow.getTo().setReceitaPlantao(new ReceitaPlantao(receitaRow.getTo()));
			}
		}
		
		return totalResult.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Obtem o valor total do balance Deal Faical.
	 * 
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getTotalBalanceDealFiscal() {
		BigDecimal totalResult = BigDecimal.valueOf(0);

		for (ReceitaDealFiscalRow receitaRow : this.recDealFiscalRowList) {
			totalResult = totalResult.add(receitaRow.getBalancoFiscalDeal());
		}

		return totalResult.setScale(2, RoundingMode.HALF_UP);
	}
	/**
	 * Obtem o total de PublsihedFB
	 * @return
	 */
	public BigDecimal getTotalPublishFB() {
		BigDecimal totalResult = BigDecimal.valueOf(0);

		for (ReceitaDealFiscalRow receitaRow : this.recDealFiscalRowList) {
			totalResult = totalResult.add(new BigDecimal(receitaRow.getPublishedFiscalBalance()));
		}

		return totalResult.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * @return the isOpened
	 */
	public Boolean getIsOpened() {
		return isOpened;
	}

	/**
	 * @param isOpened
	 *            the isOpened to set
	 */
	public void setIsOpened(final Boolean isOpened) {
		this.isOpened = isOpened;
	}

	/**
	 * @return the showFormAjusteReceita
	 */
	public Boolean getShowFormAjusteReceita() {
		return showFormAjusteReceita;
	}

	/**
	 * @param showFormAjusteReceita
	 *            the showFormAjusteReceita to set
	 */
	public void setShowFormAjusteReceita(final Boolean showFormAjusteReceita) {
		this.showFormAjusteReceita = showFormAjusteReceita;
	}

	/**
	 * @return the shortTermRevenuRow
	 */
	public ShortTermRevenueRow getShortTermRevenueRow() {
		return shortTermRevenueRow;
	}

	/**
	 * @param shortTermRevenueRow
	 *            the shortTermRevenuRow to set
	 */
	public void setShortTermRevenueRow(
			final ShortTermRevenueRow shortTermRevenueRow) {
		this.shortTermRevenueRow = shortTermRevenueRow;
	}

	/**
	 * @return the showMessage
	 */
	public Boolean getShowMessage() {
		return showMessage;
	}

	/**
	 * @param showMessage
	 *            the showMessage to set
	 */
	public void setShowMessage(final Boolean showMessage) {
		this.showMessage = showMessage;
	}

	/**
	 * @return the listaDealFiscal
	 */
	public List<String> getListaDealFiscal() {
		return listaDealFiscal;
	}

	/**
	 * @param listaDealFiscal
	 *            the listaDealFiscal to set
	 */
	public void setListaDealFiscal(final List<String> listaDealFiscal) {
		this.listaDealFiscal = listaDealFiscal;
	}

	/**
	 * @return the listaDealFiscalFiltradaTo
	 */
	public List<String> getListaDealFiscalFiltradaTo() {
		return listaDealFiscalFiltradaTo;
	}

	/**
	 * @param listaDealFiscalFiltradaTo
	 *            the listaDealFiscalFiltradaTo to set
	 */
	public void setListaDealFiscalFiltradaTo(
			final List<String> listaDealFiscalFiltradaTo) {
		this.listaDealFiscalFiltradaTo = listaDealFiscalFiltradaTo;
	}

	/**
	 * @return the dealFiscalMapTo
	 */
	public Map<String, Long> getDealFiscalMapTo() {
		return dealFiscalMapTo;
	}

	/**
	 * @param dealFiscalMapTo
	 *            the dealFiscalMapTo to set
	 */
	public void setDealFiscalMapTo(final Map<String, Long> dealFiscalMapTo) {
		this.dealFiscalMapTo = dealFiscalMapTo;
	}

}