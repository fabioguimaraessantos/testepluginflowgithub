package com.ciandt.pms.enums;

public enum StatusIntegracaoReceita {

    INTEGRADO("I"), NAO_INTEGRADO("W"), PENDENTE("P"), ERRO("E");

    private String sigla;

    StatusIntegracaoReceita(String sigla) {
        this.sigla = sigla;
    }

    public static StatusIntegracaoReceita getInstanceBySigla(String sigla) {
        if (INTEGRADO.sigla.equals(sigla)) {
            return INTEGRADO;
        } else if (NAO_INTEGRADO.sigla.equals(sigla)) {
            return NAO_INTEGRADO;
        } else if (PENDENTE.sigla.equals(sigla)) {
            return PENDENTE;
        } else if (ERRO.sigla.equals(sigla)) {
            return ERRO;
        }
        throw new IllegalArgumentException();
    }


}
