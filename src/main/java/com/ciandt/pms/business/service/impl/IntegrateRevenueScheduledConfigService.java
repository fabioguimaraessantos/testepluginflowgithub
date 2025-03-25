package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IIntegrateRevenueScheduledConfigService;
import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Parametro;
import com.ciandt.pms.util.PMSUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class IntegrateRevenueScheduledConfigService implements IIntegrateRevenueScheduledConfigService {

    private static final Logger LOGGER = LogManager
            .getLogger(IntegrateRevenueScheduledConfigService.class);

    @Autowired
    IParametroService parametroService;

    @Autowired
    Properties systemProperties;

    @Override
    public Parametro findParametro() {
        return parametroService.findParametroByNomeParametro(Constants.MEGA_MIDDLEWARE_DATE_INTERCOMPANY_REVENUE_INTEGRATION);
    }

    @Override
    @Transactional
    public void update(Parametro parametro) {
        Parametro param = findParametro();
        if (param != null) {
            parametroService.updateParametro(parametro);
        } else {
            parametroService.createParametro(parametro);
        }
    }

    @Override
    public void startManualIntegration() throws ConnectTimeoutException {
        callMegaMiddlewareByMessaging(Constants.MEGA_MIDDLEWARE_DOACTION_KEY_START_INTEGRATION);
    }

    @Override
    public void clearMegaMiddlewareCache() throws ConnectTimeoutException {
        callMegaMiddlewareByMessaging(Constants.MEGA_MIDDLEWARE_DOACTION_KEY_CLEAR_CACHE + Constants.MEGA_MIDDLEWARE_DATE_INTERCOMPANY_REVENUE_INTEGRATION);
    }

    private void callMegaMiddlewareByMessaging(String actionKey) throws ConnectTimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(systemProperties.getProperty(Constants.RABBITMQ_HOST));
        factory.setUsername(systemProperties.getProperty(Constants.RABBITMQ_USERNAME));
        factory.setPassword(systemProperties.getProperty(Constants.RABBITMQ_PASSWORD));
        factory.setPort(Integer.valueOf(systemProperties.getProperty(Constants.RABBITMQ_PORT)));

        try (Connection connection = factory.newConnection()) {
            final Channel channel = connection.createChannel();
            channel.basicPublish("", systemProperties.getProperty(Constants.RABBIT_QUEUE_MEGA_MIDDLEWARE_DOACTION), null, actionKey.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error("Failed to create connection with RabbitMQ.", e);
            throw new ConnectTimeoutException("Failed to create connection with RabbitMQ.");
        }
    }
}
