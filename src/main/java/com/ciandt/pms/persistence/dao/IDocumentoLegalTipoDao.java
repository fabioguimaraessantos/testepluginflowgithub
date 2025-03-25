package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DocumentoLegalTipo;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Osvaldo</a>
 * @since 14/11/2013
 */
@Repository
public interface IDocumentoLegalTipoDao extends IAbstractDao<Long, DocumentoLegalTipo> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<DocumentoLegalTipo> findAll();

    /**
     * Retorna todos {@link DocumentoLegalTipo} ativos.
     * 
     * @return Lista de DocumentoLegalStatus
     */
    List<DocumentoLegalTipo> findAllActive();
}