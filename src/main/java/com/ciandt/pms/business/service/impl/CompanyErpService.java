package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICompanyErpService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CompanyErp;
import com.ciandt.pms.persistence.dao.ICompanyErpDao;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompanyErpService implements ICompanyErpService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyErpService.class);

    @Autowired
    private ICompanyErpDao companyErpDao;

    @Override
    public CompanyErp findActiveByCompany(final Long companyCode) throws BusinessException {
        final List<CompanyErp> companyErpList = companyErpDao.findActiveByCompany(companyCode);

        if (CollectionUtils.isEmpty(companyErpList)) {
            logger.info("No active CompanyErp found for companyCode: {}", companyCode);
            return null;
        }

        if (CollectionUtils.size(companyErpList) > 1) logAndShowError(companyCode);

        return companyErpList.get(0);
    }

    @Override
    public Map<Long, String> findAllActive() throws BusinessException {
        final List<CompanyErp> companyErpList = companyErpDao.findAllActive();
        final Map<Long, String> companyErpMap = new HashMap<>();

        for (final CompanyErp companyErp : companyErpList) {
            if (companyErp == null || companyErp.getCompany() == null ||
                    companyErp.getCompany().getCodigoEmpresa() == null) continue;

            final Long companyCode = companyErp.getCompany().getCodigoEmpresa();
            if (companyErpMap.containsKey(companyCode)) logAndShowError(companyCode);

            companyErpMap.put(companyCode, companyErp.getErpName());
        }

        return Collections.unmodifiableMap(companyErpMap);
    }

    private void logAndShowError(final Long companyCode) throws BusinessException {
        String message = Constants.DEFAULT_MSG_ERROR_COMPANY_ERP_HAS_MORE_THAN_ONE_ACTIVE.replace("{0}", String.valueOf(companyCode));
        logger.error(message);
        throw new BusinessException(message);
    }
}
