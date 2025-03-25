package com.ciandt.pms.integration.vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class CancelInvoice {
    private final String invoiceCode;
    private final List<String> journals;

    public CancelInvoice(String invoiceCode, List<String> qboRelatedJournals) {
        this.invoiceCode = invoiceCode;
        this.journals = qboRelatedJournals != null && !qboRelatedJournals.isEmpty() ?
                new ArrayList<>(qboRelatedJournals) : new ArrayList<>();
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }
}
