package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FollowOrcamentoDesp;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IFollowOrcamentoDespDao;

/**
 * 
 * A classe FollowOrcamentoDespDao proporciona as funcionalidades de dao para
 * FollowOrcamentoDesp.
 * 
 * @since 26/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class FollowOrcamentoDespDao extends
		AbstractDao<Long, FollowOrcamentoDesp> implements
		IFollowOrcamentoDespDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public FollowOrcamentoDespDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, FollowOrcamentoDesp.class);
	}

	/**
	 * Busca por orcamentoDespesa e login.
	 * 
	 * @param entity
	 *            orcamentodespesa
	 * @param login
	 *            login
	 * @return list.
	 */
	@SuppressWarnings("unchecked")
	public FollowOrcamentoDesp findByOrcDespesaAndLogin(
			final OrcamentoDespesa entity, final String login) {
		List<FollowOrcamentoDesp> listResult = getJpaTemplate()
				.findByNamedQuery(
						FollowOrcamentoDesp.FIND_BY_ORC_DESPESA_AND_LOGIN,
						new Object[] { entity.getCodigoOrcaDespesa(), login });
		return listResult.get(0);
	}

	/**
	 * Busca por login.
	 * 
	 * @param login
	 *            login
	 * @return lista
	 */
	@SuppressWarnings("unchecked")
	public List<FollowOrcamentoDesp> findByLogin(final String login) {
		List<FollowOrcamentoDesp> listResult = getJpaTemplate()
				.findByNamedQuery(FollowOrcamentoDesp.FIND_BY_LOGIN,
						new Object[] { login });
		return listResult;
	}

	/**
	 * Busca por orcamentoDespesa.
	 * 
	 * @param entity
	 *            orcamentoDespesa
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<FollowOrcamentoDesp> findByOrcDespesa(
			final OrcamentoDespesa entity) {
		List<FollowOrcamentoDesp> listResult = getJpaTemplate()
				.findByNamedQuery(FollowOrcamentoDesp.FIND_BY_ORC_DESPESA,
						new Object[] { entity.getCodigoOrcaDespesa() });
		return listResult;
	}

	/**
	 * Busca por {@link FollowOrcamentoDesp} que estão sendo seguidos pela
	 * {@link Pessoa} e possuem ao menos um Voucher "OPEN" com data menor que
	 * {@code date}
	 * 
	 * @param Pessoa
	 *            pessoa
	 * @param date
	 * @return List of {@link FollowOrcamentoDesp}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<FollowOrcamentoDesp> findByPessoaAndDateOfOpenVoucher(
			Pessoa pessoa, Date date) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoPessoa", pessoa.getCodigoPessoa());
		params.put("date", date);

		List<FollowOrcamentoDesp> followOrcamentoDesps = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						FollowOrcamentoDesp.FIND_BY_PESSOA_AND_DATE_OF_OPEN_VOUCHER,
						params);

		return followOrcamentoDesps;
	}
}
