package com.ciandt.pms.model.vo;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.util.NumberUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Resultado do filtro FiscalBalanceRow
 * 
 * @since 23/05/2014
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
public class FiscalBalanceRow implements Serializable,
		Comparable<FiscalBalanceRow> {

	private static final long serialVersionUID = 3722078518478395585L;

	/** instancia de deal-fiscal. */
	private DealFiscal dealFiscal;

	private Date dateFiscalBalance;

	/** total da receita. */
	private Double totalReceita;

	/** total da fatura. */
	private Double totalFatura;

	/**
	 * Calcula valor total de Balance. Valor e calculado subtraindo do total de
	 * receita do mes, o total de fatura do periodo.
	 * 
	 * @return Valor total balance
	 */
	public Double getTotalBalance() {
		return this.getTotalReceita() - this.getTotalFatura();
	}
	
	/**
	 * Calcula valor total de Balance. Valor e calculado subtraindo do total de
	 * receita do mes, o total de fatura do periodo arrendondando o resultado em
	 * duas casas decimais.
	 * 
	 * @return Valor total balance
	 */
	public Double getTotalBalanceRounded() {
		return NumberUtil.round((this.getTotalReceita() - this.getTotalFatura()) * -1);
	}

	/**
	 * @return the dealFiscal
	 */
	public DealFiscal getDealFiscal() {
		return dealFiscal;
	}

	/**
	 * @param dealFiscal
	 *            the dealFiscal to set
	 */
	public void setDealFiscal(DealFiscal dealFiscal) {
		this.dealFiscal = dealFiscal;
	}

	/**
	 * @return the dateFiscalBalance
	 */
	public Date getDateFiscalBalance() {
		return dateFiscalBalance;
	}

	/**
	 * @param dateFiscalBalance the dateFiscalBalance to set
	 */
	public void setDateFiscalBalance(Date dateFiscalBalance) {
		this.dateFiscalBalance = dateFiscalBalance;
	}

	/**
	 * @return the totalReceita
	 */
	public Double getTotalReceita() {
		return totalReceita;
	}

	/**
	 * @param totalReceita
	 *            the totalReceita to set
	 */
	public void setTotalReceita(final Double totalReceita) {
		this.totalReceita = totalReceita;
	}

	/**
	 * @return the totalFatura
	 */
	public Double getTotalFatura() {
		return totalFatura;
	}

	/**
	 * @param totalFatura
	 *            the totalFatura to set
	 */
	public void setTotalFatura(final Double totalFatura) {
		this.totalFatura = totalFatura;
	}

	/**
	 * A ordenação é feita primeiramente por status (ACTIVE primeiro) e então alfabeticamente.
	 */
	@Override
	public int compareTo(FiscalBalanceRow other) {

		// Variaveis com o nome e status dos DealFiscal dentro dos objetos a serem comparados
		String thisName = this.dealFiscal.getNomeDealFiscal();
		String thisStatus = this.dealFiscal.getIndicadorStatus();
		String otherName = other.getDealFiscal().getNomeDealFiscal();
		String otherStatus = other.getDealFiscal().getIndicadorStatus();

		if (thisStatus.equals(Constants.ACTIVE) && otherStatus.equals(Constants.INACTIVE)) {
			return -1;
		} else if (thisStatus.equals(Constants.INACTIVE) && otherStatus.equals(Constants.ACTIVE)) {
			return 1;
		} else if ((thisStatus.equals(Constants.ACTIVE) && otherStatus.equals(Constants.ACTIVE))
				|| (thisStatus.equals(Constants.INACTIVE) && otherStatus.equals(Constants.INACTIVE))) {
			return thisName.compareToIgnoreCase(otherName);
		}

		return 0;
	}
}