package com.ciandt.pms.message.client;

import com.ciandt.pms.message.dto.InvoiceMegaDTO;
import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InvoiceClient extends MegaClient {
    private Logger log = LoggerFactory.getLogger(InvoiceClient.class);
    private static final String URI_PROXY_INVOICES_LICENSES = "invoices/licenses";
    private static final String URI_PROXY_INVOICES_PROJECTS = "invoices/projects";

    public  List<InvoiceMegaDTO> getAllInvoices() {
        try {
            log.info("getAllInvoicesProxy");
            JsonArray content = getData(URI_PROXY_INVOICES_LICENSES).getAsJsonArray("content");
            Gson gson = new Gson();
            List<InvoiceMegaDTO> invoices = new ArrayList<>();
            if (!content.isJsonNull()) {
                long count = 1L;
                for(JsonElement element : content){
                    InvoiceMegaDTO invoice = gson.fromJson(element, InvoiceMegaDTO.class);
                    invoice.setCode(count++);
                    invoices.add(invoice);
                }

            }
            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public  List<InvoiceProjectMegaDTO> getInvoiceProjectByInvoice(InvoiceMegaDTO invoiceMegaDTO) {
        try {
            log.info("getInvoiceProjectByInvoiceProxy");

            String uri = URI_PROXY_INVOICES_PROJECTS;
            uri = uri + "?orgTabCode=" + invoiceMegaDTO.getOrgTabCode();
            uri = uri + "&orgPadCode=" + invoiceMegaDTO.getOrgPadCode();
            uri = uri + "&orgCode=" + invoiceMegaDTO.getOrgCode();
            uri = uri + "&orgTauCode=" + invoiceMegaDTO.getOrgTauCode();
            uri = uri + "&agnTabCode=" + invoiceMegaDTO.getAgnTabCode();
            uri = uri + "&agnPadCode=" + invoiceMegaDTO.getAgnPadCode();
            uri = uri + "&agnCode=" + invoiceMegaDTO.getAgnCode();
            uri = uri + "&agnTauCode=" + invoiceMegaDTO.getAgnTauCode();
            uri = uri + "&invoiceNumber=" + invoiceMegaDTO.getInvoiceNumber();
            uri = uri + "&invoiceDate=" + invoiceMegaDTO.getInvoiceDate();

            JsonArray content = getData(uri).getAsJsonArray("content");
            Gson gson = new Gson();
            List<InvoiceProjectMegaDTO> invoices = new ArrayList<>();
            if (!content.isJsonNull()) {
                for(JsonElement element : content)
                    invoices.add(gson.fromJson(element, InvoiceProjectMegaDTO.class));
            }
            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}