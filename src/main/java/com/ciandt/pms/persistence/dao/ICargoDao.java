package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.CargoPms;


/**
 * 
 * A classe ICargoDao proporciona as funcionalidades de interface para CargoPmsDao.
 *
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public interface ICargoDao extends IAbstractDao<Long, CargoPms> {
    
    /**
     * Busca todas as entidades.
     * @return lista.
     */
    List<CargoPms> findAll();
    


}
