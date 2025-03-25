/*
 * @(#) AjusteReceitaAud.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.Date;
import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * Entity gerado a partir da tabela AJUSTE_RECEITA_AUD.
 *
 * @author pricaldeira@ciant.com
 * @since 14/06/2021 13:34:08
 * @version 14/06/2021 1.0 13:34:08
 */
@Entity
@Table(name = "AJUSTE_RECEITA_AUD")
@NamedQueries({
        @NamedQuery(name = "AjusteReceitaAud.findAll", query = "SELECT t FROM AjusteReceitaAud t"),
        @NamedQuery(name = "AjusteReceitaAud.findByCodigoAjusteReceita", query = "SELECT t FROM AjusteReceitaAud t "
                + "WHERE t.id.codigoAjusteReceita = :codigoAjusteReceita "
                + "ORDER BY t.id.revinfo.revtstmp"),
        @NamedQuery(name = "AjusteReceitaAud.findByCodigoAjusteReceitaAndRevtype", query = "SELECT t FROM AjusteReceitaAud t "
                + "WHERE t.id.codigoAjusteReceita = :codigoAjusteReceita "
                + "AND t.revType = :revtype") })
public class AjusteReceitaAud implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "AjusteReceitaAud.findAll".
     */
    public static final String FIND_ALL = "AjusteReceitaAud.findAll";

    /**
     * Constante para NamedQuery
     * "AjusteReceitaAud.findByCodigoAjusteReceita".
     */
    public static final String FIND_BY_CODIGO_AJUSTE_RECEITA = "AjusteReceitaAud.findByc";

    /**
     * Constante para NamedQuery
     * "AjusteReceitaAud.findByCodigoAjusteReceitaAndRevtype".
     */
    public static final String FIND_BY_CODIGO_AJUSTE_RECEITA_AND_REVTYPE = "AjusteReceitaAud.findByCodigoAjusteReceitaAndRevtype";

    /**
     * Chave.
     */
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "codigoAjusteReceita", column = @Column(name = "AJRE_CD_AJUSTE_RECEITA", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "revinfo.rev", column = @Column(name = "REV", nullable = false, precision = 18, scale = 0)) })
    private AjusteReceitaAudId id;

    /**
     * Atributo gerado a partir da coluna REVTYPE.
     */
    @Column(name = "REVTYPE", precision = 3, scale = 0)
    private Long revType;



    /**
     * Atributo gerado a partir da coluna TIAJ_CD_TIPO_AJUSTE.
     */
    @Column(name = "TIAJ_CD_TIPO_AJUSTE", length = 18)
    private Long tipoAjuste;

    /**
     * Atributo gerado a partir da coluna REDF_CD_RECEITA_DFISCAL.
     */
    @Column(name = "REDF_CD_RECEITA_DFISCAL", length = 18)
    private Long receitaDealFiscal;

    /**
     * Atributo gerado a partir da coluna AJRE_CD_LOGIN_AUTOR.
     */

    @Column(name = "AJRE_CD_LOGIN_AUTOR", nullable = false, length = 20)
    private String codigoLoginAutor;

    /**
     * Atributo gerado a partir da coluna AJRE_TX_OBSERVACAO.
     */

    @Column(name = "AJRE_TX_OBSERVACAO", length = 1000)
    private String textoObservacao;

    /**
     * Atributo gerado a partir da coluna AJRE_DT_MES_AJUSTE.
     */

    @Temporal(TemporalType.DATE)
    @Column(name = "AJRE_DT_MES_AJUSTE", length = 7)
    private Date dataMesAjuste;

    /**
     * Atributo gerado a partir da coluna AJRE_DT_CRIACAO.
     */

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AJRE_DT_CRIACAO", length = 7)
    private Date dataCriacao;

    /**
     * Atributo gerado a partir da coluna AJRE_DT_ATUALIZACAO.
     */

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AJRE_DT_ATUALIZACAO", length = 7)
    private Date dataAtualizacao;

    /**
     * Atributo gerado a partir da coluna AJRE_VL_AJUSTE.
     */

    @Column(name = "AJRE_VL_AJUSTE", precision = 22, scale = 0)
    private BigDecimal valorAjuste;


    @Column(name = "AJRE_CD_AJUSTE_RECEITA_PAI", length = 18)
    private Long ajusteReceitaPai;


    /**
     * Construtor default.
     */
    public AjusteReceitaAud() {
    }

    /**
     * Construtor com preenchimento da entidade.
     *
     * @param id
     *            Valor do atributo id;
     */
    public AjusteReceitaAud(AjusteReceitaAudId id) {
        this.id = id;
    }

    /**
     * Construtor com preenchimento da entidade.
     *
     * @param id
     * @param revType
     * @param tipoAjuste
     * @param receitaDealFiscal
     * @param codigoLoginAutor
     * @param textoObservacao
     * @param dataMesAjuste
     * @param dataCriacao
     * @param dataAtualizacao
     * @param valorAjuste
     * @param ajusteReceitaPai
     */
    public AjusteReceitaAud(AjusteReceitaAudId id, Long revType,
                               Long tipoAjuste, Long receitaDealFiscal,
                               String codigoLoginAutor, String textoObservacao,
                               Date dataMesAjuste, Date dataCriacao,
                               Date dataAtualizacao, BigDecimal valorAjuste,
                               Long ajusteReceitaPai) {

        this.id          =  id;
        this.revType     =  revType;
        this.tipoAjuste  =  tipoAjuste;
        this.receitaDealFiscal  = tipoAjuste;
        this.codigoLoginAutor   = codigoLoginAutor;
        this.textoObservacao    = textoObservacao;
        this.dataMesAjuste      = dataMesAjuste;
        this.dataCriacao        = dataCriacao;
        this.dataAtualizacao    = dataAtualizacao;
        this.valorAjuste        = valorAjuste;
        this.ajusteReceitaPai   = ajusteReceitaPai;


    }

    /**
     * Obtem o valor do atributo id.<BR>
     * Atributo gerado a partir da coluna REV.
     *
     * @return Valor do atributo id.
     */
    public AjusteReceitaAudId getId() {
        return this.id;
    }

    /**
     * Atualiza o valor do atributo id.<BR>
     * Atributo gerado a partir da coluna REV.
     *
     * @param id
     *            Novo valor para o atributo id.
     */
    public void setId(AjusteReceitaAudId id) {
        this.id = id;
    }

    /**
     * Obtem o valor do atributo .<BR>
     * Atributo gerado a partir da coluna REVTYPE.
     *
     * @return Valor do atributo .
     */
    public Long getRevType() {
        return this.revType;
    }

    /**
     * Atualiza o valor do atributo .<BR>
     * Atributo gerado a partir da coluna REVTYPE.
     *
     * @param revType
     *            valor para o atributo .
     */
    public void setRevType(Long revType) {
        this.revType = revType;
    }


    public Long getTipoAjuste() {
        return tipoAjuste;
    }

    public void setTipoAjuste(Long tipoAjuste) {
        this.tipoAjuste = tipoAjuste;
    }

    public Long getReceitaDealFiscal() {
        return receitaDealFiscal;
    }

    public void setReceitaDealFiscal(Long receitaDealFiscal) {
        this.receitaDealFiscal = receitaDealFiscal;
    }

    public String getCodigoLoginAutor() {
        return codigoLoginAutor;
    }

    public void setCodigoLoginAutor(String codigoLoginAutor) {
        this.codigoLoginAutor = codigoLoginAutor;
    }

    public String getTextoObservacao() {
        return textoObservacao;
    }

    public void setTextoObservacao(String textoObservacao) {
        this.textoObservacao = textoObservacao;
    }

    public Date getDataMesAjuste() {
        return dataMesAjuste;
    }

    public void setDataMesAjuste(Date dataMesAjuste) {
        this.dataMesAjuste = dataMesAjuste;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public BigDecimal getValorAjuste() {
        return valorAjuste;
    }

    public void setValorAjuste(BigDecimal valorAjuste) {
        this.valorAjuste = valorAjuste;
    }

    public Long getAjusteReceitaPai() {
        return ajusteReceitaPai;
    }

    public void setAjusteReceitaPai(Long ajusteReceitaPai) {
        this.ajusteReceitaPai = ajusteReceitaPai;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@")
                .append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("id").append("='").append(getId()).append("' ");
        buffer.append("revType").append("='").append(getRevType()).append("' ");
        buffer.append("tipoAjuste").append("='")
                .append(getTipoAjuste()).append("' ");
        buffer.append("receitaDealFiscal").append("='").append(getReceitaDealFiscal())
                .append("' ");
        buffer.append("codigoLoginAutor").append("='").append(getCodigoLoginAutor())
                .append("' ");
        buffer.append("textoObservacao").append("='")
                .append(getTextoObservacao()).append("' ");
        buffer.append("dataMesAjuste").append("='")
                .append(getDataMesAjuste()).append("' ");
        buffer.append("dataCriacao").append("='")
                .append(getDataCriacao()).append("' ");
        buffer.append("dataAtualizacao").append("='")
                .append(getDataAtualizacao()).append("' ");
        buffer.append("valorAjuste").append("='")
                .append( getValorAjuste() ).append("' ");
        buffer.append("ajusteReceitaPai").append("='")
                .append(getAjusteReceitaPai()).append("' ");

        buffer.append("]");

        return buffer.toString();
    }

}
