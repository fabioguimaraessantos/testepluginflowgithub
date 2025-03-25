package com.ciandt.pms.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MSA_TIPO_CONTRATO")
@NamedQueries({
        @NamedQuery(name = "MsaTipoContrato.findAllActive", query = "SELECT t FROM MsaTipoContrato t WHERE t.isActive = 'Y' ORDER BY t.nome"),
        @NamedQuery(name = "MsaTipoContrato.findActiveByAcronimo", query = "SELECT t FROM MsaTipoContrato t WHERE t.isActive = 'Y' and UPPER(t.acronimo) = UPPER(?)")
})
public class MsaTipoContrato implements Serializable {

    public static final String FIND_ALL_ACTIVE = "MsaTipoContrato.findAllActive";

    public static final String FIND_ACTIVE_BY_ACRONIMO = "MsaTipoContrato.findActiveByAcronimo";

    @Id
    @Column(name = "MSTC_CD_MSA_TIPO_CONTRATO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigo;

    @Column(name = "MSTC_NM_MSA_TIPO_CONTRATO", nullable = false, length = 100)
    private String nome;

    @Column(name = "MSTC_AC_MSA_TIPO_CONTRATO", nullable = false, length = 2)
    private String acronimo;

    @Column(name = "MSTC_IN_ATIVO", nullable = false, length = 1)
    @Type(type = "yes_no")
    private Boolean isActive;


    public MsaTipoContrato() {
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
