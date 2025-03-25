package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PayrollEventType;

import java.util.List;

/**
 * Created by amanda on 31/01/17.
 */
public interface IPayrollEventTypeDao extends IAbstractDao<Long, PayrollEventType> {

    List<PayrollEventType> findAll();

}
