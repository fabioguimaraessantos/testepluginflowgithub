package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.Modelo;
import com.ciandt.pms.persistence.dao.IModeloDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class ModeloDao extends AbstractDao<Long, Modelo> implements IModeloDao {

    /**
     * Construtor padrão.
     *
     * @param factory     EntityManagerFactory
     */
    @Autowired
    public ModeloDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Modelo.class);
    }

    @Override
    public List<Modelo> findDefaultModel() {
        return getJpaTemplate().findByNamedQuery(Modelo.FIND_DEFAULT_MODEL);
    }

    public List<Modelo> findAll() {
        return getJpaTemplate().findByNamedQuery(Modelo.FIND_ALL);
    }

    public Modelo findByName(String value) {
        return (Modelo) getJpaTemplate().findByNamedQuery(Modelo.FIND_BY_NAME, value).get(0);
    }
}
