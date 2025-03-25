package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.persistence.dao.IParametroDao;

/**
 * A classe ParametroService proporciona as funcionalidades da camada
 * de negócio referente a entidade {@link Parametro}.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 03/02/2014
 */
@Service
public class ParametroService implements IParametroService {

	/** Instancia do Dao da entidade ApropriacaoPlantao. */
	@Autowired
	private IParametroDao parametroDao;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Override
	public void createParametro(Parametro entity) {
		parametroDao.create(entity);
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	@Override
	public void updateParametro(Parametro entity) {
		parametroDao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	@Override
	public void removeParametro(Parametro entity) {
		parametroDao.remove(entity);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public Parametro findParametroById(final Long id) {
		return parametroDao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<Parametro> findParametroAll() {
		return parametroDao.findAll();
	}

	/**
	 * Busca {@link Parametro} pelo atributo {@code nomeParametro}.
	 *
	 * @param nomeParametro
	 * @return {@link Parametro}
	 */
	@Override
	public Parametro findParametroByNomeParametro(final String nomeParametro) {
		return parametroDao.findByNomeParametro(nomeParametro);
	}
}