package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IVwPmsIntegReceitaNacionalService;
import com.ciandt.pms.model.VwPmsIntegReceitaNacional;
import com.ciandt.pms.persistence.dao.IVwPmsIntegReceitaNacionalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VwPmsIntegReceitaNacionalService implements IVwPmsIntegReceitaNacionalService {

    @Autowired
    private IVwPmsIntegReceitaNacionalDao vwPmsIntegReceitaNacionalDao;

    public List<VwPmsIntegReceitaNacional> findAll() {
        return vwPmsIntegReceitaNacionalDao.findAll();
    }

    @Override
    public VwPmsIntegReceitaNacional findById(Long revenueCode) {
        return vwPmsIntegReceitaNacionalDao.findById(revenueCode);
    }


}
