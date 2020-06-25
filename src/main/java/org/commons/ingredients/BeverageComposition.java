package org.commons.ingredients;

import java.util.HashMap;
import java.util.Map;

/**
 * Its a recipe book for beverages and stores quantity
 * of different @{@link IngredientType} for making a
 * cup of a particular @{@link org.commons.machine.BeverageType}
 */
public class BeverageComposition {

    /**
     * quantity of different @{@link IngredientType}
     *
     */
    private Map<IngredientType, Integer> ingrQuantity;

    public BeverageComposition(){
        ingrQuantity = new HashMap<>();
    }

    /**
     * returns quantity of a specific @{@link IngredientType} needed to
     * a cup of a particular beverage.
     *
     * @param type of @{@link IngredientType} for which its quantity is
     *             is retrieved
     * @return quantity of the @{@link IngredientType} = type
     */
    public int getQuantity(IngredientType type){
        return ingrQuantity.getOrDefault(type, 0);
    }

    /**
     * Used while building or updating recipe for a beverage
     * It allows us to update/add @{@link IngredientType} and its quantity
     * for a beverage
     *
     * @param type of ingredient being added or updated
     * @param quantity of the ingredient used in a cup of brewed beverage
     */
    public void put(IngredientType type, int quantity) {
        if (type == null || quantity < 0)
            throw new IllegalArgumentException("Illegal argument check[type=" + type + "" +
                    ", quantity=" + quantity + "]");
        this.ingrQuantity.put(type, quantity);
    }
}
