package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CostCenterHierarchy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface ICostCenterHierarchyService {

    CostCenterHierarchy findByCode(final Long code) throws BusinessException;

    List<CostCenterHierarchy> findByFilter(final CostCenterHierarchy filter) throws BusinessException;

    List<CostCenterHierarchy> findAllActive();

    List<CostCenterHierarchy> findAllActiveIncludingCurrentIfInactive(final CostCenterHierarchy currentCostCenterHierarchy);

    @Transactional
    void remove(final Long code) throws BusinessException;

    @Transactional
    void create(final CostCenterHierarchy costCenterHierarchy) throws BusinessException;

    @Transactional
    void update(final CostCenterHierarchy costCenterHierarchy) throws BusinessException;
}
