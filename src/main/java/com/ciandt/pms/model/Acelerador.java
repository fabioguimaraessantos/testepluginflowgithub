/*
 * @(#) Acelerador.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.*;

/**
 * Entity gerado a partir da tabela ACELERADOR.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 05/10/2010 11:13:07
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "ACELERADOR")
@NamedQueries({
    @NamedQuery(name = "Acelerador.findAll", query = "SELECT t FROM Acelerador t"),
    
    @NamedQuery(name = "Acelerador.findAllActive", 
            query = "SELECT t FROM Acelerador t WHERE t.indicadorAtivo = 'A'")
})
public class Acelerador implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** Constante para NamedQuery "Acelerador.findAll". */
    public static final String FIND_ALL = "Acelerador.findAll";
    
    /** Constante para NamedQuery "Acelerador.findAll". */
    public static final String FIND_ALL_ACTIVE = "Acelerador.findAllActive";
    
    /** Atributo gerado a partir da coluna ACEL_CD_ACELERADOR. */
    @Id
    @GeneratedValue(generator = "AceleradorSeq")
    @SequenceGenerator(name = "AceleradorSeq", sequenceName = "SQ_ACEL_CD_ACELERADOR", allocationSize = 1)
    @Column(name = "ACEL_CD_ACELERADOR", unique = true, nullable = false, precision = 22, scale = 0)
    private Long codigoAcelerador;

    /** Atributo gerado a partir da coluna ACEL_NM_ACELERADOR. */

    @Column(name = "ACEL_NM_ACELERADOR", nullable = false, length = 100)
    private String nomeAcelerador;

    /** Atributo gerado a partir da coluna ACEL_SG_ACELERADOR. */

    @Column(name = "ACEL_SG_ACELERADOR", length = 10)
    private String siglaAcelerador;

    /** Atributo gerado a partir da coluna ACEL_IN_ATIVO. */
    @Column(name = "ACEL_IN_ATIVO", length = 1)
    private String indicadorAtivo;

    /**
     * Construtor default.
     */
    public Acelerador() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoAcelerador Valor do atributo codigoAcelerador;
     * @param nomeAcelerador Valor do atributo nomeAcelerador;
     */
    public Acelerador(final Long codigoAcelerador, final String nomeAcelerador) {
        this.codigoAcelerador = codigoAcelerador;
        this.nomeAcelerador = nomeAcelerador;
    }

    /**
     * Obtem o valor do atributo codigoAcelerador.<BR>
     * Atributo gerado a partir da coluna ACEL_CD_ACELERADOR.
     * @return Valor do atributo codigoAcelerador.
     */
    public Long getCodigoAcelerador() {
        return this.codigoAcelerador;
    }

    /**
     * Atualiza o valor do atributo codigoAcelerador.<BR>
     * Atributo gerado a partir da coluna ACEL_CD_ACELERADOR.
     * @param codigoAcelerador Novo valor para o atributo codigoAcelerador.
     */
    public void setCodigoAcelerador(final Long codigoAcelerador) {
        this.codigoAcelerador = codigoAcelerador;
    }

    /**
     * Obtem o valor do atributo nomeAcelerador.<BR>
     * Atributo gerado a partir da coluna ACEL_NM_ACELERADOR.
     * @return Valor do atributo nomeAcelerador.
     */
    public String getNomeAcelerador() {
        return this.nomeAcelerador;
    }

    /**
     * Atualiza o valor do atributo nomeAcelerador.<BR>
     * Atributo gerado a partir da coluna ACEL_NM_ACELERADOR.
     * @param nomeAcelerador Novo valor para o atributo nomeAcelerador.
     */
    public void setNomeAcelerador(final String nomeAcelerador) {
        this.nomeAcelerador = nomeAcelerador;
    }

    /**
     * Obtem o valor do atributo siglaAcelerador.<BR>
     * Atributo gerado a partir da coluna ACEL_SG_ACELERADOR.
     * @return Valor do atributo siglaAcelerador.
     */
    public String getSiglaAcelerador() {
        return this.siglaAcelerador;
    }

    /**
     * Atualiza o valor do atributo siglaAcelerador.<BR>
     * Atributo gerado a partir da coluna ACEL_SG_ACELERADOR.
     * @param siglaAcelerador Novo valor para o atributo siglaAcelerador.
     */
    public void setSiglaAcelerador(final String siglaAcelerador) {
        this.siglaAcelerador = siglaAcelerador;
    }

    /**
     * Obtem o valor do atributo indicadorAtivo.<BR>
     * Atributo gerado a partir da coluna ACEL_IN_ATIVO.
     * @return Valor do atributo indicadorAtivo.
     */
    public String getIndicadorAtivo() {
        return this.indicadorAtivo;
    }

    /**
     * Atualiza o valor do atributo indicadorAtivo.<BR>
     * Atributo gerado a partir da coluna ACEL_IN_ATIVO.
     * @param indicadorAtivo Novo valor para o atributo indicadorAtivo.
     */
    public void setIndicadorAtivo(final String indicadorAtivo) {
        this.indicadorAtivo = indicadorAtivo;
    }

    /**
     * @see Object#toString()
     * 
     * @return retorna a entidade no formato de string
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(
                Integer.toHexString(hashCode())).append(" [");
        buffer.append("codigoAcelerador").append("='").append(
                getCodigoAcelerador()).append("' ");
        buffer.append("nomeAcelerador").append("='")
                .append(getNomeAcelerador()).append("' ");
        buffer.append("siglaAcelerador").append("='").append(
                getSiglaAcelerador()).append("' ");
        buffer.append("indicadorAtivo").append("='")
                .append(getIndicadorAtivo()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
