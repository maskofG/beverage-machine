package org.commons.machine;

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
 *  Green tea beverage machine which brews cups of green tea paralelly for
 *  outlet number of people
 *  Its works on water,green mixture,ginger syrup,sugar syrup as its ingredient.
 *
 *   Green tea machine = Noutlet Base beverage machine +
 *                       green tea brewing module +
 *                       pluggable ingredient container for water,
 *                       green_mixture,ginger syrup,sugar syrup.
 *
 */
public class GreenTeaMachine extends BaseBeverageMachine {
    /**
     * recipe for the beverage. It has quantity for each of
     * the greeen tea ingredients i.e water,green mixture,
     * ginger syrup,sugar syrup
     *
     */
    private BeverageComposition beverageRecipe;

    /**
     * container for storing ingredients of green tea -
     * water,green mixture,ginger syrup,sugar syrup
     */
    private Map<IngredientType, IngredientContainer> ingredientContainer;

    /**
     * number of outlets of the green tea beverage machine
     * @param outlet
     */
    private GreenTeaMachine(int outlet) {
        super(outlet);
    }

    /**
     *  retrieve ingredients for green tea.
     *  if type is null or not green tea then it throws
     *  @{@link BeverageTypeNotSupportedException}
     *  method is thread-safe and consistency is maintained while retrieving
     *  ingredients parallely for different request.
     *
     * @param type can be one of the @{@link BeverageType}.
     *             But it only supports GREEN_TEA and throws exception
     *             on any other value of type.
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     * @throws BeverageTypeNotSupportedException
     */
    @Override
    public synchronized void brew(BeverageType type) throws RequestedQuantityNotPresentException,
            RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.GREEN_TEA )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " " +
                    BeverageOutputMessage.NOT_SUPPORTED + " in " + this.getClass().getSimpleName());

        checkAvailability();
        ingredientContainer.get(IngredientType.WATER)
                .retrieve(beverageRecipe.getQuantity(IngredientType.WATER));
        ingredientContainer.get(IngredientType.GREEN_MIXTURE)
                .retrieve(beverageRecipe.getQuantity(IngredientType.GREEN_MIXTURE));
        ingredientContainer.get(IngredientType.GINGER_SYRUP)
                .retrieve(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP)
                .retrieve(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    /**
     *  check availability of the ingredient in the container,
     *  generally checked before retrieving the ingredients.
     *  And if any of the ingredient is not present or is not insufficient,
     *  it throws either of the two exceptions.
     * if quantity = 0, then it throws @{@link RequestedQuantityNotPresentException}
     * if quantity < required amount, then it throws @{@link RequestedQuantityNotSufficientException}
     *
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    private void checkAvailability()
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
        checkHotWater();
        ingredientContainer.get(IngredientType.GREEN_MIXTURE)
                .check(beverageRecipe.getQuantity(IngredientType.GREEN_MIXTURE));
        ingredientContainer.get(IngredientType.GINGER_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    /**
     *  check availability of water in the container, generally checked while checking other ingredients.
     *  And if it is not present or is not insufficient, it throws either
     * of the two exceptions.
     * if quantity = 0, then it throws @{@link RequestedQuantityNotPresentException}
     * if quantity < required amount, then it throws @{@link RequestedQuantityNotSufficientException}
     *
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    private void checkHotWater()
            throws RequestedQuantityNotSufficientException, RequestedQuantityNotPresentException {
        try {
            ingredientContainer.get(IngredientType.WATER).check(beverageRecipe.getQuantity(IngredientType.WATER));
        } catch (RequestedQuantityNotPresentException rqnpe) {
            throw new RequestedQuantityNotPresentException("hot_water is " + BeverageOutputMessage.QTY_NA);
        } catch (RequestedQuantityNotSufficientException e) {
            throw new RequestedQuantityNotSufficientException("hot_water is " + BeverageOutputMessage.QTY_NS);
        }
    }

    /**
     * returns the quantity of the ingredient in the ingredient container
     *
     * @param type it is one of the @{@link IngredientType}.
     * @return  it will return 0 if quantity is other than core ingredient
     *          it needs to prepare green tea else it will return the quantity
     *          of the specified ingredient in the ingredient container
     */
    @Override
    public int ingredientLevel(IngredientType type) {
        if (type == null ) return 0;
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

    /**
     * Refill of the supported ingredient type in the ingredient container
     *
     * @param type it is one of @{@link IngredientType}.
     *             if the ingredient type is other than the core ingredients
     *             it need then @{@link IncorrectIngredientTypeException} is thrown
     * @param amount quantity of the ingredient being refilled
     * @throws IncorrectIngredientTypeException
     */
    @Override
    public synchronized void refillIngredient(IngredientType type, int amount)
            throws IncorrectIngredientTypeException {
        if (type == null )
            throw new IncorrectIngredientTypeException("Refill of Ingredient Type=" + type +
                    BeverageOutputMessage.NOT_SUPPORTED  + " in " + this.getClass().getSimpleName());

        switch (type) {
            case WATER:         ingredientContainer.get(IngredientType.WATER).refill(amount);
                                break;
            case GREEN_MIXTURE: ingredientContainer.get(IngredientType.GREEN_MIXTURE).refill(amount);
                                break;
            case GINGER_SYRUP:  ingredientContainer.get(IngredientType.GINGER_SYRUP).refill(amount);
                                break;
            case SUGAR_SYRUP:   ingredientContainer.get(IngredientType.SUGAR_SYRUP).refill(amount);
                                break;
            default:            throw new IncorrectIngredientTypeException("Refill of Ingredient Type=" + type +
                                BeverageOutputMessage.NOT_SUPPORTED  + " in " + this.getClass().getSimpleName());
        }
    }

    /**
     * List ingredients running low in the ingredient container.
     * It can be either of the core ingredients which is required
     * in preparing the beverage and is running low on quantity.
     * Running low is defined as the quantity which is not sufficient
     * to prepare a beverage.
     *
     * @return list of ingredients running low in the ingredient
     *          container
     */
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

    /**
     * Builder pattern to build Green tea machine to abstract out
     * multiple compulsory fields in the constructor.
     */
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
