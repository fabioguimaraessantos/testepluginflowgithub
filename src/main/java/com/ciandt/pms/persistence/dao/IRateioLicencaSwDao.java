package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.RateioLicencaSw;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.model.vo.LicencaSwUserCell;
import com.ciandt.pms.model.vo.LicencaSwUserVO;

import java.util.Date;
import java.util.List;

public interface IRateioLicencaSwDao extends IAbstractDao<Long, RateioLicencaSw> {
    List<LicencaSwUserVO> findByFilter(final Date dtCompetencia, final Long codTiRecurso, final String status);

    List<LicencaSwDetail> findByRecursoTIAndMonthGroupByCentroCustoAndProjeto(final Date dtCompetencia, Long codTiRecurso);

    List<RateioLicencaSw> findByRecursoTIAndMonth(final Date dtCompetencia, Long codTiRecurso);

    List<LicencaSwUserCell> findByRecursoTiAndMonthAndStatus(final Long itResourceCode, final Date monthDate, final String status);
}
