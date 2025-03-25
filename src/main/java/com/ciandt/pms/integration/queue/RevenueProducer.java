package com.ciandt.pms.integration.queue;

import com.ciandt.pms.Constants;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;


@Service
public class RevenueProducer {

    private Logger logger = LoggerFactory.getLogger(RevenueProducer.class);

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;


    public void send(final String message, final String type) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(systemProperties.getProperty(Constants.RABBITMQ_HOST));
        factory.setUsername(systemProperties.getProperty(Constants.RABBITMQ_USERNAME));
        factory.setPassword(systemProperties.getProperty(Constants.RABBITMQ_PASSWORD));
        factory.setPort(Integer.valueOf(systemProperties.getProperty(Constants.RABBITMQ_PORT)));

        Connection connection = null;
        try {
            connection = factory.newConnection();

            if(connection == null){
                logger.error(Constants.RABBITMQ_LOG_CONNECTION_FAIL_LAYOUT);
                logger.error(Constants.RABBITMQ_LOG_CONNECTION_FAIL_TEXT);
            }else{
                final Channel channel = connection.createChannel();

                channel.confirmSelect();
                channel.addShutdownListener(new ShutdownListener() {
                    public void shutdownCompleted(ShutdownSignalException cause) {
                        if (!cause.isInitiatedByApplication()) {
                            logger.error(Constants.RABBITMQ_QUEUE_NOT_FOUND +" : "+ Constants.RABBITMQ_DELETE_INVOICE_EXCHANGE);
                        }
                        if (type.equals(Constants.REVENUE_PRODUCER)) {
                            logger.error(Constants.RABBITMQ_QUEUE_NOT_FOUND + " : " + Constants.RABBITMQ_REVENUE_EXCHANGE_INCOMING_QUEUE_NAME);
                        } else if (type.equals(Constants.INVOICE_PRODUCER)) {
                            logger.error(Constants.RABBITMQ_QUEUE_NOT_FOUND + " : " + Constants.RABBITMQ_INVOICE_EXCHANGE_INCOMING_QUEUE_NAME);
                        } else if (type.equals(Constants.SOFTWARE_LICENSE_PRODUCER)) {
                            logger.error(Constants.RABBITMQ_QUEUE_NOT_FOUND + " : " + Constants.RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_INCOMING_QUEUE_NAME);
                        }
                    }
                });
                if (type.equals(Constants.REVENUE_PRODUCER)) {
                    channel.basicPublish("", systemProperties.getProperty(Constants.RABBITMQ_REVENUE_EXCHANGE_INCOMING_QUEUE_NAME), null, message.getBytes("UTF8"));
                } else if (type.equals(Constants.INVOICE_PRODUCER)) {
                    channel.basicPublish("", systemProperties.getProperty(Constants.RABBITMQ_INVOICE_EXCHANGE_INCOMING_QUEUE_NAME), null, message.getBytes("UTF8"));
                } else if (type.equals(Constants.SOFTWARE_LICENSE_PRODUCER)) {
                    channel.basicPublish("", systemProperties.getProperty(Constants.RABBITMQ_SOFTWARE_LICENSE_EXCHANGE_INCOMING_QUEUE_NAME), null, message.getBytes("UTF8"));
                }
            }
        } finally {
            connection.close();
        }
    }
}
