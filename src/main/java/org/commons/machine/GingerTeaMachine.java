package org.commons.machine;

import org.commons.ingredients.BeverageComposition;
import org.commons.ingredients.Ingredient;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.IncorrectIngredientTypeException;
import org.exceptions.RequestQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GingerTeaMachine extends BaseBeverageMachine {

    private BeverageComposition beverageRecipe;
    private Map<IngredientType, Ingredient> ingredientContainer;

    private GingerTeaMachine(int outlet) {
        super(outlet);
    }

    @Override
    public synchronized void retrieveBeverageItems(BeverageType type) throws BeverageTypeNotSupportedException,
            RequestQuantityNotPresentException, RequestedQuantityNotSufficientException {
        if (type == null || type != BeverageType.GINGER_TEA )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " is not supported in " +
                    this.getClass().getSimpleName() +" machine.");

        checkAvailability(type);
        ingredientContainer.get(IngredientType.WATER).retrieve(beverageRecipe.getQuantity(IngredientType.WATER));
        ingredientContainer.get(IngredientType.MILK).retrieve(beverageRecipe.getQuantity(IngredientType.MILK));
        ingredientContainer.get(IngredientType.TEA_LEAVES_SYRUP).retrieve(beverageRecipe.getQuantity(IngredientType.TEA_LEAVES_SYRUP));
        ingredientContainer.get(IngredientType.GINGER_SYRUP).retrieve(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP).retrieve(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    private void checkAvailability(BeverageType type)
            throws RequestQuantityNotPresentException, RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.GINGER_TEA )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " is not supported in " +
                    this.getClass().getSimpleName() +" machine.");

        checkHotWater();
        checkHotMilk();
        ingredientContainer.get(IngredientType.TEA_LEAVES_SYRUP).check(beverageRecipe.getQuantity(IngredientType.TEA_LEAVES_SYRUP));
        ingredientContainer.get(IngredientType.GINGER_SYRUP).check(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP).check(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    private void checkHotWater()
            throws RequestedQuantityNotSufficientException, RequestQuantityNotPresentException, BeverageTypeNotSupportedException {
        try {
            ingredientContainer.get(IngredientType.WATER).check(beverageRecipe.getQuantity(IngredientType.WATER));
        } catch (RequestQuantityNotPresentException rqnpe) {
            throw new RequestQuantityNotPresentException("hot_water is not available");
        } catch (RequestedQuantityNotSufficientException e) {
            throw new RequestedQuantityNotSufficientException("hot_water is not sufficient");
        }
    }

    private void checkHotMilk()
            throws RequestedQuantityNotSufficientException, RequestQuantityNotPresentException, BeverageTypeNotSupportedException {
        try {
            ingredientContainer.get(IngredientType.MILK).check(beverageRecipe.getQuantity(IngredientType.MILK));
        } catch (RequestQuantityNotPresentException rqnpe) {
            throw new RequestQuantityNotPresentException("hot_milk is not available");
        } catch (RequestedQuantityNotSufficientException e) {
            throw new RequestedQuantityNotSufficientException("hot_milk is not sufficient");
        }
    }

    @Override
    public int ingredientLevel(IngredientType type) {
        int level = 0;
        switch (type) {
            case WATER:             level = ingredientContainer.get(IngredientType.WATER).quantity();
                                    break;
            case MILK:              level = ingredientContainer.get(IngredientType.MILK).quantity();
                                    break;
            case TEA_LEAVES_SYRUP:  level = ingredientContainer.get(IngredientType.TEA_LEAVES_SYRUP).quantity();
                                    break;
            case GINGER_SYRUP:      level = ingredientContainer.get(IngredientType.GINGER_SYRUP).quantity();
                                    break;
            case SUGAR_SYRUP:       level = ingredientContainer.get(IngredientType.SUGAR_SYRUP).quantity();
                                    break;
        }
        return level;
    }

    @Override
    public synchronized void refillIngredient(IngredientType type, int amount) throws IncorrectIngredientTypeException {
        switch (type) {
            case WATER:             ingredientContainer.get(IngredientType.WATER).refill(amount);
                                    break;
            case MILK:              ingredientContainer.get(IngredientType.MILK).refill(amount);
                                    break;
            case TEA_LEAVES_SYRUP:  ingredientContainer.get(IngredientType.TEA_LEAVES_SYRUP).refill(amount);
                                    break;
            case GINGER_SYRUP:      ingredientContainer.get(IngredientType.GINGER_SYRUP).refill(amount);
                                    break;
            case SUGAR_SYRUP:       ingredientContainer.get(IngredientType.SUGAR_SYRUP).refill(amount);
                                    break;
            default:                throw new IncorrectIngredientTypeException("Refilling of Ingredient Type=" + type +
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
        if (ingredientContainer.get(IngredientType.TEA_LEAVES_SYRUP).quantity() < beverageRecipe.getQuantity(
                IngredientType.TEA_LEAVES_SYRUP)){
            ingredientTypeList.add(IngredientType.TEA_LEAVES_SYRUP);
        }
        if (ingredientContainer.get(IngredientType.GINGER_SYRUP).quantity() < beverageRecipe.getQuantity(
                IngredientType.GINGER_SYRUP)) {
            ingredientTypeList.add(IngredientType.GINGER_SYRUP);
        }
        if (ingredientContainer.get(IngredientType.SUGAR_SYRUP).quantity() < beverageRecipe.getQuantity(
                IngredientType.SUGAR_SYRUP)) {
            ingredientTypeList.add(IngredientType.SUGAR_SYRUP);
        }

        return ingredientTypeList;
    }

    public static class Builder {
        private int outlet;
        private Map<IngredientType, Ingredient> ingredientContainer = new HashMap<>();
        private BeverageComposition beverageRecipe;
        private Ingredient water;
        private Ingredient milk;
        private Ingredient teaLeavesSyrup;
        private Ingredient gingerSyrup;
        private Ingredient sugarSyrup;

        public Builder outlet(int outlet) {
            this.outlet = outlet;
            return this;
        }

        public Builder addIngredient(Ingredient ingredient) {
            switch(ingredient.type()) {
                case WATER:             water = ingredient;
                                        ingredientContainer.put(IngredientType.WATER, ingredient);
                                        break;
                case MILK:              milk = ingredient;
                                        ingredientContainer.put(IngredientType.MILK, ingredient);
                                        break;
                case TEA_LEAVES_SYRUP:  teaLeavesSyrup = ingredient;
                                        ingredientContainer.put(IngredientType.TEA_LEAVES_SYRUP, ingredient);
                                        break;
                case GINGER_SYRUP:      gingerSyrup = ingredient;
                                        ingredientContainer.put(IngredientType.GINGER_SYRUP, ingredient);
                                        break;
                case SUGAR_SYRUP:       sugarSyrup = ingredient;
                                        ingredientContainer.put(IngredientType.SUGAR_SYRUP, ingredient);
                                        break;
                default:                throw new IllegalArgumentException("cannot accept ingredient other than " +
                                        "[tea_leaves_syrup,ginger_syrup,sugar_syrup]");
            }
            return this;
        }

        public Builder addRecipe(BeverageComposition beverageComposition) {
            this.beverageRecipe = beverageComposition;
            return this;
        }

        public GingerTeaMachine build(){
            if (beverageRecipe == null || water == null || milk == null ||
                    teaLeavesSyrup == null || gingerSyrup == null || sugarSyrup == null)
                throw new IllegalArgumentException("argument for " + GingerTeaMachine.class.getSimpleName() +
                        " construction is not correct.");

            GingerTeaMachine gingerTeaMachine = new GingerTeaMachine(outlet);
            gingerTeaMachine.ingredientContainer = ingredientContainer;
            gingerTeaMachine.beverageRecipe = beverageRecipe;
            return gingerTeaMachine;
        }
    }


}
