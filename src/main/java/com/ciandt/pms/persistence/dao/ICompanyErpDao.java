package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.CompanyErp;

import java.util.List;
import java.util.Map;

public interface ICompanyErpDao extends IAbstractDao <Long, CompanyErp> {

    List<CompanyErp> findAllActive();

    List<CompanyErp> findActiveByCompany(final Long companyCode);
}
