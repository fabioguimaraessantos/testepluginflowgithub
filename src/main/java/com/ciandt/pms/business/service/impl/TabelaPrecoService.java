package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow.PriceTableFlow;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.persistence.dao.ITabelaPrecoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 
 * A classe TabelaPrecoService proporciona as funcionalidades da camada de
 * servi�o referentes a entidade TabelaPreco.
 * 
 * @since 02/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class TabelaPrecoService extends TabelaPrecoDeleteTemplate implements ITabelaPrecoService  {

	@Autowired
	private ITabelaPrecoDao tabelaPrecoDao;

	@Autowired
	private IItemTabelaPrecoService itemTbPrecoService;

	@Autowired
	private IAnexoTabelaPrecoService anexoTabelaPrecoService;

	@Autowired
	private IModuloService moduloService;

	/**
	 * Cria uma entidade TabelaPreco.
	 * 
	 * @param entity
	 *            da tabela de pre�o
	 * 
	 * @return retorna true se a data inicio da vigencia, � posterior a maior
	 *         data existente no banco de dados. Caso contrario retorna false
	 */
	public Boolean createTabelaPreco(final TabelaPreco entity) {

		TabelaPreco tp = findMaxStartDatebyMsaAndCurrency(entity.getMsa(),
				entity.getMoeda());

		// verifica se o nome j� existe
		if (isNameExists(entity)) {
			Messages.showError("createTabelaPreco",
					Constants.DEFAULT_MSG_ERROR_NAME_ALREADY_EXISTS,
					entity.getDescricaoTabelaPreco());

			return Boolean.FALSE;
		}

		// verifica se a data de vigencia � a maior
		if (tp != null) {
			if (!entity.getDataInicioVigencia()
					.after(tp.getDataInicioVigencia())) {

				Messages.showError("createTabelaPreco",
						Constants.MSG_ERROR_ADD_TABELA_PRECO_PERIOD);
				return Boolean.FALSE;
			}
		}

		tabelaPrecoDao.create(entity);

		return Boolean.TRUE;
	}

	/**
	 * Verifica se j� existe uma TabelaPreco com o nome a ser criado.
	 * 
	 * @param entity
	 *            - entidade do tipo TabelaPreco.
	 * @return true, se existe caso contrario retorna false.
	 */
	private Boolean isNameExists(final TabelaPreco entity) {

		if (tabelaPrecoDao.findByMsaAndName(entity.getMsa(),
				entity.getDescricaoTabelaPreco()).isEmpty()) {
			return false;
		}

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
	public TabelaPreco findTabelaPrecoById(final Long id) {
		return tabelaPrecoDao.findByCode(id);
	}

	/**
	 * Busca uma lista de entidades pelo contrato pratica.
	 * 
	 * @param contratoPratica
	 *            entidade populada com os valores do contrato pratica.
	 * @return lista de entidades que atendem ao criterio do contrato pratica.
	 */
	public List<TabelaPreco> findTabelaPrecoByContratoPratica(
			final ContratoPratica contratoPratica) {

		return tabelaPrecoDao.findByContratoPratica(contratoPratica);
	}

	/**
	 * Remove a entidade passada por parametro.
	 *
	 * @param entity
	 *            que ser� removida
	 * @return true se TabelaPreco foi removida corretamente, coso contr�rio
	 *         retorna false
	 */
	public Boolean removeTabelaPreco(final TabelaPreco entity) {
		try {
			return this.delete(entity);
		} catch (BusinessException e) {
			Messages.showError("removeTabelaPreco",
					e.getMessage());
			return false;
		}
	}

	/**
	 * Realiza um update da entidade passada por parametro.
	 * 
	 * @param entity
	 *            que sofrera o update
	 */
	public void updateTabelaPreco(final TabelaPreco entity) {
		tabelaPrecoDao.update(entity);
	}

	/**
	 * Busca a entidade com maior data inicio da vigencia.
	 * 
	 * @param contratoPratica
	 *            entidade populada com os valores do contrato pratica.
	 * 
	 * @return lista de entidades que atendem ao criterio do contrato pratica.
	 */
	public TabelaPreco findMaxStartDateByContratoPratica(
			final ContratoPratica contratoPratica) {
		return tabelaPrecoDao
				.findMaxStartDateByContratoPratica(contratoPratica);
	}

	/**
	 * Busca Tabelas de preco por msa.
	 * 
	 * @param msa
	 *            msa
	 * @return listResult.
	 */
	public List<TabelaPreco> findTabelaPrecoByMsa(final Msa msa) {
		return tabelaPrecoDao.findByMsa(msa);
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
	public TabelaPreco findMaxStartDatebyMsaAndCurrency(final Msa msa,
			final Moeda moeda) {
		return tabelaPrecoDao.findMaxStartDateByMsaAndCurrency(msa, moeda);
	}

	/**
	 * Busca Tabela de preco com maior data de inicio por moeda Aprovada.
	 *
	 * @param msa
	 *            msa
	 * @param moeda
	 *            moeda
	 * @return tabelapreco
	 */
	public TabelaPreco findMaxStartDatebyMsaAndCurrencyApproved(final Msa msa, final Moeda moeda) {
		return tabelaPrecoDao.findMaxStartDateByMsaAndCurrencyApproved(msa, moeda);
	}




	/**
	 * Busca por msa e ativos(delete logico).
	 * 
	 * @param msa
	 *            - the msa
	 * @return listResult
	 */
	public List<TabelaPreco> findTabelaPrecoByMsaAndActive(final Msa msa) {
		return tabelaPrecoDao.findByMsaAndActive(msa);
	}

	/**
	 * Busca por codigo da Tabela Pre�o
	 *
	 * @param code
	 *            - the code
	 * @return TabelaPreco
	 */
	public TabelaPreco findByCode(final Long code){
		return tabelaPrecoDao.findByCode(code);
	}

	@Override
	public boolean canDeletePriceTable(TabelaPreco entity) {

		if(entity.getPriceTableStatus().getCode().equals(PriceTableFlow.STATUS_DRAFT))
			return Boolean.TRUE;

		if(moduloService.dateAfterHistoryLock(entity.getDataInicioVigencia()))
			return Boolean.TRUE;

		return Boolean.FALSE;
	}

	@Override
	public boolean markAsDeleted(TabelaPreco entity) {
		try {
			entity.delete();
			return true;
		} catch (IllegalStateException e) {
			Messages.showError("removeTabelaPreco",
					Constants.MSG_ERROR_TABELA_PRECO_DELETE);
			return false;
		}
	}
}
