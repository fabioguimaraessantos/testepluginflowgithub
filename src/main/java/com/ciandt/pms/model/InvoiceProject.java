package com.ciandt.pms.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
public class InvoiceProject {

    private Long orgTabCode;
    private Long orgPadCode;
    private Long orgCode;
    private String orgTauCode;
    private Long agnTabCode;
    private Long agnPadCode;
    private Long agnCode;
    private String agnTauCode;
    private String invoiceNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate invoiceDate;
    private Invoice invoice;

    public InvoiceProject() {
    }

    public InvoiceProject(Long orgTabCode, Long orgPadCode, Long orgCode, String orgTauCode, Long agnTabCode, Long agnPadCode, Long agnCode, String agnTauCode, String invoiceNumber, LocalDate invoiceDate, Invoice invoice) {
        this.orgTabCode = orgTabCode;
        this.orgPadCode = orgPadCode;
        this.orgCode = orgCode;
        this.orgTauCode = orgTauCode;
        this.agnTabCode = agnTabCode;
        this.agnPadCode = agnPadCode;
        this.agnCode = agnCode;
        this.agnTauCode = agnTauCode;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.invoice = invoice;
    }

    public Long getOrgTabCode() {
        return orgTabCode;
    }

    public void setOrgTabCode(Long orgTabCode) {
        this.orgTabCode = orgTabCode;
    }

    public Long getOrgPadCode() {
        return orgPadCode;
    }

    public void setOrgPadCode(Long orgPadCode) {
        this.orgPadCode = orgPadCode;
    }

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgTauCode() {
        return orgTauCode;
    }

    public void setOrgTauCode(String orgTauCode) {
        this.orgTauCode = orgTauCode;
    }

    public Long getAgnTabCode() {
        return agnTabCode;
    }

    public void setAgnTabCode(Long agnTabCode) {
        this.agnTabCode = agnTabCode;
    }

    public Long getAgnPadCode() {
        return agnPadCode;
    }

    public void setAgnPadCode(Long agnPadCode) {
        this.agnPadCode = agnPadCode;
    }

    public Long getAgnCode() {
        return agnCode;
    }

    public void setAgnCode(Long agnCode) {
        this.agnCode = agnCode;
    }

    public String getAgnTauCode() {
        return agnTauCode;
    }

    public void setAgnTauCode(String agnTauCode) {
        this.agnTauCode = agnTauCode;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}