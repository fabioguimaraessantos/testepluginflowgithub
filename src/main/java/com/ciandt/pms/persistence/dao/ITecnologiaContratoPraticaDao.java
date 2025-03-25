package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.TecnologiaContratoPratica;

/**
 * 
 * A interface ITecnologiaContratoPraticaDao proporciona a interface de acesso
 * ao banco de dados para a entidade {@link TecnologiaContratoPratica}.
 * 
 * @since Sep 23, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public interface ITecnologiaContratoPraticaDao extends
		IAbstractDao<Long, TecnologiaContratoPratica> {

	/**
	 * Return all TecnologiaContratoPratic.
	 * 
	 * @return TecnologiaContratoPratic List.
	 */
	List<TecnologiaContratoPratica> findAll();

	/**
	 * Return all TecnologiaContratoPratica associated with the given
	 * ContratoPratica.
	 * 
	 * @return TecnologiaContratoPratica List.
	 */
	List<TecnologiaContratoPratica> findAllByClob(
			final ContratoPratica contratoPratica);
}