package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.enums.SgTipoServico;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IDealFiscalDao;

/**
 * 
 * A classe DealDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade Deal.
 * 
 * @since 15/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class DealFiscalDao extends AbstractDao<Long, DealFiscal> implements
		IDealFiscalDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	public DealFiscalDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, DealFiscal.class);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findAll() {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_ALL);

		return listResult;
	}

	/**
	 * Retorna todas as entidades com o estado igual a ativo.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findAllActive() {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_ALL_ACTIVE);

		return listResult;
	}

	/**
	 * Retorna o maior numero de sequencia do deal referente a um Msa.
	 * 
	 * @param msa
	 *            - Intancia de Msa
	 * 
	 * @return retorna um long referente ao numero sequencia.
	 */
	@SuppressWarnings("unchecked")
	public Long findMaxByMsa(final Msa msa) {
		List<Long> listResult = getJpaTemplate()
				.findByNamedQuery(DealFiscal.FIND_MAX_BY_MSA,
						new Object[] { msa.getCodigoMsa() });

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Busca uma lista de entidades relacionadas com o ContratoPratica.
	 * 
	 * @param cp
	 *            entidade do tipo ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findByContratoPratica(final ContratoPratica cp) {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_BY_CONTRATO_PRATICA,
				new Object[] { cp.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Busca uma lista de entidades relacionadas com o ContratoPratica.
	 * 
	 * @param cp
	 *            entidade do tipo ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findByContratoPraticaAndActive(
			final ContratoPratica cp) {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_BY_CONTRATO_PRATICA_AND_ACTIVE,
				new Object[] { cp.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os Dealfiscal
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findByCliente(final Cliente cliente) {

		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_BY_CLIENTE,
				new Object[] { cliente.getCodigoCliente() });

		return listResult;
	}

	/**
	 * Retorna todas as entidades ativas relacionadas com o Msa passado por
	 * parametro.
	 * 
	 * @param msa
	 *            - Msa que se deseja buscar os Dealfiscal
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findActiveByMsa(final Msa msa) {

		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_ACTIVE_BY_MSA,
				new Object[] { msa.getCodigoMsa() });

		return listResult;
	}

	/**
	 * Busca por contrato pratica, moeda e ativos.
	 * 
	 * @param cp
	 *            - contratoPratica
	 * @param moeda
	 *            - moeda
	 * @return lista de Deal Fiscals
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findActiveByClobAndCurrency(
			final ContratoPratica cp, final Moeda moeda) {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_ACTIVE_BY_CLOB_AND_CURRENCY,
				new Object[] { cp.getCodigoContratoPratica(),
						moeda.getCodigoMoeda() });
		return listResult;
	}

	/**
	 * Busca por clob, moeda, ativos e nao deletados logicamente.
	 * 
	 * @param cp
	 *            clob
	 * @param moeda
	 *            moeda
	 * @return listReuslt
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findActiveByClobAndCurrencyAndNotLogicDeleted(
			final ContratoPratica cp, final Moeda moeda) {
		List<DealFiscal> listResult = getJpaTemplate()
				.findByNamedQuery(
						DealFiscal.FIND_ACTIVE_BY_CLOB_AND_CURRENCY_AND_NOT_LOGIC_DELETED,
						new Object[] { cp.getCodigoContratoPratica(),
								moeda.getCodigoMoeda() });
		return listResult;
	}

	/**
	 * Busca por msa e nao deletados logicamente. clob
	 * 
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findByMsaAndNotLogicalDelete(final Msa msa) {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_ACTIVE_BY_MSA_AND_NOT_LOGICAL_DELETE,
				new Object[] { msa.getCodigoMsa() });
		return listResult;
	}

	/**
	 * Busca por msa.
	 * 
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findByMsa(final Msa msa) {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_BY_MSA, new Object[] { msa.getCodigoMsa() });
		return listResult;
	}

	public List<DealFiscal> findByMsaActive(final Msa msa) {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_BY_MSA_ACTIVE, new Object[] { msa.getCodigoMsa() });
		return listResult;
	}

	/**
	 * Busca por msa, ativo e nao deletado logicamente.
	 * 
	 * @param msa
	 *            - the {@link Msa}
	 * 
	 * @return listresult
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findByMsaAndActiveAndNotLogicalDelete(final Msa msa) {
		List<DealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				DealFiscal.FIND_BY_MSA_AND_ACTIVE_AND_NOT_LOGICAL_DELETE,
				new Object[] { msa.getCodigoMsa() });
		return listResult;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IDealFiscalDao#findByFilter(com.ciandt
	 * .pms.model.Msa)
	 */
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findByFilter(final Msa msa) {

		Long codigoMsa = 0L;
		Long codigoCliente = 0L;
		if (msa != null && msa.getCodigoMsa() != null) {
			codigoMsa = msa.getCodigoMsa();
		}
		if (msa.getCliente() != null
				&& msa.getCliente().getCodigoCliente() != null) {
			codigoCliente = msa.getCliente().getCodigoCliente();
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoMsa", codigoMsa);
		params.put("codigoCliente", codigoCliente);

		List<DealFiscal> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(DealFiscal.FIND_BY_FILTER,
						params);

		return listResult;
	}

	/**
	 * Busca {@link DealFiscal} ativos e não deletados logicamente por {@link ContratoPratica}, {@link Moeda}, {@link SgTipoServico}.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica} associado ao  {@link DealFiscal}
	 * @param moeda
	 *            {@link Moeda} associado ao  {@link DealFiscal}
	 * @param siglasTipoServiço
	 * 			{@link SgTipoServico} associado ao  {@link DealFiscal}
	 *
	 * @return Lista de {@link DealFiscal}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findActiveAndNotLogicDeletedByClobAndCurrencyAndTipoServico(
			final ContratoPratica contratoPratica, final Moeda moeda,
			final List<String> siglasTipoServico) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoContratoPratica",
				contratoPratica.getCodigoContratoPratica());
		params.put("codigoMoeda", moeda.getCodigoMoeda());
		params.put("siglasTipoServico", siglasTipoServico);

		List<DealFiscal> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						DealFiscal.FIND_ACTIVE_AND_NOT_LOGICAL_DELETE_BY_CLOB_AND_CURRENCY_AND_TIPO_SERVICO,
						params);

		return listResult;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DealFiscal> findFiscalDealWithActiveAllocationsInAllocationMapByFiscalDealAndClosingMapDate(
			final Long codigoDealFiscal, final Date closingMapDate, final Long codigoEmpresaERP) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoDealFiscal", codigoDealFiscal);
		params.put("closingMapDate", closingMapDate);
		params.put("codigoEmpresaERP", codigoEmpresaERP);

		List<DealFiscal> listResult = getJpaTemplate().findByNamedQueryAndNamedParams(
				DealFiscal.FIND_FISCAL_DEAL_WITH_ACTIVE_ALLOCATIONS_IN_ALLOCATION_MAP_BY_FISCAL_DEAL_AND_CLOSING_MAP_DATE,
				params);

		return listResult;
	}

}