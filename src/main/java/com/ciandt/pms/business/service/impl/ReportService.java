package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.business.service.IReportService;
import com.ciandt.pms.model.VwPmsContratoProfitCenter;
import com.ciandt.pms.persistence.dao.IReportDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * A classe ControleReajusteService proporciona as funcionalidades de serviço
 * referente a entidade ControleReajuste.
 * 
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class ReportService implements IReportService {

	/** Instancia do DAO da entidade ControleReajusteAud. */
	@Autowired
	private IReportDao dao;

	/** Instancia do Servico da entidade {@link ParametroService}. */
	@Autowired
	private IParametroService parametroService;

	public List<VwPmsContratoProfitCenter> getRevenueForecast(Date dtValor) {
		return dao.getRevenueForecast(dtValor);
	}

	@Transactional
	public void runProcPlannedCost(Long codigoContratoPratica) {
		dao.runProcPlannedCost(codigoContratoPratica);
	}
}