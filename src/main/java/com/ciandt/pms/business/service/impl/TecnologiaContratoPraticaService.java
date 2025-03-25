package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.ITecnologiaContratoPraticaService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.TecnologiaContratoPratica;
import com.ciandt.pms.persistence.dao.ITecnologiaContratoPraticaDao;

/**
 * A classe TecnologiaContratoPraticaService proporciona as funcionalidades de
 * servicos referentes a entidade {@link TecnologiaContratoPratica}.
 * 
 * @since Sep 23, 2014
 * @author <a href="mailto:eduardot@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public class TecnologiaContratoPraticaService implements
		ITecnologiaContratoPraticaService {

	/** Instancia do DAO da entidade TecnologiaContratoPraticaDao. */
	@Autowired
	private ITecnologiaContratoPraticaDao dao;

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	@Override
	public TecnologiaContratoPratica findTecnologiaContratoPraticaById(
			final Long entityId) {
		return dao.findById(entityId);
	}

	/**
	 * Executa um update na entidade passada por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 * @return true se update com sucesso, caso contrario retorna false
	 */
	@Override
	@Transactional
	public boolean updateTecnologiaContratoPratica(
			final TecnologiaContratoPratica entity) {
		dao.update(entity);

		return Boolean.TRUE;
	}

	/**
	 * Create a TecnologiaContratoPratic
	 * 
	 * @param tecnologiaContratoPratic
	 */
	@Override
	@Transactional
	public void createTecnologiaContratoPratica(
			final TecnologiaContratoPratica tecnologiaContratoPratic) {
		dao.create(tecnologiaContratoPratic);
	}

	/**
	 * Return all TecnologiaContratoPratica associated with the given
	 * ContratoPratica.
	 * 
	 * @return TecnologiaContratoPratica List.
	 */
	@Override
	public List<TecnologiaContratoPratica> findAllByClob(
			final ContratoPratica contratoPratica) {
		return dao.findAllByClob(contratoPratica);
	}

	/**
	 * Remove uma determinada tecnologia.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica} base
	 */
	@Override
	@Transactional
	public void remove(final TecnologiaContratoPratica tecnologia) {
		dao.remove(tecnologia);
		dao.flush();
	}

	/**
	 * Remove todos as tecnologias vinculadas ao CLOB informado.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica} base
	 */
	@Override
	@Transactional
	public void removeAllByClob(final ContratoPratica contratoPratica) {
		List<TecnologiaContratoPratica> tecnologias = findAllByClob(contratoPratica);
		for (TecnologiaContratoPratica tecnologia : tecnologias) {
			remove(tecnologia);
		}
	}
}