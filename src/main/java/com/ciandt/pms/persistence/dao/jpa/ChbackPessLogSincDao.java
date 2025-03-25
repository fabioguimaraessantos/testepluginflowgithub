package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ChbackPessLogSincronizacao;
import com.ciandt.pms.persistence.dao.IChbackPessLogSincDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 26/08/2011
 */
@Repository
public class ChbackPessLogSincDao extends
        AbstractDao<Long, ChbackPessLogSincronizacao> implements
        IChbackPessLogSincDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo ChbackPessLogSincronizacao
     */
    @Autowired
    public ChbackPessLogSincDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ChbackPessLogSincronizacao.class);
    }

}