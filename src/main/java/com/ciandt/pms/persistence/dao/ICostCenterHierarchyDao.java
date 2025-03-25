package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.CostCenterHierarchy;

import java.util.List;

public interface ICostCenterHierarchyDao extends IAbstractDao<Long, CostCenterHierarchy> {

    List<CostCenterHierarchy> findByFilter(final String name, final Boolean inActive);

    List<CostCenterHierarchy> findByNameOrOracleCode(final String name, final String oracleCode);
}
