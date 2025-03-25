package com.ciandt.pms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "CIDADE_BASE_FILIAL")
@NamedQueries(
        @NamedQuery(name = CidadeBaseFilial.FIND_BY_FILTER, query = "" +
                " SELECT cbf from CidadeBaseFilial cbf " +
                " LEFT JOIN cbf.id.cidadeBase " +
                " LEFT JOIN cbf.id.empresa " +
                " WHERE ( cbf.id.cidadeBase = :cidadeBase OR :cidadeBase is null) " +
                " AND ( cbf.id.empresa = :empresa OR :empresa is null ) " +
                " AND ( cbf.empresaFilial = :empresaFilial OR :empresaFilial is null ) " +
                " AND ( cbf.empresaMatriz = :empresaMatriz OR :empresaMatriz is null ) "
        )
)
public class CidadeBaseFilial implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "CidadeBaseFilial.findAll";
    public static final String FIND_BY_FILTER = "CidadeBaseFilial.findByFilter";

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "cidadeBase.codigoCidadeBase", column = @Column(name = "CIBA_CD_CIDADE_BASE", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "empresa.codigoEmpresa", column = @Column(name = "EMPR_CD_EMPRESA", nullable = false, precision = 18, scale = 0))})
    private CidadeBaseFilialId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPR_CD_EMPRESA_FILIAL", referencedColumnName = "EMPR_CD_EMPRESA", nullable = false)
    private Empresa empresaFilial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPR_CD_EMPRESA_MATRIZ", referencedColumnName = "EMPR_CD_EMPRESA", nullable = false)
    private Empresa empresaMatriz;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CidadeBaseFilial that = (CidadeBaseFilial) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getEmpresaFilial(), that.getEmpresaFilial()) &&
                Objects.equals(getEmpresaMatriz(), that.getEmpresaMatriz());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
