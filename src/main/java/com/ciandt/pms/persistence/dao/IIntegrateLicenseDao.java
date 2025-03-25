package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.IntegrateLicense;

import java.util.Date;
import java.util.List;

public interface IIntegrateLicenseDao extends IAbstractDao<Long, IntegrateLicense>  {

    /**
     *
     */
    List<IntegrateLicense> findIntegratesByResourceAndDateAndProject(Long tiRecursoCode, Date period, String project);

    /**
     *
     */
    IntegrateLicense findOneIntegrateByResourceAndDateAndProjectAndCostCenter(Long tiRecursoCode, Date period, String project, String costCenter);
}