package org.exceptions;

public class IncorrectIngredientTypeException extends Exception {
    private String message;

    public IncorrectIngredientTypeException(String message) {
        super(message);
        this.message = message;
    }
}
