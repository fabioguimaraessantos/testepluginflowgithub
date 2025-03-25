package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwPmsCustoEmbutidoPvService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwPmsCustoEmbutidoPv;
import com.ciandt.pms.persistence.dao.IVwPmsCustoEmbutidoPvDao;

/**
 * 
 * @author peter
 * 
 */
@Service
public class VwPmsCustoEmbutidoPvService implements
		IVwPmsCustoEmbutidoPvService {

	/**
	 * Dao da entidade.
	 */
	@Autowired
	private IVwPmsCustoEmbutidoPvDao dao;

	/**
	 * Busca por clob, moeda, dataMes.
	 * 
	 * @param clob
	 * @param moeda
	 * @param dataMes
	 * @return
	 */
	public List<VwPmsCustoEmbutidoPv> findByClobAndMes(ContratoPratica clob,
			Date dataMes) {
		return dao.findByClobAndMes(clob, dataMes);
	}

}
