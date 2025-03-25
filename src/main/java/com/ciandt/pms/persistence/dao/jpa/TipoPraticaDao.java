package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.TipoPratica;
import com.ciandt.pms.persistence.dao.ITipoPraticaDao;

/**
 * 
 * @author peter
 *
 */
@Repository
public class TipoPraticaDao extends AbstractDao<Long, TipoPratica> implements
		ITipoPraticaDao {

	/**
	 * Contrutor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public TipoPraticaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, TipoPratica.class);
	}
	
	/**
	 * Find All TipoPratica
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TipoPratica> findAll() {
		List<TipoPratica> resultList = getJpaTemplate().findByNamedQuery(
				TipoPratica.FIND_ALL);
		return resultList;
	}

}
