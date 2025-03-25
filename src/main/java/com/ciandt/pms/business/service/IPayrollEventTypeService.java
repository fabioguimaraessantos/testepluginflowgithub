package com.ciandt.pms.business.service;

import com.ciandt.pms.model.PayrollEventType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by amanda on 31/01/17.
 */
@Service
public interface IPayrollEventTypeService {

    /**
     * Creates an entity.
     *
     * @param entity
     *            entity to be created.
     */
    @Transactional
    void create(final PayrollEventType entity);

    /**
     * Updates the entity
     *
     * @param entity
     *            to be updated.
     *
     */
    @Transactional
    void update(final PayrollEventType entity);

    /**
     * Deletes an entity.
     *
     * @param entity
     *            entity to be removed.
     */
    @Transactional
    void remove(final PayrollEventType entity);

    /**
     * Find an entity by id.
     *
     * @param id
     *            entity's id.
     *
     * @return corresponding entity.
     */
    PayrollEventType findById(final Long id);

    /**
     * Find all entities.
     *
     * @return entity list.
     */
    List<PayrollEventType> findAll();
}
