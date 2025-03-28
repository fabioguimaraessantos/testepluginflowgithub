/*
 * @(#) EtiquetaItemReceita.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity gerado a partir da tabela ETIQUETA_ITEM_RECEITA.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 21/12/2009 12:09:33
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "ETIQUETA_ITEM_RECEITA")
@NamedQueries({
        @NamedQuery(name = "EtiquetaItemReceita.findAll", query = "SELECT t FROM EtiquetaItemReceita t"),

        @NamedQuery(name = "EtiquetaItemReceita.findByEtiquetaAndReceita", 
                query = "SELECT eire FROM EtiquetaItemReceita eire "
                + "WHERE eire.id.codigoEtiqueta = ? "
                // TODO testar esse metodo
                + "AND eire.itemReceita.receitaMoeda.receita.codigoReceita = ? "),
                
        @NamedQuery(name = "EtiquetaItemReceita.findByItemReceita", 
                query = "SELECT eire FROM EtiquetaItemReceita eire "
                + "WHERE eire.itemReceita.codigoItemReceita = ? ") 
})                
                
public class EtiquetaItemReceita implements java.io.Serializable {

    // ========================================================================
    // BEGIN - Coloque aqui o codigo manual
    // ========================================================================

    /** Constante para NamedQuery "EtiquetaItemReceita.findByEtiquetaAndReceita". */
    public static final String FIND_BY_ETIQUETA_AND_RECEITA = "EtiquetaItemReceita.findByEtiquetaAndReceita";

    
    /** Constante para NamedQuery "EtiquetaItemReceita.findByItemReceita". */
    public static final String FIND_BY_ITEM_RECEITA = "EtiquetaItemReceita.findByItemReceita";
    
    /**
     * Realiza uma copia do objeto, porem com uma referencia diferente.
     * 
     * @return a c�pia do objeto
     */
    public EtiquetaItemReceita getClone() {
        try {
            EtiquetaItemReceita clone = (EtiquetaItemReceita) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ========================================================================
    // END
    // ========================================================================

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "EtiquetaItemReceita.findAll".
     */
    public static final String FIND_ALL = "EtiquetaItemReceita.findAll";

    /**
     * Atributo gerado a partir da coluna ITRE_CD_ITEM_RECEITA.
     */
    @EmbeddedId
    @GeneratedValue(generator = "EtiquetaItemReceitaSeq")
    @SequenceGenerator(name = "EtiquetaItemReceitaSeq", sequenceName = "SQ_ETIQ_CD_ETIQUETA", allocationSize = 1)
    @AttributeOverrides({
            @AttributeOverride(name = "codigoEtiqueta", 
                    column = @Column(name = "ETIQ_CD_ETIQUETA", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "codigoItemReceita", 
                    column = @Column(name = "ITRE_CD_ITEM_RECEITA", 
                            nullable = false, precision = 18, scale = 0)) })
    private EtiquetaItemReceitaId id;

    /**
     * Atributo gerado a partir da coluna ITRE_CD_ITEM_RECEITA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITRE_CD_ITEM_RECEITA", nullable = false, insertable = false, updatable = false)
    private ItemReceita itemReceita;

    /**
     * Atributo gerado a partir da coluna ETIQ_CD_ETIQUETA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ETIQ_CD_ETIQUETA", nullable = false, insertable = false, updatable = false)
    private Etiqueta etiqueta;

    /**
     * Construtor default.
     */
    public EtiquetaItemReceita() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param id
     *            Valor do atributo id;
     * @param itemReceita
     *            Valor do atributo itemReceita;
     * @param etiqueta
     *            Valor do atributo etiqueta;
     */
    public EtiquetaItemReceita(final EtiquetaItemReceitaId id,
            final ItemReceita itemReceita, final Etiqueta etiqueta) {
        this.id = id;
        this.itemReceita = itemReceita;
        this.etiqueta = etiqueta;
    }

    /**
     * Obtem o valor do atributo id.<BR>
     * Atributo gerado a partir da coluna ITRE_CD_ITEM_RECEITA.
     * 
     * @return Valor do atributo id.
     */
    public EtiquetaItemReceitaId getId() {
        return this.id;
    }

    /**
     * Atualiza o valor do atributo id.<BR>
     * Atributo gerado a partir da coluna ITRE_CD_ITEM_RECEITA.
     * 
     * @param id
     *            Novo valor para o atributo id.
     */
    public void setId(final EtiquetaItemReceitaId id) {
        this.id = id;
    }

    /**
     * Obtem o valor do atributo itemReceita.<BR>
     * Atributo gerado a partir da coluna ITRE_CD_ITEM_RECEITA.
     * 
     * @return Valor do atributo itemReceita.
     */
    public ItemReceita getItemReceita() {
        return this.itemReceita;
    }

    /**
     * Atualiza o valor do atributo itemReceita.<BR>
     * Atributo gerado a partir da coluna ITRE_CD_ITEM_RECEITA.
     * 
     * @param itemReceita
     *            Novo valor para o atributo itemReceita.
     */
    public void setItemReceita(final ItemReceita itemReceita) {
        this.itemReceita = itemReceita;
    }

    /**
     * Obtem o valor do atributo etiqueta.<BR>
     * Atributo gerado a partir da coluna ETIQ_CD_ETIQUETA.
     * 
     * @return Valor do atributo etiqueta.
     */
    public Etiqueta getEtiqueta() {
        return this.etiqueta;
    }

    /**
     * Atualiza o valor do atributo etiqueta.<BR>
     * Atributo gerado a partir da coluna ETIQ_CD_ETIQUETA.
     * 
     * @param etiqueta
     *            Novo valor para o atributo etiqueta.
     */
    public void setEtiqueta(final Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * @see Object#toString()
     * @return representa��o String do Objeto
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(
                Integer.toHexString(hashCode())).append(" [");
        buffer.append("id").append("='").append(getId()).append("' ");
        buffer.append("itemReceita").append("='").append(getItemReceita())
                .append("' ");
        buffer.append("etiqueta").append("='").append(getEtiqueta()).append(
                "' ");
        buffer.append("]");

        return buffer.toString();
    }

}