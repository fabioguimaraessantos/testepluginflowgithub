package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.NaturezaContratoPraticaCLRow;
import com.ciandt.pms.model.vo.combo.ComboBox;
import org.richfaces.model.UploadItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Define o BackingBean da entidade.
 *
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ContratoPraticaBean implements Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** to do backingBean. */
	private ContratoPratica to = new ContratoPratica();

	/** filter do backingBean. */
	private ContratoPratica filter = new ContratoPratica();

	/** lista de resultados da pesquisa. */
	private List<ContratoPratica> resultList = null;

	/** Lista para o combobox com as praticas. */
	private List<String> praticaList = new ArrayList<>();

	/** Lista para o combobox com as praticas. */
	private Map<String, Long> praticaMap = new HashMap<>();

	/** Lista para o combobox com os tipos de modelo de negocio. */
	private Map<String, Long> tipoModeloNegocioMap = new HashMap<>();

	/** Lista para o combobox com os tipos de modelo de negocio. */
	private List<String> tipoModeloNegocioList = new ArrayList<>();
	private String tipoModeloNegocioSelected = null;

	/** Lista para o combobox com as Moeda. */
	private List<String> moedaList = new ArrayList<String>();

	/** Lista para o combobox com as Moeda. */
	private Map<String, Long> moedaMap = new HashMap<String, Long>();

	/** Auxiliar para filtro do combobox de Moeda. */
	private String nomeMoedaFilter = "";

	/** ComboBox do MSA. */
//	private ComboBox<Long> msaCombo = new ComboBox<Long>();
	private List<String> msaCombo = new ArrayList<String>();
	private Map<String, Long> msaComboMap = new HashMap<String, Long>();
	private String msaSelected = null;

	/**
	 * ComboBox do grupo de custo
	 */
	private ComboBox<GrupoCusto> grupoCustoCombo = new ComboBox<GrupoCusto>();

	private ComboBox<CentroLucro> centrolucroCombo = new ComboBox<CentroLucro>();

	private ComboBox<CentroLucro> businessManagerCombo = new ComboBox<CentroLucro>();

	private ComboBox<CentroLucro> ssoCombo = new ComboBox<CentroLucro>();

	private ComboBox<CentroLucro> ukmtCombo = new ComboBox<CentroLucro>();

	private String anoInicioVigenciaSSO;

	private String mesInicioVigenciaSSO;

	private String anoInicioVigenciaUKMT;

	private String mesInicioVigenciaUKMT;

	private String anoInicioVigenciaBM;

	private String mesInicioVigenciaBM;

	/** ComboBox do Manager. */
	private ComboBox<Pessoa> aprovadorCombo = new ComboBox<Pessoa>();

	/** ComboBox do Coord. */
	private ComboBox<Pessoa> gerenteCombo = new ComboBox<Pessoa>();

	/** ComboBox Status do CLOB na tela de Add */
	private ComboBox<String> ativoInativoCombo = new ComboBox<>();

	private ComboBox<String> requisicaoInativacaoGerenteCombo = new ComboBox<>();

	private ComboBox<String> requisicaoInativacaoControladoriaCombo = new ComboBox<>();

	private Boolean disableStatusCombo = Boolean.valueOf(false);

	/** Lista para o combobox com clientes. */
	private List<String> clienteList = new ArrayList<String>();

	/** Lista para o combobox com as clientes. */
	private Map<String, Long> clienteMap = new HashMap<String, Long>();

	/** Lista para o combobox com as NaturezaCentroLucro. */
	private List<String> naturezaList = new ArrayList<String>();

	/** Lista para o combobox com as NaturezaCentroLucro. */
	private Map<String, Long> naturezaMap = new HashMap<String, Long>();

	/** Lista para o combobox com as Pessoas. */
	private List<String> pessoaList = new ArrayList<String>();

	/** Lista para o combobox com as Pessoas. */
	private Map<String, Long> pessoaMap = new HashMap<String, Long>();

	/** Lista para o combobox com as CentroLucro. */
	private List<String> centroLucroList = new ArrayList<String>();

	/** Lista para o combobox com as CentroLucro. */
	private Map<String, Long> centroLucroMap = new HashMap<String, Long>();

	/** Lista para o combobox com as aliquotas. */
	private List<Aliquota> aliquotaList = new ArrayList<Aliquota>();

	/** Lista para o combobox com os impostos. */
	private Map<String, Long> impostoMap = new HashMap<String, Long>();

	/** Lista de DealFiscal. */
	private List<DealFiscal> dealFiscalList = new ArrayList<DealFiscal>();

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageId = Integer.valueOf(1);

	/** Indicador do modo em tempo de execucao - remove/view. */
	private Boolean isRemove = Boolean.FALSE;

	/** Indicador do modo em tempo de para criacao ou edicao de ContratoPratica. */
	private Boolean isCreation = Boolean.FALSE;

	/** Atributo para verificar se � uma update na entidade. */
	private Boolean isUpdate = Boolean.FALSE;

	/** Atributo para verificar update no profit Center. */
	private Boolean isUpdateProfitCenter = Boolean.FALSE;

	/** Lista utilizada para o view do ContratoPraticaCL. */
	private List<NaturezaContratoPraticaCLRow> naturezaCPCLRowList = new ArrayList<NaturezaContratoPraticaCLRow>();

	/** Valor do imposto total. */
	private BigDecimal totalTax = BigDecimal.valueOf(0);

	/** Entidade cliente, utilizado no filtro. */
	private Cliente cliente = new Cliente();

	/** Entidade NaturezaCentroLucro, utilizado no filtro. */
	private NaturezaCentroLucro natureza = new NaturezaCentroLucro();

	/** Entidade CentroLucro, utilizado no filtro. */
	private CentroLucro centroLucro = new CentroLucro();

	/** Data do HistoryLock. */
	private Date historyLockDate;

	private Long filterCodigoMsa;

	/** Codigo do MSA utilizado no filtro */
	private String filterMsa;
	/**
	 * Grupo de custo utilizado no filtro
	 */
	private String filterGrupoCusto;

	/**
	 * Lista de Moedas disponivel para o ContratoPratica (picklist tela
	 * configure).
	 */
	private List<SelectItem> moedaPickList = new ArrayList<SelectItem>();

	/**
	 * Array de Moedas do ContratoPratica (picklist tela configure).
	 */
	private String[] selectedMoedaPickList = null;

	/**
	 * Lista de DealFiscal disponivel para o ContratoPratica (picklist tela
	 * configure).
	 */
	private List<SelectItem> dealFiscalPickList = new ArrayList<SelectItem>();

	/**
	 * Array de DealFiscal disponivel para o ContratoPratica (picklist tela
	 * configure).
	 */
	private String[] selectedDealFiscalPickList = null;

	/** Flag que indica se o usuario � de contratos ou nao. */
	private Boolean isContractsPerfil = Boolean.FALSE;

	/** Identifica se deve exibir ou nao o combobox do . */
	private Boolean isCostCenterComboboxEditable = Boolean.FALSE;

	private List<ContratoPraticaAud> historyList;

	private String indicadorReembolsavel = "";

	private Boolean indicadorMultiFiscalDeal = Boolean.FALSE;

	private Boolean indicadorAprovaAjusteReceita = Boolean.TRUE;

	private Boolean indicadorWorkAtRisk = Boolean.FALSE;

	private List<String> indicadorWorkAtRiskFilter = null;

	private Boolean showUpdateMapaAlocacaoModal = Boolean.FALSE;

	/** Lista de CentroLucro. */
	private List<CentroLucro> centroLucroObjlList = new ArrayList<CentroLucro>();

	/** representa o arquivo que foi feito o upload. */
	private UploadArquivo uploadArquivo;

	/** representa o item que foi realizado o upload. */
	private UploadItem uploadItem;

	/** Lista com os erroas da importa��o. */
	private List<String> errorList;


	/** Lista para os erros no arquivo de upload. */
	private Map<String, List<String>> mapUploadError = new HashMap<>();

	private List<Map.Entry<String, List<String>>> errorEntryList;


	/** Nome Arquivo Update em tela*/
	private String nomeArquivoUpload = "";


	/**
	 * @return the historyLockDate
	 */
	public Date getHistoryLockDate() {
		return historyLockDate;
	}

	/**
	 * @param historyLockDate
	 *            the historyLockDate to set
	 */
	public void setHistoryLockDate(final Date historyLockDate) {
		this.historyLockDate = historyLockDate;
	}

	/**
	 * @return the isUpdate
	 */
	public Boolean getIsUpdate() {
		return isUpdate;
	}

	/**
	 * @return the isUpdateProfitCenter
	 */
	public Boolean getIsUpdateProfitCenter() {
		return isUpdateProfitCenter;
	}

	/**
	 * @param isUpdate
	 *            the isUpdate to set
	 */
	public void setIsUpdate(final Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @param isUpdateProfitCenter
	 *            the isUpdateProfitCenter to set
	 */
	public void setIsUpdateProfitCenter(final Boolean isUpdateProfitCenter) {
		this.isUpdateProfitCenter = isUpdateProfitCenter;
	}
	/**
	 * Seta o TO.
	 *
	 * @param to
	 *            ContratoPratica
	 */
	public void setTo(final ContratoPratica to) {
		this.to = to;
	}

	/**
	 * Pega o TO.
	 *
	 * @return to ContratoPratica
	 */
	public ContratoPratica getTo() {
		if (to == null) {
			to = new ContratoPratica();
		}

		if (to.getMsa() == null) {
			to.setMsa(new Msa());
		}
		if (to.getPratica() == null) {
			to.setPratica(new Pratica());
		}

		return to;
	}

	/**
	 * Seta o filtro do TO.
	 *
	 * @param filter
	 *            do TO
	 */
	public void setFilter(final ContratoPratica filter) {
		this.filter = filter;
	}

	/**
	 * Pega o filter.
	 *
	 * @return to
	 */
	public ContratoPratica getFilter() {
		if (filter == null) {
			filter = new ContratoPratica();
		}

		if (filter.getMsa() == null) {
			filter.setMsa(new Msa());
		}
		if (filter.getPratica() == null) {
			filter.setPratica(new Pratica());
		}
		/*
		 * if (filter.getPessoa() == null) { filter.setPessoa(new Pessoa()); }
		 */

		return filter;
	}

	/**
	 * Seta a lista de resultados.
	 *
	 * @param resultList
	 *            obtida na busca
	 */
	public void setResultList(final List<ContratoPratica> resultList) {
		this.resultList = resultList;
	}

	/**
	 * Pega a lista de resultados.
	 *
	 * @return a lista de resultados
	 */
	public List<ContratoPratica> getResultList() {
		return resultList;
	}

	/**
	 * Seta a lista e pratica para o combo.
	 *
	 * @param praticaList
	 *            the praticaList to set
	 */
	public void setPraticaList(final List<String> praticaList) {
		this.praticaList = praticaList;
	}

	/**
	 * Pega a lista de pratica do combo.
	 *
	 * @return a lista
	 */
	public List<String> getPraticaList() {
		return praticaList;
	}

	/**
	 * Seta o map do combo.
	 *
	 * @param praticaMap
	 *            the praticaMap to set
	 */
	public void setPraticaMap(final Map<String, Long> praticaMap) {
		this.praticaMap = praticaMap;
	}

	/**
	 * Pega o map do combo.
	 *
	 * @return o map
	 */
	public Map<String, Long> getPraticaMap() {
		return praticaMap;
	}

	/**
	 * Seta a pagina conrrente.
	 *
	 * @param currentPageId
	 *            pagina corrente
	 */
	public void setCurrentPageId(final Integer currentPageId) {
		this.currentPageId = currentPageId;
	}

	/**
	 * Pega a pagina corrente.
	 *
	 * @return o id da pagina corrente.
	 */
	public Integer getCurrentPageId() {
		return currentPageId;
	}

	/**
	 * Seta a lista do combo.
	 *
	 * @param moedaList
	 *            the moedaList to set
	 */
	public void setMoedaList(final List<String> moedaList) {
		this.moedaList = moedaList;
	}

	/**
	 * Pega a lista do combo.
	 *
	 * @return a lista
	 */
	public List<String> getMoedaList() {
		return moedaList;
	}

	/**
	 * Seta o map do combo.
	 *
	 * @param moedaMap
	 *            the moedaMap to set
	 */
	public void setMoedaMap(final Map<String, Long> moedaMap) {
		this.moedaMap = moedaMap;
	}

	/**
	 * Pega o map do combo.
	 *
	 * @return a map
	 */
	public Map<String, Long> getMoedaMap() {
		return moedaMap;
	}

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		this.setIsUpdate(Boolean.FALSE);
		resetTo();
		resetFilter();
		resetResultList();
		resetTotalTax();
		this.grupoCustoCombo.clear();
		this.aprovadorCombo.clear();
		this.gerenteCombo.clear();
		this.centrolucroCombo.clear();
		this.msaCombo.clear();
		isCostCenterComboboxEditable = true; // valor padrao para add
		this.msaSelected = null;
		this.setIsCreation(Boolean.FALSE);
		this.errorList = null;
		this.setShowUpdateMapaAlocacaoModal(Boolean.FALSE);
	}

	/**
	 * Reseta a lista de to.
	 */
	public void resetResultList() {
		this.resultList = new ArrayList<ContratoPratica>();
	}

	/** Reseta o Bean. */
	public void resetTo() {
		this.to = null;
	}

	/** Reseta o filter. */
	public void resetFilter() {
		this.cliente = new Cliente();
		this.natureza = new NaturezaCentroLucro();
		this.centroLucro = new CentroLucro();
		this.filter = new ContratoPratica();
		this.nomeMoedaFilter = "";
		this.filterCodigoMsa = null;
		this.filterGrupoCusto = null;
	}

	/** Reseta o totalTax. */
	public void resetTotalTax() {
		this.totalTax = BigDecimal.valueOf(0);
	}

	/**
	 * @param isRemove
	 *            the isRemove to set
	 */
	public void setIsRemove(final Boolean isRemove) {
		this.isRemove = isRemove;
	}

	/**
	 * @return the isRemove
	 */
	public Boolean getIsRemove() {
		return isRemove;
	}

	/**
	 * @param dealFiscalList
	 *            the dealFiscalList to set
	 */
	public void setDealFiscalList(final List<DealFiscal> dealFiscalList) {
		this.dealFiscalList = dealFiscalList;
	}

	/**
	 * @return the dealFiscalList
	 */
	public List<DealFiscal> getDealFiscalList() {
		return dealFiscalList;
	}

	/**
	 * @param naturezaCPCLRowList
	 *            the naturezaCPCLRowList to set
	 */
	public void setNaturezaCPCLRowList(
			final List<NaturezaContratoPraticaCLRow> naturezaCPCLRowList) {
		this.naturezaCPCLRowList = naturezaCPCLRowList;
	}

	/**
	 * @return the naturezaCPCLRowList
	 */
	public List<NaturezaContratoPraticaCLRow> getNaturezaCPCLRowList() {
		return naturezaCPCLRowList;
	}

	/**
	 *
	 * @param aliquotaList
	 *            the aliquotaList to set
	 */
	public void setAliquotaList(final List<Aliquota> aliquotaList) {
		this.aliquotaList = aliquotaList;
	}

	/**
	 *
	 * @return the aliquotaList
	 */
	public List<Aliquota> getAliquotaList() {
		return aliquotaList;
	}

	/**
	 * Seta o map de impostos do combo.
	 *
	 * @param impostoMap
	 *            do combo.
	 */
	public void setImpostoMap(final Map<String, Long> impostoMap) {
		this.impostoMap = impostoMap;
	}

	/**
	 * Pega o map de impostos do combo.
	 *
	 * @return the impostoMap
	 */
	public Map<String, Long> getImpostoMap() {
		return impostoMap;
	}

	/**
	 * Seta o valor do totalTax.
	 *
	 * @param totalTax
	 *            the totalTax to set
	 */
	public void setTotalTax(final BigDecimal totalTax) {
		this.totalTax = totalTax;
	}

	/**
	 * Retorna o valor do totalTax.
	 *
	 * @return the totalTax
	 */
	public BigDecimal getTotalTax() {
		return totalTax;
	}

	/**
	 * @param cliente
	 *            the cliente to set
	 */
	public void setCliente(final Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param natureza
	 *            the natureza to set
	 */
	public void setNatureza(final NaturezaCentroLucro natureza) {
		this.natureza = natureza;
	}

	/**
	 * @return the natureza
	 */
	public NaturezaCentroLucro getNatureza() {
		return natureza;
	}

	/**
	 * @param centroLucro
	 *            the centroLucro to set
	 */
	public void setCentroLucro(final CentroLucro centroLucro) {
		this.centroLucro = centroLucro;
	}

	/**
	 * @return the centroLucro
	 */
	public CentroLucro getCentroLucro() {
		return centroLucro;
	}

	/**
	 * Seta a lista e pratica para o combo.
	 *
	 * @param clienteList
	 *            the clienteList to set
	 */
	public void setClienteList(final List<String> clienteList) {
		this.clienteList = clienteList;
	}

	/**
	 * Pega a lista de clientes do combo.
	 *
	 * @return a lista
	 */
	public List<String> getClienteList() {
		return clienteList;
	}

	/**
	 * Seta o map do combo.
	 *
	 * @param clienteMap
	 *            the clienteMap to set
	 */
	public void setClienteMap(final Map<String, Long> clienteMap) {
		this.clienteMap = clienteMap;
	}

	/**
	 * Pega o map do combo.
	 *
	 * @return o map
	 */
	public Map<String, Long> getClienteMap() {
		return clienteMap;
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
	 * @param pessoaList
	 *            the pessoaList to set
	 */
	public void setPessoaList(final List<String> pessoaList) {
		this.pessoaList = pessoaList;
	}

	/**
	 * @return the pessoaList
	 */
	public List<String> getPessoaList() {
		return pessoaList;
	}

	/**
	 * @param pessoaMap
	 *            the pessoaMap to set
	 */
	public void setPessoaMap(final Map<String, Long> pessoaMap) {
		this.pessoaMap = pessoaMap;
	}

	/**
	 * @return the pessoaMap
	 */
	public Map<String, Long> getPessoaMap() {
		return pessoaMap;
	}

	/**
	 * @return the nomeMoedaFilter
	 */
	public String getNomeMoedaFilter() {
		return nomeMoedaFilter;
	}

	/**
	 * @param nomeMoedaFilter
	 *            the nomeMoedaFilter
	 */
	public void setNomeMoedaFilter(final String nomeMoedaFilter) {
		this.nomeMoedaFilter = nomeMoedaFilter;
	}

	/**
	 * @return the moedaPickList
	 */
	public List<SelectItem> getMoedaPickList() {
		return moedaPickList;
	}

	/**
	 * @param moedaPickList
	 *            the moedaPickList
	 */
	public void setMoedaPickList(final List<SelectItem> moedaPickList) {
		this.moedaPickList = moedaPickList;
	}

	/**
	 * @return the selectedMoedaPickList
	 */
	public String[] getSelectedMoedaPickList() {
		return selectedMoedaPickList;
	}

	/**
	 * @param selectedMoedaPickList
	 *            the selectedMoedaPickList
	 */
	public void setSelectedMoedaPickList(final String[] selectedMoedaPickList) {
		this.selectedMoedaPickList = selectedMoedaPickList;
	}

	/**
	 * @return the dealFiscalPickList
	 */
	public List<SelectItem> getDealFiscalPickList() {
		return dealFiscalPickList;
	}

	/**
	 * @param dealFiscalPickList
	 *            the dealFiscalPickList
	 */
	public void setDealFiscalPickList(final List<SelectItem> dealFiscalPickList) {
		this.dealFiscalPickList = dealFiscalPickList;
	}

	/**
	 * @return the selectedDealFiscalPickList
	 */
	public String[] getSelectedDealFiscalPickList() {
		return selectedDealFiscalPickList;
	}

	/**
	 * @param selectedDealFiscalPickList
	 *            the selectedDealFiscalPickList
	 */
	public void setSelectedDealFiscalPickList(
			final String[] selectedDealFiscalPickList) {
		this.selectedDealFiscalPickList = selectedDealFiscalPickList;
	}

	/**
	 * @return the isContractsPerfil
	 */
	public Boolean getIsContractsPerfil() {
		return isContractsPerfil;
	}

	/**
	 * @param isContractsPerfil
	 *            the isContractsPerfil to set
	 */
	public void setIsContractsPerfil(Boolean isContractsPerfil) {
		this.isContractsPerfil = isContractsPerfil;
	}

	public List<String> getMsaCombo() {
		return msaCombo;
	}

	public void setMsaCombo(List<String> msaCombo) {
		this.msaCombo = msaCombo;
	}

	/**
	 * @return the msaComboMap
	 */
	public Map<String, Long> getMsaComboMap() {
		return msaComboMap;
	}

	/**
	 * @param msaComboMap the msaComboMap to set
	 */
	public void setMsaComboMap(Map<String, Long> msaComboMap) {
		this.msaComboMap = msaComboMap;
	}

	public ComboBox<Pessoa> getAprovadorCombo() {
		return aprovadorCombo;
	}

	public void setAprovadorCombo(ComboBox<Pessoa> aprovadorCombo) {
		this.aprovadorCombo = aprovadorCombo;
	}

	public ComboBox<Pessoa> getGerenteCombo() {
		return gerenteCombo;
	}

	public void setGerenteCombo(ComboBox<Pessoa> gerenteCombo) {
		this.gerenteCombo = gerenteCombo;
	}

	/**
	 * @return the grupoCustoCombo
	 */
	public ComboBox<GrupoCusto> getGrupoCustoCombo() {
		return grupoCustoCombo;
	}

	/**
	 * @return the centroLucroCombo
	 */
	public ComboBox<CentroLucro> getCentroLucroCombo() {return centrolucroCombo;}

	/**
	 * @param grupoCustoCombo
	 *            the grupoCustoCombo to set
	 */
	public void setGrupoCustoCombo(ComboBox<GrupoCusto> grupoCustoCombo) {
		this.grupoCustoCombo = grupoCustoCombo;
	}

	/**
	 * @param centrolucroCombo
	 *            the centrolucroCombo to set
	 */
	public void setCentroLucroCombo(ComboBox<CentroLucro> centrolucroCombo) {
		this.centrolucroCombo = centrolucroCombo;
	}


	/**
	 * Atualiza o valor do atributo filterCodigoMsa.<BR>
	 *
	 * @param filterMsa
	 *            Novo valor para o atributo
	 *            {@link ContratoPraticaBean#filterMsa}.
	 */
	public void setFilterMsa(String filterMsa) {
		this.filterMsa = filterMsa;
	}
	/**
	 * Obtem o valor do atributo {@link ContratoPraticaBean#filterMsa}.<BR>
	 *
	 * @return the filterCodigoMsa
	 */
	public String getFilterMsa() {
		return filterMsa;
	}

	/**
	 * Obtem o valor do atributo {@link ContratoPraticaBean#filterGrupoCusto}.<BR>
	 *
	 * @return the filterGrupoCusto
	 */
	public String getFilterGrupoCusto() {
		return filterGrupoCusto;
	}

	/**
	 * Atualiza o valor do atributo filterGrupoCusto.<BR>
	 *
	 * @param filterGrupoCusto
	 *            Novo valor para o atributo
	 *            {@link ContratoPraticaBean#filterGrupoCusto}.
	 */
	public void setFilterGrupoCusto(String filterGrupoCusto) {
		this.filterGrupoCusto = filterGrupoCusto;
	}


	public Boolean getDisableStatusCombo() {
		return disableStatusCombo;
	}


	public void setDisableStatusCombo(final Boolean disableStatusCombo) {
		this.disableStatusCombo = disableStatusCombo;
	}

	/**
	 * Indica se deve ou nao ser exibido o compo de {@link GrupoCusto}.
	 *
	 * @return
	 */
	public boolean getIsCostCenterComboboxEditable() {
		return this.isCostCenterComboboxEditable;
	}

	/**
	 * Setta a variavel isCostCenterComboboxEditable.
	 *
	 * @param isCostCenterComboboxEditable
	 */
	public void setIsCostCenterComboboxEditable(
			boolean isCostCenterComboboxEditable) {
		this.isCostCenterComboboxEditable = isCostCenterComboboxEditable;
	}

	/**
	 * @return the msaSelected
	 */
	public String getMsaSelected() {
		return msaSelected;
	}

	/**
	 * @param msaSelected the msaSelected to set
	 */
	public void setMsaSelected(String msaSelected) {
		this.msaSelected = msaSelected;
	}

	/**
	 * Seta a lista e pratica para o combo.
	 *
	 * @param tipoModeloNegocioList
	 *            the praticaList to set
	 */
	public void setTipoModeloNegocioList(final List<String> tipoModeloNegocioList) {
		this.tipoModeloNegocioList = tipoModeloNegocioList;
	}

	/**
	 * Pega a lista de tipoModeloNegocio do combo.
	 *
	 * @return a lista
	 */
	public List<String> getTipoModeloNegocioList() {
		return tipoModeloNegocioList;
	}

	/**
	 * Seta o map do combo.
	 *
	 * @param tipoModeloNegocioMap
	 *            the tipoModeloNegocioMap to set
	 */
	public void setTipoModeloNegocioMap(final Map<String, Long> tipoModeloNegocioMap) {
		this.tipoModeloNegocioMap = tipoModeloNegocioMap;
	}

	/**
	 * Pega o map do combo.
	 *
	 * @return o map
	 */
	public Map<String, Long> getTipoModeloNegocioMap() {
		return tipoModeloNegocioMap;
	}

	/**
	 * @return the msaSelected
	 */
	public String getTipoModeloNegocioSelected() {
		return tipoModeloNegocioSelected;
	}

	/**
	 * @param tipoModeloNegocioSelected the tipoModeloNegocioSelected to set
	 */
	public void setTipoModeloNegocioSelected(String tipoModeloNegocioSelected) {
		this.tipoModeloNegocioSelected = tipoModeloNegocioSelected;
	}

	public Boolean getIsCreation() {
		return isCreation;
	}

	public void setIsCreation(Boolean isCreation) {
		this.isCreation = isCreation;
	}

	public List<ContratoPraticaAud> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<ContratoPraticaAud> historyList) {
		this.historyList = historyList;
	}

	public void setIndicadorReembolsavel (String indicadorReembolsavel) { this.indicadorReembolsavel = indicadorReembolsavel;}

	public String getIndicadorReembolsavel () {return this.indicadorReembolsavel;}

	public void setIndicadorMultiFiscalDeal(Boolean indicadormultiFiscalDeal){ this.indicadorMultiFiscalDeal =  indicadormultiFiscalDeal;}

	public Boolean getIndicadorMultiFiscalDeal() {return this.indicadorMultiFiscalDeal;}

	public void setIndicadorAprovaAjusteReceita(Boolean indicadorAprovaAjusteReceita){ this.indicadorAprovaAjusteReceita = indicadorAprovaAjusteReceita;}

	public Boolean getIndicadorAprovaAjusteReceita() {return this.indicadorAprovaAjusteReceita;}

	public void setIndicadorWorkAtRisk(Boolean indicadorWorkAtRisk){ this.indicadorWorkAtRisk = indicadorWorkAtRisk;}

	public Boolean getIndicadorWorkAtRisk() {return this.indicadorWorkAtRisk;}

	public ComboBox<String> getRequisicaoInativacaoGerenteCombo() {
		return requisicaoInativacaoGerenteCombo;
	}

	public void setRequisicaoInativacaoGerenteCombo(ComboBox<String> requisicaoInativacaoGerenteCombo) {
		this.requisicaoInativacaoGerenteCombo = requisicaoInativacaoGerenteCombo;
	}

	public ComboBox<String> getRequisicaoInativacaoControladoriaCombo() {
		return requisicaoInativacaoControladoriaCombo;
	}

	public void setRequisicaoInativacaoControladoriaCombo(ComboBox<String> requisicaoInativacaoControladoriaCombo) {
		this.requisicaoInativacaoControladoriaCombo = requisicaoInativacaoControladoriaCombo;
	}

	public Long getFilterCodigoMsa() {
		return filterCodigoMsa;
	}

	public void setFilterCodigoMsa(Long filterCodigoMsa) {
		this.filterCodigoMsa = filterCodigoMsa;
	}

	public ComboBox<String> getAtivoInativoCombo() {
		return ativoInativoCombo;
	}

	public void setAtivoInativoCombo(ComboBox<String> ativoInativoCombo) {
		this.ativoInativoCombo = ativoInativoCombo;
	}

	public List<CentroLucro> getCentroLucroObjlList() {
		return centroLucroObjlList;
	}

	public void setCentroLucroObjlList(List<CentroLucro> centroLucroObjlList) {
		centroLucroObjlList = centroLucroObjlList;
	}

	/**
	 * @param uploadArquivo
	 *            the uploadArquivo to set
	 */
	public void setUploadArquivo(final UploadArquivo uploadArquivo) {
		this.uploadArquivo = uploadArquivo;
	}

	/**
	 * @return the uploadArquivo
	 */
	public UploadArquivo getUploadArquivo() {
		return uploadArquivo;
	}

	/**
	 * @param uploadItem
	 *            the uploadItem to set
	 */
	public void setUploadItem(final UploadItem uploadItem) {
		this.uploadItem = uploadItem;
	}

	/**
	 * @return the uploadItem
	 */
	public UploadItem getUploadItem() {
		return uploadItem;
	}

	/**
	 * @param errorList
	 *            the errorList to set
	 */
	public void setErrorList(final List<String> errorList) {
		this.errorList = errorList;
	}

	/**
	 * @return the errorList
	 */
	public List<String> getErrorList() {
		return errorList;
	}

	/**
	 * @param mapUploadError
	 *            the mapUploadError to set
	 */
	public void setMapUploadError(Map<String, List<String>> mapUploadError) {
		this.mapUploadError = mapUploadError;
	}

	/**
	 * @return the mapUploadError
	 */
	public Map<String, List<String>> getMapUploadError() {
		return mapUploadError;
	}

	/**
	 * @param errorEntryList
	 *            the mapUploadError to set
	 */
	public void setErrorEntryList(List<Map.Entry<String, List<String>>> errorEntryList) {
		this.errorEntryList = errorEntryList;
	}

	/**
	 * @return the mapUploadError
	 */
	public List<Map.Entry<String, List<String>>> getErrorEntryList() {
		return errorEntryList;
	}

	/**
	 * @return the nomeArquivoUpload
	 */
	public String getNomeArquivoUpload() {
		return nomeArquivoUpload;
	}

	/**
	 * @param nomeArquivoUpload
	 *            the nomeArquivoUpload to set
	 */
	public void setNomeArquivoUpload(String nomeArquivoUpload) {
		this.nomeArquivoUpload = nomeArquivoUpload;
	}

	/**
	 * @return the indicadorWorkAtRiskFilter
	 */
	public List<String> getIndicadorWorkAtRiskFilter() {
		return indicadorWorkAtRiskFilter;
	}

	/**
	 * @param indicadorWorkAtRiskFilter
	 */
	public void setIndicadorWorkAtRiskFilter(final List<String> indicadorWorkAtRiskFilter) {
		this.indicadorWorkAtRiskFilter = indicadorWorkAtRiskFilter;
	}

	public Boolean getShowUpdateMapaAlocacaoModal() {
		return showUpdateMapaAlocacaoModal;
	}

	public void setShowUpdateMapaAlocacaoModal(Boolean showUpdateMapaAlocacaoModal) {
		this.showUpdateMapaAlocacaoModal = showUpdateMapaAlocacaoModal;
	}

	public ComboBox<CentroLucro> getBusinessManagerCombo() {
		return businessManagerCombo;
	}

	public void setBusinessManagerCombo(ComboBox<CentroLucro> businessManagerCombo) {
		this.businessManagerCombo = businessManagerCombo;
	}

	public String getAnoInicioVigenciaBM() {
		return anoInicioVigenciaBM;
	}

	public void setAnoInicioVigenciaBM(String anoInicioVigenciaBM) {
		this.anoInicioVigenciaBM = anoInicioVigenciaBM;
	}

	public String getMesInicioVigenciaBM() {
		return mesInicioVigenciaBM;
	}

	public void setMesInicioVigenciaBM(String mesInicioVigenciaBM) {
		this.mesInicioVigenciaBM = mesInicioVigenciaBM;
	}

	public ComboBox<CentroLucro> getSsoCombo() {
		return ssoCombo;
	}

	public void setSsoCombo(ComboBox<CentroLucro> ssoCombo) {
		this.ssoCombo = ssoCombo;
	}

	public ComboBox<CentroLucro> getUkmtCombo() {
		return ukmtCombo;
	}

	public void setUkmtCombo(ComboBox<CentroLucro> ukmtCombo) {
		this.ukmtCombo = ukmtCombo;
	}

	public String getAnoInicioVigenciaSSO() {
		return anoInicioVigenciaSSO;
	}

	public void setAnoInicioVigenciaSSO(String anoInicioVigenciaSSO) {
		this.anoInicioVigenciaSSO = anoInicioVigenciaSSO;
	}

	public String getMesInicioVigenciaSSO() {
		return mesInicioVigenciaSSO;
	}

	public void setMesInicioVigenciaSSO(String mesInicioVigenciaSSO) {
		this.mesInicioVigenciaSSO = mesInicioVigenciaSSO;
	}

	public String getAnoInicioVigenciaUKMT() {
		return anoInicioVigenciaUKMT;
	}

	public void setAnoInicioVigenciaUKMT(String anoInicioVigenciaUKMT) {
		this.anoInicioVigenciaUKMT = anoInicioVigenciaUKMT;
	}

	public String getMesInicioVigenciaUKMT() {
		return mesInicioVigenciaUKMT;
	}

	public void setMesInicioVigenciaUKMT(String mesInicioVigenciaUKMT) {
		this.mesInicioVigenciaUKMT = mesInicioVigenciaUKMT;
	}
}