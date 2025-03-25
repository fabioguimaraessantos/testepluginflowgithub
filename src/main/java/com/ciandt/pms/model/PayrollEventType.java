package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by amanda on 31/01/17.
 */
@Entity
@Table(name = "PAYROLL_EVENT_TYPE")
@NamedQueries({
        @NamedQuery(name = PayrollEventType.FIND_ALL, query = "SELECT t FROM PayrollEventType t")
})
public class PayrollEventType implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "PayrollEventType.findAll";

    @Id
    @GeneratedValue(generator = "PayrollEventTypeSeq")
    @SequenceGenerator(name = "PayrollEventTypeSeq", sequenceName = "SQ_PAET_CD_PAYROLL_EVENT_TYPE", allocationSize = 1)
    @Column(name = "PAET_CD_PAYROLL_EVENT_TYPE")
    private Long code;

    @Column(name = "PAET_NM_PAYROLL_EVENT_TYPE")
    private String name;

    @Column(name = "PAET_SG_PAYROLL_EVENT_TYPE")
    private String acronym;

    public PayrollEventType() {
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

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getDescription() {
        return this.getAcronym() + " - " + this.getName();
    }

    @Override
    public String toString() {
        return "PayrollEventType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                '}';
    }
}
