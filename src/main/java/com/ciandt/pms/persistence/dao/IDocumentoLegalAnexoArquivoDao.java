package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.DocumentoLegalAnexoArquivo;

/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Osvaldo</a>
 * @since 18/11/2013
 */
@Repository
public interface IDocumentoLegalAnexoArquivoDao extends
		IAbstractDao<Long, DocumentoLegalAnexoArquivo> {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DocumentoLegalAnexoArquivo> findAll();

	/**
	 * Retorna todos {@link DocumentoLegalAnexoArquivo} ativos.
	 * 
	 * @return Lista de DocumentoLegalAnexoArquivo
	 */
	List<DocumentoLegalAnexoArquivo> findByDocumentoLegal(
			DocumentoLegal documentoLegal);

	/**
	 * Busca por nome de arquivo.
	 * 
	 * @param fileName
	 * @return
	 */
	List<DocumentoLegalAnexoArquivo> findByFileName(String fileName);
}