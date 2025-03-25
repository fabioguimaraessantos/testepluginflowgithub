package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.VwMegaCCustoId;
import com.ciandt.pms.model.vo.FormFilter;

/**
 * A classe IVwMegaCCustoService proporciona as funcionalidades de interface
 * para entidade {@link VwMegaCCusto}.
 * 
 * @since Out 11, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
@Service
public interface IVwMegaCCustoService {

	/**
	 * Busca todos os {@link VwMegaCCusto}.
	 * 
	 * @return lista de {@link VwMegaCCusto}
	 */
	List<VwMegaCCusto> findAll();

	List<VwMegaCCusto> findAllForCombobox();

	/**
	 * Busca um {@link VwMegaCCusto} por id.
	 * 
	 * @param id
	 *            id
	 * @return {@link VwMegaCCusto}
	 */
	VwMegaCCusto findById(final Long id);

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