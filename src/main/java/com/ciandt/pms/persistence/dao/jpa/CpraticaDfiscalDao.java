package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.CpraticaDfiscalId;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICpraticaDfiscalDao;

/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 05/10/2012
 */
@Repository
public class CpraticaDfiscalDao extends
		AbstractDao<CpraticaDfiscalId, CpraticaDfiscal> implements
		ICpraticaDfiscalDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public CpraticaDfiscalDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, CpraticaDfiscal.class);
	}

	/**
	 * Busca a entidade pelo id (codigoMoeda e codigoContratoPratica).
	 * 
	 * @param codigoDealFiscal
	 *            - codigo da {@link Moeda}
	 * @param codigoContratoPratica
	 *            - codigo do {@link ContratoPratica}
	 * 
	 * @return retorna a entidade
	 */
	@Override
	@SuppressWarnings("unchecked")
	public CpraticaDfiscal findById(final Long codigoDealFiscal,
			final Long codigoContratoPratica) {

		List<CpraticaDfiscal> result = getJpaTemplate().findByNamedQuery(
				CpraticaDfiscal.FIND_BY_ID,
				new Object[] { codigoDealFiscal, codigoContratoPratica });

		if (result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	/**
	 * Busca {@link CpraticaDfiscal} pelo codigoContratoPratica.
	 *
	 * @param contratoPratica
	 * @return Lista de {@link CpraticaDfiscal}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CpraticaDfiscal> findByContratoPratica(ContratoPratica contratoPratica) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoContratoPratica", contratoPratica.getCodigoContratoPratica());

		List<CpraticaDfiscal> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						CpraticaDfiscal.FIND_BY_CONTRATO_PRATICA,
						params);

		return results;
	}
}