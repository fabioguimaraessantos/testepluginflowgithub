package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.OrcDespDelegacao;
import com.ciandt.pms.model.OrcamentoDespesa;

public interface IOrcDespDelegacaoDao extends
		IAbstractDao<Long, OrcDespDelegacao> {

	/**
	 * Busca delegados por travelBudget.
	 * 
	 * @param orcDesp
	 * @return
	 */
	List<OrcDespDelegacao> findByOrcDespesa(final OrcamentoDespesa orcDesp);

	/**
	 * Busca delegados por travelBudget e login
	 * 
	 * @param orcDesp
	 * @return
	 */
	OrcDespDelegacao findByOrcDespesaAndLogin(final OrcamentoDespesa orcDesp,
			final String login);

	/**
	 * Busca por login.
	 * 
	 * @param login
	 * @return
	 */
	List<OrcDespDelegacao> findByLogin(final String login);

}
