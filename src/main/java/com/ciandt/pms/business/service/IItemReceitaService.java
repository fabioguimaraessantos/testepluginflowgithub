package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ReceitaDealFiscal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaMoeda;

/**
 * 
 * A classe IItemReceitaService proporciona a interface de servico para a
 * entidade ItemReceita.
 * 
 * @since 28/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IItemReceitaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createItemReceita(final ItemReceita entity);

	/**
	 * Atualiza a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	@Transactional
	void updateItemReceita(final ItemReceita entity);

	/**
	 * Remove a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será removida.
	 * 
	 */
	@Transactional
	void removeItemReceita(final ItemReceita entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ItemReceita findItemReceitaById(final Long id);

	/**
	 * Busca os itens de uma receita.
	 * 
	 * @param receita
	 *            entidade do tipo Receita.
	 * 
	 * @return lista de itnes da receitas.
	 */
	List<ItemReceita> findItemReceitaByReceita(final Receita receita);

	/**
	 * Busca os itens de uma receitaMoeda.
	 * 
	 * @param receitaMoeda
	 *            - receitaMoeda.
	 * @return lista de itens de uma receita moeda.
	 */
	List<ItemReceita> findItemReceitaByReceitaMoeda(
			final ReceitaMoeda receitaMoeda);

	List<ItemReceita> calculateTax(List<ReceitaDealFiscal> receitasDealFiscal, List<ItemReceita> itensReceita);

	List<ItemReceita> calculateTaxMultiDealFiscal(List<ReceitaDealFiscal> receitasDealFiscal, List<ItemReceita> itensReceita);

	@Transactional
	Boolean updateAllTaxAndNetRevenueAfterDate(final Date dataMes);

	@Transactional
	Boolean updateAllMultiDealFiscalTaxAndNetRevenueAfterDate(final Date dataMes);
}