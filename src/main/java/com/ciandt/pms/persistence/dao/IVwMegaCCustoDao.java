package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.vo.FormFilter;

/**
 * A classe IVwMegaCCustoDao proporciona as funcionalidades interface de acesso
 * ao banco de dados referente a entidade {@link VwMegaCCusto}.
 * 
 * @since Out 11, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
public interface IVwMegaCCustoDao extends IAbstractDao<Long, VwMegaCCusto> {
	/**
	 * Busca todos os {@link VwMegaCCusto}.
	 * 
	 * @return retorna todos os {@link VwMegaCCusto}
	 */
	List<VwMegaCCusto> findAll();

	List<VwMegaCCusto> findAllForCombobox();

	/**
	 * Busca todos os{@link VwMegaCCusto} pendentes.
	 * 
	 * @return uma lista de {@link VwMegaCCusto}
	 */
	List<VwMegaCCusto> findAllPendentes();

	/**
	 * Busca todos os{@link VwMegaCCusto} por um {@link FormFilter}.
	 * 
	 * @param filter
	 *            {@link FormFilter}
	 * @return uma lista de {@link VwMegaCCusto}
	 */
	List<VwMegaCCusto> findPendentesByFormFilter(FormFilter filter);
}