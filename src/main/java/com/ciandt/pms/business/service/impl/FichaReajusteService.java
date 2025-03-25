package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IControleReajusteService;
import com.ciandt.pms.business.service.IDocumentoLegalService;
import com.ciandt.pms.business.service.IFichaReajusteService;
import com.ciandt.pms.business.service.IFichaReajusteStatusService;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteStatus;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IFichaReajusteDao;

/**
 * A classe FichaReajusteService proporciona as funcionalidades de serviço referente
 * a entidade FichaReajuste.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class FichaReajusteService implements IFichaReajusteService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IFichaReajusteDao dao;
	
	/** Instancia do Servico da entidade {@link DocumentoLegalService}. */
	@Autowired
	private IDocumentoLegalService documentoLegalService;

	/** Instancia do Servico da entidade {@link FichaReajusteStatus}. */
	@Autowired
	private IFichaReajusteStatusService fichaReajusteStatusService;
	
	/** Instancia do Servico da entidade {@link ControleReajusteService}. */
	@Autowired
	private IControleReajusteService controleReajusteService;

	/** Arquivo de configuracoes. */
	@Autowired
	protected Properties systemProperties;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public FichaReajuste findFichaReajusteById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<FichaReajuste> findFichaReajusteAll() {
		return dao.findAll();
	}

	/**
	 * Insere uma entidade.
	 *
	 * @param entity
	 */
	public void createFichaReajuste(final FichaReajuste entity) {
		entity.setFichaReajusteStatus(fichaReajusteStatusService
				.findFichaReajusteStatusBySiglaStatus(Constants.FICHA_REAJUSTE_STATUS_SG_ACTIVE));

		dao.create(entity);
	}
	
	/**
	 * Atualiza uma entidade.
	 *
	 * @param entity
	 */
	public boolean updateFichaReajuste(final FichaReajuste entity) {
		if (this.canUpdateFichaReajuste(entity)) {
			dao.update(entity);

			return true;
		}

		return false;
	}

	/**
	 * Verifica se é possível editar uma ficha uma ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	private Boolean canUpdateFichaReajuste(final FichaReajuste fichaReajuste) {

		if (!"IN".equals(fichaReajuste.getFichaReajusteStatus()
				.getSiglaFichaReajusteStatus())) {
			return Boolean.valueOf(true);
		}

		Boolean result = Boolean.valueOf(false);
		List<ControleReajuste> controlesReajuste = controleReajusteService
				.findByFichaReajuste(fichaReajuste);
		List<String> statusList = Arrays.asList(new String[] { "NE", "CA",
				"EX", "RE" });
		for (ControleReajuste controleReajuste : controlesReajuste) {
			if (statusList.contains(controleReajuste
					.getControleReajusteStatus().getSiglaControleReajStatus())) {
				result = Boolean.valueOf(true);
			}
		}
		return result;
	}

	/**
	 * Deleta uma entidade.
	 *
	 * @param entity
	 */
	public void deleteFichaReajuste(final FichaReajuste entity) {
		dao.remove(entity);
	}

    /**
     * Retorna uma {@link FichaReajuste} com seu nome igual a {@code nomeFichaReajuste}.
     *
     * @param nomeFichaReajuste
     * @return {@link FichaReajuste}
     */
	@Override
	public FichaReajuste findFichaReajusteByNomeFichaReajuste(
			String nomeFichaReajuste) {
		return dao.findByNomeFichaReajuste(nomeFichaReajuste);
	}

    /**
     * Retorna Fichas de Reajuste que estão em {@code documentosLegais}.
     *
     * @param documentosLegais
     * @return List<FichaReajuste>.
     */
	public List<FichaReajuste> findFichaReajusteByDocumentosLegais(
			List<DocumentoLegal> documentosLegais) {

		List<Long> idsFichaReajuste = new ArrayList<Long>();
		for (DocumentoLegal documentoLegal : documentosLegais) {
			idsFichaReajuste.add(documentoLegal.getFichaReajuste().getCodigoFichaReajuste());
		}

		return dao.findByDocumentosLegais(idsFichaReajuste);
	}

    /**
     * Retorna {@link FichaReajuste} que está em {@code documentoLegal}.
     *
     * @param documentoLegal
     * @return Entidade {@link FichaReajuste}.
     */
	public FichaReajuste findFichaReajusteByDocumentoLegal(
			DocumentoLegal documentoLegal) {
		List<Long> idFichaReajuste = new ArrayList<Long>();
		idFichaReajuste.add(documentoLegal.getFichaReajuste()
				.getCodigoFichaReajuste());

		List<FichaReajuste> fichaReajustes = dao
				.findByDocumentosLegais(idFichaReajuste);

		return fichaReajustes.get(0);
	}

    /**
     * Retorna Fichas de Reajuste com status igual a {@code status}.
     * 
     * @return list<FichaReajusteStatus>
     */
	@Override
    public List<FichaReajuste> findFichaReajusteByFichaReajusteStatus(
			FichaReajusteStatus status) {

    	return dao.findByFichaReajusteStatus(status);
    }

	/**
	 * Retorna todas as Fichas de Reajuste com status ativo.
	 *
	 * @return list<FichaReajusteStatus>
	 */
	@Override
	public List<FichaReajuste> findAllFichaReajusteActive() {
		FichaReajusteStatus fichaReajusteStatus = fichaReajusteStatusService
				.findFichaReajusteStatusBySiglaStatus(Constants.FICHA_REAJUSTE_STATUS_SG_ACTIVE);

		return dao.findByFichaReajusteStatus(fichaReajusteStatus);
	}

	/**
	 * Retorna Fichas de Reajuste onde seus DocumentosLegais estão relacionados a {@code msa}.
	 *
	 * @param msa
	 * @return List<FichaReajuste>
	 */
	public List<FichaReajuste> findAllFichaReajusteActiveByMsa(final Msa msa) {
		List<FichaReajuste> filteredFichasReajuste = new ArrayList<FichaReajuste>();

		List<FichaReajuste> fichaReajustes = this.findAllFichaReajusteActive();
		for (FichaReajuste fichaReajuste : fichaReajustes) {

			List<DocumentoLegal> documentosLegais = documentoLegalService
					.findByFichaReajuste(fichaReajuste);
			for (DocumentoLegal documentoLegal : documentosLegais) {
				if (documentoLegal.getMsa().getCodigoMsa().equals(msa.getCodigoMsa())) {
					filteredFichasReajuste.add(fichaReajuste);
				}
				break;
			}
		}

		return filteredFichasReajuste;
	}

	/**
	 * Indica se {@code fichaReajuste} precisa de um ou mais {@link ControleReajuste}.
	 *
	 * @param fichaReajuste
	 * @return Boolean
	 */
	@Override
	public Boolean fichaReajusteNeedsNewControleReajuste(final FichaReajuste fichaReajuste) {

		Date nextControleReajusteDate = controleReajusteService.getDataPrevistaOfNextControleReajuste(fichaReajuste);

		List<DocumentoLegal> documentosLegais = documentoLegalService
				.findByFichaReajuste(fichaReajuste);

		Date date = fichaReajuste.getFrequencyIntervalFromNow();
		Integer addition = Integer.valueOf(systemProperties.getProperty(Constants.DOCUMENTO_LEGAL_ADICAO_PERIODO));

		for (DocumentoLegal documentoLegal : documentosLegais) {
			if ((nextControleReajusteDate.before(date) || nextControleReajusteDate.equals(date))
					&& documentoLegal.dateIsBetweenVigenciaDocumentoLegalPlusAdition(nextControleReajusteDate, addition)) {

				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}
}