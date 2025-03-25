package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.TabelaPreco;

/**
 * 
 * A classe ITabelaPrecoDao proporciona a interfece de acesso para o banco de
 * dados referente a entidade TabelaPreco.
 * 
 * @since 02/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface ITabelaPrecoDao extends IAbstractDao<Long, TabelaPreco> {

	/**
	 * Busca uma lista de entidades pelo contrato pratica.
	 * 
	 * @param contratoPratica
	 *            entidade populada com os valores do contrato pratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do contrato pratica.
	 */
	List<TabelaPreco> findByContratoPratica(
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
	List<TabelaPreco> findByContratoPraticaAndName(final ContratoPratica cp,
			final String description);

	/**
	 * Busca Tabelas de preco por msa.
	 * 
	 * @param msa
	 *            msa.
	 * @return listResult.
	 */
	List<TabelaPreco> findByMsa(final Msa msa);

	/**
	 * Busca Tabela de preco com maior data de inicio por moeda.
	 * 
	 * @param msa
	 *            msa
	 * @param moeda
	 *            moeda
	 * @return tabelapreco
	 */
	TabelaPreco findMaxStartDateByMsaAndCurrency(final Msa msa,
			final Moeda moeda);

	/**
	 * Busca por msa e nome.
	 * 
	 * @param msa
	 *            msa
	 * @param name
	 *            nome
	 * @return TabelaPreco
	 */
	List<TabelaPreco> findByMsaAndName(final Msa msa, final String name);

	/**
	 * Busca por msa e ativos (delete logico).
	 * 
	 * @param msa
	 *            - the msa
	 * @return listResult
	 */
	List<TabelaPreco> findByMsaAndActive(final Msa msa);

	/**
	 * Busca por codigo da Tabela Pre?o
	 *
	 * @param code
	 *            - the code
	 * @return TabelaPreco
	 */
	TabelaPreco findByCode(final Long code);
	/**
	 * Busca Tabela de preco com maior data de inicio por moeda Aprovada.
	 *
	 * @param msa
	 *            msa
	 * @param moeda
	 *            moeda
	 * @return tabelapreco
	 */
	TabelaPreco findMaxStartDateByMsaAndCurrencyApproved(final Msa msa, final Moeda moeda);


}
