package com.ciandt.pms.enums;

public enum AjusteReceitaForecastStatusEnum {

    WAITING_FOR_APPROVAL("WAITING FOR APPROVAL"), ADJUSTMENT_APPROVED("ADJUSTMENT APPROVED"),
    REVENUE_APPROVED("REVENUE APPROVED"), SEND_TO_REVIEW("SEND TO REVIEW");

    private String indicadorStatus;

    public String getIndicadorStatus() {
        return indicadorStatus;
    }

    AjusteReceitaForecastStatusEnum(String indicadorStatus) {
        this.indicadorStatus = indicadorStatus;
    }
}