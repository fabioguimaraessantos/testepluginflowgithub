package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.MediaCotacaoMoeda;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A classe MediaCotacaoMoedaRow proporciona as funcionalidades de VO para
 * MediaCotacaoMoeda.
 * 
 * @since 09/09/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
public class MediaCotacaoMoedaRow {

	/** Media Cotacao Moeda. */
	private MediaCotacaoMoeda mediaCotacaoMoeda;

	/** Lista de MediaCotacaoMoeda. */
	private List<MediaCotacaoMoeda> listaMediaCotacaoMoeda = new ArrayList<MediaCotacaoMoeda>();

	/** Flag para mostrar os meses de MediaCotacaoMoeda. */
	private Boolean showMediaCotacaoMoeda = Boolean.valueOf(false);

	/** Flag para mostrar ou nao o botao de edit. */
	private Boolean showEditButton = Boolean.TRUE;

	/**
	 * Construtor.
	 * 
	 * @param mediaCotacaoMoeda
	 *            mediaCotacaoMoeda
	 * @param listaMediaCotacaoMoeda
	 *            lista
	 */
	public MediaCotacaoMoedaRow(final MediaCotacaoMoeda mediaCotacaoMoeda,
			final List<MediaCotacaoMoeda> listaMediaCotacaoMoeda) {
		this.mediaCotacaoMoeda = mediaCotacaoMoeda;
		this.listaMediaCotacaoMoeda = listaMediaCotacaoMoeda;
	}

	/**
	 * Default Constructor.
	 */
	public MediaCotacaoMoedaRow() {

	}

	/**
	 * @return the mediaCotacaoMoeda
	 */
	public MediaCotacaoMoeda getMediaCotacaoMoeda() {
		return mediaCotacaoMoeda;
	}

	/**
	 * @param mediaCotacaoMoeda
	 *            the mediaCotacaoMoeda to set
	 */
	public void setMediaCotacaoMoeda(MediaCotacaoMoeda mediaCotacaoMoeda) {
		this.mediaCotacaoMoeda = mediaCotacaoMoeda;
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
	 * @return the showMediaCotacaoMoeda
	 */
	public Boolean getShowMediaCotacaoMoeda() {
		return showMediaCotacaoMoeda;
	}

	/**
	 * @param showMediaCotacaoMoeda
	 *            the showMediaCotacaoMoeda to set
	 */
	public void setShowMediaCotacaoMoeda(Boolean showMediaCotacaoMoeda) {
		this.showMediaCotacaoMoeda = showMediaCotacaoMoeda;
	}

	/**
	 * @return the showEditButton
	 */
	public Boolean getShowEditButton() {
		return showEditButton;
	}

	/**
	 * @param showEditButton
	 *            the showEditButton to set
	 */
	public void setShowEditButton(Boolean showEditButton) {
		this.showEditButton = showEditButton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MediaCotacaoMoedaRow [mediaCotacaoMoeda=" + mediaCotacaoMoeda
				+ "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((mediaCotacaoMoeda == null) ? 0 : mediaCotacaoMoeda
						.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MediaCotacaoMoedaRow other = (MediaCotacaoMoedaRow) obj;
		if (mediaCotacaoMoeda == null) {
			if (other.mediaCotacaoMoeda != null)
				return false;
		} else if (!mediaCotacaoMoeda.equals(other.mediaCotacaoMoeda))
			return false;
		return true;
	}

}
