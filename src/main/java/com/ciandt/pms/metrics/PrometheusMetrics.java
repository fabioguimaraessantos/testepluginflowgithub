package com.ciandt.pms.metrics;

import org.springframework.stereotype.Component;

@Component
public class PrometheusMetrics {

	private final MetricsConfig metricsConfig;

	private final String metricsName;

	public PrometheusMetrics(String metricsName) {

		this.metricsConfig = MetricsConfig.getInstance();
		this.metricsName = metricsName;
    }

	public void incrementCounter() {

		metricsConfig.getMeterRegistry().counter(metricsName + ".counter").increment();
	}

	public void setGauge(int newValue) {

		metricsConfig.getMeterRegistry().gauge(metricsName + ".gauge", newValue);

	}

	public void summaryRecordDuration(long duration) {

		metricsConfig.getMeterRegistry().summary(metricsName + ".summary").record(duration);
	}
}
