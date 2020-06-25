package org.commons.ingredients;

import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

/**
 * Functionality than an ingredient container should support
 *
 */
public interface IngredientContainer {
    /**
     * Quantity of the ingredient present
     * @return quantity of the ingredient
     */
    public int quantity();

    /**
     *
     * @return
     */
    public IngredientType type();
    public void check(int amount) throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException;
    public void retrieve(int amount) throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException;
    public void refill(int amount);
}
