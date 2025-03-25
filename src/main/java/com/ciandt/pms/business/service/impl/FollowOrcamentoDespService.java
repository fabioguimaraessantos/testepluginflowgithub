package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IFollowOrcamentoDespService;
import com.ciandt.pms.model.FollowOrcamentoDesp;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IFollowOrcamentoDespDao;


/**
 * 
 * A classe FollowOrcamentoDespService proporciona as funcionalidades de servico
 * para FollowOrcamentoDesp.
 * 
 * @since 26/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class FollowOrcamentoDespService implements IFollowOrcamentoDespService {

    /**
     * Instancia de dao.
     */
    @Autowired
    private IFollowOrcamentoDespDao followOrcDespDao;

    /**
     * Cria FollowOrcamentoDesp.
     * 
     * @param entity
     *            FollowOrcaemntoDesp
     * @return true
     */
    @Transactional
    public Boolean createFollowOrcamentoDesp(final FollowOrcamentoDesp entity) {
        followOrcDespDao.create(entity);
        return Boolean.TRUE;
    }

    /**
     * Remove entidade do banco.
     * 
     * @param entity
     *            entidade
     */
    @Transactional
    public void removeFollowOrcamentoDesp(final FollowOrcamentoDesp entity) {
        followOrcDespDao.remove(entity);
    }

    /**
     * Busca por OrcamentoDespesa e login.
     * 
     * @param entity
     *            orcamentoDespesa
     * @param login
     *            login
     * @return lista
     */
    public FollowOrcamentoDesp findByOrcDespesaAndLogin(
            final OrcamentoDespesa entity, final String login) {
        return followOrcDespDao.findByOrcDespesaAndLogin(entity, login);
    }

    /**
     * Busca por login.
     * 
     * @param login
     *            login
     * @return lista
     */
    public List<FollowOrcamentoDesp> findByLogin(final String login) {
        return followOrcDespDao.findByLogin(login);
    }

    /**
     * Busca por orcamento despesa.
     * 
     * @param entity
     *            orcamento despesa
     * @return listResult.
     */
    public List<FollowOrcamentoDesp> findByOrcDespesa(
            final OrcamentoDespesa entity) {
        return followOrcDespDao.findByOrcDespesa(entity);
    }
    
    /**
	 * Carrega map de followers.
	 * 
	 * @param pessoa
	 *            pessoa logada
	 * @return map
	 */
    @Override
	public Map<Long, String> loadMapFollow(final Pessoa pessoa) {
		Map<Long, String> mapFollowers = new HashMap<Long, String>();

		// Busca seguidores
		List<FollowOrcamentoDesp> list = this.findByLogin(pessoa.getCodigoLogin());

		// Carrega o mapa
		for (FollowOrcamentoDesp follow : list) {
			mapFollowers.put(follow.getOrcamentoDespesa()
					.getCodigoOrcaDespesa(), follow.getCodigoLogin());
		}

		return mapFollowers;
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
	public List<FollowOrcamentoDesp> findByPessoaAndDateOfOpenVoucher(
			Pessoa pessoa, Date date) {
		return followOrcDespDao.findByPessoaAndDateOfOpenVoucher(pessoa, date);
	}
}