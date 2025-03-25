package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.OrcDespDelegacao;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcDespDelegacaoDao;

/**
 * A classe OrcDespDelegacaoDao proporciona as funcionalidades de DAO para a
 * enitdade OrcDespDelegacao.
 * 
 * @author peter
 * 
 */
@Repository
public class OrcDespDelegacaoDao extends AbstractDao<Long, OrcDespDelegacao>
		implements IOrcDespDelegacaoDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public OrcDespDelegacaoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, OrcDespDelegacao.class);
	}

	/**
	 * Busca delegados por travelBudget.
	 * 
	 * @param orcDesp
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<OrcDespDelegacao> findByOrcDespesa(
			final OrcamentoDespesa orcDesp) {
		List<OrcDespDelegacao> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespDelegacao.FIND_BY_ORC_DEPSESA,
				new Object[] { orcDesp.getCodigoOrcaDespesa() });

		return listResult;
	}

	/**
	 * Busca delegados por travelBudget e login
	 * 
	 * @param orcDesp
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public OrcDespDelegacao findByOrcDespesaAndLogin(
			final OrcamentoDespesa orcDesp, final String login) {
		List<OrcDespDelegacao> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespDelegacao.FIND_BY_ORC_DEPSESA_AND_LOGIN,
				new Object[] { orcDesp.getCodigoOrcaDespesa(), login });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		}

		return null;
	}

	/**
	 * Busca por login.
	 * @param login
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<OrcDespDelegacao> findByLogin(final String login) {
		List<OrcDespDelegacao> listResult = getJpaTemplate().findByNamedQuery(
				OrcDespDelegacao.FIND_BY_LOGIN, new Object[] { login });
		
		return listResult;
	}

}
