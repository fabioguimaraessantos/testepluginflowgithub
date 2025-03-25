package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ApropriacaoHoraExtra;
import com.ciandt.pms.model.Pessoa;

/**
 * 
 * A interface IApropriacaoHoraExtraDao proporciona a interface de acesso a
 * camada de persistencia referente a entidade {@link ApropriacaoHoraExtra}.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 22/10/2013
 */
public interface IApropriacaoHoraExtraDao extends
		IAbstractDao<Long, ApropriacaoHoraExtra> {

	/**
	 * Busca todas as {@link ApropriacaoHoraExtra}.
	 * 
	 * @return lista de {@link ApropriacaoHoraExtra}
	 */
	List<ApropriacaoHoraExtra> findAll();

	/**
	 * Checa se existe a entidade existe na base de dados.
	 * 
	 * @param apropriacaoPlantao
	 * @return true caso a entidade já exista na base de dados
	 */
	boolean exists(final ApropriacaoHoraExtra apropriacaoHoraExtra);
	
	/**
	 * Busca uma lista de entidades pela dataFechamento.
	 * 
	 * @param dataFechamento
	 *            01/mes/yyyy do fechamento.
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro.
	 */
	List<ApropriacaoHoraExtra> findApropriacaoHoraExtraByData(
			final Date dataFechamento);

	/**
	 * Busca uma lista de entidades pela dataFechamento e codigo da pessoa.
	 * 
	 * @param dataFechamento
	 *            01/mes/yyyy do fechamento.
	 * @param pessoa
	 *            {@link Pessoa}
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro.
	 */
	List<ApropriacaoHoraExtra> findApropriacaoHoraExtraByDataAndCdPessoa(
			final Date dataFechamento, final Pessoa pessoa);
}
