package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.persistence.Transient;

import com.ciandt.pms.Constants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 24/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespesaGcBean implements Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** To. */
	private OrcDespesaGc to = new OrcDespesaGc();

	/** Lista para pickList. */
	private List<SelectItem> listPickList = new ArrayList<SelectItem>();

	/** Lista de C-Lobs filtrados pela pickList. */
	private String[] grantedCLob;

	/** Lista de orcDespesaGc. */
	private List<OrcDespesaGc> listaOrcDespesaGc = new ArrayList<OrcDespesaGc>();

	/** Map para followers do orcamentoDespesa. */
	private Map<Long, String> mapFollow = new HashMap<Long, String>();

	/** Lista de C-Lobs. */
	private List<ContratoPratica> listaCLob = new ArrayList<ContratoPratica>();

	/** Lista de moedas para combobox. */
	private List<String> listaMoedaCombobox = new ArrayList<String>();

	/** Map para combo de moeda. */
	private Map<String, Long> mapMoeda = new HashMap<String, Long>();

	/** Propriedade da moeda. */
	private String moeda;

	/** Flag para travar combo moeda. */
	private Boolean isSingleCurrency = Boolean.FALSE;

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);

	/** Nome do Orcamento despesa no inplace input. */
	private String nomeOrcamentoDespesaInplaceInput;

	/** Indicador de White List only. */
	private Boolean checkWListOnly;

	/** Flag para filtrar lista de TB exibido na tela. */
	private Boolean flagFilterTravelBudgetList = Boolean.TRUE;

	private Boolean disabledTravelBudget = Boolean.FALSE;

	@Transient
	private boolean isITSupport = false;

	/**
	 * @return the to
	 */
	public OrcDespesaGc getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final OrcDespesaGc to) {
		this.to = to;
	}

	/**
	 * @return the listPickList
	 */
	public List<SelectItem> getListPickList() {
		return listPickList;
	}

	/**
	 * @param listPickList
	 *            the listPickList to set
	 */
	public void setListPickList(final List<SelectItem> listPickList) {
		this.listPickList = listPickList;
	}

	/**
	 * @return the grantedCLob
	 */
	public String[] getGrantedCLob() {
		return grantedCLob;
	}

	/**
	 * @param grantedCLob
	 *            the grantedCLob to set
	 */
	public void setGrantedCLob(final String[] grantedCLob) {
		this.grantedCLob = grantedCLob;
	}

	/**
	 * @return the mapFollow
	 */
	public Map<Long, String> getMapFollow() {
		return mapFollow;
	}

	/**
	 * @param mapFollow
	 *            the mapFollow to set
	 */
	public void setMapFollow(final Map<Long, String> mapFollow) {
		this.mapFollow = mapFollow;
	}

	/**
	 * @return the listaOrcDespesaGc
	 */
	public List<OrcDespesaGc> getListaOrcDespesaGc() {
		return listaOrcDespesaGc;
	}

	/**
	 * @param listaOrcDespesaGc
	 *            the listaOrcDespesaGc to set
	 */
	public void setListaOrcDespesaGc(final List<OrcDespesaGc> listaOrcDespesaGc) {
		this.listaOrcDespesaGc = listaOrcDespesaGc;
	}

	/**
	 * @return the listaCLob
	 */
	public List<ContratoPratica> getListaCLob() {
		return listaCLob;
	}

	/**
	 * @param listaCLob
	 *            the listaCLob to set
	 */
	public void setListaCLob(final List<ContratoPratica> listaCLob) {
		this.listaCLob = listaCLob;
	}

	/**
	 * @return the listaMoedaCombobox
	 */
	public List<String> getListaMoedaCombobox() {
		return listaMoedaCombobox;
	}

	/**
	 * @param listaMoedaCombobox
	 *            the listaMoedaCombobox to set
	 */
	public void setListaMoedaCombobox(final List<String> listaMoedaCombobox) {
		this.listaMoedaCombobox = listaMoedaCombobox;
	}

	/**
	 * @return the mapMoeda
	 */
	public Map<String, Long> getMapMoeda() {
		return mapMoeda;
	}

	/**
	 * @param mapMoeda
	 *            the mapMoeda to set
	 */
	public void setMapMoeda(final Map<String, Long> mapMoeda) {
		this.mapMoeda = mapMoeda;
	}

	/**
	 * @return the moeda
	 */
	public String getMoeda() {
		return moeda;
	}

	/**
	 * @param moeda
	 *            the moeda to set
	 */
	public void setMoeda(final String moeda) {
		this.moeda = moeda;
	}

	/**
	 * @return the isSingleCurrency
	 */
	public Boolean getIsSingleCurrency() {
		return isSingleCurrency;
	}

	/**
	 * @param isSingleCurrency
	 *            the isSingleCurrency to set
	 */
	public void setIsSingleCurrency(final Boolean isSingleCurrency) {
		this.isSingleCurrency = isSingleCurrency;
	}

	/**
	 * Resta o bean.
	 */
	public void reset() {
		this.to = new OrcDespesaGc();
		this.to.setOrcamentoDespesa(new OrcamentoDespesa(Constants.TB_PURPOSE_GENERAL));
		this.grantedCLob = null;
		this.moeda = "";
		this.isSingleCurrency = Boolean.FALSE;		
	}

	public void resetFlagFilterTravelBudgetList() {
		this.flagFilterTravelBudgetList = Boolean.valueOf(true);
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
	 * @return the nomeOrcamentoDespesaInplaceInput
	 */
	public String getNomeOrcamentoDespesaInplaceInput() {
		return nomeOrcamentoDespesaInplaceInput;
	}

	/**
	 * @param nomeOrcamentoDespesaInplaceInput
	 *            the nomeOrcamentoDespesaInplaceInput to set
	 */
	public void setNomeOrcamentoDespesaInplaceInput(
			final String nomeOrcamentoDespesaInplaceInput) {
		this.nomeOrcamentoDespesaInplaceInput = nomeOrcamentoDespesaInplaceInput;
	}

	/**
	 * @return the checkWListOnly
	 */
	public Boolean getCheckWListOnly() {
		return checkWListOnly;
	}

	/**
	 * @param checkWListOnly
	 *            the checkWListOnly to set
	 */
	public void setCheckWListOnly(final Boolean checkWListOnly) {
		this.checkWListOnly = checkWListOnly;
	}

	/**
	 * @return the flagFilterTravelBudgetList
	 */
	public Boolean getFlagFilterTravelBudgetList() {
		return flagFilterTravelBudgetList;
	}

	/**
	 * @param flagFilterTravelBudgetList
	 *            the flagFilterTravelBudgetList to set
	 */
	public void setFlagFilterTravelBudgetList(Boolean flagFilterTravelBudgetList) {
		this.flagFilterTravelBudgetList = flagFilterTravelBudgetList;
	}

	public boolean getIsITSupport() {
		return isITSupport;
	}

	public void setIsITSupport(boolean isITSupport) {
		this.isITSupport = isITSupport;
	}

	public Boolean getDisabledTravelBudget() {
		return disabledTravelBudget;
	}

	public void setDisabledTravelBudget(Boolean disabledTravelBudget) {
		this.disabledTravelBudget = disabledTravelBudget;
	}
}
