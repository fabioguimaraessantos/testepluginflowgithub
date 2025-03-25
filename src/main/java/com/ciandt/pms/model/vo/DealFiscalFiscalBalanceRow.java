package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.util.NumberUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * TODO: JavaDoc
 * 
 */
public class DealFiscalFiscalBalanceRow implements Serializable,
		Comparable<DealFiscalFiscalBalanceRow> {

	private static final long serialVersionUID = -5944681869283575373L;

	//private ReceitaDealFiscal receitaDealFiscal;

	private Date balanceDate;

	private Moeda moeda;

	private Double publishedFiscalBalance = 0d;

	private Double totalInvoiced = 0d;

	private Double totalRevenue = 0d;

	public DealFiscalFiscalBalanceRow() {
	}

	/**
	 * @return the receitaDealFiscal
	 */
//	public ReceitaDealFiscal getReceitaDealFiscal() {
//		return receitaDealFiscal;
//	}
//
//	/**
//	 * @param receitaDealFiscal
//	 *            the receitaDealFiscal to set
//	 */
//	public void setReceitaDealFiscal(ReceitaDealFiscal receitaDealFiscal) {
//		this.receitaDealFiscal = receitaDealFiscal;
//	}

	/**
	 * @return the balanceDate
	 */
	public Date getBalanceDate() {
		return balanceDate;
	}

	/**
	 * @param balanceDate the balanceDate to set
	 */
	public void setBalanceDate(Date balanceDate) {
		this.balanceDate = balanceDate;
	}

	/**
	 * @return the moeda
	 */
	public Moeda getMoeda() {
		return moeda;
	}

	/**
	 * @param moeda the moeda to set
	 */
	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	/**
	 * @return the publishedFiscalBalance
	 */
	public Double getPublishedFiscalBalance() {
		return this.publishedFiscalBalance;
	}

	/**
	 * @return the publishedFiscalBalance
	 */
	public Double getPublishedFiscalBalanceRounded() {
		return NumberUtil.round(this.publishedFiscalBalance);
	}

	/**
	 * @param publishedFiscalBalance
	 *            the publishedFiscalBalance to set
	 */
	public void setPublishedFiscalBalance(Double publishedFiscalBalance) {
		this.publishedFiscalBalance = publishedFiscalBalance;
	}

	/**
	 * @return the totalInvoiced
	 */
	public Double getTotalInvoiced() {
		return totalInvoiced;
	}

	/**
	 * @param totalInvoiced
	 *            the totalInvoiced to set
	 */
	public void setTotalInvoiced(Double totalInvoiced) {
		this.totalInvoiced = totalInvoiced;
	}

	public Double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(Double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}

	/**
	 * @return Retorna o tipo
	 */
//	public String getTipoBalance() {
//
//		BigDecimal valorReceita = BigDecimal.ZERO;
//		if (this.getReceitaDealFiscal() != null) {
//			valorReceita = this.getReceitaDealFiscal().getValorReceita();
//			valorReceita = valorReceita.setScale(2, RoundingMode.HALF_EVEN);			
//		}
//
//		BigDecimal valorFatura = BigDecimal.ZERO;
//		if (this.getTotalInvoiced() != null) {
//			valorFatura = new BigDecimal(this.getTotalInvoiced());
//			valorFatura = valorFatura.setScale(2, RoundingMode.HALF_EVEN);			
//		}
//
//		if (valorReceita != null) {
//			if (valorReceita.compareTo(valorFatura) == 1) {
//				return Constants.FISCAL_BALANCE_ACCUMULATED_A_FATURAR;
//			} else if ((valorReceita.compareTo(valorFatura) == -1)) {
//				return Constants.FISCAL_BALANCE_ACCUMULATED_A_RECEITAR;
//			}
//		}
//		return Constants.FISCAL_BALANCE_ACCUMULATED_ZERO;
//	}

	/**
	 * Implementação do metodo de comparação.
	 * 
	 * @param otherRow
	 *            - entidade do tipo DealFiscalFiscalBalanceRow.
	 * 
	 * @return a comparação dos dois objetos
	 */
	@Override
	public int compareTo(final DealFiscalFiscalBalanceRow otherRow) {

		if (this.balanceDate.compareTo(otherRow.getBalanceDate()) > 0) {
			return 1;
		} else if (this.balanceDate.compareTo(otherRow.getBalanceDate()) < 0) {
			return -1;
		}

		return 0;
	}
}