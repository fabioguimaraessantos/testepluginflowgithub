package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.ForecastRevenueStatusProject;
import com.ciandt.pms.model.ForecastRevenueStatusProjectId;
import com.ciandt.pms.persistence.dao.IForecastRevenueStatusProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Repository
public class ForecastRevenueStatusProjectDao extends AbstractDao<ForecastRevenueStatusProjectId, ForecastRevenueStatusProject>
		implements IForecastRevenueStatusProjectDao {

	/** Intancia do entity manager do hibernate. */
	private EntityManager entityManager;

	/**
	 * Construtor padr?o.
	 *
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public ForecastRevenueStatusProjectDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ForecastRevenueStatusProject.class);

		entityManager = factory.createEntityManager();
	}
}
