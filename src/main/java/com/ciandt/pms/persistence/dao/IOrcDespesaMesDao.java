package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.OrcDespesaMes;

/**
 * 
 * @author peter
 * 
 */
public interface IOrcDespesaMesDao extends IAbstractDao<Long, OrcDespesaMes> {

	/**
	 * Busca os orcDespesaMes para saber o total já emitido de TravelBudget por
	 * clob e meses.
	 * 
	 * @return
	 */
	List<OrcDespesaMes> findOrcDespesaMesByClobAndPeriod(ContratoPratica clob,
			Date dataInicial, Date dataFinal);

}
