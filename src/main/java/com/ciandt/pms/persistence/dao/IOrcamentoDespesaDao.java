package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * 
 * A classe IOrcamentoDespesaDao proporciona as funcionalidades de interface
 * para OrcamentoDespesaDao.
 * 
 * @since 24/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public interface IOrcamentoDespesaDao extends
		IAbstractDao<Long, OrcamentoDespesa> {

	/**
	 * Busca todas as entidades do banco.
	 * 
	 * @return listResult
	 */
	List<OrcamentoDespesa> findAll();

	/**
	 * Obtem um {@link OrcamentoDespesa} de acordo com o nome e tipo de
	 * orcamento informado.
	 * 
	 * @param nome
	 *            nome do {@link OrcamentoDespesa}
	 * 
	 * @param tipoOrcDesp
	 *            a sigla referente ao tipo de {@link OrcamentoDespesa}
	 * 
	 * @return {@link OrcamentoDespesa}
	 * 
	 */
	OrcamentoDespesa findByNameAndTipoOrcDespesa(final String nome,
			final String tipoOrcDesp);

}