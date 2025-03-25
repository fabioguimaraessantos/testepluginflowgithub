package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.VwItemReceita;

/**
 * 
 * A classe IVwItemReceitaDao proporciona a interface de DAO para a entidade
 * VwItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IVwItemReceitaDao extends IAbstractDao<Long, VwItemReceita> {

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param dataAlocacaoPeriodo
	 *            data mes/ano da Alocacao.
	 * @param codigoContratoPratica
	 *            código do ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<VwItemReceita> findByFilter(final Date dataAlocacaoPeriodo,
			final Long codigoContratoPratica);

	/**
	 * Busca uma lista de entidades pelo filtro e por moeda.
	 * 
	 * @param dataAlocacaoPeriodo
	 *            data mes/ano da Alocacao.
	 * @param codigoContratoPratica
	 *            código do ContratoPratica.
	 * @param codigoMoeda
	 *            código da Moeda.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<VwItemReceita> findByFilterAndCurrency(final Date dataAlocacaoPeriodo,
			final Long codigoContratoPratica, final Long codigoMoeda);

}
