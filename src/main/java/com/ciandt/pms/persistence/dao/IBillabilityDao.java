package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.Billability;

import java.util.List;

public interface IBillabilityDao extends IAbstractDao<Long, Billability> {

    /**
     * Busca todos os Billabilities ativos.
     *
     * @return lista de Billability com todos os Billabilities ativos
     */
    List<Billability> findAllActive();

    Billability findByName(String name);
}
