package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "VW_PMS_INTEG_INVOICE_NATIONAL")
@NamedQueries({
        @NamedQuery(name = "VwPmsIntegFaturaNacional.findAll", query = "SELECT t FROM VwPmsIntegFaturaNacional t "),
        @NamedQuery(name = "VwPmsIntegFaturaNacional.findByInvoiceCode", query = "SELECT t FROM VwPmsIntegFaturaNacional t "
                + " WHERE t.invoiceCode = ?")
})
public class VwPmsIntegFaturaNacional implements Serializable, Cloneable {

    public static final String FIND_ALL = "VwPmsIntegFaturaNacional.findAll";
    public static final String FIND_BY_INVOICE_CODE = "VwPmsIntegFaturaNacional.findByInvoiceCode";

    private static final long serialVersionUID = -217330419995254956L;

    @Id
    @Column(name = "ID_VIEW")
    private Long id;

    @Column(name = "INVOICE_CODE")
    private Long invoiceCode;

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

    @Column(name = "OBSERVACAO_NF_OPERACAO")
    private String invoiceObservationOperation;

    @Column(name = "NOB_CH_TIPO_OBSERVACAO")
    private String invoiceObservationType;

    @Column(name = "NOB_ST_OBSERVACAO")
    private String invoiceObservation;

    @Column(name = "PARCELAS_OPERACAO")
    private String installmentOperationCode;

    @Column(name = "MOV_ST_PARCELA")
    private Long installmentNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "MOV_DT_VENCTO")
    private Date installmentDueDate;

    @Column(name = "MOV_RE_VALORMOE")
    private BigDecimal installmentValue;

    public Long getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(Long invoiceCode) {
        this.invoiceCode = invoiceCode;
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

    public String getInvoiceObservationOperation() {
        return invoiceObservationOperation;
    }

    public void setInvoiceObservationOperation(String invoiceObservationOperation) {
        this.invoiceObservationOperation = invoiceObservationOperation;
    }

    public String getInvoiceObservationType() {
        return invoiceObservationType;
    }

    public void setInvoiceObservationType(String invoiceObservationType) {
        this.invoiceObservationType = invoiceObservationType;
    }

    public String getInvoiceObservation() {
        return invoiceObservation;
    }

    public void setInvoiceObservation(String invoiceObservation) {
        this.invoiceObservation = invoiceObservation;
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

    public Date getInstallmentDueDate() {
        return installmentDueDate;
    }

    public void setInstallmentDueDate(Date installmentDueDate) {
        this.installmentDueDate = installmentDueDate;
    }

    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
    }

    /**
     * Realiza uma copia do objeto, porem com uma referencia diferente.
     *
     * @return a cópia do Objeto
     */
    public VwPmsIntegFaturaNacional getClone() {
        try {
            return (VwPmsIntegFaturaNacional) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
