package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwPmsCustoEmbutidoPv;

/**
 * 
 * @author peter
 * 
 */
public interface IVwPmsCustoEmbutidoPvDao extends
		IAbstractDao<Long, VwPmsCustoEmbutidoPv> {

	/**
	 * Busca por clob, moeda e dataMes.
	 * 
	 * @param clob
	 * @param moeda
	 * @param dataMes
	 * @return
	 */
	List<VwPmsCustoEmbutidoPv> findByClobAndMes(ContratoPratica clob,
			Date dataMes);

}
