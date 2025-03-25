package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PerfilVendido;

/**
 * 
 * A classe IPerfilVendidoDao proporciona interface de acesso para a camada DAO
 * da entidade PerfilVendido.
 * 
 * @since 17/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IPerfilVendidoDao extends IAbstractDao<Long, PerfilVendido> {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<PerfilVendido> findAll();

	/**
	 * Busca os perfis vendidos por ContratoPratica.
	 * 
	 * @param contratoPratica
	 *            pratica que será utilizado na busca por PerfilVendido
	 * 
	 * @return lista de PerfilVendido
	 */
	List<PerfilVendido> findByContratoPratica(
			final ContratoPratica contratoPratica);

	/**
	 * Busca os perfis vendidos por ContratoPratica e Ativos.
	 * 
	 * @param contratoPratica
	 *            pratica que será utilizado na busca por PerfilVendido
	 * 
	 * @return lista de PerfilVendido
	 */
	List<PerfilVendido> findByContratoPraticaAndActive(
			final ContratoPratica contratoPratica);

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
	List<PerfilVendido> findByMsaAndName(final Msa msa,
			final String nomePerfilVendido);

	/**
	 * Busca a lista de perfil vendido por contrato pratica e por suas moedas.
	 * 
	 * @param cp
	 *            contrato pratica
	 * @return listResult
	 */
	List<PerfilVendido> findByCLobWithCurrency(final ContratoPratica cp);

	/**
	 * Find By Msa.
	 * 
	 * @param msa
	 *            msa.
	 * @return listResult.
	 */
	List<PerfilVendido> findByMsa(final Msa msa);

	/**
	 * Busca PerfilVendido por msa e ativos.
	 * 
	 * @param msa
	 *            msa.
	 * @return listresult
	 */
	List<PerfilVendido> findByMsaAndActive(final Msa msa);

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
	List<PerfilVendido> findByMsaAndMoedaAndActive(final Msa msa,
			final Moeda moeda);
	
	/**
	 * Busca PerfilVendido por msa com moeda.
	 * @param msa msa
	 * @return lista de perfilVendido.
	 */
	List<PerfilVendido> findByMsaWithCurrency(final Msa msa);

	/**
	 * Busca PerfilVendido por msa e nao deletados logicamente.
	 * 
	 * @param msa
	 *            msa
	 * @return lista de perfilVendido.
	 */
	List<PerfilVendido> findByMsaAndNotLogicalDelete(final Msa msa);
	
}
