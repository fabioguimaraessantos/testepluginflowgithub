package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ciandt.pms.model.CompanyErp;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.enums.XeroLineIntegration;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.HistoricoFatura;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.vo.FaturaRow;
import com.ciandt.pms.model.vo.ItemFaturaRow;

/**
 * 
 * A classe FaturaBean é utilizado como backingBean para a entidade Fatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class FaturaBean implements Serializable {

	public static final String BACK_TO_FATURA_MANAGE_FINANCIAL_VIEW = "FaturaManageFinancialView";

	public static final String BACK_TO_FATURA_MANAGE_VIEW = "FaturaManageView";
	
	public static final String BACK_TO_FATURA_MANAGE = "FaturaManage";

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** to do backingBean. */
	private Fatura to = new Fatura();

	/** filter do backingBean. */
	private Fatura filter = new Fatura();

	/** filter do backingBean. */
	private ItemFatura itemFaturaFilter = new ItemFatura();

	/** to FaturaRow corrente. */
	private FaturaRow faturaRow = new FaturaRow();

	/** lista de resultados da pesquisa. */
	private List<FaturaRow> resultList = new ArrayList<FaturaRow>();

	/** Lista para o combobox Cliente. */
	private List<String> clienteList = new ArrayList<String>();

	/** Lista para o combobox Cliente. */
	private Map<String, Long> clienteMap = new HashMap<String, Long>();

	/** Lista para o combobox DealFiscal. */
	private List<String> dealFiscalList = new ArrayList<String>();

	/** Lista para o combobox DealFiscal. */
	private Map<String, Long> dealFiscalMap = new HashMap<String, Long>();

	/** Lista para o combobox Empresa. */
	private List<String> empresaList = new ArrayList<String>();

	/** Lista para o combobox Empresa. */
	private Map<String, Long> empresaMap = new HashMap<String, Long>();

	/** Lista para o combobox Moeda. */
	private List<String> moedaList = new ArrayList<String>();

	/** Lista para o combobox Moeda. */
	private Map<String, Long> moedaMap = new HashMap<String, Long>();

	/** Lista para o combobox de Contrato-Pratica. */
	private List<String> contratoPraticaList = new ArrayList<String>();

	/** Lista para o combobox de Contrato-Pratica. */
	private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);

	/** Indicador do modo em tempo de execucao - create/update. */
	private Boolean isUpdate = Boolean.valueOf(false);

	/** Indicador se algum item da fatura foi comissionado. */
	private Boolean isCommissioned = Boolean.valueOf(false);

	/** Indicador do modo em tempo de execucao - remove/view. */
	private Boolean isRemove = Boolean.valueOf(false);

	/** dataPrevisao inicio. */
	private Date dataPrevisaoBeg = null;

	private boolean isAllPending;

	/** dataPrevisao fim. */
	private Date dataPrevisaoEnd = null;

	/** dataPrevisao inicio. */
	private Date dataPrevisaoBeg2 = null;

	/** dataPrevisao fim. */
	private Date dataPrevisaoEnd2 = null;

	/** Indicador do padrão para exibição de valores de moeda. */
	private String patternCurrency = "";

	/** Soma dos valores das Fatura listadas. */
	private BigDecimal totalGeralFatura = BigDecimal.valueOf(0);

	/** Mostra a linha de totalizacao se for apenas uma Moeda. */
	private Boolean showTotalGeralFatura = Boolean.valueOf(true);

	/** Determina o número de copias a ser realizada. */
	private Integer numberOfCopies = null;

	/** Lista para o combobox com os Msa. */
	private List<String> msaList = new ArrayList<String>();

	/** Lista para o combobox com os Msa. */
	private Map<String, Long> msaMap = new HashMap<String, Long>();

	/** Nome do {@link Msa} selecionado. */
	private String msaSelected = "";

	/** Flag que indica se o search da fatura é para o financeiro. */
	private Boolean financialMode = Boolean.valueOf(false);

	/** dataCancelamento da Fatura. */
	private Date dataCancelamento = null;

	/** dataVencimento da Fatura. */
	private Date dataVencimento = null;

	/** dataCancelamento da Fatura. */
	private Date dataPagamento = null;

	/** textoRazaoCancelamento da Fatura. */
	private String textoRazaoCancelamento = "";

	/** habilita a edicao da data de previsao. */
	private Boolean enableDate = Boolean.valueOf(false);

	/** habilita a edicao da data de vencimento. */
	private Boolean enableExpiryDate = Boolean.valueOf(false);

	/** Cliente utilizado no filtro. */
	private Cliente clienteFilter = new Cliente();

	/** Msa utilizado no filtro. */
	private Msa msaFilter = new Msa();

	/** Empresa utilizado no filtro. */
	private Empresa empresaFilter = new Empresa();

	/** Utilizado para filtrar somente itens não pagos. */
	private Boolean notPaidOnly = Boolean.valueOf(true);

	/** Lista com os itens da invoices. */
	private List<ItemFaturaRow> itemFaturaRowList = new ArrayList<ItemFaturaRow>();

	/** Pagina corrente. */
	private Integer currentPageId;

	/** Lista para o combobox com clientes. */
	private List<String> aeList = new ArrayList<String>();

	/** Lista para o combobox com as clientes. */
	private Map<String, Long> aeMap = new HashMap<String, Long>();

	/** Lista de HistoricoFatura da Fatura corrente. */
	private List<HistoricoFatura> historicoFaturaList = new ArrayList<HistoricoFatura>(
			0);

	/** Utilizado para indicar se a Fatura será editável ou não. */
	private Boolean isFaturaEditable = Boolean.valueOf(true);

	/** Utilizado para indicar se usuario e admin. */
	private Boolean isAdmin = Boolean.valueOf(false);

	/** Indica se a mensagem de erro deve ser mostrada. */
	private Boolean showMessageError = Boolean.valueOf(false);

	/** Recebe o valor selecionado no radio. */
	private boolean radioOption = true;

	/** Lista de SSO para combobox. */
	private List<String> SSOList = new ArrayList<String>();

	/** Map para combobx de SSO. */
	private Map<String, Long> SSOMap = new HashMap<String, Long>();

	/** Lista de UMKT para combobox. */
	private List<String> UMKTList = new ArrayList<String>();

	/** Map para combobx de UMKT. */
	private Map<String, Long> UMKTMap = new HashMap<String, Long>();

	/** Data de pagamento do {@link ItemFatura} usado no filtro do search. */
	private Date dataPagamentoDe = null;

	/** Data de pagamento do {@link ItemFatura} usado no filtro do search. */
	private Date dataPagamentoAte = null;

	/** Valor do total de fiscalBalance. */
	private Double publishedFiscalBalance = new Double(0);
	
	/** Valor do total de fiscalBalance. */
	private Double publishedFiscalBalance2 = new Double(0);
	
	private String backTo;

	private Boolean searchDeletedInvoice;

	private Boolean isFaturaIntercompany;

	private Boolean isINCCompany;

	private Boolean isDocEditable;

	private Integer faturaRowsSelected = 0;

	private Boolean isIntegrateButtonDisabled = true;

	private XeroLineIntegration xeroLineIntegration;

	private Integer faturaRowsSelectedToShowXeroLineIntegrationModal = 0;

	private Boolean showXeroLineIntegrationModal = false;

	private CompanyErp companyErp;

	/**
	 * @return the radioOption
	 */
	public boolean getRadioOption() {
		return radioOption;
	}

	/**
	 * @param radioOption
	 *            the radioOption to set
	 */
	public void setRadioOption(boolean radioOption) {
		this.radioOption = radioOption;
	}

	/**
	 * @return the isFaturaEditable
	 */
	public Boolean getIsFaturaEditable() {
		return isFaturaEditable;
	}

	/**
	 * @param isFaturaEditable
	 *            the isFaturaEditable to set
	 */
	public void setIsFaturaEditable(final Boolean isFaturaEditable) {
		this.isFaturaEditable = isFaturaEditable;
	}

	/**
	 * @return the historicoFaturaList
	 */
	public List<HistoricoFatura> getHistoricoFaturaList() {
		return historicoFaturaList;
	}

	/**
	 * @param historicoFaturaList
	 *            the historicoFaturaList to set
	 */
	public void setHistoricoFaturaList(
			final List<HistoricoFatura> historicoFaturaList) {
		this.historicoFaturaList = historicoFaturaList;
	}

	/**
	 * @return the dataCancelamento
	 */
	public Date getDataCancelamento() {
		return dataCancelamento;
	}

	/**
	 * @param dataCancelamento
	 *            the dataCancelamento to set
	 */
	public void setDataCancelamento(final Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	/**
	 * @return the textoRazaoCancelamento
	 */
	public String getTextoRazaoCancelamento() {
		return textoRazaoCancelamento;
	}

	/**
	 * @param textoRazaoCancelamento
	 *            the textoRazaoCancelamento to set
	 */
	public void setTextoRazaoCancelamento(final String textoRazaoCancelamento) {
		this.textoRazaoCancelamento = textoRazaoCancelamento;
	}

	/**
	 * @return the showTotalGeralFatura
	 */
	public Boolean getShowTotalGeralFatura() {
		return showTotalGeralFatura;
	}

	/**
	 * @param showTotalGeralFatura
	 *            the showTotalGeralFatura to set
	 */
	public void setShowTotalGeralFatura(final Boolean showTotalGeralFatura) {
		this.showTotalGeralFatura = showTotalGeralFatura;
	}

	/**
	 * @return the totalGeralFatura
	 */
	public BigDecimal getTotalGeralFatura() {
		return totalGeralFatura;
	}

	/**
	 * @param totalGeralFatura
	 *            the totalGeralFatura to set
	 */
	public void setTotalGeralFatura(final BigDecimal totalGeralFatura) {
		this.totalGeralFatura = totalGeralFatura;
	}

	/**
	 * @return the faturaRow
	 */
	public FaturaRow getFaturaRow() {
		return faturaRow;
	}

	/**
	 * @param faturaRow
	 *            the faturaRow to set
	 */
	public void setFaturaRow(final FaturaRow faturaRow) {
		this.faturaRow = faturaRow;
	}

	/**
	 * @return the dataPrevisaoBeg
	 */
	public Date getDataPrevisaoBeg() {
		return dataPrevisaoBeg;
	}

	/**
	 * @param dataPrevisaoBeg
	 *            the dataPrevisaoBeg to set
	 */
	public void setDataPrevisaoBeg(final Date dataPrevisaoBeg) {
		this.dataPrevisaoBeg = dataPrevisaoBeg;
	}

	/**
	 * @return the dataPrevisaoEnd
	 */
	public Date getDataPrevisaoEnd() {
		return dataPrevisaoEnd;
	}

	/**
	 * @param dataPrevisaoEnd
	 *            the dataPrevisaoEnd to set
	 */
	public void setDataPrevisaoEnd(final Date dataPrevisaoEnd) {
		this.dataPrevisaoEnd = dataPrevisaoEnd;
	}

	/**
	 * @return the dataPrevisaoBeg
	 */
	public Date getDataPrevisaoBeg2() {
		return dataPrevisaoBeg2;
	}

	/**
	 * @param dataPrevisaoBeg
	 *            the dataPrevisaoBeg to set
	 */
	public void setDataPrevisaoBeg2(final Date dataPrevisaoBeg2) {
		this.dataPrevisaoBeg2 = dataPrevisaoBeg2;
	}

	/**
	 * @return the dataPrevisaoEnd
	 */
	public Date getDataPrevisaoEnd2() {
		return dataPrevisaoEnd2;
	}

	/**
	 * @param dataPrevisaoEnd
	 *            the dataPrevisaoEnd to set
	 */
	public void setDataPrevisaoEnd2(final Date dataPrevisaoEnd2) {
		this.dataPrevisaoEnd2 = dataPrevisaoEnd2;
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
	public void setDealFiscalList(final List<String> dealFiscalList) {
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
	public void setDealFiscalMap(final Map<String, Long> dealFiscalMap) {
		this.dealFiscalMap = dealFiscalMap;
	}

	/**
	 * @return the empresaList
	 */
	public List<String> getEmpresaList() {
		return empresaList;
	}

	/**
	 * @param empresaList
	 *            the empresaList to set
	 */
	public void setEmpresaList(final List<String> empresaList) {
		this.empresaList = empresaList;
	}

	/**
	 * @return the empresaMap
	 */
	public Map<String, Long> getEmpresaMap() {
		return empresaMap;
	}

	/**
	 * @param empresaMap
	 *            the empresaMap to set
	 */
	public void setEmpresaMap(final Map<String, Long> empresaMap) {
		this.empresaMap = empresaMap;
	}

	/**
	 * @return the moedaList
	 */
	public List<String> getMoedaList() {
		return moedaList;
	}

	/**
	 * @param moedaList
	 *            the moedaList to set
	 */
	public void setMoedaList(final List<String> moedaList) {
		this.moedaList = moedaList;
	}

	/**
	 * @return the moedaMap
	 */
	public Map<String, Long> getMoedaMap() {
		return moedaMap;
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
	public void setContratoPraticaList(List<String> contratoPraticaList) {
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
	public void setContratoPraticaMap(Map<String, Long> contratoPraticaMap) {
		this.contratoPraticaMap = contratoPraticaMap;
	}

	/**
	 * @param moedaMap
	 *            the moedaMap to set
	 */
	public void setMoedaMap(final Map<String, Long> moedaMap) {
		this.moedaMap = moedaMap;
	}

	/**
	 * @return the patternCurrency
	 */
	public String getPatternCurrency() {
		return patternCurrency + " ";
	}

	/**
	 * @param patternCurrency
	 *            the patternCurrency to set
	 */
	public void setPatternCurrency(final String patternCurrency) {
		this.patternCurrency = patternCurrency;
	}

	/**
	 * @return the resultList
	 */
	public List<FaturaRow> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(final List<FaturaRow> resultList) {
		this.resultList = resultList;
	}

	/**
	 * @return the currentRowId
	 */
	public Long getCurrentRowId() {
		return currentRowId;
	}

	/**
	 * @param currentRowId
	 *            the currentRowId to set
	 */
	public void setCurrentRowId(final Long currentRowId) {
		this.currentRowId = currentRowId;
	}

	/**
	 * @return the isUpdate
	 */
	public Boolean getIsUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate
	 *            the isUpdate to set
	 */
	public void setIsUpdate(final Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @return the isCommissioned
	 */
	public Boolean getIsCommissioned() {
		return isCommissioned;
	}

	/**
	 * @param isCommissioned
	 *            the isCommissioned to set
	 */
	public void setIsCommisioned(final Boolean isCommissioned) {
		this.isCommissioned = isCommissioned;
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
	 * @param to
	 *            the to to set
	 */
	public void setTo(final Fatura to) {
		this.to = to;
	}

	/**
	 * 
	 * @return retorna o TO
	 */
	public Fatura getTo() {
		// Verifica se as entidades de relacionamento sao nulas,
		// se verdade instancia as novas entidades
		// Isto é necessario na criacao de uma entidade, pois as referencias
		// não podem ser nulas
		if (to == null) {
			to = new Fatura();
		}
		if (to.getDealFiscal() == null) {
			to.setDealFiscal(new DealFiscal());
		}
		if (to.getDealFiscal().getMsa() == null) {
			to.getDealFiscal().setMsa(new Msa());
		}
		if (to.getDealFiscal().getMsa().getCliente() == null) {
			to.getDealFiscal().getMsa().setCliente(new Cliente());
		}
		if (to.getDealFiscal().getCliente() == null) {
			to.getDealFiscal().setCliente(new Cliente());
		}
		if (to.getMoeda() == null) {
			to.setMoeda(new Moeda());
		}

		return to;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final Fatura filter) {
		this.filter = filter;
	}

	/**
	 * 
	 * @return o filtro do TO
	 */
	public Fatura getFilter() {
		if (filter == null) {
			filter = new Fatura();
		}
		if (filter.getDealFiscal() == null) {
			filter.setDealFiscal(new DealFiscal());
		}
		if (filter.getDealFiscal().getMsa() == null) {
			filter.getDealFiscal().setMsa(new Msa());
		}
		if (filter.getDealFiscal().getMsa().getCliente() == null) {
			filter.getDealFiscal().getMsa().setCliente(new Cliente());
		}
		if (filter.getDealFiscal().getCliente() == null) {
			filter.getDealFiscal().setCliente(new Cliente());
		}
		if (filter.getMoeda() == null) {
			filter.setMoeda(new Moeda());
		}

		return filter;
	}

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		resetTo();
		resetFilter();
		resetResultList();
		resetTotalValues();
		resetCancelFields();
		this.msaList = new ArrayList<String>();
		this.msaSelected = "";
		this.dealFiscalList = new ArrayList<String>();
		this.setFinancialMode(Boolean.FALSE);
		this.historicoFaturaList = new ArrayList<HistoricoFatura>();
		this.enableDate = Boolean.valueOf(false);
		this.enableExpiryDate = Boolean.valueOf(false);
		this.backTo = "";
		this.setSearchDeletedInvoice(Boolean.FALSE);
		this.isAllPending = true;
		this.faturaRowsSelected = 0;
		this.isIntegrateButtonDisabled = true;
		this.xeroLineIntegration = null;
		this.faturaRowsSelectedToShowXeroLineIntegrationModal = 0;
		this.showXeroLineIntegrationModal = false;
		this.companyErp = null;
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new Fatura();
	}

	/**
	 * Reseta o filter.
	 */
	public void resetFilter() {
		this.filter = new Fatura();
		this.clienteFilter = new Cliente();
		this.msaFilter = new Msa();
	}

	/**
	 * Reseta a lista de to.
	 */
	public void resetResultList() {
		this.resultList = new ArrayList<FaturaRow>();
		this.contratoPraticaList = new ArrayList<>();
		this.contratoPraticaMap = new HashMap<>();
	}

	/**
	 * Reseta a data de vigencia.
	 */
	public void resetDataPrevisao() {
		this.dataPrevisaoBeg = null;
		this.dataPrevisaoEnd = null;
	}

	/**
	 * Reseta os valures totais.
	 */
	public void resetTotalValues() {
		this.totalGeralFatura = BigDecimal.valueOf(0);
	}

	/**
	 * Reseta os campos de cancelamento da Fatura.
	 */
	public void resetCancelFields() {
		this.dataCancelamento = null;
		this.textoRazaoCancelamento = "";
	}

	public boolean getIsAllPending() {
		return isAllPending;
	}

	public void setAllPending(boolean allPending) {
		isAllPending = allPending;
	}

	/**
	 * Reseta o campo identificador Fatura editável.
	 */
	public void resetIsFaturaEditable() {
		this.isFaturaEditable = Boolean.valueOf(true);
	}

	/**
	 * Atualiza os valores dos totais.
	 * 
	 * @param totalGeralFatura
	 *            - valor total dos ItemFatura
	 */
	public void updateTotalValues(final double totalGeralFatura) {
		this.totalGeralFatura = BigDecimal.valueOf(totalGeralFatura);
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
	 * @param numberOfCopies
	 *            the numberOfCopies to set
	 */
	public void setNumberOfCopies(final Integer numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}

	/**
	 * @return the numberOfCopies
	 */
	public Integer getNumberOfCopies() {
		return numberOfCopies;
	}

	/**
	 * @param msaList
	 *            the msaList to set
	 */
	public void setMsaList(final List<String> msaList) {
		this.msaList = msaList;
	}

	/**
	 * @return the msaList
	 */
	public List<String> getMsaList() {
		return msaList;
	}

	/**
	 * @param msaMap
	 *            the msaMap to set
	 */
	public void setMsaMap(final Map<String, Long> msaMap) {
		this.msaMap = msaMap;
	}

	/**
	 * @return the msaMap
	 */
	public Map<String, Long> getMsaMap() {
		return msaMap;
	}

	/**
	 * @param financialMode
	 *            the financialMode to set
	 */
	public void setFinancialMode(final Boolean financialMode) {
		this.financialMode = financialMode;
	}

	/**
	 * @return the financialMode
	 */
	public Boolean getFinancialMode() {
		return financialMode;
	}

	/**
	 * @param clienteFilter
	 *            the clienteFilter to set
	 */
	public void setClienteFilter(final Cliente clienteFilter) {
		this.clienteFilter = clienteFilter;
	}

	/**
	 * @return the clienteFilter
	 */
	public Cliente getClienteFilter() {
		return clienteFilter;
	}

	/**
	 * @param msaFilter
	 *            the msaFilter to set
	 */
	public void setContratoPraticaFilter(final Msa msaFilter) {
		this.msaFilter = msaFilter;
	}

	/**
	 * @return the msaFilter
	 */
	public Msa getMsaFilter() {
		return msaFilter;
	}

	/**
	 * @param empresaFilter
	 *            the empresaFilter to set
	 */
	public void setEmpresaFilter(final Empresa empresaFilter) {
		this.empresaFilter = empresaFilter;
	}

	/**
	 * @return the empresaFilter
	 */
	public Empresa getEmpresaFilter() {
		return empresaFilter;
	}

	/**
	 * @param notPaidOnly
	 *            the notPaidOnly to set
	 */
	public void setNotPaidOnly(final Boolean notPaidOnly) {
		this.notPaidOnly = notPaidOnly;
	}

	/**
	 * @return the notPaidOnly
	 */
	public Boolean getNotPaidOnly() {
		return notPaidOnly;
	}

	/**
	 * @param itemFaturaFilter
	 *            the itemFaturaFilter to set
	 */
	public void setItemFaturaFilter(final ItemFatura itemFaturaFilter) {
		this.itemFaturaFilter = itemFaturaFilter;
	}

	/**
	 * @return the itemFaturaFilter
	 */
	public ItemFatura getItemFaturaFilter() {
		return itemFaturaFilter;
	}

	/**
	 * @param itemFaturarowList
	 *            the itemFaturarowList to set
	 */
	public void setItemFaturaRowList(final List<ItemFaturaRow> itemFaturarowList) {
		this.itemFaturaRowList = itemFaturarowList;
	}

	/**
	 * @return the itemFaturarowList
	 */
	public List<ItemFaturaRow> getItemFaturaRowList() {
		return itemFaturaRowList;
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
	 * @param dataVencimento
	 *            the dataVencimento to set
	 */
	public void setDataVencimento(final Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return dataVencimento;
	}

	/**
	 * @param dataPagamento
	 *            the dataPagamento to set
	 */
	public void setDataPagamento(final Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return dataPagamento;
	}

	/**
	 * @param aeList
	 *            the aeList to set
	 */
	public void setAeList(final List<String> aeList) {
		this.aeList = aeList;
	}

	/**
	 * @return the aeList
	 */
	public List<String> getAeList() {
		return aeList;
	}

	/**
	 * @param aeMap
	 *            the aeMap to set
	 */
	public void setAeMap(final Map<String, Long> aeMap) {
		this.aeMap = aeMap;
	}

	/**
	 * @return the aeMap
	 */
	public Map<String, Long> getAeMap() {
		return aeMap;
	}

	/**
	 * @return the enableDates
	 */
	public Boolean getEnableDate() {
		return enableDate;
	}

	/**
	 * @param enableDate
	 *            the enableDate to set
	 */
	public void setEnableDate(final Boolean enableDate) {
		this.enableDate = enableDate;
	}

	/**
	 * @return the enableExpiryDate
	 */
	public Boolean getEnableExpiryDate() {
		return enableExpiryDate;
	}

	/**
	 * @param enableExpiryDate
	 *            the enableExpiryDate to set
	 */
	public void setEnableExpiryDate(final Boolean enableExpiryDate) {
		this.enableExpiryDate = enableExpiryDate;
	}

	/**
	 * @return the isAdmin
	 */
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	/**
	 * @param isAdmin
	 *            the isAdmin to set
	 */
	public void setIsAdmin(final Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * @return the showMessageError
	 */
	public Boolean getShowMessageError() {
		return showMessageError;
	}

	/**
	 * @param showMessageError
	 *            the showMessageError to set
	 */
	public void setShowMessageError(final Boolean showMessageError) {
		this.showMessageError = showMessageError;
	}

	/**
	 * @return the sSOList
	 */
	public List<String> getSSOList() {
		return SSOList;
	}

	/**
	 * @param sSOList
	 *            the sSOList to set
	 */
	public void setSSOList(final List<String> sSOList) {
		SSOList = sSOList;
	}

	/**
	 * @return the sSOMap
	 */
	public Map<String, Long> getSSOMap() {
		return SSOMap;
	}

	/**
	 * @param sSOMap
	 *            the sSOMap to set
	 */
	public void setSSOMap(final Map<String, Long> sSOMap) {
		SSOMap = sSOMap;
	}

	/**
	 * @return the uMKTList
	 */
	public List<String> getUMKTList() {
		return UMKTList;
	}

	/**
	 * @param uMKTList
	 *            the uMKTList to set
	 */
	public void setUMKTList(final List<String> uMKTList) {
		UMKTList = uMKTList;
	}

	/**
	 * @return the uMKTMap
	 */
	public Map<String, Long> getUMKTMap() {
		return UMKTMap;
	}

	/**
	 * @param uMKTMap
	 *            the uMKTMap to set
	 */
	public void setUMKTMap(final Map<String, Long> uMKTMap) {
		UMKTMap = uMKTMap;
	}

	/**
	 * @return the msaSelected
	 */
	public String getMsaSelected() {
		return msaSelected;
	}

	/**
	 * @param msaSelected
	 *            the msaSelected to set
	 */
	public void setMsaSelected(final String msaSelected) {
		this.msaSelected = msaSelected;
	}

	/**
	 * @return the dataPagamentoDe
	 */
	public Date getDataPagamentoDe() {
		return dataPagamentoDe;
	}

	/**
	 * @param dataPagamentoDe
	 *            the dataPagamentoDe to set
	 */
	public void setDataPagamentoDe(final Date dataPagamentoDe) {
		this.dataPagamentoDe = dataPagamentoDe;
	}

	/**
	 * @return the dataPagamentoAte
	 */
	public Date getDataPagamentoAte() {
		return dataPagamentoAte;
	}

	/**
	 * @param dataPagamentoAte
	 *            the dataPagamentoAte to set
	 */
	public void setDataPagamentoAte(final Date dataPagamentoAte) {
		this.dataPagamentoAte = dataPagamentoAte;
	}

	/**
	 * @return the publishedFiscalBalance
	 */
	public Double getPublishedFiscalBalance() {
		return publishedFiscalBalance;
	}

	/**
	 * @param publishedFiscalBalance
	 *            the publishedFiscalBalance to set
	 */
	public void setPublishedFiscalBalance(Double publishedFiscalBalance) {
		this.publishedFiscalBalance = publishedFiscalBalance;
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
	 * @return the publishedFiscalBalance2
	 */
	public Double getPublishedFiscalBalance2() {
		return publishedFiscalBalance2;
	}

	/**
	 * @param publishedFiscalBalance2 the publishedFiscalBalance2 to set
	 */
	public void setPublishedFiscalBalance2(Double publishedFiscalBalance2) {
		this.publishedFiscalBalance2 = publishedFiscalBalance2;
	}

	/**
	 * @return the searchDeletedInvoice
	 */
	public Boolean getSearchDeletedInvoice() {
		return searchDeletedInvoice;
	}

	/**
	 * @param searchDeletedInvoice the searchDeletedInvoice to set
	 */
	public void setSearchDeletedInvoice(Boolean searchDeletedInvoice) {
		this.searchDeletedInvoice = searchDeletedInvoice;
	}

	/**
	 * @return the isFaturaIntercompany
	 */
	public Boolean getIsFaturaIntercompany() {
		return isFaturaIntercompany;
	}

	/**
	 * @param isFaturaIntercompany the isFaturaIntercompany to set
	 */
	public void setIsFaturaIntercompany(Boolean isFaturaIntercompany) {
		this.isFaturaIntercompany = isFaturaIntercompany;
	}

	/**
	 * @return the isINCCompany
	 */
	public Boolean getIsINCCompany() {
		return isINCCompany;
	}

	/**
	 * @param isINCCompany the isINCCompany to set
	 */
	public void setIsINCCompany(Boolean isINCCompany) {
		this.isINCCompany = isINCCompany;
	}

	/**
	 *
	 * @return the isDocEditable
	 */
	public Boolean getDocEditable() {
		return isDocEditable;
	}

	/**
	 *
	 * @param docEditable the isDocEditable to set
	 */
	public void setDocEditable(Boolean docEditable) {
		isDocEditable = docEditable;
	}

	public Integer getFaturaRowSelected() {
		return this.faturaRowsSelected;
	}

	public void setFaturaRowsSelected(final Integer faturaRowsSelected) {
		this.faturaRowsSelected = faturaRowsSelected;
	}

	public Integer addToFaturaRowsSelected(final Integer quantityToAdd) {
		if (quantityToAdd == null) return 0;
		this.faturaRowsSelected += quantityToAdd;
		return this.faturaRowsSelected;
	}

	public Boolean getIsIntegrateButtonDisabled() {
		return this.isIntegrateButtonDisabled;
	}

	public void setIsIntegrateButtonDisabled(final Boolean isIntegrateButtonDisabled) {
		this.isIntegrateButtonDisabled = isIntegrateButtonDisabled;
	}

	public XeroLineIntegration getXeroLineIntegration() {
		return this.xeroLineIntegration;
	}

	public void setXeroLineIntegration(final XeroLineIntegration xeroLineIntegration) {
		this.xeroLineIntegration = xeroLineIntegration;
	}

	public Integer getFaturaRowsSelectedToShowXeroLineIntegrationModal() {
		return this.faturaRowsSelectedToShowXeroLineIntegrationModal;
	}

	public void setFaturaRowsSelectedToShowXeroLineIntegrationModal(final Integer faturaRowsSelectedToShowXeroLineIntegrationModal) {
		this.faturaRowsSelectedToShowXeroLineIntegrationModal = faturaRowsSelectedToShowXeroLineIntegrationModal;
	}

	public Integer addToFaturaRowsSelectedToShowXeroLineIntegrationModal(final Integer quantityToAdd) {
		if (quantityToAdd == null) return 0;
		this.faturaRowsSelectedToShowXeroLineIntegrationModal += quantityToAdd;
		return this.faturaRowsSelectedToShowXeroLineIntegrationModal;
	}

	public Boolean getShowXeroLineIntegrationModal() {
		return this.showXeroLineIntegrationModal;
	}

	public void setShowXeroLineIntegrationModal(final Boolean showXeroLineIntegrationModal) {
		this.showXeroLineIntegrationModal = showXeroLineIntegrationModal;
	}

	public CompanyErp getCompanyErp() {
		return this.companyErp;
	}

	public void setCompanyErp(final CompanyErp companyErp) {
		this.companyErp = companyErp;
	}
}