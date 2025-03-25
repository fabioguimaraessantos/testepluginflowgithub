package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "VW_PMS_GC_CONTA_DEBITO")
@NamedQueries({
        @NamedQuery(name = VwPmsGrupoContaContabil.FIND_BY_CONTRATO_PRATICA, query = "SELECT t FROM VwPmsGrupoContaContabil t where t.codigoContratoPratica = :codigoContratoPratica "),
        @NamedQuery(name = VwPmsGrupoContaContabil.FIND_BY_CENTRO_CUSTO, query = "SELECT t FROM VwPmsGrupoContaContabil t where t.codigoCentroCustoReduzido = :codigoCentroCusto "),
        @NamedQuery(name = VwPmsGrupoContaContabil.FIND_BY_GRUPO_CUSTO, query = "SELECT t FROM VwPmsGrupoContaContabil t where t.codigoGrupoCusto = :codigoCentroCusto ")
})
public class VwPmsGrupoContaContabil implements Serializable {

    /** Constante para NamedQuery "VwMegaCCusto.findAllPending". */
    public static final String FIND_BY_CONTRATO_PRATICA = "VwPmsGrupoContaContabil.findByContratoPratica";

    public static final String FIND_BY_CENTRO_CUSTO = "VwPmsGrupoContaContabil.findByCodigoCentroCustoReduzido";

    public static final String FIND_BY_GRUPO_CUSTO = "VwPmsGrupoContaContabil.findByCodigoGrupoCusto";

    @Id
    @Column(name = "GRCU_CD_GRUPO_CUSTO")
    private Long codigoGrupoCusto;

    @Column(name = "GRCU_NM_GRUPO_CUSTO")
    private String nomeGrupoCusto;

    @Column(name = "COS_IN_REDUZIDO")
    private Long codigoCentroCustoReduzido;

    @Column(name = "PROJ_IN_REDUZIDO")
    private Long codigoProjetoReduzido;

    @Column(name = "NM_PROJ_MEGA")
    private String nomeProjetoErp;

    @Column(name = "COPR_CD_CONTRATO_PRATICA")
    private Long codigoContratoPratica;

    @Column(name = "TIAR_CD_TIPO_AREA")
    private Long codigoTipoArea;

    @Column(name = "TIAR_NM_TIPO_AREA")
    private String nomeTipoArea;

    @Column(name = "CONTA_CONTABIL_DEBITO")
    private String contaContabilDebito;

    @Column(name = "CONTA_CONTABIL_CREDITO")
    private String contaContabilCredito;

    @Column(name = "CONTA_CONTABIL_DEBITO_LP")
    private String contaContabilDebitoLongoPrazo;

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

    public Long getCodigoCentroCustoReduzido() {
        return codigoCentroCustoReduzido;
    }

    public void setCodigoCentroCustoReduzido(Long codigoCentroCustoReduzido) {
        this.codigoCentroCustoReduzido = codigoCentroCustoReduzido;
    }

    public Long getCodigoProjetoReduzido() {
        return codigoProjetoReduzido;
    }

    public void setCodigoProjetoReduzido(Long codigoProjetoReduzido) {
        this.codigoProjetoReduzido = codigoProjetoReduzido;
    }

    public String getNomeProjetoErp() {
        return nomeProjetoErp;
    }

    public void setNomeProjetoErp(String nomeProjetoErp) {
        this.nomeProjetoErp = nomeProjetoErp;
    }

    public Long getCodigoContratoPratica() {
        return codigoContratoPratica;
    }

    public void setCodigoContratoPratica(Long codigoContratoPratica) {
        this.codigoContratoPratica = codigoContratoPratica;
    }

    public Long getCodigoTipoArea() {
        return codigoTipoArea;
    }

    public void setCodigoTipoArea(Long codigoTipoArea) {
        this.codigoTipoArea = codigoTipoArea;
    }

    public String getNomeTipoArea() {
        return nomeTipoArea;
    }

    public void setNomeTipoArea(String nomeTipoArea) {
        this.nomeTipoArea = nomeTipoArea;
    }

    public String getContaContabilDebito() {
        return contaContabilDebito;
    }

    public void setContaContabilDebito(String contaContabilDebito) {
        this.contaContabilDebito = contaContabilDebito;
    }

    public String getContaContabilCredito() {
        return contaContabilCredito;
    }

    public void setContaContabilCredito(String contaContabilCredito) {
        this.contaContabilCredito = contaContabilCredito;
    }

    public String getContaContabilDebitoLongoPrazo() { return contaContabilDebitoLongoPrazo; }

    public void setContaContabilDebitoLongoPrazo(String contaContabilDebitoLongoPrazo) { this.contaContabilDebitoLongoPrazo = contaContabilDebitoLongoPrazo; }
}
