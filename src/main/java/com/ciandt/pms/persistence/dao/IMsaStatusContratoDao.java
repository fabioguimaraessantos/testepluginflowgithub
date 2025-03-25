package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MsaStatusContrato;

import java.util.List;

public interface IMsaStatusContratoDao extends IAbstractDao<Long, MsaStatusContrato> {

    List<MsaStatusContrato> findAllActive();

    MsaStatusContrato findByName(String name);
}
