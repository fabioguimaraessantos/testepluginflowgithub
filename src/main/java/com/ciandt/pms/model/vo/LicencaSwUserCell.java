package com.ciandt.pms.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by vnogueira on 08/10/18.
 */
@Data
public class LicencaSwUserCell implements Serializable {

    private String month;

    private String year;

    private Long itResourceCode;

    private String itResourceName;

    private Double licenseTotalValue;

    private String status;

    private Long costCenterCode;

    private String costCenterName;

    private Long megaProjectCode;

    private String megaProjectName;

    private Double licenseValue;

    private String licenseLogins;

}
