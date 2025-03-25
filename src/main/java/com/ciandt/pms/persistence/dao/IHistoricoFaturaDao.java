package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.HistoricoFatura;

/**
 * 
 * A classe IHistoricoFaturaDao proporciona interface de acesso ao banco de
 * dados referente a entidade HistoricoFatura.
 * 
 * @since 10/05/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IHistoricoFaturaDao extends
        IAbstractDao<Long, HistoricoFatura> {

	List<HistoricoFatura> findByCodigoFatura(Long codigoFatura);
}