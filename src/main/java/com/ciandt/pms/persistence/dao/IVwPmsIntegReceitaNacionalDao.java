package com.ciandt.pms.persistence.dao;


import com.ciandt.pms.model.VwPmsIntegReceitaNacional;

import java.util.List;

public interface IVwPmsIntegReceitaNacionalDao extends IAbstractDao<Long, VwPmsIntegReceitaNacional>{

    List<VwPmsIntegReceitaNacional> findAll();
}
