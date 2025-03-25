package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.ActiveProjectBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ConvergenciaRow;
import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.model.vo.combo.ComboBox;
import com.ciandt.pms.model.vo.combo.type.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.util.*;

/**
 * 
 * A classe ActiveProjectMegaController proporciona as funcionalidades de
 * ativacao de projeto com base nos dados do ERP.
 * 
 * @since 15/08/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BUS.VIEW_PROJECT_ASSOCIATIONS:VW", "BOF.PROJECT_ACTIVATION:VW" })
public class ActiveProjectController {

	/**********************************************/

	/*********** OUTCOMES **************************/

	/** outcome tela inclusao da entidade. */

	private static final String OUTCOME_ACTIVE_PROJECT = "projetoAtivar";

	/**********************************************/

	/********************************************************/

	/*********** SERVICES ***********************************/
	/** Instancia do servico da entidade Convergencia. */
	@Autowired
	private IConvergenciaService convergenciaService;
	/** Instancia do servico da entidade {@link Cliente}. */
	@Autowired
	private IClienteService clienteService;
	/** Instancia do servico da entidade {@link ContratoPratica}. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;
	/** Instancia do servico da entidade {@link VwMegaCCusto}. */
	@Autowired
	private IVwMegaCCustoService cCustoService;
	/** Instancia do servico da entidade {@link VwPMSProjeto}. */
	@Autowired
	private IVwPMSProjetoService vwPMSProjetoService;
	/** Instancia do servico da entidade {@link IEmpresaService}. */
	@Autowired
	private IEmpresaService empresaService;

	/********************************************************/

	/** Instancia do bean da entidade Convergencia. */
	@Autowired
	private ActiveProjectBean bean;

	/********************************************************/

	/**
	 * Prepara a tela de integracao a partir de um cost group
	 * 
	 * @param grupoCusto
	 * @return
	 */
	public String prepareActivate(GrupoCusto grupoCusto) {
		String outcome = prepareActivate();
		bean.getCostCenterCombo().setSelected(grupoCusto.getNomeGrupoCusto());
		search();
		return outcome;
	}

	/**
	 * Prepara a tela de integracao.
	 * 
	 * @return retorna uma String que representa a pagina de integracao
	 */
	public String prepareActivate() {
		bean.reset();
		// carregar combos
		List<Empresa> empresas = empresaService.findEmpresaAllParentCompany();
		ComboBox<Empresa> empresaCombo = ComboFactory.create(new EmpresaCombo(empresas));
		bean.setEmpresaCombo(empresaCombo);

		this.loadClienteList(clienteService.findClienteAllClientePaiForComboBox());

		bean.setStatusSelected("NA");
		bean.setIsUpdate(Boolean.TRUE);

		this.loadCostCenterList(cCustoService.findAllForCombobox());
		this.loadContratoPraticaList(contratoPraticaService.findAllCompleteForCombobox());

		return OUTCOME_ACTIVE_PROJECT;
	}

	/**
	 * Popula a lista de Cliente para combobox de clientes.
	 *
	 * @param clientes
	 *            lista de Cliente.
	 *
	 */
	private void loadClienteList(final List<Cliente> clientes) {

		Map<String, Long> clienteMap = new HashMap<String, Long>();
		List<String> clienteList = new ArrayList<String>();

		for (Cliente c : clientes) {
			clienteMap.put(c.getNomeCliente(), c.getCodigoCliente());
			clienteList.add(c.getNomeCliente());
		}

		bean.setClienteMap(clienteMap);
		bean.setClienteList(clienteList);
	}

	/**
	 * Popula a lista de VwMegaCCusto para combobox de costCenters.
	 *
	 * @param costCenters
	 *            lista de VwMegaCCusto.
	 *
	 */
	private void loadCostCenterList(final List<VwMegaCCusto> costCenters) {
		Collection<String> costCenterList = new ArrayList<>();

		for (VwMegaCCusto cc : costCenters) {
			costCenterList.add(cc.getDescricao());
		}

		bean.setCostCenterList(costCenterList);
	}

	/**
	 * Popula a lista de ContratoPratica para combobox de contratos praticas.
	 *
	 * @param contratosPratica
	 *            lista de ContratoPratica.
	 *
	 */
	private void loadContratoPraticaList(
			final List<ContratoPratica> contratosPratica) {

		Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
		List<String> contratoPraticaList = new ArrayList<String>();

		for (ContratoPratica cp : contratosPratica) {
			contratoPraticaMap.put(cp.getNomeContratoPratica(),
					cp.getCodigoContratoPratica());
			contratoPraticaList.add(cp.getNomeContratoPratica());
		}

		bean.setContratoPraticaMap(contratoPraticaMap);
		bean.setContratoPraticaList(contratoPraticaList);
	}

	/**
	 * Verifica se o valor do atributo ContratoPratica e valido. Se o valor na
	 * e nulo e existe no contratoPraticaMap, entao o valor e valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateContratoPratica(final FacesContext context,
										final UIComponent component, final Object value) {
		String strValue = (String) value;
		if (strValue != null && !strValue.isEmpty() && !bean.getContratoPraticaMap().containsKey(strValue)) {
			String label = (String) component.getAttributes().get("label");
			throw new ValidatorException(Messages.getMessageError(
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
		}
	}

	/**
	 * Verifica se o valor do atributo Cliente e valido. Se o valor na
	 * e nulo e existe no clienteMap, entao o valor e valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCliente(final FacesContext context, final UIComponent component, final Object value) {
		String strValue = (String) value;
		if (strValue != null && !strValue.isEmpty() && !bean.getClienteMap().containsKey(strValue)) {
			String label = (String) component.getAttributes().get("label");
			throw new ValidatorException(Messages.getMessageError(
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
		}
	}

	/**
	 * Verifica se o valor do atributo VwMegaCCusto e valido. Se o valor xiste no costCenterList,
	 * entao o valor e valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCentroCusto(final FacesContext context, final UIComponent component, final Object value) {
		String strValue = (String) value;
		if (strValue != null && !strValue.isEmpty() && !bean.getCostCenterList().contains(strValue)) {
			String label = (String) component.getAttributes().get("label");
			throw new ValidatorException(Messages.getMessageError(
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
		}
	}

	/**
	 * Prepara a tela de visualizao de projetos.
	 * 
	 * @return retorna uma String que representa a pagina de integracao
	 */
	public String prepareView() {
		bean.reset();
		// carregar combos
		List<Empresa> empresas = empresaService.findEmpresaAllParentCompany();
		ComboBox<Empresa> empresaCombo = ComboFactory.create(new EmpresaCombo(empresas));
		bean.setEmpresaCombo(empresaCombo);

		List<Cliente> clients = clienteService.findClienteAllClientePai();
		ComboBox<Cliente> clientCombo = ComboFactory.create(new ClienteCombo(
				clients));
		bean.setClienteCombo(clientCombo);
		
		bean.setStatusSelected(Constants.ACTIVATION_ASSOCIATE_SG);
		
		List<VwMegaCCusto> costCenters = cCustoService.findAll();
		ComboBox<VwMegaCCusto> costCenterCombo = ComboFactory
				.create(new CentroCustoCombo(costCenters));
		bean.setCostCenterCombo(costCenterCombo);
		
		List<ContratoPratica> cLobs = contratoPraticaService
				.findContratoPraticaAllForComboBox();
		ComboBox<ContratoPratica> contratoPraticaCombo = ComboFactory
				.create(new ContratoPraticaCombo(cLobs));
		bean.setContratoPraticaCombo(contratoPraticaCombo);
		
		return OUTCOME_ACTIVE_PROJECT;
	}

	/**
	 * @return the bean
	 */
	public ActiveProjectBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final ActiveProjectBean bean) {
		this.bean = bean;
	}

	/**
	 * Ativa o projeto do ERP (Copia o id do projeto do ERP para a tabela de
	 * convergencia).
	 * @throws MoreThanOneActiveEntityException 
	 */
	public void activate() throws MoreThanOneActiveEntityException {
		ConvergenciaRow row = bean.getTo();
		Convergencia to = row.getConvergencia();
		if (validate(to)) {
			VwPMSProjeto vwPmsProjeto = vwPMSProjetoService
					.findByCodigoProjeto(to.getCodigoProjetoMega());
			to.setCodigoCentroCustoMega(to.getGrupoCusto()
					.getErpCodigoCentroCusto());
			to.setCodigoPadraoProjeto(vwPmsProjeto.getCodigoPadraoProjeto());
			convergenciaService.update(to);
			// limpa o filtro caso seja o ultimo registro
			if (bean.getInactiveList().size() == 1) {
				bean.resetFilter(true);
			}
			search();
			Messages.showSucess("activeProjectTable",
					Constants.DEFAULT_MSG_SUCCESS_ACTIVATE, to.getGrupoCusto()
							.getNomeGrupoCusto());
		}
	}

	private boolean validate(Convergencia to) {
		boolean validate = true;

		if (validate) {
			Long codigoProjetoMega = to.getCodigoProjetoMega();
			if (codigoProjetoMega == null) {
				validate = false;
				Messages.showError("activeProjectTable",
						Constants.ACTIVATION_PROJECT_ERROR_REQUIRED_CLOB);
			} else {
				String projectName = getProjectName(codigoProjetoMega);
				if (projectName == null) {
					Messages.showError("activeProjectTable",
							Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
							new Object[] { codigoProjetoMega });
					validate = false;
				}
			}
			if (validate) {
				// verifica se o mesmo codigo de projeto utilizado esta sendo
				// usado para a mesma empresa
				List<Convergencia> list = convergenciaService
						.findConvergenciaByProjeto(codigoProjetoMega);
				if (!list.isEmpty()) {
					for (Convergencia cv : list) {
						Empresa empresa = cv.getGrupoCusto().getEmpresa();
						Long cdEmpresaSalva = empresa.getCodigoEmpresa();
						Long cdEmpresaAtual = bean.getTo().getConvergencia()
								.getGrupoCusto().getEmpresa()
								.getCodigoEmpresa();
						if (cdEmpresaAtual.equals(cdEmpresaSalva)) {
							Messages.showError(
									"activeProjectTable",
									Constants.ACTIVATION_PROJECT_ERROR_ALREADY_USED,
									new Object[] { codigoProjetoMega,
											empresa.getNomeEmpresa() });
							validate = false;
							break;
						}
					}
				}
			}
		}
		return validate;
	}

	/**
	 * Retorna nome do projeto no mega
	 *
	 * @param codigoProjetoMega
	 * @return nome do projeto no mega ou retorna null caso nao seja encontrado
	 */
	public String getProjectName(Long codigoProjetoMega) {
		VwPMSProjeto megaProjeto = vwPMSProjetoService.findByCodigoProjeto(codigoProjetoMega);
		if (megaProjeto != null) {
			return megaProjeto.getDescricao();
		}
		return null;
	}

	public void search() {
		FormFilter filter = bean.getSearchFilter();
		// carrega a lista de projetos para serem ativados
		List<Convergencia> inactiveList = this
				.findByFilterProjectStatus(filter);

		if (inactiveList.size() == 0 && !bean.isUpdate()) {
			Messages.showWarning("activeProjectTable",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
		List<ConvergenciaRow> list = new ArrayList<ConvergenciaRow>();
		for (Convergencia convergencia : inactiveList) {
			list.add(new ConvergenciaRow(convergencia, this));
		}
		bean.setInactiveList(list);
	}

	/**
	 * 
	 * @param filter
	 * @return
	 */
	private List<Convergencia> findByFilterProjectStatus(final FormFilter filter) {

		List<Convergencia> resultList = new ArrayList<Convergencia>();
		if (Constants.ACTIVATION_ASSOCIATE_SG.equals(bean.getStatusSelected())) {
			resultList = convergenciaService
					.findActiveProjectByFormFilter(filter);
		} else if (Constants.ACTIVATION_NOT_ASSOCIATE_SG.equals(bean
				.getStatusSelected())) {
			resultList = convergenciaService
					.findInativeProjectByFormFilter(filter);
		} else {
			resultList = convergenciaService.findAllProjectByFormFilter(filter);
		}

		return resultList;
	}

}