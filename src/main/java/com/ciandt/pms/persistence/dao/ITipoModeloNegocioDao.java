package com.ciandt.pms.persistence.dao;


import com.ciandt.pms.model.TipoModeloNegocio;

import java.util.List;

/**
 * Created by josef on 02/02/2017.
 */
public interface ITipoModeloNegocioDao extends IAbstractDao<Long, TipoModeloNegocio> {
    /**
     * Find All TipoModeloNegocio
     *
     * @return
     */
    List<TipoModeloNegocio> findAllActive();
}
