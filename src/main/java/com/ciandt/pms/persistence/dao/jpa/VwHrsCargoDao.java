package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwHrsCargo;
import com.ciandt.pms.persistence.dao.IVwHrsCargoDao;

/**
 * 
 * A classe TabelaPrecoDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade VwHrsCargo.
 * 
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class VwHrsCargoDao extends AbstractDao<Long, VwHrsCargo> implements
		IVwHrsCargoDao {

	/**
	 * Construtor.
	 * 
	 * @param factory
	 *            factory
	 */
	@Autowired
	public VwHrsCargoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, VwHrsCargo.class);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<VwHrsCargo> findAll() {
		List<VwHrsCargo> listResult = getJpaTemplate().findByNamedQuery(
				VwHrsCargo.FIND_ALL);

		return listResult;
	}

	/**
	 * Busca pelo nome.
	 * 
	 * @param name
	 *            nome
	 * @return entidade
	 */
	@SuppressWarnings("unchecked")
	public VwHrsCargo findVwHrsCargoByName(final String name) {
		List<VwHrsCargo> listResult = getJpaTemplate().findByNamedQuery(
				VwHrsCargo.FIND_BY_NAME, new Object[] { name });

		if (listResult.isEmpty()) {
			return null;
		}
		return listResult.get(0);
	}

	/**
	 * Busca por codigo da view.
	 * 
	 * @param codigo
	 *            codigo
	 * @return entidade
	 */
	@SuppressWarnings("unchecked")
	public VwHrsCargo findByCodigo(final Long codigo) {
		List<VwHrsCargo> listResult = getJpaTemplate().findByNamedQuery(
				VwHrsCargo.FIND_BY_CODIGO, new Object[] { codigo });

		if (listResult.isEmpty()) {
			return null;
		}
		return listResult.get(0);
	}

	/**
	 * Busca todas as entidades ativas.
	 * 
	 * @return lista.
	 */
	@SuppressWarnings("unchecked")
	public List<VwHrsCargo> findAllActive() {
		List<VwHrsCargo> listResult = getJpaTemplate().findByNamedQuery(
				VwHrsCargo.FIND_ALL_ACTVE);
		return listResult;
	}

}