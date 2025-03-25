package com.ciandt.pms.exception.service;

import org.apache.http.HttpStatus;

public class NotFoundException extends ServiceException{

    /**
     * @param message
     */
    public NotFoundException(String message) {
        super(HttpStatus.SC_NOT_FOUND, message);
    }
}