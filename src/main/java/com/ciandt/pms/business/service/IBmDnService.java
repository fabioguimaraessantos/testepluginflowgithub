package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.BmDn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IBmDnService {
    /**
     * @param filter - Filter to find bmdns
     * @return List of bmdns filtered
     * @throws BusinessException Exception
     */
    List<BmDn> findByFilter(BmDn filter) throws BusinessException;

    /**
     * @param id Id to find bmDn
     * @return bmDn
     * @throws BusinessException Exception
     */
    BmDn findById(Long id) throws BusinessException;

    /**
     * @param code Id to inactive a bmDn
     * @throws BusinessException Exception
     */
    @Transactional
    void remove(Long code) throws BusinessException;

    /**
     * @param bmdn bmDn to update
     */
    @Transactional
    void update(BmDn bmdn) throws BusinessException;

    /**
     * @param bmdn bmDn to create
     * @throws BusinessException - Exception
     */
    @Transactional
    void create(BmDn bmdn) throws BusinessException;

    /**
     * @return List of bmdn actives
     * @throws BusinessException - Exception
     */
    List<BmDn> findAllActive() throws BusinessException;
}