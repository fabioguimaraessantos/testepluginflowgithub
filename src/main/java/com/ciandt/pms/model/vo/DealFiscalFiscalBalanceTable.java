package com.ciandt.pms.model.vo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * TODO: JavaDoc
 * 
 * @author mvidolin
 * 
 */
public class DealFiscalFiscalBalanceTable {

	/** Lista de itens do fiscal balance. */
	private List<DealFiscalFiscalBalanceRow> rows = new ArrayList<DealFiscalFiscalBalanceRow>();

	/**
	 * Adiciona uma linha ({@link FiscalBalanceRow}) na tabela (
	 * {@link FiscalBalanceTable}).
	 * 
	 * @param row
	 */
	public boolean addRow(final DealFiscalFiscalBalanceRow row) {
		if (this.rows == null)
			this.rows = new ArrayList<DealFiscalFiscalBalanceRow>();

		return this.rows.add(row);
	}

	/**
	 * @return the fiscalBalanceRow
	 */
	public List<DealFiscalFiscalBalanceRow> getDealFiscalFiscalBalanceRow() {
		return calculateAllBalanceAccumulated();
	}

	/**
	 * @param rows
	 *            the dealFiscal to set
	 */
	public void setDealFiscalFiscalBalanceRow(
			List<DealFiscalFiscalBalanceRow> rows) {
		if (rows != null)
			this.rows = rows;
	}

	/**
	 * Retorna um {@link DealFiscalFiscalBalanceRow} com balance atualizado de
	 * acordo com os parametros informados. A composicao do valor do balance eh: <br>
	 * totalAcumulado = baseRow.getPublishedFiscalBalance() +
	 * toUpdateRow.getTotalInvoiced()
	 * 
	 * @param baseRow
	 * @param toUpdateRow
	 * @return o {@link DealFiscalFiscalBalanceRow} com o publishedFiscalBalance
	 *         calculado
	 */
	private DealFiscalFiscalBalanceRow calculateBalanceAccumulated(
			DealFiscalFiscalBalanceRow baseRow,
			DealFiscalFiscalBalanceRow toUpdateRow) {

		Double totalAcumulado = baseRow.getPublishedFiscalBalance()
				+ toUpdateRow.getPublishedFiscalBalance();

		toUpdateRow.setPublishedFiscalBalance(totalAcumulado);

		return toUpdateRow;
	}

	/**
	 * TODO: JavaDoc
	 */
	private List<DealFiscalFiscalBalanceRow> calculateAllBalanceAccumulated() {

		if (this.rows.isEmpty())
			return this.rows;

		LinkedList<DealFiscalFiscalBalanceRow> handled = new LinkedList<DealFiscalFiscalBalanceRow>(
				this.rows);

		// Ordena em ordem decrescente de data da receita
//		Collections.sort(handled);

		// o valor do balance do ultimo registro eh igual ao valor total da
		// Invoice
		List<DealFiscalFiscalBalanceRow> calculatedRows = new ArrayList<DealFiscalFiscalBalanceRow>();
		DealFiscalFiscalBalanceRow last = handled.getLast();
		handled.getLast().setPublishedFiscalBalance(
				last.getPublishedFiscalBalance());

		calculatedRows.add(handled.getLast());

		// caso a tabela tenha apenas um item, o valor acumulado eh igual ao
		// valor da Invoice
		if (this.rows.size() <= 1) {
			return calculatedRows;
		}

		while (!handled.isEmpty()) {
			DealFiscalFiscalBalanceRow base = handled.pollLast();
			if (!handled.isEmpty()) {
				calculatedRows.add(this.calculateBalanceAccumulated(base,
						handled.getLast()));
			}
		}

		// Ordena em ordem decrescente de data da receita
//		Collections.sort(calculatedRows);

		return calculatedRows;
	}

}