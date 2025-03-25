package com.ciandt.pms.model.vo;

import java.util.Date;

public interface IHistoricoGrupoCusto {

    String getNome();

    String getNomeDaArea();

    String getNomeAprovador();

    String getNomeGerenteAprovador();

    Date getDataDeInativacao();

    String getNomeStatus();
}
