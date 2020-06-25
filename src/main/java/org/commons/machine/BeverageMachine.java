package org.commons.machine;

import org.commons.ingredients.IngredientType;
import org.exceptions.IncorrectIngredientTypeException;

import java.util.List;

public interface BeverageMachine {

    public String dispense(BeverageType type);
    public int ingredientLevel(IngredientType type);

    public void refillIngredient(IngredientType type, int amount) throws IncorrectIngredientTypeException;

    public List<IngredientType> ingredientsRunningLow();

}
