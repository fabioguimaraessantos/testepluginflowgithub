/*
 * @(#) DespesaMes.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity gerado a partir da tabela DESPESA_MES.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 28/04/2010 10:06:23
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "DESPESA_MES")
@NamedQueries({
    @NamedQuery(name = "DespesaMes.findAll", query = "SELECT t FROM DespesaMes t"),
    
    @NamedQuery(name = "DespesaMes.findByMapaDespAndTipo", 
            query = "SELECT d FROM DespesaMes d " 
                + " WHERE d.mapaDespesa.codigoMapaDespesa = ?" 
                + " AND d.tipoDespesa.codigoTipoDespesa = ? ORDER BY d.dataMes asc"),

    @NamedQuery(name = "DespesaMes.findByMapaTipoAndDate", 
            query = "SELECT d FROM DespesaMes d " 
                + " WHERE d.mapaDespesa.codigoMapaDespesa = ?" 
                + " AND d.tipoDespesa.codigoTipoDespesa = ? " 
                + " AND TRUNC(d.dataMes) = TRUNC(?) "),                
                
    @NamedQuery(name = "DespesaMes.findDebitoByMapaTipoAndDate", 
            query = "SELECT d FROM DespesaMes d " 
                + " WHERE d.mapaDespesa.codigoMapaDespesa = ?" 
                + " AND d.tipoDespesa.codigoTipoDespesa = ? " 
                + " AND TRUNC(d.dataMes) = TRUNC(?) AND d.indicadorDebitoCredito = 'D'"),
                
    @NamedQuery(name = "DespesaMes.findByContratoPraticaAndTipo", 
            query = "SELECT d FROM DespesaMes d " 
                + " WHERE d.mapaDespesa.contratoPratica.codigoContratoPratica = ?" 
                + " AND d.tipoDespesa.codigoTipoDespesa = ? ORDER BY d.dataMes asc"),            

    @NamedQuery(name = "DespesaMes.findMaxDateByMapa",
            query = "SELECT MAX(dm.dataMes) FROM DespesaMes dm "
                + " WHERE dm.mapaDespesa.codigoMapaDespesa = ?"),
            
    @NamedQuery(name = "DespesaMes.findMinDateByMapa", 
            query = "SELECT MIN(dm.dataMes) FROM DespesaMes dm "
                + " WHERE dm.mapaDespesa.codigoMapaDespesa = ?"),
            
    @NamedQuery(name = "DespesaMes.findByMapaDespAndNotInRange",
            query = " SELECT dm FROM DespesaMes dm "
                + " WHERE dm.mapaDespesa.codigoMapaDespesa = ?1 "
                + " AND dm.codigoDespesaMes NOT IN "
                +   " ( SELECT despMes.codigoDespesaMes FROM DespesaMes despMes " 
                +   " WHERE despMes.mapaDespesa.codigoMapaDespesa = ?1 " 
                +   " AND TRUNC(despMes.dataMes) between TRUNC(?2) AND TRUNC(?3) )")            
                
})
public class DespesaMes implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** Constante para NamedQuery "DespesaMes.findAll". */
    public static final String FIND_ALL = "DespesaMes.findAll";
    
    /** Constante para NamedQuery "DespesaMes.findMinDateByMapa". */
    public static final String FIND_MAX_DATE_BY_MAPA = "DespesaMes.findMaxDateByMapa";
    
    /** Constante para NamedQuery "DespesaMes.findMinDateByMapa". */
    public static final String FIND_MIN_DATE_BY_MAPA = "DespesaMes.findMinDateByMapa";
    
    /** Constante para NamedQuery "DespesaMes.findByMapaDespAndTipo". */
    public static final String FIND_BY_MAPA_DESPESA_AND_TIPO = 
        "DespesaMes.findByMapaDespAndTipo";
    
    /** Constante para NamedQuery "DespesaMes.findByContratoPraticaAndTipo". */
    public static final String FIND_BY_CONTRATO_PRATICA_AND_TIPO = 
        "DespesaMes.findByContratoPraticaAndTipo";
    
    /** Constante para NamedQuery "DespesaMes.findDebitoByMapaTipoAndDate". */
    public static final String FIND_DEBITO_BY_MAPA_TIPO_DATE = 
        "DespesaMes.findDebitoByMapaTipoAndDate";
    
    /** Constante para NamedQuery "DespesaMes.findByMapaDespAndNotInRange". */
    public static final String FIND_BY_MAPA_AND_NOT_IN_RANGE = 
        "DespesaMes.findByMapaDespAndNotInRange";
    
    /** Constante para NamedQuery "DespesaMes.findByMapaTipoAndDate". */
    public static final String FIND_BY_MAPA_TIPO_AND_DATE = 
        "DespesaMes.findByMapaTipoAndDate";
    
    
    /**
     * Atributo gerado a partir da coluna DEME_CD_DESPESA_MES.
     */
    @Id
    @GeneratedValue(generator = "DespesaMesSeq")
    @SequenceGenerator(name = "DespesaMesSeq", sequenceName = "SQ_DEME_CD_DESPESA_MES", allocationSize = 1)
    @Column(name = "DEME_CD_DESPESA_MES", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoDespesaMes;

    /**
     * Atributo gerado a partir da coluna MADE_CD_MAPA_DESPESA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MADE_CD_MAPA_DESPESA", nullable = false)
    private MapaDespesa mapaDespesa;

    /**
     * Atributo gerado a partir da coluna TIDE_CD_TIPO_DESPESA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIDE_CD_TIPO_DESPESA", nullable = false)
    private TipoDespesa tipoDespesa;

    /**
     * Atributo gerado a partir da coluna DEME_DT_MES.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DEME_DT_MES", length = 7)
    private Date dataMes;

    /**
     * Atributo gerado a partir da coluna DEME_IN_UNIDADE.
     */

    @Column(name = "DEME_IN_UNIDADE", length = 1)
    private String indicadorUnidade;

    /**
     * Atributo gerado a partir da coluna DEME_IN_DEBITO_CREDITO.
     */

    @Column(name = "DEME_IN_DEBITO_CREDITO", length = 1)
    private String indicadorDebitoCredito;

    /** Atributo gerado a partir da coluna DEME_VL_DESPESA. */
    @Column(name = "DEME_VL_DESPESA", precision = 22, scale = 0)
    private BigDecimal valorDespesa;
    
    /**
     * Construtor default.
     */
    public DespesaMes() {
    }

    /**
     * Obtem o valor do atributo codigoDespesaMes.<BR>
     * Atributo gerado a partir da coluna DEME_CD_DESPESA_MES.
     * @return Valor do atributo codigoDespesaMes.
     */
    public Long getCodigoDespesaMes() {
        return this.codigoDespesaMes;
    }

    /**
     * Atualiza o valor do atributo codigoDespesaMes.<BR>
     * Atributo gerado a partir da coluna DEME_CD_DESPESA_MES.
     * @param codigoDespesaMes Novo valor para o atributo codigoDespesaMes.
     */
    public void setCodigoDespesaMes(final Long codigoDespesaMes) {
        this.codigoDespesaMes = codigoDespesaMes;
    }

    /**
     * Obtem o valor do atributo mapaDespesa.<BR>
     * Atributo gerado a partir da coluna MADE_CD_MAPA_DESPESA.
     * @return Valor do atributo mapaDespesa.
     */
    public MapaDespesa getMapaDespesa() {
        return this.mapaDespesa;
    }

    /**
     * Atualiza o valor do atributo mapaDespesa.<BR>
     * Atributo gerado a partir da coluna MADE_CD_MAPA_DESPESA.
     * @param mapaDespesa Novo valor para o atributo mapaDespesa.
     */
    public void setMapaDespesa(final MapaDespesa mapaDespesa) {
        this.mapaDespesa = mapaDespesa;
    }

    /**
     * Obtem o valor do atributo tipoDespesa.<BR>
     * Atributo gerado a partir da coluna TIDE_CD_TIPO_DESPESA.
     * @return Valor do atributo tipoDespesa.
     */
    public TipoDespesa getTipoDespesa() {
        return this.tipoDespesa;
    }

    /**
     * Atualiza o valor do atributo tipoDespesa.<BR>
     * Atributo gerado a partir da coluna TIDE_CD_TIPO_DESPESA.
     * @param tipoDespesa Novo valor para o atributo tipoDespesa.
     */
    public void setTipoDespesa(final TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    /**
     * Obtem o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna DEME_DT_MES.
     * @return Valor do atributo dataMes.
     */
    public Date getDataMes() {
        return this.dataMes;
    }

    /**
     * Atualiza o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna DEME_DT_MES.
     * @param dataMes Novo valor para o atributo dataMes.
     */
    public void setDataMes(final Date dataMes) {
        this.dataMes = dataMes;
    }

    /**
     * Obtem o valor do atributo indicadorUnidade.<BR>
     * Atributo gerado a partir da coluna DEME_IN_UNIDADE.
     * @return Valor do atributo indicadorUnidade.
     */
    public String getIndicadorUnidade() {
        return this.indicadorUnidade;
    }

    /**
     * Atualiza o valor do atributo indicadorUnidade.<BR>
     * Atributo gerado a partir da coluna DEME_IN_UNIDADE.
     * @param indicadorUnidade Novo valor para o atributo indicadorUnidade.
     */
    public void setIndicadorUnidade(final String indicadorUnidade) {
        this.indicadorUnidade = indicadorUnidade;
    }

    /**
     * Obtem o valor do atributo indicadorDebitoCredito.<BR>
     * Atributo gerado a partir da coluna DEME_IN_DEBITO_CREDITO.
     * @return Valor do atributo indicadorDebitoCredito.
     */
    public String getIndicadorDebitoCredito() {
        return this.indicadorDebitoCredito;
    }

    /**
     * Atualiza o valor do atributo indicadorDebitoCredito.<BR>
     * Atributo gerado a partir da coluna DEME_IN_DEBITO_CREDITO.
     * @param indicadorDebitoCredito Novo valor para o atributo indicadorDebitoCredito.
     */
    public void setIndicadorDebitoCredito(final String indicadorDebitoCredito) {
        this.indicadorDebitoCredito = indicadorDebitoCredito;
    }
    
    /**
     * Obtem o valor do atributo valorDespesa.<BR>
     * Atributo gerado a partir da coluna DEME_VL_DESPESA.
     * @return Valor do atributo valorDespesa.
     */
    public BigDecimal getValorDespesa() {
        return this.valorDespesa;
    }

    /**
     * Atualiza o valor do atributo valorDespesa.<BR>
     * Atributo gerado a partir da coluna DEME_VL_DESPESA.
     * @param valorDespesa Novo valor para o atributo valorDespesa.
     */
    public void setValorDespesa(final BigDecimal valorDespesa) {
        this.valorDespesa = valorDespesa;
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
        buffer.append("codigoDespesaMes").append("='").append(
                getCodigoDespesaMes()).append("' ");
        buffer.append("mapaDespesa").append("='").append(getMapaDespesa())
                .append("' ");
        buffer.append("tipoDespesa").append("='").append(getTipoDespesa())
                .append("' ");
        buffer.append("dataMes").append("='").append(getDataMes()).append("' ");
        buffer.append("indicadorUnidade").append("='").append(
                getIndicadorUnidade()).append("' ");
        buffer.append("indicadorDebitoCredito").append("='").append(
                getIndicadorDebitoCredito()).append("' ");
        buffer.append("valorDespesa").append("='").append(getValorDespesa())
                .append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
