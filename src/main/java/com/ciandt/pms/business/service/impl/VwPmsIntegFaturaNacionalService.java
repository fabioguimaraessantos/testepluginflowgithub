package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IVwPmsIntegFaturaNacionalService;
import com.ciandt.pms.model.VwPmsIntegFaturaNacional;
import com.ciandt.pms.persistence.dao.IVwPmsIntegFaturaNacionalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VwPmsIntegFaturaNacionalService implements IVwPmsIntegFaturaNacionalService {

    @Autowired
    private IVwPmsIntegFaturaNacionalDao vwPmsIntegFaturaNacionalDao;

    public List<VwPmsIntegFaturaNacional> findAll() {
        return vwPmsIntegFaturaNacionalDao.findAll();
    }

    @Override
    public List<VwPmsIntegFaturaNacional> findByInvoiceCode(Long revenueCode) {
        return vwPmsIntegFaturaNacionalDao.findByInvoiceCode(revenueCode);
    }


}
