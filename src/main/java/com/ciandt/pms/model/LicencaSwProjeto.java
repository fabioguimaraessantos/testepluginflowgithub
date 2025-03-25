/*
 * @(#) Receita.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "LICENCA_SW_PROJETO")
@NamedQueries({
    @NamedQuery(name = "LicencaSwProjeto.findLicencaByTiRecurso", query = "SELECT lsp FROM LicencaSwProjeto lsp WHERE lsp.tiRecurso.codigoTiRecurso = :codigoTiRecurso"),
        @NamedQuery(name = "findLicencaByTiRecurso2", query = "SELECT lsp FROM LicencaSwProjeto lsp WHERE lsp.codigoLicencaSwProjeto = :codigoTiRecurso"),
        @NamedQuery(name = "LicencaSwProjeto.findAll", query = "SELECT t FROM LicencaSwProjeto t "),
        @NamedQuery(name = "LicencaSwProjeto.findLicencaByCodigoProcurify", query = "SELECT t FROM LicencaSwProjeto t WHERE t.codigoProcurify = ?"),
        @NamedQuery(name = "LicencaSwProjeto.findByFilter", query = "SELECT t FROM LicencaSwProjeto t WHERE" +
                "((t.tiRecurso.codigoTiRecurso in (:codigosTiRecurso)) OR (:codigosTiRecursoNull = 'Y')) " +
                "AND (t.codigoProcurify = :codigoProcurify OR :codigoProcurify = null) " +
                "AND ((t.notaFiscal = :notaFiscal) OR (t.notaFiscal is null AND null = :notaFiscal) OR (:notaFiscal = 0L))"
        ),
        @NamedQuery(name = "LicencaSwProjeto.findProjetosUtilizados",
                query = "SELECT t FROM LicencaSwProjeto t WHERE t.notaFiscal = :numeroNota AND t.providerCode = :codigoErpFornecedor"),
})
@SqlResultSetMappings(@SqlResultSetMapping(name = "scalarLicencaSwProjeto"))
@NamedNativeQueries({
        @NamedNativeQuery(name = "LicencaSwProjeto.findProjectByFilter", query = "SELECT " +
                " DISTINCT COD_PROJECT, PROJECT " +
                "FROM " +
                " ( " +
                "SELECT CP.COPR_CD_CONTRATO_PRATICA AS COD_PROJECT, CP.COPR_NM_CONTRATO_PRATICA AS PROJECT FROM PMS20.Licenca_Sw_Projeto t " +
                "LEFT JOIN  PMS20.CONTRATO_PRATICA CP ON T.COPR_CD_CONTRATO_PRATICA = CP.COPR_CD_CONTRATO_PRATICA " +
                "WHERE T.COPR_CD_CONTRATO_PRATICA IS NOT NULL " +
                "AND T.LIPR_DT_START_DATE BETWEEN :searchStartDate AND :searchEndDate " +
                "AND (LIPR_NOTA_FISCAL = :invoiceNumber OR :invoiceNumber = 0) " +
                "UNION ALL " +
                "SELECT GP.GRCU_CD_GRUPO_CUSTO AS COD_PROJECT, GRCU_NM_GRUPO_CUSTO AS PROJECT FROM PMS20.Licenca_Sw_Projeto t " +
                "LEFT JOIN PMS20.GRUPO_CUSTO GP ON T.GRCU_CD_GRUPO_CUSTO = GP.GRCU_CD_GRUPO_CUSTO " +
                "WHERE T.GRCU_CD_GRUPO_CUSTO IS NOT NULL " +
                "AND T.LIPR_DT_START_DATE BETWEEN :searchStartDate AND :searchEndDate " +
                "AND (LIPR_NOTA_FISCAL = :invoiceNumber OR :invoiceNumber = 0) " +
                "    ) " +
                "order by PROJECT",resultSetMapping = "scalarLicencaSwProjeto"),
        @NamedNativeQuery(name = "LicencaSwProjeto.findProjectByDataDaParcelaAndFilter", query =
                " SELECT  DISTINCT COD_PROJECT, PROJECT " +
                " FROM (SELECT cp.COPR_CD_CONTRATO_PRATICA AS COD_PROJECT, cp.COPR_NM_CONTRATO_PRATICA AS PROJECT " +
                "       FROM PMS20.LICENCA_SW_PROJETO ls, " +
                "            PMS20.LICENCA_SW_PROJETO_PARCELA lsp, " +
                "            PMS20.CONTRATO_PRATICA cp " +
                "       WHERE ls.LIPR_CD_LICENCA_SW_PROJETO = lsp.LIPR_CD_LICENCA_SW_PROJETO " +
                "        AND lsp.COPR_CD_CONTRATO_PRATICA   = cp.COPR_CD_CONTRATO_PRATICA " +
                "        AND lsp.COPR_CD_CONTRATO_PRATICA is not null " +
                "        AND lsp.LIPP_DATE = :monthDate " +
                "        AND (ls.LIPR_NOTA_FISCAL = :invoiceNumber OR :invoiceNumber = 0) " +
                "UNION ALL " +
                "       SELECT gc.GRCU_CD_GRUPO_CUSTO AS COD_PROJECT, gc.GRCU_NM_GRUPO_CUSTO AS PROJECT " +
                "       FROM PMS20.LICENCA_SW_PROJETO ls, " +
                "            PMS20.LICENCA_SW_PROJETO_PARCELA lsp, " +
                "            PMS20.GRUPO_CUSTO gc " +
                "       WHERE ls.LIPR_CD_LICENCA_SW_PROJETO = lsp.LIPR_CD_LICENCA_SW_PROJETO " +
                "        AND lsp.GRCU_CD_GRUPO_CUSTO = gc.GRCU_CD_GRUPO_CUSTO " +
                "        AND lsp.GRCU_CD_GRUPO_CUSTO is not null " +
                "        AND lsp.LIPP_DATE = :monthDate  " +
                "        AND (ls.LIPR_NOTA_FISCAL = :invoiceNumber OR :invoiceNumber = 0) " +
                ") order by PROJECT", resultSetMapping = "scalarLicencaSwProjeto"),
        @NamedNativeQuery(name = "LicencaSwProjeto.findInvoiceByDate", query = "SELECT DISTINCT LIPR_NOTA_FISCAL FROM PMS20.LICENCA_SW_PROJETO t " +
                "WHERE t.LIPR_DT_START_DATE BETWEEN :searchStartDate AND :searchEndDate " +
                "ORDER BY LIPR_NOTA_FISCAL",resultSetMapping = "scalarLicencaSwProjeto"),
        @NamedNativeQuery(name = "LicencaSwProjeto.findInvoiceByDataDaParcela", query =
                "SELECT DISTINCT LIPR_NOTA_FISCAL " +
                "FROM PMS20.LICENCA_SW_PROJETO ls, PMS20.LICENCA_SW_PROJETO_PARCELA lsp "   +
                "WHERE ls.LIPR_CD_LICENCA_SW_PROJETO = lsp.LIPR_CD_LICENCA_SW_PROJETO AND " +
                "      lsp.LIPP_DATE = :monthDate "          +
                "ORDER BY LIPR_NOTA_FISCAL",resultSetMapping = "scalarLicencaSwProjeto")

})

public class LicencaSwProjeto implements java.io.Serializable {

    public static final String FIND_BY_TIRECURSO = "LicencaSwProjeto.findLicencaByTiRecurso";

    public static final String FIND_ALL = "LicencaSwProjeto.findAll";

    public static final String FIND_BY_CODIGO_PROCURIFY = "LicencaSwProjeto.findLicencaByCodigoProcurify";

    public static final String FIND_BY_FILTER = "LicencaSwProjeto.findByFilter";

    public static final String FIND_PROJETOS_UTILIZADOS = "LicencaSwProjeto.findProjetosUtilizados";

    public static final String FIND_PROJECT_BY_FILTER = "LicencaSwProjeto.findProjectByFilter";

    public static final String FIND_PROJECT_BY_DATA_DA_PARCELA_AND_FILTER = "LicencaSwProjeto.findProjectByDataDaParcelaAndFilter";

    public static final String FIND_INVOICE_BY_DATE = "LicencaSwProjeto.findInvoiceByDate";

    public static final String FIND_INVOICE_BY_DATA_DA_PARCELA = "LicencaSwProjeto.findInvoiceByDataDaParcela";

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "LicencaSwProjetoSeq")
    @SequenceGenerator(name = "LicencaSwProjetoSeq", sequenceName = "SQ_LIPR_CD_LICENCA_SW_PROJETO", allocationSize = 1)
    @Column(name = "LIPR_CD_LICENCA_SW_PROJETO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoLicencaSwProjeto;

    @Temporal(TemporalType.DATE)
    @Column(name = "LIPR_DT_START_DATE", nullable = false, length = 7)
    private Date dataInicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TIRE_CD_TI_RECURSO")
    private TiRecurso tiRecurso;

    @Column(name = "LIPR_TICKET_ID", length = 50)
    private String ticketId;

    @Column(name = "LIPR_NOTA_FISCAL", precision = 18, scale = 0)
    private Long notaFiscal;

    @Column(name = "LIPR_DESCRICAO", length = 2000)
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MOED_CD_MOEDA", nullable = true)
    private Moeda moeda;

    @Column(name = "LIPR_VALOR_TOTAL", precision = 22, scale = 0)
    private BigDecimal valorTotal;

    @Column(name = "LIPR_QUANTIDADE_PARECELAS", precision = 2, scale = 0)
    private Integer quantidadeParcela;

    @Column(name = "LIPR_CD_PROCURIFY", length = 100)
    private String codigoProcurify;

    @Column(name = "COPR_CD_CONTRATO_PRATICA", precision = 18, scale = 0)
    private Long cdClob;

    @Column(name = "GRCU_CD_GRUPO_CUSTO", precision = 18, scale = 0)
    private Long cdCC;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMPR_CD_EMPRESA")
    private Empresa empresa;

    @Column(name = "LIPR_CD_ERP_PROVIDER", precision = 18, scale = 0)
    private Long providerCode;

    @Column(name = "LIPR_NM_ERP_PROVIDER", length = 200)
    private String providerName;

    @Column(name = "LIPR_CD_ERP_PROJECT", precision = 18, scale = 0)
    private Long erpProjectCode;


    @Column(name = "LIPR_NM_ERP_TI_RECURSO", length = 200)
    private String tiRecursoErpName;

    @Transient
    private Long licencaSwProjetoParcela;

    @Transient
    private String clobs;

    @Transient
    private String logins;

    @Transient
    private Boolean hasIntegratedInstallments;

    @Transient
    private Boolean hasOpenedInstallments;

    @Transient
    private Boolean isExcludable;

    /**
     * Construtor default.
     */
    public LicencaSwProjeto() {
    }

    public Long getCodigoLicencaSwProjeto() {
        return codigoLicencaSwProjeto;
    }

    public void setCodigoLicencaSwProjeto(Long codigoLicencaSwProjeto) {
        this.codigoLicencaSwProjeto = codigoLicencaSwProjeto;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public TiRecurso getTiRecurso() {
        return tiRecurso;
    }

    public void setTiRecurso(TiRecurso tiRecurso) {
        this.tiRecurso = tiRecurso;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Long getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(Long notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Moeda getMoeda() {
        return moeda;
    }

    public void setMoeda(Moeda moeda) {
        this.moeda = moeda;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getQuantidadeParcela() {
        return quantidadeParcela;
    }

    public void setQuantidadeParcela(Integer quantidadeParcela) {
        this.quantidadeParcela = quantidadeParcela;
    }

    public String getClobs() {
        return clobs;
    }

    public void setClobs(String clobs) {
        this.clobs = clobs;
    }

    public String getLogins() {
        return logins;
    }

    public void setLogins(String logins) { this.logins = logins; }

    public String getCodigoProcurify() {
        return codigoProcurify;
    }

    public void setCodigoProcurify(String codigoProcurify) {
        this.codigoProcurify = codigoProcurify;
    }

    public Boolean getHasIntegratedInstallments() {
        return hasIntegratedInstallments;
    }

    public void setHasIntegratedInstallments(Boolean hasIntegratedInstallments) {
        this.hasIntegratedInstallments = hasIntegratedInstallments;
    }

    public Long getCdClob() {
        return cdClob;
    }

    public void setCdClob(Long cdClob) {
        this.cdClob = cdClob;
    }

    public Long getCdCC() {
        return cdCC;
    }

    public void setCdCC(Long cdCC) {
        this.cdCC = cdCC;
    }

    public Boolean getHasOpenedInstallments() {
        return hasOpenedInstallments;
    }

    public void setHasOpenedInstallments(Boolean hasOpenedInstallments) {
        this.hasOpenedInstallments = hasOpenedInstallments;
    }

    public Boolean getIsExcludable() {
        return isExcludable;
    }

    public void setIsExcludable(Boolean excludable) {
        isExcludable = excludable;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Long getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(Long providerCode) {
        this.providerCode = providerCode;
    }

    public String getTiRecursoErpName() {
        return tiRecursoErpName;
    }

    public void setTiRecursoErpName(String tiRecursoErpName) {
        this.tiRecursoErpName = tiRecursoErpName;
    }

    public Long getErpProjectCode() {
        return erpProjectCode;
    }

    public void setErpProjectCode(Long erpProjectCode) {
        this.erpProjectCode = erpProjectCode;
    }

    public Long getLicencaSwProjetoParcela() {
        return licencaSwProjetoParcela;
    }

    public void setLicencaSwProjetoParcela(Long licencaSwProjetoParcela) {
        this.licencaSwProjetoParcela = licencaSwProjetoParcela;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}