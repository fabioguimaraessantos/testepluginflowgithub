package com.ciandt.pms.integration.queue;

import org.springframework.stereotype.Service;

@Service
public interface IInvoiceProducer {

    void send(String message);
}

