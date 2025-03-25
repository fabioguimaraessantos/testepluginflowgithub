package com.ciandt.pms.message.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractLobDTO implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long code;

    @JsonProperty("message")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonProperty("shortCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shortCode;

    @JsonProperty("parentShortCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parentShortCode;

    @JsonProperty("padCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String padCode;

    @JsonProperty("ideCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String ideCode;

    @JsonProperty("description")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonProperty("accountType")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accountType;

    @JsonProperty("extensiveCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String extensiveCode;

    @JsonProperty("active")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean active;

    @JsonProperty("parentIdeCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parentIdeCode;

    @JsonProperty("nickname")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;

    @JsonProperty("implantationDate")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String implantationDate;

    @JsonProperty("deadline")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deadline;

}
