package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.vo.FaturaReceitaRow;
import com.ciandt.pms.model.vo.FiscalBalancePendingClobRow;
import com.ciandt.pms.model.vo.FiscalBalanceRow;

/**
 * 
 * A classe IFiscalBalanceService proporciona a interface de acesso ao serviço
 * de FiscalBalanceService.
 * 
 * @since 23/05/2014
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
@Service
public interface IFiscalBalanceService {

	/**
	 * Realiza consulta de acordo com o filtro para tela de Fiscal Balance >
	 * Search
	 * 
	 * @param msa
	 *            Objeto {@link Msa}
	 * @param endMonth
	 *            MM/yyyy
	 * @param option
	 *            Opção radio button selecionada
	 * @return Lista de {@link FiscalBalanceRow}
	 */
	List<FiscalBalanceRow> findFiscalBalanceByFilter(final Msa msa,
			final Date endMonth, final String option);

	/**
	 * Prepara a lista de faturas ainda nao receitados.
	 * 
	 * @param dealFiscal
	 *            - DealFiscal utilizado para buscar a lista.
	 * 
	 * @return lista de FaturaReceita pendentes.
	 */
	List<FaturaReceitaRow> findBalanceInvoicePending(final DealFiscal dealFiscal);

	/**
	 * Prepara a lista de CLOBs ainda nao faturados.
	 * 
	 * @param dealFiscal
	 *            - DealFiscal utilizado para buscar a lista.
	 * 
	 * @return lista com CLobs pendentes (que nao foram faturados).
	 */
	List<FiscalBalancePendingClobRow> findBalanceClobPending(
			final DealFiscal dealFiscal);

	Date defineEndDate(final Date endMonth, final String option);
}
