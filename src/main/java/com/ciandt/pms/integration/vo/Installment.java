package com.ciandt.pms.integration.vo;

import java.math.BigDecimal;

/**
 * Created by vnogueira on 22/08/18.
 */
public class Installment {

    private String installmentOperationCode;

    private Long installmentNumber;

    private BigDecimal installmentValue;


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
}
