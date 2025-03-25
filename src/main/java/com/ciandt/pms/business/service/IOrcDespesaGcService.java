package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * 
 * A classe IOrcDespesaGcService proporciona as funcionalidades de iterface para
 * OrcDespesaGcService.
 * 
 * @since 10/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public interface IOrcDespesaGcService {

	/**
	 * Busca todas os registros de OrcDespesaGc.
	 * 
	 * @return listResult.
	 */
	List<OrcDespesaGc> findAll();

	/**
	 * Busca entidade por id.
	 * 
	 * @param id
	 *            id.
	 * @return entidade.
	 */
	OrcDespesaGc findOrcDespesaGcById(final Long id);

	/**
	 * Cria OrcDespesaGc.
	 * 
	 * @param entity
	 *            entidade.
	 * @return true.
	 */
	void createOrcDespesaGc(final OrcDespesaGc entity)
			throws IntegrityConstraintException;

	/**
	 * Busca pelo Grupo de Custo.
	 * 
	 * @param codigoGrupoCusto
	 *            codigoGrupoCusto
	 * @return listResult
	 */
	public List<OrcDespesaGc> findByGrupoCustoAndActive(
			final Long codigoGrupoCusto);

	/**
	 * Remove (DELETE LOGICO) um {@link OrcDespesaGc}.
	 * 
	 * @param entity
	 *            orcDespesaGc.
	 * @return true.
	 */
	Boolean removeOrcDespesaGc(final OrcDespesaGc entity);

	/**
	 * Busca um {@link OrcDespesaGc} por nome.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaGc}
	 * @return {@link OrcDespesaGc}
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
	 * Altera um {@link OrcDespesaGc}.
	 * 
	 * @param entity
	 *            orcDespesaGc.
	 * @return true.
	 */
	void updateOrcDespesaGc(final OrcDespesaGc entity);

	/**
	 * Busca apenas os TB vigentes.
	 * 
	 * @return
	 */
	List<OrcDespesaGc> findOnlyValidByCostGroup(final GrupoCusto grupoCusto);

	void clone(OrcamentoDespesa orcamentoDespesa, OrcDespesaGc entity, Boolean copyFollow, Boolean copyWhiteList, Boolean copyWhiteListDelegation) throws IntegrityConstraintException;
}