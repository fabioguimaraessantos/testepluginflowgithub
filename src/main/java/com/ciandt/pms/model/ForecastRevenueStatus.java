package com.ciandt.pms.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "FORECAST_REVENUE_STATUS")
public class ForecastRevenueStatus {

	@Id
	@GeneratedValue(generator = "ForecastRevenueStatusSeq")
	@SequenceGenerator(name = "ForecastRevenueStatusSeq", sequenceName = "SQ_FORS_CD_ID", allocationSize = 1)
	@Column(name = "FORS_CD_ID", unique = true, nullable = false, precision = 18, scale = 0)
	private Long id;

	@Column(name = "FORS_NM_STATUS", nullable = false, length = 40)
	private String name;
}
