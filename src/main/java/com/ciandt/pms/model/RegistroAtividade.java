/*
 * @(#) RegistroAtividade.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;
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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

import com.ciandt.pms.csv.util.CsvAtividadeConverter;
import com.ciandt.pms.csv.util.CsvBigDecimalConverter;
import com.ciandt.pms.csv.util.CsvDateConverter;


/**
 * Entity gerado a partir da tabela REGISTRO_ATIVIDADE.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 12/08/2010 10:25:55
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "REGISTRO_ATIVIDADE", uniqueConstraints = @UniqueConstraint(columnNames = {
        "COPR_CD_CONTRATO_PRATICA", "PESS_CD_PESSOA", "ATIV_CD_ATIVIDADE",
        "REAT_DT_REGISTRO_ATIVIDADE"}))
@NamedQueries({
        @NamedQuery(name = "RegistroAtividade.findAll", query = "SELECT t FROM RegistroAtividade t"),

        @NamedQuery(name = "RegistroAtividade.findByFilter", query = "SELECT reat FROM RegistroAtividade reat "
                + " JOIN FETCH reat.pessoa "
                + " JOIN FETCH reat.atividade "
                + " LEFT OUTER JOIN FETCH reat.contratoPratica "
                + " LEFT OUTER JOIN FETCH reat.grupoCusto "
                + "WHERE (reat.contratoPratica.codigoContratoPratica = ? OR ? = 0L) "
                + "  AND (reat.grupoCusto.codigoGrupoCusto = ? OR ? = 0L) "
                + "  AND (UPPER(reat.pessoa.codigoLogin) like '%'||TRIM(UPPER(?))||'%' "
                + "   OR UPPER(reat.pessoa.nomePessoa) like '%'||TRIM(UPPER(?))||'%' ) "
                + "  AND (reat.atividade.codigoAtividade = ? OR ? = 0L) "
                + "  AND (TRUNC(reat.dataMesChp) = TRUNC(?)) "
                + "ORDER BY reat.dataMesChp ASC, reat.pessoa.nomePessoa ASC "),

        @NamedQuery(name = "RegistroAtividade.findByDataMesChp", query = "SELECT reat FROM RegistroAtividade reat "
                + "WHERE (TRUNC(reat.dataMesChp) = TRUNC(?)) "
                + "ORDER BY reat.pessoa.nomePessoa ASC "),

        @NamedQuery(name = "RegistroAtividade.findUnique", query = "SELECT reat FROM RegistroAtividade reat "
                + "WHERE (reat.contratoPratica.codigoContratoPratica = ? OR ? = 0L) "
                + "  AND (reat.grupoCusto.codigoGrupoCusto = ? OR ? = 0L) "
                + "  AND (UPPER(reat.pessoa.codigoLogin) = TRIM(UPPER(?))) "
                + "  AND (reat.atividade.codigoAtividade = ?) "
                + "  AND (TRUNC(reat.dataRegistroAtividade) = TRUNC(?)) ")})
@CsvDataType
public class RegistroAtividade implements java.io.Serializable {

    // ========================================================================
    // BEGIN - Coloque aqui o codigo manual
    // ========================================================================

    /** Constante para NamedQuery "RegistroAtividade.findByFilter". */
    public static final String FIND_BY_FILTER =
            "RegistroAtividade.findByFilter";

    /** Constante para NamedQuery "RegistroAtividade.findUnique". */
    public static final String FIND_UNIQUE = "RegistroAtividade.findUnique";

    /** Constante para NamedQuery "RegistroAtividade.findByDataMes". */
    public static final String FIND_BY_DATA_MES_CHP =
            "RegistroAtividade.findByDataMesChp";

    // ========================================================================
    // END
    // ========================================================================

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "RegistroAtividade.findAll".
     */
    public static final String FIND_ALL = "RegistroAtividade.findAll";

    /**
     * Atributo gerado a partir da coluna REAT_CD_REGISTRO_ATIVIDADE.
     */
    @Id
    @GeneratedValue(generator = "RegistroAtividadeSeq")
    @SequenceGenerator(name = "RegistroAtividadeSeq", sequenceName = "SQ_REAT_CD_REGISTRO_ATIVIDADE", allocationSize = 1)
    @Column(name = "REAT_CD_REGISTRO_ATIVIDADE", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoRegistroAtividade;

    /**
     * Atributo gerado a partir da coluna PESS_CD_PESSOA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESS_CD_PESSOA", nullable = false)
    private Pessoa pessoa;

    /** Login do usu�rio. */
    @Transient
    @CsvField(pos = 0)
    private String login;

    /**
     * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COPR_CD_CONTRATO_PRATICA")
    private ContratoPratica contratoPratica;

    /**
     * Atributo gerado a partir da coluna GRCU_CD_GRUPO_CUSTO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRCU_CD_GRUPO_CUSTO")
    private GrupoCusto grupoCusto;

    /** Id do ContratoPratica. */
    @Transient
    @CsvField(pos = 2)
    private Long idContratoPratica;

    /** Id do GrupoCusto. */
    @Transient
    @CsvField(pos = 3)
    private Long idGrupoCusto;

    /**
     * Atributo gerado a partir da coluna UPAR_CD_UPLOAD_ARQUIVO.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UPAR_CD_UPLOAD_ARQUIVO")
    private UploadArquivo uploadArquivo;

    /**
     * Atributo gerado a partir da coluna ATIV_CD_ATIVIDADE.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATIV_CD_ATIVIDADE", nullable = false)
    private Atividade atividade;

    /** Login do usu�rio. */
    @Transient
    @CsvField(pos = 4, converterType = CsvAtividadeConverter.class)
    private Long idAtividade;

    /**
     * Atributo gerado a partir da coluna REAT_DT_MES.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "REAT_DT_MES", length = 7)
    private Date dataMes;

    /**
     * Atributo gerado a partir da coluna REAT_DT_REGISTRO_ATIVIDADE.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REAT_DT_REGISTRO_ATIVIDADE", length = 7)
    @CsvField(pos = 1, converterType = CsvDateConverter.class)
    private Date dataRegistroAtividade;

    /**
     * Atributo gerado a partir da coluna REAT_DT_MES_CHP.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "REAT_DT_MES_CHP", length = 7)
    private Date dataMesChp;

    /**
     * Atributo gerado a partir da coluna REAT_NR_HORAS.
     */

    @Column(name = "REAT_NR_HORAS", precision = 22, scale = 0)
    @CsvField(pos = 5, required = true, converterType = CsvBigDecimalConverter.class)
    private BigDecimal numeroHoras;

    /**
     * Construtor default.
     */
    public RegistroAtividade() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoRegistroAtividade
     *            Valor do atributo codigoRegistroAtividade;
     * @param pessoa
     *            Valor do atributo pessoa;
     * @param contratoPratica
     *            Valor do atributo contratoPratica;
     * @param atividade
     *            Valor do atributo atividade;
     */
    public RegistroAtividade(final Long codigoRegistroAtividade,
            final Pessoa pessoa, final ContratoPratica contratoPratica,
            final Atividade atividade) {
        this.codigoRegistroAtividade = codigoRegistroAtividade;
        this.pessoa = pessoa;
        this.contratoPratica = contratoPratica;
        this.atividade = atividade;
    }

    /**
     * Construtor com preenchimento da entidade.
     * 
     * @param codigoRegistroAtividade
     *            Valor do atributo codigoRegistroAtividade;
     * @param pessoa
     *            Valor do atributo pessoa;
     * @param contratoPratica
     *            Valor do atributo contratoPratica;
     * @param grupoCusto
     *            Valor do atributo grupoCusto;
     * @param uploadArquivo
     *            Valor do atributo uploadArquivo;
     * @param atividade
     *            Valor do atributo atividade;
     * @param dataMes
     *            Valor do atributo dataMes;
     * @param dataRegistroAtividade
     *            Valor do atributo dataRegistroAtividade;
     * @param dataMesChp
     *            Valor do atributo dataMesChp;
     * @param numeroHoras
     *            Valor do atributo numeroHoras;
     */
    public RegistroAtividade(final Long codigoRegistroAtividade,
            final Pessoa pessoa, final ContratoPratica contratoPratica,
            final GrupoCusto grupoCusto, final UploadArquivo uploadArquivo,
            final Atividade atividade, final Date dataMes,
            final Date dataRegistroAtividade, final Date dataMesChp,
            final BigDecimal numeroHoras) {
        this.codigoRegistroAtividade = codigoRegistroAtividade;
        this.pessoa = pessoa;
        this.contratoPratica = contratoPratica;
        this.uploadArquivo = uploadArquivo;
        this.atividade = atividade;
        this.dataMes = dataMes;
        this.dataRegistroAtividade = dataRegistroAtividade;
        this.dataMesChp = dataMesChp;
        this.numeroHoras = numeroHoras;
    }

    /**
     * Obtem o valor do atributo codigoRegistroAtividade.<BR>
     * Atributo gerado a partir da coluna REAT_CD_REGISTRO_ATIVIDADE.
     * 
     * @return Valor do atributo codigoRegistroAtividade.
     */
    public Long getCodigoRegistroAtividade() {
        return this.codigoRegistroAtividade;
    }

    /**
     * Atualiza o valor do atributo codigoRegistroAtividade.<BR>
     * Atributo gerado a partir da coluna REAT_CD_REGISTRO_ATIVIDADE.
     * 
     * @param codigoRegistroAtividade
     *            Novo valor para o atributo codigoRegistroAtividade.
     */
    public void setCodigoRegistroAtividade(final Long codigoRegistroAtividade) {
        this.codigoRegistroAtividade = codigoRegistroAtividade;
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
     * Obtem o valor do atributo contratoPratica.<BR>
     * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
     * 
     * @return Valor do atributo contratoPratica.
     */
    public ContratoPratica getContratoPratica() {
        return this.contratoPratica;
    }

    /**
     * Atualiza o valor do atributo contratoPratica.<BR>
     * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
     * 
     * @param contratoPratica
     *            Novo valor para o atributo contratoPratica.
     */
    public void setContratoPratica(final ContratoPratica contratoPratica) {
        this.contratoPratica = contratoPratica;
    }

    /**
     * Obtem o valor do atributo uploadArquivo.<BR>
     * Atributo gerado a partir da coluna UPAR_CD_UPLOAD_ARQUIVO.
     * 
     * @return Valor do atributo uploadArquivo.
     */
    public UploadArquivo getUploadArquivo() {
        return this.uploadArquivo;
    }

    /**
     * Atualiza o valor do atributo uploadArquivo.<BR>
     * Atributo gerado a partir da coluna UPAR_CD_UPLOAD_ARQUIVO.
     * 
     * @param uploadArquivo
     *            Novo valor para o atributo uploadArquivo.
     */
    public void setUploadArquivo(final UploadArquivo uploadArquivo) {
        this.uploadArquivo = uploadArquivo;
    }

    /**
     * Obtem o valor do atributo atividade.<BR>
     * Atributo gerado a partir da coluna ATIV_CD_ATIVIDADE.
     * 
     * @return Valor do atributo atividade.
     */
    public Atividade getAtividade() {
        return this.atividade;
    }

    /**
     * Atualiza o valor do atributo atividade.<BR>
     * Atributo gerado a partir da coluna ATIV_CD_ATIVIDADE.
     * 
     * @param atividade
     *            Novo valor para o atributo atividade.
     */
    public void setAtividade(final Atividade atividade) {
        this.atividade = atividade;
    }

    /**
     * Obtem o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna REAT_DT_MES.
     * 
     * @return Valor do atributo dataMes.
     */
    public Date getDataMes() {
        return this.dataMes;
    }

    /**
     * Atualiza o valor do atributo dataMes.<BR>
     * Atributo gerado a partir da coluna REAT_DT_MES.
     * 
     * @param dataMes
     *            Novo valor para o atributo dataMes.
     */
    public void setDataMes(final Date dataMes) {
        this.dataMes = dataMes;
    }

    /**
     * Obtem o valor do atributo dataRegistroAtividade.<BR>
     * Atributo gerado a partir da coluna REAT_DT_REGISTRO_ATIVIDADE.
     * 
     * @return Valor do atributo dataRegistroAtividade.
     */
    public Date getDataRegistroAtividade() {
        return this.dataRegistroAtividade;
    }

    /**
     * Atualiza o valor do atributo dataRegistroAtividade.<BR>
     * Atributo gerado a partir da coluna REAT_DT_REGISTRO_ATIVIDADE.
     * 
     * @param dataRegistroAtividade
     *            Novo valor para o atributo dataRegistroAtividade.
     */
    public void setDataRegistroAtividade(final Date dataRegistroAtividade) {
        this.dataRegistroAtividade = dataRegistroAtividade;
    }

    /**
     * Obtem o valor do atributo dataMesChp.<BR>
     * Atributo gerado a partir da coluna REAT_DT_MES_CHP.
     * 
     * @return Valor do atributo dataMesChp.
     */
    public Date getDataMesChp() {
        return this.dataMesChp;
    }

    /**
     * Atualiza o valor do atributo dataMesChp.<BR>
     * Atributo gerado a partir da coluna REAT_DT_MES_CHP.
     * 
     * @param dataMesChp
     *            Novo valor para o atributo dataMesChp.
     */
    public void setDataMesChp(final Date dataMesChp) {
        this.dataMesChp = dataMesChp;
    }

    /**
     * Obtem o valor do atributo numeroHoras.<BR>
     * Atributo gerado a partir da coluna REAT_NR_HORAS.
     * 
     * @return Valor do atributo numeroHoras.
     */
    public BigDecimal getNumeroHoras() {
        return this.numeroHoras;
    }

    /**
     * Atualiza o valor do atributo numeroHoras.<BR>
     * Atributo gerado a partir da coluna REAT_NR_HORAS.
     * 
     * @param numeroHoras
     *            Novo valor para o atributo numeroHoras.
     */
    public void setNumeroHoras(final BigDecimal numeroHoras) {
        this.numeroHoras = numeroHoras;
    }

    /**
     * @return the grupoCusto
     */
    public GrupoCusto getGrupoCusto() {
        return grupoCusto;
    }

    /**
     * @param grupoCusto
     *            the grupoCusto to set
     */
    public void setGrupoCusto(final GrupoCusto grupoCusto) {
        this.grupoCusto = grupoCusto;
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
        buffer.append("codigoRegistroAtividade").append("='").append(
                getCodigoRegistroAtividade()).append("' ");
        buffer.append("pessoa").append("='").append(getPessoa()).append("' ");
        buffer.append("contratoPratica").append("='").append(
                getContratoPratica()).append("' ");
        buffer.append("atividade").append("='").append(getAtividade()).append(
                "' ");
        buffer.append("dataMes").append("='").append(getDataMes()).append("' ");
        buffer.append("dataRegistroAtividade").append("='").append(
                getDataRegistroAtividade()).append("' ");
        buffer.append("dataMesChp").append("='").append(getDataMesChp())
                .append("' ");
        buffer.append("numeroHoras").append("='").append(getNumeroHoras())
                .append("' ");
        buffer.append("]");

        return buffer.toString();
    }

    /**
     * @param login
     *            the login to set
     */
    public void setLogin(final String login) {
        this.login = login;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        if (login == null && pessoa != null) {
            return pessoa.getCodigoLogin();
        }

        return login;
    }

    /**
     * @param idContratoPratica
     *            the idContratoPratica to set
     */
    public void setIdContratoPratica(final Long idContratoPratica) {
        this.idContratoPratica = idContratoPratica;
    }

    /**
     * @return the idContratoPratica
     */
    public Long getIdContratoPratica() {
        if (idContratoPratica == null && contratoPratica != null) {
            return contratoPratica.getCodigoContratoPratica();
        }

        return idContratoPratica;
    }

    /**
     * @param idAtividade
     *            the idAtividade to set
     */
    public void setIdAtividade(final Long idAtividade) {

        this.idAtividade = idAtividade;
    }

    /**
     * @return the idAtividade
     */
    public Long getIdAtividade() {
        if (idAtividade == null && atividade != null) {
            return atividade.getCodigoAtividade();
        }
        return idAtividade;
    }

    /**
     * @return the idGrupoCusto
     */
    public Long getIdGrupoCusto() {
        if (idGrupoCusto == null && grupoCusto != null) {
            return grupoCusto.getCodigoGrupoCusto();
        }

        return idGrupoCusto;
    }

    /**
     * @param idGrupoCusto
     *            the idGrupoCusto to set
     */
    public void setIdGrupoCusto(final Long idGrupoCusto) {
        this.idGrupoCusto = idGrupoCusto;
    }

}