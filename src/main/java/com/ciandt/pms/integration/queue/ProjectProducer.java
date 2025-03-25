package com.ciandt.pms.integration.queue;

import com.ciandt.pms.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Service
public class ProjectProducer {

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;


    public void send(String message, String type) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(systemProperties.getProperty(Constants.RABBITMQ_HOST));
        factory.setUsername(systemProperties.getProperty(Constants.RABBITMQ_USERNAME));
        factory.setPassword(systemProperties.getProperty(Constants.RABBITMQ_PASSWORD));
        factory.setPort(Integer.valueOf(systemProperties.getProperty(Constants.RABBITMQ_PORT)));

        Connection connection = null;

        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            if (type.equals(Constants.PROJECT_PRODUCER)) {
                channel.basicPublish("", systemProperties.getProperty(Constants.RABBITMQ_PROJECT_EXCHANGE_QUICKBOOKS_QUEUE_NAME), null, message.getBytes("UTF8"));
            }

        } finally {
            connection.close();
        }
    }
}
