package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FollowCliente;
import com.ciandt.pms.model.FollowGrupoCusto;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.persistence.dao.IFollowGrupoCustoDao;

/**
 * 
 * A classe FollowClienteDao proporciona as funcionalidades de DAO para
 * FollowGrupoCusto.
 * 
 * @since 30/07/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila</a>
 * 
 */
@Repository
public class FollowGrupoCustoDao extends AbstractDao<Long, FollowGrupoCusto>
		implements IFollowGrupoCustoDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public FollowGrupoCustoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, FollowGrupoCusto.class);
	}

	/**
	 * Busca por codigoGrupoCusto.
	 * 
	 * @param login
	 *            codigoLogin.
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<FollowGrupoCusto> findByCodigoGrupoCusto(
			final Long codigoGrupoCusto) {
		List<FollowGrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
				FollowGrupoCusto.FIND_BY_GRUPO_CUSTO,
				new Object[] { codigoGrupoCusto });
		return listResult;
	}

	/**
	 * Busca por login.
	 * 
	 * @param login
	 *            the login
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<FollowGrupoCusto> findbyLogin(final String login) {
		List<FollowGrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
				FollowGrupoCusto.FIND_BY_LOGIN, new Object[] { login });
		return listResult;
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
	@SuppressWarnings("unchecked")
	public FollowGrupoCusto findByGrupoCustoAndLogin(
			final GrupoCusto grupoCusto, final String login) {
		List<FollowGrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
				FollowGrupoCusto.FIND_BY_GRUPO_CUSTO_AND_LOGIN,
				new Object[] { grupoCusto.getCodigoGrupoCusto(), login });
		
		return listResult.get(0);
	}
}
