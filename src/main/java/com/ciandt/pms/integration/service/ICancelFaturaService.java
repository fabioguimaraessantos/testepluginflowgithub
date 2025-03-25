package com.ciandt.pms.integration.service;

import com.ciandt.pms.exception.BusinessException;

public interface ICancelFaturaService {

    /**
     * Invoice cancelation process.
     * @param invoiceCode external ERP invoice code.
     * @throws BusinessException if invalid invoice data or error.
     */
    void cancelExternalERP(String invoiceCode) throws BusinessException;
}
