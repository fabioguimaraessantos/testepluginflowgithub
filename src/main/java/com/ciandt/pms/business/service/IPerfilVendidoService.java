package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PerfilVendido;

/**
 * Define a interface do Service da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IPerfilVendidoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return true se criado com sucesso, caso contrario retorna false
	 */
	@Transactional
	Boolean createPerfilVendido(final PerfilVendido entity);

	/**
	 * Executa um update na entidade passada por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 * @return true se update com sucesso, caso contrario retorna false
	 */
	@Transactional
	void updatePerfilVendido(final PerfilVendido entity);
	

	/**
	 * Remove a entidade passada por parametro, exclusao na tela de abas,
	 * verifica as Alocacao e remove os ItemTabelaPreco.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @return retorna true se sucesso senao false
	 */
	@Transactional
	boolean removePerfilVendido(final PerfilVendido entity);

	/**
	 * Remove a entidade passada por parametro.
	 * 
	 * @param entity
	 *            que será removida
	 */
	@Transactional
	void removePerfilVendidoFinal(final PerfilVendido entity);

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	PerfilVendido findPerfilVendidoById(final Long entityId);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<PerfilVendido> findPerfilVendidoAll();

	/**
	 * Busca os perfis vendidos por ContratoPratica.
	 * 
	 * @param contratoPratica
	 *            pratica que será utilizado na busca por PerfilVendido
	 * 
	 * @return lista de PerfilVendido
	 */
	List<PerfilVendido> findPerfilVendidoByContratoPratica(
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
	 * Busca a lista de perfil vendido por contrato pratica e por suas moedas.
	 * 
	 * @param cp
	 *            contrato pratica
	 * @return listResult
	 */
	List<PerfilVendido> findPerfilVendidoByCLobWithCurrency(
			final ContratoPratica cp);

	/**
	 * Find by Msa.
	 * 
	 * @param msa
	 *            msa.
	 * @return listresult.
	 */
	List<PerfilVendido> findPerfilVendidoByMsa(final Msa msa);

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
	List<PerfilVendido> findPerfilVendidoByMsaAndName(
			final Msa msa, final String nomePerfilVendido);

	/**
	 * Busca PerfilVendido por msa e ativos.
	 * 
	 * @param msa
	 *            msa.
	 * @return listresult
	 */
	List<PerfilVendido> findPerfilVendidoByMsaAndActive(final Msa msa);

	/**
	 * Busca {@link PerfilVendido} por {@link Msa}, por e ativos.
	 * 
	 * @param msa
	 *            {@link Msa}.
	 * 
	 * @param moeda
	 *            {@link Moeda}.
	 * 
	 * @return listresult
	 */
	List<PerfilVendido> findPerfilVendidoByMsaAndMoedaAndActive(final Msa msa,
			final Moeda moeda);

	/**
	 * Busca PerfilVendido por msa com moeda.
	 * 
	 * @param msa
	 *            msa.
	 * @return lista de PerfilVendido.
	 */
	List<PerfilVendido> findPerfilVendidoByMsaWithCurrency(final Msa msa);

	/**
	 * Busca PerfilVendido por msa e nao deletados logicamente.
	 * 
	 * @param msa
	 *            msa.
	 * @return lista de PerfilVendido.
	 */
	List<PerfilVendido> findPerfilVendidoByMsaAndNotLogicalDelete(final Msa msa);
	
	/**
	 * Verifica se já existe um PerfilVendido com o nome a ser criado.
	 * 
	 * @param entity
	 *            - entidade do tipo PerfilVendido.
	 * @return true, se existe caso contrario retorna false.
	 */
	Boolean isNameExists(final PerfilVendido entity);
	
}
