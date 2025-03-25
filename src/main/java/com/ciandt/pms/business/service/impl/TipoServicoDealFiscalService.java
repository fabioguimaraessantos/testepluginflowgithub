package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.ITipoServicoDealFiscalService;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.TipoServicoDealFiscal;
import com.ciandt.pms.persistence.dao.ITipoServicoDealFiscalDao;

/**
 * 
 * @author peter
 * 
 */
@Service
public class TipoServicoDealFiscalService implements
		ITipoServicoDealFiscalService {

	/** Dao da entidade. */
	@Autowired
	private ITipoServicoDealFiscalDao dao;

	/**
	 * Cria uma entidade.
	 * 
	 * @param tipoSevicoDealFiscal
	 */
	@Transactional
	public void createTipoServicoDealFiscal(
			final TipoServicoDealFiscal tipoSevicoDealFiscal) {
		dao.create(tipoSevicoDealFiscal);
	}

	/**
	 * Remove uma entidade
	 * 
	 * @param tipoServicoDealFiscal
	 */
	public void remove(final TipoServicoDealFiscal tipoServicoDealFiscal) {
		dao.remove(tipoServicoDealFiscal);
	}

	/**
	 * Busca por id.
	 * 
	 * @param id
	 * @return
	 */
	public TipoServicoDealFiscal findById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Busca por dealfiscal.
	 * 
	 * @param dealFiscal
	 * @return
	 */
	public List<TipoServicoDealFiscal> findByDealFiscal(
			final DealFiscal dealFiscal) {
		return dao.findByDealFiscal(dealFiscal);
	}
}
