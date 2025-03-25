package com.ciandt.pms.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class LicencaSwIntegrateDetail implements Serializable {

    private Long integrationId;
    private String docNumber;
    private String status;
    private String message;
}