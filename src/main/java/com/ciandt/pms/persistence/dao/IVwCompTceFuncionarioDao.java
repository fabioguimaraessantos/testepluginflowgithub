package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.VwCompTceFuncionario;
import com.ciandt.pms.model.VwCompTceFuncionarioId;


/**
 * 
 * A classe IVwCompTceFuncionarioDao proporciona a interface de acesso para a
 * camada de persistencia referente a entidade VwCompTceFuncionario.
 * 
 * @since 10/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IVwCompTceFuncionarioDao extends
        IAbstractDao<VwCompTceFuncionarioId, VwCompTceFuncionario> {

    /**
     * Retorna todas as entidades pela data mês.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return retorna uma lista com todas as entidades do mês.
     */
    List<VwCompTceFuncionario> findAllByDate(final Date dataMes);

}