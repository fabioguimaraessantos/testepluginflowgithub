package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.ItemFaturaApagado;

/**
 * 
 * A classe IItemFaturaApagadoDao proporciona a interface de DAO para a entidade
 * IItemFaturaApagado.
 * 
 * @since 30/10/2014
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
public interface IItemFaturaApagadoDao extends IAbstractDao<Long, ItemFaturaApagado> {

	/**
	 * Busca {@link ItemFatura} associados a {@code faturaApagada}.
	 * 
	 * @param faturaApagada
	 *
	 * @return lista de {@link ItemFaturaApagada}
	 */
	List<ItemFaturaApagado> findByFaturaApagada(final FaturaApagada faturaApagada);

	List<ItemFaturaApagado> findByCodigoFaturaApagada(Long codigoFaturaApagada);
}