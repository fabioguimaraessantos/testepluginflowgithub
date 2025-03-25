package com.ciandt.pms.persistence.dao.jpa;


import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.VwPmsCotacaoMoeda;
import com.ciandt.pms.persistence.dao.IVwPmsCotacaoMoedaDao;

@Repository
public class VwPmsCotacaoMoedaDao extends AbstractDao<Long, VwPmsCotacaoMoeda>
		implements IVwPmsCotacaoMoedaDao {
	
	/** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public VwPmsCotacaoMoedaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, VwPmsCotacaoMoeda.class);
		
		entityManager = factory.createEntityManager();
	}

	/**
	 * Busca a ultima taxa de conversão de uma moeda.
	 * 
	 * @param moeda
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public VwPmsCotacaoMoeda findLastRateByCurrency(Moeda moeda) {
		List<VwPmsCotacaoMoeda> listResult = getJpaTemplate().findByNamedQuery(
				VwPmsCotacaoMoeda.FIND_LAST_RATE_BY_CURRENCY,
				new Object[] { moeda.getCodigoMoeda() });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		}

		return null;
	}
	
	/**
	 * Chama procedure que converte o valor de uma moeda para outra moeda.
	 * 
	 * @param valorAConverter
	 *            Valor a ser convertido
	 * @param moedaDe
	 *            Moeda do valor a ser convertido
	 * @param moedaPara
	 *            Moeda na qual se quer converter o valor
	 * @return BigDecimal Valor convertido
	 */
	@SuppressWarnings("rawtypes")
	public BigDecimal getValorConvertidoMoedaDePara(
			final BigDecimal valorAConverter, final Long moedaDe,
			final Long moedaPara) {
		Object result = null;
		try {
			Query query =  entityManager.createNamedQuery(
					VwPmsCotacaoMoeda.CONVERTE_VALOR_MOEDA_DE_PARA);

			query.setParameter("valorAConverter", valorAConverter);
			query.setParameter("moedaDe", moedaDe);
			query.setParameter("moedaPara", moedaPara);
			
			List listResult = query.getResultList();

			if (listResult != null) {
				result = listResult.get(0);
			} else {
				result = BigDecimal.valueOf(0);
			}
		} catch (HibernateException e) {
			result = BigDecimal.valueOf(0);
			e.printStackTrace();
		} catch (Exception e) {
			result = BigDecimal.valueOf(0);
			e.printStackTrace();
		}

		return new BigDecimal(result.toString());
	}

}
