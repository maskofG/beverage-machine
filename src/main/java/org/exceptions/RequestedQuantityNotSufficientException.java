package org.exceptions;

public class RequestedQuantityNotSufficientException extends Exception {
    private String message;

    public RequestedQuantityNotSufficientException(String message){
        super(message);
        this.message = message;
    }

    public RequestedQuantityNotSufficientException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
