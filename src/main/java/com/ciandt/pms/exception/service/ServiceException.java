package com.ciandt.pms.exception.service;

public class ServiceException extends RuntimeException {

    private int code;
    private String message;

    /**
     * @param code
     * @param message
     */
    public ServiceException(int code, String message) {
        super(code + " - " + message);
        this.code = code;
        this.message = message;
    }
}