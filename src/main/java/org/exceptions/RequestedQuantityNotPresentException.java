package org.exceptions;

public class RequestedQuantityNotPresentException extends Exception {
    private String message;

    public RequestedQuantityNotPresentException(String message) {
        super(message);
        this.message = message;
    }
}
