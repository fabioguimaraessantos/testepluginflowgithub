package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DocumentoLegalTipo;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 14/11/2013
 */
@Service
public interface IDocumentoLegalTipoService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
	DocumentoLegalTipo findDocumentoLegalTipoById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<DocumentoLegalTipo> findDocumentoLegalTipoAll();

    /**
     * Retorna todos {@link DocumentoLegalTipo} ativos.
     * 
     * @return Lista de DocumentoLegalTipo
     */
    List<DocumentoLegalTipo> findDocumentoLegalTipoAllActive();
}