package com.ciandt.pms.integration.vo.licenses;

import java.util.Date;

public class License {

    private String companyCode;
    private String financialAccountSegment;
    private String projectSegment;
    private String currencyCode;
    private Double enteredDrAmount;
    private Double enteredCrAmount;
    private String reference10;
    private String accountingDate;
    /* Getters and Setters */
    public String getAccountingDate() { return accountingDate; }
    public void setAccountingDate(String accountingDate) {
        this.accountingDate = accountingDate;
    }
    public String getReference10() {
        return reference10;
    }
    public void setReference10(String reference10) {
        this.reference10 = reference10;
    }
    public Double getValue() {
        return enteredDrAmount;
    }
    public String getProjectSegment() {
        return projectSegment;
    }
    public void setProjectSegment(String projectSegment) {
        this.projectSegment = projectSegment;
    }
    public String getFinancialAccountSegment() {
        return financialAccountSegment;
    }
    public void setFinancialAccountSegment(String financialAccountSegment) { this.financialAccountSegment = financialAccountSegment; }
    public String getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(String customerCode) {
        this.currencyCode = customerCode;
    }
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companySegment) { this.companyCode = companySegment; }
    public Double getEnteredDrAmount() { return enteredDrAmount; }
    public void setEnteredDrAmount(Double enteredDrAmount) { this.enteredDrAmount = enteredDrAmount; }
    public Double getEnteredCrAmount() { return enteredCrAmount; }
    public void setEnteredCrAmount(Double enteredCrAmount) { this.enteredCrAmount = enteredCrAmount; }
}