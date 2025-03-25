package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IContratoPraticaOrcDespClService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaOrcDespCl;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.persistence.dao.IContratoPraticaOrcDespClDao;

/**
 * 
 * A classe ContratoPraticaOrcDesp proporciona a interface de acesso a camada de
 * serviço referente a entidade ContratoPraticaOrcDesp.
 * 
 * @since 12/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public class ContratoPraticaOrcDespClService implements
		IContratoPraticaOrcDespClService {

	/** Instancia do DAO. */
	@Autowired
	private IContratoPraticaOrcDespClDao dao;

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
	public ContratoPraticaOrcDespCl findById(final Long codigoContratoPratica,
			final Long codigoOrcDespesaCl) {
		return dao.findById(codigoContratoPratica, codigoOrcDespesaCl);
	}

	/**
	 * Cria um {@link ContratoPraticaOrcDespCl}.
	 * 
	 * @param entity
	 *            {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	@Override
	@Transactional
	public void createContratoPraticaOrcDespCl(
			final ContratoPraticaOrcDespCl entity) {
		dao.create(entity);
	}

	/**
	 * Atualiza um {@link ContratoPraticaOrcDespCl}.
	 * 
	 * @param entity
	 *            {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	@Override
	@Transactional
	public void updateContratoPraticaOrcDespCl(
			final ContratoPraticaOrcDespCl entity) {
		dao.update(entity);
	}

	/**
	 * Remove um {@link ContratoPraticaOrcDespCl}.
	 * 
	 * @param entity
	 *            {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	@Override
	@Transactional
	public void removeContratoPraticaOrcDespCl(
			final ContratoPraticaOrcDespCl entity) {
		dao.remove(entity);
	}
	
	/**
	 * Busca por contratoPraticaOrcDespCl.
	 * @param contratoPraticaOrcDespCl contratoPraticaOrcDespCl
	 * @return
	 */
	@Override
	public List<ContratoPraticaOrcDespCl> findByOrcDespesaCl(final OrcDespesaCl orcDespesaCl) {
		return dao.findByOrcDespCl(orcDespesaCl);
	}
}
