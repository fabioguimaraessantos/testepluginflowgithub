package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaCommercialPartner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IMsaCommercialPartnerService {

    List<MsaCommercialPartner> findByMsaCode(Long msaCode);

    @Transactional
    List<MsaCommercialPartner> update(Msa msa, List<String> logins);

    @Transactional
    void removeAll(List<MsaCommercialPartner> entities);

    @Transactional
    List<MsaCommercialPartner> createAll(List<MsaCommercialPartner> entities);
}
