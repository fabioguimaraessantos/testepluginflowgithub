package com.ciandt.pms.tree.interfaces.impl;

import com.ciandt.pms.message.dto.CostCenterDTO;

import java.util.List;

public class CostCenterDtoTreeImpl extends TreeImpl<CostCenterDTO>{

    public CostCenterDtoTreeImpl(List<CostCenterDTO> list) {
        super(list);
    }

    @Override
    public String element(CostCenterDTO entity) {
        return entity.getShortCode();
    }

    @Override
    public String parent(CostCenterDTO entity) {
        return entity.getParentShortCode();
    }

    @Override
    public CostCenterDTO root() {
        CostCenterDTO cc = new CostCenterDTO();
        cc.setShortCode("0");
        cc.setParentShortCode("0");
        cc.setDescription("Cost Center");
        return cc;
    }
}
