package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.TipoPratica;

/**
 * 
 * @author peter
 *
 */
@Service
public interface ITipoPraticaService {

	/**
	 * Busca por Id.
	 * 
	 * @param id
	 * @return
	 */
	TipoPratica findById(final Long id);

	/**
	 * Find All TipoPratica
	 * 
	 * @return
	 */
	List<TipoPratica> findAll();

}
