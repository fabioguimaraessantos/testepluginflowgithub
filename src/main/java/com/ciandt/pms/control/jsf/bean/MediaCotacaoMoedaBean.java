package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.MediaCotacaoMoeda;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.vo.MediaCotacaoMoedaRow;

/**
 * 
 * Define o BackingBean da entidade MediaCotacaoMoeda.
 * 
 * @since 05/09/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MediaCotacaoMoedaBean implements Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** to do backingBean. */
	private MediaCotacaoMoeda to = new MediaCotacaoMoeda();

	/** Lista de moedas */
	private List<Moeda> listaMoeda = new ArrayList<Moeda>();

	/** Lista media cotação moeda */
	private List<MediaCotacaoMoeda> listaMediaCotacaoMoeda = new ArrayList<MediaCotacaoMoeda>();

	/** Lista de MediaCotacaoMoedaRow. */
	private List<MediaCotacaoMoedaRow> listaMediaCotacaoMoedaRow = new ArrayList<MediaCotacaoMoedaRow>();

	/** MediaCotacaoMoedaRow. */
	private MediaCotacaoMoedaRow mediaCotacaoMoedaRow = new MediaCotacaoMoedaRow();

	/** Lista anos */
	private List<String> listaAno = new ArrayList<String>();

	/** Propriedade do ano. */
	private String ano;

	/** Propriedade que indica se é uma atualização ou inserção. */
	private boolean flagUpdate;

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		resetTo();
		this.ano = "";
		this.flagUpdate = Boolean.valueOf(false);
		this.listaMoeda = new ArrayList<Moeda>();
		this.listaMediaCotacaoMoeda = new ArrayList<MediaCotacaoMoeda>();
		this.listaMediaCotacaoMoedaRow = new ArrayList<MediaCotacaoMoedaRow>();
		this.listaAno = new ArrayList<String>();
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new MediaCotacaoMoeda();
	}

	/**
	 * @return the to
	 */
	public MediaCotacaoMoeda getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final MediaCotacaoMoeda to) {
		this.to = to;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(String ano) {
		this.ano = ano;
	}

	/**
	 * @return the listaMoeda
	 */
	public List<Moeda> getListaMoeda() {
		return listaMoeda;
	}

	/**
	 * @param listaMoeda
	 *            the listaMoeda to set
	 */
	public void setListaMoeda(List<Moeda> listaMoeda) {
		this.listaMoeda = listaMoeda;
	}

	/**
	 * @return the listaAno
	 */
	public List<String> getListaAno() {
		Calendar cal =  new GregorianCalendar();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 2);
		
		int yearBegin = cal.get(Calendar.YEAR);
		int range = Integer.parseInt(systemProperties
				.getProperty(Constants.MEDIA_COTACAO_MOEDA_PROPERTY_COMBOBOX_YEAR_RANGE));

		List<String> localYearList = new ArrayList<String>();

		for (int i = yearBegin; i <= yearBegin + range; i++) {
			localYearList.add("" + i);
		}

		this.listaAno = localYearList;

		return this.listaAno;
	}

	/**
	 * @param listaAno
	 *            the listaAno to set
	 */
	public void setListaAno(List<String> listaAno) {
		this.listaAno = listaAno;
	}

	/**
	 * @return the listaMediaCotacaoMoeda
	 */
	public List<MediaCotacaoMoeda> getListaMediaCotacaoMoeda() {
		return listaMediaCotacaoMoeda;
	}

	/**
	 * @param listaMediaCotacaoMoeda
	 *            the listaMediaCotacaoMoeda to set
	 */
	public void setListaMediaCotacaoMoeda(
			List<MediaCotacaoMoeda> listaMediaCotacaoMoeda) {
		this.listaMediaCotacaoMoeda = listaMediaCotacaoMoeda;
	}

	/**
	 * @return the listaMediaCotacaoMoedaRow
	 */
	public List<MediaCotacaoMoedaRow> getListaMediaCotacaoMoedaRow() {
		return listaMediaCotacaoMoedaRow;
	}

	/**
	 * @param listaMediaCotacaoMoedaRow
	 *            the listaMediaCotacaoMoedaRow to set
	 */
	public void setListaMediaCotacaoMoedaRow(
			List<MediaCotacaoMoedaRow> listaMediaCotacaoMoedaRow) {
		this.listaMediaCotacaoMoedaRow = listaMediaCotacaoMoedaRow;
	}

	/**
	 * @return the mediaCotacaoMoedaRow
	 */
	public MediaCotacaoMoedaRow getMediaCotacaoMoedaRow() {
		return mediaCotacaoMoedaRow;
	}

	/**
	 * @param mediaCotacaoMoedaRow
	 *            the mediaCotacaoMoedaRow to set
	 */
	public void setMediaCotacaoMoedaRow(
			MediaCotacaoMoedaRow mediaCotacaoMoedaRow) {
		this.mediaCotacaoMoedaRow = mediaCotacaoMoedaRow;
	}

	/**
	 * @return the flagUpdate
	 */
	public boolean isFlagUpdate() {
		return flagUpdate;
	}

	/**
	 * @param flagUpdate
	 *            the flagUpdate to set
	 */
	public void setFlagUpdate(boolean flagUpdate) {
		this.flagUpdate = flagUpdate;
	}

}