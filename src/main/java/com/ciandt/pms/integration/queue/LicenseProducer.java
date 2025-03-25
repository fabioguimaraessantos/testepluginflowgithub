package com.ciandt.pms.integration.queue;

import com.ciandt.pms.Constants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

@Service
public class LicenseProducer extends QueueProducer{

    private Logger logger = LoggerFactory.getLogger(LicenseProducer.class);

    @Autowired
    private Properties systemProperties;

    public void send(final String message) throws IOException, TimeoutException {
        Connection connection = null;
        try {
            connection = getConectionFactory().newConnection();

            if(connection == null){
                logger.error(Constants.RABBITMQ_LOG_CONNECTION_FAIL_LAYOUT);
                logger.error(Constants.RABBITMQ_LOG_CONNECTION_FAIL_TEXT);

            }else{
                final Channel channel = connection.createChannel();

                channel.confirmSelect();
                channel.addShutdownListener(new ShutdownListener() {
                    public void shutdownCompleted(ShutdownSignalException cause) {
//                        logger.error(Constants.RABBITMQ_QUEUE_NOT_FOUND + " : " + Constants.RABBITMQ_LICENSE_TO_ORACLE_QUEUE_NAME);
                    }
                });

                publish(channel, Constants.RABBITMQ_LICENSE_TO_ORACLE_QUEUE_NAME, message);
            }
        } finally {
            if(connection != null)
                connection.close();
        }
    }
}