package com.ciandt.pms.model;

import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "COMPANY_ERP")
@Audited
@AuditTable(value = "COMPANY_ERP_AUD")
@NamedQueries({
        @NamedQuery(name = CompanyErp.FIND_ALL_ACTIVE, query = " " +
                " SELECT ce FROM CompanyErp ce JOIN ce.company c " +
                " WHERE ce.isActive = 'Y'"),
        @NamedQuery(name = CompanyErp.FIND_ACTIVE_BY_COMPANY, query = " " +
                " SELECT ce FROM CompanyErp ce JOIN ce.company c " +
                " WHERE c.codigoEmpresa = :companyCode AND ce.isActive = 'Y'"),
})
public class CompanyErp implements Serializable {

    private final static long serialVersionUID = 1L;

    public static final String FIND_ALL_ACTIVE = "CompanyErp.findAllActive";
    public static final String FIND_ACTIVE_BY_COMPANY = "CompanyErp.findActiveByCompany";

    @Id
    @GeneratedValue(generator = "CompanyErpSeq")
    @SequenceGenerator(name = "CompanyErpSeq", sequenceName = "SQ_COER_CD_COMPANY_ERP", allocationSize = 1)
    @Column(name = "COER_CD_COMPANY_ERP", unique = true, nullable = false, precision = 18, scale = 0)
    private Long companyErpCode;

    @Column(name = "COER_TX_ERP", nullable = false)
    private String erpName;

    @Column(name = "COER_IN_ACTIVE", nullable = false)
    @Type(type = "yes_no")
    private Boolean isActive;

    @Column(name = "COER_CREATED_AT", nullable = false)
    private Date createdAtDate;

    @Column(name = "COER_TX_LOGIN", nullable = false)
    private String login;

    @Column(name = "COER_TX_OBSERVATION", nullable = false)
    private String observation;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne
    @JoinColumn(name = "EMPR_CD_EMPRESA", nullable = false)
    private Empresa company;

    public CompanyErp() {
    }

    public Long getCompanyErpCode() {
        return companyErpCode;
    }

    public void setCompanyErpCode(Long companyErpCode) {
        this.companyErpCode = companyErpCode;
    }

    public String getErpName() {
        return erpName;
    }

    public void setErpName(String erpName) {
        this.erpName = erpName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getCreatedAtDate() {
        return createdAtDate;
    }

    public void setCreatedAtDate(Date createdAtDate) {
        this.createdAtDate = createdAtDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Empresa getCompany() {
        return company;
    }

    public void setCompany(Empresa company) {
        this.company = company;
    }
}
