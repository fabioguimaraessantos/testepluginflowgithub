package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MSA_CP")
@NamedQueries({
        @NamedQuery(name = "MsaCommercialPartner.findByMsa", query = "SELECT t FROM MsaCommercialPartner t"
                + " WHERE t.msa.codigoMsa = :msaCode")
})
public class MsaCommercialPartner implements Serializable {

    public static final String FIND_BY_MSA = "MsaCommercialPartner.findByMsa";


    @Id
    @GeneratedValue(generator = "MsaCpSeq")
    @SequenceGenerator(name = "MsaCpSeq", sequenceName = "SQ_MSCP_CD_MSA_CP", allocationSize = 1)
    @Column(name = "MSCP_CD_MSA_CP", unique = true, nullable = false, precision = 18)
    private Long code;

    @Column(name = "MSCP_TX_LOGIN", length = 20)
    private String login;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MSAA_CD_MSA")
    private Msa msa;


    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Msa getMsa() {
        return msa;
    }

    public void setMsa(Msa msa) {
        this.msa = msa;
    }
}
