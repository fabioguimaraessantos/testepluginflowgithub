package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IControleReajusteService;
import com.ciandt.pms.business.service.IControleReajusteStatusService;
import com.ciandt.pms.business.service.IDocumentoLegalService;
import com.ciandt.pms.business.service.IFichaReajusteService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.control.jsf.bean.ControleReajusteBean;
import com.ciandt.pms.control.jsf.bean.DocumentoLegalBean;
import com.ciandt.pms.control.jsf.bean.DocumentoLegalConfigureBean;
import com.ciandt.pms.control.jsf.bean.MsaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.ControleReajusteStatus;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.vo.HistoricoControleReajusteRow;

/**
 * Define o Controller da entidade FichaReajuste.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 12/12/2013
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_CONTRACT", "ROLE_PMS_MANAGER",
		"ROLE_PMS_SR_MANAGER", "BUS.READJUSTMENT:VW", "BUS.READJUSTMENT:ED" })
public class ControleReajusteController {

	/** outcome tela de criação de ControleReajuste. */
	private static final String OUTCOME_CONTROLE_REAJUSTE_EDIT = "controle_reajuste_edit";

	/** outcome tela de criação de ControleReajuste. */
	private static final String OUTCOME_CONTROLE_REAJUSTE_VIEW = "controle_reajuste_view";

	/** outcome tela de documento legal. */
	private static final String OUTCOME_CONFIGURE_DOCUMENTO_LEGAL = "legal_doc_configure";

	/** Instancia do servico. */
	@Autowired
	private IControleReajusteService controleReajusteService;

	/** Instancia do servico. */
	@Autowired
	private IControleReajusteStatusService controleReajusteStatusService;

	/** Instancia do servico. */
	@Autowired
	private IDocumentoLegalService documentoLegalService;

	/** Instancia do servico. */
	@Autowired
	private IFichaReajusteService fichaReajusteService;

	/** Instancia do servico. */
	@Autowired
	private IMsaService msaService;

	/**
	 * Instancia do bean.
	 */
	@Autowired
	private ControleReajusteBean bean = null;

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
	public ControleReajusteBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final ControleReajusteBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepara a tela de visualização de {@link ControleReajuste}.
	 * 
	 * @return página de destino.
	 */
	public String prepareView() {
		bean.setTo(controleReajusteService.findControleReajusteById(bean
				.getChosenCodigoControleReajuste()));
		Hibernate.initialize(msaBean.getTo().getCliente());
		bean.setMsa(msaBean.getTo());

		return OUTCOME_CONTROLE_REAJUSTE_VIEW;
	}

	/**
	 * Prepara a tela de edição de {@link ControleReajuste}
	 * 
	 * @return página de destino.
	 */
	public String prepareUpdate() {
		ControleReajuste controleReajuste = controleReajusteService
				.findControleReajusteById(bean
						.getChosenCodigoControleReajuste());
		bean.setTo(controleReajuste);
		Hibernate.initialize(msaBean.getTo().getCliente());

		this.loadControleReajusteStatus(controleReajusteStatusService
				.findControleReajusteAllActive());
		bean.setMsa(msaBean.getTo());
		bean.setStatusSelected(controleReajuste.getControleReajusteStatus()
				.getNomeControleReajStatus());

		bean.getTo().setTextoObservacao("");

		return OUTCOME_CONTROLE_REAJUSTE_EDIT;
	}

	/**
	 * Atualiza as modificações realizadas na tela de edição de
	 * {@link ControleReajuste}
	 * 
	 * @return página de destino
	 */
	public String update() {
		ControleReajusteStatus status = controleReajusteStatusService
				.findControleReajusteStatusById(bean.getStatusMap().get(
						bean.getStatusSelected()));
		bean.getTo().setControleReajusteStatus(status);

		bean.getTo().setFichaReajuste(
				fichaReajusteService.findFichaReajusteById(bean.getTo()
						.getFichaReajuste().getCodigoFichaReajuste()));

		if (this.validFieldsForStatus()) {
			controleReajusteService.updateControleReajuste(bean.getTo());
			DocumentoLegal documentoLegal = documentoLegalService
					.findDocumentoLegalById(documentoLegalBean.getTo()
							.getCodigoDocumentoLegal());
			documentoLegalConfigureBean
					.setControlesReajuste(controleReajusteService
							.findControleReajusteByDocumentoLegal(documentoLegal));

			return OUTCOME_CONFIGURE_DOCUMENTO_LEGAL;
		} else {
			return OUTCOME_CONTROLE_REAJUSTE_EDIT;
		}
	}

	/**
	 * Verifica se todos os campos obrigatórios para um determinado status estão
	 * preenchidos.
	 * 
	 * @return {@link Boolean}
	 */
	private Boolean validFieldsForStatus() {
		ControleReajuste controleReajuste = bean.getTo();

		String siglaStatus = controleReajuste.getControleReajusteStatus()
				.getSiglaControleReajStatus();

		// Para os seguintes status
		if (Constants.CONTROLE_REAJUSTE_STATUS_SG_NOT_EXECUTED
				.equals(siglaStatus)
				|| Constants.CONTROLE_REAJUSTE_STATUS_SG_RESCHEDULED
						.equals(siglaStatus)
				|| Constants.CONTROLE_REAJUSTE_STATUS_SG_CANCELLED
						.equals(siglaStatus)
				|| Constants.CONTROLE_REAJUSTE_STATUS_SG_RENOVATION
						.equals(siglaStatus)) {

			// Apenas o campo "Obs" é exigido.
			if ("".equals(controleReajuste.getTextoObservacao())
					|| controleReajuste.getTextoObservacao() == null) {
				Messages.showError("requiredFields",
						Constants.BUNDLE_OBS_FIELD_REQUIRED);

				return false;
			}
		}

		// Para o seguinte status
		if (Constants.CONTROLE_REAJUSTE_STATUS_SG_EXECUTED.equals(siglaStatus)) {

			// Todos menos o campo "Obs" são exigidos.
			if (controleReajuste.getDataPrevista() == null
					|| controleReajuste.getDataReajuste() == null
					|| controleReajuste.getPercentualPrevista() == null
					|| controleReajuste.getPercentualRealizada() == null) {

				Messages.showError("requiredFields",
						Constants.BUNDLE_ALL_FIELDS_REQUIRED);

				return false;
			}
		}

		return true;
	}

	/**
	 * Popula listas para combo de status para telas que possuem
	 * {@link ControleReajusteStatus}.
	 * 
	 * @param statusAll
	 */
	public void loadControleReajusteStatus(
			List<ControleReajusteStatus> statusAll) {

		List<String> statusList = new ArrayList<String>();
		Map<String, Long> statusMap = new HashMap<String, Long>();

		for (ControleReajusteStatus status : statusAll) {
			statusList.add(status.getNomeControleReajStatus());
			statusMap.put(status.getNomeControleReajStatus(),
					status.getCodigoControleReajStatus());
		}

		bean.setStatus(statusList);
		bean.setStatusMap(statusMap);
	}

	/**
	 * prepare aba de historico de controle de reajuste.
	 */
	public void prepareHistory() {
		List<HistoricoControleReajusteRow> listResult = new ArrayList<HistoricoControleReajusteRow>();

		List<ControleReajusteAud> listaAud = controleReajusteService
				.findHistoryByCodigoControleReajuste(bean
						.getChosenCodigoControleReajuste());

		HistoricoControleReajusteRow histRow = null;
		for (ControleReajusteAud controleReajusteAud : listaAud) {
			histRow = new HistoricoControleReajusteRow(
					controleReajusteStatusService.findControleReajusteStatusById(controleReajusteAud
							.getCodigoControleReajStatus()),
					controleReajusteAud);

			listResult.add(histRow);
		}

		bean.setHistoryList(listResult);
	}
}