package com.ciandt.pms.business.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IDocumentoLegalAnexoArquivoService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.DocumentoLegalAnexoArquivo;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.persistence.dao.IDocumentoLegalAnexoArquivoDao;

/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 14/11/2013
 */
@Service
public class DocumentoLegalAnexoArquivoService implements
		IDocumentoLegalAnexoArquivoService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IDocumentoLegalAnexoArquivoDao dao;

	/** Instancia do sevico {@link UploadArquivo}. */
	@Autowired
	private IUploadArquivoService uploadArquivoService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public DocumentoLegalAnexoArquivo findDocumentoLegalAnexoArquivoById(
			final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<DocumentoLegalAnexoArquivo> findDocumentoLegalAnexoArquivoAll() {
		return dao.findAll();
	}

	/**
	 * Retorna todos {@link DocumentoLegalAnexoArquivo} ativos.
	 * 
	 * @return Lista de DocumentoLegalAnexoArquivo
	 */
	@Override
	public List<DocumentoLegalAnexoArquivo> findDocumentoLegalAnexoArquivoByDocumentoLegal(
			DocumentoLegal documentoLegal) {
		return dao.findByDocumentoLegal(documentoLegal);
	}

	/**
	 * Cria um anexo.
	 * 
	 * @param entity
	 * @return
	 */
	public Boolean createDocumentoLegalAnexoArquivo(
			DocumentoLegalAnexoArquivo entity) {
		dao.create(entity);

		return true;
	}

	/**
	 * Deleta uma entidade do banco.
	 * 
	 * @param entity
	 * @return
	 */
	@Transactional
	public Boolean deleteDocumentoLegalAnexoArquivo(
			DocumentoLegalAnexoArquivo entity) {
		dao.remove(entity);

		return true;
	}

	/**
	 * Atualiza um docLegal anexo
	 * 
	 * @param entity
	 * @return
	 */
	public Boolean updateDocumentolegalAnexoArquivo(
			DocumentoLegalAnexoArquivo entity) {
		dao.update(entity);

		return true;
	}

	/**
	 * Recupera o arquivo do filesystem para download.
	 * 
	 * @param Nome
	 *            do arquivo filename
	 */
	public void downloadDocumentoLegalAnexoArquivo(String fileName) {

		String contentType = "";

		uploadArquivoService
				.downloadFile((String) systemProperties
						.get("upload.anexo_documento_legal.destination.path"), fileName,
						contentType);
	}

	/**
	 * Deleta o arquivo do sistema de arquivos com o nome {@code fileName}
	 * 
	 * @param fileName
	 */
	@Transactional
	public void deleteDocumentoLegalAnexoArquivo(String fileName) {

		File file = new File(
				(String) systemProperties.get("upload.anexo_documento_legal.destination.path")
						+ fileName);

		file.delete();
	}

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
	@Transactional
	public void saveUploadAnexo(UploadItem anexo,
			DocumentoLegalAnexoArquivo entity) throws IOException {

		StringBuffer fileName = new StringBuffer();
		fileName.append(entity.getDocumentoLegal().getCodigoCodigoGerado());
		fileName.append("_");
		fileName.append(anexo.getFileName());

		if (!this.findByFileName(fileName.toString()).isEmpty()) {
			Messages.showError("saveUploadAnexo",
					Constants.LEGAL_DOC_ANEXO_ALREADY_EXISTS);
			return;
		}

		entity.setNomeDocLegalAnexoArq(fileName.toString());

		FileOutputStream fos = new FileOutputStream(
				(String) systemProperties.get("upload.anexo_documento_legal.destination.path")
						+ fileName.toString());
		fos.write(anexo.getData());
		fos.close();

		this.createDocumentoLegalAnexoArquivo(entity);

		Messages.showSucess("deleteAnexoDocLegal",
				Constants.GENEREC_MSG_SUCCESS_ADD);
	}

	/**
	 * Busca por nome de arquivo.
	 * 
	 * @param fileName
	 * @return
	 */
	public List<DocumentoLegalAnexoArquivo> findByFileName(String fileName) {
		return dao.findByFileName(fileName);
	}
}