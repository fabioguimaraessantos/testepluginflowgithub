package com.ciandt.pms.model;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;

@Entity
@Table(name = "MSA_CONTRATO_TIPO_SERVICO")
@Audited
@AuditTable(value="MSA_CONTRATO_TIPO_SERVICO_AUD")
@NamedQueries({
        @NamedQuery(name = "MsaContratoTipoServico.findByCodigoMsaContrato",
                query = "SELECT t FROM MsaContratoTipoServico t WHERE t.codigoMsaContrato = :msaContratoCode")
})
public class MsaContratoTipoServico {

    public static final String FIND_BY_MSA_CONTRATO_CODE = "MsaContratoTipoServico.findByCodigoMsaContrato";

    @Id
    @GeneratedValue(generator = "MsaContratoTipoServSeq")
    @SequenceGenerator(name = "MsaContratoTipoServSeq", sequenceName = "SQ_MSCO_CD_MSA_CON_TIPO_SERV", allocationSize = 1)
    @Column(name = "MSCO_CD_MSA_CON_TIPO_SERV", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigo;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_CD_MSA_CONTRATO")
    private Long codigoMsaContrato;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TISE_CD_TIPO_SERVICO")
    private TipoServico tipoServico;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigoMsaContrato() {
        return codigoMsaContrato;
    }

    public void setCodigoMsaContrato(Long codigoMsaContrato) {
        this.codigoMsaContrato = codigoMsaContrato;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }
}
