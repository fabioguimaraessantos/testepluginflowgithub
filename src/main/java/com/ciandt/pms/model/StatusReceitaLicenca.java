package com.ciandt.pms.model;

import com.ciandt.pms.Constants;

import java.math.BigDecimal;

/**
 * Created by rbastos on 08/08/2018.
 */
public class StatusReceitaLicenca {
    private Long revenueCode;
    private String revenueStatus;
    private String revenueVersion;
    private String errorMessage;
    private BigDecimal megaOrderID;

    public boolean isError() {
        return revenueStatus == Constants.INTEGRACAO_STATUS_ERROR;
    }

    public StatusReceitaLicenca(Long revenueCode, String revenueStatus, String revenueVersion, String errorMessage, BigDecimal megaOrderID){
        this.setRevenueCode(revenueCode);
        this.setRevenueStatus(revenueStatus);
        this.setRevenueVersion(revenueVersion);
        this.setErrorMessage(errorMessage);
        this.setMegaOrderID(megaOrderID);
    }

    public Long getRevenueCode() {
        return revenueCode;
    }

    public void setRevenueCode(Long revenueCode) {
        this.revenueCode = revenueCode;
    }

    public String getRevenueStatus() {
        return revenueStatus;
    }

    public void setRevenueStatus(String revenueStatus) {
        this.revenueStatus = revenueStatus;
    }

    public String getRevenueVersion() {
        return revenueVersion;
    }

    public void setRevenueVersion(String revenueVersion) {
        this.revenueVersion = revenueVersion;
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
