package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IContratoPraticaCentroLucroService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.ICustoInfraBaseService;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.CustoInfraBase;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.util.DateUtil;

/**
 * Servico para realizar o processo de validacao de General Validation.
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * @since 16/10/2013
 */
@Service
public class DreProcessoValidacoesGeraisService extends
		AbstractDreProcessoExecutavelService {

	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(DreProcessoValidacoesGeraisService.class);

	/** Instancia do Servico da entidade Fatura. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do Servico da entidade Receita. */
	@Autowired
	private IReceitaService receitaService;

	/** Instancia do Servico da entidade Fatura. */
	@Autowired
	private IFaturaService faturaService;

	/** Instancia do Servico da entidade CidadeBase. */
	@Autowired
	private ICidadeBaseService cidadeBaseService;

	/** Instancia do Servico da entidade CustoInfraBase. */
	@Autowired
	private ICustoInfraBaseService custoInfraBaseService;

	/** Instancia do Servico da entidade ContratoPratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do Servico da entidade ContratoPraticaCentroLucro. */
	@Autowired
	private IContratoPraticaCentroLucroService cpclService;

	/** Instancia do Servico da entidade NaturezaCentroLucro. */
	@Autowired
	private INaturezaCentroLucroService naturezaService;

	/** Instancia do Servico da entidade MapaAlocacao. */
	@Autowired
	private IMapaAlocacaoService mapaAlocacaoService;

	/**
	 * Valida��es gerais. Realiza valida��es por data de fechamento (MM/yyyy)
	 * de: <li>M�dulos</li> <li>Receitas</li> <li>Mapa de Aloca��o</li> <li>
	 * Fatura</li> <li>Custo Infra Base</li> <li>Contrato Pratica</li>
	 */
	public void process() throws DreProcessoExecutavelException {

		super.setCount(1d);
		super.setTotalRegistros(1d);
		
		// Incio das buscas

		// Busca a lista de modulos, a busca esta sendo feita neste momento para
		// controle do progress bar
		final List<Modulo> moduloList = moduloService.findModuloaAll();

		if (moduloList != null) {
			// Adiciona ao total de registros o total da lista de modulos
			super.addTotalRegistros(moduloList.size());
		}

		// Busca a lista de receitas n�o fechadas, a busca esta sendo feita
		// neste momento para controle do progress bar
		final List<Receita> receitaList = receitaService
				.findReceitaAllNotClosed(super.getDreMes().getDataMes());

		if (receitaList != null) {
			// Adiciona ao total de registros o total da lista de receitas n�o
			// fechadas
			super.addTotalRegistros(receitaList.size());
		}

		// Busca a lista de mapa alocacao sem receita, a busca esta sendo feita
		// neste momento para controle do progress bar
		List<MapaAlocacao> mapaList = mapaAlocacaoService
				.findMapaAlocacaoByFilterWithoutRevenue(null, null, null, null,
						super.getDreMes().getDataMes(), null);

		if (mapaList != null) {
			// Adiciona ao total de registros o total da lista de mapa alocacao
			// sem receita
			super.addTotalRegistros(mapaList.size());
		}

		// Busca a lista de fatura nao fechada, a busca esta sendo feita neste
		// momento para controle do progress bar
		final List<Fatura> faturaList = faturaService
				.findFaturaAllNotClosed(super.getDreMes().getDataMes());

		if (faturaList != null) {
			// Adiciona ao total de registros o total da lista de faturas n�o
			// fechadas
			super.addTotalRegistros(faturaList.size());
		}

		// Busca a lista de cidade base, a busca esta sendo feita neste
		// momento para controle do progress bar
		final List<CidadeBase> cidadeBaseList = cidadeBaseService
				.findCidadeBaseAllActive();

		if (cidadeBaseList != null) {
			// Adiciona ao total de registros o total da lista de cidade base
			super.addTotalRegistros(cidadeBaseList.size());
		}

		// Busca a lista de contrato pratica com mapa alocacao, a busca esta
		// sendo feita neste momento para controle do progress bar
		final List<ContratoPratica> contratoPraticaList = contratoPraticaService
				.findContPratAllWithMapaAlocacao();

		if (contratoPraticaList != null) {
			// Adiciona ao total de registros o total da lista de contrato
			// pratica com mapa alocacao
			super.addTotalRegistros(contratoPraticaList.size());
		}

		NaturezaCentroLucro naturezaFilter = new NaturezaCentroLucro();
		naturezaFilter.setIndicadorAtivo(Constants.ACTIVE);
		naturezaFilter.setIndicadorTipo(Constants.NATUREZA_TYPE_MANDATORY);

		// Busca a lista de natureza centro lucro, a busca esta sendo feita
		// neste momento para controle do progress bar
		final List<NaturezaCentroLucro> naturezaMandatoryList = naturezaService
				.findNaturezaCentroLucroByFilter(naturezaFilter);

		if (contratoPraticaList != null &&naturezaMandatoryList != null) {
			// Adiciona ao total de registros o total da lista de natureza
			// centro lucro
			super.addTotalRegistros(contratoPraticaList.size()
					* naturezaMandatoryList.size());
		}

		// Fim das buscas
		
		// Realiza valida��o dos modulos
		validaModulos(moduloList);

		// Realiza valida��o das receitas
		validaReceitas(receitaList);

		// Realiza valida��o dos mapas de aloca��o
		validaMapaAlocacao(mapaList);

		// Realiza valida��o das faturas
		validaFaturas(faturaList);

		// Realiza valida��o do custo infra base
		validaCustoInfraBase(cidadeBaseList);

		// Realiza valida��o do centro contrato pratica
		validaContratoPratica(contratoPraticaList, naturezaMandatoryList);

	}

	/**
	 * Valida��o de Contrato Pratica: Invalida os itens (Contrato Praticas) que
	 * n�o possuem Centro de Lucro associado por data de fechamento
	 * 
	 * @param contratoPraticaList
	 *            Lista de contrato pratica com mapa alocacao
	 * @param naturezaMandatoryList
	 *            Lista de natureza centro lucro
	 */
	private void validaContratoPratica(
			final List<ContratoPratica> contratoPraticaList,
			final List<NaturezaCentroLucro> naturezaMandatoryList) {

		LOGGER.info("Process Init: Contract Practice Validation");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil
				.getBundle(Constants.ENTITY_NAME_CONTRATO_PRATICA));
		super.addLog(super.BREAK_LINE);

		if (contratoPraticaList != null) {
			for (ContratoPratica contratoPratica : contratoPraticaList) {
				if (naturezaMandatoryList != null) {
					for (NaturezaCentroLucro natureza : naturezaMandatoryList) {
						ContratoPraticaCentroLucro cpcl = cpclService
								.findCPCLByContPratAndNatAndDate(
										contratoPratica, natureza, super
												.getDreMes().getDataMes());

						// Incrementa o count
						super.addCount();

						if (cpcl == null) {
							super.addLog(DateUtil.formatDate(new Date(),
									Constants.DEFAULT_DATE_PATTERN_FULL,
									Constants.DEFAULT_CALENDAR_LOCALE));
							super.addLog(super.LOG_ERROR);
							super.addLog(contratoPratica
									.getNomeContratoPratica());
							super.addLog(super.MSG_LOG_NOT_NCL_FILLED);
							super.addLog(super.BREAK_LINE);
							break;
						}
					}
				}
				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count
				super.addCount();

			}
		}

		LOGGER.info("Process End: Contract Practice Validation");
	}

	/**
	 * Valida��o de Custo Infra Base: Invalida se n�o achar o custo infra base
	 * por data de fechamento
	 * 
	 * @param cidadeBaseList
	 *            Lista de cidades base
	 */
	private void validaCustoInfraBase(final List<CidadeBase> cidadeBaseList) {

		LOGGER.info("Process Init: Infra Cost Validation");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil
				.getBundle(Constants.ENTITY_NAME_CUSTO_INFRA_BASE));
		super.addLog(super.BREAK_LINE);

		if (cidadeBaseList != null) {
			for (CidadeBase cidadeBase : cidadeBaseList) {
				CustoInfraBase custoInfraBase = custoInfraBaseService
						.findCustoInfBaseByDateAndCidadeBase(super.getDreMes()
								.getDataMes(), cidadeBase);
				if (custoInfraBase == null) {
					super.addLog(DateUtil.formatDate(new Date(),
							Constants.DEFAULT_DATE_PATTERN_FULL,
							Constants.DEFAULT_CALENDAR_LOCALE));
					super.addLog(super.LOG_ERROR);
					super.addLog(cidadeBase.getNomeCidadeBase());
					super.addLog(super.MSG_LOG_NOT_CUSTO_INF_BASE_ASSOCIATED);
					super.addLog(super.BREAK_LINE);

					// Atualiza o percentual concluido do progress bar
					super.notifyObservers(super.getPercentualConcluido());
					// Incrementa o count
					super.addCount();
				}
			}
		}
		LOGGER.info("Process End: Infra Cost Validation");
	}

	/**
	 * Valida��o de Faturas: Invalida as faturas n�o fechadas (status igual a
	 * Open e Aproved) por data de fechamento
	 * 
	 * @param faturaList
	 *            Lista de faturas nao fechadas
	 */
	private void validaFaturas(final List<Fatura> faturaList) {

		LOGGER.info("Process Init: Invoices Validation");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil.getBundle(Constants.ENTITY_NAME_FATURA));
		super.addLog(super.BREAK_LINE);

		if (faturaList != null) {
			for (Fatura fatura : faturaList) {
				super.addLog(DateUtil.formatDate(new Date(),
						Constants.DEFAULT_DATE_PATTERN_FULL,
						Constants.DEFAULT_CALENDAR_LOCALE));
				super.addLog(super.LOG_ERROR);
				super.addLog(super.MSG_LOG_INVOICEID);
				super.addLog(String.valueOf(fatura.getCodigoFatura()));
				super.addLog(super.MSG_LOG_NOT_SUBMITED);
				super.addLog(super.BREAK_LINE);

				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count
				super.addCount();
			}
		}

		LOGGER.info("Process End: Invoices Validation");

	}

	/**
	 * Valida��o de Mapa de Aloca��o: Invalida os Mapas de Aloca��o que n�o
	 * possueem receita (busca por data de fechamento)
	 * 
	 * @param mapaList
	 *            Lista de Mapa Alocacao sem receita
	 */
	private void validaMapaAlocacao(final List<MapaAlocacao> mapaList) {

		LOGGER.info("Process Init: Allocation Map without revenue validation");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil.getBundle(Constants.ENTITY_NAME_MAPA_ALOCACAO));
		super.addLog(" without ");
		super.addLog(BundleUtil.getBundle(Constants.ENTITY_NAME_RECEITA));
		super.addLog(super.BREAK_LINE);

		if (mapaList != null) {
			for (MapaAlocacao mapaAlocacao : mapaList) {
				ContratoPratica contratoPratica = mapaAlocacao
						.getContratoPratica();
				super.addLog(DateUtil.formatDate(new Date(),
						Constants.DEFAULT_DATE_PATTERN_FULL,
						Constants.DEFAULT_CALENDAR_LOCALE));
				super.addLog(super.LOG_WARN);
				super.addLog(contratoPratica.getNomeContratoPratica());
				super.addLog(super.MSG_LOG_MISSING);
				super.addLog(super.BREAK_LINE);

				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count
				super.addCount();
			}
		}

		LOGGER.info("Process End: Allocation Map without revenue validation");

	}

	/**
	 * Valida��o das Receitas: Invalida as Receitas que n�o est�o fechadas
	 * (status diferente de Published, Integrated e PD) por data de fechamento
	 * 
	 * @param receitaList
	 *            Lista de receitas n�o fechadas
	 */
	private void validaReceitas(final List<Receita> receitaList) {

		LOGGER.info("Process Init: Revenue validation");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil.getBundle(Constants.ENTITY_NAME_RECEITA));
		super.addLog(super.BREAK_LINE);

		if (receitaList != null) {
			for (Receita receita : receitaList) {
				super.addLog(DateUtil.formatDate(new Date(),
						Constants.DEFAULT_DATE_PATTERN_FULL,
						Constants.DEFAULT_CALENDAR_LOCALE));
				super.addLog(super.LOG_ERROR);
				super.addLog(receita.getContratoPratica()
						.getNomeContratoPratica());
				super.addLog(super.MSG_LOG_NOT_PUBLISHED);
				super.addLog(super.BREAK_LINE);

				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count
				super.addCount();
			}
		}

		LOGGER.info("Process End: Revenue validation");

	}

	/**
	 * Valida��o dos M�dulos: Invalida os M�dulos que possuem data de fechamento
	 * anterior a data
	 * 
	 * @param moduloList
	 *            Lista de modulos
	 */
	private void validaModulos(final List<Modulo> moduloList) {

		LOGGER.info("Process Init: Modules validation");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil.getBundle(Constants.ENTITY_NAME_MODULO));
		super.addLog(super.BREAK_LINE);

		if (moduloList != null) {
			for (Modulo modulo : moduloList) {
				if (super.getDreMes().getDataMes()
						.compareTo(modulo.getDataFechamento()) > 0) {
					super.addLog(DateUtil.formatDate(new Date(),
							Constants.DEFAULT_DATE_PATTERN_FULL,
							Constants.DEFAULT_CALENDAR_LOCALE));
					super.addLog(super.LOG_ERROR);
					super.addLog(modulo.getNomeModulo());
					super.addLog(super.MSG_LOG_NOT_CLOSED);
					super.addLog(super.BREAK_LINE);
				}

				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count
				super.addCount();
			}
		}

		LOGGER.info("Process End: Modules validation");
	}
}