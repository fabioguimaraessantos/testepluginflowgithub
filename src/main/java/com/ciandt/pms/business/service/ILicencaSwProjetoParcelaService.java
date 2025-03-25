package com.ciandt.pms.business.service;

import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntry;
import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.model.vo.LicencaSwProjetoCell;
import com.ciandt.pms.model.vo.LicencaSwProjetoRow;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public interface ILicencaSwProjetoParcelaService {
    LicencaSwProjetoParcela findById(final Long entityId);

    void update(final LicencaSwProjetoParcela licencaSwProjetoParcela);

    List<LicencaSwProjetoParcela> findByFilter(final Date monthDate, final Long codTiRecurso, final String status, Long invoiceNumber, Long project, Long licenseId);

    List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto, Date month);

    Boolean create(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal);

    AccountingEntry generateAccountingByLicencaSwProjetoAndMonth(final LicencaSwProjetoRow licencaSwProjetoRow, final Date month) throws Exception;

    List<LicencaSwProjetoParcela> findByLicencaSwProjeto(final Long codigoLicencaSwProjeto);

    void setStatusLicencaSwProjetoParcela(final Long softwareLicenseCode,final Date month, final String status, String errorMessage);

    List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonthLessThan(Long codigoLicencaSwProjeto, Date dateFirstDayOfMonth);

    void updateSoftwareLicenseProjectFromERP(String code, String status, BigDecimal orderID, String errorMessage) throws ParseException;

    BigDecimal findBalanceByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto);

    BigDecimal findBalanceToAppropriateByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto, Date month);

    BigDecimal findInstallmentValue(final Date dataParcela, final Long codigoLicencaSwProjeto, final Integer numeroParcela);

    Boolean updateParcelasFromLicencaSwProjeto(LicencaSwProjeto licencaSwProjeto, List<Long> contratoPraticaCodigos, Boolean isInternal);

    void approveParcelas(List<Long> licencaSwProjetoCodigos, Date monthDate);
    
    List<LicencaSwProjetoCell> findToExport(final Date dataInicio, Long codigoTiRecurso, String status);

    List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndStatus(final Long codigoLicencaSwProjeto, final String statusLicencaSwProjetoParcela);

    BigDecimal sumValorLicencaSwProjetoParcela(List<LicencaSwProjetoParcela> licencaSwProjetoParcelas);

    List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonthGreaterThan(Long codigoLicencaSwProjeto, Date dateFirstDayOfMonth);

    Boolean updateParcelasNaoIntegradas(LicencaSwProjeto licencaSwProjeto, List<Long> clobsSelected, Boolean isInternal);

    List<LicencaSwProjetoParcela> removeIntegratedAndPendingInstallments(List<LicencaSwProjetoParcela> parcelas);

    List<LicencaSwProjetoParcela> removeOldInstallments(List<LicencaSwProjetoParcela> licencaSwParcelas);

    List<LicencaSwProjetoParcela> getFirstOpenedInstalments(List<LicencaSwProjetoParcela> licencaSwParcelas);

		List<LicencaSwProjetoParcela> getLastIntegratedInstallments(List<LicencaSwProjetoParcela> licencaSwParcelas);

    Boolean hasIncorrectStatusAfterOpenedInstallment(Long codigoLicencaSwProjeto);

    void removeInstallments(final List<LicencaSwProjetoParcela> installments);

    void remove(LicencaSwProjetoParcela licencaSwProjetoParcela);

    List<LicencaSwProjetoCell> findToMailNotification();

    List<Long> findLicenseIdByDateStart(Date searchStartDate, Date searchEndDate, Long invoiceNumber, Long project);

    List<Long> findLicenseIdByDate(Date monthDate, Long invoiceNumber, Long aLong);

    List<LicencaSwProjetoCell> findToMailNotification(Date dataInicio, Long codigoTiRecurso, String status);
}
