package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CHARGEBACK_PESSOA_AUD")
@NamedQueries({
        @NamedQuery(name = "ChargebackPessoaAud.findAll", query = "SELECT t FROM ChargebackPessoaAud t"),
        @NamedQuery(name = "ChargebackPessoaAud.findByCodigo", query = "SELECT t FROM ChargebackPessoaAud t "
                + "WHERE t.id.codigoChargebackPess = :codigoChargebackPess ORDER BY t.id.revinfo.revtstmp"),
        @NamedQuery(name = "ChargebackPessoaAud.findByCodigoAndRevtype", query = "SELECT t FROM ChargebackPessoaAud t "
                + "WHERE t.id.codigoChargebackPess = :codigoChargebackPess "
                + "AND t.revType = ?2")})
public class ChargebackPessoaAud implements Serializable {

    private static final long serialVersionUID = 6256841205718480333L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "codigoChargebackPess", column = @Column(name = "CHPE_CD_CHARGEBACK_PESS", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "revinfo.rev", column = @Column(name = "REV", nullable = false, precision = 18, scale = 0))})
    private ChargebackPessoaAudId id;

    @Column(name = "REVTYPE", precision = 3, scale = 0)
    private Long revType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESS_CD_PESSOA", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIRE_CD_TI_RECURSO")
    private TiRecurso tiRecurso;

    @Temporal(TemporalType.DATE)
    @Column(name = "CHPE_DT_MES", length = 7)
    private Date dataMes;

    @Column(name = "CHPE_NR_UNIDADES", precision = 22)
    private BigDecimal numeroUnidades;

    @Column(name = "CHPE_IN_TIPO", length = 2)
    private String indicadorTipo;

    @Temporal(TemporalType.DATE)
    @Column(name = "CHPE_DT_FIM", length = 7)
    private Date dataFimVigencia;

    @Column(name = "CHPE_CD_LOGIN_CRIADOR", length = 50)
    private String codigoLoginCriador;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHPE_DT_CRIACAO", length = 7)
    private Date dataCriacao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CHPE_DT_ATUALIZACAO", length = 7)
    private Date dataAtualizacao;

    @Column(name = "CHPE_NM_TICKET_ATENDIMENTO", length = 100)
    private String ticketAtendimento;

}
