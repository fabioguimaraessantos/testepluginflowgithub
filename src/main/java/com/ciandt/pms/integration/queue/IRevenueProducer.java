package com.ciandt.pms.integration.queue;

import org.springframework.stereotype.Service;

@Service
public interface IRevenueProducer {

    void send(String message);
}
