package com.ciandt.pms.integration.queue;

import com.ciandt.pms.Constants;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Service
public class InvoiceProducer {

    private Logger logger = LoggerFactory.getLogger(InvoiceConsumer.class);
    @Autowired
    private Properties systemProperties;


    public void send(String message, String type) throws IOException, TimeoutException {
        boolean isReceivedAck = Boolean.FALSE;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(systemProperties.getProperty(Constants.RABBITMQ_HOST));
        factory.setUsername(systemProperties.getProperty(Constants.RABBITMQ_USERNAME));
        factory.setPassword(systemProperties.getProperty(Constants.RABBITMQ_PASSWORD));
        factory.setPort(Integer.valueOf(systemProperties.getProperty(Constants.RABBITMQ_PORT)));

        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.confirmSelect();
            channel.addShutdownListener(new ShutdownListener() {
                public void shutdownCompleted(ShutdownSignalException cause) {
                    if (!cause.isInitiatedByApplication()) {
                        logger.error( Constants.RABBITMQ_QUEUE_NOT_FOUND + " : " + Constants.RABBITMQ_DELETE_INVOICE_EXCHANGE);
                    }
                }
            });

            if (type.equals(Constants.INVOICE_DELETE)) {
                if (!Boolean.TRUE.equals(Boolean.valueOf(systemProperties.getProperty(Constants.RABBITMQ_DELETE_INVOICE_ACTIVE)))) {
                    logger.warn("Delete invoice queue is inactive.");
                    return;
                }
                channel.basicPublish(systemProperties.getProperty(Constants.RABBITMQ_DELETE_INVOICE_EXCHANGE), "", null, message.getBytes("UTF8"));
            }

            if (type.equals(Constants.INVOICE_CANCEL)) {
                if (!Boolean.TRUE.equals(Boolean.valueOf(systemProperties.getProperty(Constants.RABBITMQ_CANCEL_INVOICE_ACTIVE)))) {
                    logger.warn("Cancel invoice queue is inactive.");
                    return;
                }
                channel.basicPublish(
                        systemProperties.getProperty(Constants.RABBITMQ_CANCEL_INVOICE_EXCHANGE),
                        "",
                        null,
                        message.getBytes(StandardCharsets.UTF_8));
            }

        } finally {
            connection.close();
        }
    }

}
