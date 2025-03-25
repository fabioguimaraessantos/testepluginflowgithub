package com.ciandt.pms.business.service.impl;

import java.util.*;

import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.model.vo.MapaProspectComAlocacaoResultsetVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPapelAlocacaoService;
import com.ciandt.pms.business.service.IPessoaGrupoCustoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;
import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaGrupoCusto;
import com.ciandt.pms.util.DateUtil;

/**
 * Servico para realizar o processo de validacao do TCE Validation.
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * @since 16/10/2013
 */
@Service
public class DreProcessoValidaTCEService extends
		AbstractDreProcessoExecutavelService {

	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(DreProcessoValidaTCEService.class);

	/** Instancia do Servico da entidade PapelAlocacao. */
	@Autowired
	private IPapelAlocacaoService papelAlocacaoService;

	/** Instancia do servico PessoaGrupoCusto. */
	@Autowired
	private IPessoaGrupoCustoService pessoaGrupoCustoService;

	/** Intancia do servi�o PessoaService. */
	@Autowired
	private IPessoaService pessoaService;

	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/**
	 * Valida Papel Alocacao que nao possui TCE e Valida recursos que n�o
	 * possuem grupo de custo e custo TCE
	 */
	public void process() throws DreProcessoExecutavelException {

		super.setCount(1d); // Progress bar
		double totalRegistros = 2d;  // Progress bar 

		// Lista PapelAlocacao sem associa��o com o TcePapelAlocacao, realiza
		// esta consulta neste momento para possibilitar calculo do progress bar
		final List<PapelAlocacao> papelAlocList = papelAlocacaoService
				.findPapelAlocAllNotTceAssociated(super.getDreMes()
						.getDataMes());

		if (papelAlocList != null) {
			// Soma ao totalRegistros, o tamanho da lista de aloca��o para
			// controle do progress bar
			totalRegistros += papelAlocList.size();
		}

		// Busca lista de pessoas n�o validadas na data do fechamento, realiza
		// esta consulta neste momento para possibilitar calculo do progress bar
		final List<Pessoa> pessoaList = pessoaService
				.findPessoaNotValidatedByDate(super.getDreMes().getDataMes());

		if (pessoaList != null) {
			// Soma ao totalRegistros o tamanho da lista de pessoas para
			// controle do progress bar
			totalRegistros += pessoaList.size();
		}

		final List<MapaProspectComAlocacaoResultsetVO> prospectMaps = contratoPraticaService.findProspectMapWithAllocation();

		if (prospectMaps != null) {
			totalRegistros += prospectMaps.size();
		}

		super.setTotalRegistros(totalRegistros);  // Progress bar

		// Chama o m�todo que valida se todos os papeis aloca��o possuem TCE
		this.validaPapelAlocacao(papelAlocList);

		// Chama o m�todo que valida se todas a pessoas que ainda n�o foram
		// validadas possuem grupo de custo e custo TCE
		this.validaGrupoCustoECustoTCE(pessoaList);


		this.validaMapaAlocacaoProspect(prospectMaps);

	}

	private void validaMapaAlocacaoProspect(List<MapaProspectComAlocacaoResultsetVO> prospectMaps) {
		LOGGER.info("Process Init: Validate Prospect Allocation Map");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil.getBundle(Constants.MSG_DRE_PROCESS_VALIDATE_PROSPECT_ALLOCATION_MAP));
		super.addLog(super.BREAK_LINE);

		if (prospectMaps != null) {
			for (MapaProspectComAlocacaoResultsetVO map : prospectMaps) {
				super.addLog(DateUtil.formatDate(new Date(),
						Constants.DEFAULT_DATE_PATTERN_FULL,
						Constants.DEFAULT_CALENDAR_LOCALE));
				super.addLog(super.LOG_ERROR);
				super.addLog(BundleUtil.getBundle(
						Constants.MSG_ERROR_DRE_PROCESS_VALIDATE_ALLOCATION_MAP,
						map.getNomeMapaAlocacao()));
				super.addLog(super.BREAK_LINE);

				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count (progress bar)
				super.addCount();
			}
		}
		LOGGER.info("Process End: Validate Prospect Allocation Map");
	}

	/**
	 * Valida Papel Alocacao por data de fechamento. Caso exista algum papel
	 * aloca��o que nao possui TCE no per�odo, grava o log com as invalidacoes.
	 */
	private void validaPapelAlocacao(List<PapelAlocacao> papelAlocList) {

		LOGGER.info("Process Init: Validate Resource Role");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil.getBundle(Constants.ENTITY_NAME_PAPEL_ALOCACAO));
		super.addLog(super.BREAK_LINE);

		if (papelAlocList != null) {

			for (PapelAlocacao papelAlocacao : papelAlocList) {
				super.addLog(DateUtil.formatDate(new Date(),
						Constants.DEFAULT_DATE_PATTERN_FULL,
						Constants.DEFAULT_CALENDAR_LOCALE));
				super.addLog(super.LOG_ERROR);
				super.addLog(papelAlocacao.getNomePapelAlocacao());
				super.addLog(super.MSG_LOG_NOT_TCE_ASSOCIATED);
				super.addLog(super.BREAK_LINE);

				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count (progress bar)
				super.addCount();
			}
		}

		LOGGER.info("Process End: Validate Resource Role");
	}

	/**
	 * Valida se todas as pessoas est�o associadas a algum grupo de custo e se
	 * todas as pessoas possuem custo TCE para o mes/ano do fechamento.
	 * 
	 * @param pessoaList
	 *            Lista de pessoas que ainda n�o est�o validadas para o mes/ano
	 *            de fechamento {@link Pessoa}
	 */
	private void validaGrupoCustoECustoTCE(List<Pessoa> pessoaList) {

		LOGGER.info("Process Init:  Validate Cost Group and Cost TCE by person and closing date");

		super.addLog(super.TITLE_LINE);
		super.addLog(BundleUtil
				.getBundle(Constants.MSG_DRE_PROCESS_VALIDATE_COST_GROUP_AND_COST_TCE));
		super.addLog(super.BREAK_LINE);

		if (pessoaList != null) {
			List<Pessoa> withoutCostCenter = this.filterPeopleWithoutCostCenterAndBillability(pessoaList);
			for (Pessoa pessoa : withoutCostCenter) {
				super.addLog(DateUtil.formatDate(new Date(),
						Constants.DEFAULT_DATE_PATTERN_FULL,
						Constants.DEFAULT_CALENDAR_LOCALE));
				super.addLog(super.LOG_ERROR);
				super.addLog(BundleUtil
						.getBundle(
								Constants.MSG_ERROR_DRE_PROCESS_VALIDATE_COST_GROUP,
								pessoa.getCodigoLogin()));
				super.addLog(super.BREAK_LINE);

				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count
				super.addCount();
			}
		}

		LOGGER.info("Process End: Validate Cost Group and Cost TCE by person and closing date");
	}

	private List<Pessoa> filterPeopleWithoutCostCenterAndBillability(List<Pessoa> people) {
		Set<Long> peopleCodes = this.getPeopleCodes(people);
		List<PessoaGrupoCusto> peopleCostCenter = pessoaGrupoCustoService.findByPeopleCodeInAndDate(peopleCodes, super.getDreMes().getDataMes());
		Set<Long> peopleCostCenterCodes = new HashSet<Long>();
		List<Pessoa> peopleWithoutCostCenter = new ArrayList<Pessoa>();

		for (PessoaGrupoCusto pgc : peopleCostCenter) {
			peopleCostCenterCodes.add(pgc.getPessoa().getCodigoPessoa());
		}
		for (Pessoa person : people) {
			if (!peopleCostCenterCodes.contains(person.getCodigoPessoa())) {
				peopleWithoutCostCenter.add(person);
			}
		}

		return peopleWithoutCostCenter;
	}

	private Set<Long> getPeopleCodes(List<Pessoa> pessoas) {
		Set<Long> peopleCodes = new HashSet<Long>();
		for (Pessoa person : pessoas) {
			peopleCodes.add(person.getCodigoPessoa());
		}
		return peopleCodes;
	}
}