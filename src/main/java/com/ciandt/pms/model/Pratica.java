/*
 * @(#) Pratica.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity gerado a partir da tabela PRATICA. Relacao das Praticas dos Contratos
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 12/08/2009 17:14:06
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "PRATICA")
@NamedQueries({
        @NamedQuery(name = "Pratica.findAll", query = "SELECT t FROM Pratica t "
                + "WHERE (t.indicadorAtivo = 'A') "
                + "ORDER BY t.nomePratica ASC "),
        @NamedQuery(name = "Pratica.findByFilter", query = "SELECT prat FROM Pratica prat "
        		+ "INNER JOIN prat.tipoPratica tp "
                + "WHERE (UPPER(prat.nomePratica) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
                + "AND (UPPER(prat.siglaPratica) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
                + "AND (UPPER(prat.indicadorAtivo) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
                + "AND ((tp.codigoTipoPratica = ?) OR (? = 0L))"
                + "ORDER BY prat.nomePratica ASC ") })
public class Pratica implements java.io.Serializable {

    // ========================================================================
    // BEGIN - Coloque aqui o codigo manual
    // ========================================================================

    /**
     * Constante para NamedQuery "Pratica.findByFilter".
     */
    public static final String FIND_BY_FILTER = "Pratica.findByFilter";

    // ========================================================================
    // END
    // ========================================================================

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "Pratica.findAll".
     */
    public static final String FIND_ALL = "Pratica.findAll";

    /**
     * Atributo gerado a partir da coluna PRAT_CD_PRATICA. Codigo da Pratica
     */
    @Id
    @GeneratedValue(generator = "PraticaSeq")
    @SequenceGenerator(name = "PraticaSeq", sequenceName = "SQ_PRAT_CD_PRATICA", allocationSize = 1)
    @Column(name = "PRAT_CD_PRATICA", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoPratica;
    
	/**
	 * Atributo gerado a partir da coluna TIPR_CD_TIPO_PRATICA.
	 */
	@ManyToOne
	@JoinColumn(name = "TIPR_CD_TIPO_PRATICA", nullable = false)
	private TipoPratica tipoPratica;

    /**
     * Atributo gerado a partir da coluna PRAT_SG_PRATICA.
     */

    @Column(name = "PRAT_SG_PRATICA", length = 240)
    private String siglaPratica;

    /**
     * Atributo gerado a partir da coluna PRAT_NM_PRATICA. Descricao da Pratica
     */

    @Column(name = "PRAT_NM_PRATICA", nullable = false, length = 100)
    private String nomePratica;

    /**
     * Atributo gerado a partir da coluna PRAT_IN_ATIVO.
     */

    @Column(name = "PRAT_IN_ATIVO", nullable = false, length = 1)
    private String indicadorAtivo;

    /**
     * Relacionamento um pra muitos.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pratica")
    private Set<ContratoPratica> contratoPraticas = new HashSet<ContratoPratica>(
            0);

    /**
     * Construtor default.
     */
    public Pratica() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoPratica
     *            Valor do atributo codigoPratica;
     * @param nomePratica
     *            Valor do atributo nomePratica;
     * @param siglaPratica
     *            Valor do atributo siglaPratica
     * @param indicadorAtivo
     *            Valor do atributo indicadorAtivo;
     */
    public Pratica(final Long codigoPratica, final String nomePratica,
            final String siglaPratica, final String indicadorAtivo) {
        this.codigoPratica = codigoPratica;
        this.nomePratica = nomePratica;
        this.siglaPratica = siglaPratica;
        this.indicadorAtivo = indicadorAtivo;
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoPratica
     *            Valor do atributo codigoPratica;
     * @param nomePratica
     *            Valor do atributo nomePratica;
     * @param indicadorAtivo
     *            Valor do atributo indicadorAtivo;
     * @param siglaPratica
     *            Valor do atributo siglaPratica;
     * @param contratoPraticas
     *            Valor do atributo contratoPraticas;
     */
    public Pratica(final Long codigoPratica, final String siglaPratica,
            final String nomePratica, final String indicadorAtivo,
            final Set<ContratoPratica> contratoPraticas) {
        this.codigoPratica = codigoPratica;
        this.siglaPratica = siglaPratica;
        this.nomePratica = nomePratica;
        this.indicadorAtivo = indicadorAtivo;
        this.contratoPraticas = contratoPraticas;
    }

    /**
     * Obtem o valor do atributo codigoPratica.<BR>
     * Atributo gerado a partir da coluna PRAT_CD_PRATICA. Codigo da Pratica
     * 
     * @return Valor do atributo codigoPratica.
     */
    public Long getCodigoPratica() {
        return this.codigoPratica;
    }

    /**
     * Atualiza o valor do atributo codigoPratica.<BR>
     * Atributo gerado a partir da coluna PRAT_CD_PRATICA. Codigo da Pratica
     * 
     * @param codigoPratica
     *            Novo valor para o atributo codigoPratica.
     */
    public void setCodigoPratica(final Long codigoPratica) {
        this.codigoPratica = codigoPratica;
    }
    
    /**
	 * Obtem o valor do atributo tipoPratica.<BR>
	 * Atributo gerado a partir da coluna TIPR_CD_TIPO_PRATICA.
	 * @return Valor do atributo tipoPratica.
	 */
	public TipoPratica getTipoPratica() {
		return this.tipoPratica;
	}

	/**
	 * Atualiza o valor do atributo tipoPratica.<BR>
	 * Atributo gerado a partir da coluna TIPR_CD_TIPO_PRATICA.
	 * @param tipoPratica Novo valor para o atributo tipoPratica.
	 */
	public void setTipoPratica(TipoPratica tipoPratica) {
		this.tipoPratica = tipoPratica;
	}


    /**
     * Obtem o valor do atributo siglaPratica.<BR>
     * Atributo gerado a partir da coluna PRAT_SG_PRATICA.
     * 
     * @return Valor do atributo siglaPratica.
     */
    public String getSiglaPratica() {
        return this.siglaPratica;
    }

    /**
     * Atualiza o valor do atributo siglaPratica.<BR>
     * Atributo gerado a partir da coluna PRAT_SG_PRATICA.
     * 
     * @param siglaPratica
     *            Novo valor para o atributo siglaPratica.
     */
    public void setSiglaPratica(final String siglaPratica) {
        this.siglaPratica = siglaPratica;
    }

    /**
     * Obtem o valor do atributo nomePratica.<BR>
     * Atributo gerado a partir da coluna PRAT_NM_PRATICA. Descricao da Pratica
     * 
     * @return Valor do atributo nomePratica.
     */
    public String getNomePratica() {
        return this.nomePratica;
    }

    /**
     * Atualiza o valor do atributo nomePratica.<BR>
     * Atributo gerado a partir da coluna PRAT_NM_PRATICA. Descricao da Pratica
     * 
     * @param nomePratica
     *            Novo valor para o atributo nomePratica.
     */
    public void setNomePratica(final String nomePratica) {
        this.nomePratica = nomePratica;
    }

    /**
     * Obtem o valor do atributo indicadorAtivo.<BR>
     * Atributo gerado a partir da coluna PRAT_IN_ATIVO.
     * 
     * @return Valor do atributo indicadorAtivo.
     */
    public String getIndicadorAtivo() {
        return this.indicadorAtivo;
    }

    /**
     * Atualiza o valor do atributo indicadorAtivo.<BR>
     * Atributo gerado a partir da coluna PRAT_IN_ATIVO.
     * 
     * @param indicadorAtivo
     *            Novo valor para o atributo indicadorAtivo.
     */
    public void setIndicadorAtivo(final String indicadorAtivo) {
        this.indicadorAtivo = indicadorAtivo;
    }

    /**
     * Obtem o valor do atributo contratoPraticas.<BR>
     * 
     * @return Valor do atributo contratoPraticas.
     */
    public Set<ContratoPratica> getContratoPraticas() {
        return this.contratoPraticas;
    }

    /**
     * Atualiza o valor do atributo contratoPraticas.<BR>
     * 
     * @param contratoPraticas
     *            Novo valor para o atributo contratoPraticas.
     */
    public void setContratoPraticas(final Set<ContratoPratica> contratoPraticas) {
        this.contratoPraticas = contratoPraticas;
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
        buffer.append("codigoPratica").append("='").append(getCodigoPratica())
                .append("' ");
        buffer.append("siglaPratica").append("='").append(getSiglaPratica())
                .append("' ");
        buffer.append("nomePratica").append("='").append(getNomePratica())
                .append("' ");
        buffer.append("indicadorAtivo").append("='")
                .append(getIndicadorAtivo()).append("' ");
        buffer.append("]");

        return buffer.toString();
    }

}
