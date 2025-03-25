package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IDocumentoLegalTipoService;
import com.ciandt.pms.model.DocumentoLegalTipo;
import com.ciandt.pms.persistence.dao.IDocumentoLegalTipoDao;

/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 14/11/2013
 */
@Service
public class DocumentoLegalTipoService implements IDocumentoLegalTipoService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IDocumentoLegalTipoDao dao;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public DocumentoLegalTipo findDocumentoLegalTipoById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<DocumentoLegalTipo> findDocumentoLegalTipoAll() {
		return dao.findAll();
	}

	/**
	 * Retorna a propriedade pelo nome.
	 * 
	 * @param nome
	 *            String
	 * @return Lista de DocumentoLegalTipo
	 */
	public List<DocumentoLegalTipo> findDocumentoLegalTipoAllActive() {
		return dao.findAllActive();
	}
}