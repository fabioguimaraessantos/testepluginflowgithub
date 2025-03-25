package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DocumentoLegalResponsavel;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/08/2015
 */
@Repository
public interface IDocumentoLegalResponsavelDao extends IAbstractDao<Long, DocumentoLegalResponsavel> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<DocumentoLegalResponsavel> findAll();

}