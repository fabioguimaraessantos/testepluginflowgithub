package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IForecastRevenueStatusService;
import com.ciandt.pms.model.ForecastRevenueStatus;
import com.ciandt.pms.persistence.dao.IForecastRevenueStatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForecastRevenueStatusService implements IForecastRevenueStatusService {

	@Autowired
	private IForecastRevenueStatusDao forecastRevenueStatusDao;

	public ForecastRevenueStatus findById(Long id) {
		return forecastRevenueStatusDao.findById(id);
	}
}
