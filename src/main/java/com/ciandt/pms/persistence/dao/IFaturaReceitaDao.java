package com.ciandt.pms.persistence.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.ReceitaDealFiscal;


/**
 * 
 * A classe IFaturaReceitaDao proporciona a interface de acesso a 
 * camada de persistencia referente a entidade FaturaReceita.
 *
 * @since 19/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IFaturaReceitaDao extends IAbstractDao<Long, FaturaReceita> {

    /**
     * Pega o total da receita associada com a fatura,
     * referente a ReceitaDealFiscal pasado por parametro.
     * 
     * @param rdf - ReceitaDealFiscal em questão
     * 
     * @return retorna um double com o valor total associado da fatura
     */
    BigDecimal getTotalFaturaByReceitaDeal(final ReceitaDealFiscal rdf);
    
    /**
     * Pega o total da receita associada com a fatura,
     * referente a ReceitaDealFiscal pasado por parametro.
     * 
     * @param rdf - ReceitaDealFiscal em questão
     * 
     * @return retorna um double com o valor total associado da fatura
     */
    List<FaturaReceita> findByReceitaDealFiscal(final ReceitaDealFiscal rdf);
    
    /**
     * Pega o total associado pela fatura.
     * 
     * @param fatura - Fatura em questão
     * 
     * @return retorna um double com o valor total associado da fatura
     */
    BigDecimal getTotalByFatura(final Fatura fatura);
    
    /**
     * Pega o saldo restande de uma fatura.
     * 
     * @param fatura - Fatura em questão
     * 
     * @return retorna um double com o valor do saldo.
     */
    BigDecimal getSaldoByFatura(final Fatura fatura);
    
	/**
	 * Consulta totas as faturas até a dataFim e Deal Fiscal com status Submitted e
	 * Revenue Source = Services.
	 * 
	 * @param dealFiscal
	 *            Objeto {@link DealFiscal}
	 * @param dataFim
	 *            Data fim.
	 * @return
	 */
	BigDecimal getTotalFaturaByDealFiscal(final DealFiscal dealFiscal,
			final Date dataFim);
}
