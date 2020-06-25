package org.exceptions;

public class RequestedQuantityNotPresentException extends Exception {
    private String message;

    public RequestedQuantityNotPresentException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public RequestedQuantityNotPresentException(String message) {
        super(message);
        this.message = message;
    }
}
