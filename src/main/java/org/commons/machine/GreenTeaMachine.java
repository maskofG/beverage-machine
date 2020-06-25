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
 *
 */
public class GreenTeaMachine extends BaseBeverageMachine {

    private BeverageComposition beverageRecipe;
    private Map<IngredientType, IngredientContainer> ingredientContainer;
    private HotWaterMachine hotWaterMachine;

    private GreenTeaMachine(int outlet) {
        super(outlet);
    }

    @Override
    public synchronized void retrieveBeverageItems(BeverageType type) throws RequestedQuantityNotPresentException,
            RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.GREEN_TEA )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " is not supported in " +
                    this.getClass().getSimpleName() +" machine.");

        checkAvailability(type);
        ingredientContainer.get(IngredientType.WATER).retrieve(beverageRecipe.getQuantity(IngredientType.WATER));
        ingredientContainer.get(IngredientType.GREEN_MIXTURE).retrieve(beverageRecipe.getQuantity(IngredientType.GREEN_MIXTURE));
        ingredientContainer.get(IngredientType.GINGER_SYRUP).retrieve(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP).retrieve(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    private void checkAvailability(BeverageType type)
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.GREEN_TEA )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " is not supported in " +
                    this.getClass().getSimpleName() +" machine.");

        checkHotWater();
        ingredientContainer.get(IngredientType.GREEN_MIXTURE).check(beverageRecipe.getQuantity(IngredientType.GREEN_MIXTURE));
        ingredientContainer.get(IngredientType.GINGER_SYRUP).check(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP).check(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    private void checkHotWater()
            throws RequestedQuantityNotSufficientException, RequestedQuantityNotPresentException, BeverageTypeNotSupportedException {
        try {
            ingredientContainer.get(IngredientType.WATER).check(beverageRecipe.getQuantity(IngredientType.WATER));
        } catch (RequestedQuantityNotPresentException rqnpe) {
            throw new RequestedQuantityNotPresentException("hot_water is not available");
        } catch (RequestedQuantityNotSufficientException e) {
            throw new RequestedQuantityNotSufficientException("hot_water is not sufficient");
        }
    }

    @Override
    public int ingredientLevel(IngredientType type) {
        int level = 0;
        switch (type) {
            case WATER:         level = ingredientContainer.get(IngredientType.WATER).quantity();
                                break;
            case GREEN_MIXTURE: level = ingredientContainer.get(IngredientType.GREEN_MIXTURE).quantity();
                                break;
            case GINGER_SYRUP:  level = ingredientContainer.get(IngredientType.GINGER_SYRUP).quantity();
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
            case GREEN_MIXTURE: ingredientContainer.get(IngredientType.GREEN_MIXTURE).refill(amount);
                                break;
            case GINGER_SYRUP:  ingredientContainer.get(IngredientType.GINGER_SYRUP).refill(amount);
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
        if (ingredientContainer.get(IngredientType.GREEN_MIXTURE).quantity() < beverageRecipe.getQuantity(
                IngredientType.GREEN_MIXTURE)){
            ingredientTypeList.add(IngredientType.GREEN_MIXTURE);
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
        private IngredientContainer waterContainer;
        private IngredientContainer greenMixtureContainer;
        private IngredientContainer gingerSyrupContainer;
        private IngredientContainer sugarSyrupContainer;

        public Builder outlet(int outlet) {
            this.outlet = outlet;
            return this;
        }

        public Builder addIngredientContainer(IngredientContainer container) {
            switch(container.type()) {
                case WATER:         waterContainer = container;
                                    this.ingredientContainer.put(IngredientType.WATER, container);
                                    break;
                case GREEN_MIXTURE: greenMixtureContainer = container;
                                    this.ingredientContainer.put(IngredientType.GREEN_MIXTURE, container);
                                    break;
                case GINGER_SYRUP:  gingerSyrupContainer = container;
                                    this.ingredientContainer.put(IngredientType.GINGER_SYRUP, container);
                                    break;
                case SUGAR_SYRUP:   sugarSyrupContainer = container;
                                    this.ingredientContainer.put(IngredientType.SUGAR_SYRUP, container);
                                    break;
                default: throw new IllegalArgumentException("cannot accept ingredient other than " +
                        "[water,green_mixture,ginger_syrup,sugar_syrup]");
            }
            return this;
        }

        public Builder addRecipe(BeverageComposition beverageRecipe) {
            this.beverageRecipe = beverageRecipe;
            return this;
        }

        public GreenTeaMachine build(){
            if (beverageRecipe == null || waterContainer == null || greenMixtureContainer == null ||
                    gingerSyrupContainer == null || sugarSyrupContainer == null)
                throw new IllegalArgumentException("argument for " + GreenTeaMachine.class.getSimpleName() +
                        " construction is not correct.");

            GreenTeaMachine greenTeaMachine = new GreenTeaMachine(outlet);
            greenTeaMachine.beverageRecipe = beverageRecipe;
            greenTeaMachine.ingredientContainer = ingredientContainer;
            return greenTeaMachine;
        }
    }
}
