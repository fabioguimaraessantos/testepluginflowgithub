package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * Define o BackingBean da entidade {@link OrcDespesaCl}.
 * 
 * @since 11/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespesaClBean implements Serializable {

	/** The serial version ID. */
	private static final long serialVersionUID = 1L;

	/** To. */
	private OrcDespesaCl to = new OrcDespesaCl();

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);

	/** Objeto selecionado na lista de pesquisa. */
	private OrcDespesaCl currentRow = new OrcDespesaCl();

	/** Lista de {@link OrcDespesaCl}. */
	private List<OrcDespesaCl> orcDespesaClList = new ArrayList<OrcDespesaCl>();

	/** List de {@link SelectItem} referentes ao {@link ContratoPratica}. */
	private List<SelectItem> contratoPraticaPickList = new ArrayList<SelectItem>();

	/** Lista de moedas para o Combobox da tela de OrcDespesaClManaget.xhtml. */
	private List<String> listaMoedaCombobox = new ArrayList<String>();

	/** Lista de moedas para o Combobox da tela de OrcDespesaClManaget.xhtml. */
	private Map<String, Long> mapMoeda = new HashMap<String, Long>();

	/** Map para followers do orcamentoDespesa. */
	private Map<Long, String> mapFollow = new HashMap<Long, String>();

	/** Moeda selecionada. */
	private String moedaSelecionda = "";

	/** Lista de C-Lobs selecionados na pickList. */
	private String[] grantedCLobPickList;

	/** Flag indicando se o orcamento será reembolsabel. */
	private boolean isOrcDespesaClReembolsavel = Boolean.FALSE;

	/** Nome do Orcamento despesa no inplace input. */
	private String nomeOrcamentoDespesaInplaceInput;

	/** Tipo de Fatura selecionada */
	private String tipoFaturaSelecionada = "";

	/** Lista de Faturas para o Combobox da tela de OrcDespesaClManaget.xhtml. */
	private List<String> listaTipoFaturaCombobox = new ArrayList<String>();

	/** Lista de Faturas para o Combobox da tela de OrcDespesaClManaget.xhtml. */
	private Map<String, String> mapTipoFatura = new HashMap<String, String>();

	/** Flag para filtrar lista de TB exibido na tela. */
	private Boolean flagFilterTravelBudgetList = Boolean.valueOf(true);

	/** Flag indicando se o orcamento será extra. */
	private boolean isOrcDespesaExtra = Boolean.FALSE;

	@Transient
	private boolean isITSupport = false;


	/**
	 * Reset do To.
	 */
	public void resetTo() {
		this.to = new OrcDespesaCl();
		this.to.setOrcamentoDespesa(new OrcamentoDespesa());
	}

	/**
	 * Reset do Bean.
	 * 
	 */
	public void reset() {
		this.resetTo();
		this.orcDespesaClList = new ArrayList<OrcDespesaCl>();
		this.contratoPraticaPickList = new ArrayList<SelectItem>();
		this.listaMoedaCombobox = new ArrayList<String>();
		this.mapMoeda = new HashMap<String, Long>();
		this.mapFollow = new HashMap<Long, String>();
		this.moedaSelecionda = "";
		this.grantedCLobPickList = null;
		this.isOrcDespesaClReembolsavel = Boolean.FALSE;
		this.setListaTipoFaturaCombobox(new ArrayList<String>());
		this.mapTipoFatura = new HashMap<String, String>();
		this.tipoFaturaSelecionada = "";
		this.isOrcDespesaExtra = Boolean.FALSE;
	}

	public void resetFlagFilterTravelBudgetList() {
		this.flagFilterTravelBudgetList = Boolean.valueOf(true);
	}	

	/**
	 * @return the to
	 */
	public OrcDespesaCl getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final OrcDespesaCl to) {
		this.to = to;
	}

	/**
	 * @return the orcDespesaClList
	 */
	public List<OrcDespesaCl> getOrcDespesaClList() {
		return orcDespesaClList;
	}

	/**
	 * @param orcDespesaClList
	 *            the orcDespesaClList to set
	 */
	public void setOrcDespesaClList(final List<OrcDespesaCl> orcDespesaClList) {
		this.orcDespesaClList = orcDespesaClList;
	}

	/**
	 * @return the contratoPraticaPickList
	 */
	public List<SelectItem> getContratoPraticaPickList() {
		return contratoPraticaPickList;
	}

	/**
	 * @param contratoPraticaPickList
	 *            the contratoPraticaPickList to set
	 */
	public void setContratoPraticaPickList(
			final List<SelectItem> contratoPraticaPickList) {
		this.contratoPraticaPickList = contratoPraticaPickList;
	}

	/**
	 * @return the contratoPraticaPickList
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
	 * @return the moedaSelecionda
	 */
	public String getMoedaSelecionda() {
		return moedaSelecionda;
	}

	/**
	 * @param moedaSelecionda
	 *            the moedaSelecionda to set
	 */
	public void setMoedaSelecionda(final String moedaSelecionda) {
		this.moedaSelecionda = moedaSelecionda;
	}

	/**
	 * @return the grantedCLobPickList
	 */
	public String[] getGrantedCLobPickList() {
		return grantedCLobPickList;
	}

	/**
	 * @param grantedCLobPickList
	 *            the grantedCLobPickList to set
	 */
	public void setGrantedCLobPickList(final String[] grantedCLobPickList) {
		this.grantedCLobPickList = grantedCLobPickList;
	}

	/**
	 * @return the isOrcDespesaClReembolsavel
	 */
	public boolean getIsOrcDespesaClReembolsavel() {
		return isOrcDespesaClReembolsavel;
	}

	/**
	 * @param isOrcDespesaClReembolsavel
	 *            the isOrcDespesaClReembolsavel to set
	 */
	public void setIsOrcDespesaClReembolsavel(
			final boolean isOrcDespesaClReembolsavel) {
		this.isOrcDespesaClReembolsavel = isOrcDespesaClReembolsavel;
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
	 * @return tipoFaturaSelecionada
	 */
	public String getTipoFaturaSelecionada() {
		return tipoFaturaSelecionada;
	}

	/**
	 * @param tipoFaturaSelecionada
	 *            the tipoFaturaSelecionada to set
	 */
	public void setTipoFaturaSelecionada(String tipoFaturaSelecionada) {
		this.tipoFaturaSelecionada = tipoFaturaSelecionada;
	}

	/**
	 * @return the listaTipoFaturaCombobox
	 */
	public List<String> getListaTipoFaturaCombobox() {
		return listaTipoFaturaCombobox;
	}

	/**
	 * @param listaTipoFaturaCombobox
	 *            the listaTipoFaturaCombobox to set
	 */
	public void setListaTipoFaturaCombobox(List<String> listaTipoFaturaCombobox) {
		this.listaTipoFaturaCombobox = listaTipoFaturaCombobox;
	}

	/**
	 * @return the mapTipoFatura
	 */
	public Map<String, String> getMapTipoFatura() {
		return mapTipoFatura;
	}

	/**
	 * @param mapTipoFatura
	 *            the mapTipoFatura to set
	 */
	public void setMapTipoFatura(Map<String, String> mapTipoFatura) {
		this.mapTipoFatura = mapTipoFatura;
	}

	/**
	 * @return the isOrcDespesaExtra
	 */
	public boolean getIsOrcDespesaExtra() {
		return isOrcDespesaExtra;
	}

	/**
	 * @param isOrcDespesaExtra
	 *            the isOrcDespesaExtra to set
	 */
	public void setIsOrcDespesaExtra(boolean isOrcDespesaExtra) {
		this.isOrcDespesaExtra = isOrcDespesaExtra;
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

	/**
	 * @return the currentRow
	 */
	public OrcDespesaCl getCurrentRow() {
		return currentRow;
	}

	/**
	 * @param currentRow
	 *            the currentRow to set
	 */
	public void setCurrentRow(OrcDespesaCl currentRow) {
		this.currentRow = currentRow;
	}

	public boolean getIsITSupport() {
		return isITSupport;
	}

	public void setIsITSupport(boolean isITSupport) {
		this.isITSupport = isITSupport;
	}
}