package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.model.IndustryType;

import java.util.List;

public class IndustrySelect extends Select<IndustryType> {

    /**
     *
     * @param industries - List of Industries Type
     */
    public IndustrySelect(List<IndustryType> industries){
        super(industries);
    }

    @Override
    protected Long objectValue(IndustryType entity) {
        return entity != null ? entity.getCode() : null;
    }

    @Override
    protected String objectKey(IndustryType entity) {
        return entity != null ? entity.getName() : null;
    }

    @Override
    protected IndustryType entityByValue(Long code) {
        return getEntityByCode(code);
    }

    /**
     *
     * @param code - Code to find Industry Type
     * @return IndustryType - Industry Type
     */
    private IndustryType getEntityByCode(Long code) {

        if(code != null){
            for (IndustryType industry : entities) {
                if(code.equals(industry.getCode()))
                    return industry;
            }
        }

        return null;
    }
}