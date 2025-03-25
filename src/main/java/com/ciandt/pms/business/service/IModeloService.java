package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Modelo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IModeloService {

    Modelo findDefaultModel();

    List<Modelo> findAll();

    Modelo findByName(String value);
}
