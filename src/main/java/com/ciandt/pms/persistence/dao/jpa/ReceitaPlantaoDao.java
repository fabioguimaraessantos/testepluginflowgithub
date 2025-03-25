package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaPlantao;
import com.ciandt.pms.persistence.dao.IReceitaPlantaoDao;

/**
 * 
 * A classe AjusteReceita proporciona as funcionalidades da camada de
 * persistencia referente a entidade ReceitaPlantaoDao.
 * 
 * @since 14/07/2015
 * @author luizsj
 * 
 */
@Repository
public class ReceitaPlantaoDao extends AbstractDao<Long, ReceitaPlantao>
		implements IReceitaPlantaoDao {

	private EntityManager entityManager;

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public ReceitaPlantaoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ReceitaPlantao.class);

		entityManager = factory.createEntityManager();
	}


	/**
	 * Busca todas as entidades.
	 * 
	 * @return retorna uma lista de ReceitaPlantao.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaPlantao> findAll() {

		List<ReceitaPlantao> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaPlantao.FIND_ALL);

		return listResult;
	}

	/**
	 * Busca os ReceitaPlantao de um dealFiscal.
	 * 
	 * @param receitaDealFiscal
	 *            recebe um receitaDEalFiscal
	 * 
	 * @return ReceitaPlantao de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public ReceitaPlantao findByReceitaDealFiscal(
			final Long receitaDealFiscalCode) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoReceitaDfiscal", receitaDealFiscalCode);

		List<ReceitaPlantao> result = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ReceitaPlantao.FIND_BY_RECEITA_DEAL_FISCAL, params);

		if (result == null || result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	public Boolean updateStatusReceitaPlantao(Long revenueCode, String statusReceitaPlantao, BigDecimal orderID) {
		Boolean isUpdated = Boolean.valueOf(false);

		EntityManager entityManager = null;
		try {

			entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			Query query = entityManager.createNamedQuery(ReceitaPlantao.UPDATE_STATUS_RECEITA_PLANTAO);

			query.setParameter("revenueStatus", statusReceitaPlantao);
			query.setParameter("orderID", orderID);
			query.setParameter("revenueCode", revenueCode);

			query.executeUpdate();
			transaction.commit();

			isUpdated = Boolean.valueOf(true);
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerFactoryUtils.closeEntityManager(entityManager);
		}

		return isUpdated;
	}
}