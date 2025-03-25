package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.ItemFaturaApagado;

/**
 * 
 * A classe IItemFaturaApagadoService proporciona a interface de servico para a
 * entidade IItemFaturaApagado.
 * 
 * @since 30/10/2014
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Service
public interface IItemFaturaApagadoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void create(final ItemFaturaApagado entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ItemFaturaApagado findById(final Long id);

	/**
	 * Busca {@link ItemFatura} associados a {@code faturaApagada}.
	 * 
	 * @param faturaApagada
	 *
	 * @return lista de {@link ItemFaturaApagada}
	 */
	List<ItemFaturaApagado> findByFaturaApagada(final FaturaApagada faturaApagada);

	List<ItemFaturaApagado> findByCodigoFaturaApagada(Long codigoFaturaApagada);

	/**
	 * Soma o total dos valores dos {@link ItemFaturaApagado} associados a {@code FaturaApagada}.
	 * 
	 * @param faturaApagada
	 * 
	 * @return Soma dos totais dos {@link ItemFaturaApagado}
	 */
	BigDecimal getTotalByFaturaApagada(final FaturaApagada faturaApagada);

}