package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TiRecurso;

/**
 * 
 * A classe IMoedaService proporciona a interface de acesso para a camada de
 * serviço referente a entidade Moeda.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IMoedaService {

	/**
	 * Cria uma nova entidade.
	 * 
	 * @param entity
	 *            - entidade a ser criada.
	 */
	@Transactional
	void createMoeda(final Moeda entity);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Moeda> findMoedaAll();

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	Moeda findMoedaById(final Long id);

	/**
	 * Busca uma Moeda com a sigla passada por parametro.
	 * 
	 * @param siglaMoeda
	 *            - sigla da Moeda
	 * 
	 * @return retorna uma Moeda com a sigla passada por parametro.
	 */
	Moeda findMoedaByAcronym(final String siglaMoeda);

	/**
	 * Search for a Currency with the name passed as a parameter and if in the MSA configuration.
	 *
	 * @param nomeMoeda- Currency name
	 * @param msa - MSA name
	 *
	 * @return a currency.
	 */
	Moeda findMoedaByNameMsa(final String nomeMoeda, final String msa);

	/**
	 * Busca uma Moeda pelo contratoPratica.
	 * 
	 * @param contratoPratica
	 *            - contratoPratica
	 * 
	 * @return retorna uma Moeda.
	 */
	Moeda findMoedaByContratoPratica(final ContratoPratica contratoPratica);

	/**
	 * Busca uma Moeda pelo tiRecurso.
	 * 
	 * @param tiRecurso
	 *            - tiRecurso
	 * 
	 * @return retorna uma Moeda.
	 */
	Moeda findMoedaByTiRecurso(final TiRecurso tiRecurso);

	/**
	 * Busca lista de moeda por alocacaos de um contrato pratica num determinado
	 * periodo.
	 * 
	 * @param cp
	 *            - contrato pratica.
	 * 
	 * @param periodoAlocacao
	 *            - data mes referente ao periodo das alocacoes
	 * 
	 * @return listResult.s
	 */
	List<Moeda> findMoedaByAlocationsWithCLob(final ContratoPratica cp,
			final Date periodoAlocacao);

}