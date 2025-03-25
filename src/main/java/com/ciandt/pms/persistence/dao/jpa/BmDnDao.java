package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.BmDn;
import com.ciandt.pms.persistence.dao.IBmDnDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class BmDnDao extends AbstractDao<Long, BmDn> implements IBmDnDAO {

    /**
     * Contrutor padrão.
     *
     * @param factory da entidade
     */
    @Autowired
    public BmDnDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, BmDn.class);
    }

    @Override
    public List<BmDn> find(BmDn filter) {
        return getJpaTemplate().findByNamedQuery(
                BmDn.FIND_BY_FILTER,
                new Object[]{filter.getName(), filter.getName(), filter.getInActive(), filter.getInActive()});
    }

    @Override
    public BmDn findById(Long id) {
        return getJpaTemplate().find(BmDn.class, id);
    }

    @Override
    public BmDn findByName(String name) {
        List<BmDn> result = getJpaTemplate().findByNamedQuery(BmDn.FIND_BY_NAME, new Object[]{name});
        if (result != null && !result.isEmpty())
            return result.get(0);

        return null;
    }
}
