/*
 * @(#) FatorUrMes.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entity gerado a partir da tabela FATOR_UR_MES.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 08/03/2010 09:36:41
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "FATOR_UR_MES")
@NamedQueries({
    @NamedQuery(name = "FatorUrMes.findAll", query = "SELECT t FROM FatorUrMes t"),
    
    @NamedQuery(name = "FatorUrMes.findByMapaAndPeriod",
            query = "SELECT fator FROM FatorUrMes fator "
            + " WHERE fator.mapaAlocacao.codigoMapaAlocacao = ?"
            + " AND TRUNC(fator.dataMes) between TRUNC(?) AND TRUNC(?)" 
            + " ORDER BY fator.dataMes asc"),
    
    @NamedQuery(name = "FatorUrMes.findByMapa",
            query = "SELECT fator FROM FatorUrMes fator "
            + " WHERE fator.mapaAlocacao.codigoMapaAlocacao = ?"
            + " ORDER BY fator.dataMes asc"),
    
    @NamedQuery(name = "FatorUrMes.findMaxDateByMapa",
            query = "SELECT MAX(fator.dataMes) FROM FatorUrMes fator"
            + " WHERE fator.mapaAlocacao.codigoMapaAlocacao = ?"),
            
    @NamedQuery(name = "FatorUrMes.findMinDateByMapa", 
            query = "SELECT MIN(fator.dataMes) FROM FatorUrMes fator"
            + " WHERE fator.mapaAlocacao.codigoMapaAlocacao = ?"),
    
    @NamedQuery(name = "FatorUrMes.findByMapaAndStartDate", 
            query = "SELECT fator FROM FatorUrMes fator"
            + " WHERE fator.mapaAlocacao.codigoMapaAlocacao = ?"
            + " AND TRUNC(fator.dataMes) >= TRUNC(?)"
            + " ORDER BY fator.dataMes asc"),
    
    @NamedQuery(name = "FatorUrMes.findByMapaAndDate", query = "SELECT fator FROM FatorUrMes fator"
            + " WHERE fator.mapaAlocacao.codigoMapaAlocacao = ?"
            + " AND TRUNC(fator.dataMes) = TRUNC(?)" 
            + " ORDER BY fator.dataMes asc"),
            
    @NamedQuery(name = "FatorUrMes.findByMapaAndNotInRange",
            query = " SELECT fator FROM FatorUrMes fator "
            + " WHERE fator.mapaAlocacao.codigoMapaAlocacao = ? "  
            + " AND TRUNC(fator.dataMes) not between TRUNC(?) AND TRUNC(?) )")            
})
public class FatorUrMes implements java.io.Serializable, Comparable<FatorUrMes>, Cloneable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** Constante para NamedQuery "FatorUrMes.findAll". */
    public static final String FIND_ALL = "FatorUrMes.findAll";

    /** Constante para NamedQuery "FatorUrMes.findByMapa". */
    public static final String FIND_BY_MAPA = "FatorUrMes.findByMapa";
    
    /** Constante para NamedQuery "FatorUrMes.findByMapaAndDate". */
    public static final String FIND_BY_MAPA_AND_DATE = "FatorUrMes.findByMapaAndDate";
    
    /** Constante para NamedQuery "FatorUrMes.findMinDateByMapa" . */
    public static final String FIND_MIN_DATE_BY_MAPA = "FatorUrMes.findMinDateByMapa";

    /** Constante para NamedQuery "FatorUrMes.findByMapaAndStartDate" . */
    public static final String FIND_MAPA_AND_START_DATE = 
        "FatorUrMes.findByMapaAndStartDate";
    
    /** Constante para NamedQuery "FatorUrMes.findMaxDateByMapa" . */
    public static final String FIND_MAX_DATE_BY_MAPA = "FatorUrMes.findMaxDateByMapa";
    
    /** Constante para NamedQuery "FatorUrMes.findByMapaAndPeriod" . */
    public static final String FIND_BY_MAPA_AND_PERIOD = "FatorUrMes.findByMapaAndPeriod";
    
    /** Constante para NamedQuery "FatorUrMes.findByMapaAndNotInRange" . */
    public static final String FIND_BY_MAPA_AND_NOT_IN_RANGE = "FatorUrMes.findByMapaAndNotInRange";
    
    /**
     * Atributo gerado a partir da coluna FAUM_CD_FATOR_UR_MES.
     */
    @Id
    @GeneratedValue(generator = "FatorUrMesSeq")		
    @SequenceGenerator(name = "FatorUrMesSeq", sequenceName = "SQ_FAUM_CD_FATOR_UR_MES", allocationSize = 1)
    @Column(name = "FAUM_CD_FATOR_UR_MES", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoFatorUrMes;

    /**
     * Atributo gerado a partir da coluna MAAL_CD_MAPA_ALOCACAO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MAAL_CD_MAPA_ALOCACAO", nullable = false)
    private MapaAlocacao mapaAlocacao;

    /**
     * Atributo gerado a partir da coluna FAUM_DT_MES.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "FAUM_DT_MES", length = 7)
    private Date dataMes;

    /**
     * Atributo gerado a partir da coluna FAUM_PR_FATOR_UR.
     */

    @Column(name = "FAUM_PR_FATOR_UR", precision = 22, scale = 0)
    private BigDecimal percentualFatorUr;

    /**
     * Construtor default.
     */
    public FatorUrMes() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoFatorUrMes Valor do atributo codigoFatorUrMes;
     * @param mapaAlocacao Valor do atributo mapaAlocacao;
     */
    public FatorUrMes(final Long codigoFatorUrMes, final MapaAlocacao mapaAlocacao) {
        this.codigoFatorUrMes = codigoFatorUrMes;
        this.mapaAlocacao = mapaAlocacao;
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoFatorUrMes Valor do atributo codigoFatorUrMes;
     * @param mapaAlocacao Valor do atributo mapaAlocacao;
     * @param dataMes Valor do atributo dataMes;
     * @param percentualFatorUr Valor do atributo percentualFatorUr;
     */
    public FatorUrMes(final Long codigoFatorUrMes, final MapaAlocacao mapaAlocacao,
            final Date dataMes, final BigDecimal percentualFatorUr) {
        this.codigoFatorUrMes = codigoFatorUrMes;
        this.mapaAlocacao = mapaAlocacao;
        this.dataMes = dataMes;
        this.percentualFatorUr = percentualFatorUr;
    }

    /**
     * Obtem o valor do atributo codigoFatorUrMes.<BR>
     * Atributo gerado a partir da coluna FAUM_CD_FATOR_UR_MES.
     * @return Valor do atributo codigoFatorUrMes.
     */
    public Long getCodigoFatorUrMes() {
        return this.codigoFatorUrMes;
    }

    /**
     * Atualiza o valor do atributo codigoFatorUrMes.<BR>
     * Atributo gerado a partir da coluna FAUM_CD_FATOR_UR_MES.
     * @param codigoFatorUrMes Novo valor para o atributo codigoFatorUrMes.
     */
    public void setCodigoFatorUrMes(final Long codigoFatorUrMes) {
        this.codigoFatorUrMes = codigoFatorUrMes;
    }

    /**
     * Obtem o valor do atributo mapaAlocacao.<BR>
     * Atributo gerado a partir da coluna MAAL_CD_MAPA_ALOCACAO.
     * @return Valor do atributo mapaAlocacao.
     */
    public MapaAlocacao getMapaAlocacao() {
        return this.mapaAlocacao;
    }

    /**
     * Atualiza o valor do atributo mapaAlocacao.<BR>
     * Atributo gerado a partir da coluna MAAL_CD_MAPA_ALOCACAO.
     * @param mapaAlocacao Novo valor para o atributo mapaAlocacao.
     */
    public void setMapaAlocacao(final MapaAlocacao mapaAlocacao) {
        this.mapaAlocacao = mapaAlocacao;
    }

    /**
     * Obtem o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna FAUM_DT_MES.
     * @return Valor do atributo dataMes.
     */
    public Date getDataMes() {
        return this.dataMes;
    }

    /**
     * Atualiza o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna FAUM_DT_MES.
     * @param dataMes Novo valor para o atributo dataMes.
     */
    public void setDataMes(final Date dataMes) {
        this.dataMes = dataMes;
    }

    /**
     * Obtem o valor do atributo percentualFatorUr.<BR>
     * Atributo gerado a partir da coluna FAUM_PR_FATOR_UR.
     * @return Valor do atributo percentualFatorUr.
     */
    public BigDecimal getPercentualFatorUr() {
        return this.percentualFatorUr;
    }

    /**
     * Atualiza o valor do atributo percentualFatorUr.<BR>
     * Atributo gerado a partir da coluna FAUM_PR_FATOR_UR.
     * @param percentualFatorUr Novo valor para o atributo percentualFatorUr.
     */
    public void setPercentualFatorUr(final BigDecimal percentualFatorUr) {
        this.percentualFatorUr = percentualFatorUr;
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
        buffer.append("codigoFatorUrMes").append("='").append(
                getCodigoFatorUrMes()).append("' ");
        buffer.append("mapaAlocacao").append("='").append(getMapaAlocacao())
                .append("' ");
        buffer.append("dataMes").append("='").append(getDataMes()).append("' ");
        buffer.append("percentualFatorUr").append("='").append(
                getPercentualFatorUr()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

    /**
     * Compara a emtidade pela data.
     * 
     * @param fatorUrMes - Entidade a comparar.
     * 
     * @return retorna maior que zero se maior, zero se igual
     * e menor que sero se menor.
     * 
     */
    public int compareTo(final FatorUrMes fatorUrMes) {
        
        return this.dataMes.compareTo(fatorUrMes.getDataMes());
        
    }
    
    /**
     * Realiza uma copia do objeto, 
     * porem com uma referencia diferente.
     * 
     * @return a c�pia do FatorUrMes
     */
    public FatorUrMes getClone() {
       try {
        return (FatorUrMes) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }  
    }

}
