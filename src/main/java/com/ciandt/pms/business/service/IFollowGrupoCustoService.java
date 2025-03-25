package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.FollowGrupoCusto;
import com.ciandt.pms.model.GrupoCusto;

/**
 * 
 * A classe IFollowGrupoCustoService proporciona as funcionalidades de interface
 * para FollowGrupoCustoService.
 * 
 * @since 30/07/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila</a>
 * 
 */
@Service
public interface IFollowGrupoCustoService {

	/**
	 * Cria um seguidor de grupo de custo.
	 * 
	 * @param follow
	 *            seguidor
	 */
	void createFollowGrupoCusto(final FollowGrupoCusto follow);

	/**
	 * Remove um seguidor de grupo de custo.
	 * 
	 * @param follow
	 *            seguidor
	 */
	void removeFollowGrupoCusto(final FollowGrupoCusto follow);

	/**
	 * Busca por grupoCusto.
	 * 
	 * @param grupoCusto
	 *            grupoCusto
	 * @return listResult
	 */
	List<FollowGrupoCusto> findByGrupoCusto(final GrupoCusto grupoCusto);

	/**
	 * Busca por login.
	 * 
	 * @param login
	 *            the login
	 * @return listResult
	 */
	List<FollowGrupoCusto> findbyLogin(final String login);

	/**
	 * Busca por grupo de custo e login.
	 * 
	 * @param grupoCusto
	 *            the grupo de custo
	 * @param login
	 *            the login
	 * @return
	 */
	FollowGrupoCusto findByGrupoCustoAndLogin(final GrupoCusto grupoCusto,
			final String login);
}
