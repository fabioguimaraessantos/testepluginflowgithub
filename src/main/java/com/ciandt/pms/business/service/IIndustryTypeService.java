package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.IndustryType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IIndustryTypeService {

    /**
     * @param filter - Filter to find industries types
     * @return List of Industries Types filtered
     * @throws BusinessException Exception
     */
    List<IndustryType> findByFilter(IndustryType filter) throws BusinessException;

    /**
     * @param id Id to find industry type
     * @return Industry type
     * @throws BusinessException Exception
     */
    IndustryType findById(Long id) throws BusinessException;

    /**
     * @param code Id to inactive a industry type
     * @throws BusinessException Exception
     */
    @Transactional
    void remove(Long code) throws BusinessException;

    /**
     * @param industryType industry type to update
     */
    @Transactional
    void update(IndustryType industryType) throws BusinessException;

    /**
     * @param industryType industry type to create
     * @throws BusinessException - Exception
     */
    @Transactional
    void create(IndustryType industryType) throws BusinessException;

    /**
     * @return List of Industries Types actives
     * @throws BusinessException - Exception
     */
    List<IndustryType> findAllActive() throws BusinessException;
}