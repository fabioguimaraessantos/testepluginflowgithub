package com.ciandt.pms.model;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BM_DN")
@Audited
@AuditTable(value="BM_DN_AUD")
@NamedQueries({
        @NamedQuery(name = "Bmdn.findByFilter", query = "SELECT b FROM BmDn b "
                + "WHERE (UPPER(b.name) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
                + "AND ((b.inActive = ?) OR (? is null)) "
                + "ORDER BY b.name ASC"),

        @NamedQuery(name = "Bmdn.findByName", query = "SELECT b FROM BmDn b "
                + "WHERE (UPPER(b.name)) = TRIM(UPPER(?)) ")
})
public class BmDn implements java.io.Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "Bmdn.findByFilter".
     */
    public static final String FIND_BY_FILTER = "Bmdn.findByFilter";

    /**
     * Constante para NamedQuery "Bmdn.findByName".
     */
    public static final String FIND_BY_NAME = "Bmdn.findByName";

    @Id
    @GeneratedValue(generator = "BmDnSeq")
    @SequenceGenerator(name = "BmDnSeq", sequenceName = "SQ_BMDN_CD_BM_DN", allocationSize = 1)
    @Audited
    @Column(name = "BMDN_CD_BM_DN", unique = true, nullable = false, precision = 18 )
    private Long code;

    @Audited
    @Column(name = "BMDN_NM_BM_DN")
    private String name;

    @Audited
    @Column(name = "BMDN_IN_ACTIVE")
    private String inActive;

    @Audited
    @Column(name = "BMDN_DT_CREATED_AT")
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