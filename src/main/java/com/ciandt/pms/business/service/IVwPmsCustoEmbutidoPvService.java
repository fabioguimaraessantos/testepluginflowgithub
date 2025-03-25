package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwPmsCustoEmbutidoPv;

/**
 * 
 * @author peter
 * 
 */
@Service
public interface IVwPmsCustoEmbutidoPvService {

	/**
	 * Busca por clob, moeda, dataMes.
	 * 
	 * @param clob
	 * @param moeda
	 * @param dataMes
	 * @return
	 */
	List<VwPmsCustoEmbutidoPv> findByClobAndMes(ContratoPratica clob,
			Date dataMes);

}
