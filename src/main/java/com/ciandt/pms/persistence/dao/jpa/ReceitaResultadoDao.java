package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ReceitaResultado;
import com.ciandt.pms.persistence.dao.IReceitaResultadoDao;

/**
 * 
 * A classe ReceitaResultadoDao proporciona as funcionalidades de ... para ...
 * 
 * @since 12/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Repository
public class ReceitaResultadoDao extends AbstractDao<Long, ReceitaResultado>
		implements IReceitaResultadoDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - fabrica da entidade
	 * 
	 */
	@Autowired
	public ReceitaResultadoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ReceitaResultado.class);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ReceitaResultado> findAll() {
		return getJpaTemplate().findByNamedQuery(ReceitaResultado.FIND_ALL);
	}

	/**
	 * Retorna uma entidade.
	 * 
	 * @param entity
	 *            filtro {@link ReceitaResultado}
	 * 
	 * @return retorna uma entidade referente ao filtro passado.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ReceitaResultado findByContratoAndMoedaAndDate(
			final ReceitaResultado entity) {
		List<ReceitaResultado> result = getJpaTemplate()
				.findByNamedQuery(
						ReceitaResultado.FIND_BY_CONTRATO_AND_MOEDA_AND_DATE,
						new Object[] {
								entity.getContratoPratica()
										.getCodigoContratoPratica(),
								entity.getDataMes(),
								entity.getMoeda().getCodigoMoeda() });

		if (result == null || result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}
}