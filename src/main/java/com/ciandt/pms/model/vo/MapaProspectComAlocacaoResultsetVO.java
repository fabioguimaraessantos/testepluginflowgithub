package com.ciandt.pms.model.vo;

import java.io.Serializable;

/**
 * Created by vnogueira on 16/04/18.
 */
public class MapaProspectComAlocacaoResultsetVO implements Serializable {


    /** Nome do Mapa de Alocacao Prospect */
    private String nomeMapaAlocacao;

    /** Login do usuário configurado como Manager Approver no Contrato Prática */
    private String loginManagerApprover;

    /** Login do usuário configurado como Business Manager na tabela de Centro Lucro */
    private String loginBusinessManager;

    /** Total de percentuais de alocacao somados dentro do Mapa Prospect no mes corrente */
    private Double totalAlocacao;


    public MapaProspectComAlocacaoResultsetVO() {
    }

    public MapaProspectComAlocacaoResultsetVO(String nomeMapaAlocacao, String loginManagerApprover, String loginBusinessManager, Double totalAlocacao) {
        this.nomeMapaAlocacao = nomeMapaAlocacao;
        this.loginManagerApprover = loginManagerApprover;
        this.loginBusinessManager = loginBusinessManager;
        this.totalAlocacao = totalAlocacao;
    }

    public String getNomeMapaAlocacao() {
        return nomeMapaAlocacao;
    }

    public void setNomeMapaAlocacao(String nomeMapaAlocacao) {
        this.nomeMapaAlocacao = nomeMapaAlocacao;
    }

    public String getLoginManagerApprover() {
        return loginManagerApprover;
    }

    public void setLoginManagerApprover(String loginManagerApprover) {
        this.loginManagerApprover = loginManagerApprover;
    }

    public String getLoginBusinessManager() {
        return loginBusinessManager;
    }

    public void setLoginBusinessManager(String loginBusinessManager) {
        this.loginBusinessManager = loginBusinessManager;
    }

    public Double getTotalAlocacao() {
        return totalAlocacao;
    }

    public void setTotalAlocacao(Double totalAlocacao) {
        this.totalAlocacao = totalAlocacao;
    }
}
