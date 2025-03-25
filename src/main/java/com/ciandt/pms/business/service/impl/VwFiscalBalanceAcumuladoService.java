package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwFiscalBalanceAcumuladoService;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.VwFiscalBalanceAcumulado;
import com.ciandt.pms.persistence.dao.IVwFiscalBalanceAcumuladoDao;

/**
 * A classe IVwFiscalBalanceAcumuladoService proporciona a interface de acesso a
 * camada de serviço referente a entidade {@link VwFiscalBalanceAcumulado}.
 * 
 * @since Jun 29, 2014
 * @author <a href="mailto:vidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public class VwFiscalBalanceAcumuladoService implements
		IVwFiscalBalanceAcumuladoService {

	/** Instancia do DAO. */
	@Autowired
	private IVwFiscalBalanceAcumuladoDao dao;

	/**
	 * Retorna todas as entidades de acordo com o fiscalDeal.
	 * 
	 * @return retorna uma lista de {@link VwFiscalBalanceDealFiscal}.
	 */
	public List<VwFiscalBalanceAcumulado> findByfiscalDeal(
			final DealFiscal fiscalDeal) {

		return dao.findByfiscalDeal(fiscalDeal);
	}

	/**
	 * Retorna {@link VwFiscalBalanceAcumulado} com o Cliente e/ou MSA indicado
	 *
	 * @param msa
	 * @param cliente
	 * @return Lista de {@link VwFiscalBalanceAcumulado}
	 */
	public List<VwFiscalBalanceAcumulado> findByMsaAndCliente(final Msa msa,
			final Cliente cliente) {

		return dao.findByMsaAndCliente(msa, cliente);
	}

	/**
	 * Retorna {@link VwFiscalBalanceAcumulado} com {@code DealFiscal},
	 * {@code startDate} e {@code endDate} indicados.
	 * 
	 * @param dealFiscal
	 * @param startDate
	 * @param endDate
	 * @return Lista de {@link VwFiscalBalanceAcumulado}
	 */
	public List<VwFiscalBalanceAcumulado> findByDealFiscalAndPeriod(
			final DealFiscal dealFiscal, final Date startDate,
			final Date endDate) {

		return dao.findByDealFiscalAndPeriod(dealFiscal, startDate, endDate);
	}

	public List<VwFiscalBalanceAcumulado> findByDealFiscalAndStartDate(
			final DealFiscal dealFiscal, final Date startDate) {

		return dao.findByDealFiscalAndStartDate(dealFiscal, startDate);
	}

	public Double findPublishedFiscalBalanceByDealFiscal(
			final DealFiscal dealFiscal) {

		return dao.findPublishedFiscalBalanceByDealFiscal(dealFiscal);
	}
}