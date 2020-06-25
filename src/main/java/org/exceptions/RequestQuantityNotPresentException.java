package org.exceptions;

public class RequestQuantityNotPresentException extends Exception {
    private String message;

    public RequestQuantityNotPresentException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public RequestQuantityNotPresentException(String message) {
        super(message);
        this.message = message;
    }
}
