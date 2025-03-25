/*
 * @(#) PadraoArquivo.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity gerado a partir da tabela PADRAO_ARQUIVO.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 19/08/2010 10:27:23
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "PADRAO_ARQUIVO")
@NamedQueries({
    @NamedQuery(name = "PadraoArquivo.findAll", query = "SELECT t FROM PadraoArquivo t") })
public class PadraoArquivo implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Constante para NamedQuery "PadraoArquivo.findAll".
     */
    public static final String FIND_ALL = "PadraoArquivo.findAll";

    /**
     * Atributo gerado a partir da coluna PAAR_CD_PADRAO_ARQUIVO.
     */
    @Id
    @GeneratedValue(generator = "PadraoArquivoSeq")
    @SequenceGenerator(name = "PadraoArquivoSeq", sequenceName = "SQ_PAAR_CD_PADRAO_ARQUIVO", allocationSize = 1)
    @Column(name = "PAAR_CD_PADRAO_ARQUIVO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoPadraoArquivo;

    /**
     * Atributo gerado a partir da coluna PAAR_NM_PADRAO_ARQUIVO.
     */

    @Column(name = "PAAR_NM_PADRAO_ARQUIVO", nullable = false, length = 100)
    private String nomePadraoArquivo;

    /**
     * Atributo gerado a partir da coluna PAAR_TX_AJUDA.
     */

    @Column(name = "PAAR_TX_AJUDA", length = 200)
    private String textoAjuda;

    /**
     * Atributo gerado a partir da coluna PAAR_TX_DELIMITADOR_STRING.
     */

    @Column(name = "PAAR_TX_DELIMITADOR_STRING", length = 1)
    private Character textoDelimitadorString;

    /**
     * Atributo gerado a partir da coluna PAAR_TX_DELIMITADOR_COLUNA.
     */
    @Column(name = "PAAR_TX_DELIMITADOR_COLUNA", length = 1)
    private Character textoDelimitadorColuna;


    /**
     * Atributo gerado a partir da tabela UPLOAD_ARQUIVO.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "padraoArquivo")
    private Set<UploadArquivo> uploadArquivos = new HashSet<UploadArquivo>(0);

    /**
     * Construtor default.
     */
    public PadraoArquivo() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoPadraoArquivo Valor do atributo codigoPadraoArquivo;
     * @param nomePadraoArquivo Valor do atributo nomePadraoArquivo;
     */
    public PadraoArquivo(final Long codigoPadraoArquivo, 
            final String nomePadraoArquivo) {
        this.codigoPadraoArquivo = codigoPadraoArquivo;
        this.nomePadraoArquivo = nomePadraoArquivo;
    }

    /**
     * Obtem o valor do atributo codigoPadraoArquivo.<BR>
     * Atributo gerado a partir da coluna PAAR_CD_PADRAO_ARQUIVO.
     * @return Valor do atributo codigoPadraoArquivo.
     */
    public Long getCodigoPadraoArquivo() {
        return this.codigoPadraoArquivo;
    }

    /**
     * Atualiza o valor do atributo codigoPadraoArquivo.<BR>
     * Atributo gerado a partir da coluna PAAR_CD_PADRAO_ARQUIVO.
     * @param codigoPadraoArquivo Novo valor para o atributo codigoPadraoArquivo.
     */
    public void setCodigoPadraoArquivo(final Long codigoPadraoArquivo) {
        this.codigoPadraoArquivo = codigoPadraoArquivo;
    }

    /**
     * Obtem o valor do atributo nomePadraoArquivo.<BR>
     * Atributo gerado a partir da coluna PAAR_NM_PADRAO_ARQUIVO.
     * @return Valor do atributo nomePadraoArquivo.
     */
    public String getNomePadraoArquivo() {
        return this.nomePadraoArquivo;
    }

    /**
     * Atualiza o valor do atributo nomePadraoArquivo.<BR>
     * Atributo gerado a partir da coluna PAAR_NM_PADRAO_ARQUIVO.
     * @param nomePadraoArquivo Novo valor para o atributo nomePadraoArquivo.
     */
    public void setNomePadraoArquivo(final String nomePadraoArquivo) {
        this.nomePadraoArquivo = nomePadraoArquivo;
    }

    /**
     * Obtem o valor do atributo textoAjuda.<BR>
     * Atributo gerado a partir da coluna PAAR_TX_AJUDA.
     * @return Valor do atributo textoAjuda.
     */
    public String getTextoAjuda() {
        return this.textoAjuda;
    }

    /**
     * Atualiza o valor do atributo textoAjuda.<BR>
     * Atributo gerado a partir da coluna PAAR_TX_AJUDA.
     * @param textoAjuda Novo valor para o atributo textoAjuda.
     */
    public void setTextoAjuda(final String textoAjuda) {
        this.textoAjuda = textoAjuda;
    }

    /**
     * Obtem o valor do atributo textoDelimitadorString.<BR>
     * Atributo gerado a partir da coluna PAAR_TX_DELIMITADOR_STRING.
     * @return Valor do atributo textoDelimitadorString.
     */
    public Character getTextoDelimitadorString() {
        return this.textoDelimitadorString;
    }

    /**
     * Atualiza o valor do atributo textoDelimitadorString.<BR>
     * Atributo gerado a partir da coluna PAAR_TX_DELIMITADOR_STRING.
     * @param textoDelimitadorString Novo valor para o atributo textoDelimitadorString.
     */
    public void setTextoDelimitadorString(final Character textoDelimitadorString) {
        this.textoDelimitadorString = textoDelimitadorString;
    }

    /**
     * Obtem o valor do atributo textoDelimitadorColuna.<BR>
     * Atributo gerado a partir da coluna PAAR_TX_DELIMITADOR_COLUNA.
     * @return Valor do atributo textoDelimitadorColuna.
     */
    public Character getTextoDelimitadorColuna() {
        return this.textoDelimitadorColuna;
    }

    /**
     * Atualiza o valor do atributo textoDelimitadorColuna.<BR>
     * Atributo gerado a partir da coluna PAAR_TX_DELIMITADOR_COLUNA.
     * @param textoDelimitadorColuna Novo valor para o atributo textoDelimitadorColuna.
     */
    public void setTextoDelimitadorColuna(final Character textoDelimitadorColuna) {
        this.textoDelimitadorColuna = textoDelimitadorColuna;
    }

    /**
     * Obtem o valor do atributo uploadArquivos.<BR>

     * @return Valor do atributo uploadArquivos.
     */
    public Set<UploadArquivo> getUploadArquivos() {
        return this.uploadArquivos;
    }

    /**
     * Atualiza o valor do atributo uploadArquivos.<BR>

     * @param uploadArquivos Novo valor para o atributo uploadArquivos.
     */
    public void setUploadArquivos(final Set<UploadArquivo> uploadArquivos) {
        this.uploadArquivos = uploadArquivos;
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
        buffer.append("codigoPadraoArquivo").append("='").append(
                getCodigoPadraoArquivo()).append("' ");
        buffer.append("nomePadraoArquivo").append("='").append(
                getNomePadraoArquivo()).append("' ");
        buffer.append("textoAjuda").append("='").append(getTextoAjuda())
                .append("' ");
        buffer.append("textoDelimitadorString").append("='").append(
                getTextoDelimitadorString()).append("' ");
        buffer.append("textoDelimitadorColuna").append("='").append(
                getTextoDelimitadorColuna()).append("' ");
        
        buffer.append("]");

        return buffer.toString();
    }

}
