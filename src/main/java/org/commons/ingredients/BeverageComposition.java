package org.commons.ingredients;

import java.util.HashMap;
import java.util.Map;

public class BeverageComposition {

    private Map<IngredientType, Integer> ingrQuantity;

    public BeverageComposition(){
        ingrQuantity = new HashMap<>();
    }

    public int getQuantity(IngredientType type){
        return ingrQuantity.getOrDefault(type, 0);
    }

    public void put(IngredientType type, int quantity) {
        if (type == null || quantity < 0)
            throw new IllegalArgumentException("Illegal argument check[type=" + type + ", quantity=" + quantity + "]");
        this.ingrQuantity.put(type, quantity);
    }
}
