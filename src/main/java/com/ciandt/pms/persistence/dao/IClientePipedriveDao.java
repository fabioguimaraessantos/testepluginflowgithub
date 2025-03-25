package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.ClientePipedrive;

import java.util.List;

public interface IClientePipedriveDao extends IAbstractDao<Long, ClientePipedrive> {

    List<ClientePipedrive> findByName(String nomeClientePipedrive);
}
