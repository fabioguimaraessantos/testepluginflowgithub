package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "VW_PMS_INTEG_REVENUE_NATIONAL")
@NamedQueries({
        @NamedQuery(name = "VwPmsIntegReceitaNacional.findAll", query = "SELECT t FROM VwPmsIntegReceitaNacional t ")
})
public class VwPmsIntegReceitaNacional implements Serializable {

    public static final String FIND_ALL = "VwPmsIntegReceitaNacional.findAll";

    private static final long serialVersionUID = -217330419995254956L;

    @Id
    @Column(name = "REVENUE_CODE")
    private Long revenueCode;

    @Column(name = "REVENUE_SOURCE")
    private String revenueSource;

    @Column(name = "IS_INTERCOMPANY", length = 1)
    private String isIntercompany;

    @Column(name = "NOTA_FISCAL_OPERACAO", length = 1)
    private String operationCode;

    @Column(name = "FIL_IN_CODIGO")
    private Long branchCompanyCode;

    @Temporal(TemporalType.DATE)
    @Column(name = "NOT_DT_EMISSAO")
    private Date issueAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "NOT_DT_SAIDA")
    private Date sentAt;

    @Column(name = "COND_ST_CODIGO")
    private String paymentConditionName;

    @Column(name = "NOT_RE_VALORTOTAL", precision = 22, scale = 0)
    private BigDecimal totalValue = BigDecimal.ZERO;

    @Column(name = "NOT_CH_SITUACAO")
    private String invoiceSituation;

    @Column(name = "TPD_IN_CODIGO")
    private Long documentTypeCode;

    @Column(name = "AGN_TAU_ST_CODIGO", length = 1)
    private String auxCode;

    @Column(name = "CALC_AGN_ST_CODIGO")
    private String calcAgentCode;

    @Column(name = "CALC_AGN_ST_TIPOCODIGO")
    private String calcAgentTypeCode;

    @Column(name = "ITEM_NOTA_FISCAL_OPERACAO")
    private String itemOperationCode;

    @Column(name = "ITN_IN_SEQUENCIA")
    private Long itemSequencial;

    @Column(name = "PRO_IN_CODIGO")
    private Long itemCode;

    @Column(name = "ITN_RE_QUANTIDADE")
    private Long itemQuantity;

    @Column(name = "ITN_RE_VALORUNITARIO")
    private BigDecimal unitValue;

    @Column(name = "APL_IN_CODIGO")
    private Long itemApplicationCode;

    @Column(name = "TPC_ST_CLASSE")
    private String classType;

    @Column(name = "PROJ_IDE_ST_CODIGO")
    private Long idProjectCode;

    @Column(name = "PROJ_IN_REDUZIDO")
    private Long projectReduceCode;

    @Column(name = "CUS_IDE_ST_CODIGO")
    private Long idCostCenterCode;

    @Column(name = "CUS_IN_REDUZIDO")
    private Long costCenterReduceCode;

    @Column(name = "COS_IN_CODIGO")
    private Long serviceCode;

    @Column(name = "PARCELAS_OPERACAO")
    private String installmentOperationCode;

    @Column(name = "MOV_ST_PARCELA")
    private Long installmentNumber;

    @Column(name = "MOV_RE_VALORMOE")
    private BigDecimal installmentValue;

    public Long getRevenueCode() {
        return revenueCode;
    }

    public void setRevenueCode(Long revenueCode) {
        this.revenueCode = revenueCode;
    }

    public String getRevenueSource() {
        return revenueSource;
    }

    public void setRevenueSource(String revenueSource) {
        this.revenueSource = revenueSource;
    }

    public String getIsIntercompany() {
        return isIntercompany;
    }

    public void setIsIntercompany(String isIntercompany) {
        this.isIntercompany = isIntercompany;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public Long getBranchCompanyCode() {
        return branchCompanyCode;
    }

    public void setBranchCompanyCode(Long branchCompanyCode) {
        this.branchCompanyCode = branchCompanyCode;
    }

    public Date getIssueAt() {
        return issueAt;
    }

    public void setIssueAt(Date issueAt) {
        this.issueAt = issueAt;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getPaymentConditionName() {
        return paymentConditionName;
    }

    public void setPaymentConditionName(String paymentConditionName) {
        this.paymentConditionName = paymentConditionName;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public String getInvoiceSituation() {
        return invoiceSituation;
    }

    public void setInvoiceSituation(String invoiceSituation) {
        this.invoiceSituation = invoiceSituation;
    }

    public Long getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(Long documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getAuxCode() {
        return auxCode;
    }

    public void setAuxCode(String auxCode) {
        this.auxCode = auxCode;
    }

    public String getCalcAgentCode() {
        return calcAgentCode;
    }

    public void setCalcAgentCode(String calcAgentCode) {
        this.calcAgentCode = calcAgentCode;
    }

    public String getCalcAgentTypeCode() {
        return calcAgentTypeCode;
    }

    public void setCalcAgentTypeCode(String calcAgentTypeCode) {
        this.calcAgentTypeCode = calcAgentTypeCode;
    }

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

    public String getInstallmentOperationCode() {
        return installmentOperationCode;
    }

    public void setInstallmentOperationCode(String installmentOperationCode) {
        this.installmentOperationCode = installmentOperationCode;
    }

    public Long getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Long installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
    }
}
