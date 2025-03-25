package com.ciandt.pms.persistence.dao;


import com.ciandt.pms.model.VwPmsIntegFaturaNacional;

import java.util.List;

public interface IVwPmsIntegFaturaNacionalDao extends IAbstractDao<Long, VwPmsIntegFaturaNacional>{

    List<VwPmsIntegFaturaNacional> findAll();
    List<VwPmsIntegFaturaNacional> findByInvoiceCode(final Long invoiceCode);
}
