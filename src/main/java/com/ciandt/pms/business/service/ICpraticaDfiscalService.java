package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.Moeda;

/**
 * 
 * A classe IAceleradorService proporciona a interface de acesso a camada de
 * serviço referente a entidade Acelerador.
 * 
 * @since 08/10/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public interface ICpraticaDfiscalService {

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
	CpraticaDfiscal findCpraticaDfiscalById(final Long codigoMoeda,
			final Long codigoContratoPratica);

	/**
	 * Remove a entity.
	 * 
	 * @param cpraticaDfiscal
	 *            the {@link CpraticaDfiscal}
	 */
	void removeCpraticaDfiscal(final CpraticaDfiscal cpraticaDfiscal);

	/**
	 * Add a entity.
	 * 
	 * @param cpraticaDfiscal
	 *            the {@link CpraticaDfiscal}
	 */
	void createCpraticaDfiscal(final CpraticaDfiscal cpraticaDfiscal);

	/**
	 * Busca {@link CpraticaDfiscal} pelo codigoContratoPratica.
	 *
	 * @param cpraticaDfiscal
	 * @return Lista de {@link CpraticaDfiscal}
	 */
	List<CpraticaDfiscal> findByContratoPratica(ContratoPratica contratoPratica);
}