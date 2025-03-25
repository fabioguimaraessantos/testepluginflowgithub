package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaCommercialPartnerService;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaCommercialPartner;
import com.ciandt.pms.persistence.dao.IMsaCommercialPartnerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MsaCommercialPartnerService implements IMsaCommercialPartnerService {

    @Autowired
    private IMsaCommercialPartnerDao dao;

    @Override
    public List<MsaCommercialPartner> findByMsaCode(Long msaCode) {
        return dao.findByMsaCode(msaCode);
    }

    @Override
    @Transactional
    public List<MsaCommercialPartner> update(Msa msa, List<String> logins) {
        List<MsaCommercialPartner> entities = this.findByMsaCode(msa.getCodigoMsa());
        this.removeAll(entities);

        List<MsaCommercialPartner> newLogins = new ArrayList<MsaCommercialPartner>();
        for (String login : logins) {
            MsaCommercialPartner cp = new MsaCommercialPartner();
            cp.setLogin(login);
            cp.setMsa(msa);

            newLogins.add(cp);
        }

        return this.createAll(newLogins);
    }

    @Override
    @Transactional
    public void removeAll(List<MsaCommercialPartner> entities) {
        for (MsaCommercialPartner entity : entities) {
            dao.remove(entity);
        }
    }

    @Override
    @Transactional
    public List<MsaCommercialPartner> createAll(List<MsaCommercialPartner> entities) {
        List<MsaCommercialPartner> persisted = new ArrayList<MsaCommercialPartner>();

        for (MsaCommercialPartner entity : entities) {
            persisted.add(dao.merge(entity));
        }

        return persisted;
    }
}
