package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.TipoContrato;


/**
 * 
 * A classe ITipoContratoDao proporciona a interface de acesso
 * a camada de persistencia referente a entidade TipoContrato.
 *
 * @since 26/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface ITipoContratoDao extends IAbstractDao<Long, TipoContrato> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoContrato> findAll();
    
    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    List<TipoContrato> findAllActive();
    
}