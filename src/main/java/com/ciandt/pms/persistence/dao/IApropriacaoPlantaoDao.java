package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ApropriacaoPlantao;
import com.ciandt.pms.model.Pessoa;

/**
 * 
 * A interface IApropriacaoPlantaoDao proporciona a interface de acesso a camada
 * de persistencia referente a entidade {@link ApropriacaoPlantao}.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 22/10/2013
 */
public interface IApropriacaoPlantaoDao extends
		IAbstractDao<Long, ApropriacaoPlantao> {

	/**
	 * Busca todas as {@link ApropriacaoPlantao}.
	 * 
	 * @return lista de {@link ApropriacaoPlantao}
	 */
	List<ApropriacaoPlantao> findAll();

	/**
	 * Checa se existe a entidade existe na base de dados.
	 * 
	 * @param apropriacaoPlantao
	 * @return true caso a entidade já exista na base de dados
	 */
	boolean exists(final ApropriacaoPlantao apropriacaoPlantao);
	
	/**
	 * Busca uma lista de entidades pela dataFechamento.
	 * 
	 * @param dataFechamento
	 *            01/mm/yyyy do fechamento.
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro.
	 */
	List<ApropriacaoPlantao> findApropriacaoPlantaoByData(
			final Date dataFechamento);

	/**
	 * Busca uma lista de entidades pela dataFechamento e codigo da pessoa.
	 * 
	 * @param dataFechamento
	 *            01/mm/yyyy do fechamento.
	 * @param pessoa
	 *            {@link Pessoa}
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro.
	 */
	List<ApropriacaoPlantao> findApropriacaoPlantaoByDataAndCdPessoa(
			final Date dataFechamento, final Pessoa pessoa);
}