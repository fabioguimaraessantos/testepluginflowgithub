package com.ciandt.pms.persistence.dao;

import java.math.BigDecimal;

import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.VwPmsCotacaoMoeda;

public interface IVwPmsCotacaoMoedaDao extends
		IAbstractDao<Long, VwPmsCotacaoMoeda> {

	/**
	 * Busca a ultima taxa de conversão de uma moeda.
	 * 
	 * @param moeda
	 * @return
	 */
	VwPmsCotacaoMoeda findLastRateByCurrency(Moeda moeda);
	
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
	BigDecimal getValorConvertidoMoedaDePara(final BigDecimal valorAConverter,
			final Long moedaDe, final Long moedaPara);

}
