package com.ciandt.pms.exception.service;

import org.apache.http.HttpStatus;

public class ConflictException extends ServiceException {

    /**
     * @param message
     */
    public ConflictException(String message) {
        super(HttpStatus.SC_CONFLICT, message);
    }
}