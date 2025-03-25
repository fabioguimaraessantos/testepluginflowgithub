package com.ciandt.pms.control.jsf;

import java.util.*;

import javax.annotation.security.RolesAllowed;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAbstractDreProcessoExecutavelService;
import com.ciandt.pms.business.service.IDreProcessoService;
import com.ciandt.pms.business.service.IProcessoService;
import com.ciandt.pms.business.service.IValidacaoPessoaService;
import com.ciandt.pms.control.jsf.bean.DreProcessosFechamentoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.enums.StatusDreProcesso;
import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;

/**
 * Define o Controller da entidade.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.CLOSING_DRE:VW" })
public class DreProcessosController {

	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(DreProcessosController.class);

	/** outcome tela busca processos por data . */
	private static final String OUTCOME_DRE_PROCESSOS_FECHAMENTO = "dre_processosFechamento";

	/** outcome tela log com validacoes. */
	private static final String OUTCOME_DRE_LOG_VALIDATION = "dre_logValidation";

	/** outcome tela valida alocacoes */
	private static final String OUTCOME_DRE_VALIDA_ALOCACOES = "dre_validaAlocacoes";

	/** Id message resource */
	private static final String PROCESS_MSG_ID = "process";

	private static final int RESOURCE_VALIDATION = 3;

	private Long resourceNotValidate;

	/** Instancia do bean. */
	@Autowired
	private DreProcessosFechamentoBean bean = null;

	/** Instancia do ProcessoService. */
	@Autowired
	private IProcessoService processoService;

	/** Instancia do servico - ValidacaoPessoa. */
	@Autowired
	private IValidacaoPessoaService validacaoPessoaService;

	/** Instancia do servico - DreProcesso. */
	@Autowired
	private IDreProcessoService dreProcessoService;

	/** Instancia do DreProcessoValidaTCEService. */
	@Autowired
	@Qualifier("dreProcessoValidaTCEService")
	private IAbstractDreProcessoExecutavelService validaTCEService;

	/** Instancia do DreProcessoValidacoesGeraisService. */
	@Autowired
	@Qualifier("dreProcessoValidacoesGeraisService")
	private IAbstractDreProcessoExecutavelService validacoesGeraisService;

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

	public DreProcessosController() {
	}

	/**
	 * Prepara a tela inicial.
	 * 
	 * @return pagina de destino
	 */
	public String prepareResearch() {
		bean.reset();

		return OUTCOME_DRE_PROCESSOS_FECHAMENTO;
	}

	/**
	 * Processa a tela de Closing DRE
	 * 
	 * @return
	 */
	public String prepareProcess() {

		Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

		bean.setProcessoRowList(processoService.findProcessoByDate(dataMes));
		
		resetBar();

		return OUTCOME_DRE_PROCESSOS_FECHAMENTO;
	}

	/**
	 * Executa o processo
	 * 
	 * @return
	 */
	public String process() {

		resetBar();

		String retorno = OUTCOME_DRE_PROCESSOS_FECHAMENTO;
		Long codigoProcesso = bean.getTo().getProcesso().getCodigoProcesso();
		Date monthYear = bean.getTo().getDreMes().getDataMes();

		// Busca o status da ultima execucao para validar se o processo pode ser
		// executado
		DreProcesso lastDreProcesso = dreProcessoService
				.findLastByProcessoDataAndIndPorLogin(codigoProcesso,
						monthYear, Constants.NO);

		// Se o status da ultima execucao do processo que esta sendo executado
		// for "LK" (Loked) ou "IP" (In Progress) nao pode ser executado no
		// momento
		if (lastDreProcesso != null) {
			String lastStatus = lastDreProcesso.getIndicadorStatus();

			if ((StatusDreProcesso.LOCKED.getAbbreviation().equals(lastStatus)
					|| StatusDreProcesso.IN_PROGRESS.getAbbreviation().equals(
							lastStatus)) && lastDreProcesso.getProcesso().getCodigoProcesso() != RESOURCE_VALIDATION) {
				Messages.showWarning(PROCESS_MSG_ID,
						Constants.MSG_WARNING_DRE_PROCESS_STATUS, new Object[] {
								bean.getTo().getProcesso().getNomeProcesso(),
								StatusDreProcesso.getByAbbreviation(lastStatus)
										.getName() });
				prepareProcess();
				return retorno;
			}
		}

		DreProcesso dreProcesso = prepareDreProcesso();

		try {
			switch (Integer.valueOf(codigoProcesso.toString())) {
			case 1:
				// TCE Validation
				validaTCEService.registerObserver(bean);
				validaTCEService.execute(monthYear, dreProcesso);
				setTextoLog();
				bean.setEnabled(false);
				retorno = OUTCOME_DRE_LOG_VALIDATION;
				break;
			case 2:
				// General Validation
				validacoesGeraisService.registerObserver(bean);
				validacoesGeraisService.execute(monthYear, dreProcesso);
				setTextoLog();
				bean.setEnabled(false);
				retorno = OUTCOME_DRE_LOG_VALIDATION;
				break;
			case 3:
				// Resource Validation
				prepareResourceValidate();
				retorno = OUTCOME_DRE_VALIDA_ALOCACOES;
				break;
			case 4:
				// Duty Hours and Overtime
				registraPlantaoHorasExtrasService.registerObserver(bean);
				registraPlantaoHorasExtrasService.execute(monthYear,
						dreProcesso);

				Iterator<FacesMessage> messageIterator = FacesContext
						.getCurrentInstance().getMessages(
								Constants.REGISTER_PL_AND_HE_COSTS_ID);

				if (!messageIterator.hasNext()) {
					Messages.showSucess(PROCESS_MSG_ID,
							Constants.MSG_SUCCESS_REGISTER_PL_HE_COSTS_DRE);
				}

				prepareProcess();
				break;
			case 5:
				// Set Exchange Rates
				consolidaService.registerObserver(bean);
				consolidaService.execute(monthYear, dreProcesso);

				Iterator<FacesMessage> messageConsolidateIterator = FacesContext
						.getCurrentInstance().getMessages(
								Constants.CONSOLIDATE_ID);

				if (!messageConsolidateIterator.hasNext()) {
					Messages.showSucess(
							PROCESS_MSG_ID,
							Constants.MSG_SUCCESS_CONSOLIDATE_DRE_LOG_FECHAMENTO);
				}

				prepareProcess();
				break;
			default:
				break;
			}
		} catch (DreProcessoExecutavelException e) {
			LOGGER.error("Error on process: "
					+ bean.getTo().getProcesso().getNomeProcesso(), e);
			Messages.showError(PROCESS_MSG_ID, Constants.DEFAULT_ERROR_OUTCOME);
		} 

		// Setando mensagens em atributo do bean pois a chamada ajax do
		// progressBar pode "sumir" com os messages caso a chamada assincrona
		// finalize depois do processo. Como o scope � de request, n�o tem como
		// manter os messages do request do processo na chamada ajax do
		// progressBar
		setMessages();

		return retorno;
	}

	/**
	 * Setando as mensagens em atributo do bean pois a chamada ajax do
	 * progressBar pode "sumir" com os messages caso a chamada assincrona
	 * finalize depois do processo em si. Como o scope � de request, n�o tem
	 * como manter os messages do request do processo na chamada ajax do
	 * progressBar
	 */
	private void setMessages() {

		Iterator<FacesMessage> messageIterator = FacesContext
				.getCurrentInstance().getMessages();

		while (messageIterator.hasNext()) {
			FacesMessage msg = messageIterator.next();
			Severity severity = msg.getSeverity();

			// Caso severity for INFO, seta no atributo msgSuccess
			if (FacesMessage.SEVERITY_INFO.equals(severity)) {
				bean.setMsgSuccess(bean.getMsgSuccess() + msg.getDetail() + "<br/>");
			} else {
				// Caso severity for diferente de INFO, seta no atributo
				// msgError
				bean.setMsgError(bean.getMsgError() + msg.getDetail() + "<br/>");
			}

			// Remove a mensagem do FacesContext
			messageIterator.remove();
		}
	}

	/**
	 * Seta o log das valida��es: TCE Validation e General Validation
	 */
	private void setTextoLog() {

		DreProcesso drep = dreProcessoService
				.findLastByProcessoDataAndIndPorLogin(bean.getTo()
						.getProcesso().getCodigoProcesso(), bean.getTo()
						.getDreMes().getDataMes(), Constants.NO);

		bean.getTo().setTextoLog(drep != null ? drep.getTextoLog() : null);

		if (drep.getTextoLog() == null) {
			Messages.showSucess(PROCESS_MSG_ID, Constants.MSG_SUCCESS_VALIDATE);
		} else {
			Messages.showError(PROCESS_MSG_ID,
					Constants.MSG_ERROR_VALIDATE_DRE_LOG_FECHAMENTO);
		}
	}

	/**
	 * Prepara a tela de validação dos Recursos.
	 */
	public void prepareResourceValidate() {

		validacaoPessoaService.registerObserver(bean);
		validaAlocacaoService.registerObserver(bean);

		DreProcesso lastDreProcesso = dreProcessoService
				.findLastByProcessoDataAndIndPorLogin(bean.getTo().getProcesso().getCodigoProcesso(),
						bean.getTo().getDreMes().getDataMes(), Constants.NO);

		if(lastDreProcesso != null){bean.setLastStatusDREProcess(lastDreProcesso.getIndicadorStatus());}
		resourceNotValidate = countPessoaByDataMes();
		bean.setCountPessoaNotValidatedByDate(resourceNotValidate);

		// Se não tiver pessoas para serem validadas, executa o processo de
		// valida alocação para setar o processo como executado com sucesso
		if (resourceNotValidate == 0) {

			DreProcesso dreProcesso = prepareDreProcesso();

			try {
				validaAlocacaoService.execute(bean.getTo().getDreMes()
						.getDataMes(), dreProcesso, null);
			} catch (DreProcessoExecutavelException e) {
				LOGGER.error("Error on process: "
						+ bean.getTo().getDreMes().getDataMes(), e);
				Messages.showError(PROCESS_MSG_ID, Constants.DEFAULT_ERROR_OUTCOME);
			}
				Messages.showSucess(
						PROCESS_MSG_ID,
						Constants.VALIDATE_PERSON_SUCCESS_ALL_RESOURCES_VALIDATED);
		}

	}

	private Long countPessoaByDataMes() {
		return validacaoPessoaService.countPessoaByDataMes(bean.getTo().getDreMes().getDataMes());
	}

	/**
	 * Executa a validacao das Pessoas.
	 */
	public String resourceValidate() {

		validacaoPessoaService.registerObserver(bean);
		validaAlocacaoService.registerObserver(bean);
		
		DreProcesso dreProcesso = prepareDreProcesso();

        List<Pessoa> pessoas = new ArrayList<>(validacaoPessoaService.findPessoaByManagerAndDataMes(
                null, bean.getTo().getDreMes().getDataMes()));

		try {
			validaAlocacaoService.execute(bean.getTo().getDreMes().getDataMes(), dreProcesso, pessoas);
			bean.setCountPessoaNotValidatedByDate(countPessoaByDataMes());
		} catch (DreProcessoExecutavelException e) {
			LOGGER.error("Error on process: "
					+ bean.getTo().getDreMes().getDataMes(), e);
			Messages.showError(PROCESS_MSG_ID, Constants.DEFAULT_ERROR_OUTCOME);
		}

		if (pessoas.isEmpty()) {
			Messages.showSucess(PROCESS_MSG_ID, Constants.VALIDATE_PERSON_SUCCESS_ALL_RESOURCES_VALIDATED);
		}
		
		// Setando mensagens em atributo do bean pois a chamada ajax do
		// progressBar pode "sumir" com os messages caso a chamada assincrona
		// finalize depois do processo. Como o scope � de request, n�o tem como
		// manter os messages do request do processo na chamada ajax do
		// progressBar
		setMessages();
		
		return OUTCOME_DRE_VALIDA_ALOCACOES;
	}

	/**
	 * Reseta os valores das progressBars.
	 */
	public void resetBar() {
		bean.setCurrentValue(new Float(0d));
		bean.setEnabled(true);
		bean.setMsgError("");
		bean.setMsgSuccess("");
	}

	/**
	 * Bot�o Back
	 */
	public String back() {
		prepareProcess();
		return OUTCOME_DRE_PROCESSOS_FECHAMENTO;
	}

	/**
	 * Instancia a entidade DreProcesso com os atributos necessarios
	 * 
	 * @return {@link DreProcesso}
	 */
	private DreProcesso prepareDreProcesso() {

		DreProcesso dreProcesso = new DreProcesso();
		dreProcesso.setProcesso(processoService.findProcessoById(bean.getTo()
				.getProcesso().getCodigoProcesso()));
		dreProcesso.setIndicadorPorLogin(Constants.NO);
		dreProcesso.setCodigoLogin(LoginUtil.getLoggedUsername());

		return dreProcesso;
	}

	/**
	 * @return the bean
	 */
	public DreProcessosFechamentoBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final DreProcessosFechamentoBean bean) {
		this.bean = bean;
	}
}