package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaStatusContratoService;
import com.ciandt.pms.model.MsaStatusContrato;
import com.ciandt.pms.persistence.dao.IMsaStatusContratoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsaStatusContratoService implements IMsaStatusContratoService {

    @Autowired
    private IMsaStatusContratoDao msaStatusContratoDao;


    @Override
    public List<MsaStatusContrato> findAllActive() {
        return msaStatusContratoDao.findAllActive();
    }

    @Override
    public MsaStatusContrato findByName(String name) {
        return msaStatusContratoDao.findByName(name);
    }


    @Override
    public MsaStatusContrato findById(Long statusCode) {
        return msaStatusContratoDao.findById(statusCode);
    }
}
