package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.vo.DealFiscalFiscalBalanceRow;
import com.ciandt.pms.model.vo.FaturaReceitaRow;
import com.ciandt.pms.model.vo.FiscalBalanceFormFilter;
import com.ciandt.pms.model.vo.FiscalBalancePendingClobRow;
import com.ciandt.pms.model.vo.FiscalBalanceRow;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe FiscalBalanceBean proporciona as funcionalidades de backingBean para
 * Fiscal Balance.
 * 
 * @since 23/05/2014
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class FiscalBalanceBean implements Serializable {

	private static final long serialVersionUID = -3708387915667410323L;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** FiscalBalanceRow */
	private List<FiscalBalanceRow> resultList;

	/** Lista de faturas pendentes, que não possui receita vinculado */
	private List<FaturaReceitaRow> faturaPendenteList;

	/** Lista de clobs pendentes, que não possui receita vinculado */
	private List<FiscalBalancePendingClobRow> clobPendenteList;

	/**
	 * Objeto que guarda o filtro selecionado na tela de consulta de Fiscal
	 * Balance
	 */
	private FiscalBalanceFormFilter filter = new FiscalBalanceFormFilter();

	/** List para o combobox com clientes. */
	private List<String> clienteList;

	/** Map para o combobox com clientes. */
	private Map<String, Long> clienteMap;

	/** Lista para o combobox com os Msa. */
	private List<String> msaList;

	/** Lista para o combobox com os Msa. */
	private Map<String, Long> msaMap;

	/** Lista dos possiveis valores de meses. */
	private List<String> validityMonthList = Arrays.asList("01", "02", "03",
			"04", "05", "06", "07", "08", "09", "10", "11", "12");

	/** Lista dos possiveis valores de anos. */
	private List<String> validityYearList = new ArrayList<String>();

	/** Mes de inicio vigencia. */
	private String startValidityMonth = null;

	/** Ano de inicio vigencia. */
	private String startValidityYear = null;

	/** Mes de fim vigencia. */
	private String endValidityMonth = null;

	/** Ano de fim vigencia. */
	private String endValidityYear = null;

	/** Option usado na consulta de Fiscal Balance */
	private String option = Constants.FISCAL_BALANCE_LAST_DAY_OF_MONTH;

	/** pagina corrente. */
	private Integer currentPageId = Integer.valueOf(1);

	/** Lista para o combo de DealFiscal. */
	private List<String> dealFiscalComboList = new ArrayList<String>();

	/** Mapa para o combo de dealFisal. */
	private Map<String, Long> dealFiscalComboMap = new HashMap<String, Long>();

	/** Lista de Balanco Fiscal para a tela de DealFiscalFiscalBalance. */
	private List<DealFiscalFiscalBalanceRow> dealFiscalFiscalBalanceRowList = new ArrayList<DealFiscalFiscalBalanceRow>();

	/** Deal Fiscal selecionado. */
	private String dealFiscalSelected = null;

	/** Current DealFiscal. */
	private DealFiscal currentDealFiscal;
	
    /** Tela origem que deve ser aberta ao clicar no botao back.*/
    private String backTo = "";

	/** Reseta o bean. */
	public void reset() {
		backTo = "";
		this.currentDealFiscal = null;
		this.currentPageId = Integer.valueOf(1);
		this.filter = new FiscalBalanceFormFilter();
		this.msaList = new ArrayList<String>();
		this.msaMap = new HashMap<String, Long>();
		this.clienteList = new ArrayList<String>();
		this.clienteMap = new HashMap<String, Long>();
		this.resultList = new ArrayList<FiscalBalanceRow>();
		this.faturaPendenteList = new ArrayList<FaturaReceitaRow>();
		this.clobPendenteList = new ArrayList<FiscalBalancePendingClobRow>();
		this.option = Constants.FISCAL_BALANCE_LAST_DAY_OF_MONTH;
	}
	
	/** Reseta o meses e o anos da tela Revenue > Fiscal Balance > Fiscal Deal */
	public void resetFiscalDealOutcome() {
		this.setStartValidityMonth(null);
		this.setStartValidityYear(null);
		this.setEndValidityMonth(null);
		this.setEndValidityYear(null);
		this.option = Constants.FISCAL_BALANCE_LAST_DAY_OF_MONTH;
	}

	/**
	 * @return the resultList
	 */
	public List<FiscalBalanceRow> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(List<FiscalBalanceRow> resultList) {
		this.resultList = resultList;
	}

	/**
	 * @return the filter
	 */
	public FiscalBalanceFormFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(FiscalBalanceFormFilter filter) {
		this.filter = filter;
	}

	/**
	 * @return the msaList
	 */
	public List<String> getMsaList() {
		return msaList;
	}

	/**
	 * @param msaList
	 *            the msaList to set
	 */
	public void setMsaList(List<String> msaList) {
		this.msaList = msaList;
	}

	/**
	 * @return the msaMap
	 */
	public Map<String, Long> getMsaMap() {
		return msaMap;
	}

	/**
	 * @param msaMap
	 *            the msaMap to set
	 */
	public void setMsaMap(Map<String, Long> msaMap) {
		this.msaMap = msaMap;
	}

	/**
	 * @return the clienteList
	 */
	public List<String> getClienteList() {
		return clienteList;
	}

	/**
	 * @param clienteList
	 *            the clienteList to set
	 */
	public void setClienteList(List<String> clienteList) {
		this.clienteList = clienteList;
	}

	/**
	 * @return the clienteMap
	 */
	public Map<String, Long> getClienteMap() {
		return clienteMap;
	}

	/**
	 * @param clienteMap
	 *            the clienteMap to set
	 */
	public void setClienteMap(Map<String, Long> clienteMap) {
		this.clienteMap = clienteMap;
	}

	/**
	 * @return the validityMonthList
	 */
	public List<String> getValidityMonthList() {
		return validityMonthList;
	}

	/**
	 * @return the validityYearList
	 */
	public List<String> getValidityYearList() {
		int yearBegin = Integer.parseInt(systemProperties
				.getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
		int range = Integer.parseInt(systemProperties
				.getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

		List<String> yearList = new ArrayList<String>();

		for (int i = yearBegin; i <= yearBegin + range; i++) {
			yearList.add("" + i);
		}

		validityYearList = yearList;

		return validityYearList;
	}

	/**
	 * @param validityYearList
	 *            the validityYearList to set
	 */
	public void setValidityYearList(List<String> validityYearList) {
		this.validityYearList = validityYearList;
	}

	/**
	 * @return the startValidityMonth
	 */
	public String getStartValidityMonth() {
		return startValidityMonth;
	}

	/**
	 * @return Data baseado nos combos de inicio de mês e ano selecionado
	 */
	public Date getStartValidityDate() {
		return DateUtil.getDate(this.startValidityMonth, this.startValidityYear);
	}

	/**
	 * @param startValidityMonth
	 *            the startValidityMonth to set
	 */
	public void setStartValidityMonth(String startValidityMonth) {
		this.startValidityMonth = startValidityMonth;
	}

	/**
	 * @return the startValidityYear
	 */
	public String getStartValidityYear() {
		return startValidityYear;
	}

	/**
	 * @param startValidityYear
	 *            the startValidityYear to set
	 */
	public void setStartValidityYear(String startValidityYear) {
		this.startValidityYear = startValidityYear;
	}

	/**
	 * @return Data baseado nos combos de fim de mês e ano selecionado
	 */
	public Date getEndValidityDate() {
		return DateUtil.getDate(this.endValidityMonth, this.endValidityYear);
	}

	/**
	 * @return the endValidityMonth
	 */
	public String getEndValidityMonth() {
		return endValidityMonth;
	}

	/**
	 * @param endValidityMonth
	 *            the endValidityMonth to set
	 */
	public void setEndValidityMonth(String endValidityMonth) {
		this.endValidityMonth = endValidityMonth;
	}

	/**
	 * @return the endValidityYear
	 */
	public String getEndValidityYear() {
		return endValidityYear;
	}

	/**
	 * @param endValidityYear
	 *            the endValidityYear to set
	 */
	public void setEndValidityYear(String endValidityYear) {
		this.endValidityYear = endValidityYear;
	}

	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * @return the currentPageId
	 */
	public Integer getCurrentPageId() {
		return currentPageId;
	}

	/**
	 * @param currentPageId
	 *            the currentPageId to set
	 */
	public void setCurrentPageId(Integer currentPageId) {
		this.currentPageId = currentPageId;
	}

	/**
	 * @return the faturaPendenteList
	 */
	public List<FaturaReceitaRow> getFaturaPendenteList() {
		return faturaPendenteList;
	}

	/**
	 * @param faturaPendenteList
	 *            the faturaPendenteList to set
	 */
	public void setFaturaPendenteList(List<FaturaReceitaRow> faturaPendenteList) {
		this.faturaPendenteList = faturaPendenteList;
	}

	/**
	 * @return the dealFiscalComboList
	 */
	public List<String> getDealFiscalComboList() {
		return dealFiscalComboList;
	}

	/**
	 * @param dealFiscalComboList
	 *            the dealFiscalComboList to set
	 */
	public void setDealFiscalComboList(List<String> dealFiscalComboList) {
		this.dealFiscalComboList = dealFiscalComboList;
	}

	/**
	 * @return the dealFiscalComboMap
	 */
	public Map<String, Long> getDealFiscalComboMap() {
		return dealFiscalComboMap;
	}

	/**
	 * @param dealFiscalComboMap
	 *            the dealFiscalComboMap to set
	 */
	public void setDealFiscalComboMap(Map<String, Long> dealFiscalComboMap) {
		this.dealFiscalComboMap = dealFiscalComboMap;
	}

	/**
	 * @return the dealFiscalFiscalBalanceRowList
	 */
	public List<DealFiscalFiscalBalanceRow> getDealFiscalFiscalBalanceRowList() {
		return dealFiscalFiscalBalanceRowList;
	}

	/**
	 * @param dealFiscalFiscalBalanceRowList
	 *            the dealFiscalFiscalBalanceRowList to set
	 */
	public void setDealFiscalFiscalBalanceRowList(
			List<DealFiscalFiscalBalanceRow> dealFiscalFiscalBalanceRowList) {
		this.dealFiscalFiscalBalanceRowList = dealFiscalFiscalBalanceRowList;
	}

	/**
	 * @return the dealFiscalSelected
	 */
	public String getDealFiscalSelected() {
		return dealFiscalSelected;
	}

	/**
	 * @param dealFiscalSelected
	 *            the dealFiscalSelected to set
	 */
	public void setDealFiscalSelected(String dealFiscalSelected) {
		this.dealFiscalSelected = dealFiscalSelected;
	}

	/**
	 * @return the clobPendenteList
	 */
	public List<FiscalBalancePendingClobRow> getClobPendenteList() {
		return clobPendenteList;
	}

	/**
	 * @param clobPendenteList
	 *            the clobPendenteList to set
	 */
	public void setClobPendenteList(
			List<FiscalBalancePendingClobRow> clobPendenteList) {
		this.clobPendenteList = clobPendenteList;
	}

	/**
	 * @return the currentDealFiscal
	 */
	public DealFiscal getCurrentDealFiscal() {
		return currentDealFiscal;
	}

	/**
	 * @param currentDealFiscal
	 *            the currentDealFiscal to set
	 */
	public void setCurrentDealFiscal(DealFiscal currentDealFiscal) {
		this.currentDealFiscal = currentDealFiscal;
	}

	/**
	 * @return the backTo
	 */
	public String getBackTo() {
		return backTo;
	}

	/**
	 * @param backTo the backTo to set
	 */
	public void setBackTo(String backTo) {
		this.backTo = backTo;
	}
}