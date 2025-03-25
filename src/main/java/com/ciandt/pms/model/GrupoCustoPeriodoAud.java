package com.ciandt.pms.model;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Entity
@Table(name = "GRUPO_CUSTO_PERIODO_AUD")
@NamedQueries({
        @NamedQuery(name = "GrupoCustoPeriodoAud.findByCodigo", query = "SELECT t FROM GrupoCustoPeriodoAud t "
                + "WHERE t.grupoCusto.codigoGrupoCusto = ? ORDER BY t.id.revinfo.revtstmp")
})
public class GrupoCustoPeriodoAud implements Serializable {

    private static final long serialVersionUID = 6256841205718480333L;

    public static final String FIND_BY_CODIGO = "GrupoCustoPeriodoAud.findByCodigo";

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "codigoGcPeriodo", column = @Column(name = "GRCP_CD_GC_PERIODO", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "revinfo.rev", column = @Column(name = "REV", nullable = false, precision = 18, scale = 0))})
    private GrupoCustoPeriodoAudId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRCU_CD_GRUPO_CUSTO", nullable = false)
    private GrupoCusto grupoCusto;

    @Temporal(TemporalType.DATE)
    @Column(name = "GRCP_DT_INICIO", length = 7)
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "GRCP_DT_FIM", length = 7)
    private Date dataFim;

    @Transient
    private String type = "Profit Center";

    /*
     * Essas propriedades Transient não recebem dados, mas são
     * necessárias para a ordenação da tabela de history do cost center
     */
    @Transient
    private String nome;
    @Transient
    private String nomeDaArea;
    @Transient
    private String nomeAprovador;
    @Transient
    private String nomeGerenteAprovador;
    @Transient
    private Date dataDeInativacao;
    @Transient
    private String nomeStatus;
}
