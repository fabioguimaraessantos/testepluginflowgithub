package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.MediaCotacaoMoeda;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.IMediaCotacaoMoedaDao;

/**
 * 
 * A classe MediaCotacaoMoedaDao proporciona as funcionalidades de acesso ao
 * banco para referentes a entidade MediaCotacaoMoeda.
 * 
 * @since 05/09/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Repository
public class MediaCotacaoMoedaDao extends AbstractDao<Long, MediaCotacaoMoeda>
		implements IMediaCotacaoMoedaDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public MediaCotacaoMoedaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, MediaCotacaoMoeda.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IMediaCotacaoMoedaDao#findByDataAndMoeda
	 * (com.ciandt.pms.model.MediaCotacaoMoeda)
	 */
	@SuppressWarnings("unchecked")
	public List<MediaCotacaoMoeda> findByDataAndMoeda(
			final MediaCotacaoMoeda mediaCotacaoMoeda) {

		List<MediaCotacaoMoeda> listResult = getJpaTemplate().findByNamedQuery(
				MediaCotacaoMoeda.FIND_BY_DATA_AND_MOEDA,
				new Object[] { mediaCotacaoMoeda.getAno(),
						mediaCotacaoMoeda.getMoeda().getCodigoMoeda() });

		return listResult;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IMediaCotacaoMoedaDao#findByMoeda(com.
	 * ciandt.pms.model.Moeda)
	 */
	@SuppressWarnings("unchecked")
	public List<MediaCotacaoMoeda> findByMoeda(Moeda moeda) {
		List<MediaCotacaoMoeda> listResult = getJpaTemplate().findByNamedQuery(
				MediaCotacaoMoeda.FIND_BY_MOEDA,
				new Object[] { moeda.getCodigoMoeda() });

		return listResult;
	}

}
