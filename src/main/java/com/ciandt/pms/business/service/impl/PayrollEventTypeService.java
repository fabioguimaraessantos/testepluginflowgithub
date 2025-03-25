package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IPayrollEventTypeService;
import com.ciandt.pms.model.PayrollEventType;
import com.ciandt.pms.persistence.dao.IPayrollEventTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by amanda on 31/01/17.
 */
@Service
public class PayrollEventTypeService implements IPayrollEventTypeService {

    @Autowired
    private IPayrollEventTypeDao dao;

    /**
     * Creates an entity.
     *
     * @param entity
     *            entity to be created.
     */
    @Transactional
    public void create(final PayrollEventType entity) {
        dao.create(entity);
    }

    /**
     * Updates the entity
     *
     * @param entity
     *            to be updated.
     *
     */
    @Transactional
    public void update(final PayrollEventType entity) {
        dao.update(entity);
    }

    /**
     * Deletes an entity.
     *
     * @param entity
     *            entity to be removed.
     */
    @Transactional
    public void remove(final PayrollEventType entity) {
        dao.remove(entity);
    }

    /**
     * Find an entity by id.
     *
     * @param id
     *            entity's id.
     *
     * @return corresponding entity.
     */
    public PayrollEventType findById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Find all entities.
     *
     * @return entity list.
     */
    public List<PayrollEventType> findAll() {
        return dao.findAll();
    }

}
