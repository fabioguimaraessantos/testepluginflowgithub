package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.Area;
import com.ciandt.pms.model.Billability;
import com.ciandt.pms.persistence.dao.IBillabilityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 *
 * A classe BillabilityDao proporciona as funcionalidades da camada
 * de persistencia referente a entidade Billability.
 *
 */
@Repository
public class BillabilityDao extends AbstractDao<Long, Billability> implements IBillabilityDao {

    /**
     * Construtor padrão.
     *
     * @param factory - Fabrica da entidade.
     */
    @Autowired
    public BillabilityDao(@Qualifier("entityManagerFactory")
                   final EntityManagerFactory factory) {
        super(factory, Billability.class);
    }

    /**
     * Busca todas as entidades ativas.
     *
     * @return retorna uma lista de Area.
     */
    @SuppressWarnings("unchecked")
    public List<Billability> findAllActive() {

        List<Billability> listResult = getJpaTemplate().findByNamedQuery(
                Billability.FIND_ALL_ACTIVE);

        return listResult;
    }

    /**
            * Find Billabity by name
     *
             * @return object
     */
    @SuppressWarnings("unchecked")
    public Billability findByName(String name) {
        return (Billability)getJpaTemplate().findByNamedQuery(Billability.FIND_BY_NAME,  new Object[]{name}).get(0);
    }

}