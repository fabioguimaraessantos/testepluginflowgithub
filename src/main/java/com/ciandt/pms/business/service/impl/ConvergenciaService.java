package com.ciandt.pms.business.service.impl;

import java.util.List;

import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.ContratoPratica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IConvergenciaService;
import com.ciandt.pms.model.Convergencia;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.persistence.dao.IConvergenciaDao;

/**
 * 
 * A classe ConvergenciaService proporciona as funcionalidades de servico para a
 * entidade {@link Convergencia}.
 * 
 * @since Aug 15, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public class ConvergenciaService implements IConvergenciaService {

	/** DAO da entidade {@link Convergencia}. */
	@Autowired
	private IConvergenciaDao dao;

	/** Service da entidade {@link IContratoPraticaService}. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param codigoConvergencia
	 *            ID da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public Convergencia findById(final Long codigoConvergencia) {
		return dao.findById(codigoConvergencia);
	}

	/**
	 * Busca todas as {@link Convergencia}.
	 * 
	 * @return lista do tipo {@link Convergencia}
	 */
	@Override
	public List<Convergencia> findAll() {
		return dao.findAll();
	}

	/**
	 * Cria uma entidade {@link Convergencia}.
	 * 
	 * @param convergencia
	 *            {@link Convergencia}
	 */
	@Transactional
	public void create(final Convergencia convergencia) {
		dao.create(convergencia);
	}

	/**
	 * Atualiza uma entidade {@link Convergencia}.
	 * 
	 * @param convergencia
	 *            {@link Convergencia}
	 */
	@Transactional
	public void update(final Convergencia convergencia) {

		dao.update(convergencia);

        if (convergencia.getContratoPratica() != null && convergencia.getContratoPratica().getCodigoContratoPratica() != null) {
			contratoPraticaService.verifyContratoPraticaComplete(convergencia.getContratoPratica());
		}
	}

	/**
	 * Deleta uma entidade {@link Convergencia}.
	 * 
	 * @param convergencia
	 *            {@link Convergencia}
	 */
	@Transactional
	public void delete(final Convergencia convergencia) {
		dao.remove(convergencia);
		dao.flush();
	}

	/* 
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findInativeProjectByFormFilter(com.ciandt.pms.model.vo.FormFilter)
	 */
	@Override
	public List<Convergencia> findInativeProjectByFormFilter(FormFilter filter) {
		return dao.findInativeProjectByFormFilter(filter);
	}
	
	/* 
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findInativeProjectByFormFilter(com.ciandt.pms.model.vo.FormFilter)
	 */
	@Override
	public List<Convergencia> findActiveProjectByFormFilter(FormFilter filter) {
		return dao.findActiveProjectByFormFilter(filter);
	}
	
	/* 
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findInativeProjectByFormFilter(com.ciandt.pms.model.vo.FormFilter)
	 */
	@Override
	public List<Convergencia> findAllProjectByFormFilter(FormFilter filter) {
		return dao.findAllProjectByFormFilter(filter);
	}

	/* 
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findByContratoPraticaId(java.lang.Long)
	 */
	@Override
	public Convergencia findByContratoPraticaId(Long idContratoPratica) {
		return dao.findByContratoPraticaId(idContratoPratica);
	}

	/* 
	 * @see com.ciandt.pms.business.service.IConvergenciaService#hasProjectToActivate(com.ciandt.pms.model.GrupoCusto)
	 */
	@Override
	public boolean hasProjectToActivate(GrupoCusto grupoCusto) {
		return dao.hasProjectToActivate(grupoCusto);
	}

	/* 
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findByCostGroupId(java.lang.Long)
	 */
	@Override
	public Convergencia findByCostGroupId(Long codigoGrupoCusto) {
		return dao.findByCostGroupId(codigoGrupoCusto);
	}

	/* 
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findByCLCostGroupId(java.lang.Long)
	 */
	@Override
	public List<Convergencia> findByCLCostGroupId(Long codigoGrupoCusto) {
		return dao.findByCLCostGroupId(codigoGrupoCusto);
	}

	/* 
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findConvergenciaByProjeto(java.lang.Long)
	 */
	@Override
	public List<Convergencia> findConvergenciaByProjeto(Long codigoProjetoMega) {
		return dao.findConvergenciaByProjeto(codigoProjetoMega);
	}

}