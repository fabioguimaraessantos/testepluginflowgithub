package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IApropriacaoHoraExtraService;
import com.ciandt.pms.business.service.IApropriacaoPlantaoService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.ICustoPessoaMesService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPessoaGrupoCustoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IPessoaTipoContratoService;
import com.ciandt.pms.business.service.IValidacaoPessoaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.ApropriacaoHoraExtra;
import com.ciandt.pms.model.ApropriacaoPlantao;
import com.ciandt.pms.model.CustoPessoaCp;
import com.ciandt.pms.model.CustoPessoaGc;
import com.ciandt.pms.model.CustoPessoaMes;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaGrupoCusto;

/**
 * Servico para realizar o processo de Duty Hours and Overtime.
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * @since 16/10/2013
 */
@Service
public class DreProcessoRegistraPlantaoHorasExtrasService extends
		AbstractDreProcessoExecutavelService {

	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(DreProcessoRegistraPlantaoHorasExtrasService.class);

	/** Instancia do servico - ApropriacaoPlantaoService. */
	@Autowired
	private IApropriacaoPlantaoService apropriacaoPlantaoService;

	/** Instancia do servico - Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico - PessoaTipoContrato. */
	@Autowired
	private IPessoaTipoContratoService pessoaTipoContratoService;

	/** Instancia do servico - ApropriacaoHoraExtraService. */
	@Autowired
	private IApropriacaoHoraExtraService apropriacaoHoraExtraService;

	/** Instancia do servico - PessoaGrupoCusto. */
	@Autowired
	private IPessoaGrupoCustoService pessoaGrupoCustoService;

	/** Instancia do servico - ValidacaoPessoa. */
	@Autowired
	private IValidacaoPessoaService validacaoPessoaService;

	/** Instancia do servico - GrupoCusto. */
	@Autowired
	private IGrupoCustoService grupoCustoService;

	/** Instancia do Servico da entidade ContratoPratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do Servico da entidade CustoPessoaMesService. */
	@Autowired
	private ICustoPessoaMesService custoPessoaMesService;

	/** Instancia do Servico da entidade MoedaService. */
	@Autowired
	private IMoedaService moedaService;

	/**
	 * Registra Plantao e Horas Extras
	 */
	public void process() throws DreProcessoExecutavelException {

		synchronized (CustoPessoaMesService.class) {

			// Para controle do progress bar: inicia o count com 1 e
			// totalRegistros com 2
			super.setCount(1d);
			super.setTotalRegistros(2d);

			final Date dataMes = super.getDreMes().getDataMes();

			List<CustoPessoaMes> custoPessoaMesList = new ArrayList<CustoPessoaMes>();
			List<ApropriacaoPlantao> plantaoList = new ArrayList<ApropriacaoPlantao>();
			List<ApropriacaoHoraExtra> horaExtraList = new ArrayList<ApropriacaoHoraExtra>();

			if (Constants.NO.equals(super.getDreProcesso()
					.getIndicadorPorLogin())) {// Se o processo estiver sendo
												// executado pelo menu "DRE"

				// Busca o custo pessoa mes por MM/yyyy
				custoPessoaMesList = custoPessoaMesService
						.findCustPessMesByDataMes(dataMes);

				// Busca a lista de Plantao por MM/yyyy, a busca esta sendo
				// feita neste momento para controle do progress bar.
				plantaoList = apropriacaoPlantaoService
						.findApropriacaoPlantaoByData(dataMes);

				// Busca a lista de Horas Extras por MM/yyyy, a busca esta sendo
				// feita neste momento para controle do progress bar.
				horaExtraList = apropriacaoHoraExtraService
						.findApropriacaoHoraExtraByData(dataMes);

			} else if (Constants.SIM.equals(super.getDreProcesso()
					.getIndicadorPorLogin())) { // Se o processo estiver sendo
												// executado por Login (menu
												// "DRE By Login")

				for (Pessoa pessoa : super.getPessoas()) {

					// Busca o custo pessoa mes por MM/yyyy para cada login
					custoPessoaMesList
							.addAll(custoPessoaMesService
									.findCustPessMesByPessoaAndDataMes(pessoa,
											dataMes));

					// Busca a lista de Plantao por MM/yyyy e login, a busca
					// esta sendo feita neste momento para controle do progress
					// bar.
					plantaoList.addAll(apropriacaoPlantaoService
							.findApropriacaoPlantaoByDataAndCdPessoa(dataMes,
									pessoa));

					// Busca a lista de Horas Extras por MM/yyyy e login, a
					// busca esta sendo feita neste momento para controle do
					// progress bar.
					horaExtraList.addAll(apropriacaoHoraExtraService
							.findApropriacaoHoraExtraByDataAndCdPessoa(dataMes,
									pessoa));
				}
			}

			// Adiciona o total de plantao ao totalRegistros
			super.addTotalRegistros(plantaoList.size());

			// Adiciona o total de horas extras ao totalRegistros
			super.addTotalRegistros(horaExtraList.size());

			// remove todos os registros do mes para possibilitar nova inser��o
			this.removeInformacoesCustoPessoa(custoPessoaMesList);

			// REGISTRA PLANTAO
			this.registraPlantao(plantaoList, dataMes);

			// REGISTRA HORAS EXTRAS
			this.registraHorasExtras(horaExtraList, dataMes);

			// Adiciona mensagens de erro ou sucesso para salvar no banco
			this.adicionaMensagensAoLog();
		}

	}

	/**
	 * Caso j� exista, remove informa��es de CustoPessoaMes, CustoPessoaGC e
	 * CustoPessoaCP
	 * 
	 * @param custoPessoaMesList
	 *            Lista de {@link CustoPessoaMes}
	 */
	private void removeInformacoesCustoPessoa(
			List<CustoPessoaMes> custoPessoaMesList) {

		// Adiciona o total de custo pessoa mes ao totalRegistros (para
		// controle do progress bar)
		super.addTotalRegistros(custoPessoaMesList.size());

		for (CustoPessoaMes custoPessoaMes : custoPessoaMesList) {
			custoPessoaMesService.removeCustoPessoaMes(custoPessoaMes);

			// Atualiza o progress bar
			notifyObservers(super.getPercentualConcluido());
			super.addCount();
		}

	}

	/**
	 * Processa informa��es de Horas Extras
	 * 
	 * @param horaExtraList
	 *            Lista de {@link ApropriacaoHoraExtra}
	 * @param dataMes
	 *            Data do fechamento
	 */
	private void registraHorasExtras(
			final List<ApropriacaoHoraExtra> horaExtraList, final Date dataMes)
			throws DreProcessoExecutavelException {

		LOGGER.info("Process Init: Register Overtime");

		for (ApropriacaoHoraExtra horaExtra : horaExtraList) {

			// Apropria valores de Hora extra
			apropriaValores(horaExtra.getPessoa().getCodigoPessoa(), dataMes,
					horaExtra.getMoeda().getCodigoMoeda(),
					Constants.CUSTO_PESSOA_MES_ORIGEM_HORAS_EXTRAS,
					horaExtra.getValorHoraExtra());
		}

		LOGGER.info("Process End: Register Overtime");
	}

	/**
	 * Processa informa��es de Plant�o
	 * 
	 * @param plantaoList
	 *            Lista de {@link ApropriacaoPlantao}
	 * @param dataMes
	 *            Data fechamento
	 */
	private void registraPlantao(final List<ApropriacaoPlantao> plantaoList,
			final Date dataMes) throws DreProcessoExecutavelException {

		LOGGER.info("Process Init: Register Duty Hours");

		for (ApropriacaoPlantao plantao : plantaoList) {

			// Apropria valores de Plant�o
			apropriaValores(plantao.getPessoa().getCodigoPessoa(), dataMes,
					plantao.getMoeda().getCodigoMoeda(),
					Constants.CUSTO_PESSOA_MES_ORIGEM_PLANTAO,
					plantao.getValorPlantao());
		}

		LOGGER.info("Process End: Register Duty Hours");
	}

	/**
	 * Apropria valores de Plant�o e Hora Extra
	 * 
	 * @param codigoPessoa
	 *            C�digo da pessoa
	 * @param dataMes
	 *            Data do fechamento
	 * @param siglaMoeda
	 *            Sigla da moeda do valor apropriado para HE ou PL
	 * @param indicadorOrigem
	 *            PL (Plant�o) e HE (Hora Extra)
	 * @param valorApropriado
	 *            Valor total de PL ou HE
	 * 
	 */
	private void apropriaValores(final Long codigoPessoa, final Date dataMes,
			final Long cdMoeda, final String indicadorOrigem,
			final BigDecimal valorApropriado) {

		Boolean success = Boolean.valueOf(true);

		double totalAlocacao = Double.valueOf(0);

		Pessoa pessoa = pessoaService.findPessoaById(codigoPessoa);

		PessoaGrupoCusto pgc = pessoaGrupoCustoService
				.findPessGCByPessoaAndDate(pessoa, dataMes);

		if (pgc == null) {
			success = Boolean.valueOf(false);

			// Adiciona mensagem de erro no FacesContext
			Messages.showError(
					Constants.REGISTER_PL_AND_HE_COSTS_ID,
					Constants.REGISTER_PL_HE_ERROR_VALIDATE_RESOURCE_COST_GROUP,
					pessoa.getCodigoLogin());
		}

		// Busca a moeda pelo codigo, se n�o encontrar, seta flag "success" para
		// false
		Moeda moeda = moedaService.findMoedaById(cdMoeda);
		if (moeda == null) {
			success = Boolean.valueOf(false);

			// Adiciona mensagem de erro no FacesContext
			Messages.showError(Constants.REGISTER_PL_AND_HE_COSTS_ID,
					Constants.REGISTER_PL_HE_ERROR_VALIDATE_CODE_CURRENCY,
					cdMoeda.toString());
		}

		// Se algum Recurso est� incompleto, n�o grava o CustoPessoaMes,
		// CustoPessoaGc e CustoPessoaCp e vai para pr�ximo
		if (!success) {

			// Atualiza o progress bar
			notifyObservers(super.getPercentualConcluido());
			super.addCount();

		} else {

			CustoPessoaMes cpm = new CustoPessoaMes();
			cpm.setPessoa(pessoa);
			cpm.setDataMes(dataMes);
			cpm.setIndicadorOrigem(indicadorOrigem);
			cpm.setMoeda(moeda);

			// Cria o CustoPessoaMes
			custoPessoaMesService.createCustoPessoaMes(cpm);

			// Recupera a lista de AlocacaoPeriodo (aloca��es dos
			// mapas) por pessoa e data de fechamento
			List<AlocacaoPeriodo> alocacaoPeriodoList = validacaoPessoaService
					.getAlocacaoPeriodoList(pessoa, dataMes);

			// Itera a lista de aloca��o para obter o percentual total de
			// aloca��o
			for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoList) {
				totalAlocacao += alocacaoPeriodo.getPercentualAlocacaoPeriodo()
						.doubleValue();
			}

			// Se tem Alocacao no mes de fechamento, apropria no
			// ContratoPratica, caso contr�rio apropria no GrupoCusto.
			if (alocacaoPeriodoList.size() > 0) {
				List<CustoPessoaCp> cpcpList = new ArrayList<CustoPessoaCp>();

				for (AlocacaoPeriodo ap : alocacaoPeriodoList) {
					CustoPessoaCp cpcp = new CustoPessoaCp();
					cpcp.setCustoPessoaMes(cpm);
					// Busca o contrato pratica por id
					cpcp.setContratoPratica(contratoPraticaService
							.findContratoPraticaById(ap.getAlocacao()
									.getMapaAlocacao().getContratoPratica()
									.getCodigoContratoPratica()));
					// Calcula o percentual de aloca��o
					cpcp.setPercentualApropriado(totalAlocacao != 0 ? BigDecimal
							.valueOf(ap.getPercentualAlocacaoPeriodo()
									.doubleValue() / totalAlocacao)
							: BigDecimal.valueOf(0L));
					// Seta o valor de Plantao ou Hora Extra
					cpcp.setValorApropriado(valorApropriado);

					cpcpList.add(cpcp);
				}

				cpm.setCustoPessoaCps(cpcpList);
			} else {
				CustoPessoaGc cpgc = new CustoPessoaGc();
				cpgc.setCustoPessoaMes(cpm);
				// Busca o grupo de custo por id
				cpgc.setGrupoCusto(grupoCustoService.findGrupoCustoById(pgc
						.getGrupoCusto().getCodigoGrupoCusto()));
				// Seta o percentual para 1
				cpgc.setPercentualApropriado(BigDecimal.valueOf(1));
				// Seta o valor de Plantao ou Hora Extra
				cpgc.setValorApropriado(valorApropriado);

				List<CustoPessoaGc> cpgcList = new ArrayList<CustoPessoaGc>();
				cpgcList.add(cpgc);

				cpm.setCustoPessoaGcs(cpgcList);
			}

			// atualiza o CustoPessoaMes e as listas de CustoPessoaCp e
			// CustoPessoaGc
			custoPessoaMesService.updateCustoPessoaMes(cpm);

			// Atualiza o progress bar
			notifyObservers(super.getPercentualConcluido());
			super.addCount();
		}
	}

	/**
	 * Itera o FacesMessage para adicionar ao log a mensagem de erro que ser�
	 * gravado no banco (coluna DREP_TX_LOG da tabela DRE_PROCESSO). Se n�o
	 * possuir mensagem de erro, ent�o adiciona mensagem de sucesso.
	 */
	private void adicionaMensagensAoLog() {

		Iterator<FacesMessage> messageIterator = FacesContext
				.getCurrentInstance().getMessages(
						Constants.REGISTER_PL_AND_HE_COSTS_ID);

		if (messageIterator != null && messageIterator.hasNext()) {

			// Itera o FacesMessage para setar as mensagens de erro
			while (messageIterator.hasNext()) {
				FacesMessage msg = messageIterator.next();
				super.addLog(msg.getDetail());
				super.addLog(super.BREAK_LINE);
			}

		} else {

			// Se n�o tiver mensagens de erro no FacesMessage, ent�o
			// completou o processo com sucesso. Seta no log mensagem de sucesso
			super.addLog(BundleUtil
					.getBundle(Constants.MSG_SUCCESS_SUCCESFULLY_COMPLETED));

		}
	}

}