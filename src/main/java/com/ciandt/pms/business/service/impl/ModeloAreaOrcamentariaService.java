package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IModeloAreaOrcamentariaService;
import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.ModeloAreaOrcamentaria;
import com.ciandt.pms.persistence.dao.IModeloAreaOrcamentariaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModeloAreaOrcamentariaService implements IModeloAreaOrcamentariaService {

    @Autowired
    private IModeloAreaOrcamentariaDao repository;

    /**
     * Insere uma entidade.
     *
     * @param entity
     */
    @Override
    @Transactional
    public void create(final ModeloAreaOrcamentaria entity) {
        repository.create(entity);
        repository.flush();
    }

    public ModeloAreaOrcamentaria findByCurrentAreaOrcamentaria(Long codigoAreaOrcamentaria) {
        return repository.findByCurrentAreaOrcamentaria(codigoAreaOrcamentaria);
    }

    public void update(ModeloAreaOrcamentaria modeloAreaOrcamentariaAtual) {
        repository.update(modeloAreaOrcamentariaAtual);
    }

}
