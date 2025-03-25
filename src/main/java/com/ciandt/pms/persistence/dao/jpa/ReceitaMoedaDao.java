package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.persistence.dao.IReceitaMoedaDao;

/**
 * 
 * A classe ReceitaMoedaDao proporciona as funcionalidades de persistencia
 * referente a entidade ReceitaMoeda.
 * 
 * @since 22/10/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
@Repository
public class ReceitaMoedaDao extends AbstractDao<Long, ReceitaMoeda> implements
		IReceitaMoedaDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - fabrica do entity manager
	 */
	@Autowired
	public ReceitaMoedaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ReceitaMoeda.class);
	}

	/**
	 * Busca todas as entidades.
	 * 
	 * @return retorna uma lista de ReceitaMoeda.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaMoeda> findAll() {

		List<ReceitaMoeda> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaMoeda.FIND_ALL);

		return listResult;
	}

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
	@SuppressWarnings("unchecked")
	public List<ReceitaMoeda> findByPeriodoAndContratoPratica(
			final Date dataInicio, final Date dataFim,
			final Long codigoContratoPratica) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataInicio", dataInicio);
		params.put("dataFim", dataFim);
		params.put("codigoContratoPratica", codigoContratoPratica);

		List<ReceitaMoeda> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ReceitaMoeda.FIND_BY_PERIODO_AND_CONTRATO_PRATICA,
						params);

		return listResult;
	}

	/**
	 * Busca ReceitaMoedas por receita.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaMoeda> findByReceita(final Receita receita) {
		List<ReceitaMoeda> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaMoeda.FIND_BY_RECEITA,
				new Object[] { receita.getCodigoReceita() });

		return listResult;
	}

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
	@SuppressWarnings("unchecked")
	public ReceitaMoeda findByClobAndMoedaAndDataMes(
			final ContratoPratica contratoPratica, final Moeda moeda,
			final Date dataMes) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoContratoPratica",
				contratoPratica.getCodigoContratoPratica());
		params.put("codigoMoeda", moeda.getCodigoMoeda());
		params.put("dataMes", dataMes);

		List<ReceitaMoeda> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ReceitaMoeda.FIND_BY_CLOB_AND_MOEDA_AND_DATAMES, params);

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}
}