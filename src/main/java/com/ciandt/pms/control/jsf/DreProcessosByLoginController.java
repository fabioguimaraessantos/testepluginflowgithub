package com.ciandt.pms.control.jsf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAbstractDreProcessoExecutavelService;
import com.ciandt.pms.business.service.IDreMesService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IProcessoService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.business.service.IValidacaoPessoaService;
import com.ciandt.pms.control.jsf.bean.DreProcessosFechamentoByLoginBean;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.enums.ProcessoDre;
import com.ciandt.pms.enums.StatusDreMes;
import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;
import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.model.vo.ValidacaoPessoaRow;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;

/**
 * Define o Controller da entidade.
 * 
 * @since 29/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.CLOSING_DRE:VW" })
public class DreProcessosByLoginController {

	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(DreProcessosByLoginController.class);

	/** outcome tela busca processos por data . */
	private static final String OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN = "dre_processosFechamento_byLogin";

	/** Id message resource */
	private static final String PROCESS_BY_LOGIN_MSG_ID = "processByLogin";

	/** Instancia do bean. */
	@Autowired
	private DreProcessosFechamentoByLoginBean bean = null;

	/** Instancia do ProcessoService. */
	@Autowired
	private IProcessoService processoService;

	/** Instancia de PessoaService. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do DreMesService. */
	@Autowired
	private IDreMesService dreMesService;

	/** Instancia do DreProcessoValidaAlocacaoService. */
	@Autowired
	@Qualifier("dreProcessoValidaAlocacaoService")
	private IAbstractDreProcessoExecutavelService validaAlocacaoService;

	/** Instancia do DreProcessoRegistraPlantaoHorasExtrasService. */
	@Autowired
	@Qualifier("dreProcessoRegistraPlantaoHorasExtrasService")
	private IAbstractDreProcessoExecutavelService registraPlantaoHorasExtrasService;

	/** Instancia do DreProcessoConsolidaService. */
	@Autowired
	@Qualifier("dreProcessoConsolidaService")
	private IAbstractDreProcessoExecutavelService consolidaService;

	/** Servico de {@link Recurso}. */
	@Autowired
	private IRecursoService recursoService;

	/** Instancia servi�o {@link ValidacaoPessoaRow}. */
	@Autowired
	private IValidacaoPessoaService validacaoPessoaService;

	private boolean isITSupport = false;

	/**
	 * Prepara a tela inicial.
	 * 
	 * @return pagina de destino
	 */
	public String prepareResearch() {
		bean.reset();
		setIsITSupport(validateITSupport());
		return OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN;
	}

	/**
	 * Processa a tela de Closing DRE
	 */
	public String prepareProcess() {
		bean.setScreenNumber(Integer.valueOf(1));
		setIsITSupport(validateITSupport());
		return OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN;
	}

	/**
	 * Executa a validacao de Mapa Alocacao
	 * Acao da Tela 1
	 */
	public String processResourceValidation() {
		setIsITSupport(validateITSupport());

		if (bean.getPessoas().isEmpty()) {
			Messages.showError("", Constants.MSG_ERROR_VALIDATE_DRE_BY_LOGIN);

			return OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN;
		}

		try {
			Date dataMes = DateUtil.getDate(bean.getMonthBeg(),
					bean.getYearBeg());

			// Busca na tabela DRE_MES pelo MM/yyyy
			DreMes dreMes = dreMesService.findDreMesByDataMes(dataMes);

			// Verifica se o processo de Closing DRE j� foi completamente
			// executado para o MM/yyyy. S� poder� reprocessar por login se
			// todos os processos do DRE estiver sido processado para o MM/yyyy.
			if (dreMes == null
					|| StatusDreMes.INCOMPLETE.getAbbreviation().equals(
							dreMes.getIndicadorStatus())) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						BundleUtil
								.getBundle(Constants.DEFAULT_DATE_PATTERN_MONTH_YEAR));
				Messages.showWarning(PROCESS_BY_LOGIN_MSG_ID,
						Constants.MSG_WARNING_DRE_PROCESS_NOT_COMPLETED,
						sdf.format(dataMes));
				return OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN;
			}
			
			List<Pessoa> listaPessoas = bean.getPessoas();
			
			for (Pessoa p : listaPessoas) {
				// Seta para true pois neste processo n�o pode atualizar a
				// dataMesValidado
				p.setValidacaoCloseDreByLogin(true);
			}

			// Instancia o DreProcesso com os valores necessarios
			DreProcesso dreProcesso = prepareDreProcesso(Long
					.valueOf(ProcessoDre.RESOURCE_VALIDATION.getCodigo()));
			
			// Executa o processo de Resource Validation
			// o m�todo ValidacaoPessoaService.validatePessoa tenta criar um novo custo_realizado que j� existe.
			validaAlocacaoService.execute(dataMes, dreProcesso, listaPessoas);

			// Envia para tela 2
			bean.setScreenNumber(Integer.valueOf(2));

		} catch (DreProcessoExecutavelException e) {
			LOGGER.error("Error on process: ", e);
			Messages.showError(PROCESS_BY_LOGIN_MSG_ID, Constants.DEFAULT_ERROR_OUTCOME);
		}

		return OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN;
	}

	/**
	 * Executa a apropriacao de plantao e horas extras
	 * Acao da Tela 2
	 */
	public String processDutyHoursAndOvertime() {

		try {
			Date dataMes = DateUtil.getDate(bean.getMonthBeg(),
					bean.getYearBeg());

			// Instancia o DreProcesso
			DreProcesso dreProcesso = prepareDreProcesso(Long
					.valueOf(ProcessoDre.DUTY_HOURS_AND_OVERTIME.getCodigo()));

			// Executa o processo de Duty Hours and Overtime
			registraPlantaoHorasExtrasService.execute(dataMes, dreProcesso,
					bean.getPessoas());

			// Envia para tela 3
			bean.setScreenNumber(Integer.valueOf(3));
			Messages.showSucess("", Constants.MSG_SUCCESS_REGISTER_PL_HE_COSTS_DRE);

		} catch (DreProcessoExecutavelException e) {
			LOGGER.error("Error on process: "
					+ bean.getTo().getDreMes().getDataMes(), e);
			Messages.showError(PROCESS_BY_LOGIN_MSG_ID,
					Constants.DEFAULT_ERROR_OUTCOME);
		}

		return OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN;
	}

	/**
	 * Executa a consolidacao
	 * Acao da Tela 3
	 */
	public String processSetExchangeRates() {

		try {
			Date dataMes = DateUtil.getDate(bean.getMonthBeg(),
					bean.getYearBeg());

			// Instancia o DreProcesso
			DreProcesso dreProcesso = prepareDreProcesso(Long
					.valueOf(ProcessoDre.SET_EXCHANGE_RATES.getCodigo()));

			// Executa o processo de Set Exchange Rates
			consolidaService.execute(dataMes, dreProcesso, bean.getPessoas());
			Messages.showSucess("", Constants.MSG_SUCCESS_CONSOLIDATE_DRE_LOG_FECHAMENTO);
			bean.reset();

		} catch (DreProcessoExecutavelException e) {
			LOGGER.error("Error on process: "
					+ bean.getTo().getDreMes().getDataMes(), e);
			Messages.showError(PROCESS_BY_LOGIN_MSG_ID,
					Constants.DEFAULT_ERROR_OUTCOME);
		}

		return OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN;
	}

	/**
	 * Bot�o Back
	 */
	public String back() {
		prepareProcess();
		setIsITSupport(validateITSupport());
		return OUTCOME_DRE_PROCESSOS_FECHAMENTO_BY_LOGIN;
	}

	/**
	 * Instancia a entidade DreProcesso com os atributos necessarios
	 * 
	 * @param cdProcesso
	 *            Codigo do Processo
	 * @return {@link DreProcesso}
	 */
	private DreProcesso prepareDreProcesso(final Long cdProcesso) {

		DreProcesso dreProcesso = new DreProcesso();
		dreProcesso.setProcesso(processoService.findProcessoById(cdProcesso));
		dreProcesso.setIndicadorPorLogin(Constants.SIM);
		dreProcesso.setCodigoLogin(LoginUtil.getLoggedUsername());

		return dreProcesso;
	}

	/**
	 * @return the bean
	 */
	public DreProcessosFechamentoByLoginBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(DreProcessosFechamentoByLoginBean bean) {
		this.bean = bean;
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
	 * Vaida a pessoa e se a {@link Pessoa} j� foi 
	 * 
	 * @param context
	 * @param component
	 * @param value
	 */
	public void validateDuplicatedLogin(final FacesContext context,
			final UIComponent component, final Object value) {

		String login = (String) value;
		if (login != null && (!"".equals(login))) {
			Recurso r = recursoService.findRecursoByMnemonico(login);
			if (r == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			} else if (true) {
				for (Pessoa pessoa : bean.getPessoas()) {
					if (login.equals(pessoa.getCodigoLogin())) {
						throw new ValidatorException(
								Messages.getMessageError(Constants.MSG_ERROR_VALIDATE_DRE_LOGIN_DUPLICATED));
					}
				}
			}
		}
	}

	/**
	 * Adiciona uma {@link Pessoa} a lista de logins.
	 */
	public void addPersonLoginList() {
		bean.getPessoas().add(pessoaService.findPessoaByLogin(bean.getTo().getCodigoLogin()));
		bean.getTo().setCodigoLogin("");
	}
	
	/**
	 * Remove uma {@link Pessoa} a lista de logins.
	 */
	public void removePersonLoginList() {
		if (bean.getPessoas() != null) {
			bean.getPessoas().remove(bean.getPessoa());
		}
	}

    /**
     * Cancela a atualiza��o de um DreProcessosByLoginController.
     */
    public void cancel() {
        bean.reset();
    }

	private Boolean validateITSupport() {
		GrantedAuthority[] loggedUserAuthorities = LoginUtil
				.getLoggedUserAuthorities();
        boolean isItSupport = false;
		for (GrantedAuthority authority : loggedUserAuthorities) {

			if(authority.getAuthority().equals("BOF.CLOSING_DRE:ED")
			   ||authority.getAuthority().equals("BOF.CLOSING_DRE:CR")){
				isItSupport = false;
				continue;
			}
			if (authority.getAuthority().equals("BOF.CLOSING_DRE:VW")) {
				isItSupport = true;
			}
		}
		return isItSupport;
	}
	public boolean getIsITSupport() {
		return isITSupport;
	}

	public void setIsITSupport(boolean isITSupport) {
		this.isITSupport = isITSupport;
	}
}