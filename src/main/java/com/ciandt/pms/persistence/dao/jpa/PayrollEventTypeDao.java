package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.PayrollEventType;
import com.ciandt.pms.persistence.dao.IPayrollEventTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by amanda on 31/01/17.
 */
@Repository
public class PayrollEventTypeDao extends AbstractDao<Long, PayrollEventType> implements IPayrollEventTypeDao {

    /**
     * Construtor padrão.
     *
     * @param factory
     *            - Fabrica da entidade.
     */
    @Autowired
    public PayrollEventTypeDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PayrollEventType.class);
    }

    /**
     * Search all {@link PayrollEventType}
     * @return {@link PayrollEventType} list
     */
    public List<PayrollEventType> findAll() {
        return getJpaTemplate().findByNamedQuery(PayrollEventType.FIND_ALL);
    }

}
