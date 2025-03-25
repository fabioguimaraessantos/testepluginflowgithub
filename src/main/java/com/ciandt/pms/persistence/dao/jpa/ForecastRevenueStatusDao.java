package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.ForecastRevenueStatus;
import com.ciandt.pms.persistence.dao.IForecastRevenueStatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Repository
public class ForecastRevenueStatusDao extends AbstractDao<Long, ForecastRevenueStatus>
		implements IForecastRevenueStatusDao {

	/** Intancia do entity manager do hibernate. */
	private EntityManager entityManager;

	/**
	 * Construtor padr?o.
	 *
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public ForecastRevenueStatusDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ForecastRevenueStatus.class);

		entityManager = factory.createEntityManager();
	}
}
