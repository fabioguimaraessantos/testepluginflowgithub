package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IMsaChargeMethodService;
import com.ciandt.pms.model.MsaChargeMethod;
import com.ciandt.pms.persistence.dao.IMsaChargeMethodDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsaChargeMethodService implements IMsaChargeMethodService {

    @Autowired
    private IMsaChargeMethodDao repository;

    @Override
    public List<MsaChargeMethod> findAll() {
        return repository.findAll();
    }
}
