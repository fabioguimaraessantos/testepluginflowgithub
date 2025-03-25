package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TiRecurso;

/**
 * 
 * A classe IMoedaDao proporciona a interface de acesso para a camada DAO da
 * entidade Moeda.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IMoedaDao extends IAbstractDao<Long, Moeda> {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Moeda> findAll();

	/**
	 * Busca uma Moeda com a sigla passada por parametro.
	 * 
	 * @param siglaMoeda
	 *            - sigla da Moeda
	 * 
	 * @return retorna uma Moeda com a sigla passada por parametro.
	 */
	Moeda findByAcronym(final String siglaMoeda);

	Moeda findByNameAndMsa(final String siglaMoeda, final String msa);


	/**
	 * Busca uma Moeda pelo contratoPratica.
	 * 
	 * @param contratoPratica
	 *            - contratoPratica
	 * 
	 * @return retorna uma Moeda.
	 */
	Moeda findByContratoPratica(final ContratoPratica contratoPratica);

	/**
	 * Busca uma Moeda pelo tiRecurso.
	 * 
	 * @param tiRecurso
	 *            - tiRecurso
	 * 
	 * @return retorna uma Moeda.
	 */
	Moeda findByTiRecurso(final TiRecurso tiRecurso);

	/**
	 * Busca moeda de um mapa alocacao de um contrato pratica no periodo
	 * informado.
	 * 
	 * @param cp
	 *            contrato pratica
	 * 
	 * @param periodoAlocacao
	 *            - data mes referente ao periodo das alocacoes
	 * 
	 * @return listresult.
	 */
	List<Moeda> findByAlocationsAndCLob(final ContratoPratica cp,
			final Date periodoAlocacao);

}