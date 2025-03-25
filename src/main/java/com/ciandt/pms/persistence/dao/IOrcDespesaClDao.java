package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * 
 * A classe IOrcDespesaClDao proporciona as funcionalidades de interface para
 * OrcDespesaClDao.
 * 
 * @since 10/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public interface IOrcDespesaClDao extends IAbstractDao<Long, OrcDespesaCl> {

	/**
	 * Busca todas as entidades do banco.
	 * 
	 * @return listResult
	 */
	List<OrcDespesaCl> findAll();

	/**
	 * Busca por cliente.
	 * 
	 * @param cliente
	 *            cliente
	 * @return listResult
	 */
	List<OrcDespesaCl> findByCliente(final Cliente cliente);

	/**
	 * Busca por cliente.
	 * 
	 * @param cliente
	 *            cliente.
	 * @return lista.
	 */
	List<OrcDespesaCl> findByClientAndActive(final Cliente cliente);

	/**
	 * Busca por Cliente e nome.
	 * 
	 * @param cliente
	 *            cliente
	 * @param name
	 *            nome
	 * @return listResult
	 */
	List<OrcDespesaCl> findByClientAndName(final Cliente cliente,
			final String name);

	/**
	 * Busca um {@link OrcDespesaCl} por nome.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	OrcDespesaCl findByNameAndActive(final String name);

	/**
	 * Busca um {@link OrcDespesaCl} por OrcamentoDespesa.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	OrcDespesaCl findByOrcamentoDespesa(final OrcamentoDespesa orcamentoDespesa);

	/**
	 * Busca um OrcDespCl por nome, cliente e ativos para validacao de nome na
	 * criacao do mesmo.
	 * 
	 * @param {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	OrcDespesaCl findByNameAndClientAndActive(final OrcDespesaCl orcDespCl);
	
	/**
	 * Busca apenas os TB vigentes.
	 * 
	 * @return
	 */
	List<OrcDespesaCl> findOnlyValidByClient(final Cliente cliente);
}