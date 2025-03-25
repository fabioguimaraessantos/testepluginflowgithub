package com.ciandt.pms.model;


import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Entity
@Table(name = "GRUPO_CUSTO_AREA_ORCAMENTARIA_AUD")
@NamedQueries({
        @NamedQuery(name = "GrupoCustoAreaOrcamentariaAud.findByCodigo", query = "SELECT t FROM GrupoCustoAreaOrcamentariaAud t "
                + "WHERE t.grupoCusto.codigoGrupoCusto = ? ORDER BY t.id.revinfo.revtstmp")
})
public class GrupoCustoAreaOrcamentariaAud implements Serializable {

    private static final long serialVersionUID = 6256841205718480333L;

    public static final String FIND_BY_CODIGO = "GrupoCustoAreaOrcamentariaAud.findByCodigo";

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "codigoGrupoCustoAreaOrcamentaria", column = @Column(name = "GCAO_CD_GRUPO_CUSTO_AREA_ORCAM", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "revinfo.rev", column = @Column(name = "REV", nullable = false, precision = 18, scale = 0))})
    private GrupoCustoAreaOrcamentariaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRCU_CD_GRUPO_CUSTO", nullable = false)
    private GrupoCusto grupoCusto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AROR_CD_AREA_ORCAMENTARIA", nullable = false)
    private AreaOrcamentaria areaOrcamentaria;

    @Temporal(TemporalType.DATE)
    @Column(name = "GCAO_DT_INICIO")
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "GCAO_DT_FIM")
    private Date dataFim;

    @Transient
    private String type = "Budget Area";

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
