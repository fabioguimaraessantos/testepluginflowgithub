package com.ciandt.pms.integration.queue;

import com.ciandt.pms.integration.vo.ConsumerExceptionMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Component for publish messages to exception exchanges/queues.
 */
@Component
public class ConsumerExceptionProducer extends QueueProducer {

    private final Logger logger = LoggerFactory.getLogger(RevenueConsumer.class);

    /**
     * Sends payload to exception queue
     * @param payload incoming payload that thrown exception
     * @param e exception that occurred
     * @param queueName name of rabbitMQ queue to publish exception payload
     */
    public void send(String payload, Exception e, String queueName) {
        ConsumerExceptionMessage exceptionMessage = new ConsumerExceptionMessage(e, payload);
        try (Connection conn = getConectionFactory().newConnection()) {
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(exceptionMessage);

            logger.info("Publishing exception payload at {}.", queueName);

            Channel channel = conn.createChannel();
            if (channel == null) {
                logger.warn("Failed to create channel.");
                return;
            }

            publish(channel, queueName, json);
            logger.info("Message published with success {}.", exceptionMessage);
        } catch (Exception ex) {
            logger.error("Failed to publish exception payload at {}. JSON {}.", queueName, exceptionMessage, ex);
        }
    }
}
