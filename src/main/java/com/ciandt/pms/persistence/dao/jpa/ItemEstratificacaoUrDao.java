package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ItemEstratificacaoUr;
import com.ciandt.pms.persistence.dao.IItemEstratificacaoUrDao;


/**
 * 
 * A classe ItemEstratificacaoUr proporciona as funcionalidades de acesso ao
 * banco para referentes a entidade ItemEstratificacaoUr.
 * 
 * @since 15/04/2011
 * @author cmantovani
 * 
 */
@Repository
public class ItemEstratificacaoUrDao extends
        AbstractDao<Long, ItemEstratificacaoUr> implements
        IItemEstratificacaoUrDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica do entity manager
     */
    @Autowired
    public ItemEstratificacaoUrDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ItemEstratificacaoUr.class);
    }

}
