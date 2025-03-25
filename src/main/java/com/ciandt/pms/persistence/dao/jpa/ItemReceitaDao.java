package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.persistence.dao.IItemReceitaDao;

/**
 * 
 * A classe ItemReceitaDao proporciona as funcionalidades de acesso ao banco
 * para referentes a entidade ItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class ItemReceitaDao extends AbstractDao<Long, ItemReceita> implements
		IItemReceitaDao {

	/** Intancia do entity manager do hibernate. */
	private EntityManager entityManager;
	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public ItemReceitaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ItemReceita.class);

		entityManager = factory.createEntityManager();
	}

	/**
	 * Busca os itens de uma receita.
	 * 
	 * @param receita
	 *            entidade do tipo Receita.
	 * 
	 * @return lista de itnes da receitas.
	 */
	@SuppressWarnings("unchecked")
	public List<ItemReceita> findByReceita(final Receita receita) {

		List<ItemReceita> listResult = getJpaTemplate().findByNamedQuery(
				ItemReceita.FIND_BY_RECEITA,
				new Object[] { receita.getCodigoReceita() });

		return listResult;
	}

	/**
	 * Busca os itens de uma receitaMoeda.
	 * @param receitaMoeda - receitaMoeda.
	 * @return lista de itens de uma receita moeda.
	 */
	@SuppressWarnings("unchecked")
	public List<ItemReceita> findByReceitaMoeda(final ReceitaMoeda receitaMoeda) {
		List<ItemReceita> listResult = getJpaTemplate().findByNamedQuery(
				ItemReceita.FIND_BY_RECEITA_MOEDA,
				new Object[] { receitaMoeda.getCodigoReceitaMoeda() });

		return listResult;
	}

	/**
	 * Remove as entidades pela data e tipo.
	 *
	 * @param dataMes
	 *            - data da vigencia.
	 *
	 * @return true se a atualizacao ocorrou corretamente. False caso contrário.
	 */
	public Boolean updateAllTaxAndNetRevenueAfterDate(final Date dataMes) {
		Boolean isUpdated = Boolean.valueOf(false);
		EntityManager entityManager = null;
		try {

			entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();

			Query query = entityManager.createNamedQuery(ItemReceita.UPDATE_ALL_TAX_AND_NET_REVENUE);

			query.setParameter("param1", dataMes);

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

	/**
	 * Remove as entidades pela data e tipo.
	 *
	 * @param dataMes
	 *            - data da vigencia.
	 *
	 * @return true se a atualizacao ocorrou corretamente. False caso contrário.
	 */
	public Boolean updateAllMultiDealFiscalTaxAndNetRevenueAfterDate(final Date dataMes) {
		Boolean isUpdated = Boolean.valueOf(false);
		EntityManager entityManager = null;
		try {
			entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();

			Query query = entityManager.createNamedQuery(ItemReceita.UPDATE_ALL_MUTIDEALFISCAL_TAX_AND_NET_REVENUE);

			query.setParameter("param1", dataMes);

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
