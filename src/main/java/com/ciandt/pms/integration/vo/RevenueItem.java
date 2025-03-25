package com.ciandt.pms.integration.vo;

import java.math.BigDecimal;

/**
 * Created by vnogueira on 22/08/18.
 */
public class RevenueItem {

    private String itemOperationCode;

    private Long itemSequencial;

    private Long itemCode;

    private Long itemQuantity;

    private BigDecimal unitValue;

    private Long itemApplicationCode;

    private String classType;

    private Long idProjectCode;

    private Long projectReduceCode;

    private Long idCostCenterCode;

    private Long costCenterReduceCode;

    private Long serviceCode;


    public String getItemOperationCode() {
        return itemOperationCode;
    }

    public void setItemOperationCode(String itemOperationCode) {
        this.itemOperationCode = itemOperationCode;
    }

    public Long getItemSequencial() {
        return itemSequencial;
    }

    public void setItemSequencial(Long itemSequencial) {
        this.itemSequencial = itemSequencial;
    }

    public Long getItemCode() {
        return itemCode;
    }

    public void setItemCode(Long itemCode) {
        this.itemCode = itemCode;
    }

    public Long getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Long itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(BigDecimal unitValue) {
        this.unitValue = unitValue;
    }

    public Long getItemApplicationCode() {
        return itemApplicationCode;
    }

    public void setItemApplicationCode(Long itemApplicationCode) {
        this.itemApplicationCode = itemApplicationCode;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public Long getIdProjectCode() {
        return idProjectCode;
    }

    public void setIdProjectCode(Long idProjectCode) {
        this.idProjectCode = idProjectCode;
    }

    public Long getProjectReduceCode() {
        return projectReduceCode;
    }

    public void setProjectReduceCode(Long projectReduceCode) {
        this.projectReduceCode = projectReduceCode;
    }

    public Long getIdCostCenterCode() {
        return idCostCenterCode;
    }

    public void setIdCostCenterCode(Long idCostCenterCode) {
        this.idCostCenterCode = idCostCenterCode;
    }

    public Long getCostCenterReduceCode() {
        return costCenterReduceCode;
    }

    public void setCostCenterReduceCode(Long costCenterReduceCode) {
        this.costCenterReduceCode = costCenterReduceCode;
    }

    public Long getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(Long serviceCode) {
        this.serviceCode = serviceCode;
    }
}
