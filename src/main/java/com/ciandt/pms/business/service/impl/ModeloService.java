package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IModeloService;
import com.ciandt.pms.model.Modelo;
import com.ciandt.pms.persistence.dao.IModeloDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ModeloService implements IModeloService {

    @Autowired
    private IModeloDao repository;

    @Override
    public Modelo findDefaultModel() throws EntityNotFoundException {
        List<Modelo> modelos = repository.findDefaultModel();

        if (null == modelos || modelos.isEmpty()) {
            throw new EntityNotFoundException("Não foi encontrado nenhum modelo padrão.");
        } else if (modelos.size() > 1) {
            throw new EntityNotFoundException("Foram encontrados mais de UM modelo padrão.");
        }

        return modelos.get(0);
    }

    public List<Modelo> findAll() {
        return repository.findAll();
    }

    public Modelo findByName(String value){
        return repository.findByName(value);
    }

}
