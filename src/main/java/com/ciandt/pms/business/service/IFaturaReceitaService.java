package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.ReceitaDealFiscal;

/**
 * 
 * A classe IFaturaReceitaService proporciona a interface de acesso a camada de
 * serviço, referente a entidade FaturaReceitaService.
 * 
 * @since 19/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IFaturaReceitaService {

	/**
	 * Pega o total da receita associada com a fatura, referente a
	 * ReceitaDealFiscal pasado por parametro.
	 * 
	 * @param rdf
	 *            - ReceitaDealFiscal en questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	BigDecimal getTotalFaturaByReceitaDeal(final ReceitaDealFiscal rdf);

	/**
	 * Pega o total da receita associada com a fatura, referente a
	 * ReceitaDealFiscal pasado por parametro.
	 * 
	 * @param rdf
	 *            - ReceitaDealFiscal en questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	List<FaturaReceita> findFaturaReceitaByReceitaDealFiscal(
			final ReceitaDealFiscal rdf);

	/**
	 * Pega o total associado pela fatura.
	 * 
	 * @param fatura
	 *            - Fatura em questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	BigDecimal getFaturaReceitaTotalByFatura(final Fatura fatura);

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	void createFaturaReceita(final FaturaReceita entity);

	/**
	 * realiza o update de uma entidade.
	 * 
	 * @param entity
	 *            entidade a realizar o update.
	 */
	void updateFaturaReceita(final FaturaReceita entity);

	/**
	 * remove uma entidade.
	 * 
	 * @param entity
	 *            entidade a realizar a remoção.
	 */
	void removeFaturaReceita(final FaturaReceita entity);

	/**
	 * Pega o saldo restande de uma fatura.
	 * 
	 * @param fatura
	 *            - Fatura em questão
	 * 
	 * @return retorna um double com o valor do saldo.
	 */
	BigDecimal getFaturaReceitaSaldoByFatura(final Fatura fatura);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	FaturaReceita findFaturaReceitaById(final Long id);

	/**
	 * Calcula o valor da coluna Publish FB.
	 * 
	 * @param receDealFiscal
	 * @return
	 */
	Double calculatePublishFiscalBalance(final ReceitaDealFiscal receDealFiscal);
    
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
