package com.ciandt.pms.model;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Embeddable
public class GrupoCustoAreaOrcamentariaId implements Serializable {

    private static final long serialVersionUID = -2617534381945672220L;

    @Column(name = "GCAO_CD_GRUPO_CUSTO_AREA_ORCAM", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoGrupoCustoAreaOrcamentaria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REV", nullable = false, insertable = false, updatable = false)
    private PMSRevisionEntity revinfo;
}
