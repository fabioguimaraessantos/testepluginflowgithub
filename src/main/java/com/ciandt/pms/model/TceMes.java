/*
 * @(#) TceMes.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity gerado a partir da tabela TCE_MES.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 23/07/2010 15:55:11
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "TCE_MES")
@NamedQueries({
    
    @NamedQuery(name = "TceMes.findAll", query = "SELECT t FROM TceMes t")
    
})
public class TceMes implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Constante para NamedQuery "TceMes.findAll".
     */
    public static final String FIND_ALL = "TceMes.findAll";

    /**
     * Atributo gerado a partir da coluna TCME_CD_TCE_MES.
     */
    @Id
    @GeneratedValue(generator = "TceMesSeq")
    @SequenceGenerator(name = "TceMesSeq", sequenceName = "SQ_TCME_CD_TCE_MES", allocationSize = 1)
    @Column(name = "TCME_CD_TCE_MES", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoTceMes;

    /**
     * Atributo gerado a partir da coluna PESS_CD_PESSOA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESS_CD_PESSOA", nullable = false)
    private Pessoa pessoa;

    /**
     * Atributo gerado a partir da coluna MOED_CD_MOEDA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOED_CD_MOEDA", nullable = false)
    private Moeda moeda;

    /**
     * Atributo gerado a partir da coluna TICO_CD_TIPO_CONTRATO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TICO_CD_TIPO_CONTRATO", nullable = false)
    private TipoContrato tipoContrato;

    /**
     * Atributo gerado a partir da coluna TCME_DT_TCE_MES.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TCME_DT_TCE_MES", length = 7)
    private Date dataTceMes;

    /**
     * Atributo gerado a partir da coluna TCME_VL_TCE_MES.
     */

    @Column(name = "TCME_VL_TCE_MES", precision = 22, scale = 0)
    private BigDecimal valorTceMes;

    /**
     * Atributo gerado a partir da coluna TCME_VL_TAXA.
     */

    @Column(name = "TCME_VL_TAXA", precision = 22, scale = 0)
    private BigDecimal valorTaxa;

    /**
     * Construtor default.
     */
    public TceMes() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoTceMes Valor do atributo codigoTceMes;
     * @param pessoa Valor do atributo pessoa;
     * @param moeda Valor do atributo moeda;
     * @param tipoContrato Valor do atributo tipoContrato;
     */
    public TceMes(final Long codigoTceMes, final Pessoa pessoa, final Moeda moeda,
            final TipoContrato tipoContrato) {
        this.codigoTceMes = codigoTceMes;
        this.pessoa = pessoa;
        this.moeda = moeda;
        this.tipoContrato = tipoContrato;
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoTceMes Valor do atributo codigoTceMes;
     * @param pessoa Valor do atributo pessoa;
     * @param moeda Valor do atributo moeda;
     * @param tipoContrato Valor do atributo tipoContrato;
     * @param dataTceMes Valor do atributo dataTceMes;
     * @param valorTceMes Valor do atributo valorTceMes;
     * @param valorTaxa Valor do atributo valorTaxa;
     */
    public TceMes(final Long codigoTceMes, final Pessoa pessoa, final Moeda moeda,
            final TipoContrato tipoContrato, final Date dataTceMes, final BigDecimal valorTceMes,
            final BigDecimal valorTaxa) {
        this.codigoTceMes = codigoTceMes;
        this.pessoa = pessoa;
        this.moeda = moeda;
        this.tipoContrato = tipoContrato;
        this.dataTceMes = dataTceMes;
        this.valorTceMes = valorTceMes;
        this.valorTaxa = valorTaxa;
    }

    /**
     * Obtem o valor do atributo codigoTceMes.<BR>
     * Atributo gerado a partir da coluna TCME_CD_TCE_MES.
     * @return Valor do atributo codigoTceMes.
     */
    public Long getCodigoTceMes() {
        return this.codigoTceMes;
    }

    /**
     * Atualiza o valor do atributo codigoTceMes.<BR>
     * Atributo gerado a partir da coluna TCME_CD_TCE_MES.
     * @param codigoTceMes Novo valor para o atributo codigoTceMes.
     */
    public void setCodigoTceMes(final Long codigoTceMes) {
        this.codigoTceMes = codigoTceMes;
    }

    /**
     * Obtem o valor do atributo pessoa.<BR>
     * Atributo gerado a partir da coluna PESS_CD_PESSOA.
     * @return Valor do atributo pessoa.
     */
    public Pessoa getPessoa() {
        return this.pessoa;
    }

    /**
     * Atualiza o valor do atributo pessoa.<BR>
     * Atributo gerado a partir da coluna PESS_CD_PESSOA.
     * @param pessoa Novo valor para o atributo pessoa.
     */
    public void setPessoa(final Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * Obtem o valor do atributo moeda.<BR>
     * Atributo gerado a partir da coluna MOED_CD_MOEDA.
     * @return Valor do atributo moeda.
     */
    public Moeda getMoeda() {
        return this.moeda;
    }

    /**
     * Atualiza o valor do atributo moeda.<BR>
     * Atributo gerado a partir da coluna MOED_CD_MOEDA.
     * @param moeda Novo valor para o atributo moeda.
     */
    public void setMoeda(final Moeda moeda) {
        this.moeda = moeda;
    }

    /**
     * Obtem o valor do atributo tipoContrato.<BR>
     * Atributo gerado a partir da coluna TICO_CD_TIPO_CONTRATO.
     * @return Valor do atributo tipoContrato.
     */
    public TipoContrato getTipoContrato() {
        return this.tipoContrato;
    }

    /**
     * Atualiza o valor do atributo tipoContrato.<BR>
     * Atributo gerado a partir da coluna TICO_CD_TIPO_CONTRATO.
     * @param tipoContrato Novo valor para o atributo tipoContrato.
     */
    public void setTipoContrato(final TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    /**
     * Obtem o valor do atributo dataTceMes.<BR>
     * Atributo gerado a partir da coluna TCME_DT_TCE_MES.
     * @return Valor do atributo dataTceMes.
     */
    public Date getDataTceMes() {
        return this.dataTceMes;
    }

    /**
     * Atualiza o valor do atributo dataTceMes.<BR>
     * Atributo gerado a partir da coluna TCME_DT_TCE_MES.
     * @param dataTceMes Novo valor para o atributo dataTceMes.
     */
    public void setDataTceMes(final Date dataTceMes) {
        this.dataTceMes = dataTceMes;
    }

    /**
     * Obtem o valor do atributo valorTceMes.<BR>
     * Atributo gerado a partir da coluna TCME_VL_TCE_MES.
     * @return Valor do atributo valorTceMes.
     */
    public BigDecimal getValorTceMes() {
        return this.valorTceMes;
    }

    /**
     * Atualiza o valor do atributo valorTceMes.<BR>
     * Atributo gerado a partir da coluna TCME_VL_TCE_MES.
     * @param valorTceMes Novo valor para o atributo valorTceMes.
     */
    public void setValorTceMes(final BigDecimal valorTceMes) {
        this.valorTceMes = valorTceMes;
    }

    /**
     * Obtem o valor do atributo valorTaxa.<BR>
     * Atributo gerado a partir da coluna TCME_VL_TAXA.
     * @return Valor do atributo valorTaxa.
     */
    public BigDecimal getValorTaxa() {
        return this.valorTaxa;
    }

    /**
     * Atualiza o valor do atributo valorTaxa.<BR>
     * Atributo gerado a partir da coluna TCME_VL_TAXA.
     * @param valorTaxa Novo valor para o atributo valorTaxa.
     */
    public void setValorTaxa(final BigDecimal valorTaxa) {
        this.valorTaxa = valorTaxa;
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
        buffer.append("codigoTceMes").append("='").append(getCodigoTceMes())
                .append("' ");
        buffer.append("pessoa").append("='").append(getPessoa()).append("' ");
        buffer.append("moeda").append("='").append(getMoeda()).append("' ");
        buffer.append("tipoContrato").append("='").append(getTipoContrato())
                .append("' ");
        buffer.append("dataTceMes").append("='").append(getDataTceMes())
                .append("' ");
        buffer.append("valorTceMes").append("='").append(getValorTceMes())
                .append("' ");
        buffer.append("valorTaxa").append("='").append(getValorTaxa()).append(
                "' ");
        buffer.append("]");

        return buffer.toString();
    }

}