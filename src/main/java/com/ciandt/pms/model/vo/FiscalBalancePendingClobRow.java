package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.Receita;

/**
 * 
 * A classe FiscalBalancePendingRow que representa a linha da modal de Detalhe
 * saldo acumulado do Fiscal Deal.
 * 
 * @since 29/05/2014
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
public class FiscalBalancePendingClobRow implements java.io.Serializable,
		Comparable<FiscalBalancePendingClobRow> {

	/** Default seraul version UID. */
	private static final long serialVersionUID = 1L;

	/** Intancia da entidade Receita. */
	private Receita receita;

	/** total associado a receita. */
	private Double totalValue;

	/** Valor pendente. */
	private Double pendingValue;

	/** Sigla da moeda */
	private String patternCurrency;

	/**
	 * @return the receita
	 */
	public Receita getReceita() {
		return receita;
	}

	/**
	 * @param receita
	 *            the receita to set
	 */
	public void setReceita(Receita receita) {
		this.receita = receita;
	}

	/**
	 * @return the totalValue
	 */
	public Double getTotalValue() {
		return totalValue;
	}

	/**
	 * @param totalValue
	 *            the totalValue to set
	 */
	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	/**
	 * @return the pendingValue
	 */
	public Double getPendingValue() {
		return pendingValue;
	}

	/**
	 * @param pendingValue
	 *            the pendingValue to set
	 */
	public void setPendingValue(Double pendingValue) {
		this.pendingValue = pendingValue;
	}

	/**
	 * @return the patternCurrency
	 */
	public String getPatternCurrency() {
		return patternCurrency;
	}

	/**
	 * @param patternCurrency
	 *            the patternCurrency to set
	 */
	public void setPatternCurrency(String patternCurrency) {
		this.patternCurrency = patternCurrency;
	}

	/**
	 * Implementação do metodo de comparação para ordenação por dataMes da
	 * receita
	 * 
	 * @param otherRow
	 *            - entidade do tipo FiscalBalancePendingRow.
	 * 
	 * @return a comparação dos dois objetos
	 */
	@Override
	public int compareTo(final FiscalBalancePendingClobRow otherRow) {

		// ordena pela data
		if (this.receita.getDataMes()
				.before(otherRow.getReceita().getDataMes())) {
			return -1;
		}

		if (this.receita.getDataMes().after(otherRow.getReceita().getDataMes())) {
			return 1;
		}

		return 0;
	}

}