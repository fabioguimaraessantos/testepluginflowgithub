/*
 * @(#) Receita.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "RATEIO_LICENCA_SW")
@SqlResultSetMappings(@SqlResultSetMapping(name = "scalarRateioLicencaSw"))
@NamedNativeQueries({
        @NamedNativeQuery(name = "RateioLicencaSw.findByFilter", query =
                "select  rls.tire_cd_ti_recurso, rls.tire_nm_ti_recurso, sum(rls.rali_vl_custo_licenca) valorLicenca, rls.reli_in_status, rls.reli_tx_error, rls.dt_last_update \n" +
                        "    from pms20.rateio_licenca_sw rls \n" +
                        "    where trunc(rls.dt_competencia, 'MONTH') = trunc(:dtCompetencia, 'MONTH') \n" +
                        "      and (rls.tire_cd_ti_recurso = :codigoLicenca OR :codigoLicenca = 0) \n" +
                        "      AND ( UPPER(rls.reli_in_status) like '%'||TRIM(UPPER(:status))||'%' OR (:status is null) ) \n" +
                        "	group by rls.tire_cd_ti_recurso, rls.tire_nm_ti_recurso, rls.reli_in_status, rls.reli_tx_error, rls.dt_last_update order by tire_nm_ti_recurso \n", resultSetMapping = "scalarRateioLicencaSw"),
        @NamedNativeQuery(name = "RateioLicencaSw.findByRecursoTIAndMonthGroupByCentroCustoAndProjeto", query =
                "select  rls.grcu_cd_grupo_custo, rls.grcu_nm_grupo_custo, rls.conv_cd_ccusto_mega, rls.conv_cd_projeto_mega, rls.conv_nm_projeto_mega, rls.copr_cd_contrato_pratica, rls.copr_nm_contrato_pratica, sum(rls.rali_vl_custo_licenca) valorLicenca, rls.reli_in_status, rls.tire_nm_ti_recurso \n" +
                        "                          from pms20.rateio_licenca_sw rls \n" +
                        "                          where trunc(rls.dt_competencia, 'MONTH') =  trunc(:dtCompetencia, 'MONTH') \n" +
                        "                          and rls.tire_cd_ti_recurso = :codigoLicenca  \n" +
                        "                       group by rls.grcu_cd_grupo_custo, rls.grcu_nm_grupo_custo, rls.conv_cd_ccusto_mega, rls.conv_cd_projeto_mega, rls.conv_nm_projeto_mega, rls.copr_cd_contrato_pratica, rls.copr_nm_contrato_pratica, rls.reli_in_status, rls.tire_nm_ti_recurso \n", resultSetMapping = "scalarRateioLicencaSw"),
        @NamedNativeQuery(name = "RateioLicencaSw.findByRecursoTiAndMonthAndStatus", query = "select \n" +
                                "       rls.MONTH,\n" +
                                "       rls.tire_cd_ti_recurso,\n" +
                                "       rls.tire_nm_ti_recurso,\n" +
                                "       rls.TOTAL_VALUE,\n" +
                                "       rl.reli_in_status,\n" +
                                "       rl.conv_cd_ccusto_mega,\n" +
                                "       rl.grcu_nm_grupo_custo,\n" +
                                "       rl.conv_cd_projeto_mega,       \n" +
                                "       rl.conv_nm_projeto_mega,\n" +
                                "       rl.TOTAL,\n" +
                                "       rl.LOGINS\n" +
                                "from (select to_char(rls.dt_competencia, 'YYYY') YEAR,\n" +
                                "       to_char(rls.dt_competencia, 'MM') MONTH,\n" +
                                "       rls.dt_competencia,\n" +
                                "       rls.tire_cd_ti_recurso,\n" +
                                "       rls.tire_nm_ti_recurso,\n" +
                                "       sum(rls.rali_vl_custo_licenca) TOTAL_VALUE\n" +
                                "      from pms20.rateio_licenca_sw rls\n" +
                                "      group by to_char(rls.dt_competencia, 'YYYY') ,\n" +
                                "             to_char(rls.dt_competencia, 'MM') ,\n" +
                                "             rls.dt_competencia,\n" +
                                "             rls.tire_cd_ti_recurso,\n" +
                                "             rls.tire_nm_ti_recurso) rls \n" +
                                "inner join (select r.dt_competencia, \n" +
                                "                   r.tire_cd_ti_recurso, \n" +
                                "                   r.conv_cd_ccusto_mega,\n" +
                                "                   r.grcu_nm_grupo_custo,\n" +
                                "                   r.conv_cd_projeto_mega,       \n" +
                                "                   r.conv_nm_projeto_mega,\n" +
                                "                   r.reli_in_status,\n" +
                                "                   sum(r.rali_vl_custo_licenca) TOTAL,  \n" +
                                "                   LISTAGG(R.PESS_CD_LOGIN, ', ') within group (order by R.PESS_CD_LOGIN) LOGINS  \n" +
                                "                   from pms20.rateio_licenca_sw r\n" +
                                "                   group by r.dt_competencia, \n" +
                                "                            r.tire_cd_ti_recurso, \n" +
                                "                            r.conv_cd_ccusto_mega,\n" +
                                "                             r.grcu_nm_grupo_custo,\n" +
                                "                             r.conv_cd_projeto_mega,       \n" +
                                "                             r.conv_nm_projeto_mega,\n" +
                                "                             r.reli_in_status) rl on  rl.dt_competencia = rls.dt_competencia \n" +
                                "                                                       and rls.tire_cd_ti_recurso = rl.tire_cd_ti_recurso\n" +
                                "where rls.dt_competencia = :dataCompetencia\n" +
                                "  and (rl.reli_in_status = :status OR :status is null)\n" +
                                "  and (rls.tire_cd_ti_recurso = :codTiRecurso OR :codTiRecurso = 0)", resultSetMapping = "scalarRateioLicencaSw")
})
@NamedQueries(
        @NamedQuery(name = "RateioLicencaSw.findByRecursoTIAndMonth", query = "SELECT rls FROM RateioLicencaSw rls "
                + " WHERE TRUNC(rls.dataCompetencia, 'MONTH') = TRUNC(?, 'MONTH') "
                + " AND (rls.codigoLicenca = ? OR (? is null)) "))
public class RateioLicencaSw implements java.io.Serializable {

    public static final String FIND_BY_FILTER = "RateioLicencaSw.findByFilter";
    public static final String FIND_BY_RECURSO_TI_MONTH_GROUPBY_CENTROCUSTO_PROJETO = "RateioLicencaSw.findByRecursoTIAndMonthGroupByCentroCustoAndProjeto";
    public static final String FIND_BY_RECURSO_TI_AND_MONTH = "RateioLicencaSw.findByRecursoTIAndMonth";
    public static final String FIND_BY_CODIGO_RECURSO_DATA_INICIO_AND_STATUS = "RateioLicencaSw.findByRecursoTiAndMonthAndStatus";

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "RateioLicencaSwSeq")
    @SequenceGenerator(name = "RateioLicencaSwSeq", sequenceName = "SQ_RALI_CD_RATEIO_LICENCA", allocationSize = 1)
    @Column(name = "RALI_CD_RATEIO_LICENCA", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoRateioLicencaSw;

    @Column(name = "PESS_CD_LOGIN", length = 20)
    private String login;

    @Temporal(TemporalType.DATE)
    @Column(name = "DT_COMPETENCIA", nullable = false, length = 7)
    private Date dataCompetencia;

    @Column(name = "CONV_CD_CCUSTO_MEGA", precision = 8, scale = 0)
    private Long codigoCCustoErp;

    @Column(name = "CONV_CD_PROJETO_MEGA", precision = 8, scale = 0)
    private Long codigoProjetoErp;

    @Column(name = "CONV_NM_PROJETO_MEGA", length = 200)
    private String nomeProjetoErp;

    @Column(name = "GRCU_CD_GRUPO_CUSTO", precision = 8, scale = 0)
    private Long codigoGrupoCusto;

    @Column(name = "GRCU_NM_GRUPO_CUSTO", length = 200)
    private String nomeGrupoCusto;

    @Column(name = "CLIE_CD_CLIENTE", precision = 8, scale = 0)
    private Long codigoCliente;

    @Column(name = "CLIE_NM_CLIENTE", length = 200)
    private String nomeCliente;

    @Column(name = "COPR_CD_CONTRATO_PRATICA", precision = 8, scale = 0)
    private Long codigoClob;

    @Column(name = "COPR_NM_CONTRATO_PRATICA", length = 240)
    private String nomeClob;

    @Column(name = "TIRE_NM_TI_RECURSO", length = 100)
    private String nomeLicenca;

    @Column(name = "ALPE_PR_ALOCACAO_PERIODO", precision = 22, scale = 0)
    private BigDecimal percentualAlocacao;

    @Column(name = "RALI_VL_CUSTO_LICENCA", precision = 22, scale = 0)
    private BigDecimal valorLicenca;

    @Column(name = "TIRE_CD_TI_RECURSO", precision = 18, scale = 0)
    private Long codigoLicenca;

    @Column(name = "RELI_TX_ERROR", length = 4000)
    private String textoError;

    @Column(name = "RELI_IN_STATUS", length = 20)
    private String status;


    public Long getCodigoRateioLicencaSw() {
        return codigoRateioLicencaSw;
    }

    public void setCodigoRateioLicencaSw(Long codigoRateioLicencaSw) {
        this.codigoRateioLicencaSw = codigoRateioLicencaSw;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getDataCompetencia() {
        return dataCompetencia;
    }

    public void setDataCompetencia(Date dataCompetencia) {
        this.dataCompetencia = dataCompetencia;
    }

    public Long getCodigoCCustoErp() {
        return codigoCCustoErp;
    }

    public void setCodigoCCustoErp(Long codigoCCustoErp) {
        this.codigoCCustoErp = codigoCCustoErp;
    }

    public Long getCodigoProjetoErp() {
        return codigoProjetoErp;
    }

    public void setCodigoProjetoErp(Long codigoProjetoErp) {
        this.codigoProjetoErp = codigoProjetoErp;
    }

    public String getNomeProjetoErp() {
        return nomeProjetoErp;
    }

    public void setNomeProjetoErp(String nomeProjetoErp) {
        this.nomeProjetoErp = nomeProjetoErp;
    }

    public Long getCodigoGrupoCusto() {
        return codigoGrupoCusto;
    }

    public void setCodigoGrupoCusto(Long codigoGrupoCusto) {
        this.codigoGrupoCusto = codigoGrupoCusto;
    }

    public String getNomeGrupoCusto() {
        return nomeGrupoCusto;
    }

    public void setNomeGrupoCusto(String nomeGrupoCusto) {
        this.nomeGrupoCusto = nomeGrupoCusto;
    }

    public Long getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Long getCodigoClob() {
        return codigoClob;
    }

    public void setCodigoClob(Long codigoClob) {
        this.codigoClob = codigoClob;
    }

    public String getNomeClob() {
        return nomeClob;
    }

    public void setNomeClob(String nomeClob) {
        this.nomeClob = nomeClob;
    }

    public String getNomeLicenca() {
        return nomeLicenca;
    }

    public void setNomeLicenca(String nomeLicenca) {
        this.nomeLicenca = nomeLicenca;
    }

    public BigDecimal getPercentualAlocacao() {
        return percentualAlocacao;
    }

    public void setPercentualAlocacao(BigDecimal percentualAlocacao) {
        this.percentualAlocacao = percentualAlocacao;
    }

    public BigDecimal getValorLicenca() {
        return valorLicenca;
    }

    public void setValorLicenca(BigDecimal valorLicenca) {
        this.valorLicenca = valorLicenca;
    }

    public Long getCodigoLicenca() {
        return codigoLicenca;
    }

    public void setCodigoLicenca(Long codigoLicenca) {
        this.codigoLicenca = codigoLicenca;
    }

    public String getTextoError() {
        return textoError;
    }

    public void setTextoError(String textoError) {
        this.textoError = textoError;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}