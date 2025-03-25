package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.persistence.dao.IPraticaDao;

/**
 * 
 * A classe PraticaDao proporciona as funcionalidades de acesso ao banco de
 * dados referentes a entidade Pratica.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class PraticaDao extends AbstractDao<Long, Pratica> implements
		IPraticaDao {

	/**
	 * Contrutor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public PraticaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, Pratica.class);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<Pratica> findAll() {
		List<Pratica> listResult = getJpaTemplate().findByNamedQuery(
				Pratica.FIND_ALL);

		return listResult;
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	@SuppressWarnings("unchecked")
	public List<Pratica> findByFilter(final Pratica filter) {
		Long codigoTipoPratica = 0L;
		// checa se existe tipoPratica
		if (filter.getTipoPratica() != null) {
			codigoTipoPratica = filter.getTipoPratica().getCodigoTipoPratica();
		}

		List<Pratica> listResult = getJpaTemplate().findByNamedQuery(
				Pratica.FIND_BY_FILTER,
				new Object[] { filter.getNomePratica(),
						filter.getNomePratica(), filter.getSiglaPratica(),
						filter.getSiglaPratica(), filter.getIndicadorAtivo(),
						filter.getIndicadorAtivo(), codigoTipoPratica,
						codigoTipoPratica });
		return listResult;
	}

}
