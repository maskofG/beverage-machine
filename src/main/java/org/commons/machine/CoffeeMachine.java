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
 *  Hot coffee beverage machine which brews cups of hot coffee paralelly for
 *  outlet number of people
 *  Its works on water,milk,coffee syrup,sugar syrup as its ingredient.
 *
 *  CoffeeMachine = Noutlet Base beverage machine +
 *                  coffee brewing module +
 *                  pluggable ingredient container
 *                  for water,milk,coffee syrup and sugar syrup.
 *
 */
public class CoffeeMachine extends BaseBeverageMachine {

    /**
     * recipe for the beverage. It has quantity for each of
     * the ginger tea ingredients i.e water,milk,coffee syrup,sugar syrup
     *
     */
    private BeverageComposition beverageRecipe;

    /**
     * container for storing ingredients of hot coffee -
     * water,milk,coffee syrup, sugar syrup
     */
    private Map<IngredientType, IngredientContainer> ingredientContainer;

    /**
     * number of outlets of the hot coffee beverage machine
     * @param outlet
     */
    private CoffeeMachine(int outlet) {
        super(outlet);
    }

    /**
     *  retrieve ingredients for hot coffee.
     *  if type is null or not HOT_COFFEE then it throws
     *  @{@link BeverageTypeNotSupportedException}
     *  method is thread-safe and consistency is maintained while retrieving
     *  ingredients parallely for different request.
     *
     * @param type can be one of the @{@link BeverageType}.
     *             But it only supports HOT_COFFEE and throws exception
     *             on any other value of type.
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     * @throws BeverageTypeNotSupportedException
     */
    @Override
    public synchronized void brew(BeverageType type) throws BeverageTypeNotSupportedException,
            RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
        if (type == null || type != BeverageType.HOT_COFFEE )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " " +
                    BeverageOutputMessage.NOT_SUPPORTED + " in " + this.getClass().getSimpleName());

        checkAvailability();
        ingredientContainer.get(IngredientType.WATER)
                .retrieve(beverageRecipe.getQuantity(IngredientType.WATER));
        ingredientContainer.get(IngredientType.MILK)
                .retrieve(beverageRecipe.getQuantity(IngredientType.MILK));
        ingredientContainer.get(IngredientType.COFFEE_SYRUP)
                .retrieve(beverageRecipe.getQuantity(IngredientType.COFFEE_SYRUP));
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
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException{
        checkHotWater();
        checkHotMilk();
        ingredientContainer.get(IngredientType.COFFEE_SYRUP)
                .check(beverageRecipe.getQuantity(IngredientType.COFFEE_SYRUP));
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
     *          it needs to prepare hot coffee else it will return the quantity
     *          of the specified ingredient in the ingredient container
     */
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

    /**
     * Builder pattern to build hot coffee machine to abstract out
     * multiple compulsory fields in the constructor.
     */
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

        public Builder addIngredientContainer(IngredientContainer container) {
            switch(container.type()) {
                case WATER:         water = container;
                                    this.ingredientContainer.put(IngredientType.WATER, container);
                                    break;
                case MILK:          milk = container;
                                    this.ingredientContainer.put(IngredientType.MILK, container);
                                    break;
                case COFFEE_SYRUP:  coffeeSyrup = container;
                                    this.ingredientContainer.put(IngredientType.TEA_LEAVES_SYRUP, container);
                                    break;
                case SUGAR_SYRUP:   sugarSyrup = container;
                                    this.ingredientContainer.put(IngredientType.SUGAR_SYRUP, container);
                                    break;
                default:            throw new IllegalArgumentException("cannot accept ingredient other than " +
                                    "[coffee_syrup,sugar_syrup]");
            }
            return this;
        }

        public Builder addRecipe(BeverageComposition beverageRecipe) {
            this.beverageRecipe = beverageRecipe;
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
