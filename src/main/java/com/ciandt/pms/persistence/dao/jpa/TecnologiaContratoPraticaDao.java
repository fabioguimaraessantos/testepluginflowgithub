package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.TecnologiaContratoPratica;
import com.ciandt.pms.persistence.dao.ITecnologiaContratoPraticaDao;

/**
 * 
 * A classe TecnologiaContratoPraticaDao proporciona as funcionalidades de
 * acesso ao banco de dados referente a entidade
 * {@link TecnologiaContratoPratica}.
 * 
 * @since Sep 23, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Repository
public class TecnologiaContratoPraticaDao extends
		AbstractDao<Long, TecnologiaContratoPratica> implements
		ITecnologiaContratoPraticaDao {

	/**
	 * Construtor padrao.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public TecnologiaContratoPraticaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, TecnologiaContratoPratica.class);
	}

	/**
	 * Return all TecnologiaContratoPratic.
	 * 
	 * @return TecnologiaContratoPratic List.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TecnologiaContratoPratica> findAll() {
		List<TecnologiaContratoPratica> listResult = getJpaTemplate()
				.findByNamedQuery(TecnologiaContratoPratica.FIND_ALL);

		return listResult;
	}

	/**
	 * Return all TecnologiaContratoPratica associated with the given
	 * ContratoPratica.
	 * 
	 * @return TecnologiaContratoPratica List.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TecnologiaContratoPratica> findAllByClob(
			final ContratoPratica contratoPratica) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoContratoPratica",
				contratoPratica.getCodigoContratoPratica());
		return getJpaTemplate().findByNamedQueryAndNamedParams(
				TecnologiaContratoPratica.FIND_ALL_BY_CLOB, params);
	}

}