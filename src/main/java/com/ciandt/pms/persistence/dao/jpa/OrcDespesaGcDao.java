package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcDespesaGcDao;

/**
 * 
 * A classe OrcDespesaGcDao proporciona as funcionalidades de DAO para a
 * entidade OrcDespesaGc.
 * 
 * @since 10/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Repository
public class OrcDespesaGcDao extends AbstractDao<Long, OrcDespesaGc> implements
		IOrcDespesaGcDao {

	/**
	 * Construtor.
	 * 
	 * @param factory
	 *            factory
	 */
	@Autowired
	public OrcDespesaGcDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, OrcDespesaGc.class);
	}

	/**
	 * Busca todas as entidades do banco.
	 * 
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaGc> findAll() {
		List<OrcDespesaGc> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaGc.FIND_ALL);
		return listResult;
	}

	/**
	 * Busca pelo Grupo de Custo.
	 * 
	 * @param codigoGrupoCusto
	 *            codigoGrupoCusto
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaGc> findByGrupoCustoAndActive(
			final Long codigoGrupoCusto) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoGrupoCusto", codigoGrupoCusto);

		List<OrcDespesaGc> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						OrcDespesaGc.FIND_BY_CODIGO_GRUPO_CUSTO_AND_ACTIVE,
						params);

		return listResult;
	}

	/**
	 * Busca pelo Nome.
	 * 
	 * @param name
	 *            name
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public OrcDespesaGc findByNameAndActive(final String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nomeOrcDespesa", name);

		List<OrcDespesaGc> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						OrcDespesaGc.FIND_BY_NAME_AND_ACTIVE, params);

		if (listResult == null || listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Busca por orcamentoDespesa.
	 * 
	 * @param orcDesp
	 *            the orcamentoDespesa.
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public OrcDespesaGc findByOrcDespesa(final OrcamentoDespesa orcDesp) {
		List<OrcDespesaGc> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaGc.FIND_BY_ORC_DESPESA,
				new Object[] { orcDesp.getCodigoOrcaDespesa() });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		}

		return null;
	}

	/**
	 * Busca por nome grupo de custo e ativos para validacao de nome na criacao
	 * 
	 * @param orcDespGc
	 *            orcDespGc
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OrcDespesaGc findByNameAndGrupoCustoAndActive(
			final OrcDespesaGc orcDespGc) {
		List<OrcDespesaGc> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaGc.FIND_BY_NAME_AND_GRUPO_CUSTO_AND_ACTIVE,
				new Object[] {
						orcDespGc.getOrcamentoDespesa().getNomeOrcDespesa(),
						orcDespGc.getGrupoCusto().getCodigoGrupoCusto() });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		}

		return null;
	}

	/**
	 * Busca apenas os TB vigentes.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaGc> findOnlyValidByCostGroup(
			final GrupoCusto grupoCusto) {
		List<OrcDespesaGc> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaGc.FIND_ONLY_VALID_BY_COST_GROUP,
				new Object[] { grupoCusto.getCodigoGrupoCusto() });
		return listResult;

	}
}