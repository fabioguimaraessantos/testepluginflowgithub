package com.ciandt.pms.business.service;

import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntry;
import com.ciandt.pms.model.RateioLicencaSw;
import com.ciandt.pms.model.vo.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public interface IRateioLicencaSwService {
    RateioLicencaSw findById(final Long entityId);

    List<LicencaSwUserVO> findByFilter(final Date dtCompetencia, final Long codTiRecurso, final String status);

    List<LicencaSwDetail> findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(final Date dtCompetencia, Long codTiRecurso);

    AccountingEntry generateAccountingByTiRecursoAndMonth(final LicencaSwUserRow licencaSwUserRow, final Date month) throws Exception;

    void setStatusRateioLicencaSw(final Long softwareLicenseCode,final Date month, final String status, String errorMessage);

    List<RateioLicencaSw> findByRecursoTIAndMonth(final Date dtCompetencia, Long codTiRecurso);

    void updateSoftwareLicenseUserFromERP(String code, String status, BigDecimal orderID, String errorMessage) throws ParseException;

    List<LicencaSwUserCell> findToExport(Date monthDate, Long codTiRecurso, String status);

    boolean isMonthApprovedByTiRecurso(Date startDate, Long codigoTiRecurso);
}