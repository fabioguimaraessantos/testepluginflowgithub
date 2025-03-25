package com.ciandt.pms.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;

/**
 * 
 * A classe IItemTabelaPrecoService proporciona a interfece acesso a camada de
 * serviço referente a entidade ItemTabelaPreco.
 * 
 * @since 03/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IItemTabelaPrecoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * @return true
	 */
	@Transactional
	Boolean createItemTabelaPreco(final ItemTabelaPreco entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	@Transactional
	void updateItemTabelaPreco(final ItemTabelaPreco entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * @return true.
	 */
	@Transactional
	Boolean removeItemTabelaPreco(final ItemTabelaPreco entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ItemTabelaPreco findItemTabelaPrecoById(final Long id);

	/**
	 * Busca uma lista de entidades pelo TabelaPreco.
	 * 
	 * @param tabelaPreco
	 *            entidade populada com a tabela de preco.
	 * 
	 * @return lista de entidades que estao associados a TabelaPreco.
	 */
	List<ItemTabelaPreco> findItemTabelaPrecoByTabelaPreco(
			final TabelaPreco tabelaPreco);

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
	ItemTabelaPreco findItemTabPrecoByMsaAndPerfil(final Msa msa,
			final PerfilVendido perfil, final Date dataMes);

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
	List<ItemTabelaPreco> findTabelaPrecoByPerfilVendido(
			final PerfilVendido perfilVendido);

	/**
	 * Atualiza lista de itens da tabela de itemtabelapreco.
	 * 
	 * @param lista
	 *            lista
	 */
	@Transactional
	void updateListItemTabPrecoRow(final List<ItemTabelaPrecoRow> lista);

	/**
	 * 
	 * @param uploadItem
	 * @return
	 * @throws IOException
	 */
	UploadArquivo uploadArquivoItemTabPreco(final UploadItem uploadItem)
			throws IOException;

	Boolean canRemove(Long codigoPerfilVendido, Long codigoTabelaPreco);

	/**
	* Busca uma lista de ItemTabelaPreco vigente associado a um perfil vendido e moeda passados
	* por parametro.
	*
	* @param perfilVendido
	*            do tipo PerfilVendido para realizar a busca
	*
	* @return lista de ItemTabelaPreco
	*/
	List<ItemTabelaPreco> findByPerfilAndMoeda(final PerfilVendido perfilVendido);

	/**
	 * Retorna um mapa de ItemTabelaPreco com a key do perfil.
	 * @param tabelaPreco do tipo TabelaPreco para realizar a busca
	 * @return Mapa de ItemTabelaPreco
	 */
	Map<Long, ItemTabelaPreco> findMapItensByTabelaPreco(TabelaPreco tabelaPreco);

	/**
	 * Atualiza a lista   de itens passada por parâmetro.
	 * @param items Lista de itens a ser atualizada.
	 */
	void updateListItemTabelaPreco(List<ItemTabelaPreco> items);
}
