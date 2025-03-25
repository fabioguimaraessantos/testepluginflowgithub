package com.ciandt.pms.control.jsf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IReceitaLicencaService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.control.jsf.bean.ReceitaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.ReceitaLicenca;
import com.ciandt.pms.model.vo.ReceitaLicencaRow;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe ReceitaLicencaController proporciona as funcionalidades da camada de
 * apresenta��o para a entidade Receita.
 * 
 * @since 01/09/2014
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_SR_MANAGER", "ROLE_PMS_AE",
"ROLE_PMS_MANAGER", "BUS.REVENUE:VW", "BUS.REVENUE:ED", "BUS.REVENUE:DE"})
public class ReceitaLicencaController {

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/*********** OUTCOMES **************************/

	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_RECEITA_SEARCH = "receita_research";
	private static final String OUTCOME_RECEITA_LICENCA_MANAGE = "receita_licenca_manage";
	/** outcome tela gerenciamento da entidade - view. */
	private static final String OUTCOME_RECEITA_LICENCA_MANAGE_VIEW = "receita_licenca_manage_view";
	/** outcome tela de criacao de receitas de licen�a. */
	private static final String OUTCOME_RECEITA_LICENSE_ADD = "receita_license_add";

	/*********** SERVICES **************************/

	/** Instancia do servico. */
	@Autowired
	private IReceitaService receitaService;
	
	/** Instancia do servico. */
	@Autowired
	private IReceitaLicencaService receitaLicencaService;

	/** Instancia do servico do ReceitaDealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;
	
	/** Instancia do servico do ModuloService. */
	@Autowired
	private IModuloService moduloService;
	
	/** Instancia do servico do Cliente. */
	@Autowired
	private IClienteService clienteService;

	/*********** BEANS **************************/

	/** Instancia do bean. */
	@Autowired
	private ReceitaBean bean = null;

	/********************************************/

	@Autowired
	private ReceitaController receitaController;


	/**
	 * Pega o bean ReceitaBean.
	 * 
	 * @return retorna o bean
	 */
	public ReceitaBean getBean() {
		return bean;
	}

	/**
	 * Seta o bean ReceitaBean.
	 * 
	 * @param bean
	 *            do tipo ReceitaBean
	 */
	public void setBean(final ReceitaBean bean) {
		this.bean = bean;
	}

	public String prepareManage() {
		bean.setIsRemove(Boolean.FALSE);
		bean.setIsUpdate(Boolean.TRUE);

		ReceitaLicenca receitaLicenca = receitaLicencaService.findById(bean
				.getCurrentRowId());

		if (receitaLicenca.getIndicadorStatus() != null && receitaLicenca.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_PENDENTE)) {
			Messages.showWarning("prepareManage",
					Constants.RECEITA_MSG_AVISO_PENDENTE);
			return null;
		}

		List<ReceitaLicenca> receitaLicencas = new ArrayList<ReceitaLicenca>();
		receitaLicencas.add(receitaLicenca);

		bean.setReceitaLicenca(receitaLicenca);
		bean.setReceitaLicencas(receitaLicencas);

		return OUTCOME_RECEITA_LICENCA_MANAGE;
	}
	
	public String prepareView() {
		bean.setIsRemove(Boolean.FALSE);
		bean.setIsUpdate(Boolean.TRUE);

		ReceitaLicenca receitaLicenca = receitaLicencaService.findById(bean
				.getCurrentRowId());

		List<ReceitaLicenca> receitaLicencas = new ArrayList<ReceitaLicenca>();
		receitaLicencas.add(receitaLicenca);

		bean.setReceitaLicenca(receitaLicenca);
		bean.setReceitaLicencas(receitaLicencas);

		return OUTCOME_RECEITA_LICENCA_MANAGE_VIEW;
	}

	/**
	 * Cria todas as parcelas da receita parcelada.
	 */
	public void create() {

		Integer installments = null;
		Double totalValueRevenue = null;
		try {
			installments = Integer.valueOf(bean.getInstallments());
			totalValueRevenue = bean.getValorReceitaLicenca().doubleValue();
		} catch (NumberFormatException e) {
			//TODO: see if this works
			Messages.showError("createLicense",
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
					Constants.ENTITY_NAME_CONTRATO_PRATICA);

			return;
		}
		String validityMonth = bean.getValidityMonth();
		String validityYear = bean.getValidityYear();
		Date dataMes = DateUtil.getDate(validityMonth, validityYear);

		DealFiscal fiscalDeal = dealFiscalService.findDealFiscalById(bean
				.getDealFiscalNameAndCodeMap().get(bean.getNomeFiscalDeal()));

		Date closingDate = null;
		Boolean isDateAfterClosingDate = null;
		if (fiscalDeal.getIndicadorIntercompany()) {
			closingDate = moduloService.getClosingDateInternationalRevenue();
			isDateAfterClosingDate = moduloService.isDateAfterRevenueClosingDate(dataMes, Constants.CLIENTE_TYPE_INTERNATIONAL);
		} else {
			closingDate = moduloService.getClosingDateRevenue();
			isDateAfterClosingDate = moduloService.isDateAfterRevenueClosingDate(dataMes, Constants.CLIENTE_TYPE_NATIONAL);
		}

		if (!isDateAfterClosingDate) {
			Messages.showError("verifyClosingDateRevenue",
					Constants.MSG_ERROR_AJUSTE_RECEITA_ADD_CLOS_DATE,
					DateUtil.formatDate(closingDate,
							Constants.DEFAULT_DATE_PATTERN_SIMPLE,
							Constants.DEFAULT_CALENDAR_LOCALE));

			return;
		}

		ReceitaLicenca receitaLicenca = new ReceitaLicenca();
		receitaLicenca.setContratoPratica(bean.getTo().getContratoPratica());
		receitaLicenca.setDataMes(dataMes);
		receitaLicenca.setDealFiscal(fiscalDeal);
		receitaLicenca.setIndicadorVersao(bean.getTo().getIndicadorVersao());
		receitaLicenca.setIndicadorStatus(Constants.INTEGRACAO_STATUS_NAO_INTEGRADO);
		receitaLicenca.setTextoObservacao(bean.getTo().getTextoObservacao());
		receitaLicenca.setValorReceita(bean.getValorReceitaLicenca());
		receitaLicenca.setIndicadorDeleteLogico(Constants.NO);

		List<ReceitaLicenca> parcelasReceita = receitaLicencaService.createReceitaLicenca(receitaLicenca, installments, totalValueRevenue);

		List<ReceitaLicencaRow> receitaLicencaRows = ReceitaLicencaRow.toReceitaLicencaRow(parcelasReceita);


		BigDecimal installmentTotal = BigDecimal.ZERO;
		for (ReceitaLicencaRow receitaLicencaRow : receitaLicencaRows) {
			if (receitaLicencaRow.getReceitaLicenca()
					.getIndicadorDeleteLogico().equals(Constants.NO)) {

				installmentTotal = installmentTotal.add(receitaLicencaRow.getReceitaLicenca().getValorReceita());
			}
		}

		bean.setReceitaLicencaInstallmentsTotal(installmentTotal);
		bean.setReceitaLicencaRows(receitaLicencaRows);

		Messages.showSucess("createLicense", Constants.REVENUE_MSG_CREATED_SUCCESSFULLY);
		bean.setCreateReceitaLicencaButtonDisabled(true);
	}

	public String saveReceitaLicenca() {
		ReceitaLicenca receitaLicenca = receitaLicencaService.findById(bean
				.getReceitaLicenca().getCodigoReceitaLicenca());

		receitaLicenca.setTextoObservacao(bean.getReceitaLicenca()
				.getTextoObservacao());
		receitaLicenca.setValorReceita(bean.getReceitaLicenca()
				.getValorReceita());
		receitaLicencaService.update(receitaLicenca);

		bean.setReceitaLicencaRows(null);

		Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_SAVE,
				Constants.ENTITY_NAME_RECEITA);

		return this.prepareManage();
	}

	public String preparePublish() {
		bean.setShowModalConfirmation(Boolean.TRUE);

		return null;
	}

	/**
	 * Publica a receita
	 *
	 * @return
	 */
	public String publish() {
		ReceitaLicenca receitaLicenca = bean.getReceitaLicenca();

		receitaLicenca.setPercentualImposto(receitaLicencaService
				.getTaxaImpostoForReceitaLicenca(receitaLicenca));

		receitaLicenca.setIndicadorVersao(Constants.VERSION_RECEITA_PUBLISHED);

		receitaLicencaService.update(receitaLicenca);

		bean.setShowModalConfirmation(Boolean.FALSE);

		return this.prepareManage();
	}
	
	public String prepareRemove() {
		//TODO: verificar se � necess�rio
		// marca o modo de remo��o como true
		bean.setIsRemove(Boolean.TRUE);
		bean.setIsUpdate(Boolean.FALSE);
		
		ReceitaLicenca receitaLicenca = receitaLicencaService.findById(bean
				.getCurrentRowId());

		if (receitaLicenca.getIndicadorStatus() != null && receitaLicenca.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_PENDENTE)) {
			Messages.showWarning("prepareRemove",
					Constants.RECEITA_MSG_AVISO_PENDENTE);
			return null;
		}
		
		List<ReceitaLicenca> receitaLicencas = new ArrayList<ReceitaLicenca>();
		receitaLicencas.add(receitaLicenca);
		
		bean.setReceitaLicenca(receitaLicenca);
		bean.setReceitaLicencas(receitaLicencas);
		
		return OUTCOME_RECEITA_LICENCA_MANAGE_VIEW;
	}

	/**
	 * Prepara a aba e carrega a lista de HistoricoReceita.
	 * 
	 */
	public void prepareHistory() {

		List<ReceitaLicenca> receitaLicencas = receitaLicencaService
				.findByCodigoPaiReceitaLicenca(bean.getReceitaLicenca()
						.getCodigoPaiReceitaLicenca());

		List<ReceitaLicencaRow> receitaLicencaRows = ReceitaLicencaRow
				.toReceitaLicencaRow(receitaLicencas);

		BigDecimal installmentTotal = BigDecimal.ZERO;
		for (ReceitaLicencaRow receitaLicencaRow : receitaLicencaRows) {
			if (receitaLicencaRow.getReceitaLicenca()
					.getIndicadorDeleteLogico().equals(Constants.NO)) {

				installmentTotal = installmentTotal.add(receitaLicencaRow.getReceitaLicenca().getValorReceita());
			}
		}

		bean.setReceitaLicencaInstallmentsTotal(installmentTotal);
		bean.setReceitaLicencaRows(receitaLicencaRows);
	}

	public String remove() {
		ReceitaLicenca receitaLicenca = receitaLicencaService.findById(bean
				.getReceitaLicenca().getCodigoReceitaLicenca());
		
		receitaLicencaService.removeLogically(receitaLicenca);

		bean.setReceitaLicenca(new ReceitaLicenca());

		Messages.showSucess("revenue", Constants.RECEITA_LICENCA_SUCCESS_DELETE);

		return receitaController.prepareSearch();
	}
}
