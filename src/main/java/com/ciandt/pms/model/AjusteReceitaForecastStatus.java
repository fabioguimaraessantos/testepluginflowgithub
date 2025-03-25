package com.ciandt.pms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FORE_REVE_ADJUSTMENT_STATUS")
public class AjusteReceitaForecastStatus {

    /**
     * Coluna FRAS_CD_FORE_REVE_ADJU_STATUS da tabela FORE_REVE_ADJUSTMENT_STATUS.
     * Chave primária. Sequence: SQ_FRAS_CD_FORE_REVE_ADJU_STATUS.
     */
    @Id
    @GeneratedValue(generator = "AjusteReceitaForecastStatusSeq")
    @SequenceGenerator(name = "AjusteReceitaForecastStatusSeq", sequenceName = "SQ_FRAS_CD_FORE_REVE_ADJU_STATUS", allocationSize = 1)
    @Column(name = "FRAS_CD_FORE_REVE_ADJU_STATUS", unique = true, nullable = false, precision = 2, scale = 0)
    private Long codigoAjusteReceitaForecastStatus;

    /**
     * Coluna FRAS_NM_FORE_REVE_ADJU_STATUS da tabela FORE_REVE_ADJUSTMENT_STATUS.
     */
    @Column(name = "FRAS_NM_FORE_REVE_ADJU_STATUS", nullable = false, length = 30)
    private String status;

    public AjusteReceitaForecastStatus() {}

    public AjusteReceitaForecastStatus(Long codigoAjusteReceitaForecastStatus, String status) {
        this.codigoAjusteReceitaForecastStatus = codigoAjusteReceitaForecastStatus;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCodigoAjusteReceitaForecastStatus() {
        return codigoAjusteReceitaForecastStatus;
    }

    public void setCodigoAjusteReceitaForecastStatus(Long codigoAjusteReceitaForecastStatus) {
        this.codigoAjusteReceitaForecastStatus = codigoAjusteReceitaForecastStatus;
    }

    @Override
    public String toString() {
        return "AjusteReceitaForecastStatus{" +
                "codigoAjusteReceitaForecastStatus=" + codigoAjusteReceitaForecastStatus +
                ", status='" + status + '\'' +
                '}';
    }
}
