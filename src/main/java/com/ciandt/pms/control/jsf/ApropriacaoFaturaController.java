package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IApropriacaoFaturaService;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IFaturaReceitaService;
import com.ciandt.pms.business.service.IItemFaturaService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.control.jsf.bean.ApropriacaoFaturaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.vo.ApropriacaoFaturaFormFilter;
import com.ciandt.pms.model.vo.FaturaReceitaRow;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe ApropriacaoFaturaController proporciona as funcionalidades da camada
 * de apresentação, referente as funcionalidade de apropriação da fatura.
 * 
 * @since 20/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BUS.REVENUE:CR", "BUS.REVENUE:VW", "BUS.REVENUE:ED", "BUS.REVENUE:DE", "BUS.REVENUE.ADJUSTMENT:ED", "BUS.REVENUE.ADJUSTMENT:DE", "BUS.REVENUE.ADJUSTMENT:CR", "BUS.REVENUE.FISCAL_BALANCE:ED"})
public class ApropriacaoFaturaController {

	/** bean do controller. */
	@Autowired
	private ApropriacaoFaturaBean bean;

	/*********** SERVICES **************************/

	/** Instancia do servico Natureza. */
	@Autowired
	private INaturezaCentroLucroService naturezaService;

	/** Instancia do servico Centro Lucro. */
	@Autowired
	private ICentroLucroService centroLucroService;

	/** Instancia do servico Cliente. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do servico ContratoPratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do servico Apropriacao Fatura. */
	@Autowired
	private IApropriacaoFaturaService apropriacaoService;

	/** Instancia do servico FaturaReceita. */
	@Autowired
	private IFaturaReceitaService faturaReceitaService;

	/** instância do serviço ItemFatura. */
	@Autowired
	private IItemFaturaService itemFaturaService;

	/** instancia do servico de DealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;
	
	@Autowired
	private ReceitaController receitaController;

	/************************************************/

	/*********** OUTCOMES **************************/

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_RESEARCH = "apropriacaoFatura_research";

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_MANAGE = "apropriacaoFatura_manage";

	/***********************************************/

	/**
	 * Prepara a tela de filtro.
	 * 
	 * @return retorna a pagina de busca
	 */
	public String prepareResearch() {

		bean.reset();

		this.setDefaultDateFilter();
		this.loadCombos();

		return OUTCOME_RESEARCH;
	}

	/**
	 * Seta a data default inicial do formulario de filtro.
	 */
	private void setDefaultDateFilter() {
		Date currentDate = new Date();
		ApropriacaoFaturaFormFilter filter = bean.getFilter();

		Date startDate = DateUtils.addMonths(currentDate, -1);

		filter.setMesInicio("" + DateUtil.getMonthNumber(startDate));
		filter.setAnoInicio("" + DateUtil.getYear(startDate));

		Date endDate = DateUtils.addMonths(currentDate, 2);

		filter.setMesFim("" + DateUtil.getMonthNumber(endDate));
		filter.setAnoFim("" + DateUtil.getYear(endDate));
	}

	/**
	 * Ação que realiza o filtro.
	 */
	public void findByFilter() {
		bean.setBackTo(OUTCOME_RESEARCH);
		
		ApropriacaoFaturaFormFilter filter = bean.getFilter();

		filter.setCodigoCentroLucro(bean.getCentroLucroMap().get(
				filter.getNomeCentroLucro()));

		filter.setCodigoCliente(bean.getClienteMap().get(
				filter.getNomeCliente()));

		filter.setCodigoContratoPratica(bean.getContratoPraticaMap().get(
				filter.getNomeContratoPratica()));

		filter.setCodigoNatureza(bean.getNaturezaMap().get(
				filter.getNomeNatureza()));

		filter.setCodigoDealFiscal(bean.getDealFiscalMap().get(
				filter.getNomeDealFiscal()));

		bean.setFilterResultList(apropriacaoService
				.findApropriacaoFaturaByFilter(filter));

		if (bean.getFilterResultList().size() == 0) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
	}

	/**
	 * Prepara a tela de gerenciamento da apropriação.
	 * 
	 * @return pagina de gerenciamento.
	 */
	public String prepareManage() {
		ReceitaDealFiscal receitaDealFiscal = bean.getReceitaDealFiscal();

		List<FaturaReceitaRow> rowList = apropriacaoService
				.prepareManage(receitaDealFiscal);
		bean.setFaturaReceitaRowList(rowList);

		if (rowList.isEmpty()) {
			Messages.showWarning("prepareManage",
					Constants.APROPRIACAO_FATURA_WARNING_NO_INVOICE);
		}

		bean.setTotalReceitaDealFiscal(receitaDealFiscal.getValorReceita()
				.doubleValue());

		this.calculateTotalInvoiceValue();
		
		ContratoPratica cp = contratoPraticaService.findContratoPraticaById(receitaDealFiscal.getReceitaMoeda().getReceita().getContratoPratica().getCodigoContratoPratica());
		receitaDealFiscal.getReceitaMoeda().getReceita().setContratoPratica(cp);

		bean.setPatternCurrency(receitaDealFiscal.getReceitaMoeda().getMoeda()
				.getSiglaMoeda());

		return OUTCOME_MANAGE;
	}

	/**
	 * Salva a apropriação.
	 */
	public void save() {
		List<FaturaReceitaRow> frRowList = bean.getFaturaReceitaRowList();
		if (!frRowList.isEmpty()) {
			Boolean success = apropriacaoService.save(frRowList);

			if (success) {
				Messages.showSucess("save", Constants.DEFAULT_MSG_SUCCESS_SAVE);
			}
		} else {
			Messages.showWarning("save",
					Constants.APROPRIACAO_FATURA_WARNING_NO_INVOICE);
		}
	}

	/**
	 * Retorna para a página de search.
	 * 
	 * @return retorna a pagina de busca.
	 */
	public String backTo() {
		
		String outcomeReturn = OUTCOME_RESEARCH;
		
		if (OUTCOME_RESEARCH.equals(bean.getBackTo())) {
			this.findByFilter();
		} else {
			outcomeReturn = receitaController.backTo();
		}
		

		return outcomeReturn;
	}
	
	/**
	 * Calcula o total associado da invoice.
	 */
	public void calculateTotalInvoiceValue() {

		List<FaturaReceitaRow> faturaReceitaRowList = bean
				.getFaturaReceitaRowList();
		Double total = Double.valueOf(0.0);

		for (FaturaReceitaRow faturaReceitaRow : faturaReceitaRowList) {

			FaturaReceita fr = faturaReceitaRow.getFaturaReceita();

			BigDecimal associatedValue = fr.getValorReceitaAssociada();
			if (associatedValue == null) {
				associatedValue = BigDecimal.valueOf(0);

				fr.setValorReceitaAssociada(associatedValue);
			}

			total += associatedValue.doubleValue();

			faturaReceitaRow.setInvoiceBalance(calculateBalance(fr));
		}

		bean.setTotalAssociationInvoiceValue(total);
	}

	/**
	 * Calcula o saldo da apropriação.
	 * 
	 * @param fr
	 *            - FaturaReceita que se deseja calcular o saldo.
	 * 
	 * @return retorna o valor do saldo.
	 */
	private Double calculateBalance(final FaturaReceita fr) {
		Double totalAssociate = faturaReceitaService
				.getFaturaReceitaTotalByFatura(fr.getFatura()).doubleValue();

		Double totalInvoice = itemFaturaService.getItemFaturaTotalByFatura(
				fr.getFatura()).doubleValue();

		if (fr.getCodigoFaturaReceita() != null) {
			FaturaReceita fatRec = faturaReceitaService
					.findFaturaReceitaById(fr.getCodigoFaturaReceita());

			if (fatRec.getValorReceitaAssociada() != null) {
				totalAssociate -= fatRec.getValorReceitaAssociada()
						.doubleValue();
			}
		}

		totalAssociate += fr.getValorReceitaAssociada().doubleValue();

		return (totalInvoice - totalAssociate);
	}

	/**
	 * Adiciona o valor do saldo ao valor associado.
	 */
	public void addBalanceToAssociatedValue() {
		FaturaReceitaRow row = bean.getFaturaReceitaRow();

		FaturaReceita faturaReceita = row.getFaturaReceita();
		BigDecimal valorReceitaAssociada = BigDecimal.valueOf(0.0);
		if (faturaReceita.getValorReceitaAssociada() != null) {
			valorReceitaAssociada = faturaReceita.getValorReceitaAssociada();
		}

		faturaReceita.setValorReceitaAssociada(valorReceitaAssociada
				.add(BigDecimal.valueOf(row.getInvoiceBalance())));

		calculateTotalInvoiceValue();
	}

	/**
	 * Carrega todos os combos.
	 */
	private void loadCombos() {
		this.loadNaturezaCentroLucroList(naturezaService
				.findNaturezaCentroLucroAll());
		this.loadCentroLucroList(centroLucroService.findCentroLucroAll());
		this.loadClienteList(clienteService.findClienteAllClientePai());
		this.loadContratoPraticaList(contratoPraticaService
				.findContratoPraticaAllComplete());
		this.loadDealFiscalList(dealFiscalService.findDealFiscalAllActive());
	}

	/**
	 * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void prepareCentroLucroCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null && !"".equals(value)) {
			Long codNatureza = bean.getNaturezaMap().get(value);
			NaturezaCentroLucro natureza = null;

			// se o centro de lucro existir
			if (codNatureza != null) {
				natureza = naturezaService
						.findNaturezaCentroLucroById(codNatureza);
				this.loadCentroLucroList(centroLucroService
						.findCentroLucroByNatureza(natureza));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadCentroLucroList(new ArrayList<CentroLucro>());
			}
		} else {
			this.loadCentroLucroList(centroLucroService.findCentroLucroAll());
		}
	}

	/**
	 * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void prepareContratoPraticaCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null && !"".equals(value)) {
			Long codCliente = bean.getClienteMap().get(value);
			Cliente cli = null;

			// se o centro de lucro existir
			if (codCliente != null) {
				cli = clienteService.findClienteById(codCliente);
				this.loadContratoPraticaList(contratoPraticaService
						.findContratoPraticaByCliente(cli));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadCentroLucroList(new ArrayList<CentroLucro>());
			}
		} else {
			this.loadContratoPraticaList(contratoPraticaService
					.findContratoPraticaAllComplete());
		}
		bean.getFilter().setNomeContratoPratica("");
	}

	/**
	 * Popula o combobox do dealFiscal de acordo com o clob selecionado.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void prepareDealFiscalCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null && !"".equals(value)) {
			Long codClob = bean.getContratoPraticaMap().get(value);
			ContratoPratica clob = null;

			// se o centro de lucro existir
			if (codClob != null) {
				clob = contratoPraticaService.findContratoPraticaById(codClob);
				this.loadDealFiscalList(dealFiscalService
						.findDealFiscalByContratoPraticaAndActive(clob));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadDealFiscalList(new ArrayList<DealFiscal>());
			}
		} else {
			this.loadDealFiscalList(dealFiscalService.findDealFiscalAllActive());
		}
	}

	/**
	 * Verifica se o valor do atributo Cliente é valido. Se o valor não é nulo e
	 * existe no clienteMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCliente(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoCliente = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoCliente = bean.getClienteMap().get(strValue);
			if (codigoCliente == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de clientes para combobox.
	 * 
	 * @param clientes
	 *            lista de clientes.
	 * 
	 */
	private void loadClienteList(final List<Cliente> clientes) {

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
	 * Verifica se o valor do atributo CentroLucro é valido. Se o valor não é
	 * nulo e existe no centroLucroMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCentroLucro(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoCentroLucro = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoCentroLucro = bean.getCentroLucroMap().get(strValue);
			if (codigoCentroLucro == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de centroLucros para combobox.
	 * 
	 * @param centroLucros
	 *            lista de contratos.
	 * 
	 */
	private void loadCentroLucroList(final List<CentroLucro> centroLucros) {

		Map<String, Long> centroLucroMap = new HashMap<String, Long>();
		List<String> centroLucroList = new ArrayList<String>();

		for (CentroLucro centroLucro : centroLucros) {
			centroLucroMap.put(centroLucro.getNomeCentroLucro(),
					centroLucro.getCodigoCentroLucro());
			centroLucroList.add(centroLucro.getNomeCentroLucro());
		}

		bean.setCentroLucroMap(centroLucroMap);
		bean.setCentroLucroList(centroLucroList);
	}

	/**
	 * Popula a lista de centroLucros para combobox.
	 * 
	 * @param centroLucros
	 *            lista de contratos.
	 * 
	 */
	private void loadDealFiscalList(final List<DealFiscal> dealFiscalList) {

		Map<String, Long> dealFiscalMap = new HashMap<String, Long>();
		List<String> dealFiscalListReturn = new ArrayList<String>();

		for (DealFiscal dealFiscal : dealFiscalList) {
			dealFiscalMap.put(dealFiscal.getNomeDealFiscal(),
					dealFiscal.getCodigoDealFiscal());
			dealFiscalListReturn.add(dealFiscal.getNomeDealFiscal());
		}

		bean.setDealFiscalMap(dealFiscalMap);
		bean.setDealFiscalList(dealFiscalListReturn);
	}

	/**
	 * Verifica se o valor do atributo NaturezaCentroLucro é valido. Se o valor
	 * não é nulo e existe no naturezaMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateNaturezaCentroLucro(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoNatureza = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoNatureza = bean.getNaturezaMap().get(strValue);
			if (codigoNatureza == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de naturezas para combobox da natureza.
	 * 
	 * @param naturezas
	 *            lista de naturezas.
	 * 
	 */
	private void loadNaturezaCentroLucroList(
			final List<NaturezaCentroLucro> naturezas) {

		Map<String, Long> naturezaCentroLucroMap = new HashMap<String, Long>();
		List<String> naturezaCentroLucroList = new ArrayList<String>();

		for (NaturezaCentroLucro natureza : naturezas) {
			naturezaCentroLucroMap.put(natureza.getNomeNatureza(),
					natureza.getCodigoNatureza());
			naturezaCentroLucroList.add(natureza.getNomeNatureza());
		}

		bean.setNaturezaMap(naturezaCentroLucroMap);
		bean.setNaturezaList(naturezaCentroLucroList);
	}

	/**
	 * Verifica se o valor do atributo ContratoPratica é valido. Se o valor não
	 * é nulo e existe no contratoPraticaMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateContratoPratica(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoContratoPratica = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoContratoPratica = bean.getContratoPraticaMap().get(strValue);
			if (codigoContratoPratica == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Busca todas as FaturaReceita associadas a uma fatura.
	 */
	public void findRevenuesByFatura() {

		List<FaturaReceita> faturaReceitaList = apropriacaoService
				.findRevenuesByInvoice(bean.getFatura());

		bean.setFaturaReceitaList(faturaReceitaList);

	}

	/**
	 * Popula a lista de ContratoPratica para combobox.
	 * 
	 * @param pContratoPraticaList
	 *            lista de ContratoPratica.
	 * 
	 */
	private void loadContratoPraticaList(
			final List<ContratoPratica> pContratoPraticaList) {

		Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
		List<String> contratoPraticaList = new ArrayList<String>();

		for (ContratoPratica contratoPratica : pContratoPraticaList) {
			contratoPraticaMap.put(contratoPratica.getNomeContratoPratica(),
					contratoPratica.getCodigoContratoPratica());
			contratoPraticaList.add(contratoPratica.getNomeContratoPratica());
		}

		bean.setContratoPraticaMap(contratoPraticaMap);
		bean.setContratoPraticaList(contratoPraticaList);
	}

	/**
	 * @return the bean
	 */
	public ApropriacaoFaturaBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final ApropriacaoFaturaBean bean) {
		this.bean = bean;
	}

}
