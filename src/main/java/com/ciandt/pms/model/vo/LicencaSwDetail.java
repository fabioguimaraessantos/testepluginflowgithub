package com.ciandt.pms.model.vo;

import com.ciandt.pms.Constants;
import java.util.Date;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class LicencaSwDetail implements Serializable {

    /** The serial version ID. */
    private static final long serialVersionUID = 1L;

    private Long codigoGrupoCusto;

    private String nomeGrupoCusto;

    private Long codigoCentroCustoErp;

    private Long codigoProjetoErp;

    private String nomeProjetoErp;

    private Long codigoContratoPratica;

    private String nomeContratoPratica;

    private String nomeProjeto;

    private BigDecimal valor;

    private String contaContabilDebito;

    private String contaContabilCredito;

    private String tipoParcela;

    private LicencaSwIntegrateDetail integrate;

    /**
     * Como n�o existe a informa��o de NF na Licen�a por Usu�rio, este campo � utilizado para armazenar
     *  o nome do TI_RECURSO para ser exibido na mensagem de erro ao Aprovar ou Integrar uma Licen�a
     */
    private String notaFiscal;

    private Boolean isUserLicense;

    private Long idLicenca;

    private Integer numeroParcela;

    private Date dataParcela;

    public Boolean isLongTerm(){
        if(this.tipoParcela != null && this.getTipoParcela().equals(Constants.LICENCA_SW_PROJETO_TIPO_PARCELA_LP)){
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}
