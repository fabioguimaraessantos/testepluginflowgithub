package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.ItemTabPerPadrao;
import com.ciandt.pms.model.TabelaPerfilPadrao;

/**
 * 
 * A classe IItemTabPerfilPadraoDao proporciona as funcionalidades de ... para
 * ...
 * 
 * @since 21/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public interface IItemTabPerPadraoDao extends
		IAbstractDao<Long, ItemTabPerPadrao> {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return lista
	 */
	List<ItemTabPerPadrao> findAll();

	/**
	 * Busca pelas entidades por tabelaPerfilPadrao.
	 * 
	 * @param entity
	 *            tabela
	 * @return lista
	 */
	List<ItemTabPerPadrao> findByTabelaPerfilPadrao(
			final TabelaPerfilPadrao entity);

}