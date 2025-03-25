package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DocumentoLegalTipo;
import com.ciandt.pms.persistence.dao.IDocumentoLegalTipoDao;

/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 14/11/2013
 */
@Repository
public class DocumentoLegalTipoDao extends AbstractDao<Long, DocumentoLegalTipo>
        implements IDocumentoLegalTipoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public DocumentoLegalTipoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, DocumentoLegalTipo.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentoLegalTipo> findAll() {
		List<DocumentoLegalTipo> listResult = getJpaTemplate()
				.findByNamedQuery(DocumentoLegalTipo.FIND_ALL);

        return listResult;
    }

    /**
     * Retorna todos {@link DocumentoLegalStatus} ativos.
     * 
     * @return DocumentoLegalStatus
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentoLegalTipo> findAllActive() {
		List<DocumentoLegalTipo> listResult = getJpaTemplate()
				.findByNamedQuery(DocumentoLegalTipo.FIND_ALL_ACTIVE);

		return listResult;
    }
}