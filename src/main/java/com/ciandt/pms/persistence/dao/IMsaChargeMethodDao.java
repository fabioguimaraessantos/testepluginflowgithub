package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MsaChargeMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMsaChargeMethodDao extends IAbstractDao<Long, MsaChargeMethod> {

    List<MsaChargeMethod> findAll();

}
