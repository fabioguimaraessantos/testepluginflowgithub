package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcDespesaClDao;

/**
 * 
 * A classe OrcDespesaClDao proporciona as funcionalidades de DAO para a
 * entidade OrcDespesaCl.
 * 
 * @since 10/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Repository
public class OrcDespesaClDao extends AbstractDao<Long, OrcDespesaCl> implements
		IOrcDespesaClDao {

	/**
	 * Construtor.
	 * 
	 * @param factory
	 *            factory
	 */
	@Autowired
	public OrcDespesaClDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, OrcDespesaCl.class);
	}

	/**
	 * Busca todas as entidades do banco.
	 * 
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaCl> findAll() {
		List<OrcDespesaCl> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaCl.FIND_ALL);
		return listResult;
	}

	/**
	 * Busca por cliente.
	 * 
	 * @param cliente
	 *            cliente
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaCl> findByCliente(final Cliente cliente) {
		List<OrcDespesaCl> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaCl.FIND_BY_CLIENTE,
				new Object[] { cliente.getCodigoCliente() });
		return listResult;
	}

	/**
	 * Busca por cliente e ativo.
	 * 
	 * @param cliente
	 *            cliente.
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaCl> findByClientAndActive(final Cliente cliente) {
		List<OrcDespesaCl> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaCl.FIND_BY_CLIENT_AND_ACTIVE,
				new Object[] { cliente.getCodigoCliente() });
		return listResult;
	}

	/**
	 * Busca por Cliente e nome.
	 * 
	 * @param cliente
	 *            cliente
	 * @param name
	 *            nome
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaCl> findByClientAndName(final Cliente cliente,
			final String name) {
		List<OrcDespesaCl> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaCl.FIND_BY_CLIENT_AND_NAME,
				new Object[] { cliente.getCodigoCliente(), name });
		return listResult;
	}

	/**
	 * Busca um {@link OrcDespesaCl} por nome.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public OrcDespesaCl findByNameAndActive(final String name) {
		List<OrcDespesaCl> result = getJpaTemplate().findByNamedQuery(
				OrcDespesaCl.FIND_BY_NAME_AND_ACTIVE, new Object[] { name });

		if (result == null || result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	/**
	 * Busca um {@link OrcDespesaCl} por OrcamentoDespesa.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public OrcDespesaCl findByOrcamentoDespesa(
			final OrcamentoDespesa orcamentoDespesa) {
		List<OrcDespesaCl> result = getJpaTemplate().findByNamedQuery(
				OrcDespesaCl.FIND_BY_ORCAMENTO_DESPESA,
				new Object[] { orcamentoDespesa.getCodigoOrcaDespesa() });

		if (result == null || result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	/**
	 * Busca um OrcDespCl por nome, cliente e ativos para validacao de nome na
	 * criacao do mesmo.
	 * 
	 * @param {@link OrcDespesaCl}
	 * @return {@link OrcDespesaCl}
	 */
	@SuppressWarnings("unchecked")
	public OrcDespesaCl findByNameAndClientAndActive(
			final OrcDespesaCl orcDespCl) {
		List<OrcDespesaCl> result = getJpaTemplate().findByNamedQuery(
				OrcDespesaCl.FIND_BY_NAME_AND_CLIENT_AND_ACTIVE,
				new Object[] {
						orcDespCl.getOrcamentoDespesa().getNomeOrcDespesa(),
						orcDespCl.getCliente().getCodigoCliente() });

		if (result == null || result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

	/**
	 * Busca apenas os TB vigentes.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaCl> findOnlyValidByClient(final Cliente cliente) {
		List<OrcDespesaCl> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespesaCl.FIND_ONLY_VALID_BY_CLIENT,
				new Object[] { cliente.getCodigoCliente() });
		return listResult;

	}
}