package com.ciandt.pms.model;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.Date;
import javax.persistence.*;

/**
 * Entity gerado a partir da tabela PESSOA_BILLABILITY.
 *
 */
@Entity
@Table(name = "PESSOA_BILLABILITY")
@Audited
@AuditTable(value="PESSOA_BILLABILITY_AUD")
@NamedQueries({
        @NamedQuery(name = PessoaBillability.FIND_ALL, query = "SELECT t FROM PessoaBillability t"),
        @NamedQuery(name = PessoaBillability.FIND_BY_ID, query = "SELECT t FROM PessoaBillability t "
                + " WHERE t.id.codigoPessoa = ?"
                + " AND t.id.codigoBillability = ?" +
                " AND t.dataInicio = TRUNC(?) "),
        @NamedQuery(name = PessoaBillability.FIND_BY_PESSOA, query = "SELECT t FROM PessoaBillability t"
                + " WHERE t.id.codigoPessoa = :codigoPessoa"),
        @NamedQuery(name = PessoaBillability.FIND_BY_PESSOA_AND_STARTDATE, query = "SELECT t FROM PessoaBillability t" +
                " where t.dataInicio = TRUNC(?) and t.id.codigoPessoa = ? "),
        @NamedQuery(name = PessoaBillability.FIND_BY_PESSOA_AND_ENDDATE_ISNULL, query = "SELECT t FROM PessoaBillability t" +
                " where t.dataFim is null and t.id.codigoPessoa = ? "),
        @NamedQuery(name = PessoaBillability.FIND_BY_PESSOA_AND_ENDDATE, query = "SELECT t FROM PessoaBillability t" +
                " where t.id.codigoPessoa = ? and t.dataFim = TRUNC(?)"),
        @NamedQuery(name = PessoaBillability.FIND_BY_PESSOA_AND_DATE_BETWEEN, query = "SELECT t FROM PessoaBillability t" +
                " where t.id.codigoPessoa = ? and TRUNC(?) Between t.dataInicio and t.dataFim "),
        @NamedQuery(name = PessoaBillability.FIND_BY_PESSOA_AND_AFTER_OR_EQUAL_STARTDATE, query = "SELECT t FROM PessoaBillability t" +
                " where t.id.codigoPessoa = ? and t.dataInicio >= TRUNC(?) "),
        @NamedQuery(name = PessoaBillability.FIND_BY_PESSOA_AND_DATE,
                query = "SELECT t FROM PessoaBillability t"
                        + " WHERE t.id.codigoPessoa = ? "
                        + " AND ((TRUNC(?) between t.dataInicio and t.dataFim) or "
                        + " (t.dataFim is null and t.dataInicio <= TRUNC(?))) "),
        @NamedQuery(name = PessoaBillability.FIND_BY_PESSOA_IN_AND_DATE,
                query = "SELECT t FROM PessoaBillability t"
                        + " WHERE t.id.codigoPessoa in :peopleCodes "
                        + " AND ((TRUNC(:month) between t.dataInicio and t.dataFim) or "
                        + " (t.dataFim is null and t.dataInicio <= TRUNC(:month))) ")

})
public class PessoaBillability implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** Constante para NamedQuery "PessoaBillability.findAll". */
    public static final String FIND_ALL = "PessoaBillability.findAll";

    /**
     * Constante para NamedQuery "PessoaBillability.findById".
     */
    public static final String FIND_BY_ID = "PessoaBillability.findById";

    /**
     * Constante para NamedQuery "PessoaBillability.findByPessoa".
     */
    public static final String FIND_BY_PESSOA = "PessoaBillability.findByPessoa";

    public static final String FIND_BY_PESSOA_AND_STARTDATE = "PessoaBillability.findByPessoaAndStartDate";

    public static final String FIND_BY_PESSOA_AND_ENDDATE_ISNULL = "PessoaBillability.findByPessoaAndEndDateIsNull";

    public static final String  FIND_BY_PESSOA_AND_ENDDATE = "PessoaBillability,findByPessoaAndEndDate";

    public static final String FIND_BY_PESSOA_AND_DATE_BETWEEN = "PessoaBillability,findByPessoaAndDateBetween";

    public static final String FIND_BY_PESSOA_AND_AFTER_OR_EQUAL_STARTDATE = "PessoaBillability,findByPessoaAndAfterDate";

    public static final String FIND_BY_PESSOA_AND_DATE = "PessoaBillability,findByPessoaAndDate";

    public static final String FIND_BY_PESSOA_IN_AND_DATE = "PessoaBillability,findByPessoaInAndDate";

    /**
     * Atributo gerado a partir da coluna DEFI_CD_DEAL_FISCAL.
     */
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "codigoPessoa", column = @Column(name = "PESS_CD_PESSOA", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "codigoBillability", column = @Column(name = "BILL_CD_BILLABILITY", nullable = false, precision = 18, scale = 0)),
            @AttributeOverride(name = "dataInicio", column = @Column(name = "PEBI_DT_INICIO", nullable = false, length = 7))})
    private PessoaBillabilityId id;

    /**
     * Atributo gerado a partir da coluna PESS_CD_PESSOA.
     */
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PESS_CD_PESSOA", nullable = false, insertable = false, updatable = false)
    private Pessoa pessoa;

    /**
     * Atributo gerado a partir da coluna BILL_CD_BILLABILITY.
     */
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BILL_CD_BILLABILITY", nullable = false, insertable = false, updatable = false)
    private Billability billability;

    /**
     * Atributo gerado a partir da coluna PEBI_DT_INICIO.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "PEBI_DT_INICIO", length = 7, insertable = false, updatable = false)
    private Date dataInicio;

    /**
     * Atributo gerado a partir da coluna PEBI_DT_FIM.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "PEBI_DT_FIM", length = 7)
    private Date dataFim;

    /**
     * Atributo gerado a partir da coluna PEBI_DT_CRIACAO.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PEBI_DT_CRIACAO")
    private Date dataCriacao;

    @Transient
    private boolean canDelete = false;

    @Transient
    private boolean canEdit = false;

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }


    /**
     * Construtor default.
     */
    public PessoaBillability() {
    }

    public PessoaBillabilityId getId() {
        return id;
    }

    public void setId(PessoaBillabilityId id) {
        this.id = id;
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
     * Obtem o valor do atributo grupoCusto.<BR>
     * Atributo gerado a partir da coluna GRCU_CD_GRUPO_CUSTO.
     * @return Valor do atributo grupoCusto.
     */
    public Billability getBillability() {
        return this.billability;
    }

    /**
     * Atualiza o valor do atributo billability.<BR>
     * Atributo gerado a partir da coluna BILL_CD_BILLABILITY.
     * @param billability Novo valor para o atributo billability.
     */
    public void setBillability(final Billability billability) {
        this.billability = billability;
    }

    /**
     * Obtem o valor do atributo dataInicio.<BR>
     * Atributo gerado a partir da coluna PEBI_DT_INICIO.
     * @return Valor do atributo dataInicio.
     */
    public Date getDataInicio() {
        return this.dataInicio;
    }

    /**
     * Atualiza o valor do atributo dataInicio.<BR>
     * Atributo gerado a partir da coluna PEBI_DT_INICIO.
     * @param dataInicio Novo valor para o atributo dataInicio.
     */
    public void setDataInicio(final Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * Obtem o valor do atributo dataFim.<BR>
     * Atributo gerado a partir da coluna PEBI_DT_FIM.
     * @return Valor do atributo dataFim.
     */
    public Date getDataFim() {
        return this.dataFim;
    }

    /**
     * Atualiza o valor do atributo dataFim.<BR>
     * Atributo gerado a partir da coluna PEBI_DT_FIM.
     * @param dataFim Novo valor para o atributo dataFim.
     */
    public void setDataFim(final Date dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * Obtem o valor do atributo dataCriacao.<BR>
     * Atributo gerado a partir da coluna PEBI_DT_CRIACAO.
     * @return Valor do atributo dataCriacao.
     */
    public Date getDataCriacao() {
        return this.dataCriacao;
    }

    /**
     * Atualiza o valor do atributo dataCriacao.<BR>
     * Atributo gerado a partir da coluna PEBI_DT_CRIACAO.
     * @param dataCriacao Novo valor para o atributo dataCriacao.
     */
    public void setDataCriacao(final Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("pessoa").append("='").append(getPessoa()).append("' ");
        buffer.append("billability").append("='").append(getBillability())
                .append("' ");
        buffer.append("dataInicio").append("='").append(getDataInicio())
                .append("' ");
        buffer.append("dataFim").append("='").append(getDataFim()).append("' ");
        buffer.append("dataCriacao").append("='").append(getDataCriacao())
                .append("' ");
        buffer.append("]");

        return buffer.toString();
    }

    public boolean hasSameBillabilityType(Billability billability) {
        return this.billability.equals(billability);
    }

}