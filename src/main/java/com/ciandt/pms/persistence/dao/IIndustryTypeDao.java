package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.IndustryType;

import java.util.List;

public interface IIndustryTypeDao extends IAbstractDao<Long, IndustryType> {

    List<IndustryType> find(IndustryType filter);

    IndustryType findById(Long id);

    IndustryType findByName(String name);

}
