package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IControleReajusteService;
import com.ciandt.pms.business.service.IDocumentoLegalService;
import com.ciandt.pms.business.service.IFichaReajusteIndiceService;
import com.ciandt.pms.business.service.IFichaReajusteService;
import com.ciandt.pms.business.service.IFichaReajusteStatusService;
import com.ciandt.pms.control.jsf.bean.DocumentoLegalBean;
import com.ciandt.pms.control.jsf.bean.DocumentoLegalConfigureBean;
import com.ciandt.pms.control.jsf.bean.FichaReajusteBean;
import com.ciandt.pms.control.jsf.bean.MsaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteIndice;
import com.ciandt.pms.model.FichaReajusteStatus;

/**
 * Define o Controller da entidade FichaReajuste.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 12/12/2013
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_CONTRACT" })
public class FichaReajusteController {

	/** outcome tela de criação de FichaReajuste. */
	private static final String OUTCOME_FICHA_REAJUSTE_ADD = "ficha_reajuste_add";

	/** outcome tela de criação de FichaReajuste. */
	private static final String OUTCOME_FICHA_REAJUSTE_EDIT = "ficha_reajuste_edit";

	/** outcome tela de criação de FichaReajuste. */
	private static final String OUTCOME_FICHA_REAJUSTE_VIEW = "ficha_reajuste_view";

	/** outcome tela de documento legal. */
	private static final String OUTCOME_CONFIGURE_DOCUMENTO_LEGAL = "legal_doc_configure";

	/** Instancia do servico. */
	@Autowired
	private IFichaReajusteService fichaReajusteService;

	/** Instancia do servico. */
	@Autowired
	private IFichaReajusteIndiceService fichaReajusteIndiceService;

	/** Instancia do servico. */
	@Autowired
	private IDocumentoLegalService documentoLegalService;

	/** Instancia do servico. */
	@Autowired
	private IFichaReajusteStatusService fichaReajusteStatusService;

	/** Instancia do servico. */
	@Autowired
	private IControleReajusteService controleReajusteService;

	/**
	 * Instancia do bean.
	 */
	@Autowired
	private FichaReajusteBean bean = null;

	/** Instancia do msaBbean. */
	@Autowired
	private MsaBean msaBean = null;

	/** Instancia do DocumentoLegalConfigureBean. */
	@Autowired
	private DocumentoLegalConfigureBean documentoLegalConfigureBean = null;

	/** Instancia do DocumentoLegalBean. */
	@Autowired
	private DocumentoLegalBean documentoLegalBean = null;

	/**
	 * @return the bean
	 */
	public FichaReajusteBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final FichaReajusteBean bean) {
		this.bean = bean;
	}

	/**
	 * Atribui uma {@link FichaReajuste} a um {@link DocumentoLegal}.
	 * 
	 * @return página de destino.
	 */
	public String assignOrCreateFichaReajuste() {
		if (Constants.FICHA_REAJUSTE_NEW.equals(documentoLegalConfigureBean
				.getFichaSelected())) {

			// TODO verificar o que pode ser resetado
			bean.setInactiveFichaReajusteId(null);

			return this.prepareCreate();
		} else {
			DocumentoLegal documentoLegal = documentoLegalService
					.findDocumentoLegalById(documentoLegalBean.getTo()
							.getCodigoDocumentoLegal());
			FichaReajuste fichaReajuste = fichaReajusteService
					.findFichaReajusteByNomeFichaReajuste(documentoLegalConfigureBean
							.getFichaSelected());
			documentoLegal.setFichaReajuste(fichaReajuste);

			documentoLegalService.updateDocumentoLegal(documentoLegal);
			controleReajusteService.createAllControleReajusteForFichaReajuste(documentoLegal.getFichaReajuste());

			List<ControleReajuste> controlesReajuste = controleReajusteService
					.findControleReajusteByDocumentoLegal(documentoLegal);

			documentoLegalConfigureBean.setFichaReajuste(fichaReajuste);
			documentoLegalConfigureBean.setControlesReajuste(controlesReajuste);
			documentoLegalBean.getTo().setFichaReajuste(fichaReajuste);

			return OUTCOME_CONFIGURE_DOCUMENTO_LEGAL;
		}
	}

	/**
	 * Prepara a tela de de criação da entidade.
	 * 
	 * @return página de destino.
	 */
	public String prepareCreate() {
		bean.resetTo();
		this.loadFichaReajusteIndices(fichaReajusteIndiceService
				.findFichaReajusteIndiceAll());
		bean.setMsa(msaBean.getTo());
		bean.setIsUpdate(false);

		return OUTCOME_FICHA_REAJUSTE_ADD;
	}

	/**
	 * Ação que cria uma Ficha de Reajuste.
	 * 
	 * @return returna página de configuração de DocumentoLegal.
	 */
	public String create() {
		bean.getTo().setNumeroPeriodicidade(
				Integer.valueOf(bean.getFrequency()));
		bean.getTo().setFichaReajusteIndice(
				fichaReajusteIndiceService.findFichaReajusteIndiceByNome(bean
						.getIndiceSelected()));

		fichaReajusteService.createFichaReajuste(bean.getTo());

		DocumentoLegal documentoLegal = documentoLegalService
				.findDocumentoLegalById(documentoLegalBean.getTo()
						.getCodigoDocumentoLegal());

		FichaReajuste fichaReajuste = fichaReajusteService
				.findFichaReajusteById(bean.getTo().getCodigoFichaReajuste());

		documentoLegal.setFichaReajuste(fichaReajuste);
		documentoLegalService.updateDocumentoLegal(documentoLegal);

		controleReajusteService
				.createAllControleReajusteForFichaReajuste(fichaReajuste);

		List<ControleReajuste> controlesReajuste = controleReajusteService
				.findControleReajusteByDocumentoLegal(documentoLegal);

		documentoLegalConfigureBean.setFichaReajuste(fichaReajuste);
		documentoLegalConfigureBean.setControlesReajuste(controlesReajuste);

		return OUTCOME_CONFIGURE_DOCUMENTO_LEGAL;
	}

	/**
	 * Cria uma {@link FichaReajuste} a partir do form e atualiza os Documentos
	 * Legais da {@link FichaReajuste} antiga com a nova.
	 * 
	 * @return página de destino.
	 */
	public String createAndUpdateInactiveFichaReajuste() {
		bean.getTo().setNumeroPeriodicidade(
				Integer.valueOf(bean.getFrequency()));
		bean.getTo().setFichaReajusteIndice(
				fichaReajusteIndiceService.findFichaReajusteIndiceByNome(bean
						.getIndiceSelected()));

		fichaReajusteService.createFichaReajuste(bean.getTo());

		FichaReajuste fichaReajusteInativa = fichaReajusteService
				.findFichaReajusteById(bean.getInactiveFichaReajusteId());

		List<DocumentoLegal> documentosLegais = documentoLegalService
				.findByFichaReajuste(fichaReajusteInativa);

		FichaReajuste fichaReajuste = fichaReajusteService
				.findFichaReajusteById(bean.getTo().getCodigoFichaReajuste());

		for (DocumentoLegal documentoLegal : documentosLegais) {
			documentoLegal.setFichaReajuste(fichaReajuste);

			documentoLegalService.updateDocumentoLegal(documentoLegal);
		}

		controleReajusteService
				.createAllControleReajusteForFichaReajuste(fichaReajuste);

		List<ControleReajuste> controlesReajuste = controleReajusteService
				.findByFichaReajuste(fichaReajuste);

		documentoLegalConfigureBean.setFichaReajuste(fichaReajuste);
		documentoLegalConfigureBean.setControlesReajuste(controlesReajuste);

		return OUTCOME_CONFIGURE_DOCUMENTO_LEGAL;
	}

	/**
	 * Prepara a tela de visualização de {@link FichaReajuste}.
	 * 
	 * @return página de destino.
	 */
	public String prepareView() {
		FichaReajuste fichaReajuste = fichaReajusteService
				.findFichaReajusteById(documentoLegalConfigureBean
						.getFichaReajuste().getCodigoFichaReajuste());

		bean.setTo(fichaReajuste);

		return OUTCOME_FICHA_REAJUSTE_VIEW;
	}

	/**
	 * Prepara a tela de edição de {@link FichaReajuste}.
	 * 
	 * @return página de destino.
	 */
	public String prepareEdit() {
		bean.setTo(fichaReajusteService
				.findFichaReajusteById(documentoLegalConfigureBean
						.getFichaReajuste().getCodigoFichaReajuste()));

		this.loadFichaReajusteIndices(fichaReajusteIndiceService
				.findFichaReajusteIndiceAll());
		this.loadFichaReajusteStatus(fichaReajusteStatusService
				.findAllFichaReajusteStatusActive());

		bean.setIndiceSelected(bean.getTo().getFichaReajusteIndice()
				.getNomeFichaReajusteIndice());
		bean.setStatusSelected(bean.getTo().getFichaReajusteStatus()
				.getNomeFichaReajustesStatus());

		bean.setFrequency(String.valueOf(bean.getTo().getNumeroPeriodicidade()));
		bean.setMsa(msaBean.getTo());
		bean.setIsUpdate(false);

		return OUTCOME_FICHA_REAJUSTE_EDIT;
	}

	/**
	 * Atualiza uma {@link FichaReajuste}.
	 * 
	 * @return página de destino.
	 */
	public String update() {
		bean.getTo().setFichaReajusteStatus(
				fichaReajusteStatusService.findFichaReajusteStatusById(bean
						.getStatusMap().get(bean.getStatusSelected())));

		if (fichaReajusteService.updateFichaReajuste(bean.getTo())) {
			Messages.showSucess("update", Constants.GENEREC_MSG_SUCCESS_UPDATE);

			if (Constants.FICHA_REAJUSTE_STATUS_SG_ACTIVE.equals(bean.getTo()
					.getFichaReajusteStatus().getSiglaFichaReajusteStatus())) {

				return OUTCOME_CONFIGURE_DOCUMENTO_LEGAL;
			} else {
				bean.setInactiveFichaReajusteId(bean.getTo()
						.getCodigoFichaReajuste());

				return OUTCOME_CONFIGURE_DOCUMENTO_LEGAL;
			}
		}

		Messages.showError("update", Constants.FICHA_REAJUSTE_MSG_INATIVO);

		return OUTCOME_FICHA_REAJUSTE_EDIT;
	}

	/**
	 * Popula listas para combo de indice para telas de {@link FichaReajuste}.
	 * 
	 * @param indices
	 */
	public void loadFichaReajusteIndices(List<FichaReajusteIndice> indices) {

		List<String> nomeFichaReajusteIndices = new ArrayList<String>();
		Map<String, Long> indicesMap = new HashMap<String, Long>();

		for (FichaReajusteIndice indice : indices) {
			nomeFichaReajusteIndices.add(indice.getNomeFichaReajusteIndice());
			indicesMap.put(indice.getNomeFichaReajusteIndice(),
					indice.getCodigoFichaReajusteIndice());
		}

		bean.setIndices(nomeFichaReajusteIndices);
		bean.setIndicesMap(indicesMap);
	}

	/**
	 * Popula listas para combo de status para telas de {@link FichaReajuste}.
	 * 
	 * @param statusAll
	 */
	public void loadFichaReajusteStatus(List<FichaReajusteStatus> statusAll) {

		List<String> statusList = new ArrayList<String>();
		Map<String, Long> statusMap = new HashMap<String, Long>();

		for (FichaReajusteStatus status : statusAll) {
			statusList.add(status.getNomeFichaReajustesStatus());
			statusMap.put(status.getNomeFichaReajustesStatus(),
					status.getCodigoFichaReajusteStatus());
		}

		bean.setStatus(statusList);
		bean.setStatusMap(statusMap);
	}
}