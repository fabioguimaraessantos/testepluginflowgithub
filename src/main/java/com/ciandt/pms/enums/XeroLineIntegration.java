package com.ciandt.pms.enums;

public enum XeroLineIntegration {

    DETAILED("DETAILED"),
    SUMMARIZED("SUMMARIZED");

    private final String lineIntegration;

    public String getLineIntegration() {
        return this.lineIntegration;
    }

    XeroLineIntegration(final String lineIntegration) {
        this.lineIntegration = lineIntegration;
    }
}
