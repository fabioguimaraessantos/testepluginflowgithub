package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.message.dto.CostCenterDTO;

import java.util.List;

public class CentroCustoSelect extends Select<CostCenterDTO> {

    /**
     *
     * @param costCenterDTO - List of CostCenterDTO
     */
    public CentroCustoSelect(List<CostCenterDTO> costCenterDTO){
        super(costCenterDTO);
    }

    @Override
    protected Long objectValue(CostCenterDTO entity) {
        return entity != null ? Long.valueOf(entity.getShortCode()) : null;
    }

    @Override
    protected String objectKey(CostCenterDTO entity) {
        return entity != null ? entity.getDescription() : null;
    }

    @Override
    protected CostCenterDTO entityByValue(Long shortCode) {
        return getEntityByShortCode(shortCode);
    }

    /**
     *
     * @param shortCode - Code to find CostCenter
     * @return CostCenterDTO - Cost Center
     */
    private CostCenterDTO getEntityByShortCode(Long shortCode) {

        if(shortCode != null){
            for (CostCenterDTO costCenterDTO : entities) {
                if(shortCode.equals(Long.parseLong(costCenterDTO.getShortCode())))
                    return costCenterDTO;
            }
        }

        return null;
    }
}
