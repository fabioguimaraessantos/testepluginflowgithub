package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MSA_CHARGE_METHOD")
@NamedQueries({
        @NamedQuery(name = MsaChargeMethod.FIND_ALL, query = "SELECT t FROM MsaChargeMethod t")
})
public class MsaChargeMethod implements Serializable {

    public static final String FIND_ALL = "MsaChargeMethod.getAll";

    @Id
    @Column(name = "MSCM_CD_MSA_CHARGE_METHOD", nullable = false, unique = true, precision = 18)
    private Long code;

    @Column(name = "MSCM_TX_CHARGE_METHOD", length = 20)
    private String name;


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
}
