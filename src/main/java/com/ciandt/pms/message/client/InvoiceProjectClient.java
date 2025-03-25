package com.ciandt.pms.message.client;

import com.ciandt.pms.message.dto.InvoiceProjectDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InvoiceProjectClient extends MegaClient {
    private Logger log = LoggerFactory.getLogger(InvoiceProjectClient.class);
    private static final String URI_PROXY_INVOICES_PROJECTS = "invoices/projects?orgTabCode=" + 1 +
            "&orgPadCode=" + 1 +
            "&orgCode=" + 1 +
            "&orgTauCode=" + "orgTauCode" +
            "&agnTabCode=" + 1 +
            "&orgPadCode=" + 1 +
            "&agnCode=" + 1 +
            "&agnTauCode=" + "agnTauCode" +
            "&invoiceNumber=" + "invoiceNumber" +
            "&invoiceDate=" + LocalDateTime.now();

    public  List<InvoiceProjectDTO> get() {
        try {
            log.info("getInvoicesProjectsByProxy");
            JsonArray content = getData(URI_PROXY_INVOICES_PROJECTS).getAsJsonArray("content");
            Gson gson = new Gson();
            List<InvoiceProjectDTO> invoices = new ArrayList<>();
            if (!content.isJsonNull()) {
                for(JsonElement element : content)
                    invoices.add(gson.fromJson(element, InvoiceProjectDTO.class));
            }
            return invoices;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}