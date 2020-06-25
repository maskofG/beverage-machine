package org.exceptions;

public class IncorrectIngredientTypeException extends Exception {
    private String message;

    public IncorrectIngredientTypeException(String message) {
        super(message);
        this.message = message;
    }

    public IncorrectIngredientTypeException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
