package com.ciandt.pms.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MSA_STATUS_CONTRATO")
@NamedQueries({
        @NamedQuery(name = "MsaStatusContrato.findAllActive", query = "SELECT t FROM MsaStatusContrato t WHERE t.isActive = 'Y' ORDER BY t.nome"),
        @NamedQuery(name = "MsaStatusContrato.findByName", query = "SELECT t FROM MsaStatusContrato t WHERE UPPER(t.nome) = UPPER(?)")
})
public class MsaStatusContrato implements Serializable {

    public static final String FIND_ALL_ACTIVE = "MsaStatusContrato.findAllActive";
    public static final String FIND_BY_NAME = "MsaStatusContrato.findByName";

    @Id
    @Column(name = "MSSC_CD_MSA_STATUS_CONTR", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigo;

    @Column(name = "MSSC_NM_MSA_STATUS_CONTR", nullable = false, length = 100)
    private String nome;

    @Column(name = "MSSC_IN_ATIVO", nullable = false, length = 1)
    @Type(type = "yes_no")
    private Boolean isActive;

    @Column(name = "MSSC_IN_DEFAULT_PESQUISA")
    @Type(type = "yes_no")
    private Boolean indicadorDefaultPesquisa;


    public MsaStatusContrato() {
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getIndicadorDefaultPesquisa() { return indicadorDefaultPesquisa;  }

    public void setIndicadorDefaultPesquisa(Boolean indicadorDefaultPesquisa) { this.indicadorDefaultPesquisa = indicadorDefaultPesquisa; }
}
