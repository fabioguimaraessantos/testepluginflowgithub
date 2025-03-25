package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IFollowGrupoCustoService;
import com.ciandt.pms.model.FollowGrupoCusto;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.persistence.dao.IFollowGrupoCustoDao;

/**
 * 
 * A classe FollowGrupoCustoService proporciona as funcionalidades de serviço
 * para FollowGrupoCusto.
 * 
 * @since 30/07/2012
 * @author <a href="mailto:diegos@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class FollowGrupoCustoService implements IFollowGrupoCustoService {

	/** Instancia de dao. */
	@Autowired
	private IFollowGrupoCustoDao dao;

	/**
	 * Cria um seguidor de grupo de custo.
	 * 
	 * @param follow
	 *            seguidor
	 */
	@Transactional
	public void createFollowGrupoCusto(final FollowGrupoCusto follow) {
		dao.create(follow);
	}

	/**
	 * Remove um seguidor de grupo de custo.
	 * 
	 * @param follow
	 *            seguidor
	 */
	@Transactional
	public void removeFollowGrupoCusto(final FollowGrupoCusto follow) {
		dao.remove(follow);
	}

	/**
	 * Busca por GrupoCusto.
	 * 
	 * @param cliente
	 *            GrupoCusto
	 * @return listResult
	 */
	public List<FollowGrupoCusto> findByGrupoCusto(final GrupoCusto grupoCusto) {
		return dao.findByCodigoGrupoCusto(grupoCusto.getCodigoGrupoCusto());
	}

	/**
	 * Busca por login.
	 * 
	 * @param login
	 *            the login
	 * @return listResult
	 */
	public List<FollowGrupoCusto> findbyLogin(final String login) {
		return dao.findbyLogin(login);
	}

	/**
	 * Busca por grupo de custo e login.
	 * 
	 * @param grupoCusto
	 *            the grupo de custo
	 * @param login
	 *            the login
	 * @return
	 */
	public FollowGrupoCusto findByGrupoCustoAndLogin(
			final GrupoCusto grupoCusto, final String login) {
		return dao.findByGrupoCustoAndLogin(grupoCusto, login);
	}
}
