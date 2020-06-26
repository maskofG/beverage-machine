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
 *  Ginger tea beverage machine which brews cups of ginger tea paralelly for
 *  outlet number of people
 *  Its works on water,milk,tea leaves syrup,ginger syrup, sugar syrup as its ingredient.
 *
 *   Ginger tea machine = Noutlet Base beverage machine +
 *                         ginger tea brewing module +
 *                         pluggable ingredient container for water,milk,
 *                         tea leaves syrup,ginger syrup and sugar syrup.
 *
 */
public class GingerTeaMachine extends BaseBeverageMachine {

    /**
     * recipe for the beverage. It has quantity for each of
     * the ginger tea ingredients i.e water,milk,tea leaves
     * syrup,ginger syrup,sugar syrup
     *
     */
    private BeverageComposition beverageRecipe;

    /**
     * container for storing ingredients of ginger tea -
     * water,milk,tea leaves syrup,ginger syrup,sugar syrup
     */
    private Map<IngredientType, IngredientContainer> ingredientContainer;

    /**
     * number of outlets of the ginger tea beverage machine
     * @param outlet
     */
    private GingerTeaMachine(int outlet) {
        super(outlet);
    }

    /**
     *  retrieve ingredients for ginger tea.
     *  if type is null or not ginger tea then it throws
     *  @{@link BeverageTypeNotSupportedException}
     *  method is thread-safe and consistency is maintained while retrieving
     *  ingredients parallely for different request.
     *
     * @param type can be one of the @{@link BeverageType}.
     *             But it only supports GINGER_TEA and throws exception
     *             on any other value of type.
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     * @throws BeverageTypeNotSupportedException
     */
    @Override
    public synchronized void brew(BeverageType type) throws BeverageTypeNotSupportedException,
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
        checkHotMilk();
        ingredientContainer.get(IngredientType.TEA_LEAVES_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.TEA_LEAVES_SYRUP));
        ingredientContainer.get(IngredientType.GINGER_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.GINGER_SYRUP));
        ingredientContainer.get(IngredientType.SUGAR_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.SUGAR_SYRUP));
    }

    /**
     *  check availability of water in the container, generally checked while checking other ingredients.
     *  And if it is not present or is not insufficient, it throws either
     *  of the two exceptions.
     *  if quantity = 0, then it throws @{@link RequestedQuantityNotPresentException}
     *  if quantity < required amount, then it throws @{@link RequestedQuantityNotSufficientException}
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
     *  check availability of milk in the container, generally checked while checking other ingredients.
     *  And if it is not present or is not insufficient, it throws either
     * of the two exceptions.
     * if quantity = 0, then it throws @{@link RequestedQuantityNotPresentException}
     * if quantity < required amount, then it throws @{@link RequestedQuantityNotSufficientException}
     *
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    private void checkHotMilk()
            throws RequestedQuantityNotSufficientException, RequestedQuantityNotPresentException {
        try {
            ingredientContainer.get(IngredientType.MILK).check(beverageRecipe.getQuantity(IngredientType.MILK));
        } catch (RequestedQuantityNotPresentException rqnpe) {
            throw new RequestedQuantityNotPresentException("hot_milk is " + BeverageOutputMessage.QTY_NA);
        } catch (RequestedQuantityNotSufficientException e) {
            throw new RequestedQuantityNotSufficientException("hot_milk is " + BeverageOutputMessage.QTY_NS);
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
            default:                throw new IncorrectIngredientTypeException("Refill of Ingredient Type=" + type +
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

    /**
     * Builder pattern to build Ginger tea machine to abstract out
     * multiple compulsory fields in the constructor.
     */
    public static class Builder {
        private int outlet;
        private Map<IngredientType, IngredientContainer> ingredientContainer = new HashMap<>();
        private BeverageComposition beverageRecipe;
        private IngredientContainer waterContainer;
        private IngredientContainer milkContainer;
        private IngredientContainer teaLeavesSyrupContainer;
        private IngredientContainer gingerSyrupContainer;
        private IngredientContainer sugarSyrupContainer;

        public Builder outlet(int outlet) {
            this.outlet = outlet;
            return this;
        }

        public Builder addIngredient(IngredientContainer container) {
            switch(container.type()) {
                case WATER:             waterContainer = container;
                                        this.ingredientContainer.put(IngredientType.WATER, container);
                                        break;
                case MILK:              milkContainer = container;
                                        this.ingredientContainer.put(IngredientType.MILK, container);
                                        break;
                case TEA_LEAVES_SYRUP:  teaLeavesSyrupContainer = container;
                                        this.ingredientContainer.put(IngredientType.TEA_LEAVES_SYRUP, container);
                                        break;
                case GINGER_SYRUP:      gingerSyrupContainer = container;
                                        this.ingredientContainer.put(IngredientType.GINGER_SYRUP, container);
                                        break;
                case SUGAR_SYRUP:       sugarSyrupContainer = container;
                                        this.ingredientContainer.put(IngredientType.SUGAR_SYRUP, container);
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
            if (beverageRecipe == null || waterContainer == null || milkContainer == null ||
                    teaLeavesSyrupContainer == null || gingerSyrupContainer == null || sugarSyrupContainer == null)
                throw new IllegalArgumentException("argument for " + GingerTeaMachine.class.getSimpleName() +
                        " construction is not correct.");

            GingerTeaMachine gingerTeaMachine = new GingerTeaMachine(outlet);
            gingerTeaMachine.ingredientContainer = ingredientContainer;
            gingerTeaMachine.beverageRecipe = beverageRecipe;
            return gingerTeaMachine;
        }
    }


}
