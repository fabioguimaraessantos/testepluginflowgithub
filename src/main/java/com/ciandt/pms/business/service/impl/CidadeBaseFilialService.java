package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICidadeBaseFilialService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.CidadeBaseFilial;
import com.ciandt.pms.model.CidadeBaseFilialId;
import com.ciandt.pms.persistence.dao.ICidadeBaseFilialDao;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CidadeBaseFilialService implements ICidadeBaseFilialService {

    @Autowired
    private ICidadeBaseFilialDao dao;

    public List<CidadeBaseFilial> findAll() {
        return dao.findAll();
    }

    public CidadeBaseFilial findById(final CidadeBaseFilialId id) {
        final CidadeBaseFilial c = dao.findById(id);
        Hibernate.initialize(c.getId().getCidadeBase());
        Hibernate.initialize(c.getId().getEmpresa());
        Hibernate.initialize(c.getEmpresaFilial());
        Hibernate.initialize(c.getEmpresaMatriz());
        return c;
    }

    public List<CidadeBaseFilial> findByFilter(final CidadeBaseFilial filter) throws BusinessException {
        final List<CidadeBaseFilial> results = dao.findByFilter(filter);

        if (CollectionUtils.isEmpty(results)) {
            throw new BusinessException(Constants.MSG_NOT_FOUND_CIDADE_BASE_FILIAL);
        }

        for (final CidadeBaseFilial c : results) {
            Hibernate.initialize(c.getId().getCidadeBase());
            Hibernate.initialize(c.getId().getEmpresa());
            Hibernate.initialize(c.getEmpresaFilial());
            Hibernate.initialize(c.getEmpresaMatriz());
        }

        return results;
    }

    public void create(final CidadeBaseFilial cidadeBaseFilial) throws DataIntegrityViolationException {
        dao.create(cidadeBaseFilial);
    }

    public void update(final CidadeBaseFilial oldCidadeBaseFilial,
                       final CidadeBaseFilial newCidadeBaseFilial) throws DataIntegrityViolationException {

        if (!Objects.equals(newCidadeBaseFilial.getId(), oldCidadeBaseFilial.getId())) {
            final CidadeBaseFilial alreadyExistentNewCidadeBaseFilialId = dao.findById(newCidadeBaseFilial.getId());
            if (alreadyExistentNewCidadeBaseFilialId != null) {
                throw new DataIntegrityViolationException(Constants.MSG_ALREADY_EXISTS_CIDADE_BASE_FILIAL);
            }
        }

        dao.remove(oldCidadeBaseFilial);
        dao.update(newCidadeBaseFilial);
    }

    public void remove(final CidadeBaseFilial cidadeBaseFilial) throws DataIntegrityViolationException {
        dao.remove(cidadeBaseFilial);
    }
}
