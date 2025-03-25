package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.VwFiscalBalanceAcumulado;

/**
 * 
 * A classe IVwFiscalBalanceAcumuladoService proporciona a interface de acesso a
 * camada de servi�o referente a entidade {@link VwFiscalBalanceAcumulado}.
 * 
 * @since Jun 29, 2014
 * @author <a href="mailto:vidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public interface IVwFiscalBalanceAcumuladoService {

	/**
	 * Retorna todas as entidades de acordo com o fiscalDeal.
	 * 
	 * @return retorna uma lista de {@link VwFiscalBalanceAcumulado}.
	 */
	List<VwFiscalBalanceAcumulado> findByfiscalDeal(final DealFiscal fiscalDeal);

	/**
	 * Retorna {@link VwFiscalBalanceAcumulado} com o Cliente e/ou MSA indicado
	 *
	 * @param msa
	 * @param cliente
	 * @return Lista de {@link VwFiscalBalanceAcumulado}
	 */
	List<VwFiscalBalanceAcumulado> findByMsaAndCliente(final Msa msa,
			final Cliente cliente);

	/**
	 * Retorna {@link VwFiscalBalanceAcumulado} com {@code DealFiscal},
	 * {@code startDate} e {@code endDate} indicados.
	 * 
	 * @param dealFiscal
	 * @param startDate
	 * @param endDate
	 * @return Lista de {@link VwFiscalBalanceAcumulado}
	 */
	List<VwFiscalBalanceAcumulado> findByDealFiscalAndPeriod(
			final DealFiscal dealFiscal, final Date startDate,
			final Date endDate);

	List<VwFiscalBalanceAcumulado> findByDealFiscalAndStartDate(
			final DealFiscal dealFiscal, final Date startDate);

	Double findPublishedFiscalBalanceByDealFiscal(final DealFiscal dealFiscal);
}