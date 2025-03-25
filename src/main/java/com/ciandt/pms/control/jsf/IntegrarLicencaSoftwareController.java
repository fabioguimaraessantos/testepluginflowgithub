package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IIntegrateLicenseService;
import com.ciandt.pms.business.service.ILicencaSoftwareValidacaoService;
import com.ciandt.pms.business.service.ILicencaSwDetailService;
import com.ciandt.pms.business.service.ILicencaSwProjetoParcelaService;
import com.ciandt.pms.business.service.ILicencaSwProjetoPessoaService;
import com.ciandt.pms.business.service.ILicencaSwProjetoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IRateioLicencaSwService;
import com.ciandt.pms.business.service.ITiRecursoService;
import com.ciandt.pms.control.jsf.bean.IntegrarLicencaSoftwareBean;
import com.ciandt.pms.control.jsf.components.impl.CompanySelect;
import com.ciandt.pms.control.jsf.components.impl.InvoiceProjectMegaSelect;
import com.ciandt.pms.control.jsf.components.impl.ResourceTypeSelect;
import com.ciandt.pms.control.jsf.components.impl.Select;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.integration.queue.RevenueProducer;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntry;
import com.ciandt.pms.integration.vo.licenses.IntegLicense;
import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.model.LicencaSwProjetoPessoa;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.model.vo.LicencaSwProjetoCell;
import com.ciandt.pms.model.vo.LicencaSwProjetoRow;
import com.ciandt.pms.model.vo.LicencaSwUserCell;
import com.ciandt.pms.model.vo.LicencaSwUserRow;
import com.ciandt.pms.model.vo.LicencaSwUserVO;
import com.ciandt.pms.poi.HSSFTemplate;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.MailSenderUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"ICB.INTEGRATE_LICENSES:VW"})
public class IntegrarLicencaSoftwareController {

    private static final Logger logger = LogManager.getLogger(IntegrarLicencaSoftwareController.class);

    private static final String OUTCOME_INTEGRATE_MANAGE = "integraLicencaSw_manager";

    private static final String OUTCOME_CHBACK_PESS_SEARCH_PROJECT = "chbackPess_search_project";

    private static final String OUTCOME_INTEGRATE_VIEW = "integraLicencaSw_view";

    private static final String OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT = "chbackPess_edit_by_contract_new";

    private static final String OUTCOME_CHBACK_SW_PROJECT_APPROPRIATE = "chbackPess_edit_by_contract_appropriate";

    @Autowired
    private Properties systemProperties;

    @Autowired
    private IntegrarLicencaSoftwareBean bean;

    @Autowired
    private ITiRecursoService tiRecursoService;

    @Autowired
    private ILicencaSwProjetoParcelaService licencaSwProjetoParcelaService;

    @Autowired
    private ILicencaSwProjetoPessoaService licencaSwProjetoPessoaService;

    @Autowired
    private IRateioLicencaSwService rateioLicencaSwService;

    @Autowired
    private ILicencaSwDetailService licencaSwDetailService;

    @Autowired
    private ILicencaSoftwareValidacaoService licencaSoftwareValidacaoService;

    @Autowired
    private IModuloService moduloService;

    @Autowired
    private RevenueProducer revenueProducer;

    @Autowired
    private MailSenderUtil mailSenderUtil;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private ChargebackController chargebackController;

    @Autowired
    private IIntegrateLicenseService licenseService;

    @Autowired
    private ILicencaSwProjetoService licencaSwProjetoService;

    public String prepareAppropriate() {
        LicencaSwProjetoRow licencaSwProjetoRow = bean.getToProject();

        chargebackController.prepareLicencaProjetoAppropriation(licencaSwProjetoRow);

        return OUTCOME_CHBACK_SW_PROJECT_APPROPRIATE;
    }

    public String prepareIntegrate() {
        bean.reset();

        List<TiRecurso> tiRecursoList = tiRecursoService.findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);
        loadTiRecursoList(tiRecursoList);

        prepareCompanyList();
        bean.setTipoTiRecursoList(getTipoTiRecursos());
        return OUTCOME_INTEGRATE_MANAGE;
    }

    public String integrate() throws NullPointerException, IOException, TimeoutException {
        try {
            int successCount = 0;
            int count = 0;

            if (this.isSomeLicenseSelected()) {
                AccountingEntry accountingEntry;
                if (bean.isProjectLicense()) {

                    for (LicencaSwProjetoRow projetoRow : bean.getLicencaSwProjetoRowList()) {
                        if (projetoRow.getIsSelected()) {
                            if (!projetoRow.getStatus().equals(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                                Messages.showWarning("integrate",
                                        Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_NOT_ALL_APPROVED);

                                return OUTCOME_INTEGRATE_MANAGE;
                            }
                        }
                    }

                    if (this.validateLicencaSwProjetoToApprove(false)) {
                        for (LicencaSwProjetoRow projetoRow : bean.getLicencaSwProjetoRowList()) {

                            if (projetoRow.getIsSelected()) {
                                count++;

                                try {
                                    accountingEntry = licencaSwProjetoParcelaService.generateAccountingByLicencaSwProjetoAndMonth(projetoRow, bean.getMonthDate());
                                    licencaSoftwareValidacaoService.validateRequiredFields(accountingEntry);
                                    this.revenueProducer.send(accountingEntry.toJson(), Constants.SOFTWARE_LICENSE_PRODUCER);
                                    licencaSwProjetoParcelaService.setStatusLicencaSwProjetoParcela(projetoRow.getCodigoLicencaSwProjeto(), DateUtil.getDateFirstDayOfMonth(projetoRow.getDataParcela()), Constants.LICENSE_SW_INTEGRACAO_STATUS_PENDENTE, null);
                                    successCount++;
                                } catch (NullPointerException npe) {
                                    String nomeLicenca = projetoRow.getNomeLicenca();
                                    String errorMsg = nomeLicenca + " -> " + (npe.getMessage() != null ? npe.getMessage() : BundleUtil.getBundle("_nls.licenca_software_integracao.revenue_not_found.message"));
                                    mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle(
                                            "_nls.licenca_software_integracao.mail.exception.subject", nomeLicenca),
                                            BundleUtil.getBundle("_nls.licenca_software_integracao.mail.exception.message", npe.getMessage() != null ? npe.getMessage() : BundleUtil.getBundle("_nls.licenca_software_integracao.revenue_not_found.message"), npe.getStackTrace()));
                                    Messages.showError("integrarLicencaSoftware", Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_INFO_NULL, errorMsg);
                                    System.out.println("ERROR Message: " + npe.getMessage());
                                    npe.printStackTrace();
                                }
                            }
                        }
                    } else {
                        return OUTCOME_INTEGRATE_MANAGE;
                    }
                } else {
                    for (LicencaSwUserRow userRow : bean.getLicencaSwUserRowList()) {
                        if (userRow.getIsSelected()) {
                            if (!userRow.getStatus().equals(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                                Messages.showWarning("integrate",
                                        Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_NOT_ALL_APPROVED);

                                return OUTCOME_INTEGRATE_MANAGE;
                            }
                        }
                    }

                    if (this.validateLicencaSwProjetoToApprove(false)) {
                        for (LicencaSwUserRow userRow : bean.getLicencaSwUserRowList()) {
                            if (userRow.getIsSelected()) {
                                count++;

                                try {
                                    accountingEntry = rateioLicencaSwService.generateAccountingByTiRecursoAndMonth(userRow, bean.getMonthDate());
                                    licencaSoftwareValidacaoService.validateRequiredFields(accountingEntry);

                                    if(!Constants.CD_EMPRESA_INC.equals(bean.getCompanySelect().filter())){
                                        this.revenueProducer.send(accountingEntry.toJson(), Constants.SOFTWARE_LICENSE_PRODUCER);
                                        rateioLicencaSwService.setStatusRateioLicencaSw(userRow.getCodigoTiRecurso(), bean.getMonthDate(), Constants.LICENSE_SW_INTEGRACAO_STATUS_PENDENTE, null);
                                    }else{
                                        IntegLicense payload = this.licenseService.payload(userRow.getCodigoTiRecurso(), bean.getMonthDate(), bean.getCompanySelect().filter());
                                        rateioLicencaSwService.setStatusRateioLicencaSw(userRow.getCodigoTiRecurso(), bean.getMonthDate(), Constants.LICENSE_SW_INTEGRACAO_STATUS_PENDENTE, null);
                                        this.licenseService.integrate(payload);
                                    }

                                    successCount++;
                                } catch (NullPointerException npe) {
                                    String nomeLicenca = userRow.getNomeLicenca();
                                    String errorMsg = nomeLicenca + " -> " + (npe.getMessage() != null ? npe.getMessage() : BundleUtil.getBundle("_nls.licenca_software_integracao.revenue_not_found.message"));
                                    mailSenderUtil.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle(
                                            "_nls.licenca_software_integracao.mail.exception.subject", nomeLicenca),
                                            BundleUtil.getBundle("_nls.licenca_software_integracao.mail.exception.message", npe.getMessage() != null ? npe.getMessage() : BundleUtil.getBundle("_nls.licenca_software_integracao.revenue_not_found.message"), npe.getStackTrace()));
                                    Messages.showError("integrarLicencaSoftware", Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_INFO_NULL, errorMsg);
                                    System.out.println("ERROR Message: " + npe.getMessage());
                                    npe.printStackTrace();
                                }
                            }
                        }
                    } else {
                        return OUTCOME_INTEGRATE_MANAGE;
                    }
                }

                if (successCount == count) {
                    Messages.showSucess("integrate",
                            Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_SUCCESS);
                } else if (successCount > 0) {
                    Messages.showWarning("integrate",
                            Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_COMPLETE);
                } else {
                    Messages.showError("integrate",
                            Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_ERROR);
                }

                this.filter();
            } else {
                Messages.showWarning("integrate",
                        Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("_nls.")) {
                Messages.showError("integrateLicencaSw", e.getMessage());
            } else {
                Messages.showError("integrateLicencaSw", Constants.LICENSE_SW_APROVACAO_ERROR_GENERIC);
            }
        }


        return OUTCOME_INTEGRATE_MANAGE;
    }

    public Boolean isSomeLicenseSelected() {

        if (bean.isProjectLicense()) {
            List<LicencaSwProjetoRow> projetoRowList = bean.getLicencaSwProjetoRowList();
            for(LicencaSwProjetoRow projetoRow : projetoRowList){
                if (projetoRow.getIsSelected()) {
                    return Boolean.TRUE;
                }
            }
        } else {
            List<LicencaSwUserRow> userRowList = bean.getLicencaSwUserRowList();
            for (LicencaSwUserRow userRow : userRowList) {
                if (userRow.getIsSelected()){
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public String prepareView() {

        bean.setLicencaSwUserRowListDetail(null);
        bean.setLicencaSwProjetoRowDetailList(null);
        bean.setPreviousLicencaSwProjetoRowDetailList(null);

        if (bean.getToProject() != null) {
            this.loadLicencaSwProjetoDetail();
        } else {
            this.loadLicencaSwUserDetail();
        }

        return OUTCOME_INTEGRATE_VIEW;
    }

    public String prepareEdit() {
        LicencaSwProjetoRow licencaSwProjetoRow = bean.getToProject();

        chargebackController.prepareLicencaProjetoUpdateFromIntegration(bean.getTipoRecurso(), licencaSwProjetoRow.getCodigoLicencaSwProjeto(), licencaSwProjetoRow.getHasIntegratedInstallments());
        return OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT;
    }

    private void loadTiRecursoList(final List<TiRecurso> tiRecursos) {

        Map<String, Long> tiRecursoMap = new HashMap<String, Long>();
        List<String> tiRecursoList = new ArrayList<String>();

        for (TiRecurso t : tiRecursos) {
            tiRecursoMap.put(t.getNomeTiRecurso(),
                    t.getCodigoTiRecurso());
            tiRecursoList.add(t.getNomeTiRecurso());
        }

        bean.setTiRecursoMap(tiRecursoMap);
        Collections.sort(tiRecursoList);
        bean.setTiRecursoList(tiRecursoList);
    }

    public String backIntegrate() {
        if (bean.getToProject() != null) {
            bean.setToProject(null);
        } else {
            bean.setToUser(null);
        }

        return OUTCOME_INTEGRATE_MANAGE;
    }

    public String backSearch(){
        if (bean.getToProject() != null) {
            bean.setToProject(null);
        } else {
            bean.setToUser(null);
        }

        return OUTCOME_CHBACK_PESS_SEARCH_PROJECT;
    }

    public void validateTiRecurso(final FacesContext context,
                                  final UIComponent component, final Object value) {

        Long tiRecurso = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            tiRecurso = bean.getTiRecursoMap().get(strValue);
            if (tiRecurso == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    public IntegrarLicencaSoftwareBean getBean() {
        return bean;
    }

    public void setBean(final IntegrarLicencaSoftwareBean bean) {
        this.bean = bean;
    }

    //@PostConstruct
    public void filter() {
        bean.resetResultList();

        if (bean.getTipoRecurso() != null) {
            if (bean.getTipoRecurso().equals(Constants.TI_RECURSO_TYPE_SOFTWARE_USER)) {
                this.filterUserLicense();
            }
            if (bean.getTipoRecurso().equals(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT)) {
                this.filterProjectLicense();
            }
        }
    }

    private void filterProjectLicense() {
        Long codTiRecurso = bean.getTiRecursoMap().get(bean.getNomeTiRecurso());

        saveFilterState();

        List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList = licencaSwProjetoParcelaService.findByFilter(
                bean.getMonthDate(),
                codTiRecurso,
                bean.getStatus(),
                bean.getInvoiceNumber(),
                bean.getInvoiceProjectMegaSelect().value(),
                bean.getLicenseID()
        );

        loadLicencaSwProjetoParcela(licencaSwProjetoParcelaList);

        if (bean.getLicencaSwProjetoRowList().isEmpty()) {
            Messages.showWarning("filtraLicencaProjeto",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    private void saveFilterState(){
        bean.setMonthDateFiltered(bean.getMonthDate());
        bean.setStatusFiltered(bean.getStatus());
        bean.setCodigoTiRecursoFiltered(bean.getTiRecursoMap().get(bean.getNomeTiRecurso()));
    }

    private void filterUserLicense() {
        Long codTiRecurso = bean.getTiRecursoMap().get(
                bean.getNomeTiRecurso());


        List<LicencaSwUserVO> licencaSwUserVOList = rateioLicencaSwService
                .findByFilter(bean.getMonthDate(),codTiRecurso,bean.getStatus());

        saveFilterState();
        loadLicencaSwUser(licencaSwUserVOList);

        if (bean.getLicencaSwUserRowList().isEmpty()) {
            Messages.showWarning("filtraLicencaProjeto",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * @param detailList
     * @return - True if has Inc Integration
     */
    private Boolean hasIncIntegration(List<LicencaSwDetail> detailList){
        for (LicencaSwDetail detail : detailList)
            if(detail.getIntegrate() != null)
               return Boolean.TRUE;

        return Boolean.FALSE;
    }

    private void loadLicencaSwUserDetail() {
        bean.setHasIncIntegration(Boolean.FALSE);
        List<LicencaSwDetail> detailList = rateioLicencaSwService.findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(bean.getMonthDate(), bean.getToUser().getCodigoTiRecurso());


        licenseService.findIntegrateDetailsByTiRecurso(bean.getToUser().getCodigoTiRecurso(), bean.getMonthDate(), detailList);
        bean.setLicencaSwUserRowListDetail(detailList);

        if(detailList != null && !detailList.isEmpty())
            bean.setHasIncIntegration(hasIncIntegration(detailList));
    }

    private void loadLicencaSwProjetoDetail() {

        LicencaSwProjeto licencaSwProjeto = new LicencaSwProjeto();
        licencaSwProjeto.setCodigoLicencaSwProjeto(bean.getToProject().getCodigoLicencaSwProjeto());

        List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList = licencaSwProjetoParcelaService.findByLicencaSwProjetoAndMonth(licencaSwProjeto.getCodigoLicencaSwProjeto(), bean.getToProject().getDataParcela());
        List<LicencaSwDetail> detailList = licencaSwDetailService.convertLicencaSwParcela(licencaSwProjetoParcelaList);

        List<LicencaSwProjetoParcela> previousLicencaSwProjetoParcelaList = licencaSwProjetoParcelaService.findByLicencaSwProjetoAndMonthLessThan(licencaSwProjeto.getCodigoLicencaSwProjeto(), bean.getToProject().getDataParcela());
        List<LicencaSwDetail> previousDetailList = licencaSwDetailService.convertLicencaSwParcela(previousLicencaSwProjetoParcelaList);

        BigDecimal valorParcial = BigDecimal.ZERO;
        for (LicencaSwDetail detail : previousDetailList) {
            valorParcial = valorParcial.add(detail.getValor());
        }

        Set<String> loginsLicenca = new HashSet<String>();
        List<LicencaSwProjetoPessoa> licencaSwProjetoPessoas = new ArrayList<LicencaSwProjetoPessoa>();
        licencaSwProjetoPessoas = licencaSwProjetoPessoaService.findByLicencaSwProjeto(licencaSwProjeto);

        for (LicencaSwProjetoPessoa licencaSwProjetoPessoa : licencaSwProjetoPessoas) {
            loginsLicenca.add(licencaSwProjetoPessoa.getPessoa().getCodigoLogin());
        }

        bean.getToProject().setLogins(this.getLoginsLicenca(loginsLicenca));
        bean.setLicencaSwProjetoRowDetailList(detailList);
        bean.setPreviousLicencaSwProjetoRowDetailList(previousDetailList);
        bean.setValorParcial(valorParcial);

    }

    private String getLoginsLicenca(Set<String> loginsLicenca) {
        String logins = "";
        for (String s : loginsLicenca) {
            if (!logins.isEmpty()) {
                logins = logins + " , " + s;
            } else {
                logins = s;
            }
        }

        return logins;
    }


    private void loadLicencaSwProjetoParcela(final List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList) {

        bean.setIsAllPending(true);

        bean.setLicencaSwProjetoRowList(this.groupByLicencaSwProjeto(licencaSwProjetoParcelaList));
    }

    private List<LicencaSwProjetoRow> groupByLicencaSwProjeto(List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList){

        this.calculateClobQuantity(licencaSwProjetoParcelaList);

        List<LicencaSwProjetoRow> rowList = new ArrayList<LicencaSwProjetoRow>();
        for (LicencaSwProjetoParcela licencaSwProjetoParcela : licencaSwProjetoParcelaList) {
            LicencaSwProjetoRow row = new LicencaSwProjetoRow();

            if(!this.findLicencaSwProjetoRowByCodigoLicencaProjeto(rowList, licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto())) {

                BigDecimal valorParcela = licencaSwProjetoParcelaService.findInstallmentValue(licencaSwProjetoParcela.getDataParcela(), licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto(), licencaSwProjetoParcela.getNumeroParcela());
                row.setNomeLicenca(licencaSwProjetoParcela.getLicencaSwProjeto().getTiRecurso() != null ? licencaSwProjetoParcela.getLicencaSwProjeto().getTiRecurso().getNomeTiRecurso() : licencaSwProjetoParcela.getLicencaSwProjeto().getTiRecursoErpName());
                row.setNomeProjeto(getNomeProjeto(licencaSwProjetoParcela));
                row.setValorParcela(licencaSwProjetoParcela.getValorParcela());
                row.setMoeda(licencaSwProjetoParcela.getLicencaSwProjeto().getMoeda());
                row.setEmpresa(licencaSwProjetoParcela.getLicencaSwProjeto().getEmpresa());
                row.setNomeFornecedor(licencaSwProjetoParcela.getLicencaSwProjeto().getProviderName());
                row.setCodigoLicencaSwProjetoParcela(licencaSwProjetoParcela.getCodigoLicencaSwProjetoParcela());
                row.setCodigoLicencaSwProjeto(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto());
                row.setNotaFiscal(licencaSwProjetoParcela.getLicencaSwProjeto().getNotaFiscal());
                row.setParcelaApropriada(licencaSwProjetoParcela.getNumeroParcela());
                row.setStatus(licencaSwProjetoParcela.getStatus());
                row.setTextoErro(licencaSwProjetoParcela.getTextoError());

                List<LicencaSwProjetoParcela> licencaSwProjetoParcelas = licencaSwProjetoParcelaService.findByLicencaSwProjetoAndStatus(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto(), LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName());
                row.setValorApropriacao(licencaSwProjetoParcelaService.sumValorLicencaSwProjetoParcela(licencaSwProjetoParcelas));

                row.setQtdeParcelas(licencaSwProjetoParcela.getLicencaSwProjeto().getQuantidadeParcela());
                row.setSaldoParcelas(licencaSwProjetoParcelaService.findBalanceByLicencaSwProjetoAndMonth(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto()));
                row.setDataParcela(licencaSwProjetoParcela.getDataParcela());
                row.setDataInicio(licencaSwProjetoParcela.getLicencaSwProjeto().getDataInicio());
                row.setValorTotal(licencaSwProjetoParcela.getLicencaSwProjeto().getValorTotal());
                row.setValorParcial(valorParcela);
                row.setIsEditable(licencaSwProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                        || licencaSwProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName()));
                row.setIsAppropriate(checkIsAppropriate(licencaSwProjetoParcela));
                row.setHasIntegratedInstallments(false);

                List<LicencaSwProjetoParcela> licencasProjeto = licencaSwProjetoParcelaService.findByLicencaSwProjeto(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto());
                for (LicencaSwProjetoParcela swProjetoParcela : licencasProjeto) {
                    if (swProjetoParcela.getStatus().equals(LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName())
                            || swProjetoParcela.getStatus().equals(LicencaSwProjetoParcela.Status.PENDENTE.getFieldName())) {
                        row.setHasIntegratedInstallments(true);
                    }
                }

                if (licencaSwProjetoParcela.getStatus() == null
                        || (!licencaSwProjetoParcela.getStatus().equals(Constants.LICENSE_SW_INTEGRACAO_STATUS_PENDENTE)
                            && !licencaSwProjetoParcela.getStatus().equals(Constants.LICENSE_SW_INTEGRACAO_STATUS_INTEGRADO))) {
                    bean.setIsAllPending(false);
                }
                rowList.add(row);
            }
        }
        return rowList;
    }

    private String getNomeProjeto(LicencaSwProjetoParcela licencaSwProjetoParcela) {
        if (licencaSwProjetoParcela.getContratoPratica() != null) {
            return licencaSwProjetoParcela.getContratoPratica().getNomeContratoPratica();
        }
        if (licencaSwProjetoParcela.getGrupoCusto() != null) {
            return licencaSwProjetoParcela.getGrupoCusto().getNomeGrupoCusto();
        }
        return "not found";
    }

    private Boolean checkIsAppropriate(LicencaSwProjetoParcela licencaSwProjetoParcela) {
        if (Boolean.TRUE.equals(DateUtil.after(licencaSwProjetoParcela.getDataParcela(), moduloService.getClosingDateChargeBack()))) {
            if (licencaSwProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                    || licencaSwProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                if (!licencaSwProjetoParcela.getLicencaSwProjeto().getQuantidadeParcela().equals(licencaSwProjetoParcela.getNumeroParcela())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void calculateClobQuantity(List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList){
        Map<Long, Long> mapClob = new HashMap<Long, Long>();
        for(LicencaSwProjetoParcela licencaSwProjetoParcela : licencaSwProjetoParcelaList){
            if(mapClob.containsKey(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto())){
                 mapClob.put(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto(),mapClob.get(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto()) + 1);
            }else {
                mapClob.put(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto(), 1L);
            }
        }

        for(LicencaSwProjetoParcela licencaSwProjetoParcela : licencaSwProjetoParcelaList){
            if(mapClob.containsKey(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto())){
                licencaSwProjetoParcela.setQuantidadeClob(mapClob.get(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto()));
            }
        }

    }

    private Boolean findLicencaSwProjetoRowByCodigoLicencaProjeto(List<LicencaSwProjetoRow> list, Long codigoLicencaProjeto) {
        for (LicencaSwProjetoRow row : list) {
            if(row.getCodigoLicencaSwProjeto().equals(codigoLicencaProjeto)){
                return true;
            }
        }
        return false;
    }

    private Boolean findLicencaSwProjetoParcelaByCodigoLicencaProjeto(List<LicencaSwProjetoParcela> list, Long codigoLicencaProjeto) {
        for (LicencaSwProjetoParcela row : list) {
            if(row.getLicencaSwProjeto().getCodigoLicencaSwProjeto().equals(codigoLicencaProjeto)){
                return true;
            }
        }
        return false;
    }

    private void loadLicencaSwUser(final List<LicencaSwUserVO> licencaSwUserVOList) {

        List<LicencaSwUserRow> rowList = new ArrayList<LicencaSwUserRow>();
        bean.setIsAllPending(true);
        LicencaSwUserRow row = null;

        List<TiRecurso> resources = tiRecursoService.findTiRecursoByFilter(getSfUserFilter());
        boolean foundLicences = (resources != null && !resources.isEmpty()) ? true : false;

        for (LicencaSwUserVO licencaSwUserVO : licencaSwUserVOList) {

            TiRecurso tiRecurso = foundLicences ? filterLicence(licencaSwUserVO.getCodigoTiRecurso(), resources) : null;
            if(tiRecurso != null && tiRecurso.getEmpresa() != null && bean.getCompanySelect().filter().equals(tiRecurso.getEmpresa().getCodigoEmpresa())){
                row = new LicencaSwUserRow();
                row.setCodigoTiRecurso(licencaSwUserVO.getCodigoTiRecurso());
                row.setNomeLicenca(licencaSwUserVO.getNomeLicenca());
                row.setValorTotal(licencaSwUserVO.getValorLicencaTotal());
                row.setStatus(licencaSwUserVO.getStatus());
                row.setTextoErro(licencaSwUserVO.getTextoError());
                row.setLastUpdate(licencaSwUserVO.getLastUpdate());

                if (licencaSwUserVO.getStatus() == null || !licencaSwUserVO.getStatus().equals(Constants.LICENSE_SW_INTEGRACAO_STATUS_PENDENTE)) {
                    bean.setIsAllPending(false);
                }

                rowList.add(row);
            }
        }

        bean.setLicencaSwUserRowList(rowList);
    }

    /**
     * @param codigoTiRecurso - Code to Search
     * @param recursos - List fo search
     * @return TiRecurso - Found
     */
    private TiRecurso filterLicence(Long codigoTiRecurso, List<TiRecurso> recursos){

        for (TiRecurso tiRecurso : recursos) {
            if(codigoTiRecurso.equals(tiRecurso.getCodigoTiRecurso()))
                return tiRecurso;
        }

        return null;
    }

    /**
     * @return - Software User Filter
     */
    private TiRecurso getSfUserFilter(){
        TiRecurso filter = new TiRecurso();
        filter.setIndicadorTipoAlocacao(Constants.TI_RECURSO_TYPE_SOFTWARE_USER);
        return filter;
    }

    /**
     * @param e
     */
    public void prepareTipoTiRecursoCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();
        if(bean.getLicencaSwUserRowList() != null && !bean.getLicencaSwUserRowList().isEmpty())
            bean.setLicencaSwUserRowList(Collections.EMPTY_LIST);

        List<String> tipoTiRecursosList = getTipoTiRecursos();
        if (value != null) {
            Long code = bean.getCompanySelect().map().get(value);

            if(code != null && code.equals(10l)){
                tipoTiRecursosList.remove(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);
                bean.setTipoTiRecursoList(tipoTiRecursosList);

                bean.setTipoRecurso(Constants.TI_RECURSO_TYPE_SOFTWARE_USER);
                loadTiRecursoList(tiRecursoService.findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_USER));

                return;
            }
        }

        bean.setTipoRecurso(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);
        loadTiRecursoList(tiRecursoService.findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT));
        bean.setTipoTiRecursoList(tipoTiRecursosList);
    }

    public void prepareTiRecursoCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null) {
            List<TiRecurso> tiRecursoList = tiRecursoService.findTiRecursoByType(value);
            loadTiRecursoList(tiRecursoList);
        }
    }

    public void downloadLicensesReport() {
        if (bean.getTipoRecurso().equals(Constants.TI_RECURSO_TYPE_SOFTWARE_USER)) {
            this.downloadUserLicensesReport();
        }
        if (bean.getTipoRecurso().equals(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT)){
            this.downloadProjectLicensesReport();
        }
    }

    private void downloadProjectLicensesReport() {
        HSSFTemplate template = new HSSFTemplate();
        List<LicencaSwProjetoCell> licencaSwProjetoCellList = licencaSwProjetoParcelaService.findToExport(bean.getMonthDateFiltered(), bean.getCodigoTiRecursoFiltered(), bean.getStatusFiltered());

        try {
            template.getProjectLicensesReport(licencaSwProjetoCellList);
        } catch (IOException e) {
            Messages.showError("integrarLicencaSoftware", Constants.LICENSES_MSG_ERROR_EXPORT);
        }
    }

    private void downloadUserLicensesReport() {
        HSSFTemplate template = new HSSFTemplate();
        List<LicencaSwUserCell> licencaSwUserCellList = rateioLicencaSwService.findToExport(bean.getMonthDateFiltered(), bean.getCodigoTiRecursoFiltered(), bean.getStatusFiltered());

        try {
            template.getUserLicensesReport(licencaSwUserCellList);
        } catch (IOException e) {
            Messages.showError("integrarLicencaSoftware", Constants.LICENSES_MSG_ERROR_EXPORT);
        }
    }

    public void sendMailToManager() {
        if(Boolean.FALSE.equals(isSomeLicenseSelected())) {
            Messages.showWarning("sendMailToManager", Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
            return;
        }

        HSSFTemplate template = new HSSFTemplate();
        try {
            HashMap<String, List<LicencaSwProjetoCell>> groupByManager = getProjectLicensesByManager();

            String subject = getSubjectMailToManagerNotification();
            String htmlMessage = BundleUtil.getBundle(Constants.EMAIL_LICENSE_PROEJCT_MANAGER_NOTIF_BODY_MSG);
            htmlMessage = new String(htmlMessage.getBytes(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"));
            String fileName = getAttachmentFileNameToMailManagerNotification();
            String replyTo = systemProperties.getProperty(Constants.MAIL_ACCOUNTING);
            String cc = replyTo + ", " + systemProperties.get(Constants.MAIL_COMMERCIAL_PARTNERS);

            for (Map.Entry<String, List<LicencaSwProjetoCell>> entry : groupByManager.entrySet()) {
                XSSFWorkbook workbook = template.getProjectLicensesManagerNotificationReport(entry.getValue());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    workbook.write(bos);
                } finally {
                    bos.close();
                }
                mailSenderUtil.sendMailAttachment(entry.getKey().concat(Constants.COMPANY_MAIL_DOMAIN), subject, replyTo, cc, htmlMessage, true, bos.toByteArray(), fileName);
            }
            Messages.showSucess("sendMailToManager", Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_MAIL_SENT_SUCCESS);

        } catch (Exception e) {
            logger.error("Error on sendMailToManager() - cause: {}", e);
            Messages.showError("integrarLicencaSoftware", Constants.LICENSES_MSG_ERROR_EXPORT);
        }
    }

    private String getSubjectMailToManagerNotification() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM/yyyy");

        return String.format("Gerenciamento de Licenças Temporárias %s", dateFormat.format(new Date()));
    }

    private String getAttachmentFileNameToMailManagerNotification() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        return "ProjectLicenses_".concat(dateFormat.format(new Date())).concat(".xlsx");
    }

    private HashMap<String, List<LicencaSwProjetoCell>> getProjectLicensesByManager() {
        List<Long> licencaSwProjetoCodigos = new ArrayList<>();
        for (LicencaSwProjetoRow projetoRow : bean.getLicencaSwProjetoRowList()) {
            if (Boolean.TRUE.equals(projetoRow.getIsSelected())) {
                licencaSwProjetoCodigos.add(projetoRow.getCodigoLicencaSwProjeto());
            }
        }

        List<LicencaSwProjetoCell> licencaSwProjetoCellList = licencaSwProjetoParcelaService
                .findToMailNotification(bean.getMonthDateFiltered(), bean.getCodigoTiRecursoFiltered(),
                        bean.getStatusFiltered());
        HashMap<String, List<LicencaSwProjetoCell>> groupByManager = new HashMap<String, List<LicencaSwProjetoCell>>();

        for (LicencaSwProjetoCell cell : licencaSwProjetoCellList) {
            if (licencaSwProjetoCodigos.contains(cell.getCodigoLicencaSwProjeto())) {
                if (groupByManager.containsKey(cell.getProjectManager())) {
                    groupByManager.get(cell.getProjectManager()).add(cell);
                } else {
                    List<LicencaSwProjetoCell> listAttachments = new ArrayList<>();
                    listAttachments.add(cell);
                    groupByManager.put(cell.getProjectManager(), listAttachments);
                }
            }

        }
        return groupByManager;
    }

    public void approve() {
        if (!this.isSomeLicenseSelected()) {
            Messages.showWarning("integrate",
                    Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
            return;
        }

        if (bean.isProjectLicense()) {
            List<LicencaSwProjetoRow> licencaSwProjetoRows = bean.getLicencaSwProjetoRowList();
            List<Long> licencaSwProjetoCodigos = new ArrayList<Long>();

            for (LicencaSwProjetoRow row : licencaSwProjetoRows) {
                if (row.getIsSelected()) {
                    if (null == row.getStatus() || !row.getStatus().equalsIgnoreCase(Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL)) {
                        Messages.showWarning("integrate",
                                Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_NOT_ALL_WAITING_APPROVAL);
                        return;
                    }

                    licencaSwProjetoCodigos.add(row.getCodigoLicencaSwProjeto());

                }
            }

            if (this.validateLicencaSwProjetoToApprove(true)) {
                licencaSwProjetoParcelaService.approveParcelas(licencaSwProjetoCodigos, bean.getMonthDate());
            } else {
                return;
            }

            this.filter();

            Messages.showSucess("integrate",
                    Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_APPROVE_SUCCESS);
        } else {
            List<LicencaSwUserRow> licencaSwUserRows = bean.getLicencaSwUserRowList();

            for (LicencaSwUserRow row : licencaSwUserRows) {
                if (row.getIsSelected()) {
                    if (null == row.getStatus() || !row.getStatus().equalsIgnoreCase(Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL)) {
                        Messages.showWarning("integrate",
                                Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_NOT_ALL_WAITING_APPROVAL);
                        return;
                    }
                }
            }

            if (this.validateLicencaSwProjetoToApprove(true)) {

                for (LicencaSwUserRow userRow : licencaSwUserRows) {
                    if (userRow.getIsSelected()) {
                        rateioLicencaSwService.setStatusRateioLicencaSw(userRow.getCodigoTiRecurso(), bean.getMonthDate(), Constants.LICENSE_SW_INTEGRACAO_STATUS_APROVADO, null);
                    }
                }
            }else {
                return;
            }

            this.filter();

            Messages.showSucess("integrate",
                    Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_APPROVE_SUCCESS);
        }
    }

    private Boolean validateLicencaSwProjetoToApprove(Boolean isApprovalOrigin) {
        List<LicencaSwProjetoRow> projectRows = bean.getLicencaSwProjetoRowList();
        List<LicencaSwUserRow> userRows = bean.getLicencaSwUserRowList();
        List<String> messageValidation = null;
        Boolean isValidated = true;
        try{
            List<LicencaSwDetail> licencaSwDetailList = new ArrayList<LicencaSwDetail>();
            if(!projectRows.isEmpty()) {
                for (LicencaSwProjetoRow row : projectRows) {
                    if (row.getIsSelected() && (null == row.getNotaFiscal() || row.getNotaFiscal().equals(""))) {
                        Messages.showError("integrate", Constants.INTEGRACAO_LICENCA_SOFTWARE_MSG_APPROVE_NOTAFISCAL_MISSING);
                        return Boolean.FALSE;
                    }

                    if (row.getIsSelected()) {
                        List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList = licencaSwProjetoParcelaService.findByLicencaSwProjetoAndMonth(row.getCodigoLicencaSwProjeto(), DateUtil.getDateFirstDayOfMonth(row.getDataParcela()));
                        licencaSwDetailList.addAll(licencaSwDetailService.convertLicencaSwParcela(licencaSwProjetoParcelaList));
                    }
                }

                messageValidation = licencaSwDetailService.validateCostCenterAndProjectActive(licencaSwDetailList);
                if (messageValidation != null) {
                    if (isApprovalOrigin) {
                        for (String msg : messageValidation) {
                            Messages.showError("integrateLicencaSw", Constants.LICENSE_SW_APROVACAO_ERROR_GENERIC, msg);
                        }
                    } else {
                        for (String msg : messageValidation) {
                            Messages.showError("integrateLicencaSw", Constants.LICENSE_SW_INTEGRACAO_ERROR_GENERIC, msg);
                        }
                    }
                    isValidated = false;
                }
            }else if (!userRows.isEmpty()){
                for (LicencaSwUserRow userRow : userRows) {
                    if (userRow.getIsSelected()) {
                        licencaSwDetailList.addAll(rateioLicencaSwService.findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(bean.getMonthDateFiltered(), userRow.getCodigoTiRecurso()));
                    }
                }

                messageValidation = licencaSwDetailService.validateCostCenterAndProjectActive(licencaSwDetailList);
                if (messageValidation != null) {
                    if (isApprovalOrigin) {
                        for (String msg : messageValidation) {
                            Messages.showError("integrateLicencaSw", Constants.LICENSE_SW_APROVACAO_ERROR_GENERIC, msg);
                        }
                    } else {
                        for (String msg : messageValidation) {
                            Messages.showError("integrateLicencaSw", Constants.LICENSE_SW_INTEGRACAO_ERROR_GENERIC, msg);
                        }
                    }
                    isValidated = false;
                }
            }
        }catch (Exception e){
            if (e.getMessage().contains(Constants.DEFAULT_BUNDLES_KEY_BEGIN)) {
                Messages.showError("integrateLicencaSw", e.getMessage());
            } else {
                Messages.showError("integrateLicencaSw", Constants.LICENSE_SW_APROVACAO_ERROR_GENERIC, new Object[]{e.getMessage()});
            }
            return Boolean.FALSE;
        }
        return isValidated;
    }

    public void prepareResourceTypeList() {
        List<TiRecurso> tiRecursoList = tiRecursoService.findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);
        loadTiRecursoList(tiRecursoList);

        bean.setResourceTypeSelect(new ResourceTypeSelect(tiRecursoList));
        ((Select<TiRecurso>)bean.getResourceTypeSelect()).setFilter(tiRecursoList.get(0).getNomeTiRecurso());
    }


    public void prepareInvoiceAndProjectAndLicenseCombo() {
        if (Objects.equals(bean.getTipoRecurso(), Constants.TI_RECURSO_TYPE_SOFTWARE_USER)) {
            bean.resetSearchFilters();
            return;
        }
        loadInvoices();
        loadProjects();
        loadLicenseID();
    }

    public void prepareProjectAndLicenseCombo(){
        if (Objects.equals(bean.getTipoRecurso(), Constants.TI_RECURSO_TYPE_SOFTWARE_USER)) {
            bean.resetSearchFilters();
            return;
        }
        loadProjects();
        loadLicenseID();
    }

    public void prepareLicenseCombo(){
        if (Objects.equals(bean.getTipoRecurso(), Constants.TI_RECURSO_TYPE_SOFTWARE_USER)) {
            bean.resetSearchFilters();
            return;
        }
        loadLicenseID();
    }

    public void loadLicenseID() {
        bean.setLicenseID(null);
        bean.setLicenseIDList(
                licencaSwProjetoParcelaService.findLicenseIdByDate(
                        bean.getMonthDate(),
                        bean.getInvoiceNumber(),
                        bean.getInvoiceProjectMegaSelect() == null ? null : bean.getInvoiceProjectMegaSelect().value()
                )
        );
    }

    private void loadProjects() {
        bean.getInvoiceProjectMegaSelect().select(null);
        bean.setInvoiceProjectMegaSelect(null);
        List<InvoiceProjectMegaDTO> invoiceList = licencaSwProjetoService.findProjectByDataDaParcelaAndFilter(
                bean.getMonthDate(),
                bean.getInvoiceNumber() == null ? 0 : bean.getInvoiceNumber()
        );
        bean.setInvoiceProjectMegaSelect(new InvoiceProjectMegaSelect(invoiceList));
    }

    private void loadInvoices() {
        bean.setInvoiceNumber(null);
        bean.setInvoiceNumberList(licencaSwProjetoService.findInvoiceByDataDaParcela(bean.getMonthDate()));
    }

    /**
     * Prepara a lista de empresas para o filtro.
     */
    public void prepareCompanyList() {

        List<Empresa> empresas = new ArrayList<>();
        empresas.add(empresaService.findEmpresaById(1l));
        empresas.add(empresaService.findEmpresaById(10l));

        bean.setCompanySelect(new CompanySelect(empresas));
        ((Select<Empresa>)bean.getCompanySelect()).setFilter(empresas.get(0).getNomeEmpresa());
    }

    /**
     * @return List - Collection of Resources Type.
     */
    public List<String> getTipoTiRecursos(){

        List<String> result = new ArrayList<>();
        result.add(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);
        result.add(Constants.TI_RECURSO_TYPE_SOFTWARE_USER);
        return  result;
    }


}