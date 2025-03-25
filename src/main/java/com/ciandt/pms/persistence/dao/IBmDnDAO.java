package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.BmDn;

import java.util.List;

public interface IBmDnDAO extends IAbstractDao<Long, BmDn> {
    List<BmDn> find(BmDn filter);

    BmDn findById(Long id);

    BmDn findByName(String name);
}
