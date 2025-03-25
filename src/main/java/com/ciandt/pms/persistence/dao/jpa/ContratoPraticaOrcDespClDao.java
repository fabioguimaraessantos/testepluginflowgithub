package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaOrcDespCl;
import com.ciandt.pms.model.ContratoPraticaOrcDespClId;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.persistence.dao.IContratoPraticaOrcDespClDao;

/**
 * A classe ContratoPraticaOrcDespClDao proporciona as funcionalidades de acesso
 * ao banco de dados referente a entidade ContratoPraticaOrcDespCl.
 * 
 * @since 11/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Repository
public class ContratoPraticaOrcDespClDao extends
		AbstractDao<ContratoPraticaOrcDespClId, ContratoPraticaOrcDespCl>
		implements IContratoPraticaOrcDespClDao {

	/**
	 * Construtor.
	 * 
	 * @param factory
	 *            factory
	 */
	@Autowired
	public ContratoPraticaOrcDespClDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ContratoPraticaOrcDespCl.class);
	}

	/**
	 * Busca uma entidade ContratoPraticaOrcDespCl.
	 * 
	 * @param codigoContratoPratica
	 *            - o codigo do {@link ContratoPratica}
	 * @param codigoOrcDespesaCl
	 *            - o codigo do {@link OrcDespesaCl}
	 * 
	 * @return {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	@Override
	public ContratoPraticaOrcDespCl findById(Long codigoContratoPratica,
			Long codigoOrcDespesaCl) {
		return null;
	}
	
	/**
	 * Busca todas as entidades do banco.
	 * 
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContratoPraticaOrcDespCl> findAll() {
		List<ContratoPraticaOrcDespCl> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPraticaOrcDespCl.FIND_ALL);
		return listResult;
	}
	
	/**
	 * Busca pelo OrcaDespesaCl.
	 * 
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ContratoPraticaOrcDespCl> findByOrcDespCl(final OrcDespesaCl orcDespesaCl) {
		List<ContratoPraticaOrcDespCl> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPraticaOrcDespCl.FIND_BY_ORC_DESP_CL, new Object[] { orcDespesaCl.getCodigoOrcaDespCl() });
		return listResult;
	}
}
