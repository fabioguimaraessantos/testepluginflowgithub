package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Moeda;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class LicencaSwProjetoRow implements Serializable {
    /** The serial version ID. */
    private static final long serialVersionUID = 1L;

    /** Variavel representa se o item esta selecionado. */
    private Boolean isSelected = Boolean.FALSE;

    private Long codigoLicencaSwProjetoParcela;

    private Long codigoLicencaSwProjeto;

    private String nomeLicenca;

    private Integer qtdeParcelas;

    private Integer parcelaApropriada;

    private BigDecimal valorApropriacao;

    private BigDecimal saldoParcelas;

    private Date dataInicio;

    private Date dataParcela;

    private Long notaFiscal;

    private String status;

    private String textoErro;

    private BigDecimal valorTotal;

    private BigDecimal valorParcial;

    private Boolean isEditable;

    private Boolean isAppropriate;

    private Boolean hasIntegratedInstallments;

    private String logins;

    private String nomeProjeto;

    private BigDecimal valorParcela;

    private String nomeFornecedor;

    private Moeda moeda;

    private Empresa empresa;

    public BigDecimal getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(BigDecimal valorParcial) {
        this.valorParcial = valorParcial;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getDataParcela() { return dataParcela; }

    public void setDataParcela(Date dataParcela) { this.dataParcela = dataParcela; }

    public String getTextoErro() {
        return textoErro;
    }

    public void setTextoErro(String textoErro) {
        this.textoErro = textoErro;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Long getCodigoLicencaSwProjeto() {
        return codigoLicencaSwProjeto;
    }

    public void setCodigoLicencaSwProjeto(Long codigoLicencaSwProjeto) {
        this.codigoLicencaSwProjeto = codigoLicencaSwProjeto;
    }

    /**
     * Construtor default.
     */
    public LicencaSwProjetoRow() {
    }

    public Integer getQtdeParcelas() {
        return qtdeParcelas;
    }

    public void setQtdeParcelas(Integer qtdeParcelas) {
        this.qtdeParcelas = qtdeParcelas;
    }

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

    public Integer getParcelaApropriada() {
        return parcelaApropriada;
    }

    public void setParcelaApropriada(Integer parcelaApropriada) {
        this.parcelaApropriada = parcelaApropriada;
    }

    public Long getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(Long notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorApropriacao() {
        return valorApropriacao;
    }

    public void setValorApropriacao(BigDecimal valorApropriacao) {
        this.valorApropriacao = valorApropriacao;
    }

    public Long getCodigoLicencaSwProjetoParcela() { return codigoLicencaSwProjetoParcela; }

    public void setCodigoLicencaSwProjetoParcela(Long codigoLicencaSwProjetoParcela) { this.codigoLicencaSwProjetoParcela = codigoLicencaSwProjetoParcela; }

    public BigDecimal getSaldoParcelas() { return saldoParcelas; }

    public void setSaldoParcelas(BigDecimal saldoParcelas) { this.saldoParcelas = saldoParcelas; }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Boolean getHasIntegratedInstallments() {
        return hasIntegratedInstallments;
    }

    public void setHasIntegratedInstallments(Boolean hasIntegratedInstallments) {
        this.hasIntegratedInstallments = hasIntegratedInstallments;
    }

    public Boolean getIsAppropriate() {
        return isAppropriate;
    }

    public void setIsAppropriate(Boolean appropriate) {
        isAppropriate = appropriate;
    }

    public String getLogins() {
        return logins;
    }

    public void setLogins(String logins) {
        this.logins = logins;
    }

    public String getNomeProjeto() {
        return nomeProjeto;
    }

    public void setNomeProjeto(String nomeProjeto) {
        this.nomeProjeto = nomeProjeto;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public Moeda getMoeda() {
        return moeda;
    }

    public void setMoeda(Moeda moeda) {
        this.moeda = moeda;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
}
