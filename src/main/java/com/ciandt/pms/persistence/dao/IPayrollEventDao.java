package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PayrollEvent;

import java.util.List;

/**
 * Created by amanda on 31/01/17.
 */
public interface IPayrollEventDao extends IAbstractDao<Long, PayrollEvent> {

    List<PayrollEvent> findAll();

    List<PayrollEvent> findByCodeAndName(Long code, String name);

}
