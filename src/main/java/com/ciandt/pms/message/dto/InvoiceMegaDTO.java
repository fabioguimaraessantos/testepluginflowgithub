package com.ciandt.pms.message.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceMegaDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long code;

    @JsonProperty("orgTabCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long orgTabCode;

    @JsonProperty("orgPadCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long orgPadCode;

    @JsonProperty("orgCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long orgCode;

    @JsonProperty("orgTauCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String orgTauCode;

    @JsonProperty("agnTabCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long agnTabCode;

    @JsonProperty("agnPadCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long agnPadCode;

    @JsonProperty("agnCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long agnCode;

    @JsonProperty("agnTauCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String agnTauCode;

    @JsonProperty("invoiceNumber")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String invoiceNumber;

    @JsonProperty("invoiceDate")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String invoiceDate;

    @JsonProperty("providerCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long providerCode;
}