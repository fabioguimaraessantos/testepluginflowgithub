package com.ciandt.pms.business.service.impl;

import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.ciandt.pms.business.service.IPessoaBillabilityService;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ClosingDREPessoaVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICustoRealizadoService;
import com.ciandt.pms.business.service.ICustoTceGrupoCustoService;
import com.ciandt.pms.business.service.IValidacaoPessoaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;

/**
 * Servico para realizar o processo de Resource Validation.
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * @since 16/10/2013
 */
@Service
public class DreProcessoValidaAlocacaoService extends
		AbstractDreProcessoExecutavelService {

	/** {@link Logger}. */
	private static final Logger LOGGER = LogManager
			.getLogger(DreProcessoValidaAlocacaoService.class);

	/** Instancia do servico - ValidacaoPessoa. */
	@Autowired
	private IValidacaoPessoaService validacaoPessoaService;

	/** Instancia do servico PessoaBillability. */
	@Autowired
	private IPessoaBillabilityService pessoaBillabilityService;

	/** Instancia do servico CustoRealizado. */
	@Autowired
	private ICustoRealizadoService custoRealizadoService;
	
    /** Instancia do servico CustoTceGrupoCusto. */
    @Autowired
    private ICustoTceGrupoCustoService custoTceGrupoCustoService;

	/**
	 * Valida Alocacao
	 */
	public void process() throws DreProcessoExecutavelException {

		super.setCount(1d);
		super.setTotalRegistros(2d);
		
		this.validaAlocacao();

	}

	/**
	 * Valida Alocacao
	 */
	private void validaAlocacao() throws DreProcessoExecutavelException {

		LOGGER.info("Process Init: Allocation validation");

		final Date dataMes = super.getDreMes().getDataMes();

		if (super.getPessoas() != null && super.getPessoas().size() > 0) {

			super.addLog("Validated Resources: ");
			super.addLog(super.BREAK_LINE);
			
			// Adiciona ao total de registros o total da lista de pessas
			super.addTotalRegistros(super.getPessoas().size());

			List<ClosingDREPessoaVO> peopleInformation = validacaoPessoaService.loadPeopleInformation(
					super.getPessoas(), super.getDreMes().getDataMes());

			for (ClosingDREPessoaVO pessoa : peopleInformation) {

				// Se o processo estiver sendo executado por login, pode ja
				// existir registro na CUSTO_REALIZADO e na
				// CUSTO_TCE_GRUPO_CUSTO para o login, entao e necessario
				// excluir
				if (Constants.SIM.equals(super.getDreProcesso().getIndicadorPorLogin())) {

					// Lista de CUSTO_REALIZADO por login e dataMes
					List<CustoRealizado> listaCustoRealizado = custoRealizadoService
							.findCustRealizByPessoaAndDataMes(pessoa.getPerson(), dataMes);

					// Exclui a CUSTO_REALIZADO
					if (listaCustoRealizado != null) {
						for (CustoRealizado cr : listaCustoRealizado) {
							custoRealizadoService.removeCustoRealizado(cr);
						}
					}
					
					// Lista de CUSTO_TCE_GRUPO_CUSTO por login e dataMes
					List<CustoTceGrupoCusto> listaCustoTCE = custoTceGrupoCustoService
							.findCusTceGCByPessoaAndDataMes(pessoa.getPerson(), dataMes);

					// Exclui a CUSTO_TCE_GRUPO_CUSTO
					if (listaCustoTCE != null) {
						for (CustoTceGrupoCusto tce : listaCustoTCE) {
							custoTceGrupoCustoService.removeCustoTceGrupoCusto(tce);
						}
					}

				}

				try {
					this.validacaoPessoaService.validatePeople(pessoa, dataMes);

					super.addLog("- ");
					super.addLog(pessoa.getPerson().getCodigoLogin());
					super.addLog(super.BREAK_LINE);
				} catch (Exception e) {
					super.addLog("ERROR on validating " + pessoa.getLogin());
					super.addLog("  -  Message: " + e.getMessage());
					super.addLog(super.BREAK_LINE);
					System.out.println("ERROR on validating " + pessoa.getLogin());
					System.out.println(e.getMessage());
				}

				// Atualiza o percentual concluido do progress bar
				super.notifyObservers(super.getPercentualConcluido());
				// Incrementa o count
				super.addCount();
			}

			Iterator<FacesMessage> messageIterator = FacesContext
					.getCurrentInstance().getMessages("validatePessoa");

			if (!messageIterator.hasNext()) {
				Messages.showSucess("validatePessoa",
						Constants.VALIDATE_PERSON_SUCCESS_VALIDATE);
			} else {
				while (messageIterator.hasNext()) {
					FacesMessage msg = messageIterator.next();
					super.addLog(msg.getDetail());
					super.addLog(super.BREAK_LINE);
				}
			}
		}

		LOGGER.info("Process End: Allocation validation");
	}

	private List<AlocacaoPeriodo> getAllocationPeriodsFromPerson(Map<String, List<AlocacaoPeriodo>> allocationPeriods, Pessoa person) {
		List<AlocacaoPeriodo> periods = allocationPeriods.get(person.getCodigoLogin());
		if (periods != null) {
			return periods;
		} else {
			return new ArrayList<AlocacaoPeriodo>();
		}
	}
}