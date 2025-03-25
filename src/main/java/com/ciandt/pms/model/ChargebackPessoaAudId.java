package com.ciandt.pms.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@RequiredArgsConstructor
@Setter
@Getter
@Embeddable
public class ChargebackPessoaAudId implements Serializable {

    private static final long serialVersionUID = -2617534381945672220L;

    @Column(name = "CHPE_CD_CHARGEBACK_PESS", unique = true, nullable = false, precision = 18)
    private Long codigoChargebackPess;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REV", nullable = false, insertable = false, updatable = false)
    private PMSRevisionEntity revinfo;
}
