package com.ciandt.pms.integration.vo;

public class ConsumerExceptionMessage {

    private final String exceptionMessage;
    private final String exceptionCause;
    private final String payload;

    public ConsumerExceptionMessage(Exception e, String payload) {
        this.exceptionMessage = e.getMessage();
        this.exceptionCause = e.getCause() != null ? e.getCause().toString() : "unknown";
        this.payload = payload;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public String getExceptionCause() {
        return exceptionCause;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "ConsumerExceptionMessage{" +
                "exceptionMessage='" + exceptionMessage + '\'' +
                ", exceptionCause='" + exceptionCause + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
