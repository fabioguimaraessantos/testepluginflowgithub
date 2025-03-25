package com.ciandt.pms.persistence.dao;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ChbackPessLogSincronizacao;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 07/06/2010
 */
@Repository
public interface IChbackPessLogSincDao extends
        IAbstractDao<Long, ChbackPessLogSincronizacao> {

}