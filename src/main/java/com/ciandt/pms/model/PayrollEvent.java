package com.ciandt.pms.model;

import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by amanda on 31/01/17.
 */
@Entity
@Table(name = "PAYROLL_EVENT")
@NamedQueries({
        @NamedQuery(name = PayrollEvent.FIND_ALL, query = "SELECT t FROM PayrollEvent t ORDER BY t.code"),
        @NamedQuery(name = PayrollEvent.FIND_BY_CODE_AND_NAME, query = "SELECT t FROM PayrollEvent t " +
                "WHERE (t.code = :code or :code is null) " +
                "AND (upper(t.name) like '%'||TRIM(UPPER(:name))||'%' or :name is null) " +
                "ORDER BY t.code")
})
public class PayrollEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "PayrollEvent.findAll";

    public static final String FIND_BY_CODE_AND_NAME = "PayrollEvent.findByCodeAndName";

    @Id
    @GeneratedValue(generator = "PayrollEventSeq")
    @SequenceGenerator(name = "PayrollEventSeq", sequenceName = "SQ_PAEV_CD_PAYROLL_EVENT", allocationSize = 1)
    @Column(name = "PAEV_CD_PAYROLL_EVENT")
    private Long code;

    @Column(name = "PAEV_NM_PAYROLL_EVENT")
    private String name;

    @Column(name = "PAEV_CD_PAYROLL_EVENT_NATURE")
    private String natureCode;

    @Column(name = "PAEV_SG_PAYROLL_EVENT_TYPE")
    private String eventTypeCode;

    @Column(name = "PAEV_NM_PAYROLL_EVENT_TYPE")
    private String eventTypeName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAEV_DT_CREATE")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PAEV_DT_UPDATE")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAET_CD_PAYROLL_EVENT_TYPE")
    private PayrollEventType payrollEventType;

    @Transient
    private String reclass;

    public PayrollEvent() {
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public PayrollEventType getPayrollEventType() {
        return payrollEventType;
    }

    public String getReclass() {
        return StringUtils.isBlank(this.reclass) ? this.getPayrollEventType() == null ?
                "" : this.getPayrollEventType().getDescription() : this.reclass;
    }

    public void setReclass(String reclass) {
        this.reclass = reclass;
    }

    public void setPayrollEventType(PayrollEventType payrollEventType) {
        this.payrollEventType = payrollEventType;
    }

    @Override
    public String toString() {
        return "PayrollEvent{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", natureCode='" + natureCode + '\'' +
                ", eventTypeCode='" + eventTypeCode + '\'' +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", payrollEventType=" + payrollEventType +
                '}';
    }
}