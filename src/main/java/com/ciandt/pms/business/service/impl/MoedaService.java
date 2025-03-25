package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.persistence.dao.IMoedaDao;

/**
 * 
 * A classe MoedaService proporciona as funcionalidades da camada de serviço
 * referente a entidade Moeda.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class MoedaService implements IMoedaService {

	/** Instancia DAO da entidade moeda. */
	@Autowired
	private IMoedaDao moedaDao;

	/**
	 * Cria uma nova entidade.
	 * 
	 * @param entity
	 *            - entidade a ser criada.
	 */
	public void createMoeda(final Moeda entity) {
		moedaDao.create(entity);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<Moeda> findMoedaAll() {
		return moedaDao.findAll();
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public Moeda findMoedaById(final Long id) {
		return moedaDao.findById(id);
	}

	/**
	 * Busca uma Moeda com a sigla passada por parametro.
	 * 
	 * @param siglaMoeda
	 *            - sigla da Moeda
	 * 
	 * @return retorna uma Moeda com a sigla passada por parametro.
	 */
	public Moeda findMoedaByAcronym(final String siglaMoeda) {
		return moedaDao.findByAcronym(siglaMoeda);
	}

	public Moeda findMoedaByNameMsa(final String nomeMoeda, final String msa) {
		return moedaDao.findByNameAndMsa(nomeMoeda, msa);
	}


	/**
	 * Busca uma Moeda pelo contratoPratica.
	 * 
	 * @param contratoPratica
	 *            - contratoPratica
	 * 
	 * @return retorna uma Moeda.
	 */
	public Moeda findMoedaByContratoPratica(
			final ContratoPratica contratoPratica) {
		return moedaDao.findByContratoPratica(contratoPratica);
	}

	/**
	 * Busca uma Moeda pelo tiRecurso.
	 * 
	 * @param tiRecurso
	 *            - tiRecurso
	 * 
	 * @return retorna uma Moeda.
	 */
	public Moeda findMoedaByTiRecurso(final TiRecurso tiRecurso) {
		return moedaDao.findByTiRecurso(tiRecurso);
	}

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
	public List<Moeda> findMoedaByAlocationsWithCLob(final ContratoPratica cp,
			final Date periodoAlocacao) {
		return moedaDao.findByAlocationsAndCLob(cp, periodoAlocacao);
	}

}