package com.ciandt.pms.model;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "DEAL_FISCAL_COND_PAGTO")
@NamedNativeQueries({
        @NamedNativeQuery(name = "PaymentConditionDealFiscal.findByDealFiscal", query = "SELECT DFCP.* FROM pms20.DEAL_FISCAL_COND_PAGTO DFCP " +
                " WHERE DFCP.DEFI_CD_DEAL_FISCAL = :dealFiscalCode", resultClass = PaymentConditionDealFiscal.class),
        @NamedNativeQuery(name = "PaymentConditionDealFiscal.findActualPaymentCondition", query = "SELECT DFCP.* FROM pms20.DEAL_FISCAL_COND_PAGTO DFCP " +
                " WHERE DFCP.DEFI_CD_DEAL_FISCAL = :dealFiscalCode" +
                " AND DFCP.DFCP_DT_FIM_VIGENCIA IS NULL", resultClass = PaymentConditionDealFiscal.class)})
public class PaymentConditionDealFiscal implements Serializable {

    public static final String FIND_BY_DEAL_FISCAL = "PaymentConditionDealFiscal.findByDealFiscal";
    public static final String FIND_ACTUAL_PAYMENT_CONDITION = "PaymentConditionDealFiscal.findActualPaymentCondition";

    @Id
    @GeneratedValue(generator = "PaymentConditionDealFiscalSeq")
    @SequenceGenerator(name = "PaymentConditionDealFiscalSeq", sequenceName = "SQ_DFCP_CD_DF_COND_PAGTO", allocationSize = 1)
    @Column(name = "DFCP_CD_DEAL_FISCAL_COND_PAGTO")
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFI_CD_DEAL_FISCAL", nullable = false)
    private DealFiscal dealFiscal;

    @Column(name = "DFCP_ST_COND_PAGTO")
    private String paymentConditionName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DFCP_DT_INICIO_VIGENCIA")
    private Date initialDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DFCP_DT_FIM_VIGENCIA")
    private Date finalDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DFCP_DT_ALTERACAO")
    private Date updatedAt;


    @Column(name = "DFCP_CD_LOGIN_AUTOR")
    private String loginAuthor;


    public PaymentConditionDealFiscal() {
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public DealFiscal getDealFiscal() {
        return dealFiscal;
    }

    public void setDealFiscal(DealFiscal dealFiscal) {
        this.dealFiscal = dealFiscal;
    }

    public String getPaymentConditionName() {
        return paymentConditionName;
    }

    public void setPaymentConditionName(String paymentConditionName) {
        this.paymentConditionName = paymentConditionName;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLoginAuthor() {
        return loginAuthor;
    }

    public void setLoginAuthor(String loginAuthor) {
        this.loginAuthor = loginAuthor;
    }
}