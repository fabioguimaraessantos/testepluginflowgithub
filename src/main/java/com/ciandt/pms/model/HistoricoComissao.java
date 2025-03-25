/*
 * @(#) HistoricoComissao.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity gerado a partir da tabela HISTORICO_COMISSAO.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 07/10/2010 14:10:45
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "HISTORICO_COMISSAO")
@NamedQueries({
    @NamedQuery(name = "HistoricoComissao.findAll", 
            query = "SELECT t FROM HistoricoComissao t") 
})
public class HistoricoComissao implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Constante para NamedQuery "HistoricoComissao.findAll".
     */
    public static final String FIND_ALL = "HistoricoComissao.findAll";

    /**
     * Atributo gerado a partir da coluna HICO_CD_HISTORICO_COMISSAO.
     */
    @Id
    @GeneratedValue(generator = "HistoricoComissaoSeq")
    @SequenceGenerator(name = "HistoricoComissaoSeq", 
            sequenceName = "SQ_HICO_CD_HISTORICO_COMISSAO", allocationSize = 1)
    @Column(name = "HICO_CD_HISTORICO_COMISSAO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoHistoricoComissao;

    /**
     * Atributo gerado a partir da coluna COMI_CD_COMISSAO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMI_CD_COMISSAO", nullable = false)
    private Comissao comissao;

    /**
     * Atributo gerado a partir da coluna HICO_DT_HISTORICO.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "HICO_DT_HISTORICO", length = 7)
    private Date dataHistorico;

    /**
     * Atributo gerado a partir da coluna HICO_CD_AUTOR_LOGIN.
     */

    @Column(name = "HICO_CD_AUTOR_LOGIN", length = 100)
    private String codigoAutorLogin;

    /**
     * Atributo gerado a partir da coluna HICO_IN_ESTADO.
     */

    @Column(name = "HICO_IN_ESTADO", length = 3)
    private String indicadorEstado;

    /**
     * Atributo gerado a partir da coluna HICO_TX_COMENTARIO.
     */

    @Column(name = "HICO_TX_COMENTARIO", length = 4000)
    private String textoComentario;

    /**
     * Construtor default.
     */
    public HistoricoComissao() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoHistoricoComissao Valor do atributo codigoHistoricoComissao;
     * @param comissao Valor do atributo comissao;
     */
    public HistoricoComissao(
            final Long codigoHistoricoComissao, final Comissao comissao) {
        this.codigoHistoricoComissao = codigoHistoricoComissao;
        this.comissao = comissao;
    }

    /**
     * Obtem o valor do atributo codigoHistoricoComissao.<BR>
     * Atributo gerado a partir da coluna HICO_CD_HISTORICO_COMISSAO.
     * @return Valor do atributo codigoHistoricoComissao.
     */
    public Long getCodigoHistoricoComissao() {
        return this.codigoHistoricoComissao;
    }

    /**
     * Atualiza o valor do atributo codigoHistoricoComissao.<BR>
     * Atributo gerado a partir da coluna HICO_CD_HISTORICO_COMISSAO.
     * @param codigoHistoricoComissao Novo valor para o atributo codigoHistoricoComissao.
     */
    public void setCodigoHistoricoComissao(final Long codigoHistoricoComissao) {
        this.codigoHistoricoComissao = codigoHistoricoComissao;
    }

    /**
     * Obtem o valor do atributo comissao.<BR>
     * Atributo gerado a partir da coluna COMI_CD_COMISSAO.
     * @return Valor do atributo comissao.
     */
    public Comissao getComissao() {
        return this.comissao;
    }

    /**
     * Atualiza o valor do atributo comissao.<BR>
     * Atributo gerado a partir da coluna COMI_CD_COMISSAO.
     * @param comissao Novo valor para o atributo comissao.
     */
    public void setComissao(final Comissao comissao) {
        this.comissao = comissao;
    }

    /**
     * Obtem o valor do atributo dataHistorico.<BR>
     * Atributo gerado a partir da coluna HICO_DT_HISTORICO.
     * @return Valor do atributo dataHistorico.
     */
    public Date getDataHistorico() {
        return this.dataHistorico;
    }

    /**
     * Atualiza o valor do atributo dataHistorico.<BR>
     * Atributo gerado a partir da coluna HICO_DT_HISTORICO.
     * @param dataHistorico Novo valor para o atributo dataHistorico.
     */
    public void setDataHistorico(final Date dataHistorico) {
        this.dataHistorico = dataHistorico;
    }

    /**
     * Obtem o valor do atributo codigoAutorLogin.<BR>
     * Atributo gerado a partir da coluna HICO_CD_AUTOR_LOGIN.
     * @return Valor do atributo codigoAutorLogin.
     */
    public String getCodigoAutorLogin() {
        return this.codigoAutorLogin;
    }

    /**
     * Atualiza o valor do atributo codigoAutorLogin.<BR>
     * Atributo gerado a partir da coluna HICO_CD_AUTOR_LOGIN.
     * @param codigoAutorLogin Novo valor para o atributo codigoAutorLogin.
     */
    public void setCodigoAutorLogin(final String codigoAutorLogin) {
        this.codigoAutorLogin = codigoAutorLogin;
    }

    /**
     * Obtem o valor do atributo indicadorEstado.<BR>
     * Atributo gerado a partir da coluna HICO_IN_ESTADO.
     * @return Valor do atributo indicadorEstado.
     */
    public String getIndicadorEstado() {
        return this.indicadorEstado;
    }

    /**
     * Atualiza o valor do atributo indicadorEstado.<BR>
     * Atributo gerado a partir da coluna HICO_IN_ESTADO.
     * @param indicadorEstado Novo valor para o atributo indicadorEstado.
     */
    public void setIndicadorEstado(final String indicadorEstado) {
        this.indicadorEstado = indicadorEstado;
    }

    /**
     * Obtem o valor do atributo textoComentario.<BR>
     * Atributo gerado a partir da coluna HICO_TX_COMENTARIO.
     * @return Valor do atributo textoComentario.
     */
    public String getTextoComentario() {
        return this.textoComentario;
    }

    /**
     * Atualiza o valor do atributo textoComentario.<BR>
     * Atributo gerado a partir da coluna HICO_TX_COMENTARIO.
     * @param textoComentario Novo valor para o atributo textoComentario.
     */
    public void setTextoComentario(final String textoComentario) {
        this.textoComentario = textoComentario;
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
        buffer.append("codigoHistoricoComissao").append("='").append(
                getCodigoHistoricoComissao()).append("' ");
        buffer.append("comissao").append("='").append(getComissao()).append(
                "' ");
        buffer.append("dataHistorico").append("='").append(getDataHistorico())
                .append("' ");
        buffer.append("codigoAutorLogin").append("='").append(
                getCodigoAutorLogin()).append("' ");
        buffer.append("indicadorEstado").append("='").append(
                getIndicadorEstado()).append("' ");
        buffer.append("textoComentario").append("='").append(
                getTextoComentario()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
