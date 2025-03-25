package com.ciandt.pms.integration.vo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigDecimal;
import java.util.*;

public class IncomingInvoice implements java.io.Serializable  {

    private static final long serialVersionUID = 1L;

    private Long branchCompanyCode;

    private Date issueAt;

    private Date sentAt;

    private String paymentConditionName;

    private Long invoiceCode;

    private String invoiceSituation;

    private Long documentTypeCode;

    private String auxCode;

    private String calcAgentCode;

    private String calcAgentTypeCode;

    private List<RevenueItem> invoiceItens;

    private InvoiceObservation invoiceObservation;

    private InvoiceInstallment installment;

    public Long getBranchCompanyCode() {
        return branchCompanyCode;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        return gson.toJson(this);
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

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getPaymentConditionName() {
        return paymentConditionName;
    }

    public void setPaymentConditionName(String paymentConditionName) {
        this.paymentConditionName = paymentConditionName;
    }

    public Long getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(Long invoiceCode) {
        this.invoiceCode = invoiceCode;
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

    public List<RevenueItem> getInvoiceItens() {
        return invoiceItens;
    }

    public void setInvoiceItens(List<RevenueItem> invoiceItens) {
        this.invoiceItens = invoiceItens;
    }

    public InvoiceObservation getInvoiceObservation() {
        return invoiceObservation;
    }

    public void setInvoiceObservation(InvoiceObservation invoiceObservation) {
        this.invoiceObservation = invoiceObservation;
    }

    public InvoiceInstallment getInstallment() {
        return installment;
    }

    public void setInstallment(InvoiceInstallment installment) {
        this.installment = installment;
    }
}
