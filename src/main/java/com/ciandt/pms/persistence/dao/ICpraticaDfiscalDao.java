package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.CpraticaDfiscalId;
import com.ciandt.pms.model.DealFiscal;

/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 05/10/2012
 */
@Repository
public interface ICpraticaDfiscalDao extends
		IAbstractDao<CpraticaDfiscalId, CpraticaDfiscal> {

	/**
	 * Busca a entidade pelo id (codigoMoeda e codigoContratoPratica).
	 * 
	 * @param codigoDealFiscal
	 *            - codigo da {@link DealFiscal}
	 * @param codigoContratoPratica
	 *            - codigo do {@link ContratoPratica}
	 * 
	 * @return retorna a entidade
	 */
	CpraticaDfiscal findById(final Long codigoDealFiscal,
			final Long codigoContratoPratica);

	/**
	 * Busca {@link CpraticaDfiscal} pelo codigoContratoPratica.
	 *
	 * @param contratoPratica
	 * @return Lista de {@link CpraticaDfiscal}
	 */
	List<CpraticaDfiscal> findByContratoPratica(ContratoPratica contratoPratica);
}