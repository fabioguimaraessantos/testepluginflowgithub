package com.ciandt.pms.exception;

public class PriceTableException extends Throwable {

    private final String messageBundleKey;

    public PriceTableException(String message, String messageBundleKey) {
        super(message);
        this.messageBundleKey = messageBundleKey;
    }

    public String getMessageBundleKey() {
        return messageBundleKey;
    }
}
