package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DocumentoLegalResponsavel;
import com.ciandt.pms.persistence.dao.IDocumentoLegalResponsavelDao;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @since 11/08/2015
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Repository
public class DocumentoLegalResponsavelDao extends AbstractDao<Long, DocumentoLegalResponsavel>
        implements IDocumentoLegalResponsavelDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public DocumentoLegalResponsavelDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, DocumentoLegalResponsavel.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
	@Override
	@SuppressWarnings("unchecked")
	public List<DocumentoLegalResponsavel> findAll() {
		List<DocumentoLegalResponsavel> listResult = getJpaTemplate()
				.findByNamedQuery(DocumentoLegalResponsavel.FIND_ALL);

		return listResult;
	}
}