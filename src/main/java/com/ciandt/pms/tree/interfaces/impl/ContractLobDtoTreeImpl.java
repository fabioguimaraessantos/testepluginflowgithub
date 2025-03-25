package com.ciandt.pms.tree.interfaces.impl;

import com.ciandt.pms.message.dto.ContractLobDTO;

import java.util.List;

public class ContractLobDtoTreeImpl extends TreeImpl<ContractLobDTO>{

    public ContractLobDtoTreeImpl(List<ContractLobDTO> list) {
        super(list);
    }

    @Override
    public String element(ContractLobDTO entity) {
        return entity.getShortCode();
    }

    @Override
    public String parent(ContractLobDTO entity) {
        return entity.getParentShortCode();
    }

    @Override
    public ContractLobDTO root() {
        ContractLobDTO clob = new ContractLobDTO();
        clob.setShortCode("0");
        clob.setParentShortCode("0");
        clob.setDescription("Project");
        return clob;
    }
}
