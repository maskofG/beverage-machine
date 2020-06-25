package org.commons.machine;

import org.commons.ingredients.BeverageComposition;
import org.commons.ingredients.IngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.IncorrectIngredientTypeException;
import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoffeeMachine extends BaseBeverageMachine {
    private BeverageComposition beverageRecipe;
    private Map<IngredientType, IngredientContainer> ingredientContainer;

    private CoffeeMachine(int outlet) {
        super(outlet);
    }

    @Override
    public synchronized void retrieveBeverageItems(BeverageType type) throws BeverageTypeNotSupportedException,
            RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
        if (type == null || type != BeverageType.HOT_COFFEE )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " is not supported in " +
                    this.getClass().getSimpleName() +" machine.");

        checkAvailability(type);
        ingredientContainer.get(IngredientType.WATER).retrieve(beverageRecipe.getQuantity(IngredientType.WATER));
        ingredientContainer.get(IngredientType.MILK).retrieve(beverageRecipe.getQuantity(IngredientType.MILK));
        ingredientContainer.get(IngredientType.COFFEE_SYRUP).retrieve(beverageRecipe.getQuantity(IngredientType.COFFEE_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP).retrieve(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }


    private void checkAvailability(BeverageType type)
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.HOT_COFFEE )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " is not supported in " +
                    this.getClass().getSimpleName() +" machine.");

        checkHotWater();
        checkHotMilk();
        ingredientContainer.get(IngredientType.COFFEE_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.COFFEE_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    private void checkHotWater()
            throws RequestedQuantityNotSufficientException, RequestedQuantityNotPresentException {
        try {
            ingredientContainer.get(IngredientType.WATER).check(beverageRecipe.getQuantity(IngredientType.WATER));
        } catch (RequestedQuantityNotPresentException rqnpe) {
            throw new RequestedQuantityNotPresentException("hot_water is not available");
        } catch (RequestedQuantityNotSufficientException e) {
            throw new RequestedQuantityNotSufficientException("hot_water is not sufficient");
        }
    }

    private void checkHotMilk()
            throws RequestedQuantityNotSufficientException, RequestedQuantityNotPresentException {
        try {
            ingredientContainer.get(IngredientType.MILK).check(beverageRecipe.getQuantity(IngredientType.MILK));
        } catch (RequestedQuantityNotPresentException rqnpe) {
            throw new RequestedQuantityNotPresentException("hot_milk is not available");
        } catch (RequestedQuantityNotSufficientException e) {
            throw new RequestedQuantityNotSufficientException("hot_milk is not sufficient");
        }
    }

    @Override
    public int ingredientLevel(IngredientType type) {
        int level = 0;
        switch (type) {
            case WATER:         level = ingredientContainer.get(IngredientType.WATER).quantity();
                                break;
            case MILK:          level = ingredientContainer.get(IngredientType.MILK).quantity();
                                break;
            case COFFEE_SYRUP:  level = ingredientContainer.get(IngredientType.COFFEE_SYRUP).quantity();
                                break;
            case SUGAR_SYRUP:   level = ingredientContainer.get(IngredientType.SUGAR_SYRUP).quantity();
                                break;
        }
        return level;
    }

    @Override
    public synchronized void refillIngredient(IngredientType type, int amount) throws IncorrectIngredientTypeException {
        switch (type) {
            case WATER:         ingredientContainer.get(IngredientType.WATER).refill(amount);
                                break;
            case MILK:          ingredientContainer.get(IngredientType.MILK).refill(amount);
                                break;
            case COFFEE_SYRUP:  ingredientContainer.get(IngredientType.COFFEE_SYRUP).refill(amount);
                                break;
            case SUGAR_SYRUP:   ingredientContainer.get(IngredientType.SUGAR_SYRUP).refill(amount);
                                break;
            default:            throw new IncorrectIngredientTypeException("Refilling of Ingredient Type=" + type +
                                " is not supported in " + getClass().getSimpleName());
        }
    }

    @Override
    public List<IngredientType> ingredientsRunningLow() {
        List<IngredientType> ingredientTypeList = new ArrayList<>();
        if (ingredientContainer.get(IngredientType.WATER).quantity() < beverageRecipe.getQuantity(
                IngredientType.WATER)) {
            ingredientTypeList.add(IngredientType.WATER);
        }
        if (ingredientContainer.get(IngredientType.MILK).quantity() < beverageRecipe.getQuantity(
                IngredientType.MILK)){
            ingredientTypeList.add(IngredientType.MILK);
        }
        if (ingredientContainer.get(IngredientType.COFFEE_SYRUP).quantity() < beverageRecipe.getQuantity(
                IngredientType.COFFEE_SYRUP)) {
            ingredientTypeList.add(IngredientType.COFFEE_SYRUP);
        }
        if (ingredientContainer.get(IngredientType.SUGAR_SYRUP).quantity() < beverageRecipe.getQuantity(
                IngredientType.SUGAR_SYRUP)) {
            ingredientTypeList.add(IngredientType.SUGAR_SYRUP);
        }

        return ingredientTypeList;
    }

    public static class Builder {
        private int outlet;
        private Map<IngredientType, IngredientContainer> ingredientContainer = new HashMap<>();
        private BeverageComposition beverageRecipe;
        private IngredientContainer water;
        private IngredientContainer milk;
        private IngredientContainer coffeeSyrup;
        private IngredientContainer sugarSyrup;

        public Builder outlet(int outlet) {
            this.outlet = outlet;
            return this;
        }

        public Builder addIngredient(IngredientContainer ingredientContainer) {
            switch(ingredientContainer.type()) {
                case WATER:         water = ingredientContainer;
                                    this.ingredientContainer.put(IngredientType.WATER, ingredientContainer);
                                    break;
                case MILK:          milk = ingredientContainer;
                                    this.ingredientContainer.put(IngredientType.MILK, ingredientContainer);
                                    break;
                case COFFEE_SYRUP:  coffeeSyrup = ingredientContainer;
                                    this.ingredientContainer.put(IngredientType.TEA_LEAVES_SYRUP, ingredientContainer);
                                    break;
                case SUGAR_SYRUP:   sugarSyrup = ingredientContainer;
                                    this.ingredientContainer.put(IngredientType.SUGAR_SYRUP, ingredientContainer);
                                    break;
                default:            throw new IllegalArgumentException("cannot accept ingredient other than " +
                                    "[coffee_syrup,sugar_syrup]");
            }
            return this;
        }

        public Builder addRecipe(BeverageComposition beverageComposition) {
            this.beverageRecipe = beverageComposition;
            return this;
        }

        public CoffeeMachine build(){
            if (beverageRecipe == null || water == null || milk == null ||
                    coffeeSyrup == null || sugarSyrup == null)
                throw new IllegalArgumentException("argument for " + GreenTeaMachine.class.getSimpleName() +
                        " construction is not correct.");

            CoffeeMachine coffeeMachine = new CoffeeMachine(outlet);
            coffeeMachine.ingredientContainer = ingredientContainer;
            coffeeMachine.beverageRecipe = beverageRecipe;
            return coffeeMachine;
        }
    }
}
