package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAreaOrcamentariaService;
import com.ciandt.pms.business.service.IAreaService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IConvergenciaService;
import com.ciandt.pms.business.service.ICostCenterHierarchyService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IFollowGrupoCustoService;
import com.ciandt.pms.business.service.IGrupoCustoAreaOrcamentariaService;
import com.ciandt.pms.business.service.IGrupoCustoPeriodoService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IGrupoCustoStatusService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.bean.GrupoCustoAreaOrcamentariaBean;
import com.ciandt.pms.control.jsf.bean.GrupoCustoBean;
import com.ciandt.pms.control.jsf.bean.MessageControlBean;
import com.ciandt.pms.control.jsf.components.impl.CostCenterHierarchySelect;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.message.dto.ContractLobDTO;
import com.ciandt.pms.message.dto.CostCenterDTO;
import com.ciandt.pms.model.Area;
import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.FollowGrupoCusto;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoAreaOrcamentariaAud;
import com.ciandt.pms.model.GrupoCustoAud;
import com.ciandt.pms.model.GrupoCustoPeriodo;
import com.ciandt.pms.model.GrupoCustoPeriodoAud;
import com.ciandt.pms.model.GrupoCustoStatus;
import com.ciandt.pms.model.HistoricoGrupoCusto;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.combo.ComboBox;
import com.ciandt.pms.model.vo.combo.type.AreaCombo;
import com.ciandt.pms.model.vo.combo.type.AreaOrcamentariaCombo;
import com.ciandt.pms.model.vo.combo.type.ComboFactory;
import com.ciandt.pms.model.vo.combo.type.GrupoCustoStatusCombo;
import com.ciandt.pms.model.vo.combo.type.PessoaCombo;
import com.ciandt.pms.persistence.dao.jpa.GrupoCustoAreaOrcamentaria;
import com.ciandt.pms.tree.interfaces.impl.ContractLobDtoTreeImpl;
import com.ciandt.pms.tree.interfaces.impl.CostCenterDtoTreeImpl;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.richfaces.component.html.HtmlTree;
import org.richfaces.event.NodeSelectedEvent;
import org.richfaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Define o Controller da entidade (Cost Center).
 *
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BUS.COST_CENTER:VW", "BUS.COST_CENTER:ED", "BUS.COST_CENTER:DE", "BUS.COST_CENTER.TRAVEL_BUDGET:ED",
				"BOF.COST_CENTER:VW", "BOF.COST_CENTER:ED", "BOF.COST_CENTER:DE", "BOF.COST_CENTER.TRAVEL_BUDGET:ED",
		        "BUS.COST_CENTER.ADDS:VW", "BUS.COST_CENTER.ADDS:ED", "BUS.COST_CENTER.MANAGER:VW",
		        "BUS.COST_CENTER.MANAGER:ED", "BUS.COST_CENTER.MANAGER:CR"})
public class GrupoCustoController {

	public static final String ANALYTICAL = "Analytical";
	public static final String SYNTHETIC = "Synthetic";
	/** Logger. */
	private final Logger log = Logger.getLogger(CostCenterController.class
			.getName());

	/**
	 * Maximo de caracteres para a sigla do centro de custo
	 */
	private static final int LIMITE_SIGLA = 10;
	/**
	 * Maximo de caracteres para cada token
	 */
	private static final int TOKEN_MAXIMO = 3;

	/** outcome tela criacao da entidade. */
	private static final String OUTCOME_COST_CENTER_ADD = "costCenter_add";

	/** outcome tela alteracao da entidade. */
	private static final String OUTCOME_COST_CENTER_EDIT = "costCenter_edit";

	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_COST_CENTER_SEARCH = "costCenter_search";

	/** outcome tela de view da entidade. */
	private static final String OUTCOME_COST_CENTER_VIEW = "costCenter_view";

	/** Instancia do servico. */
	@Autowired
	private IGrupoCustoService grupoCustoService;

	@Autowired
	private ICostCenterHierarchyService costCenterHierarchyService;

	@Autowired
	private IContratoPraticaService contractLobService;

	/** Instancia do servico GrupoCustoPeriodo. */
	@Autowired
	private IGrupoCustoPeriodoService grupoCustoPeriodoService;

	/** Instancia do servico Area. */
	@Autowired
	private IAreaService areaService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do servico de follorGrupoCusto. */
	@Autowired
	private IFollowGrupoCustoService followGrupoCustoService;

	/** Instancia do servico de Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico de {@link NaturezaCentroLucro}. */
	@Autowired
	private INaturezaCentroLucroService naturezaCentroLucroService;

	/** Instancia do servico de {@link GrupoCustoPeriodo}. */
	@Autowired
	private IGrupoCustoPeriodoService gcustoPeriodoService;

	/** Instancia do bean. */
	@Autowired
	private GrupoCustoBean bean = null;

	/** Instancia do bean. */
	@Autowired
	private GrupoCustoAreaOrcamentariaBean gcaoBean = null;

	/** Instancia do Controller. */
	@Autowired
	private ActiveProjectController activeProjectController;

	@Autowired
	private IConvergenciaService convergenciaService;

	@Autowired
	private IAreaOrcamentariaService areaOrcamentariaService;

	@Autowired
	private IEmpresaService empresaService;

	@Autowired
	private IGrupoCustoAreaOrcamentariaService grupoCustoAreaOrcamentariaService;

	@Autowired
	private IGrupoCustoStatusService grupoCustoStatusService;

	private boolean enabledFieldArea = false;
	private boolean enabledProfitCenter = false;
	private boolean enabledButtonAdd = true;
	private boolean enabledButtonDelete = true;


	private String statusInfoMessage = null;

	/** Instancia do bean controle de mensagnes. */
	@Autowired
	private MessageControlBean messageConntrolBean = null;


	/**
	 * @return the bean
	 */
	public GrupoCustoBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final GrupoCustoBean bean) {
		this.bean = bean;
	}

	/**
	 * @return the gcaoBean
	 */
	public GrupoCustoAreaOrcamentariaBean getGcaoBean() {
		return gcaoBean;
	}

	/**
	 * @param gcaoBean the gcaoBean to set
	 */
	public void setGcaoBean(GrupoCustoAreaOrcamentariaBean gcaoBean) {
		this.gcaoBean = gcaoBean;
	}

	public boolean isEnabledFieldArea() {
		return enabledFieldArea;
	}

	public void setEnabledFieldArea(boolean enabledFieldArea) {
		this.enabledFieldArea = enabledFieldArea;
	}

	public boolean isEnabledProfitCenter() {
		return enabledProfitCenter;
	}

	public void setEnabledProfitCenter(boolean enabledProfitCenter) {
		this.enabledProfitCenter = enabledProfitCenter;
	}

	public boolean isEnabledButtonAdd() {
		return enabledButtonAdd;
	}

	public void setEnabledButtonAdd(boolean enabledButtonAdd) {
		this.enabledButtonAdd = enabledButtonAdd;
	}

	public boolean isEnabledButtonDelete() {
		return enabledButtonDelete;
	}

	public void setEnabledButtonDelete(boolean enabledButtonDelete) {
		this.enabledButtonDelete = enabledButtonDelete;
	}

	/**
	 * Prepara a tela de pesquisa da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareSearch() {
		bean.reset();
		messageConntrolBean.setDisplayMessages(Boolean.TRUE);

		// Carrega o map de seguidor.
		bean.setMapFollowGrupoCusto(this.mapFollowGrupoCusto(LoginUtil
				.getLoggedUser()));
		setStatusInfoMessage("");
		return OUTCOME_COST_CENTER_SEARCH;
	}

	public String backCostCenter() {
		this.findByFilter();
		messageConntrolBean.setDisplayMessages(Boolean.TRUE);
		return OUTCOME_COST_CENTER_SEARCH;
	}

	/**
	 * Constroi os combos de Manager da tela.
	 */
	private void buildManagerCombos() {
		List<Pessoa> pessoas = pessoaService.findAllForComboBox();
		List<Pessoa> gerentes = pessoaService.findAllManagerForComboBox();

		// combo de Approver
		ComboBox<Pessoa> approvers = ComboFactory.create(new PessoaCombo(
				pessoas));
		bean.setApproverCombo(approvers);

		// combo de Manager Approver
		ComboBox<Pessoa> managers = ComboFactory.create(new PessoaCombo(
				gerentes));
		bean.setManagerApproverCombo(managers);
	}

	private void buildAccountTypeCombos() {
		Map<String, String> mapType = getOptionsAccountType();

		ComboBox<String> combo = new ComboBox<>();

		combo.createComboBox(mapType);
		bean.setAccountTypeSelect(combo);
	}

	private Map<String, String> getOptionsAccountType() {
		Map<String, String> mapType = new LinkedHashMap<>();
		mapType.put(ANALYTICAL, "A");
		mapType.put(SYNTHETIC, "S");
		return mapType;
	}

	public void onNodeSelectCostCenter(NodeSelectedEvent evt){
		HtmlTree tree = (HtmlTree) evt.getComponent();
		TreeNode node = tree.getModelTreeNode(tree.getRowKey());
		CostCenterDTO selected = (CostCenterDTO) node.getData();
		if (selected.getShortCode() != null && !selected.getShortCode().equals("0")) {
			bean.setParentCostCenterSelected(selected);
		} else {
			bean.setParentCostCenterSelected(new CostCenterDTO());
		}
	}

	public void onSelectAccountType(ValueChangeEvent evt) {
		if (evt.getNewValue().equals(ANALYTICAL)) {
			loadParentOverheadProject(bean.getTo());
		} else {
			bean.getTo().setEnabledParentOverheadProject(Boolean.FALSE);
		}
	}

	public void onNodeSelectOverheadProject(NodeSelectedEvent evt){
		HtmlTree tree = (HtmlTree) evt.getComponent();
		TreeNode node = tree.getModelTreeNode(tree.getRowKey());
		ContractLobDTO selected = (ContractLobDTO) node.getData();
		if (selected.getShortCode() != null && !selected.getShortCode().equals("0")) {
			bean.setParentOverheadProjectSelected(selected);
		} else {
			bean.setParentOverheadProjectSelected(new ContractLobDTO());
		}

	}


	private void loadParentCostCenterList(List<CostCenterDTO> listExternal) {
		try{
			bean.setParentCostCenterTree(new CostCenterDtoTreeImpl(listExternal).build());
		}catch (Exception e){
			Messages.showWarning("treeSelectionCostCenter",Constants.LIST_COST_CENTER_LOAD_INTEGRATE_ERROR);
			log.info("List of actives cost center not found: "+ e.getMessage());
		}
	}

	private void loadParentOverheadProjectList(List<ContractLobDTO> listExternal) {
		try{
			bean.setParentOverheadProjectTree(new ContractLobDtoTreeImpl(listExternal).build());
		} catch (Exception e){
			Messages.showWarning("treeSelectionOverheadProject",Constants.LIST_PROJECT_LOAD_INTEGRATE_ERROR);
			log.info("List of actives projects not found: "+ e.getMessage());
		}
	}


	/**
	 * Verifica se o valor do atributo ContratoPratica ? valido. Se o valor n?o
	 * ? nulo e existe no contratoPraticaMap, ent?o o valor ? valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateDataImplantacao(final FacesContext context,
									   final UIComponent component,
									   final Object value) throws IOException {
		Date dtValue = (Date) value;

		if (dtValue != null) {
			bean.getTo().setDataImplantacao(dtValue);
		}
	}



	private void buildStatusCombo() {
		List<GrupoCustoStatus> grupoCustoSigla;

		GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(bean.getTo().getCodigoGrupoCusto());
		GrupoCustoStatus grupoCustoStatus = grupoCusto.getGrupoCustoStatus();

		List<String> siglaList = getStatusList(grupoCustoStatus.getSiglaStatusGrupoCusto());

		if (siglaList.isEmpty()) {
			grupoCustoSigla = grupoCustoStatusService.findAllActive();

		} else {
			grupoCustoSigla = grupoCustoStatusService.findBySiglaInList(siglaList);
		}

		ComboBox<GrupoCustoStatus> grupoCustoStatusComboBox = ComboFactory.create(new GrupoCustoStatusCombo(grupoCustoSigla));
		bean.setStatusCombo(grupoCustoStatusComboBox);
		bean.setDisableStatusCombo(disableStatusCombo(grupoCusto));

		if(grupoCustoStatus.getSiglaStatusGrupoCusto().equals(Constants.NEW_INACTIVE)){
			bean.getTo().setEnabledFieldInactivationDate(Boolean.TRUE);
			bean.getTo().setDisabledFieldInactivationDate(Boolean.FALSE);
		}else{
			bean.getTo().setEnabledFieldInactivationDate(Boolean.FALSE);
			bean.getTo().setDisabledFieldInactivationDate(Boolean.TRUE);
		}
	}

	private List<String> getStatusList(String currentStatus) {
		List<String> list = new ArrayList<>();

		if (Boolean.TRUE.equals(isControllership())) {
			if (currentStatus.equals(Constants.NEW_ACTIVE) || currentStatus.equals(Constants.REQUEST_INACTIVATION)) {
				list.add(Constants.NEW_ACTIVE);
				list.add(Constants.CLOSED);
				list.add(Constants.REQUEST_INACTIVATION);
				list.add(Constants.INTEGRATING_INACTIVATION);
			}
			if (currentStatus.equals(Constants.REQUEST_CREATION)) {
				list.add(Constants.REQUEST_CREATION);
				list.add(Constants.INTEGRATING_CREATION);
			}
			if (currentStatus.equals(Constants.NEW_INACTIVE)) {
				list.add(Constants.NEW_INACTIVE);
				list.add(Constants.NEW_ACTIVE);
				list.add(Constants.CLOSED);
			}
		} else {
			if (currentStatus.equals(Constants.NEW_ACTIVE)) {
				list.add(Constants.NEW_ACTIVE);
				list.add(Constants.REQUEST_INACTIVATION);
			}
		}

		if (currentStatus.equals(Constants.INTEGRATING_CREATION)) {
			list.add(Constants.INTEGRATING_CREATION);
			list.add(Constants.REQUEST_CREATION);
		}
		if (currentStatus.equals(Constants.INTEGRATING_INACTIVATION)) {
			list.add(Constants.INTEGRATING_INACTIVATION);
			list.add(Constants.REQUEST_INACTIVATION);
		}

		return list;
	}

	public Boolean disableStatusCombo(GrupoCusto grupoCusto) {

		String status = grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto();

		if(status.equals(Constants.REQUEST_CREATION) && Boolean.FALSE.equals(isControllership())){
			return Boolean.TRUE;
		}

		return (status.equals(Constants.INTEGRATING_INACTIVATION) || status.equals(Constants.INTEGRATING_CREATION)) &&
				grupoCusto.getRequestIntegration().equals(Constants.YES);
	}

	/**
	 * Constroi o combos de Area da tela.
	 */
	private void buildAreaCombo() {
		List<Area> areasAtivas = areaService.findAreaAllActive();
		ComboBox<Area> areas = ComboFactory.create(new AreaCombo(areasAtivas));
		bean.setAreaCombo(areas);
	}

	/**
	 * Prepara a tela de insercao da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareCreate() {
		bean.reset();
		this.buildManagerCombos();
		this.buildAccountTypeCombos();
		this.buildAreaCombo();
		this.loadCostCenterHierarchyListCreateCostCenter();
		return OUTCOME_COST_CENTER_ADD;
	}

	private void loadCostCenterHierarchyListCreateCostCenter() {
		bean.setCostCenterHierarchySelect(new CostCenterHierarchySelect(costCenterHierarchyService.findAllActive()));
	}

	/**
	 * Insere uma entidade.
	 *
	 * @return retorna a pagina de destino
	 */
	@Transactional
	public String createGrupoCusto() {
		Date startDate = DateUtil
				.getDate(bean.getMonthBeg(), bean.getYearBeg());
		// verifica o History Lock. Se startDate nao for valido, mensagem de
		// erro
		if (!moduloService.verifyHistoryLock(startDate, Boolean.TRUE, Boolean.FALSE)) {
			return null;
		}

		// pega a area selecionada
		Area areaSelecionada = bean.getAreaCombo().getSelectedItem();

		// obtem o aprovador selecionado
		Pessoa aprovadorSelecionado = bean.getApproverCombo().getSelectedItem();

		// obtem o gerente aprovador selecionado
		Pessoa gerenteAprovadorSelecionado = bean.getManagerApproverCombo()
				.getSelectedItem();

		// Monta o grupo de custo
		GrupoCusto grupoCusto = bean.getTo();
		grupoCusto.setArea(areaSelecionada);
		grupoCusto.setAprovador(aprovadorSelecionado);
		grupoCusto.setGerenteAprovador(gerenteAprovadorSelecionado);
		try {
			grupoCusto.setCostCenterHierarchy(bean.getCostCenterHierarchySelect().value() != null ?
					costCenterHierarchyService.findByCode(bean.getCostCenterHierarchySelect().value()) : null);
		} catch (BusinessException e) {
			log.warning(e.getMessage());
			Messages.showError("createGrupoCusto",
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
					Constants.ENTITY_NAME_COST_CENTER_HIERARCHY);
			return null;
		}

		// cria o grupo de custo
		try {
			grupoCustoService.create(grupoCusto);
		} catch (IntegrityConstraintException e) {
			log.warning(e.getMessage());
			Messages.showSucess("activate",
					Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
		}

		Messages.showSucess("createGrupoCusto",
				Constants.DEFAULT_MSG_SUCCESS_CREATE,
				Constants.ENTITY_NAME_GRUPO_CUSTO);

		return prepareCreate();
	}
	@Transactional
	public String requestCreationGrupoCusto() {

		GrupoCusto grupoCusto = new GrupoCusto();
		grupoCusto.setNomeGrupoCusto(bean.getTo().getNomeGrupoCusto());
		grupoCusto.setAprovador(bean.getApproverCombo().getSelectedItem());
		grupoCusto.setGerenteAprovador(bean.getManagerApproverCombo().getSelectedItem());
		grupoCusto.setErpTipoConta(bean.getAccountTypeSelect().getSelectedItem());
		grupoCusto.setDescricaoGrupoCusto(bean.getTo().getDescricaoGrupoCusto());
		grupoCusto.setIndicadorDeletado(Constants.NO);
		grupoCusto.setIndicadorAtivo(Constants.INACTIVE);
		grupoCusto.setDataImplantacao(bean.getTo().getDataImplantacao());
		GrupoCustoStatus grupoCustoStatus = grupoCustoStatusService.findBySiglaStatusGrupoCusto(Constants.REQUEST_CREATION);
		grupoCusto.setGrupoCustoStatus(grupoCustoStatus);
		grupoCusto.setRequestIntegration(Constants.NO);
		try {
			grupoCusto.setCostCenterHierarchy(bean.getCostCenterHierarchySelect().value() != null ?
					costCenterHierarchyService.findByCode(bean.getCostCenterHierarchySelect().value()) : null);
		} catch (BusinessException e) {
			log.warning(e.getMessage());
			Messages.showError("requestCreationGrupoCusto",
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
					Constants.ENTITY_NAME_COST_CENTER_HIERARCHY);
			return null;
		}
		try {
			grupoCustoService.create(grupoCusto);
		} catch (IntegrityConstraintException e) {
			log.warning(e.getMessage());
			Messages.showError("requestCreationGrupoCusto",
					Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
			return null;
		} catch (DataIntegrityViolationException e) {
			log.warning(e.getMessage());
			Messages.showError("requestCreationGrupoCusto",
					Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
			return null;
		}

		Messages.showSucess("requestCreationGrupoCusto",
				Constants.DEFAULT_MSG_SUCCESS_CREATE,
				Constants.ENTITY_NAME_GRUPO_CUSTO);

		grupoCustoService.sendEmailToControladoria(
				BundleUtil.getBundle(Constants.EMAIL_ASSUNTO_CRIACAO_GRUPO_CUSTO),
				BundleUtil.getBundle(
						Constants.EMAIL_MSG_CRIACAO_GRUPO_CUSTO,
						grupoCusto.getNomeGrupoCusto(), LoginUtil.getLoggedUser().getCodigoLogin()));

		return prepareCreate();
	}

	/**
	 * Insere uma entidade GrupoCustoPeriodo.
	 *
	 */
	@Transactional
	public void createGrupoCustoPeriodo() {
		Date startDate = DateUtil
				.getDate(bean.getMonthBeg(), bean.getYearBeg());

		if (startDate != null) {

			// verifica o History Lock. Se startDate nao for valido, da mensagem
			// de erro
			if (!moduloService.verifyHistoryLock(startDate, Boolean.TRUE,Boolean.FALSE)) {
				return;
			}

			GrupoCustoPeriodo grupoCustoPeriodo = new GrupoCustoPeriodo();
			grupoCustoPeriodo.setGrupoCusto(bean.getTo());
			grupoCustoPeriodo.setDataInicio(startDate);

			if (grupoCustoPeriodoService
					.createGrupoCustoPeriodo(grupoCustoPeriodo)) {
				Messages.showSucess("create",
						Constants.DEFAULT_MSG_SUCCESS_CREATE,
						Constants.ENTITY_NAME_GRUPO_CUSTO_PERIODO);

				GrupoCusto grupoCusto = grupoCustoService
						.findGrupoCustoByIdWithPeriodos(bean.getTo()
								.getCodigoGrupoCusto());

				bean.getTo().setGrupoCustoPeriodos(getGrupoCustoPeriodos());
			}
		} else {
			Messages.showError("createGrupoCustoPeriodo",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.LABEL_GRUPO_CUSTO_PERIODO_PERIOD);
		}
	}

	/**
	 * Executa um update da entidade.
	 *
	 * @return pagina de destino
	 *
	 */
	@Transactional
	public String update() {

		if (bean.getTo().getChangeStatusCostCenter() != null
				&& !bean.getTo().getChangeStatusCostCenter()
				&& bean.getTo().getHasBudgetArea() != null
				&& bean.getTo().getHasBudgetArea()
				&& bean.getTo().getIsInactivationProcess() != null
				&& bean.getTo().getIsInactivationProcess()) {
			return null;
		} else {
			try {
				bean.getTo().setCostCenterHierarchy(bean.getCostCenterHierarchySelect().value() != null ?
						costCenterHierarchyService.findByCode(bean.getCostCenterHierarchySelect().value())
						:null);

				validateBeforeUpdate();

				GrupoCusto grupoCusto = bean.getTo();

				// pega a area selecionada
				Area areaSelecionada = bean.getAreaCombo().getSelectedItem();

				// obtem o aprovador selecionado
				Pessoa aprovadorSelecionado = bean.getApproverCombo()
						.getSelectedItem();

				// obtem o gerente aprovador selecionado
				Pessoa gerenteAprovadorSelecionado = bean.getManagerApproverCombo()
						.getSelectedItem();

				if (bean.getStatusCombo().getSelected() != null) {
					GrupoCustoStatus grupoCustoStatus = grupoCustoStatusService.findBySiglaStatusGrupoCusto(bean.getStatusCombo().getSelected());
					grupoCusto.setGrupoCustoStatus(grupoCustoStatus);
				}

				// Monta o grupo de custo
				grupoCusto.setArea(areaSelecionada);
				grupoCusto.setAprovador(aprovadorSelecionado);
				grupoCusto.setDataImplantacao(bean.getTo().getDataImplantacao());
				grupoCusto.setDataInativacao(bean.getTo().getDataInativacao());
				grupoCusto.setErpCodigoCentroCustoPai(Long.parseLong(bean.getParentCostCenterSelected().getShortCode()));
				grupoCusto.setErpCentroCustoPadrao(grupoCusto.getErpCentroCustoPadrao() == null ? 1L : grupoCusto.getErpCentroCustoPadrao());
				grupoCusto.setErpCodigoIdePai(Long.parseLong(bean.getParentCostCenterSelected().getIdeCode()));
				grupoCusto.setErpTipoConta(bean.getAccountTypeSelect().getSelectedItem());
				Pessoa gerenteAprovador = pessoaService.findPessoaByLogin(gerenteAprovadorSelecionado.getCodigoLogin());
				grupoCusto.setGerenteAprovador(gerenteAprovador);

				if (isTypeAnalytical() &&
					(grupoCusto.getErpCodigoProjetoOverheadPai() == null || grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.REQUEST_CREATION)|| grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.INTEGRATING_CREATION))) {
					grupoCusto.setErpCodigoProjetoOverheadPai(Long.parseLong(bean.getParentOverheadProjectSelected().getShortCode()));
					grupoCusto.setErpCodigoIdePaiProjetoOverhead(Long.parseLong(bean.getParentOverheadProjectSelected().getIdeCode()));
				}

				List<String> shouldActive = new ArrayList<>();
				shouldActive.add(Constants.NEW_INACTIVE);
				shouldActive.add(Constants.REQUEST_INACTIVATION);

				if (!shouldActive.contains(grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto())) {
					grupoCusto.setIndicadorAtivo(Constants.INACTIVE);
				}

				if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.NEW_ACTIVE)) {
					grupoCusto.setIndicadorAtivo(Constants.ACTIVE);
					grupoCusto.setDataInativacao(null);
				}

				grupoCustoService.checkGrupoCustoAreaOrcamentaria(grupoCusto);

				// atualiza grupo de custo com seus respectivos centros de lucro
				grupoCustoService.updateGrupoCusto(grupoCusto);

				Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
						Constants.ENTITY_NAME_GRUPO_CUSTO);

				findByFilter();

				return OUTCOME_COST_CENTER_SEARCH;
			} catch (BusinessException e) {
				log.warning(e.getMessage());
				Messages.showError("update",
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
						Constants.ENTITY_NAME_COST_CENTER_HIERARCHY);

				return null;
			} catch (IntegrityConstraintException ice) {
				Messages.showError("update", ice.getMessage(), new Object[]{
						Constants.ENTITY_NAME_GRUPO_CUSTO,
						Constants.ENTITY_NAME_GRUPO_CUSTO_PERIODO});

				return null;
			}
		}
	}

	private void validateBeforeUpdate() throws IntegrityConstraintException {

		if(isControllership()){
			if (bean.getParentCostCenterSelected() == null || bean.getParentCostCenterSelected().getShortCode() == null) {
				throw new IntegrityConstraintException(Constants.MSG_PARENT_COST_CENTER_REQUIRED);
			}

			if (isTypeAnalytical() &&
					(bean.getParentOverheadProjectSelected() == null || bean.getParentOverheadProjectSelected().getShortCode() == null)) {
				throw new IntegrityConstraintException(Constants.MSG_PARENT_OVERHEAD_PROJECT_REQUIRED);
			}
		}

		if (bean.getStatusCombo().getSelected() != null && bean.getStatusCombo().getSelected().equals(Constants.INTEGRATING_INACTIVATION)) {
			if (bean.getDataInativacao() == null) {
				throw new IntegrityConstraintException(Constants.MSG_DATA_INACTIVATION_GRUPO_CUSTO);
			}
			if (Boolean.TRUE.equals(DateUtil.before(new Date(), bean.getDataInativacao()))) {
				throw new IntegrityConstraintException(Constants.MSG_DATA_INACTIVATION_ATUAL_FUTURA_GRUPO_CUSTO);
			}

		}

		if(bean.getTo().getCostCenterHierarchy() == null
				|| Boolean.FALSE.equals(bean.getTo().getCostCenterHierarchy().getInActive()))
			throw new IntegrityConstraintException(Constants.COST_CENTER_HIERARCHY_WARN_INACTIVE_SELECTED);

	}

	private boolean isTypeAnalytical() {
		return bean.getAccountTypeSelect().getSelectedItem().equals("A");
	}

	/**
	 * Deleta uma entidade.
	 *
	 * @return pagina de destino
	 *
	 */
	@Transactional
	public String remove() {
		try {
			grupoCustoService.deleteLogico(bean.getTo());
		} catch (IntegrityConstraintException e) {
			Messages.showError(
					"remove",
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					new Object[] { bean.getTo().getNomeGrupoCusto(),
							e.getMessage() });
			return null;
		} catch (Exception e) {
			Messages.showError("remove",
					Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					new Object[] { Constants.ENTITY_NAME_GRUPO_CUSTO,
							Constants.ENTITY_NAME_DESPESA });
			return null;
		}
		Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
				Constants.ENTITY_NAME_GRUPO_CUSTO);
		bean.resetTo();
		findByFilter();
		return OUTCOME_COST_CENTER_SEARCH;
	}

	/**
	 * Prepara a tela de visualizacao da entidade.
	 *
	 * @return retorna a pagina de visualizacao do GrupoCusto
	 */
	public String prepareView() {
		bean.setIsRemove(Boolean.FALSE);

		GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(bean
				.getTo().getCodigoGrupoCusto());

		List<CostCenterDTO> costCenterExternalList = getExternalCostCentList();
		List<ContractLobDTO> overheadProjectList = getExternalOverheadProjectList();

		loadParentCostCenterSelected(grupoCusto, costCenterExternalList);
		loadParentOverheadProjectSelected(grupoCusto, overheadProjectList);

		loadGrupoCustoPanel(grupoCusto);

		prepareGrupoCustoUpdateCreate(grupoCusto);

		return OUTCOME_COST_CENTER_VIEW;
	}

	/**
	 * Prepara a tela de remocao da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareRemove() {
		bean.setIsRemove(Boolean.TRUE);
		GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(bean
				.getTo().getCodigoGrupoCusto());
		loadGrupoCustoPanel(grupoCusto);
		prepareGrupoCustoUpdateCreate(grupoCusto);

		return OUTCOME_COST_CENTER_VIEW;
	}

	private void prepareGrupoCustoUpdateCreate(GrupoCusto grupoCusto) {
		grupoCustoService.prepareUpdateGrupoCusto(grupoCusto);

		grupoCustoService.orderGrupoCustoHierarchyList(grupoCusto);

		bean.setTo(grupoCusto);
	}

	/**
	 * Prepara a tela de edicao da entidade a partir de um grupo de custo.
	 *
	 * @param grupoCusto
	 */
	public void prepareUpdate(GrupoCusto grupoCusto) {
		bean.getTo().setCodigoGrupoCusto(grupoCusto.getCodigoGrupoCusto());
		bean.getTo().setDataInativacao(grupoCusto.getDataInativacao());
		prepareUpdate();
		bean.setBackButtonOutcome("costcenter_pendingSearch");
	}

	/**
	 * Prepara a tela de edicao da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareUpdate() {

		bean.setBackButtonOutcome(OUTCOME_COST_CENTER_SEARCH);
		bean.setIsUpdate(Boolean.TRUE);
		bean.setHasPermissionToEditFields(hasPermissionToEditFields());

		buildAreaCombo();
		buildManagerCombos();
		buildAccountTypeCombos();

		buildStatusCombo();

		GrupoCusto grupoCusto = loadGrupoCustoUpdate();
		bean.setDisableStatusCombo(disableStatusCombo(grupoCusto));
		bean.setDisabledStartDate(!enabledStartDate(grupoCusto));
		bean.setCostCenterHierarchySelect(new CostCenterHierarchySelect(costCenterHierarchyService
				.findAllActiveIncludingCurrentIfInactive(grupoCusto.getCostCenterHierarchy())));
		bean.getCostCenterHierarchySelect()
				.select(grupoCusto.getCostCenterHierarchy() != null ? grupoCusto.getCostCenterHierarchy().getCode() : null);

		if (grupoCusto.getCostCenterHierarchy() != null && Boolean.FALSE.equals(grupoCusto.getCostCenterHierarchy().getInActive())) {
			Messages.showWarning("prepareUpdate", Constants.COST_CENTER_HIERARCHY_WARN_INACTIVE_SELECTED);
		}

		loadParentCostCenter(grupoCusto);

		if (grupoCusto.getErpTipoConta() != null && grupoCusto.getErpTipoConta().equals("A")) {
			loadParentOverheadProject(grupoCusto);
		}

		loadGrupoCustoPanel(grupoCusto);

		setSiglaDefault(grupoCusto);
		setStatusInfoMessageControl(grupoCusto);
		// guarda a data do HistoryLock
		bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());
		disableFieldAndButtons(bean.getTo());
		return OUTCOME_COST_CENTER_EDIT;
	}

	private void loadParentOverheadProject(GrupoCusto grupoCusto) {
		List<ContractLobDTO> overheadProjectList = getExternalOverheadProjectList();
		loadParentOverheadProjectList(overheadProjectList);
		loadParentOverheadProjectSelected(grupoCusto, overheadProjectList);
		bean.getTo().setEnabledParentOverheadProject(Boolean.TRUE);
	}

	private void loadParentCostCenter(GrupoCusto grupoCusto) {
		List<CostCenterDTO> costCenterExternalList = getExternalCostCentList();
		loadParentCostCenterList(costCenterExternalList);
		loadParentCostCenterSelected(grupoCusto, costCenterExternalList);
	}

	private void loadParentCostCenterSelected(GrupoCusto grupoCusto, List<CostCenterDTO> costCenterExternalList) {
		bean.setParentCostCenterSelected(new CostCenterDTO());
		if (grupoCusto.getErpCodigoCentroCustoPai() != null && costCenterExternalList != null) {
			for (CostCenterDTO costCenterDTO : costCenterExternalList) {
				if (costCenterDTO.getShortCode().equals(grupoCusto.getErpCodigoCentroCustoPai().toString())) {
					bean.setParentCostCenterSelected(costCenterDTO);
				}
			}
		}
	}

	private void loadParentOverheadProjectSelected(GrupoCusto grupoCusto, List<ContractLobDTO> projectExternalList) {
		bean.setParentOverheadProjectSelected(new ContractLobDTO());
		if (grupoCusto.getErpCodigoProjetoOverheadPai() != null && projectExternalList != null) {
			for (ContractLobDTO contractLobDTO : projectExternalList) {
				if (contractLobDTO.getShortCode().equals(grupoCusto.getErpCodigoProjetoOverheadPai().toString())) {
					bean.setParentOverheadProjectSelected(contractLobDTO);
				}
			}
		}
	}

	private List<CostCenterDTO> getExternalCostCentList() {
		return grupoCustoService.findAllWithExternalRestRequest();
	}

	private List<ContractLobDTO> getExternalOverheadProjectList() {
		return contractLobService.findAllWithExternalRestRequest();
	}


	private void setStatusInfoMessageControl(GrupoCusto grupoCusto) {

		if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.NEW_INACTIVE)) {
			setStatusInfoMessage(BundleUtil.getBundle(Constants.STATUS_INFO_MESSAGE));
		}else{
			setStatusInfoMessage("");
		}

	}

	private void setSiglaDefault(GrupoCusto grupoCusto) {
		//Abrevia o nome do centro do custo se nao possuir valor
		if (grupoCusto.getSiglaGrupoCusto() == null) {
			StringBuilder bd = new StringBuilder();
			String nomeGC = StringUtils.isEmpty(grupoCusto.getNomeGrupoCusto()) ? StringUtils.EMPTY : grupoCusto.getNomeGrupoCusto();

			//Se o nome for possuir 10 caracters ou menos, copia o nome
			if(nomeGC.length() <= 10) {
				bd.append(nomeGC);
			} else{
				//Separa o nome em tokens de utilizando '_' como delimitador
				StringTokenizer tokenizer = new StringTokenizer(nomeGC, "[_/]");

				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					//Para cada token o tamanho maximo e tres caracteres
					int len = token.length() > TOKEN_MAXIMO ? 3 : token
							.length();
					bd.append(token.substring(0, len));

					// se o tamanho superar os 10 caracteres (limite para o
					// campo sigla), remove caracteres adicionais
					if (bd.length() > LIMITE_SIGLA) {
						bd.delete(LIMITE_SIGLA, bd.length());
					}
					// sai do loop caso encontre o limite maximo para o campo de
					// sigla
					if (bd.length() == LIMITE_SIGLA) {
						break;
					}
				}
			}

			String sg = bd.toString();
			bean.getTo().setSiglaGrupoCusto(sg);
		}
	}

	/**
	 * Prepara a painel da tela de de remocao e visualizacao
	 *
	 * @param grupoCusto
	 */
	private void loadGrupoCustoPanel(GrupoCusto grupoCusto) {
		if (grupoCusto.getAprovador() != null)
			bean.getApproverCombo().setSelected(
					grupoCusto.getAprovador().getCodigoLogin());

		if (grupoCusto.getGerenteAprovador() != null)
			bean.getManagerApproverCombo().setSelected(
					grupoCusto.getGerenteAprovador().getCodigoLogin());

		if (grupoCusto.getArea() != null)
			bean.getAreaCombo().setSelected(grupoCusto.getArea().getNomeArea());


		if (grupoCusto.getGrupoCustoStatus() != null)
			bean.getStatusCombo().setSelected(grupoCusto.getGrupoCustoStatus().getNomeStatus());

		if (grupoCusto.getErpTipoConta() != null) {
			if (grupoCusto.getErpTipoConta().equals("A")) {
				bean.getAccountTypeSelect().setSelected(ANALYTICAL);
			}
			if (grupoCusto.getErpTipoConta().equals("S")) {
				bean.getAccountTypeSelect().setSelected(SYNTHETIC);
			}
		}

		// popula o to na tela para ser exibido
		bean.setTo(grupoCusto);
	}

	/**
	 * Busca uma entidade pelo id.
	 *
	 * @param id
	 *            da entidade.
	 *
	 */
	public void findById(final Long id) {
		bean.setTo(grupoCustoService.findGrupoCustoById(id));
		if (bean.getTo() == null || bean.getTo().getCodigoGrupoCusto() == null) {
			Messages.showWarning("findById",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
	}

	/**
	 * Deleta uma entidade.
	 *
	 */
	@Transactional
	public void removeGrupoCustoPeriodo() {
		if (grupoCustoPeriodoService
				.removeGrupoCustoPeriodo(grupoCustoPeriodoService
						.findGrupoCustoPeriodoById(bean.getToGCPeriodo()
								.getCodigoGcPeriodo()))) {
			Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_GRUPO_CUSTO_PERIODO);

			bean.getTo().setGrupoCustoPeriodos(getGrupoCustoPeriodos());
		}
	}

	private List<GrupoCustoPeriodo> getGrupoCustoPeriodos() {
		GrupoCusto grupoCusto = grupoCustoService
				.findGrupoCustoByIdWithPeriodos(bean.getTo()
						.getCodigoGrupoCusto());

		grupoCustoService.prepareUpdateGrupoCusto(grupoCusto);

		grupoCustoService.orderGrupoCustoHierarchyList(grupoCusto);

		return grupoCusto.getGrupoCustoPeriodos();
	}

	private List<GrupoCustoAreaOrcamentaria> getGrupoCustoAreaOrcamentaria() {
		GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(bean.getTo().getCodigoGrupoCusto());

		grupoCustoService.checkGrupoCustoAreaOrcamentaria(grupoCusto);

		return grupoCusto.getGrupoCustoAreaOrcamentarias();
	}

	/**
	 * Carrega os conteudo de um GrupoCusto (GrupoCustoPeriodo,
	 * GrupoCustoCentroLucro), para exibir na tela.
	 *
	 * @return
	 *
	 */
	private GrupoCusto loadGrupoCustoUpdate() {
		GrupoCusto grupoCusto = grupoCustoService
				.findGrupoCustoByIdWithPeriodos(bean.getTo()
						.getCodigoGrupoCusto());

		// prepara os dados para serem exibidos na tela de edicao do GrupoCusto
		grupoCustoService.prepareUpdateGrupoCusto(grupoCusto);

		// ordena as listas de GrupoCustoPeriodo e suas respectivas listas de
		// GrupoCustoCentroLucro
		grupoCustoService.orderGrupoCustoHierarchyList(grupoCusto);

		grupoCustoService.checkGrupoCustoAreaOrcamentaria(grupoCusto);

		// popula o to na tela para ser exibido
		prepareGrupoCustoUpdateCreate(grupoCusto);
		setSiglaDefault(grupoCusto);

		return grupoCusto;
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 *
	 */
	public void findByFilter() {
		bean.setResultList(grupoCustoService.findGrupoCustoByFilter(bean.getFilter()));

		if (bean.getResultList().size() == 0) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		} else {
			for (GrupoCusto grupoCusto: bean.getResultList()) {
				actionView(grupoCusto);
				grupoCusto.setChangeStatusCostCenter(Boolean.FALSE);
				grupoCusto.setIsInactivationProcess(Boolean.FALSE);
            }
        }

		// volta para a primeira pagina da paginacao
		bean.setCurrentPageId(0);
	}

	/**
	 * Cria um seguidor de grupo de custo.
	 */
	public void followGrupoCusto() {
		// busca grupo de custo
		GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(bean
				.getCurrentRowId());

		// cria o seguidor
		FollowGrupoCusto followGC = new FollowGrupoCusto();
		followGC.setGrupoCusto(grupoCusto);
		followGC.setCodigoLogin(LoginUtil.getLoggedUsername());

		followGrupoCustoService.createFollowGrupoCusto(followGC);

		// Carrega o map de seguidor.
		bean.setMapFollowGrupoCusto(this.mapFollowGrupoCusto(LoginUtil
				.getLoggedUser()));
	}

	/**
	 * Remove um seguidor de Grupo de custo.
	 */
	public void unfollowGrupoCusto() {
		// busca grupo de custo
		GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(bean
				.getCurrentRowId());

		// busca o followgrupocusto pelo grupo de custo e login
		FollowGrupoCusto followGrupoCusto = followGrupoCustoService
				.findByGrupoCustoAndLogin(grupoCusto,
						LoginUtil.getLoggedUsername());

		followGrupoCustoService.removeFollowGrupoCusto(followGrupoCusto);

		// Carrega o map de seguidor.
		bean.setMapFollowGrupoCusto(this.mapFollowGrupoCusto(LoginUtil
				.getLoggedUser()));

		this.findByFilter();

	}

	/**
	 * Carrega o map de seguidores pelo login.
	 *
	 * @param pessoa
	 *            login.
	 * @return map.
	 */
	private Map<Long, String> mapFollowGrupoCusto(final Pessoa pessoa) {
		Map<Long, String> mapFollow = new HashMap<Long, String>();
		List<FollowGrupoCusto> listaFollow = followGrupoCustoService
				.findbyLogin(pessoa.getCodigoLogin());

		for (FollowGrupoCusto followGrupoCusto : listaFollow) {
			mapFollow.put(followGrupoCusto.getGrupoCusto()
					.getCodigoGrupoCusto(), followGrupoCusto.getCodigoLogin());
		}

		return mapFollow;
	}

	/**
	 * Obtem o outcome default para o backbutton.
	 *
	 * @return outcome
	 */
	public String backButtonOutcome() {
		return bean.getBackButtonOutcome();
	}

	public void prepareCreateGrupoCustoAreaOrcamentaria() {

		ComboBox<AreaOrcamentaria> comboAreaOrcamentariaPai = ComboFactory
				.create(new AreaOrcamentariaCombo(areaOrcamentariaService
						.findAllActiveAreaOrcamentariaPai()));
		bean.setAreaOrcamentariaPaiCombo(comboAreaOrcamentariaPai);
	}

	public void prepareAreaOrcamentariaFilho(final ValueChangeEvent e) {

		String value = (String) e.getNewValue();

		if (value != null) {
			AreaOrcamentaria areaOrcamentaria = bean.getAreaOrcamentariaPaiCombo().getMap().get(value);

			List<AreaOrcamentaria> areaOrcamentarias = areaOrcamentariaService.findByAreaOrcamentariaPai(areaOrcamentaria.getCodigoAreaOrcamentaria());
			ComboBox<AreaOrcamentaria> comboAreaOrcamentariaFilho = ComboFactory
					.create(new AreaOrcamentariaCombo(areaOrcamentarias));
			bean.setAreaOrcamentariaFilhoCombo(comboAreaOrcamentariaFilho);
		}
	}

	@Transactional
	public void createGrupoCustoAreaOrcamentaria() {
		GrupoCustoAreaOrcamentaria gcao = gcaoBean.getTo();
		Date startDate = null;

		String[] dateFormat = { "MM/yyyy" };
		try {
			startDate = DateUtils.parseDate(gcaoBean.getMesInicioVigencia() + "/"
					+ gcaoBean.getAnoInicioVigencia(), dateFormat);

			// verifica o History Lock. Se startDate nao for valido, exibe
			// mensagem de erro
			if (!moduloService.verifyHistoryLock(startDate, Boolean.TRUE, Boolean.FALSE)) {
				return;
			}

			gcao.setDataInicio(startDate);
			gcao.setGrupoCusto(bean.getTo());
			gcao.setAreaOrcamentaria(bean.getAreaOrcamentariaFilhoCombo()
					.getSelectedItem());

			gcao.setAreaOrcamentaria(
					areaOrcamentariaService.findById(gcao.getAreaOrcamentaria().getCodigoAreaOrcamentaria()));
			grupoCustoAreaOrcamentariaService.create(gcao);

			gcaoBean.reset();

			bean.getTo().setGrupoCustoAreaOrcamentarias(getGrupoCustoAreaOrcamentaria());


		} catch (ParseException e) {
			e.printStackTrace();
			Messages.showError("createTabelaPreco",
					Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
		}
	}

	@Transactional
	public void removeGrupoCustoAreaOrcamentaria() {

		Date closingDateHistoryLock = moduloService.getClosingDateHistoryLock();

		if (moduloService.dateAfterHistoryLock(gcaoBean.getTo().getDataInicio())) {

			GrupoCustoAreaOrcamentaria gcaoToDelete = grupoCustoAreaOrcamentariaService
					.findById(gcaoBean.getTo().getCodigoGrupoCustoAreaOrcamentaria());
			grupoCustoAreaOrcamentariaService.remove(gcaoToDelete);

			gcaoBean.resetTo();

			bean.getTo().setGrupoCustoAreaOrcamentarias(getGrupoCustoAreaOrcamentaria());
		}
		else {
			Messages.showError("removeGrupoCustoAreaOrcamentaria",
					Constants.MSG_ERROR_MODULO_VERIFY_HISTORY_LOCK_REMOVE_BUDGET_AREA, DateUtil
							.formatDate(closingDateHistoryLock,
									Constants.DEFAULT_DATE_PATTERN_SIMPLE,
									Constants.LOCALE_DEFAULT_BUNDLE));
			gcaoBean.resetTo();
		}
	}

    public Boolean actionView(GrupoCusto grupoCusto) {

		this.setActionController(grupoCusto, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);

		if (Boolean.TRUE.equals(isControllership())) {

			List<String> controllershipRuleOne = new ArrayList<>();
			controllershipRuleOne.add(Constants.REQUEST_CREATION);

			if (controllershipRuleOne.contains(grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto())) {
				this.setActionController(grupoCusto, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
			}

			List<String> controllershipRuleTwo = new ArrayList<>();
			controllershipRuleTwo.add(Constants.NEW_INACTIVE);
			if (controllershipRuleTwo.contains(grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto())) {
				this.setActionController(grupoCusto, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
			}

			List<String> controllershipRuleThree = new ArrayList<>();
			controllershipRuleThree.add(Constants.NEW_ACTIVE);
			controllershipRuleThree.add(Constants.CLOSED);

			if (controllershipRuleThree.contains(grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto())) {
				this.setActionController(grupoCusto, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
			}

			List<String> controllershipRuleFour = new ArrayList<>();
			controllershipRuleFour.add(Constants.REQUEST_INACTIVATION);

			if (controllershipRuleFour.contains(grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto())) {
				this.setActionController(grupoCusto, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
			}

			//Desabilitar edição nas integrações com o Mega para evitar concorrencia entre PMS X JOB
			List<String> controllershipRuleFive = new ArrayList<>();
			controllershipRuleFive.add(Constants.INTEGRATING_CREATION);
			controllershipRuleFive.add(Constants.INTEGRATING_INACTIVATION);

			if (controllershipRuleFive.contains(grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto())) {
				this.setActionController(grupoCusto, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
			}

			return grupoCusto.getActiveView();
		}

		if (Boolean.TRUE.equals(isCostCenterManager())) {
			if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.NEW_ACTIVE)) {
				this.setActionController(grupoCusto, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
			}

			if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.CLOSED)) {
				this.setActionController(grupoCusto, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
			}
		}

        return grupoCusto.getActiveView();
    }

	public void disableFieldAndButtons(GrupoCusto grupoCusto) {
		 if (Boolean.FALSE.equals(isControllership()) &&
				 grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.NEW_ACTIVE)) {
			bean.setEnabledButtonAddBudgetArea(Boolean.FALSE);
		 	setEnabledButtonAdd(false);
			setEnabledButtonDelete(false);
			setEnabledFieldArea(true);
			setEnabledProfitCenter(true);
		 }

		if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.NEW_INACTIVE)
				|| grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.CLOSED)){
			bean.getTo().setEnabledFieldInactivationDate(Boolean.TRUE);
		}

		if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.REQUEST_INACTIVATION)) {
			bean.getTo().setEnabledFieldInactivationDate(Boolean.FALSE);
			bean.getTo().setDisabledFieldInactivationDate(Boolean.FALSE);
		}
	}



	private void setActionController(GrupoCusto grupoCusto, boolean isActiveView,
									 boolean isActiveEdit, boolean isActiveTravelBudget,
									 boolean isActiveFollow, boolean isActiveUnfollow, boolean isActiveDelete){
		grupoCusto.setActiveView(isActiveView);
		grupoCusto.setActiveEdit(isActiveEdit);
		grupoCusto.setActiveTravelBudget(isActiveTravelBudget);
		grupoCusto.setActiveFollow(isActiveFollow);
		grupoCusto.setActiveUnfollow(isActiveUnfollow);
		grupoCusto.setActiveDelete(isActiveDelete);
	}


	private Boolean isControllership() {
		return hasAuthority("BUS.COST_CENTER.ADDS:VW");
	}

	private Boolean isCostCenterManager() {
		return hasAuthority("BUS.COST_CENTER.MANAGER:VW");
	}

	private Boolean hasAuthority(String operation) {
		GrantedAuthority[] loggedUserAuthorities = LoginUtil
				.getLoggedUserAuthorities();

		for (GrantedAuthority authority : loggedUserAuthorities) {
			if (authority.getAuthority().equals(operation)) {
				return true;
			}
		}
		return false;
	}


	public void changeStatusCostCenter(){
		bean.getTo().setChangeStatusCostCenter(Boolean.TRUE);
	}

	public void setSelectedStatusCombo(final ValueChangeEvent e) {
		bean.getTo().setIsInactivationProcess(Boolean.FALSE);
		bean.getTo().setEnabledFieldInactivationDate(Boolean.FALSE);
		bean.getTo().setDisabledFieldInactivationDate(Boolean.FALSE);

		String value = (String) e.getNewValue();

		if (value.equals(Constants.REQUEST_INACTIVATION) || value.equals(Constants.CLOSED) ) {
			bean.getTo().setChangeStatusCostCenter(Boolean.FALSE);
			bean.getTo().setIsInactivationProcess(Boolean.TRUE);
		}

		if (value.equals(Constants.NEW_INACTIVE) || value.equals(Constants.CLOSED)) {
			bean.getTo().setEnabledFieldInactivationDate(Boolean.TRUE);
		}

		if (value.equals(Constants.INTEGRATING_INACTIVATION)) {
		    bean.getTo().setChangeStatusCostCenter(Boolean.FALSE);
			bean.getTo().setIsInactivationProcess(Boolean.TRUE);

			bean.getTo().setEnabledFieldInactivationDate(Boolean.TRUE);

		   if(Boolean.FALSE.equals(isControllership())) {
			   bean.getTo().setDisabledFieldInactivationDate(Boolean.TRUE);
		   }
	    }
	}

	public void prepareCostCenterHistory() {
		List<GrupoCustoAud> grupoCustoAudList = grupoCustoService
				.findHistoryByCodigo(bean.getTo().getCodigoGrupoCusto());

		List<GrupoCustoAreaOrcamentariaAud> grupoCustoAreaOrcamentariaAudList = grupoCustoAreaOrcamentariaService.
				findHistoryByCodigo(bean.getTo().getCodigoGrupoCusto());

		List<GrupoCustoPeriodoAud> grupoCustoPeriodoAudList = grupoCustoPeriodoService.
				findHistoryByCodigo(bean.getTo().getCodigoGrupoCusto());


		HistoricoGrupoCusto historicoGrupoCusto = new HistoricoGrupoCusto();

		for(GrupoCustoAud grupoCustoAud : grupoCustoAudList){
			historicoGrupoCusto.addDataSet(grupoCustoAud);
		}
		for(GrupoCustoAreaOrcamentariaAud gcAreaOrcamentariaAud: grupoCustoAreaOrcamentariaAudList){
			historicoGrupoCusto.addDataSet(gcAreaOrcamentariaAud);
		}

		for (GrupoCustoPeriodoAud  gcPeriodoAud: grupoCustoPeriodoAudList) {
			historicoGrupoCusto.addDataSet(gcPeriodoAud);
		}

		bean.setHistoryList(historicoGrupoCusto);
	}




	private Boolean validateITSupport() {
		GrantedAuthority[] loggedUserAuthorities = LoginUtil
				.getLoggedUserAuthorities();
		boolean isItSupport = false;
		for (GrantedAuthority authority : loggedUserAuthorities) {

			if(authority.getAuthority().equals("BOF.COST_CENTER:ED")
			  ||authority.getAuthority().equals("BOF.COST_CENTER:CR")
			  ||authority.getAuthority().equals("BUS.COST_CENTER:ED")
			  ||authority.getAuthority().equals("BUS.COST_CENTER:CR")){
				return false;
			}
			if (authority.getAuthority().equals("BOF.COST_CENTER:VW")
			    || authority.getAuthority().equals("BUS.COST_CENTER:VW")) {
				isItSupport = true;
			}
		}
		return isItSupport;
	}

	public String getStatusInfoMessage() {
		return statusInfoMessage;
	}

	public void setStatusInfoMessage(String statusInfoMessage) {
		this.statusInfoMessage = statusInfoMessage;
	}

	private boolean hasPermissionToEditFields(){
		return isControllership() ? Boolean.TRUE : Boolean.FALSE;
	}

	private boolean enabledStartDate(GrupoCusto grupoCusto) {

		boolean isUpdate = bean.getIsUpdate();
		boolean hasPermissionToEditFields = bean.getHasPermissionToEditFields();
		boolean hasPermissionToEditStartDate = grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.REQUEST_CREATION) || grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.INTEGRATING_CREATION);
		boolean isNull = grupoCusto.getDataInativacao() == null;

		return isUpdate && hasPermissionToEditFields && (isNull || hasPermissionToEditStartDate);
	}
}
