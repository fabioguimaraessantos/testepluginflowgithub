package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Potencial;


/**
 * A classe IPotencialDao proporciona as funcionalidades de ... para ...
 * 
 * @since 05/04/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
public interface IPotencialDao extends IAbstractDao<Long, Potencial> {

    /**
     * Busca todas as entidades ativas.
     * 
     * @return retorna uma lista de Potencial.
     */
    List<Potencial> findAllActive();

}