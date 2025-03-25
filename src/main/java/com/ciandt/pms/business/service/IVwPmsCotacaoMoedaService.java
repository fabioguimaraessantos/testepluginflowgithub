package com.ciandt.pms.business.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.VwPmsCotacaoMoeda;

@Service
public interface IVwPmsCotacaoMoedaService {

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
