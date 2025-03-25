package com.ciandt.pms.model;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Embeddable
public class GrupoCustoPeriodoAudId implements Serializable {

    private static final long serialVersionUID = -2617534381945672220L;

    @Column(name = "GRCP_CD_GC_PERIODO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoGcPeriodo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REV", nullable = false, insertable = false, updatable = false)
    private PMSRevisionEntity revinfo;

}
