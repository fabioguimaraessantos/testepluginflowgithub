package com.ciandt.pms.exception.service;

import org.apache.http.HttpStatus;

public class InternalServerErrorException extends ServiceException {

    /**
     * @param message
     */
    public InternalServerErrorException(String message) {
        super(HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
    }
}