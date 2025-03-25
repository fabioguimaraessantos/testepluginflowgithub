package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IOrcDespesaMesService;
import com.ciandt.pms.business.service.IOrcamentoDespesaService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.OrcDespesaMes;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcDespesaMesDao;

/**
 * 
 * @author peter
 * 
 */
@Service
public class OrcDespesaMesService implements IOrcDespesaMesService {

	/**
	 * Dao da entidade.
	 */
	@Autowired
	private IOrcDespesaMesDao orcDespesaMesDao;

	/**
	 * Servico de orcDespesa.
	 */
	@Autowired
	private IOrcamentoDespesaService orcamentoDespesaService;

	/**
	 * Cria um OrcDespesaMes.
	 * 
	 * @param entity
	 */
	@Transactional
	public void createOrcDespesaMes(OrcDespesaMes entity) {
		orcDespesaMesDao.create(entity);
	}

	/**
	 * Busca os orcDespesaMes para saber o total já emitido de TravelBudget por
	 * clob e meses.
	 * 
	 * @return
	 */
	public List<OrcDespesaMes> findOrcDespesaMesByClobAndPeriod(
			ContratoPratica clob, Date dataInicial, Date dataFinal) {
		return orcDespesaMesDao.findOrcDespesaMesByClobAndPeriod(clob,
				dataInicial, dataFinal);
	}

	/**
	 * Cria os OrcDespesaMes a partir de um OrcamentoDespesa, quebrando o mesmo
	 * em meses.
	 * 
	 * @param orcDesp
	 */
	public void createOrcDespesaMesByOrcDespesa(OrcamentoDespesa orcDesp) {

		Calendar dataMesPersist = Calendar.getInstance();
		BigDecimal percentualPersist;

		Calendar dataInicial = Calendar.getInstance();
		Calendar dataFinal = Calendar.getInstance();
		Calendar dataUltimoDiaMes = Calendar.getInstance();
		Calendar dataAux = Calendar.getInstance();

		OrcDespesaMes orcDespesaMes;

		dataInicial.setTime(orcDesp.getDataInicio());
		dataFinal.setTime(orcDesp.getDataFim());

		float totalDiasPeriodoTB = orcamentoDespesaService.diferenciaDiasTB(
				orcDesp.getDataInicio(), orcDesp.getDataFim());
		

		dataAux.setTime(dataInicial.getTime());

		while (dataAux.before(dataFinal)) {

			orcDespesaMes = new OrcDespesaMes();

			dataUltimoDiaMes = orcamentoDespesaService.ultimoDiaMes(dataAux);

			if (dataUltimoDiaMes.after(dataFinal)) {
				dataUltimoDiaMes.setTime(dataFinal.getTime());
			}

			float totalDiasMes = orcamentoDespesaService.diferenciaDiasTB(
					dataAux.getTime(), dataUltimoDiaMes.getTime());

			dataMesPersist.setTime(dataAux.getTime());

			// data a ser persistida (getTime)
			dataMesPersist = orcamentoDespesaService
					.primeiroDiaMes(dataMesPersist);

			// percentual a ser persistido
			percentualPersist = new BigDecimal(
					(totalDiasMes / totalDiasPeriodoTB) * 100);

			orcDespesaMes.setOrcDespesa(orcDesp);
			orcDespesaMes.setDataMes(dataMesPersist.getTime());
			orcDespesaMes.setPercentualMes(percentualPersist);

			this.createOrcDespesaMes(orcDespesaMes);

			dataUltimoDiaMes.add(Calendar.DAY_OF_MONTH, 1);
			dataAux = dataUltimoDiaMes;
		}
	}

}
