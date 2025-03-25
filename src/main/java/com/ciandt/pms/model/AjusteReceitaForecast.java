package com.ciandt.pms.model;

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
import java.math.BigDecimal;
import java.util.Date;

/**
 * Entidade da tabela FORECAST_REVENUE_ADJUSTMENT.
 * Contém as receitas com ajustes manuais que necessitam de aprovação no Forecast.
 *
 * @author gbrunetto
 * @since 17/06/2024
 */
@Entity
@Table(name = "FORECAST_REVENUE_ADJUSTMENT")
@NamedQueries({
        @NamedQuery(name = "AjusteReceitaForecast.findByContratoPratica", query = "SELECT arf FROM AjusteReceitaForecast arf"
                + " JOIN arf.mapaAlocacao ma"
                + " JOIN ma.contratoPratica cp"
                + " WHERE cp.codigoContratoPratica = :codigoContratoPratica"),

        @NamedQuery(name = "AjusteReceitaForecast.findByCodigoContratoPraticaAndDataMesReceitaOrderByDataAprovacaoDesc",
                query = "SELECT arf FROM AjusteReceitaForecast arf"
                        + " JOIN arf.mapaAlocacao ma"
                        + " JOIN ma.contratoPratica cp"
                        + " WHERE cp.codigoContratoPratica = :codigoContratoPratica"
                        + " AND arf.dataMesReceita = :dataMesReceita"
                        + " ORDER BY arf.codigoAjusteReceitaForecast DESC")
})
public class AjusteReceitaForecast {

    public static final String FIND_BY_CONTRATO_PRATICA = "AjusteReceitaForecast.findByContratoPratica";

    public static final String FIND_BY_CONTRATO_PRATICA_AND_DATA_MES_RECEITA = "AjusteReceitaForecast.findByCodigoContratoPraticaAndDataMesReceitaOrderByDataAprovacaoDesc";

    /**
     * Coluna FRAD_CD_FORE_REVE_ADJU da tabela FORECAST_REVENUE_ADJUSTMENT.
     * Chave primária. Sequence: SQ_FRAD_CD_FORE_REVE_ADJU.
     */
    @Id
    @GeneratedValue(generator = "AjusteReceitaForecastSeq")
    @SequenceGenerator(name = "AjusteReceitaForecastSeq", sequenceName = "SQ_FRAD_CD_FORE_REVE_ADJU", allocationSize = 1)
    @Column(name = "FRAD_CD_FORE_REVE_ADJU", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoAjusteReceitaForecast;

    /**
     * Coluna FRAD_DT_ADJUSTMENT da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FRAD_DT_ADJUSTMENT", nullable = false)
    private Date dataAjuste;

    /**
     * Coluna MAAL_CD_MAPA_ALOCACAO da tabela MAPA_ALOCACAO.
     * Chave estrangeira.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MAAL_CD_MAPA_ALOCACAO")
    private MapaAlocacao mapaAlocacao;

    /**
     * Coluna REMO_CD_RECEITA_MOEDA da tabela RECEITA_MOEDA.
     * Chave estrangeira.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REMO_CD_RECEITA_MOEDA")
    private ReceitaMoeda receitaMoeda;

    /**
     * Coluna RECE_DT_MES da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "RECE_DT_MES", nullable = false)
    private Date dataMesReceita;

    /**
     * Coluna FRAD_VL_ADJUSTED da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Column(name = "FRAD_VL_ADJUSTED", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorAjustado;

    /**
     * Coluna FRAD_VL_ACTUAL da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Column(name = "FRAD_VL_ACTUAL", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorVigente;

    /**
     * Coluna FRAD_VL_DIFFERENCE da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Column(name = "FRAD_VL_DIFFERENCE", nullable = false, precision = 18, scale = 2)
    private BigDecimal valorDiferenca;

    /**
     * Coluna FRAD_CD_LOGIN_REQUESTER da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Column(name = "FRAD_CD_LOGIN_REQUESTER", nullable = false, length = 200)
    private String loginSolicitante;

    /**
     * Coluna FRAD_CD_LOGIN_APPROVER da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Column(name = "FRAD_CD_LOGIN_APPROVER", nullable = true, length = 200)
    private String loginAprovador;

    /**
     * Coluna FRAS_CD_FORE_REVE_ADJU_STATUS da tabela FORE_REVE_ADJUSTMENT_STATUS.
     * Chave estrangeira.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FRAS_CD_FORE_REVE_ADJU_STATUS")
    private AjusteReceitaForecastStatus ajusteReceitaForecastStatus;

    /**
     * Coluna FRAD_TX_OBSERVATION da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Column(name = "FRAD_TX_OBSERVATION", length = 4000)
    private String observacao;

    /**
     * Coluna FRAD_DT_APPROVED da tabela FORECAST_REVENUE_ADJUSTMENT.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FRAD_DT_APPROVED", nullable = true)
    private Date dataAprovacao;

    public AjusteReceitaForecast() {}

    public AjusteReceitaForecast(Long codigoAjusteReceitaForecast, Date dataAjuste, MapaAlocacao mapaAlocacao,
                                 ReceitaMoeda receitaMoeda, Date dataMesReceita, BigDecimal valorAjustado,
                                 BigDecimal valorVigente, BigDecimal valorDiferenca,
                                 String loginSolicitante, String loginAprovador,
                                 AjusteReceitaForecastStatus ajusteReceitaForecastStatus, String observacao,
                                 Date dataAprovacao) {
        this.codigoAjusteReceitaForecast = codigoAjusteReceitaForecast;
        this.dataAjuste = dataAjuste;
        this.mapaAlocacao = mapaAlocacao;
        this.receitaMoeda = receitaMoeda;
        this.dataMesReceita = dataMesReceita;
        this.valorAjustado = valorAjustado;
        this.valorVigente = valorVigente;
        this.valorDiferenca = valorDiferenca;
        this.loginSolicitante = loginSolicitante;
        this.loginAprovador = loginAprovador;
        this.ajusteReceitaForecastStatus = ajusteReceitaForecastStatus;
        this.observacao = observacao;
        this.dataAprovacao = dataAprovacao;
    }

    public Long getCodigoAjusteReceitaForecast() {
        return codigoAjusteReceitaForecast;
    }

    public void setCodigoAjusteReceitaForecast(Long codigoAjusteReceitaForecast) {
        this.codigoAjusteReceitaForecast = codigoAjusteReceitaForecast;
    }

    public Date getDataAjuste() {
        return dataAjuste;
    }

    public void setDataAjuste(Date dataAjuste) {
        this.dataAjuste = dataAjuste;
    }

    public MapaAlocacao getMapaAlocacao() {
        return mapaAlocacao;
    }

    public void setMapaAlocacao(MapaAlocacao mapaAlocacao) {
        this.mapaAlocacao = mapaAlocacao;
    }

    public ReceitaMoeda getReceitaMoeda() {
        return receitaMoeda;
    }

    public void setReceitaMoeda(ReceitaMoeda receitaMoeda) {
        this.receitaMoeda = receitaMoeda;
    }

    public Date getDataMesReceita() {
        return dataMesReceita;
    }

    public void setDataMesReceita(Date dataMesReceita) {
        this.dataMesReceita = dataMesReceita;
    }

    public BigDecimal getValorAjustado() {
        return valorAjustado;
    }

    public void setValorAjustado(BigDecimal valorAjustado) {
        this.valorAjustado = valorAjustado;
    }

    public BigDecimal getValorVigente() {
        return valorVigente;
    }

    public void setValorVigente(BigDecimal valorVigente) {
        this.valorVigente = valorVigente;
    }

    public BigDecimal getValorDiferenca() {
        return valorDiferenca;
    }

    public void setValorDiferenca(BigDecimal valorDiferenca) {
        this.valorDiferenca = valorDiferenca;
    }

    public String getLoginSolicitante() {
        return loginSolicitante;
    }

    public void setLoginSolicitante(String loginSolicitante) {
        this.loginSolicitante = loginSolicitante;
    }

    public String getLoginAprovador() {
        return loginAprovador;
    }

    public void setLoginAprovador(String loginAprovador) {
        this.loginAprovador = loginAprovador;
    }

    public AjusteReceitaForecastStatus getAjusteReceitaForecastStatus() {
        return ajusteReceitaForecastStatus;
    }

    public void setAjusteReceitaForecastStatus(AjusteReceitaForecastStatus ajusteReceitaForecastStatus) {
        this.ajusteReceitaForecastStatus = ajusteReceitaForecastStatus;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(Date dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    @Override
    public String toString() {
        return "AjusteReceitaForecast{" +
                "codigoAjusteReceitaForecast=" + codigoAjusteReceitaForecast +
                ", dataAjuste=" + dataAjuste +
                ", mapaAlocacao=" + mapaAlocacao +
                ", receitaMoeda=" + receitaMoeda +
                ", dataMesReceita=" + dataMesReceita +
                ", valorAjustado=" + valorAjustado +
                ", valorVigente=" + valorVigente +
                ", valorDiferenca=" + valorDiferenca +
                ", loginSolicitante=" + loginSolicitante +
                ", loginAprovador=" + loginAprovador +
                ", ajusteReceitaForecastStatus=" + ajusteReceitaForecastStatus +
                ", observacao='" + observacao + '\'' +
                ", dataAprovacao=" + dataAprovacao +
                '}';
    }
}
