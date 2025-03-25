package com.ciandt.pms.exception;

public class ExchangeRateException extends Throwable {

    private final String messageBundleKey;

    public ExchangeRateException(String message, String messageBundleKey) {
        super(message);
        this.messageBundleKey = messageBundleKey;
    }

    public String getMessageBundleKey() {
        return messageBundleKey;
    }
}
