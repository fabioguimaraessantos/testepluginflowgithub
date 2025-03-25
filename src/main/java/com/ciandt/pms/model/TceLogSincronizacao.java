/*
 * @(#) TceLogSincronizacao.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity gerado a partir da tabela TCE_LOG_SINCRONIZACAO.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 13/06/2011 14:39:50
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "TCE_LOG_SINCRONIZACAO")
@NamedQueries({ 
    @NamedQuery(name = "TceLogSincronizacao.findAll", query = "SELECT t FROM TceLogSincronizacao t")
})
public class TceLogSincronizacao implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Constante para NamedQuery "TceLogSincronizacao.findAll".
     */
    public static final String FIND_ALL = "TceLogSincronizacao.findAll";

    /**
     * Atributo gerado a partir da coluna TCLS_CD_LOG_SINCRONIZACAO.
     */
    @Id
    @GeneratedValue(generator = "TceLogSincronizacaoSeq")
    @SequenceGenerator(name = "TceLogSincronizacaoSeq", 
            sequenceName = "SQ_TCLS_CD_LOG_SINCRONIZACAO", allocationSize = 1)
    @Column(name = "TCLS_CD_LOG_SINCRONIZACAO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoLogSincronizacao;

    /**
     * Atributo gerado a partir da coluna TCLS_DT_MES.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "TCLS_DT_MES", length = 7)
    private Date dataMes;

    /**
     * Atributo gerado a partir da coluna TCLS_DT_EXECUCAO.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TCLS_DT_EXECUCAO", length = 7)
    private Date dataExecucao;

    /**
     * Atributo gerado a partir da coluna TCLS_TX_LOG.
     */
    @Lob
    @Column(name = "TCLS_TX_LOG")
    @Basic(fetch = FetchType.LAZY)
    private String textoLog;

    /**
     * Atributo gerado a partir da coluna TCLS_CD_AUTOR.
     */

    @Column(name = "TCLS_CD_AUTOR", length = 50)
    private String codigoAutor;

    /**
     * Construtor default.
     */
    public TceLogSincronizacao() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoLogSincronizacao Valor do atributo codigoLogSincronizacao;
     */
    public TceLogSincronizacao(final Long codigoLogSincronizacao) {
        this.codigoLogSincronizacao = codigoLogSincronizacao;
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoLogSincronizacao
     *            Valor do atributo codigoLogSincronizacao;
     * @param dataMes
     *            Valor do atributo dataMes;
     * @param dataExecucao
     *            Valor do atributo dataExecucao;
     * @param textoLog
     *            Valor do atributo textoLog;
     * @param codigoAutor
     *            Valor do atributo codigoAutor;            
     */
    public TceLogSincronizacao(final Long codigoLogSincronizacao,
            final Date dataMes, final Date dataExecucao, final String textoLog,
            final String codigoAutor) {
        this.codigoLogSincronizacao = codigoLogSincronizacao;
        this.dataMes = dataMes;
        this.dataExecucao = dataExecucao;
        this.textoLog = textoLog;
        this.codigoAutor = codigoAutor;
    }

    /**
     * Obtem o valor do atributo codigoLogSincronizacao.<BR>
     * Atributo gerado a partir da coluna TCLS_CD_LOG_SINCRONIZACAO.
     * @return Valor do atributo codigoLogSincronizacao.
     */
    public Long getCodigoLogSincronizacao() {
        return this.codigoLogSincronizacao;
    }

    /**
     * Atualiza o valor do atributo codigoLogSincronizacao.<BR>
     * Atributo gerado a partir da coluna TCLS_CD_LOG_SINCRONIZACAO.
     * @param codigoLogSincronizacao Novo valor para o atributo codigoLogSincronizacao.
     */
    public void setCodigoLogSincronizacao(final Long codigoLogSincronizacao) {
        this.codigoLogSincronizacao = codigoLogSincronizacao;
    }

    /**
     * Obtem o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna TCLS_DT_MES.
     * @return Valor do atributo dataMes.
     */
    public Date getDataMes() {
        return this.dataMes;
    }

    /**
     * Atualiza o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna TCLS_DT_MES.
     * @param dataMes Novo valor para o atributo dataMes.
     */
    public void setDataMes(final Date dataMes) {
        this.dataMes = dataMes;
    }

    /**
     * Obtem o valor do atributo dataExecucao.<BR>
     * Atributo gerado a partir da coluna TCLS_DT_EXECUCAO.
     * @return Valor do atributo dataExecucao.
     */
    public Date getDataExecucao() {
        return this.dataExecucao;
    }

    /**
     * Atualiza o valor do atributo dataExecucao.<BR>
     * Atributo gerado a partir da coluna TCLS_DT_EXECUCAO.
     * @param dataExecucao Novo valor para o atributo dataExecucao.
     */
    public void setDataExecucao(final Date dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    /**
     * Obtem o valor do atributo textoLog.<BR>
     * Atributo gerado a partir da coluna TCLS_TX_LOG.
     * @return Valor do atributo textoLog.
     */
    public String getTextoLog() {
        return this.textoLog;
    }

    /**
     * Atualiza o valor do atributo textoLog.<BR>
     * Atributo gerado a partir da coluna TCLS_TX_LOG.
     * @param textoLog Novo valor para o atributo textoLog.
     */
    public void setTextoLog(final String textoLog) {
        this.textoLog = textoLog;
    }

    /**
     * Obtem o valor do atributo codigoAutor.<BR>
     * Atributo gerado a partir da coluna TCLS_CD_AUTOR.
     * @return Valor do atributo codigoAutor.
     */
    public String getCodigoAutor() {
        return this.codigoAutor;
    }

    /**
     * Atualiza o valor do atributo codigoAutor.<BR>
     * Atributo gerado a partir da coluna TCLS_CD_AUTOR.
     * @param codigoAutor Novo valor para o atributo codigoAutor.
     */
    public void setCodigoAutor(final String codigoAutor) {
        this.codigoAutor = codigoAutor;
    }

    /**
     * @see Object#toString()
     * @return representação String do Objeto
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@").append(
                Integer.toHexString(hashCode())).append(" [");
        buffer.append("codigoLogSincronizacao").append("='").append(
                getCodigoLogSincronizacao()).append("' ");
        buffer.append("dataMes").append("='").append(getDataMes()).append("' ");
        buffer.append("dataExecucao").append("='").append(getDataExecucao())
                .append("' ");
        buffer.append("textoLog").append("='").append(getTextoLog()).append(
                "' ");
        buffer.append("codigoAutor").append("='").append(getCodigoAutor())
                .append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}