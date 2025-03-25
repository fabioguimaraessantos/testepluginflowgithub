package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ciandt.pms.enums.AjusteReceitaForecastStatusEnum;
import com.ciandt.pms.model.AjusteReceitaForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.HistoricoReceita;
import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.model.MotivoResultado;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaLicenca;
import com.ciandt.pms.model.VwReceitaFilter;
import com.ciandt.pms.model.VwReceitaFilterId;
import com.ciandt.pms.model.vo.ItemReceitaRow;
import com.ciandt.pms.model.vo.ReceitaDealFiscalRow;
import com.ciandt.pms.model.vo.ReceitaFilter;
import com.ciandt.pms.model.vo.ReceitaLicencaRow;
import com.ciandt.pms.model.vo.ReceitaMoedaRow;
import com.ciandt.pms.model.vo.ShortTermRevenueRow;

/**
 * 
 * A classe ReceitaBean é utilizado como backingBean para a entidade Receita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ReceitaBean implements Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** to do backingBean. */
	private Receita to = new Receita();

	/** to ReceitaDealFiscal do BackingBean. */
	private ReceitaDealFiscal toReceitaDealFiscal;

	/** filter do backingBean. */
	private Receita filter = new Receita();

	/** to ItemReceita do backingBean. */
	private ItemReceita toItemReceita = new ItemReceita();

	/** to ItemReceitaRow do backingBean. */
	private ItemReceitaRow toItemReceitaRow = new ItemReceitaRow();

	/** lista de {@link ReceitaDealFiscalRow}. */
	private List<ReceitaDealFiscalRow> listaRecDealFiscalRow = new ArrayList<ReceitaDealFiscalRow>();

	/** lista de {@link DealFiscal}. */
	private List<DealFiscal> listaDealFiscal = new ArrayList<DealFiscal>();

	/** lista de resultados da pesquisa. */
	private List<Receita> resultList = new ArrayList<Receita>();

	/** lista de resultados da pesquisa. */
	private List<ReceitaDealFiscal> receitaDealFiscalList = new ArrayList<ReceitaDealFiscal>();

	/** Lista para o combobox com as ContratoPratica. */
	private List<String> contratoPraticaList = new ArrayList<String>();

	/** Lista para o combobox com as ContratoPratica. */
	private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

	/** Lista para o combobox com as ReceitaTipo. */
	private List<String> receitaTipoList = new ArrayList<String>();

	/** Lista para o combobox com as ReceitaTipo. */
	private Map<String, Long> receitaTipoMap = new HashMap<String, Long>();

	/** Lista para o combobox com as Cliente. */
	private List<String> clienteList = new ArrayList<String>();

	/** Lista para o combobox com as Cliente. */
	private Map<String, Long> clienteMap = new HashMap<String, Long>();

	/** Lista para o combobox com as Pratica. */
	private List<String> praticaList = new ArrayList<String>();

	/** Lista para o combobox com as Pratica. */
	private Map<String, Long> praticaMap = new HashMap<String, Long>();

	/** Lista para o combobox com as NaturezaCentroLucro. */
	private List<String> naturezaList = new ArrayList<String>();

	/** Lista para o combobox com as NaturezaCentroLucro. */
	private Map<String, Long> naturezaMap = new HashMap<String, Long>();

	/** Lista para o combobox com as CentroLucro. */
	private List<String> centroLucroList = new ArrayList<String>();

	/** Lista para o combobox com as CentroLucro. */
	private Map<String, Long> centroLucroMap = new HashMap<String, Long>();

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);
	
	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private String currentRowType = "";

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageId = Integer.valueOf(0);

	private Integer lastPageId = Integer.valueOf(0);

	/** Lista dos possiveis valores de meses. */
	private List<String> validityMonthList = Arrays.asList("01", "02", "03",
			"04", "05", "06", "07", "08", "09", "10", "11", "12");

	/** Lista dos possiveis valores de anos. */
	private List<String> validityYearList = new ArrayList<String>();

	/** Indicador do modo em tempo de execucao - create/update. */
	private Boolean isUpdate = Boolean.valueOf(false);

	/** Indicador do modo em tempo de execucao - remove/view. */
	private Boolean isRemove = Boolean.valueOf(false);

	/** Armazena a versão de um Receita. */
	private String receitaVersion = "";

	/** Mes vigencia. */
	private String validityMonth = null;

	/** Ano vigencia. */
	private String validityYear = null;

	/** Flag para desabilitar o campo mes/ano. */
	private boolean disabledDataMes = false;

	/** Valor do campo FTE, utilizado para setar todos os itens selecionados. */
	private BigDecimal fte = null;

	/** Valor do campo Horas, utilizado para setar todos os itens selecionados. */
	private BigDecimal hours = null;

	/** Valor total da receita selecionado (visível). */
	private BigDecimal totalAmountView = BigDecimal.valueOf(0);

	/** Valor da redistribuição da receita . */
	private BigDecimal redistributionValue = null;

	/** Lista de VwReceitaFilter, utilizado na listagem da tela de search. */
	private List<ReceitaFilter> receitaFilterList = new ArrayList<ReceitaFilter>();

	/** Lista de ReceitaFilter, com os totais das Receitas da tela de Report. */
	private List<ReceitaFilter> receitaFilterTotalsList = new ArrayList<ReceitaFilter>();

	/** Lista de Ajustes de Receita. */
	private List<AjusteReceita> ajusteReceitaList = new ArrayList<AjusteReceita>();

	/** Total de Ajuste. */
	private BigDecimal totalAjuste = BigDecimal.valueOf(0);

	/** Total de Balanco. */
	private BigDecimal totalBalanco = BigDecimal.valueOf(0);

	/** Indica se uma receita foi ajustada. */
	private Boolean isAdjusted = Boolean.valueOf(false);

	/** VwReceitaFilter utilizado no filtro. */
	private VwReceitaFilter vwReceitaFilter = new VwReceitaFilter();

	/** Indica se a ordenção é crescente ou decrecente. */
	private boolean ascending = true;

	/**
	 * Flag indicando se o modal de confirmação deve ser exibido.
	 */
	private boolean showModalConfirmation = Boolean.valueOf(false);

	/**
	 * Flag para verificar se a data utilizada na busca é posterior a data de
	 * fechamento .
	 */
	private Boolean isBeforeClosingDate = Boolean.TRUE;

	/** Armazena o nome da Etiqueta para ser adicionada ao combo. */
	private String nomeEtiquetaAdd = "";

	/** Armazena o nome da Etiqueta selecionada no combo. */
	private String nomeEtiquetaSelected = "";

	/** Lista para o combobox com as Etiqueta. */
	private List<String> etiquetaList = new ArrayList<String>();

	/** Mapa para o combobox com as Etiqueta. */
	private Map<String, Long> etiquetaMap = new HashMap<String, Long>();

	/** Total receita total de cada deal. */
	private Double totalReceitaDeal = Double.valueOf(0);

	/** Atributo que incica se a revenus possui um unico deal relacionado. */
	private Boolean hasOnlyIOneFiscalDeal = Boolean.valueOf(false);

	/** Atributo que armazena o valor total convertido da Receita. */
	private BigDecimal totalReceitaConvert = BigDecimal.valueOf(0);

	/** Atributo que armazena o nome da Moeda do total convertido da Receita. */
	private String nomeMoedaTotReceitaConvert = "";

	/** Lista de HistoricoReceita da Receita corrente. */
	private List<HistoricoReceita> historicoReceitaList = new ArrayList<HistoricoReceita>(
			0);

	/** Moeda selecionada no simpleTogglePanel. */
	private List<ReceitaMoedaRow> receitaMoedaRowList = new ArrayList<ReceitaMoedaRow>();

	/** Indice da lista de ReceitaMoeda. */
	private Integer receitaMoedaRowId;

	/**
	 * {@link ReceitaMoedaRow} corrente. Usado na inclusao/edicao de um item da
	 * receita moeda.
	 */
	private ReceitaMoedaRow currentReceitaMoedaRow;

	/** Lista de {@link MotivoResultado} a ser selecionada no combo. */
	private List<String> motivoResultadoList = new ArrayList<String>();

	/** Lista para o combobox com os {@link MotivoResultado}. */
	private Map<String, Long> motivoResultadoMap = new HashMap<String, Long>();

	/** Percentual tolerancia entra os valores receita planejada/realizada. */
	private BigDecimal percentualTolerancia = BigDecimal.valueOf(0);

	/** Lista de {@link ShortTermRevenueRow} exibido na tela de justificativa. */
	private List<ShortTermRevenueRow> shortTermRevenueRowList = new ArrayList<ShortTermRevenueRow>();

	/** Flag para indicar se a justificativa deve ser exibida no botao save. */
	private boolean isJustificativaBySave = Boolean.valueOf(false);

	/** Flag para indicar se a justificativa deve ser exibida no botao publich. */
	private boolean isJustificativaByPublish = Boolean.valueOf(false);

	/** Moeda padrão do sistema. */
	private String patternCurrency = "";

	/** Nome da moeda referente a ReceitaMoeda selecionada na tela. */
	private String nameTabMoedaRow = "";
	
	/** Indicador se a receita que está sendo editada já foi integrada. */
	private Boolean integratedRevenue = Boolean.FALSE;
	
	private String backTo;

	private String nomeReceitaTipo = "";

	/** Campo usado no formulario de criação de receita de licença */
	private String installments = "";

	/** Campo usado no formulario de criação de receita de licença */
	private BigDecimal valorReceitaLicenca = BigDecimal.ZERO;

	private List<String> dealFiscalNameList = new ArrayList<String>();
	private Map<String, Long> dealFiscalNameAndCodeMap = new HashMap<String, Long>();
	private String nomeFiscalDeal = "";
	private List<ReceitaLicencaRow> receitaLicencaRows = new ArrayList<ReceitaLicencaRow>();
	private ReceitaLicenca receitaLicenca = new ReceitaLicenca();
	private List<ReceitaLicenca> receitaLicencas = new ArrayList<ReceitaLicenca>();
	private Boolean createReceitaLicencaButtonDisabled = false;
	private BigDecimal receitaLicencaInstallmentsTotal = BigDecimal.ZERO;
	private Double receitadutyhours = 0.0;

	private List<AjusteReceitaForecast> ajusteReceitaForecastList = new ArrayList<>();

	public Integer getLastPageId() {
		return lastPageId;
	}

	public void setLastPageId(Integer lastPageId) {
		this.lastPageId = lastPageId;
	}

	private Boolean isRevenueOpenToAdjustment = Boolean.TRUE;

	public Boolean getIsRevenueOpenToAdjustment() {
		return isRevenueOpenToAdjustment;
	}

	public void setIsRevenueOpenToAdjustment(
			final Boolean isRevenueOpenToAdjustment) {
		this.isRevenueOpenToAdjustment = isRevenueOpenToAdjustment;
	}

	/**
	 * @return the nomeMoedaTotReceitaConvert
	 */
	public String getNomeMoedaTotReceitaConvert() {
		return nomeMoedaTotReceitaConvert;
	}

	/**
	 * @param nomeMoedaTotReceitaConvert
	 *            the nomeMoedaTotReceitaConvert to set
	 */
	public void setNomeMoedaTotReceitaConvert(
			final String nomeMoedaTotReceitaConvert) {
		this.nomeMoedaTotReceitaConvert = nomeMoedaTotReceitaConvert;
	}

	/**
	 * @return the totalReceitaConvert
	 */
	public BigDecimal getTotalReceitaConvert() {
		return totalReceitaConvert;
	}

	/**
	 * @param totalReceitaConvert
	 *            the totalReceitaConvert to set
	 */
	public void setTotalReceitaConvert(final BigDecimal totalReceitaConvert) {
		this.totalReceitaConvert = totalReceitaConvert;
	}

	/**
	 * @return the receitaFilterTotalsList
	 */
	public List<ReceitaFilter> getReceitaFilterTotalsList() {
		return receitaFilterTotalsList;
	}

	/**
	 * @param receitaFilterTotalsList
	 *            the receitaFilterTotalsList to set
	 */
	public void setReceitaFilterTotalsList(
			final List<ReceitaFilter> receitaFilterTotalsList) {
		this.receitaFilterTotalsList = receitaFilterTotalsList;
	}

	/**
	 * @return the etiquetaList
	 */
	public List<String> getEtiquetaList() {
		return etiquetaList;
	}

	/**
	 * @param etiquetaList
	 *            the etiquetaList to set
	 */
	public void setEtiquetaList(final List<String> etiquetaList) {
		this.etiquetaList = etiquetaList;
	}

	/**
	 * @return the etiquetaMap
	 */
	public Map<String, Long> getEtiquetaMap() {
		return etiquetaMap;
	}

	/**
	 * @param etiquetaMap
	 *            the etiquetaMap to set
	 */
	public void setEtiquetaMap(final Map<String, Long> etiquetaMap) {
		this.etiquetaMap = etiquetaMap;
	}

	/**
	 * @return the nomeEtiquetaAdd
	 */
	public String getNomeEtiquetaAdd() {
		return nomeEtiquetaAdd;
	}

	/**
	 * @param nomeEtiquetaAdd
	 *            the nomeEtiquetaAdd to set
	 */
	public void setNomeEtiquetaAdd(final String nomeEtiquetaAdd) {
		this.nomeEtiquetaAdd = nomeEtiquetaAdd;
	}

	/**
	 * @return the nomeEtiquetaSelected
	 */
	public String getNomeEtiquetaSelected() {
		return nomeEtiquetaSelected;
	}

	/**
	 * @param nomeEtiquetaSelected
	 *            the nomeEtiquetaSelected to set
	 */
	public void setNomeEtiquetaSelected(final String nomeEtiquetaSelected) {
		this.nomeEtiquetaSelected = nomeEtiquetaSelected;
	}

	/**
	 * @return the toItemReceitaRow
	 */
	public ItemReceitaRow getToItemReceitaRow() {
		return toItemReceitaRow;
	}

	/**
	 * @param toItemReceitaRow
	 *            the toItemReceitaRow to set
	 */
	public void setToItemReceitaRow(final ItemReceitaRow toItemReceitaRow) {
		this.toItemReceitaRow = toItemReceitaRow;
	}

	/**
	 * @return the disabledDataMes
	 */
	public boolean isDisabledDataMes() {
		return disabledDataMes;
	}

	/**
	 * @param disabledDataMes
	 *            the disabledDataMes to set
	 */
	public void setDisabledDataMes(final boolean disabledDataMes) {
		this.disabledDataMes = disabledDataMes;
	}

	/**
	 * @return the toItemReceita
	 */
	public ItemReceita getToItemReceita() {
		return toItemReceita;
	}

	/**
	 * @param toItemReceita
	 *            the toItemReceita to set
	 */
	public void setToItemReceita(final ItemReceita toItemReceita) {
		this.toItemReceita = toItemReceita;
	}

	/**
	 * @return the validityMonth
	 */
	public String getValidityMonth() {
		return validityMonth;
	}

	/**
	 * @param validityMonth
	 *            the validityMonth to set
	 */
	public void setValidityMonth(final String validityMonth) {
		this.validityMonth = validityMonth;
	}

	/**
	 * @return the validityYear
	 */
	public String getValidityYear() {
		return validityYear;
	}

	/**
	 * @param validityYear
	 *            the validityYear to set
	 */
	public void setValidityYear(final String validityYear) {
		this.validityYear = validityYear;
	}

	/**
	 * Indicador habilitado / desabilitado dos botões (editar/deletar) da lista
	 * de ItemReceita.
	 */
	private Boolean isItemReceitaListButtonsEnabled = Boolean.valueOf(false);

	/**
	 * @return the resultList
	 */
	public List<Receita> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(final List<Receita> resultList) {
		this.resultList = resultList;
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
	 * @return the currentPageId
	 */
	public Integer getCurrentPageId() {
		return currentPageId;
	}

	/**
	 * @param currentPageId
	 *            the currentPageId to set
	 */
	public void setCurrentPageId(final Integer currentPageId) {
		this.currentPageId = currentPageId;
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
	 * @return the receitaVersion
	 */
	public String getReceitaVersion() {
		return receitaVersion;
	}

	/**
	 * @param receitaVersion
	 *            the receitaVersion to set
	 */
	public void setReceitaVersion(final String receitaVersion) {
		this.receitaVersion = receitaVersion;
	}

	/**
	 * @return the isItemReceitaListButtonsEnabled
	 */
	public Boolean getIsItemReceitaListButtonsEnabled() {
		return isItemReceitaListButtonsEnabled;
	}

	/**
	 * @param isItemReceitaListButtonsEnabled
	 *            the isItemReceitaListButtonsEnabled to set
	 */
	public void setIsItemReceitaListButtonsEnabled(
			final Boolean isItemReceitaListButtonsEnabled) {
		this.isItemReceitaListButtonsEnabled = isItemReceitaListButtonsEnabled;
	}

	/**
	 * @return the validityMonthList
	 */
	public List<String> getValidityMonthList() {
		return validityMonthList;
	}

	/**
	 * @return lista de anos da vigencia
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
	 * @param to
	 *            the to to set
	 */
	public void setTo(final Receita to) {
		this.to = to;
	}

	/**
	 * 
	 * @return retorna o TO
	 */
	public Receita getTo() {
		// Verifica se o ContratoPratica é nulo,
		// se verdade instancia um novo ContratoPratica
		// Isto é necessario na criacao de uma entidade, pois o a referencia da
		// ContratoPratica não pode ser nulo
		if (to != null && to.getContratoPratica() == null) {
			to.setContratoPratica(new ContratoPratica());
		}
		return to;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final Receita filter) {
		this.filter = filter;
	}

	/**
	 * 
	 * @return o filtro do TO
	 */
	public Receita getFilter() {
		if (filter != null && filter.getContratoPratica() == null) {
			filter.setContratoPratica(new ContratoPratica());
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
		resetDataMes();
		this.currentReceitaMoedaRow = null;
		this.isItemReceitaListButtonsEnabled = Boolean.valueOf(false);
		this.disabledDataMes = Boolean.valueOf(false);
		this.hasOnlyIOneFiscalDeal = Boolean.valueOf(false);
		this.totalReceitaDeal = Double.valueOf(0);
		this.isJustificativaBySave = Boolean.valueOf(false);
		this.isJustificativaByPublish = Boolean.valueOf(false);
		this.resetJustificativaFields();
		this.integratedRevenue = Boolean.FALSE;
		this.receitaTipoList = new ArrayList<String>();
		this.createReceitaLicencaButtonDisabled = false;
		this.currentRowType = "";
		this.receitaLicencaInstallmentsTotal = BigDecimal.ZERO;
		this.dealFiscalNameAndCodeMap = new HashMap<String, Long>();
		this.dealFiscalNameList = new ArrayList<String>();
		this.ajusteReceitaForecastList = new ArrayList<>();
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new Receita();
	}

	/**
	 * Reset dos campos da tela de confirmação justificativa.
	 */
	public void resetJustificativaFields() {
		this.motivoResultadoMap = new HashMap<String, Long>();
		this.motivoResultadoList = new ArrayList<String>();
		this.showModalConfirmation = Boolean.valueOf(false);
	}

	/**
	 * Reseta o filter.
	 */
	public void resetFilter() {
		this.vwReceitaFilter = new VwReceitaFilter();
		this.receitaFilterList = new ArrayList<ReceitaFilter>();
		this.filter = new Receita();
		this.receitaFilterTotalsList = new ArrayList<ReceitaFilter>();
		this.totalReceitaConvert = BigDecimal.valueOf(0);
		this.nomeMoedaTotReceitaConvert = "";
	}

	/**
	 * Reseta a lista de to.
	 */
	public void resetResultList() {
		this.resultList = new ArrayList<Receita>();
	}

	/**
	 * Reseta a data de vigencia.
	 */
	public void resetDataMes() {
		this.validityMonth = "";
		this.validityYear = "";
	}

	/**
	 * Reseta os valures dos campos da Etiqueta (tag).
	 */
	public void resetEtiquetaFileds() {
		this.nomeEtiquetaAdd = "";
		this.nomeEtiquetaSelected = "";
	}

	/**
	 * @param redistributionValue
	 *            the redistributionValue to set
	 */
	public void setRedistributionValue(final BigDecimal redistributionValue) {
		this.redistributionValue = redistributionValue;
	}

	/**
	 * @return the redistributionValue
	 */
	public BigDecimal getRedistributionValue() {
		return redistributionValue;
	}

	/**
	 * @param vwReceitaFilterList
	 *            the vwReceitaFilterList to set
	 */
	public void setVwReceitaFilterList(
			final List<ReceitaFilter> vwReceitaFilterList) {
		this.receitaFilterList = vwReceitaFilterList;
	}

	/**
	 * @return the vwReceitaFilterList
	 */
	public List<ReceitaFilter> getVwReceitaFilterList() {
		return receitaFilterList;
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
	 * @return the praticaList
	 */
	public List<String> getPraticaList() {
		return praticaList;
	}

	/**
	 * @param praticaList
	 *            the praticaList to set
	 */
	public void setPraticaList(final List<String> praticaList) {
		this.praticaList = praticaList;
	}

	/**
	 * @return the praticaMap
	 */
	public Map<String, Long> getPraticaMap() {
		return praticaMap;
	}

	/**
	 * @param praticaMap
	 *            the praticaMap to set
	 */
	public void setPraticaMap(final Map<String, Long> praticaMap) {
		this.praticaMap = praticaMap;
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
	 * @return the controLucroList
	 */
	public List<String> getCentroLucroList() {
		return centroLucroList;
	}

	/**
	 * @param controLucroList
	 *            the controLucroList to set
	 */
	public void setCentroLucroList(final List<String> controLucroList) {
		this.centroLucroList = controLucroList;
	}

	/**
	 * @return the controLucroMap
	 */
	public Map<String, Long> getCentroLucroMap() {
		return centroLucroMap;
	}

	/**
	 * @param controLucroMap
	 *            the controLucroMap to set
	 */
	public void setCentroLucroMap(final Map<String, Long> controLucroMap) {
		this.centroLucroMap = controLucroMap;
	}

	/**
	 * @param vwReceitaFilter
	 *            the vwReceitaFilter to set
	 */
	public void setVwReceitaFilter(final VwReceitaFilter vwReceitaFilter) {
		this.vwReceitaFilter = vwReceitaFilter;
	}

	/**
	 * @return the vwReceitaFilter
	 */
	public VwReceitaFilter getVwReceitaFilter() {
		if (vwReceitaFilter != null && vwReceitaFilter.getId() == null) {
			vwReceitaFilter.setId(new VwReceitaFilterId());
		}
		return vwReceitaFilter;
	}

	/**
	 * @param fte
	 *            the fte to set
	 */
	public void setFte(final BigDecimal fte) {
		this.fte = fte;
	}

	/**
	 * @return the fte
	 */
	public BigDecimal getFte() {
		return fte;
	}

	/**
	 * @return the receitaTipoList
	 */
	public List<String> getReceitaTipoList() {
		return receitaTipoList;
	}

	/**
	 * @param receitaTipoList the receitaTipoList to set
	 */
	public void setReceitaTipoList(List<String> receitaTipoList) {
		this.receitaTipoList = receitaTipoList;
	}

	/**
	 * @return the receitaTipoMap
	 */
	public Map<String, Long> getReceitaTipoMap() {
		return receitaTipoMap;
	}

	/**
	 * @param receitaTipoMap the receitaTipoMap to set
	 */
	public void setReceitaTipoMap(Map<String, Long> receitaTipoMap) {
		this.receitaTipoMap = receitaTipoMap;
	}

	/**
	 * @param isBeforeClosingDate
	 *            the isBeforeClosingDate to set
	 */
	public void setIsBeforeClosingDate(final Boolean isBeforeClosingDate) {
		this.isBeforeClosingDate = isBeforeClosingDate;
	}

	/**
	 * @return the isBeforeClosingDate
	 */
	public Boolean getIsBeforeClosingDate() {
		return isBeforeClosingDate;
	}

	/**
	 * @param hours
	 *            the hours to set
	 */
	public void setHours(final BigDecimal hours) {
		this.hours = hours;
	}

	/**
	 * @return the hours
	 */
	public BigDecimal getHours() {
		return hours;
	}

	/**
	 * @param toReceitaDealFiscal
	 *            the toReceitaDealFiscal to set
	 */
	public void setToReceitaDealFiscal(
			final ReceitaDealFiscal toReceitaDealFiscal) {
		this.toReceitaDealFiscal = toReceitaDealFiscal;
	}

	/**
	 * @return the toReceitaDealFiscal
	 */
	public ReceitaDealFiscal getToReceitaDealFiscal() {
		return toReceitaDealFiscal;
	}

	/**
	 * @param receitaDealFiscalList
	 *            the receitaDealFiscalList to set
	 */
	public void setReceitaDealFiscalList(
			final List<ReceitaDealFiscal> receitaDealFiscalList) {
		this.receitaDealFiscalList = receitaDealFiscalList;
	}

	/**
	 * @return the receitaDealFiscalList
	 */
	public List<ReceitaDealFiscal> getReceitaDealFiscalList() {
		return receitaDealFiscalList;
	}

	/**
	 * @param totalReceitaDeal
	 *            the totalReceitaDeal to set
	 */
	public void setTotalReceitaDeal(final Double totalReceitaDeal) {
		this.totalReceitaDeal = totalReceitaDeal;
	}

	/**
	 * @return the totalReceitaDeal
	 */
	public Double getTotalReceitaDeal() {
		return totalReceitaDeal;
	}

	/**
	 * @param hasOnlyIOneFiscalDeal
	 *            the hasOnlyIOneFiscalDeal to set
	 */
	public void setHasOnlyIOneFiscalDeal(final Boolean hasOnlyIOneFiscalDeal) {
		this.hasOnlyIOneFiscalDeal = hasOnlyIOneFiscalDeal;
	}

	/**
	 * @return the hasOnlyIOneFiscalDeal
	 */
	public Boolean getHasOnlyIOneFiscalDeal() {
		return hasOnlyIOneFiscalDeal;
	}

	/**
	 * @param ascending
	 *            the ascending to set
	 */
	public void setAscending(final boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param totalAmountSelected
	 *            the totalAmountSelected to set
	 */
	public void setTotalAmountView(final BigDecimal totalAmountSelected) {
		this.totalAmountView = totalAmountSelected;
	}

	/**
	 * @return the totalAmountSelected
	 */
	public BigDecimal getTotalAmountView() {
		return totalAmountView;
	}

	/**
	 * @return the historicoReceitaList
	 */
	public List<HistoricoReceita> getHistoricoReceitaList() {
		return historicoReceitaList;
	}

	/**
	 * @param historicoReceitaList
	 *            the historicoReceitaList to set
	 */
	public void setHistoricoReceitaList(
			final List<HistoricoReceita> historicoReceitaList) {
		this.historicoReceitaList = historicoReceitaList;
	}

	/**
	 * Lista de ajuste receita.
	 * 
	 * @return retorna um list
	 */
	public List<AjusteReceita> getAjusteReceitaList() {
		return ajusteReceitaList;
	}

	/**
	 * @return the receitaFilterList
	 */
	public List<ReceitaFilter> getReceitaFilterList() {
		return receitaFilterList;
	}

	/**
	 * @param receitaFilterList
	 *            the receitaFilterList to set
	 */
	public void setReceitaFilterList(final List<ReceitaFilter> receitaFilterList) {
		this.receitaFilterList = receitaFilterList;
	}

	/**
	 * @return the totalAjuste
	 */
	public BigDecimal getTotalAjuste() {
		return totalAjuste;
	}

	/**
	 * @param totalAjuste
	 *            the totalAjuste to set
	 */
	public void setTotalAjuste(final BigDecimal totalAjuste) {
		this.totalAjuste = totalAjuste;
	}

	/**
	 * @return the totalBalanco
	 */
	public BigDecimal getTotalBalanco() {
		return totalBalanco;
	}

	/**
	 * @param totalBalanco
	 *            the totalBalanco to set
	 */
	public void setTotalBalanco(final BigDecimal totalBalanco) {
		this.totalBalanco = totalBalanco;
	}

	/**
	 * @return the isAdjusted
	 */
	public Boolean getIsAdjusted() {
		return isAdjusted;
	}

	/**
	 * @param isAdjusted
	 *            the isAdjusted to set
	 */
	public void setIsAdjusted(final Boolean isAdjusted) {
		this.isAdjusted = isAdjusted;
	}

	/**
	 * @param ajusteReceitaList
	 *            the ajusteReceitaList to set
	 */
	public void setAjusteReceitaList(final List<AjusteReceita> ajusteReceitaList) {
		this.ajusteReceitaList = ajusteReceitaList;
	}

	/**
	 * @return the showModalConfirmation
	 */
	public boolean getShowModalConfirmation() {
		return showModalConfirmation;
	}

	/**
	 * @param showModalConfirmation
	 *            the isLogarMotivoResultado to set
	 */
	public void setShowModalConfirmation(final boolean showModalConfirmation) {
		this.showModalConfirmation = showModalConfirmation;
	}

	/**
	 * @return the motivoResultadoList
	 */
	public List<String> getMotivoResultadoList() {
		return motivoResultadoList;
	}

	/**
	 * @param motivoResultadoList
	 *            the motivoResultadoList to set
	 */
	public void setMotivoResultadoList(final List<String> motivoResultadoList) {
		this.motivoResultadoList = motivoResultadoList;
	}

	/**
	 * @return the mapMotivoResultado
	 */
	public Map<String, Long> getMotivoResultadoMap() {
		return motivoResultadoMap;
	}

	/**
	 * @param motivoResultadoMap
	 *            the mapMotivoResultado to set
	 */
	public void setMotivoResultadoMap(final Map<String, Long> motivoResultadoMap) {
		this.motivoResultadoMap = motivoResultadoMap;
	}

	/**
	 * @return the percentualTolerancia
	 */
	public BigDecimal getPercentualTolerancia() {
		return percentualTolerancia;
	}

	/**
	 * @param percentualTolerancia
	 *            the percentualTolerancia to set
	 */
	public void setPercentualTolerancia(final BigDecimal percentualTolerancia) {
		this.percentualTolerancia = percentualTolerancia;
	}

	/**
	 * @return the listaRecDealFiscalRow
	 */
	public List<ReceitaDealFiscalRow> getListaRecDealFiscalRow() {
		return listaRecDealFiscalRow;
	}

	/**
	 * @param listaRecDealFiscalRow
	 *            the listaReceitaRow to set
	 */
	public void setListaRecDealFiscalRow(
			final List<ReceitaDealFiscalRow> listaRecDealFiscalRow) {
		this.listaRecDealFiscalRow = listaRecDealFiscalRow;
	}

	/**
	 * @return the listaDealFiscal
	 */
	public List<DealFiscal> getListaDealFiscal() {
		return listaDealFiscal;
	}

	/**
	 * @param listaDealFiscal
	 *            the listaDealFiscal to set
	 */
	public void setListaDealFiscal(final List<DealFiscal> listaDealFiscal) {
		this.listaDealFiscal = listaDealFiscal;
	}

	/**
	 * @return the receitaMoedaRowList
	 */
	public List<ReceitaMoedaRow> getReceitaMoedaRowList() {
		return receitaMoedaRowList;
	}

	/**
	 * @param receitaMoedaRowList
	 *            the receitaMoedaRowList
	 */
	public void setReceitaMoedaRowList(
			final List<ReceitaMoedaRow> receitaMoedaRowList) {
		this.receitaMoedaRowList = receitaMoedaRowList;
	}

	/**
	 * @return the currentReceitaMoedaRow
	 */
	public ReceitaMoedaRow getCurrentReceitaMoedaRow() {
		return currentReceitaMoedaRow;
	}

	/**
	 * @param currentReceitaMoedaRow
	 *            the currentReceitaMoedaRow
	 */
	public void setCurrentReceitaMoedaRow(
			final ReceitaMoedaRow currentReceitaMoedaRow) {
		this.currentReceitaMoedaRow = currentReceitaMoedaRow;
	}

	/**
	 * @return the receitaMoedaRowId
	 */
	public Integer getReceitaMoedaRowId() {
		return receitaMoedaRowId;
	}

	/**
	 * @param receitaMoedaRowId
	 *            the receitaMoedaRowId to set
	 */
	public void setReceitaMoedaRowId(final Integer receitaMoedaRowId) {
		this.receitaMoedaRowId = receitaMoedaRowId;
	}

	/**
	 * @return the shortTermRevenueRowList
	 */
	public List<ShortTermRevenueRow> getShortTermRevenueRowList() {
		return shortTermRevenueRowList;
	}

	/**
	 * @param shortTermRevenueRowList
	 *            the shortTermRevenueRowList to set
	 */
	public void setShortTermRevenueRowList(
			final List<ShortTermRevenueRow> shortTermRevenueRowList) {
		this.shortTermRevenueRowList = shortTermRevenueRowList;
	}

	/**
	 * @return the isJustificativaBySave
	 */
	public boolean getIsJustificativaBySave() {
		return isJustificativaBySave;
	}

	/**
	 * @param isJustificativaBySave
	 *            the isJustificativaBySave to set
	 */
	public void setIsJustificativaBySave(final boolean isJustificativaBySave) {
		this.isJustificativaBySave = isJustificativaBySave;
	}

	/**
	 * @return the isJustificativaByPublish
	 */
	public boolean getIsJustificativaByPublish() {
		return isJustificativaByPublish;
	}

	/**
	 * @param isJustificativaByPublish
	 *            the isJustificatiaByPublish to set
	 */
	public void setIsJustificativaByPublish(
			final boolean isJustificativaByPublish) {
		this.isJustificativaByPublish = isJustificativaByPublish;
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
	public void setPatternCurrency(final String patternCurrency) {
		this.patternCurrency = patternCurrency;
	}

	/**
	 * @return the nameTabMoedaRow
	 */
	public String getNameTabMoedaRow() {
		return nameTabMoedaRow;
	}

	/**
	 * @param nameTabMoedaRow
	 *            the nameTabMoedaRow to set
	 */
	public void setNameTabMoedaRow(String nameTabMoedaRow) {
		this.nameTabMoedaRow = nameTabMoedaRow;
	}

	/**
	 * @return the integratedRevenue
	 */
	public Boolean getIntegratedRevenue() {
		return integratedRevenue;
	}

	/**
	 * @param integratedRevenue the integratedRevenue to set
	 */
	public void setIntegratedRevenue(final Boolean integratedRevenue) {
		this.integratedRevenue = integratedRevenue;
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
	 * @return the nomeReceitaTipo
	 */
	public String getNomeReceitaTipo() {
		return nomeReceitaTipo;
	}

	/**
	 * @param nomeReceitaTipo the nomeReceitaTipo to set
	 */
	public void setNomeReceitaTipo(String nomeReceitaTipo) {
		this.nomeReceitaTipo = nomeReceitaTipo;
	}

	/**
	 * @return the installments
	 */
	public String getInstallments() {
		return installments;
	}

	/**
	 * @param installments the installments to set
	 */
	public void setInstallments(String installments) {
		this.installments = installments;
	}

	/**
	 * @return the totalValue
	 */
	public BigDecimal getValorReceitaLicenca() {
		return valorReceitaLicenca;
	}

	/**
	 * @param valorReceitaLicenca the totalValue to set
	 */
	public void setValorReceitaLicenca(BigDecimal valorReceitaLicenca) {
		this.valorReceitaLicenca = valorReceitaLicenca;
	}

	/**
	 * @return the dealFiscalNameList
	 */
	public List<String> getDealFiscalNameList() {
		return dealFiscalNameList;
	}

	/**
	 * @param dealFiscalNameList the dealFiscalNameList to set
	 */
	public void setDealFiscalNameList(List<String> dealFiscalNameList) {
		this.dealFiscalNameList = dealFiscalNameList;
	}

	/**
	 * @return the dealFiscalNameAndCodeMap
	 */
	public Map<String, Long> getDealFiscalNameAndCodeMap() {
		return dealFiscalNameAndCodeMap;
	}

	/**
	 * @param dealFiscalNameAndCodeMap the dealFiscalNameAndCodeMap to set
	 */
	public void setDealFiscalNameAndCodeMap(
			Map<String, Long> dealFiscalNameAndCodeMap) {
		this.dealFiscalNameAndCodeMap = dealFiscalNameAndCodeMap;
	}

	/**
	 * @return the nomeFiscalDeal
	 */
	public String getNomeFiscalDeal() {
		return nomeFiscalDeal;
	}

	/**
	 * @param nomeFiscalDeal the nomeFiscalDeal to set
	 */
	public void setNomeFiscalDeal(String nomeFiscalDeal) {
		this.nomeFiscalDeal = nomeFiscalDeal;
	}

	/**
	 * @return the receitaLicencaRows
	 */
	public List<ReceitaLicencaRow> getReceitaLicencaRows() {
		return receitaLicencaRows;
	}

	/**
	 * @param receitaLicencaRows the receitaLicencaRows to set
	 */
	public void setReceitaLicencaRows(List<ReceitaLicencaRow> receitaLicencaRows) {
		this.receitaLicencaRows = receitaLicencaRows;
	}

	/**
	 * @return the ajusteReceitaForecastList
	 */
	public List<AjusteReceitaForecast> getAjusteReceitaForecastList() {
		return ajusteReceitaForecastList;
	}

	/**
	 * @param ajusteReceitaForecastList the ajusteReceitaForecastList to set
	 */
	public void setAjusteReceitaForecastList(List<AjusteReceitaForecast> ajusteReceitaForecastList) {
		this.ajusteReceitaForecastList = ajusteReceitaForecastList;
	}

	/**
	 * @return the currentRowType
	 */
	public String getCurrentRowType() {
		return currentRowType;
	}

	/**
	 * @param currentRowType the currentRowType to set
	 */
	public void setCurrentRowType(String currentRowType) {
		this.currentRowType = currentRowType;
	}

	/**
	 * @return the receitaLicenca
	 */
	public ReceitaLicenca getReceitaLicenca() {
		return receitaLicenca;
	}

	/**
	 * @param receitaLicenca the receitaLicenca to set
	 */
	public void setReceitaLicenca(ReceitaLicenca receitaLicenca) {
		this.receitaLicenca = receitaLicenca;
	}

	/**
	 * @return the receitaLicencas
	 */
	public List<ReceitaLicenca> getReceitaLicencas() {
		return receitaLicencas;
	}

	/**
	 * @param receitaLicencas the receitaLicencas to set
	 */
	public void setReceitaLicencas(List<ReceitaLicenca> receitaLicencas) {
		this.receitaLicencas = receitaLicencas;
	}

	/**
	 * @return the createReceitaLicencaButtonEnabled
	 */
	public Boolean getCreateReceitaLicencaButtonDisabled() {
		return createReceitaLicencaButtonDisabled;
	}

	/**
	 * @param createReceitaLicencaButtonDisabled the createReceitaLicencaButtonEnabled to set
	 */
	public void setCreateReceitaLicencaButtonDisabled(
			Boolean createReceitaLicencaButtonDisabled) {
		this.createReceitaLicencaButtonDisabled = createReceitaLicencaButtonDisabled;
	}

	/**
	 * @return the receitaLicencaInstallmentsTotal
	 */
	public BigDecimal getReceitaLicencaInstallmentsTotal() {
		return receitaLicencaInstallmentsTotal;
	}

	/**
	 * @param receitaLicencaInstallmentsTotal the receitaLicencaInstallmentsTotal to set
	 */
	public void setReceitaLicencaInstallmentsTotal(BigDecimal receitaLicencaInstallmentsTotal) {
		this.receitaLicencaInstallmentsTotal = receitaLicencaInstallmentsTotal;
	}

	/**
	 * @return the receitadutyhours
	 */
	public Double getReceitadutyhours() {
		return receitadutyhours;
	}

	/**
	 * @param receitadutyhours the receitadutyhours to set
	 */
	public void setReceitadutyhours(Double receitadutyhours) {
		this.receitadutyhours = receitadutyhours;
	}
}