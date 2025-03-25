package com.ciandt.pms.business.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Voucher;

/**
 * 
 * A classe IOrcamentoDespesaService proporciona as funcionalidades de iterface
 * para OrcamentoDespesaService.
 * 
 * @since 24/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface IOrcamentoDespesaService {

	/**
	 * Cria OrcamentoDespesa.
	 * 
	 * @param entity
	 *            entidade.
	 * 
	 */
	void createOrcamentoDespesa(final OrcamentoDespesa entity);

	/**
	 * Remove OrcamentoDespesa e seus respectivos ContratoPraticaOrcDesp.
	 * 
	 * @param entity
	 *            orcamentoDespesa.
	 * @return true.
	 */
	Boolean removeOrcamentoDespesa(final OrcamentoDespesa entity);

	/**
	 * Update OrcamentoDespesa.
	 * 
	 * @param entity
	 *            orcamentoDespesa.
	 * @return true.
	 */
	Boolean updateOrcamentoDespesa(final OrcamentoDespesa entity);

	/**
	 * Busca todas os registros de OrcamentoDespesa.
	 * 
	 * @return listResult.
	 */
	List<OrcamentoDespesa> findAll();

	/**
	 * Busca entidade por id.
	 * 
	 * @param id
	 *            id.
	 * @return entidade.
	 */
	OrcamentoDespesa findOrcamentoDespesaById(final Long id);

	/**
	 * Obtem um {@link OrcamentoDespesa} de acordo com o nome e tipo de
	 * orcamento informado.
	 * 
	 * @param nome
	 *            nome do {@link OrcamentoDespesa}
	 * 
	 * @param tipoOrcDesp
	 *            a sigla referente ao tipo de {@link OrcamentoDespesa}
	 * 
	 * @return {@link OrcamentoDespesa}
	 * 
	 */
	OrcamentoDespesa findOrcamentoDespByNameAndTipoOrcDespesa(
			final String nome, final String tipoOrcDesp);

	/**
	 * Quebra o TB em meses (01/01/2013 - 01/02/2013 - 01/03/2013 - ...)
	 * 
	 * @param orcDesp
	 * @return
	 */
	List<Date> quebraOrcamentoDespesaMeses(final OrcamentoDespesa orcDesp);

	/**
	 * Calcula a diferenca de dias de um TravelBudget.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	long diferenciaDiasTB(Date dataInicial, Date dataFinal);

	/**
	 * Busca o primeiro dia do mes passado por parametro.
	 * 
	 * @param data
	 * @return
	 */
	Calendar primeiroDiaMes(Calendar data);

	/**
	 * Busca o ultimo dia do mes passado por parametro.
	 * 
	 * @param data
	 * @return
	 */
	Calendar ultimoDiaMes(Calendar data);

}