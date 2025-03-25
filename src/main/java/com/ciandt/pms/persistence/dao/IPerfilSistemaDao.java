package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.PerfilSistema;


/**
 * 
 * A classe IPerfilSistemaDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade PerfilSistema.
 * 
 * @since 03/01/2011
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IPerfilSistemaDao extends IAbstractDao<Long, PerfilSistema> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PerfilSistema> findAll();
}
