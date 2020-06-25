package org.commons.machine;

import org.commons.ingredients.IngredientType;
import org.exceptions.IncorrectIngredientTypeException;

import java.util.List;

public interface BeverageMachine {
    /**
     * Dispense beverage type and accordingly return string information
     * @param type is one of the type of @{@link BeverageType}
     * @return information is in the form of string if the beverage is
     *          prepared or if its not prepared
     */
    public String dispense(BeverageType type);

    /**
     *  query the quantity or level of the ingredient present in the ingredient container
     * @param type it is one of the @{@link IngredientType}.
     *             Type of ingredient for which the quantity is being queried
     * @return quantity of ingredient left in beverage machine
     */
    public int ingredientLevel(IngredientType type);

    /**
     * add/refill the quantity of the ingredient in the ingredient container.
     *
     * @param type it is one of @{@link IngredientType}.
     *             Type of the ingredient which is being refilled.
     *             if try to fill ingredient which is not supported by beverage machine
     *             then @{@link IncorrectIngredientTypeException} is thrown
     * @param amount quantity of the ingredient being refilled
     * @throws IncorrectIngredientTypeException
     */
    public void refillIngredient(IngredientType type, int amount)
            throws IncorrectIngredientTypeException;

    /**
     * Returns the list of ingredients which are running low in ingredient container
     * and might stop brewing any of the beverages
     * @return list of @{@link IngredientType} which are running low in the beverage
     *          machine and is stoping brewing any of the beverages.
     */
    public List<IngredientType> ingredientsRunningLow();

}
