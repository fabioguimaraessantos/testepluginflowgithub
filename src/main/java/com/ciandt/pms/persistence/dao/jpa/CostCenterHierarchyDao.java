package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.CostCenterHierarchy;
import com.ciandt.pms.persistence.dao.ICostCenterHierarchyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CostCenterHierarchyDao extends AbstractDao<Long, CostCenterHierarchy> implements ICostCenterHierarchyDao {

    @Autowired
    public CostCenterHierarchyDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CostCenterHierarchy.class);
    }

    @Override
    public List<CostCenterHierarchy> findByFilter(final String name, final Boolean inActive) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        params.put("inActive", inActive);

        return getJpaTemplate()
                .findByNamedQueryAndNamedParams(CostCenterHierarchy.FIND_BY_FILTER, params);
    }

    @Override
    public List<CostCenterHierarchy> findByNameOrOracleCode(final String name, final String oracleCode) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("oracleCode", oracleCode);

        return getJpaTemplate().findByNamedQueryAndNamedParams(CostCenterHierarchy.FIND_BY_NAME_OR_ORACLE_CODE, params);
    }
}
