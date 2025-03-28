/*
 * @(#) Pmg.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity gerado a partir da tabela PMG.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 20/06/2012 18:42:49
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "PMG")
@NamedQueries({@NamedQuery(name = "Pmg.findAll", query = "SELECT t FROM Pmg t")})
public class Pmg implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Constante para NamedQuery "Pmg.findAll".
     */
    public static final String FIND_ALL = "Pmg.findAll";

    /**
     * Atributo gerado a partir da coluna PEMG_CD_PMG.
     */
    @Id
    @GeneratedValue(generator = "PmgSeq")
    @SequenceGenerator(name = "PmgSeq", sequenceName = "SQ_PEMG_CD_PMG", allocationSize = 1)
    @Column(name = "PEMG_CD_PMG", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoPmg;

    /**
     * Atributo gerado a partir da coluna PEMG_NM_PMG.
     */

    @Column(name = "PEMG_NM_PMG")
    private String nomePmg;

    /**
     * Atributo gerado a partir da coluna PEMG_IN_ATIVO.
     */

    @Column(name = "PEMG_IN_ATIVO", length = 1)
    private String indicadorAtivo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pmg")
    private List<PerfilPadrao> perfilPadraos = new ArrayList<PerfilPadrao>(0);

    /**
     * Construtor default.
     */
    public Pmg() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoPmg Valor do atributo codigoPmg;
     */
    public Pmg(Long codigoPmg) {
        this.codigoPmg = codigoPmg;
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoPmg Valor do atributo codigoPmg;
     * @param nomePmg Valor do atributo nomePmg;
     * @param indicadorAtivo Valor do atributo indicadorAtivo;
     * @param perfilPadraos Valor do atributo perfilPadraos;
     */
    public Pmg(Long codigoPmg, String nomePmg, String indicadorAtivo,
            List<PerfilPadrao> perfilPadraos) {
        this.codigoPmg = codigoPmg;
        this.nomePmg = nomePmg;
        this.indicadorAtivo = indicadorAtivo;
        this.perfilPadraos = perfilPadraos;
    }

    /**
     * Obtem o valor do atributo codigoPmg.<BR>
     * Atributo gerado a partir da coluna PEMG_CD_PMG.
     * @return Valor do atributo codigoPmg.
     */
    public Long getCodigoPmg() {
        return this.codigoPmg;
    }

    /**
     * Atualiza o valor do atributo codigoPmg.<BR>
     * Atributo gerado a partir da coluna PEMG_CD_PMG.
     * @param codigoPmg Novo valor para o atributo codigoPmg.
     */
    public void setCodigoPmg(final Long codigoPmg) {
        this.codigoPmg = codigoPmg;
    }

    /**
     * Obtem o valor do atributo nomePmg.<BR>
     * Atributo gerado a partir da coluna PEMG_NM_PMG.
     * @return Valor do atributo nomePmg.
     */
    public String getNomePmg() {
        return this.nomePmg;
    }

    /**
     * Atualiza o valor do atributo nomePmg.<BR>
     * Atributo gerado a partir da coluna PEMG_NM_PMG.
     * @param nomePmg Novo valor para o atributo nomePmg.
     */
    public void setNomePmg(final String nomePmg) {
        this.nomePmg = nomePmg;
    }

    /**
     * Obtem o valor do atributo indicadorAtivo.<BR>
     * Atributo gerado a partir da coluna PEMG_IN_ATIVO.
     * @return Valor do atributo indicadorAtivo.
     */
    public String getIndicadorAtivo() {
        return this.indicadorAtivo;
    }

    /**
     * Atualiza o valor do atributo indicadorAtivo.<BR>
     * Atributo gerado a partir da coluna PEMG_IN_ATIVO.
     * @param indicadorAtivo Novo valor para o atributo indicadorAtivo.
     */
    public void setIndicadorAtivo(final String indicadorAtivo) {
        this.indicadorAtivo = indicadorAtivo;
    }

    /**
     * Obtem o valor do atributo perfilPadraos.<BR>

     * @return Valor do atributo perfilPadraos.
     */
    public List<PerfilPadrao> getPerfilPadraos() {
        return this.perfilPadraos;
    }

    /**
     * Atualiza o valor do atributo perfilPadraos.<BR>

     * @param perfilPadraos Novo valor para o atributo perfilPadraos.
     */
    public void setPerfilPadraos(final List<PerfilPadrao> perfilPadraos) {
        this.perfilPadraos = perfilPadraos;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(
                Integer.toHexString(hashCode())).append(" [");
        buffer.append("codigoPmg").append("='").append(getCodigoPmg()).append(
                "' ");
        buffer.append("nomePmg").append("='").append(getNomePmg()).append("' ");
        buffer.append("indicadorAtivo").append("='")
                .append(getIndicadorAtivo()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
