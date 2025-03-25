package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.TceLogSincronizacao;
import com.ciandt.pms.persistence.dao.ITceLogSincronizacaoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 13/06/2011
 */
@Repository
public class TceLogSincronizacaoDao extends
        AbstractDao<Long, TceLogSincronizacao> implements
        ITceLogSincronizacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo TceLogSincronizacao
     */
    @Autowired
    public TceLogSincronizacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TceLogSincronizacao.class);
    }

}