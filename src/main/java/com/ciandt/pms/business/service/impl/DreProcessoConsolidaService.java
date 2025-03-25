package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICotacaoMoedaService;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IReceitaMoedaService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.dre.DreProcessoConsolidaException;
import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;
import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.persistence.dao.IFaturaDao;
import com.ciandt.pms.util.DateUtil;

/**
 * Servico para realizar o processo de Set Exchange Rates.
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * @since 16/10/2013
 */
@Service
public class DreProcessoConsolidaService extends
		AbstractDreProcessoExecutavelService {

	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(DreProcessoConsolidaService.class);

	/** Instancia do Servico da entidade Moeda. */
	@Autowired
	private IMoedaService moedaService;

	/** Instancia do Servico da entidade CotacaoMoeda. */
	@Autowired
	private ICotacaoMoedaService cotacaoMoedaService;

	/** Instancia do Servico da entidade ReceitaMoeda. */
	@Autowired
	private IReceitaMoedaService receitaMoedaService;

	/** Instancia do Servico da entidade Receita. */
	@Autowired
	private IReceitaService receitaService;

	/** Instancia do Servico da entidade Fatura. */
	@Autowired
	private IFaturaService faturaService;

	/** Instancia do Dao da entidade Fatura. */
	@Autowired
	private IFaturaDao faturaDao;

	/**
	 * Valida��o de Moeda e Atualiza��o de cota��es de Receitas e Faturas
	 */
	public void process() throws DreProcessoExecutavelException,
			DreProcessoExecutavelException {

		super.setCount(1d);
		super.setTotalRegistros(21d);

		final Date dataMes = super.getDreMes().getDataMes();

		// Busca todas as moedas, a busca esta sendo feita neste momento para
		// controle do progress bar
		List<Moeda> moedaList = moedaService.findMoedaAll();

		if (moedaList != null) {
			// Adiciona ao total de registros o total da lista de moedas
			super.addTotalRegistros(moedaList.size());
		}

		// Busca todas as receitas do m�s/ano, a busca esta sendo feita neste
		// momento para controle do progress bar
		List<Receita> receitaList = this.receitaService
				.findReceitaByMonthDate(dataMes);

		if (receitaList != null) {
			// Adiciona ao total de registros o total da lista de receitas
			super.addTotalRegistros(receitaList.size());
		}

		// Busca todas as cota��es do m�s/ano, a busca esta sendo feita neste
		// momento para controle do progress bar
		List<Fatura> faturaList = faturaService.findFaturaByMonthDate(dataMes);

		if (faturaList != null) {
			// Adiciona ao total de registros o total da lista de faturas
			super.addTotalRegistros(receitaList.size());
		}

		// Valida cotacao moeda
		this.validaCotacaoMoeda(moedaList, dataMes);

		// Atualiza cotacoes
		this.atualizaCotacoes(receitaList, faturaList, dataMes);

	}

	/**
	 * Valida��o de Moeda: Invalida Moeda se n�o tiver cota��o para a data de
	 * fechamento
	 * 
	 * @param moedaList
	 *            Lista de moedas.
	 * @param dataMes
	 *            MM/yyyy do fechamento
	 * 
	 */
	private void validaCotacaoMoeda(final List<Moeda> moedaList,
			final Date dataMes) throws DreProcessoExecutavelException {

		LOGGER.info("Process Init: Currency Validation");

		boolean success = true;

		super.addLog(BundleUtil.getBundle(Constants.ENTITY_NAME_COTACAO_MOEDA));
		super.addLog(super.BREAK_LINE);

		if (moedaList != null) {
			for (Moeda moeda : moedaList) {
				if (!BundleUtil.getBundle(Constants.PROPERTY_CURRENCY_BRL_CODE)
						.equals(moeda.getSiglaMoeda())) {
					CotacaoMoeda cotacaoMoeda = cotacaoMoedaService
							.findCotMoedaByMoedaAndLastDayMonth(moeda, dataMes);
					if (cotacaoMoeda == null) {
						super.addLog(DateUtil.formatDate(new Date(),
								Constants.DEFAULT_DATE_PATTERN_FULL,
								Constants.DEFAULT_CALENDAR_LOCALE));
						super.addLog(super.LOG_ERROR);
						super.addLog(moeda.getNomeMoeda());
						super.addLog(super.MSG_LOG_NOT_VALUE);
						super.addLog(super.BREAK_LINE);

						success = false;

						Messages.showError(
								Constants.CONSOLIDATE_ID,
								Constants.MSG_ERROR_DRE_PROCESS_VALIDATE_CURRENCY,
								moeda.getNomeMoeda());
					}
				}
				
				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count
				super.addCount();
			}
		}

		if (!success) {

			throw new DreProcessoConsolidaException(BundleUtil.getBundle(
					Constants.MSG_ERROR_DRE_PROCESS_CONSOLIDATE, DateUtil
							.formatDate(dataMes,
									Constants.DEFAULT_DATE_PATTERN_MONTH_YEAR,
									Constants.DEFAULT_CALENDAR_LOCALE)));

		}

		LOGGER.info("Process End: Currency Validation");
	}

	/**
	 * Atualiza cota��es de Receitas e Faturas do m�s de execu��o.
	 * 
	 * @param receitaList
	 *            Lista de todas as receitas do m�s
	 * @param faturaList
	 *            Lista de todas as cota��es do m�s
	 * @param dataMes
	 *            MM/yyyy
	 * @throws DreProcessoExecutavelException
	 */
	private void atualizaCotacoes(final List<Receita> receitaList,
			final List<Fatura> faturaList, final Date dataMes)
			throws DreProcessoExecutavelException {

		LOGGER.info("Process Init: Update Quotes");

		try {

			// atualiza as cota��es das receita
			if (receitaList != null) {
				for (Receita receita : receitaList) {
					List<ReceitaMoeda> receitaMoedaList = receita
							.getReceitaMoedas();

					for (ReceitaMoeda receitaMoeda : receitaMoedaList) {
						receitaMoeda.setCotacaoMoeda(this.cotacaoMoedaService
								.findCotacaoMoedaByMonth(
										receitaMoeda.getMoeda(),
										receita.getDataMes()));

						this.receitaMoedaService
								.updateReceitaMoeda(receitaMoeda);
					}

					// Atualiza o percentual concluido do progress bar
					super.notifyObservers(super.getPercentualConcluido());
					// Incrementa o count
					super.addCount();
				}
			}

			// atualiza as cota��es das faturas
			if (faturaList != null) {
				for (Fatura fatura : faturaList) {

					this.faturaService.setCotacaoMoeda(fatura);
					this.faturaDao.update(fatura);

					// Atualiza o percentual concluido do progress bar
					super.notifyObservers(super.getPercentualConcluido());
					// Incrementa o count
					super.addCount();
				}
			}
		} catch (Exception e) {

			Messages.showError(Constants.CONSOLIDATE_ID,
					Constants.MSG_ERROR_DRE_PROCESS_CONSOLIDATE, DateUtil
							.formatDate(dataMes,
									Constants.DEFAULT_DATE_PATTERN_MONTH_YEAR,
									Constants.DEFAULT_CALENDAR_LOCALE));

			throw new DreProcessoConsolidaException(BundleUtil.getBundle(
					Constants.MSG_ERROR_DRE_PROCESS_CONSOLIDATE, DateUtil
							.formatDate(dataMes,
									Constants.DEFAULT_DATE_PATTERN_MONTH_YEAR,
									Constants.DEFAULT_CALENDAR_LOCALE)), e);
		}

		LOGGER.info("Process End: Update Quotes");
	}
}