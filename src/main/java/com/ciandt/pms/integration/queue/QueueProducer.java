package com.ciandt.pms.integration.queue;

import com.ciandt.pms.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class QueueProducer {
    private final Logger logger = LoggerFactory.getLogger(QueueProducer.class);

    @Autowired
    protected Properties systemProperties;

    /**
     *
     */
    protected ConnectionFactory getConectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(systemProperties.getProperty(Constants.RABBITMQ_HOST));
        factory.setUsername(systemProperties.getProperty(Constants.RABBITMQ_USERNAME));
        factory.setPassword(systemProperties.getProperty(Constants.RABBITMQ_PASSWORD));
        factory.setPort(Integer.parseInt(systemProperties.getProperty(Constants.RABBITMQ_PORT)));
        return factory;
    }

    /**
     * @param channel
     * @param queue
     * @param message
     */
    protected void publish(Channel channel, String queue, final String message) throws IOException {
        logger.info(String.format("Sending message %s to queue %s.", message, systemProperties.getProperty(queue)));
        channel.basicPublish("", systemProperties.getProperty(queue), null, message.getBytes(StandardCharsets.UTF_8));
    }

    protected void publish(String queue, final String message) throws TimeoutException, IOException {
        try (Connection connection = getConectionFactory().newConnection()) {
            Channel channel = connection.createChannel();
            publish(channel, queue, message);
        } catch (Exception e) {
            logger.error(String.format("Failed to publish. %s", e));
        }
    }
}