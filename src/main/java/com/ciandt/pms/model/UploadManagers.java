package com.ciandt.pms.model;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

@CsvDataType
public class UploadManagers implements java.io.Serializable {


    @CsvField(pos = 1, required = true)
    private Long codigoContratoPratica;


    @CsvField(pos = 2)
    private String pesoaAprovador;


    @CsvField(pos = 3)
    private String pessoaGerente;


    @CsvField(pos = 4)
    private String businessManager;

    @CsvField(pos = 5)
    private String startDate;


    public Long getCodigoContratoPratica() {
        return codigoContratoPratica;
    }

    public void setCodigoContratoPratica(Long codigoContratoPratica) {
        this.codigoContratoPratica = codigoContratoPratica;
    }

    public String getPesoaAprovador() {
        return pesoaAprovador;
    }

    public void setPesoaAprovador(String pesoaAprovador) {
        this.pesoaAprovador = pesoaAprovador;
    }

    public String getPessoaGerente() {
        return pessoaGerente;
    }

    public void setPessoaGerente(String pessoaGerente) {
        this.pessoaGerente = pessoaGerente;
    }

    public String getBusinessManager() {
        return businessManager;
    }

    public void setBusinessManager(String businessManager) {
        this.businessManager = businessManager;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
