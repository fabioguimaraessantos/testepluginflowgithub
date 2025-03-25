package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.CidadeBaseFilial;
import com.ciandt.pms.model.CidadeBaseFilialId;

import java.util.List;

public interface ICidadeBaseFilialDao extends IAbstractDao<CidadeBaseFilialId, CidadeBaseFilial> {

    List<CidadeBaseFilial> findAll();

    List<CidadeBaseFilial> findByFilter(final CidadeBaseFilial filter);
}
