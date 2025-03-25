package com.ciandt.pms.model;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "PESSOA_GRUPO_CUSTO_MG")
@Audited
@AuditTable(value = "PESSOA_GRUPO_CUSTO_MG_AUD")
@NamedQueries(value = {
        @NamedQuery(name = PessoaGrupoCustoMG.FIND_BY_PESSOA_GRUPO_CUSTO_START_DATE,
                query = "SELECT t FROM PessoaGrupoCustoMG t " +
                " WHERE t.pessoa.codigoPessoa = :codigoPessoa " +
                " AND t.grupoCusto.codigoGrupoCusto = :codigoGrupoCusto " +
                " AND t.dataInicio = :dataInicio "),
        @NamedQuery(name = PessoaGrupoCustoMG.FIND_PREVIOUS,
                query = "SELECT t FROM PessoaGrupoCustoMG t "
                        + " WHERE t.pessoa.codigoPessoa = :codigoPessoa "
                        + " AND TRUNC(t.dataInicio) = "
                        + "     (SELECT MAX(TRUNC(pgcBefore.dataInicio)) "
                        + "         FROM PessoaGrupoCustoMG pgcBefore "
                        + "         WHERE pgcBefore.pessoa.codigoPessoa = :codigoPessoa"
                        + "         AND pgcBefore.dataInicio < :dataInicio)"),
        @NamedQuery(name = PessoaGrupoCustoMG.FIND_NEXT,
                query = "SELECT t FROM PessoaGrupoCustoMG t "
                        + " WHERE t.pessoa.codigoPessoa = :codigoPessoa "
                        + " AND TRUNC(t.dataInicio) = "
                        + "     (SELECT MIN(TRUNC(pgcNext.dataInicio)) "
                        + "         FROM PessoaGrupoCustoMG pgcNext "
                        + "         WHERE pgcNext.pessoa.codigoPessoa = :codigoPessoa"
                        + "         AND pgcNext.dataInicio > :dataInicio)")
})
public class PessoaGrupoCustoMG implements Serializable {

    public static final String FIND_BY_PESSOA_GRUPO_CUSTO_START_DATE = "PessoaGrupoCustoMG.findByPessoaAndGrupoCustoAndStartDate";
    public static final String FIND_PREVIOUS = "PessoaGrupoCustoMG.findPrevious";
    public static final String FIND_NEXT = "PessoaGrupoCustoMG.findNext";

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PEGC_CD_PESSOA_GRUPO_CUSTO")
    private Long codigoPessoaGrupoCusto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESS_CD_PESSOA")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRCU_CD_GRUPO_CUSTO")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private GrupoCusto grupoCusto;

    @Temporal(TemporalType.DATE)
    @Column(name = "PEGC_DT_INICIO")
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "PEGC_DT_FIM")
    private Date dataFim;

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public GrupoCusto getGrupoCusto() {
        return grupoCusto;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setGrupoCusto(GrupoCusto grupoCusto) {
        this.grupoCusto = grupoCusto;
    }

    public Long getCodigoPessoaGrupoCusto() {
        return codigoPessoaGrupoCusto;
    }
}
