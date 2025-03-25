package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.FollowGrupoCusto;
import com.ciandt.pms.model.GrupoCusto;

/**
 * 
 * A classe IFollowClienteDao proporciona as funcionalidades de interface para
 * FollowGrupoCustoDao.
 * 
 * @since 30/07/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila</a>
 * 
 */
public interface IFollowGrupoCustoDao extends
		IAbstractDao<Long, FollowGrupoCusto> {

	/**
	 * Busca por codigoGrupoCusto.
	 * 
	 * @param codigoGrupoCusto
	 *            codigoGrupoCusto.
	 * @return listResult.
	 */
	List<FollowGrupoCusto> findByCodigoGrupoCusto(final Long codigoGrupoCusto);

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
