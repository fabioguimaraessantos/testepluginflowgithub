package com.ciandt.pms.model;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
@Table(name = "MSA_CONTRATO_FILIAL")
@Audited
@AuditTable(value="MSA_CONTRATO_FILIAL_AUD")
@NamedQueries({
        @NamedQuery(name = "MsaContratoFilial.findByCodigoMsaContrato",
                query = "SELECT t FROM MsaContratoFilial t WHERE t.codigoMsaContrato = :codigoMsaContrato")
})
public class MsaContratoFilial {

    public static final String FIND_BY_MSA_CODE = "MsaContratoFilial.findByCodigoMsaContrato";

    @Id
    @GeneratedValue(generator = "MsaContratoFilialSeq")
    @SequenceGenerator(name = "MsaContratoFilialSeq", sequenceName = "SQ_MSCO_CD_MSA_CON_FILIAL", allocationSize = 1)
    @Column(name = "MSCO_CD_MSA_CON_FILIAL", unique = true, nullable = false, precision = 18, scale = 0)
    private long codigo;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_CD_MSA_CONTRATO")
    private Long codigoMsaContrato;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPR_CD_EMPRESA")
    private Empresa filial;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigoMsaContrato() {
        return codigoMsaContrato;
    }

    public void setCodigoMsaContrato(Long codigoMsaContrato) {
        this.codigoMsaContrato = codigoMsaContrato;
    }

    public Empresa getFilial() {
        return filial;
    }

    public void setFilial(Empresa filial) {
        this.filial = filial;
    }
}
