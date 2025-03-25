package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaMoeda;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * A classe IItemReceitaDao proporciona a interface de DAO para a entidade
 * ItemReceita.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IItemReceitaDao extends IAbstractDao<Long, ItemReceita> {

	/**
	 * Busca os itens de uma receita.
	 * 
	 * @param receita
	 *            entidade do tipo Receita.
	 * 
	 * @return lista de itnes da receitas.
	 */
	List<ItemReceita> findByReceita(final Receita receita);

	/**
	 * Busca os itens de uma receitaMoeda.
	 * 
	 * @param receitaMoeda
	 *            - receitaMoeda.
	 * @return lista de itens de uma receita moeda.
	 */
	List<ItemReceita> findByReceitaMoeda(final ReceitaMoeda receitaMoeda);
	@Transactional
	Boolean updateAllMultiDealFiscalTaxAndNetRevenueAfterDate(final Date dataMes);
	@Transactional
	Boolean updateAllTaxAndNetRevenueAfterDate(final Date dataMes);

}
