package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.IntegrateLicense;
import com.ciandt.pms.persistence.dao.IIntegrateLicenseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class IntegrateLicenseDao extends AbstractDao<Long, IntegrateLicense> implements IIntegrateLicenseDao {

    @Autowired
    public IntegrateLicenseDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, IntegrateLicense.class);
    }

    @Override
    public List<IntegrateLicense> findIntegratesByResourceAndDateAndProject(Long tiRecursoCode, Date period, String project) {
        List<IntegrateLicense> result = getJpaTemplate().findByNamedQuery(IntegrateLicense.FIND_BY_RESOURCE_DATE_PROJECT, tiRecursoCode, period, project);
        if(result != null && !result.isEmpty())
            return result;

        return Collections.emptyList();
    }

    @Override
    public IntegrateLicense findOneIntegrateByResourceAndDateAndProjectAndCostCenter(Long tiRecursoCode, Date period, String project, String costCenter) {
        List<IntegrateLicense> result = getJpaTemplate().findByNamedQuery(IntegrateLicense.FIND_BY_RESOURCE_DATE_PROJECT_COST_CENTER, tiRecursoCode, period, project, costCenter);
        if(result != null && !result.isEmpty())
            return result.get(0);

        return null;
    }
}