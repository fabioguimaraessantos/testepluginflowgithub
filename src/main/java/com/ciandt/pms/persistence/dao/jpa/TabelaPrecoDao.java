package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.persistence.dao.ITabelaPrecoDao;

/**
 * 
 * A classe TabelaPrecoDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade TabelaPreco.
 * 
 * @since 02/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class TabelaPrecoDao extends AbstractDao<Long, TabelaPreco> implements
		ITabelaPrecoDao {

	/**
	 * Contrutor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public TabelaPrecoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, TabelaPreco.class);
	}

	/**
	 * Busca uma lista de entidades pelo contrato pratica.
	 * 
	 * @param contratoPratica
	 *            entidade populada com os valores do contrato pratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do contrato pratica.
	 */
	@SuppressWarnings("unchecked")
	public List<TabelaPreco> findByContratoPratica(
			final ContratoPratica contratoPratica) {

		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_BY_CONTRATO_PRATICA,
				new Object[] { contratoPratica.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Busca a entidade com maior data inicio da vigencia.
	 * 
	 * @param contratoPratica
	 *            entidade populada com os valores do contrato pratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do contrato pratica.
	 */
	@SuppressWarnings("unchecked")
	public TabelaPreco findMaxStartDateByContratoPratica(
			final ContratoPratica contratoPratica) {
		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_BY_MAX_START_DATE_BY_CONTRATO_PRATICA,
				new Object[] { contratoPratica.getCodigoContratoPratica(),
						contratoPratica.getCodigoContratoPratica() });
		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Busca uma lista de entidades pelo nome e ContratoPratica.
	 * 
	 * @param description
	 *            - nome da tabela para buscar
	 * 
	 * @param cp
	 *            - ContratoPratica para buscar
	 * 
	 * @return lista de entidades que atendem ao criterio do parametro.
	 */
	@SuppressWarnings("unchecked")
	public List<TabelaPreco> findByContratoPraticaAndName(
			final ContratoPratica cp, final String description) {

		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_BY_CONTRATO_PRATICA_AND_NAME,
				new Object[] { description, cp.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Busca Tabelas de preco por msa.
	 * 
	 * @param msa
	 *            msa.
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<TabelaPreco> findByMsa(final Msa msa) {
		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_BY_MSA, new Object[] { msa.getCodigoMsa() });
		return listResult;
	}

	/**
	 * Busca Tabela de preco com maior data de inicio por moeda.
	 * 
	 * @param msa
	 *            msa
	 * @param moeda
	 *            moeda
	 * @return tabelapreco
	 */
	@SuppressWarnings("unchecked")
	public TabelaPreco findMaxStartDateByMsaAndCurrency(final Msa msa,
			final Moeda moeda) {
		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_MAX_START_DATE_BY_MSA_AND_CURRENCY,
				new Object[] { msa.getCodigoMsa(), moeda.getCodigoMoeda(),
						moeda.getCodigoMoeda(), msa.getCodigoMsa() });
		if (!listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Busca Tabela de preco com maior data de inicio por moeda e Aprovada.
	 *
	 * @param msa
	 *            msa
	 * @param moeda
	 *            moeda
	 * @return tabelapreco
	 */
	public TabelaPreco findMaxStartDateByMsaAndCurrencyApproved(final Msa msa,
																final Moeda moeda) {
		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_MAX_START_DATE_BY_MSA_AND_CURRENCY_APPROVED,
				new Object[] { msa.getCodigoMsa(), moeda.getCodigoMoeda(),
						moeda.getCodigoMoeda(), msa.getCodigoMsa() });
		if (!listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}


	/**
	 * Busca por msa e nome.
	 * 
	 * @param msa
	 *            msa
	 * @param name
	 *            nome
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<TabelaPreco> findByMsaAndName(final Msa msa, final String name) {
		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_BY_MSA_AND_NAME,
				new Object[] { msa.getCodigoMsa(), name });

		return listResult;

	}

	/**
	 * Busca por msa e ativos (delete logico).
	 * @param msa - the msa
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<TabelaPreco> findByMsaAndActive(final Msa msa) {
		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_BY_MSA_AND_ACTIVE,
				new Object[] { msa.getCodigoMsa() });
		return listResult;
	}

	/**
	 * Busca por codigo de Tabela Pre?o
	 * @param code
	 * @return TabelaPreco
	 */
	@SuppressWarnings("unchecked")
	public TabelaPreco findByCode(final Long code) {
		List<TabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				TabelaPreco.FIND_BY_CODE,
				new Object[] { code });
		if(listResult != null){
			return listResult.get(0);
		}
		return null;
	}
}
