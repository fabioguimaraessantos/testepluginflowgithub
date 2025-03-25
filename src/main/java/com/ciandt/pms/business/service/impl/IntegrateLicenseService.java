package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IIntegrateLicenseService;
import com.ciandt.pms.business.service.IRateioLicencaSwService;
import com.ciandt.pms.business.service.ITiRecursoService;
import com.ciandt.pms.business.service.IVwPmsGrupoContaContabilService;
import com.ciandt.pms.integration.queue.LicenseProducer;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntry;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntryDebitItem;
import com.ciandt.pms.integration.vo.licenses.IntegLicense;
import com.ciandt.pms.integration.vo.licenses.License;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.model.vo.LicencaSwIntegrateDetail;
import com.ciandt.pms.persistence.dao.IIntegrateLicenseDao;
import com.ciandt.pms.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IntegrateLicenseService implements IIntegrateLicenseService {

    private static final Long PROD_CODE_AREA_PROJECT = 1l;
    private static final Long COMERCIAL_CODE_AREA_PROJECT = 2l;
    private static final Long ADM_CODE_AREA_PROJECT = 3l;
    private static final Long PANDD_CODE_AREA_PROJECT = 4l;

    @Autowired
    private IIntegrateLicenseDao dao;

    @Autowired
    private ITiRecursoService tiRecursoService;

    @Autowired
    private IRateioLicencaSwService rateioService;

    @Autowired
    private IVwPmsGrupoContaContabilService vwPmsGrupoContaContabilService;

    @Autowired
    private LicenseProducer queue;


    /**
     *
     */

    /**
     * @param integrate
     * @return LicencaSwIntegrateDetail - Integrate Detail
     */
    private LicencaSwIntegrateDetail getIntegrateDetail(IntegrateLicense integrate) {
        LicencaSwIntegrateDetail detail = new LicencaSwIntegrateDetail();
        detail.setIntegrationId(integrate.getId());
        detail.setDocNumber(integrate.getDocumentNumber());
        detail.setStatus(integrate.getStatus());
        detail.setMessage(integrate.getMessage());

        return detail;
    }

    /**
     * @param tiRecursoCode - Ti Resource Code
     * @return List - LicencaSwDetail
     */
    public List<LicencaSwDetail> findIntegrateDetailsByTiRecurso(Long tiRecursoCode, Date period, List<LicencaSwDetail> details) {

        if (tiRecursoCode != null && (details != null && !details.isEmpty())) {
            TiRecurso tiRecurso = tiRecursoService.findTiRecursoById(tiRecursoCode);
            if (Constants.CD_EMPRESA_INC.equals(tiRecurso.getEmpresa().getCodigoEmpresa())) {
                for (LicencaSwDetail detail : details) {
                    IntegrateLicense integrate = findIntegrateDetail(tiRecursoCode, period, detail.getNomeProjetoErp(), detail.getNomeGrupoCusto());
                    if (integrate != null)
                        detail.setIntegrate(getIntegrateDetail(integrate));
                }
            }
        }

        return details;
    }

    /**
     *
     */
    private IntegrateLicense findIntegrateDetail(Long tiRecursoCode, Date period, String project, String costCenter) {
        List<IntegrateLicense> integrates = dao.findIntegratesByResourceAndDateAndProject(tiRecursoCode, period, project);

        if (integrates == null || integrates.isEmpty())
            return null;

        if (integrates.size() == 1)
            return integrates.get(0);

        if (costCenter != null) {
            for (IntegrateLicense integrate : integrates) {
                if (costCenter.equals(integrate.getCostCenter()))
                    return integrate;
            }
        }

        return integrates.get(0);
    }

    /**
     *
     */
    private boolean isExistsIntegrateLicense(Long tiRecursoCode, Date period, String project, String costCenter) {

        IntegrateLicense integrate = dao.findOneIntegrateByResourceAndDateAndProjectAndCostCenter(tiRecursoCode, period, project, costCenter);
        return (integrate != null) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * @param tiRecursoCode
     * @param period
     * @param companyCode
     */
    public IntegLicense payload(Long tiRecursoCode, Date period, Long companyCode) {

        if (tiRecursoCode != null) {
            TiRecurso tiRecurso = tiRecursoService.findTiRecursoById(tiRecursoCode);

            List<LicencaSwDetail> list = rateioService.findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(period, tiRecursoCode);
            if (list != null && !list.isEmpty()) {
                return payload(list, tiRecurso, companyCode, period);
            }
        }

        return new IntegLicense();
    }

    /**
     * @param debits - List of Debits from License
     * @return Double - Total Amount of Debit
     */
    private Double getTotalValue(List<AccountingEntryDebitItem> debits) {

        try {
            if (debits != null && !debits.isEmpty()) {
                Double value = 0D;
                NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
                for (AccountingEntryDebitItem debit : debits)
                    value = value + format.parse(debit.getAmount()).doubleValue();

                return value;
            }
        } catch (Exception e) {
        }

        return 0D;
    }

    /**
     * @param tiRecurso - User Software
     * @return Payload for Integration
     */
    private IntegLicense payload(List<LicencaSwDetail> details, TiRecurso tiRecurso, Long companyCode, Date period) {

        IntegLicense payload = new IntegLicense();
        payload.setBatchName(tiRecurso.getNomeTiRecurso());
        payload.setBatchDescription(tiRecurso.getNomeTiRecurso());
        payload.setUserSourceName(Constants.ORACLE_USER_SOURCE_NAME);
        payload.setUserCategoryName(Constants.ORACLE_USER_CATEGORY_NAME);
        payload.setJournalName(Constants.ORACLE_JOURNAL_NAME);
        payload.setReference5(Constants.ORACLE_JOURNAL_NAME);
        payload.setAccountingDate(DateUtil.getStringDate(DateUtil.getDateLastDayOfMonth(period),
                new SimpleDateFormat("yyyy-MM-dd")));
        payload.setAccountingPeriodName(DateUtil.getStringDate(DateUtil.getDateLastDayOfMonth(period),
                new SimpleDateFormat("MMM-yy", new Locale("us", "US"))));
        payload.setLicenses(this.generateLicences(details, companyCode, period, tiRecurso.getNomeTiRecurso()));

        return payload;
    }

    /**
     * @param detail - License Detail from Rateio.
     * @return Debit Financial Account.
     */
    private String getContaContabil(LicencaSwDetail detail) {

        VwPmsGrupoContaContabil contaContabil = vwPmsGrupoContaContabilService.findByCodigoContratoPratica(detail.getCodigoContratoPratica());
        if (contaContabil == null) {
            contaContabil = vwPmsGrupoContaContabilService.findByCodigoCentroCusto(detail.getCodigoCentroCustoErp());

            if (contaContabil == null)
                return "";
        }

        if (PROD_CODE_AREA_PROJECT.equals(contaContabil.getCodigoTipoArea()))
            return Constants.LICENSE_FINANCIAL_ACCOUNT_PROD;

        if (COMERCIAL_CODE_AREA_PROJECT.equals(contaContabil.getCodigoTipoArea()))
            return Constants.LICENSE_FINANCIAL_ACCOUNT_COMERCIAL;

        if (ADM_CODE_AREA_PROJECT.equals(contaContabil.getCodigoTipoArea()))
            return Constants.LICENSE_FINANCIAL_ACCOUNT_ADM;

        if (PANDD_CODE_AREA_PROJECT.equals(contaContabil.getCodigoTipoArea()))
            return Constants.LICENSE_FINANCIAL_ACCOUNT_PANDD;

        return StringUtils.EMPTY;
    }

    /**
     * @param period           - License Period
     * @param tiRecurso        - License User Software
     * @param codigoProjetoERP - ERP Project Code of License
     * @return Customer Code.
     */
    private String getCustomerCode(Date period, Long tiRecurso, Long codigoProjetoERP) {

        List<RateioLicencaSw> softwares = rateioService.findByRecursoTIAndMonth(period, tiRecurso);
        if (softwares != null && !softwares.isEmpty()) {
            for (RateioLicencaSw sw : softwares) {
                if (sw.getCodigoProjetoErp().equals(codigoProjetoERP) && sw.getCodigoCliente() != null)
                    return sw.getCodigoCliente().toString();
            }
        }

        return StringUtils.EMPTY;
    }

    /**
     * @param payload - Message of license to be sent.
     */
    public void integrate(IntegLicense payload) {

        if (null == payload)
            return;

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        publish(gson.toJson(payload));
    }

    /**
     * @param message - Message to be sent.
     */
    private void publish(String message) {
        try {
            queue.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param details     -- data from licenses integration
     * @param companyCode --id from company to be integrated
     */
    private List<License> generateLicences(List<LicencaSwDetail> details, Long companyCode, Date period, String nomeTiRecurso) {
        {
            List<License> licenses = new ArrayList<>();
            for (LicencaSwDetail detail : details) {
                License licenseDebit = new License();
                licenseDebit.setCompanyCode(companyCode.toString());
                licenseDebit.setFinancialAccountSegment(getContaContabil(detail));
                licenseDebit.setEnteredDrAmount(detail.getValor().doubleValue());
                licenseDebit.setProjectSegment((null != detail.getCodigoProjetoErp() ?
                        detail.getCodigoProjetoErp().toString() : detail.getCodigoCentroCustoErp().toString()));
                licenseDebit.setCurrencyCode(Constants.COMPANY_INC_CURRENCY_CODE);
                licenseDebit.setReference10(this.getReference10(detail, nomeTiRecurso));
                licenseDebit.setAccountingDate(DateUtil.getStringDate(DateUtil.getDateLastDayOfMonth(period), new SimpleDateFormat("yyyy-MM-dd")));
                licenses.add(licenseDebit);

                License licenseCredit = new License();
                licenseCredit.setCompanyCode(companyCode.toString());
                licenseCredit.setFinancialAccountSegment(Constants.LICENSE_FINANCIAL_ORACLE_CREDIT_ACCOUNT);
                licenseCredit.setEnteredCrAmount(detail.getValor().doubleValue());
                licenseCredit.setProjectSegment((null != detail.getCodigoProjetoErp() ?
                        detail.getCodigoProjetoErp().toString() : detail.getCodigoCentroCustoErp().toString()));
                licenseCredit.setCurrencyCode(Constants.COMPANY_INC_CURRENCY_CODE);
                licenseCredit.setReference10(this.getReference10(detail, nomeTiRecurso));
                licenseCredit.setAccountingDate(DateUtil.getStringDate(DateUtil.getDateLastDayOfMonth(period), new SimpleDateFormat("yyyy-MM-dd")));
                licenses.add(licenseCredit);

            }

            return licenses;
        }
    }

    private String getReference10(LicencaSwDetail licenseDetail, String nomeTiRecurso) {
        return Constants.ORACLE_JOURNAL_NAME + " - " +
                (null != licenseDetail.getNomeContratoPratica() ? licenseDetail.getNomeContratoPratica() :
                        licenseDetail.getNomeGrupoCusto()) + " - " + nomeTiRecurso;
    }
}