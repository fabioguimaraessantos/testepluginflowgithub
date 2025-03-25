package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IFaturaReceitaService;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.persistence.dao.IFaturaReceitaDao;

/**
 * 
 * A classe FaturaReceitaService proporciona as funcionalidades da camada de
 * serviço referente a entidade FaturaReceita.
 * 
 * @since 19/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class FaturaReceitaService implements IFaturaReceitaService {

	/** instancia do serviço FaturaReceita. */
	@Autowired
	private IFaturaReceitaDao faturaReceitaDao;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public FaturaReceita findFaturaReceitaById(final Long id) {
		return faturaReceitaDao.findById(id);
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createFaturaReceita(final FaturaReceita entity) {
		faturaReceitaDao.create(entity);
	}

	/**
	 * realiza o update de uma entidade.
	 * 
	 * @param entity
	 *            entidade a realizar o update.
	 */
	public void updateFaturaReceita(final FaturaReceita entity) {
		faturaReceitaDao.update(entity);
	}

	/**
	 * remove uma entidade.
	 * 
	 * @param entity
	 *            entidade a realizar a remoção.
	 */
	public void removeFaturaReceita(final FaturaReceita entity) {
		faturaReceitaDao.remove(entity);
	}

	/**
	 * Pega o total da receita associada com a fatura, referente a
	 * ReceitaDealFiscal pasado por parametro.
	 * 
	 * @param rdf
	 *            - ReceitaDealFiscal en questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	public BigDecimal getTotalFaturaByReceitaDeal(final ReceitaDealFiscal rdf) {
		return faturaReceitaDao.getTotalFaturaByReceitaDeal(rdf);
	}

	/**
	 * Pega o total da receita associada com a fatura, referente a
	 * ReceitaDealFiscal pasado por parametro.
	 * 
	 * @param rdf
	 *            - ReceitaDealFiscal en questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	public List<FaturaReceita> findFaturaReceitaByReceitaDealFiscal(
			final ReceitaDealFiscal rdf) {
		return faturaReceitaDao.findByReceitaDealFiscal(rdf);
	}

	/**
	 * Pega o total associado pela fatura.
	 * 
	 * @param fatura
	 *            - Fatura em questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	public BigDecimal getFaturaReceitaTotalByFatura(final Fatura fatura) {
		return faturaReceitaDao.getTotalByFatura(fatura);
	}

	/**
	 * Pega o saldo restande de uma fatura.
	 * 
	 * @param fatura
	 *            - Fatura em questão
	 * 
	 * @return retorna um double com o valor do saldo.
	 */
	public BigDecimal getFaturaReceitaSaldoByFatura(final Fatura fatura) {
		return faturaReceitaDao.getSaldoByFatura(fatura);
	}

	/**
	 * Calcula o valor da coluna Publish FB.
	 * 
	 * @param receDealFiscal
	 * @return
	 */
	public Double calculatePublishFiscalBalance(
			final ReceitaDealFiscal receDealFiscal) {
		Double totalResult = new Double(0);

		Double totalReceita = new Double(0);
		Double totalAjuste = new Double(0);
		Double totalFaturado = new Double(0);

//		totalReceita = receDealFiscal.getValorReceita().doubleValue();
//		totalAjuste = receDealFiscal.getTotalAdjustmentValue().doubleValue();

		List<FaturaReceita> faturaReceitaList = this
				.findFaturaReceitaByReceitaDealFiscal(receDealFiscal);
		for (FaturaReceita faturaReceita : faturaReceitaList) {
			totalFaturado += faturaReceita.getValorReceitaAssociada()
					.doubleValue();
		}

		totalResult = totalFaturado - (totalReceita += totalAjuste);

		return totalResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IFaturaReceitaService#
	 * getTotalFaturaByDealFiscal(com.ciandt.pms.model.DealFiscal,
	 * java.util.Date)
	 */
	public BigDecimal getTotalFaturaByDealFiscal(final DealFiscal dealFiscal,
			final Date dataFim) {
		return faturaReceitaDao.getTotalFaturaByDealFiscal(dealFiscal,
				dataFim);
	}
    
}
