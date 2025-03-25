package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaOrcDespCl;
import com.ciandt.pms.model.ContratoPraticaOrcDespClId;
import com.ciandt.pms.model.OrcDespesaCl;

/**
 * Define a interface do DAO da entidade ContratoPraticaOrcDespCl.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 11/04/2013
 * 
 */
@Repository
public interface IContratoPraticaOrcDespClDao extends
		IAbstractDao<ContratoPraticaOrcDespClId, ContratoPraticaOrcDespCl> {

	/**
	 * Busca uma entidade ContratoPraticaOrcDespCl.
	 * 
	 * @param codigoContratoPratica
	 *            - o codigo do {@link ContratoPratica}
	 * @param codigoOrcDespesaCl
	 *            - o codigo do {@link OrcDespesaCl}
	 * 
	 * @return {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	ContratoPraticaOrcDespCl findById(final Long codigoContratoPratica,
			final Long codigoOrcDespesaCl);
	
	/**
	 * Busca todas as entidades do banco.
	 * 
	 * @return listResult
	 */
	List<ContratoPraticaOrcDespCl> findAll();
	
	/**
	 * Busca pelo OrcaDespesaCl.
	 * 
	 * @return listResult
	 */
	List<ContratoPraticaOrcDespCl> findByOrcDespCl(final OrcDespesaCl orcDespesaCl);
}