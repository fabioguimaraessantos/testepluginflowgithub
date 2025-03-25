package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAjusteReceitaService;
import com.ciandt.pms.business.service.ICpraticaDfiscalService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IFaturaReceitaService;
import com.ciandt.pms.business.service.IFaturaService;
import com.ciandt.pms.business.service.IFiscalBalanceService;
import com.ciandt.pms.business.service.IItemFaturaService;
import com.ciandt.pms.business.service.IReceitaDealFiscalService;
import com.ciandt.pms.business.service.IVwFiscalBalanceAcumuladoService;
import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.VwFiscalBalanceAcumulado;
import com.ciandt.pms.model.vo.FaturaReceitaRow;
import com.ciandt.pms.model.vo.FiscalBalancePendingClobRow;
import com.ciandt.pms.model.vo.FiscalBalanceRow;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe FiscalBalanceService proporciona as funcionalidades relacionadas a
 * tela de FiscalBalance.
 * 
 * @since 23/05/2014
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
@Service
public class FiscalBalanceService implements IFiscalBalanceService {

	/** Instancia de DealFiscalService. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/** Instancia de ReceitaDealFiscalService. */
	@Autowired
	private IReceitaDealFiscalService receitaDealFiscalService;

	/** Instancia de AjusteReceitaService. */
	@Autowired
	private IAjusteReceitaService ajusteReceitaService;

	/** Instancia de FaturaReceitaService. */
	@Autowired
	private IFaturaReceitaService faturaReceitaService;

	/** Instancia de FaturaService. */
	@Autowired
	private IFaturaService faturaService;

	/** Instancia de ItemFaturaService. */
	@Autowired
	private IItemFaturaService itemFaturaService;
	
	/** Instancia de CpraticaDfiscalService. */
	@Autowired
	private ICpraticaDfiscalService cpDfService;
	
	/** Instancia de VwFiscalBalanceAcumulado. */
	@Autowired
	private IVwFiscalBalanceAcumuladoService vwFiscalBalanceAcumuladoService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/**
	 * Realiza consulta de acordo com o filtro para tela de Fiscal Balance >
	 * Search
	 * 
	 * @param msa
	 *            Objeto {@link Msa}
	 * @param endMonth
	 *            MM/yyyy
	 * @param option
	 *            Op��o radio button selecionada
	 * @return Lista de {@link FiscalBalanceRow}
	 */
	public List<FiscalBalanceRow> findFiscalBalanceByFilter(final Msa msa,
			final Date endMonth, final String option) {

		Date endDateRevenue = defineEndDate(endMonth, Constants.FISCAL_BALANCE_LAST_DAY_OF_MONTH);
		Date endDateInvoice = defineEndDate(endMonth, option);

		List<VwFiscalBalanceAcumulado> vwFiscalBalanceAcumulados = vwFiscalBalanceAcumuladoService.findByMsaAndCliente(msa, msa.getCliente());
		Map<Long, FiscalBalanceRow> fiscalBalanceRowMap = new HashMap<Long, FiscalBalanceRow>();

		for (VwFiscalBalanceAcumulado vwFBAcumulado : vwFiscalBalanceAcumulados) {

			if ((vwFBAcumulado.getFonte().equals(Constants.FISCAL_BALANCE_FONTE_REVENUE) && vwFBAcumulado.getDataMes().compareTo(endDateRevenue) <= 0) 
					|| (vwFBAcumulado.getFonte().equals(Constants.FISCAL_BALANCE_FONTE_INVOICE) && vwFBAcumulado.getDataMes().compareTo(endDateInvoice) <= 0)) {

				Long codigoDealFiscal = vwFBAcumulado.getDealFiscal().getCodigoDealFiscal(); 
				if (fiscalBalanceRowMap.containsKey(codigoDealFiscal)) {

					FiscalBalanceRow row = fiscalBalanceRowMap.get(codigoDealFiscal);
					row.setTotalReceita(row.getTotalReceita() + vwFBAcumulado.getTotalRevenue());
					row.setTotalFatura(row.getTotalFatura() + vwFBAcumulado.getTotalInvoice());

					fiscalBalanceRowMap.put(codigoDealFiscal, row);
				} else {

					FiscalBalanceRow row = new FiscalBalanceRow();
					row.setDealFiscal(vwFBAcumulado.getDealFiscal());
					row.setTotalReceita(vwFBAcumulado.getTotalRevenue());
					row.setTotalFatura(vwFBAcumulado.getTotalInvoice());

					fiscalBalanceRowMap.put(codigoDealFiscal, row);
				}
			}
		}

		List<FiscalBalanceRow> fiscalBalanceRows = new ArrayList<FiscalBalanceRow>(fiscalBalanceRowMap.values());

		return fiscalBalanceRows;
	}

	/**
	 * Seta a data fim para consulta de faturas. Se option for CFP, o ultimo dia
	 * � o dia 4 do m�s subsequente. Se for Last day, retorna o ultimo dia o mes
	 * 
	 * @param endMonth
	 *            Data informada
	 * @param option
	 * 
	 * @return data fim para consulta de invoices.
	 */
	public Date defineEndDate(final Date endMonth, final String option) {
		Calendar dataFim = Calendar.getInstance();
		dataFim.setTime(endMonth);

		if (Constants.FISCAL_BALANCE_CFP_CUT_DATE_OPTION.equals(option)) {
			// Seta o dia para o dia 04
			dataFim.set(Calendar.DAY_OF_MONTH, Integer.valueOf(systemProperties
					.getProperty(Constants.FISCAL_BALANCE_DAYS_CFP_CUT_DATE)));

			// Seta o mes o proximo
			dataFim.set(Calendar.MONTH,
					dataFim.get(Calendar.MONTH) + Integer.valueOf(1));
		} else {
			int lastDayOfMonth = DateUtil.getLastDayOfMonth(endMonth);
			dataFim.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
		}
		return dataFim.getTime();
	}

	/**
	 * Soma o total de {@link ReceitaDealFiscal} com total de
	 * {@link AjusteReceita} publicadas.
	 * 
	 * @param dealFiscal
	 *            Objeto {@link DealFiscal}
	 * @param dataFim
	 *            Data Fim para consulta de receitas
	 * 
	 * @return Total de receitas e ajustes de receitas publicadas.
	 */
	private Double getTotalReceita(final DealFiscal dealFiscal,
			final Date dataFim) {

		// Consulta o total de as receitas publicadas (DealFiscal)
		BigDecimal totalReceitaDealFiscal = receitaDealFiscalService
				.getTotalPublishedByDealFiscalAndDate(dealFiscal, dataFim);

		// Consulta os ajustes de receitas publicadas
		BigDecimal totalAjusteReceita = ajusteReceitaService
				.getTotalPublishedByDealFiscalAndDate(dealFiscal, dataFim);

		totalReceitaDealFiscal = totalReceitaDealFiscal.add(totalAjusteReceita);

		return totalReceitaDealFiscal.doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IFiscalBalanceService#
	 * findBalanceInvoicePending(com.ciandt.pms.model.DealFiscal)
	 */
	public List<FaturaReceitaRow> findBalanceInvoicePending(
			final DealFiscal dealFiscal) {

		List<FaturaReceita> faturaReceitaList = new ArrayList<FaturaReceita>();

		// Map auxiliar para evitar que a mesma fatura de deal fiscal que nao
		// foi vinculado a uma receita se repita para cada item de
		// ReceitaDealFiscal.
		Map<Long, Fatura> mapFaturaSemReceitaVinculado = new HashMap<Long, Fatura>();

		DealFiscal df = dealFiscalService.findDealFiscalById(dealFiscal
				.getCodigoDealFiscal());

		// Itera a lista de ReceitaDealFiscal para buscar todas as faturas
		// receitas vinculados
		for (ReceitaDealFiscal rdf : df.getReceitaDealFiscals()) {

			// Consulta as faturas vinculadas a receita do deal fiscal
			faturaReceitaList.addAll(faturaReceitaService
					.findFaturaReceitaByReceitaDealFiscal(rdf));

			// Consulta as faturas nao vinculas ao deal fiscal que ainda nao foi
			// vinculada a nenhuma receita
			List<Fatura> faturaList = faturaService
					.findInvoicePendingByDeal(rdf);

			// Adiciona a lista faturaReceitaList a fatura que ainda n�o foi
			// vinculada a receita
			FaturaReceita faturaReceita = null;
			for (Fatura fatura : faturaList) {

				if (mapFaturaSemReceitaVinculado.get(fatura.getCodigoFatura()) == null) {
					faturaReceita = new FaturaReceita();

					faturaReceita.setFatura(fatura);
					faturaReceita.setReceitaDealFiscal(rdf);
					faturaReceita.setValorReceitaAssociada(BigDecimal
							.valueOf(0.0));

					faturaReceitaList.add(faturaReceita);
					mapFaturaSemReceitaVinculado.put(fatura.getCodigoFatura(),
							fatura);
				}
			}
		}

		return setFaturaReceitaRow(faturaReceitaList);
	}

	/**
	 * Preenche lista de FaturaReceitaRow que � mostrado na tabela de Balance -
	 * Invoice Detail Pending Values da modal.
	 * 
	 * @param faturaReceitaList
	 * @return
	 */
	private List<FaturaReceitaRow> setFaturaReceitaRow(
			List<FaturaReceita> faturaReceitaList) {

		// Map auxiliar para evitar duplicidade de faturas na tela
		Map<Long, Fatura> mapTodasFaturaDealFiscal = new HashMap<Long, Fatura>();
		List<FaturaReceitaRow> resultList = new ArrayList<FaturaReceitaRow>();
		FaturaReceitaRow row = null;

		for (FaturaReceita fr : faturaReceitaList) {
			
			// Se existir no map, � porque j� foi adicionado na lista de FaturaReceitaRow
			if (mapTodasFaturaDealFiscal.get(fr.getFatura().getCodigoFatura()) == null) {
				row = new FaturaReceitaRow();

				// Calcula o valor total da fatura vinculado a deal fiscal
				double totalAssociated = faturaReceitaService
						.getFaturaReceitaTotalByFatura(fr.getFatura())
						.doubleValue();

				// Calcula o valor total da fatura
				double totalInvoice = itemFaturaService
						.getItemFaturaTotalByFatura(fr.getFatura())
						.doubleValue();

				// Se o valor associado for igual ao valor total de invoice, n�o
				// mostra na lista
				if (totalAssociated < totalInvoice) {
					row.setFaturaReceita(fr);
					row.setTotalValueInvoice(totalInvoice);
					row.setPendingInvoiceValue(totalInvoice - totalAssociated);

					resultList.add(row);
				}
				mapTodasFaturaDealFiscal.put(fr.getFatura().getCodigoFatura(),
						fr.getFatura());
			}
		}

		// Ordena pela data previsao da fatura
		Collections.sort(resultList);

		return resultList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.business.service.IFiscalBalanceService#findBalanceClobPending
	 * (com.ciandt.pms.model.DealFiscal)
	 */
	public List<FiscalBalancePendingClobRow> findBalanceClobPending(
			final DealFiscal dealFiscal) {

		List<ReceitaDealFiscal> listaFinal = receitaDealFiscalService
				.findByDealFiscal(dealFiscal.getCodigoDealFiscal());

		FiscalBalancePendingClobRow row = null;
		List<FiscalBalancePendingClobRow> rowList = new ArrayList<FiscalBalancePendingClobRow>();

		for (ReceitaDealFiscal rdf : listaFinal) {
			row = new FiscalBalancePendingClobRow();

			Double pendingValue = faturaReceitaService
					.getTotalFaturaByReceitaDeal(rdf).doubleValue();

			Double totalValue = receitaDealFiscalService
					.getTotalReceitaByDealAndReceita(
							rdf.getReceitaMoeda().getReceita(),
							rdf.getDealFiscal()).doubleValue();

			if (totalValue < pendingValue) {

				row.setPendingValue(pendingValue);

				row.setTotalValue(totalValue);

				row.setReceita(rdf.getReceitaMoeda().getReceita());

				row.setPatternCurrency(rdf.getReceitaMoeda().getMoeda()
						.getSiglaMoeda());

				rowList.add(row);
			}
		}

		Collections.sort(rowList);

		return rowList;
	}

}
