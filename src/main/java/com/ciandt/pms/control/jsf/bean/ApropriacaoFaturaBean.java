package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.vo.ApropriacaoFaturaFormFilter;
import com.ciandt.pms.model.vo.ApropriacaoFaturaRow;
import com.ciandt.pms.model.vo.FaturaReceitaRow;
import com.ciandt.pms.util.NumberUtil;


/**
 * 
 * A classe ApropriacaoFaturaBean proporciona as funcionalidades de backingBean.
 * 
 * @since 19/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ApropriacaoFaturaBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** Intancia de ReceitaDealFiscal. */
    private ReceitaDealFiscal receitaDealFiscal = new ReceitaDealFiscal();

    /** form do filtro. */
	private ApropriacaoFaturaFormFilter filter = new ApropriacaoFaturaFormFilter();

    /** pagina corrente. */
    private Integer currentPageId = Integer.valueOf(1);

    /** Lista para o combobox com as ContratoPratica. */
    private List<String> contratoPraticaList = new ArrayList<String>();

    /** Lista para o combobox com as ContratoPratica. */
    private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

    /** Lista para o combobox com as Cliente. */
    private List<String> clienteList = new ArrayList<String>();

    /** Lista para o combobox com as Cliente. */
    private Map<String, Long> clienteMap = new HashMap<String, Long>();

    /** Lista para o combobox com as NaturezaCentroLucro. */
    private List<String> naturezaList = new ArrayList<String>();

    /** Lista para o combobox com as NaturezaCentroLucro. */
    private Map<String, Long> naturezaMap = new HashMap<String, Long>();

    /** Lista para o combobox com as CentroLucro. */
    private List<String> centroLucroList = new ArrayList<String>();

    /** Lista para o combobox com as CentroLucro. */
    private Map<String, Long> centroLucroMap = new HashMap<String, Long>();

	/** Lista para o combobox com as CentroLucro. */
	private List<String> dealFiscalList = new ArrayList<String>();

	/** Lista para o combobox com as CentroLucro. */
	private Map<String, Long> dealFiscalMap = new HashMap<String, Long>();

    /** lista de resultados do filtro. */
	private List<ApropriacaoFaturaRow> filterResultList = new ArrayList<ApropriacaoFaturaRow>();

    /** Padrão da moeda. */
    private String patternCurrency = "";

    /** lista de dias dos meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;

    /** lista de fatura-receita. */
	private List<FaturaReceitaRow> faturaReceitaRowList = new ArrayList<FaturaReceitaRow>();

    /** valor total das faturas associadas ao ReceitaDealFiscal. */
    private Double totalAssociationInvoiceValue = Double.valueOf(0.0);

    /** valor total da receita-deal-fiscal. */
    private Double totalReceitaDealFiscal = Double.valueOf(0.0);

    /** fatura-receita. */
    private FaturaReceita faturaReceita = new FaturaReceita();

    /** fatura-receita. */
    private FaturaReceitaRow faturaReceitaRow = new FaturaReceitaRow();

    /** lista de fatura-receita. */
	private List<FaturaReceita> faturaReceitaList = new ArrayList<FaturaReceita>();

    /** entidade Fatura. */
    private Fatura fatura = new Fatura();
    
    /** entidade ContratoPratica. */
    private ContratoPratica contratoPratica = new ContratoPratica();
    
    /** True se a ação do back for para a tecla de Fiscal Balance > Search.*/
    private String backTo;

    /** Reseta o bean. */
    public void reset() {
    	backTo = "";
        resetFilter();
        resetTo();
        contratoPratica = new ContratoPratica();
    }

    /** Reseta o filtro. */
    public void resetFilter() {
        this.filter = new ApropriacaoFaturaFormFilter();

        this.filterResultList = new ArrayList<ApropriacaoFaturaRow>();

    }

    /** Reseta o TO. */
    public void resetTo() {
        this.receitaDealFiscal = new ReceitaDealFiscal();
    }

    /**
     * @param receitaDealFiscal
     *            the receitaDealFiscal to set
     */
    public void setReceitaDealFiscal(final ReceitaDealFiscal receitaDealFiscal) {
        this.receitaDealFiscal = receitaDealFiscal;
    }

    /**
     * @return the receitaDealFiscal
     */
    public ReceitaDealFiscal getReceitaDealFiscal() {
        return receitaDealFiscal;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final ApropriacaoFaturaFormFilter filter) {
        this.filter = filter;
    }

    /**
     * @return the filter
     */
    public ApropriacaoFaturaFormFilter getFilter() {
        return filter;
    }

    /**
     * @param currentPageId
     *            the currentPageId to set
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the currentPageId
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @return the contratoPraticaList
     */
    public List<String> getContratoPraticaList() {
        return contratoPraticaList;
    }

    /**
     * @param contratoPraticaList
     *            the contratoPraticaList to set
     */
    public void setContratoPraticaList(final List<String> contratoPraticaList) {
        this.contratoPraticaList = contratoPraticaList;
    }

    /**
     * @return the contratoPraticaMap
     */
    public Map<String, Long> getContratoPraticaMap() {
        return contratoPraticaMap;
    }

    /**
     * @param contratoPraticaMap
     *            the contratoPraticaMap to set
     */
    public void setContratoPraticaMap(final Map<String, Long> contratoPraticaMap) {
        this.contratoPraticaMap = contratoPraticaMap;
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
    public void setClienteList(final List<String> clienteList) {
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
    public void setClienteMap(final Map<String, Long> clienteMap) {
        this.clienteMap = clienteMap;
    }

    /**
     * @return the naturezaList
     */
    public List<String> getNaturezaList() {
        return naturezaList;
    }

    /**
     * @param naturezaList
     *            the naturezaList to set
     */
    public void setNaturezaList(final List<String> naturezaList) {
        this.naturezaList = naturezaList;
    }

    /**
     * @return the naturezaMap
     */
    public Map<String, Long> getNaturezaMap() {
        return naturezaMap;
    }

    /**
     * @param naturezaMap
     *            the naturezaMap to set
     */
    public void setNaturezaMap(final Map<String, Long> naturezaMap) {
        this.naturezaMap = naturezaMap;
    }

    /**
     * @return the centroLucroList
     */
    public List<String> getCentroLucroList() {
        return centroLucroList;
    }

    /**
     * @param centroLucroList
     *            the centroLucroList to set
     */
    public void setCentroLucroList(final List<String> centroLucroList) {
        this.centroLucroList = centroLucroList;
    }

    /**
     * @return the centroLucroMap
     */
    public Map<String, Long> getCentroLucroMap() {
        return centroLucroMap;
    }

    /**
     * @param centroLucroMap
     *            the centroLucroMap to set
     */
    public void setCentroLucroMap(final Map<String, Long> centroLucroMap) {
        this.centroLucroMap = centroLucroMap;
    }

    /**
     * @param filterResultList
     *            the filterResultList to set
     */
    public void setFilterResultList(
            final List<ApropriacaoFaturaRow> filterResultList) {
        this.filterResultList = filterResultList;
    }

    /**
     * @return the filterResultList
     */
    public List<ApropriacaoFaturaRow> getFilterResultList() {
        return filterResultList;
    }

    /**
     * @param monthList
     *            the monthList to set
     */
    public void setMonthList(final List<String> monthList) {
        this.monthList = monthList;
    }

    /**
     * @return the monthList
     */
    public List<String> getMonthList() {
        return monthList;
    }

    /**
     * @return lista de anos da vigencia
     */
    public List<String> getYearList() {

		int yearBegin = Integer.parseInt(systemProperties
                                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
		int range = Integer.parseInt(systemProperties
                                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yearList.add("" + i);
        }

        return yearList;
    }

    /**
     * @param faturaReceitaRowList
     *            the faturaReceitaRowList to set
     */
    public void setFaturaReceitaRowList(
            final List<FaturaReceitaRow> faturaReceitaRowList) {
        this.faturaReceitaRowList = faturaReceitaRowList;
    }

    /**
     * @return the faturaReceitaRowList
     */
    public List<FaturaReceitaRow> getFaturaReceitaRowList() {
        return faturaReceitaRowList;
    }

    /**
     * @param totalAssociationInvoiceValue
     *            the totalAssociationInvoiceValue to set
     */
    public void setTotalAssociationInvoiceValue(
            final Double totalAssociationInvoiceValue) {
        this.totalAssociationInvoiceValue = totalAssociationInvoiceValue;
    }

    /**
     * @return the totalAssociationInvoiceValue
     */
    public Double getTotalAssociationInvoiceValue() {
        if (totalAssociationInvoiceValue != null) {
            return NumberUtil.round(totalAssociationInvoiceValue);
        }
        return totalAssociationInvoiceValue;
    }

    /**
     * @param totalReceitaDealFiscal
     *            the totalReceitaDealFiscal to set
     */
    public void setTotalReceitaDealFiscal(final Double totalReceitaDealFiscal) {
        this.totalReceitaDealFiscal = totalReceitaDealFiscal;
    }

    /**
     * @return the totalReceitaDealFiscal
     */
    public Double getTotalReceitaDealFiscal() {
        if (totalReceitaDealFiscal != null) {
            return NumberUtil.round(totalReceitaDealFiscal);
        }

        return totalReceitaDealFiscal;
    }

    /**
     * @param patternCurrency
     *            the patternCurrency to set
     */
    public void setPatternCurrency(final String patternCurrency) {
        this.patternCurrency = patternCurrency;
    }

    /**
     * @return the patternCurrency
     */
    public String getPatternCurrency() {
        return patternCurrency;
    }

    /**
     * @param faturaReceita
     *            the faturaReceita to set
     */
    public void setFaturaReceita(final FaturaReceita faturaReceita) {
        this.faturaReceita = faturaReceita;
    }

    /**
     * @return the faturaReceita
     */
    public FaturaReceita getFaturaReceita() {
        return faturaReceita;
    }

    /**
     * @param fatura
     *            the fatura to set
     */
    public void setFatura(final Fatura fatura) {
        this.fatura = fatura;
    }

    /**
     * @return the fatura
     */
    public Fatura getFatura() {
        return fatura;
    }

    /**
     * @param faturaReceitaList
     *            the faturaReceitaList to set
     */
    public void setFaturaReceitaList(final List<FaturaReceita> faturaReceitaList) {
        this.faturaReceitaList = faturaReceitaList;
    }

    /**
     * @return the faturaReceitaList
     */
    public List<FaturaReceita> getFaturaReceitaList() {
        return faturaReceitaList;
    }

    /**
     * @param faturaReceitaRow
     *            the faturaReceitaRow to set
     */
    public void setFaturaReceitaRow(final FaturaReceitaRow faturaReceitaRow) {
        this.faturaReceitaRow = faturaReceitaRow;
    }

    /**
     * @return the faturaReceitaRow
     */
    public FaturaReceitaRow getFaturaReceitaRow() {
        return faturaReceitaRow;
    }

    /**
	 * @return the dealFiscalList
	 */
	public List<String> getDealFiscalList() {
		return dealFiscalList;
	}

	/**
	 * @param dealFiscalList
	 *            the dealFiscalList to set
	 */
	public void setDealFiscalList(List<String> dealFiscalList) {
		this.dealFiscalList = dealFiscalList;
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
	public void setDealFiscalMap(Map<String, Long> dealFiscalMap) {
		this.dealFiscalMap = dealFiscalMap;
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

	/**
	 * @return the contratoPratica
	 */
	public ContratoPratica getContratoPratica() {
		return contratoPratica;
	}

	/**
	 * @param contratoPratica the contratoPratica to set
	 */
	public void setContratoPratica(ContratoPratica contratoPratica) {
		this.contratoPratica = contratoPratica;
	}

	/**
     * Retorna o balanço do Fiscal Balance (Total invoice - (Total Revenue +=
     * Total Adjustment)).
     * 
     * @return total do fiscal balance
     */
    public Double getBalance() {
        Double totalInvoice = getTotalAssociationInvoiceValue();
        Double totalReceita = getTotalReceitaDealFiscal();
		Double totalAjuste = receitaDealFiscal.getTotalAdjustmentValue()
				.doubleValue();
        return totalInvoice - (totalReceita += totalAjuste);
    }
}