package org.exceptions;

public class RequestedQuantityNotSufficientException extends Exception {
    private String message;

    public RequestedQuantityNotSufficientException(String message){
        super(message);
        this.message = message;
    }
}
