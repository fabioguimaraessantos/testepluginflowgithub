package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.integration.vo.accountingEntry.*;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.model.vo.LicencaSwUserCell;
import com.ciandt.pms.model.vo.LicencaSwUserRow;
import com.ciandt.pms.model.vo.LicencaSwUserVO;
import com.ciandt.pms.persistence.dao.IRateioLicencaSwDao;
import com.ciandt.pms.util.DateUtil;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RateioLicencaSwService implements IRateioLicencaSwService {

    @Autowired
    private IRateioLicencaSwDao rateioLicencaSwDao;

    @Autowired
    private IGrupoCustoService grupoCustoService;

    @Autowired
    private ILicencaSwDetailService licencaSwDetailService;

    @Autowired
    private Properties systemProperties;

    public RateioLicencaSw findById(final Long entityId) {
        return rateioLicencaSwDao.findById(entityId);
    }


    public List<LicencaSwUserVO> findByFilter(final Date dtCompetencia, final Long codTiRecurso, final String status) {
        String statusReal = null;
        if (status.equals("Waiting Approval")) {
            statusReal = Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL;
        } else if (status.equals("Approved")) {
            statusReal = Constants.LICENCA_SW_PROJETO_STATUS_APPROVED;
        } else if (status.equals("Pending")) {
            statusReal = Constants.LICENCA_SW_PROJETO_STATUS_PENDENTE;
        } else if (status.equals("Integrated")) {
            statusReal = Constants.LICENCA_SW_PROJETO_STATUS_INTEGRADO;
        } else if (status.equals("Error")) {
            statusReal = Constants.LICENCA_SW_PROJETO_STATUS_ERRO;
        }

        return rateioLicencaSwDao.findByFilter(dtCompetencia, codTiRecurso, status.equals("All") ? null : statusReal);
    }

    public List<LicencaSwDetail> findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(final Date dtCompetencia, Long codTiRecurso){
        return rateioLicencaSwDao.findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(dtCompetencia, codTiRecurso);
    }

    public List<RateioLicencaSw> findByRecursoTIAndMonth(final Date dtCompetencia, Long codTiRecurso){
        return rateioLicencaSwDao.findByRecursoTIAndMonth(dtCompetencia, codTiRecurso);
    }

    public AccountingEntry generateAccountingByTiRecursoAndMonth(final LicencaSwUserRow licencaSwUserRow, final Date month) throws Exception{
        try {
            List<LicencaSwDetail> licencaSwDetailList = this.findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(month, licencaSwUserRow.getCodigoTiRecurso());

            if (licencaSwDetailList.size() > 0) {

                Integer itemCount = 0;
                AccountingEntry accountingEntry = new AccountingEntry();

                List<AccountingEntryCreditItem> creditItems = new ArrayList<AccountingEntryCreditItem>();
                List<AccountingEntryDebitItem> debitItems = new ArrayList<AccountingEntryDebitItem>();

                GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(licencaSwDetailList.get(0).getCodigoGrupoCusto());

                accountingEntry.setFileName("U_" + new SimpleDateFormat("yyyyMMdd").format(DateUtil.getDateFirstDayOfMonth(month)) + licencaSwUserRow.getCodigoTiRecurso());
                accountingEntry.setCompanyCode(grupoCusto.getEmpresa().getCodigoEmpresaERP().toString());
                accountingEntry.setDate(DateUtil.getLastWorkingDayOfMonth(month));
                accountingEntry.setActionCode(systemProperties.getProperty(Constants.LICENSE_SW_INTEGRATION_ACTION_CODE));

                Map<String, List<LicencaSwDetail>> mapByAccounting = licencaSwDetailService.createMapFromAccounting(licencaSwDetailList);

                for (Map.Entry<String, List<LicencaSwDetail>> entry : mapByAccounting.entrySet()) {
                    List<AccountingEntryItemCostCenter> costCenters = new ArrayList<AccountingEntryItemCostCenter>();
                    List<AccountingEntryItemCostCenter> costCentersCredit = new ArrayList<AccountingEntryItemCostCenter>();

                    itemCount++;
                    AccountingEntryDebitItem debitItem = new AccountingEntryDebitItem();
                    debitItem.setCode(itemCount.toString());
                    debitItem.setBranchCompanyCode("3");
                    debitItem.setConversionDate(DateUtil.getLastWorkingDayOfMonth(month));
                    debitItem.setBudgetDate(DateUtil.getLastWorkingDayOfMonth(month));
                    debitItem.setComplement("Apropriação de Despesa com licença de software - " + licencaSwUserRow.getNomeLicenca() + " - " + DateUtil.getMonthString(month) + "/" + DateUtil.getYearString(month));
                    debitItem.setDebitAccountCode(entry.getKey());
                    debitItem.setAmount(licencaSwDetailService.getValorTotal(entry.getValue()));

                    itemCount++;
                    AccountingEntryCreditItem creditItem = new AccountingEntryCreditItem();
                    creditItem.setCode(itemCount.toString());
                    creditItem.setBranchCompanyCode("3");
                    creditItem.setBudgetDate(DateUtil.getLastWorkingDayOfMonth(month));
                    creditItem.setConversionDate(DateUtil.getLastWorkingDayOfMonth(month));
                    creditItem.setComplement("Apropriação de Despesa com licença de software - " + licencaSwUserRow.getNomeLicenca() + " - " + DateUtil.getMonthString(month) + "/" + DateUtil.getYearString(month));
                    creditItem.setCreditAccountCode(entry.getValue().get(0).getContaContabilCredito());
                    creditItem.setAmount(licencaSwDetailService.getValorTotal(entry.getValue()));

                    //item de debito
                    for (LicencaSwDetail licencaSwDetail : entry.getValue()) {
                        AccountingEntryItemCostCenter costCenter = new AccountingEntryItemCostCenter();
                        costCenter.setAmount(licencaSwDetail.getValor().toString().replace('.', ','));
                        costCenter.setCostCenterReduceCode(licencaSwDetail.getCodigoCentroCustoErp().toString());
                        costCenter.setType(AccountingEntryItemCostCenter.Type.DEBT.getAcronym().toString());

                        AccountingEntryItemProject project = new AccountingEntryItemProject();
                        project.setAmount(licencaSwDetail.getValor().toString().replace('.', ','));
                        project.setMegaProjectCode(licencaSwDetail.getCodigoProjetoErp().toString());

                        //join
                        costCenter.setProject(project);
                        costCenters.add(costCenter);


                        //item de credito

                        AccountingEntryItemCostCenter costCenterCredit = new AccountingEntryItemCostCenter();
                        costCenterCredit.setAmount(licencaSwDetail.getValor().toString().replace('.', ','));
                        costCenterCredit.setCostCenterReduceCode(licencaSwDetail.getCodigoCentroCustoErp().toString());
                        costCenterCredit.setType(AccountingEntryItemCostCenter.Type.CREDIT.getAcronym().toString());

                        AccountingEntryItemProject projectCredit = new AccountingEntryItemProject();
                        projectCredit.setAmount(licencaSwDetail.getValor().toString().replace('.', ','));
                        projectCredit.setMegaProjectCode(licencaSwDetail.getCodigoProjetoErp().toString());

                        //join
                        costCenterCredit.setProject(projectCredit);
                        costCentersCredit.add(costCenterCredit);
                    }
                    debitItem.setCostCenters(costCenters);
                    creditItem.setCostCenters(costCentersCredit);

                    debitItems.add(debitItem);
                    creditItems.add(creditItem);


                }
                accountingEntry.setDebitItems(debitItems);
                accountingEntry.setCreditItems(creditItems);

                return accountingEntry;
            }

            return null;
        }catch (Exception e){
            if(e.getMessage().contains("_nls.")){
                throw new Exception(e.getMessage());
            }else {
                throw new Exception(Constants.LICENSE_SW_APROVACAO_ERROR_GENERIC);
            }
        }
    }



    @Transactional
    public void setStatusRateioLicencaSw(final Long softwareLicenseCode,final Date month, final String status, String errorMessage) {
        List<RateioLicencaSw> rateioLicencaSwList = this.findByRecursoTIAndMonth(month, softwareLicenseCode);

        for (RateioLicencaSw rateioLicencaSw : rateioLicencaSwList){
            if (rateioLicencaSw.getStatus() != null) {
                if (rateioLicencaSw.getStatus().equals(Constants.LICENSE_SW_INTEGRACAO_STATUS_INTEGRADO)){
                    return;
                }
            }
            rateioLicencaSw.setStatus(status);
            rateioLicencaSw.setTextoError(errorMessage);
            rateioLicencaSwDao.update(rateioLicencaSw);
        }
    }

    @Transactional
    public void updateSoftwareLicenseUserFromERP(String code, String status, BigDecimal orderID, String errorMessage) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date softwareLicenseDate = sdf.parse(code.substring(6,8) + "/" + code.substring(4,6) + "/" + code.substring(0,4));
        Long softwareLicenseCode = Long.parseLong(code.substring(8,code.length()));

        if (status.equals("A")) {
            this.setStatusRateioLicencaSw(softwareLicenseCode, softwareLicenseDate, Constants.LICENSE_SW_INTEGRACAO_STATUS_INTEGRADO, errorMessage);
        }
        else if (status.equals("E")){
            this.setStatusRateioLicencaSw(softwareLicenseCode, softwareLicenseDate, Constants.LICENSE_SW_INTEGRACAO_STATUS_ERRO, errorMessage);
        }
    }

    public List<LicencaSwUserCell> findToExport(Date monthDate, Long codTiRecurso, String status) {
        return rateioLicencaSwDao.findByRecursoTiAndMonthAndStatus(codTiRecurso, monthDate, LicencaSwProjetoParcela.translateStatus(status));
    }

    @Override
    public boolean isMonthApprovedByTiRecurso(Date startDate, Long codigoTiRecurso) {

        List<RateioLicencaSw> rateioLicencaSws = this.findByRecursoTIAndMonth(startDate, codigoTiRecurso);
        if(rateioLicencaSws == null || rateioLicencaSws.isEmpty())
            return false;

        for (RateioLicencaSw rateio : rateioLicencaSws) {
            if (rateio.getStatus().equalsIgnoreCase(Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL))
                return false;
        }

        return true;
    }
}
