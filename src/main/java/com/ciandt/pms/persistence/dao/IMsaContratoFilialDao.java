package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MsaContratoFilial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMsaContratoFilialDao extends IAbstractDao<Long, MsaContratoFilial> {

    List<MsaContratoFilial> findByMsaContratoCode(Long msaContratoCode);

    void merge(List<MsaContratoFilial> msaContratoFiliais);
}
