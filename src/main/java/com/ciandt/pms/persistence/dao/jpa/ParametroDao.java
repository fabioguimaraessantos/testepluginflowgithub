package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.persistence.dao.IParametroDao;

/**
 * 
 * A classe ApropriacaoPlantaoDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade {@link Parametro}.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 03/02/2014
 */
@Repository
public class ParametroDao extends
		AbstractDao<Long, Parametro> implements IParametroDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public ParametroDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, Parametro.class);
	}

	/**
	 * Busca todas as entidades.
	 * 
	 * @return retorna uma lista de Area.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Parametro> findAll() {
		return getJpaTemplate().findByNamedQuery(Parametro.FIND_ALL);
	}

	/**
	 * Busca {@link Parametro} pelo atributo {@code nomeParametro}.
	 *
	 * @param nomeParametro
	 * @return {@link Parametro}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Parametro findByNomeParametro(final String nomeParametro) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nomeParametro", nomeParametro);

		List<Parametro> parametros = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						Parametro.FIND_BY_NOME_PARAMETRO, params);

		if (parametros.isEmpty()) {
			return null;
		}

		return parametros.get(0);
	}
}