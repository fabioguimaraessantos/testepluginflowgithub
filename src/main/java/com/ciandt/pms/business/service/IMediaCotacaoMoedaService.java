package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.MediaCotacaoMoeda;
import com.ciandt.pms.model.Moeda;

/**
 * 
 * A classe IMediaCotacaoMoedaService proporciona a interface de servico para a
 * entidade MediaCotacaoMoeda.
 * 
 * @since 05/09/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Service
public interface IMediaCotacaoMoedaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createMediaCotacaoMoeda(final MediaCotacaoMoeda entity,
			final String year);

	/**
	 * Atualiza a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	@Transactional
	void updateMediaCotacaoMoeda(final MediaCotacaoMoeda entity);

	/**
	 * Remove a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será removida.
	 * 
	 */
	@Transactional
	void removeMediaCotacaoMoeda(final MediaCotacaoMoeda entity);

	/**
	 * Busca uma entidade por ano e moeda.
	 * 
	 * @param entity
	 *            Objeto MediaCotacaoMoeda
	 * 
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