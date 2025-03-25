package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ReceitaResultado;

/**
 * 
 * A classe IReceitaResultadoDao proporciona as funcionalidades de acesso ao
 * banco para a entidade {@link ReceitaResultado}.
 * 
 * @since 12/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Repository
public interface IReceitaResultadoDao extends
		IAbstractDao<Long, ReceitaResultado> {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ReceitaResultado> findAll();

	/**
	 * Retorna uma entidade.
	 * 
	 * @param entity
	 *            filtro {@link ReceitaResultado}
	 * 
	 * @return retorna uma entidade referente ao filtro passado.
	 */
	ReceitaResultado findByContratoAndMoedaAndDate(final ReceitaResultado entity);

}