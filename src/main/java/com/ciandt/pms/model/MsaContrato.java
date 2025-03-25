package com.ciandt.pms.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "MSA_CONTRATO")
@Audited
@AuditTable(value="MSA_CONTRATO_AUD")
@NamedQueries({
        @NamedQuery(name = "MsaContrato.findByMsa", query = "SELECT t FROM MsaContrato t where t.msa.codigoMsa = ?  and t.deleteLogico = 'N'" +
                " order by case when t.status.nome = 'Not Signed' then 1 " +
                "               when t.status.nome = 'Valid' then 2 " +
                "               when t.status.nome = 'Closed' then 3 " +
                "               when t.status.nome = 'Expired' then 4 end ASC"),
        @NamedQuery(name = "MsaContrato.findByFilter", query = "SELECT t FROM MsaContrato t "
                + "LEFT JOIN FETCH t.moeda "
                + "WHERE (UPPER(t.jiraCp) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
                + "AND (UPPER(t.jiraLegal) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
                + "AND ((t.moeda.codigoMoeda = ?) OR (? = 0L)) "
                + "AND ((t.status.codigo = ?) OR (? = 0L)) "
                + " AND t.msa.codigoMsa = ? "
                + "AND t.deleteLogico = 'N' "
                + " order by case when t.status.nome = 'Not Signed' then 1 "
                + "               when t.status.nome = 'Valid' then 2 "
                + "               when t.status.nome = 'Closed' then 3 "
                + "               when t.status.nome = 'Expired' then 4 end ASC"),
        @NamedQuery(name = "MsaContrato.findByMsaAndIndicadorDefaultPesquisa", query = "SELECT t FROM MsaContrato t " +
                " where t.msa.codigoMsa = ? and t.status.indicadorDefaultPesquisa = ?  and t.deleteLogico = 'N' " +
                " order by case when t.status.nome = 'Not Signed' then 1 " +
                "               when t.status.nome = 'Valid' then 2 " +
                "               when t.status.nome = 'Closed' then 3 " +
                "               when t.status.nome = 'Expired' then 4 end ASC"),
        @NamedQuery(name = "MsaContrato.findByJiraCp", query = "SELECT t FROM MsaContrato t " +
                " WHERE UPPER(t.jiraCp) = UPPER(?) " +
                "   AND t.deleteLogico = 'N' "),
        @NamedQuery(name = "MsaContrato.findByJiraLegal", query = "SELECT t FROM MsaContrato t " +
                " WHERE UPPER(t.jiraLegal) = UPPER(?) " +
                "   AND t.deleteLogico = 'N' "),
        @NamedQuery(name = "MsaContrato.findByUniqueKey", query = "SELECT t FROM MsaContrato t " +
                " WHERE (UPPER(t.jiraLegal) = UPPER(?)  OR (? is null))" +
                " AND  (UPPER(t.jiraCp) = UPPER(?) OR (? is null)) " +
                " AND (UPPER(t.sow) = UPPER(?) OR (? is null))" +
                " AND (UPPER(t.po) = UPPER(?) OR (? is null))" +
                " AND (TRUNC(t.dataInicial) = TRUNC(?) " +
                "   or TRUNC(?) between TRUNC(t.dataInicial) and TRUNC(t.dataFinal) " +
                "   or TRUNC(?) between TRUNC(t.dataInicial) and TRUNC(t.dataFinal)) " +
                " AND t.msa.codigoMsa = ? " +
                " AND t.deleteLogico = 'N' ")
})
public class MsaContrato {

    public static final String FIND_BY_FILTER = "MsaContrato.findByFilter";

    public static final String FIND_BY_MSA = "MsaContrato.findByMsa";

    public static final String FIND_BY_MSA_AND_INDICADOR_DEFAULT_PESQUISA = "MsaContrato.findByMsaAndIndicadorDefaultPesquisa";

    public static final String FIND_BY_JIRA_CP = "MsaContrato.findByJiraCp";

    public static final String FIND_BY_JIRA_LEGAL = "MsaContrato.findByJiraLegal";

    public static final String FIND_BY_UNIQUE_KEY = "MsaContrato.findByUniqueKey";

    @Id
    @GeneratedValue(generator = "MsaContratoSeq")
    @SequenceGenerator(name = "MsaContratoSeq", sequenceName = "SQ_MSCO_CD_MSA_CONTRATO", allocationSize = 1)
    @Column(name = "MSCO_CD_MSA_CONTRATO", unique = true, nullable = false, precision = 18, scale = 0)
    private long codigo;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_NM_DESCRICAO_PROJETO", nullable = false, length = 200)
    private String descricao;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_NM_JIRA_CP", nullable = false, length = 10)
    private String jiraCp;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_NM_JIRA_LEGAL", length = 10)
    private String jiraLegal;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_NM_SOW", length = 20)
    private String sow;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_NM_PO", length = 20)
    private String po;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MSTC_CD_MSA_TIPO_CONTRATO")
    private MsaTipoContrato tipoContrato;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MOED_CD_MOEDA")
    private Moeda moeda;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_IN_FTE", nullable = false, length = 1)
    @Type(type = "yes_no")
    private Boolean fte;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_IN_HAS_LIMIT", nullable = false, length = 1)
    @Type(type = "yes_no")
    private Boolean hasLimit;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_VL_TOTAL_CONTRATO", precision = 18, scale = 4)
    private Double totalContrato;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MTMD_CD_MSA_TIPO_MONT_DESP")
    private MsaTipoMontanteDespesa tipoDespesa;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_VL_DESPESA_CONTRATO", precision = 18, scale = 4)
    private Double valorDespesa;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_PR_DESPESA_CONTRATO", precision = 5, scale = 2)
    private Double porcentagemDespesa;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MSSC_CD_MSA_STATUS_CONTR")
    private MsaStatusContrato status;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Temporal(TemporalType.DATE)
    @Column(name = "MSCO_DT_INICIAL", nullable = false, length = 7)
    private Date dataInicial;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Temporal(TemporalType.DATE)
    @Column(name = "MSCO_DT_FINAL", length = 7)
    private Date dataFinal;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_IN_INDETERMINADO", length = 1)
    @Type(type = "yes_no")
    private Boolean dataIndeterminada;

    @NotAudited
    @OneToMany(mappedBy="codigoMsaContrato", fetch=FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MsaContratoTipoServico> tipoServicos;

    @NotAudited
    @OneToMany(mappedBy="codigoMsaContrato", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<MsaContratoFilial> filials;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_TX_NOTES", length = 250)
    private String comentarios;

    @NotAudited
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MSAA_CD_MSA")
    private Msa msa;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_IN_DELETE_LOGICO")
    @Type(type = "yes_no")
    private Boolean deleteLogico;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_DT_ATUALIZACAO")
    private Date dataAtualizacao;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_CD_LOGIN_ATUALIZACAO")
    private String loginAtualizacao;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "MSCO_TX_MES_IPCA")
    private String monthOfReadjustment;

    @Transient
    private String nomeTipoServicos;

    @Transient
    private String nomeFiliais;

    public Boolean getHasLimit() {
        return hasLimit;
    }

    public void setHasLimit(Boolean hasLimit) {
        this.hasLimit = hasLimit;
    }

    public Boolean getDeleteLogico() {
        return deleteLogico;
    }

    public void setDeleteLogico(Boolean deleteLogico) {
        this.deleteLogico = deleteLogico;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getLoginAtualizacao() {
        return loginAtualizacao;
    }

    public void setLoginAtualizacao(String loginAtualizacao) {
        this.loginAtualizacao = loginAtualizacao;
    }

    public String getNomeFiliais() {
        String nomes = "";
        for (MsaContratoFilial mcf : this.getFilials()) {
            nomes = nomes.concat(mcf.getFilial().getNomeEmpresa()).concat(", ");
        }
        return  this.filials.isEmpty() ? nomes : nomes.substring(0, nomes.length()-2);
    }

    public String getNomeTipoServicos() {
        String nomes = "";
        for (MsaContratoTipoServico mcts : this.getTipoServicos()) {
            nomes = nomes.concat(mcts.getTipoServico().getNomeTipoServico()).concat(", ");
        }
        return this.tipoServicos.isEmpty() ? nomes : nomes.substring(0, nomes.length()-2);
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getJiraCp() {
        return jiraCp;
    }

    public void setJiraCp(String jiraCp) {
        this.jiraCp = jiraCp;
    }

    public String getJiraLegal() {
        if (jiraLegal == null) {
            return "";
        }
        if (jiraLegal.trim().equalsIgnoreCase("-")) {
            return "";
        }
        return jiraLegal.length() == 0 ? "" : jiraLegal.trim();
    }

    public void setJiraLegal(String jiraLegal) {
        if (jiraLegal != null && jiraLegal.trim().equalsIgnoreCase("-")) {
            this.jiraLegal = "";
        } else {
            this.jiraLegal = jiraLegal;
        }
    }

    public String getSow() {
        if (sow == null) {
            return "";
        }
        if (sow.trim().equalsIgnoreCase("-")) {
            return "";
        }
        return sow.length() == 0 ? "" : sow.trim();
    }

    public void setSow(String sow) {
        if (sow != null && sow.trim().equalsIgnoreCase("-")) {
            this.sow = "";
        } else {
            this.sow = sow;
        }
    }

    public String getPo() {
        if (po == null) {
            return "";
        }
        if (po.trim().equalsIgnoreCase("-")) {
            return "";
        }
        return po.length() == 0 ? "" : po.trim();
    }

    public void setPo(String po) {
        if (po != null && po.trim().equalsIgnoreCase("-")) {
            this.po = "";
        } else {
            this.po = po;
        }
    }

    public MsaTipoContrato getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(MsaTipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Moeda getMoeda() {
        return moeda;
    }

    public void setMoeda(Moeda moeda) {
        this.moeda = moeda;
    }

    public Boolean getFte() {
        return fte;
    }

    public void setFte(Boolean fte) {
        this.fte = fte;
    }

    public Double getTotalContrato() {
        return totalContrato == null ? 0.0 : totalContrato;
    }

    public void setTotalContrato(Double totalContrato) {
        this.totalContrato = totalContrato;
    }

    public MsaTipoMontanteDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    public void setTipoDespesa(MsaTipoMontanteDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public Double getValorDespesa() {
        return valorDespesa == null ? 0.0 : valorDespesa;
    }

    public void setValorDespesa(Double valorDespesa) {
        this.valorDespesa = valorDespesa;
    }

    public Double getPorcentagemDespesa() {
        return porcentagemDespesa == null ? 0.0 : porcentagemDespesa;
    }

    public void setPorcentagemDespesa(Double porcentagemDespesa) {
        this.porcentagemDespesa = porcentagemDespesa;
    }

    public MsaStatusContrato getStatus() {
        return status;
    }

    public void setStatus(MsaStatusContrato status) {
        this.status = status;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Boolean getDataIndeterminada() {
        return dataIndeterminada;
    }

    public void setDataIndeterminada(Boolean dataIndeterminada) {
        this.dataIndeterminada = dataIndeterminada;
    }

    public List<MsaContratoTipoServico> getTipoServicos() {
        return tipoServicos;
    }

    public void setTipoServicos(List<MsaContratoTipoServico> tipoServicos) {
        this.tipoServicos = tipoServicos;
    }

    public List<MsaContratoFilial> getFilials() {
        return filials;
    }

    public void setFilials(List<MsaContratoFilial> filials) {
        this.filials = filials;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Msa getMsa() { return msa; }

    public void setMsa(Msa msa) { this.msa = msa; }

    public String getMonthOfReadjustment() {
        return monthOfReadjustment;
    }

    public void setMonthOfReadjustment(String monthOfReadjustment) {
        this.monthOfReadjustment = monthOfReadjustment;
    }
}