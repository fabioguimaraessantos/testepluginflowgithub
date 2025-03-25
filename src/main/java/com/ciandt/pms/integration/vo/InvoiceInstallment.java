package com.ciandt.pms.integration.vo;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceInstallment {
    private String installmentOperationCode;

    private Long installmentNumber;

    private BigDecimal installmentValue;

    private Date installmentDueDate;


    public String getInstallmentOperationCode() {
        return installmentOperationCode;
    }

    public void setInstallmentOperationCode(String installmentOperationCode) {
        this.installmentOperationCode = installmentOperationCode;
    }

    public Long getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Long installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
    }

    public Date getInstallmentDueDate() {
        return installmentDueDate;
    }

    public void setInstallmentDueDate(Date installmentDueDate) {
        this.installmentDueDate = installmentDueDate;
    }
}
