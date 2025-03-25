package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Potencial;
import com.ciandt.pms.persistence.dao.IPotencialDao;


/**
 * 
 * A classe AceleradorDao proporciona as funcionalidades de persistencia
 * referente a entidade Acelerador.
 * 
 * @since 06/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class PotencialDao extends AbstractDao<Long, Potencial> implements
        IPotencialDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica do entity manager
     */
    @Autowired
    public PotencialDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Potencial.class);
    }

    /**
     * Busca todas as entidades ativas.
     * 
     * @return retorna uma lista de Potencial.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Potencial> findAllActive() {
        return getJpaTemplate().findByNamedQuery(Potencial.FIND_ALL_ACTIVE);
    }

}
