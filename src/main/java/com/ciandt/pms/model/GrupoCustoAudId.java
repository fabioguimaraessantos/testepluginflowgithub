package com.ciandt.pms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Embeddable
public class GrupoCustoAudId implements Serializable {

    private static final long serialVersionUID = -2617534381945672220L;

    @Column(name = "GRCU_CD_GRUPO_CUSTO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoGrupoCusto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REV", nullable = false, insertable = false, updatable = false)
    private PMSRevisionEntity revinfo;
}
