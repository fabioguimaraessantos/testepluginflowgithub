package com.ciandt.pms.model;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "LICENCA_SW_PROJETO_PARCELA")
@SqlResultSetMappings(@SqlResultSetMapping(name = "scalarLicencaSwProjetoParcela"))
@NamedNativeQueries({
        @NamedNativeQuery(name = "LicencaSwProjetoParcela.findInstallmentValue", query = "select sum(lspp.lipp_valor_parcela) from pms20.licenca_sw_projeto_parcela lspp " +
                " join pms20.licenca_sw_projeto lsp on lspp.lipr_cd_licenca_sw_projeto = lsp.lipr_cd_licenca_sw_projeto "  +
                " where trunc(lspp.lipp_date, 'MONTH') = TRUNC(:dataParcela,'MONTH') " +
                " and lsp.lipr_cd_licenca_sw_projeto = :codigoLicencaSwProjeto " +
                " and lspp.lipp_numero_parcela = :numeroParcela ", resultSetMapping = "scalarLicencaSwProjetoParcela"),
        @NamedNativeQuery(name = "LicencaSwProjetoParcela.findAllInstallmentsCurrentMonth", query = "select " +
                "    lp.lipr_cd_licenca_sw_projeto, " +
                "    tr.TIRE_NM_TI_RECURSO, " +
                "    to_char(lp.lipr_nota_fiscal) lipr_nota_fiscal, " +
                "    nvl((lp.LIPR_QUANTIDADE_PARECELAS - lpp.LIPP_NUMERO_PARCELA), '0') saldo, " +
                "    lpp.lipp_valor_parcela, " +
                "    lp.lipr_dt_start_date, " +
                "    gc.erp_cd_ccusto, " +
                "    gc.grcu_nm_grupo_custo, " +
                "    c.conv_cd_projeto_mega, " +
                "    c.conv_nm_projeto_mega, " +
                "    LISTAGG(pe.pess_cd_login, ', ') WITHIN GROUP(order by pe.pess_cd_login) logins, " +
                "    BM.celu_nm_centro_lucro BM, " +
                "    p.PESS_CD_LOGIN PM, " +
                "    lp.lipr_descricao " +
                "from pms20.TI_RECURSO tr " +
                "    inner join pms20.LICENCA_SW_PROJETO lp ON lp.TIRE_CD_TI_RECURSO = tr.TIRE_CD_TI_RECURSO " +
                "    inner join pms20.LICENCA_SW_PROJETO_PARCELA lpp ON lpp.LIPR_CD_LICENCA_SW_PROJETO = lp.LIPR_CD_LICENCA_SW_PROJETO " +
                "    inner join pms20.CONVERGENCIA c ON (c.COPR_CD_CONTRATO_PRATICA = lpp.COPR_CD_CONTRATO_PRATICA AND lpp.GRCU_CD_GRUPO_CUSTO is null) " +
                "                                OR (c.GRCU_CD_GRUPO_CUSTO = lpp.GRCU_CD_GRUPO_CUSTO AND lpp.COPR_CD_CONTRATO_PRATICA is null AND c.COPR_CD_CONTRATO_PRATICA is null) " +
                "    inner join pms20.grupo_custo gc on gc.grcu_cd_grupo_custo = c.grcu_cd_grupo_custo " +
                "    left join pms20.contrato_pratica cp on cp.copr_cd_contrato_pratica = lpp.copr_cd_contrato_pratica " +
                "    left join pms20.grupo_custo gc on gc.grcu_cd_grupo_custo = lpp.grcu_cd_grupo_custo " +
                "    left join pms20.pessoa p on p.PESS_CD_PESSOA = nvl(cp.PESS_CD_GERENTE, gc.pess_cd_gerente) " +
                "    left join ( select cp.copr_cd_contrato_pratica, cp.copr_nm_contrato_pratica, cl.celu_nm_centro_lucro " +
                "                            from pms20.contrato_pratica cp " +
                "                       left join pms20.contrato_pratica_centro_lucro cpcl on cpcl.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica and cpcl.cpcl_dt_fim_vigencia is null  " +
                "                       inner join pms20.centro_lucro cl on cl.celu_cd_centro_lucro = cpcl.celu_cd_centro_lucro and cl.nacl_cd_natureza = 3) BM on bm.copr_cd_contrato_pratica = lpp.copr_cd_contrato_pratica " +
                "        left join pms20.licenca_sw_projeto_pessoa lpe ON lpe.lipr_cd_licenca_sw_projeto = lp.lipr_cd_licenca_sw_projeto " +
                "        left join pms20.pessoa pe ON pe.pess_cd_pessoa = lpe.pess_cd_pessoa " +
                "where tr.TIRE_IN_ATIVO = 'A' " +
                "  and lpp.LIPP_DATE = trunc(sysdate,'MM') " +
                "    group by lp.lipr_cd_licenca_sw_projeto, " +
                "            tr.TIRE_NM_TI_RECURSO, " +
                "            to_char(lp.lipr_nota_fiscal), " +
                "            nvl((lp.LIPR_QUANTIDADE_PARECELAS - lpp.LIPP_NUMERO_PARCELA), '0'), " +
                "            lpp.lipp_valor_parcela, " +
                "            lp.lipr_dt_start_date, " +
                "            gc.erp_cd_ccusto, " +
                "            gc.grcu_nm_grupo_custo, " +
                "            c.conv_cd_projeto_mega, " +
                "            c.conv_nm_projeto_mega, " +
                "            BM.celu_nm_centro_lucro, " +
                "            p.PESS_CD_LOGIN, " +
                "            lp.lipr_descricao" , resultSetMapping = "scalarLicencaSwProjetoParcela"),
        @NamedNativeQuery(name = "LicencaSwProjetoParcela.findByStartDateAndCodigoLicencaProjeto", query = "select \n" +
                                "        to_char(lpp.lipp_date,'MM') month, lp.lipr_cd_licenca_sw_projeto,\n" +
                                "        tr.TIRE_NM_TI_RECURSO, \n" +
                                "        to_char(lp.lipr_nota_fiscal) lipr_nota_fiscal, \n" +
                                "        lp.LIPR_VALOR_TOTAL,  \n" +
                                "        lpp.lipp_in_status, \n" +
                                "        lp.LIPR_QUANTIDADE_PARECELAS, \n" +
                                "        lpp.lipp_valor_parcela,\n" +
                                "        lpp.LIPP_NUMERO_PARCELA,\n" +
                                "        NVL(apr.apropriated, 0) VALOR_APROPRIADO,\n" +
                                "        nvl(bal.balance, 0) balance,\n" +
                                "        lp.lipr_dt_start_date,\n" +
                                "        gc.erp_cd_ccusto,\n" +
                                "        gc.grcu_nm_grupo_custo,\n" +
                                "        c.conv_cd_projeto_mega,\n" +
                                "        c.conv_nm_projeto_mega,\n" +
                                "        lp.lipr_descricao,\n" +
                                "        nvl((lp.LIPR_QUANTIDADE_PARECELAS - lpp.LIPP_NUMERO_PARCELA), 0) saldo, \n" +
                                "        BM.celu_nm_centro_lucro BM,\n" +
                                "        p.PESS_CD_LOGIN PM, \n" +
                                "        (select LISTAGG(p.pess_cd_login,', ') within group (order by p.pess_cd_login) \n" +
                                "        from pms20.licenca_sw_projeto_pessoa lspess \n" +
                                "        join pms20.pessoa p on lspess.pess_cd_pessoa = p.pess_cd_pessoa \n" +
                                "        where lspess.lipr_cd_licenca_sw_projeto = lp.lipr_cd_licenca_sw_projeto) logins \n" +
        " from pms20.TI_RECURSO tr\n" +
                                "    inner join pms20.LICENCA_SW_PROJETO lp ON lp.TIRE_CD_TI_RECURSO = tr.TIRE_CD_TI_RECURSO\n" +
                                "    inner join pms20.LICENCA_SW_PROJETO_PARCELA lpp ON lpp.LIPR_CD_LICENCA_SW_PROJETO = lp.LIPR_CD_LICENCA_SW_PROJETO\n" +
                                "    inner join pms20.CONVERGENCIA c ON (c.COPR_CD_CONTRATO_PRATICA = lpp.COPR_CD_CONTRATO_PRATICA AND lpp.GRCU_CD_GRUPO_CUSTO is null)\n" +
                                "                                OR (c.GRCU_CD_GRUPO_CUSTO = lpp.GRCU_CD_GRUPO_CUSTO AND lpp.COPR_CD_CONTRATO_PRATICA is null AND c.COPR_CD_CONTRATO_PRATICA is null)\n" +
                                "    inner join pms20.grupo_custo gc on gc.grcu_cd_grupo_custo = c.grcu_cd_grupo_custo\n" +
                                "    left join (SELECT lsp.lipr_cd_licenca_sw_projeto, \n" +
                                "                    sum(lsp.lipp_valor_parcela) apropriated\n" +
                                "                FROM pms20.LICENCA_SW_PROJETO_PARCELA lsp\n" +
                                "                    where lsp.LIPP_IN_STATUS = 'INTEGRADO'\n" +
                                "                    group by lsp.lipr_cd_licenca_sw_projeto) apr on apr.lipr_cd_licenca_sw_projeto = lpp.lipr_cd_licenca_sw_projeto\n" +
                                "    left join (SELECT lp.lipr_cd_licenca_sw_projeto, \n" +
                                "                    sum(lp.lipp_valor_parcela) balance\n" +
                                "                FROM pms20.LICENCA_SW_PROJETO_PARCELA lp\n" +
                                "                    where lp.LIPP_IN_STATUS <> 'INTEGRADO'\n" +
                                "                    group by lp.lipr_cd_licenca_sw_projeto) bal on bal.lipr_cd_licenca_sw_projeto = lpp.lipr_cd_licenca_sw_projeto \n" +
                                "   left join pms20.contrato_pratica cp on cp.copr_cd_contrato_pratica = lpp.copr_cd_contrato_pratica\n" +
                                "   left join pms20.pessoa p on p.PESS_CD_PESSOA = nvl(cp.PESS_CD_GERENTE, gc.pess_cd_gerente)\n" +
                                "   left join ( select cp.copr_cd_contrato_pratica, cp.copr_nm_contrato_pratica, cl.celu_nm_centro_lucro \n" +
                                "                                   from pms20.contrato_pratica cp \n" +
                                "                              left join pms20.contrato_pratica_centro_lucro cpcl on cpcl.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica and cpcl.cpcl_dt_fim_vigencia is null \n" +
                                "                              inner join pms20.centro_lucro cl on cl.celu_cd_centro_lucro = cpcl.celu_cd_centro_lucro and cl.nacl_cd_natureza = 3) BM on bm.copr_cd_contrato_pratica = lpp.copr_cd_contrato_pratica\n" +
                "where tr.TIRE_IN_ATIVO = 'A' \n" +
                                "  and lpp.LIPP_DATE = :dataInicio \n" +
                                "  and (lp.tire_cd_ti_recurso = :codigoTiRecurso or :codigoTiRecurso = 0) \n" +
                                "  and (lpp.lipp_in_status = :status or :status is null) ", resultSetMapping = "scalarLicencaSwProjetoParcela")})
@NamedQueries({
        @NamedQuery(name = "LicencaSwProjetoParcela.findByLicencaSwProjetoAndMonth", query = "SELECT distinct lspp FROM LicencaSwProjetoParcela lspp "
                + "JOIN FETCH lspp.licencaSwProjeto lsp "
                + "WHERE ( TRUNC(lspp.dataParcela,'MONTH') = TRUNC(?,'MONTH') ) "
                + "AND lsp.codigoLicencaSwProjeto = ? "),
        @NamedQuery(name = "LicencaSwProjetoParcela.findBalanceByLicencaSwProjetoAndMonth", query = "SELECT sum(l.valorParcela) FROM LicencaSwProjetoParcela l "
                + "WHERE  licencaSwProjeto.codigoLicencaSwProjeto = :codigoLicencaSwProjeto "
                + "AND l.status != 'INTEGRADO' "),
        @NamedQuery(name = "LicencaSwProjetoParcela.findBalanceToAppropriateByLicencaSwProjetoAndMonth", query = "SELECT sum(l.valorParcela) FROM LicencaSwProjetoParcela l "
                + "WHERE  licencaSwProjeto.codigoLicencaSwProjeto = :codigoLicencaSwProjeto "
                + " AND ( TRUNC(l.dataParcela,'MONTH') >= TRUNC(:month,'MONTH') ) "
                + "AND l.status != 'INTEGRADO' "),
        @NamedQuery(name = "LicencaSwProjetoParcela.findByLicencaSwProjeto", query = "SELECT lspp FROM LicencaSwProjetoParcela lspp "
                + "WHERE licencaSwProjeto.codigoLicencaSwProjeto = :codigoLicencaSwProjeto"),
        @NamedQuery(name = "LicencaSwProjetoParcela.findByCodigoIn", query = "SELECT lpp FROM LicencaSwProjetoParcela lpp "
                + "WHERE lpp.licencaSwProjeto.codigoLicencaSwProjeto IN (:licencaSwProjetoCodigos)"
                + "  AND lpp.dataParcela = :monthDate"),
        @NamedQuery(name = "LicencaSwProjetoParcela.findByLicencaSwProjetoAndStatus", query = "SELECT lspp FROM LicencaSwProjetoParcela lspp "
                + "WHERE lspp.licencaSwProjeto.codigoLicencaSwProjeto = :codigoLicencaSwProjeto "
                + "AND lspp.status = :statusLicencaSwProjetoParcela "
        ),
        @NamedQuery(name = "LicencaSwProjetoParcela.findByLicencaSwProjetoAndMonthGreaterThan", query = "SELECT lspp FROM LicencaSwProjetoParcela lspp "
                + " WHERE licencaSwProjeto.codigoLicencaSwProjeto = :codigoLicencaSwProjeto "
                + "   AND lspp.dataParcela >= :beginDate "),
        @NamedQuery(name = "LicencaSwProjetoParcela.findLicenseIdByDateStart", query = "SELECT lspp.codigoLicencaSwProjetoParcela FROM LicencaSwProjetoParcela lspp "
                + "INNER JOIN lspp.licencaSwProjeto t "
                + "WHERE t.dataInicio BETWEEN :searchStartDate AND :searchEndDate  "
                + "AND lspp.dataParcela = t.dataInicio "
                + "AND (t.notaFiscal = :invoiceNumber OR :invoiceNumber = null) "
                + "AND ((t.cdClob = :project OR t.cdCC = :project) OR :project = null) "
                + " ORDER BY lspp.codigoLicencaSwProjetoParcela "),
        @NamedQuery(name = "LicencaSwProjetoParcela.findByFilter", query =
                "  SELECT lspp FROM LicencaSwProjetoParcela lspp "
                + "INNER JOIN lspp.licencaSwProjeto t "
                + "WHERE lspp.dataParcela = :monthDate "
                + "AND (t.tiRecurso.codigoTiRecurso = :codTiRecurso OR :codTiRecurso = 0) "
                + "AND (lspp.status = :status OR :status is null) "
                + "AND (t.notaFiscal = :invoiceNumber OR :invoiceNumber = 0) "
                + "AND ((lspp.contratoPratica.codigoContratoPratica = :project OR lspp.grupoCusto.codigoGrupoCusto = :project) OR :project = 0) "
                + "AND (lspp.codigoLicencaSwProjetoParcela = :licenseId OR :licenseId = 0) "
                + " ORDER BY t.notaFiscal ASC, lspp.numeroParcela asc "),
        @NamedQuery(name = "LicencaSwProjetoParcela.findLicenseIdByDate", query = "SELECT lspp.codigoLicencaSwProjetoParcela FROM LicencaSwProjetoParcela lspp "
                + "INNER JOIN lspp.licencaSwProjeto t "
                + "WHERE lspp.dataParcela = :monthDate "
                + "AND (t.notaFiscal = :invoiceNumber OR :invoiceNumber = null) "
                + "AND ((lspp.contratoPratica.id = :project OR lspp.grupoCusto.id = :project) OR :project = null) "
                + " ORDER BY lspp.codigoLicencaSwProjetoParcela "),
        @NamedQuery(name = "LicencaSwProjetoParcela.findLicencaSwProjetoByDate", query = "SELECT DISTINCT t FROM LicencaSwProjetoParcela p " +
                "INNER JOIN p.licencaSwProjeto t " +
                "WHERE (t.tiRecurso.codigoTiRecurso in (:codigosTiRecurso) OR (:codigosTiRecursoNull = 'Y')) " +
                "AND (t.codigoProcurify = :codigoProcurify OR :codigoProcurify = null) " +
                "AND t.dataInicio BETWEEN :searchStartDate AND :searchEndDate " +
                "AND p.dataParcela = t.dataInicio " +
                "AND (t.notaFiscal = :invoiceNumber OR :invoiceNumber = null) " +
                "AND ((t.cdClob = :project OR t.cdCC = :project) OR :project = null) " +
                "AND (P.codigoLicencaSwProjetoParcela = :licenseID OR :licenseID = null) " +
                "AND (t.descricao = :resourceName OR :resourceName = null) " +
                "AND ((t.notaFiscal = :notaFiscal) OR (t.notaFiscal is null AND :notaFiscal is null) OR (:notaFiscal = 0L))"),
        @NamedQuery(name = "LicencaSwProjetoParcela.findByLicencaSwProjetoAndMonthLessThan", query = "SELECT lspp FROM LicencaSwProjetoParcela lspp "
                + " WHERE licencaSwProjeto.codigoLicencaSwProjeto = :codigoLicencaSwProjeto "
                + "   AND lspp.dataParcela < :beginDate "
                + " ORDER BY lspp.numeroParcela DESC ")
})
public class LicencaSwProjetoParcela implements java.io.Serializable {

    public static final String FIND_BY_FILTER = "LicencaSwProjetoParcela.findByFilter";

    public static final String FIND_ALL_INSTAMENTS_CURRENT_MONTH = "LicencaSwProjetoParcela.findAllInstallmentsCurrentMonth";

    public static final String FIND_BY_LICENCA_SW_PROJETO_AND_MONTH = "LicencaSwProjetoParcela.findByLicencaSwProjetoAndMonth";

    public static final String FIND_BALANCE_BY_LICENCA_SW_PROJETO_AND_MONTH = "LicencaSwProjetoParcela.findBalanceByLicencaSwProjetoAndMonth";

    public static final String FIND_BALANCE_TO_APPROPRIATE_BY_LICENCA_SW_PROJETO_AND_MONTH = "LicencaSwProjetoParcela.findBalanceToAppropriateByLicencaSwProjetoAndMonth";

    public static final String FIND_BY_LICENCA_SW_PROJETO = "LicencaSwProjetoParcela.findByLicencaSwProjeto";

    public static final String FIND_INSTALLMENT_VALUE = "LicencaSwProjetoParcela.findInstallmentValue";

    public static final String FIND_BY_START_DATE_AND_CODIGO_LICENCA_SW_PROJETO = "LicencaSwProjetoParcela.findByStartDateAndCodigoLicencaProjeto";

    public static final String FIND_BY_CODIGO_IN = "LicencaSwProjetoParcela.findByCodigoIn";

    public static final String FIND_BY_LICENCA_SW_PROJETO_AND_STATUS = "LicencaSwProjetoParcela.findByLicencaSwProjetoAndStatus";

    public static final String FIND_BY_LICENCA_SW_PROJETO_AND_MONTH_GREATER_THAN = "LicencaSwProjetoParcela.findByLicencaSwProjetoAndMonthGreaterThan";

    public static final String FIND_LICENSEID_BY_DATE_START = "LicencaSwProjetoParcela.findLicenseIdByDateStart";

    public static final String FIND_LICENSEID_BY_DATE = "LicencaSwProjetoParcela.findLicenseIdByDate";

    public static final String FIND_LICENCA_SW_PROJETO_BY_DATE = "LicencaSwProjetoParcela.findLicencaSwProjetoByDate";

    public static final String FIND_BY_LICENCA_SW_PROJETO_AND_MONTH_LESS_THAN = "LicencaSwProjetoParcela.findByLicencaSwProjetoAndMonthLessThan";

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    public GrupoCusto getGrupoCusto() {
        return grupoCusto;
    }

    public void setGrupoCusto(GrupoCusto grupoCusto) {
        this.grupoCusto = grupoCusto;
    }

    public enum TipoParcela {
        CURTO("CURTO"), LONGO("LONGO");

        @Getter
        private String fieldName;

        TipoParcela(String fieldName) { this.fieldName = fieldName; }
    }

    public enum Status {
        AGUARDANDO_APROVACAO("AGUARDANDO_APROVACAO"), APROVADO("APROVADO"),
        PENDENTE("PENDENTE"), INTEGRADO("INTEGRADO"), ERRO("ERRO");

        @Getter
        private String fieldName;

        Status(String fieldName) { this.fieldName = fieldName; }
    }

    public enum StatusFromWeb {
        WAITING_APPROVAL("Waiting Approval"), APPROVED("Approved"),
        PENDING("Pending"), INTEGRATED("Integrated"), ERROR("Error");

        @Getter
        private String fieldName;

        StatusFromWeb(String fieldName) { this.fieldName = fieldName; }
    }

    @Id
    @GeneratedValue(generator = "LicencaSwProjetoParcelaSeq")
    @SequenceGenerator(name = "LicencaSwProjetoParcelaSeq", sequenceName = "SQ_LIPP_CD_LIC_SW_PROJ_PARC", allocationSize = 1)
    @Column(name = "LIPP_CD_LICENCA_SW_PROJ_PARC", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoLicencaSwProjetoParcela;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LIPR_CD_LICENCA_SW_PROJETO", nullable = false)
    private LicencaSwProjeto licencaSwProjeto;

    @Column(name = "LIPP_VALOR_PARCELA", precision = 22, scale = 0)
    private BigDecimal valorParcela;

    @Temporal(TemporalType.DATE)
    @Column(name = "LIPP_DATE", nullable = false, length = 7)
    private Date dataParcela;

    @Column(name = "LIPP_NUMERO_PARCELA", precision = 2, scale = 0)
    private Integer numeroParcela;

    @Column(name = "LIPP_TIPO_PARCELA", length = 5)
    private String tipoParcela;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COPR_CD_CONTRATO_PRATICA")
    private ContratoPratica contratoPratica;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GRCU_CD_GRUPO_CUSTO")
    private GrupoCusto grupoCusto;

    @Column(name = "LIPP_IN_STATUS", length = 20)
    private String status;

    @Column(name = "LIPP_TX_ERROR", length = 4000)
    private String textoError;

    @Transient
    private Long quantidadeClob;

    @Transient
    private String logins;

    /**
     * Construtor default.
     */
    public LicencaSwProjetoParcela() {
    }

    public LicencaSwProjetoParcela(LicencaSwProjeto licencaSwProjeto, ContratoPratica contratoPratica, Date dataParcela, Integer numeroParcela, BigDecimal valorParcela, String status) {
        this.contratoPratica = contratoPratica;
        this.dataParcela = dataParcela;
        this.licencaSwProjeto = licencaSwProjeto;
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.tipoParcela = TipoParcela.CURTO.getFieldName();
        this.status = status;
    }

    public LicencaSwProjetoParcela(LicencaSwProjeto licencaSwProjeto, GrupoCusto grupoCusto, Date dataParcela, Integer numeroParcela, BigDecimal valorParcela, String status) {
        this.grupoCusto = grupoCusto;
        this.dataParcela = dataParcela;
        this.licencaSwProjeto = licencaSwProjeto;
        this.numeroParcela = numeroParcela;
        this.valorParcela = valorParcela;
        this.tipoParcela = TipoParcela.CURTO.getFieldName();
        this.status = status;
    }

    public Long getCodigoLicencaSwProjetoParcela() {
        return codigoLicencaSwProjetoParcela;
    }

    public void setCodigoLicencaSwProjetoParcela(Long codigoLicencaSwProjetoParcela) {
        this.codigoLicencaSwProjetoParcela = codigoLicencaSwProjetoParcela;
    }

    public LicencaSwProjeto getLicencaSwProjeto() {
        return licencaSwProjeto;
    }

    public void setLicencaSwProjeto(LicencaSwProjeto licencaSwProjeto) {
        this.licencaSwProjeto = licencaSwProjeto;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public Date getDataParcela() {
        return dataParcela;
    }

    public void setDataParcela(Date dataParcela) {
        this.dataParcela = dataParcela;
    }

    public Integer getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(Integer numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public String getTipoParcela() {
        return tipoParcela;
    }

    public void setTipoParcela(String tipoParcela) {
        this.tipoParcela = tipoParcela;
    }

    public ContratoPratica getContratoPratica() {
        return contratoPratica;
    }

    public void setContratoPratica(ContratoPratica contratoPratica) {
        this.contratoPratica = contratoPratica;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTextoError() {
        return textoError;
    }

    public void setTextoError(String textoError) {
        this.textoError = textoError;
    }

    public Long getQuantidadeClob() { return quantidadeClob; }

    public void setQuantidadeClob(Long quantidadeClob) { this.quantidadeClob = quantidadeClob; }

    public String getLogins() { return logins; }

    public void setLogins(String logins) { this.logins = logins; }

    public static BigDecimal sumParcelas(List<LicencaSwProjetoParcela> parcelas) {
        BigDecimal result = BigDecimal.ZERO;
        for (LicencaSwProjetoParcela parcela : parcelas) {
            result = result.add(parcela.getValorParcela());
        }

        return result;
    }

    public static String translateStatus(final String status) {
        if (Status.AGUARDANDO_APROVACAO.getFieldName().equalsIgnoreCase(status)) {
            return StatusFromWeb.WAITING_APPROVAL.getFieldName();
        } else if (Status.APROVADO.getFieldName().equalsIgnoreCase(status)) {
            return StatusFromWeb.APPROVED.getFieldName();
        } else if (Status.PENDENTE.getFieldName().equalsIgnoreCase(status)) {
            return StatusFromWeb.PENDING.getFieldName();
        } else if (Status.INTEGRADO.getFieldName().equalsIgnoreCase(status)) {
            return StatusFromWeb.INTEGRATED.getFieldName();
        } else if (Status.ERRO.getFieldName().equalsIgnoreCase(status)) {
            return StatusFromWeb.ERROR.getFieldName();

        } else if (StatusFromWeb.WAITING_APPROVAL.getFieldName().equalsIgnoreCase(status)) {
            return Status.AGUARDANDO_APROVACAO.getFieldName();
        } else if (StatusFromWeb.APPROVED.getFieldName().equalsIgnoreCase(status)) {
            return Status.APROVADO.getFieldName();
        } else if (StatusFromWeb.PENDING.getFieldName().equalsIgnoreCase(status)) {
            return Status.PENDENTE.getFieldName();
        } else if (StatusFromWeb.INTEGRATED.getFieldName().equalsIgnoreCase(status)) {
            return Status.INTEGRADO.getFieldName();
        } else if (StatusFromWeb.ERROR.getFieldName().equalsIgnoreCase(status)) {
            return Status.ERRO.getFieldName();
        }

        return null;
    }
}
