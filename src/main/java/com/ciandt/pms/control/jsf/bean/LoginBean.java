package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.*;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.HashGeneratorUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.RememberLoginUtil;
import org.ajax4jsf.webapp.FilterServletResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Define o BackingBean das funcionalidades de login.
 * 
 * @since 29/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class LoginBean implements Serializable {

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;

	/** outcome tela de profile. */
	private static final String OUTCOME_PROFILE = "profile";

	/** outcome tela de login. */
	private static final String OUTCOME_LOGIN = "login";

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;
	/** Arquivo de configuracoes. */

	/** Instancia do servico de pessoa. */
	@Autowired
	private IPessoaService pessoaService;
	
	/** Instancia do servico de CidadeBase. */
	@Autowired
	private ICidadeBaseService cidadeBaseService;

	/** Instancia do servico de pessoaDelegacao. */
	@Autowired
	private IPessoaDelegacaoService pessoaDelegacaoService;

	/** Instancia do servico de pessoaGrupoCusto. */
	@Autowired
	private IPessoaGrupoCustoService pessoaGrupoCustoService;

	/** Instancia do servico de pessoaCidadeBase. */
	@Autowired
	private IPessoaCidadeBaseService pessoaCidadeBaseService;

	/** Instancia do servico de pessoaTipoContrato. */
	@Autowired
	private IPessoaTipoContratoService pessoaTipoContratoService;

	/** Login do name. */
	private String name = "";

	/** Login do usuario. */
	private String username = "";

	/** Senha do usuario. */
	private String password = "";

	/** Indica se o cookie ja foi verificado. */
	private Boolean cookieVerificado = Boolean.valueOf(false);

	/** Indica se o cookie deve ser gravado. */
	private Boolean staySignedIn = Boolean.valueOf(false);

	/** Pessoa autenticada. */
	private Pessoa pessoa;

	/** Pessoa delegada. */
	private Pessoa pessoaDelegada;

	/** Grupo Custo da Pessoa autenticada. */
	private String nomeGrupoCusto;

	/** Cidade Base da Pessoa autenticada. */
	private String nomeCidadeBase;

	/** Tipo Contrato da Pessoa autenticada. */
	private String nomeTipoContrato;

	/** Lista de Gerentes de Pessoas. */
	private List<Pessoa> peopleManagers;

	/** Lista de peopleManager para combobox. */
	private List<String> peopleManagerList;

	/** Lista de peopleManager para combobox. */
	private Map<String, Long> peopleManagerMap;

	/** Objeto da delegacao. */
	private PessoaDelegacao pessoaDelegacao = new PessoaDelegacao();

	/** Indicador se os recursos estao delegados. */
	private Boolean isDelegate = false;

	/**
	 * Método que pega o username e senha redireciona para o serviço de
	 * autenticação.
	 * 
	 * @throws IOException
	 *             exception de entrada e saida.
	 * @throws ServletException
	 *             exception do servlet
	 * 
	 * @return retorna null, pois terminou a requisição.
	 */
	public String doLogin() throws IOException, ServletException {

		Map<String, Object> sessionMap = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		// seta a pagina que o usuario sera rediricionado após a autenticação
		sessionMap.put("REDIRECT_INDEX_URL",
				systemProperties.getProperty("home.page.url"));

		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();

		RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
				.getRequestDispatcher("/j_spring_security_check");

		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpSession session = request.getSession();

		if (maxLoginAttemptsReached(session)) {
			Messages.showError("beforePhase", Constants.MAX_LOGIN_ATTEMPTS_ERROR_MESSAGE);
			HttpServletResponse response = ((HttpServletResponse) context.getResponse());

			response.setStatus(406);

			return null;
		}

		dispatcher.forward((ServletRequest) context.getRequest(),
				(ServletResponse) context.getResponse());

		String redirect = ((FilterServletResponseWrapper) context.getResponse()).getRedirectLocation();
		int tempoCookie = 3600;

		// Verifica se a flag de remember foi selecionada
		if (this.staySignedIn) {
			// Grava o username, a senha e a flag de remember por 1 dia
			RememberLoginUtil.remember(
					this.username,
					this.password,
					this.staySignedIn.toString(),
					"/",
					Integer.valueOf(Integer.valueOf(systemProperties
							.getProperty("tempo.cookie")) * tempoCookie));
		} else {
			// Grava somemte a flag de remember por dois dias
			RememberLoginUtil.setRememberMe(
					this.staySignedIn.toString(),
					"/",
					Integer.valueOf(Integer.valueOf(systemProperties
							.getProperty("tempo.cookie")) * tempoCookie));
		}

		FacesContext facesContext = FacesContext.getCurrentInstance();

		facesContext.responseComplete();

		// Marca a flag de cookie verificado como falsa e ativa o botao de
		// verificacao de cookies
		this.cookieVerificado = Boolean.valueOf(false);

		if (redirect.endsWith("login.jsf?failure=true")) {
			return null;
		}

		this.preparePessoa();

		return null;
	}

	public boolean maxLoginAttemptsReached(HttpSession session) {
		int maxAttempts = 5;
		Integer attempts = (Integer) session.getAttribute(Constants.LOGIN_ATTEMPTS);
		attempts = attempts != null ? attempts : 1;
		if (attempts > maxAttempts) {
			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime sessionTime = (LocalDateTime) session.getAttribute(Constants.WAIT_ATTEMPT_TIME);
			if (sessionTime != null && currentTime.isAfter(sessionTime)) {
				session.removeAttribute(Constants.WAIT_ATTEMPT_TIME);
				session.setAttribute(Constants.LOGIN_ATTEMPTS, 1);
			} else if (sessionTime == null) {
				session.setAttribute(Constants.WAIT_ATTEMPT_TIME, currentTime.plusMinutes(3));
			}
			return true;
		} else {
			session.setAttribute(Constants.LOGIN_ATTEMPTS, attempts + 1);
			return false;
		}
	}

	/**
	 * Método que redireciona para o serviço de logout.
	 * 
	 * @throws IOException
	 *             exception de entrada e saida.
	 */
	public void doLogout() throws IOException {
		HttpServletResponse response = (HttpServletResponse) FacesContext
				.getCurrentInstance().getExternalContext().getResponse();

		response.sendRedirect("/pms/j_spring_security_logout");
	}

	/**
	 * Método que redireciona para a tela de login.
	 * 
	 * @return the outcome
	 */
	public String sign() {
		return OUTCOME_LOGIN;
	}

	/**
	 * Verifica se o cookie contem os valores de login e senha e autentica o
	 * usuario.
	 * 
	 * @throws IOException
	 *             - IOException
	 * @throws ServletException
	 *             - ServletException
	 */
	public void init() throws IOException, ServletException {

		if (RememberLoginUtil.remembered()) {
			this.username = RememberLoginUtil.getUsername();
			this.password = RememberLoginUtil.getPassword();
			FacesContext.getCurrentInstance().responseComplete();
			this.doLogin();
		}
		// Marca a flag de cookie verificado como verdadeira e desativa o botao
		// de verificacao de cookies
		this.cookieVerificado = Boolean.valueOf(true);
	}

	/**
	 * Verifica se existe o cookie com usuario e senha e carrega os campos do
	 * formulario antes da construcao da tela.
	 * 
	 * @throws IOException
	 *             - IOException
	 * @throws ServletException
	 *             - ServletException
	 */
	@PostConstruct
	public void carregarCookie() throws IOException, ServletException {
		if (RememberLoginUtil.remembered()) {
			this.username = RememberLoginUtil.getUsername();
			this.password = RememberLoginUtil.getPassword();
			FacesContext.getCurrentInstance().responseComplete();
		}
		this.staySignedIn = RememberLoginUtil.getRememberMe();
	}

	/**
	 * Prepara a pessoa para exibicao dos dados na tela de My Profile.
	 * 
	 * @return pagina de destino
	 */
	public String preparePessoa() {
		Pessoa loggedUser = pessoaService.findPessoaByLogin(this.username);

		if (loggedUser == null || loggedUser.getCodigoPessoa() == null) {
			return null;
		}

		PessoaDelegacao pessDelegacao = pessoaDelegacaoService
				.findPessoaDelegacaoByPessoaAndFinalDate(loggedUser, new Date());

		this.peopleManagers = pessoaService.findAllManager();

		this.pessoaDelegada = new Pessoa();
		this.pessoaDelegacao = new PessoaDelegacao();

		this.loadPeopleManagerList(peopleManagers);

		if (pessDelegacao != null) {
			loggedUser.setIndicadorDelegateOn(Constants.YES);
			this.pessoaDelegacao = pessDelegacao;
			this.pessoaDelegada = pessoaService
					.findPessoaById(this.pessoaDelegacao.getPessoaDelegada()
							.getCodigoPessoa());
			Date dataAtual = new Date();
			if ((dataAtual.after(this.pessoaDelegacao.getDataInicio()) || dataAtual
					.equals(this.pessoaDelegacao.getDataInicio()))
					&& (this.pessoaDelegacao.getDataFim() == null
							|| dataAtual.before(this.pessoaDelegacao
									.getDataFim()) || dataAtual
								.equals(this.pessoaDelegacao.getDataFim()))) {
				this.isDelegate = Boolean.valueOf(true);
			}
		} else {
			loggedUser.setIndicadorDelegateOn(Constants.NO);
			this.isDelegate = Boolean.valueOf(false);
		}

		if (loggedUser.getIndicadorFollowOn() == null) {
			loggedUser.setIndicadorFollowOn(Constants.NO);
		}

		this.setPessoa(loggedUser);

		// verifica a associacao do GrupoCusto, TipoContrato e Salario para
		// exibir os valores corretamente conforme vigencia (data atual)
		Date currentDate = DateUtil.getDate(new Date());
		PessoaGrupoCusto pessoaGC = pessoaGrupoCustoService
				.findPessGCByPessoaAndDate(loggedUser, currentDate);
		CidadeBase cidadeBase = cidadeBaseService
				.findCurrentCidadeBaseByPessoa(pessoa);
		PessoaTipoContrato pessoaTC = pessoaTipoContratoService
				.findPessTCByPessoaAndDate(loggedUser, currentDate);

		if (pessoaGC != null) {
			this.setNomeGrupoCusto(pessoaGC.getGrupoCusto().getNomeGrupoCusto());
		}

		if (cidadeBase != null) {
			this.setNomeCidadeBase(cidadeBase.getNomeCidadeBase());			
		}

		if (pessoaTC != null) {
			this.setNomeTipoContrato(pessoaTC.getTipoContrato()
					.getNomeTipoContrato());
		}

		return (OUTCOME_PROFILE);

	}

	/**
	 * Popula a lista de PeopleManager para combobox.
	 * 
	 * @param peopleManagers
	 *            lista de PeopleManager.
	 * 
	 */
	private void loadPeopleManagerList(final List<Pessoa> peopleManagers) {

		Map<String, Long> peopleMngrMap = new HashMap<String, Long>();
		List<String> peopleMngrList = new ArrayList<String>();

		for (Pessoa peopleManager : peopleManagers) {
			peopleMngrMap.put(peopleManager.getCodigoLogin(),
					peopleManager.getCodigoPessoa());
			peopleMngrList.add(peopleManager.getCodigoLogin());
		}

		this.setPeopleManagerMap(peopleMngrMap);
		this.setPeopleManagerList(peopleMngrList);
	}

	/**
	 * Evento que seta a lista de empresas.
	 * 
	 * @param e
	 *            - evento
	 */
	public void changeValuePeopleManager(final ValueChangeEvent e) {

		String login = (String) e.getNewValue();

		this.getPessoaDelegada().setCodigoLogin(login);
	}

	/**
	 * Desabilita a delegacao de recursos.
	 * @throws Exception 
	 */
	public void updateDelegateOff() {
		PessoaDelegacao pessDelegacao = new PessoaDelegacao();

		pessDelegacao = pessoaDelegacaoService
				.findPessoaDelegacaoByPessoaAndDate(pessoa, new Date());

		if (pessDelegacao != null) {
			Date dataOff = DateUtil.sumDays(new Long(-1), new Date());
			pessDelegacao.setDataFim(dataOff);
			pessoaDelegacaoService.updatePessoaDelegacao(pessDelegacao);
		} else {
			pessDelegacao = pessoaDelegacaoService
					.findPessoaDelegacaoByPessoaAndFinalDate(pessoa, new Date());
			if (pessDelegacao != null) {
				pessoaDelegacaoService.removePessoaDelegacao(pessDelegacao);
			}
		}
		this.pessoaDelegada = new Pessoa();
		this.pessoaDelegacao = new PessoaDelegacao();
		this.isDelegate = Boolean.valueOf(false);
		Messages.showSucess("updateDelegateOff",
				Constants.MSG_SUCCESS_DELEGATE_OFF, Constants.MY_PROFILE);
	}

	/**
	 * Habilita a delegacao de recursos.
	 */
	public void updateDelegateOn() {
		PessoaDelegacao pessDelegacao = pessoaDelegacaoService
				.findPessoaDelegacaoByPessoaAndFinalDate(pessoa, new Date());

		Boolean update = Boolean.valueOf(false);

		String message = this.validaDelegacao();

		if (message.equals("")) {

			if (pessDelegacao != null) {
				update = Boolean.valueOf(true);
			} else {
				pessDelegacao = new PessoaDelegacao();
			}

			pessDelegacao.setPessoa(pessoa);
			Long codigoDelegada = this.getPeopleManagerMap().get(
					this.pessoaDelegada.getCodigoLogin());
			pessDelegacao.setPessoaDelegada(pessoaService
					.findPessoaById(codigoDelegada));
			pessDelegacao.setDataInicio(this.pessoaDelegacao.getDataInicio());
			pessDelegacao.setDataFim(this.pessoaDelegacao.getDataFim());

			if (update) {
				pessoaDelegacaoService.updatePessoaDelegacao(pessDelegacao);
			} else {
				pessoaDelegacaoService.createPessoaDelegacao(pessDelegacao);
			}
			Date dataAtual = new Date();
			if ((dataAtual.after(this.pessoaDelegacao.getDataInicio()) || dataAtual
					.equals(this.pessoaDelegacao.getDataInicio()))
					&& (this.pessoaDelegacao.getDataFim() == null
							|| dataAtual.before(this.pessoaDelegacao
									.getDataFim()) || dataAtual
								.equals(this.pessoaDelegacao.getDataFim()))) {
				this.isDelegate = Boolean.valueOf(true);
			}
			Messages.showSucess("updateMyProfile",
					Constants.MSG_SUCCESS_DELEGATE_ON, Constants.MY_PROFILE);
		} else {
			Messages.showError("updateMyProfile", message);
		}
	}

	/**
	 * Valida as datas de delegacao.
	 * 
	 * @return true se delegacao permitida
	 */
	private String validaDelegacao() {
		if (this.pessoaDelegacao.getDataFim() == null) {
			return "";
		}
		if (this.pessoaDelegacao.getDataInicio().after(
				this.pessoaDelegacao.getDataFim())) {
			return Constants.DEFAULT_MSG_ERROR_DATE_BEG_GT_DATE_END;
		} else if (this.pessoaDelegacao.getDataFim().before(new Date())) {
			return Constants.DEFAULT_MSG_ERROR_CURRENT_DATE_GT_END_DATE;
		}

		return "";
	}

	/**
	 * Atualiza o People Follow do My Profile.
	 */
	public void updatePeopleFollow() {
		Pessoa loggedUser = LoginUtil.getLoggedUser();

		String indicadorFollowOn = this.pessoa.getIndicadorFollowOn();
		loggedUser.setIndicadorFollowOn(indicadorFollowOn);

		// se for 'Y', cadastra o codigo MD5. Caso contrario, cadastra null
		if (Constants.YES.equals(indicadorFollowOn)) {
			// time atual que vai concatenar com o login do usuário logado
			String currentTime = String.valueOf((new Date()).getTime());
			// gera o código hash MD5 para ser usado no unsubscribe para
			// garantir
			// que nao haverá códigos hash iguais na base e garantir que somente
			// o
			// próprio usuário pode fazer o UnFollow
			loggedUser.setCodigoMD5(HashGeneratorUtil
					.generateAlphanumericHash(currentTime
							+ loggedUser.getCodigoLogin()));
		} else {
			loggedUser.setCodigoMD5(null);
		}

		pessoaService.updatePessoa(loggedUser);

		Messages.showSucess("updateMyProfile",
				Constants.DEFAULT_MSG_SUCCESS_UPDATE, Constants.MY_PROFILE);
	}

	/**
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the nomeGrupoCusto
	 */
	public String getNomeGrupoCusto() {
		return nomeGrupoCusto;
	}

	/**
	 * @param nomeGrupoCusto
	 *            the nomeGrupoCusto to set
	 */
	public void setNomeGrupoCusto(final String nomeGrupoCusto) {
		this.nomeGrupoCusto = nomeGrupoCusto;
	}

	/**
	 * @return the nomeCidadeBase
	 */
	public String getNomeCidadeBase() {
		return nomeCidadeBase;
	}

	/**
	 * @param nomeCidadeBase
	 *            the nomeCidadeBase to set
	 */
	public void setNomeCidadeBase(final String nomeCidadeBase) {
		this.nomeCidadeBase = nomeCidadeBase;
	}

	/**
	 * @return the nomeTipoContrato
	 */
	public String getNomeTipoContrato() {
		return nomeTipoContrato;
	}

	/**
	 * @param nomeTipoContrato
	 *            the nomeTipoContrato to set
	 */
	public void setNomeTipoContrato(final String nomeTipoContrato) {
		this.nomeTipoContrato = nomeTipoContrato;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the cookieVerificado
	 */
	public Boolean getCookieVerificado() {
		return cookieVerificado;
	}

	/**
	 * @param cookieVerificado
	 *            the cookieVerificado to set
	 */
	public void setCookieVerificado(final Boolean cookieVerificado) {
		this.cookieVerificado = cookieVerificado;
	}

	/**
	 * @return the staySignedIn
	 */
	public Boolean getStaySignedIn() {
		return staySignedIn;
	}

	/**
	 * @param staySignedIn
	 *            the staySignedIn to set
	 */
	public void setStaySignedIn(final Boolean staySignedIn) {
		this.staySignedIn = staySignedIn;
	}

	/**
	 * @return the peopleManagers
	 */
	public List<Pessoa> getPeopleManagers() {
		return peopleManagers;
	}

	/**
	 * @param peopleManagers
	 *            the peopleManagers to set
	 */
	public void setPeopleManagers(final List<Pessoa> peopleManagers) {
		this.peopleManagers = peopleManagers;
	}

	/**
	 * @return the peopleManagerList
	 */
	public List<String> getPeopleManagerList() {
		return peopleManagerList;
	}

	/**
	 * @param peopleManagerList
	 *            the peopleManagerList to set
	 */
	public void setPeopleManagerList(final List<String> peopleManagerList) {
		this.peopleManagerList = peopleManagerList;
	}

	/**
	 * @return the peopleManagerMap
	 */
	public Map<String, Long> getPeopleManagerMap() {
		return peopleManagerMap;
	}

	/**
	 * @param peopleManagerMap
	 *            the peopleManagerMap to set
	 */
	public void setPeopleManagerMap(final Map<String, Long> peopleManagerMap) {
		this.peopleManagerMap = peopleManagerMap;
	}

	/**
	 * @return the pessoaDelegada
	 */
	public Pessoa getPessoaDelegada() {
		return pessoaDelegada;
	}

	/**
	 * @param pessoaDelegada
	 *            the pessoaDelegada to set
	 */
	public void setPessoaDelegada(final Pessoa pessoaDelegada) {
		this.pessoaDelegada = pessoaDelegada;
	}

	/**
	 * @return the pessoaDelegacao
	 */
	public PessoaDelegacao getPessoaDelegacao() {
		return pessoaDelegacao;
	}

	/**
	 * @param pessoaDelegacao
	 *            the pessoaDelegacao to set
	 */
	public void setPessoaDelegacao(final PessoaDelegacao pessoaDelegacao) {
		this.pessoaDelegacao = pessoaDelegacao;
	}

	/**
	 * @return the isDelegate
	 */
	public Boolean getIsDelegate() {
		return isDelegate;
	}

	/**
	 * @param isDelegate
	 *            the isDelegate to set
	 */
	public void setIsDelegate(final Boolean isDelegate) {
		this.isDelegate = isDelegate;
	}

}