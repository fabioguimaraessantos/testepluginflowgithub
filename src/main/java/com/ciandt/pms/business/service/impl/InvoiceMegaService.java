package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IInvoiceMegaService;
import com.ciandt.pms.message.InvoiceMessage;
import com.ciandt.pms.message.dto.InvoiceMegaDTO;
import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceMegaService implements IInvoiceMegaService {

    @Autowired
    private InvoiceMessage invoiceMessage;

    @Override
    public List<InvoiceMegaDTO> findAllInvoices() {
        return invoiceMessage.findAllInvoices();
    }

    @Override
    public List<InvoiceProjectMegaDTO> findInvoiceProjectByInvoice(InvoiceMegaDTO invoiceMegaDTO) {
        return invoiceMessage.findInvoiceProjectByInvoice(invoiceMegaDTO);
    }

}