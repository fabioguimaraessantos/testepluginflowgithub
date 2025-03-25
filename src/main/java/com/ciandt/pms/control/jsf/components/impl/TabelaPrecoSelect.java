package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.model.TabelaPreco;

import java.util.List;

public class TabelaPrecoSelect extends Select<TabelaPreco> {

    /**
     * @param tabelaPrecoList - List of Price Table
     */
    public TabelaPrecoSelect(List<TabelaPreco> tabelaPrecoList) {
        super(tabelaPrecoList);
    }

    @Override
    protected Long objectValue(TabelaPreco entity) {
        return entity != null ? entity.getCodigoTabelaPreco() : null;
    }

    @Override
    protected String objectKey(TabelaPreco entity) {
        return entity != null ? entity.getDescricaoTabelaPreco() : null;
    }

    @Override
    protected TabelaPreco entityByValue(Long code) {
        return getEntityByCode(code);
    }

    /**
     * @param code - Code to find Price Table History
     * @return TabelaPreco - Price Table
     */
    private TabelaPreco getEntityByCode(Long code) {

        if (code != null) {
            for (TabelaPreco entity : entities) {
                if (code.equals(entity.getCodigoTabelaPreco()))
                    return entity;
            }
        }

        return null;
    }
}
