package com.ciandt.pms.business.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IItemTabelaPrecoService;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.persistence.dao.IPerfilVendidoDao;

/**
 * Define o Service da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class PerfilVendidoService implements IPerfilVendidoService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IPerfilVendidoDao perfilVendidoDao;

	/** Intancia do servico. */
	@Autowired
	private IItemTabelaPrecoService itemTbService;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return true se inserido com sucesso, caso contrario false.
	 */
	public Boolean createPerfilVendido(final PerfilVendido entity) {

		if (isNameExists(entity)) {
			Messages.showError("createTabelaPreco",
					Constants.DEFAULT_MSG_ERROR_NAME_ALREADY_EXISTS,
					entity.getNomePerfilVendido());

			return Boolean.FALSE;
		}

		perfilVendidoDao.create(entity);

		return Boolean.TRUE;
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 * @return true se update com sucesso, caso contrario retorna false
	 */
	public void updatePerfilVendido(final PerfilVendido entity) {

		perfilVendidoDao.update(entity);

	}

	/**
	 * Verifica se já existe um PerfilVendido com o nome a ser criado.
	 * 
	 * @param entity
	 *            - entidade do tipo PerfilVendido.
	 * @return true, se existe caso contrario retorna false.
	 */
	public Boolean isNameExists(final PerfilVendido entity) {

		List<PerfilVendido> listPerfil = findPerfilVendidoByMsaAndName(
				entity.getMsa(), entity.getNomePerfilVendido());

		Long codigoPerfilVendido = entity.getCodigoPerfilVendido();

		if (listPerfil.isEmpty()) {
			return false;
		} else if ((codigoPerfilVendido != null)
				&& (listPerfil.get(0).getCodigoPerfilVendido()
						.compareTo(codigoPerfilVendido)) == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @return retorna true se sucesso senao false
	 */
	public boolean removePerfilVendido(final PerfilVendido entity) {

		if (!entity.getAlocacaos().isEmpty()) {
			Messages.showError("removePerfilVendido",
					"_nls.perfil_vendido.error.delete.exists.alocacao");
			return false;
		}

		Iterator<ItemTabelaPreco> itemTabelaPrecoIt = entity
				.getItemTabelaPrecos().iterator();
		ItemTabelaPreco itemTabelaPreco;
		while (itemTabelaPrecoIt.hasNext()) {
			itemTabelaPreco = itemTabelaPrecoIt.next();
			itemTbService.removeItemTabelaPreco(itemTabelaPreco);
		}

		perfilVendidoDao.remove(entity);

		return true;
	}

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	@Transactional
	public PerfilVendido findPerfilVendidoById(final Long entityId) {
		PerfilVendido perfilVendido = perfilVendidoDao.findById(entityId);
		return perfilVendido;
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<PerfilVendido> findPerfilVendidoAll() {
		return perfilVendidoDao.findAll();
	}

	/**
	 * Busca os perfis vendidos por ContratoPratica.
	 * 
	 * @param contratoPratica
	 *            pratica que será utilizado na busca por PerfilVendido
	 * 
	 * @return lista de PerfilVendido
	 */
	public List<PerfilVendido> findPerfilVendidoByContratoPratica(
			final ContratoPratica contratoPratica) {

		return perfilVendidoDao.findByContratoPratica(contratoPratica);
	}

	/**
	 * Remove a entidade passada por parametro.
	 * 
	 * @param entity
	 *            que será removida
	 */
	public void removePerfilVendidoFinal(final PerfilVendido entity) {
		perfilVendidoDao.remove(entity);
	}

	/**
	 * Busca os perfis vendidos por ContratoPratica e Ativos.
	 * 
	 * @param contratoPratica
	 *            pratica que será utilizado na busca por PerfilVendido
	 * 
	 * @return lista de PerfilVendido
	 */
	public List<PerfilVendido> findByContratoPraticaAndActive(
			final ContratoPratica contratoPratica) {

		return perfilVendidoDao.findByContratoPraticaAndActive(contratoPratica);
	}

	/**
	 * Busca a lista de perfil vendido por contrato pratica e por suas moedas.
	 * 
	 * @param cp
	 *            contrato pratica
	 * @return listResult
	 */
	public List<PerfilVendido> findPerfilVendidoByCLobWithCurrency(
			final ContratoPratica cp) {
		return perfilVendidoDao.findByCLobWithCurrency(cp);
	}

	/**
	 * Find by Msa.
	 * 
	 * @param msa
	 *            msa.
	 * @return listresult.
	 */
	public List<PerfilVendido> findPerfilVendidoByMsa(final Msa msa) {
		return perfilVendidoDao.findByMsa(msa);
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
	public List<PerfilVendido> findPerfilVendidoByMsaAndName(
			final Msa msa, final String nomePerfilVendido) {
		return perfilVendidoDao.findByMsaAndName(msa, nomePerfilVendido);
	}

	/**
	 * Busca PerfilVendido por msa e ativos.
	 * 
	 * @param msa
	 *            msa.
	 * @return listresult
	 */
	public List<PerfilVendido> findPerfilVendidoByMsaAndActive(final Msa msa) {
		return perfilVendidoDao.findByMsaAndActive(msa);
	}

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
	public List<PerfilVendido> findPerfilVendidoByMsaAndMoedaAndActive(
			final Msa msa, final Moeda moeda) {
		return perfilVendidoDao.findByMsaAndMoedaAndActive(msa, moeda);
	}

	/**
	 * Busca PerfilVendido por msa com moeda.
	 * 
	 * @param msa
	 *            msa.
	 * @return lista de PerfilVendido.
	 */
	public List<PerfilVendido> findPerfilVendidoByMsaWithCurrency(final Msa msa) {
		return perfilVendidoDao.findByMsaWithCurrency(msa);
	}

	/**
	 * Busca PerfilVendido por msa e nao deletados logicamente.
	 * 
	 * @param msa
	 *            msa.
	 * @return lista de PerfilVendido.
	 */
	public List<PerfilVendido> findPerfilVendidoByMsaAndNotLogicalDelete(
			final Msa msa) {
		return perfilVendidoDao.findByMsaAndNotLogicalDelete(msa);
	}

}