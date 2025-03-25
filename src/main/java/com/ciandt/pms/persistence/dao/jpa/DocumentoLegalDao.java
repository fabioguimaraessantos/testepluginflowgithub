package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IDocumentoLegalDao;

/**
 * 
 * @author peter
 * 
 */
@Repository
public class DocumentoLegalDao extends AbstractDao<Long, DocumentoLegal>
		implements IDocumentoLegalDao {

	/**
	 * Contrutor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public DocumentoLegalDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, DocumentoLegal.class);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IDocumentoLegalDao#findByMsa(com.ciandt
	 * .pms.model.Msa)
	 */
	@SuppressWarnings("unchecked")
	public List<DocumentoLegal> findByMsa(final Msa msa) {
		List<DocumentoLegal> listResult = getJpaTemplate()
				.findByNamedQuery(DocumentoLegal.FIND_BY_MSA,
						new Object[] { msa.getCodigoMsa() });

		return listResult;
	}

	/**
	 * Busca a maior sequencie por msa, para geracao do codigo do legal doc.
	 * 
	 * @param msa
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer findMaxSequencieByMsa(final Msa msa) {
		List<Integer> listResult = getJpaTemplate().findByNamedQuery(
				DocumentoLegal.FIND_MAX_SEQUENCIE_BY_MSA,
				new Object[] { msa.getCodigoMsa() });

		try {
			return Integer.parseInt(listResult.get(0).toString());
		} catch (NullPointerException e) {
			return Integer.valueOf(0);
		}
	}

	/**
	 * Retorna todos {@link DocumentoLegal} ativos.
	 * 
	 * @return List<DocuemtnoLegal>
	 */
	@SuppressWarnings("unchecked")
	public List<DocumentoLegal> findAllActive() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("indicadorStatus", Constants.DOCUMENTO_LEGAL_STATUS_ACTIVE);

		List<DocumentoLegal> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(DocumentoLegal.FIND_ALL_ACTIVE,
						params);

		return results;
	}

	/**
	 * busca por ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DocumentoLegal> findByFichaReajuste(
			final FichaReajuste fichaReajuste) {
		List<DocumentoLegal> listResult = getJpaTemplate().findByNamedQuery(
				DocumentoLegal.FIND_BY_FICHA_REAJUSTE,
				new Object[] { fichaReajuste.getCodigoFichaReajuste() });

		return listResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IDocumentoLegalDao#findToSendEmail(java
	 * .util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<DocumentoLegal> findToSendEmail(final Date dataInicioLembrete,
			final Date dataUltimoEnvioEmail) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataInicioLembrete", dataInicioLembrete);
		params.put("dataUltimoEnvioEmail", dataUltimoEnvioEmail);

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				DocumentoLegal.FIND_TO_SEND_EMAIL, params);
	}
}
