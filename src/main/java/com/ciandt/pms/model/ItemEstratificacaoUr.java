/*
 * @(#) ItemEstratificacaoUr.java
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
import javax.persistence.Transient;

/**
 * Entity gerado a partir da tabela ITEM_ESTRATIFICACAO_UR.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 14/04/2011 15:56:16
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "ITEM_ESTRATIFICACAO_UR")
@NamedQueries({@NamedQuery(name = "ItemEstratificacaoUr.findAll", query = "SELECT t FROM ItemEstratificacaoUr t")})
public class ItemEstratificacaoUr implements java.io.Serializable, Cloneable {

    // ========================================================================
    // BEGIN - Coloque aqui o codigo manual
    // ========================================================================

    /** Indicador selecionado / nao selecionado. */
    @Transient
    private Boolean isSelected = Boolean.valueOf(false);

    /**
     * @return the isSelected
     */
    public Boolean getIsSelected() {
        return isSelected;
    }

    /**
     * @param isSelected
     *            the isSelected to set
     */
    public void setIsSelected(final Boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * Realiza uma copia do objeto, porem com uma referencia diferente.
     * 
     * @return a c�pia do FatorUrMes
     */
    public ItemEstratificacaoUr getClone() {
        try {
            return (ItemEstratificacaoUr) super.clone();
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
     * Constante para NamedQuery "ItemEstratificacaoUr.findAll".
     */
    public static final String FIND_ALL = "ItemEstratificacaoUr.findAll";

    /**
     * Atributo gerado a partir da coluna ITEU_CD_ITEM_ESTRAT_UR.
     */
    @Id
    @GeneratedValue(generator = "ItemEstratificacaoUrSeq")
    @SequenceGenerator(name = "ItemEstratificacaoUrSeq", sequenceName = "SQ_ITEU_CD_ITEM_ESTRAT_UR", allocationSize = 1)
    @Column(name = "ITEU_CD_ITEM_ESTRAT_UR", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoItemEstratUr;

    /**
     * Atributo gerado a partir da coluna PESS_CD_PESSOA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESS_CD_PESSOA", nullable = false)
    private Pessoa pessoa;

    /**
     * Atributo gerado a partir da coluna ESUR_CD_ESTRATIFICACAO_UR.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ESUR_CD_ESTRATIFICACAO_UR", nullable = false)
    private EstratificacaoUr estratificacaoUr;

    /**
     * Atributo gerado a partir da coluna EXDE_CD_MOTIVO_DESVIO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXDE_CD_MOTIVO_DESVIO")
    private ExplicacaoDesvio explicacaoDesvio;

    /**
     * Atributo gerado a partir da coluna ITEU_NR_FTE_ALOCADO.
     */

    @Column(name = "ITEU_NR_FTE_ALOCADO", precision = 22, scale = 0)
    private BigDecimal numeroFteAlocado;

    /**
     * Atributo gerado a partir da coluna ITEU_NR_FTE_RECEITADO.
     */

    @Column(name = "ITEU_NR_FTE_RECEITADO", precision = 22, scale = 0)
    private BigDecimal numeroFteReceitado;

    /**
     * Atributo gerado a partir da coluna ITEU_VL_DIFERENCA.
     */
    @Column(name = "ITEU_VL_DIFERENCA", precision = 22, scale = 0)
    private BigDecimal valorDiferenca;

    /**
     * Construtor default.
     */
    public ItemEstratificacaoUr() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoItemEstratUr
     *            Valor do atributo codigoItemEstratUr;
     * @param pessoa
     *            Valor do atributo pessoa;
     * @param estratificacaoUr
     *            Valor do atributo estratificacaoUr;
     * @param explicacaoDesvio
     *            Valor do atributo explicacaoDesvio;
     */
    public ItemEstratificacaoUr(final Long codigoItemEstratUr,
            final Pessoa pessoa, final EstratificacaoUr estratificacaoUr,
            final ExplicacaoDesvio explicacaoDesvio) {
        this.codigoItemEstratUr = codigoItemEstratUr;
        this.pessoa = pessoa;
        this.estratificacaoUr = estratificacaoUr;
        this.explicacaoDesvio = explicacaoDesvio;
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoItemEstratUr
     *            Valor do atributo codigoItemEstratUr;
     * @param pessoa
     *            Valor do atributo pessoa;
     * @param estratificacaoUr
     *            Valor do atributo estratificacaoUr;
     * @param explicacaoDesvio
     *            Valor do atributo explicacaoDesvio;
     * @param numeroFteAlocado
     *            Valor do atributo numeroFteAlocado;
     * @param numeroFteReceitado
     *            Valor do atributo numeroFteReceitado;
     */
    public ItemEstratificacaoUr(final Long codigoItemEstratUr,
            final Pessoa pessoa, final EstratificacaoUr estratificacaoUr,
            final ExplicacaoDesvio explicacaoDesvio,
            final BigDecimal numeroFteAlocado,
            final BigDecimal numeroFteReceitado) {
        this.codigoItemEstratUr = codigoItemEstratUr;
        this.pessoa = pessoa;
        this.estratificacaoUr = estratificacaoUr;
        this.explicacaoDesvio = explicacaoDesvio;
        this.numeroFteAlocado = numeroFteAlocado;
        this.numeroFteReceitado = numeroFteReceitado;
    }

    /**
     * Obtem o valor do atributo codigoItemEstratUr.<BR>
     * Atributo gerado a partir da coluna ITEU_CD_ITEM_ESTRAT_UR.
     * 
     * @return Valor do atributo codigoItemEstratUr.
     */
    public Long getCodigoItemEstratUr() {
        return this.codigoItemEstratUr;
    }

    /**
     * Atualiza o valor do atributo codigoItemEstratUr.<BR>
     * Atributo gerado a partir da coluna ITEU_CD_ITEM_ESTRAT_UR.
     * 
     * @param codigoItemEstratUr
     *            Novo valor para o atributo codigoItemEstratUr.
     */
    public void setCodigoItemEstratUr(final Long codigoItemEstratUr) {
        this.codigoItemEstratUr = codigoItemEstratUr;
    }

    /**
     * Obtem o valor do atributo pessoa.<BR>
     * Atributo gerado a partir da coluna PESS_CD_PESSOA.
     * 
     * @return Valor do atributo pessoa.
     */
    public Pessoa getPessoa() {
        return this.pessoa;
    }

    /**
     * Atualiza o valor do atributo pessoa.<BR>
     * Atributo gerado a partir da coluna PESS_CD_PESSOA.
     * 
     * @param pessoa
     *            Novo valor para o atributo pessoa.
     */
    public void setPessoa(final Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * Obtem o valor do atributo estratificacaoUr.<BR>
     * Atributo gerado a partir da coluna ESUR_CD_ESTRATIFICACAO_UR.
     * 
     * @return Valor do atributo estratificacaoUr.
     */
    public EstratificacaoUr getEstratificacaoUr() {
        return this.estratificacaoUr;
    }

    /**
     * Atualiza o valor do atributo estratificacaoUr.<BR>
     * Atributo gerado a partir da coluna ESUR_CD_ESTRATIFICACAO_UR.
     * 
     * @param estratificacaoUr
     *            Novo valor para o atributo estratificacaoUr.
     */
    public void setEstratificacaoUr(final EstratificacaoUr estratificacaoUr) {
        this.estratificacaoUr = estratificacaoUr;
    }

    /**
     * Obtem o valor do atributo explicacaoDesvio.<BR>
     * Atributo gerado a partir da coluna EXDE_CD_MOTIVO_DESVIO.
     * 
     * @return Valor do atributo explicacaoDesvio.
     */
    public ExplicacaoDesvio getExplicacaoDesvio() {
        return this.explicacaoDesvio;
    }

    /**
     * Atualiza o valor do atributo explicacaoDesvio.<BR>
     * Atributo gerado a partir da coluna EXDE_CD_MOTIVO_DESVIO.
     * 
     * @param explicacaoDesvio
     *            Novo valor para o atributo explicacaoDesvio.
     */
    public void setExplicacaoDesvio(final ExplicacaoDesvio explicacaoDesvio) {
        this.explicacaoDesvio = explicacaoDesvio;
    }

    /**
     * Obtem o valor do atributo numeroFteAlocado.<BR>
     * Atributo gerado a partir da coluna ITEU_NR_FTE_ALOCADO.
     * 
     * @return Valor do atributo numeroFteAlocado.
     */
    public BigDecimal getNumeroFteAlocado() {
        return this.numeroFteAlocado;
    }

    /**
     * Atualiza o valor do atributo numeroFteAlocado.<BR>
     * Atributo gerado a partir da coluna ITEU_NR_FTE_ALOCADO.
     * 
     * @param numeroFteAlocado
     *            Novo valor para o atributo numeroFteAlocado.
     */
    public void setNumeroFteAlocado(final BigDecimal numeroFteAlocado) {
        this.numeroFteAlocado = numeroFteAlocado;
    }

    /**
     * Obtem o valor do atributo numeroFteReceitado.<BR>
     * Atributo gerado a partir da coluna ITEU_NR_FTE_RECEITADO.
     * 
     * @return Valor do atributo numeroFteReceitado.
     */
    public BigDecimal getNumeroFteReceitado() {
        return this.numeroFteReceitado;
    }

    /**
     * Atualiza o valor do atributo numeroFteReceitado.<BR>
     * Atributo gerado a partir da coluna ITEU_NR_FTE_RECEITADO.
     * 
     * @param numeroFteReceitado
     *            Novo valor para o atributo numeroFteReceitado.
     */
    public void setNumeroFteReceitado(final BigDecimal numeroFteReceitado) {
        this.numeroFteReceitado = numeroFteReceitado;
    }

    /**
     * @return the valorDiferenca
     */
    public BigDecimal getValorDiferenca() {
        return valorDiferenca;
    }

    /**
     * @param valorDiferenca
     *            the valorDiferenca to set
     */
    public void setValorDiferenca(final BigDecimal valorDiferenca) {
        this.valorDiferenca = valorDiferenca;
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
        buffer.append("codigoItemEstratUr").append("='").append(
                getCodigoItemEstratUr()).append("' ");
        buffer.append("pessoa").append("='").append(getPessoa()).append("' ");
        buffer.append("estratificacaoUr").append("='").append(
                getEstratificacaoUr()).append("' ");
        buffer.append("explicacaoDesvio").append("='").append(
                getExplicacaoDesvio()).append("' ");
        buffer.append("numeroFteAlocado").append("='").append(
                getNumeroFteAlocado()).append("' ");
        buffer.append("numeroFteReceitado").append("='").append(
                getNumeroFteReceitado()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
