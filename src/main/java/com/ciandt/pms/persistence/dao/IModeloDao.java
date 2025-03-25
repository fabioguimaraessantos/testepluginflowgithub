package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.Modelo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IModeloDao extends IAbstractDao<Long, Modelo> {

    List<Modelo> findDefaultModel();

    List<Modelo> findAll();

    Modelo findByName(String value);
}
