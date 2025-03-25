package com.ciandt.pms.enums;

public enum ErpEnum {

    ORACLE("ORACLE"),
    MEGA("MEGA"),
    XERO("XERO");

    private String name;

    public String getName() {
        return this.name;
    }

    ErpEnum(final String name) {
        this.name = name;
    }
}
