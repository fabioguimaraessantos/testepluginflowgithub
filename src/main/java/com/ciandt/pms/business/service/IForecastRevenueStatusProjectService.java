package com.ciandt.pms.business.service;

import com.ciandt.pms.model.ForecastRevenueStatusProject;
import org.springframework.stereotype.Service;

@Service
public interface IForecastRevenueStatusProjectService {

	ForecastRevenueStatusProject createOrUpdate(ForecastRevenueStatusProject forecastRevenueStatusProject);
}
