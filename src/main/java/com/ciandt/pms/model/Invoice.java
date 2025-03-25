package com.ciandt.pms.model;

import com.ciandt.pms.message.dto.CostCenterDTO;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Entity gerado a partir da integra??o Mega API
 */
public class Invoice {
    private String code;
    private Date date;
    private String invoiceNumber;
    private InvoiceProject project;
    private String licenseID;
    private String resourceName;
    private Long branch;
    private Long installmentAmountQuantity; // entrada manual, quantidade
    private Long installmentNumber;
    private String provider;
    private Long itResource;
    private BigDecimal totalAmount;
    private Moeda currency;
    private BigDecimal installmentAmountValue;
    private Date startDate;
    private String login;
    private CostCenterDTO costCenterDTO;
    private BigDecimal amountPaid;
    public Invoice() {
    }
    public Invoice(String code, Date date, String invoiceNumber,
                   InvoiceProject project, String licenseID, String resourceName,
                   Long branch, Long installmentAmountQuantity, Long installmentNumber,
                   String provider, Long itResource, BigDecimal totalAmount,
                   Moeda currency, BigDecimal installmentAmountValue, Date startDate,
                   String login, CostCenterDTO costCenterDTO, BigDecimal amountPaid) {
        this.code = code;
        this.date = date;
        this.invoiceNumber = invoiceNumber;
        this.project = project;
        this.licenseID = licenseID;
        this.resourceName = resourceName;
        this.branch = branch;
        this.installmentAmountQuantity = installmentAmountQuantity;
        this.installmentNumber = installmentNumber;
        this.provider = provider;
        this.itResource = itResource;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.installmentAmountValue = installmentAmountValue;
        this.startDate = startDate;
        this.login = login;
        this.costCenterDTO = costCenterDTO;
        this.amountPaid = amountPaid;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    public InvoiceProject getProject() {
        return project;
    }
    public void setProject(InvoiceProject project) {
        this.project = project;
    }
    public String getLicenseID() {
        return licenseID;
    }
    public void setLicenseID(String licenseID) {
        this.licenseID = licenseID;
    }
    public String getResourceName() {
        return resourceName;
    }
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    public Long getBranch() {
        return branch;
    }
    public void setBranch(Long branch) {
        this.branch = branch;
    }
    public Long getInstallmentAmountQuantity() {
        return installmentAmountQuantity;
    }
    public void setInstallmentAmountQuantity(Long installmentAmountQuantity) {
        this.installmentAmountQuantity = installmentAmountQuantity;
    }
    public Long getInstallmentNumber() {
        return installmentNumber;
    }
    public void setInstallmentNumber(Long installmentNumber) {
        this.installmentNumber = installmentNumber;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public Long getItResource() {
        return itResource;
    }
    public void setItResource(Long itResource) {
        this.itResource = itResource;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Moeda getCurrency() {
        return currency;
    }
    public void setCurrency(Moeda currency) {
        this.currency = currency;
    }
    public BigDecimal getInstallmentAmountValue() {
        return installmentAmountValue;
    }
    public void setInstallmentAmountValue(BigDecimal installmentAmountValue) {
        this.installmentAmountValue = installmentAmountValue;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public CostCenterDTO getCostCenterDTO() {
        return costCenterDTO;
    }
    public void setCostCenterDTO(CostCenterDTO costCenterDTO) {
        this.costCenterDTO = costCenterDTO;
    }
    public BigDecimal getAmountPaid() {
        return amountPaid;
    }
    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}