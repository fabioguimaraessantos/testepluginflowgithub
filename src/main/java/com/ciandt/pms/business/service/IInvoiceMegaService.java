package com.ciandt.pms.business.service;

import com.ciandt.pms.message.dto.InvoiceMegaDTO;
import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import java.util.List;
import org.springframework.stereotype.Service;
@Service
public interface IInvoiceMegaService {
    List<InvoiceMegaDTO> findAllInvoices();

    List<InvoiceProjectMegaDTO> findInvoiceProjectByInvoice(InvoiceMegaDTO invoiceMegaDTO);

}