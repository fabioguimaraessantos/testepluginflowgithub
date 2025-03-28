/*
 * @(#) MapaDespesa.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entity gerado a partir da tabela MAPA_DESPESA.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 28/04/2010 10:46:23
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "MAPA_DESPESA")
@NamedQueries({ 
    @NamedQuery(name = "MapaDespesa.findAll", query = "SELECT t FROM MapaDespesa t"),
    
    @NamedQuery(name = "MapaDespesa.findByContratoPratica", 
            query = "SELECT mapaDes FROM MapaDespesa mapaDes" 
                + " WHERE mapaDes.contratoPratica.codigoContratoPratica = ?"),
                
    @NamedQuery(name = "MapaDespesa.findByFilter", query = "SELECT distinct mapa FROM MapaDespesa mapa "
        + " LEFT JOIN FETCH mapa.contratoPratica cp "
        + " LEFT JOIN cp.contratoPraticaCentroLucros contratoPraticaCL "
        + "WHERE ( (cp.codigoContratoPratica = ?) OR (? = 0L) ) "
        + " AND ( (cp.msa.codigoMsa = ?) OR (? = 0L) ) "
        + " AND ( (cp.msa.cliente.codigoCliente = ?) OR (? = 0L) ) "
        + " AND ( (contratoPraticaCL.centroLucro.codigoCentroLucro = ?) OR (? = 0L) ) "
        + " AND ( (contratoPraticaCL.centroLucro.naturezaCentroLucro.codigoNatureza = ?) OR (? = 0L) ) "
        + " AND (contratoPraticaCL.dataFimVigencia IS NULL) "
        + "ORDER BY cp.nomeContratoPratica ASC ")
})
public class MapaDespesa implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** Constante para NamedQuery "MapaDespesa.findAll". */
    public static final String FIND_ALL = "MapaDespesa.findAll";
    
    /** Constante para NamedQuery "MapaDespesa.findByContratoPratica". */
    public static final String FIND_BY_CONTRATO_PRATICA = "MapaDespesa.findByContratoPratica";
    
    /** Constante para NamedQuery "MapaDespesa.findByFilter". */
    public static final String FIND_BY_FILTER = "MapaDespesa.findByFilter";
    
    /**
     * Atributo gerado a partir da coluna MADE_CD_MAPA_DESPESA.
     */
    @Id
    @GeneratedValue(generator = "MapaDespesaSeq")
    @SequenceGenerator(name = "MapaDespesaSeq", sequenceName = "SQ_MADE_CD_MAPA_DESPESA", allocationSize = 1)
    @Column(name = "MADE_CD_MAPA_DESPESA", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoMapaDespesa;

    /**
     * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPR_CD_CONTRATO_PRATICA", nullable = false)
    private ContratoPratica contratoPratica;

    /** Atributo gerado a partir da coluna MADE_NM_MAPA_DESPESA. */
    @Column(name = "MADE_NM_MAPA_DESPESA", length = 100)
    private String nomeMapaDespesa;

    /** Atributo gerado a partir da tabela DESPESA_MES. */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "mapaDespesa", cascade = CascadeType.ALL)
    @OrderBy("dataMes")
    private List<DespesaMes> despesaMeses = new ArrayList<DespesaMes>(0);

    /**
     * Construtor default.
     */
    public MapaDespesa() {
    }

    /**
     * Obtem o valor do atributo codigoMapaDespesa.<BR>
     * Atributo gerado a partir da coluna MADE_CD_MAPA_DESPESA.
     * @return Valor do atributo codigoMapaDespesa.
     */
    public Long getCodigoMapaDespesa() {
        return this.codigoMapaDespesa;
    }

    /**
     * Atualiza o valor do atributo codigoMapaDespesa.<BR>
     * Atributo gerado a partir da coluna MADE_CD_MAPA_DESPESA.
     * @param codigoMapaDespesa Novo valor para o atributo codigoMapaDespesa.
     */
    public void setCodigoMapaDespesa(final Long codigoMapaDespesa) {
        this.codigoMapaDespesa = codigoMapaDespesa;
    }

    /**
     * Obtem o valor do atributo contratoPratica.<BR>
     * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
     * @return Valor do atributo contratoPratica.
     */
    public ContratoPratica getContratoPratica() {
        return this.contratoPratica;
    }

    /**
     * Atualiza o valor do atributo contratoPratica.<BR>
     * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
     * @param contratoPratica Novo valor para o atributo contratoPratica.
     */
    public void setContratoPratica(final ContratoPratica contratoPratica) {
        this.contratoPratica = contratoPratica;
    }

    /**
     * Obtem o valor do atributo nomeMapaDespesa.<BR>
     * Atributo gerado a partir da coluna MADE_NM_MAPA_DESPESA.
     * @return Valor do atributo nomeMapaDespesa.
     */
    public String getNomeMapaDespesa() {
        return this.nomeMapaDespesa;
    }

    /**
     * Atualiza o valor do atributo nomeMapaDespesa.<BR>
     * Atributo gerado a partir da coluna MADE_NM_MAPA_DESPESA.
     * @param nomeMapaDespesa Novo valor para o atributo nomeMapaDespesa.
     */
    public void setNomeMapaDespesa(final String nomeMapaDespesa) {
        this.nomeMapaDespesa = nomeMapaDespesa;
    }

    /**
     * Obtem o valor do atributo despesaMeses.<BR>

     * @return Valor do atributo despesaMeses.
     */
    public List<DespesaMes> getDespesaMeses() {
        return this.despesaMeses;
    }

    /**
     * Atualiza o valor do atributo despesaMeses.<BR>

     * @param despesaMeses Novo valor para o atributo despesaMeses.
     */
    public void setDespesaMeses(final List<DespesaMes> despesaMeses) {
        this.despesaMeses = despesaMeses;
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
        buffer.append("codigoMapaDespesa").append("='").append(
                getCodigoMapaDespesa()).append("' ");
        buffer.append("contratoPratica").append("='").append(
                getContratoPratica()).append("' ");
        buffer.append("nomeMapaDespesa").append("='").append(
                getNomeMapaDespesa()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
