package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.control.jsf.components.ISelect;
import com.ciandt.pms.message.dto.ContractLobDTO;
import com.ciandt.pms.message.dto.CostCenterDTO;
import com.ciandt.pms.model.Area;
import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoPeriodo;
import com.ciandt.pms.model.GrupoCustoStatus;
import com.ciandt.pms.model.HistoricoGrupoCusto;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.NaturezaRow;
import com.ciandt.pms.model.vo.combo.ComboBox;
import org.richfaces.model.TreeNodeImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * A classe GrupoCustoBean deve ser utilizada como backingBean para a entidade
 * GrupoCusto.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class GrupoCustoBean implements Serializable {

	/** Default serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Outcome tela pendencias da entidade. */
	private static final String DEFAULT_OUTCOME_TO_BACKBUTTON = "costcenter_pendingSearch";

	/** to do backingBean. */
	private GrupoCusto to = new GrupoCusto();

	/** filter do backingBean. */
	private GrupoCusto filter = new GrupoCusto();

	/** to do GrupoCustoPeriodo do backingBean. */
	private GrupoCustoPeriodo toGCPeriodo = new GrupoCustoPeriodo();

	/** Lista de GrupoCusto. */
	private List<GrupoCusto> resultList = new ArrayList<GrupoCusto>();

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageId = Integer.valueOf(0);

	/** Indicador do modo em tempo de execucao - create/update. */
	private Boolean isUpdate = Boolean.valueOf(false);



	/** Indicador do modo em tempo de execucao - se perfil logado tem permissão para editar campos. */
	private Boolean hasPermissionToEditFields = Boolean.valueOf(true);

	private Boolean disabledStartDate = Boolean.FALSE;


	/** Indicador do modo em tempo de execucao - remove/view. */
	private Boolean isRemove = Boolean.valueOf(false);

	/** Lista utilizada para listar todas as natureza, na tela de cria��o. */
	private List<NaturezaRow> naturezaRowList = new ArrayList<NaturezaRow>();

	/** Map utilizado no combo de GrupoCusto. */
	private Map<String, Long> grupoCustoMap = new HashMap<String, Long>();

	/** List utilizado no combo de GrupoCusto. */
	private List<String> grupoCustoList = new ArrayList<String>();

	/** Map utilizado no combo de Area. */
	private Map<String, Long> areaMap = new HashMap<String, Long>();

	/** Map utilizado no combo de status. */
	private Map<String, Long> statusComboMap = new HashMap<String, Long>();

	private List<String> statusComboList = new ArrayList<String>();

	/** List utilizado no combo de Area. */
	private List<String> areaList = new ArrayList<String>();

	/** representa o mes selecionado. */
	private String monthBeg;

	/** representa o ano selecionado. */
	private String yearBeg;

	/** Data do HistoryLock. */
	private Date historyLockDate;

	/** Data do Inativação do Centro de Custo. */
	private Date dataInativacao;

	/** Mapa de seguidores do grupo de custo. */
	private Map<Long, String> mapFollowGrupoCusto = new HashMap<Long, String>();

	/** ComboBox do Approver. */
	private ComboBox<Pessoa> approverCombo = new ComboBox<Pessoa>();

	/** ComboBox do Manager Approver. */
	private ComboBox<Pessoa> managerApproverCombo = new ComboBox<Pessoa>();

	/** Tree for Cost Center. */
	private TreeNodeImpl<CostCenterDTO> parentCostCenterTree = new TreeNodeImpl<>();
	private CostCenterDTO parentCostCenterSelected;

	/** Tree for Cost Center. */
	private TreeNodeImpl<ContractLobDTO> parentOverheadProjectTree = new TreeNodeImpl<>();
	private ContractLobDTO parentOverheadProjectSelected;

	private ComboBox<GrupoCustoStatus> statusCombo = new ComboBox<GrupoCustoStatus>();

	/** List para o combobox tipo de cost center. */
	private ComboBox<String> accountTypeSelect = new ComboBox<>();

	/** ComboBox de Area. */
	private ComboBox<Area> areaCombo = new ComboBox<Area>();
	
	/** ComboBox de Area Orcamentaria Pai. */
	private ComboBox<AreaOrcamentaria> areaOrcamentariaPaiCombo = new ComboBox<AreaOrcamentaria>();
	
	/** ComboBox de Area Orcamentaria Filho. */
	private ComboBox<AreaOrcamentaria> areaOrcamentariaFilhoCombo = new ComboBox<AreaOrcamentaria>();

	private Boolean disableStatusCombo = Boolean.valueOf(false);

	private HistoricoGrupoCusto historyList;

	private Boolean enabledButtonAddBudgetArea;

	private ISelect costCenterHierarchySelect;

	/**
	 * Define o outcome do back button da tela de edicao de Cost Center (Grupo
	 * de Custo).
	 */
	private String backButtonOutcome = DEFAULT_OUTCOME_TO_BACKBUTTON;

	/**
	 * @return the toGCPeriodo
	 */
	public GrupoCustoPeriodo getToGCPeriodo() {
		return toGCPeriodo;
	}

	/**
	 * @param toGCPeriodo
	 *            the toGCPeriodo to set
	 */
	public void setToGCPeriodo(final GrupoCustoPeriodo toGCPeriodo) {
		this.toGCPeriodo = toGCPeriodo;
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
	 * @param accountTypeSelect
	 */
	public void setAccountTypeSelect(ComboBox<String> accountTypeSelect) {
		this.accountTypeSelect = accountTypeSelect;
	}

	/**
	 * @return the costCenterCombo
	 */
	public ComboBox<String>  getAccountTypeSelect() {
		return accountTypeSelect;
	}

	/**
	 * @return the to
	 */
	public GrupoCusto getTo() {
		if (to == null) {
			to = new GrupoCusto();
		}
		if (to.getGrupoCustoStatus() == null) {
			to.setGrupoCustoStatus(new GrupoCustoStatus());
		}

		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final GrupoCusto to) {
		this.to = to;
	}

	/**
	 * @return the filter
	 */
	public GrupoCusto getFilter() {
		if (filter == null) {
			filter = new GrupoCusto();
		}
		if (filter.getArea() == null) {
			filter.setArea(new Area());
		}
		if (filter.getGrupoCustoStatus() == null) {
			filter.setGrupoCustoStatus(new GrupoCustoStatus());
		}

		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final GrupoCusto filter) {
		this.filter = filter;
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
	 * @return the grupoCustoMap
	 */
	public Map<String, Long> getGrupoCustoMap() {
		return grupoCustoMap;
	}

	/**
	 * @param grupoCustoMap
	 *            the grupoCustoMap to set
	 */
	public void setGrupoCustoMap(final Map<String, Long> grupoCustoMap) {
		this.grupoCustoMap = grupoCustoMap;
	}

	/**
	 * @return the grupoCustoList
	 */
	public List<String> getGrupoCustoList() {
		return grupoCustoList;
	}

	/**
	 * @param grupoCustoList
	 *            the grupoCustoList to set
	 */
	public void setGrupoCustoList(final List<String> grupoCustoList) {
		this.grupoCustoList = grupoCustoList;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(final List<GrupoCusto> resultList) {
		this.resultList = resultList;
	}

	/**
	 * @return the resultList
	 */
	public List<GrupoCusto> getResultList() {
		return resultList;
	}

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		resetTo();
		resetFilter();
		resetResultList();
		resetValidityDate();
		resetTree();
		this.isUpdate = Boolean.FALSE;
		this.hasPermissionToEditFields = Boolean.TRUE;
		this.enabledButtonAddBudgetArea = Boolean.TRUE;
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new GrupoCusto();
		this.monthBeg = "";
		this.yearBeg = "";
		this.dataInativacao = new Date();
	}

	/**
	 * Reseta o filter.
	 */
	public void resetFilter() {
		this.filter = new GrupoCusto();
	}

	/**
	 * Reseta a lista de to.
	 */
	public void resetResultList() {
		this.resultList = new ArrayList<GrupoCusto>();
	}

	/**
	 * Reseta a data do periodo da vigencia.
	 */
	public void resetValidityDate() {
		this.monthBeg = null;
		this.yearBeg = null;
	}

	public void resetTree(){
		this.parentCostCenterTree = new TreeNodeImpl<>();
		this.parentOverheadProjectTree = new TreeNodeImpl<>();
	}

	/**
	 * @param naturezaRowList
	 *            the naturezaRowList to set
	 */
	public void setNaturezaRowList(final List<NaturezaRow> naturezaRowList) {
		this.naturezaRowList = naturezaRowList;
	}

	/**
	 * @return the naturezaRowList
	 */
	public List<NaturezaRow> getNaturezaRowList() {
		return naturezaRowList;
	}

	/**
	 * @param monthBeg
	 *            the monthBeg to set
	 */
	public void setMonthBeg(final String monthBeg) {
		this.monthBeg = monthBeg;
	}

	/**
	 * @return the monthBeg
	 */
	public String getMonthBeg() {
		return monthBeg;
	}

	/**
	 * @param yearBeg
	 *            the yearBeg to set
	 */
	public void setYearBeg(final String yearBeg) {
		this.yearBeg = yearBeg;
	}

	/**
	 * @return the yearBeg
	 */
	public String getYearBeg() {
		return yearBeg;
	}

	/**
	 * @param areaMap
	 *            the areaMap to set
	 */
	public void setAreaMap(final Map<String, Long> areaMap) {
		this.areaMap = areaMap;
	}

	/**
	 * @return the areaMap
	 */
	public Map<String, Long> getAreaMap() {
		return areaMap;
	}

	/**
	 * @param areaList
	 *            the areaList to set
	 */
	public void setAreaList(final List<String> areaList) {
		this.areaList = areaList;
	}

	/**
	 * @return the areaList
	 */
	public List<String> getAreaList() {
		return areaList;
	}

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
	 * @return the dataInativacao
	 */
	public Date getDataInativacao() {
		return dataInativacao;
	}

	/**
	 * @param dataInativacao
	 *            the dataInativacao to set
	 */
	public void setDataInativacao(final Date dataInativacao) {
		this.dataInativacao = dataInativacao;
	}


	/**
	 * @return the mapFollowGrupoCusto
	 */
	public Map<Long, String> getMapFollowGrupoCusto() {
		return mapFollowGrupoCusto;
	}

	/**
	 * @param mapFollowGrupoCusto
	 *            the mapFollowGrupoCusto to set
	 */
	public void setMapFollowGrupoCusto(
			final Map<Long, String> mapFollowGrupoCusto) {
		this.mapFollowGrupoCusto = mapFollowGrupoCusto;
	}

	/**
	 * @return the approverCombo
	 */
	public ComboBox<Pessoa> getApproverCombo() {
		return this.approverCombo;
	}

	/**
	 * @param approverCombo
	 *            the approverCombo to set
	 */
	public void setApproverCombo(ComboBox<Pessoa> approverCombo) {
		this.approverCombo = approverCombo;
	}

	/**
	 * @return the managerApproverCombo
	 */
	public ComboBox<Pessoa> getManagerApproverCombo() {
		return this.managerApproverCombo;
	}

	/**
	 * @param managerApproverCombo
	 *            the managerApproverCombo to set
	 */
	public void setManagerApproverCombo(ComboBox<Pessoa> managerApproverCombo) {
		this.managerApproverCombo = managerApproverCombo;
	}

	public ComboBox<GrupoCustoStatus> getStatusCombo() {
		return this.statusCombo;
	}

	public void setStatusCombo(ComboBox<GrupoCustoStatus> statusCombo) {
		this.statusCombo = statusCombo;
	}

	public Map<String, Long> getStatusComboMap() {
		return statusComboMap;
	}

	public void setStatusComboMap(Map<String, Long> statusComboMap) {
		this.statusComboMap = statusComboMap;
	}

	public List<String> getStatusComboList() {
		return statusComboList;
	}

	public void setStatusComboList(List<String> statusComboList) {
		this.statusComboList = statusComboList;
	}

	public Boolean getDisableStatusCombo() {
		return disableStatusCombo;
	}

	public void setDisableStatusCombo(final Boolean disableStatusCombo) {
		this.disableStatusCombo = disableStatusCombo;
	}

	/**
	 * @return the areaCombo
	 */
	public ComboBox<Area> getAreaCombo() {
		return this.areaCombo;
	}

	/**
	 * @param areaCombo
	 *            the areaCombo to set
	 */
	public void setAreaCombo(ComboBox<Area> areaCombo) {
		this.areaCombo = areaCombo;
	}

	/**
	 * @return the backButtonOutcome
	 */
	public String getBackButtonOutcome() {
		return backButtonOutcome;
	}

	/**
	 * @param backButtonOutcome
	 *            the backButtonOutcome to set
	 */
	public void setBackButtonOutcome(String backButtonOutcome) {
		this.backButtonOutcome = backButtonOutcome;
	}

	/**
	 * @return the areaOrcamentariaPaiCombo
	 */
	public ComboBox<AreaOrcamentaria> getAreaOrcamentariaPaiCombo() {
		return areaOrcamentariaPaiCombo;
	}

	/**
	 * @param areaOrcamentariaPaiCombo the areaOrcamentariaPaiCombo to set
	 */
	public void setAreaOrcamentariaPaiCombo(
			ComboBox<AreaOrcamentaria> areaOrcamentariaPaiCombo) {
		this.areaOrcamentariaPaiCombo = areaOrcamentariaPaiCombo;
	}

	/**
	 * @return the areaOrcamentariaFilhoCombo
	 */
	public ComboBox<AreaOrcamentaria> getAreaOrcamentariaFilhoCombo() {
		return areaOrcamentariaFilhoCombo;
	}

	/**
	 * @param areaOrcamentariaFilhoCombo the areaOrcamentariaFilhoCombo to set
	 */
	public void setAreaOrcamentariaFilhoCombo(
			ComboBox<AreaOrcamentaria> areaOrcamentariaFilhoCombo) {
		this.areaOrcamentariaFilhoCombo = areaOrcamentariaFilhoCombo;
	}

	public HistoricoGrupoCusto getHistoryList() {
		return historyList;
	}

	public void setHistoryList(HistoricoGrupoCusto historyList) {
		this.historyList = historyList;
	}

	/* Tree */
	public TreeNodeImpl<CostCenterDTO> getParentCostCenterTree() {
		return parentCostCenterTree;
	}
	public void setParentCostCenterTree(TreeNodeImpl<CostCenterDTO> parentCostCenterTree) {
		this.parentCostCenterTree = parentCostCenterTree;
	}
	public CostCenterDTO getParentCostCenterSelected() {
		return parentCostCenterSelected;
	}
	public void setParentCostCenterSelected(CostCenterDTO parentCostCenterSelected) {
		this.parentCostCenterSelected = parentCostCenterSelected;
	}

	public TreeNodeImpl<ContractLobDTO> getParentOverheadProjectTree() {
		return parentOverheadProjectTree;
	}

	public void setParentOverheadProjectTree(TreeNodeImpl<ContractLobDTO> parentOverheadProjectTree) {
		this.parentOverheadProjectTree = parentOverheadProjectTree;
	}

	public ContractLobDTO getParentOverheadProjectSelected() {
		return parentOverheadProjectSelected;
	}

	public void setParentOverheadProjectSelected(ContractLobDTO parentOverheadProjectSelected) {
		this.parentOverheadProjectSelected = parentOverheadProjectSelected;
	}


	public Boolean getHasPermissionToEditFields() {
		return hasPermissionToEditFields;
	}

	public void setHasPermissionToEditFields(Boolean hasPermissionToEditFields) {
		this.hasPermissionToEditFields = hasPermissionToEditFields;
	}

	public Boolean getEnabledButtonAddBudgetArea() {
		return enabledButtonAddBudgetArea;
	}

	public void setEnabledButtonAddBudgetArea(Boolean enabledButtonAddBudgetArea) {
		this.enabledButtonAddBudgetArea = enabledButtonAddBudgetArea;
	}

	public ISelect getCostCenterHierarchySelect() {
		return this.costCenterHierarchySelect;
	}

	public void setCostCenterHierarchySelect(ISelect costCenterHierarchySelect) {
		this.costCenterHierarchySelect = costCenterHierarchySelect;
	}

	public Boolean getDisabledStartDate() {
		return disabledStartDate;
	}

	public void setDisabledStartDate(Boolean disabledStartDate) {
		this.disabledStartDate = disabledStartDate;
	}
}
