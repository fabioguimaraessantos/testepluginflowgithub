package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.OrcDespesaMes;
import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * 
 * @author peter
 * 
 */
@Service
public interface IOrcDespesaMesService {

	/**
	 * Cria um OrcDespesaMes.
	 * 
	 * @param entity
	 */
	void createOrcDespesaMes(OrcDespesaMes entity);

	/**
	 * Busca os orcDespesaMes para saber o total já emitido de TravelBudget por
	 * clob e meses.
	 * 
	 * @return
	 */
	List<OrcDespesaMes> findOrcDespesaMesByClobAndPeriod(ContratoPratica clob,
			Date dataInicial, Date dataFinal);

	/**
	 * Cria os OrcDespesaMes a partir de um OrcamentoDespesa, quebrando o mesmo
	 * em meses.
	 * 
	 * @param orcDesp
	 */
	void createOrcDespesaMesByOrcDespesa(OrcamentoDespesa orcDesp);

}
