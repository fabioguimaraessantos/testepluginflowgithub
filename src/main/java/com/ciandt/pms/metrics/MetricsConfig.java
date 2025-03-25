package com.ciandt.pms.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class MetricsConfig {

	private static MetricsConfig instance;

	private final PrometheusMeterRegistry meterRegistry;

	private MetricsConfig() {
		this.meterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
		Metrics.addRegistry(this.meterRegistry);
	}

	public static synchronized MetricsConfig getInstance() {
		if (instance == null) {
			instance = new MetricsConfig();
		}
		return instance;
	}

	public PrometheusMeterRegistry getMeterRegistry() {
		return this.meterRegistry;
	}
}
