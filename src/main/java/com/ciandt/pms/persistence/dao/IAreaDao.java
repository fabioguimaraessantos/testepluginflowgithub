package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Area;


/**
 * 
 * A classe IAreaDao proporciona a interface de acesso 
 * a camada de persistencia referente a entidade Area.
 *
 * @since 13/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IAreaDao extends IAbstractDao<Long, Area> {

    /**
     * Busca todos os CentroLucro.
     * 
     * @return lista de CentroLucro com todos os centro de lucros
     */
    List<Area> findAllActive();
}
