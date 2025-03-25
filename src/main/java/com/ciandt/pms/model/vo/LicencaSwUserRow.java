package com.ciandt.pms.model.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LicencaSwUserRow implements Serializable {

    /** The serial version ID. */
    private static final long serialVersionUID = 1L;

    /** Variavel representa se o item esta selecionado. */
    private Boolean isSelected = Boolean.FALSE;

    private Long codigoTiRecurso;

    private String nomeLicenca;

    private BigDecimal valorTotal;

    private String status;

    private String textoErro;

    private Date lastUpdate;


    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getCodigoTiRecurso() { return codigoTiRecurso; }

    public void setCodigoTiRecurso(Long codigoTiRecurso) { this.codigoTiRecurso = codigoTiRecurso; }

    public String getTextoErro() { return textoErro; }

    public void setTextoErro(String textoErro) { this.textoErro = textoErro; }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getNomeLicenca() {
        return nomeLicenca;
    }

    public void setNomeLicenca(String nomeLicenca) {
        this.nomeLicenca = nomeLicenca;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
