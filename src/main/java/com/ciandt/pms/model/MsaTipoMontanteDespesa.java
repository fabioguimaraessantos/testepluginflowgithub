package com.ciandt.pms.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MSA_TIPO_MONTANTE_DESPESA")
@NamedQueries({
        @NamedQuery(name = "MsaTipoMontanteDespesa.findAllActive", query = "SELECT t FROM MsaTipoMontanteDespesa t WHERE t.isActive = 'Y' ORDER BY t.name"),
        @NamedQuery(name = "MsaTipoMontanteDespesa.findByName", query = "SELECT t FROM MsaTipoMontanteDespesa t  WHERE UPPER(t.name) = UPPER(?)")
})
public class MsaTipoMontanteDespesa implements Serializable {

    public static final String FIND_ALL_ACTIVE = "MsaTipoMontanteDespesa.findAllActive";
    public static final String FIND_BY_NAME = "MsaTipoMontanteDespesa.findByName";
    @Id
    @Column(name = "MTMD_CD_MSA_TIPO_MONT_DESP", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigo;

    @Column(name = "MTMD_NM_MSA_TIPO_MONT_DESP", length = 100, nullable = false)
    private String name;

    @Column(name = "MTMD_IN_ATIVO", length = 1, nullable = false)
    @Type(type = "yes_no")
    private Boolean isActive;


    public MsaTipoMontanteDespesa() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
