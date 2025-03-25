package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.model.MsaContratoCnpj;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMsaContratoCnpjDao extends
        IAbstractDao<Long, MsaContratoCnpj>{
    List<MsaContratoCnpj> findByMsaContrato(MsaContrato msaContrato);
}
