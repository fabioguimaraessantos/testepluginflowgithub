package com.ciandt.pms.persistence.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaMoeda;

/**
 * 
 * A classe IAjusteReceitaDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade AjusteReceita.
 * 
 * @since 14/07/2011
 * @author cmantovani
 * 
 */
public interface IAjusteReceitaDao extends IAbstractDao<Long, AjusteReceita> {

	/**
	 * Busca todos os AjusteReceita.
	 * 
	 * @return lista de AjusteReceita com todos os AjusteReceita
	 */
	List<AjusteReceita> findAll();

	/**
	 * Busca os AjusteReceita pelo filtro.
	 * 
	 * @param filter
	 *            - entidade contendo o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByFilter(final AjusteReceita filter);

	/**
	 * Busca os AjusteReceita pela data de ajuste.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByDateAjuste(final Date date);

	/**
	 * Busca os AjusteReceita pela data de ajuste e ContratoPratica.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByDateAjusteAndContratoPratica(final Date date,
			final ContratoPratica contratoPratica);

	/**
	 * Busca os AjusteReceita pela data de receita.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByDateReceita(final Date date);

	/**
	 * Busca os AjusteReceita pela data de receita e ContratoPratica.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByDateReceitaAndContratoPratica(final Date date,
			final ContratoPratica contratoPratica);

	/**
	 * Busca os AjusteReceita pela data de receita e data de ajuste.
	 * 
	 * @param dateReceita
	 *            - data da receita para o filtro
	 * @param dateAjuste
	 *            - data de ajuste para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByDateReceitaAndDateAjuste(final Date dateReceita,
			final Date dateAjuste);

	/**
	 * Busca os AjusteReceita pela data de receita, data de ajuste e
	 * ContratoPratica.
	 * 
	 * @param dateReceita
	 *            - data da receita para o filtro
	 * @param dateAjuste
	 *            - data de ajuste para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByDateReceitaAndDateAjusteAndContratoPratica(
			final Date dateReceita, final Date dateAjuste,
			final ContratoPratica contratoPratica);

	/**
	 * Busca os AjusteReceita entre duas datas.
	 * 
	 * @param dataInicio
	 *            - data de inicio para o filtro
	 * @param dataFim
	 *            - data de fim para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByRangeDate(final Date dataInicio,
			final Date dataFim);

	/**
	 * Busca os AjusteReceita de um dealFiscal.
	 * 
	 * @param receitaDealFiscal
	 *            recebe um receitaDEalFiscal
	 * 
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findByReceitaDealFiscal(
			final ReceitaDealFiscal receitaDealFiscal);

	/**
	 * Busca AjusteReceita por Receita.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult
	 */
	List<AjusteReceita> findByReceita(final Receita receita);

	/**
	 * Busca AjusteReceita por ReceitaMoeda.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult
	 */
	List<AjusteReceita> findByReceitaMoeda(final ReceitaMoeda receitaMoeda);

	/**
	 * Busca AjusteReceita pelo ajusteReceitaPai
	 * 
	 * @param ajusteReceitaPai
	 * @return
	 */
	AjusteReceita findByAjusteReceitaPai(final AjusteReceita ajusteReceitaPai);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.persistence.dao.IAjusteReceitaDao#
	 * getTotalPublishedByDealFiscalAndDate(com.ciandt.pms.model.DealFiscal,
	 * java.util.Date)
	 */
	BigDecimal getTotalPublishedByDealFiscalAndDate(
			final DealFiscal dealFiscal, final Date dataFim);
}
