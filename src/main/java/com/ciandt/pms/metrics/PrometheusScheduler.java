package com.ciandt.pms.metrics;

import com.ciandt.pms.model.vo.ReceitaDealFiscalRow;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PrometheusScheduler implements ServletContextListener {

	private Timer timer;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		timer = new Timer();
		ServletContext servletContext = servletContextEvent.getServletContext();
		timer.scheduleAtFixedRate(new PrometheusTask(servletContext), 0, 15000); // Executar a cada 15 segundos (em milissegundos)
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		timer.cancel();
	}

	private static class PrometheusTask extends TimerTask {
	private static final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

		ServletContext servletContext;
		public PrometheusTask(ServletContext servletContext) {
			this.servletContext = servletContext;
		}

		@Override
		public void run() {

			recordToFilePrometheus();
		}

		public void recordToFilePrometheus() {

			File file = new File( servletContext.getRealPath("/") + "/pages/public/prometheus");
			try (FileWriter writer = new FileWriter(file.getAbsoluteFile());
				 FileInputStream fis = new FileInputStream(file)){

				 MetricsConfig metricsConfig = MetricsConfig.getInstance();
				 String content = metricsConfig.getMeterRegistry().scrape();
				 writer.write(content);

				System.out.println("Update Metrics");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
