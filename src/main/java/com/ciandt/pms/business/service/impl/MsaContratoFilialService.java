package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaContratoFilialService;
import com.ciandt.pms.model.MsaContratoFilial;
import com.ciandt.pms.persistence.dao.IMsaContratoFilialDao;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MsaContratoFilialService implements IMsaContratoFilialService {

    @Autowired
    private IMsaContratoFilialDao msaContratoFilialDao;

    @Override
    public List<MsaContratoFilial> findByMsaContratoCode(Long msaContratoCode) {
        List<MsaContratoFilial> result = msaContratoFilialDao.findByMsaContratoCode(msaContratoCode);

        for (MsaContratoFilial filial : result) {
            Hibernate.initialize(filial.getFilial());
        }

        return result;
    }

    @Override
    @Transactional
    public void save(List<MsaContratoFilial> msaContratoFiliais) {
        msaContratoFilialDao.merge(msaContratoFiliais);
    }

    @Override
    @Transactional
    public void create(MsaContratoFilial msaContratoFiliai) {
        msaContratoFilialDao.create(msaContratoFiliai);
    }

    @Override
    @Transactional
    public void removeByMsaContratoCode(Long msaContratoCode) {
        List<MsaContratoFilial> filiais = msaContratoFilialDao.findByMsaContratoCode(msaContratoCode);

        for (MsaContratoFilial filial : filiais) {
            msaContratoFilialDao.remove(filial);
        }
    }
}
