package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IOrcDespDelegacaoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.OrcDespDelegacao;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcDespDelegacaoDao;

/**
 * A classe OrcDespDelegacaoService proporciona as funcionalidades de servico
 * para a enitdade OrcDespDelegacao.
 * 
 * @author peter
 * 
 */
@Service
public class OrcDespDelegacaoService implements IOrcDespDelegacaoService {

	/** Instancia do dao da enitdade. */
	@Autowired
	private IOrcDespDelegacaoDao orcDespDelegacaoDao;

	/**
	 * Cria um OrcDespDelegacao.
	 * 
	 * @param entity
	 */
	@Transactional
	public boolean createOrcDespDelegacao(final OrcDespDelegacao entity) {

		if (isNameExists(entity)) {
			Messages.showWarning("create", Constants.ALLOW_LIST_ALREADY_EXISTS);
			return Boolean.FALSE;
		}
		orcDespDelegacaoDao.create(entity);
		return Boolean.TRUE;
	}

	/**
	 * Remove um OrcDespDelegacao.
	 * 
	 * @param entity
	 */
	@Transactional
	public boolean removeOrcDespDelegacao(final OrcDespDelegacao entity) {
		orcDespDelegacaoDao.remove(entity);
		return Boolean.TRUE;
	}

	/**
	 * Busca delegados por travelBudget.
	 * 
	 * @param orcDesp
	 * @return
	 */
	public List<OrcDespDelegacao> findByOrcDespesa(
			final OrcamentoDespesa orcDesp) {
		return orcDespDelegacaoDao.findByOrcDespesa(orcDesp);
	}

	/**
	 * Busca delegados por travelBudget e login
	 * 
	 * @param orcDesp
	 * @return
	 */
	public OrcDespDelegacao findByOrcDespesaAndLogin(
			final OrcamentoDespesa orcDesp, final String login) {
		return orcDespDelegacaoDao.findByOrcDespesaAndLogin(orcDesp, login);
	}

	/**
	 * Busca por login.
	 * 
	 * @param login
	 * @return
	 */
	public List<OrcDespDelegacao> findByLogin(final String login) {
		return orcDespDelegacaoDao.findByLogin(login);
	}

	/**
	 * Verifica se o login ja existe no travel budget.
	 * 
	 * @param entity
	 * @return
	 */
	private boolean isNameExists(final OrcDespDelegacao entity) {
		OrcDespDelegacao orcDespDelegacao = this.findByOrcDespesaAndLogin(
				entity.getOrcDespesa(), entity.getCodigoLogin());

		if (orcDespDelegacao == null) {
			return false;
		}

		return true;
	}

}
