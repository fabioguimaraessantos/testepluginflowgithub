package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MsaTipoContrato;

import java.util.List;

public interface IMsaTipoContratoDao extends IAbstractDao<Long, MsaTipoContrato> {

    List<MsaTipoContrato> findAllActive();

    MsaTipoContrato findActiveByAcronimo(String acronimo);
}
