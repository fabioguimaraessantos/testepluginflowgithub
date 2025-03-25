package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.model.vo.LicencaSwProjetoCell;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ILicencaSwProjetoParcelaDao extends IAbstractDao<Long, LicencaSwProjetoParcela> {
    List<LicencaSwProjetoParcela> findByFilter(final Date monthDate, final Long codTiRecurso, final String status, Long invoiceNumber, Long project, Long licenseId);

    List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonth(final Long codigoLicencaSwProjeto, final Date month);

    List<LicencaSwProjetoParcela> findByLicencaSwProjeto(final Long codigoLicencaSwProjeto);

    BigDecimal findBalanceByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto);

    BigDecimal findBalanceToAppropriateByLicencaSwProjetoAndMonth(Long codigoLicencaSwProjeto, Date month);

    BigDecimal findInstallmentValue(final Date dataParcela, final Long codigoLicencaSwProjeto, final Integer numeroParcela);

    List<LicencaSwProjetoParcela> findByCodigoLicencaSwProjetoIn(List<Long> licencaSwProjetoCodigos, Date monthDate);
    
    List<LicencaSwProjetoCell> findByStartDateAndCodigoLicencaProjeto(final Date data, Long codigoTiRecurso, String status);

    List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndStatus(final Long codigoLicencaSwProjeto, final String statusLicencaSwProjetoParcela);

    List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonthGreaterThan(Long codigoLicencaSwProjeto, Date beginDate);

    List<LicencaSwProjetoParcela> findByLicencaSwProjetoAndMonthLessThan(Long codigoLicencaSwProjeto, Date beginDate);

    List<LicencaSwProjetoCell> findAllInstallmentsFromCurrentMonth();

    List<Long> findLicenseIdByDateStart(Date searchStartDate, Date searchEndDate, Long invoiceNumber, Long project);

    List<Long> findLicenseIdByDate(Date monthDate, Long invoiceNumber, Long project);
}
