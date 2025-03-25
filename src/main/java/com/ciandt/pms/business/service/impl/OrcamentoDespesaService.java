package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IOrcamentoDespesaService;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcamentoDespesaDao;

/**
 * 
 * A classe OrcamentoDespesaService proporciona as funcionalidades de servico
 * para a entidade OrcamentoDespesa.
 * 
 * @since 24/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class OrcamentoDespesaService implements IOrcamentoDespesaService {

	/** Instancia de dao. */
	@Autowired
	private IOrcamentoDespesaDao dao;

	/**
	 * Cria OrcamentoDespesa.
	 * 
	 * @param entity
	 *            entidade.
	 * @return true.
	 */
	@Override
	@Transactional
	public void createOrcamentoDespesa(final OrcamentoDespesa entity) {
		dao.create(entity);
	}

	/**
	 * Remove (DELETE LOGICO) um {@link OrcamentoDespesa} e seus respectivos
	 * ContratoPraticaOrcDesp.
	 * 
	 * @param entity
	 *            orcamentoDespesa.
	 * @return true.
	 */
	@Transactional
	public Boolean removeOrcamentoDespesa(final OrcamentoDespesa entity) {

		entity.setIndicadorDeleteLogico(Constants.SIM);
		this.updateOrcamentoDespesa(entity);

		return Boolean.TRUE;
	}

	/**
	 * Update OrcamentoDespesa. Ao passar por este método o
	 * {@link OrcamentoDespesa} voltará a ser sincronizado no GAE.
	 * 
	 * @param entity
	 *            orcamentoDespesa.
	 * @return true.
	 */
	@Transactional
	public Boolean updateOrcamentoDespesa(final OrcamentoDespesa entity) {
		// setta o TravelBudget para ser sincronizado (GAE)
		entity.setIndicadorSync(Constants.NO);
		dao.update(entity);
		return Boolean.TRUE;
	}

	/**
	 * Busca todas os registros de OrcamentoDespesa.
	 * 
	 * @return listResult.
	 */
	public List<OrcamentoDespesa> findAll() {
		return dao.findAll();
	}

	/**
	 * Busca entidade por id.
	 * 
	 * @param id
	 *            id.
	 * @return entidade.
	 */
	public OrcamentoDespesa findOrcamentoDespesaById(final Long id) {
		return dao.findById(id);
	}

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
	public OrcamentoDespesa findOrcamentoDespByNameAndTipoOrcDespesa(
			final String nome, final String tipoOrcDesp) {
		return dao.findByNameAndTipoOrcDespesa(nome, tipoOrcDesp);
	}

	/** VALIDACAO TB */
	
	/**
	 * Quebra o TB em meses (01/01/2013 - 01/02/2013 - 01/03/2013 - ...)
	 * 
	 * @param orcDesp
	 * @return
	 */
	public List<Date> quebraOrcamentoDespesaMeses(final OrcamentoDespesa orcDesp) {
		List<Date> listResult = new ArrayList<Date>();
		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		Calendar dataUltimoDiaMes = Calendar.getInstance();
		Calendar dataMes = Calendar.getInstance();

		Calendar dataAux = Calendar.getInstance();

		dataInicial.setTime(orcDesp.getDataInicio());
		dataFinal.setTime(orcDesp.getDataFim());

		dataAux.setTime(dataInicial.getTime());

		while (dataAux.before(dataFinal)) {

			dataUltimoDiaMes = this.ultimoDiaMes(dataAux);

			if (dataUltimoDiaMes.after(dataFinal)) {
				dataUltimoDiaMes.setTime(dataFinal.getTime());
			}

			dataMes.setTime(dataAux.getTime());

			// data a ser persistida (getTime)
			dataMes = this.primeiroDiaMes(dataMes);

			listResult.add(dataMes.getTime());

			dataUltimoDiaMes.add(Calendar.DAY_OF_MONTH, 1);
			dataAux = dataUltimoDiaMes;

		}

		return listResult;
	}
	
	/**
	 * Busca o ultimo dia do mes passado por parametro.
	 * 
	 * @param data
	 * @return
	 */
	public Calendar ultimoDiaMes(Calendar data) {

		Calendar dataResult = Calendar.getInstance();
		dataResult.setTime(data.getTime());

		dataResult.add(Calendar.MONTH, 1);
		dataResult.set(Calendar.DAY_OF_MONTH, 1);
		dataResult.add(Calendar.DAY_OF_MONTH, -1);

		return dataResult;
	}

	/**
	 * Busca o primeiro dia do mes passado por parametro.
	 * 
	 * @param data
	 * @return
	 */
	public Calendar primeiroDiaMes(Calendar data) {

		Calendar dataResult = Calendar.getInstance();
		dataResult.setTime(data.getTime());

		dataResult.add(Calendar.MONTH, -1);
		dataResult = this.ultimoDiaMes(dataResult);
		dataResult.add(Calendar.DAY_OF_MONTH, 1);

		return dataResult;
	}

	/**
	 * Calcula a diferenca de dias de um TravelBudget.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public long diferenciaDiasTB(Date dataInicial, Date dataFinal) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(dataInicial);
		end.setTime(dataFinal);
		Date startDate = start.getTime();
		Date endDate = end.getTime();
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		long diffTime = endTime - startTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);

		return diffDays + 1;
	}
}