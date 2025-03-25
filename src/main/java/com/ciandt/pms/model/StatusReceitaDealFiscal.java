package com.ciandt.pms.model;

import com.ciandt.pms.Constants;

import java.math.BigDecimal;

/**
 * Created by rbastos on 08/08/2018.
 */
public class StatusReceitaDealFiscal {
    private Long revenueDealFiscalCode;
    private String revenueStatus;
    private String errorMessage;
    private BigDecimal megaOrderID;

    public boolean isError() {
        return revenueStatus == Constants.INTEGRACAO_STATUS_ERROR;
    }

    public StatusReceitaDealFiscal(Long revenueDealFiscalCode, String revenueStatus, String errorMessage, BigDecimal megaOrderID){
        this.setRevenueDealFiscalCode(revenueDealFiscalCode);
        this.setRevenueStatus(revenueStatus);
        this.setErrorMessage(errorMessage);
        this.setMegaOrderID(megaOrderID);
    }

    public Long getRevenueDealFiscalCode() {
        return revenueDealFiscalCode;
    }

    public void setRevenueDealFiscalCode(Long revenueDealFiscalCode) {
        this.revenueDealFiscalCode = revenueDealFiscalCode;
    }

    public String getRevenueStatus() {
        return revenueStatus;
    }

    public void setRevenueStatus(String revenueStatus) {
        this.revenueStatus = revenueStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BigDecimal getMegaOrderID() {
        return megaOrderID;
    }

    public void setMegaOrderID(BigDecimal megaOrderID) {
        this.megaOrderID = megaOrderID;
    }
}
