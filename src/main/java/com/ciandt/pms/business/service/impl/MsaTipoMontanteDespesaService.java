package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaTipoMontanteDespesaService;
import com.ciandt.pms.model.MsaTipoMontanteDespesa;
import com.ciandt.pms.persistence.dao.IMsaTipoMontanteDespesaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsaTipoMontanteDespesaService implements IMsaTipoMontanteDespesaService {

    @Autowired
    private IMsaTipoMontanteDespesaDao tipoMontanteDespesaDao;


    @Override
    public List<MsaTipoMontanteDespesa> findAllActive() {
        return tipoMontanteDespesaDao.findAllActive();
    }

    @Override
    public MsaTipoMontanteDespesa findById(Long amountExpenseTypeCode) {
        return tipoMontanteDespesaDao.findById(amountExpenseTypeCode);
    }

    @Override
    public MsaTipoMontanteDespesa findByName(String name) {
        return tipoMontanteDespesaDao.findByName(name);
    }
}
