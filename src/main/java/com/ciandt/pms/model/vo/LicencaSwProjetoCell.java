package com.ciandt.pms.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class LicencaSwProjetoCell implements Serializable {

    private String month;

    private Long codigoLicencaSwProjeto;

    private String nomeLicenca;

    private String notaFiscal;

    private String status;

    private BigDecimal valorTotal;

    private Long qtdeParcelas;

    private Long parcelaApropriada;

    private BigDecimal valorParcela;

    private BigDecimal valorApropriacao;

    private BigDecimal saldoParcelas;

    private Date dataInicio;

    private Long codigoCentroCustoErp;

    private String nomeCentroCusto;

    private Long codigoProjetoErp;

    private String nomeProjetoErp;

    private String descricaoLicenca;

    private Long saldo;

    private String businessManager;

    private String projectManager;

    private String logins;
}