package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CidadeBaseFilial;
import com.ciandt.pms.model.CidadeBaseFilialId;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface ICidadeBaseFilialService {

    public List<CidadeBaseFilial> findAll();

    public CidadeBaseFilial findById(final CidadeBaseFilialId id);

    public List<CidadeBaseFilial> findByFilter(final CidadeBaseFilial filter) throws BusinessException;

    @Transactional
    public void create(final CidadeBaseFilial cidadeBaseFilial) throws DataIntegrityViolationException;

    @Transactional
    public void update(final CidadeBaseFilial oldCidadeBaseFilial,
                       final CidadeBaseFilial newCidadeBaseFilial) throws DataIntegrityViolationException;

    @Transactional
    public void remove(final CidadeBaseFilial cidadeBaseFilial) throws DataIntegrityViolationException;
}
