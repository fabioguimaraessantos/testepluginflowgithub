package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.OrcDespDelegacao;
import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * A classe OrcDespDelegacaoService proporciona as funcionalidades de interface
 * para a classe OrcDespDelegacaoService.
 * 
 * @author peter
 * 
 */
@Service
public interface IOrcDespDelegacaoService {

	/**
	 * Cria um OrcDespDelegacao.
	 * 
	 * @param entity
	 */
	boolean createOrcDespDelegacao(final OrcDespDelegacao entity);

	/**
	 * Remove um OrcDespDelegacao.
	 * 
	 * @param entity
	 */
	boolean removeOrcDespDelegacao(final OrcDespDelegacao entity);

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
