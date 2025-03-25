package com.ciandt.pms.message.dto;

import java.math.BigDecimal;

public interface InvoiceProjectDTO {
    Long getProjectCode();
    String getProductDescription();
    Long getMegaBranchCode();
    Long getProviderCode();
    String getProviderName();
    BigDecimal getTotalInvoiceValue();
}
