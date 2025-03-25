package com.ciandt.pms.business.service;

import com.ciandt.pms.model.ForecastRevenueStatus;
import org.springframework.stereotype.Service;

@Service
public interface IForecastRevenueStatusService {

	ForecastRevenueStatus findById(Long id);
}
