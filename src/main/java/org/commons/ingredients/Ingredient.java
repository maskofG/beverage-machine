package org.commons.ingredients;

import org.exceptions.RequestQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

public interface Ingredient {
    public int quantity();
    public IngredientType type();
    public void check(int amount) throws RequestQuantityNotPresentException, RequestedQuantityNotSufficientException;
    public void retrieve(int amount) throws RequestQuantityNotPresentException, RequestedQuantityNotSufficientException;
    public void refill(int amount);
}
