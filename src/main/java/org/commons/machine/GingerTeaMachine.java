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

/**
 *  Ginger tea beverage machine which brews cups of ginger tea paralelly for
 *  outlet number of people
 *  Its works on water,milk,tea leaves syrup,ginger syrup, sugar syrup as its ingredient.
 */
public class GingerTeaMachine extends BaseBeverageMachine {

    private BeverageComposition beverageRecipe;
    private Map<IngredientType, IngredientContainer> ingredientContainer;

    private GingerTeaMachine(int outlet) {
        super(outlet);
    }

    @Override
    public synchronized void retrieveBeverageItems(BeverageType type) throws BeverageTypeNotSupportedException,
            RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
        if (type == null || type != BeverageType.GINGER_TEA )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " " +
                    BeverageOutputMessage.NOT_SUPPORTED + " in " + this.getClass().getSimpleName());

        checkAvailability();
        ingredientContainer.get(IngredientType.WATER)
                .retrieve(beverageRecipe.getQuantity(IngredientType.WATER));
        ingredientContainer.get(IngredientType.MILK)
                .retrieve(beverageRecipe.getQuantity(IngredientType.MILK));
        ingredientContainer.get(IngredientType.TEA_LEAVES_SYRUP)
                .retrieve(beverageRecipe.getQuantity(IngredientType.TEA_LEAVES_SYRUP));
        ingredientContainer.get(IngredientType.GINGER_SYRUP)
                .retrieve(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP)
                .retrieve(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    private void checkAvailability()
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        checkHotWater();
        checkHotMilk();
        ingredientContainer.get(IngredientType.TEA_LEAVES_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.TEA_LEAVES_SYRUP));
        ingredientContainer.get(IngredientType.GINGER_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
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
            throws RequestedQuantityNotSufficientException, RequestedQuantityNotPresentException, BeverageTypeNotSupportedException {
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
        private Map<IngredientType, IngredientContainer> ingredientContainer = new HashMap<>();
        private BeverageComposition beverageRecipe;
        private IngredientContainer water;
        private IngredientContainer milk;
        private IngredientContainer teaLeavesSyrup;
        private IngredientContainer gingerSyrup;
        private IngredientContainer sugarSyrup;

        public Builder outlet(int outlet) {
            this.outlet = outlet;
            return this;
        }

        public Builder addIngredient(IngredientContainer ingredientContainer) {
            switch(ingredientContainer.type()) {
                case WATER:             water = ingredientContainer;
                                        this.ingredientContainer.put(IngredientType.WATER, ingredientContainer);
                                        break;
                case MILK:              milk = ingredientContainer;
                                        this.ingredientContainer.put(IngredientType.MILK, ingredientContainer);
                                        break;
                case TEA_LEAVES_SYRUP:  teaLeavesSyrup = ingredientContainer;
                                        this.ingredientContainer.put(IngredientType.TEA_LEAVES_SYRUP, ingredientContainer);
                                        break;
                case GINGER_SYRUP:      gingerSyrup = ingredientContainer;
                                        this.ingredientContainer.put(IngredientType.GINGER_SYRUP, ingredientContainer);
                                        break;
                case SUGAR_SYRUP:       sugarSyrup = ingredientContainer;
                                        this.ingredientContainer.put(IngredientType.SUGAR_SYRUP, ingredientContainer);
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
