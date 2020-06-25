package org.commons.machine;

import org.commons.ingredients.BeverageComposition;
import org.commons.ingredients.Ingredient;
import org.commons.ingredients.IngredientType;
import org.exceptions.*;

import java.util.ArrayList;
import java.util.List;


public class HotWaterMachine extends BaseBeverageMachine {

    private BeverageComposition beverageRecipe;
    private Ingredient water;

    private HotWaterMachine(int outletNumber){
        super(outletNumber);
    }

    @Override
    public synchronized void retrieveBeverageItems(BeverageType type) throws RequestQuantityNotPresentException,
            RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.HOT_WATER )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " " + BeverageOutputMessage.NOT_SUPPORTED
                    + " in " + this.getClass().getSimpleName() +" machine.");
        checkAvailability();
        water.retrieve(beverageRecipe.getQuantity(IngredientType.WATER));
    }

    private void checkAvailability()
            throws RequestQuantityNotPresentException, RequestedQuantityNotSufficientException{
        water.check(beverageRecipe.getQuantity(IngredientType.WATER));
    }

    @Override
    public int ingredientLevel(IngredientType type) {
        if (type == null || type != IngredientType.WATER) return 0;
        return water.quantity();
    }

    @Override
    public synchronized void refillIngredient(IngredientType type, int amount)
            throws IncorrectIngredientTypeException{
        if (type == null || type != IngredientType.WATER)
            throw new IncorrectIngredientTypeException("Refilling of Ingredient Type=" + type
                    + BeverageOutputMessage.NOT_SUPPORTED  + " in " + this.getClass().getSimpleName());
        water.refill(amount);
    }

    @Override
    public List<IngredientType> ingredientsRunningLow() {
        List<IngredientType> ingr = new ArrayList<>();
        if (water.quantity() < beverageRecipe.getQuantity(IngredientType.WATER)) {
            ingr.add(IngredientType.WATER);
        }
        return ingr;
    }

    public static class Builder {
        private BeverageComposition beverageComposition;
        private Ingredient water;
        private int outlet;

        public Builder outlet(int outlet){
            this.outlet = outlet;
            return this;
        }

        public Builder water(Ingredient ingredient){
            this.water = ingredient;
            return this;
        }

        public Builder beverageComposition(BeverageComposition beverageComposition){
            this.beverageComposition = beverageComposition;
            return this;
        }

        public HotWaterMachine build(){
            if (water == null || beverageComposition == null) throw new IllegalArgumentException("argument for "
                    + HotWaterMachine.class.getSimpleName() + " construction is not correct.");
            HotWaterMachine hotWaterMachine = new HotWaterMachine(outlet);
            hotWaterMachine.water = water;
            hotWaterMachine.beverageRecipe = beverageComposition;
            return hotWaterMachine;
        }
    }
}
