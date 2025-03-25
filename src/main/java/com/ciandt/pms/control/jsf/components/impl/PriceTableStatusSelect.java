package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.model.PriceTableStatus;

import java.util.List;

public class PriceTableStatusSelect extends Select<PriceTableStatus>{
    /**
     *
     * @param priceTableStatusList - List of PriceTableStatus
     */
    public PriceTableStatusSelect(List<PriceTableStatus> priceTableStatusList){
        super(priceTableStatusList);
    }

    @Override
    protected Long objectValue(PriceTableStatus entity) {
        return entity != null ? entity.getCode() : null;
    }

    @Override
    protected String objectKey(PriceTableStatus entity) {
        return entity != null ? entity.getName() : null;
    }

    @Override
    protected PriceTableStatus entityByValue(Long code) {
        return getEntityByCode(code);
    }

    /**
     *
     * @param code - Code to find Industry Type
     * @return IndustryType - Industry Type
     */
    private PriceTableStatus getEntityByCode(Long code) {

        if(code != null){
            for (PriceTableStatus priceTableStatus : entities) {
                if(code.equals(priceTableStatus.getCode()))
                    return priceTableStatus;
            }
        }

        return null;
    }

}
