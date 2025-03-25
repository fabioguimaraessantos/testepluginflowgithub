package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IAreaOrcamentariaService;
import com.ciandt.pms.business.service.IModeloAreaOrcamentariaService;
import com.ciandt.pms.business.service.IModeloService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.Modelo;
import com.ciandt.pms.model.ModeloAreaOrcamentaria;
import com.ciandt.pms.persistence.dao.IAreaOrcamentariaDao;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A classe ControleReajusteService proporciona as funcionalidades de serviço
 * referente a entidade ControleReajuste.
 * 
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class AreaOrcamentariaService extends AbstractGestaoReajusteService
		implements IAreaOrcamentariaService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IAreaOrcamentariaDao dao;

	/** Instancia do DAO da entidade. */
	@Autowired
	private IPessoaService pessoaService;

	@Autowired
	private IModeloService modeloService;

	@Autowired
	private IModeloAreaOrcamentariaService modeloAreaOrcamentariaService;

	private static final Logger logger = LoggerFactory.getLogger(AreaOrcamentariaService.class);


	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public AreaOrcamentaria findById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<AreaOrcamentaria> findAll() {
		return dao.findAll();
	}

	/**
	 * Insere uma entidade.
	 *
	 * @param entity
	 */

	@Transactional
	public Boolean create(final AreaOrcamentaria entity, Modelo selectedModelo) {
		try {
			dao.create(entity);
			dao.flush();

			createModelToBudgetArea(entity, selectedModelo);

			return true;
		} catch (Exception ex) {
			logger.error("ERROR: {}", ex.getMessage() != null ? ex.getMessage() : ex);
			return false;
		}
	}

	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 */
	public void update(final AreaOrcamentaria entity) {
		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 */
	public void delete(final AreaOrcamentaria entity) {
		dao.remove(entity);
	}

	@Override
	public List<AreaOrcamentaria> findAllActiveAreaOrcamentariaPai() {
		return dao.findAllActiveAreaOrcamentariaPai();
	}

	@Override
	public List<AreaOrcamentaria> findByAreaOrcamentariaPai(Long codigoAreaOrcamentariaPai) {
		return dao.findByAreaOrcamentariaPai(codigoAreaOrcamentariaPai);
	}

	@Override
	public List<AreaOrcamentaria> findAllAreaOrcamentariaFilho() {
		return dao.findAllAreaOrcamentariaFilho();
	}

	@Override
	public List<AreaOrcamentaria> findByFilter(final AreaOrcamentaria filter) {
		List<AreaOrcamentaria> result = dao.findByFilter(filter);

		for (AreaOrcamentaria ao : result) {
			Hibernate.initialize(ao.getAreaOrcamentariaPai());
		}

		return result;
	}


	private void createModelToBudgetArea (AreaOrcamentaria entity, Modelo selectedModelo) {

		ModeloAreaOrcamentaria modeloAreaOrcamentaria = new ModeloAreaOrcamentaria();
		modeloAreaOrcamentaria.setAreaOrcamentaria(entity);
		modeloAreaOrcamentaria.setDataInicio(getStartOfTheYear());

		if (selectedModelo != null) {
			modeloAreaOrcamentaria.setModelo(selectedModelo);
		} else {
			modeloAreaOrcamentaria.setModelo(modeloService.findDefaultModel());
		}

		modeloAreaOrcamentariaService.create(modeloAreaOrcamentaria);
	}

	private Date getStartOfTheYear() {
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		startDate.set(Calendar.MILLISECOND, 0);
		return startDate.getTime();
	}
}