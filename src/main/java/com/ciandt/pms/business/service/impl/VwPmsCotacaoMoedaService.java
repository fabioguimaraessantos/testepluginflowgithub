package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwPmsCotacaoMoedaService;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.VwPmsCotacaoMoeda;
import com.ciandt.pms.persistence.dao.IVwPmsCotacaoMoedaDao;

@Service
public class VwPmsCotacaoMoedaService implements IVwPmsCotacaoMoedaService {

	/**
	 * Dao da entidade.
	 */
	@Autowired
	private IVwPmsCotacaoMoedaDao vwPmsCotacaoMoedaDao;

	/**
	 * Busca a ultima taxa de conversão de uma moeda.
	 * 
	 * @param moeda
	 * @return
	 */
	public VwPmsCotacaoMoeda findLastRateByCurrency(Moeda moeda) {
		return vwPmsCotacaoMoedaDao.findLastRateByCurrency(moeda);
	}
	
	
	/**
	 * Chama function que converte o valor de uma moeda para outra moeda.
	 * 
	 * @param valorAConverter
	 *            Valor a ser convertido
	 * @param moedaDe
	 *            Moeda do valor a ser convertido
	 * @param moedaPara
	 *            Moeda na qual se quer converter o valor
	 * @return BigDecimal Valor convertido
	 */
	public BigDecimal getValorConvertidoMoedaDePara(
			final BigDecimal valorAConverter, final Long moedaDe,
			final Long moedaPara) {
		return vwPmsCotacaoMoedaDao.getValorConvertidoMoedaDePara(
				valorAConverter, moedaDe, moedaPara);
	}

}
