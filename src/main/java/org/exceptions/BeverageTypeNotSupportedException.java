package org.exceptions;

public class BeverageTypeNotSupportedException extends Exception {
    private String message;

    public BeverageTypeNotSupportedException(String message) {
        super(message);
        this.message = message;
    }

    public BeverageTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
