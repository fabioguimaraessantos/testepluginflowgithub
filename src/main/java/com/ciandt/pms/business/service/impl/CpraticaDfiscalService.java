package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ICpraticaDfiscalService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICpraticaDfiscalDao;

/**
 * 
 * A classe AceleradorService proporciona as funcionalidades da camada de
 * serviço referente a entidade Acelerador.
 * 
 * @since 08/10/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public class CpraticaDfiscalService implements ICpraticaDfiscalService {

	/** Instancia do DAO do CpraticaDfiscal. */
	@Autowired
	private ICpraticaDfiscalDao dao;

	/**
	 * Busca a entidade pelo id (codigoMoeda e codigoContratoPratica).
	 * 
	 * @param codigoMoeda
	 *            - codigo da {@link Moeda}
	 * @param codigoContratoPratica
	 *            - codigo do {@link ContratoPratica}
	 * 
	 * @return retorna a entidade
	 */
	@Override
	public CpraticaDfiscal findCpraticaDfiscalById(final Long codigoMoeda,
			final Long codigoContratoPratica) {
		return dao.findById(codigoMoeda, codigoContratoPratica);
	}

	/**
	 * Remove a entity.
	 * 
	 * @param cpraticaDfiscal
	 *            the {@link CpraticaDfiscal}
	 */
	@Override
	public void removeCpraticaDfiscal(final CpraticaDfiscal cpraticaDfiscal) {
		dao.remove(cpraticaDfiscal);
	}

	/**
	 * Add a entity.
	 * 
	 * @param cpraticaDfiscal
	 *            the {@link CpraticaDfiscal}
	 */
	@Override
	public void createCpraticaDfiscal(final CpraticaDfiscal cpraticaDfiscal) {
		dao.create(cpraticaDfiscal);
	}

	/**
	 * Busca {@link CpraticaDfiscal} pelo codigoContratoPratica.
	 *
	 * @param controleReajuste
	 * @return Lista de {@link CpraticaDfiscal}
	 */
	@Override
	public List<CpraticaDfiscal> findByContratoPratica(ContratoPratica contratoPratica) {
		return dao.findByContratoPratica(contratoPratica);
	}
}