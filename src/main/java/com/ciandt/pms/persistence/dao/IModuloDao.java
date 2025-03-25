package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Modulo;


/**
 * 
 * A classe IModuloDao proporciona a interface de acesso para o
 * banco de dados referente a entidade Modulo.
 *
 * @since 26/11/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IModuloDao extends IAbstractDao<Long, Modulo> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Modulo> findAll();

}
