package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.persistence.dao.IPerfilVendidoDao;

/**
 * 
 * A classe PerfilVendidoDao proporciona as funcionalidades de de acesso ao
 * banco de dados para a entidade PerfilVendido.
 * 
 * @since 17/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class PerfilVendidoDao extends AbstractDao<Long, PerfilVendido>
		implements IPerfilVendidoDao {

	/**
	 * Contrutor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public PerfilVendidoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, PerfilVendido.class);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findAll() {
		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_ALL);

		return listResult;
	}

	/**
	 * Busca os perfis vendidos por ContratoPratica.
	 * 
	 * @param contratoPratica
	 *            pratica que será utilizado na busca por PerfilVendido
	 * 
	 * @return lista de PerfilVendido
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByContratoPratica(
			final ContratoPratica contratoPratica) {

		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_CONTRATO_PRATICA,
				new Object[] { contratoPratica.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Busca os perfis vendidos por ContratoPratica e Ativos.
	 * 
	 * @param contratoPratica
	 *            pratica que será utilizado na busca por PerfilVendido
	 * 
	 * @return lista de PerfilVendido
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByContratoPraticaAndActive(
			final ContratoPratica contratoPratica) {

		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_CONTRATO_PRATICA_AND_ACTIVE,
				new Object[] { contratoPratica.getCodigoContratoPratica() });

		return listResult;

	}

	/**
	 * Busca uma lista de entidades pelo Msa e ContratoPratica.
	 * 
	 * @param nomePerfilVendido
	 *            - nome do PerfilVendido
	 * 
	 * @param msa
	 *            - Msa para buscar
	 * 
	 * @return lista de entidades que atendem ao criterio do parametro.
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByMsaAndName(final Msa msa,
			final String nomePerfilVendido) {

		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_MSA_AND_NAME,
				new Object[] { msa.getCodigoMsa(), nomePerfilVendido });

		return listResult;
	}

	/**
	 * Busca a lista de perfil vendido por contrato pratica e por suas moedas.
	 * 
	 * @param cp
	 *            contrato pratica
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByCLobWithCurrency(final ContratoPratica cp) {

		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_CLOB_WITH_CURRENCY,
				new Object[] { cp.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Find By Msa.
	 * 
	 * @param msa
	 *            msa.
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByMsa(final Msa msa) {
		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_MSA, new Object[] { msa.getCodigoMsa() });
		return listResult;
	}

	/**
	 * Busca PerfilVendido por msa e ativos.
	 * 
	 * @param msa
	 *            msa.
	 * @return listresult
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByMsaAndActive(final Msa msa) {
		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_MSA_AND_ACTIVE,
				new Object[] { msa.getCodigoMsa() });
		return listResult;
	}

	/**
	 * Busca PerfilVendido por msa, por e ativos.
	 * 
	 * @param msa
	 *            {@link Msa}.
	 * 
	 * @param moeda
	 *            {@link Moeda}.
	 * 
	 * @return listresult
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByMsaAndMoedaAndActive(final Msa msa,
			final Moeda moeda) {
		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_MSA_AND_MOEDA_AND_ACTIVE,
				new Object[] { msa.getCodigoMsa(), moeda.getCodigoMoeda() });
		return listResult;
	}

	/**
	 * Busca PerfilVendido por msa com moeda.
	 * 
	 * @param msa
	 *            msa
	 * @return lista de perfilVendido.
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByMsaWithCurrency(final Msa msa) {
		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_MSA_WITH_CURRENCY,
				new Object[] { msa.getCodigoMsa(), msa.getCodigoMsa() });

		return listResult;
	}

	/**
	 * Busca PerfilVendido por msa e nao deletados logicamente.
	 * 
	 * @param msa
	 *            msa
	 * @return lista de perfilVendido.
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilVendido> findByMsaAndNotLogicalDelete(final Msa msa) {
		List<PerfilVendido> listResult = getJpaTemplate().findByNamedQuery(
				PerfilVendido.FIND_BY_MSA_AND_NOT_LOGICAL_DELETE,
				new Object[] { msa.getCodigoMsa() });
		return listResult;
	}
}