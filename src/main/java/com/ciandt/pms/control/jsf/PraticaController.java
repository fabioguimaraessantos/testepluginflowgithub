package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPraticaService;
import com.ciandt.pms.business.service.ITipoPraticaService;
import com.ciandt.pms.control.jsf.bean.PraticaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.TipoPratica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define o Controller da entidade.
 *
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * @since 27/09/2009
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.LOB:VW", "BOF.LOB:CR", "BOF.LOB:ED", "BOF.LOB:DE" })
public class PraticaController {

	/** outcome tela criacao da entidade. */
	private static final String OUTCOME_PRATICA_ADD = "pratica_add";

	/** outcome tela alteracao da entidade. */
	private static final String OUTCOME_PRATICA_EDIT = "pratica_edit";

	/** outcome tela remocao da entidade. */
	private static final String OUTCOME_PRATICA_DELETE = "pratica_delete";

	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_PRATICA_RESEARCH = "pratica_research";

	/**
	 * Instancia de Servicos.
	 */
	@Autowired
	private IPraticaService praticaService;

	/**
	 * Instancia de tipoPraticaService.
	 */
	@Autowired
	private ITipoPraticaService tipoPraticaService;

	/**
	 * Instancia do bean.
	 */
	@Autowired
	private PraticaBean bean = null;

	/**
	 * @return the bean
	 */
	public PraticaBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final PraticaBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepara a tela de insercao da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareCreate() {
		bean.reset();
		bean.setIsUpdate(Boolean.FALSE);
		this.loadLobTypeList(tipoPraticaService.findAll());
		return OUTCOME_PRATICA_ADD;
	}

	/**
	 * Load lobType list and lobType Map for combobox.
	 *
	 * @param lobList
	 */
	private void loadLobTypeList(final List<TipoPratica> lobList) {
		List<String> resultList = new ArrayList<String>();
		Map<String, Long> resultMap = new HashMap<String, Long>();
		for (TipoPratica lobType : lobList) {
			resultList.add(lobType.getNomeTipoPratica());
			resultMap.put(lobType.getNomeTipoPratica(),
					lobType.getCodigoTipoPratica());
		}
		bean.setTypeLobList(resultList);
		bean.setTypeLobMap(resultMap);

	}

	/**
	 * Insere uma entidade.
	 *
	 */
	public void create() {
		bean.getTo().setIndicadorAtivo(Constants.ACTIVE);

		// lob type
		TipoPratica lobType = tipoPraticaService.findById(bean.getTypeLobMap()
				.get(bean.getTo().getTipoPratica().getNomeTipoPratica()));
		bean.getTo().setTipoPratica(lobType);

		praticaService.createPratica(bean.getTo());
		Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
				Constants.ENTITY_NAME_PRATICA);
		bean.resetTo();
	}

	/**
	 * Prepara a tela de pesquisa da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareResearch() {
		bean.reset();
		this.loadLobTypeList(tipoPraticaService.findAll());
		bean.setSuggestionsListInAtivo(Constants.ACTIVE_INACTIVE_VALUES);
		return OUTCOME_PRATICA_RESEARCH;
	}

	/**
	 * Prepara a tela de edicao da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareUpdate() {
		findById(bean.getCurrentRowId());
		this.loadLobTypeList(tipoPraticaService.findAll());
		bean.setIsUpdate(Boolean.TRUE);
		return OUTCOME_PRATICA_EDIT;
	}

	/**
	 * Executa um update da entidade.
	 *
	 * @return pagina de destino
	 */
	public String update() {
		// lob type
		TipoPratica lobType = tipoPraticaService.findById(bean.getTypeLobMap()
				.get(bean.getTo().getTipoPratica().getNomeTipoPratica()));
		bean.getTo().setTipoPratica(lobType);

		try {
			praticaService.updatePratica(bean.getTo());
		} catch (IntegrityConstraintException | MoreThanOneActiveEntityException ice) {
			Messages.showError("update", ice.getMessage(), new Object[] {
					Constants.ENTITY_NAME_PRATICA,
					Constants.ENTITY_NAME_CONTRATO_PRATICA });
			return null;
		}

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
				Constants.ENTITY_NAME_PRATICA);
		bean.resetTo();
		findByFilter();
		return OUTCOME_PRATICA_RESEARCH;
	}

	/**
	 * Prepara a tela de remocao da entidade.
	 *
	 * @return pagina de destino
	 */
	public String prepareRemove() {
		findById(bean.getCurrentRowId());
		return OUTCOME_PRATICA_DELETE;
	}

	/**
	 * Deleta uma entidade.
	 *
	 * @return pagina de destino
	 *
	 */
	public String remove() {
		try {
			praticaService.removePratica(praticaService.findPraticaById(bean
					.getTo().getCodigoPratica()));
		} catch (IntegrityConstraintException ice) {
			Messages.showError("remove", ice.getMessage(), new Object[] {
					Constants.ENTITY_NAME_PRATICA,
					Constants.ENTITY_NAME_CONTRATO_PRATICA });
			return null;
		}

		Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
				Constants.ENTITY_NAME_PRATICA);
		bean.resetTo();
		findByFilter();
		return OUTCOME_PRATICA_RESEARCH;
	}

	/**
	 * Busca uma entidade pelo id.
	 *
	 * @param id
	 *            da entidade.
	 *
	 */
	public void findById(final Long id) {
		Pratica pratica = praticaService.findPraticaById(id);
		bean.setTo(pratica);
		if (bean.getTo() == null || bean.getTo().getCodigoPratica() == null) {
			Messages.showWarning("findById",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 *
	 */
	public void findByFilter() {

		// lob type
		TipoPratica lobType = bean.getFilter().getTipoPratica();
		lobType.setCodigoTipoPratica(bean.getTypeLobMap().get(
				lobType.getNomeTipoPratica()));

		bean.getFilter().setTipoPratica(lobType);
		bean.setResultList(praticaService.findPraticaByFilter(bean.getFilter()));

		if (bean.getResultList().size() == 0) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		// volta para a primeira pagina da paginacao
		bean.setCurrentPageId(0);
	}

	/**
	 * Verifica se o tipoPratica � v�lido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateTipoPratica(final FacesContext context,
			final UIComponent component, final Object value) {

		Long tipoPraticaId = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			String label = (String) component.getAttributes().get("label");

			tipoPraticaId = bean.getTypeLobMap().get(strValue);

			if (tipoPraticaId == null) {
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			} else {
				if (tipoPraticaService.findById(tipoPraticaId) == null) {
					throw new ValidatorException(
							Messages.getMessageError(Constants.DEFAULT_MSG_ERROR_NOT_FOUND));
				}
			}
		}
	}

}
