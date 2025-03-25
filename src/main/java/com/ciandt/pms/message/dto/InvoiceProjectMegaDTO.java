package com.ciandt.pms.message.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceProjectMegaDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long code;

    @JsonProperty("megaBranchCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long megaBranchCode;

    @JsonProperty("productDescription")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String productDescription;

    @JsonProperty("totalInvoiceValue")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalInvoiceValue;

    @JsonProperty("projectValue")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal projectValue;

    @JsonProperty("providerCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long providerCode;

    @JsonProperty("projectCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long projectCode;

    @JsonProperty("projectDescription")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String projectDescription;

    @JsonProperty("providerName")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String providerName;
}