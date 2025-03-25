package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.TipoServicoDealFiscal;

/**
 * 
 * @author peter
 * 
 */
public interface ITipoServicoDealFiscalDao extends
		IAbstractDao<Long, TipoServicoDealFiscal> {

	/**
	 * Busca por dealfiscal.
	 * 
	 * @param dealFiscal
	 * @return
	 */
	List<TipoServicoDealFiscal> findByDealFiscal(final DealFiscal dealFiscal);

}
