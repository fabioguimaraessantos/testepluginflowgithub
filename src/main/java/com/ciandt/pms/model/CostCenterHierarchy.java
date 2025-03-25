package com.ciandt.pms.model;

import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "COST_CENTER_HIERARCHY")
@Audited
@AuditTable(value = "COST_CENTER_HIERARCHY_AUD")
@NamedQueries({
        @NamedQuery(name = CostCenterHierarchy.FIND_BY_FILTER, query = "SELECT c from CostCenterHierarchy c "
                + "WHERE (UPPER(c.name) like '%'||TRIM(UPPER(:name))||'%' OR (:name is null)) "
                + "AND ((c.inActive = :inActive) OR (:inActive is null)) "
                + "ORDER BY c.name ASC"),
        @NamedQuery(name = CostCenterHierarchy.FIND_BY_NAME_OR_ORACLE_CODE, query = " " +
                " SELECT c FROM CostCenterHierarchy c " +
                " WHERE UPPER(c.name) = UPPER(:name) " +
                " OR    UPPER(c.oracleCode) = UPPER(:oracleCode) " +
                " ORDER BY c.name ASC "),
})
public class CostCenterHierarchy implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FIND_BY_FILTER = "CostCenterHierarchy.findByFilter";
    public static final String FIND_BY_NAME_OR_ORACLE_CODE = "CostCenterHierarchy.findByNameOrOracleCode";

    @Id
    @GeneratedValue(generator = "CostCenterHierarchySeq")
    @SequenceGenerator(name = "CostCenterHierarchySeq", sequenceName = "SQ_COCH_CD_COST_CENTER_HIERARCHY", allocationSize = 1)
    @Audited
    @Column(name = "COCH_CD_COST_CENTER_HIERARCHY", unique = true, nullable = false, precision = 18)
    private Long code;

    @Audited
    @Column(name = "COCH_NM_COST_CENTER_HIERARCHY", nullable = false, length = 100)
    private String name;

    @Audited
    @Type(type = "yes_no")
    @Column(name = "COCH_IN_ACTIVE", nullable = false, length = 1)
    private Boolean inActive;

    @Audited
    @Column(name = "COCH_DT_CREATED_AT", nullable = false)
    private Date createdAt;

    @Audited
    @Column(name = "COCH_DT_UPDATED_AT", nullable = false)
    private Date updatedAt;

    @Audited
    @Column(name = "COCH_CD_ORACLE", nullable = false, length = 30)
    private String oracleCode;

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

    public Boolean getInActive() {
        return this.inActive;
    }

    public void setInActive(final Boolean inActive) {
        this.inActive = inActive;
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

    public String getOracleCode() {
        return oracleCode;
    }

    public void setOracleCode(String oracleCode) {
        this.oracleCode = oracleCode;
    }
}
