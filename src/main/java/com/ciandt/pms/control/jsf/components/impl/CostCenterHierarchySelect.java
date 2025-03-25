package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.model.CostCenterHierarchy;

import java.util.List;

public class CostCenterHierarchySelect extends Select<CostCenterHierarchy> {

    public CostCenterHierarchySelect(List<CostCenterHierarchy> costCenterHierarchyList) {
        super(costCenterHierarchyList);
    }

    @Override
    protected Long objectValue(CostCenterHierarchy entity) {
        return entity != null ? entity.getCode() : null;
    }

    @Override
    protected String objectKey(CostCenterHierarchy entity) {
        return entity != null ? entity.getName() : null;
    }

    @Override
    protected CostCenterHierarchy entityByValue(Long code) {
        return getEntityByCode(code);
    }

    private CostCenterHierarchy getEntityByCode(Long code) {

        if (code != null) {
            for (CostCenterHierarchy costCenterHierarchy : entities) {
                if (code.equals(costCenterHierarchy.getCode()))
                    return costCenterHierarchy;
            }
        }

        return null;
    }
}
