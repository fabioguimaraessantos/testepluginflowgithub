package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.ReceitaIntegracaoSemaforo;

import java.util.List;

public interface IReceitaIntegracaoSemaforoDao extends
        IAbstractDao<Long, ReceitaIntegracaoSemaforo> {

    List<ReceitaIntegracaoSemaforo> findByReceitaDealFiscal(Long codigoReceitaDealFiscal);
}
