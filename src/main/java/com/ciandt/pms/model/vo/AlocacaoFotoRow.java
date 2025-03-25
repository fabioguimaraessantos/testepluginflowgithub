/*
 * @(#) AlocacaoFotoRow.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa a linha da matriz de AlocacaoFoto.
 * 
 */
public class AlocacaoFotoRow implements java.io.Serializable {

    /** Valor do serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Atributo que irá exibir nomeContratoPratica (C-Lob follow) ou nomeRecurso
     * (People follow), que está sendo seguido, no template de e-mail do
     * velocity.
     */
    private String currentFollowedName;
    
    /**
     * Atributo que irá exibir nomeContratoPratica ou nomeRecurso (na lista de
     * alterações) no template de e-mail do velocity.
     */
    private String listedName;
    
    /** Atributo gerado a partir da coluna SNAP_CD_MAPA_ALOCACAO. */
    private Long codigoMapaAlocacao;

    /** Atributo gerado a partir da coluna SNAP_NM_CONTRATO_PRATICA. */
    private String nomeContratoPratica;

    /** Atributo gerado a partir da coluna SNAP_IN_TIPO_OP. */
    private String indicadorTipoOp;

    /** Atributo gerado a partir da coluna SNAP_CD_ALOCACAO. */
    private Long codigoAlocacao;
    
    /** Atributo gerado a partir da coluna SNAP_CD_RECURSO. */
    private Long codigoRecurso;

    /** Atributo gerado a partir da coluna SNAP_NM_RECURSO. */
    private String nomeRecurso;

    /** Atributo gerado a partir da coluna SNAP_NM_PERFIL_VENDIDO_PV. */
    private String nomePerfilVendidoPv;

    /** Atributo gerado a partir da coluna SNAP_NM_PERFIL_VENDIDO_CR. */
    private String nomePerfilVendidoCr;

    /** Atributo gerado a partir da coluna SNAP_IN_ESTAGIO_PV. */
    private String indicadorEstagioPv;

    /** Atributo gerado a partir da coluna SNAP_IN_ESTAGIO_CR. */
    private String indicadorEstagioCr;

    /** Atributo gerado a partir da coluna SNAP_VL_UR_PV. */
    private BigDecimal valorUrPv;

    /** Atributo gerado a partir da coluna SNAP_VL_UR_CR. */
    private BigDecimal valorUrCr;

    /** Lista de AlocacaoPeriodoFoto relacionado com a AlocacaoFoto corrente. */
    private List<AlocacaoFotoCell> alocacaoFotoCellList = new ArrayList<AlocacaoFotoCell>(
            0);

    /**
     * @return the currentFollowedName
     */
    public String getCurrentFollowedName() {
        return currentFollowedName;
    }

    /**
     * @param currentFollowedName the currentFollowedName to set
     */
    public void setCurrentFollowedName(final String currentFollowedName) {
        this.currentFollowedName = currentFollowedName;
    }

    /**
     * @return the listedName
     */
    public String getListedName() {
        return listedName;
    }

    /**
     * @param listedName the listedName to set
     */
    public void setListedName(final String listedName) {
        this.listedName = listedName;
    }

    /**
     * @return the codigoMapaAlocacao
     */
    public Long getCodigoMapaAlocacao() {
        return codigoMapaAlocacao;
    }

    /**
     * @param codigoMapaAlocacao
     *            the codigoMapaAlocacao to set
     */
    public void setCodigoMapaAlocacao(final Long codigoMapaAlocacao) {
        this.codigoMapaAlocacao = codigoMapaAlocacao;
    }

    /**
     * @return the nomeContratoPratica
     */
    public String getNomeContratoPratica() {
        return nomeContratoPratica;
    }

    /**
     * @param nomeContratoPratica
     *            the nomeContratoPratica to set
     */
    public void setNomeContratoPratica(final String nomeContratoPratica) {
        this.nomeContratoPratica = nomeContratoPratica;
    }

    /**
     * @return the indicadorTipoOp
     */
    public String getIndicadorTipoOp() {
        return indicadorTipoOp;
    }

    /**
     * @param indicadorTipoOp
     *            the indicadorTipoOp to set
     */
    public void setIndicadorTipoOp(final String indicadorTipoOp) {
        this.indicadorTipoOp = indicadorTipoOp;
    }

    /**
     * @return the codigoAlocacao
     */
    public Long getCodigoAlocacao() {
        return codigoAlocacao;
    }

    /**
     * @param codigoAlocacao
     *            the codigoAlocacao to set
     */
    public void setCodigoAlocacao(final Long codigoAlocacao) {
        this.codigoAlocacao = codigoAlocacao;
    }
    
    /**
     * @return the codigoRecurso
     */
    public Long getCodigoRecurso() {
        return codigoRecurso;
    }

    /**
     * @param codigoRecurso
     *            the codigoRecurso to set
     */
    public void setCodigoRecurso(final Long codigoRecurso) {
        this.codigoRecurso = codigoRecurso;
    }

    /**
     * @return the nomeRecurso
     */
    public String getNomeRecurso() {
        return nomeRecurso;
    }

    /**
     * @param nomeRecurso
     *            the nomeRecurso to set
     */
    public void setNomeRecurso(final String nomeRecurso) {
        this.nomeRecurso = nomeRecurso;
    }

    /**
     * @return the nomePerfilVendidoPv
     */
    public String getNomePerfilVendidoPv() {
        return nomePerfilVendidoPv;
    }

    /**
     * @param nomePerfilVendidoPv
     *            the nomePerfilVendidoPv to set
     */
    public void setNomePerfilVendidoPv(final String nomePerfilVendidoPv) {
        this.nomePerfilVendidoPv = nomePerfilVendidoPv;
    }

    /**
     * @return the nomePerfilVendidoCr
     */
    public String getNomePerfilVendidoCr() {
        return nomePerfilVendidoCr;
    }

    /**
     * @param nomePerfilVendidoCr
     *            the nomePerfilVendidoCr to set
     */
    public void setNomePerfilVendidoCr(final String nomePerfilVendidoCr) {
        this.nomePerfilVendidoCr = nomePerfilVendidoCr;
    }

    /**
     * @return the indicadorEstagioPv
     */
    public String getIndicadorEstagioPv() {
        return indicadorEstagioPv;
    }

    /**
     * @param indicadorEstagioPv
     *            the indicadorEstagioPv to set
     */
    public void setIndicadorEstagioPv(final String indicadorEstagioPv) {
        this.indicadorEstagioPv = indicadorEstagioPv;
    }

    /**
     * @return the indicadorEstagioCr
     */
    public String getIndicadorEstagioCr() {
        return indicadorEstagioCr;
    }

    /**
     * @param indicadorEstagioCr
     *            the indicadorEstagioCr to set
     */
    public void setIndicadorEstagioCr(final String indicadorEstagioCr) {
        this.indicadorEstagioCr = indicadorEstagioCr;
    }

    /**
     * @return the valorUrPv
     */
    public BigDecimal getValorUrPv() {
        return valorUrPv;
    }

    /**
     * @param valorUrPv
     *            the valorUrPv to set
     */
    public void setValorUrPv(final BigDecimal valorUrPv) {
        this.valorUrPv = valorUrPv;
    }

    /**
     * @return the valorUrCr
     */
    public BigDecimal getValorUrCr() {
        return valorUrCr;
    }

    /**
     * @param valorUrCr
     *            the valorUrCr to set
     */
    public void setValorUrCr(final BigDecimal valorUrCr) {
        this.valorUrCr = valorUrCr;
    }

    /**
     * @return the alocacaoFotoCellList
     */
    public List<AlocacaoFotoCell> getAlocacaoFotoCellList() {
        return alocacaoFotoCellList;
    }

    /**
     * @param alocacaoFotoCellList
     *            the alocacaoFotoCellList to set
     */
    public void setAlocacaoFotoCellList(
            final List<AlocacaoFotoCell> alocacaoFotoCellList) {
        this.alocacaoFotoCellList = alocacaoFotoCellList;
    }

}