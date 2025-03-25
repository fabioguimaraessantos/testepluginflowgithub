/**
 * Classe VwItemReceitaDao que implementa a camada de DAO da entidade VwItemReceita 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwItemReceita;
import com.ciandt.pms.persistence.dao.IVwItemReceitaDao;

/**
 * 
 * A classe VwItemReceitaDao proporciona as funcionalidades de acesso ao banco
 * de dados para a entidade VwItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class VwItemReceitaDao extends AbstractDao<Long, VwItemReceita>
		implements IVwItemReceitaDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public VwItemReceitaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, VwItemReceita.class);
	}

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
	@SuppressWarnings("unchecked")
	public List<VwItemReceita> findByFilter(final Date dataAlocacaoPeriodo,
			final Long codigoContratoPratica) {
		List<VwItemReceita> listResult = getJpaTemplate().findByNamedQuery(
				VwItemReceita.FIND_BY_FILTER,
				new Object[] { dataAlocacaoPeriodo, codigoContratoPratica });
		return listResult;
	}

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
	@SuppressWarnings("unchecked")
	public List<VwItemReceita> findByFilterAndCurrency(
			final Date dataAlocacaoPeriodo, final Long codigoContratoPratica,
			final Long codigoMoeda) {
		List<VwItemReceita> listResult = getJpaTemplate().findByNamedQuery(
				VwItemReceita.FIND_BY_FILTER_AND_CURRENCY,
				new Object[] { dataAlocacaoPeriodo, codigoContratoPratica,
						codigoMoeda });
		return listResult;
	}

}