package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.MsaCommercialPartner;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMsaCommercialPartnerDao extends IAbstractDao<Long, MsaCommercialPartner> {

    List<MsaCommercialPartner> findByMsaCode(Long msaCode);
}
