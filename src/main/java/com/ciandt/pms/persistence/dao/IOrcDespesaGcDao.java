package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * 
 * A classe IOrcDespesaGcDao proporciona as funcionalidades de interface para
 * OrcDespesaGcDao.
 * 
 * @since 10/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public interface IOrcDespesaGcDao extends IAbstractDao<Long, OrcDespesaGc> {

	/**
	 * Busca todas as entidades do banco.
	 * 
	 * @return listResult
	 */
	List<OrcDespesaGc> findAll();

	/**
	 * Busca pelo Grupo de Custo.
	 * 
	 * @param codigoGrupoCusto
	 *            codigoGrupoCusto
	 * @return listResult
	 */
	List<OrcDespesaGc> findByGrupoCustoAndActive(final Long codigoGrupoCusto);

	/**
	 * Busca pelo Nome.
	 * 
	 * @param name
	 *            name
	 * @return listResult
	 */
	OrcDespesaGc findByNameAndActive(final String name);

	/**
	 * Busca por orcamentoDespesa.
	 * 
	 * @param orcDesp
	 *            the orcamentoDespesa.
	 * @return listResult
	 */
	OrcDespesaGc findByOrcDespesa(final OrcamentoDespesa orcDesp);

	/**
	 * Busca por nome grupo de custo e ativos para validacao de nome na criacao
	 * 
	 * @param orcDespGc
	 *            orcDespGc
	 * @return
	 */
	OrcDespesaGc findByNameAndGrupoCustoAndActive(final OrcDespesaGc orcDespGc);

	/**
	 * Busca apenas os TB vigentes.
	 * 
	 * @return
	 */
	List<OrcDespesaGc> findOnlyValidByCostGroup(final GrupoCusto grupoCusto);
}