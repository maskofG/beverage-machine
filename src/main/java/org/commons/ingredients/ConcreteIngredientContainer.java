package org.commons.ingredients;

import org.commons.machine.BeverageOutputMessage;
import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

/**
 * Concrete class for ingredient container that can contain
 * any of the @{@link IngredientType} ingredient
 *
 */
public class ConcreteIngredientContainer implements IngredientContainer {
    private IngredientType type;
    private int quantity;

    /**
     * fill container with initial amount of ingredient of the type @{@link IngredientType}
     * negative quantity is not supported and throws @{@link IllegalArgumentException}.
     * @param type of the ingredient filled in the container
     * @param quantity of the ingredient being filled in the container
     */
    public ConcreteIngredientContainer(IngredientType type, int quantity) {
        if (type == null || quantity < 0)
            throw new IllegalArgumentException("argument is not correct, type=" + type +
                    ", quantity=" + quantity);
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Quantity of the ingredient in the container
     * @return quantity of the ingredient in the container
     */
    @Override
    public int quantity() {
        return quantity;
    }

    /**
     * type of the ingredient filled in the container
     * @return @{@link IngredientType} of the ingredient
     */
    @Override
    public IngredientType type() {
        return type;
    }

    /**
     * check if container has specified amount of ingredient in the container
     * if quantity == 0, it throws @{@link RequestedQuantityNotPresentException}
     * if quantity < amount, it throws @{@link RequestedQuantityNotSufficientException}
     *
     * @param amount of the ingredient that is being check in the container
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    @Override
    public void check(int amount)
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
            if (quantity == 0)
                throw new RequestedQuantityNotPresentException(type.toString() + " " +
                        BeverageOutputMessage.QTY_NA);
            if (quantity < amount)
                throw new RequestedQuantityNotSufficientException( type.toString() + " " +
                        BeverageOutputMessage.QTY_NS);
    }


    /**
     * Retrieve the specified amount of ingredient from the container.
     *
     * @param amount of ingredient that is retrieved from container
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    @Override
    public void retrieve(int amount)
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
        check(amount);
        quantity -= amount;
    }

    /**
     * refill specified amount of ingredient in the container
     * @param amount of the ingredient being refilled in the container
     */
    @Override
    public void refill(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("amount refilled cannot be negative");

        quantity += amount;
    }
}
