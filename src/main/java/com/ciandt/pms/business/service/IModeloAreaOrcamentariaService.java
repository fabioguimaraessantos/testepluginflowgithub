package com.ciandt.pms.business.service;

import com.ciandt.pms.model.ModeloAreaOrcamentaria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Define a interface do Service da entidade.
 *
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IModeloAreaOrcamentariaService {

    /**
     * Insere uma entidade.
     *
     * @param entity
     */
    @Transactional
    void create(final ModeloAreaOrcamentaria entity);

    ModeloAreaOrcamentaria findByCurrentAreaOrcamentaria(Long codigoAreaOrcamentaria);

    void update(ModeloAreaOrcamentaria modeloAreaOrcamentariaAtual);
}