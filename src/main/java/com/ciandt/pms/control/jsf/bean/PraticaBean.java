package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.TipoPratica;

/**
 * Define o BackingBean da entidade.
 * 
 * @since 23/09/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PraticaBean implements Serializable {

	/** Serial version. */
	private static final long serialVersionUID = 837826097295531600L;

	/** to do BackingBean. */
	private Pratica to = new Pratica();

	/** filter do backingBean. */
	private Pratica filter = new Pratica();

	/** lista de resultados da pesquisa. */
	private List<Pratica> resultList = new ArrayList<Pratica>();

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageId = Integer.valueOf(0);

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);

	/** Lista para o combobox com as sugestoes - indicadores Ativo/Inativo. */
	private List<String> suggestionsListInAtivo = new ArrayList<String>();

	/** Indicador do modo em tempo de execucao - create/update. */
	private Boolean isUpdate = Boolean.FALSE;

	/** Lista de tipo de praticas para combobox. */
	private List<String> typeLobList = new ArrayList<String>();

	/** Map para associar tipo de praticas. */
	private Map<String, Long> typeLobMap = new HashMap<String, Long>();

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
	 * @return the suggestionsListInAtivo
	 */
	public List<String> getSuggestionsListInAtivo() {
		return suggestionsListInAtivo;
	}

	/**
	 * @param suggestionsListInAtivo
	 *            the suggestionsListInAtivo to set
	 */
	public void setSuggestionsListInAtivo(
			final List<String> suggestionsListInAtivo) {
		this.suggestionsListInAtivo = suggestionsListInAtivo;
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
	 * @param currentRowId
	 *            the currentRowId to set
	 */
	public void setCurrentRowId(final Long currentRowId) {
		this.currentRowId = currentRowId;
	}

	/**
	 * @return the currentRowId
	 */
	public Long getCurrentRowId() {
		return currentRowId;
	}

	/**
	 * @return the filter
	 */
	public Pratica getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final Pratica filter) {
		if (filter != null && filter.getTipoPratica() == null) {
			filter.setTipoPratica(new TipoPratica());
		}
		this.filter = filter;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(final List<Pratica> resultList) {
		this.resultList = resultList;
	}

	/**
	 * @return the resultList
	 */
	public List<Pratica> getResultList() {
		return resultList;
	}

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		resetTo();
		resetFilter();
		resetResultList();
		resetSuggestionsListInAtivo();
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new Pratica();
	}

	/**
	 * Reseta o filter.
	 */
	public void resetFilter() {
		this.setFilter(new Pratica());
	}

	/**
	 * Reseta a lista de to.
	 */
	public void resetResultList() {
		this.setResultList(new ArrayList<Pratica>());
	}

	/**
	 * Reseta a lista de sugestoes - indicadores Ativo/Inativo.
	 */
	public void resetSuggestionsListInAtivo() {
		this.suggestionsListInAtivo = new ArrayList<String>();
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final Pratica to) {
		this.to = to;
	}

	/**
	 * @return the to
	 */
	public Pratica getTo() {
		// Verifica se o TipoPratica é nulo, se verdade instancia um novo.
		if (to != null && to.getTipoPratica() == null) {
			to.setTipoPratica(new TipoPratica());
		}

		return to;
	}

	public List<String> getTypeLobList() {
		return typeLobList;
	}

	public void setTypeLobList(List<String> typeLobList) {
		this.typeLobList = typeLobList;
	}

	public Map<String, Long> getTypeLobMap() {
		return typeLobMap;
	}

	public void setTypeLobMap(Map<String, Long> typeLobMap) {
		this.typeLobMap = typeLobMap;
	}

}