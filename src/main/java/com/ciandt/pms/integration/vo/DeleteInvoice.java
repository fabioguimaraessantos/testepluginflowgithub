package com.ciandt.pms.integration.vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

@Data
public class DeleteInvoice {

    private String invoiceCode;

    public String toJson(){
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        return gson.toJson(this);
    }

}
