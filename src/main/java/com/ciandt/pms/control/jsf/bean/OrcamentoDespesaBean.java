package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.vo.OrcamentoDespesaRow;

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
public class OrcamentoDespesaBean implements Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** To. */
	private OrcamentoDespesa to = new OrcamentoDespesa();

	/** Lista para pickList. */
	private List<SelectItem> listPickList = new ArrayList<SelectItem>();

	/** Lista de C-Lobs filtrados pela pickList. */
	private String[] grantedCLob;

	/** Boolean indicando o tipo de orcamento. */
	private Boolean tipoOrc = Boolean.valueOf(false);

	/** Lista de orcamentoDespesa. */
	private List<OrcamentoDespesaRow> listaOrcDespesaRow = new ArrayList<OrcamentoDespesaRow>();

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

	/** Lista de {@link OrcDespesaCl}. */
	private List<OrcDespesaCl> orcDespesaClList = new ArrayList<OrcDespesaCl>();

	/**
	 * @return the to
	 */
	public OrcamentoDespesa getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final OrcamentoDespesa to) {
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
	 * @return the tipoOrc
	 */
	public Boolean getTipoOrc() {
		return tipoOrc;
	}

	/**
	 * @param tipoOrc
	 *            the tipoOrc to set
	 */
	public void setTipoOrc(final Boolean tipoOrc) {
		this.tipoOrc = tipoOrc;
	}

	/**
	 * @return the listaOrcDespesaRow
	 */
	public List<OrcamentoDespesaRow> getListaOrcDespesaRow() {
		return listaOrcDespesaRow;
	}

	/**
	 * @param listaOrcDespesaRow
	 *            the listaOrcDespesaRow to set
	 */
	public void setListaOrcDespesaRow(
			final List<OrcamentoDespesaRow> listaOrcDespesaRow) {
		this.listaOrcDespesaRow = listaOrcDespesaRow;
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
		this.to = new OrcamentoDespesa();
		this.tipoOrc = Boolean.FALSE;
		this.grantedCLob = null;
		this.moeda = "";
		this.isSingleCurrency = Boolean.FALSE;
	}

	/**
	 * @return the {@link OrcDespesaCl}.
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

}
