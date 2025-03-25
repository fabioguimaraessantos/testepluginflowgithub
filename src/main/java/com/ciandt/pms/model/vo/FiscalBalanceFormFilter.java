package com.ciandt.pms.model.vo;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.util.DateUtil;

import java.util.Date;

/**
 * Classe que representa as opcoes de filtro da tela de 'FiscalBalance'.
 * 
 * @since 26/05/2014
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
public class FiscalBalanceFormFilter implements java.io.Serializable {

	private static final long serialVersionUID = 4057405305787246532L;

	/** Objeto MSA usado na consulta de Fiscal Balance */
	private Msa msa;

	/** Objeto Cliente usado na consulta de Fiscal Balance */
	private Cliente cliente;

	private DealFiscal currentDealFiscal;

	private String startMonth;

	private String startYear;

	/** End Month usado na consulta de Fiscal Balance */
	private String endMonth;

	/** End Month usado na consulta de Fiscal Balance */
	private String endYear;

	/** Option usado na consulta de Fiscal Balance */
	private String option;

	/**
	 * Construtor default da classe
	 */
	public FiscalBalanceFormFilter() {
		this.msa = new Msa();
		this.cliente = new Cliente();
		this.option = Constants.FISCAL_BALANCE_LAST_DAY_OF_MONTH;
	}

	/**
	 * Construtor da classe
	 * 
	 * @param cliente
	 *            Cliente selecionado na tela de consulta de Fiscal Balance
	 * @param msa
	 *            Msa selecionado na tela de consulta de Fiscal Balance
	 * @param endMonth
	 *            Mes selecionado na tela de consulta de Fiscal Balance
	 * 
	 * @param endYear
	 *            Ano selecionado na tela de consulta de Fiscal Balance
	 * @param option
	 *            Opcao selecionado na tela de consulta de Fiscal Balance
	 */
	public FiscalBalanceFormFilter(final Msa msa, final Cliente cliente,
			final String endMonth, final String endYear, final String option) {
		this.cliente = cliente;
		this.msa = msa;
		this.endMonth = endMonth;
		this.endYear = endYear;
		this.option = option;
	}

	/**
	 * 
	 * @return Data baseado nos combos de mês e ano selecionado
	 */
	public Date getEndDate() {
		return DateUtil.getDate(this.getEndMonth(), this.getEndYear());
	}

	/**
	 * @return the msa
	 */
	public Msa getMsa() {
		return msa;
	}

	/**
	 * @param msa
	 *            the msa to set
	 */
	public void setMsa(Msa msa) {
		this.msa = msa;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente
	 *            the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the currentDealFiscal
	 */
	public DealFiscal getCurrentDealFiscal() {
		return currentDealFiscal;
	}

	/**
	 * @param currentDealFiscal the currentDealFiscal to set
	 */
	public void setCurrentDealFiscal(DealFiscal currentDealFiscal) {
		this.currentDealFiscal = currentDealFiscal;
	}

	/**
	 * @return the startMonth
	 */
	public String getStartMonth() {
		return startMonth;
	}

	/**
	 * @return Data baseado nos combos de inicio de mês e ano selecionado
	 */
	public Date getStartDate() {
		return DateUtil.getDate(this.startMonth, this.startYear);
	}

	/**
	 * @param startMonth the startMonth to set
	 */
	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	/**
	 * @return the startYear
	 */
	public String getStartYear() {
		return startYear;
	}

	/**
	 * @param startYear the startYear to set
	 */
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	/**
	 * @return the endMonth
	 */
	public String getEndMonth() {
		return endMonth;
	}

	/**
	 * @param endMonth
	 *            the endMonth to set
	 */
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	/**
	 * @return the endYear
	 */
	public String getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear
	 *            the endYear to set
	 */
	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	/**
	 * @return the option
	 */
	public String getOption() {

		if (this.option == null) {
			this.option = Constants.FISCAL_BALANCE_LAST_DAY_OF_MONTH;
		}

		return option;
	}

	/**
	 * @param option
	 *            the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}

}
