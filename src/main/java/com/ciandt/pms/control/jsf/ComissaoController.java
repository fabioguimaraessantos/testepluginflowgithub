package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAceleradorService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IComissaoService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IMsaSaldoMoedaService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.bean.ComissaoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Acelerador;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Comissao;
import com.ciandt.pms.model.ComissaoAcelerador;
import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaSaldoMoeda;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.ComissaoRow;
import com.ciandt.pms.util.LoginUtil;

/**
 * 
 * A classe ComissaoController proporciona as funcionalidades da camada de
 * apresentação referente as ações do modulo de comissão.
 * 
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "PER.COMMISSION:VW", "BUS.COMMISSION:CR", "BUS.COMMISSION:ED", "BUS.COMMISSION:VW" })
public class ComissaoController {

	/** Instancia do ClienteService. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do ContratoPraticaService. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do servico de comissao. */
	@Autowired
	private IComissaoService comissaoService;

	/** Instancia do servico do acelerador. */
	@Autowired
	private IAceleradorService aceleradorService;

	/** Instancia do servico de moeda. */
	@Autowired
	private IMoedaService moedaService;

	/** Instancia do servico de msa saldo moeda. */
	@Autowired
	private IMsaSaldoMoedaService msaSaldoMoedaService;

	/** Instancia do servico de pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instanca do servica de MsaService. */
	@Autowired
	private IMsaService msaService;

	/** Instancia do backbean da Comissao. */
	@Autowired
	private ComissaoBean bean;

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_ACELERADOR_CREATE = "comissaoAcelerador_add";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_ACELERADOR_UPDATE = "comissaoAcelerador_edit";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_ACELERADOR_UPDATE_DN = "comissaoAcelerador_edit_dn";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_FATURA_VIEW_DN = "comissaoFatura_view_dn";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_FATURA_UPDATE_AE = "comissaoFatura_edit_ae";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_FATURA_VIEW_AE = "comissaoFatura_view_ae";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_FATURA_UPDATE_DN = "comissaoFatura_edit_dn";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_ACELERADOR_VIEW_AE = "comissaoAcelerador_view";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_ACELERADOR_VIEW_DN = "comissaoAcelerador_view_dn";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_VIEW = "comissao_view";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_ACELERADOR_RESEARCH = "comissaoAcelerador_research";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_FATURA_RESEARCH_AE = "comissaoFatura_research_ae";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_ACELERADOR_RESEARCH_DN = "comissaoAcelerador_research_dn";

	/** outcome tela pendencias da entidade. */
	private static final String COMISSAO_RESEARCH_DP = "comissao_research_dp";

	/**
	 * Prepara a tela de criação de uma comissão de acelerador.
	 * 
	 * @return retorna a pagina de criação.
	 */
	public String prepareCreateComissaoAcelerador() {
		bean.reset();

		loadClienteList(clienteService.findClienteAllClientePai());

		loadAceleradorList(aceleradorService.findAceleradorAllActive());

		loadContratoPraticaList(contratoPraticaService
				.findContratoPraticaAllComplete());

		bean.setLoginAe(LoginUtil.getLoggedUsername());

		return COMISSAO_ACELERADOR_CREATE;
	}

	/**
	 * Prepara a tela de busca.
	 * 
	 * @return retorna a página de busca
	 */
	public String prepareSearchAe() {
		bean.reset();

		loadSearch();

		return COMISSAO_ACELERADOR_RESEARCH;
	}

	/**
	 * Prepara o search do DN.
	 * 
	 * @return retorna a página de busca.
	 */
	public String prepareSearchDn() {

		loadSearch();

		bean.getComissaoAceleradorFilter().getComissao()
				.setIndicadorEstadoAtual(Constants.COMISSAO_STATUS_REQUEST);

		return COMISSAO_ACELERADOR_RESEARCH_DN;
	}

	/**
	 * Prepara o search do DP.
	 * 
	 * @return retorna a página de busca.
	 */
	public String prepareSearchDp() {

		loadSearch();

		return COMISSAO_RESEARCH_DP;
	}

	/**
	 * Carrega os campos do formulário de busca.
	 */
	private void loadSearch() {
		bean.reset();

		Date currentDate = new Date();
		Date startDate = DateUtils.addMonths(new Date(), -1);
		bean.setDataBeg(startDate);
		bean.setDataEnd(DateUtils.addMonths(currentDate, 2));

		loadClienteList(clienteService.findClienteAllClientePai());

		loadContratoPraticaList(contratoPraticaService
				.findContratoPraticaAllComplete());

		loadAceleradorList(aceleradorService.findAceleradorAllActive());

		loadPessoaAll(pessoaService.findPessoaAll());

	}

	/**
	 * Cria uma comissão de acelerador.
	 */
	public void createComissaoAcelerador() {
		ComissaoAcelerador cAcelerador = bean.getComissaoAcelerador();

		prepareSaveOrUpdate(cAcelerador);

		Boolean success = comissaoService.createComissaoAcelerador(cAcelerador);

		if (success) {
			Messages.showSucess("createComissaoAcelerador",
					Constants.DEFAULT_MSG_SUCCESS_CREATE,
					Constants.ENTITY_NAME_COMISSAO_ACELERADOR);

			prepareCreateComissaoAcelerador();
		}
	}

	/**
	 * Prepara a para a entidade ComissaoAcelerador para ser salvo ou
	 * atualizado.
	 * 
	 * @param cAcelerador
	 *            entidade do tipo ComissaoAcelerador.
	 */
	private void prepareSaveOrUpdate(final ComissaoAcelerador cAcelerador) {

		cAcelerador.getComissao().setPessoaAe(
				pessoaService.findPessoaByLogin(bean.getLoginAe()));

		// seta o contrato-pratica
		Long codCP = bean.getContratoPraticaMap().get(
				cAcelerador.getComissao().getContratoPratica()
						.getNomeContratoPratica());
		cAcelerador.getComissao().setContratoPratica(
				contratoPraticaService.findContratoPraticaById(codCP));

		// seta a moeda
		Long codMoeda = bean.getMoedaMap().get(
				cAcelerador.getComissao().getMoeda().getNomeMoeda());

		cAcelerador.getComissao()
				.setMoeda(moedaService.findMoedaById(codMoeda));

		// seta o tipo de acelerador
		Long codAcelerador = bean.getAceleradorMap().get(
				cAcelerador.getAcelerador().getNomeAcelerador());
		cAcelerador.setAcelerador(aceleradorService
				.findAceleradorById(codAcelerador));
	}

	/**
	 * Prepara a tela de view da comissao fatura por AE.
	 * 
	 * @return retorna a pagina de view
	 */
	public String prepareViewComissaoFaturaAe() {

		ComissaoFatura cf = bean.getComissaoFatura();

		bean.setComissaoFatura(comissaoService.findComissaoFaturaById(cf
				.getCodigoComissaoFatura()));

		return COMISSAO_FATURA_VIEW_AE;
	}

	/**
	 * Prepara a tela de view da comissao fatura por DN.
	 * 
	 * @return a pagina de view
	 */
	public String prepareViewComissaoFaturaDn() {

		ComissaoFatura cf = bean.getComissaoFatura();

		bean.setComissaoFatura(comissaoService.findComissaoFaturaById(cf
				.getCodigoComissaoFatura()));

		return COMISSAO_FATURA_VIEW_DN;
	}

	/**
	 * Prepara a tela de View da comisao acelerador.
	 * 
	 * @return retorna a pagina de view.
	 */
	public String prepareViewComissaoAceleradorAe() {
		bean.setIsDelete(false);

		ComissaoAcelerador ca = bean.getComissaoAcelerador();

		bean.setComissaoAcelerador(comissaoService
				.findComissaoAceleradorById(ca.getCodigoComissaoAcel()));

		return COMISSAO_ACELERADOR_VIEW_AE;
	}

	/**
	 * Prepara a tela de View da comisao acelerador do DN.
	 * 
	 * @return retorna a pagina de view
	 */
	public String prepareViewComissaoAceleradorDn() {
		bean.setIsDelete(false);

		ComissaoAcelerador ca = bean.getComissaoAcelerador();

		bean.setComissaoAcelerador(comissaoService
				.findComissaoAceleradorById(ca.getCodigoComissaoAcel()));

		return COMISSAO_ACELERADOR_VIEW_DN;
	}

	/**
	 * Prepara a tela de View da comissao.
	 * 
	 * @return retorna a pagina de view.
	 */
	public String prepareViewComissao() {
		Comissao comissao = bean.getComissao();

		bean.setComissao(comissaoService.findComissaoById(comissao
				.getCodigoComissao()));

		return COMISSAO_VIEW;
	}

	/**
	 * Prepara a tela de remoção de um acelerador.
	 * 
	 * @return retorna a pagina de remoção.
	 */
	public String prepareDeleteComissaoAcelerador() {

		String outcomePage = prepareViewComissaoAceleradorAe();

		bean.setIsDelete(true);

		return outcomePage;
	}

	/**
	 * Prepara a tela de remoção de um acelerador.
	 * 
	 * @return retorna a pagina de remoção.
	 */
	public String prepareDeleteComissaoAceleradorDn() {

		String outcomePage = prepareViewComissaoAceleradorDn();

		bean.setIsDelete(true);

		return outcomePage;
	}

	/**
	 * Retorna para o search da fatura do AE.
	 * 
	 * @return retorna a pagina de search
	 */
	public String backToSearchFaturaAe() {
		findComissaoFaturaByFilterPerAe();

		return COMISSAO_FATURA_RESEARCH_AE;
	}

	/**
	 * Retorna para o Search do AE.
	 * 
	 * @return retorna a pagina de busca
	 */
	public String backToSearchAe() {

		findComissaoAceleradorByFilterPerAe();

		return COMISSAO_ACELERADOR_RESEARCH;
	}

	/**
	 * Retorna para o Search do AE.
	 * 
	 * @return retorna a pagina de busca
	 */
	public String backToSearchDn() {

		findComissaoByFilterPerDn();

		return COMISSAO_ACELERADOR_RESEARCH_DN;
	}

	/**
	 * Retorna para o Search do DP.
	 * 
	 * @return retorna a pagina de search
	 */
	public String backToSearchDp() {
		findComissaoAceleradorByFilterPerDp();

		return COMISSAO_RESEARCH_DP;
	}

	/**
	 * Remove um comissao de acelerador.
	 * 
	 */
	private void removeComissaoAcelerador() {
		ComissaoAcelerador comissaoAcelerador = bean.getComissaoAcelerador();

		try {
			if (comissaoService.removeComissaoAcelerador(comissaoAcelerador)) {
				// caso seja removido com sucesso, dá mensagem de sucesso
				Messages.showSucess("removeComissaoAcelerador",
						Constants.DEFAULT_MSG_SUCCESS_REMOVE,
						Constants.ENTITY_NAME_COMISSAO_ACELERADOR);
			}
			// capitura algum erro de violação de integridade que ocorra.
		} catch (DataIntegrityViolationException e) {
			Messages.showError("removeComissaoAcelerador",
					Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_COMISSAO_ACELERADOR);
		}
	}

	/**
	 * Remove uma comissao acelerador AE.
	 * 
	 * @return retorna a pagina de search
	 */
	public String removeComissaoAceleradorAe() {
		removeComissaoAcelerador();

		findComissaoAceleradorByFilterPerAe();

		return COMISSAO_ACELERADOR_RESEARCH;
	}

	/**
	 * Remove uma comissao acelerador DN.
	 * 
	 * @return retorna a pagina de search
	 */
	public String removeComissaoAceleradorDn() {
		removeComissaoAcelerador();

		findComissaoByFilterPerDn();

		return COMISSAO_ACELERADOR_RESEARCH_DN;
	}

	/**
	 * Prepara a tela de edição da comissao do acelerador.
	 */
	private void prepareUpdateComissaoAcelerador() {
		ComissaoAcelerador ca = comissaoService.findComissaoAceleradorById(bean
				.getComissaoAcelerador().getCodigoComissaoAcel());

		bean.setComissaoAcelerador(ca);

		Comissao comissao = ca.getComissao();

		Cliente cliente = comissao.getContratoPratica().getMsa().getCliente();

		bean.setNomeCli(cliente.getNomeCliente());

		loadClienteList(clienteService.findClienteAllClientePai());

		loadAceleradorList(aceleradorService.findAceleradorAllActive());

		loadContratoPraticaList(contratoPraticaService
				.findContratoPraticaAllComplete());

		loadMoedaList(msaSaldoMoedaService.findMsaSalMoeByMsaAndActive(comissao
				.getContratoPratica().getMsa()));

		bean.setLoginAe(comissao.getPessoaAe().getCodigoLogin());
	}

	/**
	 * Prepara o update da entidade Comissao Acelerador.
	 * 
	 * @return retorna a página de update.
	 */
	public String prepareUpdateComissaoAceleradorAe() {
		prepareUpdateComissaoAcelerador();

		return COMISSAO_ACELERADOR_UPDATE;
	}

	/**
	 * Prepara a tela de update de comissao do DN.
	 * 
	 * @return retorna a página de edição do dn.
	 */
	public String prepareUpdateComissaoAceleradorDn() {
		prepareUpdateComissaoAcelerador();

		return COMISSAO_ACELERADOR_UPDATE_DN;
	}

	/**
	 * realiza o update da comissao de acelerador.
	 * 
	 */
	public void updateComissaoAcelerador() {
		ComissaoAcelerador cAcelerador = bean.getComissaoAcelerador();

		Comissao comissao = cAcelerador.getComissao();
		comissao.setContratoPratica(contratoPraticaService
				.findContratoPraticaById(bean.getContratoPraticaMap().get(
						comissao.getContratoPratica().getNomeContratoPratica())));

		cAcelerador.setAcelerador(aceleradorService.findAceleradorById(bean
				.getAceleradorMap().get(
						cAcelerador.getAcelerador().getNomeAcelerador())));

		comissao.setMoeda(moedaService.findMoedaById(bean.getMoedaMap().get(
				comissao.getMoeda().getNomeMoeda())));

		comissao.setPessoaAe(pessoaService.findPessoaByLogin(bean.getLoginAe()));

		Boolean success = comissaoService.updateComissaoAcelerador(cAcelerador);

		if (success) {
			Messages.showSucess("createComissaoAcelerador",
					Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_COMISSAO_ACELERADOR);
		}

	}

	/**
	 * Realiza o update da Comissao Fatura.
	 */
	private void updateComissaoFatura() {
		ComissaoFatura cf = bean.getComissaoFatura();

		Boolean success = comissaoService.updateComissaoFatura(cf);

		if (success) {
			Messages.showSucess("updateComissaoFatura",
					Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_COMISSAO_FATURA);
		}
	}

	/**
	 * Prepara a tela de update ComissaoFatura do AE.
	 * 
	 * @return retorna a página de update
	 */
	public String prepareUpdateComissaoFaturaAe() {
		ComissaoFatura cf = bean.getComissaoFatura();

		bean.setComissaoFatura(comissaoService.findComissaoFaturaById(cf
				.getCodigoComissaoFatura()));

		return COMISSAO_FATURA_UPDATE_AE;
	}

	/**
	 * Prepara a tela de update ComissaoFatura do DN.
	 * 
	 * @return retorna a página de update
	 */
	public String prepareUpdateComissaoFaturaDn() {
		ComissaoFatura cf = bean.getComissaoFatura();

		bean.setComissaoFatura(comissaoService.findComissaoFaturaById(cf
				.getCodigoComissaoFatura()));

		return COMISSAO_FATURA_UPDATE_DN;
	}

	/**
	 * Realiza o update comissoa fatura AE.
	 * 
	 * @return retorna a página de update
	 */
	public String updateComissaoFaturaAe() {
		updateComissaoFatura();

		findComissaoFaturaByFilterPerAe();

		return COMISSAO_FATURA_RESEARCH_AE;
	}

	/**
	 * Realiza o update comissoa fatura AE.
	 * 
	 * @return retorna a página de update
	 */
	public String updateComissaoFaturaDn() {
		updateComissaoFatura();

		findComissaoByFilterPerDn();

		return COMISSAO_ACELERADOR_RESEARCH_DN;
	}

	/**
	 * Popula o combo de contrato-prática.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void populateComboContratoPratica(final ValueChangeEvent e) {
		String clientName = (String) e.getNewValue();

		Long codCli = bean.getClienteMap().get(clientName);

		if (codCli != null) {
			loadContratoPraticaList(contratoPraticaService
					.findContratoPraticaByCliente(clienteService
							.findClienteById(codCli)));

		} else {
			bean.setContratoPraticaList(new ArrayList<String>());
		}

		bean.getComissao().setContratoPratica(new ContratoPratica());
		bean.getComissaoAcelerador().getComissao()
				.setContratoPratica(new ContratoPratica());
	}

	/**
	 * Pega a moeda do contrato=pratica selecionado.
	 * 
	 * @param e
	 *            - evento de mudança.
	 */
	public void getMoeda(final ValueChangeEvent e) {
		String contratoPraticaName = (String) e.getNewValue();

		Long codCP = bean.getContratoPraticaMap().get(contratoPraticaName);

		if (codCP != null) {
			ContratoPratica cp = contratoPraticaService
					.findContratoPraticaById(codCP);

			if (cp != null) {
				loadMoedaList(msaSaldoMoedaService
						.findMsaSalMoeByMsaAndActive(cp.getMsa()));
			}
		}

		bean.setMoeda(null);
	}

	/**
	 * Ação que seta o status para "Request Approval".
	 */
	public void sendAllToApprove() {
		List<ComissaoRow> rowList = bean.getComissaoRowList();

		if (comissaoService.setAllComissaoAceleradorToApprove(rowList)) {
			// mensagem de sucesso
			Messages.showSucess("sendAllToApprove",
					Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_COMISSAO_ACELERADOR);
		} else {
			Messages.showWarning("sendAllToApprove",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Muda o status das comissões selecionadas.
	 */
	public void changeStatus() {
		List<ComissaoRow> rowList = new ArrayList<ComissaoRow>();

		rowList.addAll(bean.getComissaoRowList());
		rowList.addAll(bean.getComissaoFaturaRowList());

		Boolean hasSelected = Boolean.valueOf(false);

		// Verifica se existe algum item selecionado na lista
		for (ComissaoRow row : rowList) {
			// verifica se a linha foi selecionada
			if (row.getIsSelected()) {
				hasSelected = Boolean.valueOf(true);
				break;
			}
		}

		if (hasSelected) {
			if (comissaoService.changeStatus(rowList, bean.getSelectedStatus(),
					bean.getComments())) {

				// mensagem de sucesso
				Messages.showSucess("sendAllToApprove",
						Constants.DEFAULT_MSG_SUCCESS_UPDATE,
						Constants.ENTITY_NAME_COMISSAO);

				bean.setComments(null);
			} else {
				this.findComissaoFaturaByFilterPerAe();
			}
		} else {
			Messages.showWarning("changeStatus",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Altera o status da comissao fatura.
	 */
	public void changeStatusComissaoFatura() {
		List<ComissaoRow> rowList = bean.getComissaoFaturaRowList();

		if (comissaoService.changeStatus(rowList, bean.getSelectedStatus(),
				bean.getComments())) {

			// mensagem de sucesso
			Messages.showSucess("changeStatusComissaoFatura",
					Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_COMISSAO_ACELERADOR);
		} else {
			Messages.showWarning("changeStatusComissaoFatura",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Altera o status da comissão AE.
	 */
	public void changeStatusAe() {
		findComissaoAceleradorByFilterPerAe();
	}

	/**
	 * Altera o status da comissão DN.
	 */
	public void changeStatusDn() {
		findComissaoByFilterPerDn();
	}

	/**
	 * Ação que realiza a busca pelo filtro das ComissaoAcelerador.
	 */
	public void findComissaoAceleradorByFilterPerAe() {
		Comissao comissao = bean.getComissaoAceleradorFilter().getComissao();

		// seta o contrato-pratica
		Long codCP = bean.getContratoPraticaMap().get(
				comissao.getContratoPratica().getNomeContratoPratica());
		ContratoPratica cp = null;
		if (codCP != null) {
			cp = contratoPraticaService.findContratoPraticaById(codCP);
		}

		// seta o tipo de acelerador
		Long codAcelerador = bean.getAceleradorMap().get(
				bean.getComissaoAceleradorFilter().getAcelerador()
						.getNomeAcelerador());
		Acelerador a = null;
		if (codAcelerador != null) {
			a = aceleradorService.findAceleradorById(codAcelerador);
		}

		Long codCli = bean.getClienteMap().get(bean.getNomeCliente());
		Cliente c = null;
		if (codCli != null) {
			c = clienteService.findClienteById(codCli);
		}

		// Setta o AE
		Pessoa ae = LoginUtil.getLoggedUser();
		if (LoginUtil.isCurrentUserInRole("BUS.COMMISSION:ED")) {
			ae = null;
			if (bean.getLoginAeFilter() != null
					&& !bean.getLoginAeFilter().isEmpty()) {
				// Verifica se o usuário do campo login existe no combo
				if (bean.getMapPessoas().containsKey(bean.getLoginAeFilter())) {
					ae = pessoaService.findPessoaByLogin(bean.getMapPessoas()
							.get(bean.getLoginAeFilter()));
				} else {
					Messages.showError("cdLogin",
							Constants.MSG_ERRO_LOGIN_INVALID_USER_PASS,
							Constants.ENTITY_NAME_COMISSAO);
					return;
				}
			}
		}

		// Setta o DN
		Pessoa dn = null;
		if (LoginUtil.isCurrentUserInRole("BUS.COMMISSION:ED")) {
			if (bean.getLoginDnFilter() != null
					&& !bean.getLoginDnFilter().isEmpty()) {
				// Verifica se o usuário do campo login existe no combo
				if (bean.getMapPessoas().containsKey(bean.getLoginDnFilter())) {
					dn = pessoaService.findPessoaByLogin(bean.getMapPessoas()
							.get(bean.getLoginDnFilter()));
				} else {
					Messages.showError("cdLogin",
							Constants.MSG_ERRO_LOGIN_INVALID_USER_PASS,
							Constants.ENTITY_NAME_COMISSAO);
					return;
				}
			}
		}

		List<ComissaoRow> resultList = comissaoService
				.findComissaoAceleradorByFilterPerAe(bean.getDataBeg(),
						bean.getDataEnd(), c, cp, a, ae, dn,
						comissao.getIndicadorEstadoAtual());

		bean.setComissaoRowList(resultList);

		if (resultList.isEmpty()) {
			Messages.showWarning("findComissaoAceleradorByFilterPerAe",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

	}

	/**
	 * Prepara a tela de busca por EA.
	 * 
	 * @return retorna a tela de busca por AE.
	 */
	public String prepareSearchComissaoFaturaPerAe() {
		bean.reset();

		this.loadMsaCombo(msaService.findMsaAll());

		Date currentDate = new Date();
		Date startDate = DateUtils.addMonths(new Date(), -1);
		bean.setDataBeg(startDate);
		bean.setDataEnd(DateUtils.addMonths(currentDate, 2));

		return COMISSAO_FATURA_RESEARCH_AE;
	}

	/**
	 * Ação que busca comissão fatura pelo filtro e por AE logado.
	 */
	public void findComissaoFaturaByFilterPerAe() {

		// Seta null para trazer todos, no caso do usuario corrente ser um
		// ADMIN
		String loginAe = LoginUtil.getLoggedUsername();
		if (LoginUtil.isCurrentUserInRole("BUS.COMMISSION:ED")) {
			loginAe = null;

			if (bean.getLoginAeFilter() != null
					&& !bean.getLoginAeFilter().isEmpty()) {
				// Verifica se o usuário do campo login existe no combo
				if (bean.getMapPessoas().containsKey(bean.getLoginAeFilter())) {
					loginAe = bean.getMapPessoas().get(bean.getLoginAeFilter());
				} else {
					Messages.showError("cdLogin",
							Constants.MSG_ERRO_LOGIN_INVALID_USER_PASS,
							Constants.ENTITY_NAME_COMISSAO);
					return;
				}
			}
		}

		String loginDn = null;
		if (LoginUtil.isCurrentUserInRole("BUS.COMMISSION:ED")) {
			if (bean.getLoginDnFilter() != null
					&& !bean.getLoginDnFilter().isEmpty()) {
				// Verifica se o usuário do campo login existe no combo
				if (bean.getMapPessoas().containsKey(bean.getLoginDnFilter())) {
					loginDn = bean.getMapPessoas().get(bean.getLoginDnFilter());
				} else {
					Messages.showError("cdLogin",
							Constants.MSG_ERRO_LOGIN_INVALID_USER_PASS,
							Constants.ENTITY_NAME_COMISSAO);
					return;
				}
			}
		}

		// Busca o msa do filtro
		Msa msa = null;
		Long codigoMsa = bean.getMapMsa().get(bean.getNameMsa());
		if (codigoMsa != null) {
			msa = msaService.findMsaById(codigoMsa);
		}

		// Monta lista para a tela
		List<ComissaoRow> resultList = comissaoService
				.findComissaoFaturaByFilterAe(bean.getDataBeg(),
						bean.getDataEnd(), bean.getComissaoAceleradorFilter()
								.getComissao().getIndicadorEstadoAtual(),
						loginAe, loginDn, msa, bean.getCodeInvoice());

		this.comissaoRowHibernateInitialize(resultList);

		bean.setComissaoFaturaRowList(resultList);

		if (resultList.isEmpty()) {
			Messages.showWarning("findComissaoFaturaByFilterPerAe",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
	}

	/**
	 * Ação que realiza a busca pelo filtro das ComissaoAcelerador.
	 */
	public void findComissaoByFilterPerDn() {
		// seta o contrato-pratica
		Long codCP = bean.getContratoPraticaMap().get(
				bean.getComissaoAceleradorFilter().getComissao()
						.getContratoPratica().getNomeContratoPratica());

		ContratoPratica cp = null;
		if (codCP != null) {
			cp = contratoPraticaService.findContratoPraticaById(codCP);
		}

		// seta o tipo de acelerador
		Long codAcelerador = bean.getAceleradorMap().get(
				bean.getComissaoAceleradorFilter().getAcelerador()
						.getNomeAcelerador());
		Acelerador a = null;
		if (codAcelerador != null) {
			a = aceleradorService.findAceleradorById(codAcelerador);
		}

		Long codCli = bean.getClienteMap().get(bean.getNomeCliente());
		Cliente c = null;
		if (codCli != null) {
			c = clienteService.findClienteById(codCli);
		}

		String indicadorEstadoAtual = bean.getComissaoAceleradorFilter()
				.getComissao().getIndicadorEstadoAtual();

		Pessoa ae = pessoaService.findPessoaByLogin(bean.getMapPessoas().get(
				bean.getLoginAeFilter()));
		if (bean.getLoginAeFilter() != null
				&& !bean.getLoginAeFilter().isEmpty()) {

			// Verifica se o usuário do campo login existe no combo
			if (!bean.getMapPessoas().containsKey(bean.getLoginAeFilter())) {
				Messages.showError("cdLogin",
						Constants.MSG_ERRO_LOGIN_INVALID_USER_PASS,
						Constants.ENTITY_NAME_COMISSAO);
				return;
			}
		}

		Pessoa dn = LoginUtil.getLoggedUser();
		// Checa se o usuario corrente é o ADMIN
		if (LoginUtil.isCurrentUserInRole("BUS.COMMISSION:ED")) {
			dn = null;

			if (bean.getLoginDnFilter() != null
					&& !bean.getLoginDnFilter().isEmpty()) {
				// Verifica se o usuário do campo login existe no combo
				if (bean.getMapPessoas().containsKey(bean.getLoginDnFilter())) {
					dn = pessoaService.findPessoaByLogin(bean.getMapPessoas()
							.get(bean.getLoginDnFilter()));
				} else {
					Messages.showError("cdLogin",
							Constants.MSG_ERRO_LOGIN_INVALID_USER_PASS,
							Constants.ENTITY_NAME_COMISSAO);
					return;
				}
			}
		}

		// busca as comissões de aceleradores
		List<ComissaoRow> resultList = comissaoService
				.findComissaoAceleradorByFilterPerDn(bean.getDataBeg(),
						bean.getDataEnd(), c, cp, a, ae, indicadorEstadoAtual,
						dn);
		bean.setComissaoRowList(resultList);

		// busca as comissões de invoices
		List<ComissaoRow> comissaoFaturaList = comissaoService
				.findComissaoFaturaByFilterDn(bean.getDataBeg(),
						bean.getDataEnd(), c, cp, ae, indicadorEstadoAtual, dn);

		this.comissaoRowHibernateInitialize(comissaoFaturaList);

		bean.setComissaoFaturaRowList(comissaoFaturaList);

		if (resultList.isEmpty() && comissaoFaturaList.isEmpty()) {
			Messages.showWarning("findComissaoAceleradorByFilterPerDn",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

	}

	/**
	 * Ação que realiza a busca pelo filtro das ComissaoAcelerador.
	 */
	public void findComissaoAceleradorByFilterPerDp() {
		Pessoa ae = null;
		if (bean.getLoginAe() != null && !"".equals(bean.getLoginAe())) {
			ae = pessoaService.findPessoaByLogin(bean.getLoginAe());
			if (ae == null) {
				ae = new Pessoa();
				ae.setCodigoPessoa(-1L);
			}
		}

		List<Comissao> resultList = comissaoService.findComissaoByFilterDp(
				bean.getDataBeg(), bean.getDataEnd(), ae, bean
						.getComissaoAceleradorFilter().getComissao()
						.getIndicadorEstadoAtual());

		List<ComissaoRow> rowList = new ArrayList<ComissaoRow>();
		ComissaoRow row;
		BigDecimal total = BigDecimal.ZERO;
		for (Comissao comissao : resultList) {
			row = new ComissaoRow();
			row.setComissao(comissao);
			row.setConvertedComissionValue(comissaoService
					.convertComissaoValueToDefaulCurrency(comissao));
			rowList.add(row);

			total = total.add(row.getConvertedComissionValue());
		}
		bean.setComissaoRowList(rowList);
		bean.setTotal(total);

		if (resultList.isEmpty()) {
			Messages.showWarning("findComissaoAceleradorByFilterPerDp",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

	}

	/**
	 * Ação utilizada no autocomplete da Pessoa. Retorna uma lista de Pessoas.
	 * 
	 * @param value
	 *            - valor (login) utilizado na busca das Pessoas
	 * 
	 * @return retorna uma lista de recurso
	 */
	public List<Pessoa> autoCompletePessoa(final Object value) {
		return pessoaService.findPessoaByLikeLogin((String) value);
	}

	/**
	 * Realiza a validaçao do campo Login.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validatePessoa(final FacesContext context,
			final UIComponent component, final Object value) {

		String login = (String) value;

		if ((login != null) && (!"".equals(login))) {
			Pessoa pessoa = pessoaService.findPessoaByLogin(login);

			if (pessoa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}

	}

	/**
	 * Realiza o update do percentual da comissao de acelerador.
	 */
	public void updatePercentualComissao() {
		Comissao comissao = bean.getComissao();
		BigDecimal valorTotal;
		boolean isComissaoAcelerador = Constants.COMISSAO_TYPE_ACCELERATOR
				.equals(comissao.getIndicadorTipo());
		if (isComissaoAcelerador) {
			valorTotal = bean.getSelectedRow().getNetValue();
		} else {
			valorTotal = bean.getComissaoFatura().getValorItemFatura();
		}

		comissaoService.updatePercentualComissao(comissao, valorTotal);

		ComissaoRow row = bean.getSelectedRow();
		row.setConvertedComissionValue(comissaoService
				.convertComissaoValueToDefaulCurrency(comissao));
	}

	/**
	 * Popula a lista de clientes para combobox.
	 * 
	 * @param aceleradores
	 *            lista de clientes.
	 * 
	 */
	private void loadAceleradorList(final List<Acelerador> aceleradores) {

		Map<String, Long> aceleradorMap = new HashMap<String, Long>();
		List<String> aceleradorList = new ArrayList<String>();

		for (Acelerador acelerador : aceleradores) {
			aceleradorMap.put(acelerador.getNomeAcelerador(),
					acelerador.getCodigoAcelerador());
			aceleradorList.add(acelerador.getNomeAcelerador());
		}
		bean.setAceleradorMap(aceleradorMap);
		bean.setAceleradorList(aceleradorList);
	}

	/**
	 * Popula a lista de clientes para combobox.
	 * 
	 * @param clientes
	 *            lista de clientes.
	 * 
	 */
	private void loadClienteList(final List<Cliente> clientes) {

		Map<String, Long> clienteMap = new HashMap<String, Long>();
		List<String> clienteList = new ArrayList<String>();

		for (Cliente cliente : clientes) {
			clienteMap
					.put(cliente.getNomeCliente(), cliente.getCodigoCliente());
			clienteList.add(cliente.getNomeCliente());
		}
		bean.setClienteMap(clienteMap);
		bean.setClienteList(clienteList);
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
	 * Popula a lista de moedas para combobox.
	 * 
	 * @param msaSaldoMoedas
	 *            lista de moedas.
	 * 
	 */
	private void loadMoedaList(final List<MsaSaldoMoeda> msaSaldoMoedas) {

		Map<String, Long> moedaMap = new HashMap<String, Long>();
		List<String> moedaList = new ArrayList<String>();

		for (MsaSaldoMoeda msaSaldoMoeda : msaSaldoMoedas) {
			moedaMap.put(msaSaldoMoeda.getMoeda().getNomeMoeda(), msaSaldoMoeda
					.getMoeda().getCodigoMoeda());
			moedaList.add(msaSaldoMoeda.getMoeda().getNomeMoeda());
		}

		bean.setMoedaMap(moedaMap);
		bean.setMoedaList(moedaList);
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final ComissaoBean bean) {
		this.bean = bean;
	}

	/**
	 * @return the bean
	 */
	public ComissaoBean getBean() {
		return bean;
	}

	/**
	 * Lista todas as {@link Pessoa} que se encontram no banco para ser
	 * carregado no combo.
	 * 
	 * @param pessoas
	 *            lista de pessoas
	 */
	public void loadPessoaAll(final List<Pessoa> pessoas) {
		Map<String, String> mapPessoas = new HashMap<String, String>();
		List<String> listaPessoas = new ArrayList<String>();
		String key;

		for (Pessoa pessoa : pessoas) {
			// key = pessoa.getCodigoLogin() + " : " + pessoa.getNomePessoa();
			key = pessoa.getCodigoLogin();
			mapPessoas.put(key, pessoa.getCodigoLogin());
			listaPessoas.add(key);
		}

		bean.setMapPessoas(mapPessoas);
		bean.setListaPessoas(listaPessoas);
	}

	/**
	 * Inicializa os objetos sujeitos a Lazy do objeto {@link ComissaoRow}.
	 * 
	 * @param comissoesRow
	 *            the list of {@link ComissaoRow}
	 */
	private void comissaoRowHibernateInitialize(
			final List<ComissaoRow> comissoesRow) {
		for (ComissaoRow comissaoRow : comissoesRow) {
			Hibernate.initialize(comissaoRow.getComissaoFatura()
					.getItemFatura().getFatura().getDealFiscal());
			Hibernate.initialize(comissaoRow.getComissaoFatura()
					.getItemFatura().getFatura().getDealFiscal().getMsa());
			Hibernate.initialize(comissaoRow.getComissaoFatura()
					.getItemFatura().getFatura().getDealFiscal().getCliente());
			Hibernate.initialize(comissaoRow.getComissaoFatura()
					.getItemFatura().getFatura().getDealFiscal().getMoeda());
		}
	}

	/**
	 * AutoComplete do campo de Msa da tela de busca.
	 * 
	 * @param value
	 *            faces
	 * @return lista de Msa.
	 */
	public List<Msa> autoCompleteMsa(final Object value) {
		return msaService.findMsabyNameQuick(value.toString());
	}

	/**
	 * Valida o nome do Msa digitado no filtro de busca.
	 * 
	 * @param context
	 *            faces
	 * @param component
	 *            faces
	 * @param value
	 *            faces
	 */
	public void validadeMsa(final FacesContext context,
			final UIComponent component, final Object value) {

		String nameMsa = (String) value;

		if ((nameMsa != null) && !"".equals(nameMsa)) {
			Long codigoMsa = bean.getMapMsa().get(nameMsa);

			if (codigoMsa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));

			}
		}

	}

	/**
	 * Carrega Map e Lista de MSA para a combobox da tela de search.
	 */
	private void loadMsaCombo(final List<Msa> listaMsa) {
		Map<String, Long> map = new HashMap<String, Long>();
		List<String> list = new ArrayList<String>();

		for (Msa msa : listaMsa) {
			map.put(msa.getNomeMsa(), msa.getCodigoMsa());
			list.add(msa.getNomeMsa());
		}

		bean.setMapMsa(map);
		bean.setListaMsa(list);
	}

}