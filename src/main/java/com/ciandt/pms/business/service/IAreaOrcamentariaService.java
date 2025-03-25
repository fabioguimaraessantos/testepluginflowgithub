package com.ciandt.pms.business.service;

import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.Modelo;
import com.ciandt.pms.model.ModeloAreaOrcamentaria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IAreaOrcamentariaService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    AreaOrcamentaria findById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<AreaOrcamentaria> findAll();

	/**
	 * Insere uma entidade.
	 *
	 * @param entity
	 */
    @Transactional
	Boolean create(final AreaOrcamentaria entity, Modelo selectedModelo);
	
	/**
	 * Atualiza uma entidade.
	 *
	 * @param entity
	 */
	@Transactional
	void update(final AreaOrcamentaria entity);

	/**
	 * Deleta uma entidade.
	 *
	 * @param entity
	 */
	@Transactional
	void delete(final AreaOrcamentaria entity);

	List<AreaOrcamentaria> findAllActiveAreaOrcamentariaPai();

	List<AreaOrcamentaria> findByAreaOrcamentariaPai(
			Long codigoAreaOrcamentariaPai);

	List<AreaOrcamentaria> findAllAreaOrcamentariaFilho();

    List<AreaOrcamentaria> findByFilter(final AreaOrcamentaria filter);

}