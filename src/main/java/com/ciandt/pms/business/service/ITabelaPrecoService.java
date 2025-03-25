package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.TabelaPreco;

/**
 * Define a interface do Service da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface ITabelaPrecoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * @return retorna true se a data inicio da vigencia, � posterior a maior
	 *         data existente no banco de dados. Caso contrario retorna false
	 */
	@Transactional
	Boolean createTabelaPreco(final TabelaPreco entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que ser� atualizada.
	 * 
	 */
	@Transactional
	void updateTabelaPreco(final TabelaPreco entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que ser� apagada.
	 * 
	 * @return true se foi removido corretamente, false caso tenha acontecido
	 *         algum erro
	 * 
	 */
	@Transactional
	Boolean removeTabelaPreco(final TabelaPreco entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	TabelaPreco findTabelaPrecoById(final Long id);

	/**
	 * Retorna todas as tabelas de pre�o associados a um ContratoPratica.
	 * 
	 * @param contratoPratica
	 *            entidade populada com os valores do contrato pratica.
	 * @return retorna uma lista de TabelaPreco de um ContratoPratica.
	 */
	List<TabelaPreco> findTabelaPrecoByContratoPratica(
			final ContratoPratica contratoPratica);

	/**
	 * Busca a entidade com maior data inicio da vigencia.
	 * 
	 * @param contratoPratica
	 *            entidade populada com os valores do contrato pratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do contrato pratica.
	 */
	TabelaPreco findMaxStartDateByContratoPratica(
			final ContratoPratica contratoPratica);

	/**
	 * Busca Tabelas de preco por msa.
	 * 
	 * @param msa
	 *            msa
	 * @return listResult.
	 */
	List<TabelaPreco> findTabelaPrecoByMsa(final Msa msa);

	/**
	 * Busca Tabela de preco com maior data de inicio por moeda.
	 * 
	 * @param msa
	 *            msa
	 * @param moeda
	 *            moeda
	 * @return tabelapreco
	 */
	TabelaPreco findMaxStartDatebyMsaAndCurrency(final Msa msa,
			final Moeda moeda);

	/**
	 * Busca por msa e ativos(delete logico).
	 * 
	 * @param msa
	 *            - the msa
	 * @return listResult
	 */
	List<TabelaPreco> findTabelaPrecoByMsaAndActive(final Msa msa);

	/**
	 * Busca por codigo da Tabela Pre�o
	 *
	 * @param code
	 *            - the code
	 * @return TabelaPreco
	 */
	TabelaPreco findByCode(final Long code);

	/**
	 * Busca Tabela de preco com maior data de inicio por moeda Approved.
	 *
	 * @param msa
	 *            msa
	 * @param moeda
	 *            moeda
	 * @return tabelapreco
	 */
	TabelaPreco findMaxStartDatebyMsaAndCurrencyApproved(final Msa msa, final Moeda moeda);

}
