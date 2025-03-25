package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import java.util.List;

public class InvoiceProjectMegaSelect extends Select<InvoiceProjectMegaDTO> {

    public InvoiceProjectMegaSelect(List<InvoiceProjectMegaDTO> invoiceProjectMegaDTOS){
        super(invoiceProjectMegaDTOS);
    }

    @Override
    protected Long objectValue(InvoiceProjectMegaDTO entity) {
        return entity != null ? entity.getProjectCode() : null;
    }

    @Override
    protected String objectKey(InvoiceProjectMegaDTO entity) {
        return entity != null ? entity.getProjectDescription() : null;
    }

    @Override
    protected InvoiceProjectMegaDTO entityByValue(Long code) {
        return getEntityByCode(code);
    }

    private InvoiceProjectMegaDTO getEntityByCode(Long code) {

        if(code != null){
            for (InvoiceProjectMegaDTO invoiceDTO : entities) {
                if(code.equals(invoiceDTO.getProjectCode()))
                    return invoiceDTO;
            }
        }

        return null;
    }
}