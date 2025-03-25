package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IOrcDespesaGcDao;
import com.ciandt.pms.util.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * A classe OrcDespesaGcService proporciona as funcionalidades de servico para a
 * entidade OrcDespesaGc.
 * 
 * @since 10/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public class OrcDespesaGcService implements IOrcDespesaGcService {

	/** Instancia de dao. */
	@Autowired
	private IOrcDespesaGcDao dao;

	/** Instancia do OrcamentoDespesaService. */
	@Autowired
	private IOrcamentoDespesaService orcamentoDespesaService;

	@Autowired
	private IFollowOrcamentoDespService followOrcamentoDespService;

	@Autowired
	private IOrcDespWhiteListService whiteListService;

	@Autowired
	private IOrcDespDelegacaoService orcDespDelegacaoService;

	/**
	 * Busca todas os registros de OrcDespesaGc.
	 * 
	 * @return listResult.
	 */
	@Override
	public List<OrcDespesaGc> findAll() {
		return dao.findAll();
	}

	/**
	 * Busca entidade por id.
	 * 
	 * @param id
	 *            id.
	 * @return entidade.
	 */
	@Override
	public OrcDespesaGc findOrcDespesaGcById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Cria OrcDespesaGc.
	 * 
	 * @param entity
	 *            entidade.
	 * @return true.
	 * @throws IntegrityConstraintException
	 */
	@Override
	@Transactional
	public void createOrcDespesaGc(final OrcDespesaGc entity)
			throws IntegrityConstraintException {
		if (this.nameAlreadyExiste(entity)) {
			throw new IntegrityConstraintException(
					BundleUtil
							.getBundle(Constants.TRAVEL_BUDGET_ALREADY_EXISTS));
		}

		dao.create(entity);
	}

	/**
	 * Checa se o nome do OrcDespesaGc já existe no banco de dados.
	 * 
	 * @param entity
	 *            {@link OrcDespesaGc}
	 * 
	 * @return boolean
	 * 
	 */
	private boolean nameAlreadyExiste(final OrcDespesaGc entity) {

		/*
		 * OrcDespesaGc orcDespGc = this.findByNameAndActive(entity)
		 */
		OrcDespesaGc orcDespGc = this.findByNameAndGrupoCustoAndActive(entity);

		if (orcDespGc == null) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * Busca pelo Grupo de Custo.
	 * 
	 * @param codigoGrupoCusto
	 *            codigoGrupoCusto
	 * @return listResult
	 */
	@Override
	public List<OrcDespesaGc> findByGrupoCustoAndActive(
			final Long codigoGrupoCusto) {
		return dao.findByGrupoCustoAndActive(codigoGrupoCusto);
	}

	/**
	 * Remove (DELETE LOGICO) um {@link OrcDespesaGc}.
	 * 
	 * @param entity
	 *            orcDespesaGc.
	 * @return true.
	 */
	@Override
	@Transactional
	public Boolean removeOrcDespesaGc(final OrcDespesaGc entity) {
		orcamentoDespesaService.removeOrcamentoDespesa(entity
				.getOrcamentoDespesa());

		entity.setIndicadorDeleteLogico(Constants.SIM);
		dao.update(entity);

		return Boolean.TRUE;
	}

	/**
	 * Busca um {@link OrcDespesaGc} por nome.
	 * 
	 * @param name
	 *            nome do {@link OrcDespesaGc}
	 * @return {@link OrcDespesaGc}
	 */
	@Override
	public OrcDespesaGc findByNameAndActive(final String name) {
		return dao.findByNameAndActive(name);
	}

	/**
	 * Busca por orcamentoDespesa.
	 * 
	 * @param orcDesp
	 *            the orcamentoDespesa.
	 * @return listResult
	 */
	@Override
	public OrcDespesaGc findByOrcDespesa(final OrcamentoDespesa orcDesp) {
		return dao.findByOrcDespesa(orcDesp);
	}

	/**
	 * Busca por nome grupo de custo e ativos para validacao de nome na criacao
	 * 
	 * @param orcDespGc
	 *            orcDespGc
	 * @return
	 */
	@Override
	public OrcDespesaGc findByNameAndGrupoCustoAndActive(
			final OrcDespesaGc orcDespGc) {
		return dao.findByNameAndGrupoCustoAndActive(orcDespGc);
	}
	
	/**
	 * Altera um {@link OrcDespesaGc}.
	 * 
	 * @param entity
	 *            orcDespesaGc.
	 * @return true.
	 */
	@Override
	@Transactional
	public void updateOrcDespesaGc(final OrcDespesaGc entity) {
		dao.update(entity);
	}

	/**
	 * Busca apenas os TB vigentes.
	 * 
	 * @return
	 */
	public List<OrcDespesaGc> findOnlyValidByCostGroup(
			final GrupoCusto grupoCusto) {
		return dao.findOnlyValidByCostGroup(grupoCusto);
	}

	@Transactional
	public void clone(OrcamentoDespesa orcamentoDespesa, OrcDespesaGc entity, Boolean copyFollow, Boolean copyWhiteList, Boolean copyWhiteListDelegation) throws IntegrityConstraintException {
		this.createOrcDespesaGc(entity);

		// Criar Follower por default
		FollowOrcamentoDesp follower = new FollowOrcamentoDesp();
		follower.setOrcamentoDespesa(orcamentoDespesa);
		follower.setCodigoLogin(LoginUtil.getLoggedUsername());
		followOrcamentoDespService.createFollowOrcamentoDesp(follower);

		if (Boolean.TRUE.equals(copyWhiteList)) {
			List<OrcDespWhiteList> whiteListOrigin = whiteListService.findByOrcamentoDespesa(orcamentoDespesa);
			for (OrcDespWhiteList whiteList : whiteListOrigin) {
				OrcDespWhiteList whiteListToClone = new OrcDespWhiteList();
				whiteListToClone.setOrcamentoDespesa(entity.getOrcamentoDespesa());
				whiteListToClone.setCodigoLogin(whiteList.getCodigoLogin());
				whiteListService.addPersonOrcDespWhiteList(whiteListToClone);
			}
		}

		if (Boolean.TRUE.equals(copyFollow)) {
			List<FollowOrcamentoDesp> followOrcamentoDespListOrigin = followOrcamentoDespService.findByOrcDespesa(orcamentoDespesa);
			for (FollowOrcamentoDesp follow : followOrcamentoDespListOrigin) {
				FollowOrcamentoDesp followToClone = new FollowOrcamentoDesp();
				followToClone.setOrcamentoDespesa(entity.getOrcamentoDespesa());
				followToClone.setCodigoLogin(follow.getCodigoLogin());
				followOrcamentoDespService.createFollowOrcamentoDesp(followToClone);
			}
		}

		if (Boolean.TRUE.equals(copyWhiteListDelegation)) {
			List<OrcDespDelegacao> delegations = orcDespDelegacaoService.findByOrcDespesa(orcamentoDespesa);
			for(OrcDespDelegacao delegation : delegations) {
				OrcDespDelegacao delegationToClone = new OrcDespDelegacao();
				delegationToClone.setId(new OrcDespDelegacaoId(entity.getOrcamentoDespesa().getCodigoOrcaDespesa(), delegation.getCodigoLogin()));
				delegationToClone.setCodigoLogin(delegation.getCodigoLogin());
				delegationToClone.setOrcDespesa(entity.getOrcamentoDespesa());
				orcDespDelegacaoService.createOrcDespDelegacao(delegationToClone);
			}
		}
	}
}