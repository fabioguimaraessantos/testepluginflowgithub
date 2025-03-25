package com.ciandt.pms.model;

import javax.persistence.*;

@Entity
@Table(name = "MSA_CONTRATO_CNPJ")
@NamedQueries({
        @NamedQuery(name = "MsaContratoCnpj.findByMsaContrato", query = "SELECT t FROM MsaContratoCnpj t where t.msaContrato.codigo = ? " )})
public class MsaContratoCnpj {

    public static final String FIND_BY_MSA_CONTRATO = "MsaContratoCnpj.findByMsaContrato";

    @Id
    @GeneratedValue(generator = "MsaContratoCnpjSeq")
    @SequenceGenerator(name = "MsaContratoCnpjSeq", sequenceName = "SQ_MSCC_CD_MSA_CONTRATO_CNPJ", allocationSize = 1)
    @Column(name = "MSCC_CD_MSA_CONTRATO_CNPJ")
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MSCO_CD_MSA_CONTRATO")
    private MsaContrato msaContrato;

    @Column(name = "MSCC_TX_CNPJ")
    private String msaContratoCnpj;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public MsaContrato getMsaContrato() {
        return msaContrato;
    }

    public void setMsaContrato(MsaContrato msaContrato) {
        this.msaContrato = msaContrato;
    }

    public String getMsaContratoCnpj() {
        return msaContratoCnpj;
    }

    public void setMsaContratoCnpj(String msaContratoCnpj) {
        this.msaContratoCnpj = msaContratoCnpj;
    }
}
