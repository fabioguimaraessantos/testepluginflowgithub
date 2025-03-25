package com.ciandt.pms.control.jsf;

import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IVwMegaCCustoService;
import com.ciandt.pms.control.jsf.bean.CostCenterBean;
import com.ciandt.pms.control.jsf.bean.GrupoCustoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.vo.FormFilter;

/**
 * A classe CostCenterController proporciona as funcionalidades da camada de
 * apresentacao referente a cost center.
 * 
 * @since Aug 10, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
@Controller
@RolesAllowed({ "BUS.COST_CENTER:ED", "BOF.COST_CENTER:ED" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CostCenterController {

	/** Objeto logger. */
	@SuppressWarnings("unused")
	private final Logger log = Logger.getLogger(CostCenterController.class
			.getName());

	/** Outcome tela pendencias da entidade. */
	private static final String OUTCOME_COST_CENTER_PENDING_SEARCH = "costcenter_pendingSearch";

	/** Outcome tela pendencias da entidade. */
	private static final String OUTCOME_COST_CENTER_EDIT = "costCenter_edit";

	/** Instancia do servico da entidade {@link VwMegaCCusto}. */
	@Autowired
	private IVwMegaCCustoService vwMegaCCustoService;

	/** Instancia do servico da entidade {@link GrupoCusto}. */
	@Autowired
	private IGrupoCustoService grupoCustoService;

	/** Instancia do bean de CostCenter. */
	@Autowired
	private CostCenterBean bean;

	/** Instancia do bean de {@link GrupoCusto}. */
	@Autowired
	private GrupoCustoBean grupoCustoBean;

	@Autowired
	private GrupoCustoController grupoCustoController;

	/**
	 * @return the bean
	 */
	public CostCenterBean getBean() {
		return bean;
	}

	/**
	 * the bean
	 * 
	 * @param bean
	 */
	public void setBean(CostCenterBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepara a tela de busca de pendentes.
	 * 
	 * @return retorna a pagina de busca.
	 */
	public String preparePendingSearch() {
		bean.reset();
		bean.setPendentes(vwMegaCCustoService.findAllPendentes());
		return OUTCOME_COST_CENTER_PENDING_SEARCH;
	}

	/**
	 * Efetua a busca de acordo com os parametros da tela de busca.
	 */
	public void pendingSearch() {
		FormFilter filter = bean.getPendentesFormFilter();
		bean.setPendentes(vwMegaCCustoService.findPendentesByFormFilter(filter));
		if (bean.getPendentes().isEmpty())
			Messages.showWarning("pendingSearch",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
	}

	/**
	 * Ativa um Cost Center (Copia um centro de custo do sistema Mega para o
	 * PMS).
	 * 
	 * @return {@link GrupoCusto}
	 * @throws IntegrityConstraintException 
	 */
	public GrupoCusto activate() {
		VwMegaCCusto centroCustoMega = bean.getTo();
		GrupoCusto grupoCusto = null;
		try {
			grupoCusto = grupoCustoService.createCostCenter(centroCustoMega);
		} catch (ConstraintViolationException e) {
			log.warning(e.getMessage());
			Messages.showWarning("activate",
					Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
			return null;
		} catch (IntegrityConstraintException e) {
			log.warning(e.getMessage());
			Messages.showWarning("activate",
					Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
			return null;
		} catch (Exception e) {
			log.warning(e.getMessage());
			Messages.showWarning("activate",
					Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
			return null;
		} 
		bean.getPendentes().remove(centroCustoMega);
		return grupoCusto;
	}

	/**
	 * Ativa um Cost Center (Copia um centro de custo do sistema Mega para o
	 * PMS) e encaminha para a tela de complemento (e ativacao final).
	 */
	public String activateAndNext() {
		GrupoCusto grupoCusto = activate();
		if (grupoCusto == null) {
			return null;
		}
		grupoCustoController.prepareUpdate(grupoCusto);
		Messages.showSucess("activateAndNext",
				Constants.DEFAULT_MSG_SUCCESS_CREATE,
				Constants.ENTITY_NAME_GRUPO_CUSTO);
		return OUTCOME_COST_CENTER_EDIT;
	}

}