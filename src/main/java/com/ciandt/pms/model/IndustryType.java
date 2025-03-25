package com.ciandt.pms.model;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "INDUSTRY_TYPE")
@Audited
@AuditTable(value="INDUSTRY_TYPE_AUD")
@NamedQueries({
        @NamedQuery(name = "IndustryType.findByFilter", query = "SELECT it FROM IndustryType it "
                + "WHERE (UPPER(it.name) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
                + "AND ((it.inActive = ?) OR (? is null)) "
                + "ORDER BY it.name ASC"),

        @NamedQuery(name = "IndustryType.findByName", query = "SELECT it FROM IndustryType it "
                + "WHERE (UPPER(it.name)) = TRIM(UPPER(?)) ")
})
public class IndustryType implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "IndustryType.findByFilter".
     */
    public static final String FIND_BY_FILTER = "IndustryType.findByFilter";

    /**
     * Constante para NamedQuery "IndustryType.findByName".
     */
    public static final String FIND_BY_NAME = "IndustryType.findByName";

    @Id
    @GeneratedValue(generator = "IndustryTypeSeq")
    @SequenceGenerator(name = "IndustryTypeSeq", sequenceName = "SQ_INTY_CD_INDUSTRY_TYPE", allocationSize = 1)
    @Audited
    @Column(name = "INTY_CD_INDUSTRY_TYPE", unique = true, nullable = false, precision = 18)
    private Long code;

    @Audited
    @Column(name = "INTY_NM_INDUSTRY_TYPE")
    private String name;

    @Audited
    @Column(name = "INTY_IN_ACTIVE")
    private String inActive;

    @Audited
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INTY_DT_CREATED_AT")
    private Date createdAt;

    /* Getters and Setters */
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

    public String getInActive() {
        return inActive;
    }

    public void setInActive(String inActive) {
        this.inActive = inActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}