package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.PayrollEvent;
import com.ciandt.pms.persistence.dao.IPayrollEventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amanda on 31/01/17.
 */
@Repository
public class PayrollEventDao extends AbstractDao<Long, PayrollEvent> implements IPayrollEventDao {

    /**
     * Construtor padrão.
     *
     * @param factory
     *            - Fabrica da entidade.
     */
    @Autowired
    public PayrollEventDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PayrollEvent.class);
    }

    /**
     * Search all {@link PayrollEvent}
     * @return {@link PayrollEvent} list
     */
    public List<PayrollEvent> findAll() {
        return getJpaTemplate().findByNamedQuery(PayrollEvent.FIND_ALL);
    }

    public List<PayrollEvent> findByCodeAndName(Long code, String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        params.put("name", name);

        return getJpaTemplate().findByNamedQueryAndNamedParams(
                PayrollEvent.FIND_BY_CODE_AND_NAME, params);
    }
}
