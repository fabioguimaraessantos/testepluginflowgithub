package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.persistence.dao.IVwMegaCCustoDao;

/**
 * A classe VwMegaCCustoDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade {@link VwMegaCCusto}.
 * 
 * @since Out 11, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
@Repository
public class VwMegaCCustoDao extends AbstractDao<Long, VwMegaCCusto> implements
		IVwMegaCCustoDao {

	/**
	 * Construtor.
	 * 
	 * @param factory
	 *            facotry
	 */
	@Autowired
	public VwMegaCCustoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, VwMegaCCusto.class);
	}

	/**
	 * Busca todos os {@link VwMegaCCusto}.
	 * 
	 * @return retorna todos os {@link VwMegaCCusto}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<VwMegaCCusto> findAll() {
		return getJpaTemplate().findByNamedQuery(VwMegaCCusto.FIND_ALL);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VwMegaCCusto> findAllForCombobox() {
		return getJpaTemplate().findByNamedQuery(VwMegaCCusto.FIND_ALL_FOR_COMBOBOX);
	}

	/**
	 * Busca todos os{@link VwMegaCCusto} pendentes.
	 * 
	 * @return uma lista de {@link VwMegaCCusto}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<VwMegaCCusto> findAllPendentes() {
		return getJpaTemplate().findByNamedQuery(VwMegaCCusto.FIND_ALL_PENDING);
	}

	/**
	 * Busca todos os{@link VwMegaCCusto} por um {@link FormFilter}.
	 * 
	 * @param filter
	 *            {@link FormFilter}
	 * @return uma lista de {@link VwMegaCCusto}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<VwMegaCCusto> findPendentesByFormFilter(FormFilter filter) {
		return getJpaTemplate().findByNamedQueryAndNamedParams(
				VwMegaCCusto.FIND_PENDING_BY_FORM_FILTER, filter.toMap());
	}

}