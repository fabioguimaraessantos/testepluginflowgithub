package com.ciandt.pms.business.service;

import com.ciandt.pms.model.PayrollEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by amanda on 31/01/17.
 */
@Service
public interface IPayrollEventService {

    /**
     * Creates an entity.
     *
     * @param entity
     *            entity to be created.
     */
    @Transactional
    void create(final PayrollEvent entity);

    /**
     * Updates the entity
     *
     * @param entity
     *            to be updated.
     *
     */
    @Transactional
    void update(final PayrollEvent entity);

    /**
     * Deletes an entity.
     *
     * @param entity
     *            entity to be removed.
     */
    @Transactional
    void remove(final PayrollEvent entity);

    /**
     * Find an entity by id.
     *
     * @param id
     *            entity's id.
     *
     * @return corresponding entity.
     */
    PayrollEvent findById(final Long id);

    /**
     * Find all entities.
     *
     * @return entity list.
     */
    List<PayrollEvent> findAll();

    List<PayrollEvent> findByCodeAndName(Long code, String name);
}
