package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAbstractDreProcessoExecutavelService;
import com.ciandt.pms.business.service.IDreMesService;
import com.ciandt.pms.business.service.IDreProcessoService;
import com.ciandt.pms.business.service.IProcessoService;
import com.ciandt.pms.enums.StatusDreMes;
import com.ciandt.pms.enums.StatusDreProcesso;
import com.ciandt.pms.exception.dre.DreProcessoConsolidaException;
import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;
import com.ciandt.pms.exception.dre.DreProcessoPlantaoHorasExtrasException;
import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.Pessoa;

/**
 * Classe abstrata que define a estrutura de execucao de um processo de closing
 * DRE.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 16/10/2013
 */
@Service
public abstract class AbstractDreProcessoExecutavelService extends Observable
		implements IAbstractDreProcessoExecutavelService {

	final String TITLE_LINE = "********** ";
	final String BREAK_LINE = "\n";
	final String LOG_ERROR = " ERROR ";
	final String MSG_LOG_NOT_TCE_ASSOCIATED = " does not have TCE value associated.";
	final String LOG_WARN = " WARN ";
	final String MSG_LOG_NOT_CLOSED = " is not closed.";
	final String MSG_LOG_NOT_PUBLISHED = " is not published.";
	final String MSG_LOG_INVOICEID = "Invoice ID #";
	final String MSG_LOG_NOT_SUBMITED = " is not submited.";
	final String MSG_LOG_NOT_CUSTO_INF_BASE_ASSOCIATED = " does not have Site Costs associated.";
	final String MSG_LOG_NOT_NCL_FILLED = " has Profit Center Type Mandatory to be filled.";
	final String MSG_LOG_MISSING = " is missing.";
	final String MSG_LOG_NOT_VALUE = " has no value to the last day of the month.";

	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(AbstractDreProcessoExecutavelService.class);

	/** Instancia do servico {@link DreMesService}. */
	@Autowired
	private IDreMesService dreMesService;

	/** Instancia do servico {@link DreProcessoService}. */
	@Autowired
	private IDreProcessoService dreProcessoService;

	/** {@link DreMes}. */
	private DreMes dreMes;

	/** {@link DreProcesso}. */
	private DreProcesso dreProcesso;

	/** Instancia do servico {@link ProcessoService}. */
	@Autowired
	private IProcessoService processoService;

	/** Log de execucao do processamento. */
	private StringBuilder log = new StringBuilder();

	/** Lista de pessoas a validar aloca��o */
	private List<Pessoa> pessoas;

	private ArrayList<Object> observers;

	/** Contador para o progress bar */
	private double count;

	/** Total de registos a serem processados para o progress bar */
	private double totalRegistros;

	public AbstractDreProcessoExecutavelService() {
		observers = new ArrayList<Object>();
	}

	/**
	 * Prepara um processo para ser persistido.
	 * 
	 * @param dreProcesso
	 *            {@link DreProcesso}
	 * @param dreMes
	 *            {@link DreMes}
	 */
	private void prepareDreProcessoToPersist(final DreProcesso dreProcesso,
			final DreMes dreMes) {
		dreProcesso.setDataInicio(new Date());
		dreProcesso.setDreMes(dreMesService.findDreMesById(dreMes
				.getCodigoDreMes()));
		dreProcesso.setIndicadorStatus(StatusDreProcesso.IN_PROGRESS
				.getAbbreviation());
		this.dreProcesso = dreProcesso;
	}

	/**
	 * Metodo executado antes da chamada ao metodo process.
	 * 
	 * @param dateRef
	 *            data base de execucao do processamento (pode ser retroativo)
	 * @param dreProcesso
	 *            o processo a ser executado
	 */
	public final void preProcess(final Date dateRef,
			final DreProcesso dreProcesso) {

		LOGGER.info("Pre Process Init: "
				+ dreProcesso.getProcesso().getNomeProcesso());

		// Seta o progress bar para 1%
		notifyObservers(new Float(1));

		log = new StringBuilder();
		this.dreMes = null;
		this.dreProcesso = null;

		// Busca o DRE_MES e cria caso nao exista

		this.dreMes = dreMesService.findDreMesByDataMes(dateRef);

		if (this.dreMes == null) {
			this.dreMes = new DreMes(dateRef,
					StatusDreMes.INCOMPLETE.getAbbreviation(), new Date());
			dreMesService.createDreMes(dreMes);
		}

		// Cria o DRE_PROCESSO
		this.prepareDreProcessoToPersist(dreProcesso, dreMes);
		dreProcessoService.createDreProcesso(this.dreProcesso);

		// Invalida um (ou mais) processos filhos do processo em execucao
		// (Somente se o processo n�o estiver sendo executado para um login
		// especifico)
		if (Constants.NO.equals(this.dreProcesso.getIndicadorPorLogin())) {
			processoService.invalidateDependingProcessoCascade(dreMes,
					this.dreProcesso.getProcesso());
		}

		LOGGER.info("Pre Process End: "
				+ dreProcesso.getProcesso().getNomeProcesso());
	}

	/**
	 * Realiza o processamento do processo executavel.
	 * 
	 * @throws DreProcessoExecutavelException
	 */
	public abstract void process() throws DreProcessoExecutavelException;

	/**
	 * Metodo executado logo apos o termino do metodo proccess.
	 */
	public final void postProcess() {

		LOGGER.info("Pos Process Init: "
				+ dreProcesso.getProcesso().getNomeProcesso());

		// Seta o log no objeto
		this.dreProcesso.setTextoLog(this.getLog().toString());

		// Atualiza o DRE_PROCESSO
		dreProcessoService.updateDreProcesso(dreProcesso);

		// Atualizar o status do DRE_MES caso todos os processos tenham sido
		// executados e se o processo nao estiver sendo executado para um login
		// especifico
		if (Constants.NO.equals(this.dreProcesso.getIndicadorPorLogin())
				&& dreMesService.isDreMesCompleted(this.dreMes)) {
			dreMes.setIndicadorStatus(StatusDreMes.COMPLETE.getAbbreviation());
			dreMes.setDataFim(new Date());
			dreMesService.updateDreMes(dreMes);
		}

		// Seta o progress bar para 100%
		notifyObservers(new Float(100));

		LOGGER.info("Pos Process End: "
				+ dreProcesso.getProcesso().getNomeProcesso());
	}

	/**
	 * Realiza a execucao do processo.
	 * 
	 * @param dateRef
	 *            data base de execucao do processamento (pode ser retroativo)
	 * @param dreProcesso
	 *            o processo a ser executado
	 */
	@Override
	public final void execute(final Date dateRef, final DreProcesso dreProcesso)
			throws DreProcessoExecutavelException {

		LOGGER.info("Execute Init: "
				+ dreProcesso.getProcesso().getNomeProcesso());

		// Pre Process
		this.preProcess(dateRef, dreProcesso);
		try {
			// Process
			this.process();
			this.dreProcesso.setIndicadorStatus(StatusDreProcesso.PERFORMED
					.getAbbreviation());
		} catch (DreProcessoPlantaoHorasExtrasException plantaoHorasExtrasException) {
			this.dreProcesso.setIndicadorStatus(StatusDreProcesso.NOT_PERFORMED
					.getAbbreviation());
		} catch (DreProcessoConsolidaException consolidaException) {
			this.dreProcesso.setIndicadorStatus(StatusDreProcesso.NOT_PERFORMED
					.getAbbreviation());
		} catch (Exception e) {
			LOGGER.error("Erro gerado no processamento.", e);
			this.dreProcesso.setIndicadorStatus(StatusDreProcesso.ERROR
					.getAbbreviation());
			this.addLog(e.getMessage());
			throw new DreProcessoExecutavelException(e.getMessage());
		} finally {
			// Pos Process
			this.postProcess();
			LOGGER.info("Execute End: "
					+ dreProcesso.getProcesso().getNomeProcesso());
		}
	}

	/**
	 * Sobrecarga do metodo execute para realizar a execucao do processo de
	 * Resource Validation.
	 * 
	 * @param dateRef
	 *            data base de execucao do processamento (pode ser retroativo)
	 * @param dreProcesso
	 *            o processo a ser executado
	 * @param pessoas
	 *            Lista de pessoas a validar (mapa de alocacao)
	 */
	@Override
	public final void execute(final Date dateRef,
			final DreProcesso dreProcesso, final List<Pessoa> pessoas)
			throws DreProcessoExecutavelException {
		this.pessoas = pessoas;
		this.execute(dateRef, dreProcesso);
	}

	/**
	 * Notifica o percentual do processo concluido atualizado aos Observers
	 * 
	 * @param percentualConcluido
	 *            percentual do processo j� executado
	 */
	public void notifyObservers(final Float progress) {
		for (int i = 0; i < observers.size(); i++) {
			Observer observer = (Observer) observers.get(i);
			observer.update(this, progress);
		}
	}

	/**
	 * Registra o objeto observer (que fica "escutando" qualquer atualiza��o no
	 * percentual de conclus�o do processo)
	 * 
	 * @param obs
	 *            Objeto que ficara observando
	 */
	public void registerObserver(Observer o) {
		boolean isRegistered = false;
		for (int i = 0; i < observers.size(); i++) {
			if (observers.get(i).equals(o)) {
				isRegistered = true;
				break;
			}
		}
		if (!isRegistered) {
			observers.add(o);
		}
	}

	/**
	 * Incrementa o texto de log.
	 * 
	 * @param texto
	 *            {@link String} com texto de log
	 */
	public void addLog(String texto) {
		this.log.append(texto);
	}

	/**
	 * Obtem o log
	 * 
	 * @return {@link StringBuilder}
	 */
	public StringBuilder getLog() {
		return this.log;
	}

	/**
	 * @return the dreMes
	 */
	public DreMes getDreMes() {
		return dreMes;
	}

	/**
	 * @return the pessoas
	 */
	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	/**
	 * @return the dreProcesso
	 */
	public DreProcesso getDreProcesso() {
		return dreProcesso;
	}

	/**
	 * @return the observers
	 */
	public ArrayList<Object> getObservers() {
		return observers;
	}

	/**
	 * @param observers
	 *            the observers to set
	 */
	public void setObservers(ArrayList<Object> observers) {
		this.observers = observers;
	}

	/**
	 * @return the count
	 */
	public double getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(double count) {
		this.count = count;
	}

	/**
	 * Adiciona 1 ao count
	 */
	public void addCount() {
		this.count++;
	}

	/**
	 * @return the totalRegistros
	 */
	public double getTotalRegistros() {
		return totalRegistros;
	}

	/**
	 * @param totalRegistros
	 *            the totalRegistros to set
	 */
	public void setTotalRegistros(double totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	/**
	 * @param totalRegistros
	 *            the totalRegistros to add
	 */
	public void addTotalRegistros(double totalRegistros) {
		this.totalRegistros += totalRegistros;
	}

	/**
	 * Calcula o percentual concluido para o progress bar
	 * 
	 * @return percentaul concluido
	 */
	public Float getPercentualConcluido() {

		Float percentual = new Float(0);
		try {
			percentual = new Float(
					(this.getCount() / this.getTotalRegistros()) * 100d);
		} catch (Exception e) {
			LOGGER.error("Error on calculating the percentage completed. "
					+ e.getMessage());
		}
		return percentual;
	}
	
	
	
	
	/**
	 * FIXME: *** ATEN��O *** 
	 * 
	 * Este m�todo s� foi criado para implementar o m�todo do registro de
	 * plant�o e hora extra por valor com o Antigo Closing DRE, quando mudar para
	 * o Novo Closing DRE, este m�todo n�o precisa ser posto em produ��o, n�o
	 * precisa fazer merge
	 * 
	 * @param dataMes data do fechamento
	 */
	public boolean processOldClosindDreByValue(final Date dataMes)  {
		try {
			this.dreMes = new DreMes();
			this.dreMes.setDataMes(dataMes);
			
			this.dreProcesso = new DreProcesso();
			this.dreProcesso.setIndicadorPorLogin(Constants.NO);
			
			this.process();
			return true;
		} catch (DreProcessoExecutavelException e) {
			return false;
		}
	}
}