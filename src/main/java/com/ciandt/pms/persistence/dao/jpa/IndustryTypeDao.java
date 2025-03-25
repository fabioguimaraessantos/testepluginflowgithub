package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.IndustryType;
import com.ciandt.pms.persistence.dao.IIndustryTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class IndustryTypeDao extends AbstractDao<Long, IndustryType> implements IIndustryTypeDao {

    /**
     * Contrutor padrão.
     *
     * @param factory da entidade
     */
    @Autowired
    public IndustryTypeDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, IndustryType.class);
    }

    @Override
    public List<IndustryType> find(IndustryType filter) {
        return getJpaTemplate().findByNamedQuery(
                IndustryType.FIND_BY_FILTER,
                new Object[]{filter.getName(), filter.getName(), filter.getInActive(), filter.getInActive()});
    }

    @Override
    public IndustryType findById(Long id) {
        return getJpaTemplate().find(IndustryType.class, id);
    }

    @Override
    public IndustryType findByName(String name) {
        List<IndustryType> result = getJpaTemplate().findByNamedQuery(IndustryType.FIND_BY_NAME, new Object[]{name});
        if (result != null && !result.isEmpty())
            return result.get(0);

        return null;
    }
}