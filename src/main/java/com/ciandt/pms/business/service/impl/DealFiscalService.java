package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IHistoricoPercentualIntercompService;
import com.ciandt.pms.business.service.ITipoServicoDealFiscalService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.enums.SgTipoServico;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.model.TipoServicoDealFiscal;
import com.ciandt.pms.persistence.dao.IDealFiscalDao;

/**
 * 
 * A classe DealService proporciona as funcionalidades da camada de serviço
 * referente a entidade Deal.
 * 
 * @since 15/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class DealFiscalService implements IDealFiscalService {

	/** Instancia do dao da entidade Deal. */
	@Autowired
	private IDealFiscalDao dealFiscalDao;

	@Autowired
	private ITipoServicoDealFiscalService tipoServicoDealFiscalService;
	
	@Autowired
	private IHistoricoPercentualIntercompService historicoPercentualIntercompService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public synchronized void createDealFiscal(final DealFiscal entity) {

		this.createDealName(entity);

		dealFiscalDao.create(entity);
	}

	/**
	 * Cria e seta o nome do deal.
	 * 
	 * @param deal
	 *            - deal que se deseja criar o nome
	 */
	private void createDealName(final DealFiscal deal) {

		Long sequence = this.findDealFiscalMaxByMsa(deal.getMsa());

		if (sequence != null) {
			deal.setNumeroSequencia(++sequence);
		} else {
			deal.setNumeroSequencia(1L);
		}

		deal.setNomeDealFiscal(systemProperties
				.getProperty(Constants.DEAL_FISCAL_DEFAULT_ACRONYM_NAME)
				+ deal.getNumeroSequencia()
				+ " - "
				+ deal.getEmpresa().getCodigoMnemonico()
				+ "/"
				+ deal.getCliente().getCodigoMnemonico()
				+ "-"
				+ deal.getTipoServico().getSiglaTipoServico());
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	public void updateDealFiscal(final DealFiscal entity) {

		historicoPercentualIntercompService.update(entity.getHistoricoPercentualIntercomps());

		List<TipoServicoDealFiscal> tipoSerivoDFList = tipoServicoDealFiscalService
				.findByDealFiscal(entity);

		String siglaTipoDealFiscal = "";
		if (tipoSerivoDFList.size() == 1) {
			siglaTipoDealFiscal = "-"
					+ tipoSerivoDFList.get(0).getTipoServico()
							.getSiglaTipoServico();
		}

		// cria o nome da entidade
		entity.setNomeDealFiscal(systemProperties
				.getProperty(Constants.DEAL_FISCAL_DEFAULT_ACRONYM_NAME)
				+ entity.getNumeroSequencia()
				+ " - "
				+ entity.getEmpresa().getCodigoMnemonico()
				+ "/"
				+ entity.getCliente().getCodigoMnemonico()
				+ siglaTipoDealFiscal);

		dealFiscalDao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @return retorna true se removido com sucesso, caso contrario false.
	 */
	public Boolean removeDealFiscal(final DealFiscal entity) {
		// verifica se existem Receitas
		if (!entity.getReceitaDealFiscals().isEmpty()) {
			Messages.showError("removeDealFiscal",
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					new Object[] { Constants.ENTITY_NAME_DEAL,
							Constants.ENTITY_NAME_HORAS_FATURADAS_DEAL });

			return false;
			// verifica se existem Faturas
		} else if (!entity.getFaturas().isEmpty()) {
			Messages.showError("removeDealFiscal",
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					new Object[] { Constants.ENTITY_NAME_DEAL,
							Constants.ENTITY_NAME_FATURA });

			return false;
			// verifica se existem ContratoPraticas
		} else if (!entity.getCpraticaDfiscals().isEmpty()) {
			Messages.showError("removeDealFiscal",
					Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					new Object[] { Constants.ENTITY_NAME_DEAL,
							Constants.ENTITY_NAME_CONTRATO_PRATICA });

			return false;
		}

		// limpa a lista de tipos de serviços
		// associado ao deal para realizar a remoção
		entity.getTipoServicos().clear();
		// realiza a remoção
		dealFiscalDao.remove(entity);

		return true;

	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param entityId
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public DealFiscal findDealFiscalById(final Long entityId) {
		
		DealFiscal dealFiscal = null;
		
		if (entityId != null) {
			dealFiscal = dealFiscalDao.findById(entityId);
		}
		
		if (dealFiscal != null) {
			Hibernate.initialize(dealFiscal.getMsa());
			Hibernate.initialize(dealFiscal.getMsa().getCliente());
		}
		
		return dealFiscal;
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<DealFiscal> findDealFiscalAll() {
		return dealFiscalDao.findAll();
	}

	/**
	 * Retorna todas as entidades com o estado igual a ativo.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<DealFiscal> findDealFiscalAllActive() {
		return dealFiscalDao.findAllActive();
	}

	/**
	 * Retorna o maior numero de sequencia do deal referente a um Msa.
	 * 
	 * @param msa
	 *            - Intancia de Msa
	 * 
	 * @return retorna um long referente ao numero sequencia.
	 */
	public Long findDealFiscalMaxByMsa(final Msa msa) {
		return dealFiscalDao.findMaxByMsa(msa);
	}

	/**
	 * Busca uma lista de entidades relacionadas com o ContratoPratica.
	 * 
	 * @param cp
	 *            entidade do tipo ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<DealFiscal> findDealFiscalByContratoPratica(
			final ContratoPratica cp) {
		return dealFiscalDao.findByContratoPratica(cp);
	}

	/**
	 * Busca uma lista de entidades relacionadas com o ContratoPratica.
	 * 
	 * @param cp
	 *            entidade do tipo ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<DealFiscal> findDealFiscalByContratoPraticaAndActive(
			final ContratoPratica cp) {
		return dealFiscalDao.findByContratoPraticaAndActive(cp);
	}

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os DealFiscal
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<DealFiscal> findDealFiscalByCliente(final Cliente cliente) {
		return dealFiscalDao.findByCliente(cliente);
	}

	/**
	 * Retorna todas as entidades ativas relacionadas com o Msa passado por
	 * parametro.
	 * 
	 * @param msa
	 *            - Msa que se deseja buscar os DealFiscal
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<DealFiscal> findDealFiscalActiveByMsa(final Msa msa) {
		return dealFiscalDao.findActiveByMsa(msa);
	}

	/**
	 * Busca por contrato pratica, moeda e ativos.
	 * 
	 * @param cp
	 *            - contratoPratica
	 * @param moeda
	 *            - moeda
	 * @return lista de Deal Fiscals
	 */
	public List<DealFiscal> findActiveDealFiscalByClobAndCurrency(
			final ContratoPratica cp, final Moeda moeda) {
		return dealFiscalDao.findActiveByClobAndCurrency(cp, moeda);
	}

	/**
	 * Busca por clob, moeda, ativos e nao deletados logicamente.
	 * 
	 * @param cp
	 *            clob
	 * @param moeda
	 *            moeda
	 * @return listReuslt
	 */
	public List<DealFiscal> findActiveByClobAndCurrencyAndNotLogicDeleted(
			final ContratoPratica cp, final Moeda moeda) {
		return dealFiscalDao.findActiveByClobAndCurrencyAndNotLogicDeleted(cp,
				moeda);
	}
	
	/**
	 * Busca {@link DealFiscal} ativos e não deletados logicamente por {@link ContratoPratica}, {@link Moeda}, {@link SgTipoServico}.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica} associado ao  {@link DealFiscal}
	 * @param moeda
	 *            {@link Moeda} associado ao  {@link DealFiscal}
	 * @param siglasTipoServico
	 * 			{@link SgTipoServico} associado ao  {@link DealFiscal}
	 *
	 * @return Lista de {@link DealFiscal}
	 */
	@Override
	public List<DealFiscal> findActiveAndNotLogicDeletedByClobAndCurrencyAndTipoServico(
			final ContratoPratica contratoPratica, final Moeda moeda, final List<String> siglasTipoServico) {
		return dealFiscalDao.findActiveAndNotLogicDeletedByClobAndCurrencyAndTipoServico(contratoPratica,
				moeda, siglasTipoServico);
	}

	/**
	 * Busca msa e nao deletados logicamente. clob
	 * 
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	public List<DealFiscal> findDealFiscalByMsaAndNotLogicalDelete(final Msa msa) {
		return dealFiscalDao.findByMsaAndNotLogicalDelete(msa);
	}

	/**
	 * Busca por msa.
	 * 
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	public List<DealFiscal> findDealFiscalByMsa(final Msa msa) {
		return dealFiscalDao.findByMsa(msa);
	}

	/**
	 * Busca por msa ativo.
	 *
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	public List<DealFiscal> findDealFiscalByMsaActive(final Msa msa) {
		return dealFiscalDao.findByMsaActive(msa);
	}

	/**
	 * Busca por msa, ativo e nao deletado logicamente.
	 * 
	 * @param msa
	 *            - the {@link Msa}
	 * 
	 * @return listresult
	 */
	public List<DealFiscal> findDealFiscalByMsaAndActiveAndNotLogicalDelete(
			final Msa msa) {
		return dealFiscalDao.findByMsaAndActiveAndNotLogicalDelete(msa);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.business.service.IDealFiscalService#findByFilter(com.ciandt
	 * .pms.model.Msa)
	 */
	public List<DealFiscal> findByFilter(final Msa msa) {
		return dealFiscalDao.findByFilter(msa);
	}


	/**
	 * Busca {@link DealFiscal} por {@code contratoPratica} e {@code tipoServico} que estão ativos
	 *
	 * @param contratoPratica
	 * @param tipoServico
	 * @return Lista de {@link DealFiscal}
	 */
	public List<DealFiscal> findByContratoPraticaAndTipoServicoAndActive(
			ContratoPratica contratoPratica, TipoServico tipoServico) {

		List<DealFiscal> filteredDealFiscals = new ArrayList<DealFiscal>();

		List<DealFiscal> dealFiscals = dealFiscalDao.findByContratoPraticaAndActive(contratoPratica);
		for (DealFiscal dealFiscal : dealFiscals) {
			List<TipoServicoDealFiscal> tipoServicoDealFiscals = tipoServicoDealFiscalService.findByDealFiscal(dealFiscal);
			for (TipoServicoDealFiscal tipoServicoDealFiscal : tipoServicoDealFiscals) {
				if (tipoServicoDealFiscal.getTipoServico().getCodigoTipoServico().equals(tipoServico.getCodigoTipoServico())) {
					filteredDealFiscals.add(dealFiscal);
				}
			}
		}

		return filteredDealFiscals;
	}


	@Override
	public List<DealFiscal> findFiscalDealWithActiveAllocationsInAllocationMapByFiscalDealAndClosingMapDate(
			final Long codigoDealFiscal, final Date closingMapDate, final Long codigoEmpresaERP) {
		return dealFiscalDao.findFiscalDealWithActiveAllocationsInAllocationMapByFiscalDealAndClosingMapDate(codigoDealFiscal, closingMapDate, codigoEmpresaERP);
	}

}