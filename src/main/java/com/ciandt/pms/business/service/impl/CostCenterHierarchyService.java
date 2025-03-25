package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICostCenterHierarchyService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CostCenterHierarchy;
import com.ciandt.pms.persistence.dao.ICostCenterHierarchyDao;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CostCenterHierarchyService implements ICostCenterHierarchyService {

    private static final Logger logger = LogManager.getLogger(CostCenterHierarchyService.class);

    private static final String EXCEPTION_NOT_FOUND = "CostCenterHierarchy not found.";
    private static final String EXCEPTION_COST_CENTER_HIERARCHY_IS_REQUIRED = "CostCenterHierarchy is required.";

    @Autowired
    private ICostCenterHierarchyDao costCenterHierarchyDao;

    @Override
    public CostCenterHierarchy findByCode(final Long code) throws BusinessException {
        CostCenterHierarchy costCenterHierarchy = costCenterHierarchyDao.findById(code);

        if (costCenterHierarchy == null) {
            logger.info("CostCenterHierarchy not found for code {}.", code);
            throw new BusinessException(EXCEPTION_NOT_FOUND);
        }

        return costCenterHierarchy;
    }

    @Override
    public List<CostCenterHierarchy> findByFilter(final CostCenterHierarchy filter) throws BusinessException {
        List<CostCenterHierarchy> costCenterHierarchyList = costCenterHierarchyDao
                .findByFilter(filter.getName(), filter.getInActive());

        if (costCenterHierarchyList == null || costCenterHierarchyList.isEmpty()) {
            logger.info("CostCenterHierarchy not found for filter {}.", filter);
            throw new BusinessException(EXCEPTION_NOT_FOUND);
        }

        return costCenterHierarchyList;
    }

    @Override
    public List<CostCenterHierarchy> findAllActive() {
        return costCenterHierarchyDao.findByFilter(null, Boolean.TRUE);
    }

    @Override
    public List<CostCenterHierarchy> findAllActiveIncludingCurrentIfInactive(final CostCenterHierarchy currentCostCenterHierarchy) {
        final List<CostCenterHierarchy> costCenterHierarchyList = new ArrayList<>(costCenterHierarchyDao
                .findByFilter(null, Boolean.TRUE));

        if (currentCostCenterHierarchy != null && Boolean.FALSE.equals(currentCostCenterHierarchy.getInActive()))
            costCenterHierarchyList.add(currentCostCenterHierarchy);

        return costCenterHierarchyList;
    }

    @Override
    public void remove(final Long code) throws BusinessException {
        CostCenterHierarchy costCenterHierarchy = costCenterHierarchyDao.findById(code);

        if (costCenterHierarchy == null) {
            logger.info("CostCenterHierarchy not found for code {}.", code);
            throw new BusinessException(EXCEPTION_NOT_FOUND);
        }

        costCenterHierarchyDao.remove(costCenterHierarchy);
    }

    @Override
    public void create(final CostCenterHierarchy costCenterHierarchy) throws BusinessException {
        if (costCenterHierarchy == null) throw new BusinessException(EXCEPTION_COST_CENTER_HIERARCHY_IS_REQUIRED);

        if (isInvalidOracleCode(costCenterHierarchy)) {
            logger.info("CostCenterHierarchy Oracle Code is invalid: {}. Should accept just letters [A-Z],[a-z] or numbers [0-9].",
                    costCenterHierarchy.getOracleCode());
            throw new BusinessException(Constants.MSG_COST_CENTER_HIERARCHY_ORACLE_CODE_IS_INVALID);
        }

        final List<CostCenterHierarchy> costCenterHierarchyList = costCenterHierarchyDao
                .findByNameOrOracleCode(costCenterHierarchy.getName(), costCenterHierarchy.getOracleCode());

        if (costCenterHierarchyList != null && !costCenterHierarchyList.isEmpty()) {
            logger.info("CostCenterHierarchy already exists with Name: {} or Oracle Code: {}.",
                    costCenterHierarchy.getName(), costCenterHierarchy.getOracleCode());
            throw new BusinessException(Constants.MSG_COST_CENTER_HIERARCHY_ALREADY_EXISTS_WITH_NAME_ORACLE_CODE);
        }

        costCenterHierarchy.setName(StringUtils.trim(costCenterHierarchy.getName()));
        costCenterHierarchy.setOracleCode(StringUtils.trim(costCenterHierarchy.getOracleCode()));
        costCenterHierarchy.setCreatedAt(new Date());
        costCenterHierarchy.setUpdatedAt(new Date());
        costCenterHierarchy.setInActive(Boolean.TRUE);

        costCenterHierarchyDao.create(costCenterHierarchy);
    }

    @Override
    public void update(final CostCenterHierarchy costCenterHierarchy) throws BusinessException {
        if (costCenterHierarchy == null) throw new BusinessException(EXCEPTION_COST_CENTER_HIERARCHY_IS_REQUIRED);

        if (isInvalidOracleCode(costCenterHierarchy)) {
            logger.info("CostCenterHierarchy Oracle Code is invalid: {}. Should accept just letters [A-Z],[a-z] or numbers [0-9].",
                    costCenterHierarchy.getOracleCode());
            throw new BusinessException(Constants.MSG_COST_CENTER_HIERARCHY_ORACLE_CODE_IS_INVALID);
        }

        final List<CostCenterHierarchy> costCenterHierarchyList = costCenterHierarchyDao
                .findByNameOrOracleCode(costCenterHierarchy.getName(), costCenterHierarchy.getOracleCode());
        final boolean nameOrOracleCodeAlreadyExistsAndEntityHasNotTheSameCode = costCenterHierarchyList != null && (costCenterHierarchyList.size() > 1 ||
                !costCenterHierarchyList.isEmpty() && !Objects.equals(costCenterHierarchyList.get(0).getCode(), costCenterHierarchy.getCode()));

        if (nameOrOracleCodeAlreadyExistsAndEntityHasNotTheSameCode) {
            logger.info("There are more than one CostCenterHierarchy with this Name: {} or Oracle Code: {}.",
                    costCenterHierarchy.getName(), costCenterHierarchy.getOracleCode());
            throw new BusinessException(Constants.MSG_COST_CENTER_HIERARCHY_ALREADY_EXISTS_WITH_NAME_ORACLE_CODE);
        }

        costCenterHierarchy.setName(StringUtils.trim(costCenterHierarchy.getName()));
        costCenterHierarchy.setOracleCode(StringUtils.trim(costCenterHierarchy.getOracleCode()));
        costCenterHierarchy.setUpdatedAt(new Date());
        costCenterHierarchyDao.update(costCenterHierarchy);
    }

    private boolean isInvalidOracleCode(CostCenterHierarchy costCenterHierarchy) {
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = pattern.matcher(costCenterHierarchy.getOracleCode());
        return matcher.find();
    }
}
