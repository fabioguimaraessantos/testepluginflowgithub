package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.message.dto.InvoiceMegaDTO;
import java.util.List;

public class InvoiceMegaSelect extends Select<InvoiceMegaDTO> {

    public InvoiceMegaSelect(List<InvoiceMegaDTO> invoiceMegaDTOS){
        super(invoiceMegaDTOS);
    }

    @Override
    protected Long objectValue(InvoiceMegaDTO entity) {
        return entity != null ? entity.getCode() : null;
    }

    @Override
    protected String objectKey(InvoiceMegaDTO entity) {
        return entity != null ? entity.getInvoiceNumber() + " (" + entity.getInvoiceDate() + ") - " + entity.getCode()  : null;
    }

    @Override
    protected InvoiceMegaDTO entityByValue(Long code) {
        return getEntityByCode(code);
    }

    private InvoiceMegaDTO getEntityByCode(Long code) {

        if(code != null){
            for (InvoiceMegaDTO invoiceDTO : entities) {
                if(code.equals(invoiceDTO.getCode()))
                    return invoiceDTO;
            }
        }

        return null;
    }
}