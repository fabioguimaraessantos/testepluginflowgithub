package com.ciandt.pms.business.service;

import java.io.IOException;
import java.util.List;

import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.DocumentoLegalAnexoArquivo;

/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 18/11/2013
 */
@Service
public interface IDocumentoLegalAnexoArquivoService {

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	DocumentoLegalAnexoArquivo findDocumentoLegalAnexoArquivoById(final Long id);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DocumentoLegalAnexoArquivo> findDocumentoLegalAnexoArquivoAll();

	/**
	 * Retorna todos {@link DocumentoLegalAnexoArquivo} ativos.
	 * 
	 * @return Lista de DocumentoLegalAnexoArquivo
	 */
	List<DocumentoLegalAnexoArquivo> findDocumentoLegalAnexoArquivoByDocumentoLegal(
			DocumentoLegal documentoLegal);

	/**
	 * Save anexoTabelaPrevo.
	 * 
	 * @param anexoTabelaPreco
	 *            anexoTabelaPreco
	 * @param fileBytes
	 *            bytes.
	 * @throws IOException
	 *             excecao.
	 * 
	 */
	void saveUploadAnexo(UploadItem anexo, DocumentoLegalAnexoArquivo entity)
			throws IOException;

	/**
	 * Recupera o arquivo do filesystem para download.
	 * 
	 * @param Nome
	 *            do arquivo filename
	 */
	void downloadDocumentoLegalAnexoArquivo(String fileName);

	/**
	 * Deleta uma entidade do banco.
	 * 
	 * @param entity
	 * @return
	 */
	Boolean deleteDocumentoLegalAnexoArquivo(DocumentoLegalAnexoArquivo entity);

	/**
	 * Deleta o arquivo do sistema de arquivos com o nome {@code fileName}
	 * 
	 * @param fileName
	 */
	void deleteDocumentoLegalAnexoArquivo(String fileName);

	/**
	 * Busca por nome de arquivo.
	 * 
	 * @param fileName
	 * @return
	 */
	List<DocumentoLegalAnexoArquivo> findByFileName(String fileName);

}