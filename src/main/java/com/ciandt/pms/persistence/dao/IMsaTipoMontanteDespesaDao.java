package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MsaTipoMontanteDespesa;

import java.util.List;

public interface IMsaTipoMontanteDespesaDao extends IAbstractDao<Long, MsaTipoMontanteDespesa> {

    List<MsaTipoMontanteDespesa> findAllActive();

    MsaTipoMontanteDespesa findByName(String Name);

}
