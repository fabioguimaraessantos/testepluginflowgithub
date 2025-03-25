package com.ciandt.pms.integration.queue;

import com.ciandt.pms.Constants;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class RecalculateMapProducer extends QueueProducer{
    public void send(final String message) throws IOException, TimeoutException {
        publish(Constants.RABBITMQ_RECALCULATE_MAP_QUEUE_NAME, message);
    }
}