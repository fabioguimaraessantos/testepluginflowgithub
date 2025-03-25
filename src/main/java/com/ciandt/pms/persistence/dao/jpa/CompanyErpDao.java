package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.CompanyErp;
import com.ciandt.pms.persistence.dao.ICompanyErpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CompanyErpDao extends AbstractDao<Long, CompanyErp> implements ICompanyErpDao {

    @Autowired
    public CompanyErpDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CompanyErp.class);
    }

    @Override
    public List<CompanyErp> findActiveByCompany(final Long companyCode) {
        final Map<String, Object> params = new HashMap<>();
        params.put("companyCode", companyCode);

        return getJpaTemplate().findByNamedQueryAndNamedParams(CompanyErp.FIND_ACTIVE_BY_COMPANY, params);
    }

    @Override
    public List<CompanyErp> findAllActive() {
        return getJpaTemplate().findByNamedQuery(CompanyErp.FIND_ALL_ACTIVE);
    }
}
