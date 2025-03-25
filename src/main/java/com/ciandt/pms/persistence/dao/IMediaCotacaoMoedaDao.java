package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.MediaCotacaoMoeda;
import com.ciandt.pms.model.Moeda;

/**
 * 
 * A classe IMediaCotacaoMoedaDao proporciona a interface de DAO para a entidade
 * MediaCotacaoMoeda.
 * 
 * @since 05/09/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
public interface IMediaCotacaoMoedaDao extends
		IAbstractDao<Long, MediaCotacaoMoeda> {

	/**
	 * Busca MediaCotacaoMoeda por ano e código da moeda.
	 * 
	 * @param entity
	 *            Objeto MediaCotacaoMoeda.
	 * @return List of {@link MediaCotacaoMoeda}
	 */
	List<MediaCotacaoMoeda> findByDataAndMoeda(final MediaCotacaoMoeda entity);

	/**
	 * Busca uma entidade pela moeda.
	 * 
	 * @param moeda
	 *            Objeto Moeda.
	 * 
	 * @return List of {@link MediaCotacaoMoeda}
	 */
	List<MediaCotacaoMoeda> findByMoeda(final Moeda moeda);

}
