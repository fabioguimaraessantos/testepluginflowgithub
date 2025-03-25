package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ITipoPraticaService;
import com.ciandt.pms.model.TipoPratica;
import com.ciandt.pms.persistence.dao.ITipoPraticaDao;

@Service
public class TipoPraticaService implements ITipoPraticaService {
	
	/**
	 * TipoPratica Dao.
	 */
	@Autowired
	private ITipoPraticaDao tipoPraticaDao;
	
	/**
	 * Busca por Id.
	 * @param id
	 * @return
	 */
	@Override
	public TipoPratica findById(final Long id) {
		return tipoPraticaDao.findById(id);
	}

	/**
	 * Find All TipoPratica
	 * @return
	 */
	@Override
	public List<TipoPratica> findAll() {
		return tipoPraticaDao.findAll();
	}
	
}
