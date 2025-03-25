package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MODELO")
@NamedQueries({
        @NamedQuery(name = Modelo.FIND_DEFAULT_MODEL,
                query = "SELECT t FROM Modelo t WHERE t.indicadorPadrao = 'Y'"),
        @NamedQuery(name = Modelo.FIND_ALL, query = "SELECT t FROM Modelo t"),
        @NamedQuery(name = Modelo.FIND_BY_NAME, query = "SELECT t FROM Modelo t WHERE t.nomeModelo = ?1")
})
public class Modelo implements Serializable {

    /** Constant to determine the NamedQuery */
    public static final String FIND_DEFAULT_MODEL = "Modelo.findDefaultModel";

    public static final String FIND_ALL = "Modelo.findAll";
    public static final String FIND_BY_NAME = "Modelo.findByName";
    @Id
    @GeneratedValue(generator = "modeloSeq")
    @SequenceGenerator(name = "modeloSeq", sequenceName = "SQ_MODE_CD_MODELO", allocationSize = 1)
    @Column(name = "MODE_CD_MODELO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoModelo;

    @Column(name = "MODE_NM_MODELO", nullable = false, length = 1000)
    private String nomeModelo;

    @Column(name = "MODE_IN_PADRAO", nullable = false, length = 1)
    private String indicadorPadrao;

    @Override
    public String toString() {
        return nomeModelo;
    }

    public Long getCodigoModelo() {
        return codigoModelo;
    }

    public void setCodigoModelo(Long codigoModelo) {
        this.codigoModelo = codigoModelo;
    }

    public String getNomeModelo() {
        return nomeModelo;
    }

    public void setNomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
    }

    public String getIndicadorPadrao() {
        return indicadorPadrao;
    }

    public void setIndicadorPadrao(String indicadorPadrao) {
        this.indicadorPadrao = indicadorPadrao;
    }
}
