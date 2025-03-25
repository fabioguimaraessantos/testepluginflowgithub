package com.ciandt.pms.model;

import lombok.Data;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "FORECAST_REVENUE_STATUS_PROJECT")
public class ForecastRevenueStatusProject implements Serializable {

	@EmbeddedId
	private ForecastRevenueStatusProjectId id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FRSP_CD_CONTRATO_PRATICA")
	private ContratoPratica contratoPratica;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FRSP_CD_REVENUE_STATUS")
	private ForecastRevenueStatus revenueStatusCode;
}
