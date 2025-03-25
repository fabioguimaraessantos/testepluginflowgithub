package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IDocumentoLegalResponsavelService;
import com.ciandt.pms.business.service.IDocumentoLegalService;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.DocumentoLegalResponsavel;
import com.ciandt.pms.persistence.dao.IDocumentoLegalResponsavelDao;

/**
 * A classe ControleReajusteService proporciona as funcionalidades de serviço
 * referente a entidade ControleReajuste.
 * 
 * @since 11/08/2015
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class DocumentoLegalResponsavelService implements
		IDocumentoLegalResponsavelService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IDocumentoLegalResponsavelDao dao;
	
	/** Instancia do DAO da entidade. */
	@Autowired
	private IDocumentoLegalService documentoLegalService;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public DocumentoLegalResponsavel findById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<DocumentoLegalResponsavel> findAll() {
		return dao.findAll();
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void create(final DocumentoLegalResponsavel entity) {
		dao.create(entity);
	}

	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void update(final DocumentoLegalResponsavel entity) {
		dao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 */
	@Override
	@Transactional
	public void delete(final DocumentoLegalResponsavel dlr) {
		DocumentoLegal dl = documentoLegalService
				.findDocumentoLegalById(dlr.getDocumentoLegal().getCodigoDocumentoLegal());
		dl.getDocumentoLegalResponsaveis().remove(dlr);

		dao.remove(dlr);
	}
}