package com.ciandt.pms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INTERCOMP_CONFIG")
@NamedQueries({
        @NamedQuery(name = "IntercompanyConfig.findByCompanies",
                query = "SELECT inco FROM IntercompanyConfig inco "
                + "WHERE inco.mainCompany.codigoEmpresa = :mainCompany "
                + "AND inco.interCompany.codigoEmpresa = :interCompany ")
        })
public class IntercompanyConfig {

    public static final String FIND_BY_COMPANIES = "IntercompanyConfig.findByCompanies";

    @Id
    @GeneratedValue(generator = "InterCompanyConfigSeq")
    @SequenceGenerator(name = "InterCompanyConfigSeq", sequenceName = "SQ_INT_CD_INTERCOMP_CONFIG", allocationSize = 1)
    @Column(name = "INCO_CD_INTERCOMP_CONFIG", unique = true, nullable = false, precision = 18, scale = 0)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPR_CD_EMPRESA_PRINCIPAL", nullable = false)
    private Empresa mainCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPR_CD_EMPRESA_INTERCOMPANY", nullable = false)
    private Empresa interCompany;

    @Column(name = "INCO_IN_ATIVO")
    @Type(type = "yes_no")
    private Boolean isActive;
}
