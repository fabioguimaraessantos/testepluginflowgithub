package com.ciandt.pms.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LicencaSwUserVO implements Serializable {

    private Long codigoTiRecurso;

    private String nomeLicenca;

    private String status;

    private String textoError;

    private BigDecimal valorLicencaTotal;

    private Date lastUpdate;

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LicencaSwUserVO() {
    }

    public LicencaSwUserVO(Long codigoTiRecurso, String nomeLicenca, String status, String textoError, BigDecimal valorLicencaTotal, Date lastUpdate) {
        this.codigoTiRecurso = codigoTiRecurso;
        this.nomeLicenca = nomeLicenca;
        this.status = status;
        this.valorLicencaTotal = valorLicencaTotal;
        this.textoError = textoError;
        this.lastUpdate = lastUpdate;
    }

    public Long getCodigoTiRecurso() { return codigoTiRecurso; }

    public void setCodigoTiRecurso(Long codigoTiRecurso) { this.codigoTiRecurso = codigoTiRecurso; }

    public String getNomeLicenca() {
        return nomeLicenca;
    }

    public void setNomeLicenca(String nomeLicenca) {
        this.nomeLicenca = nomeLicenca;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorLicencaTotal() {
        return valorLicencaTotal;
    }

    public void setValorLicencaTotal(BigDecimal valorLicencaTotal) {
        this.valorLicencaTotal = valorLicencaTotal;
    }

    public String getTextoError() {
        return textoError;
    }

    public void setTextoError(String textoError) {
        this.textoError = textoError;
    }
}
