package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.TabelaPreco;

/**
 * 
 * A classe IItemTabelaPrecoDao proporciona a interface a acesso ao banco de
 * dados referente a entidade ItemTabelaPreco.
 * 
 * @since 03/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IItemTabelaPrecoDao extends
		IAbstractDao<Long, ItemTabelaPreco> {

	/**
	 * Busca uma lista de ItemTabelaPreco associado a uma TabelaPreco passada
	 * por parametro.
	 * 
	 * @param tabelaPreco
	 *            do tipo TabelaPreco para realizar a busca
	 * @return lista de ItemTabelaPreco que estao associados a TabelaPreco
	 */
	List<ItemTabelaPreco> findByTabelaPreco(final TabelaPreco tabelaPreco);

	/**
	 * Busca um ItemTabelaPreco associado a uma TabelaPreco e PerfilVendido
	 * passada por parametro.
	 * 
	 * @param tabelaPreco
	 *            do tipo TabelaPreco para realizar a busca
	 * @param perfil
	 *            do tipo PerfilVendido para realizar a busca
	 * 
	 * @return ItemTabelaPreco que esta associados a TabelaPreco e PerfilVendido
	 */
	ItemTabelaPreco findByTabelaPrecoAndPerfil(final TabelaPreco tabelaPreco,
			final PerfilVendido perfil);

	/**
	 * Busca uma lista de ItemTabelaPreco associado a um perfil vendido passado
	 * por parametro.
	 * 
	 * @param perfilVendido
	 *            do tipo PerfilVendido para realizar a busca
	 * 
	 * @return lista de ItemTabelaPreco que estao associados a PerfilVendido
	 */
	List<ItemTabelaPreco> findByPerfilVendido(final PerfilVendido perfilVendido);

	/**
	 * Busca um ItemTabelaPreco associado a um Msa e PerfilVendido passada por
	 * parametro.
	 * 
	 * @param msa
	 *            do tipo {@link Msa} para realizar a busca
	 * @param perfil
	 *            do tipo {@link PerfilVendido} para realizar a busca
	 * @param dataMes
	 *            do tipo Date com o mes/ano para realizar a busca
	 * 
	 * @return {@link ItemTabelaPreco} que esta associados a {@link Msa} e
	 *         {@link PerfilVendido}
	 */
	ItemTabelaPreco findByMsaAndPerfil(final Msa msa,
			final PerfilVendido perfil, final Date dataMes);

	Long findAllocationByPerfilVendidoAndTabelaPreco(Long codigoPerfilVendido, Long codigoTabelaPreco);

	List<ItemTabelaPreco> findByPerfilAndMoeda (final PerfilVendido perfilVendido);
}
