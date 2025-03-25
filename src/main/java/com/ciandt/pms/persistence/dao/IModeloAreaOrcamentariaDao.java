package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.ModeloAreaOrcamentaria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IModeloAreaOrcamentariaDao extends IAbstractDao<Long, ModeloAreaOrcamentaria> {

    List<ModeloAreaOrcamentaria> findAll();

    ModeloAreaOrcamentaria findByCurrentAreaOrcamentaria(Long codigoAreaOrcamentaria);

    void update(ModeloAreaOrcamentaria modeloAreaOrcamentaria);
}
