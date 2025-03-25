package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.ILicencaSwDetailService;
import com.ciandt.pms.business.service.ILicencaSwProjetoParcelaService;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntry;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntryCreditItem;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntryDebitItem;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntryItemCostCenter;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntryItemProject;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.model.vo.LicencaSwProjetoCell;
import com.ciandt.pms.model.vo.LicencaSwProjetoRow;
import com.ciandt.pms.persistence.dao.ILicencaSwProjetoParcelaDao;
import com.ciandt.pms.util.DateUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class LicencaSwProjetoParcelaService implements ILicencaSwProjetoParcelaService {

    @Autowired
    private ILicencaSwProjetoParcelaDao dao;

    @Autowired
    private ILicencaSwDetailService licencaSwDetailService;

    @Autowired
    private IContratoPraticaService contratoPraticaService;

    @Autowired
    private IGrupoCustoService grupoCustoService;

    @Autowired
    private Properties systemProperties;

    public LicencaSwProjetoParcela findById(final Long entityId) {
        return dao.findById(entityId);
    }

    public void update(final LicencaSwProjetoParcela licencaSwProjetoParcela) {
        dao.update(licencaSwProjetoParcela);
    }

    public List<LicencaSwProjetoParcela> findByFilter(final Date monthDate, final Long codTiRecurso, final String status, Long invoiceNumber, Long project, Long licenseId) {
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
        return dao.findByFilter(monthDate, codTiRecurso, statusReal, invoiceNumber, project, licenseId);
    }

    public List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto, Date month) {
        return dao.findByLicencaSwProjetoAndMonth(codigoLicencaSwProjeto, month);
    }

    public BigDecimal findBalanceByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto) {
        return dao.findBalanceByLicencaSwProjetoAndMonth(codigoLicencaSwProjeto);
    }

    public BigDecimal findBalanceToAppropriateByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto, Date month) {
        return dao.findBalanceToAppropriateByLicencaSwProjetoAndMonth(codigoLicencaSwProjeto, month);
    }

    public Boolean create(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal) {
        Integer numParcelas = licencaSwProjeto.getQuantidadeParcela() * contratoPraticaCodigos.size();
        BigDecimal valorUnitarioParcela = this.getValorParcela(licencaSwProjeto.getValorTotal(), numParcelas);

        LicencaSwProjetoParcela licencaSwProjetoParcela = null;
        List<LicencaSwProjetoParcela> parcelas = new ArrayList<>();

        if (Boolean.TRUE.equals(isInternal)) {
            for (Long codigoContratoPratica : contratoPraticaCodigos) {
                GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(codigoContratoPratica);

                for (int i = 0; i < licencaSwProjeto.getQuantidadeParcela(); i++) {
                    licencaSwProjetoParcela = new LicencaSwProjetoParcela(licencaSwProjeto, grupoCusto, DateUtil.addMonths(licencaSwProjeto.getDataInicio(), i), i + 1, valorUnitarioParcela, Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL);
                    if (i >= 12) {
                        licencaSwProjetoParcela.setTipoParcela(LicencaSwProjetoParcela.TipoParcela.LONGO.getFieldName());
                    }

                    parcelas.add(licencaSwProjetoParcela);
                }
            }
        } else {
            for (Long codigoContratoPratica : contratoPraticaCodigos) {
                ContratoPratica contratoPratica = contratoPraticaService.findContratoPraticaById(codigoContratoPratica);

                for (int i = 0; i < licencaSwProjeto.getQuantidadeParcela(); i++) {
                    licencaSwProjetoParcela = new LicencaSwProjetoParcela(licencaSwProjeto, contratoPratica, DateUtil.addMonths(licencaSwProjeto.getDataInicio(), i), i + 1, valorUnitarioParcela, Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL);
                    if (i >= 12) {
                        licencaSwProjetoParcela.setTipoParcela(LicencaSwProjetoParcela.TipoParcela.LONGO.getFieldName());
                    }

                    parcelas.add(licencaSwProjetoParcela);
                }
            }
        }

        parcelas = this.distributeResidualInstallments(licencaSwProjeto.getValorTotal(), parcelas);

        for (LicencaSwProjetoParcela parcela : parcelas) {
            dao.create(parcela);
        }

        return true;
    }

    private List<LicencaSwProjetoParcela> distributeResidualInstallments(BigDecimal valorTotal, List<LicencaSwProjetoParcela> licencaSwProjetoParcelas) {

        if (valorTotal.scale() > 2) {
            valorTotal = valorTotal.setScale(2, RoundingMode.HALF_EVEN);
        }

        BigDecimal resto = valorTotal.subtract(this.sumValorLicencaSwProjetoParcela(licencaSwProjetoParcelas));
        if (resto.equals(BigDecimal.ZERO)) {
            return licencaSwProjetoParcelas;
        }

        BigDecimal step = BigDecimal.valueOf(0.01);
        int iterations = resto.divide(step).intValueExact();

        for (int i = 0; i < iterations; i++) {
            licencaSwProjetoParcelas.get(i).setValorParcela(licencaSwProjetoParcelas.get(i).getValorParcela().add(step));
        }

        return licencaSwProjetoParcelas;
    }

    public BigDecimal getValorParcela(BigDecimal valorTotal, Integer qtdParcelas) {
        return new BigDecimal(valorTotal.doubleValue() / qtdParcelas).setScale(2, RoundingMode.FLOOR);
    }

    public AccountingEntry generateAccountingByLicencaSwProjetoAndMonth(final LicencaSwProjetoRow licencaSwProjetoRow, final Date month) throws Exception {
        try {
            AccountingEntry accountingEntry = new AccountingEntry();
            List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList = this.findByLicencaSwProjetoAndMonth(licencaSwProjetoRow.getCodigoLicencaSwProjeto(), DateUtil.getDateFirstDayOfMonth(licencaSwProjetoRow.getDataParcela()));
            List<LicencaSwDetail> licencaSwDetailList = licencaSwDetailService.convertLicencaSwParcela(licencaSwProjetoParcelaList);

            if (licencaSwDetailList.size() > 0) {

                Integer itemCount = 0;

                List<AccountingEntryCreditItem> creditItems = new ArrayList<AccountingEntryCreditItem>();
                List<AccountingEntryDebitItem> debitItems = new ArrayList<AccountingEntryDebitItem>();

                GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(licencaSwDetailList.get(0).getCodigoGrupoCusto());

                accountingEntry.setFileName("P_" + new SimpleDateFormat("yyyyMMdd").format(DateUtil.getDateFirstDayOfMonth(month)) + licencaSwProjetoRow.getCodigoLicencaSwProjeto());
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
                    debitItem.setComplement("Apropriação de Despesa com licença de software - " + licencaSwProjetoRow.getNomeLicenca() + " - " + DateUtil.getMonthString(month) + "/" + DateUtil.getYearString(month));
                    debitItem.setDebitAccountCode(entry.getKey());
                    debitItem.setAmount(licencaSwDetailService.getValorTotal(entry.getValue()));

                    itemCount++;
                    AccountingEntryCreditItem creditItem = new AccountingEntryCreditItem();
                    creditItem.setCode(itemCount.toString());
                    creditItem.setBranchCompanyCode("3");
                    creditItem.setBudgetDate(DateUtil.getLastWorkingDayOfMonth(month));
                    creditItem.setConversionDate(DateUtil.getLastWorkingDayOfMonth(month));
                    creditItem.setComplement("Apropriação de Despesa com licença de software - " + licencaSwProjetoRow.getNomeLicenca() + " - " + DateUtil.getMonthString(month) + "/" + DateUtil.getYearString(month));
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

            return accountingEntry;

        } catch (Exception e) {
            if (e.getMessage().contains("_nls.")) {
                throw new Exception(e.getMessage());
            } else {
                throw new Exception(Constants.LICENSE_SW_APROVACAO_ERROR_GENERIC);
            }
        }
    }

    @Transactional
    public void setStatusLicencaSwProjetoParcela(final Long softwareLicenseCode, final Date month, final String status, String errorMessage) {
        List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList = this.findByLicencaSwProjetoAndMonth(softwareLicenseCode, month);

        for (LicencaSwProjetoParcela licencaSwProjetoParcela : licencaSwProjetoParcelaList) {
            if (licencaSwProjetoParcela.getStatus() != null) {
                if (licencaSwProjetoParcela.getStatus().equals(Constants.LICENSE_SW_INTEGRACAO_STATUS_INTEGRADO)) {
                    return;
                }
            }
            licencaSwProjetoParcela.setStatus(status);
            licencaSwProjetoParcela.setTextoError(errorMessage);
            dao.update(licencaSwProjetoParcela);
        }
    }

    @Override
    public List<LicencaSwProjetoParcela> findByLicencaSwProjeto(Long codigoLicencaSwProjeto) {
        return dao.findByLicencaSwProjeto(codigoLicencaSwProjeto);
    }

    @Override
    public List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndStatus(final Long codigoLicencaSwProjeto, final String statusLicencaSwProjetoParcela) {
        return dao.findByLicencaSwProjetoAndStatus(codigoLicencaSwProjeto, statusLicencaSwProjetoParcela);
    }

    @Override
    public BigDecimal sumValorLicencaSwProjetoParcela(List<LicencaSwProjetoParcela> licencaSwProjetoParcelas) {
        BigDecimal total = BigDecimal.ZERO;
        for (LicencaSwProjetoParcela licencaSwProjetoParcela : licencaSwProjetoParcelas) {
            total = total.add(licencaSwProjetoParcela.getValorParcela());
        }

        return total;
    }

    @Override
    public List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonthGreaterThan(Long codigoLicencaSwProjeto, Date beginDate) {
        return dao.findByLicencaSwProjetoAndMonthGreaterThan(codigoLicencaSwProjeto, beginDate);
    }

    @Override
    public List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonthLessThan(Long codigoLicencaSwProjeto, Date dateFirstDayOfMonth) {
        return dao.findByLicencaSwProjetoAndMonthLessThan(codigoLicencaSwProjeto, dateFirstDayOfMonth);
    }

    @Transactional
    public void updateSoftwareLicenseProjectFromERP(String code, String status, BigDecimal orderID, String errorMessage) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date softwareLicenseDate = sdf.parse(code.substring(6, 8) + "/" + code.substring(4, 6) + "/" + code.substring(0, 4));
        Long softwareLicenseCode = Long.parseLong(code.substring(8, code.length()));

        if (status.equals("A")) {
            this.setStatusLicencaSwProjetoParcela(softwareLicenseCode, softwareLicenseDate, Constants.LICENSE_SW_INTEGRACAO_STATUS_INTEGRADO, errorMessage);
        } else if (status.equals("E")) {
            this.setStatusLicencaSwProjetoParcela(softwareLicenseCode, softwareLicenseDate, Constants.LICENSE_SW_INTEGRACAO_STATUS_ERRO, errorMessage);
        }
    }

    public BigDecimal findInstallmentValue(final Date dataParcela, final Long codigoLicencaSwProjeto, Integer numeroParcela) {
        return dao.findInstallmentValue(dataParcela, codigoLicencaSwProjeto, numeroParcela);
    }

    @Override
    @Transactional
    public Boolean updateParcelasFromLicencaSwProjeto(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal) {
        List<LicencaSwProjetoParcela> parcelasOld = dao.findByLicencaSwProjeto(licencaSwProjeto.getCodigoLicencaSwProjeto());
        for (LicencaSwProjetoParcela parcelaOld : parcelasOld) {
            dao.remove(parcelaOld);
        }

        return this.create(licencaSwProjeto, contratoPraticaCodigos, isInternal);
    }

    public List<LicencaSwProjetoCell> findToExport(final Date dataInicio, Long codigoTiRecurso, String status) {
        return dao.findByStartDateAndCodigoLicencaProjeto(dataInicio, codigoTiRecurso, LicencaSwProjetoParcela.translateStatus(status));
    }

    public List<LicencaSwProjetoCell> findToMailNotification(Date dataInicio, Long codigoTiRecurso, String status) {
        return dao.findByStartDateAndCodigoLicencaProjeto(dataInicio, codigoTiRecurso, LicencaSwProjetoParcela.translateStatus(status));
    }

    public List<LicencaSwProjetoCell> findToMailNotification() {
        return dao.findAllInstallmentsFromCurrentMonth();
    }

    @Override
    public List<Long> findLicenseIdByDateStart(Date searchStartDate, Date searchEndDate, Long invoiceNumber, Long project) {
        return dao.findLicenseIdByDateStart(searchStartDate, searchEndDate, invoiceNumber, project);
    }

    @Override
    public List<Long> findLicenseIdByDate(Date monthDate, Long invoiceNumber, Long project) {
        return dao.findLicenseIdByDate(monthDate, invoiceNumber, project);
    }

    @Transactional
    public void approveParcelas(List<Long> licencaSwProjetoCodigos, Date monthDate) {
        List<LicencaSwProjetoParcela> licencaSwProjetoParcelas = dao.findByCodigoLicencaSwProjetoIn(licencaSwProjetoCodigos, monthDate);

        for (LicencaSwProjetoParcela parcela : licencaSwProjetoParcelas) {
            parcela.setStatus(Constants.LICENCA_SW_PROJETO_STATUS_APPROVED);
            dao.update(parcela);
        }
    }

    @Override
    @Transactional
    public Boolean updateParcelasNaoIntegradas(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal) {

        List<LicencaSwProjetoParcela> parcelas = this.findByLicencaSwProjeto(licencaSwProjeto.getCodigoLicencaSwProjeto());
        List<LicencaSwProjetoParcela> parcelasAbertas = this.removeIntegratedAndPendingInstallments(parcelas);
        List<LicencaSwProjetoParcela> novasParcelas = new ArrayList<LicencaSwProjetoParcela>();

        Integer qtdParcelasAbertas = this.getQuantidadeParcelasAbertas(parcelasAbertas);
        LicencaSwProjetoParcela primeiraParcelaRestante = this.getPrimeiraParcelaRestante(parcelasAbertas);
        BigDecimal valorRestante = this.sumValorLicencaSwProjetoParcela(parcelasAbertas);
        BigDecimal valorUnitarioParcela = this.getValorParcela(valorRestante, qtdParcelasAbertas * contratoPraticaCodigos.size());

        if (isInternal) {
            for (Long codigo : contratoPraticaCodigos) {
                GrupoCusto grupoCusto = grupoCustoService.findGrupoCustoById(codigo);

                for (int i = 0; i < qtdParcelasAbertas; i++) {
                    LicencaSwProjetoParcela licencaSwProjetoParcela = new LicencaSwProjetoParcela(
                            licencaSwProjeto, grupoCusto, DateUtil.addMonths(primeiraParcelaRestante.getDataParcela(), i),
                            primeiraParcelaRestante.getNumeroParcela() + i, valorUnitarioParcela,
                            Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL);
                    if (primeiraParcelaRestante.getNumeroParcela() + i > 12) {
                        licencaSwProjetoParcela.setTipoParcela(LicencaSwProjetoParcela.TipoParcela.LONGO.getFieldName());
                    }
                    novasParcelas.add(licencaSwProjetoParcela);
                }
            }
        } else {
            for (Long codigo : contratoPraticaCodigos) {
                ContratoPratica contratoPratica = contratoPraticaService.findContratoPraticaById(codigo);

                for (int i = 0; i < qtdParcelasAbertas; i++) {
                    LicencaSwProjetoParcela licencaSwProjetoParcela = new LicencaSwProjetoParcela(
                            licencaSwProjeto, contratoPratica, DateUtil.addMonths(primeiraParcelaRestante.getDataParcela(), i),
                            primeiraParcelaRestante.getNumeroParcela() + i, valorUnitarioParcela,
                            Constants.LICENCA_SW_PROJETO_STATUS_WAITING_APPROVAL);
                    if (primeiraParcelaRestante.getNumeroParcela() + i > 12) {
                        licencaSwProjetoParcela.setTipoParcela(LicencaSwProjetoParcela.TipoParcela.LONGO.getFieldName());
                    }

                    novasParcelas.add(licencaSwProjetoParcela);
                }
            }
        }

        novasParcelas = this.distributeResidualInstallments(valorRestante, novasParcelas);

        for (LicencaSwProjetoParcela parcela : parcelasAbertas) {
            dao.remove(parcela);
        }

        for (LicencaSwProjetoParcela parcela : novasParcelas) {
            dao.create(parcela);
        }

        return Boolean.TRUE;
    }

    @Override
    public List<LicencaSwProjetoParcela> getFirstOpenedInstalments(List<LicencaSwProjetoParcela> licencaSwParcelas) {
        List<LicencaSwProjetoParcela> primeirasParcelas = new ArrayList<LicencaSwProjetoParcela>();
        Date firstDate = null;

        for (LicencaSwProjetoParcela parcela : licencaSwParcelas) {
            if (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                    || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                if (null == firstDate || parcela.getDataParcela().before(firstDate)) {
                    firstDate = parcela.getDataParcela();
                }
            }
        }

        if (null != firstDate) {
            for (LicencaSwProjetoParcela parcela : licencaSwParcelas) {
                if (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                        || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                    if (parcela.getDataParcela().equals(firstDate)) {
                        primeirasParcelas.add(parcela);
                    }
                }
            }
        }

        return primeirasParcelas;
    }

    @Override
    public List<LicencaSwProjetoParcela> getLastIntegratedInstallments(List<LicencaSwProjetoParcela> licencaSwParcelas) {
      List<LicencaSwProjetoParcela> ultimasParcelas = new ArrayList<LicencaSwProjetoParcela>();
      Date lastDate = null;

      for (LicencaSwProjetoParcela parcela : licencaSwParcelas) {
        if (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName())) {
          if (null == lastDate || parcela.getDataParcela().after(lastDate)) {
            lastDate = parcela.getDataParcela();
          }
        }
      }

      if (null != lastDate) {
        for (LicencaSwProjetoParcela parcela : licencaSwParcelas) {
          if (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName())) {
            if (parcela.getDataParcela().equals(lastDate)) {
              ultimasParcelas.add(parcela);
            }
          }
        }
      }
        return ultimasParcelas;
    }

    @Override
    public Boolean hasIncorrectStatusAfterOpenedInstallment(Long codigoLicencaSwProjeto) {
        List<LicencaSwProjetoParcela> parcelas = this.findByLicencaSwProjeto(codigoLicencaSwProjeto);
        LicencaSwProjetoParcela primeiraParcelaAberta = null;

        Collections.sort(parcelas, new Comparator<LicencaSwProjetoParcela>() {
            @Override
            public int compare(LicencaSwProjetoParcela obj1, LicencaSwProjetoParcela obj2) {
                return obj1.getDataParcela().compareTo(obj2.getDataParcela());
            }
        });

        for (LicencaSwProjetoParcela parcela : parcelas) {
            if (null == primeiraParcelaAberta
                    && (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                        || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName()))) {
                primeiraParcelaAberta = parcela;
            }

             if (null != primeiraParcelaAberta && parcela.getDataParcela().after(primeiraParcelaAberta.getDataParcela())
                     && (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName())
                        || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.PENDENTE.getFieldName())
                        || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.ERRO.getFieldName()))) {
                 return Boolean.TRUE;
             }
        }

        return Boolean.FALSE;
    }

    private LicencaSwProjetoParcela getPrimeiraParcelaRestante(List<LicencaSwProjetoParcela> parcelasAbertas) {
        LicencaSwProjetoParcela primeiraParcela = null;
        for (LicencaSwProjetoParcela parcela : parcelasAbertas) {
            if (null == primeiraParcela || primeiraParcela.getDataParcela().after(parcela.getDataParcela())) {
                primeiraParcela = parcela;
            }
        }
        return primeiraParcela;
    }

    private Integer getQuantidadeParcelasAbertas(List<LicencaSwProjetoParcela> parcelasAbertas) {
        Set<Date> datasRestantes = new HashSet<Date>();

        for (LicencaSwProjetoParcela parcela : parcelasAbertas) {
            datasRestantes.add(parcela.getDataParcela());
        }
        return datasRestantes.size();
    }

    @Override
    public List<LicencaSwProjetoParcela> removeIntegratedAndPendingInstallments(List<LicencaSwProjetoParcela> parcelas) {
        List<LicencaSwProjetoParcela> parcelasAbertas = new ArrayList<LicencaSwProjetoParcela>();

        for (LicencaSwProjetoParcela parcela : parcelas) {
            if (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                    || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                parcelasAbertas.add(parcela);
            }
        }
        return parcelasAbertas;
    }

    @Override
    public List<LicencaSwProjetoParcela> removeOldInstallments(List<LicencaSwProjetoParcela> licencaSwParcelas) {
        List<LicencaSwProjetoParcela> parcelasAbertas = new ArrayList<LicencaSwProjetoParcela>();

        for (LicencaSwProjetoParcela parcela : licencaSwParcelas) {
            if (!parcela.getDataParcela().before(DateUtil.getDateFirstDayOfMonth(new Date()))) {
                parcelasAbertas.add(parcela);
            }
        }
        return parcelasAbertas;
    }

    @Override
    @Transactional
    public void removeInstallments(final List<LicencaSwProjetoParcela> installments) {
        for (LicencaSwProjetoParcela installment : installments) {
            dao.remove(installment);
        }
    }

    @Transactional
    public void remove(LicencaSwProjetoParcela licencaSwProjetoParcela) {
        dao.remove(licencaSwProjetoParcela);
    }

}
