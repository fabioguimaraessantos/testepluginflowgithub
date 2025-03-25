package com.ciandt.pms.business.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ciandt.pms.model.*;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IItemTabelaPrecoService;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.business.service.ITabelaPrecoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import com.ciandt.pms.persistence.dao.IItemTabelaPrecoDao;
import com.ciandt.pms.util.LoginUtil;

/**
 * 
 * A classe ItemTabelaPrecoService proporciona as funcionalidades da camada de
 * serviço referente a entidade ItemTabelaPreco.
 * 
 * @since 03/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class ItemTabelaPrecoService implements IItemTabelaPrecoService {

	/** Instancia do DAO da entidade ItemTabelaPreco. */
	@Autowired
	private IItemTabelaPrecoDao itemTabelaPrecoDao;

	/** Instancia do serviço da entidade PerfilVendido. */
	@Autowired
	private IPerfilVendidoService perfilVendioService;

	/**  */
	@Autowired
	private ITabelaPrecoService tabelaPrecoService;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * @return true.
	 */
	public Boolean createItemTabelaPreco(final ItemTabelaPreco entity) {
		itemTabelaPrecoDao.create(entity);
		return true;
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public ItemTabelaPreco findItemTabelaPrecoById(final Long id) {
		return itemTabelaPrecoDao.findById(id);
	}

	/**
	 * Busca uma lista de entidades pelo TabelaPreco.
	 * 
	 * @param tabelaPreco
	 *            entidade populada com a tabela de preco.
	 * 
	 * @return lista de entidades que estao associados a TabelaPreco.
	 */
	public List<ItemTabelaPreco> findItemTabelaPrecoByTabelaPreco(
			final TabelaPreco tabelaPreco) {
		return itemTabelaPrecoDao.findByTabelaPreco(tabelaPreco);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * @return true.
	 */
	public Boolean removeItemTabelaPreco(final ItemTabelaPreco entity) {
		itemTabelaPrecoDao.remove(entity);
		return true;
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	public void updateItemTabelaPreco(final ItemTabelaPreco entity) {
		ItemTabelaPreco item = itemTabelaPrecoDao.findById(entity
				.getCodigoItemTbPreco());

		item.setPerfilVendido(perfilVendioService.findPerfilVendidoById(entity
				.getPerfilVendido().getCodigoPerfilVendido()));
		item.setTabelaPreco(tabelaPrecoService.findTabelaPrecoById(entity
				.getTabelaPreco().getCodigoTabelaPreco()));
		item.setValorItemTbPreco(entity.getValorItemTbPreco());

		itemTabelaPrecoDao.update(item);
	}

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
	@Override
	public ItemTabelaPreco findItemTabPrecoByMsaAndPerfil(final Msa msa,
			final PerfilVendido perfil, final Date dataMes) {
		return itemTabelaPrecoDao.findByMsaAndPerfil(msa, perfil, dataMes);
	}

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
	public ItemTabelaPreco findByTabelaPrecoAndPerfil(
			final TabelaPreco tabelaPreco, final PerfilVendido perfil) {
		return itemTabelaPrecoDao.findByTabelaPrecoAndPerfil(tabelaPreco,
				perfil);
	}

	/**
	 * Busca uma lista de ItemTabelaPreco associado a um perfil vendido passado
	 * por parametro.
	 * 
	 * @param perfilVendido
	 *            do tipo PerfilVendido para realizar a busca
	 * 
	 * @return lista de ItemTabelaPreco que estao associados a PerfilVendido
	 */
	public List<ItemTabelaPreco> findTabelaPrecoByPerfilVendido(
			final PerfilVendido perfilVendido) {
		return itemTabelaPrecoDao.findByPerfilVendido(perfilVendido);
	}

	/**
	 * 
	 * @param lista
	 *            lista
	 */
	@Transactional
	public void updateListItemTabPrecoRow(final List<ItemTabelaPrecoRow> lista) {
		for (ItemTabelaPrecoRow item : lista) {
			// Busca itemTabelaPreco
			ItemTabelaPreco itemTabPreco = itemTabelaPrecoDao.findById(item
					.getItemTabelaPreco().getCodigoItemTbPreco());
			itemTabPreco.setPercentualDespesa(item.getItemTabelaPreco()
					.getPercentualDespesa());

			// Verifica se usuario mudou o valor do fte ou da hora.
			BigDecimal valorAtual = itemTabPreco.getValorItemTbPreco();
			if (valorAtual.setScale(2, RoundingMode.HALF_UP).equals(
					item.getItemTabelaPreco().getValorItemTbPreco()
							.setScale(2, RoundingMode.HALF_UP))) {
				itemTabPreco.setValorItemTbPreco(new BigDecimal(
						item.getFte() / 168));
			} else {
				itemTabPreco.setValorItemTbPreco(item.getItemTabelaPreco()
						.getValorItemTbPreco());
			}

			itemTabPreco.setIndicadorApproved(item.getApproved().equals(Boolean.TRUE) ? Constants.YES : Constants.NO);

			// Atualiza entidade
			itemTabelaPrecoDao.update(itemTabPreco);
		}

	}

	/**
	 * Upload do arquivo de item da tabela de preco.
	 * 
	 * @param uploadItem
	 *            - the {@link UploadItem}
	 * 
	 * @return retorna um {@link UploadArquivo}
	 * 
	 * @throws IOException
	 *             lanca {@link IOException}
	 */
	public UploadArquivo uploadArquivoItemTabPreco(final UploadItem uploadItem)
			throws IOException {

		UploadArquivo arquivoItemTabPreco = new UploadArquivo();

		String fileName = uploadItem.getFileName();
		arquivoItemTabPreco.setNomeArquivo(fileName);
		arquivoItemTabPreco.setCodigoAutor(LoginUtil.getLoggedUsername());
		arquivoItemTabPreco.setDataDataHoraUpload(new Date());
		arquivoItemTabPreco.setValorBytes(String.valueOf(uploadItem
				.getFileSize()));
		arquivoItemTabPreco.setTextoExtArquivo(fileName.substring(
				fileName.lastIndexOf('.') + 1, fileName.length()));
		Messages.showSucess("uploadBancoHoras",
				Constants.UPLOAD_MSG_SUCCESS_UPLOAD);
		return arquivoItemTabPreco;

	}

	/**
	 * Metodo que verifica se existe alocacao vigente utilizando o perfil vendido dentro da vigencia da tabela de preço que esta sendo editada
	 * @param codigoPerfilVendido
	 * @param codigoTabelaPreco
	 * @return Boolean
	 */
	public Boolean canRemove(Long codigoPerfilVendido, Long codigoTabelaPreco){
		boolean canRemove = false;

		if(itemTabelaPrecoDao.findAllocationByPerfilVendidoAndTabelaPreco(codigoPerfilVendido, codigoTabelaPreco) == 0){
			canRemove = true;
		}

		return canRemove;
	}

	@Override
	public List<ItemTabelaPreco> findByPerfilAndMoeda(final PerfilVendido perfilVendido) {
		return itemTabelaPrecoDao.findByPerfilAndMoeda(perfilVendido);
	}

	@Override
	public Map<Long, ItemTabelaPreco> findMapItensByTabelaPreco(TabelaPreco tabelaPreco) {
		return getMapByList(findItemTabelaPrecoByTabelaPreco(tabelaPreco));
	}

	/**
	 * @param items
	 * @return
	 */
	private Map<Long, ItemTabelaPreco> getMapByList(List<ItemTabelaPreco> items) {
		Map<Long, ItemTabelaPreco> map = new HashMap<Long, ItemTabelaPreco>();

		if (items != null && !items.isEmpty()) {
			for (ItemTabelaPreco item : items)
				map.put(item.getPerfilVendido().getCodigoPerfilVendido(), item);
		}

		return map;
	}

	public void updateListItemTabelaPreco(List<ItemTabelaPreco> items) {
		if (items != null && !items.isEmpty()) {
			for (ItemTabelaPreco item : items)
				itemTabelaPrecoDao.merge(item);
		}
	}
}