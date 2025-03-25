package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IPayrollEventService;
import com.ciandt.pms.model.PayrollEvent;
import com.ciandt.pms.persistence.dao.IPayrollEventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by amanda on 31/01/17.
 */
@Service
public class PayrollEventService implements IPayrollEventService {

    @Autowired
    private IPayrollEventDao dao;

    /**
     * Creates an entity.
     *
     * @param entity
     *            entity to be created.
     */
    @Transactional
    public void create(final PayrollEvent entity) {
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
    public void update(final PayrollEvent entity) {
        dao.update(entity);
    }

    /**
     * Deletes an entity.
     *
     * @param entity
     *            entity to be removed.
     */
    @Transactional
    public void remove(final PayrollEvent entity) {
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
    public PayrollEvent findById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Find all entities.
     *
     * @return entity list.
     */
    public List<PayrollEvent> findAll() {
        return dao.findAll();
    }

    public List<PayrollEvent> findByCodeAndName(Long code, String name) {
        return dao.findByCodeAndName(code, name);
    }
}
