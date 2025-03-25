package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DocumentoLegalResponsavel;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/08/2015
 */
@Service
public interface IDocumentoLegalResponsavelService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
	DocumentoLegalResponsavel findById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<DocumentoLegalResponsavel> findAll();

	void create(DocumentoLegalResponsavel entity);

	void update(DocumentoLegalResponsavel entity);

	void delete(DocumentoLegalResponsavel entity);

}