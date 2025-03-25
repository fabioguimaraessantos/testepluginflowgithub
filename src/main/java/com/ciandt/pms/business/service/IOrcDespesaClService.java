package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * 
 * A classe IOrcDespesaClService proporciona as funcionalidades de iterface para
 * OrcDespesaClService.
 * 
 * @since 10/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public interface IOrcDespesaClService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createOrcDespesaCl(final OrcDespesaCl entity)
			throws IntegrityConstraintException;

	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 */
	@Transactional
	void updateOrcDespesaCl(final OrcDespesaCl entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser deletada.
	 */
	@Transactional
	void removeOrcDespesaCl(final OrcDespesaCl entity);

	/**
	 * Busca todas os registros de OrcDespesaCl.
	 * 
	 * @return listResult.
	 */
	List<OrcDespesaCl> findAll();

	/**
	 * Busca por cliente.
	 * 
	 * @param cliente
	 *            cliente
	 * @return listResult.
	 */
	List<OrcDespesaCl> findOrcDespesaClByCliente(final Cliente cliente);

	/**
	 * Busca entidade por id.
	 * 
	 * @param id
	 *            id.
	 * @return entidade.
	 */
	OrcDespesaCl findOrcDespesaClById(final Long id);

	/**
	 * Busca por cliente.
	 * 
	 * @param cliente
	 *            cliente.
	 * @return lista.
	 */
	List<OrcDespesaCl> findOrcDespesaClByClientAndActive(final Cliente cliente);

	/**
	 * Busca por cliente e name.
	 * 
	 * @param cliente
	 *            cliente.
	 * @param name
	 *            name.
	 * @return lista.
	 */
	List<OrcDespesaCl> findOrcDespesaClByClientAndName(final Cliente cliente,
			final String name);

	/**
	 * Busca um {@link OrcDespesaCl} por nome.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	OrcDespesaCl findOrcDespesaClByNameAndActive(final String name);

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

	/**
	 * Método que verifica se o Travel Budget pode ser criado com o valor
	 * informado.
	 * 
	 * @param orcDespesa
	 *            Objeto do tipo {@link OrcamentoDespesa}
	 * @param grantedCLobList
	 *            Lista com os ids de c-Lobs selecionados na tela
	 * 
	 * @return boolean <li>true - o valor do travel budget está dentro do limite
	 *         permitido</li> <li>false - o valor do travel budget está fora do
	 *         limite permitido</li>
	 */
	boolean canCreateTravelBudget(OrcamentoDespesa orcDespesa,
			String[] grantedCLobList);

	/**
	 * Método que verifica se o Travel Budget pode ser criado com o valor
	 * informado.
	 *
	 * @param orcDespesa
	 *            Objeto do tipo {@link OrcamentoDespesa}
	 * @param grantedCLobList
	 *            Lista com os ids de c-Lobs selecionados na tela
	 *
	 * @return boolean <li>true - o valor do travel budget está dentro do limite
	 *         permitido</li> <li>false - o valor do travel budget está fora do
	 *         limite permitido</li>
	 */
	boolean hasBudgetToCreateTravelBudget(final OrcamentoDespesa orcDespesa, final Long[] grantedCLobList);

	/**
	 * Verifica se um login é VP ou nao.
	 * 
	 * @param login
	 * @return
	 */
	Boolean isVp(String login);

	void clone(OrcamentoDespesa orcamentoDespesa, OrcDespesaCl entity, Long[] clobsCodeToClone, Boolean copyFollow, Boolean copyWhiteList, Boolean copyWhiteListDelegation)  throws IntegrityConstraintException;
}