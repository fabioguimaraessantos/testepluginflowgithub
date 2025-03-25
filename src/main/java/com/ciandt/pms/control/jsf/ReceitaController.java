package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.*;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.enums.AjusteReceitaForecastStatusEnum;
import com.ciandt.pms.enums.SgTipoServico;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.TaxaImpostoNotFoundException;
import com.ciandt.pms.metrics.PrometheusMetrics;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.*;
import com.ciandt.pms.poi.HSSFTemplate;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * A classe ReceitaController proporciona as funcionalidades da camada de
 * apresentacao para a entidade Receita.
 *
 * @since 24/09/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BUS.REVENUE:CR", "BUS.REVENUE:VW", "BUS.REVENUE:ED", "BUS.REVENUE:DE", "BUS.REVENUE.REPORT:VW",
		"BUS.REVENUE.UR_REVIEW:VW", "BUS.REVENUE.ADJUSTMENT:ED", "BUS.REVENUE.ADJUSTMENT:DE", "BUS.REVENUE.ADJUSTMENT:CR"})
public class ReceitaController {

	/** Logger. */
	private final Logger logger= LogManager.getLogger(ReceitaController.class.getName());


	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_RECEITA_ADD = "receita_add";
	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_RECEITA_SEARCH = "receita_research";
	/** outcome tela relatorio da entidade. */
	private static final String OUTCOME_RECEITA_REPORT = "receita_report";
	/** outcome tela gerenciamento da entidade. */
	private static final String OUTCOME_RECEITA_MANAGE = "receita_manage";
	/** outcome tela gerenciamento da entidade - view. */
	private static final String OUTCOME_RECEITA_MANAGE_VIEW = "receita_manage_view";
	/** outcome tela gerenciamento da entidade - view. */
	private static final String OUTCOME_RECEITA_SEARCH_REVIEW_UR = "receita_research_review_ur";
	/** outcome tela gerenciamento da entidade. */
	private static final String OUTCOME_RECEITA_REVIEW_UR = "receita_review_ur";
	/** Outcome para tela de justificativa de resultado. */
	private static final String OUTCOME_RECEITA_JUSTIFICATIVA_RESULTADO = "receita_justificativa_resultado";
	/** outcome tela view da AjusteReceita. */
	private static final String OUTCOME_AJUSTE_RECEITA_VIEW = "ajusteReceita_view";
	/** outcome tela de criacao de receitas de licenca. */
	private static final String OUTCOME_RECEITA_LICENSE_ADD = "receita_license_add";

	/** Instancia do servico. */
	@Autowired
	private IReceitaService receitaService;

	/** Instancia do servico. */
	@Autowired
	private IReceitaLicencaService receitaLicencaService;

	/** Instancia do servico. */
	@Autowired
	private IReceitaDealFiscalService receitaDealFiscalService;

	/** Instancia do servico. */
	@Autowired
	private IAjusteReceitaService ajusteReceitaService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico PerfilVendido. */
	@Autowired
	private IPerfilVendidoService perfilVendidoService;

	/** Instancia do servico ContratoPratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do servico Cliente. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do servico Pratica. */
	@Autowired
	private IPraticaService praticaService;

	/** Instancia do servico ExplicacaoDesvio. */
	@Autowired
	private IExplicacaoDesvioService explicacaoDesvioService;

	/** Instancia do servico NaturezaCentroLucro. */
	@Autowired
	private INaturezaCentroLucroService naturezaService;

	/** Instancia do servico CentroLucro. */
	@Autowired
	private ICentroLucroService centroLucroService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do servico Etiqueta. */
	@Autowired
	private IEtiquetaService etiquetaService;

	/** Instancia do servico do ReceitaDealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/** Instancia do servico do ReceitaPlantao. */
	@Autowired
	private IReceitaPlantaoService receitaPlantaoService;

	/** Instancia do servico do ReceitaDealFiscal. */
	@Autowired
	private IMapaAlocacaoService mapaAlocacaoService;

	/** Instancia do servico do EstratificacaoUr. */
	@Autowired
	private IEstratificacaoUrService estratificacaoUrService;

	/** Instancia do servico do VwEstratificacaoUrResultado. */
	@Autowired
	private IVwEstratificacaoUrResultadoService estratificacaoUrResultadoService;

	/** Instancia do servico da ReceitaResultado. */
	@Autowired
	private IReceitaResultadoService receitaResultadoService;

	/** Instancia do servico da MotivoResultado. */
	@Autowired
	private IMotivoResultadoService motivoResultadoService;

	/** Servico de TipoAjuste. */
	@Autowired
	private ITipoAjusteService tipoAjusteService;

	/** Servico de CotacaoMoeda. */
	@Autowired
	private ICotacaoMoedaService cotacaoMoedaService;

	/** Servico de ReceitaMoeda. */
	@Autowired
	private IReceitaMoedaService receitaMoedaService;

	/** Servico de Msa. */
	@Autowired
	private IMsaService msaService;

	/** Servico de moeda. */
	@Autowired
	private IMoedaService moedaService;

	/** Servico de ItemReceita. */
	@Autowired
	private IItemReceitaService itemReceitaService;

	/** Servico de faturaReceita. */
	@Autowired
	private IFaturaReceitaService faturaReceitaService;

	@Autowired
	private IReceitaTipoService receitaTipoService;

	@Autowired
	private ITipoServicoService tipoServicoService;

	@Autowired
	private IVwMegaCCustoService vwMegaCCustoService;

	@Autowired
	private IVwPMSProjetoService vwPMSProjetoService;

	@Autowired
	private IConvergenciaService convergenciaService;

	@Autowired
	private ICpraticaDfiscalService cpraticaDfiscalService;

	@Autowired
	private ICompanyErpService companyErpService;

	@Autowired
	private IAjusteReceitaForecastService ajusteReceitaForecastService;

	/** Instancia do bean. */
	@Autowired
	private ReceitaBean bean = null;

	/** Instancia do bean de MapaAlocacao. */
	@Autowired
	private MapaAlocacaoBean mapaAlocacaoBean = null;

	/** Instancia do bean de AjusteReceita. */
	@Autowired
	private AjusteReceitaBean ajusteReceitaBean = null;

	/** Instancia do bean de ItemReceita. */
	@Autowired
	private ItemReceitaBean itemReceitaBean = null;

	/** Instancia do bean controle de mensagnes. */
	@Autowired
	private MessageControlBean messageConntrolBean = null;

	@Autowired
	private IReportService reportService;

	@Autowired
	private ITabelaPrecoService tabelaPrecoService;

	/**
	 * @return the mapaAlocacaoBean
	 */
	public MapaAlocacaoBean getMapaAlocacaoBean() {
		return mapaAlocacaoBean;
	}

	/**
	 * @param mapaAlocacaoBean
	 *            the mapaAlocacaoBean to set
	 */
	public void setMapaAlocacaoBean(final MapaAlocacaoBean mapaAlocacaoBean) {
		this.mapaAlocacaoBean = mapaAlocacaoBean;
	}

	/**
	 * @return the ajusteReceitaBean
	 */
	public AjusteReceitaBean getAjusteReceitaBean() {
		return ajusteReceitaBean;
	}

	/**
	 * @param ajusteReceitaBean
	 *            the ajusteReceitaBean to set
	 */
	public void setAjusteReceitaBean(final AjusteReceitaBean ajusteReceitaBean) {
		this.ajusteReceitaBean = ajusteReceitaBean;
	}

	/**
	 * @return the itemReceitaBean
	 */
	public ItemReceitaBean getItemReceitaBean() {
		return itemReceitaBean;
	}

	/**
	 * @param itemReceitaBean
	 *            the itemReceitaBean to set
	 */
	public void setItemReceitaBean(final ItemReceitaBean itemReceitaBean) {
		this.itemReceitaBean = itemReceitaBean;
	}

	/**
	 * @return the messageConntrolBean
	 */
	public MessageControlBean getMessageConntrolBean() {
		return messageConntrolBean;
	}

	/**
	 * @param messageConntrolBean
	 *            the messageConntrolBean to set
	 */
	public void setMessageConntrolBean(
			final MessageControlBean messageConntrolBean) {
		this.messageConntrolBean = messageConntrolBean;
	}

	/**
	 * Pega o bean ReceitaBean.
	 *
	 * @return retorna o bean
	 */
	public ReceitaBean getBean() {
		return bean;
	}

	/**
	 * Seta o bean ReceitaBean.
	 *
	 * @param bean
	 *            do tipo ReceitaBean
	 */
	public void setBean(final ReceitaBean bean) {
		this.bean = bean;
	}

	/**
	 * Carrega os combos e prepara o formulario de filtro.
	 *
	 */
	private void loadFilterForm() {
		bean.reset();

		// pega o mes corrente
		Date currentMonth = DateUtils.addMonths(this.getClosingDate(), 1);
		// seta a data com o mes corrente
		bean.setValidityMonth("" + DateUtil.getMonthNumber(currentMonth));
		bean.setValidityYear("" + DateUtil.getYear(currentMonth));

		this.loadClienteList(clienteService.findClienteAllClientePai());
		this.loadPraticaList(praticaService.findPraticaAll());
		this.loadNaturezaCentroLucroList(naturezaService
				.findNaturezaCentroLucroAll());

		List<ContratoPratica> cp = contratoPraticaService.findAllCompleteForCombobox();
		this.loadContratoPraticaList(cp);
	}

	/**
	 * Carrega os combos e prepara o formulario de filtro da estratificacao.
	 *
	 */
	private void loadFilterFormReview() {
		mapaAlocacaoBean.reset();

		// pega o mes corrente
		Date currentMonth = new Date();
		// seta a data com o mes corrente
		mapaAlocacaoBean.setValidityMonthBeg(""
				+ DateUtil.getMonthNumber(currentMonth));
		mapaAlocacaoBean
				.setValidityYearBeg("" + DateUtil.getYear(currentMonth));

		this.loadContratoPraticaListMapa(contratoPraticaService.findAllCompleteForCombobox());

		this.loadNaturezaCentroLucroListReview(naturezaService
				.findNaturezaCentroLucroAll());

		// busca as explicacoes para o desvio
		this.loadExplicacoesDesvio(explicacaoDesvioService
				.findExplicacaoDesvioAtivos());

		this.loadClienteListMapa(clienteService.findClienteAllClientePaiForComboBox());
	}

	/**
	 * Popula a lista de clientes para combobox.
	 *
	 * @param clientes
	 *            lista de clientes.
	 *
	 */
	private void loadClienteListMapa(final List<Cliente> clientes) {

		Map<String, Long> clienteMap = new HashMap<String, Long>();
		List<String> clienteList = new ArrayList<String>();

		for (Cliente cliente : clientes) {
			clienteMap
					.put(cliente.getNomeCliente(), cliente.getCodigoCliente());
			clienteList.add(cliente.getNomeCliente());
		}
		mapaAlocacaoBean.setClienteMap(clienteMap);
		mapaAlocacaoBean.setClienteList(clienteList);
	}

	/**
	 * Popula a lista de ContratoPratica para combobox de contratos praticas.
	 *
	 * @param motivoResultado
	 *            lista de MotivoResultado
	 */
	private void loadMotivoResultadoMapa(
			final List<MotivoResultado> motivoResultado) {
		Map<String, Long> motivoResultadoMap = new HashMap<String, Long>();
		List<String> motivoResultadoList = new ArrayList<String>();

		for (MotivoResultado motivo : motivoResultado) {
			motivoResultadoMap.put(motivo.getNomeMotivoResultado(),
					motivo.getCodigoMotivoResultado());
			motivoResultadoList.add(motivo.getNomeMotivoResultado());
		}

		bean.setMotivoResultadoList(motivoResultadoList);
		bean.setMotivoResultadoMap(motivoResultadoMap);
	}

	/**
	 * Popula a lista de ContratoPratica para combobox de contratos praticas.
	 *
	 * @param contratosPratica
	 *            lista de ContratoPratica.
	 *
	 */
	private void loadContratoPraticaListMapa(
			final List<ContratoPratica> contratosPratica) {

		Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
		List<String> contratoPraticaList = new ArrayList<String>();

		for (ContratoPratica cp : contratosPratica) {
			contratoPraticaMap.put(cp.getNomeContratoPratica(),
					cp.getCodigoContratoPratica());
			contratoPraticaList.add(cp.getNomeContratoPratica());
		}

		mapaAlocacaoBean.setContratoPraticaMap(contratoPraticaMap);
		mapaAlocacaoBean.setContratoPraticaList(contratoPraticaList);
	}

	/**
	 * Popula a lista de ExplicacoesDesvio para combobox.
	 *
	 * @param explicacoesDesvio
	 *            lista de ExplicacoesDesvio.
	 *
	 */
	private void loadExplicacoesDesvio(
			final List<ExplicacaoDesvio> explicacoesDesvio) {

		Map<String, Long> explicacoesDesvioMap = new HashMap<String, Long>();
		List<String> explicacoesDesvioList = new ArrayList<String>();

		for (ExplicacaoDesvio ed : explicacoesDesvio) {
			if (ed.getIndicadorAtivo().toUpperCase().equals(Constants.SIM)) {
				explicacoesDesvioMap.put(ed.getNomeMotivoDesvio(),
						ed.getCodigoMotivoDesvio());
				explicacoesDesvioList.add(ed.getNomeMotivoDesvio());

				if (ed.getIndicadorSelecionado().toUpperCase()
						.equals(Constants.SIM)) {
					mapaAlocacaoBean.setExplicacaoDesvioDefault(ed);
				}
			}
		}
		mapaAlocacaoBean.setExplicacoesDesvioMap(explicacoesDesvioMap);
		mapaAlocacaoBean.setExplicacoesDesvio(explicacoesDesvioList);
	}

	/**
	 * Prepara a tela de pesquisa da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareSearch() {
		this.loadFilterForm();
		ajusteReceitaBean.setShowViewListVaziaPanel(Boolean.valueOf(false));
		return OUTCOME_RECEITA_SEARCH;
	}

	/**
	 * Volta para a tela de busca de mapa.
	 *
	 * @return pagina de destino
	 */
	public String backFromReviewUr() {
		this.findMapaByFilter();
		mapaAlocacaoBean.setIsUpdateReviewUr(Boolean.FALSE);
		return OUTCOME_RECEITA_SEARCH_REVIEW_UR;
	}

	/**
	 * Prepara a tela de pesquisa da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareResearchReviewUr() {
		this.loadFilterFormReview();
		return OUTCOME_RECEITA_SEARCH_REVIEW_UR;
	}

	/**
	 * Prepara a tela do relatorio da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareReport() {
		this.loadFilterForm();
		return OUTCOME_RECEITA_REPORT;
	}

	/**
	 * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
	 *
	 * @param e
	 *            - evento de mudanca
	 */
	public void prepareCentroLucroCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null) {
			Long codNatureza = bean.getNaturezaMap().get(value);
			NaturezaCentroLucro natureza = null;

			// se o centro de lucro existir
			if (codNatureza != null) {
				natureza = naturezaService
						.findNaturezaCentroLucroById(codNatureza);
				this.loadCentroLucroList(centroLucroService
						.findCentroLucroByNatureza(natureza));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadCentroLucroList(new ArrayList<CentroLucro>());
			}
		}
	}

	/**
	 * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
	 *
	 * @param e
	 *            - evento de mudanca
	 */
	public void prepareCentroLucroComboReview(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null) {
			Long codNatureza = mapaAlocacaoBean.getNaturezaMap().get(value);
			NaturezaCentroLucro natureza = null;

			// se o centro de lucro existir
			if (codNatureza != null) {
				natureza = naturezaService
						.findNaturezaCentroLucroById(codNatureza);
				this.loadCentroLucroListReview(centroLucroService
						.findCentroLucroByNatureza(natureza));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadCentroLucroListReview(new ArrayList<CentroLucro>());
			}
		}
	}

	/**
	 * Prepara a tela para criar uma entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareCreate() {
		bean.reset();
		// marca como modo de inser��o
		bean.setIsUpdate(Boolean.FALSE);

		if(bean.getContratoPraticaList().size() == 0 || bean.getContratoPraticaMap().size() == 0) {
			// carrega a lista ContratoPratica
			this.loadContratoPraticaList(contratoPraticaService.findAllCompleteForCombobox());
		}
		this.loadReceitaTipoList(receitaTipoService.findAll());

		bean.setCurrentRowType(Constants.RECEITA_TYPE_SERVICE);

		return OUTCOME_RECEITA_ADD;
	}

	/**
	 * Prepara a tela para criar uma entidade. Partindo da tela de filtro.
	 *
	 * @return pagina de destino
	 */
	public String prepareFilterCreate() {
		ajusteReceitaBean.setFlagAbaAjusteReceita(Boolean.valueOf(false));
		// reseta a justificativa
		bean.resetJustificativaFields();

		// marca como modo de inser��o
		bean.setIsUpdate(Boolean.FALSE);

		if(bean.getContratoPraticaList().size() == 0 || bean.getContratoPraticaMap().size() == 0) {
			// carrega a lista ContratoPratica
			this.loadContratoPraticaList(contratoPraticaService.findContratoPraticaAllComplete());
		}
		bean.setLastPageId(bean.getCurrentPageId());
		return this.prepareCreateNext();
	}

	/**
	 * Carrega lista de ReceitaMoedaRow apartir do {@link ContratoPratica}.
	 *
	 * @param contratoPratica
	 *            - {@link ContratoPratica}
	 *
	 * @param periodoAlocacao
	 *            - data mes referente ao periodo das alocacoes
	 *
	 * @return lista de {@link ReceitaMoedaRow}
	 */
	private List<ReceitaMoedaRow> loadReceitaMoedaRowList(
			final ContratoPratica contratoPratica, final Date periodoAlocacao) {

		List<ReceitaMoedaRow> receitaMoedaRows = new ArrayList<>();
		List<DealFiscal> dealFiscalList;
		ReceitaMoedaRow receitaMoedaRow;
		ReceitaDealFiscalRow recDealFiscalRow;

		// Busca lista de moedas associadas as alocacao do contrato pratica
		List<Moeda> listMoeda = moedaService.findMoedaByAlocationsWithCLob(
				contratoPratica, periodoAlocacao);

		boolean beanSetado = Boolean.valueOf(false);

		// Percorre todas as moedas associadas ao ContratoPratica (monta a aba
		// main com as respectivas moedas do contrato pratica)
		for (Moeda moeda : listMoeda) {

			// Cria a lista de ItemReceitaRow
			List<ItemReceitaRow> itemReceitaRowList = receitaService
					.prepareCreateListItemReceita(
							contratoPratica.getCodigoContratoPratica(),
							bean.getValidityMonth(), bean.getValidityYear(),
							moeda.getCodigoMoeda());

			// para evitar null pointer caso a lista nao teja item
			if (itemReceitaRowList == null) {
				itemReceitaRowList = new ArrayList<>();
			}

			// ReceitaMoedaRow
			ReceitaMoeda rm = new ReceitaMoeda();
			rm.setMoeda(moeda);
			rm.setReceita(bean.getTo());
			receitaMoedaRow = new ReceitaMoedaRow();
			receitaMoedaRow.setReceitaMoeda(rm);

			//Carregar e ordernar itens da receita
			bean.getToItemReceitaRow().setComparableField(ItemReceitaRow.COLUMN_SITE);
			bean.setAscending(true);
			Collections.sort(itemReceitaRowList);
			receitaMoedaRow.setItemReceitaRowList(itemReceitaRowList);

			List<ReceitaDealFiscalRow> recDealFiscalRowList = new ArrayList<>();
			ReceitaDealFiscal receitaDealFiscal;

			List<String> listSgTipoServico = new ArrayList<>();
			//Consultando somente tipos de serviços referente a ServiceRevenue, para evitar de criar receitas de Licenses em FD c/ tipo de serviço DEV/SUPT
			List<TipoServico> listTipoServico = tipoServicoService.findByFonteReceita(FonteReceita.SERVICES);
			for(TipoServico ts : listTipoServico){
				listSgTipoServico.add(ts.getSiglaTipoServico());
			}
			dealFiscalList = dealFiscalService.findActiveAndNotLogicDeletedByClobAndCurrencyAndTipoServico(contratoPratica, moeda, listSgTipoServico);

			// Percorre todos os DealFiscals associados ao ContratoPratica e
			// monta o ReceitaMoedaRow (monta a lista de ReceitaDealFiscal por
			// moeda)
			for (DealFiscal dealFiscal : dealFiscalList) {
				if (dealFiscal.getMoeda().getCodigoMoeda()
						.equals(moeda.getCodigoMoeda())) {

					// Cria uma ReceitaDealFiscal em memoria
					receitaDealFiscal = new ReceitaDealFiscal();
					receitaDealFiscal.setDealFiscal(dealFiscalService
							.findDealFiscalById(dealFiscal
									.getCodigoDealFiscal()));

					// Verifica se possui apenas um dealfiscal (para ja trazer o
					// valor da receita no campo editavel)
					if (dealFiscalList.size() == 1) {
						receitaDealFiscal.setValorReceita(receitaMoedaRow.getTotalAmount());
					} else {
						receitaDealFiscal.setValorReceita(BigDecimal.valueOf(0));
					}

					ReceitaPlantao receitaPlantao = receitaPlantaoService
							.findByReceitaDealFiscal(receitaDealFiscal);
					receitaDealFiscal.setReceitaPlantao(receitaPlantao);

					// Seta os atributos do objeto receitaMoedaRow
					recDealFiscalRow = new ReceitaDealFiscalRow();
					recDealFiscalRow.setTo(receitaDealFiscal);
					if(contratoPratica.getIndicadorMultiDealFiscal().equals(Constants.FLAG_YES)) {
						recDealFiscalRow.setIsReceitaEditavel(Boolean.valueOf(false));
					}else{
						recDealFiscalRow.setIsReceitaEditavel(Boolean.valueOf(true));
					}
					recDealFiscalRow.setIsReceitaPlantaoEditavel(Boolean.valueOf(true));

					recDealFiscalRowList.add(recDealFiscalRow);
				}
			}

			receitaMoedaRow.setRecDealFiscalRowList(recDealFiscalRowList);

			if (!beanSetado) {
				bean.setCurrentReceitaMoedaRow(receitaMoedaRow);
				beanSetado = Boolean.valueOf(true);
			}

			receitaMoedaRows.add(receitaMoedaRow);
		}

		return receitaMoedaRows;
	}

	public Boolean verifyClosingDateRevenue(final Date startDate,
											final Boolean showMsgError) {
		Date closingRevenueDate = moduloService.getClosingDateRevenue();
		if (startDate.compareTo(closingRevenueDate) > 0) {
			return Boolean.valueOf(true);
		} else {
			if (showMsgError) {
				Messages.showError("verifyClosingDateRevenue",
						Constants.MSG_ERROR_AJUSTE_RECEITA_ADD_CLOS_DATE,
						DateUtil.formatDate(closingRevenueDate,
								Constants.DEFAULT_DATE_PATTERN_SIMPLE,
								Constants.DEFAULT_CALENDAR_LOCALE));
			}

			return Boolean.valueOf(false);
		}
	}

	/**
	 * Prepara a tela de gerenciamento do Receita - criacao.
	 *
	 * @return pagina de destino
	 */
	public String prepareCreateNext() {

		String validityMonth = bean.getValidityMonth();
		String validityYear = bean.getValidityYear();

		bean.setTotalReceitaDeal(0D);

		// verifica o Receita e atualiza o ContratoPratica
		Receita to = this.verifyCreateOrUpdate();

		if (to == null) {
			// se o ContratoPratica nao existir na base de dados
			Messages.showError("prepareCreateNext",
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
					Constants.ENTITY_NAME_CONTRATO_PRATICA);
			return null;
		}
		// se existir o ContratoPratica segue o fluxo
		// (redireciona para a tela de gerenciamento do Receita)

		Boolean isInternationalClient = receitaService.isInternationalClient(to);

		if(!this.verifyClosingDateRevenueOrInternationalRevenue(DateUtil.getDate(validityMonth, validityYear),
				Boolean.TRUE, isInternationalClient)){
			return null;
		}

		ReceitaTipo receitaTipo = receitaTipoService.findByNomeReceitaTipo(bean.getCurrentRowType());
		if (receitaTipo == null) {
			Messages.showError("receitaTipo", Constants.RECEITA_MSG_ERROR_TIPO_INVALIDO);
			return null;
		}

		if (receitaTipo.getSiglaReceitaTipo().equals(Constants.RECEITA_TYPE_ABBREVIATION_SERVICE)) {
			// verifica se existe um mapa para a receita
			if (mapaAlocacaoService.findMapaAlocacaoByContratoPratica(to
					.getContratoPratica()) == null) {
				Messages.showWarning("prepareCreateNext",
						Constants.MSG_ERROR_NO_ALOCATION_MAP_FOUND);
				return null;
			}

			// horas faturadas sempre � criada com versao WORKING
			to.setIndicadorVersao(Constants.VERSION_RECEITA_WORKING);

			// valida se j� existe a Receita (ContratoPratica x M�s)
			ContratoPratica contratoPratica = contratoPraticaService
					.findContratoPraticaByName(to.getContratoPratica()
							.getNomeContratoPratica());

			List<AjusteReceitaForecast> ajusteReceitaForecastList = getAjusteReceitaForecastByContratoPratica(contratoPratica);

			if (contratoPratica.getIndicadorAprovaAjusteReceita().equals(Constants.YES) &&
					(ajusteReceitaForecastList.isEmpty() || !isAjusteReceitaForecastApproved(ajusteReceitaForecastList.get(0)))) {
				Messages.showError("prepareCreateNext", Constants.MSG_ERROR_INVAL_RECEITA_AJUSTE);
				return null;
			}
			to.setContratoPratica(contratoPratica);

			// verifica se o ajuste não foi retornado para revisão
			boolean isRevenueOpenToAdjustment = AjusteReceitaForecastStatusEnum.SEND_TO_REVIEW.getIndicadorStatus()
					.equals(ajusteReceitaForecastList.get(0).getAjusteReceitaForecastStatus().getStatus());
			bean.setIsRevenueOpenToAdjustment(isRevenueOpenToAdjustment);

			if (!receitaService.isValidReceitaVersion(contratoPratica.getCodigoContratoPratica(), validityMonth, validityYear)) {
				// se o Receita nao for v�lido, retorna null
				Messages.showError("prepareCreateNext",
						Constants.MSG_ERROR_INVAL_RECEITA);
				return null;
			}
			// atribui o mes/ano
			to.setDataMes(DateUtil.getDate(validityMonth, validityYear));

			bean.setTo(to);

			// Carrega lista de ReceitaMoedaRow para a aba main da tela
			List<ReceitaMoedaRow> receitaMoedaRowList = this.loadReceitaMoedaRowList(contratoPratica, bean.getTo().getDataMes());

			if (receitaMoedaRowList.isEmpty()) {
				Messages.showError("prepareCreateNext", Constants.MENSAGEM_NO_ITENS_MAPA_ALOACAO_RECEITA);
				return null;
			}

			Boolean canCreateRevenue = true;
			if (contratoPratica.getIndicadorAprovaAjusteReceita().equals(Constants.YES)) {
				bean.getCurrentReceitaMoedaRow().setValorReceitaAjustadoForecast(ajusteReceitaForecastList.get(0).getValorAjustado());

				if (Constants.FLAG_YES.equals(contratoPratica.getIndicadorMultiDealFiscal())) {
					receitaService.calculateIncomeRedistribution(bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(),
							ajusteReceitaForecastList.get(0).getValorAjustado(), bean.getTo());

					bean.getCurrentReceitaMoedaRow().getRecDealFiscalRowList().get(0).getTo().setValorReceita(ajusteReceitaForecastList.get(0).getValorAjustado());
					bean.setReceitaMoedaRowList(receitaMoedaRowList);

					BigDecimal factor = ajusteReceitaForecastList.get(0).getValorAjustado().divide(bean.getReceitaMoedaRowList().get(0).getTotalAmount(), 100, RoundingMode.HALF_UP);

					receitaService.calculateDealFiscalRedistribution(
							factor,
							bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(),
							bean.getReceitaMoedaRowList().get(0).getRecDealFiscalRowList());
				} else {
					this.calculateIncomeRedistribution(bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(),
							ajusteReceitaForecastList.get(0).getValorAjustado(), bean.getTo());

					if (!bean.getCurrentReceitaMoedaRow().getRecDealFiscalRowList().isEmpty()) {
						bean.getCurrentReceitaMoedaRow().getRecDealFiscalRowList().get(0).getTo().setValorReceita(ajusteReceitaForecastList.get(0).getValorAjustado());
					}
					bean.setReceitaMoedaRowList(receitaMoedaRowList);
				}
			} else {
				bean.setReceitaMoedaRowList(receitaMoedaRowList);
				if (Constants.FLAG_YES.equals(contratoPratica.getIndicadorMultiDealFiscal())) {
					bean.setReceitaMoedaRowList(receitaMoedaRowList);
					canCreateRevenue = receitaService.calculateDealFiscalRedistribution(
							bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(),
							bean.getReceitaMoedaRowList().get(0).getRecDealFiscalRowList());
				}
			}
			ajusteReceitaBean.setFlagAbaAjusteReceita(Boolean.valueOf(false));

			if (canCreateRevenue) {
				return OUTCOME_RECEITA_MANAGE;
			}

		} else if (receitaTipo.getSiglaReceitaTipo().equals(Constants.RECEITA_TYPE_ABBREVIATION_LICENCE)) {

			bean.setValorReceitaLicenca(BigDecimal.ZERO);
			bean.setInstallments(null);
			bean.setNomeFiscalDeal("");
			to.setTextoObservacao("");
			bean.setReceitaLicencaRows(new ArrayList<ReceitaLicencaRow>());
			ContratoPratica contratoPratica = contratoPraticaService
					.findContratoPraticaByName(to.getContratoPratica()
							.getNomeContratoPratica());
			to.setContratoPratica(contratoPratica);
			to.setIndicadorVersao(Constants.VERSION_RECEITA_WORKING);

			to.setDataMes(DateUtil.getDate(validityMonth, validityYear));
			TipoServico tipoServico = tipoServicoService.findTipoServicoById(3L);
			this.loadDealFiscalList(dealFiscalService.findByContratoPraticaAndTipoServicoAndActive(contratoPratica, tipoServico));

			TipoServico tipoServicoComissao = tipoServicoService.findTipoServicoById(22L);
			this.loadDealFiscalList(dealFiscalService.findByContratoPraticaAndTipoServicoAndActive(contratoPratica, tipoServicoComissao));

			bean.setTo(to);

			return OUTCOME_RECEITA_LICENSE_ADD;
		}

		// retorna null caso nao seja validado
		return null;
	}

	private boolean isAjusteReceitaForecastApproved(AjusteReceitaForecast ajusteReceitaForecast) {
		return AjusteReceitaForecastStatusEnum.ADJUSTMENT_APPROVED.getIndicadorStatus().equals(
				ajusteReceitaForecast.getAjusteReceitaForecastStatus().getStatus())
				||
				AjusteReceitaForecastStatusEnum.REVENUE_APPROVED.getIndicadorStatus().equals(
						ajusteReceitaForecast.getAjusteReceitaForecastStatus().getStatus());
	}

	private List<AjusteReceitaForecast> getAjusteReceitaForecastByContratoPratica(ContratoPratica contratoPratica) {

		if (contratoPratica.getIndicadorAprovaAjusteReceita().equals(Constants.YES)) {
			Date dataMesReceita = DateUtil.getDate(bean.getValidityMonth(), bean.getValidityYear());

			List<AjusteReceitaForecast> ajusteReceitaForecastList = ajusteReceitaForecastService
					.findByContratoPraticaAndDataMesReceita(contratoPratica.getCodigoContratoPratica(), dataMesReceita);

			if (ajusteReceitaForecastList == null || ajusteReceitaForecastList.isEmpty()) {
				return Collections.emptyList();
			}

			bean.setAjusteReceitaForecastList(ajusteReceitaForecastList);

			return ajusteReceitaForecastList;
		}
		return Collections.emptyList();
	}

	/**
	 * Cria um {@link ShortTermRevenueRow} apartir do {@link ContratoPratica},
	 * {@link Moeda} e dataMes.
	 *
	 * @param contratoPratica
	 *            contrato pratica usado no filtro.
	 *
	 * @param moeda
	 *            moeda usada no filtro.
	 *
	 * @param dataMes
	 *            mes referencia usado no filtro.
	 *
	 * @return retorna um objeto do tipo {@link ShortTermRevenueRow}
	 */
	private ShortTermRevenueRow buildShortTermRevenueRow(
			final ContratoPratica contratoPratica, final Moeda moeda,
			final Date dataMes) {

		this.prepareJustificativa();

		// filtro para a busca da receita resultado
		ReceitaResultado filter = new ReceitaResultado();
		filter.setContratoPratica(contratoPratica);
		filter.setMoeda(moeda);
		filter.setDataMes(dataMes);

		ShortTermRevenueRow strr = new ShortTermRevenueRow();
		ReceitaResultado rr = receitaResultadoService
				.findReceitaResultadoByContratoAndMoedaAndDate(filter);

		// setta o receita resultado caso exista foto
		if (rr != null && rr.getMotivoResultado() != null) {
			strr.setMotivoResultadoSelected(rr.getMotivoResultado()
					.getNomeMotivoResultado());
			strr.setReceitaResultado(rr);
			strr.setPercentualDiferenca(receitaService.getPercentualDiferenca(
					rr.getValorReceitaPlanejada(),
					rr.getValorReceitaRealizada()));
		}

		return strr;
	}

	/**
	 * Prepara a tela de gerenciamento do Receita - atualizacao.
	 *
	 * @return pagina de destino
	 */
	public String prepareManage() {
		Receita receita = receitaService
				.findReceitaById(bean.getCurrentRowId());
		List<ReceitaMoedaRow> receitaMoedaRowList = new ArrayList<ReceitaMoedaRow>();
		ContratoPratica cp = contratoPraticaService.findContratoPraticaById(receita.getContratoPratica().getCodigoContratoPratica());
		ReceitaMoedaRow receitaMoedaRow;
		ReceitaDealFiscalRow receitaDealFiscalRow;
		receitaDealFiscalRow = new ReceitaDealFiscalRow();

		// Busca ReceitaMoedas criadas.
		List<ReceitaMoeda> receitaMoedaList = receitaMoedaService
				.findReceitaMoedaByReceita(receita);

		boolean beanSetado = Boolean.valueOf(false);

		// Percorre lista de receita Moeda (aba main)
		for (ReceitaMoeda receitaMoeda : receitaMoedaList) {

			this.loadAjusteReceitaRowList(receitaMoeda);
			// Lista de deal fiscal para combobox
			List<DealFiscal> listDealFiscal = new ArrayList<DealFiscal>();

			receitaMoedaRow = new ReceitaMoedaRow();

			List<ReceitaDealFiscalRow> receitaRowList = new ArrayList<ReceitaDealFiscalRow>();
			// Percorre lista de receita deal fiscal para montar VO.
			for (ReceitaDealFiscal rdf : this
					.loadReceitaDFiscalListByReceitaMoeda(receitaMoeda)) {

				if (rdf.getIndicadorStatus() != null && rdf.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_PENDENTE)) {
					Messages.showWarning("prepareManage",
							Constants.RECEITA_MSG_AVISO_PENDENTE);
					return null;
				}

				// DealFiscals com tipo de servi�o Licenca nao devem aparecer em
				// receitas de servico
				if (!rdf.getDealFiscal().getTipoServicos().get(0)
						.getSiglaTipoServico()
						.equals(SgTipoServico.LICE.getAbbreviation())) {

					if (rdf.getReceitaPlantao() == null) {
						rdf.setReceitaPlantao(new ReceitaPlantao(rdf));
					}

					receitaDealFiscalRow = new ReceitaDealFiscalRow();
					receitaDealFiscalRow.setTo(rdf);
					if(cp.getIndicadorMultiDealFiscal().equals(Constants.FLAG_YES)){
						receitaDealFiscalRow.setIsReceitaEditavel(Boolean.valueOf(false));
					}else {
						receitaDealFiscalRow.setIsReceitaEditavel(Boolean.valueOf(true));
					}
					receitaDealFiscalRow.setIsReceitaPlantaoEditavel(Boolean.valueOf(true));
					receitaRowList.add(receitaDealFiscalRow);
					listDealFiscal.add(rdf.getDealFiscal());
				}
			}

			this.loadDealFiscalList(listDealFiscal, receitaMoedaRow);

			// seta objeto para adiciona-lo na lista
			receitaMoedaRow.setShortTermRevenueRow(this
					.buildShortTermRevenueRow(receita.getContratoPratica(),
							receitaMoeda.getMoeda(), receita.getDataMes()));
			receitaMoedaRow.setReceitaMoeda(receitaMoeda);
			receitaMoedaRow.setRecDealFiscalRowList(receitaRowList);

			//Carregar e ordernar itens da receita
			List<ItemReceitaRow> list = receitaService.prepareUpdateListItemReceita(receitaMoeda);
			bean.getToItemReceitaRow().setComparableField(ItemReceitaRow.COLUMN_SITE);
			bean.setAscending(true);
			Collections.sort(list);
			receitaMoedaRow.setItemReceitaRowList(list);

			receitaMoedaRow.setAjusteReceitaRowList(this
					.loadAjusteReceitaRowList(receitaMoeda));
			receitaMoedaRowList.add(receitaMoedaRow);

			if (!beanSetado) {
				bean.setCurrentReceitaMoedaRow(receitaMoedaRow);
				bean.setNameTabMoedaRow(receitaMoeda.getMoeda().getNomeMoeda());
				beanSetado = Boolean.valueOf(true);
			}

		}

		bean.setReceitaMoedaRowList(receitaMoedaRowList);
		bean.setTo(receita);
		bean.setIsUpdate(Boolean.valueOf(true));
		ajusteReceitaBean.setFlagAbaAjusteReceita(Boolean.valueOf(true));
		bean.setNomeEtiquetaSelected("");

		ContratoPratica contratoPratica = contratoPraticaService
				.findContratoPraticaById(receita.getContratoPratica()
						.getCodigoContratoPratica());
		Msa msa = msaService.findMsaById(contratoPratica.getMsa()
				.getCodigoMsa());
		this.loadEtiquetaList(etiquetaService.findEtiquetaAtivaByCliente(msa
				.getCliente().getCodigoCliente()));

		bean.setLastPageId(bean.getCurrentPageId());
		verifyPriceTableStatusByMsa(msa);

		return OUTCOME_RECEITA_MANAGE;
	}

	/**
	 * Carrega lista de receitaDealFiscal (faz uniao das receitasDealFiscals ja
	 * persistidas com as de valor zero para edicao de receita).
	 *
	 * @param receitaMoeda
	 *            - receita Moeda
	 * @return listResult
	 */
	private List<ReceitaDealFiscal> loadReceitaDFiscalListByReceitaMoeda(
			final ReceitaMoeda receitaMoeda) {
		// Lista de ReceitaDealFiscal a ser retornada (ja preenchida com o que
		// ja existe no banco).

		List<ReceitaDealFiscal> listResult = receitaDealFiscalService
				.findReceitaDealFiscalByReceitaMoeda(receitaMoeda);
		// r.getReceitaDealFiscals();

		// lista total de deal fiscal
		List<DealFiscal> listTotalDealFiscal = dealFiscalService
				.findActiveDealFiscalByClobAndCurrency(receitaMoeda
						.getReceita().getContratoPratica(), receitaMoeda
						.getMoeda());

		// la�o para exluir da lista total de deal fiscal as receitadealfiscals
		// ja existentes no banco
		for (DealFiscal dealFiscal : listTotalDealFiscal) {

			// boolean para verificao de dealfiscal ja existe no banco.
			boolean jaExiste = false;

			for (ReceitaDealFiscal rdf : listResult) {
				// se ja existir dealfiscal sai do laco.
				if (dealFiscal.getCodigoDealFiscal().equals(
						rdf.getDealFiscal().getCodigoDealFiscal())) {
					jaExiste = true;
					break;
				}
			}

			// se nao existir, cria RDF e adiciona na lista a ser retornada.
			if (!jaExiste) {
				ReceitaDealFiscal rdf = new ReceitaDealFiscal();
				rdf.setDealFiscal(dealFiscal);
				rdf.setReceitaMoeda(receitaMoeda);
				rdf.setValorReceita(BigDecimal.valueOf(0));
				listResult.add(rdf);
			}

		}
		return listResult;
	}

	/**
	 * Insere uma entidade.
	 *
	 * @return pagina de destino
	 */
	public String create() {
		// habilita as mensagens de validacao
		messageConntrolBean.setDisplayMessages(Boolean.valueOf(true));

		// pega o objeto Receita que est� sendo criado
		Receita receita = bean.getTo();

		// pega a lista de ReceitaMoeda para ser populada
		List<ReceitaMoeda> recMoeList = receita.getReceitaMoedas();

		// indica se a Receita ser� salva no estado Draft
		Boolean isDraft = Boolean.FALSE;

		// indica primeira moeda.
		Boolean firstCurrency = Boolean.TRUE;

		// moedas sem nenhum deal
		List<String> moedasWarning = new ArrayList<String>();

		// pega a lista de ReceitaMoedaRow da tela (VOs)
		List<ReceitaMoedaRow> recMoeRowList = bean.getReceitaMoedaRowList();

		BigDecimal valorTotalReceitaDealFiscal = getValorTotalReceitaDealFiscal();

		// verifica se alguma moeda nao tem deal
		for (ReceitaMoedaRow recMoeRow : recMoeRowList) {
			if (recMoeRow.getRecDealFiscalRowList().isEmpty()) {
				isDraft = Boolean.TRUE;
				moedasWarning.add(recMoeRow.getReceitaMoeda().getMoeda()
						.getNomeMoeda());
			}
			if (Constants.YES.equals(receita.getContratoPratica().getIndicadorAprovaAjusteReceita()) && (
					recMoeRow.getTotalAmount().compareTo(valorTotalReceitaDealFiscal) != 0 ||
							recMoeRow.getValorReceitaAjustadoForecast().compareTo(valorTotalReceitaDealFiscal) != 0)) {
				Messages.showError("create",
						Constants.MSG_ERROR_RECEITA_TOTAL_NOT_MATCH,
						recMoeRow.getReceitaMoeda().getMoeda()
								.getNomeMoeda());

				// esconde as mensagens da Receita Moeda
				for (ReceitaMoedaRow recMoeRow1 : recMoeRowList) {
					recMoeRow1.setShowMessage(Boolean.FALSE);
				}

				// se a lista tem algum erro, retorna null
				return null;
			}
		}

		for (ReceitaMoedaRow recMoeRow : recMoeRowList) {
			// cria os objetos ReceitaMoeda para popular a lista
			ReceitaMoeda receitaMoeda = recMoeRow.getReceitaMoeda();

			// carrega a lista de ItemReceita para ser persistida no
			// banco a partir da lista da tela
			List<ItemReceita> itemReceitaList = receitaService
					.loadItemReceitaList(recMoeRow, isDraft, receita.getContratoPratica().getCodigoContratoPratica());

			List<ReceitaDealFiscal> recDealFiscalList = receitaService
					.loadReceitaDealFiscalList(recMoeRow);

			// Seta panel aberto na primeira moeda
			if (firstCurrency) {
				bean.setNameTabMoedaRow(receitaMoeda.getMoeda().getNomeMoeda());
				firstCurrency = Boolean.FALSE;
			}

			// se a lista foi "traduzida" corretamente
			if (itemReceitaList != null) {
				// verifica se o total da receitaMoeda eh igual ao total dos
				// deals
				if (this.validateTotalDeal(recMoeRow) || isDraft) {
					// atribui a lista de ItemReceita
					receitaMoeda.setItemReceitas(itemReceitaList);
					receitaMoeda.setReceitaDealFiscals(recDealFiscalList);
					receitaMoeda.setValorTotalMoeda(recMoeRow.getTotalReceitaDealFiscal());
					Long defaultCodigoMoeda = Long.valueOf(
							systemProperties.getProperty(Constants.DEFAULT_PROPERTY_CURRENCY_CODE));
					Moeda moeda = receitaMoeda.getMoeda();
					if (moeda.getCodigoMoeda() != defaultCodigoMoeda) {
						receitaMoeda.setCotacaoMoeda(
								cotacaoMoedaService.findCotacaoMoedaByMonth(moeda, receita.getDataMes()));
					} else {
						receitaMoeda.setCotacaoMoeda(null);
					}
				} else {
					Messages.showError("create",
							Constants.MSG_ERROR_RECEITA_TOTAL_NOT_MATCH,
							recMoeRow.getReceitaMoeda().getMoeda().getNomeMoeda());

					// esconde as mensagens da Receita Moeda
					for (ReceitaMoedaRow recMoeRow1 : recMoeRowList) {
						recMoeRow1.setShowMessage(Boolean.FALSE);
					}

					// se a lista tem algum erro, retorna null
					return null;
				}
			}else {
				//itemReceita nao foi traduzida corretamente, houver alguma inconsistencia e não deve prosseguir
				return null;
			}


			recMoeList.add(receitaMoeda);
		}

		receita.setCodigoReceita(null);

		// seta a receita como draft
		if (isDraft) {
			receita.setIndicadorVersao(Constants.VERSION_RECEITA_DRAFT);
		}

		// cria o Receita
		receitaService.createReceita(receita);

		for (ReceitaMoedaRow recMoeRow : recMoeRowList) {
			// cria as Etiqueta
			etiquetaService.applyEtiquetaForItemReceitaRowList(recMoeRow.getItemReceitaRowList());
		}

		// mostra a mensagem correta
		if (isDraft) {
			if (moedasWarning.size() == 1) {
				Messages.showSucess("create",
						Constants.MSG_SUCCESS_RECEITA_TOTAL_NOT_MATCH,
						moedasWarning.get(0));
			} else {
				String moedasString = "";
				for (Iterator<String> iterator = moedasWarning.iterator(); iterator.hasNext();) {
					String moedaWarning = iterator.next();
					moedasString += moedaWarning;
					if (iterator.hasNext()) {
						moedasString += ", ";
					}
				}

				Messages.showSucess("create",
						Constants.MSG_SUCCESS_RECEITA_MANY_TOTAL_NOT_MATCH,
						moedasString);
			}
		} else {
			// mensagem de sucesso
			Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
					Constants.ENTITY_NAME_RECEITA);
		}

		// faz a busca novamente para recarregar a lista da pesquisa
		bean.setCurrentRowId(receita.getCodigoReceita());
		return this.prepareManage();
	}

	private BigDecimal getValorTotalReceitaDealFiscal() {
		BigDecimal valorTotalReceitaDealFiscal = BigDecimal.ZERO;
		for (ReceitaDealFiscalRow recDealFiscalRow : bean.getCurrentReceitaMoedaRow().getRecDealFiscalRowList()) {
			valorTotalReceitaDealFiscal = valorTotalReceitaDealFiscal.add(recDealFiscalRow.getTo().getValorReceita());
		}
		return valorTotalReceitaDealFiscal.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Prepara a tela de Edicao Estratificacao da UR.
	 *
	 * @return pagina de destino
	 */
	public String prepareViewReviewUr() {
		mapaAlocacaoBean.setIsViewReviewUr(Boolean.valueOf(true));
		mapaAlocacaoBean.setIsUpdateReviewUr(Boolean.valueOf(false));
		mapaAlocacaoBean.setIsRemove(Boolean.valueOf(false));

		this.loadReview();

		return OUTCOME_RECEITA_REVIEW_UR;
	}

	/**
	 * Prepara a tela de Edicao Estratificacao da UR.
	 *
	 * @return pagina de destino
	 */
	public String prepareRemoveReviewUr() {
		mapaAlocacaoBean.setIsRemove(Boolean.valueOf(true));
		mapaAlocacaoBean.setIsViewReviewUr(Boolean.valueOf(true));
		mapaAlocacaoBean.setIsUpdateReviewUr(Boolean.valueOf(false));

		this.loadReview();

		return OUTCOME_RECEITA_REVIEW_UR;
	}

	/**
	 * Carrega os campos do Stratification.
	 */
	private void loadReview() {
		// Pega a data (mes/ano)
		Date monthDate = DateUtil.getDate(
				mapaAlocacaoBean.getValidityMonthBeg(),
				mapaAlocacaoBean.getValidityYearBeg());

		MapaAlocacao mapaAlocacao = mapaAlocacaoService
				.findMapaAlocacaoById(mapaAlocacaoBean.getCurrentRowId());

		ContratoPratica contratoPratica = contratoPraticaService
				.findContratoPraticaById(mapaAlocacao.getContratoPratica()
						.getCodigoContratoPratica());

		updateItensUrReview(contratoPratica, monthDate);

		EstratificacaoUr estratificacaoUr = estratificacaoUrService
				.findEstratificacaoUrByIdMapaAlocacaoData(
						mapaAlocacaoBean.getCurrentRowId(), monthDate);

		mapaAlocacaoBean.setTo(estratificacaoUr.getMapaAlocacao());

		mapaAlocacaoBean.setItensExtratificacaoUr(estratificacaoUr
				.getItemEstratificacaoUrs());

		// calcula o total da diferenca
		float totalAlocado = 0;
		float totalReceitado = 0;
		for (Iterator<ItemEstratificacaoUr> iterator = mapaAlocacaoBean
				.getItensExtratificacaoUr().iterator(); iterator.hasNext();) {
			ItemEstratificacaoUr itemEstratificacaoUr = iterator.next();

			float fteReceitado = itemEstratificacaoUr.getNumeroFteReceitado()
					.setScale(2, RoundingMode.HALF_UP).floatValue();
			float fteAlocado = itemEstratificacaoUr.getNumeroFteAlocado()
					.setScale(2, RoundingMode.HALF_UP).floatValue();

			totalReceitado += fteReceitado;
			totalAlocado += fteAlocado;

		}
		mapaAlocacaoBean.setTotalAlocado(new BigDecimal(totalAlocado).setScale(
				2, RoundingMode.HALF_UP));
		mapaAlocacaoBean.setTotalReceitado(new BigDecimal(totalReceitado)
				.setScale(2, RoundingMode.HALF_UP));

		mapaAlocacaoBean.setTotalDifference(new BigDecimal(totalReceitado
				- totalAlocado).setScale(2, RoundingMode.HALF_UP));

		BigDecimal totalDiffereceOld = estratificacaoUr.getNumeroTotalDesvio()
				.setScale(2, RoundingMode.HALF_UP);
		estratificacaoUr.setNumeroTotalDesvio(mapaAlocacaoBean
				.getTotalDifference());

		if (!totalDiffereceOld.equals(estratificacaoUr.getNumeroTotalDesvio())) {
			estratificacaoUrService.updateEstratificacaoUr(estratificacaoUr);
		}
	}

	/**
	 * Prepara a tela de Edicao Estratificacao da UR.
	 *
	 * @return pagina de destino
	 */
	public String prepareEditReviewUr() {

		mapaAlocacaoBean.setIsUpdateReviewUr(Boolean.TRUE);
		mapaAlocacaoBean.setIsViewReviewUr(Boolean.FALSE);

		this.loadReview();

		mapaAlocacaoBean.setExplicacaoDesvioApply(mapaAlocacaoBean
				.getExplicacaoDesvioDefault().getNomeMotivoDesvio());

		return OUTCOME_RECEITA_REVIEW_UR;
	}

	/**
	 * Atualiza os itens da estratificacao.
	 *
	 * @param contratoPratica
	 *            - contratoPratica da estratificacao
	 * @param dataMes
	 *            - mes da estratificacao
	 */
	private void updateItensUrReview(final ContratoPratica contratoPratica,
									 final Date dataMes) {

		List<VwEstratificacaoUrResultado> listEstratificacaoUrResultado = estratificacaoUrResultadoService
				.findEstratificacaoUrResultadoByContratoPraticaAndDataMes(
						contratoPratica, dataMes);

		EstratificacaoUr estratificacaoUr = estratificacaoUrService
				.findEstratificacaoUrByIdMapaAlocacaoData(
						mapaAlocacaoBean.getCurrentRowId(), dataMes);

		Long codigoUndefined = new Long(
				systemProperties.getProperty("explicacao.undefined.code"));

		List<ItemEstratificacaoUr> listItemEstratificacaoUr = new ArrayList<ItemEstratificacaoUr>();

		EstratificacaoUr newEstratificacaoUr = new EstratificacaoUr();

		for (VwEstratificacaoUrResultado estratificacaoUrResultado : listEstratificacaoUrResultado) {

			if (estratificacaoUrResultado != null) {

				ItemEstratificacaoUr itemEstratificacaoUr = new ItemEstratificacaoUr();

				itemEstratificacaoUr.setEstratificacaoUr(newEstratificacaoUr);

				itemEstratificacaoUr
						.setValorDiferenca(estratificacaoUrResultado
								.getValorDiferencaReal());
				itemEstratificacaoUr
						.setNumeroFteAlocado(estratificacaoUrResultado
								.getValorTotalAlocado());
				itemEstratificacaoUr
						.setNumeroFteReceitado(estratificacaoUrResultado
								.getValorTotalReceitado());

				itemEstratificacaoUr.setPessoa(pessoaService
						.findPessoaById(estratificacaoUrResultado.getId()
								.getCodigoPessoa()));

				Long codigoExplicacaoDesvio = estratificacaoUrResultado
						.getCodigoMotivoDesvio();

				if (estratificacaoUrResultado.getIndicadorInconsistente()
						.equals('S')) {

					itemEstratificacaoUr
							.setExplicacaoDesvio(explicacaoDesvioService
									.findExplicacaoDesvioById(codigoUndefined));
				} else if (codigoExplicacaoDesvio != null) {
					itemEstratificacaoUr
							.setExplicacaoDesvio(explicacaoDesvioService
									.findExplicacaoDesvioById(codigoExplicacaoDesvio));
				} else {
					if (estratificacaoUrResultado
							.getIndicadorDiferencaRealNeg().equals('S')) {
						itemEstratificacaoUr
								.setExplicacaoDesvio(explicacaoDesvioService
										.findExplicacaoDesvioById(codigoUndefined));
					}
					itemEstratificacaoUr.setExplicacaoDesvio(null);
				}

				listItemEstratificacaoUr.add(itemEstratificacaoUr);
			}
		}

		newEstratificacaoUr.setCodigoLoginCriador(estratificacaoUr
				.getCodigoLoginCriador());
		newEstratificacaoUr.setDataCriacao(estratificacaoUr.getDataCriacao());
		newEstratificacaoUr.setDataAtualizacao(estratificacaoUr
				.getDataAtualizacao());
		newEstratificacaoUr.setDataMes(estratificacaoUr.getDataMes());
		newEstratificacaoUr.setMapaAlocacao(estratificacaoUr.getMapaAlocacao());
		newEstratificacaoUr.setReceita(estratificacaoUr.getReceita());
		newEstratificacaoUr.setNumeroTotalDesvio(estratificacaoUr
				.getNumeroTotalDesvio());
		newEstratificacaoUr.setItemEstratificacaoUrs(listItemEstratificacaoUr);
		estratificacaoUrService.removeEstratificacaoUr(estratificacaoUr);
		estratificacaoUrService.createEstratificacaoUr(newEstratificacaoUr);
	}

	/**
	 * Popula o combobox do contrato-pratica de acordo com o cliente
	 * selecionado.
	 *
	 * @param e
	 *            - evento de mudanca
	 */
	public void prepareContratoPraticaCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null && !"".equals(value)) {
			Long codCliente = mapaAlocacaoBean.getClienteMap().get(value);
			List<ContratoPratica> contratosPraticas = new ArrayList<ContratoPratica>();

			// se o centro de lucro existir
			if (codCliente != null) {
				Cliente cliente = new Cliente();
				cliente.setCodigoCliente(codCliente);
				contratosPraticas = contratoPraticaService
						.findContratoPraticaByCliente(cliente);
				this.loadContratoPraticaListMapa(contratosPraticas);
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadContratoPraticaListMapa(new ArrayList<ContratoPratica>());
			}
		} else {
			this.loadContratoPraticaListMapa(contratoPraticaService
					.findContratoPraticaAllComplete());
		}
	}

	/**
	 * Edita o motivo do desvio da estratificacao.
	 *
	 * @param e
	 *            - evento de mudanca
	 */
	public void editMotivo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletRequest myRequest = (HttpServletRequest) facesContext
				.getExternalContext().getRequest();
		String login = myRequest.getParameter("login");

		if (value != null && !"".equals(value)) {

			Long codigoMotivoDesvio = mapaAlocacaoBean
					.getExplicacoesDesvioMap().get(value);

			ExplicacaoDesvio explicacaoDesvio = new ExplicacaoDesvio();

			explicacaoDesvio = explicacaoDesvioService
					.findExplicacaoDesvioById(codigoMotivoDesvio);

			for (ItemEstratificacaoUr itemEstratificacaoUr : mapaAlocacaoBean
					.getItensExtratificacaoUr()) {
				if (itemEstratificacaoUr.getPessoa().getCodigoLogin()
						.equals(login)) {
					itemEstratificacaoUr.setExplicacaoDesvio(explicacaoDesvio);
				}
			}
		}
	}

	/**
	 * Prepara a tela de Estratificacao da UR.
	 *
	 * @return pagina de destino
	 */
	public String prepareReviewUr() {

		final Map<Long, String> companyErp;
		try {
			companyErp = companyErpService.findAllActive();
		} catch (final BusinessException e) {
			Messages.showError("prepareReviewUr", e.getMessage());
			return "";
		}

		mapaAlocacaoBean.setIsUpdateReviewUr(Boolean.FALSE);
		mapaAlocacaoBean.setIsViewReviewUr(Boolean.FALSE);

		mapaAlocacaoBean
				.setItensExtratificacaoUr(new ArrayList<ItemEstratificacaoUr>());

		// busca o mapa de alocacao corrente pelo c�digo
		findMapaById(mapaAlocacaoBean.getCurrentRowId());

		// Pega a data (mes/ano)
		Date monthDate = DateUtil.getDate(
				mapaAlocacaoBean.getValidityMonthBeg(),
				mapaAlocacaoBean.getValidityYearBeg());

		List<Alocacao> alocacoes = mapaAlocacaoBean.getTo().getAlocacaos();

		for (Iterator<Alocacao> iteratorAlocacao = alocacoes.iterator(); iteratorAlocacao
				.hasNext();) {

			Alocacao alocacao = iteratorAlocacao.next();

			List<AlocacaoPeriodo> alocacoesPeriodos = new ArrayList<AlocacaoPeriodo>(
					alocacao.getAlocacaoPeriodos());

			for (Iterator<AlocacaoPeriodo> iterator = alocacoesPeriodos
					.iterator(); iterator.hasNext();) {
				AlocacaoPeriodo alocacaoPeriodo = iterator.next();

				ItemEstratificacaoUr itemEstratificacaoUr = new ItemEstratificacaoUr();

				if (alocacaoPeriodo.getDataAlocacaoPeriodo().compareTo(
						monthDate) == 0) {

					Pessoa pessoa = pessoaService.findPessoaByRecursoTipo(
							alocacaoPeriodo.getAlocacao().getRecurso(), "PE");

					if (pessoa != null) {
						itemEstratificacaoUr.setPessoa(pessoa);

						itemEstratificacaoUr
								.setNumeroFteAlocado(BigDecimal.ZERO
										.setScale(2));
						itemEstratificacaoUr
								.setNumeroFteReceitado(BigDecimal.ZERO
										.setScale(2));

						itemEstratificacaoUr
								.setNumeroFteAlocado(alocacaoPeriodo
										.getPercentualAlocacaoPeriodo()
										.setScale(2, RoundingMode.HALF_UP));

						ExplicacaoDesvio explicacaoDesvio = mapaAlocacaoBean
								.getExplicacaoDesvioDefault();

						itemEstratificacaoUr
								.setExplicacaoDesvio(explicacaoDesvio);

						// verifica se a pessoa ja esta na lista e unifica os
						// FTE
						boolean existe = false;
						for (Iterator<ItemEstratificacaoUr> iteratorEstratificacao = mapaAlocacaoBean
								.getItensExtratificacaoUr().iterator(); iteratorEstratificacao
									 .hasNext();) {
							ItemEstratificacaoUr itemEstratificacaoUr2 = iteratorEstratificacao
									.next();

							if (itemEstratificacaoUr2.getPessoa()
									.getCodigoPessoa() == itemEstratificacaoUr
									.getPessoa().getCodigoPessoa()) {
								existe = true;
								itemEstratificacaoUr2
										.setNumeroFteAlocado(itemEstratificacaoUr2
												.getNumeroFteAlocado()
												.add(itemEstratificacaoUr
														.getNumeroFteAlocado()
														.setScale(
																2,
																RoundingMode.HALF_UP))
												.setScale(2,
														RoundingMode.HALF_UP));
								itemEstratificacaoUr2
										.setNumeroFteReceitado(itemEstratificacaoUr2
												.getNumeroFteReceitado()
												.add(itemEstratificacaoUr
														.getNumeroFteReceitado()
														.setScale(
																2,
																RoundingMode.HALF_UP))
												.setScale(2,
														RoundingMode.HALF_UP));
							}
						}
						if (!existe) {
							mapaAlocacaoBean.getItensExtratificacaoUr().add(
									itemEstratificacaoUr);
						}
					} else {
						continue;
					}
				}
			}
		}

		// Busca as receitas do mesmo Contract-LOB e data
		Receita receita = (findReceitaByMapaData(mapaAlocacaoBean.getTo(),
				monthDate));
		bean.setTo(receita);

		if (receita != null) {
			List<ItemReceita> itensReceita = itemReceitaService
					.findItemReceitaByReceita(receita);

			// Verifica se a pessoa ja esta na lista
			boolean existe = false;
			// Seta os valores de FTE Receitado para as pessoas da lista
			for (Iterator<ItemReceita> iteratorReceita = itensReceita
					.iterator(); iteratorReceita.hasNext();) {
				ItemReceita itemReceita = iteratorReceita.next();

				for (Iterator<ItemEstratificacaoUr> iteratorEstratificacao = mapaAlocacaoBean
						.getItensExtratificacaoUr().iterator(); iteratorEstratificacao
							 .hasNext();) {
					ItemEstratificacaoUr itemEstratificacaoUr = iteratorEstratificacao
							.next();

					if (itemReceita.getPessoa().getCodigoPessoa() == itemEstratificacaoUr
							.getPessoa().getCodigoPessoa()) {
						existe = true;

						itemEstratificacaoUr
								.setNumeroFteReceitado(itemEstratificacaoUr
										.getNumeroFteReceitado()
										.add(itemReceita.getNumeroFte()
												.setScale(2,
														RoundingMode.HALF_UP)));
					}
				}

				// Se a pessoa nao esta na lista, adiciona com FTE Alocado igual
				// a 0
				if (!existe) {

					ItemEstratificacaoUr itemEstratificacaoUr = new ItemEstratificacaoUr();
					itemEstratificacaoUr.setNumeroFteAlocado(BigDecimal.ZERO
							.setScale(2, RoundingMode.HALF_UP));
					itemEstratificacaoUr.setNumeroFteReceitado(itemReceita
							.getNumeroFte().setScale(2, RoundingMode.HALF_UP));
					itemEstratificacaoUr.setPessoa(itemReceita.getPessoa());
					itemEstratificacaoUr
							.setExplicacaoDesvio(new ExplicacaoDesvio());

					mapaAlocacaoBean.getItensExtratificacaoUr().add(
							itemEstratificacaoUr);
				}
			}
		}

		// calcula o total da diferenca
		float totalDifference = 0;
		float totalAlocado = 0;
		float totalReceitado = 0;
		for (Iterator<ItemEstratificacaoUr> iterator = mapaAlocacaoBean
				.getItensExtratificacaoUr().iterator(); iterator.hasNext();) {
			ItemEstratificacaoUr itemEstratificacaoUr = iterator.next();

			float fteReceitado = itemEstratificacaoUr.getNumeroFteReceitado()
					.setScale(2, RoundingMode.HALF_UP).floatValue();
			float fteAlocado = itemEstratificacaoUr.getNumeroFteAlocado()
					.setScale(2, RoundingMode.HALF_UP).floatValue();

			totalReceitado += fteReceitado;
			totalAlocado += fteAlocado;
			float difference = fteReceitado - fteAlocado;

			itemEstratificacaoUr.setValorDiferenca(new BigDecimal(difference)
					.setScale(2, RoundingMode.HALF_UP));

			totalDifference += difference;

		}
		mapaAlocacaoBean.setTotalAlocado(new BigDecimal(totalAlocado).setScale(
				2, RoundingMode.HALF_UP));
		mapaAlocacaoBean.setTotalReceitado(new BigDecimal(totalReceitado)
				.setScale(2, RoundingMode.HALF_UP));
		mapaAlocacaoBean.setTotalDifference(new BigDecimal(totalDifference)
				.setScale(2, RoundingMode.HALF_UP));

		mapaAlocacaoBean.setExplicacaoDesvioApply(mapaAlocacaoBean
				.getExplicacaoDesvioDefault().getNomeMotivoDesvio());

		Boolean isRevenueClosed = receitaService
				.verifyClosingDateRevenueCompareTo(monthDate) >= 0;

		Boolean isInternationalRevenueClosed = receitaService
				.verifyClosingDateInternationalRevenueCompareTo(monthDate) >= 0;

		if (bean.getTo() == null) {
			Messages.showWarning("prepareReviewUr",
					Constants.MSG_ERROR_RECEITA_NAO_PUBLICADA_INTEGRADA);
		} else if (receitaService.isInternationalRevenue(companyErp, receita)
				&& !isInternationalRevenueClosed) {
			Messages.showWarning("prepareReviewUr",
					Constants.MSG_ERROR_INTERNATIONAL_REVENUE_CLOSING_DATE);
		} else if (!isRevenueClosed) {
			Messages.showWarning("prepareReviewUr",
					Constants.MSG_ERROR_REVENUE_CLOSING_DATE);
		}

		return OUTCOME_RECEITA_REVIEW_UR;
	}

	/**
	 * Aplica a explicacao para todos os itens de estratificacao selecionados.
	 */
	public void applyExplicacao() {

		Long codigoMotivoDesvio = mapaAlocacaoBean.getExplicacoesDesvioMap()
				.get(mapaAlocacaoBean.getExplicacaoDesvioApply());

		ExplicacaoDesvio explicacaoDesvio = new ExplicacaoDesvio();

		explicacaoDesvio = explicacaoDesvioService
				.findExplicacaoDesvioById(codigoMotivoDesvio);

		List<ItemEstratificacaoUr> itensEstratificacaoUr = mapaAlocacaoBean
				.getItensExtratificacaoUr();
		for (ItemEstratificacaoUr itemEstratificacaoUr : itensEstratificacaoUr) {
			if (itemEstratificacaoUr.getIsSelected()) {
				itemEstratificacaoUr.setExplicacaoDesvio(explicacaoDesvio);
			}
		}
	}

	/**
	 * Salva as modificacoes da tela de Estratificacao da UR.
	 *
	 * @return pagina de destino
	 */
	public String saveEditReviewUr() {

		// Pega a data (mes/ano)
		Date monthDate = DateUtil.getDate(
				mapaAlocacaoBean.getValidityMonthBeg(),
				mapaAlocacaoBean.getValidityYearBeg());

		EstratificacaoUr estratificacaoUr = estratificacaoUrService
				.findEstratificacaoUrByIdMapaAlocacaoData(
						mapaAlocacaoBean.getCurrentRowId(), monthDate);
		estratificacaoUr.setDataAtualizacao(new Date());

		List<ItemEstratificacaoUr> itensExtratificacaoUr = mapaAlocacaoBean
				.getItensExtratificacaoUr();
		for (ItemEstratificacaoUr item : itensExtratificacaoUr) {
			item.setEstratificacaoUr(estratificacaoUr);

			float fteReceitado = item.getNumeroFteReceitado().floatValue();
			float fteAlocado = item.getNumeroFteAlocado().floatValue();

			ExplicacaoDesvio explicacaoDesvio = new ExplicacaoDesvio();

			if (fteReceitado - fteAlocado < 0) {
				Long codigoMotivoDesvio = mapaAlocacaoBean
						.getExplicacoesDesvioMap().get(
								item.getExplicacaoDesvio()
										.getNomeMotivoDesvio());

				explicacaoDesvio = explicacaoDesvioService
						.findExplicacaoDesvioById(codigoMotivoDesvio);
			} else {
				explicacaoDesvio = null;
			}

			item.setExplicacaoDesvio(explicacaoDesvio);
		}

		estratificacaoUr.setItemEstratificacaoUrs(itensExtratificacaoUr);

		estratificacaoUrService.updateEstratificacaoUr(estratificacaoUr);

		Messages.showSucess("saveEditReviewUr",
				Constants.MSG_SUCCESS_UPDATE_REVIEW);

		return OUTCOME_RECEITA_REVIEW_UR;
	}

	/**
	 * Salva as modificacoes da tela de Estratificacao da UR.
	 *
	 * @return pagina de destino
	 */
	public String saveReviewUr() {

		final Map<Long, String> companyErp;
		try {
			companyErp = companyErpService.findAllActive();
		} catch (final BusinessException e) {
			Messages.showError("saveReviewUr", e.getMessage());
			return "";
		}

		// Pega a data (mes/ano)
		Date monthDate = DateUtil.getDate(
				mapaAlocacaoBean.getValidityMonthBeg(),
				mapaAlocacaoBean.getValidityYearBeg());

		Boolean isRevenueClosed = receitaService
				.verifyClosingDateRevenueCompareTo(monthDate) >= 0;

		Boolean isInternationalRevenueClosed = receitaService
				.verifyClosingDateInternationalRevenueCompareTo(monthDate) >= 0;

		if (bean.getTo() != null
				&& isRevenueClosed
				&& (isInternationalRevenueClosed && receitaService
						.isInternationalRevenue(companyErp, bean.getTo()))) {

			mapaAlocacaoBean.setIsUpdateReviewUr(Boolean.FALSE);

			EstratificacaoUr estratificacaoUr = new EstratificacaoUr();

			Date dataCriacao = new Date();

			estratificacaoUr.setDataCriacao(dataCriacao);
			estratificacaoUr.setDataAtualizacao(dataCriacao);
			estratificacaoUr.setDataMes(monthDate);
			estratificacaoUr.setNumeroTotalDesvio(mapaAlocacaoBean
					.getTotalDifference());
			estratificacaoUr.setMapaAlocacao(mapaAlocacaoBean.getTo());
			estratificacaoUr.setReceita(bean.getTo());

			estratificacaoUr.setCodigoLoginCriador(LoginUtil
					.getLoggedUsername());

			List<ItemEstratificacaoUr> itensExtratificacaoUr = mapaAlocacaoBean
					.getItensExtratificacaoUr();
			for (ItemEstratificacaoUr item : itensExtratificacaoUr) {
				item.setEstratificacaoUr(estratificacaoUr);

				float fteReceitado = item.getNumeroFteReceitado().floatValue();
				float fteAlocado = item.getNumeroFteAlocado().floatValue();

				ExplicacaoDesvio explicacaoDesvio = new ExplicacaoDesvio();

				if (fteReceitado - fteAlocado < 0) {
					Long codigoMotivoDesvio = mapaAlocacaoBean
							.getExplicacoesDesvioMap().get(
									item.getExplicacaoDesvio()
											.getNomeMotivoDesvio());

					explicacaoDesvio = explicacaoDesvioService
							.findExplicacaoDesvioById(codigoMotivoDesvio);
				} else {
					explicacaoDesvio = null;
				}

				item.setExplicacaoDesvio(explicacaoDesvio);
			}

			estratificacaoUr.setItemEstratificacaoUrs(itensExtratificacaoUr);

			estratificacaoUrService.createEstratificacaoUr(estratificacaoUr);

			Messages.showSucess("saveReviewUr",
					Constants.MSG_SUCCESS_CREATE_REVIEW, estratificacaoUr
							.getMapaAlocacao().getContratoPratica()
							.getNomeContratoPratica());

			mapaAlocacaoBean.setIsUpdateReviewUr(Boolean.TRUE);
		} else {
			if (bean.getTo() == null) {
				Messages.showError("saveReviewUr",
						Constants.MSG_ERROR_RECEITA_NAO_PUBLICADA_INTEGRADA);
			} else if (!isRevenueClosed) {
				Messages.showError("saveReviewUr",
						Constants.MSG_ERROR_REVENUE_CLOSING_DATE);
			} else if (!isInternationalRevenueClosed) {
				Messages.showError("saveReviewUr",
						Constants.MSG_ERROR_INTERNATIONAL_REVENUE_CLOSING_DATE);
			}
		}

		return OUTCOME_RECEITA_REVIEW_UR;
	}

	/**
	 * Prepara a aba e carrega a lista de HistoricoReceita.
	 *
	 */
	public void prepareHistoricoReceita() {
		// pega a entidade Receita
		Receita receita = receitaService.findReceitaById(bean.getTo()
				.getCodigoReceita());
		List<HistoricoReceita> historicoReceitaList = receita
				.getHistoricoReceitas();
		orderHistoricoReceitaList(historicoReceitaList);
		bean.setHistoricoReceitaList(historicoReceitaList);
	}

	/**
	 * Ordena a lista de HistoricoReceita.
	 *
	 * @param historicoReceitaList
	 *            - lista de HistoricoReceita da Receita corrente
	 */
	private void orderHistoricoReceitaList(
			final List<HistoricoReceita> historicoReceitaList) {
		Collections.sort(historicoReceitaList,
				new Comparator<HistoricoReceita>() {
					public int compare(final HistoricoReceita hr1,
									   final HistoricoReceita hr2) {
						return hr1.getDataAlteracao().compareTo(
								hr2.getDataAlteracao());
					}
				});
	}

	/**
	 * Realiza a validacao do total da receitao com o total dos deals.
	 *
	 * @param receitaMoedaRow
	 *            - ReceitaMoedaRow que deve ter os valores validados
	 *
	 * @return true se validado com sucesso, caso contrario false.
	 */
	private Boolean validateTotalDeal(final ReceitaMoedaRow receitaMoedaRow) {
		Double totalAmount = receitaMoedaRow.getTotalAmount().doubleValue();

		Double totalDeals = receitaMoedaRow.getTotalReceitaDealFiscal()
				.doubleValue();

		return totalAmount.compareTo(totalDeals) == 0;
	}

	/**
	 * Checa se deve ser exibida a justificativa e prepara a lista de
	 * ShortTermRevenue a ser preenchida (justificada) pelo usuario.
	 *
	 * @param receitaMoedaRowList
	 *            lista com as receitas moeda a serem checadas
	 *
	 * @return boolean retorna true caso deve exibir a tela de justificativa
	 */
	private boolean checkJustificativa(
			final List<ReceitaMoedaRow> receitaMoedaRowList) {

		bean.setShortTermRevenueRowList(null);

		List<ShortTermRevenueRow> shortTermRevenueRowList = new ArrayList<ShortTermRevenueRow>();

		ShortTermRevenueRow str;
		ReceitaResultado rr;

		// se a receita estiver publicada ou for acao de publicar, entao valida
		if (Constants.VERSION_RECEITA_PUBLISHED.equals(bean.getTo()
				.getIndicadorVersao()) || bean.getIsJustificativaByPublish()) {

			// percorre todas as receitas moedas
			for (ReceitaMoedaRow rmr : receitaMoedaRowList) {

				ReceitaResultado filter = new ReceitaResultado();
				filter.setContratoPratica(bean.getTo().getContratoPratica());
				filter.setDataMes(bean.getTo().getDataMes());
				filter.setMoeda(rmr.getReceitaMoeda().getMoeda());

				rr = receitaResultadoService
						.findReceitaResultadoByContratoAndMoedaAndDate(filter);

				// se nao existir foto entao deve ser justificado
				if (rr == null) {

					// cria um ReceitaResultado (nao tem foto anterior)
					rr = new ReceitaResultado();
					rr.setContratoPratica(bean.getTo().getContratoPratica());
					rr.setDataMes(bean.getTo().getDataMes());
					rr.setMoeda(rmr.getReceitaMoeda().getMoeda());
					rr.setValorReceitaPlanejada(BigDecimal.valueOf(0));
					rr.setValorReceitaRealizada(rmr.getTotalReceitaDealFiscal());

					// setta o receita resultado na lista
					str = new ShortTermRevenueRow();
					str.setReceitaResultado(rr);
					str.setPercentualDiferenca(receitaService
							.getPercentualDiferenca(
									rr.getValorReceitaPlanejada(),
									rr.getValorReceitaRealizada()));

					shortTermRevenueRowList.add(str);
				} else {
					// se o valor da receita exceder o percentual permitido deve
					// ser justificado (encontrou foto)
					if (receitaService.excedePercentualToleravel(
							rmr.getTotalReceitaDealFiscal(),
							rr.getValorReceitaPlanejada())) {

						rr.setValorReceitaRealizada(rmr
								.getTotalReceitaDealFiscal());
						str = new ShortTermRevenueRow();
						// caso j� possua jutificativa anteiror traz o motivo
						MotivoResultado motivoResultado = null;
						if (rr.getMotivoResultado() != null) {
							Long motivo = bean.getMotivoResultadoMap().get(
									rmr.getShortTermRevenueRow()
											.getMotivoResultadoSelected());
							motivoResultado = motivoResultadoService
									.findMotivoResultadoById(motivo);
							str.setMotivoResultadoSelected(motivoResultado
									.getNomeMotivoResultado());
							rr.setMotivoResultado(motivoResultado);
						}
						// setta o receita resultado na lista
						str.setReceitaResultado(rr);
						str.setPercentualDiferenca(receitaService
								.getPercentualDiferenca(
										rr.getValorReceitaPlanejada(),
										rr.getValorReceitaRealizada()));

						shortTermRevenueRowList.add(str);
					}
				}
			}
		}

		// setta o bean com todos os ShortTermRevenue de cada receita moeda que
		// deve ser justificada
		bean.setShortTermRevenueRowList(shortTermRevenueRowList);

		return !shortTermRevenueRowList.isEmpty();
	}

	/**
	 * Prepara a tela de justificativa.
	 */
	private void prepareJustificativa() {
		bean.resetJustificativaFields();
		// popula o combo com os motivos
		this.loadMotivoResultadoMapa(motivoResultadoService
				.findMotivoResultadoAllAtivos());
	}

	/**
	 * Cancela a tela de justificativa e volta para a tela receita manage.
	 *
	 * @return OUTCOME_RECEITA_MANAGE
	 */
	public String cancelJustificativa() {
		return OUTCOME_RECEITA_MANAGE;
	}

	/**
	 * Realiza a copia de um ContratoPratica para a versao 'Publish'.
	 *
	 * @return true caso publicado
	 */
	public Boolean publish() {

		PrometheusMetrics publishRevenueMetrics = new PrometheusMetrics("pms.publish.revenue");
		// prometheus - Conta quantas receitas vão ser publicadas
		publishRevenueMetrics.incrementCounter();

		//Verifica se algum fiscal deal estiver inativo, nao permitir publicar receita
		if (Boolean.FALSE.equals(validateDealFiscal())) {
			return Boolean.valueOf(false);
		}

		// testa se o calculo do imposto estao ok. Caso nao encontre TaxaImposto
		// para a Empresa e TipoServico, retorna false e nao publica a Receita
		if (!this.calculateImposto()) {
			return Boolean.valueOf(false);
		}

		// valida se j� existe a Receita (ContratoPratica x M�s)
		ContratoPratica contratoPratica = contratoPraticaService
				.findContratoPraticaByName(bean.getTo().getContratoPratica()
						.getNomeContratoPratica());

		List<AjusteReceitaForecast> ajusteReceitaForecastList = getAjusteReceitaForecastByContratoPratica(bean.getTo().getContratoPratica());

		if (contratoPratica.getIndicadorAprovaAjusteReceita().equals(Constants.YES) &&
				(ajusteReceitaForecastList.isEmpty() || !isAjusteReceitaForecastApproved(ajusteReceitaForecastList.get(0)))) {
			Messages.showError("preparePublish", Constants.MSG_ERROR_INVAL_RECEITA_PUBLISH);
			return Boolean.FALSE;
		}

		bean.getTo().setIndicadorVersao(Constants.VERSION_PUBLISHED);
		// altera no banco
		this.update();

		// Atualiza a receita resultado
		this.saveJustificativaReceita();

		messageConntrolBean.setDisplayMessages(Boolean.TRUE);
		bean.setShowModalConfirmation(Boolean.valueOf(false));

		return Boolean.valueOf(true);
	}

	private boolean validateDealFiscal(){
		List<CpraticaDfiscal> deals = cpraticaDfiscalService.findByContratoPratica(bean.getTo().getContratoPratica());
		Boolean validated = Boolean.TRUE;

		for (CpraticaDfiscal cpdf : deals){
			DealFiscal df = cpdf.getDealFiscal();

			if(df.getIndicadorStatus().equals(Constants.INACTIVE)){
				Messages.showError("updateAjusteReceita",
						Constants.MSG_ERROR_DEALFISCAL_INATIVO);
				validated = Boolean.FALSE;
			}
			if (df.getCliente().getIndicadorAtivo().equalsIgnoreCase(Constants.INACTIVE)) {
				Messages.showError("updateAjusteReceita",
						Constants.MSG_ERROR_CLIENTENTITY_INATIVO, df.getCliente().getCodigoMnemonico());
				validated = Boolean.FALSE;
			}
			if (df.getIndicadorEntrega() == null) {
				Messages.showError("updateAjusteReceita",
						Constants.MSG_ERROR_DELIVERYPLACE_EMPTY, df.getNomeDealFiscal());
				validated = Boolean.FALSE;
			}
		}

		return validated;
	}

	/**
	 * Realiza a publicacao da receita e salva a justificativa.
	 *
	 * @return OUTCOME_RECEITA_MANAGE
	 */
	public String publishAndJustifies() {
		// se publicou corretamente, continua o processo, recarrega a p�gina de
		// manage e redireciona
		if (this.publish()) {
			this.prepareManage();
			return OUTCOME_RECEITA_MANAGE;
		} else {
			return null;
		}
	}

	/**
	 * Atualiza a justificativa corrente - save da aba ShortTerm Revenue.
	 */
	public void updateCurrentJustificativaReceita() {

		ReceitaResultado rr = bean.getCurrentReceitaMoedaRow()
				.getShortTermRevenueRow().getReceitaResultado();

		// obtem o motivo resultado
		Long motivo = bean.getMotivoResultadoMap().get(
				bean.getCurrentReceitaMoedaRow().getShortTermRevenueRow()
						.getMotivoResultadoSelected());
		MotivoResultado motivoResultado = motivoResultadoService
				.findMotivoResultadoById(motivo);

		String observacao = rr.getTextoObservacao();

		ReceitaResultado rrEM = receitaResultadoService
				.findReceitaResultadoById(rr.getCodigoReceitaResultado());
		rrEM.setTextoObservacao(observacao);
		rrEM.setMotivoResultado(motivoResultado);

		receitaResultadoService.updateReceitaResultado(rrEM);

		Messages.showSucess("updateCurrentJustificativaReceita",
				Constants.DEFAULT_MSG_SUCCESS_UPDATE,
				Constants.ENTITY_NAME_RECEITA);
	}

	/**
	 * Atualiza a receita resultado com as justificativas.
	 */
	public void saveJustificativaReceita() {
		ReceitaResultado receitaResultado;
		// itera por todas as justificativas (justificativas p/ Moeda) da
		// receita
		for (ShortTermRevenueRow strr : bean.getShortTermRevenueRowList()) {

			// para evitar lazy
			strr.getReceitaResultado().setContratoPratica(
					contratoPraticaService.findContratoPraticaById(bean.getTo()
							.getContratoPratica().getCodigoContratoPratica()));

			receitaResultado = receitaResultadoService
					.findReceitaResultadoByContratoAndMoedaAndDate(strr
							.getReceitaResultado());

			// caso a justificativa nao seja do banco, entao cria uma instancia
			// para ser persistida
			if (receitaResultado == null) {
				receitaResultado = new ReceitaResultado();
			}

			// obtem o motivo resultado
			Long motivo = bean.getMotivoResultadoMap().get(
					strr.getMotivoResultadoSelected());
			if (motivo != null) {
				MotivoResultado motivoResultado = motivoResultadoService
						.findMotivoResultadoById(motivo);
				receitaResultado.setMotivoResultado(motivoResultado);
			}

			receitaResultado.setTextoObservacao(strr.getReceitaResultado()
					.getTextoObservacao());
			receitaResultado.setValorReceitaPlanejada(strr
					.getReceitaResultado().getValorReceitaPlanejada());
			receitaResultado.setValorReceitaRealizada(strr
					.getReceitaResultado().getValorReceitaRealizada());
			receitaResultado.setDataAtualizacao(new Date());

			// cria ou atualiza a receita resultado
			if (receitaResultado.getCodigoReceitaResultado() != null) {
				receitaResultadoService
						.updateReceitaResultado(receitaResultado);
			} else {
				receitaResultado.setDataMes(strr.getReceitaResultado()
						.getDataMes());
				receitaResultado
						.setMoeda(strr.getReceitaResultado().getMoeda());
				receitaResultado.setContratoPratica(strr.getReceitaResultado()
						.getContratoPratica());
				receitaResultadoService
						.createReceitaResultado(receitaResultado);
			}
		}
	}

	/**
	 * Calcula os impostos da {@link ReceitaMoeda} e de suas
	 * {@link ReceitaDealFiscal}s. Caso nao consiga calcular algum imposto, uma
	 * exception � lan�ada e o c�lculo � interrompido.
	 *
	 * @param receitaMoeda
	 *            - {@link ReceitaMoeda} informada como base de calculo
	 * @param dataMes
	 *            - data/mes base para o calculo (mesmo mes da receita)
	 * @return {@link ReceitaMoeda} com os impostos calculados (a lista de
	 *         {@link ReceitaDealFiscal} tambem estara com os impostos
	 *         calculados)
	 * @throws TaxaImpostoNotFoundException
	 *             - exception lan�ada caso algum imposto nao seja calculado
	 */
	private ReceitaMoeda calculaImpostoReceitaMoeda(
			final ReceitaMoeda receitaMoeda, final Date dataMes)
			throws TaxaImpostoNotFoundException {

		// media de imposto dos receita deal fiscal que sera calculado para o
		// receita moeda informada
		BigDecimal mediaPonderadaReceitaMoeda = BigDecimal.valueOf(0);

		// lista a ser preenchida com os receita deal fiscals com impostos
		// calculados
		List<ReceitaDealFiscal> receitaDealFiscalList = new ArrayList<ReceitaDealFiscal>();

		// percorre todas as receita deal fiscal da receita moeda informada
		for (ReceitaDealFiscal receitaDealFiscal : receitaMoeda
				.getReceitaDealFiscals()) {

			if (receitaDealFiscal.getCodigoReceitaDfiscal() != null) {
				// obtem uma receita deal fiscal do banco
				receitaDealFiscal = receitaDealFiscalService
						.findReceitaDealById(receitaDealFiscal
								.getCodigoReceitaDfiscal());

				// obtem o percentual da m�dia de imposto do receita deal fiscal
				BigDecimal mediaPercImpostoDealFiscal = receitaDealFiscalService
						.calculaMediaPercentualImposto(receitaDealFiscal,
								dataMes);

				// setta o percentual da receita deal fiscal
				receitaDealFiscal
						.setPercentualImposto(mediaPercImpostoDealFiscal);

				// calcula a m�dia ponderada das taxas dos fiscal deals
				// mediaPonderadaReceitaMoeda = (ValorReceitaDealFiscal *
				// mediaPercImpostoDealFiscal) / ValorTotalReceitaMoeda
				if (!receitaMoeda.getValorTotalMoeda().equals(BigDecimal.ZERO)) {

					mediaPonderadaReceitaMoeda = mediaPonderadaReceitaMoeda
							.add((receitaDealFiscal.getValorReceita()
									.multiply(mediaPercImpostoDealFiscal))
									.divide(receitaMoeda.getValorTotalMoeda(),
											BigDecimal.ROUND_UP));
				}
			}
			// add receita moeda com o imposto calculado
			receitaDealFiscalList.add(receitaDealFiscal);
		}

		// atualiza a receita moeda com a lista de receita deal fiscal com
		// impostos calculados
		receitaMoeda.setReceitaDealFiscals(receitaDealFiscalList);
		// setta o percentual de imposto da receita moeda
		receitaMoeda.setPercentualImposto(mediaPonderadaReceitaMoeda);

		return receitaMoeda;
	}

	/**
	 * Calcula a media dos impostos dos TiposServico do DealFiscal e da Empresa
	 * e atribui para a ReceitaDealFiscal. Caso nao encontre a TaxaImposto
	 * associada � Empresa e TipoServico, nao termina o c�uculo e retorna false
	 * para que a Receita nao seja publicada. Este calculo � realizado nas
	 * receitas moedas do objeto ReceitaMoedaRowList do Bean.
	 *
	 * @return true se o c�lculo est� ok, false caso contr�rio
	 */
	private Boolean calculateImposto() {

		// percorre todas as receitas moedas
		for (ReceitaMoedaRow receitaMoedaRow : bean.getReceitaMoedaRowList()) {

			ReceitaMoeda receitaMoeda = receitaMoedaRow.getReceitaMoeda();

			ReceitaMoeda rm = receitaMoedaService
					.findReceitaMoedaById(receitaMoeda.getCodigoReceitaMoeda());

			try {
				// obtem a ReceitaMoeda com os impostos calculados
				rm = this.calculaImpostoReceitaMoeda(rm, bean.getTo()
						.getDataMes());
			} catch (TaxaImpostoNotFoundException e) {
				// caso nao tenha TaxaImposto cadastrada, mostra a
				// mensagem de erro da Taxa que est� faltando e nao
				// permite que a Receita seja publicada (return false)
				Messages.showError("calculateImposto",
						Constants.REVENUE_MSG_ERROR_NO_TAX_TO_PUBLISH_2,
						new Object[] { e.getMessage() });

				return Boolean.valueOf(false);
			}
			// atualiza o receita moeda row
			receitaMoeda.setPercentualImposto(rm.getPercentualImposto());
			receitaMoeda.setReceitaDealFiscals(rm.getReceitaDealFiscals());

			receitaMoedaRow.setRecDealFiscalRowList(this
					.loadReceitaDealFiscalRow(receitaMoeda));
			receitaMoedaRow.setReceitaMoeda(receitaMoeda);
		}

		return Boolean.valueOf(true);
	}

	/**
	 * Prepare do publish.
	 *
	 * @return retorna o out
	 */
	public String preparePublish() {

		this.prepareUpdate();

		// pega a lista de ReceitaMoedaRow da tela (VOs)
		List<ReceitaMoedaRow> recMoeRowList = bean.getReceitaMoedaRowList();

		// percorre todas as MoedasRow para checar o total do deal
		// verifica se o total da receita � igual ao total dos deals
		for (ReceitaMoedaRow recMoeRow : recMoeRowList) {
			if (!this.validateTotalDeal(recMoeRow)) {
				Messages.showError("preparePublish",
						Constants.MSG_ERROR_RECEITA_TOTAL_NOT_MATCH, recMoeRow
								.getReceitaMoeda().getMoeda().getNomeMoeda());

				// esconde as mensagens da Receita Moeda
				for (ReceitaMoedaRow recMoeRow1 : recMoeRowList) {
					recMoeRow1.setShowMessage(Boolean.FALSE);
				}

				return null;
			}

			// verifica se o valor dos fiscal deals é igual ao valor ajustado no forecast
			BigDecimal valorTotalReceitaDealFiscal = getValorTotalReceitaDealFiscal();
			if (Constants.YES.equals(recMoeRow.getReceitaMoeda().getReceita().getContratoPratica().getIndicadorAprovaAjusteReceita()) && (
					recMoeRow.getTotalAmount().compareTo(valorTotalReceitaDealFiscal) != 0 ||
							recMoeRow.getValorReceitaAjustadoForecast().compareTo(valorTotalReceitaDealFiscal) != 0)) {
				Messages.showError("create",
						Constants.MSG_ERROR_RECEITA_TOTAL_NOT_MATCH,
						recMoeRow.getReceitaMoeda().getMoeda()
								.getNomeMoeda());

				// esconde as mensagens da Receita Moeda
				for (ReceitaMoedaRow recMoeRow1 : recMoeRowList) {
					recMoeRow1.setShowMessage(Boolean.FALSE);
				}

				// se a lista tem algum erro, retorna null
				return null;
			}
		}

		bean.setIsJustificativaByPublish(Boolean.valueOf(true));
		// Checa se deve ser realizado uma justificativa
		if (this.checkJustificativa(recMoeRowList)) {
			bean.setIsJustificativaBySave(Boolean.valueOf(false));
			this.prepareJustificativa();
			return OUTCOME_RECEITA_JUSTIFICATIVA_RESULTADO;
		}

		// Mantem a tela de receita e exibe o modal de confirmacao da
		// publicacao
		bean.setShowModalConfirmation(Boolean.valueOf(true));

		return null;
	}

	/**
	 * Prepare para o update.
	 *
	 * @return retorna o outcome da pagina caso necessario
	 */
	public String prepareUpdate() {

		// indicador de estado Fradt
		Boolean isDraft = Boolean.FALSE;

		Boolean firstCurrency = Boolean.TRUE;

		// pega a lista de ReceitaMoedaRow da tela (VOs)
		List<ReceitaMoedaRow> recMoeRowList = bean.getReceitaMoedaRowList();
		for (ReceitaMoedaRow recMoeRow : recMoeRowList) {

			if (firstCurrency) {
				bean.setNameTabMoedaRow(recMoeRow.getReceitaMoeda().getMoeda()
						.getNomeMoeda());
				firstCurrency = Boolean.FALSE;
			}

			// verifica se alguma moeda nao tem deal
			if (recMoeRow.getRecDealFiscalRowList().isEmpty()) {
				isDraft = Boolean.TRUE;
			}

			// verifica se o total da receita eh igual ao total dos deals
			if (!this.validateTotalDeal(recMoeRow)) {
				Messages.showError("prepareUpdate",
						Constants.MSG_ERROR_RECEITA_TOTAL_NOT_MATCH, recMoeRow
								.getReceitaMoeda().getMoeda().getNomeMoeda());

				// esconde as mensagens da Receita Moeda
				for (ReceitaMoedaRow recMoeRow1 : recMoeRowList) {
					recMoeRow1.setShowMessage(Boolean.FALSE);
				}

				return null;
			}

			// verifica se o valor dos fiscal deals é igual ao valor ajustado no forecast
			BigDecimal valorTotalReceitaDealFiscal = getValorTotalReceitaDealFiscal();
			if (Constants.YES.equals(recMoeRow.getReceitaMoeda().getReceita().getContratoPratica().getIndicadorAprovaAjusteReceita()) && (
					recMoeRow.getTotalAmount().compareTo(valorTotalReceitaDealFiscal) != 0 ||
							recMoeRow.getValorReceitaAjustadoForecast().compareTo(valorTotalReceitaDealFiscal) != 0)) {
				Messages.showError("create",
						Constants.MSG_ERROR_RECEITA_TOTAL_NOT_MATCH,
						recMoeRow.getReceitaMoeda().getMoeda()
								.getNomeMoeda());

				// esconde as mensagens da Receita Moeda
				for (ReceitaMoedaRow recMoeRow1 : recMoeRowList) {
					recMoeRow1.setShowMessage(Boolean.FALSE);
				}

				// se a lista tem algum erro, retorna null
				return null;
			}
		}

		// seta o estado correto
		boolean isPublished = Constants.VERSION_RECEITA_PUBLISHED.equals(bean
				.getTo().getIndicadorVersao());
		if (!isPublished) {
			if (isDraft) {
				bean.getTo()
						.setIndicadorVersao(Constants.VERSION_RECEITA_DRAFT);
			} else {
				bean.getTo().setIndicadorVersao(
						Constants.VERSION_RECEITA_WORKING);
			}
		}

		bean.setShowModalConfirmation(Boolean.valueOf(false));
		bean.setIsJustificativaByPublish(Boolean.valueOf(false));

		// Checa se deve ser realizado uma justificativa
		if (this.checkJustificativa(recMoeRowList)) {
			bean.setIsJustificativaBySave(Boolean.valueOf(true));
			this.prepareJustificativa();
			return OUTCOME_RECEITA_JUSTIFICATIVA_RESULTADO;
		}

		// Mantem a tela de receita e exibe realiza o update
		this.update();

		// esconde as mensagens da Receita Moeda
		for (ReceitaMoedaRow recMoeRow1 : recMoeRowList) {
			recMoeRow1.setShowMessage(Boolean.FALSE);
		}

		return null;
	}

	/**
	 * Realiza a atualizacao da entidade Receita completa e salva a
	 * justificativa.
	 *
	 * @return OUTCOME_RECEITA_MANAGE
	 */
	public String updateAndJustifies() {
		// Atualiza a receita resultado
		this.saveJustificativaReceita();
		this.update();
		this.prepareManage();
		return OUTCOME_RECEITA_MANAGE;
	}

	/**
	 * Realiza a atualizacao da entidade Receita completa.
	 *
	 */
	private void update() {

		// habilita as mensagens de validacao
		messageConntrolBean.setDisplayMessages(Boolean.valueOf(true));

		Receita to = bean.getTo();
		String indStatusNew = to.getIndicadorVersao();
		// busca novamente o Receita para abrir sessao de conexao
		// com o banco (hibernate)
		Receita receita = receitaService.findReceitaById(to.getCodigoReceita());

		ContratoPratica cp = contratoPraticaService.findContratoPraticaById(receita.getContratoPratica().getCodigoContratoPratica());

		// cria uma lista de ReceitaMoeda para substituir a original relacionada
		// com a Receita
		List<ReceitaMoeda> recMoeListAux = new ArrayList<ReceitaMoeda>();

		// pega a lista de ReceitaMoedaRow da tela (VOs)
		for (ReceitaMoedaRow recMoeRow : bean.getReceitaMoedaRowList()) {

			// cria os objetos ReceitaMoeda para popular a lista
			ReceitaMoeda receitaMoeda = recMoeRow.getReceitaMoeda();

			// carrega a lista de ItemReceita para ser persistida no
			// banco a partir da lista da tela
			List<ItemReceita> itemReceitaList = receitaService
					.loadItemReceitaList(recMoeRow, indStatusNew.equals(Constants.VERSION_RECEITA_DRAFT), receita.getContratoPratica().getCodigoContratoPratica());

			List<ReceitaDealFiscal> recDealFiscalList = new ArrayList<ReceitaDealFiscal>();
			List<ReceitaPlantao> receitaPlantaos = new ArrayList<ReceitaPlantao>();

			for (ReceitaDealFiscalRow rdf : recMoeRow.getRecDealFiscalRowList()) {

				if (rdf.getTo().getCodigoReceitaDfiscal() == null) {
					rdf.getTo().setReceitaPlantao(null);
				}
				recDealFiscalList.add(rdf.getTo());
				receitaPlantaos.add(rdf.getTo().getReceitaPlantao());
			}

			//receitaPlantaoService.createOrUpdate(receitaPlantaos);

			// se a lista foi "traduzida" corretamente
			if (itemReceitaList != null) {

				if(cp.getIndicadorMultiDealFiscal().equals(Constants.FLAG_YES)){
					itemReceitaService.calculateTaxMultiDealFiscal(recDealFiscalList, itemReceitaList);
				}else {
					// Adiciona imposto e valor liquido a lista de itensReceica
					itemReceitaService.calculateTax(recDealFiscalList, itemReceitaList);
				}
				// atribui a lista de ItemReceita
				receitaMoeda.setItemReceitas(itemReceitaList);
				receitaMoeda.setReceitaDealFiscals(recDealFiscalList);
				receitaMoeda.setValorTotalMoeda(recMoeRow
						.getTotalReceitaDealFiscal());
				Long defaultCodicoMoeda = Long.valueOf(systemProperties
						.getProperty(Constants.DEFAULT_PROPERTY_CURRENCY_CODE));
				Moeda moeda = receitaMoeda.getMoeda();
				if (moeda.getCodigoMoeda() != defaultCodicoMoeda) {
					receitaMoeda.setCotacaoMoeda(cotacaoMoedaService
							.findCotacaoMoedaByMonth(moeda,
									receita.getDataMes()));
				} else {
					receitaMoeda.setCotacaoMoeda(null);
				}
			}else {
				//itemReceita nao foi traduzida corretamente, houver alguma inconsistencia e não deve prosseguir
				return;
			}
			recMoeListAux.add(receitaMoeda);
		}

		// substitui a lista de ReceitaMoeda original relacionada com a
		// Receita
		receita.setReceitaMoedas(recMoeListAux);

		receita.setTextoObservacao(to.getTextoObservacao());
		String indStatusOld = receita.getIndicadorVersao();
		receita.setIndicadorVersao(indStatusNew);

		// altera a Receita
		receitaService.updateReceita(receita, indStatusOld);

		// mensagem de sucesso
		Messages.showSucess("updateReceita",
				Constants.DEFAULT_MSG_SUCCESS_UPDATE,
				Constants.ENTITY_NAME_RECEITA);

		// atualiza o Receita na mem�ria
		bean.setTo(receita);

		this.prepareHistoricoReceita();
		reportService.runProcPlannedCost(receita.getContratoPratica().getCodigoContratoPratica());
	}

	/**
	 * Prepara a tela de remo��o da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareRemove() {
		// marca o modo de remo��o como true
		bean.setIsRemove(Boolean.TRUE);
		bean.setIsUpdate(Boolean.FALSE);

		Receita receita = receitaService
				.findReceitaById(bean.getCurrentRowId());
		List<ReceitaMoedaRow> receitaMoedaRowList = new ArrayList<ReceitaMoedaRow>();

		ReceitaMoedaRow receitaMoedaRow;
		ReceitaDealFiscalRow receitaDealFiscalRow;

		// Percorre lista de receita Moeda (aba main)
		for (ReceitaMoeda receitaMoeda : receita.getReceitaMoedas()) {
			receitaMoedaRow = new ReceitaMoedaRow();

			List<ReceitaDealFiscalRow> receitaRowList = new ArrayList<ReceitaDealFiscalRow>();
			// Percorre lista de receita deal fiscal para montar VO.
			for (ReceitaDealFiscal rdf : receitaMoeda.getReceitaDealFiscals()) {
				if (rdf.getIndicadorStatus() != null && rdf.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_PENDENTE)) {
					Messages.showWarning("prepareRemove",
							Constants.RECEITA_MSG_AVISO_PENDENTE);
					return null;
				}

				receitaDealFiscalRow = new ReceitaDealFiscalRow();
				receitaDealFiscalRow.setTo(rdf);

				if (rdf.getReceitaPlantao() == null) {
					rdf.setReceitaPlantao(new ReceitaPlantao(rdf));
				}

				receitaDealFiscalRow.setIsReceitaEditavel(Boolean.valueOf(true));
				receitaRowList.add(receitaDealFiscalRow);
			}

			// seta objeto para adiciona-lo na lista
			receitaMoedaRow.setShortTermRevenueRow(this
					.buildShortTermRevenueRow(receita.getContratoPratica(),
							receitaMoeda.getMoeda(), receita.getDataMes()));
			receitaMoedaRow.setReceitaMoeda(receitaMoeda);
			receitaMoedaRow.setRecDealFiscalRowList(receitaRowList);

			//Carregar e ordernar itens da receita
			List<ItemReceitaRow> list = receitaService.prepareViewListItemReceita(receitaMoeda);
			bean.getToItemReceitaRow().setComparableField(ItemReceitaRow.COLUMN_SITE);
			bean.setAscending(true);
			Collections.sort(list);
			receitaMoedaRow.setItemReceitaRowList(list);

			receitaMoedaRow.setAjusteReceitaList(this
					.loadAjusteReceitaList(receitaMoeda));
			receitaMoedaRowList.add(receitaMoedaRow);
		}

		bean.setReceitaMoedaRowList(receitaMoedaRowList);
		bean.setTo(receita);
		bean.setLastPageId(bean.getCurrentPageId());

		return OUTCOME_RECEITA_MANAGE_VIEW;
	}

	/**
	 * Deleta uma entidade.
	 *
	 * @return pagina de destino
	 */
	public String remove() {

		try {

			// tenta remover o Receita corrente e apaga os campos de usuario
			// referentes a justificativa
			receitaService
					.removeReceitaAndShortTermRevenueAndAdjusts(receitaService
							.findReceitaById(bean.getCurrentRowId()));

		} catch (DataIntegrityViolationException e) {
			Messages.showError("remove",
					Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_RECEITA);

			return null;
		}

		// caso seja removido com sucesso, d� mensagem de sucesso
		Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
				Constants.ENTITY_NAME_RECEITA);
		bean.resetTo();
		// faz a busca novamente para recarregar a lista da pesquisa
		this.findByFilter();

		return OUTCOME_RECEITA_SEARCH;
	}

	/**
	 * Prepara a tela de visualiza��o da entidade.
	 *
	 * @return retorna a pagina de visualiza��o do Receita
	 */
	public String prepareView() {
		// marca o modo de remo��o como false (view) para que a tela nao mostre
		// o bot�o de excluir
		bean.setIsRemove(Boolean.FALSE);
		bean.setIsUpdate(Boolean.FALSE);

		Receita receita = receitaService
				.findReceitaById(bean.getCurrentRowId());
		List<ReceitaMoedaRow> receitaMoedaRowList = new ArrayList<ReceitaMoedaRow>();

		ReceitaMoedaRow receitaMoedaRow;

		// Percorre lista de receita Moeda (aba main)
		for (ReceitaMoeda receitaMoeda : receita.getReceitaMoedas()) {
			receitaMoedaRow = new ReceitaMoedaRow();

			// seta objeto para adiciona-lo na lista
			receitaMoedaRow.setReceitaMoeda(receitaMoeda);
			receitaMoedaRow.setRecDealFiscalRowList(this.loadReceitaDealFiscalRow(receitaMoeda));

			//Carregar e ordernar itens da receita
			List<ItemReceitaRow> list = receitaService.prepareViewListItemReceita(receitaMoeda);
			bean.getToItemReceitaRow().setComparableField(ItemReceitaRow.COLUMN_SITE);
			bean.setAscending(true);
			Collections.sort(list);
			receitaMoedaRow.setItemReceitaRowList(list);

			receitaMoedaRow.setShortTermRevenueRow(this
					.buildShortTermRevenueRow(receita.getContratoPratica(),
							receitaMoeda.getMoeda(), receita.getDataMes()));
			receitaMoedaRow.setAjusteReceitaRowList(this
					.loadAjusteReceitaRowList(receitaMoeda));

			receitaMoedaRowList.add(receitaMoedaRow);
		}

		bean.setReceitaMoedaRowList(receitaMoedaRowList);
		bean.setTo(receita);

		bean.setBackTo(OUTCOME_RECEITA_MANAGE_VIEW);
		bean.setLastPageId(bean.getCurrentPageId());
		return OUTCOME_RECEITA_MANAGE_VIEW;
	}

	/**
	 * Retorna para a p�gina de search.
	 *
	 * @return retorna a pagina de busca.
	 */
	public String backFromFiscalBalanceView() {
		this.prepareView();
		return OUTCOME_RECEITA_MANAGE_VIEW;
	}

	/**
	 * Busca uma entidade pelo id.
	 *
	 * @param id
	 *            da entidade.
	 *
	 */
	public void findById(final Long id) {
		Receita receita = receitaService.findReceitaById(id);
		bean.setTo(receita);

		if (bean.getTo() == null || bean.getTo().getCodigoReceita() == null) {
			Messages.showWarning("findById",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		} else {
			bean.setValidityMonth(""
					+ DateUtil.getMonthNumber(bean.getTo().getDataMes()));
			bean.setValidityYear(""
					+ DateUtil.getYear(bean.getTo().getDataMes()));
		}
	}

	/**
	 * Busca uma entidade pelo id.
	 *
	 * @param id
	 *            da entidade.
	 *
	 */
	public void findMapaById(final Long id) {

		MapaAlocacao mapaAlocacao = mapaAlocacaoService
				.findMapaAlocacaoById(id);
		mapaAlocacaoBean.setTo(mapaAlocacao);

		if (mapaAlocacaoBean.getTo() == null
				|| mapaAlocacaoBean.getTo().getCodigoMapaAlocacao() == null) {
			Messages.showWarning("findMapaById",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
	}

	/**
	 * Busca uma Receita a partir de uma MapaAlocacao.
	 *
	 * @param mapa
	 *            - MapaAlocacao
	 * @param monthDate
	 *            - data mes
	 *
	 * @return uma Receita a partir de uma MapaAlocacao.
	 */
	public Receita findReceitaByMapaData(final MapaAlocacao mapa,
										 final Date monthDate) {

		Receita filter = new Receita();
		filter.setContratoPratica(mapa.getContratoPratica());
		filter.setDataMes(monthDate);

		List<Receita> receitas = receitaService
				.findReceitaAllNoWorkingByFilter(filter);

		if (receitas == null || receitas.isEmpty()) {
			return null;
		}
		return receitas.get(0);
	}

	/**
	 * A��o que realiza o filtro e vai para tela de busca.
	 *
	 * @return retorna a tela de busca
	 */
	public String backToFilter() {
		this.findByFilter();
		bean.resetTo();

		return OUTCOME_RECEITA_SEARCH;
	}

	/**
	 * A��o do bot�o back da tela de Fiscal Balance > Manage
	 *
	 * @return
	 */
	public String backTo() {
		String outcomeBack = OUTCOME_RECEITA_MANAGE_VIEW;

		if (OUTCOME_RECEITA_MANAGE_VIEW.equals(bean.getBackTo())) {
			outcomeBack = prepareView();
		}

		return outcomeBack;
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 *
	 */
	public void findByFilter() {

		VwReceitaFilter filter = bean.getVwReceitaFilter();

		// pega o Cliente
		Long codCli = bean.getClienteMap().get(filter.getId().getNomeCliente());
		Cliente cli = null;
		if (codCli != null) {
			cli = clienteService.findClienteById(codCli);
		}
		// pega a NaturezaCentroLucro
		Long codNatureza = bean.getNaturezaMap().get(
				filter.getId().getNomeNatureza());
		NaturezaCentroLucro natureza = null;
		if (codNatureza != null) {
			natureza = naturezaService.findNaturezaCentroLucroById(codNatureza);
		}
		// pega a Pratica
		Long codPratica = bean.getPraticaMap().get(
				filter.getId().getNomePratica());
		Pratica pratica = null;
		if (codPratica != null) {
			pratica = praticaService.findPraticaById(codPratica);
		}
		// pega o CentroLucro
		Long codCentroLucro = bean.getCentroLucroMap().get(
				filter.getId().getNomeCentroLucro());
		CentroLucro centroLucro = null;
		if (codCentroLucro != null) {
			centroLucro = centroLucroService
					.findCentroLucroById(codCentroLucro);
		}
		// Pega a data (mes/ano)
		Date monthDate = DateUtil.getDate(bean.getValidityMonth(),
				bean.getValidityYear());
		Date closingDate = this.getClosingDate();

		bean.setIsBeforeClosingDate(closingDate.before(monthDate));

		// realiza a busca pelo Receita, utilizando a view
		bean.setVwReceitaFilterList(receitaService.findReceitaByFilter(
				natureza, centroLucro, pratica, cli, filter.getId()
						.getIndicadorVersao(), monthDate));

		// se nenhum resultado encontrado
		if (bean.getVwReceitaFilterList().size() == 0) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		// calcula os ajustes de cada ReceitaMoeda
		for (ReceitaFilter receitaFilter : bean.getVwReceitaFilterList()) {
			for (ReceitaMoeda receitaMoeda : receitaFilter.getReceitaMoedas()) {
				receitaMoeda.setTotalAjuste(receitaMoedaService
						.calculateTotalAjuste(receitaMoeda));

				receitaMoeda.setTotalReceitaPlantao(receitaMoedaService
						.calculateTotalReceitaPlantao(receitaMoeda));
			}
		}

		if (bean.getLastPageId() != null && bean.getLastPageId() == 0 && bean.getVwReceitaFilterList().size() < 20) {
			// volta para a primeira pagina
			bean.setCurrentPageId(0);
		} else {
			bean.setCurrentPageId(bean.getLastPageId());
		}
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 *
	 */
	public void findMapaByFilter() {

		MapaAlocacao filter = mapaAlocacaoBean.getFilter();

		// pega a NaturezaCentroLucro
		Long codNatureza = mapaAlocacaoBean.getNaturezaMap().get(
				mapaAlocacaoBean.getNatureza().getNomeNatureza());
		NaturezaCentroLucro natureza = new NaturezaCentroLucro();
		if (codNatureza != null) {
			natureza = naturezaService.findNaturezaCentroLucroById(codNatureza);
		}

		// pega o CentroLucro
		Long codCentroLucro = mapaAlocacaoBean.getCentroLucroMap().get(
				mapaAlocacaoBean.getCentroLucro().getNomeCentroLucro());

		CentroLucro centroLucro = new CentroLucro();
		if (codCentroLucro != null) {
			centroLucro = centroLucroService
					.findCentroLucroById(codCentroLucro);
		}

		Long contratoPraticaId = mapaAlocacaoBean.getContratoPraticaMap().get(
				filter.getContratoPratica().getNomeContratoPratica());

		if (contratoPraticaId != null) {
			filter.getContratoPratica().setCodigoContratoPratica(
					contratoPraticaId);

		} else if (!"".equals(filter.getContratoPratica()
				.getNomeContratoPratica())) {
			filter.getContratoPratica().setCodigoContratoPratica(-1L);

		} else {
			filter.getContratoPratica().setCodigoContratoPratica(0L);
		}

		Cliente cli = mapaAlocacaoBean.getCliente();
		cli.setCodigoCliente(mapaAlocacaoBean.getClienteMap().get(
				cli.getNomeCliente()));

		// Pega a data (mes/ano)
		Date monthDate = DateUtil.getDate(
				mapaAlocacaoBean.getValidityMonthBeg(),
				mapaAlocacaoBean.getValidityYearBeg());

		// realiza a busca pelo MapaAlocacao
		mapaAlocacaoBean.setResultList(mapaAlocacaoService
				.findMapaAlocacaoByFilter(filter, cli, monthDate, natureza,
						centroLucro));

		for (MapaAlocacao mapaAlocacao : mapaAlocacaoBean.getResultList()) {

			List<VwEstratificacaoUrResultado> listEstratificacaoUrResultado = estratificacaoUrResultadoService
					.findEstratificacaoUrResultadoByContratoPraticaAndDataMes(
							mapaAlocacao.getContratoPratica(), monthDate);

			if (listEstratificacaoUrResultado.size() > 0) {

				for (VwEstratificacaoUrResultado vwEstratificacaoUrResultado : listEstratificacaoUrResultado) {
					if (vwEstratificacaoUrResultado != null) {
						if (vwEstratificacaoUrResultado.getCodigoItemEstratUr() != null) {

							mapaAlocacao.setStratified(Boolean.valueOf(true));

							if (vwEstratificacaoUrResultado
									.getIndicadorInconsistente().equals('S')
									|| (vwEstratificacaoUrResultado
									.getIndicadorDiferencaRealNeg()
									.equals('S') && vwEstratificacaoUrResultado
									.getIndicadorNaoDefinido().equals(
											'S'))) {
								mapaAlocacao.setHasUndefined(Boolean
										.valueOf(true));
								break;
							}
						} else {
							mapaAlocacao.setHasUndefined(Boolean.valueOf(true));
						}
					}
				}
			} else {
				mapaAlocacao.setStratified(Boolean.valueOf(false));
			}

		}

		// se nenhum resultado encontrado
		if (mapaAlocacaoBean.getResultList().size() == 0) {
			Messages.showWarning("findMapaByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		// volta para a primeira pagina
		mapaAlocacaoBean.setCurrentPageId(0);
	}

	/**
	 * Gera o relatorio conforme o filtro da tela.
	 */
	public void findByFilterReport() {
		// busca os resultados conforme o filtro da tela
		this.findByFilter();

		// recupera a lista de ReceitaFilter para fazer a totalizacao
		List<ReceitaFilter> vwReceitaFilterList = bean.getVwReceitaFilterList();

		// atribui a lista para ser exibido os totais na tela
		bean.setReceitaFilterTotalsList(receitaService
				.calculateTotalReceitaByMoeda(vwReceitaFilterList));

		// faz a totalizacao geral (conversoes para Moeda Real)
		bean.setPatternCurrency(receitaService.getCurrency(null, true));
		bean.setNomeMoedaTotReceitaConvert(receitaService.getCurrency(null,
				false));
		bean.setTotalReceitaConvert(receitaService
				.calculateTotalReceitaConvert(vwReceitaFilterList));
	}

	/**
	 * Prepara a tela para adicionar uma ItemReceita. Monta a lista de Login com
	 * todas as {@link Pessoa} do banco e monta a lista de de Profile com todos
	 * os {@link PerfilVendido} ativos associados ao {@link Msa}.
	 *
	 */
	public void prepareAddItemReceita() {
		itemReceitaBean.reset();
		itemReceitaBean.setNumHoras(BigDecimal.valueOf(receitaService.getNumberHoursMonth(bean.getTo().getContratoPratica().getCodigoContratoPratica(), itemReceitaBean.getTo().getPessoa().getCidadeBase().getCodigoCidadeBase(), bean.getTo().getDataMes(), itemReceitaBean.getTo().getNumeroFte().doubleValue())));
		itemReceitaBean.setIsUpdate(Boolean.FALSE);

		// carrega a lista de pessoas
		this.loadPessoaList(pessoaService.findAllForComboBox());

		ContratoPratica cp = contratoPraticaService
				.findContratoPraticaById(bean.getTo().getContratoPratica()
						.getCodigoContratoPratica());

		// obtem a moeda da ReceitaMoeda no qual o item foi adicionado
		Moeda moeda = bean.getCurrentReceitaMoedaRow().getReceitaMoeda()
				.getMoeda();

		// carrega a lista de perfil vendido
		this.loadPerfilVendidoList(perfilVendidoService
				.findPerfilVendidoByMsaAndMoedaAndActive(cp.getMsa(), moeda));

		messageConntrolBean.setDisplayMessages(Boolean.FALSE);
	}

	/**
	 * Adiciona um ItemReceita na lista de itens da Receita na
	 * {@link ReceitaMoeda} correspondente.
	 */
	public void addItemReceita() {

		Receita r = bean.getTo();
		MapaAlocacao ma = mapaAlocacaoService.findMapaAlocacaoByContratoPratica(r.getContratoPratica());
		Pessoa pessoa = pessoaService.findPessoaById(itemReceitaBean
				.getPessoaMap().get(
						itemReceitaBean.getTo().getPessoa().getCodigoLogin()));
		Boolean isPessoaAllocatedOnMapa = mapaAlocacaoService
				.isPessoaAllocatedOnMapaAlocacaoAtDate(pessoa, ma,
						r.getDataMes());

		if (!isPessoaAllocatedOnMapa) {
			Messages.showError("updateAjusteReceita",
					Constants.MSG_ERROR_DEALFISCAL_INATIVO);
			return;
		}

		// popula o ItemReceita corrente
		ItemReceita itemReceita = this.addOrEditItemReceita(itemReceitaBean
				.getTo());

		r.setDataMes(DateUtil.getDate(bean.getValidityMonth(),
				bean.getValidityYear()));

		ReceitaMoeda rm = new ReceitaMoeda();
		rm.setReceita(r);

		itemReceita.setReceitaMoeda(rm);

		// adiciona o ItemReceita na lista
		ItemReceitaRow itemReceitaRow = receitaService.addItemReceita(
				itemReceita, bean.getCurrentReceitaMoedaRow(), bean.getTo()
						.getContratoPratica(), itemReceitaBean
						.getPercentualFaturamento());

		if (itemReceitaRow != null) {
			bean.setToItemReceitaRow(itemReceitaRow);
			this.updateItemReceitaValuesByNumberFte();
			messageConntrolBean.setDisplayMessages(Boolean.TRUE);
		}

		bean.setNameTabMoedaRow(bean.getCurrentReceitaMoedaRow()
				.getReceitaMoeda().getMoeda().getNomeMoeda());
	}

	/**
	 * Prepara a tela para editar um ItemReceita.
	 *
	 */
	public void prepareEditItemReceita() {
		itemReceitaBean.reset();
		itemReceitaBean.setIsUpdate(Boolean.TRUE);

		this.loadPessoaList(pessoaService.findPessoaAll());

		ContratoPratica cp = contratoPraticaService
				.findContratoPraticaById(bean.getTo().getContratoPratica()
						.getCodigoContratoPratica());

		// obtem a moeda da ReceitaMoeda no qual o item foi adicionado
		Moeda moeda = bean.getCurrentReceitaMoedaRow().getReceitaMoeda()
				.getMoeda();

		loadPerfilVendidoList(perfilVendidoService
				.findPerfilVendidoByMsaAndMoedaAndActive(cp.getMsa(), moeda));

		itemReceitaBean.setTo(bean.getToItemReceita());

		messageConntrolBean.setDisplayMessages(Boolean.FALSE);

		// clones dos objetos para quebrar as referencias duplicadas
		ItemReceita itemReceita = itemReceitaBean.getTo();
		itemReceita.setPessoa(itemReceita.getPessoa().getClone());

		itemReceita.setPerfilVendido(perfilVendidoService
				.findPerfilVendidoById(itemReceita.getPerfilVendido()
						.getCodigoPerfilVendido()));

		itemReceita.setPerfilVendido(itemReceita.getPerfilVendido().getClone());
	}

	/**
	 * Edita uma ItemReceita na lista de itens do Receita.
	 *
	 */
	public void editItemReceita() {
		ItemReceitaRow itemReceitaRow = bean.getToItemReceitaRow();

		// popula o ItemReceita corrente
		ItemReceita itemReceita = addOrEditItemReceita(itemReceitaRow
				.getItemReceita());

		Receita r = bean.getTo();
		r.setDataMes(DateUtil.getDate(bean.getValidityMonth(),
				bean.getValidityYear()));

		ReceitaMoeda rm = new ReceitaMoeda();
		rm.setReceita(r);

		itemReceita.setReceitaMoeda(rm);

		// atribui o item novo na row da memoria
		itemReceitaRow.setItemReceita(itemReceita);

		// edita o ItemReceita na lista
		receitaService.editItemReceita(bean.getTo().getContratoPratica(),
				itemReceitaRow);

		itemReceitaRow.getItemReceita().setNumeroFte(
				itemReceitaRow.getNumeroFteHidden());
		itemReceitaRow.setNumeroFteHidden(BigDecimal.valueOf(0));
		// atualiza os valores da grade
		updateItemReceitaValuesByNumberFte();
		bean.setNameTabMoedaRow(bean.getCurrentReceitaMoedaRow()
				.getReceitaMoeda().getMoeda().getNomeMoeda());
		messageConntrolBean.setDisplayMessages(Boolean.TRUE);
	}

	/**
	 * Popula a ItemReceita para ser adicionada ou editada na lista.
	 *
	 * @param itemReceita
	 *            - o ItemReceita a ser populada
	 * @return o ItemReceita populado
	 */
	private ItemReceita addOrEditItemReceita(final ItemReceita itemReceita) {

		// atribui a Pessoa para o ItemReceita corrente
		Long codigoPessoa = itemReceitaBean.getPessoaMap().get(
				itemReceitaBean.getTo().getPessoa().getCodigoLogin());
		if (codigoPessoa != null) {
			itemReceita.setPessoa(pessoaService.findPessoaById(codigoPessoa));
		}

		// atribui o PerfilVendido para o ItemReceita corrente
		Long codigoPerfilVendido = itemReceitaBean.getPerfilVendidoMap().get(
				itemReceitaBean.getTo().getPerfilVendido()
						.getNomePerfilVendido());

		if (codigoPerfilVendido != null) {
			itemReceita.setPerfilVendido(perfilVendidoService
					.findPerfilVendidoById(codigoPerfilVendido));
		}

		return itemReceita;
	}

	/**
	 * Deleta uma ItemReceita na lista do Receita.
	 *
	 */
	public void deleteItemReceita() {
		bean.getToItemReceitaRow().setIsRemove(Boolean.TRUE);
	}

	/**
	 * Seta todas as ItemReceita selecionadas como removidas.
	 */
	public void deleteItemReceitaAll() {
		List<ItemReceitaRow> itemReceitaRowList = bean
				.getCurrentReceitaMoedaRow().getItemReceitaRowList();

		if (isSomeSelectedItemReceita()) {
			for (ItemReceitaRow row : itemReceitaRowList) {
				if (row.getIsSelected() && row.getIsView()) {
					row.setIsRemove(Boolean.TRUE);
				}
			}
		} else {
			Messages.showWarning("deleteItemReceitaAll",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
		bean.setNameTabMoedaRow(bean.getCurrentReceitaMoedaRow()
				.getReceitaMoeda().getMoeda().getNomeMoeda());
	}

	/**
	 * Deleta a EstratificacaoUr.
	 *
	 * @return pagina de destino
	 */
	public String removeEstratificacaoUr() {
		// Pega a data (mes/ano)
		Date monthDate = DateUtil.getDate(
				mapaAlocacaoBean.getValidityMonthBeg(),
				mapaAlocacaoBean.getValidityYearBeg());

		EstratificacaoUr estratificacaoUr = estratificacaoUrService
				.findEstratificacaoUrByIdMapaAlocacaoData(
						mapaAlocacaoBean.getCurrentRowId(), monthDate);

		estratificacaoUrService.removeEstratificacaoUr(estratificacaoUr);

		mapaAlocacaoBean.setIsRemove(Boolean.valueOf(false));
		mapaAlocacaoBean.setIsViewReviewUr(Boolean.valueOf(false));

		Messages.showSucess("removeEstratificacaoUr",
				Constants.MSG_SUCCESS_DELETE_REVIEW);

		findMapaByFilter();

		return OUTCOME_RECEITA_SEARCH_REVIEW_UR;
	}

	/**
	 * Popula a lista de ContratoPratica para combobox.
	 *
	 * @param pContratoPraticaList
	 *            lista de ContratoPratica.
	 *
	 */
	private void loadContratoPraticaList(
			final List<ContratoPratica> pContratoPraticaList) {

		Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
		List<String> contratoPraticaList = new ArrayList<String>();

		for (ContratoPratica contratoPratica : pContratoPraticaList) {
			contratoPraticaMap.put(contratoPratica.getNomeContratoPratica(),
					contratoPratica.getCodigoContratoPratica());
			contratoPraticaList.add(contratoPratica.getNomeContratoPratica());
		}

		bean.setContratoPraticaMap(contratoPraticaMap);
		bean.setContratoPraticaList(contratoPraticaList);
	}

	/**
	 * Popula a lista de ContratoPratica para combobox.
	 *
	 * @param receitaTipos
	 *            lista de ContratoPratica.
	 *
	 */
	private void loadReceitaTipoList(
			final List<ReceitaTipo> receitaTipos) {

		Map<String, Long> receitaTipoMap = new HashMap<String, Long>();
		List<String> receitaTipoList = new ArrayList<String>();

		for (ReceitaTipo receitaTipo : receitaTipos) {
			receitaTipoMap.put(receitaTipo.getNomeReceitaTipo(),
					receitaTipo.getCodigoReceitaTipo());
			receitaTipoList.add(receitaTipo.getNomeReceitaTipo());
		}

		bean.setReceitaTipoMap(receitaTipoMap);
		bean.setReceitaTipoList(receitaTipoList);
	}

	private void loadDealFiscalList(final List<DealFiscal> dealFiscals) {

		Map<String, Long> dealFiscalMap = null;
		if(bean.getDealFiscalNameAndCodeMap().size() > 0){
			dealFiscalMap = bean.getDealFiscalNameAndCodeMap();
		}else {	 dealFiscalMap = new HashMap<String, Long>(); }

		List<String> dealFiscalList = null;

		if(bean.getDealFiscalNameList().size() > 0){
			dealFiscalList = bean.getDealFiscalNameList();
		} else { dealFiscalList = new ArrayList<String>(); }

		for (DealFiscal dealFiscal : dealFiscals) {
			dealFiscalMap.put(dealFiscal.getNomeDealFiscal(),
					dealFiscal.getCodigoDealFiscal());
			dealFiscalList.add(dealFiscal.getNomeDealFiscal());
		}

		bean.setDealFiscalNameList(dealFiscalList);
		bean.setDealFiscalNameAndCodeMap(dealFiscalMap);
	}

	/**
	 * Verifica se o valor do atributo ContratoPratica eh valido. Se o valor nao
	 * � nulo e existe no contratoPraticaMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateContratoPratica(final FacesContext context,
										final UIComponent component, final Object value) {

		Long codigoContratoPratica = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoContratoPratica = bean.getContratoPraticaMap().get(strValue);
			if (codigoContratoPratica == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	public void validateReceitaTipo(final FacesContext context,
									final UIComponent component, final Object value) {

		Long codigoReceitaTipo = null;
		String nomeReceitaTipo = (String) value;

		if ((nomeReceitaTipo != null) && (!"".equals(nomeReceitaTipo))) {
			codigoReceitaTipo = bean.getReceitaTipoMap().get(nomeReceitaTipo);
			if (codigoReceitaTipo == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de Pessoa para combobox.
	 *
	 * @param pPessoaList
	 *            lista de Pessoa.
	 *
	 */
	private void loadPessoaList(final List<Pessoa> pPessoaList) {

		Map<String, Long> pessoaMap = new HashMap<String, Long>();
		List<String> pessoaList = new ArrayList<String>();

		for (Pessoa pessoa : pPessoaList) {
			pessoaMap.put(pessoa.getCodigoLogin(), pessoa.getCodigoPessoa());
			pessoaList.add(pessoa.getCodigoLogin());
		}

		itemReceitaBean.setPessoaMap(pessoaMap);
		itemReceitaBean.setPessoaList(pessoaList);
	}

	/**
	 * Verifica se o valor do atributo Pessoa eh valido. Se o valor nao eh nulo e
	 * existe no pessoaMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validatePessoa(final FacesContext context,
							   final UIComponent component, final Object value) {

		Long codigoPessoa = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoPessoa = itemReceitaBean.getPessoaMap().get(strValue);
			if (codigoPessoa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de PerfilVendido para combobox.
	 *
	 * @param pPerfilVendidoList
	 *            lista de PerfilVendido.
	 *
	 */
	private void loadPerfilVendidoList(
			final List<PerfilVendido> pPerfilVendidoList) {
		Map<String, Long> perfilVendidoMap = new HashMap<String, Long>();
		List<String> perfilVendidoList = new ArrayList<String>();

		for (PerfilVendido perfilVendido : pPerfilVendidoList) {
			perfilVendidoMap.put(perfilVendido.getNomePerfilVendido(),
					perfilVendido.getCodigoPerfilVendido());
			perfilVendidoList.add(perfilVendido.getNomePerfilVendido());
		}

		itemReceitaBean.setPerfilVendidoMap(perfilVendidoMap);
		itemReceitaBean.setPerfilVendidoList(perfilVendidoList);
	}

	/**
	 * Atualiza o valor do FTE. de todas os itens selecionados.
	 */
	public void updateIncomeFte() {
		if (bean.getFte() != null) {
			// verifica se algum item foi selecionado
			if (isSomeItemSelected()) {
				receitaService.updateIncomeFte(bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(), bean.getFte(), bean.getTo());

				bean.setFte(BigDecimal.valueOf(0));
			} else {
				Messages.showWarning("updateIncomeFte",
						Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
			}
		} else {
			Messages.showWarning("updateIncomeFte",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.LABEL_RECEITA_NUMERO_FTE);
		}

		bean.setNameTabMoedaRow(bean.getCurrentReceitaMoedaRow()
				.getReceitaMoeda().getMoeda().getNomeMoeda());
	}

	/**
	 * Atualiza o valor do FTE. de todas os itens selecionados.
	 */
	public void updateIncomeHours() {
		if (bean.getHours() != null) {
			// verifica se algum item foi selecionado
			if (this.isSomeItemSelected()) {
				receitaService.updateIncomeHours(bean
								.getCurrentReceitaMoedaRow().getItemReceitaRowList(),
						bean.getHours(),
						bean.getTo());

				bean.setHours(BigDecimal.valueOf(0));
			} else {
				Messages.showWarning("updateIncomeHours",
						Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
			}
		} else {
			Messages.showWarning("updateIncomeHours",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.LABEL_RECEITA_NUMERO_HOURS);
		}

		bean.setNameTabMoedaRow(bean.getCurrentReceitaMoedaRow()
				.getReceitaMoeda().getMoeda().getNomeMoeda());
	}

	/**
	 * Verifica se algum item foi selecionado.
	 *
	 * @return true se algum item selecionado, caso contrario retorna false.
	 */
	private Boolean isSomeItemSelected() {
		List<ItemReceitaRow> itemReceitaRowList = bean
				.getCurrentReceitaMoedaRow().getItemReceitaRowList();
		for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
			if (itemReceitaRow.getIsSelected()) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Faz a verifica��o do ContratoPratica para criar ou editar uma Receita.
	 *
	 * @return receita - retorna a Receita
	 */
	private Receita verifyCreateOrUpdate() {
		Receita receita = bean.getTo();

		Long codigoContratoPratica = bean.getContratoPraticaMap().get(
				receita.getContratoPratica().getNomeContratoPratica());

		ContratoPratica contratoPratica = null;
		if (codigoContratoPratica != null) {
			contratoPratica = contratoPraticaService
					.findContratoPraticaById(codigoContratoPratica);
		}

		// se existir o ContratoPratica � setado no mapa
		if (contratoPratica != null) {
			receita.setContratoPratica(contratoPratica);
			return receita;
		}

		return null;
	}

	/**
	 * Habilita / desabilita os bot�es (editar / deletar) da lista de
	 * ItemHrsFatDeal.
	 */
	public void changeStatusItemReceitaListButtons() {
		bean.setIsItemReceitaListButtonsEnabled(!bean
				.getIsItemReceitaListButtonsEnabled());
	}

	/**
	 * Atualiza os valores do ItemReceita pelo numero de horas.
	 */
	public String updateItemReceitaValuesByNumberHours() {
		ItemReceitaRow toItemReceitaRow = bean.getToItemReceitaRow();
		if (toItemReceitaRow.getNumberHours() == null) {
			toItemReceitaRow.setNumberHours(BigDecimal.valueOf(0));
			toItemReceitaRow.setNumberHoursHidden(BigDecimal.valueOf(0));
		}

		//double numberHoursMonth = receitaService.getNumberHoursMonth(bean.getTo().getContratoPratica().getCodigoContratoPratica(), toItemReceitaRow.getItemReceita().getPessoa().getCidadeBase().getCodigoCidadeBase(), bean.getTo().getDataMes(), toItemReceitaRow.getNumeroFteHidden().doubleValue());
		double numberHoursMonth = receitaService.getNumberHoursMonth(bean.getTo().getContratoPratica().getCodigoContratoPratica(), toItemReceitaRow.getItemReceita().getPessoa().getCidadeBase().getCodigoCidadeBase(), bean.getTo().getDataMes(), 1.0);
		double numberHours = toItemReceitaRow.getNumberHours().doubleValue();
		double numeroFte = numberHours / numberHoursMonth;
		double hrsPrice = toItemReceitaRow.getHrsPrice().doubleValue();

		toItemReceitaRow.setNumberHoursHidden(BigDecimal.valueOf(numberHours));

		toItemReceitaRow.getItemReceita().setNumeroFte(
				BigDecimal.valueOf(numeroFte));
		toItemReceitaRow.setNumeroFteHidden(BigDecimal.valueOf(numeroFte));

		toItemReceitaRow.setAmountValue(BigDecimal.valueOf(numberHours
				* hrsPrice));

		Boolean canUpdateRevenue;
		canUpdateRevenue = receitaService.calculateDealFiscalRedistribution(bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(), bean.getCurrentReceitaMoedaRow().getRecDealFiscalRowList());

		if(!canUpdateRevenue) {
			return OUTCOME_RECEITA_MANAGE;
		}
		return null;
	}

	/**
	 * Realiza o update do FTE referente as horas inseridas, na tela de add
	 * item.
	 */
	public void updateFteByHours() {
		BigDecimal numHoras = itemReceitaBean.getNumHoras();

		double numberHoursMonth = receitaService.getNumberHoursMonth(bean.getTo().getContratoPratica().getCodigoContratoPratica(), itemReceitaBean.getTo().getPessoa().getCidadeBase().getCodigoCidadeBase(), bean.getTo().getDataMes(), itemReceitaBean.getTo().getNumeroFte().doubleValue());
		double numberHours;

		if (numHoras != null) {
			numberHours = numHoras.doubleValue();
		} else {
			numberHours = 0.0;
		}

		itemReceitaBean.setPercentualFaturamento(BigDecimal.valueOf(numberHours
				/ numberHoursMonth));
	}

	/**
	 * Realiza o update das Horas referente ao FTE inserido, na tela de add
	 * item.
	 */
	public void updateHoursByFte() {
		BigDecimal fte = itemReceitaBean.getPercentualFaturamento();

		double numberHoursMonth = receitaService.getNumberHoursMonth(bean.getTo().getContratoPratica().getCodigoContratoPratica(), itemReceitaBean.getTo().getPessoa().getCidadeBase().getCodigoCidadeBase(), bean.getTo().getDataMes(), itemReceitaBean.getTo().getNumeroFte().doubleValue());
		double newFte;

		if (fte != null) {
			newFte = fte.doubleValue();
		} else {
			newFte = 0.0;
		}

		if(bean.getTo().getContratoPratica().getMsa().getIndicadorArredondamentoDiarias().equals(Constants.NO)) {
			itemReceitaBean.setNumHoras(BigDecimal.valueOf(newFte * numberHoursMonth));
		}else{
			itemReceitaBean.setNumHoras(BigDecimal.valueOf(numberHoursMonth));
		}

	}

	/**
	 * Atualiza os valores do ItemReceita pelo numero de FTE.
	 */
	public String updateItemReceitaValuesByNumberFte() {
		ItemReceitaRow toItemReceitaRow = bean.getToItemReceitaRow();
		if (toItemReceitaRow.getItemReceita().getNumeroFte() == null) {
			toItemReceitaRow.getItemReceita().setNumeroFte(
					BigDecimal.valueOf(0));
			toItemReceitaRow.setNumeroFteHidden(BigDecimal.valueOf(0));
		}

		double numeroFte = toItemReceitaRow.getItemReceita().getNumeroFte()
				.doubleValue();
		BigDecimal numeroFteHiddenBD = toItemReceitaRow.getNumeroFteHidden()
				.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		if (numeroFte != numeroFteHiddenBD.doubleValue()) {
			toItemReceitaRow.setNumeroFteHidden(BigDecimal.valueOf(numeroFte));

			double numberHoursMonth = receitaService.getNumberHoursMonth(bean.getTo().getContratoPratica().getCodigoContratoPratica(), toItemReceitaRow.getItemReceita().getPessoa().getCidadeBase().getCodigoCidadeBase(), bean.getTo().getDataMes(), numeroFte);
			double numberHours;
			if(bean.getTo().getContratoPratica().getMsa().getIndicadorArredondamentoDiarias().equals(Constants.NO)) {
				numberHours= numeroFte * numberHoursMonth;
			}else{
				numberHours = numberHoursMonth;
			}
			double hrsPrice = toItemReceitaRow.getHrsPrice().doubleValue();

			toItemReceitaRow.setNumberHours(BigDecimal.valueOf(numberHours));
			toItemReceitaRow.setNumberHoursHidden(BigDecimal
					.valueOf(numberHours));

			toItemReceitaRow.setFtePrice(BigDecimal.valueOf(numberHoursMonth
					* hrsPrice));
			toItemReceitaRow.setAmountValue(BigDecimal.valueOf(numberHours
					* hrsPrice));

			Boolean canUpdateRevenue;
			canUpdateRevenue = receitaService.calculateDealFiscalRedistribution(bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(), bean.getCurrentReceitaMoedaRow().getRecDealFiscalRowList());

			if(!canUpdateRevenue) {
				return OUTCOME_RECEITA_MANAGE;
			}
		}
		return null;
	}

	public void calculateIncomeRedistribution() {
		calculateIncomeRedistribution(bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(),
				bean.getRedistributionValue(), bean.getTo());
	}

	/**
	 * A��o que aciona redistribui��o da receita para os itens das horas
	 * faturadas selecionados.
	 */
	public void calculateIncomeRedistribution(final List<ItemReceitaRow> itemReceitaRowList,
											  final BigDecimal redistributionValue,
											  final Receita receita) {

		if (redistributionValue != null) {
			// verifica se algum item foi selecionado
			if (isSomeSelectedItemReceita()) {
				receitaService.calculateIncomeRedistribution(itemReceitaRowList, redistributionValue, receita);

				if(Constants.FLAG_YES.equals(receita.getContratoPratica().getIndicadorMultiDealFiscal())) {
					receitaService.calculateDealFiscalRedistribution(
							bean.getCurrentReceitaMoedaRow().getItemReceitaRowList(),
							bean.getCurrentReceitaMoedaRow().getRecDealFiscalRowList());
				}

				bean.setRedistributionValue(new BigDecimal(0));
			} else {
				Messages.showWarning("calculateIncomeRedistribution",
						Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
			}
		} else {
			Messages.showWarning("calculateIncomeRedistribution",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.ITEM_RECEITA_REDISTRIBUTION_LABEL);
		}

		// Lista de receitaDealFiscalRow para atualizar tabela de resumo dos
		// dealfiscal.
		List<ReceitaDealFiscalRow> listaReceitaDealFiscalRow =
				bean.getCurrentReceitaMoedaRow().getRecDealFiscalRowList();

		// Se a lista tiver apenas um elemento, atualiza valor na tela
		if (listaReceitaDealFiscalRow.size() == 1) {
			if (Constants.FLAG_NO.equals(receita.getContratoPratica().getIndicadorAprovaAjusteReceita())) {
				listaReceitaDealFiscalRow.get(0).getTo().setValorReceita(bean.getCurrentReceitaMoedaRow().getTotalAmount());
			}
		}

		bean.setNameTabMoedaRow(bean.getCurrentReceitaMoedaRow().getReceitaMoeda().getMoeda().getNomeMoeda());
	}

	/**
	 * Verifica se algum item foi selecionado.
	 *
	 * @return true se algum item selecionado, caso contrario retorna false.
	 */
	private Boolean isSomeSelectedItemReceita() {
		List<ItemReceitaRow> itemReceitaRowList = bean
				.getCurrentReceitaMoedaRow().getItemReceitaRowList();
		for (ItemReceitaRow itemReceitaRow : itemReceitaRowList) {
			if (itemReceitaRow.getIsSelected()) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Pega o numero de horas do mes do arquivo de configuracoes.
	 *
	 * @return numero de horas do mes do arquivo de configuracoes
	 */
	private double getNumberHoursMonth() {
		return Double.parseDouble(systemProperties
				.getProperty(Constants.DEFAULT_PROPERTY_NUMBER_HOURS_MONTH));
	}

	/**
	 * Popula a lista de naturezas para combobox da natureza.
	 *
	 * @param naturezas
	 *            lista de naturezas.
	 *
	 */
	private void loadNaturezaCentroLucroList(
			final List<NaturezaCentroLucro> naturezas) {

		Map<String, Long> naturezaCentroLucroMap = new HashMap<String, Long>();
		List<String> naturezaCentroLucroList = new ArrayList<String>();

		for (NaturezaCentroLucro natureza : naturezas) {
			naturezaCentroLucroMap.put(natureza.getNomeNatureza(),
					natureza.getCodigoNatureza());
			naturezaCentroLucroList.add(natureza.getNomeNatureza());
		}

		bean.setNaturezaMap(naturezaCentroLucroMap);
		bean.setNaturezaList(naturezaCentroLucroList);
	}

	/**
	 * Popula a lista de naturezas para combobox da natureza.
	 *
	 * @param naturezas
	 *            lista de naturezas.
	 *
	 */
	private void loadNaturezaCentroLucroListReview(
			final List<NaturezaCentroLucro> naturezas) {

		Map<String, Long> naturezaCentroLucroMap = new HashMap<String, Long>();
		List<String> naturezaCentroLucroList = new ArrayList<String>();

		for (NaturezaCentroLucro natureza : naturezas) {
			naturezaCentroLucroMap.put(natureza.getNomeNatureza(),
					natureza.getCodigoNatureza());
			naturezaCentroLucroList.add(natureza.getNomeNatureza());
		}

		mapaAlocacaoBean.setNaturezaMap(naturezaCentroLucroMap);
		mapaAlocacaoBean.setNaturezaList(naturezaCentroLucroList);
	}

	/**
	 * Verifica se o valor do atributo NaturezaCentroLucro eh valido. Se o valor
	 * nao eh nulo e existe no naturezaMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateNaturezaCentroLucro(final FacesContext context,
											final UIComponent component, final Object value) {

		Long codigoNatureza = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoNatureza = bean.getNaturezaMap().get(strValue);
			if (codigoNatureza == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo NaturezaCentroLucro eh valido. Se o valor
	 * nao eh nulo e existe no naturezaMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateNaturezaCentroLucroReview(final FacesContext context,
												  final UIComponent component, final Object value) {

		Long codigoNatureza = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoNatureza = mapaAlocacaoBean.getNaturezaMap().get(strValue);
			if (codigoNatureza == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de centroLucros para combobox.
	 *
	 * @param centroLucros
	 *            lista de contratos.
	 *
	 */
	private void loadCentroLucroList(final List<CentroLucro> centroLucros) {

		Map<String, Long> centroLucroMap = new HashMap<String, Long>();
		List<String> centroLucroList = new ArrayList<String>();

		for (CentroLucro centroLucro : centroLucros) {
			centroLucroMap.put(centroLucro.getNomeCentroLucro(),
					centroLucro.getCodigoCentroLucro());
			centroLucroList.add(centroLucro.getNomeCentroLucro());
		}

		bean.setCentroLucroMap(centroLucroMap);
		bean.setCentroLucroList(centroLucroList);
	}

	/**
	 * Popula a lista de centroLucros para combobox.
	 *
	 * @param centroLucros
	 *            lista de contratos.
	 *
	 */
	private void loadCentroLucroListReview(final List<CentroLucro> centroLucros) {

		Map<String, Long> centroLucroMap = new HashMap<String, Long>();
		List<String> centroLucroList = new ArrayList<String>();

		for (CentroLucro centroLucro : centroLucros) {
			centroLucroMap.put(centroLucro.getNomeCentroLucro(),
					centroLucro.getCodigoCentroLucro());
			centroLucroList.add(centroLucro.getNomeCentroLucro());
		}

		mapaAlocacaoBean.setCentroLucroMap(centroLucroMap);
		mapaAlocacaoBean.setCentroLucroList(centroLucroList);
	}

	/**
	 * Verifica se o valor do atributo CentroLucro eh valido. Se o valor nao eh
	 * nulo e existe no centroLucroMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCentroLucro(final FacesContext context,
									final UIComponent component, final Object value) {

		Long codigoCentroLucro = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoCentroLucro = bean.getCentroLucroMap().get(strValue);
			if (codigoCentroLucro == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo CentroLucro eh valido. Se o valor nao eh
	 * nulo e existe no centroLucroMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCentroLucroReview(final FacesContext context,
										  final UIComponent component, final Object value) {

		Long codigoCentroLucro = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoCentroLucro = mapaAlocacaoBean.getCentroLucroMap().get(
					strValue);
			if (codigoCentroLucro == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de clientes para combobox.
	 *
	 * @param clientes
	 *            lista de clientes.
	 *
	 */
	private void loadClienteList(final List<Cliente> clientes) {

		Map<String, Long> clienteMap = new HashMap<String, Long>();
		List<String> clienteList = new ArrayList<String>();

		for (Cliente cliente : clientes) {
			clienteMap
					.put(cliente.getNomeCliente(), cliente.getCodigoCliente());
			clienteList.add(cliente.getNomeCliente());
		}
		bean.setClienteMap(clienteMap);
		bean.setClienteList(clienteList);
	}

	/**
	 * Verifica se o valor do atributo Cliente eh valido. Se o valor nao eh nulo e
	 * existe no clienteMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCliente(final FacesContext context,
								final UIComponent component, final Object value) {

		Long codigoCliente = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoCliente = bean.getClienteMap().get(strValue);
			if (codigoCliente == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de praticas para combobox.
	 *
	 * @param praticas
	 *            lista de praticas.
	 *
	 */
	private void loadPraticaList(final List<Pratica> praticas) {

		Map<String, Long> praticaMap = new HashMap<String, Long>();
		List<String> praticaList = new ArrayList<String>();

		for (Pratica pratica : praticas) {
			praticaMap
					.put(pratica.getNomePratica(), pratica.getCodigoPratica());
			praticaList.add(pratica.getNomePratica());
		}

		bean.setPraticaMap(praticaMap);
		bean.setPraticaList(praticaList);
	}

	/**
	 * Verifica se o valor do atributo Pratica eh valido. Se o valor nao eh nulo e
	 * existe no praticaMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validatePratica(final FacesContext context,
								final UIComponent component, final Object value) {

		Long codigoPratica = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoPratica = bean.getPraticaMap().get(strValue);
			if (codigoPratica == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo PerfilVendido eh valido. Se o valor nao eh
	 * nulo e existe no getPerfilVendidoMap, entao o valor eh valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validatePerfilVendido(final FacesContext context,
									  final UIComponent component, final Object value) {

		Long codStage = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codStage = itemReceitaBean.getPerfilVendidoMap().get(strValue);
			if (codStage == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de Etiqueta para combobox.
	 *
	 * @param pEtiquetaList
	 *            lista de Etiqueta.
	 *
	 */
	private void loadEtiquetaList(final List<Etiqueta> pEtiquetaList) {
		Map<String, Long> etiquetaMap = new HashMap<String, Long>();
		List<String> etiquetaList = new ArrayList<String>();

		String statusAll = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
		etiquetaMap.put(statusAll, Long.valueOf(-1));
		etiquetaList.add(statusAll);

		String statusWithoutTag = BundleUtil
				.getBundle(Constants.BUNDLE_KEY_WITHOUT_TAG);
		etiquetaMap.put(statusWithoutTag, Long.valueOf(0));
		etiquetaList.add(statusWithoutTag);

		for (Etiqueta etiqueta : pEtiquetaList) {
			etiquetaMap.put(etiqueta.getNomeEtiqueta(),
					etiqueta.getCodigoEtiqueta());
			etiquetaList.add(etiqueta.getNomeEtiqueta());
		}

		bean.setEtiquetaMap(etiquetaMap);
		bean.setEtiquetaList(etiquetaList);
	}

	/**
	 * Pega a data de fechamento do horas faturadas.
	 *
	 * @return retorna a Data de Fechamento.
	 */
	private Date getClosingDate() {
		// pega a data de fechamento do modulo do mapa de alocacao
		Modulo moduloRevenue = moduloService.findModuloById(new Long(
				systemProperties.getProperty(Constants.MODULO_RECEITA_CODE)));

		return moduloRevenue.getDataFechamento();
	}

	/**
	 * Filtra somente os ItemReceita da Etiqueta corrente.
	 *
	 */
	public void filterEtiqueta() {
		// recupera o nome da Etiqueta selecionada
		String nomeEtiquetaSelected = bean.getNomeEtiquetaSelected();
		if (!StringUtils.isEmpty(nomeEtiquetaSelected)) {
			// recupera o c�digo da Etiqueta selecionada
			Long codigoEtiqueta = bean.getEtiquetaMap().get(
					nomeEtiquetaSelected);
			// se existir o c�digo e for maior do que zero (diferente de All)
			if (codigoEtiqueta != null && codigoEtiqueta > 0) {
				etiquetaService.filterEtiquetaForItemReceitaRowList(
						codigoEtiqueta, bean.getTo().getCodigoReceita(), bean
								.getCurrentReceitaMoedaRow()
								.getItemReceitaRowList());
				// se igual a 'Without' tag
			} else if (codigoEtiqueta == 0) {
				etiquetaService.filterWithoutEtiquetaForItemReceitaRow(bean
						.getCurrentReceitaMoedaRow().getItemReceitaRowList());
			} else {
				receitaService.showFullListItemReceita(bean
						.getCurrentReceitaMoedaRow().getItemReceitaRowList());
			}
		} else {
			Messages.showError("filterEtiqueta",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.ENTITY_NAME_ETIQUETA);
		}
	}

	/**
	 * Adiciona uma Etiqueta na tabela Etiqueta relacionado ao Cliente corrente.
	 *
	 */
	public void addEtiqueta() {
		// recupera o nome da Etiqueta a ser adicionado
		String nomeEtiquetaAdd = bean.getNomeEtiquetaAdd();
		Cliente cliente = getClienteByReceita(bean.getTo());

		Etiqueta etiquetaAdd = etiquetaService.addEtiqueta(nomeEtiquetaAdd,
				cliente);
		// se adiocionou com sucessp
		if (etiquetaAdd != null) {
			// mensagem de sucesso
			Messages.showSucess("addEtiqueta",
					Constants.DEFAULT_MSG_SUCCESS_CREATE,
					Constants.ENTITY_NAME_ETIQUETA);

			// limpa o campo nome da Etiqueta
			bean.setNomeEtiquetaAdd("");
			// recarrega a lista de Etiqueta do combo
			loadEtiquetaList(etiquetaService.findEtiquetaAtivaByCliente(cliente
					.getCodigoCliente()));
		} else {
			Messages.showError("addEtiqueta",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.ENTITY_NAME_ETIQUETA);
		}
	}

	/**
	 * Remove uma Etiqueta na tabela Etiqueta relacionado ao Cliente corrente.
	 *
	 */
	public void deleteEtiqueta() {
		// recupera o nome da Etiqueta selecionado
		String nomeEtiquetaSelected = bean.getNomeEtiquetaSelected();
		// recupera o cliente corrente
		Cliente cliente = getClienteByReceita(bean.getTo());

		if (etiquetaService.deleteEtiqueta(nomeEtiquetaSelected, cliente)) {
			// mensagem de sucesso
			Messages.showSucess("deleteEtiqueta",
					Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_ETIQUETA);

			// limpa o campo nome da Etiqueta
			bean.setNomeEtiquetaSelected("");
			// recarrega a lista de Etiqueta do combo
			loadEtiquetaList(etiquetaService.findEtiquetaAtivaByCliente(cliente
					.getCodigoCliente()));

		} else {
			Messages.showError("deleteEtiqueta",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.ENTITY_NAME_ETIQUETA);
		}
	}

	/**
	 * Aplica uma Etiqueta � ItemReceita corrente.
	 *
	 */
	public void applyEtiqueta() {
		// se pelo menos um ItemReceita estiver selecionado
		if (isSomeSelectedItemReceita()) {
			// recupera o nome da Etiqueta selecionado
			String nomeEtiquetaSelected = bean.getNomeEtiquetaSelected();
			// verifica se nao � vazio ou nulo
			if (!StringUtils.isEmpty(nomeEtiquetaSelected)) {
				// recupera a Etiqueta selecionada
				Long codigoEtiqueta = bean.getEtiquetaMap().get(
						nomeEtiquetaSelected);
				// se existir o c�digo e for maior do que zero (diferente de
				// All)
				if (codigoEtiqueta != null && codigoEtiqueta > 0) {
					etiquetaService.applyEtiquetaForItemReceitaRowList(bean
							.getCurrentReceitaMoedaRow()
							.getItemReceitaRowList(), codigoEtiqueta);
				} else {
					Messages.showError("applyEtiqueta",
							Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
							Constants.ENTITY_NAME_ETIQUETA);
				}
			} else {
				Messages.showError("applyEtiqueta",
						Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
						Constants.ENTITY_NAME_ETIQUETA);
			}
		} else {
			Messages.showWarning("applyEtiqueta",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
		bean.setNomeEtiquetaSelected("");
	}

	/**
	 * A��o que realiza a orden��o pela coluna.
	 */
	public void columnSort() {
		if (bean.getCurrentReceitaMoedaRow() != null) {

			if (ItemReceitaRow.getStaticComparableField().compareTo(
					ItemReceitaRow.getLastComparableField()) != 0) {
				bean.setAscending(true);
			} else {
				bean.setAscending(false);
			}

			List<ItemReceitaRow> rowList = bean.getCurrentReceitaMoedaRow().getItemReceitaRowList();

			if (bean.isAscending()) {
				Collections.sort(rowList);
			} else {
				Collections.reverse(rowList);
			}
		}
	}

	/**
	 * Desaplica uma Etiqueta ao ItemReceita corrente.
	 *
	 */
	public void unapplyEtiqueta() {
		// se pelo menos um ItemReceita estiver selecionado
		if (isSomeSelectedItemReceita()) {
			// recupera o nome da Etiqueta selecionado
			String nomeEtiquetaSelected = bean.getNomeEtiquetaSelected();
			// verifica se nao � vazio ou nulo
			if (!StringUtils.isEmpty(nomeEtiquetaSelected)) {
				// recupera a Etiqueta selecionada
				Long codigoEtiqueta = bean.getEtiquetaMap().get(
						nomeEtiquetaSelected);
				// se existir o c�digo e for maior do que zero (diferente de
				// All)
				if (codigoEtiqueta != null && codigoEtiqueta > 0) {
					etiquetaService.unapplyEtiquetaForItemReceitaRowList(bean
							.getCurrentReceitaMoedaRow()
							.getItemReceitaRowList(), codigoEtiqueta);
				} else {
					Messages.showError("unapplyEtiqueta",
							Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
							Constants.ENTITY_NAME_ETIQUETA);
				}
			} else {
				Messages.showError("unapplyEtiqueta",
						Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
						Constants.ENTITY_NAME_ETIQUETA);
			}
		} else {
			Messages.showWarning("unapplyEtiqueta",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}

		bean.setNomeEtiquetaSelected("");
	}

	/**
	 * Recupera o Cliente do Receita corrente.
	 *
	 * @param receita
	 *            - Receita corrente
	 * @return o Cliente do Receita corrente
	 */
	private Cliente getClienteByReceita(final Receita receita) {

		ContratoPratica contratoPratica = contratoPraticaService
				.findContratoPraticaById(receita.getContratoPratica()
						.getCodigoContratoPratica());

		if (contratoPratica != null) {
			Msa msa = contratoPratica.getMsa();
			if (msa != null) {
				Cliente cliente = msa.getCliente();
				if (cliente != null) {
					return clienteService.findClienteById(cliente
							.getCodigoCliente());
				}
			}
		}
		return null;
	}

	/**
	 * Atualiza AjusteReceita.
	 */
	public void updateAjusteReceita() {

		final Map<Long, String> companyErp;
		try {
			companyErp = companyErpService.findAllActive();
		} catch (final BusinessException e) {
			Messages.showError("updateAjusteReceita", e.getMessage());
			return;
		}

		Long codigoAjusteReceitaPai = ajusteReceitaBean.getCurrentRowId();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date changeDate = null;
		try {
			changeDate = sdf.parse("29/05/2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		AjusteReceita ajusteReceitaPai;
		AjusteReceita ajusteReceitaFilho;

		ajusteReceitaPai = ajusteReceitaService
				.findAjusteReceitaById(codigoAjusteReceitaPai);

		Receita receita = bean.getTo();

		if (!receitaService.verifyClosingDateRevenue(ajusteReceitaPai.getDataMesAjuste(), Boolean.TRUE)) {
			return;
		} else if (receitaService.isInternationalRevenue(companyErp, receita)
			&& !receitaService.verifyClosingDateInternationalRevenue(ajusteReceitaPai.getDataMesAjuste(), Boolean.TRUE)) {
			return;
		}

		Date adjustmentDate = DateUtil.getDate(ajusteReceitaBean.getValidityMonthAdjust(),
				ajusteReceitaBean.getValidityYearAdjust());
		ajusteReceitaPai.setDataMesAjuste(adjustmentDate);

		// Validação de data dentro do mês/ano corrente
		if (adjustmentDate.compareTo(new Date()) > 0) {
			Messages.showError("updateAjusteReceita", Constants.RECEITA_ADJUSTMENT_INVALID_DATE);
			return;
		}

		if (!receitaService.verifyClosingDateRevenue(adjustmentDate, Boolean.TRUE)) {
			return;
		} else if (receitaService.isInternationalRevenue(companyErp, receita)
			&& !receitaService.verifyClosingDateInternationalRevenue(adjustmentDate, Boolean.TRUE)) {
			return;
		}

		ajusteReceitaPai.setCodigoLoginAutor(ajusteReceitaBean.getTo().getCodigoLoginAutor());
		ajusteReceitaPai.setValorAjuste(ajusteReceitaBean.getTo().getValorAjuste());

		if (ajusteReceitaPai.getTipoAjuste().getCodigoTipoAjuste() == 3L
				&& ajusteReceitaPai.getDataCriacao().after(changeDate)) {
			ajusteReceitaPai.setValorAjuste(ajusteReceitaBean.getTo()
					.getValorAjuste().multiply(new BigDecimal(-1)));
		}

		ajusteReceitaPai.setTextoObservacao(ajusteReceitaBean.getTo()
				.getTextoObservacao());

		ajusteReceitaService.updateAjusteReceita(ajusteReceitaPai);

		if (ajusteReceitaPai.getTipoAjuste().getCodigoTipoAjuste() == 3L
				&& ajusteReceitaPai.getDataCriacao().after(changeDate)) {
			ajusteReceitaFilho = ajusteReceitaService
					.findByAjusteReceitaPai(ajusteReceitaPai);
			ajusteReceitaFilho.setDataMesAjuste(DateUtil.getDate(
					ajusteReceitaBean.getValidityMonthAdjust(),
					ajusteReceitaBean.getValidityYearAdjust()));
			ajusteReceitaFilho.setCodigoLoginAutor(ajusteReceitaBean.getTo()
					.getCodigoLoginAutor());
			ajusteReceitaFilho.setValorAjuste(ajusteReceitaBean.getTo()
					.getValorAjuste());
			ajusteReceitaFilho.setTextoObservacao(ajusteReceitaBean.getTo()
					.getTextoObservacao());
			ajusteReceitaService.updateAjusteReceita(ajusteReceitaFilho);

		}

		ReceitaMoedaRow receitaMoedaRow = bean.getCurrentReceitaMoedaRow();

		Long codigoReceitaMoeda = receitaMoedaRow.getReceitaMoeda().getCodigoReceitaMoeda();
		ReceitaMoeda receitaMoeda = receitaMoedaService.findReceitaMoedaById(codigoReceitaMoeda);
		List<AjusteReceitaRow> ajusteReceitaRows = this.loadAjusteReceitaRowList(receitaMoeda);
		receitaMoedaRow.setAjusteReceitaRowList(ajusteReceitaRows);

		receitaMoedaRow.setShowFormAjusteReceita(Boolean.FALSE);

		Messages.showSucess("updateAjusteReceita",
				Constants.DEFAULT_MSG_SUCCESS_UPDATE,
				Constants.ENTITY_NAME_AJUSTE_RECEITA);
	}

	public void updateAjusteReceitfdsaa() {

		final Map<Long, String> companyErp;
		try {
			companyErp = companyErpService.findAllActive();
		} catch (final BusinessException e) {
			Messages.showError("updateAjusteReceitfdsaa", e.getMessage());
			return;
		}

		ReceitaMoedaRow receitaMoedaRow = bean.getCurrentReceitaMoedaRow();

		if (this.validateAddEdit()) {

			// pega o ajusteReceita
			Long codigoAjusteReceita = ajusteReceitaBean.getCurrentRowId();
			AjusteReceita ajusteReceita = ajusteReceitaService
					.findAjusteReceitaById(codigoAjusteReceita);

			// se tipo "Adjustment" duas combo box s�o habilitadas
			if (ajusteReceita.getTipoAjuste().getNomeTipoAjuste().equals("Adjusment")) {
				ajusteReceitaBean.setFlagComboDealAndType(Boolean.valueOf(true));
				ajusteReceitaBean.setFlagComboTo(Boolean.valueOf(true));
			}

			ReceitaDealFiscal receitaDealFiscalOriginal = receitaDealFiscalService
					.findReceitaDealById(ajusteReceita.getReceitaDealFiscal()
							.getCodigoReceitaDfiscal());

			// Receita
			Receita receita = bean.getTo();

			// Pega o bean
			ajusteReceita = ajusteReceitaBean.getTo();

			// Seta a data de atualiza��o
			ajusteReceitaBean.getTo().setDataAtualizacao(new Date());

			// Seta a data do ajuste
			ajusteReceita.setDataMesAjuste(DateUtil.getDate(
					ajusteReceitaBean.getValidityMonthAdjust(),
					ajusteReceitaBean.getValidityYearAdjust()));

			// Verifica Closing date da Revenue
			Date startDate = null;
			try {
				String startDateStr = ajusteReceitaBean.getValidityMonthAdjust()
						+ "/"
						+ ajusteReceitaBean.getValidityYearAdjust();

				String[] dateFormat = { "MM/yyyy" };

				startDate = DateUtils.parseDate(startDateStr, dateFormat);

				if (!receitaService.verifyClosingDateRevenue(startDate,
						Boolean.valueOf(true))) {
					return;
				} else if (!receitaService
						.verifyClosingDateInternationalRevenue(startDate,
								Boolean.TRUE)
						&& receitaService.isInternationalRevenue(companyErp, receita)) {
					return;
				}

				// Pega o codigo do tipo de ajuste selecionado
				Long codigoTipoAjuste = ajusteReceitaBean.getTipoAjusteMap()
						.get(ajusteReceitaBean.getNomeTipoAjuste());

				// Tipo de ajuste
				TipoAjuste tipoAjuste = tipoAjusteService
						.findTipoAjusteById(codigoTipoAjuste);

				// Pega o codigo da DealFiscal selecionada
				Long codigoDealFiscal = receitaMoedaRow.getDealFiscalMap().get(
						ajusteReceitaBean.getNomeDealFiscalSelected());

				// Se nao existir ou dealFiscal tiver inativo
				if (codigoDealFiscal != null) {

					ReceitaDealFiscal receitaDealFiscal = null;
					DealFiscal dealFiscal = null;

					// Deal Fiscal
					dealFiscal = dealFiscalService
							.findDealFiscalById(codigoDealFiscal);

					// Receita DealFiscal
					receitaDealFiscal = receitaDealFiscalService
							.findReceitaDealByReceitaAndDeal(receita,
									dealFiscal);

					// Cria Receita DealFiscal se nao tiver
					if (receitaDealFiscal == null) {
						receitaDealFiscal = new ReceitaDealFiscal();
						receitaDealFiscal.setValorReceita(new BigDecimal(0.0));
						receitaDealFiscal.setIndicadorAjuste(Constants.YES);
						// receitaDealFiscal.setReceita(receita);
						receitaDealFiscal.setDealFiscal(dealFiscal);
						receitaDealFiscalService
								.createReceitaDealFiscal(receitaDealFiscal);
					}

					// TipoAjuste anterior, antes de ser alterado.
					TipoAjuste oldTipoAjuste = ajusteReceita.getTipoAjuste();

					// Seta o ajusteReceita atualizado
					ajusteReceita.setTipoAjuste(tipoAjuste);
					ajusteReceita.setReceitaDealFiscal(receitaDealFiscal);

					// Flag para setar valor negativo de ajuste

					boolean negativo = Boolean.valueOf(false);
					BigDecimal valorNegativo = new BigDecimal(0);

					// Checa se houve alteracao no tipo de ajuste (Loss ou
					// Reproval)
					// para um "Adjustment".
					if (!oldTipoAjuste.getNomeTipoAjuste().equals(
							Constants.TIPO_AJUSTE_ADJUSTMENT)
							&& tipoAjuste.getNomeTipoAjuste().equals(
									Constants.TIPO_AJUSTE_ADJUSTMENT)) {

						ReceitaDealFiscal receitaDealFiscalTo = null;

						AjusteReceita ajusteReceitaTo = new AjusteReceita();

						// DealFiscal To
						DealFiscal dealFiscalTo = null;

						// Pega o codigo
						Long codigoDealFiscalTo = receitaMoedaRow
								.getDealFiscalMapTo().get(
										ajusteReceitaBean
												.getNomeDealFiscalSelectedTo());
						// Busca nova DealFiscal
						if (codigoDealFiscalTo != null) {
							dealFiscalTo = dealFiscalService
									.findDealFiscalById(codigoDealFiscalTo);
							receitaDealFiscalTo = receitaDealFiscalService
									.findReceitaDealByReceitaAndDeal(receita,
											dealFiscalTo);
						}

						if (receitaDealFiscalTo == null) {
							receitaDealFiscalTo = new ReceitaDealFiscal();
							receitaDealFiscalTo.setValorReceita(new BigDecimal(
									0.0));
							receitaDealFiscalTo
									.setIndicadorAjuste(Constants.YES);
							receitaDealFiscalTo.setDealFiscal(dealFiscalTo);
							receitaDealFiscalService
									.createReceitaDealFiscal(receitaDealFiscalTo);
						}

						ajusteReceitaTo.setTipoAjuste(tipoAjuste);
						ajusteReceitaTo.setDataAtualizacao(new Date(System
								.currentTimeMillis()));
						ajusteReceitaTo.setDataCriacao(new Date(System
								.currentTimeMillis()));
						ajusteReceitaTo.setDataMesAjuste(ajusteReceitaBean
								.getTo().getDataMesAjuste());
						ajusteReceitaTo
								.setReceitaDealFiscal(receitaDealFiscalTo);
						ajusteReceitaTo.setValorAjuste(ajusteReceitaBean
								.getTo().getValorAjuste());
						ajusteReceitaTo.setCodigoLoginAutor(ajusteReceitaBean
								.getTo().getCodigoLoginAutor());

						if (!ajusteReceitaBean.getFlagComboDealAndType()) {
							ajusteReceitaService
									.createAjusteReceita(ajusteReceitaTo);
							negativo = Boolean.valueOf(true);

						}

						valorNegativo = ajusteReceitaBean.getTo()
								.getValorAjuste().multiply(new BigDecimal(-1));
					}

					// Atualiza o ajuste Receita
					if (negativo) {
						ajusteReceita.setValorAjuste(valorNegativo);
					} else {
						ajusteReceita.setValorAjuste(ajusteReceitaBean.getTo()
								.getValorAjuste());
					}

					ajusteReceitaService.updateAjusteReceita(ajusteReceita);

					// Verifica se ainda existe Deal Fiscal igual ao original
					Date data = DateUtil.getDate(bean.getValidityMonth(),
							bean.getValidityYear());
					List<AjusteReceita> ajusteReceitaList = ajusteReceitaService
							.findAjusteReceitaByDateReceitaAndContratoPratica(
									data, receita.getContratoPratica());
					int totalDealFiscal = 0;
					for (AjusteReceita ar : ajusteReceitaList) {
						if (ar.getReceitaDealFiscal()
								.getDealFiscal()
								.equals(receitaDealFiscalOriginal
										.getDealFiscal())) {
							totalDealFiscal++;
						}
					}

					// Se nao existe, remove a dealFiscal
					if (totalDealFiscal == 0
							&& receitaDealFiscalOriginal.getIndicadorAjuste()
									.equals(Constants.YES)) {
						receitaDealFiscalService
								.removeReceitaDealFiscal(receitaDealFiscalOriginal);
					}

					receitaMoedaRow
							.setReceitaMoeda(receitaMoedaService
									.findReceitaMoedaById(receitaMoedaRow
											.getReceitaMoeda()
											.getCodigoReceitaMoeda()));

					// Carrega lista de receitaDealfiscalRow. (tabela de
					// dealfiscal)
					receitaMoedaRow.setRecDealFiscalRowList(this
							.loadReceitaDealFiscalRow(receitaMoedaRow
									.getReceitaMoeda()));
					receitaMoedaRow.setAjusteReceitaList(this
							.loadAjusteReceitaList(receitaMoedaRow
									.getReceitaMoeda()));

					ajusteReceitaBean.reset();
					ajusteReceitaBean.setShowEditAjusteReceitaPanel(Boolean
							.valueOf(false));
					receitaMoedaRow.setShowFormAjusteReceita(Boolean.FALSE);
					ajusteReceitaBean.setShowViewListVaziaPanel(false);

					Messages.showSucess("updateAjusteReceita",
							Constants.DEFAULT_MSG_SUCCESS_UPDATE,
							Constants.ENTITY_NAME_AJUSTE_RECEITA);

				} else {
					Messages.showError("updateAjusteReceita",
							Constants.MSG_ERROR_DEALFISCAL_INATIVO);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				Messages.showError("createTabelaPreco",
						Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
			}
		}
	}

	/**
	 * Prepara a tela de visualiza��o do AjusteReceita.
	 *
	 * @return retorna o outcome d atela de edi��o.
	 */
	public String prepareViewReceita() {
		ajusteReceitaBean.setShowEditAjusteReceitaPanel(Boolean.valueOf(false));

		Long codigoAjusteReceita = ajusteReceitaBean.getCurrentRowId();

		AjusteReceita ajusteReceita = ajusteReceitaService
				.findAjusteReceitaById(codigoAjusteReceita);

		ajusteReceitaBean.setTo(ajusteReceita);
		ajusteReceitaBean.setNomeDealFiscalSelected(ajusteReceita
				.getReceitaDealFiscal().getDealFiscal().getNomeDealFiscal());

		ajusteReceitaBean.setNomeTipoAjuste(ajusteReceita.getTipoAjuste()
				.getNomeTipoAjuste());

		Date dataMes = ajusteReceita.getDataMesAjuste();

		ajusteReceitaBean.setValidityMonthAdjust(DateUtil
				.getMonthString(dataMes));
		ajusteReceitaBean
				.setValidityYearAdjust(DateUtil.getYearString(dataMes));

		ajusteReceitaBean.setBackFlag(Boolean.valueOf(true));

		return OUTCOME_AJUSTE_RECEITA_VIEW;
	}

	/**
	 * Prepare add do Ajuste.
	 */
	public void addAjuste() {

		final Map<Long, String> companyErp;
		try {
			companyErp = companyErpService.findAllActive();
		} catch (final BusinessException e) {
			Messages.showError("addAjuste", e.getMessage());
			return;
		}

		AjusteReceita ajusteReceita = ajusteReceitaBean.getTo();
		Date data = new Date(System.currentTimeMillis());

		// Receita
		Receita receita = receitaService.findReceitaById(bean.getTo()
				.getCodigoReceita());

		ReceitaMoedaRow receitaMoedaRow = bean.getCurrentReceitaMoedaRow();
		ReceitaMoeda receitaMoeda = receitaMoedaService
				.findReceitaMoedaById(receitaMoedaRow.getReceitaMoeda()
						.getCodigoReceitaMoeda());

		// Data do ajuste
		ajusteReceitaBean.setAnoMesAjuste(DateUtil.getDate(
				ajusteReceitaBean.getValidityMonthAdjust(),
				ajusteReceitaBean.getValidityYearAdjust()));

		// Verifica Closing date da Revenue
		Date startDate = null;
		try {
			String startDateStr = ajusteReceitaBean.getValidityMonthAdjust()
					+ "/" + ajusteReceitaBean.getValidityYearAdjust();

			String[] dateFormat = { "MM/yyyy" };

			startDate = DateUtils.parseDate(startDateStr, dateFormat);

			if (!receitaService.verifyClosingDateRevenue(startDate,
					Boolean.valueOf(true))) {
				return;
			} else if (receitaService.isInternationalRevenue(companyErp, receita)
					&& !receitaService.verifyClosingDateInternationalRevenue(
					startDate, Boolean.TRUE)) {
				return;
			}

			// Validação de data dentro do mês/ano corrente
			if (startDate.compareTo(new Date()) > 0) {
				Messages.showError("addAjuste",
						Constants.RECEITA_ADJUSTMENT_INVALID_DATE);
				return;
			}

			// Tipo de ajuste
			Long codigoTipoAjuste = ajusteReceitaBean.getTipoAjusteMap().get(
					ajusteReceitaBean.getNomeTipoAjuste());
			TipoAjuste tipoAjuste = tipoAjusteService
					.findTipoAjusteById(codigoTipoAjuste);

			// DealFiscal Original
			Long codigoDealFiscalOriginal = receitaMoedaRow.getDealFiscalMap()
					.get(ajusteReceitaBean.getNomeDealFiscalSelected());
			DealFiscal dealFiscalOriginal = dealFiscalService
					.findDealFiscalById(codigoDealFiscalOriginal);

			ReceitaDealFiscal receitaDealFiscalOriginal = receitaDealFiscalService
					.findReceitaDealByReceitaAndDeal(receita,
							dealFiscalOriginal);

			// Se ReceitaDealFiscal nao existir, cria uma
			if (receitaDealFiscalOriginal == null) {
				receitaDealFiscalOriginal = new ReceitaDealFiscal();
				receitaDealFiscalOriginal.setValorReceita(new BigDecimal(0.0));
				receitaDealFiscalOriginal.setIndicadorAjuste(Constants.YES);
				receitaDealFiscalOriginal.setReceitaMoeda(receitaMoeda);
				receitaDealFiscalOriginal.setDealFiscal(dealFiscalOriginal);
				receitaDealFiscalOriginal.setReceitaPlantao(new ReceitaPlantao(
						receitaDealFiscalOriginal));
				receitaDealFiscalService
						.createReceitaDealFiscal(receitaDealFiscalOriginal);
			}

			// Se for tipo adjustment
			ReceitaDealFiscal receitaDealFiscalTo;
			DealFiscal dealFiscalTo;
			AjusteReceita ajusteReceitaTo;
			ajusteReceita.setValorAjuste(ajusteReceitaBean.getTo()
					.getValorAjuste());
			ajusteReceita.setReceitaDealFiscal(receitaDealFiscalOriginal);
			ajusteReceita.setTipoAjuste(tipoAjuste);
			ajusteReceita.setDataAtualizacao(data);
			ajusteReceita.setDataCriacao(data);
			ajusteReceita.setDataMesAjuste(ajusteReceitaBean.getAnoMesAjuste());
			ajusteReceita.setCodigoAjusteReceita(null);

			ajusteReceitaService.createAjusteReceita(ajusteReceita);
			if (tipoAjuste.getNomeTipoAjuste().equals(
					Constants.TIPO_AJUSTE_ADJUSTMENT)) {

				Long codigoDealFiscalTo = receitaMoedaRow.getDealFiscalMapTo()
						.get(ajusteReceitaBean.getNomeDealFiscalSelectedTo());
				if (codigoDealFiscalTo == null) {
					Messages.showError("createTabelaPreco",
							Constants.FATURA_MSG_ERROR_NDF_INVALID_SERVICE_TYPE);

					return;
				}

				dealFiscalTo = dealFiscalService
						.findDealFiscalById(codigoDealFiscalTo);

				receitaDealFiscalTo = receitaDealFiscalService
						.findReceitaDealByReceitaAndDeal(receita, dealFiscalTo);

				if (receitaDealFiscalTo == null) {
					receitaDealFiscalTo = new ReceitaDealFiscal();
					receitaDealFiscalTo.setDealFiscal(dealFiscalTo);
					receitaDealFiscalTo.setReceitaMoeda(receitaMoeda);
					receitaDealFiscalTo.setIndicadorAjuste("Y");
					receitaDealFiscalTo
							.setValorReceita(BigDecimal.valueOf(0.0));
					receitaDealFiscalTo.setReceitaPlantao(new ReceitaPlantao(
							receitaDealFiscalTo));
					receitaDealFiscalService
							.createReceitaDealFiscal(receitaDealFiscalTo);
				}

				ajusteReceitaTo = new AjusteReceita();

				ajusteReceitaTo.setTipoAjuste(tipoAjuste);
				ajusteReceitaTo.setDataAtualizacao(data);
				ajusteReceitaTo.setDataCriacao(data);
				ajusteReceitaTo.setDataMesAjuste(ajusteReceitaBean
						.getAnoMesAjuste());
				ajusteReceitaTo.setReceitaDealFiscal(receitaDealFiscalTo);
				ajusteReceitaTo.setValorAjuste(ajusteReceitaBean.getTo()
						.getValorAjuste());
				ajusteReceitaTo.setCodigoLoginAutor(ajusteReceitaBean.getTo()
						.getCodigoLoginAutor());
				ajusteReceitaTo.setTextoObservacao(ajusteReceitaBean.getTo()
						.getTextoObservacao());
				ajusteReceitaTo.setAjusteReceitaPai(ajusteReceita);

				// ajusteReceitaTo.setAjusteReceitaPai(ajusteReceitaPai)

				ajusteReceita.setValorAjuste(ajusteReceitaBean.getTo()
						.getValorAjuste().multiply(BigDecimal.valueOf(-1)));

				ajusteReceitaService.createAjusteReceita(ajusteReceitaTo);

			} else {
				ajusteReceita.setValorAjuste(ajusteReceitaBean.getTo()
						.getValorAjuste());
			}
			/*
			 * ajusteReceita.setValorAjuste(ajusteReceitaBean.getTo()
			 * .getValorAjuste());
			 * ajusteReceita.setReceitaDealFiscal(receitaDealFiscalOriginal);
			 * ajusteReceita.setTipoAjuste(tipoAjuste);
			 * ajusteReceita.setDataAtualizacao(data);
			 * ajusteReceita.setDataCriacao(data);
			 * ajusteReceita.setDataMesAjuste
			 * (ajusteReceitaBean.getAnoMesAjuste());
			 * ajusteReceita.setCodigoAjusteReceita(null);
			 *
			 * ajusteReceitaService.createAjusteReceita(ajusteReceita);
			 */

			// Repopula a lista de deal fiscal ap�s incluir ajuste de receita.
			// Carrega lista de receitaDealfiscalRow. (tabela de dealfiscal)
			receitaMoedaRow.setRecDealFiscalRowList(this
					.loadReceitaDealFiscalRow(receitaMoeda));

			receitaMoedaRow
					.setReceitaMoeda(receitaMoedaService
							.findReceitaMoedaById(receitaMoeda
									.getCodigoReceitaMoeda()));

			receitaMoedaRow.setAjusteReceitaRowList(this
					.loadAjusteReceitaRowList(receitaMoeda));
			receitaMoedaRow.setShowFormAjusteReceita(Boolean.FALSE);

			Messages.showSucess("addAjuste", Constants.GENEREC_MSG_SUCCESS_ADD);

		} catch (ParseException e) {
			e.printStackTrace();
			Messages.showError("createTabelaPreco",
					Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
		}
	}

	/**
	 * Prepare para add Ajuste Receita.
	 */
	public void prepareAddAjusteReceita() {
		// limpa o ajusteReceitabean
		ajusteReceitaBean.reset();

		// seta as flags do ajusteReceitaBean
		ajusteReceitaBean.setIsEditAjusteReceita(Boolean.FALSE);

		ReceitaMoedaRow receitaMoedaRow = bean.getCurrentReceitaMoedaRow();
		ReceitaMoeda receitaMoeda = receitaMoedaService
				.findReceitaMoedaById(receitaMoedaRow.getReceitaMoeda()
						.getCodigoReceitaMoeda());

		receitaMoedaRow.setShowFormAjusteReceita(Boolean.TRUE);

		// Lista de deal fiscal para combobox
		List<DealFiscal> listDealFiscal = new ArrayList<DealFiscal>();
		// Monta lista de deal fiscal
		for (ReceitaDealFiscal rdf : this
				.loadReceitaDFiscalListByReceitaMoeda(receitaMoeda)) {
			listDealFiscal.add(rdf.getDealFiscal());
		}

		// Carrega lista de tipo de ajuste e seu respectivo mapa
		this.loadTipoAjusteList(tipoAjusteService.findTipoAjusteAllActive());
		// Carrega lista de deal fiscal para combo e seu respectivo mapa
		this.loadDealFiscalList(listDealFiscal, receitaMoedaRow);
	}

	/**
	 * Prepare para a combo de dealfiscal filtrada (To).
	 *
	 * @param e
	 *            evento
	 */
	public void prepareComboDealFiscalTo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		ReceitaMoedaRow receitaMoedaRow = bean.getCurrentReceitaMoedaRow();

		ReceitaMoeda receitaMoeda = receitaMoedaRow.getReceitaMoeda();

		receitaMoedaRow.setListaDealFiscalFiltradaTo(null);

		// Lista de DealFiscal com todos eles (persistidos e novos)
		List<DealFiscal> listaDealFiscal = new ArrayList<DealFiscal>();

		// Lista de receitadealFiscal para montar listaDealFiscal
		List<ReceitaDealFiscal> listaReceitaDealFiscal = this
				.loadReceitaDFiscalListByReceitaMoeda(receitaMoeda);

		// Carrega lista de deal fiscal
		for (ReceitaDealFiscal receitaDealFiscal : listaReceitaDealFiscal) {
			listaDealFiscal.add(receitaDealFiscal.getDealFiscal());
		}

		// Lista filtrada de deal fiscal para combobox To. (Todos dealFiscal
		// menos o selecionado no combo anterior)
		List<DealFiscal> listaDealFiscalFiltrada = new ArrayList<DealFiscal>();

		// Carrega lista filtrada
		for (DealFiscal dealFiscal : listaDealFiscal) {
			if (!dealFiscal.getNomeDealFiscal().equals(value)) {
				listaDealFiscalFiltrada.add(dealFiscal);
			}
		}

		// Carrega combo e seu respectivo mapa da lista filtrada.
		this.loadDealFiscalListTo(listaDealFiscalFiltrada, receitaMoedaRow);
	}

	/**
	 * Fecha o painel de ajuste ao clicar no botao cancel.
	 */
	public void cancelAjusteReceita() {
		ReceitaMoedaRow receitaMoedaRow = bean.getCurrentReceitaMoedaRow();
		receitaMoedaRow.setShowFormAjusteReceita(Boolean.FALSE);
		ajusteReceitaBean.reset();
	}

	/**
	 * Prepare para a combo de To do DealFiscal.
	 *
	 * @param e
	 *            parametro
	 */
	public void disabledComboTo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();
		// Seta a flag para habilitar a combo To ou nao
		ajusteReceitaBean.setFlagComboTo(!value
				.equals(Constants.TIPO_AJUSTE_ADJUSTMENT));
	}

	/**
	 * Prepare edit do Ajuste Receita.
	 */
	public void prepareEditAjusteReceita() {

		Long codigoAjusteReceita = ajusteReceitaBean.getCurrentRowId();
		AjusteReceita ajusteReceita = ajusteReceitaService
				.findAjusteReceitaById(codigoAjusteReceita);

		ReceitaMoedaRow receitaMoedaRow = bean.getCurrentReceitaMoedaRow();

		ajusteReceitaBean.setTo(ajusteReceita);

		ajusteReceitaBean.setNomeDealFiscalSelected(ajusteReceita
				.getReceitaDealFiscal().getDealFiscal().getNomeDealFiscal());

		ajusteReceitaBean.setNomeDealFiscalSelectedTo("");
		if (ajusteReceita.getTipoAjuste().getCodigoTipoAjuste() == 3L) {
			AjusteReceita ajRecFilho = ajusteReceitaService
					.findByAjusteReceitaPai(ajusteReceita);
			ajusteReceitaBean
					.setNomeDealFiscalSelectedTo(ajRecFilho
							.getReceitaDealFiscal().getDealFiscal()
							.getNomeDealFiscal());
			ajusteReceitaBean.getTo().setValorAjuste(
					ajusteReceitaBean.getTo().getValorAjuste()
							.multiply(new BigDecimal(-1)));
		}

		ajusteReceitaBean.setNomeTipoAjuste(ajusteReceita.getTipoAjuste()
				.getNomeTipoAjuste());

		Calendar dataInicio = new GregorianCalendar();
		dataInicio.setTime(ajusteReceita.getDataMesAjuste());

		ajusteReceitaBean.setValidityMonthAdjust(Integer.toString(dataInicio
				.get(Calendar.MONTH) + 1));
		ajusteReceitaBean.setValidityYearAdjust(Integer.toString(dataInicio
				.get(Calendar.YEAR)));

		ajusteReceitaBean.setFlagComboTo(Boolean.valueOf(true));
		ajusteReceitaBean.setFlagComboDealAndType(Boolean.valueOf(true));

		receitaMoedaRow.setShowFormAjusteReceita(Boolean.TRUE);
		ajusteReceitaBean.setIsEditAjusteReceita(Boolean.TRUE);
	}

	/**
	 * A��o de exclus�o do ajuste receita.
	 */
	public void deleteAjusteReceita() {

		final Map<Long, String> companyErp;
		try {
			companyErp = companyErpService.findAllActive();
		} catch (final BusinessException e) {
			Messages.showError("deleteAjusteReceita", e.getMessage());
			return;
		}

		ReceitaMoedaRow receitaMoedaRow = bean.getCurrentReceitaMoedaRow();
		ReceitaMoeda receitaMoeda = receitaMoedaService
				.findReceitaMoedaById(receitaMoedaRow.getReceitaMoeda()
						.getCodigoReceitaMoeda());

		Long codigoAjusteReceita = ajusteReceitaBean.getCurrentRowId();

		AjusteReceita ajusteReceita = ajusteReceitaService
				.findAjusteReceitaById(codigoAjusteReceita);

		TipoAjuste tipoAjuste = tipoAjusteService
				.findTipoAjusteById(ajusteReceita.getTipoAjuste()
						.getCodigoTipoAjuste());

		ajusteReceita.setTipoAjuste(tipoAjuste);

		Receita receita = bean.getTo();

		if (!receitaService.verifyClosingDateRevenue(ajusteReceita.getDataMesAjuste(), Boolean.TRUE)) {
			return;
		} else if (receitaService.isInternationalRevenue(companyErp, receita)
			&& !receitaService.verifyClosingDateInternationalRevenue(
				ajusteReceita.getDataMesAjuste(), Boolean.TRUE)) {
			return;
		}

		if (tipoAjuste.getCodigoTipoAjuste() == 3L) {
			AjusteReceita ajusteFilho = ajusteReceitaService
					.findByAjusteReceitaPai(ajusteReceita);
			ajusteReceitaService.removeAjusteReceita(ajusteFilho);
		}
		ajusteReceitaService.removeAjusteReceita(ajusteReceita);

		Date data = DateUtil.getDate(bean.getValidityMonth(),
				bean.getValidityYear());
		List<AjusteReceita> ajusteReceitaList = ajusteReceitaService
				.findAjusteReceitaByDateReceitaAndContratoPratica(data,
						receita.getContratoPratica());
		int totalDealFiscal = 0;
		for (AjusteReceita ar : ajusteReceitaList) {
			if (ar.getReceitaDealFiscal()
					.getDealFiscal()
					.equals(ajusteReceita.getReceitaDealFiscal()
							.getDealFiscal())) {
				totalDealFiscal++;
			}
		}

		ReceitaDealFiscal receitaDealFiscal = receitaDealFiscalService
				.findReceitaDealById(ajusteReceita.getReceitaDealFiscal()
						.getCodigoReceitaDfiscal());

		if (totalDealFiscal == 0
				&& receitaDealFiscal.getIndicadorAjuste() == Constants.YES) {
			receitaDealFiscalService.removeReceitaDealFiscal(ajusteReceita
					.getReceitaDealFiscal());
		}

		receitaMoedaRow.setReceitaMoeda(receitaMoeda);

		receitaMoedaRow.setAjusteReceitaRowList(this
				.loadAjusteReceitaRowList(receitaMoeda));

		// Carrega lista de receitaDealfiscalRow. (tabela de dealfiscal)
		receitaMoedaRow.setRecDealFiscalRowList(this
				.loadReceitaDealFiscalRow(receitaMoeda));
		receitaMoedaRow.setShowFormAjusteReceita(Boolean.FALSE);

		Messages.showSucess("deleteAjusteReceita",
				Constants.DEFAULT_MSG_SUCCESS_REMOVE,
				Constants.ENTITY_NAME_AJUSTE_RECEITA);

		bean.setNameTabMoedaRow(bean.getCurrentReceitaMoedaRow()
				.getReceitaMoeda().getMoeda().getNomeMoeda());

		ajusteReceitaBean.reset();
	}

	/**
	 * Carrega a lista de ReceitaDealFiscalRow a partir da ReceitaMoeda. (tabela
	 * de dealfiscals)
	 *
	 * @param receitaMoeda
	 *            - the ReceitaMoeda
	 * @return lista de ReceitaDealFiscalRow.
	 */
	private List<ReceitaDealFiscalRow> loadReceitaDealFiscalRow(
			final ReceitaMoeda receitaMoeda) {
		ReceitaDealFiscalRow receitaDealFiscalRow;
		List<ReceitaDealFiscalRow> receitaDealFiscalRowList = new ArrayList<ReceitaDealFiscalRow>();
		// Percorre lista de receita deal fiscal para montar VO.
		for (ReceitaDealFiscal rdf : this.loadReceitaDFiscalListByReceitaMoeda(receitaMoeda)) {
			receitaDealFiscalRow = new ReceitaDealFiscalRow();
			receitaDealFiscalRow.setTo(rdf);

			if (rdf.getReceitaPlantao() == null) {
				rdf.setReceitaPlantao(new ReceitaPlantao(rdf));
			}

			if(receitaMoeda.getReceita().getContratoPratica().getIndicadorMultiDealFiscal().equals(Constants.FLAG_YES)){
				receitaDealFiscalRow.setIsReceitaEditavel(Boolean.valueOf(false));
			}else {
				receitaDealFiscalRow.setIsReceitaEditavel(Boolean.valueOf(true));
			}
			receitaDealFiscalRowList.add(receitaDealFiscalRow);
		}
		return receitaDealFiscalRowList;
	}

	/**
	 * Prepara a tela de visualiza��o do AjusteReceita.
	 *
	 * @return retorna o outcome d atela de edi��o.
	 */
	public String prepareViewAjusteReceita() {
		ajusteReceitaBean.setShowEditAjusteReceitaPanel(Boolean.valueOf(false));

		Long codigoAjusteReceita = ajusteReceitaBean.getCurrentRowId();

		AjusteReceita ajusteReceita = ajusteReceitaService
				.findAjusteReceitaById(codigoAjusteReceita);

		ajusteReceitaBean.setTo(ajusteReceita);
		ajusteReceitaBean.setNomeDealFiscalSelected(ajusteReceita
				.getReceitaDealFiscal().getDealFiscal().getNomeDealFiscal());

		ajusteReceitaBean.setNomeTipoAjuste(ajusteReceita.getTipoAjuste()
				.getNomeTipoAjuste());

		Date dataMes = ajusteReceita.getDataMesAjuste();

		ajusteReceitaBean.setValidityMonthAdjust(DateUtil
				.getMonthString(dataMes));
		ajusteReceitaBean
				.setValidityYearAdjust(DateUtil.getYearString(dataMes));
		ajusteReceitaBean.setBackFlag(Boolean.valueOf(true));
		return OUTCOME_AJUSTE_RECEITA_VIEW;
	}

	/**
	 * Atualiza a lista de AjusteReceita. Update do link de search de Adjustment
	 * reveneu.
	 */
	public void updateAjusteReceitaSearch() {

		if (validateAddEdit()) {

			Long codigoAjusteReceita = ajusteReceitaBean.getCurrentRowId();

			AjusteReceita ajusteReceita = ajusteReceitaService
					.findAjusteReceitaById(codigoAjusteReceita);

			ajusteReceita = ajusteReceitaBean.getTo();

			ajusteReceitaBean.getTo().setDataAtualizacao(new Date());

			// setta a data do ajuste
			ajusteReceita.setDataMesAjuste(DateUtil.getDate(
					ajusteReceitaBean.getValidityMonthAdjust(),
					ajusteReceitaBean.getValidityYearAdjust()));

			TipoAjuste tipoAjuste = null;
			Long codigoTipoAjuste = ajusteReceitaBean.getTipoAjusteMap().get(
					ajusteReceitaBean.getNomeTipoAjuste());

			if (codigoTipoAjuste != null) {
				tipoAjuste = tipoAjusteService
						.findTipoAjusteById(codigoTipoAjuste);
			}

			ReceitaDealFiscal receitaDealFiscal = null;
			Long codigoReceitaDealFiscal = bean.getCurrentReceitaMoedaRow()
					.getDealFiscalMap()
					.get(ajusteReceitaBean.getNomeDealFiscalSelected());

			if (codigoReceitaDealFiscal != null) {
				receitaDealFiscal = receitaDealFiscalService
						.findReceitaDealById(codigoReceitaDealFiscal);
			}

			ajusteReceita.setTipoAjuste(tipoAjuste);
			ajusteReceita.setReceitaDealFiscal(receitaDealFiscal);
			ajusteReceitaService.updateAjusteReceita(ajusteReceita);

			Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_AJUSTE_RECEITA);
		}
	}

	/**
	 * Realiza a valida�ao do campo Login.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validatePessoaAjusteReceita(final FacesContext context,
											final UIComponent component, final Object value) {

		String login = (String) value;

		if ((login != null) && (!"".equals(login))) {
			Pessoa pessoa = pessoaService.findPessoaByLogin(login);

			if (pessoa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		} else {
			Messages.showError("validatePessoa",
					Constants.MSG_WARN_REVENUE_ADJUSTMENT_REPORTER_LOGIN_NULL);
		}

	}

	/**
	 * A��o utilizada no autocomplete da Pessoa. Retorna uma lista de Pessoas.
	 *
	 * @param value
	 *            - valor (login) utilizado na busca das Pessoas
	 *
	 * @return retorna uma lista de recurso
	 */
	public List<Pessoa> autoCompletePessoa(final Object value) {
		return pessoaService.findPessoaByLikeLogin((String) value);
	}

	/**
	 * Valida as datas e o login para inser��o e update.
	 *
	 * @return true se ok
	 */
	private Boolean validateAddEdit() {

		Boolean retorno = Boolean.valueOf(true);

		if (ajusteReceitaBean.getValidityMonthAdjust() == null
				|| ajusteReceitaBean.getValidityMonthAdjust() == "") {
			Messages.showError("createAjusteReceita",
					Constants.MSG_WARN_REVENUE_ADJUSTMENT_MONTH_ADJUST_NULL);
			retorno = Boolean.valueOf(false);
		}

		if (ajusteReceitaBean.getValidityYearAdjust() == null
				|| ajusteReceitaBean.getValidityYearAdjust() == "") {
			Messages.showError("createAjusteReceita",
					Constants.MSG_WARN_REVENUE_ADJUSTMENT_YEAR_ADJUST_NULL);
			retorno = Boolean.valueOf(false);
		}

		if (ajusteReceitaBean.getTo().getCodigoLoginAutor() == null
				|| ajusteReceitaBean.getTo().getCodigoLoginAutor() == "") {
			ajusteReceitaBean.getTo().setCodigoLoginAutor(null);
			Messages.showError("createAjusteReceita",
					Constants.MSG_WARN_REVENUE_ADJUSTMENT_REPORTER_LOGIN_NULL);
			retorno = Boolean.valueOf(false);
		}

		return retorno;
	}

	/**
	 * List de DealFiscal para a combo.
	 *
	 * @param listaDeal
	 *            lista de deal discal
	 */
	private void loadDealFiscalList(final List<DealFiscal> listaDeal,
									ReceitaMoedaRow receitaMoedaRow) {
		Map<String, Long> listaDealMap = new HashMap<String, Long>();
		List<String> listaDealList = new ArrayList<String>();

		for (DealFiscal dealFiscal : listaDeal) {
			listaDealMap.put(dealFiscal.getNomeDealFiscal(),
					dealFiscal.getCodigoDealFiscal());
			listaDealList.add(dealFiscal.getNomeDealFiscal());
		}

		receitaMoedaRow.setDealFiscalMap(listaDealMap);
		receitaMoedaRow.setListaDealFiscal(listaDealList);
	}

	/**
	 * Popula a lista de TipoAjuste para combobox.
	 *
	 * @param tiposAjuste
	 *            lista de TipoAjuste.
	 *
	 */
	private void loadTipoAjusteList(final List<TipoAjuste> tiposAjuste) {

		Map<String, Long> tipoAjusteMap = new HashMap<String, Long>();
		List<String> tipoAjusteList = new ArrayList<String>();

		for (TipoAjuste ta : tiposAjuste) {
			tipoAjusteMap.put(ta.getNomeTipoAjuste(), ta.getCodigoTipoAjuste());
			tipoAjusteList.add(ta.getNomeTipoAjuste());
		}

		ajusteReceitaBean.setTipoAjusteMap(tipoAjusteMap);
		ajusteReceitaBean.setTipoAjusteList(tipoAjusteList);
	}

	/**
	 * List de DealFiscal para a combo filtrada To.
	 *
	 * @param listaDeal
	 *            - lista de Deal
	 */
	private void loadDealFiscalListTo(final List<DealFiscal> listaDeal,
									  ReceitaMoedaRow receitaMoedaRow) {
		Map<String, Long> listaDealMap = new HashMap<String, Long>();
		List<String> listaDealList = new ArrayList<String>();

		for (DealFiscal dealFiscal : listaDeal) {
			listaDealMap.put(dealFiscal.getNomeDealFiscal(),
					dealFiscal.getCodigoDealFiscal());
			listaDealList.add(dealFiscal.getNomeDealFiscal());
		}

		receitaMoedaRow.setDealFiscalMapTo(listaDealMap);
		receitaMoedaRow.setListaDealFiscalFiltradaTo(listaDealList);
	}

	/**
	 * Carrega lista de AjusteReceita a partir de uma ReceitaMoeda.
	 *
	 * @param receitaMoeda
	 *            receitaMoeda.
	 * @return lista de ajusteReceita.
	 */
	private List<AjusteReceita> loadAjusteReceitaList(
			final ReceitaMoeda receitaMoeda) {
		List<AjusteReceita> ajusteReceitaList = new ArrayList<AjusteReceita>();

		List<ReceitaDealFiscal> listRecDealFiscal = receitaDealFiscalService
				.findReceitaDealFiscalByReceitaMoeda(receitaMoeda);

		for (ReceitaDealFiscal receitaDealFiscal : listRecDealFiscal) {

			for (AjusteReceita ajusteReceita : ajusteReceitaService
					.findAjusteReceitaByReceitaDealFiscal(receitaDealFiscal)) {
				ajusteReceitaList.add(ajusteReceita);
			}
		}

		for (int i = 0; i < ajusteReceitaList.size();/*
		 * incremento dentro do
		 * while
		 */) {
			ReceitaDealFiscal receitaDealFiscal = ajusteReceitaList.get(i)
					.getReceitaDealFiscal();

			BigDecimal ajuste = ajusteReceitaList.get(i).getValorAjuste();
			BigDecimal balanco = receitaDealFiscal.getValorReceita()
					.add(ajuste);

			ajusteReceitaList.get(i).setBalanco(balanco);

			// enquanto for o mesmo deal fiscal
			Long codigoFiscalDeal = receitaDealFiscal.getDealFiscal()
					.getCodigoDealFiscal();
			while (i < ajusteReceitaList.size()
					&& codigoFiscalDeal == ajusteReceitaList.get(i)
					.getReceitaDealFiscal().getDealFiscal()
					.getCodigoDealFiscal()) {
				// incremento
				i++;
				if (i < ajusteReceitaList.size()) {
					balanco = balanco.add(ajusteReceitaList.get(i)
							.getValorAjuste());
					ajusteReceitaList.get(i).setBalanco(balanco);
				}
			}
		}

		return ajusteReceitaList;
	}

	private List<AjusteReceitaRow> loadAjusteReceitaRowList(
			final ReceitaMoeda receitaMoeda) {

		List<AjusteReceitaRow> listResult = new ArrayList<AjusteReceitaRow>();

		List<AjusteReceita> ajusteReceitaList = ajusteReceitaService
				.findByReceitaMoeda(receitaMoeda);

		AjusteReceitaRow ajusteReceitaRowFilho;
		AjusteReceitaRow ajusteReceitaRowPai;
		AjusteReceita ar;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date changeDate = null;
		try {
			changeDate = sdf.parse("29/05/2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int k = 0; k < ajusteReceitaList.size(); k++) {

			ar = ajusteReceitaService.findAjusteReceitaById(ajusteReceitaList
					.get(k).getCodigoAjusteReceita());

			ajusteReceitaRowFilho = new AjusteReceitaRow();
			if (ar.getTipoAjuste().getCodigoTipoAjuste() == 3L
					&& ar.getDataCriacao().after(changeDate)) {
				ajusteReceitaRowFilho.setExibeLinha(false);
				ajusteReceitaRowFilho.setQuantidadeRowSpan(1);
				ajusteReceitaRowFilho.setDealReferencia(AjusteReceitaRow.NEW_DEAL);
				ajusteReceitaRowFilho.setAjusteReceita(ar);

				if (ar.getAjusteReceitaPai() != null) {
					ajusteReceitaRowPai = new AjusteReceitaRow();
					ajusteReceitaRowPai.setExibeLinha(true);
					ajusteReceitaRowPai.setQuantidadeRowSpan(2);
					ajusteReceitaRowPai.setDealReferencia(AjusteReceitaRow.ORIGINAL_DEAL);
					ajusteReceitaRowPai.setAjusteReceita(ar.getAjusteReceitaPai());

					listResult.add(ajusteReceitaRowPai);

					ajusteReceitaList
							.remove(ajusteReceitaRowPai.getAjusteReceita());
				}

				listResult.add(ajusteReceitaRowFilho);

			} else {
				ajusteReceitaRowFilho.setExibeLinha(true);
				ajusteReceitaRowFilho.setQuantidadeRowSpan(1);
				ajusteReceitaRowFilho.setAjusteReceita(ar);
				ajusteReceitaRowFilho.setDealReferencia(AjusteReceitaRow.EMPTY_DEAL);
				listResult.add(ajusteReceitaRowFilho);
			}
		}

		return listResult;
	}


	public void downloadForecastReport() {
		HSSFTemplate template = new HSSFTemplate();

		Date monthDate = DateUtil.getDate(bean.getValidityMonth(),
				bean.getValidityYear());

		List<VwPmsContratoProfitCenter> contratoProfitCenters = reportService.getRevenueForecast(monthDate);
		try {
			template.getRevenueForecastReport(contratoProfitCenters);
		} catch (IOException e) {
			Messages.showError("receitaRelatorio", Constants.RECEITA_MSG_ERROR_EXPORT_REPORT);
		}
	}

	public Boolean verifyClosingDateRevenueOrInternationalRevenue(final Date startDate,
																  final Boolean showMsgError,
																  final Boolean isInternationalClient) {
		String msgError = "";
		Date closingRevenueDate = null;

		if (isInternationalClient) {
			closingRevenueDate = moduloService.getClosingDateInternationalRevenue();
			msgError = Constants.MSG_ERROR_RECEITA_INTERNACIONAL_ADD_CLOS_DATE;
		} else {
			closingRevenueDate = moduloService.getClosingDateRevenue();
			msgError = Constants.MSG_ERROR_AJUSTE_RECEITA_ADD_CLOS_DATE;
		}

		if (startDate.compareTo(closingRevenueDate) > 0) {
			return Boolean.valueOf(true);
		} else {
			if (showMsgError) {
				Messages.showError("verifyClosingDateRevenue",
						msgError,
						DateUtil.formatDate(closingRevenueDate,
								Constants.DEFAULT_DATE_PATTERN_SIMPLE,
								Constants.DEFAULT_CALENDAR_LOCALE));
			}

			return Boolean.valueOf(false);
		}
	}

	/**
	 * @param msa - Msa to search price tables
	 */
	private void verifyPriceTableStatusByMsa(Msa msa){

		try{
			List<TabelaPreco> listPriceTable = tabelaPrecoService.findTabelaPrecoByMsa(msa);
			if(listPriceTable != null && !listPriceTable.isEmpty()){
				for (TabelaPreco priceTable : listPriceTable) {
					if(priceTable.getPriceTableStatusFlowPms() != null &&
							!Constants.APPROVED.equals(priceTable.getPriceTableStatusFlowPms().getAcronym()) &&
							!Constants.DELETED.equals(priceTable.getPriceTableStatusFlowPms().getAcronym())){

						Messages.showWarning("findPriceTableByMsaAndRevenue", Constants.MSG_WARN_REVENUE_UPDATE_VALUES_PRICE_TABLE_NOT_APPROVED);
						return;
					}
				}
			}
		}catch (Exception e){
			logger.error("Error trying to find Price Tables by Msa", e.getMessage());
		}
	}
}
