/*
 * @(#) ItemTabPerPadrao.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity gerado a partir da tabela ITEM_TAB_PER_PADRAO.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 13/07/2012 12:48:37
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "ITEM_TAB_PER_PADRAO")
@NamedQueries({
        @NamedQuery(name = "ItemTabPerPadrao.findAll", query = "SELECT t FROM ItemTabPerPadrao t"),
        @NamedQuery(name = "ItemTabPerPadrao.findByTabelaPerfilPadrao", query = "SELECT t FROM ItemTabPerPadrao t JOIN FETCH t.perfilPadrao "
                + "WHERE t.tabelaPerfilPadrao.codigoTabPerfilPadrao = ?")})
public class ItemTabPerPadrao implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "ItemTabPerPadrao.findAll".
     */
    public static final String FIND_ALL = "ItemTabPerPadrao.findAll";

    /**
     * Constante para NamedQuery "ItemTabPerPadrao.findByTabelaPerfilPadrao".
     */
    public static final String FIND_BY_TABELA_PERFIL_PADRAO =
            "ItemTabPerPadrao.findByTabelaPerfilPadrao";

    /**
     * Atributo gerado a partir da coluna ITPP_CD_ITEM_TAB_PER_PADRAO.
     */
    @Id
    @GeneratedValue(generator = "ItemTabPerPadraoSeq")
    @SequenceGenerator(name = "ItemTabPerPadraoSeq", sequenceName = "SQ_ITPP_CD_ITEM_TAB_PER_PADRAO", allocationSize = 1)
    @Column(name = "ITPP_CD_ITEM_TAB_PER_PADRAO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoItemTabPerPadrao;

    /**
     * Atributo gerado a partir da coluna PEPA_CD_PERFIL_PADRAO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEPA_CD_PERFIL_PADRAO", nullable = false)
    private PerfilPadrao perfilPadrao;

    /**
     * Atributo gerado a partir da coluna TAPP_CD_TAB_PERFIL_PADRAO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAPP_CD_TAB_PERFIL_PADRAO", nullable = false)
    private TabelaPerfilPadrao tabelaPerfilPadrao;

    /**
     * Atributo gerado a partir da coluna ITPP_VL_PRECO_PADRAO.
     */

    @Column(name = "ITPP_VL_PRECO_PADRAO", precision = 22, scale = 0)
    private BigDecimal valorPrecoPadrao;

    /**
     * Construtor default.
     */
    public ItemTabPerPadrao() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoItemTabPerPadrao
     *            Valor do atributo codigoItemTabPerPadrao;
     * @param perfilPadrao
     *            Valor do atributo perfilPadrao;
     * @param tabelaPerfilPadrao
     *            Valor do atributo tabelaPerfilPadrao;
     */
    public ItemTabPerPadrao(Long codigoItemTabPerPadrao,
            PerfilPadrao perfilPadrao, TabelaPerfilPadrao tabelaPerfilPadrao) {
        this.codigoItemTabPerPadrao = codigoItemTabPerPadrao;
        this.perfilPadrao = perfilPadrao;
        this.tabelaPerfilPadrao = tabelaPerfilPadrao;
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoItemTabPerPadrao
     *            Valor do atributo codigoItemTabPerPadrao;
     * @param perfilPadrao
     *            Valor do atributo perfilPadrao;
     * @param tabelaPerfilPadrao
     *            Valor do atributo tabelaPerfilPadrao;
     * @param valorPrecoPadrao
     *            Valor do atributo valorPrecoPadrao;
     */
    public ItemTabPerPadrao(Long codigoItemTabPerPadrao,
            PerfilPadrao perfilPadrao, TabelaPerfilPadrao tabelaPerfilPadrao,
            BigDecimal valorPrecoPadrao) {
        this.codigoItemTabPerPadrao = codigoItemTabPerPadrao;
        this.perfilPadrao = perfilPadrao;
        this.tabelaPerfilPadrao = tabelaPerfilPadrao;
        this.valorPrecoPadrao = valorPrecoPadrao;
    }

    /**
     * Obtem o valor do atributo codigoItemTabPerPadrao.<BR>
     * Atributo gerado a partir da coluna ITPP_CD_ITEM_TAB_PER_PADRAO.
     * 
     * @return Valor do atributo codigoItemTabPerPadrao.
     */
    public Long getCodigoItemTabPerPadrao() {
        return this.codigoItemTabPerPadrao;
    }

    /**
     * Atualiza o valor do atributo codigoItemTabPerPadrao.<BR>
     * Atributo gerado a partir da coluna ITPP_CD_ITEM_TAB_PER_PADRAO.
     * 
     * @param codigoItemTabPerPadrao
     *            Novo valor para o atributo codigoItemTabPerPadrao.
     */
    public void setCodigoItemTabPerPadrao(final Long codigoItemTabPerPadrao) {
        this.codigoItemTabPerPadrao = codigoItemTabPerPadrao;
    }

    /**
     * Obtem o valor do atributo perfilPadrao.<BR>
     * Atributo gerado a partir da coluna PEPA_CD_PERFIL_PADRAO.
     * 
     * @return Valor do atributo perfilPadrao.
     */
    public PerfilPadrao getPerfilPadrao() {
        return this.perfilPadrao;
    }

    /**
     * Atualiza o valor do atributo perfilPadrao.<BR>
     * Atributo gerado a partir da coluna PEPA_CD_PERFIL_PADRAO.
     * 
     * @param perfilPadrao
     *            Novo valor para o atributo perfilPadrao.
     */
    public void setPerfilPadrao(final PerfilPadrao perfilPadrao) {
        this.perfilPadrao = perfilPadrao;
    }

    /**
     * Obtem o valor do atributo tabelaPerfilPadrao.<BR>
     * Atributo gerado a partir da coluna TAPP_CD_TAB_PERFIL_PADRAO.
     * 
     * @return Valor do atributo tabelaPerfilPadrao.
     */
    public TabelaPerfilPadrao getTabelaPerfilPadrao() {
        return this.tabelaPerfilPadrao;
    }

    /**
     * Atualiza o valor do atributo tabelaPerfilPadrao.<BR>
     * Atributo gerado a partir da coluna TAPP_CD_TAB_PERFIL_PADRAO.
     * 
     * @param tabelaPerfilPadrao
     *            Novo valor para o atributo tabelaPerfilPadrao.
     */
    public void setTabelaPerfilPadrao(
            final TabelaPerfilPadrao tabelaPerfilPadrao) {
        this.tabelaPerfilPadrao = tabelaPerfilPadrao;
    }

    /**
     * Obtem o valor do atributo valorPrecoPadrao.<BR>
     * Atributo gerado a partir da coluna ITPP_VL_PRECO_PADRAO.
     * 
     * @return Valor do atributo valorPrecoPadrao.
     */
    public BigDecimal getValorPrecoPadrao() {
        return this.valorPrecoPadrao;
    }

    /**
     * Atualiza o valor do atributo valorPrecoPadrao.<BR>
     * Atributo gerado a partir da coluna ITPP_VL_PRECO_PADRAO.
     * 
     * @param valorPrecoPadrao
     *            Novo valor para o atributo valorPrecoPadrao.
     */
    public void setValorPrecoPadrao(final BigDecimal valorPrecoPadrao) {
        this.valorPrecoPadrao = valorPrecoPadrao;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(
                Integer.toHexString(hashCode())).append(" [");
        buffer.append("codigoItemTabPerPadrao").append("='").append(
                getCodigoItemTabPerPadrao()).append("' ");
        buffer.append("perfilPadrao").append("='").append(getPerfilPadrao())
                .append("' ");
        buffer.append("tabelaPerfilPadrao").append("='").append(
                getTabelaPerfilPadrao()).append("' ");
        buffer.append("valorPrecoPadrao").append("='").append(
                getValorPrecoPadrao()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
