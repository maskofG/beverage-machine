package org.commons.ingredients;

import org.commons.machine.BeverageOutputMessage;
import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

public class ConcreteIngredientContainer implements IngredientContainer {
    private IngredientType type;
    private int quantity;

    public ConcreteIngredientContainer(IngredientType type, int quantity) {
        if (type == null || quantity < 0)
            throw new IllegalArgumentException("argument is not correct, type=" + type + ", quantity=" + quantity);
        this.type = type;
        this.quantity = quantity;
    }

    @Override
    public int quantity() {
        return quantity;
    }

    @Override
    public IngredientType type() {
        return type;
    }

    @Override
    public void check(int amount)
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
            if (quantity == 0)
                throw new RequestedQuantityNotPresentException(type.toString() + " " + BeverageOutputMessage.QTY_NA);
            if (quantity < amount)
                throw new RequestedQuantityNotSufficientException( type.toString() + " " + BeverageOutputMessage.QTY_NS);
    }


    @Override
    public void retrieve(int amount)
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
        check(amount);
        quantity -= amount;
    }

    @Override
    public void refill(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("amount refilled cannot be negative");

        quantity += amount;
    }
}
