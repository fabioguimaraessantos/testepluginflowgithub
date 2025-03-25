package com.ciandt.pms.business.service.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IMediaCotacaoMoedaService;
import com.ciandt.pms.model.MediaCotacaoMoeda;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.IMediaCotacaoMoedaDao;

/**
 * 
 * A classe MediaCotacaoMoedaService proporciona as funcionalidades de serviço
 * para a entidade MediaCotacaoMoeda.
 * 
 * @since 05/09/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Service
public class MediaCotacaoMoedaService implements IMediaCotacaoMoedaService {

	/**
	 * Instancia do DAO da entidade.
	 */
	@Autowired
	private IMediaCotacaoMoedaDao mediaCotacaoMoedaDao;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createMediaCotacaoMoeda(final MediaCotacaoMoeda entity,
			final String year) {

		MediaCotacaoMoeda mediaCotacaoMoedaAux = null;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0); // Seta milissegundo para zero

		// Insere uma linha para cada mês do ano
		for (int i = 0; i < 12; i++) {
			mediaCotacaoMoedaAux = (MediaCotacaoMoeda) SerializationUtils
					.clone(entity);

			cal.set(Integer.valueOf(year), i, 1, 0, 0, 0);
			mediaCotacaoMoedaAux.setDataData(cal.getTime());

			mediaCotacaoMoedaDao.create(mediaCotacaoMoedaAux);
		}
	}

	/**
	 * Atualiza a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	public void updateMediaCotacaoMoeda(final MediaCotacaoMoeda entity) {

		List<MediaCotacaoMoeda> listaMediaCotacaoMoeda = this
				.findByDataAndMoeda(entity);

		// Atualiza o valor cotação para todos os meses do ano
		for (MediaCotacaoMoeda mediaCotacaoMoeda : listaMediaCotacaoMoeda) {
			mediaCotacaoMoeda.setValorCotacao(entity.getValorCotacao());
			mediaCotacaoMoedaDao.update(mediaCotacaoMoeda);
		}
	}

	/**
	 * Remove a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será removida.
	 * 
	 */
	public void removeMediaCotacaoMoeda(final MediaCotacaoMoeda entity) {
		mediaCotacaoMoedaDao.remove(entity);
	}

	/**
	 * Busca uma entidade por data e moeda.
	 * 
	 * @param entity
	 *            MediaCotacaoMoeda.
	 * 
	 * @return Lista de {@link MediaCotacaoMoeda}
	 */
	public List<MediaCotacaoMoeda> findByDataAndMoeda(
			final MediaCotacaoMoeda entity) {
		return mediaCotacaoMoedaDao.findByDataAndMoeda(entity);
	}

	/**
	 * Busca uma entidade pela moeda.
	 * 
	 * @param moeda
	 *            Objeto Moeda.
	 * 
	 * @return List of {@link MediaCotacaoMoeda}
	 */
	public List<MediaCotacaoMoeda> findByMoeda(Moeda moeda) {

		return mediaCotacaoMoedaDao.findByMoeda(moeda);
	}

}