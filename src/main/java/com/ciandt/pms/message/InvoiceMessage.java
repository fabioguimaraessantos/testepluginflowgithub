package com.ciandt.pms.message;

import com.ciandt.pms.message.client.InvoiceClient;
import com.ciandt.pms.message.dto.InvoiceMegaDTO;
import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class InvoiceMessage {
    @Autowired
    private InvoiceClient client;

    public List<InvoiceMegaDTO> findAllInvoices() {
        try {
            return client.getAllInvoices();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<InvoiceProjectMegaDTO> findInvoiceProjectByInvoice(InvoiceMegaDTO invoiceMegaDTO) {
        try {
            return client.getInvoiceProjectByInvoice(invoiceMegaDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}