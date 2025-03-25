package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaTipoContratoService;
import com.ciandt.pms.model.MsaTipoContrato;
import com.ciandt.pms.persistence.dao.IMsaTipoContratoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsaTipoContratoService implements IMsaTipoContratoService {

    @Autowired
    private IMsaTipoContratoDao msaTipoContratoDao;


    @Override
    public List<MsaTipoContrato> findAllActive() {
        return msaTipoContratoDao.findAllActive();
    }

    @Override
    public MsaTipoContrato findById(Long typeCode) {
        return msaTipoContratoDao.findById(typeCode);
    }

    @Override
    public MsaTipoContrato findActiveByAcronimo(String acronimo) {
        return msaTipoContratoDao.findActiveByAcronimo(acronimo);
    }
}
