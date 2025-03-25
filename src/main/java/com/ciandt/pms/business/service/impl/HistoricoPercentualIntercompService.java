package com.ciandt.pms.business.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IHistoricoPercentualIntercompService;
import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.model.HistoricoPercentualIntercomp;
import com.ciandt.pms.persistence.dao.IHistoricoPercentualIntercompDao;

/**
 * A classe ControleReajusteService proporciona as funcionalidades de serviço
 * referente a entidade ControleReajuste.
 * 
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class HistoricoPercentualIntercompService implements IHistoricoPercentualIntercompService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IHistoricoPercentualIntercompDao dao;

	/** Instancia do Servico da entidade {@link ParametroService}. */
	@Autowired
	private IParametroService parametroService;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public HistoricoPercentualIntercomp findById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 *
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<HistoricoPercentualIntercomp> findAll() {
		return dao.findAll();
	}

	/**
	 * Retorna todas as entidades.
	 *
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<HistoricoPercentualIntercomp> findByDealFiscal(final Long dealFiscalCode) {
		return dao.findByDealFiscal(dealFiscalCode);
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @param hpi
	 */
	@Override
	@Transactional
	public void create(final HistoricoPercentualIntercomp hpi) {
		dao.create(hpi);
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @param hpis
	 */
	@Override
	@Transactional
	public void create(final Set<HistoricoPercentualIntercomp> hpis) {
		for (HistoricoPercentualIntercomp hpi : hpis) {
			if (hpi.getCodigoHistoricoPercentualIntercomp() == null) {
				
				this.create(hpi);
			}
		}
	}

	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void update(final HistoricoPercentualIntercomp entity) {
		dao.update(entity);
	}
	
	/**
	 * Atualiza uma entidade.
	 * 
	 * @param historicoPercentualIntercomps
	 */
	@Override
	@Transactional
	public void update(final Set<HistoricoPercentualIntercomp> historicoPercentualIntercomps) {

		for (HistoricoPercentualIntercomp hpi : historicoPercentualIntercomps) {
			if (hpi.getCodigoHistoricoPercentualIntercomp() == null) {				
				this.create(hpi);
			} else {
				this.update(hpi);
			}
		}
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void delete(final HistoricoPercentualIntercomp entity) {
		dao.remove(entity);
	}
}