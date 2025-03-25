package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.FollowOrcamentoDesp;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IFollowOrcamentoDespService proporciona as funcionalidades de
 * interface para FollowOrcamentoDespService.
 * 
 * @since 26/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface IFollowOrcamentoDespService {
    
    /**
     * Cria FollowORcamentoDesp.
     * @param entity FollowOrcaemntoDesp
     * @return true
     */
    Boolean createFollowOrcamentoDesp(final FollowOrcamentoDesp entity);
    
    /**
     * Remove entidade do banco.
     * @param entity entidade
     */
    void removeFollowOrcamentoDesp(final FollowOrcamentoDesp entity);

    /**
     * Busca por OrcamentoDespesa e login.
     * @param entity orcamentoDespesa
     * @param login login
     * @return lista
     */
    FollowOrcamentoDesp findByOrcDespesaAndLogin(final OrcamentoDespesa entity, final String login);
    
    /**
     * Busca por login.
     * @param login login
     * @return lista
     */
    List<FollowOrcamentoDesp> findByLogin(final String login);
    
    /**
     * Busca por orcamento despesa.
     * @param entity orcamento despesa
     * @return listResult.
     */
    List<FollowOrcamentoDesp> findByOrcDespesa(final OrcamentoDespesa entity);
    
    /**
	 * Carrega map de followers.
	 * 
	 * @param pessoa
	 *            pessoa logada
	 * @return map
	 */
	Map<Long, String> loadMapFollow(final Pessoa pessoa);

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