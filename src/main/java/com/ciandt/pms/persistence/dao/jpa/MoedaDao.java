package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.persistence.dao.IMoedaDao;

/**
 * 
 * A classe MoedaDao proporciona as funcionalidades de acesso ao banco de dados
 * referentes as entidade Moeda.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class MoedaDao extends AbstractDao<Long, Moeda> implements IMoedaDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public MoedaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, Moeda.class);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<Moeda> findAll() {

		List<Moeda> listResult = getJpaTemplate().findByNamedQuery(
				Moeda.FIND_ALL);

		return listResult;
	}

	/**
	 * Busca uma Moeda com a sigla passada por parametro.
	 * 
	 * @param siglaMoeda
	 *            - sigla da Moeda
	 * 
	 * @return retorna uma Moeda com a sigla passada por parametro.
	 */
	@SuppressWarnings("unchecked")
	public Moeda findByAcronym(final String siglaMoeda) {

		List<Moeda> listResult = getJpaTemplate().findByNamedQuery(
				Moeda.FIND_BY_ACRONYM, new Object[] { siglaMoeda });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}

	public Moeda findByNameAndMsa(final String nomeMoeda, final String msa) {

		List<Moeda> listResult = getJpaTemplate().findByNamedQuery(
				Moeda.FIND_BY_NAME_AND_MSA, new Object[] { msa, nomeMoeda });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}


	/**
	 * Busca uma Moeda pelo contratoPratica.
	 * 
	 * @param contratoPratica
	 *            - contratoPratica
	 * 
	 * @return retorna uma Moeda.
	 */
	@SuppressWarnings("unchecked")
	public Moeda findByContratoPratica(final ContratoPratica contratoPratica) {
		List<Moeda> listResult = getJpaTemplate().findByNamedQuery(
				Moeda.FIND_BY_CONTRATO_PRATICA,
				new Object[] { contratoPratica.getCodigoContratoPratica() });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Busca uma Moeda pelo tiRecurso.
	 * 
	 * @param tiRecurso
	 *            - tiRecurso
	 * 
	 * @return retorna uma Moeda.
	 */
	@SuppressWarnings("unchecked")
	public Moeda findByTiRecurso(final TiRecurso tiRecurso) {
		List<Moeda> listResult = getJpaTemplate().findByNamedQuery(
				Moeda.FIND_BY_TI_RECURSO,
				new Object[] { tiRecurso.getCodigoTiRecurso() });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		} else {
			return null;
		}
	}

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
	@SuppressWarnings("unchecked")
	public List<Moeda> findByAlocationsAndCLob(final ContratoPratica cp,
			final Date periodoAlocacao) {
		List<Moeda> listResult = getJpaTemplate()
				.findByNamedQuery(
						Moeda.FIND_BY_ALOCATIONS_WITH_CLOB_AND_PERIODO,
						new Object[] { cp.getCodigoContratoPratica(),
								periodoAlocacao });
		return listResult;

	}

}