package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.IntegrateRevenueBean;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.integration.queue.RevenueProducer;
import com.ciandt.pms.metrics.PrometheusMetrics;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ReceitaDealFiscalRow;
import com.ciandt.pms.model.vo.ReceitaLicencaIntegravelRow;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.MailSenderUtil;
import com.ciandt.pms.enums.ErpEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * A classe IntegrateRevenueController proporciona as funcionalidades de
 * integracao das receitas-deal com o ERP.
 *
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * @since 05/01/2010
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.INTEGRATE_REVENUE:VW", "BOF.INTEGRATE_REVENUE:ED"})
public class IntegrateRevenueController {

    /**
     * Logger padrao da classe.
     */
    private final static Logger log = LogManager
            .getLogger(IntegrateRevenueController.class.getName());

    /**
     * Instancia do servico da entidade ReceitaDealFiscal.
     */
    @Autowired
    private IReceitaDealFiscalService rdfService;

    /**
     * Instancia do servico da entidade ContratoPratica.
     */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /**
     * Instancia do servico da entidade Receita.
     */
    @Autowired
    private IReceitaService receitaService;

    /**
     * Instancia do servico da entidade PessoaTipoContrato.
     */
    @Autowired
    private IPessoaTipoContratoService pessoaTipoContratoService;

    /**
     * Instancia do servico da entidade Empresa.
     */
    @Autowired
    private IEmpresaService empresaService;

    /**
     * Instancia do servico da entidade Deal Fiscal.
     */
    @Autowired
    private IDealFiscalService dealFiscalService;

    /**
     * Instancia do servico de TipoServico.
     */
    @Autowired
    private ITipoServicoService tipoServicoService;

    /**
     * Instancia do servico de {@link ReceitaLicenca}.
     */
    @Autowired
    private IReceitaLicencaService receitaLicencaService;

    /**
     * Instancia do servico de {@link ReceitaPlantao}.
     */
    @Autowired
    private IReceitaPlantaoService receitaPlantaoService;

    @Autowired
    private IReceitaDealFiscalService receitaDealFiscalService;

    @Autowired
    private IHistoricoPercentualIntercompService iHistoricoPercentualIntercompService;

    @Autowired
    private IVwPmsIntegReceitaNacionalService vwPmsIntegReceitaNacionalService;

    @Autowired
    private IVwPmsIntegFaturaNacionalService vwPmsIntegFaturaNacionalService;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    private IFaturaService faturaService;

    @Autowired
    private RevenueProducer revenueProducer;

    @Autowired
    private IReceitaIntegracaoSemaforoService receitaIntegracaoSemaforoService;

	@Autowired
	private ICompanyErpService companyErpService;

    /**
     * outcome tela inclusao da entidade.
     */
    private static final String OUTCOME_INTEGRETE_MANAGE = "integraReceita_manager";

    @Autowired
    private IntegrateRevenueBean bean;

    /**
     * Prepara a tela de integracao.
     *
     * @return retorna uma String que representa a pagina de integracao
     */
    public String prepareIntegrate() {
        bean.reset();

        this.loadContratoPraticaList(contratoPraticaService
                .findAllCompleteForCombobox());

        this.loadEmpresaList(empresaService
                .findAllActiveForCombobox());

        return OUTCOME_INTEGRETE_MANAGE;
    }

    /**
     * Acao que integra todas as receitas selecionadas.
     */
    public void integrateAll() {
        if (bean.getIsLicenca()) {
            this.integrateAllReceitasLicenca();
        } else {
            this.integrateAllReceitasServico();
        }
    }

    /**
     * Acao que integra todas as receitas selecionadas do tipo Licenca.
     */
    private void integrateAllReceitasLicenca() {

        final Map<Long, String> companyErp;
        try {
            companyErp = companyErpService.findAllActive();
        } catch (final BusinessException e) {
            Messages.showError("integrateAllReceitasLicenca", e.getMessage());
            return;
        }

        PrometheusMetrics revenuesDealFiscalPublishedMetrics = new PrometheusMetrics("revenues.deal.fiscal.published");
        PrometheusMetrics revenuesDealFiscalNoERPMetrics = new PrometheusMetrics("revenues.deal.fiscal.no.erp");
        PrometheusMetrics revenuesDealFiscalSentIntegrationMetrics = new PrometheusMetrics("revenues.deal.fiscal.sent.for.integration");
        PrometheusMetrics revenuesDealFiscalPublicationErrorMetrics = new PrometheusMetrics("revenues.deal.fiscal.publication.error");

        List<ReceitaLicencaIntegravelRow> selectedRows = bean
                .getAllReceitaLicencaSelected();
        if (selectedRows.isEmpty()) {
            Messages.showWarning("integrateAllReceitasLicenca",
                    Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
            return;
        }

        int successCount = 0;
        boolean hasMoreThanOneServiceType = Boolean.FALSE;

        for (ReceitaLicencaIntegravelRow row : selectedRows) {
            revenuesDealFiscalPublishedMetrics.incrementCounter();

            try {

                if (!"I".equals(row.getTo().getIndicadorStatus())) {
                    DealFiscal dealFiscal = dealFiscalService
                            .findDealFiscalById(row.getTo().getDealFiscal()
                                    .getCodigoDealFiscal());
                    if (tipoServicoService.findTipoServicoByDeal(dealFiscal).size() > 1) {
                        // se o deal fiscal possuir mais eu um tipo de servico
                        hasMoreThanOneServiceType = Boolean.TRUE;
                    }

                    // receita licenca de empresa que não possui ERP: só marca como integrada
                    if (!this.companyHasIntegration(companyErp, dealFiscal.getEmpresa().getCodigoEmpresa())) {

                        revenuesDealFiscalNoERPMetrics.incrementCounter();
                        markLicenseAsIntegrated(row.getTo());
                        successCount++;

                    } else if (companyErp.get(dealFiscal.getEmpresa().getCodigoEmpresa()).equals(ErpEnum.ORACLE.getName())
							|| companyErp.get(dealFiscal.getEmpresa().getCodigoEmpresa()).equals(ErpEnum.XERO.getName())
					) {

                        setStatusRevenueLicense(row.getTo(), Constants.INTEGRACAO_STATUS_PENDENTE);
                        revenuesDealFiscalSentIntegrationMetrics.incrementCounter();
                        successCount++;

                    } else {
                        try {
                            VwPmsIntegReceitaNacional receitaView = vwPmsIntegReceitaNacionalService.findById(row.getTo().getCodigoReceitaLicenca());
                            receitaService.integrate(receitaView);

                            setStatusRevenueLicense(row.getTo(), Constants.INTEGRACAO_STATUS_PENDENTE);
                            revenuesDealFiscalSentIntegrationMetrics.incrementCounter();

                            successCount++;

                        } catch (NullPointerException npe) {
                            String errorMsg = row.getTo().getContratoPratica().getNomeContratoPratica() + " -> " + npe.getMessage();
                            mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle(
                                            "_nls.receita_integracao.mail.exception.subject", row.getTo().getContratoPratica().getNomeContratoPratica()),
                                    npe.getMessage());
                            Messages.showError("reIntegrarReceitaDeal", Constants.INTEGRACAO_RECEITA_MSG_INFO_NULL, errorMsg);
                            throw new Exception(npe);
                        } catch (Exception e) {
                            mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle(
                                            "_nls.receita_integracao.mail.exception.subject", row.getTo().getContratoPratica().getNomeContratoPratica()),
                                    e.getMessage());
                            Messages.showError("reIntegrarReceitaDeal", Constants.INTEGRACAO_RECEITA_MSG_COMPLETE);
                            throw new Exception(e);
                        }
                    }
                }

            } catch (Exception e) {
                revenuesDealFiscalPublicationErrorMetrics.incrementCounter();
            }

        }

        // se todas receitas integradas com sucesso
        if (successCount == selectedRows.size()) {
            Messages.showSucess("integrateAllReceitasLicenca",
                    Constants.INTEGRACAO_RECEITA_MSG_SUCCESS);
            // se algumas revenues integradas com sucesso
        } else if (successCount > 0) {
            Messages.showWarning("integrateAllReceitasLicenca",
                    Constants.INTEGRACAO_RECEITA_MSG_COMPLETE);
        } else {
            // se nenhuma receita integrada
            Messages.showError("integrateAllReceitasLicenca",
                    Constants.INTEGRACAO_RECEITA_MSG_ERROR);
        }

        // se existir mais de um tipo de servico
        if (hasMoreThanOneServiceType) {
            Messages.showWarning(
                    "integrateAllReceitasLicenca",
                    Constants.INTEGRACAO_RECEITA_MSG_WARNING_MORE_THAN_ONE_SERVICE_TYPE);
        }

        this.filter();
    }

    /**
     * Acao que integra todas as receitas selecionadas do tipo Outras.
     */
    private void integrateAllReceitasServico() {

		final Map<Long, String> companyErp;
		try {
			companyErp = companyErpService.findAllActive();
		} catch (final BusinessException e) {
			Messages.showError("integrateAllReceitasServico", e.getMessage());
			return;
		}

        PrometheusMetrics revenuesDealFiscalPublishedMetrics = new PrometheusMetrics("revenues.deal.fiscal.published");
        PrometheusMetrics revenuesDealFiscalNoERPMetrics = new PrometheusMetrics("revenues.deal.fiscal.no.erp");
        PrometheusMetrics revenuesDealFiscalSentIntegrationMetrics = new PrometheusMetrics("revenues.deal.fiscal.sent.for.integration");
        PrometheusMetrics revenuesDealFiscalPublicationErrorMetrics = new PrometheusMetrics("revenues.deal.fiscal.publication.error");

        List<ReceitaDealFiscalRow> rowList = bean.getReceitaDealFiscalRowList();

        int successCount = 0;
        int count = 0;
        boolean hasIntercompanyResource = Boolean.TRUE;
        boolean hasMoreThanOneServiceType = Boolean.FALSE;

        if (this.isSomeReceitaDealSelected()) {
            for (ReceitaDealFiscalRow row : rowList) {
                // prometheus - Total de receitas a integrar
                revenuesDealFiscalPublishedMetrics.incrementCounter();
                try {
                    ReceitaDealFiscal to = row.getTo();
                    if (row.getIsSelected() && !"I".equals(to.getIndicadorStatus())) {
                        Receita receita = to.getReceitaMoeda().getReceita();

                        DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(to.getDealFiscal().getCodigoDealFiscal());
                        ReceitaPlantao receitaPlantao = receitaPlantaoService.findByReceitaDealFiscal(to);

                        count++;
                        if (Boolean.TRUE.equals(validateDealFiscal(dealFiscal))) {
                            // receitas JP, CHI, UK, CIT COL que nao possuem Intercompany, nao integra (só marca como integrada)
							if (!this.companyHasIntegration(companyErp, dealFiscal.getEmpresa().getCodigoEmpresa())
									&& !this.hasIntercompanyHistoryResource(companyErp, receita, dealFiscal)) {

                                markAsIntegrated(to, receita);
                                revenuesDealFiscalNoERPMetrics.incrementCounter();

                                successCount++;

                                //cenario internacional
							} else if (receitaService.isInternationalRevenueWithoutIntercompanyResource(companyErp, receita, dealFiscal)) {
                                hasIntercompanyResource = Boolean.FALSE;

                                setStatusRevenueDealFiscal(to, Constants.INTEGRACAO_STATUS_PENDENTE, Boolean.TRUE);
                                revenuesDealFiscalSentIntegrationMetrics.incrementCounter();

                                if (receitaPlantao != null) {
                                    setRevenueDutyHoursToIntegrate(receitaPlantao, true);
                                }
                                successCount++;
                                //cenário Receita Nacional e Intercompany
							} else if (receitaService.isInternationalRevenueWithIntercompany(companyErp, receita, dealFiscal)) {
                                //Sinalizar a receita para integracao no Revenue-API
                                List<ReceitaIntegracaoSemaforo> receitaIntegracaoSemaforoList = receitaIntegracaoSemaforoService.findByReceitaDealFiscal(to.getCodigoReceitaDfiscal());

                                if (receitaIntegracaoSemaforoList.isEmpty()) {
                                    ReceitaDealFiscal rdf = receitaDealFiscalService.findReceitaDealById(to.getCodigoReceitaDfiscal());
                                    ReceitaIntegracaoSemaforo receitaIntegracaoSemaforo = new ReceitaIntegracaoSemaforo();
                                    receitaIntegracaoSemaforo.setReceitaDealFiscal(rdf);
                                    receitaIntegracaoSemaforoService.createReceitaIntegracaoSemaforo(receitaIntegracaoSemaforo);
                                }

								if (!this.companyHasIntegration(companyErp, dealFiscal.getEmpresa().getCodigoEmpresa())) {

                                    markAsIntegrated(to, receita);
                                    revenuesDealFiscalNoERPMetrics.incrementCounter();

                                } else {
                                    setStatusRevenueDealFiscal(to, Constants.INTEGRACAO_STATUS_PENDENTE, dealFiscal.getIndicadorIntercompany());
                                    revenuesDealFiscalSentIntegrationMetrics.incrementCounter();
                                }
                                successCount++;
                            } else {
                                VwPmsIntegReceitaNacional receitaView = vwPmsIntegReceitaNacionalService.findById(to.getCodigoReceitaDfiscal());
                                try {
                                    if (!receitaView.getIsIntercompany().equals("Y")) {
                                        receitaService.integrate(receitaView);
                                    } else {
                                        if (receitaPlantao != null) {
                                            setRevenueDutyHoursToIntegrate(receitaPlantao, true);
                                        }
                                    }

                                    //Integrar pro MEGA somente receitas nacionais e sem intercompany
                                    if (receitaPlantao != null && !dealFiscal.getIndicadorIntercompany()) {
                                        if (receitaPlantao.getValorReceitaPlantao().doubleValue() > 0) {
                                            VwPmsIntegReceitaNacional receitaPlantaoView = vwPmsIntegReceitaNacionalService.findById(receitaPlantao.getCodigoReceitaPlantao());
                                            receitaService.integrate(receitaPlantaoView);
                                        }
                                    }
                                    // TODO: porque esse cara demora muito???
                                    System.out.println(new Date());

                                    setStatusRevenueDealFiscal(to, Constants.INTEGRACAO_STATUS_PENDENTE, dealFiscal.getIndicadorIntercompany());
                                    revenuesDealFiscalSentIntegrationMetrics.incrementCounter();

                                    System.out.println(new Date());
                                    successCount++;
                                } catch (NullPointerException npe) {
                                    String nomeContratoPratica = row.getTo().getReceitaMoeda().getReceita().getContratoPratica().getNomeContratoPratica();
                                    String errorMsg = nomeContratoPratica + " -> " + (npe.getMessage() != null ? npe.getMessage() : BundleUtil.getBundle("_nls.receita_integracao.revenue_not_found.message"));
                                    mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle(
                                                    "_nls.receita_integracao.mail.exception.subject", nomeContratoPratica),
                                            BundleUtil.getBundle("_nls.receita_integracao.mail.exception.message", npe.getMessage() != null ? npe.getMessage() : BundleUtil.getBundle("_nls.receita_integracao.revenue_not_found.message"), npe.getStackTrace()));
                                    Messages.showError("reIntegrarReceitaDeal", Constants.INTEGRACAO_RECEITA_MSG_INFO_NULL, errorMsg);
                                    System.out.println("ERROR Message: " + npe.getMessage());
                                    npe.printStackTrace();
                                    throw new Exception(npe);

                                } catch (Exception e) {
                                    // TODO: acho que esse tratamento esta incorreto. O fluxo deveria seguir? Mantive pois ja foi testado.
                                    //							mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle(
                                    //									"_nls.receita_integracao.mail.exception.subject", to.getReceitaMoeda().getReceita().getContratoPratica().getNomeContratoPratica()),
                                    //									BundleUtil.getBundle("_nls.receita_integracao.mail.exception.message", e.getMessage(), e.getStackTrace()));
                                    if (e.getMessage().contains(Constants.DEFAULT_BUNDLES_KEY_BEGIN)) {
                                        Messages.showError("reIntegrarReceitaDeal", e.getMessage());
                                    } else {
                                        Messages.showError("reIntegrarReceitaDeal", Constants.INTEGRACAO_RECEITA_MSG_COMPLETE);
                                    }
                                    throw new Exception(e);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    revenuesDealFiscalPublicationErrorMetrics.incrementCounter();
                }
            }

            // se todas receitas integradas com sucesso
            if (successCount == count) {
                Messages.showSucess("integrateAll",
                        Constants.INTEGRACAO_RECEITA_MSG_SUCCESS);

                // se algumas revenues integradas com sucesso
            } else if (successCount > 0) {
                Messages.showWarning("integrateAll",
                        Constants.INTEGRACAO_RECEITA_MSG_COMPLETE);
            } else {
                // se nenhuma receita integrada
                Messages.showError("integrateAll",
                        Constants.INTEGRACAO_RECEITA_MSG_ERROR);
            }

            if (!hasIntercompanyResource) {
                Messages.showWarning(
                        "integrateAll",
                        Constants.INTEGRACAO_RECEITA_MSG_WARNING_NO_INTERCOMPANY_RESOURCE);
            }

            // se existir mais de um tipo de servico
            if (hasMoreThanOneServiceType) {
                Messages.showWarning(
                        "integrateAll",
                        Constants.INTEGRACAO_RECEITA_MSG_WARNING_MORE_THAN_ONE_SERVICE_TYPE);
            }

            this.filter();
        } else {
            Messages.showWarning("integrateAll",
                    Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
        }
    }

    private boolean validateDealFiscal(DealFiscal dealFiscal) {
        if (dealFiscal.getCliente().getIndicadorAtivo().equalsIgnoreCase(Constants.INACTIVE)) {
            Messages.showError("integrateAll",
                    Constants.MSG_ERROR_CLIENTENTITY_INATIVO,
                    dealFiscal.getCliente().getCodigoMnemonico());
            return false;
        }

        if (dealFiscal.getIndicadorEntrega() == null) {
            Messages.showError("integrateAll",
                    Constants.MSG_ERROR_DELIVERYPLACE_EMPTY,
                    dealFiscal.getNomeDealFiscal());
            return false;
        }

        return true;
    }

	/**
	 * Método que valida se a empresa do FiscalDeal deve ser integrada para algum ERP ou não.
	 * Atualmente essas empresas são Japao, China, UK e Canada.
	 *
	 */
	private boolean companyHasIntegration(final Map<Long, String> companyErp, final Long companyCode) {
		return companyErp.containsKey(companyCode) && StringUtils.isNotBlank(companyErp.get(companyCode));
	}

    private void markAsIntegrated(ReceitaDealFiscal to, Receita receita) {
        receitaService.updateRevenueStatusAsIntegrated(receita);
        to.setIndicadorIntegradoQuickbooks(Constants.FLAG_YES);
        setStatusRevenueDealFiscal(to, Constants.INTEGRACAO_STATUS_INTEGRADO, Boolean.FALSE);
    }

    private void setStatusRevenueDealFiscal(ReceitaDealFiscal receitaDealFiscal, String status, Boolean isIntercompany) {
        ReceitaDealFiscal rdf = receitaDealFiscalService.findReceitaDealById(receitaDealFiscal.getCodigoReceitaDfiscal());
        DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(rdf.getDealFiscal().getCodigoDealFiscal());
        receitaDealFiscal.setIndicadorStatus(status);
        if (receitaDealFiscal.getIndicadorIntegradoQuickbooks() == null && isIntercompany) {
            if (empresaService.isUk(dealFiscal.getEmpresa())
                    || empresaService.isCinqInc(dealFiscal.getEmpresa())
                    || empresaService.isCinqUs(dealFiscal.getEmpresa())
                    || empresaService.isDextraInc(dealFiscal.getEmpresa())
                    || empresaService.isColombia(dealFiscal.getEmpresa())
            ) {
                receitaDealFiscal.setIndicadorIntegradoQuickbooks(Constants.YES);
            } else {
                receitaDealFiscal.setIndicadorIntegradoQuickbooks(Constants.NO);
            }
        }
        if (receitaDealFiscal.getIndicadorIntegradoQBO() == null && isIntercompany) {
            receitaDealFiscal.setIndicadorIntegradoQBO(false);
        }
        if (receitaDealFiscal.getReceitaPlantao() != null && receitaDealFiscal.getReceitaPlantao().getIndicadorIntegradoQBO() == null) {
            receitaDealFiscal.getReceitaPlantao().setIndicadorIntegradoQBO(Constants.NO);
        }

        receitaDealFiscalService.updateReceitaDealFiscal(receitaDealFiscal);
    }

    private void setRevenueDutyHoursToIntegrate(ReceitaPlantao receitaPlantao, Boolean isIntercompany) {
        if (receitaPlantao.getIndicadorIntegradoQBO() == null && isIntercompany) {
            receitaPlantao.setIndicadorIntegradoQBO(Constants.NO);
        }
        receitaPlantaoService.update(receitaPlantao);
    }

    private void markLicenseAsIntegrated(ReceitaLicenca receitaLicenca) {
        receitaLicenca.setIndicadorStatus(Constants.INTEGRACAO_STATUS_INTEGRADO);
        receitaLicenca.setIndicadorVersao(Constants.VERSION_RECEITA_INTEGRATED);
        receitaLicencaService.update(receitaLicenca);
    }

    private void setStatusRevenueLicense(ReceitaLicenca receitaLicenca, String status) {
        receitaLicenca.setIndicadorStatus(status);
        receitaLicenca.setIndicadorIntegradoQBO(false);
        receitaLicencaService.update(receitaLicenca);
    }

    /**
     * Acao que integra todas as receitas selecionadas.
     */
    public void reintegrate() {
        if (bean.getIsLicenca()) {
            try {
                this.reintegrateReceitasLicenca();
            } catch (HibernateException e) {
                Messages.showError("reIntegrarReceitaDeal",
                        Constants.INTEGRACAO_RECEITA_DATABASE_ACCESS_ERP_ERROR);
                log.error(e);
            } catch (SQLException e) {
                Messages.showError("reIntegrarReceitaDeal",
                        Constants.INTEGRACAO_RECEITA_DATABASE_ACCESS_ERP_ERROR);
                log.error(e);
            }
        } else {
            this.reintegrateOutrasReceitas();
        }
        this.filter();
    }

    /**
     * Acao que integra todas as {@link ReceitaLicenca} selecionadas.
     *
     * @throws SQLException
     * @throws HibernateException
     */
    private void reintegrateReceitasLicenca() throws HibernateException,
            SQLException {
        ReceitaLicenca receitaLicencaSelected = bean.getToReceitaLicenca();

        DealFiscal dealFiscal = dealFiscalService
                .findDealFiscalById(receitaLicencaSelected.getDealFiscal()
                        .getCodigoDealFiscal());

        if (tipoServicoService.findTipoServicoByDeal(dealFiscal).size() > 1) {
            Messages.showWarning(
                    "reintegrateReceitasLicenca",
                    Constants.INTEGRACAO_RECEITA_MSG_WARNING_MORE_THAN_ONE_SERVICE_TYPE);
        }

        receitaLicencaService.reintegrate(receitaLicencaSelected);
    }

    /**
     * Acao que integra todas as {@link Receita} selecionadas.
     */
    private void reintegrateOutrasReceitas() {
        // pega o status antigo / anterior da Receita
        ReceitaDealFiscal to = bean.getTo();
        Receita receita = to.getReceitaMoeda().getReceita();
        String indStatusOld = receita.getIndicadorVersao();

        DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(to
                .getDealFiscal().getCodigoDealFiscal());

        // se possuir mais de um tipo de servico nao reintegra.
        if (tipoServicoService.findTipoServicoByDeal(dealFiscal).size() > 1) {
            Messages.showWarning(
                    "reintegrate",
                    Constants.INTEGRACAO_RECEITA_MSG_WARNING_MORE_THAN_ONE_SERVICE_TYPE);
            return;
        }

        rdfService.reIntegrarReceitaDeal(to);

        Receita receitaAux = receitaService.findReceitaById(receita
                .getCodigoReceita());
        // cria o HistoricoReceita
        receitaService.createHistoricoReceita(receitaAux, indStatusOld);
    }

    /**
     * Verifica se algum item foi selecionado.
     *
     * @return true se algum item selecionado, caso contrario retorna false.
     */
    private Boolean isSomeReceitaDealSelected() {
        List<ReceitaDealFiscalRow> rowList = bean.getReceitaDealFiscalRowList();
        for (ReceitaDealFiscalRow row : rowList) {
            if (row.getIsSelected()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Seleciona todos os itens.
     */
    public void selectAll() {
        List<ReceitaDealFiscalRow> rowList = bean.getReceitaDealFiscalRowList();

        for (ReceitaDealFiscalRow row : rowList) {
            if (!"I".equals(row.getTo().getIndicadorStatus())) {
                row.setIsSelected(Boolean.TRUE);
            }
        }
    }

    /**
     * Tira a selecao de todos os itens.
     */
    public void unselectAll() {
        List<ReceitaDealFiscalRow> rowList = bean.getReceitaDealFiscalRowList();
        for (ReceitaDealFiscalRow row : rowList) {
            row.setIsSelected(Boolean.FALSE);
        }
    }

    /**
     * Realiza filtros de receitas de tipo Licenca.
     */
    private void filterLicenseRevenues() {

        List<ReceitaLicenca> receitasLicenca = receitaLicencaService
                .findIntegrableRevenueByFilter(bean
                        .getIntegrableRevenuesFormFilter());

        bean.setAllPending(true);
        List<ReceitaLicencaIntegravelRow> rows = new ArrayList<ReceitaLicencaIntegravelRow>();
        for (ReceitaLicenca receitaLicenca : receitasLicenca) {
            ReceitaLicencaIntegravelRow row = new ReceitaLicencaIntegravelRow(
                    receitaLicenca);
            rows.add(row);
            if (receitaLicenca.getIndicadorStatus() == null
					|| !receitaLicenca.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_PENDENTE)
					|| !receitaLicenca.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_ORACLE)) {
                bean.setAllPending(false);
            }
        }

        bean.setReceitasLicencaIntegraveisRow(rows);

        if (bean.getReceitasLicencaIntegraveisRow().size() == 0) {
            Messages.showWarning("filtraReceitaDeLicenca",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Realiza filtros de receitas de tipo diferente de Licenca.
     */
    private void filterServiceRevenues() {
        Long idContratoPratica = bean.getContratoPraticaMap().get(
                bean.getNomeContratoPratica());

        Long idEmpresa = bean.getEmpresaMap().get(
                bean.getNomeEmpresa());

        Date monthDate = DateUtil.getDate(bean.getMonth(), bean.getYear());

        List<ReceitaDealFiscal> receitaDealFiscalList = rdfService
                .findReceitaDealByFilter(idContratoPratica, idEmpresa, monthDate,
                        bean.getStatus());

//		this.loadReceitaDealFiscalList(receitaDealFiscalList);
        this.loadReceitaDealFiscalReceitaPlantao(receitaDealFiscalList);

        if (bean.getReceitaDealFiscalRowList().size() == 0) {
            Messages.showWarning("filtraReceitaNaoLicenca",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Realiza o filtro das receitas.
     */
    public void filter() {
        bean.resetResultList();

        switch (bean.getReceitaTipo()) {
            case SERVICO:
                this.filterServiceRevenues();
                break;

            case LICENCA:
                this.filterLicenseRevenues();
                break;

            default:
                break;
        }
    }

    /**
     * Carrega a lista de deals do filtro.
     *
     * @param receitaDealFiscalList - lista de ReceitaDealFiscal
     * @param receitaDealFiscalList
     */
    private void loadReceitaDealFiscalList(
            final List<ReceitaDealFiscal> receitaDealFiscalList) {

        List<ReceitaDealFiscalRow> rowList = new ArrayList<ReceitaDealFiscalRow>();

        ReceitaDealFiscalRow row = null;
        for (ReceitaDealFiscal receitaDealFiscal : receitaDealFiscalList) {
            row = new ReceitaDealFiscalRow();
            row.setTo(receitaDealFiscal);
            rowList.add(row);
        }

        bean.setReceitaDealFiscalRowList(rowList);
    }

    /**
     * Carrega a lista de deals do filtro para ReceitaPlantao.
     *
     * @param receitaDealFiscalList - lista de ReceitaDealFiscal
     * @param receitaDealFiscalList
     */
    private void loadReceitaDealFiscalReceitaPlantao(
            final List<ReceitaDealFiscal> receitaDealFiscalList) {

        List<ReceitaDealFiscalRow> rowList = new ArrayList<ReceitaDealFiscalRow>();
        bean.setAllPending(true);
        ReceitaDealFiscalRow row = null;
        for (ReceitaDealFiscal receitaDealFiscal : receitaDealFiscalList) {
            row = new ReceitaDealFiscalRow();

            if (receitaDealFiscal.getReceitaPlantao() == null) {
                receitaDealFiscal.setReceitaPlantao(new ReceitaPlantao(receitaDealFiscal));
            }

			/* INDICA SE É UMA INTEGRAÇÃO ORACLE */
			if(receitaDealFiscal.getOracleDocumentNumber() == null) {
				if (receitaDealFiscal.getIndicadorIntegradoQuickbooks() != null && receitaDealFiscal.getIndicadorIntegradoQuickbooks().equals(Constants.NO)) {
					if (receitaDealFiscal.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_INTEGRADO) && receitaDealFiscal.getTextoError().toUpperCase().contains(BundleUtil.getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_INTEGRADO).toUpperCase())) {
						receitaDealFiscal.setIndicadorStatus(Constants.INTEGRACAO_STATUS_PENDENTE);
						receitaDealFiscal.setTextoError(receitaDealFiscal.getTextoError().concat(" MEGA "));
					}

					if (receitaDealFiscal.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_INTEGRADO) && !receitaDealFiscal.getTextoError().toUpperCase().contains(BundleUtil.getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_INTEGRADO).toUpperCase())) {
						receitaDealFiscal.setIndicadorStatus(Constants.INTEGRACAO_STATUS_ERROR);
					}
				}
			}

            row.setTo(receitaDealFiscal);
            rowList.add(row);

            if (receitaDealFiscal.getIndicadorStatus() == null
					|| !receitaDealFiscal.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_PENDENTE)
					|| !receitaDealFiscal.getIndicadorStatus().equals(Constants.INTEGRACAO_STATUS_ORACLE)
			) {
                bean.setAllPending(false);
            }
        }

        bean.setReceitaDealFiscalRowList(rowList);
    }

    /**
     * Checa se a receita possui recursos intercompany.
     *
	 * @param companyErp especifica a configuracao de ERP que a Company esta configurada para utilizar.
     * @param receita recebe a receita a ser checada.
     * @return retorna true caso exista um ou mais recurso intercompany.
     */
    private boolean hasIntercompanyHistoryResource(final Map<Long, String> companyErp, final Receita receita, final DealFiscal dealFiscal) {
        Receita r = receitaService.findReceitaById(receita.getCodigoReceita());

        List<ItemReceita> itemReceitaList = new ArrayList<ItemReceita>();

        // popula a lista de Item de Receita
        for (ReceitaMoeda receitaMoeda : r.getReceitaMoedas()) {
            itemReceitaList.addAll(receitaMoeda.getItemReceitas());
        }

        if (itemReceitaList != null && !itemReceitaList.isEmpty()) {
            List<HistoricoPercentualIntercomp> listHistoricoIntercompany = iHistoricoPercentualIntercompService.findByDealFiscal(dealFiscal.getCodigoDealFiscal());
            for (ItemReceita item : itemReceitaList) {

                if (item.getValorTotalItem().compareTo(BigDecimal.ZERO) > 0) {
                    PessoaTipoContrato pessoaTipoContrato = pessoaTipoContratoService
                            .findPessTCByPessoaAndDate(item.getPessoa(), item
                                    .getReceitaMoeda().getReceita().getDataMes());

                    Empresa empresaRecurso = empresaService.findEmpresaById(pessoaTipoContrato.getEmpresa().getCodigoEmpresa());
                    Empresa filter = new Empresa();
                    Long empresaPai = empresaRecurso.getCodigoEmpresa();
                    filter.setEmpresa(empresaService.findEmpresaById(empresaPai));
                    List<Empresa> listEmpresaFilialRecurso = empresaService.findEmpresaByFilter(filter);

                    for (HistoricoPercentualIntercomp historicoIntercomp : listHistoricoIntercompany) {
                        if (historicoIntercomp.getDataFim() == null) {
                            for (Empresa empresaFilialRecurso : listEmpresaFilialRecurso) {
                                // Verifica se a empresa Intercompany
                                if (empresaFilialRecurso.getCodigoEmpresa().equals(historicoIntercomp.getEmpresaIntercomp().getCodigoEmpresa())
										&& this.companyHasIntegration(companyErp, empresaFilialRecurso.getCodigoEmpresa())) {
                                    return Boolean.TRUE;
                                }
                            }

                        }
                    }
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Popula a lista de ContratoPratica para combobox de contratos praticas.
     *
     * @param contratosPratica lista de ContratoPratica.
     */
    private void loadContratoPraticaList(
            final List<ContratoPratica> contratosPratica) {

        Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
        List<String> contratoPraticaList = new ArrayList<String>();

        for (ContratoPratica cp : contratosPratica) {
            contratoPraticaMap.put(cp.getNomeContratoPratica(),
                    cp.getCodigoContratoPratica());
            contratoPraticaList.add(cp.getNomeContratoPratica());
        }

        bean.setContratoPraticaMap(contratoPraticaMap);
        bean.setContratoPraticaList(contratoPraticaList);
    }

    /**
     * Popula a lista de Empresa para combobox de empresas.
     *
     * @param empresas lista de Empresa.
     */
    private void loadEmpresaList(
            final List<Empresa> empresas) {

        Map<String, Long> empresaMap = new HashMap<String, Long>();
        List<String> empresaList = new ArrayList<String>();

        for (Empresa e : empresas) {
            empresaMap.put(e.getNomeEmpresa(),
                    e.getCodigoEmpresa());
            empresaList.add(e.getNomeEmpresa());
        }

        bean.setEmpresaMap(empresaMap);
        bean.setEmpresaList(empresaList);
    }

    /**
     * Verifica se o valor do atributo ContratoPratica � valido. Se o valor n�o
     * � nulo e existe no contratoPraticaMap, ent�o o valor � valido.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateContratoPratica(final FacesContext context,
                                        final UIComponent component, final Object value) {

        Long contratoPratica = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            contratoPratica = bean.getContratoPraticaMap().get(strValue);
            if (contratoPratica == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * @return the bean
     */
    public IntegrateRevenueBean getBean() {
        return bean;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(final IntegrateRevenueBean bean) {
        this.bean = bean;
    }
}
