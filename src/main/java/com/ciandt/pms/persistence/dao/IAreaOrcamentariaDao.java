package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.AreaOrcamentaria;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/08/2015
 */
@Repository
public interface IAreaOrcamentariaDao extends IAbstractDao<Long, AreaOrcamentaria> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<AreaOrcamentaria> findAll();

	List<AreaOrcamentaria> findAllActiveAreaOrcamentariaPai();

	List<AreaOrcamentaria> findByAreaOrcamentariaPai(
			Long codigoAreaOrcamentariaPai);

	List<AreaOrcamentaria> findAllAreaOrcamentariaFilho();

    List<AreaOrcamentaria> findByFilter(AreaOrcamentaria filter);

}