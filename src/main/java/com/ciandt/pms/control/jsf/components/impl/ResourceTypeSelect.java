package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.model.TiRecurso;

import java.util.List;

public class ResourceTypeSelect extends Select<TiRecurso>{

    /**
     * @param companyList - List of Companies
     */
    public ResourceTypeSelect(List<TiRecurso> companyList) {
        super(companyList);
    }

    @Override
    protected Long objectValue(TiRecurso entity) {
        return entity != null ? entity.getCodigoTiRecurso() : null;
    }

    @Override
    protected String objectKey(TiRecurso entity) {
        return entity != null ? entity.getNomeTiRecurso() : null;
    }

    @Override
    protected TiRecurso entityByValue(Long code) {
        return getEntityByCode(code);
    }

    /**
     * @param code - Code to find TiRecurso
     * @return TiRecurso
     */
    private TiRecurso getEntityByCode(Long code) {

        if (code != null) {
            for (TiRecurso entity : entities) {
                if (code.equals(entity.getCodigoTiRecurso()))
                    return entity;
            }
        }

        return null;
    }
}
