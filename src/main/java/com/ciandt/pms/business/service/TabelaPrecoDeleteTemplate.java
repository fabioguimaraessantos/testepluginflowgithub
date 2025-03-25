package com.ciandt.pms.business.service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.TabelaPreco;

public abstract class TabelaPrecoDeleteTemplate {

    protected boolean delete(TabelaPreco entity) throws BusinessException {
        if (canDeletePriceTable(entity)) {
            return markAsDeleted(entity);
        }
        throw new BusinessException(Constants.MSG_ERROR_TABELA_PRECO_REMOVE);
    }

    protected abstract boolean canDeletePriceTable(TabelaPreco entity);

    protected abstract boolean markAsDeleted(TabelaPreco entity);
}
