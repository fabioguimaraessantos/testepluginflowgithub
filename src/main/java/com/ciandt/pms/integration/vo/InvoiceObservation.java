package com.ciandt.pms.integration.vo;

import java.math.BigDecimal;

/**
 * Created by vnogueira on 22/08/18.
 */
public class InvoiceObservation {

    private String invoiceObservationOperation;

    private String invoiceObservationType;

    private String invoiceObservation;

    public String getInvoiceObservationOperation() {
        return invoiceObservationOperation;
    }

    public void setInvoiceObservationOperation(String invoiceObservationOperation) {
        this.invoiceObservationOperation = invoiceObservationOperation;
    }

    public String getInvoiceObservationType() {
        return invoiceObservationType;
    }

    public void setInvoiceObservationType(String invoiceObservationType) {
        this.invoiceObservationType = invoiceObservationType;
    }

    public String getInvoiceObservation() {
        return invoiceObservation;
    }

    public void setInvoiceObservation(String invoiceObservation) {
        this.invoiceObservation = invoiceObservation;
    }
}
