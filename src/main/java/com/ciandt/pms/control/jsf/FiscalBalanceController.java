package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IFaturaReceitaService;
import com.ciandt.pms.business.service.IFiscalBalanceService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.IReceitaDealFiscalService;
import com.ciandt.pms.business.service.IVwFiscalBalanceAcumuladoService;
import com.ciandt.pms.control.jsf.bean.FaturaBean;
import com.ciandt.pms.control.jsf.bean.FiscalBalanceBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.VwFiscalBalanceAcumulado;
import com.ciandt.pms.model.vo.DealFiscalFiscalBalanceRow;
import com.ciandt.pms.model.vo.FiscalBalanceFormFilter;
import com.ciandt.pms.model.vo.FiscalBalanceRow;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe FiscalBalanceController proporciona as funcionalidades da camada de
 * apresentação, referente a Fiscal Balance.
 * 
 * @since 23/05/2014
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BUS.REVENUE.FISCAL_BALANCE:VW"})
public class FiscalBalanceController {

	/** bean do controller. */
	@Autowired
	private FiscalBalanceBean bean;

	/*********** SERVICES **************************/

	/** Instancia do servico FiscalBaLanceService. */
	@Autowired
	private IFiscalBalanceService fiscalBalanceService;

	/** Instancia do servico ClienteService. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do servico MsaService. */
	@Autowired
	private IMsaService msaService;

	/** Instancia do servico de DealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/** Instancia do servico de ReceitaDealFiscal. */
	@Autowired
	private IReceitaDealFiscalService receitaDealFiscalService;

	/** Instancia do servico de FaturaReceita. */
	@Autowired
	private IFaturaReceitaService faturaReceitaService;

	/** Instancia do servico de VwFiscalBalanceAcumulado. */
	@Autowired
	private IVwFiscalBalanceAcumuladoService vwFiscalBalanceAcumuladoService;

	/************************************************/

	@Autowired
	private ReceitaController receitaController;

	@Autowired
	private FaturaController faturaController;

	/*********** OUTCOMES **************************/

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_RESEARCH = "fiscalBalance_research";

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_DEAL_FISCAL_FISCAL_BALANCE = "deal_fiscal_fiscal_balance";

	/***********************************************/

	/**
	 * Prepara a tela de filtro.
	 * 
	 * @return retorna a pagina de busca
	 */
	public String prepareResearch() {

		bean.reset();
		bean.setBackTo(OUTCOME_RESEARCH);

		loadClienteList();
		loadMsaList(msaService.findMsaAll());

		return OUTCOME_RESEARCH;
	}

	/**
	 * Ação do botão Back da tela Revenue > Fiscal Balance > Fiscal Deal Retorna
	 * para a página de origem. (Esta tela pode ser chamada por telas
	 * diferentes)
	 * 
	 * @return retorna a pagina de busca.
	 */
	public String back() {

		String outcomeReturn = OUTCOME_RESEARCH;

		// Limpa campos de "Date" e "Until"
		bean.resetFiscalDealOutcome();

		if (OUTCOME_RESEARCH.equals(bean.getBackTo())) {
			// Retorna para tela de Revenue > Search
			this.findByFilter();
		} else if (FaturaBean.BACK_TO_FATURA_MANAGE_VIEW.equals(bean
				.getBackTo())) {
			// Retorna para tela de Visualização de Fatura (Invoice > Search >
			// view)
			outcomeReturn = faturaController.prepareView();
		} else if (FaturaBean.BACK_TO_FATURA_MANAGE.equals(bean.getBackTo())) {
			// Retorna para tela de Edição de Fatura (Invoice > Search > edit)
			outcomeReturn = faturaController.prepareManage();
		} else {
			// Retorna para tela de Receita (Revenue > Search > view/edit)
			outcomeReturn = receitaController.prepareView();
		}

		return outcomeReturn;
	}

	/**
	 * Consulta dados de Fiscal Deals pelo filtro
	 */
	public void findByFilter() {
		bean.setBackTo(OUTCOME_RESEARCH);

		Long codigoMsa = bean.getMsaMap().get(
				bean.getFilter().getMsa().getNomeMsa());

		Long codigoCliente = bean.getClienteMap().get(
				bean.getFilter().getCliente().getNomeCliente());

		if (codigoMsa == null && codigoCliente == null) {
			Messages.showWarning("findByFilter",
					Constants.MSG_WARNING_FISCAL_BALANCE_MSA_OR_CLIENT_REQUIRED);
		} else {

			Msa msaFilter = bean.getFilter().getMsa();
			msaFilter.setCodigoMsa(codigoMsa);

			Cliente clientFilter = bean.getFilter().getCliente();
			clientFilter.setCodigoCliente(codigoCliente);
			msaFilter.setCliente(clientFilter);

			List<FiscalBalanceRow> fiscalBalanceRows = fiscalBalanceService
					.findFiscalBalanceByFilter(msaFilter, bean.getFilter()
							.getEndDate(), bean.getFilter().getOption());

			Collections.sort(fiscalBalanceRows);

			bean.setResultList(fiscalBalanceRows);
		}
	}

	/**
	 * Popula a lista de clientes para combobox.
	 */
	private void loadClienteList() {
		List<Cliente> clientes = clienteService.findClienteAllClientePai();

		Map<String, Long> clienteMap = new HashMap<String, Long>();
		List<String> clienteList = new ArrayList<String>();

		for (Cliente cliente : clientes) {
			clienteMap
					.put(cliente.getNomeCliente(), cliente.getCodigoCliente());
			clienteList.add(cliente.getNomeCliente());
		}
		bean.setClienteMap(clienteMap);
		bean.setClienteList(clienteList);
	}

	/**
	 * Popula a lista de Msa para combobox.
	 * 
	 * @param msas
	 *            lista de contratos.
	 * 
	 */
	private void loadMsaList(final List<Msa> msas) {

		Map<String, Long> msaMap = new HashMap<String, Long>();
		List<String> msaList = new ArrayList<String>();

		for (Msa msa : msas) {
			msaMap.put(msa.getNomeMsa(), msa.getCodigoMsa());
			msaList.add(msa.getNomeMsa());
		}

		bean.setMsaMap(msaMap);
		bean.setMsaList(msaList);
	}

	/**
	 * Popula o combobox do MSA de acordo com o cliente selecionado.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void prepareMsaCombo(final ValueChangeEvent e) {

		clearResultList(e);

		String value = (String) e.getNewValue();

		if (value != null && !"".equals(value)) {
			Long codCli = bean.getClienteMap().get(value);
			Cliente cliente = null;

			// se o Clinte existir
			if (codCli != null) {
				cliente = clienteService.findClienteById(codCli);
				this.loadMsaList(msaService.findMsaByCliente(cliente));
				// senao existir cria uma lista vazia de centro-lucro
			}
		} else {
			this.loadMsaList(msaService.findMsaAll());
		}
	}

	/**
	 * Chamando ao mudar o valor do MSA da tela de Fiscal Balance > Search
	 * 
	 * @param e
	 *            {@link ValueChangeEvent}
	 */
	public void clearResultList(final ValueChangeEvent e) {
		bean.getFilter().setMsa(new Msa());
		bean.setResultList(new ArrayList<FiscalBalanceRow>());
	}

	/**
	 * Chamado ao mudar o valor do combo de Deal Fiscal da tela de Fiscal
	 * Balance > Search > Deal Fiscal
	 * 
	 * @param e
	 *            {@link ValueChangeEvent}
	 */
	public void clearDealFiscalResultList(final ValueChangeEvent e) {
		bean.setDealFiscalFiscalBalanceRowList(new ArrayList<DealFiscalFiscalBalanceRow>());
	}

	/**
	 * Verifica se o MSA é válido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateMsa(final FacesContext context,
			final UIComponent component, final Object value) {

		Long msaId = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			String label = (String) component.getAttributes().get("label");

			msaId = bean.getMsaMap().get(strValue);
			if (msaId == null) {
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			} else {
				if (msaService.findMsaById(msaId) == null) {
					throw new ValidatorException(Messages.getMessageError(
							Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
				}
			}
		}
	}

	/**
	 * Prepara modal com os itens pendentes, a faturar.
	 * 
	 * @return
	 */
	public void pendingBalanceAFaturarModal() {

		bean.setClobPendenteList(fiscalBalanceService
				.findBalanceClobPending(bean.getCurrentDealFiscal()));
	}

	/**
	 * Prepara modal com os itens pendentes, a receitar.
	 * 
	 * @return
	 */
	public void pendingBalanceAReceitarModal() {

		bean.setFaturaPendenteList(fiscalBalanceService
				.findBalanceInvoicePending(bean.getCurrentDealFiscal()));
	}

	/**
	 * @return the bean
	 */
	public FiscalBalanceBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(FiscalBalanceBean bean) {
		this.bean = bean;
	}

	public String prepareDealFiscalFiscalBalanceBlank() {

		DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(bean
				.getCurrentDealFiscal().getCodigoDealFiscal());

		bean.setFilter(new FiscalBalanceFormFilter());

		bean.getFilter().setCurrentDealFiscal(dealFiscal);

		return this.prepareDealFiscalFiscalBalance(bean.getFilter());
	}

	/**
	 * transição do primeira tela para a tela de fiscalBalance por DealFiscal
	 * @return
	 */
	public String prepareDealFiscalFiscalBalanceGeneral() {

		DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(bean
				.getCurrentDealFiscal().getCodigoDealFiscal());

		if (bean.getFilter() == null) {
			bean.setFilter(new FiscalBalanceFormFilter());
		}

		bean.getFilter().setCurrentDealFiscal(dealFiscal);

		return this.prepareDealFiscalFiscalBalance(bean.getFilter());
	}

	/**
	 * Prepara a tela de visualizacao do FiscalBalance por DealFiscal.
	 * 
	 * @return
	 */
	public String prepareDealFiscalFiscalBalance(FiscalBalanceFormFilter formFilter) {
		DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(formFilter
				.getCurrentDealFiscal().getCodigoDealFiscal());

		bean.setCurrentDealFiscal(dealFiscal);
		bean.setEndValidityMonth(formFilter.getEndMonth());
		bean.setEndValidityYear(formFilter.getEndYear());
		bean.setOption(formFilter.getOption());

		bean.setDealFiscalSelected(dealFiscal.getNomeDealFiscal());
		this.loadDealFiscalMap(dealFiscalService.findDealFiscalByMsa(dealFiscal.getMsa()));

		// Filtra os registros pela data da tela
		boolean filterStartDate = false;
		if (formFilter.getStartDate() != null) {

			filterStartDate = true;
		}

		boolean filterEndDate = false;
		Date endDateRevenue = null;
		Date endDateInvoice = null;
		if (formFilter.getEndDate() != null) {

			filterEndDate = true;
			endDateRevenue = fiscalBalanceService.defineEndDate(formFilter.getEndDate(), Constants.FISCAL_BALANCE_LAST_DAY_OF_MONTH);
			endDateInvoice = fiscalBalanceService.defineEndDate(formFilter.getEndDate(), formFilter.getOption());
		}

		List<VwFiscalBalanceAcumulado> vwFiscalBalanceAcumulados = null;
		if (filterStartDate) {
			vwFiscalBalanceAcumulados = vwFiscalBalanceAcumuladoService.findByDealFiscalAndStartDate(dealFiscal, bean.getStartValidityDate());
		} else {
			vwFiscalBalanceAcumulados = vwFiscalBalanceAcumuladoService.findByfiscalDeal(dealFiscal);
		}

		List<VwFiscalBalanceAcumulado> vwFiscalBalanceFiltered = new ArrayList<VwFiscalBalanceAcumulado>();
		if (filterEndDate) {
			
			for (VwFiscalBalanceAcumulado vwFBAcumulado : vwFiscalBalanceAcumulados) {
				if ((vwFBAcumulado.getFonte().equals(Constants.FISCAL_BALANCE_FONTE_REVENUE) && vwFBAcumulado.getDataMes().compareTo(endDateRevenue) <= 0) 
						|| (vwFBAcumulado.getFonte().equals(Constants.FISCAL_BALANCE_FONTE_INVOICE) && vwFBAcumulado.getDataMes().compareTo(endDateInvoice) <= 0)) {
					
					vwFiscalBalanceFiltered.add(vwFBAcumulado);
				}
			}
		} else {
			vwFiscalBalanceFiltered.addAll(vwFiscalBalanceAcumulados);
		}

		// agrupa a lista de FiscalBalance por Mês
		List<VwFiscalBalanceAcumulado> fiscalBalanceGroupedByMonth = new ArrayList<VwFiscalBalanceAcumulado>();
		for (Iterator<VwFiscalBalanceAcumulado> iterator = vwFiscalBalanceFiltered.iterator(); iterator.hasNext();) {
			VwFiscalBalanceAcumulado vwFiscalBalanceAcumulado = (VwFiscalBalanceAcumulado) iterator.next();

			boolean balanceRepeatedDate = false;
			for (VwFiscalBalanceAcumulado fiscalBalanceGrouped : fiscalBalanceGroupedByMonth) {
				if (DateUtil.equalDateMonthAndYear(fiscalBalanceGrouped.getDataMes(), vwFiscalBalanceAcumulado.getDataMes())) {
					fiscalBalanceGrouped.setTotalInvoice(fiscalBalanceGrouped.getTotalInvoice() + vwFiscalBalanceAcumulado.getTotalInvoice());
					fiscalBalanceGrouped.setTotalRevenue(fiscalBalanceGrouped.getTotalRevenue() + vwFiscalBalanceAcumulado.getTotalRevenue());

					balanceRepeatedDate = true;
				}
			}

			if (!balanceRepeatedDate) {
				fiscalBalanceGroupedByMonth.add(vwFiscalBalanceAcumulado);				
			}
		}

		// transforma uma lista de VwFiscalBalanceAcumulado em uma lista de DealFiscalBalanceRow
		List<DealFiscalFiscalBalanceRow> dealFiscalFiscalBalanceRows = new ArrayList<DealFiscalFiscalBalanceRow>();
		for (VwFiscalBalanceAcumulado vwFiscalBalanceAcumulado : fiscalBalanceGroupedByMonth) {
			DealFiscalFiscalBalanceRow d = vwFiscalBalanceAcumulado
					.toDealFiscalFiscalBalanceRow();

			dealFiscalFiscalBalanceRows.add(d);
		}

		// Calcula o valor da coluna "Fiscal Balance" da tabela
		ListIterator<DealFiscalFiscalBalanceRow> li = dealFiscalFiscalBalanceRows
				.listIterator(dealFiscalFiscalBalanceRows.size());

		Double lastPublishedFiscalBalance = null;
		while (li.hasPrevious()) {
			DealFiscalFiscalBalanceRow dealFiscalFiscalBalanceRow = li.previous();
			if (lastPublishedFiscalBalance != null) {
				dealFiscalFiscalBalanceRow.setPublishedFiscalBalance(dealFiscalFiscalBalanceRow.getPublishedFiscalBalance() + lastPublishedFiscalBalance);
			}

			lastPublishedFiscalBalance = dealFiscalFiscalBalanceRow
					.getPublishedFiscalBalance();
		}

		for (DealFiscalFiscalBalanceRow row : dealFiscalFiscalBalanceRows) {
			row.setPublishedFiscalBalance(row.getPublishedFiscalBalance() * -1);
		}

		bean.setDealFiscalFiscalBalanceRowList(dealFiscalFiscalBalanceRows);

		return OUTCOME_DEAL_FISCAL_FISCAL_BALANCE;
	}

	/**
	 * Carrega o mapa e a lista para o combobox de dealFiscal.
	 * 
	 * @param dealFiscalList
	 */
	private void loadDealFiscalMap(final List<DealFiscal> dealFiscalList) {
		List<String> dealFiscalComboList = new ArrayList<String>();
		Map<String, Long> dealFiscalComboMap = new HashMap<String, Long>();
		for (DealFiscal dealFiscal : dealFiscalList) {
			dealFiscalComboMap.put(dealFiscal.getNomeDealFiscal(),
					dealFiscal.getCodigoDealFiscal());
			dealFiscalComboList.add(dealFiscal.getNomeDealFiscal());
		}
		bean.setDealFiscalComboList(dealFiscalComboList);
		bean.setDealFiscalComboMap(dealFiscalComboMap);
	}

	/**
	 * Carrega lista de DealFiscalFiscalBalanceRow.
	 * 
	 * @param receitaDealFiscalList
	 * @return
	 */
	private List<DealFiscalFiscalBalanceRow> loadDealFiscalFiscalBalanceRowList(
			final List<ReceitaDealFiscal> receitaDealFiscalList) {
		List<DealFiscalFiscalBalanceRow> listResult = new ArrayList<DealFiscalFiscalBalanceRow>();
		DealFiscalFiscalBalanceRow row;
		List<FaturaReceita> faturaReceitaList;
		BigDecimal totalInvoiced;
		BigDecimal publishedFiscalBalance;

		for (ReceitaDealFiscal rdf : receitaDealFiscalList) {
			row = new DealFiscalFiscalBalanceRow();
//			reimplementar
//			row.setReceitaDealFiscal(rdf);
			publishedFiscalBalance = new BigDecimal(
					faturaReceitaService.calculatePublishFiscalBalance(rdf));
			publishedFiscalBalance = publishedFiscalBalance.setScale(2,
					RoundingMode.HALF_EVEN);
			row.setPublishedFiscalBalance(publishedFiscalBalance.doubleValue());
			faturaReceitaList = faturaReceitaService
					.findFaturaReceitaByReceitaDealFiscal(rdf);
			totalInvoiced = new BigDecimal(0d);
			for (FaturaReceita faturaReceita : faturaReceitaList) {
				totalInvoiced = totalInvoiced.add(faturaReceita
						.getValorReceitaAssociada());
			}
			totalInvoiced = totalInvoiced.setScale(2, RoundingMode.HALF_EVEN);
			row.setTotalInvoiced(totalInvoiced.doubleValue());

			listResult.add(row);
		}
		// Ordena em ordem decrescente de data da receita
//		Collections.sort(listResult);

		return listResult;
	}

	public void filterDealFiscalFiscalBalance() {
		// Pegando dados do bean
		Long codigoDealFiscal = bean.getDealFiscalComboMap().get(
				bean.getDealFiscalSelected());
		DealFiscal dealFiscal = dealFiscalService
				.findDealFiscalById(codigoDealFiscal);
		Date startDate = DateUtil.getDate(bean.getStartValidityMonth(),
				bean.getStartValidityYear());
		Date endDate = DateUtil.getDate(bean.getEndValidityMonth(),
				bean.getEndValidityYear());

		FiscalBalanceFormFilter formFilter = new FiscalBalanceFormFilter();
		formFilter.setCurrentDealFiscal(dealFiscal);
		formFilter.setEndMonth(bean.getEndValidityMonth());
		formFilter.setEndYear(bean.getEndValidityYear());
		formFilter.setStartMonth(bean.getStartValidityMonth());
		formFilter.setStartYear(bean.getStartValidityYear());
		formFilter.setOption(bean.getOption());

		this.prepareDealFiscalFiscalBalance(formFilter);

//		List<VwFiscalBalanceAcumulado> vwFiscalBalanceAcumulados = vwFiscalBalanceAcumuladoService
//				.findByDealFiscalAndPeriod(dealFiscal, startDate, endDate);
//
//		List<DealFiscalFiscalBalanceRow> dealFiscalFiscalBalanceRows = new ArrayList<DealFiscalFiscalBalanceRow>();
//		for (VwFiscalBalanceAcumulado vwFiscalBalanceAcumulado : vwFiscalBalanceAcumulados) {
//			DealFiscalFiscalBalanceRow d = vwFiscalBalanceAcumulado.toDealFiscalFiscalBalanceRow();
//
//			dealFiscalFiscalBalanceRows.add(d);
//		}
//
//		ListIterator<DealFiscalFiscalBalanceRow> li = dealFiscalFiscalBalanceRows.listIterator(dealFiscalFiscalBalanceRows.size());
//
//		Double lastPublishedFiscalBalance = null;
//		while(li.hasPrevious()) {
//			DealFiscalFiscalBalanceRow dealFiscalFiscalBalanceRow = li.previous();
//			if (lastPublishedFiscalBalance != null) {
//				Double publishedBalance = dealFiscalFiscalBalanceRow.getPublishedFiscalBalance() + lastPublishedFiscalBalance;
//				if (publishedBalance.equals(new Double(0.0))) {
//					publishedBalance = new Double(0.0);
//				}
//				dealFiscalFiscalBalanceRow.setPublishedFiscalBalance(publishedBalance);				
//			}
//
//			lastPublishedFiscalBalance = dealFiscalFiscalBalanceRow.getPublishedFiscalBalance();
//		}
//
//		bean.setDealFiscalFiscalBalanceRowList(dealFiscalFiscalBalanceRows);
	}
}