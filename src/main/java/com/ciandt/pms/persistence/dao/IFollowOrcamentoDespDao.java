package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.FollowOrcamentoDesp;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IFollowOrcamentoDespDao proporciona as funcionalidades de interface
 * para FollowOrcaemntoDesoDao.
 * 
 * @since 26/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public interface IFollowOrcamentoDespDao extends
        IAbstractDao<Long, FollowOrcamentoDesp> {

    /**
     * Busca por orcamentoDespesa e login.
     * 
     * @param entity
     *            orcamentodespesa
     * @param login
     *            login.
     * @return list.
     */
    FollowOrcamentoDesp findByOrcDespesaAndLogin(final OrcamentoDespesa entity,
            final String login);

    /**
     * Busca por login.
     * 
     * @param login
     *            login
     * @return lista
     */
    List<FollowOrcamentoDesp> findByLogin(final String login);

    /**
     * Busca por orcamentoDespesa.
     * 
     * @param entity
     *            orcamentoDespesa
     * @return listResult.
     */
    List<FollowOrcamentoDesp> findByOrcDespesa(final OrcamentoDespesa entity);
    
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
	List<FollowOrcamentoDesp> findByPessoaAndDateOfOpenVoucher(
			final Pessoa pessoa, final Date date);
}
