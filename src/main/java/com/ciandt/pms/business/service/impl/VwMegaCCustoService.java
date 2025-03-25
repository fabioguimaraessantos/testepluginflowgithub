package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwMegaCCustoService;
import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.VwMegaCCustoId;
import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.persistence.dao.IVwMegaCCustoDao;
import com.ciandt.pms.persistence.dao.jpa.VwMegaCCustoDao;

/**
 * A classe VwMegaCCustoService proporciona as funcionalidades de servico para a
 * entidade VwMegaCCusto.
 * 
 * @since Out 11, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
@Service
public class VwMegaCCustoService implements IVwMegaCCustoService {

	/** Instancia de {@link VwMegaCCustoDao}. */
	@Autowired
	private IVwMegaCCustoDao dao;

	/**
	 * Busca todos os {@link VwMegaCCusto}.
	 * 
	 * @return lista de {@link VwMegaCCusto}
	 */
	@Override
	public List<VwMegaCCusto> findAll() {
		return dao.findAll();
	}

	@Override
	public List<VwMegaCCusto> findAllForCombobox() {
		return dao.findAllForCombobox();
	}

	/**
	 * Busca um {@link VwMegaCCusto} por id.
	 * 
	 * @param id
	 *            id
	 * @return {@link VwMegaCCusto}
	 */
	@Override
	public VwMegaCCusto findById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Busca todos os{@link VwMegaCCusto} pendentes.
	 * 
	 * @return uma lista de {@link VwMegaCCusto}
	 */
	@Override
	public List<VwMegaCCusto> findAllPendentes() {
		return dao.findAllPendentes();
	}

	/**
	 * Busca todos os{@link VwMegaCCusto} por um {@link FormFilter}.
	 * 
	 * @param filter
	 *            {@link FormFilter}
	 * @return uma lista de {@link VwMegaCCusto}
	 */
	@Override
	public List<VwMegaCCusto> findPendentesByFormFilter(FormFilter filter) {
		return dao.findPendentesByFormFilter(filter);
	}
}