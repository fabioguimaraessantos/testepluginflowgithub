package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IForecastRevenueStatusProjectService;
import com.ciandt.pms.model.ForecastRevenueStatusProject;
import com.ciandt.pms.persistence.dao.IForecastRevenueStatusProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForecastRevenueStatusProjectService implements IForecastRevenueStatusProjectService {

	@Autowired
	IForecastRevenueStatusProjectDao forecastRevenueStatusProjectDao;

	public ForecastRevenueStatusProject createOrUpdate(ForecastRevenueStatusProject forecastRevenueStatusProject) {
		return forecastRevenueStatusProjectDao.merge(forecastRevenueStatusProject);
	}
}
