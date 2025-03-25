package com.ciandt.pms.model;

import com.ciandt.pms.model.vo.IHistoricoGrupoCusto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "GRUPO_CUSTO_AUD")
@NamedQueries({
        @NamedQuery(name = "GrupoCustoAud.findByCodigo", query = "SELECT t FROM GrupoCustoAud t "
                + "WHERE t.id.codigoGrupoCusto = ? ORDER BY t.id.revinfo.revtstmp")
})
public class GrupoCustoAud implements Serializable, IHistoricoGrupoCusto {

    private static final long serialVersionUID = 6256841205718480333L;

    public static final String FIND_BY_CODIGO = "GrupoCustoAud.findByCodigo";

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "codigoGrupoCusto", column = @Column(name = "GRCU_CD_GRUPO_CUSTO", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "revinfo.rev", column = @Column(name = "REV", nullable = false, precision = 18, scale = 0))})
    private GrupoCustoAudId id;

    @Column(name = "REVTYPE", precision = 3, scale = 0)
    private Long revType;

    @Column(name = "GRCU_NM_GRUPO_CUSTO", length = 100)
    private String nomeGrupoCusto;

    @Column(name = "GRCU_SG_GRUPO_CUSTO", length = 10)
    private String siglaGrupoCusto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AREA_CD_AREA")
    private Area area;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PESS_CD_APROVADOR")
    private Pessoa aprovador;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PESS_CD_GERENTE")
    private Pessoa gerenteAprovador;

    @Column(name = "GRCU_IN_DELETE_LOGICO")
    private String indicadorDeletado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPR_CD_EMPRESA")
    private Empresa empresa;

    @Temporal(TemporalType.DATE)
    @Column(name = "GRCU_DT_INATIVACAO", length = 7, nullable = true)
    private Date dataInativacao;

    @Column(name = "ERP_CD_CCUSTO", nullable = true)
    private Long erpCodigoCentroCusto;

    @Column(name = "ERP_CD_PAD_CCUSTO", nullable = true)
    private Long erpCentroCustoPadrao;

    @Column(name = "GRCU_TX_DESCRICAO")
    private String descricaoGrupoCusto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRCS_CD_GRUPO_CUSTO_STATUS", nullable = false)
    private GrupoCustoStatus grupoCustoStatus;

    @Transient
    private String type = "GC";

    @Override
    public String getNome() {
        return this.getNomeGrupoCusto();
    }

    @Override
    public String getNomeDaArea() {
        return this.getArea() != null ? this.getArea().getNomeArea() : "-";
    }

    @Override
    public String getNomeAprovador() {
        return this.getAprovador() != null ?
                this.getAprovador().getCodigoLogin() : "-";
    }

    @Override
    public String getNomeGerenteAprovador() {
        return this.getGerenteAprovador() != null ?
                this.getGerenteAprovador().getCodigoLogin() : "-";
    }

    @Override
    public Date getDataDeInativacao() {
        return this.getDataInativacao();
    }

    @Override
    public String getNomeStatus() {
        return this.getGrupoCustoStatus().getNomeStatus();
    }

}
