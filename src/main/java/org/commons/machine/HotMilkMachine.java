package org.commons.machine;

import org.commons.ingredients.BeverageComposition;
import org.commons.ingredients.Ingredient;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.IncorrectIngredientTypeException;
import org.exceptions.RequestQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

import java.util.ArrayList;
import java.util.List;

public class HotMilkMachine extends BaseBeverageMachine {

    private BeverageComposition beverageRecipe;
    private Ingredient milk;

    private HotMilkMachine(int outlet){
        super(outlet);
    }

    @Override
    public synchronized void retrieveBeverageItems(BeverageType type) throws RequestQuantityNotPresentException,
            RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.HOT_MILK )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " is not supported in " +
                    this.getClass().getSimpleName() +" machine.");
        checkAvailability(type);
        milk.retrieve(beverageRecipe.getQuantity(IngredientType.MILK));
    }

    private void checkAvailability(BeverageType type)
            throws RequestQuantityNotPresentException, RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.HOT_MILK )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " is not supported in " +
                    this.getClass().getSimpleName() +" machine.");

        milk.check(beverageRecipe.getQuantity(IngredientType.MILK));
    }

    @Override
    public int ingredientLevel(IngredientType type) {
        if (type == null || type != IngredientType.MILK) return 0;

        return milk.quantity();
    }

    @Override
    public void refillIngredient(IngredientType type, int amount) throws IncorrectIngredientTypeException {
        if (type == null || type != IngredientType.MILK)
            throw new IncorrectIngredientTypeException("Refilling of Ingredient Type=" + type + " is not supported in " +
                    this.getClass().getSimpleName());

        milk.refill(amount);
    }

    @Override
    public List<IngredientType> ingredientsRunningLow() {
        List<IngredientType> ingr = new ArrayList<>();
        if (milk.quantity() < beverageRecipe.getQuantity(IngredientType.MILK)) {
            ingr.add(IngredientType.MILK);
        }
        return ingr;
    }

    public static class Builder {
        private BeverageComposition beverageComposition;
        private Ingredient milk;
        private int outlet;

        public Builder outlet(int outlet){
            this.outlet = outlet;
            return this;
        }

        public Builder milk(Ingredient ingredient){
            this.milk = ingredient;
            return this;
        }

        public Builder beverageComposition(BeverageComposition beverageComposition){
            this.beverageComposition = beverageComposition;
            return this;
        }

        public HotMilkMachine build(){
            if (milk == null || beverageComposition == null) throw new IllegalArgumentException("argument for "
                    + HotMilkMachine.class.getSimpleName() + " construction is not correct.");

            HotMilkMachine hotMilkMachine = new HotMilkMachine(outlet);
            hotMilkMachine.milk = milk;
            hotMilkMachine.beverageRecipe = beverageComposition;
            return hotMilkMachine;
        }
    }
}
