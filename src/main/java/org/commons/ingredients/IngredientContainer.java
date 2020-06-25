package org.commons.ingredients;

import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

/**
 * Functionality that an ingredient container should support
 */
public interface IngredientContainer {
    /**
     * Quantity of the ingredient present in the container
     * @return quantity of the ingredient
     */
    public int quantity();

    /**
     * type of ingredient present in the container
     * @return @{@link IngredientType} which is container in the container
     */
    public IngredientType type();

    /**
     * check if container has specified amount of ingredient in the container
     * Depending on the value of quantity it can throw either of the
     * two exceptions
     *
     * @param amount of the ingredient that is being check in the container
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    public void check(int amount)
            throws RequestedQuantityNotPresentException,
            RequestedQuantityNotSufficientException;

    /**
     * Retrieve the specified amount of ingredient from the container.
     *
     * @param amount of ingredient that is retrieved from container
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    public void retrieve(int amount)
            throws RequestedQuantityNotPresentException,
            RequestedQuantityNotSufficientException;

    /**
     * refill specified amount of ingredient in the container
     *
     * @param amount of the ingredient being refilled in the container
     */
    public void refill(int amount);
}
