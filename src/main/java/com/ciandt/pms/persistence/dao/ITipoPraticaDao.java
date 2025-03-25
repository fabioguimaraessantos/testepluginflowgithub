package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.TipoPratica;

/**
 * 
 * @author peter
 *
 */
public interface ITipoPraticaDao extends IAbstractDao<Long, TipoPratica> {

	/**
	 * Find All TipoPratica
	 * 
	 * @return
	 */
	List<TipoPratica> findAll();

}
