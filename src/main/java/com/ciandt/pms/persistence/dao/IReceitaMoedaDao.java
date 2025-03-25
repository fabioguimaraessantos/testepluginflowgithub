package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaMoeda;

/**
 * 
 * A classe IReceitaMoedaDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade ReceitaMoeda.
 * 
 * @since 22/10/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
public interface IReceitaMoedaDao extends IAbstractDao<Long, ReceitaMoeda> {

	/**
	 * Busca todas as entidades.
	 * 
	 * @return retorna uma lista de ReceitaMeoda.
	 */
	List<ReceitaMoeda> findAll();

	/**
	 * Busca ReceitaMoeda por um período e contrato prática.
	 * 
	 * @param dataInicio
	 *            dataInicio
	 * @param dataFim
	 *            dataFim
	 * @param codigoContratoPratica
	 *            codigoContratoPratica
	 * @return retorna uma lista de ReceitaMoeda.
	 */
	List<ReceitaMoeda> findByPeriodoAndContratoPratica(final Date dataInicio,
			final Date dataFim, final Long codigoContratoPratica);

	/**
	 * Busca ReceitaMoedas por receita.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult.
	 */
	List<ReceitaMoeda> findByReceita(final Receita receita);

	/**
	 * Obtem uma {@link ReceitaMoeda} a partir de uma {@link ContratoPratica},
	 * {@link Moeda} e Mes.
	 * 
	 * @param contratoPratica
	 *            - o {@link ContratoPratica} em questao
	 * 
	 * @param moeda
	 *            - a {@link Moeda} em questao
	 * 
	 * @param dataMes
	 *            - mes de referencia
	 * 
	 * @return retorna uma lista de ReceitaMoeda.
	 */
	ReceitaMoeda findByClobAndMoedaAndDataMes(
			final ContratoPratica contratoPratica, final Moeda moeda,
			final Date dataMes);
}