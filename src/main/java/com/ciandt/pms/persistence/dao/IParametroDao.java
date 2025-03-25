package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Parametro;

/**
 * 
 * A interface IParametroDao proporciona a interface de acesso a camada
 * de persistencia referente a entidade {@link Parametro}.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 03/02/2014
 */
public interface IParametroDao extends
		IAbstractDao<Long, Parametro> {

	/**
	 * Busca todas as {@link Parametro}.
	 * 
	 * @return lista de {@link Parametro}
	 */
	List<Parametro> findAll();

	/**
	 * Busca {@link Parametro} pelo atributo {@code nomeParametro}.
	 *
	 * @param nomeParametro
	 * @return {@link Parametro}
	 */
	Parametro findByNomeParametro(String nomeParametro);
}