package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import com.ciandt.pms.control.jsf.bean.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IContratoPraticaOrcDespClService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IFollowOrcamentoDespService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IOrcDespDelegacaoService;
import com.ciandt.pms.business.service.IOrcDespWhiteListService;
import com.ciandt.pms.business.service.IOrcDespesaClService;
import com.ciandt.pms.business.service.IOrcDespesaMesService;
import com.ciandt.pms.business.service.IOrcamentoDespesaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.business.service.IVoucherMail;
import com.ciandt.pms.business.service.IVwPmsCotacaoMoedaService;
import com.ciandt.pms.business.service.IVwPmsCustoEmbutidoPvService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaOrcDespCl;
import com.ciandt.pms.model.ContratoPraticaOrcDespClId;
import com.ciandt.pms.model.FollowOrcamentoDesp;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.OrcDespDelegacao;
import com.ciandt.pms.model.OrcDespDelegacaoId;
import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;

/**
 * A classe OrcDespesaClController proporciona as funcionalidades de Controller
 * para a entidade OrcDespesaCl.
 * 
 * @since 11/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Controller
@RolesAllowed({ "BUS.CLIENT.TRAVEL_BUDGET:ED", "BUS.CLIENT.TRAVEL_BUDGET.EXTRA:ED",
		"BUS.CLIENT.TRAVEL_BUDGET:VW", "BUS.CLIENT.TRAVEL_BUDGET.EXTRA:VW"})
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespesaClController {

	/** Outcome tela de Manage. */
	private static final String OUTCOME_ORC_DESPESA_CL_MANAGE = "orc_despesa_cl_manage";

	/** Outcome tela de View. */
	private static final String OUTCOME_ORC_DESPESA_CL_VIEW = "orc_despesa_cl_view";

	/** Outcome tela de White List. */
	private static final String OUTCOME_ALLOW_LIST = "orc_despesa_cl_allow_list";

	/** Outcome tela de delegacao de travel budget. */
	private static final String OUTCOME_DELEGATION = "orc_despesa_cl_delegation";

	/** Outcome tela de clone de travel budget. */
	private static final String OUTCOME_CLONE = "orc_despesa_clone";

	/** Instancia do bean do {@link OrcDespesaCl}. */
	@Autowired
	private OrcDespesaClBean bean;

	/** Instancia do bean do {@link Cliente} */
	@Autowired
	private ClienteBean clienteBean;

	/** Instancia do bean do {@link OrcDespWhiteList}. */
	@Autowired
	private OrcDespWhiteListBean whiteListBean;

	/** Instancia do bean de delegation. */
	@Autowired
	private OrcDespesaDelegationBean orcDespesaDelegationBean;

	/** Servico de {@link OrcDespesaCl}. */
	@Autowired
	private IOrcDespesaClService orcDespesaClService;

	/** Servico de {@link Cliente}. */
	@Autowired
	private IClienteService clienteService;

	/** Servico de {@link ContratoPratica}. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Servico de {@link FollowOrcamentoDesp}. */
	@Autowired
	private IFollowOrcamentoDespService followOrcDespService;

	/** Servico de {@link Moeda}. */
	@Autowired
	private IMoedaService moedaService;

	/** Servico de {@link ContratoPraticaOrcDespCl}. */
	@Autowired
	private IContratoPraticaOrcDespClService contratoPraticaOrcDespClService;

	/** Servico de {@link OrcamentoDespesa}. */
	@Autowired
	private IOrcamentoDespesaService orcamentoDespesaService;

	/** Servico de {@link OrcDespWhiteList}. */
	@Autowired
	private IOrcDespWhiteListService whiteListService;

	/** Servico de {@link Recurso}. */
	@Autowired
	private IRecursoService recursoService;

	/** Servico de {@link Pessoa}. */
	@Autowired
	private IPessoaService pessoaService;

	/** Servico de {@link OrcDespDelegacao}. */
	@Autowired
	private IOrcDespDelegacaoService orcDespDelegacaoService;

	/** Servico de voucherMail. */
	@Autowired
	private IVoucherMail voucherMail;

	/** Servico de OrcDespesaMes. */
	@Autowired
	private IOrcDespesaMesService orcDespesaMesService;

	/** Servico de VwPmsCotacaoMoeda. */
	@Autowired
	private IVwPmsCotacaoMoedaService vwPmsCotacaoMoedaService;

	/** Servico de vwPmsCustoEmbutidoPv. */
	@Autowired
	private IVwPmsCustoEmbutidoPvService vwPmsCustoEmbutidoPvService;



	/**
	 * Obtem o bean da entidade {@link OrcDespesaCl}.
	 * 
	 * @return {@link OrcDespesaClBean}.
	 */
	public OrcDespesaClBean getBean() {
		return bean;
	}

	/**
	 * @param
	 *            bean to set
	 */
	public void setBean(final OrcDespesaClBean bean) {
		this.bean = bean;
	}

	/**
	 * Obtem o bean da entidade {@link Cliente}.
	 * 
	 * @return {@link ClienteBean}.
	 */
	public ClienteBean getClienteBean() {
		return clienteBean;
	}

	/**
	 * @param
	 *            clienteBean to set
	 */
	public void setClienteBean(final ClienteBean clienteBean) {
		this.clienteBean = clienteBean;
	}

	/**
	 * Obtem o bean da entidade {@link OrcDespWhiteList}.
	 * 
	 * @return {@link OrcDespWhiteList}.
	 */
	public OrcDespWhiteListBean getWhiteListBean() {
		return whiteListBean;
	}

	/**
	 * @param
	 *            whiteListBean to set
	 */
	public void setWhiteListBean(final OrcDespWhiteListBean whiteListBean) {
		this.whiteListBean = whiteListBean;
	}

	/**
	 * Obtem o {@link Cliente} corrente selecionado na view.
	 * 
	 * @return the {@link Cliente}
	 * 
	 */
	private Cliente getCurrentClienteSelected() {
		Long codigoCliente = clienteBean.getCurrentRowId();
		return clienteService.findClienteById(codigoCliente);
	}

	/**
	 * Obtem o {@link OrcDespesaCl} selecionado.
	 * 
	 * @return the {@link OrcDespesaCl}
	 * 
	 */
	private OrcDespesaCl getCurrentOrcDespesaClSelected() {
		return orcDespesaClService.findOrcDespesaClById(bean.getTo()
				.getCodigoOrcaDespCl());
	}

	/**
	 * @return the orcDespesaDelegationBean
	 */
	public OrcDespesaDelegationBean getOrcDespesaDelegationBean() {
		return orcDespesaDelegationBean;
	}

	/**
	 * @param orcDespesaDelegationBean
	 *            the orcDespesaDelegationBean to set
	 */
	public void setOrcDespesaDelegationBean(
			OrcDespesaDelegationBean orcDespesaDelegationBean) {
		this.orcDespesaDelegationBean = orcDespesaDelegationBean;
	}

	/**
	 * Converte uma lista de {@link ContratoPratica} para uma lista de
	 * {@link SelectItem} a ser exibida no componente de PickList.
	 * 
	 * @param contratoPraticaList
	 *            a lista de {@link ContratoPratica} para ser convertida
	 * 
	 * @return lista de {@link SelectItem}
	 */
	private List<SelectItem> loadClobPickListItems(
			final List<ContratoPratica> contratoPraticaList) {
		List<SelectItem> selectItemList = new ArrayList<SelectItem>();

		for (ContratoPratica cp : contratoPraticaList) {
			String reembolsavel = cp.getIndicadorReembolsavel() != null ? " (" + (cp.getIndicadorReembolsavel().equalsIgnoreCase("Y") ? Constants.ORCAMENTO_DESPESA_REEMBOLSAVEL : Constants.ORCAMENTO_DESPESA_NAO_REEMBOLSAVEL) + ")" : "";
			selectItemList.add(new SelectItem(""
					+ cp.getCodigoContratoPratica(), cp
					.getNomeContratoPratica().concat(reembolsavel)));

		}

		return selectItemList;
	}

	public String backToOrcDespesaCl() {

		this.prepareOrcDespesaCl();

		return OUTCOME_ORC_DESPESA_CL_MANAGE;
	}

	public String prepareOrcDespesaCl() {
		bean.reset();
		bean.resetFlagFilterTravelBudgetList();
		bean.setIsITSupport(validateITSupport());
		return prepareOrcDespesaClManage();
	}

	/**
	 * Prepare para a tela de manage do OrcDespesaCl.
	 * 
	 * @return
	 * 
	 */
	public String prepareOrcDespesaClManage() {

		Cliente cliente = this.getCurrentClienteSelected();
		clienteBean.setTo(cliente);

		// obtem os clob de um determinado cliente
		List<ContratoPratica> clobs = contratoPraticaService
				.findContratoPraticaByCliente(cliente);

		// carrega o pick list para a tela de OrcDespesaCl
		bean.setContratoPraticaPickList(this.loadClobPickListItems(clobs));

		// carrega a lista de moedas com todas as moedas
		// antes buscava as moedas dos clobs
		this.loadMoedaList(moedaService.findMoedaAll());

		// seta o Map de followers
		bean.setMapFollow(followOrcDespService.loadMapFollow(LoginUtil
				.getLoggedUser()));

		// carrega a lista de tipos de fatura
		this.loadTiposFaturaList();

		// carrega a lista contendo os itens OrcDespesaCl
		this.filterTravelBudgetList();

		// carrega as mensagens do tipo de faturamento
		this.prepareInvoiceTypeCombobox();

		return OUTCOME_ORC_DESPESA_CL_MANAGE;
	}

	/**
	 * Carrega lista de moeda para combobox e seta o map.
	 * 
	 * @param moedas
	 *            lista de moeda
	 * 
	 */
	private void loadMoedaList(final List<Moeda> moedas) {
		Map<String, Long> mapMoeda = new HashMap<String, Long>();
		List<String> listResult = new ArrayList<String>();

		for (Moeda moeda : moedas) {
			mapMoeda.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
			listResult.add(moeda.getNomeMoeda());
		}

		bean.setListaMoedaCombobox(listResult);
		bean.setMapMoeda(mapMoeda);
	}

	/**
	 * Obtem a {@link Moeda} selecionada na tela.
	 * 
	 * @return {@link Moeda} selecionada
	 * 
	 */
	private Moeda getMoedaSelecionada() {
		Long codigoMoeda = bean.getMapMoeda().get(bean.getMoedaSelecionda());
		return moedaService.findMoedaById(codigoMoeda);
	}

	/**
	 * Valida o input de datas inseridas na tela.
	 * 
	 * @return retorna true se o per�odo for v�lido. Se a data corrente � menor
	 *         que a data inicial ou se a data inicial � maior que a data final,
	 *         o per�odo � inv�lido e retorna false
	 */
	private boolean isInputDateValid() {
		Date dataBeg = bean.getTo().getOrcamentoDespesa().getDataInicio();
		Date dataEnd = bean.getTo().getOrcamentoDespesa().getDataFim();
		if (dataBeg != null && dataEnd != null) {
			if (DateUtil.validateValidityDate(dataBeg, dataEnd,
					Boolean.valueOf(false))) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Obtem a sigla RB caso seja um orcamento reembolsavel. Caso contrario
	 * obtem NM.
	 * 
	 * @return String RB ou NM
	 */
	private String getSiglaOrcamentoReembolsavel() {
		if (bean.getIsOrcDespesaClReembolsavel()) {
			return Constants.REEMBOLSAVEL;
		}
		return Constants.NOT_REEMBOLSAVEL;
	}

	/**
	 * Retorna o valor inserido pelo usuario para ser atribuido ao OrcDespesaCl.
	 * Valor arredondado RoundingMode.HALF_UP.
	 * 
	 * @return {@link BigDecimal}
	 * 
	 */
	private BigDecimal getValorOrcDespesaClTo() {
		BigDecimal valorOrcado = bean.getTo().getOrcamentoDespesa()
				.getValorOrcado();
		return valorOrcado.setScale(0, RoundingMode.HALF_UP);
	}

	/**
	 * Preapara um novo objeto {@link OrcamentoDespesa} para ser persistido
	 * juntamento com {@link OrcDespesaCl}.
	 * 
	 * @return {@link OrcamentoDespesa}
	 * 
	 */
	private OrcamentoDespesa getOrcamentoDespesaToPersist() {
		Moeda moeda = this.getMoedaSelecionada();

		OrcamentoDespesa orcamentoDespesa = bean.getTo().getOrcamentoDespesa();
		orcamentoDespesa.setMoeda(moeda);
		orcamentoDespesa.setLoginCriador(LoginUtil.getLoggedUsername());
		orcamentoDespesa.setIndicadorAtivo(Constants.SIM);
		orcamentoDespesa.setIndicadorSync(Constants.NO);
		orcamentoDespesa.setIndicadorDeleteLogico(Constants.NO);
		orcamentoDespesa.setTpOrcDesp(Constants.TRAVEL_BUDGET_CLIENT_TYPE);
		orcamentoDespesa.setValorOrcado(this.getValorOrcDespesaClTo());
		orcamentoDespesa.setTbPurpose(Constants.TB_PURPOSE_GENERAL);

		return orcamentoDespesa;
	}

	/**
	 * Prepara um novo objeto {@link OrcDespesaCl} para ser persistido.
	 * 
	 * @return {@link OrcDespesaCl}
	 * 
	 */
	private OrcDespesaCl getOrcDespesaClToPersist() {
		OrcDespesaCl orcDespesaCl = new OrcDespesaCl();
		orcDespesaCl.setCliente(clienteBean.getTo());
		orcDespesaCl.setIndicadorReembolsavel(this.getSiglaOrcamentoReembolsavel());
		orcDespesaCl.setOrcamentoDespesa(this.getOrcamentoDespesaToPersist());
		orcDespesaCl.setIndicadorDeleteLogico(Constants.NO);
		orcDespesaCl.setIndicadorTipoFatura(bean.getTo().getIndicadorTipoFatura());

		return orcDespesaCl;
	}

	/**
	 * Cria todos os relacionamentos entre um {@link OrcDespesaCl} e N
	 * {@link ContratoPratica} selecionados pelo usuario.
	 * 
	 * @param orcDespesaCl
	 *            {@link OrcDespesaCl}
	 * 
	 */
	private void createContratoPraticaOrcDespClByOrcDespesaCl(
			final OrcDespesaCl orcDespesaCl) {

		for (String grantedCLob : bean.getGrantedCLobPickList()) {

			ContratoPratica cp = contratoPraticaService
					.findContratoPraticaById(Long.valueOf(grantedCLob));

			ContratoPraticaOrcDespClId id = new ContratoPraticaOrcDespClId(
					cp.getCodigoContratoPratica(),
					orcDespesaCl.getCodigoOrcaDespCl());
			ContratoPraticaOrcDespCl cpodCl = new ContratoPraticaOrcDespCl(id,
					orcDespesaCl, cp);

			contratoPraticaOrcDespClService
					.createContratoPraticaOrcDespCl(cpodCl);
		}
	}

	private Boolean validateCLobs() {

		String lastReimbusableIndicator = null;

		for (String clobCode : bean.getGrantedCLobPickList()) {
			ContratoPratica cp = contratoPraticaService.findContratoPraticaById(Long.valueOf(clobCode));

			//Validar se existe algum indicador reembolsável não preenchido
			if(cp.getIndicadorReembolsavel() == null) {
				Messages.showError("validateCLobs",
					Constants.BUNDLE_IS_CLOB_REIMBURSABLE_MISSING,
					cp.getNomeContratoPratica());
				return Boolean.FALSE;
			}

			//Validar se todos os indicadores reembolsáveis são do mesmo tipo
			if (lastReimbusableIndicator == null) {
				lastReimbusableIndicator = cp.getIndicadorReembolsavel();
			} else if (!lastReimbusableIndicator.equals(cp.getIndicadorReembolsavel())) {
				Messages.showError("validateCLobs",
						Constants.BUNDLE_IS_CLOB_NOT_REIMBURSABLE);
				return Boolean.FALSE;
			}
		}

		String invoiceType = bean.getTo().getIndicadorTipoFatura() != null ? bean.getTo().getIndicadorTipoFatura() : "";

		//Validar se o Invoice Type está preenchido caso o tipo do reembolso for Y
		if (lastReimbusableIndicator.equalsIgnoreCase("Y") && invoiceType.equalsIgnoreCase("")) {
			Messages.showError("validateCLobs",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.ORCAMENTO_DESPESA_TIPO_FATURA);
			return Boolean.FALSE;
		}
		//Se o tipo do reembolso for N, zerar o valor do Invoice Type
		else if (lastReimbusableIndicator.equalsIgnoreCase("N")) {
			bean.getTo().setIndicadorTipoFatura(null);
		}

		return Boolean.TRUE;
	}

	/**
	 * Cria um {@link OrcDespesaCl}. Adiciona um item na lista de OrcDespesaCl
	 * da tela de OrcDespesaClForm.xhtml.
	 * 
	 */
	public void createOrcDespesaCl() {

		// valida as datas do formulario inseridas pelo usuario
		if (!this.isInputDateValid()) {
			return;
		}

		if (!this.validateCLobs()) {
			return;
		}

		OrcDespesaCl orcDespesaCl = this.getOrcDespesaClToPersist();

		// o valor do orc. despesa deve ser maior que zero
		if (orcDespesaCl.getOrcamentoDespesa().getValorOrcado().doubleValue() <= 0) {
			Messages.showError("createOrcDespesaCl",
					Constants.BUNDLE_VALUE_IS_LESS_THAN_ALLOWABLE_MINIMUM);
			return;
		}

		if (!this.isValidOrcamentoDespesaName(bean.getTo()
				.getOrcamentoDespesa().getNomeOrcDespesa())) {
			return;
		}

		try {

			// validacao de limite de valores do TB
			orcDespesaCl.setIndicadorOrcamentoExtra(Constants.NO);

			if ((!bean.getIsOrcDespesaClReembolsavel())
					&& (!orcDespesaClService.canCreateTravelBudget(
							orcDespesaCl.getOrcamentoDespesa(),
							bean.getGrantedCLobPickList()))) {

				if ((orcDespesaClService.isVp(LoginUtil.getLoggedUsername()))
						&& (bean.getIsOrcDespesaExtra())) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(orcDespesaCl.getOrcamentoDespesa()
							.getDataInicio());
					cal.add(Calendar.MONTH, 3);

					if (orcDespesaCl.getOrcamentoDespesa().getDataFim()
							.compareTo(cal.getTime()) > 0) {
						Messages.showError("createOrcDespesaCl",
								Constants.TRAVEL_BUDGET_THREE_MONTHS);
						return;
					}
					orcDespesaCl.setIndicadorOrcamentoExtra(Constants.SIM);
				} else {
					Messages.showError("createOrcDespesaCl",
							Constants.ORCAMENTO_DESPESA_ORCAMENTO_VIAGEM_LIMITE);
					return;
				}

			}

			orcDespesaClService.createOrcDespesaCl(orcDespesaCl);

			// cria todas as associa��es entre o ContratoPratica e o
			// OrcDespesaCl
			this.createContratoPraticaOrcDespClByOrcDespesaCl(orcDespesaCl);

			// envia o email de follow
			voucherMail.sendMailCreateClientTravelBudget(orcDespesaCl);

			// Seta o Map de followers
			bean.setMapFollow(followOrcDespService.loadMapFollow(LoginUtil
					.getLoggedUser()));

			bean.reset();

			// Carrega a lista de Travel Budgets inicial
			this.prepareOrcDespesaClManage();

		} catch (IntegrityConstraintException e) {
			Messages.showWarning("createOrcDespesaCl",
					Constants.TRAVEL_BUDGET_ALREADY_EXISTS);
		}
	}

	/**
	 * Prepara a tela de View.
	 * 
	 * @return Stirng da pagina de outcome
	 * 
	 */
	public String prepareOrcDespesaClView() {
		bean.setTo(orcDespesaClService.findOrcDespesaClById(bean.getTo()
				.getCodigoOrcaDespCl()));
		return OUTCOME_ORC_DESPESA_CL_VIEW;
	}

	/**
	 * Remove um {@link OrcDespesaCl}.
	 * 
	 */
	public void removeOrcDespesaCl() {

		OrcDespesaCl ordc = this.getCurrentOrcDespesaClSelected();

		List<OrcDespDelegacao> listOdd = orcDespDelegacaoService
				.findByOrcDespesa(ordc.getOrcamentoDespesa());

		for (OrcDespDelegacao orcDespDelegacao : listOdd) {
			orcDespDelegacaoService.removeOrcDespDelegacao(orcDespDelegacao);
		}

		// evia e-mail para os seguidores de cliente e do tralvel budget
		voucherMail.sendMailDeleteClientTravelBudget(ordc);

		// remove o or�amento despesa
		orcDespesaClService.removeOrcDespesaCl(this
				.getCurrentOrcDespesaClSelected());

		bean.reset();		

		this.prepareOrcDespesaClManage();
	}

	/**
	 * Desabilita um {@link OrcDespesaCl}.
	 * 
	 */
	public void desactivateOrcDespesaCl() {
		// desativa o or�amento despesa
		OrcamentoDespesa orcamento = this.getCurrentOrcDespesaClSelected()
				.getOrcamentoDespesa();
		orcamento.setIndicadorAtivo(Constants.NO);
		orcamentoDespesaService.updateOrcamentoDespesa(orcamento);

		// evia e-mail para os seguidores de cliente e do tralvel budget
		voucherMail
				.sendMailDisableClientTravelBudget(getCurrentOrcDespesaClSelected());

		bean.reset();				

		this.prepareOrcDespesaClManage();
	}

	/**
	 * Prepara a tela de White List.
	 * 
	 * @return outcome da tela de white list
	 * 
	 */
	public String prepareWhiteList() {
		whiteListBean.reset();
		whiteListBean.setListaOrcDespWhiteList(whiteListService
				.findByOrcamentoDespesa(bean.getTo().getOrcamentoDespesa()));

		return OUTCOME_ALLOW_LIST;
	}

	public String prepareClone() {
		this.bean.setIsITSupport(validateITSupport());
		return OUTCOME_CLONE;
	}

	/**
	 * A��o utilizada no autocomplete login. Retorna uma lista de recursos.
	 * 
	 * @param value
	 *            - valor (mnemonico) utilizado na busca dos recursos
	 * 
	 * @return retorna uma lista de recurso
	 */
	public List<Recurso> autoCompleteRecurso(final Object value) {
		String tipoRecurso = Constants.RESOURCE_TYPE_PE;
		return recursoService.quickSearch((String) value, tipoRecurso);
	}

	/**
	 * Realiza a valida�ao do Recurso e da Pessoa.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateRecursoPessoa(final FacesContext context,
			final UIComponent component, final Object value) {

		String login = (String) value;
		if ((login != null) && (!"".equals(login))) {
			Recurso r = recursoService.findRecursoByMnemonico(login);
			if (r == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Adiciona login na WhiteList.
	 * 
	 */
	public void addPersonWhiteList() {
		OrcDespWhiteList whiteList = whiteListBean.getTo();
		Long codigoOrcamento = this.getCurrentOrcDespesaClSelected()
				.getOrcamentoDespesa().getCodigoOrcaDespesa();

		OrcamentoDespesa orcamento = orcamentoDespesaService
				.findOrcamentoDespesaById(codigoOrcamento);

		Hibernate.initialize(orcamento);
		whiteList.setOrcamentoDespesa(orcamento);

		if (whiteListService.addPersonOrcDespWhiteList(whiteList)) {
			Messages.showSucess("addPersonWhiteList",
					Constants.GENEREC_MSG_SUCCESS_ADD);
			whiteListBean.reset();
		}
		this.prepareWhiteList();
	}

	/**
	 * Delete pessoa da whiteLIst.
	 * 
	 */
	public void removePersonWhiteList() {
		OrcDespWhiteList entity = whiteListService.findById(whiteListBean
				.getTo().getCodigoOrcDespWl());
		if (whiteListService.removePersonOrcDespWhiteLIst(entity)) {
			Messages.showSucess("remove", Constants.GENEREC_MSG_SUCCESS_REMOVE);
		}
		this.prepareWhiteList();
	}

	/**
	 * Cria um seguidor para travel budget de Cliente.
	 */
	public void followOrcDesp() {
		OrcamentoDespesa orcDesp = orcamentoDespesaService
				.findOrcamentoDespesaById(bean.getTo().getOrcamentoDespesa()
						.getCodigoOrcaDespesa());

		// Busca nome do usuario loggado (seguidor)
		String userName = LoginUtil.getLoggedUsername();

		// Cria Follower
		FollowOrcamentoDesp follower = new FollowOrcamentoDesp();
		follower.setOrcamentoDespesa(orcDesp);
		follower.setCodigoLogin(userName);

		followOrcDespService.createFollowOrcamentoDesp(follower);

		bean.reset();

		// Atualiza Map
		bean.setMapFollow(followOrcDespService.loadMapFollow(LoginUtil
				.getLoggedUser()));

		// Atualiza lista
		this.prepareOrcDespesaClManage();
	}

	/**
	 * Remove um seguidor de travel budget de cliente.
	 */
	public void unfollowOrcDesp() {
		// busca or�amento despesa
		OrcamentoDespesa orcDesp = orcamentoDespesaService
				.findOrcamentoDespesaById(bean.getTo().getOrcamentoDespesa()
						.getCodigoOrcaDespesa());

		// busca o followgrupocusto pelo grupo de custo e login
		FollowOrcamentoDesp followOrcamentoDesp = followOrcDespService
				.findByOrcDespesaAndLogin(orcDesp,
						LoginUtil.getLoggedUsername());

		followOrcDespService.removeFollowOrcamentoDesp(followOrcamentoDesp);

		bean.reset();

		// Atualiza Map
		bean.setMapFollow(followOrcDespService.loadMapFollow(LoginUtil
				.getLoggedUser()));

		// Atualiza lista
		this.prepareOrcDespesaClManage();
	}

	/**
	 * Altera o nome do Orcamento Despesa no inplace input.
	 */
	public void updateNomeOrcamentoDespesa() {
		// orcamento despesa a ser alterado
		OrcamentoDespesa orcamentoDespesa = orcamentoDespesaService
				.findOrcamentoDespesaById(bean.getCurrentRowId());

		// seta o novo nome
		orcamentoDespesa.setNomeOrcDespesa(bean
				.getNomeOrcamentoDespesaInplaceInput());

		// salva no banco
		orcamentoDespesaService.updateOrcamentoDespesa(orcamentoDespesa);
	}

	/**
	 * Carrega lista de Tipos de fatura para combobox e seta o map.
	 */
	private void loadTiposFaturaList() {
		Map<String, String> mapTipoFatura = new HashMap<String, String>();
		List<String> listResult = new ArrayList<String>();

		mapTipoFatura.put(BundleUtil
				.getBundle(Constants.ORCAMENTO_DESPESA_NOTA_FISCAL), BundleUtil
				.getBundle(Constants.ORCAMENTO_DESPESA_NOTA_FISCAL_VALOR));
		mapTipoFatura.put(BundleUtil
				.getBundle(Constants.ORCAMENTO_DESPESA_NOTA_DEBITO), BundleUtil
				.getBundle(Constants.ORCAMENTO_DESPESA_NOTA_DEBITO_VALOR));

		listResult.add(BundleUtil
				.getBundle(Constants.ORCAMENTO_DESPESA_NOTA_FISCAL));
		listResult.add(BundleUtil
				.getBundle(Constants.ORCAMENTO_DESPESA_NOTA_DEBITO));

		bean.setListaTipoFaturaCombobox(listResult);
		bean.setMapTipoFatura(mapTipoFatura);
	}

	/**
	 * Obtem o tipoFatura selecionado na tela.
	 * 
	 * @return codigo do tipoFatura selecionado
	 * 
	 */
	private String getTipoFaturaSelecionada() {
		String codigoTipoFatura = bean.getMapTipoFatura().get(
				bean.getTo().getIndicadorTipoFatura());

		return codigoTipoFatura;
	}

	/**
	 * Verifica se o tipo de fatura � v�lido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateTipoFatura(final FacesContext context,
			final UIComponent component, final Object value) {

		List<String> tiposFatura = new ArrayList<String>(bean
				.getMapTipoFatura().values());
		String strValue = (String) value;

		if ((strValue == null) || (!tiposFatura.contains(strValue))) {
			String label = (String) component.getAttributes().get("label");
			throw new ValidatorException(Messages.getMessageError(
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
		}
	}

	/**
	 * Prepare da tela de delegation.
	 * 
	 * @return
	 */
	public String prepareDelegation() {
		orcDespesaDelegationBean.resetBean();
		orcDespesaDelegationBean
				.setListOrcDespDelegacao(orcDespDelegacaoService
						.findByOrcDespesa(bean.getTo().getOrcamentoDespesa()));

		return OUTCOME_DELEGATION;
	}

	/**
	 * A��o utilizada no autocomplete login. Retorna uma lista de pessoas.
	 * 
	 * @param value
	 *            - valor (mnemonico) utilizado na busca dos recursos
	 * 
	 * @return retorna uma lista de recurso
	 */
	public List<Pessoa> autoCompletePessoaDelegation(final Object value) {
		return pessoaService.quickSearchPMSUser((String) value);
	}

	/**
	 * Realiza a valida�ao do Recurso e da Pessoa para o delegation.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            , valor do componente.
	 */
	public void validateRecursoPessoaDelegation(final FacesContext context,
			final UIComponent component, final Object value) {

		String login = (String) value;
		if ((login != null) && (!"".equals(login))) {
			Pessoa pessoa = pessoaService
					.findByLoginExecutiveSeniorManager(login);
			if (pessoa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Adiciona login na Delegacao.
	 * 
	 */
	public void addPersonDelegation() {
		Long codigoOrcDespesa = this.getCurrentOrcDespesaClSelected()
				.getOrcamentoDespesa().getCodigoOrcaDespesa();

		OrcDespDelegacao delagation = new OrcDespDelegacao(
				new OrcDespDelegacaoId(codigoOrcDespesa,
						orcDespesaDelegationBean.getTo().getCodigoLogin()));

		OrcamentoDespesa orcDesp = orcamentoDespesaService
				.findOrcamentoDespesaById(codigoOrcDespesa);
		delagation.setOrcDespesa(orcDesp);
		delagation.setCodigoLogin(orcDespesaDelegationBean.getTo()
				.getCodigoLogin());

		if (orcDespDelegacaoService.createOrcDespDelegacao(delagation)) {
			Messages.showSucess("addPersonWhiteList",
					Constants.GENEREC_MSG_SUCCESS_ADD);
		}

		this.prepareDelegation();

	}

	/**
	 * Delete pessoa da delegacao.
	 * 
	 */
	public void removePersonDelegation() {
		Long codigoOrcDespesa = this.getCurrentOrcDespesaClSelected()
				.getOrcamentoDespesa().getCodigoOrcaDespesa();

		OrcamentoDespesa orcDesp = orcamentoDespesaService
				.findOrcamentoDespesaById(codigoOrcDespesa);
		OrcDespDelegacao entity = orcDespDelegacaoService
				.findByOrcDespesaAndLogin(orcDesp, orcDespesaDelegationBean
						.getTo().getCodigoLogin());

		if (orcDespDelegacaoService.removeOrcDespDelegacao(entity)) {
			Messages.showSucess("remove", Constants.GENEREC_MSG_SUCCESS_REMOVE);
		}
		this.prepareDelegation();
	}

	/**
	 * Exibe somente os TBs vigentes.
	 */

	public void filterTravelBudgetList() {

		Cliente cliente = this.getCurrentClienteSelected();

		if (bean.getFlagFilterTravelBudgetList()) {
			bean.setOrcDespesaClList(orcDespesaClService
					.findOnlyValidByClient(cliente));
		} else {
			bean.setOrcDespesaClList(orcDespesaClService
					.findOrcDespesaClByClientAndActive(cliente));
		}
		bean.setIsITSupport(validateITSupport());
	}

	/**
	 * Prepare do combobox de Tipo de Faturamento.
	 */
	public void prepareInvoiceTypeCombobox() {

		Cliente cliente = this.getCurrentClienteSelected();
		List<Msa> msaList = cliente.getMsas();

		List<String> tipoFaturamentoList = new ArrayList<String>();

		for (Msa msa : msaList) {
			if ((msa.getIndicadorTipoFatura() != null)
					&& (!tipoFaturamentoList.contains(msa
							.getIndicadorTipoFatura()))) {
				tipoFaturamentoList.add(msa.getIndicadorTipoFatura());
			}
		}

		if (tipoFaturamentoList.size() == 1) {
			bean.getTo().setIndicadorTipoFatura(tipoFaturamentoList.get(0));
		}

		if (tipoFaturamentoList.size() > 1) {
			Messages.showWarning("prepareInvoiceTypeCombobox",
					Constants.MSA_WITH_MORE_THAN_ONE_INVOICE_TYPE);
		}

		if (tipoFaturamentoList.isEmpty()) {
			Messages.showWarning("prepareInvoiceTypeCombobox",
					Constants.MSA_WITHOUT_INVOICE_TYPE);
		}

	}

	/**
	 * Atualiza o tipo de faturamento de um registro no resultado da pesquisa.
	 */
	public void updateInvoiceType() {

		OrcDespesaCl entity = bean.getCurrentRow();

		orcDespesaClService.updateOrcDespesaCl(entity);

		Messages.showSucess("updateIncoiceType",
				Constants.GENEREC_MSG_SUCCESS_UPDATE);

	}

	/**
	 * Realiza a validacao do campo de nome do OrcamentoDespesa.
	 * 
	 * @param name
	 */
	public Boolean isValidOrcamentoDespesaName(String name) {

		if (name.indexOf("'") >= 0) {
			Messages.showError("orcDespName",
					Constants.ORCAMENTO_DESPESA_CHARACTER_NOT_ALLOWED,
					" \" ' \" ");

			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}


	private Boolean validateITSupport() {
		GrantedAuthority[] loggedUserAuthorities = LoginUtil
				.getLoggedUserAuthorities();
		boolean isItSupport = false;
		for (GrantedAuthority authority : loggedUserAuthorities) {

			if(authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET:ED")
					|| authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET:CR")
					|| authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET.EXTRA:ED")
					|| authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET.EXTRA:CR")){
				isItSupport = false;
				continue;
			}
			if (authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET:VW") ||
					authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET.EXTRA:VW")) {
				isItSupport = true;
			}
		}
		return isItSupport;
	}

}
