package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.model.Empresa;

import java.util.List;

public class CompanySelect extends Select<Empresa>{

    /**
     * @param companyList - List of Companies
     */
    public CompanySelect(List<Empresa> companyList) {
        super(companyList);
    }

    @Override
    protected Long objectValue(Empresa entity) {
        return entity != null ? entity.getCodigoEmpresa() : null;
    }

    @Override
    protected String objectKey(Empresa entity) {
        return entity != null ? entity.getNomeEmpresa() : null;
    }

    @Override
    protected Empresa entityByValue(Long code) {
        return getEntityByCode(code);
    }

    /**
     * @param code - Code to find Company
     * @return Empresa - Company
     */
    private Empresa getEntityByCode(Long code) {

        if (code != null) {
            for (Empresa entity : entities) {
                if (code.equals(entity.getCodigoEmpresa()))
                    return entity;
            }
        }

        return null;
    }
}
