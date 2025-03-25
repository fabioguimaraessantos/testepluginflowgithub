package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ReportRow;
import com.ciandt.pms.model.VwPmsContratoProfitCenter;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Repository
public interface IReportDao extends IAbstractDao<Long, ReportRow> {

	List<VwPmsContratoProfitCenter> getRevenueForecast(Date dtValor);

	void runProcPlannedCost(Long codigoContratoPratica);
}