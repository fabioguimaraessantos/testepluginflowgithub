package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CompanyErp;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ICompanyErpService {

    Map<Long, String> findAllActive() throws BusinessException;

    CompanyErp findActiveByCompany(final Long companyCode) throws BusinessException;
}
