package com.ciandt.pms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Embeddable
public class CidadeBaseFilialId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CIBA_CD_CIDADE_BASE", referencedColumnName = "CIBA_CD_CIDADE_BASE", nullable = false)
    private CidadeBase cidadeBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPR_CD_EMPRESA", referencedColumnName = "EMPR_CD_EMPRESA", nullable = false)
    private Empresa empresa;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CidadeBaseFilialId that = (CidadeBaseFilialId) o;
        return (Objects.equals(getCidadeBase(), that.getCidadeBase()) && Objects.equals(getEmpresa(), that.getEmpresa())) ||
                (getCidadeBase() != null && that.getCidadeBase() != null &&
                        Objects.equals(getCidadeBase().getCodigoCidadeBase(), that.getCidadeBase().getCodigoCidadeBase()) &&
                        getEmpresa() != null && that.getEmpresa() != null &&
                        Objects.equals(getEmpresa().getCodigoEmpresa(), that.getEmpresa().getCodigoEmpresa()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(cidadeBase, empresa);
    }
}
