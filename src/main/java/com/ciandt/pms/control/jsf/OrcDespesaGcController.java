package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IFollowOrcamentoDespService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IGrupoCustoStatusService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IOrcDespDelegacaoService;
import com.ciandt.pms.business.service.IOrcDespWhiteListService;
import com.ciandt.pms.business.service.IOrcDespesaGcService;
import com.ciandt.pms.business.service.IOrcamentoDespesaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.business.service.IVoucherMail;
import com.ciandt.pms.control.jsf.bean.ClienteBean;
import com.ciandt.pms.control.jsf.bean.GrupoCustoBean;
import com.ciandt.pms.control.jsf.bean.OrcDespWhiteListBean;
import com.ciandt.pms.control.jsf.bean.OrcDespesaDelegationBean;
import com.ciandt.pms.control.jsf.bean.OrcDespesaGcBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.FollowOrcamentoDesp;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoStatus;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.OrcDespDelegacao;
import com.ciandt.pms.model.OrcDespDelegacaoId;
import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import org.springframework.security.GrantedAuthority;

/**
 * 
 * A classe TravelBudgetController proporciona as funcionalidades de Controller
 * para a entidade TravelBudget.
 * 
 * @since 24/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Controller
@RolesAllowed({ "BUS.COST_CENTER.TRAVEL_BUDGET:ED", "BOF.COST_CENTER.TRAVEL_BUDGET:ED", "BUS.COST_CENTER.TRAVEL_BUDGET:VW", "BOF.COST_CENTER.TRAVEL_BUDGET:VW" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespesaGcController {

	/*********** OUTCOMES **************************/

	/** outcome tela travel budget. */
	private static final String OUTCOME_ORCAMENTO_DESPESA_GRUPO_CUSTO = "orcamento_despesa_grupo_custo";

	/** outcome tela whitelist. */
	private static final String OUTCOME_LISTA_BRANCA = "lista_branca_grupo_custo";
	
	/** Outcome tela de delegacao de travel budget. */
	private static final String OUTCOME_DELEGATION = "orc_despesa_gc_delegation";

	/** Outcome tela de clone de travel budget. */
	private static final String OUTCOME_CLONE = "orc_despesa_clone";

	/** Outcome tela de View. */
	private static final String OUTCOME_ORC_DESPESA_GC_VIEW = "orc_despesa_gc_view";


	/*********** SERVICES **************************/

	/** servico de orcamento despesa. */
	@Autowired
	private IOrcamentoDespesaService orcamentoDespesaService;

	/** servico de orcDespesaGc. */
	@Autowired
	private IOrcDespesaGcService orcDespesaGcService;

	/** servi�o de GrupoCusto. */
	@Autowired
	private IGrupoCustoService grupoCustoService;

	/** servi�o de OrcDespWhiteListService. */
	@Autowired
	private IOrcDespWhiteListService whiteListService;

	/** servi�o de RecursoService. */
	@Autowired
	private IRecursoService recursoService;

	/** servi�o de FollowOrcamentoDespesa. */
	@Autowired
	private IFollowOrcamentoDespService followOrcDespService;

	/** servi�o de Moeda. */
	@Autowired
	private IMoedaService moedaService;
	
	/** Servico de {@link Pessoa}. */
	@Autowired
	private IPessoaService pessoaService;

	/** Servico de {@link OrcDespDelegacao}. */
	@Autowired
	private IOrcDespDelegacaoService orcDespDelegacaoService;

	/** Servico de voucherMail. */
	@Autowired
	private IVoucherMail voucherMail;

	/*********** BEANS **************************/

	/** Instancia de bean. */
	@Autowired
	private OrcDespesaGcBean bean;

	/** Instancia de clienteBean. */
	@Autowired
	private ClienteBean clienteBean;

	/** Instancia de grupoCustoBean. */
	@Autowired
	private GrupoCustoBean grupoCustoBean;

	/** Instancia de orcDespWhiteListBean. */
	@Autowired
	private OrcDespWhiteListBean whiteListBean;
	
	/** Instancia do bean de delegation. */
	@Autowired
	private OrcDespesaDelegationBean orcDespesaDelegationBean;

	@Autowired
	private IGrupoCustoStatusService grupoCustoStatusService;


	/*********************************************/

	/**
	 * @return the bean
	 */
	public OrcDespesaGcBean getBean() {
		return bean;
	}

	/**
	 * @return the grupoCustoBean
	 */
	public GrupoCustoBean getGrupoCustoBean() {
		return grupoCustoBean;
	}

	/**
	 * @return the clienteBean
	 */
	public ClienteBean getClienteBean() {
		return clienteBean;
	}

	/**
	 * @return the whiteListBean
	 */
	public OrcDespWhiteListBean getWhiteListBean() {
		return whiteListBean;
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

	public String backToOrcDespesaGcManage() {
		this.prepareOrcDespesaGc();

		return OUTCOME_ORCAMENTO_DESPESA_GRUPO_CUSTO;
	}

	public String prepareOrcDespesaGc() {
		bean.reset();
		bean.resetFlagFilterTravelBudgetList();

		return prepareOrcDespesaGcManage();
	}

	/**
	 * Prepare para a tela de travel budget.
	 * 
	 * @return pagina de destino.k
	 */
	public String prepareOrcDespesaGcManage() {

		bean.setIsITSupport(validateITSupport());

		// Busca grupo de custo
		Long codigoGrupoCusto = grupoCustoBean.getCurrentRowId();
		GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(codigoGrupoCusto);

		disabledAddTravelBudget(grupoCusto);

		// Carrega combobox moeda
		this.loadMoedaList(moedaService.findMoedaAll());

		// Seta o Map de followers
		bean.setMapFollow(followOrcDespService.loadMapFollow(LoginUtil
				.getLoggedUser()));

		// Atualiza lista
		bean.setListaOrcDespesaGc(orcDespesaGcService
				.findByGrupoCustoAndActive(codigoGrupoCusto));

		// Seta o To do cliente
		grupoCustoBean.setTo(grupoCusto);

		this.filterTravelBudgetList();

		return OUTCOME_ORCAMENTO_DESPESA_GRUPO_CUSTO;
	}

	private void disabledAddTravelBudget(GrupoCusto grupoCusto) {
		bean.setDisabledTravelBudget(true);
		if (grupoCusto.getGrupoCustoStatus().getSiglaStatusGrupoCusto().equals(Constants.NEW_ACTIVE)) {
			bean.setDisabledTravelBudget(false);
		}
	}

	/**
	 * Cria um orcamento despesa no banco.
	 */
	public void createOrcDespesaGc() {

		// Valida data
		boolean isValidDate = false;

		Date dataBeg = bean.getTo().getOrcamentoDespesa().getDataInicio();
		Date dataEnd = bean.getTo().getOrcamentoDespesa().getDataFim();
		if (dataBeg != null && dataEnd != null) {
			if (DateUtil.validateValidityDate(dataBeg, dataEnd,
					Boolean.valueOf(false))) {
				isValidDate = true;
			}
		}

		if (!this.isValidOrcamentoDespesaName(bean.getTo()
				.getOrcamentoDespesa().getNomeOrcDespesa())) {
			return;
		}

		if (isValidDate) {

			OrcDespesaGc orcDespesaGc = new OrcDespesaGc();

			// Busca o bean
			OrcamentoDespesa orcamentoDespesa = bean.getTo()
					.getOrcamentoDespesa();

			// seta o or�amento despesa no orcDespesaGc
			orcDespesaGc.setOrcamentoDespesa(orcamentoDespesa);

			// o valor do orc. despesa deve ser maior que zero
			if (orcDespesaGc.getOrcamentoDespesa().getValorOrcado()
					.doubleValue() <= 0) {
				Messages.showError("createOrcDespesaGc",
						Constants.BUNDLE_VALUE_IS_LESS_THAN_ALLOWABLE_MINIMUM);
				return;
			}

			// Seta o grupo de custo
			orcDespesaGc.setGrupoCusto(grupoCustoBean.getTo());

			orcDespesaGc.setIndicadorDeleteLogico(Constants.NO);

			orcDespesaGc.setIndicadorWlOnly(
					orcamentoDespesa.getTbPurpose().equalsIgnoreCase(Constants.TB_PURPOSE_BENEFIT)
							? Constants.SIM : Constants.NO);

			// Captura a moeda
			Long codigoMoeda = bean.getMapMoeda().get(bean.getMoeda());
			Moeda moeda = moedaService.findMoedaById(codigoMoeda);
			orcamentoDespesa.setMoeda(moeda);

			// Captura o dono do TravelBudget
			orcamentoDespesa.setLoginCriador(LoginUtil.getLoggedUsername());

			// Seta o indicadorAtivo
			orcamentoDespesa.setIndicadorAtivo(Constants.SIM);

			// Seta sync
			orcamentoDespesa.setIndicadorSync(Constants.NO);

			// Seta a flag de Indicador de delete
			orcamentoDespesa.setIndicadorDeleteLogico(Constants.NO);

			// seta a flag de indicador tipo
			orcamentoDespesa
					.setTpOrcDesp(Constants.TRAVEL_BUDGET_COST_GROUP_TYPE);

			// Seta o valorOrcado
			BigDecimal valorOrcado = bean.getTo().getOrcamentoDespesa()
					.getValorOrcado();
			valorOrcado = valorOrcado.setScale(0, RoundingMode.HALF_UP);
			orcamentoDespesa.setValorOrcado(valorOrcado);

			// Cria orcamentoDespesa
			try {

				orcDespesaGcService.createOrcDespesaGc(orcDespesaGc);

				// Cria Follower por default
				FollowOrcamentoDesp follower = new FollowOrcamentoDesp();
				follower.setOrcamentoDespesa(orcamentoDespesa);
				follower.setCodigoLogin(LoginUtil.getLoggedUsername());

				followOrcDespService.createFollowOrcamentoDesp(follower);

				// Seta o Map de followers
				bean.setMapFollow(followOrcDespService.loadMapFollow(LoginUtil
						.getLoggedUser()));

				/*
				 * orcamentoDespesaService
				 * .sendEmailFollowOrcamentoDespesa(orcamentoDespesa);
				 */
				voucherMail.sendMailCreateCostGroupTravelBudget(orcDespesaGc);

				Messages.showSucess("createOrcDespesa",
						Constants.GENEREC_MSG_SUCCESS_ADD);

				bean.reset();
			} catch (IntegrityConstraintException e) {
				Messages.showWarning("createOrcDesp",
						Constants.TRAVEL_BUDGET_ALREADY_EXISTS);
			}

		}

		// Carrega a lista de Travel Budgets inicial
		this.prepareOrcDespesaGcManage();

	}

	/**
	 * Remove OrcamentoDespesa.
	 */
	public void removeOrcDespesaGc() {

		// Busca OrcamentoDespesa
		OrcDespesaGc orcDesp = orcDespesaGcService.findOrcDespesaGcById(bean
				.getTo().getCodigoOrcDespGc());

		List<OrcDespDelegacao> listOdd = orcDespDelegacaoService
				.findByOrcDespesa(orcDesp.getOrcamentoDespesa());

		for (OrcDespDelegacao orcDespDelegacao : listOdd) {
			orcDespDelegacaoService.removeOrcDespDelegacao(orcDespDelegacao);
		}

		/*
		 * orcamentoDespesaService.sendEmailDeleteOrcamentoDespesa(orcDesp
		 * .getOrcamentoDespesa());
		 */
		voucherMail.sendMailDeleteCostGroupTravelBudget(orcDesp);

		// Remove e exibe mensagem de sucesso
		if (orcDespesaGcService.removeOrcDespesaGc(orcDesp)) {
			Messages.showSucess("remove", Constants.GENEREC_MSG_SUCCESS_REMOVE);
		}

		bean.reset();

    	// carrega a lista contendo os itens OrcDespesaGC
    	this.filterTravelBudgetList();
	}

	/**
	 * Redireciona para pagina de WhiteList.
	 * 
	 * @return outcome pagina de destino.
	 */
	public String prepareWhiteList() {

		whiteListBean.reset();
		whiteListBean.setListaOrcDespWhiteList(whiteListService
				.findByOrcamentoDespesa(bean.getTo().getOrcamentoDespesa()));

		return OUTCOME_LISTA_BRANCA;
	}

	public String prepareClone() {
		return OUTCOME_CLONE;
	}

	/**
	 * Adiciona login na WhiteList.
	 */
	public void addPersonWhiteList() {
		// Busca o To.
		OrcDespWhiteList entity = whiteListBean.getTo();

		// Busca OrcamentoDespesa
		OrcamentoDespesa orcDesp = orcamentoDespesaService
				.findOrcamentoDespesaById(bean.getTo().getOrcamentoDespesa()
						.getCodigoOrcaDespesa());
		// Seta OrcamentoDespesa
		entity.setOrcamentoDespesa(orcDesp);

		// Cria no banco
		if (whiteListService.addPersonOrcDespWhiteList(entity)) {
			Messages.showSucess("addPerson", Constants.GENEREC_MSG_SUCCESS_ADD);
			whiteListBean.reset();
		}

		// Atualiza lista de WhiteList
		whiteListBean.setListaOrcDespWhiteList(whiteListService
				.findByOrcamentoDespesa(bean.getTo().getOrcamentoDespesa()));

	}

	/**
	 * Delete pessoa da whiteLIst.
	 */
	public void removePersonWhiteList() {
		// Busca pessoa
		OrcDespWhiteList entity = whiteListService.findById(whiteListBean
				.getTo().getCodigoOrcDespWl());
		// Deleta e exibe mensagem
		if (whiteListService.removePersonOrcDespWhiteLIst(entity)) {
			Messages.showSucess("remove", Constants.GENEREC_MSG_SUCCESS_REMOVE);
		}

		whiteListBean.reset();
		// Atualiza lista
		whiteListBean.setListaOrcDespWhiteList(whiteListService
				.findByOrcamentoDespesa(bean.getTo().getOrcamentoDespesa()));
	}

	/**
	 * Desativa OrcamentoDespesa.
	 */
	public void desactivateOrcDesp() {
		// Busca OrcamentoDespesa
		OrcamentoDespesa orcDesp = orcamentoDespesaService
				.findOrcamentoDespesaById(bean.getTo().getOrcamentoDespesa()
						.getCodigoOrcaDespesa());

		// Torca o status
		orcDesp.setIndicadorAtivo("N");

		// Atualiza OrcamentoDespesa
		if (orcamentoDespesaService.updateOrcamentoDespesa(orcDesp)) {
			Messages.showSucess("update",
					Constants.TRAVEL_BUDGET_DESATIVADO_SUCESSO);
		}

		// orcamentoDespesaService.sendEmailDisableOrcamentoDespesa(orcDesp);
		voucherMail.sendMailDisableCostGroupTravelBudget(orcDespesaGcService
				.findByOrcDespesa(orcDesp));

		bean.reset();

		// carrega a lista contendo os itens OrcDespesaGC
		this.filterTravelBudgetList();
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
	 * Cria um seguidor do TravelBudget.
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

		// carrega a lista contendo os itens OrcDespesaGC
		this.filterTravelBudgetList();
	}

	/**
	 * Unfollow de OrcamentoDespesa.
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

		// carrega a lista contendo os itens OrcDespesaGC
		this.filterTravelBudgetList();
	}

	/**
	 * Carrega lista de moeda para combobox e seta o map.
	 * 
	 * @param listaMoeda
	 *            lista de moeda
	 */
	private void loadMoedaList(final List<Moeda> listaMoeda) {
		Map<String, Long> mapMoeda = new HashMap<String, Long>();
		List<String> listResult = new ArrayList<String>();

		for (Moeda moeda : listaMoeda) {
			mapMoeda.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
			listResult.add(moeda.getNomeMoeda());
		}

		bean.setListaMoedaCombobox(listResult);
		bean.setMapMoeda(mapMoeda);
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
	 * Altera o indicador de White List Only no banco.
	 */
	public void updateWListOrcamentoDespesa() {
		// orcamento despesa gc a ser alterado
		OrcDespesaGc orcDespesaGc = orcDespesaGcService
				.findOrcDespesaGcById(bean.getCurrentRowId());

		// seta o white list only
		orcDespesaGc.setIndicadorWlOnlyAsBoolean(bean.getCheckWListOnly());

		// salva no banco
		orcDespesaGcService.updateOrcDespesaGc(orcDespesaGc);
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
		Long codigoOrcDespesa = this.getCurrentOrcDespesaGcSelected()
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
		Long codigoOrcDespesa = this.getCurrentOrcDespesaGcSelected()
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
	 * Obtem o {@link OrcDespesaCl} selecionado.
	 * 
	 * @return the {@link OrcDespesaCl}
	 * 
	 */
	private OrcDespesaGc getCurrentOrcDespesaGcSelected() {
		return orcDespesaGcService.findOrcDespesaGcById(bean.getTo()
				.getCodigoOrcDespGc());
	}

	/**
	 * Exibe somente os TBs vigentes.
	 */
	public void filterTravelBudgetList() {

		// Busca grupo de custo
		Long codigoGrupoCusto = grupoCustoBean.getCurrentRowId();
		GrupoCusto grupoCusto = grupoCustoService
				.findGrupoCustoById(codigoGrupoCusto);

		if (bean.getFlagFilterTravelBudgetList()) {
			bean.setListaOrcDespesaGc(orcDespesaGcService
					.findOnlyValidByCostGroup(grupoCusto));
		} else {
			// Atualiza lista
			bean.setListaOrcDespesaGc(orcDespesaGcService
					.findByGrupoCustoAndActive(codigoGrupoCusto));
		}
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
			if(authority.getAuthority().equals("BUS.COST_CENTER.TRAVEL_BUDGET:ED")
					|| authority.getAuthority().equals("BUS.COST_CENTER.TRAVEL_BUDGET:CR")
					|| authority.getAuthority().equals("BOF.COST_CENTER.TRAVEL_BUDGET:ED")
					|| authority.getAuthority().equals("BOF.COST_CENTER.TRAVEL_BUDGET:CR")
					|| authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET:ED")
					|| authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET:CR")){
				return false;
			}
			if (authority.getAuthority().equals("BUS.CLIENT.TRAVEL_BUDGET:VW")
			     || authority.getAuthority().equals("BOF.COST_CENTER.TRAVEL_BUDGET:VW")) {
				isItSupport = true;
			}
		}
		return isItSupport;
	}


	/**
	 * Prepara a tela de View.
	 *
	 * @return Stirng da pagina de outcome
	 *
	 */
	public String prepareOrcDespesaGcView() {
		bean.setTo(orcDespesaGcService.findOrcDespesaGcById(bean.getTo().getCodigoOrcDespGc()));
		return OUTCOME_ORC_DESPESA_GC_VIEW;
	}
}
