package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.DocumentoLegalAnexoArquivo;
import com.ciandt.pms.persistence.dao.IDocumentoLegalAnexoArquivoDao;

/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 14/11/2013
 */
@Repository
public class DocumentoLegalAnexoArquivoDao extends
		AbstractDao<Long, DocumentoLegalAnexoArquivo> implements
		IDocumentoLegalAnexoArquivoDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            Entidade do tipo Atividade
	 */
	@Autowired
	public DocumentoLegalAnexoArquivoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, DocumentoLegalAnexoArquivo.class);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DocumentoLegalAnexoArquivo> findAll() {
		List<DocumentoLegalAnexoArquivo> listResult = getJpaTemplate()
				.findByNamedQuery(DocumentoLegalAnexoArquivo.FIND_ALL);
		return listResult;
	}

	/**
	 * Retorna todos {@link DocumentoLegalAnexoArquivo} ativos.
	 * 
	 * @return Lista de DocumentoLegalAnexoArquivo
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DocumentoLegalAnexoArquivo> findByDocumentoLegal(
			DocumentoLegal documentoLegal) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("documentoLegal", documentoLegal);

		List<DocumentoLegalAnexoArquivo> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						DocumentoLegalAnexoArquivo.FIND_BY_DOCUMENTO_LEGAL,
						params);

		return listResult;
	}

	/**
	 * Busca por nome de arquivo.
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DocumentoLegalAnexoArquivo> findByFileName(String fileName) {
		List<DocumentoLegalAnexoArquivo> resultList = getJpaTemplate()
				.findByNamedQuery(DocumentoLegalAnexoArquivo.FIND_BY_FILE_NAME,
						new Object[] { fileName });

		return resultList;
	}
}