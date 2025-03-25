package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import com.ciandt.pms.control.jsf.bean.MessageControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IFollowClienteService;
import com.ciandt.pms.business.service.IPotencialService;
import com.ciandt.pms.control.jsf.bean.ClienteBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.FollowCliente;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Potencial;
import com.ciandt.pms.util.LoginUtil;

/**
 * Define o Controller da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BUS.CLIENT:VW", "BUS.CLIENT:ED", "BUS.CLIENT:DE", "BUS.CLIENT.TRAVEL_BUDGET:ED",
				"BUS.CLIENT.TRAVEL_BUDGET.EXTRA:ED", "BUS.CLIENT:CR" })
public class ClientePaiController {

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_CLIENTE_PAI_ADD = "cliente_pai_add";

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_CLIENTE_PAI_EDIT = "cliente_pai_edit";

	/** outcome tela remocao da entidade. */
	private static final String OUTCOME_CLIENTE_PAI_DELETE = "cliente_pai_delete";

	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_CLIENTE_PAI_RESEARCH = "cliente_pai_research";

	/**
	 * Instancia do servico.
	 */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do servico. */
	@Autowired
	private IPotencialService potencialService;

	/** Instancia do servico. */
	@Autowired
	private IFollowClienteService followClienteService;

	/** Instancia do bean controle de mensagnes. */
	@Autowired
	private MessageControlBean messageConntrolBean = null;

	/**
	 * Instancia do bean.
	 */
	@Autowired
	private ClienteBean bean = null;

	/**
	 * @return the bean
	 */
	public ClienteBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final ClienteBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepara a tela de pesquisa da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareResearch() {
		bean.reset();
		bean.setSuggestionsListInAtivo(Constants.ACTIVE_INACTIVE_VALUES);

		this.loadPotencialListAndMapa(potencialService.findPotencialAllActive());

		// Carrega o map de seguidor.
		bean.setMapFollow(this.mapFollowClient(LoginUtil.getLoggedUser()));

		return OUTCOME_CLIENTE_PAI_RESEARCH;
	}

	/**
	 * Back para a pagina de reserach do cliente sem resetar o bean.
	 * 
	 * @return pagina de destino.
	 */
	public String backClient() {
		this.findByFilter();
		messageConntrolBean.setDisplayMessages(Boolean.FALSE);
		return OUTCOME_CLIENTE_PAI_RESEARCH;
	}

	/**
	 * Prepara a tela de insercao da entidade.
	 * 
	 * @return pagina de criação de cliente
	 */
	public String prepareCreate() {
		bean.resetTo();
		bean.setIsUpdate(Boolean.FALSE);
		this.loadPotencialListAndMapa(potencialService.findPotencialAllActive());
		return OUTCOME_CLIENTE_PAI_ADD;
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @return pagina de criação de cliente
	 */
	public String create() {
		Cliente cliente = bean.getTo();
		cliente.setIndicadorAtivo(Constants.ACTIVE);

		// checa se o potencial foi selecionado
		Potencial potencial = null;
		if (bean.getPotencialMap().containsKey(bean.getPotencialSelected())) {
			Long codigoPotencial = bean.getPotencialMap().get(
					bean.getPotencialSelected());

			potencial = potencialService.findPotencialById(codigoPotencial);
		}
		cliente.setPotencial(potencial);

		clienteService.createCliente(cliente);
		Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
				Constants.ENTITY_NAME_CLIENTE_PAI);
		bean.resetTo();

		clienteService.sendClienteMail(cliente);

		return OUTCOME_CLIENTE_PAI_ADD;
	}

	/**
	 * Prepara a tela de edicao da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareUpdate() {
		bean.setSuggestionsListInAtivo(Constants.ACTIVE_INACTIVE_VALUES);
		findById(bean.getCurrentRowId());

		// seta o nome do potencial do cliente
		bean.setPotencialSelected("");
		if (bean.getTo().getPotencial() != null) {
			bean.setPotencialSelected(bean.getTo().getPotencial()
					.getNomePotencial());
		}

		// carrega o combo de potencial
		this.loadPotencialListAndMapa(potencialService.findPotencialAllActive());

		bean.setIsUpdate(Boolean.TRUE);
		return OUTCOME_CLIENTE_PAI_EDIT;
	}

	/**
	 * Executa um update da entidade.
	 * 
	 * @return pagina de destino
	 * 
	 */
	public String update() {
		Cliente cliente = bean.getTo();
		cliente.setCliente(null);

		// checa se o potencial foi selecionado
		Potencial potencial = null;
		if (bean.getPotencialMap().containsKey(bean.getPotencialSelected())) {
			Long codigoPotencial = bean.getPotencialMap().get(
					bean.getPotencialSelected());

			potencial = potencialService.findPotencialById(codigoPotencial);
		}
		cliente.setPotencial(potencial);

		try {
			clienteService.updateCliente(cliente);

		} catch (IntegrityConstraintException ice) {
			Messages.showError("update", ice.getMessage(), new Object[] {
					Constants.ENTITY_NAME_CLIENTE_PAI,
					Constants.ENTITY_NAME_CLIENTE });

			return null;
		}

		Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
				Constants.ENTITY_NAME_CLIENTE_PAI);

		bean.resetTo();
		findByFilter();

		return OUTCOME_CLIENTE_PAI_RESEARCH;
	}

	/**
	 * Prepara a tela de remocao da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareRemove() {
		findById(bean.getCurrentRowId());

		// seta o nome do potencial do cliente
		bean.setPotencialSelected("");
		if (bean.getTo().getPotencial() != null) {
			bean.setPotencialSelected(bean.getTo().getPotencial()
					.getNomePotencial());
		}

		return OUTCOME_CLIENTE_PAI_DELETE;
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @return pagina de destino
	 * 
	 */
	public String remove() {

		Cliente cl = clienteService.findClienteById(bean.getTo()
				.getCodigoCliente());

		try {
			clienteService.removeCliente(cl);

		} catch (IntegrityConstraintException ice) {

			if (cl.getClientes().size() > 0) {
				Messages.showError("remove", ice.getMessage(), new Object[] {
						Constants.ENTITY_NAME_CLIENTE_PAI,
						Constants.ENTITY_NAME_CLIENTE });
			} else {
				Messages.showError("remove", ice.getMessage(), new Object[] {
						Constants.ENTITY_NAME_CLIENTE_PAI,
						Constants.ENTITY_NAME_MSA });
			}
			return null;
		}

		Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
				Constants.ENTITY_NAME_CLIENTE);

		bean.resetTo();
		findByFilter();

		return OUTCOME_CLIENTE_PAI_RESEARCH;
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 */
	public void findById(final Long id) {
		bean.setTo(clienteService.findClienteById(id));
		Cliente to = bean.getTo();
		if (to == null) {
			Messages.showWarning("findById",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 */
	public void findByFilter() {
		Cliente filter = bean.getFilter();

		// checa se o potencial foi selecionado
		Potencial potencial = null;
		if (bean.getPotencialMap().containsKey(bean.getPotencialSelected())) {
			Long codigoPotencial = bean.getPotencialMap().get(
					bean.getPotencialSelected());

			potencial = potencialService.findPotencialById(codigoPotencial);
		}
		filter.setPotencial(potencial);

		bean.setResultList(clienteService.findClientePaiByFilter(filter));
		if (bean.getResultList().size() == 0) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		// volta para a primeira pagina da paginacao
		bean.setCurrentPageId(0);
	}

	/**
	 * Popula a lista de potenciais para combobox.
	 * 
	 * @param potenciais
	 *            lista de clientes.
	 * 
	 */
	private void loadPotencialListAndMapa(final List<Potencial> potenciais) {

		Map<String, Long> potencialMap = new HashMap<String, Long>();
		List<String> potencialList = new ArrayList<String>();

		for (Potencial potencial : potenciais) {
			potencialMap.put(potencial.getNomePotencial(),
					potencial.getCodigoPotencial());
			potencialList.add(potencial.getNomePotencial());
		}
		bean.setPotencialMap(potencialMap);
		bean.setPotencialList(potencialList);
	}

	/**
	 * Cria seguidor do clientePai.
	 */
	public void followClientePai() {
		Long codigoClientePai = bean.getCurrentRowId();
		Cliente cliente = clienteService.findClienteById(codigoClientePai);

		FollowCliente follow = new FollowCliente();
		follow.setCliente(cliente);
		follow.setCodigoLogin(LoginUtil.getLoggedUsername());

		followClienteService.createFollowCliente(follow);

		bean.setMapFollow(this.mapFollowClient(LoginUtil.getLoggedUser()));

	}

	/**
	 * Unfollow de ClientePai.
	 */
	public void unfollowClientePai() {
		Long codigoClientePai = bean.getCurrentRowId();
		Cliente cliente = clienteService.findClienteById(codigoClientePai);

		FollowCliente followCliente = followClienteService
				.findByClienteAndCodLogin(cliente,
						LoginUtil.getLoggedUsername());

		followClienteService.removeFollowCliente(followCliente);

		bean.setMapFollow(this.mapFollowClient(LoginUtil.getLoggedUser()));
	}

	/**
	 * Carrega o map de seguidores pelo login.
	 * 
	 * @param pessoa
	 *            login.
	 * @return map.
	 */
	private Map<Long, String> mapFollowClient(final Pessoa pessoa) {
		Map<Long, String> mapFollow = new HashMap<Long, String>();
		List<FollowCliente> listaFollow = followClienteService
				.findByCodigoLogin(pessoa.getCodigoLogin());

		for (FollowCliente followCliente : listaFollow) {
			mapFollow.put(followCliente.getCliente().getCodigoCliente(),
					followCliente.getCodigoLogin());
		}

		return mapFollow;
	}

	/**
	 * Acao do botao back.
	 * 
	 * @return outcome da pagina de busca
	 */
	public String back() {
		this.findByFilter();
		return OUTCOME_CLIENTE_PAI_RESEARCH;
	}

}