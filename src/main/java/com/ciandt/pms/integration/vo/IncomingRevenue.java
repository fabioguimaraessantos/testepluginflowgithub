package com.ciandt.pms.integration.vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class IncomingRevenue implements java.io.Serializable  {

    private static final long serialVersionUID = 1L;

    private Long branchCompanyCode;

    private Date issueAt;

    private Date sentAt;

    private String paymentConditionName;

    private BigDecimal totalValue;

    private Long revenueCode;

    private String invoiceSituation;

    private Long documentTypeCode;

    private String auxCode;

    private String calcAgentCode;

    private String calcAgentTypeCode;

    private RevenueItem revenueItem;

    private Installment installment;


    public Long getRevenueCode() {
        return revenueCode;
    }

    public void setRevenueCode(Long revenueCode) {
        this.revenueCode = revenueCode;
    }

    public Long getBranchCompanyCode() {
        return branchCompanyCode;
    }

    public void setBranchCompanyCode(Long branchCompanyCode) {
        this.branchCompanyCode = branchCompanyCode;
    }

    public Date getIssueAt() {
        return issueAt;
    }

    public void setIssueAt(Date issueAt) {
        this.issueAt = issueAt;
    }

    public String getPaymentConditionName() {
        return paymentConditionName;
    }

    public void setPaymentConditionName(String paymentConditionName) {
        this.paymentConditionName = paymentConditionName;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        totalValue = totalValue.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        this.totalValue = totalValue;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        return gson.toJson(this);
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getInvoiceSituation() {
        return invoiceSituation;
    }

    public void setInvoiceSituation(String invoiceSituation) {
        this.invoiceSituation = invoiceSituation;
    }

    public Long getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(Long documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getAuxCode() {
        return auxCode;
    }

    public void setAuxCode(String auxCode) {
        this.auxCode = auxCode;
    }

    public String getCalcAgentCode() {
        return calcAgentCode;
    }

    public void setCalcAgentCode(String calcAgentCode) {
        this.calcAgentCode = calcAgentCode;
    }

    public String getCalcAgentTypeCode() {
        return calcAgentTypeCode;
    }

    public void setCalcAgentTypeCode(String calcAgentTypeCode) {
        this.calcAgentTypeCode = calcAgentTypeCode;
    }

    public RevenueItem getRevenueItem() {
        return revenueItem;
    }

    public void setRevenueItem(RevenueItem revenueItem) {
        this.revenueItem = revenueItem;
    }

    public Installment getInstallment() {
        return installment;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }
}
