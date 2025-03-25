package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.TipoOverhead;
import com.ciandt.pms.persistence.dao.ITipoOverheadDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 19/07/2010
 */
@Repository
public class TipoOverheadDao extends AbstractDao<Long, TipoOverhead> implements
        ITipoOverheadDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo NaturezaCentroLucro
     */
    @Autowired
    public TipoOverheadDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TipoOverhead.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TipoOverhead> findAll() {
        List<TipoOverhead> listResult = getJpaTemplate().findByNamedQuery(
                TipoOverhead.FIND_ALL);

        return listResult;
    }

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    @SuppressWarnings("unchecked")
    public List<TipoOverhead> findAllActive() {
        List<TipoOverhead> listResult = getJpaTemplate().findByNamedQuery(
                TipoOverhead.FIND_ALL_ACTIVE);

        return listResult;
    }

}