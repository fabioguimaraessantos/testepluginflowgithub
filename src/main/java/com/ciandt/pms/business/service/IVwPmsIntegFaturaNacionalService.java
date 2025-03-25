package com.ciandt.pms.business.service;

import com.ciandt.pms.model.VwPmsIntegFaturaNacional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IVwPmsIntegFaturaNacionalService {

    List<VwPmsIntegFaturaNacional> findAll();

    List<VwPmsIntegFaturaNacional> findByInvoiceCode(Long revenueCode);
}
