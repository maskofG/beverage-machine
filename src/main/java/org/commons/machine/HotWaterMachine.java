package org.commons.machine;

import org.commons.ingredients.IngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Hot water beverage machine which brews cups of hot water paralelly for
 * outlet number of people
 * Its works on water as its ingredient.
 *
 *   Hot water machine = Noutlet Base beverage machine +
 *                         hot water brewing module +
 *                         pluggable ingredient container for water.
 *
 *
 */
public class HotWaterMachine extends BaseBeverageMachine {
    /**
     * recipe for the beverage. It tells quantity of water in one brewed cup.
     */
    private BeverageComposition beverageRecipe;

    /**
     * container for storing water
     */
    private IngredientContainer waterContainer;

    /**
     * number of outlets of the hot water beverage machine
     * @param outlet
     */
    private HotWaterMachine(int outlet){
        super(outlet);
    }

    /**
     *  retrieve ingredients for hot water.
     *  if type is null or not hot water then it throws @{@link BeverageTypeNotSupportedException}
     *  method is thread-safe and consistency is maintained while retrieving
     *  ingredients parallely for different request.
     *
     * @param type can be one of the @{@link BeverageType}. But it only supports HOT_WATER and throws exception
     *             on any other value of type.
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     * @throws BeverageTypeNotSupportedException
     */
    @Override
    public synchronized void brew(BeverageType type) throws RequestedQuantityNotPresentException,
            RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.HOT_WATER )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " " + BeverageOutputMessage.NOT_SUPPORTED
                    + " in " + this.getClass().getSimpleName());
        checkAvailability();
        waterContainer.retrieve(beverageRecipe.getQuantity(IngredientType.WATER));
    }

    /**
     *  check availability of the ingredient in the container,
     *  generally checked before retrieving the ingredients.
     *  And if any of the ingredient is not present or is not insufficient, it throws either
     * of the two exceptions.
     * if quantity = 0, then it throws @{@link RequestedQuantityNotPresentException}
     * if quantity < required amount, then it throws @{@link RequestedQuantityNotSufficientException}
     *
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    private void checkAvailability()
            throws RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException{
        waterContainer.check(beverageRecipe.getQuantity(IngredientType.WATER));
    }

    /**
     * returns the quantity of the ingredient in the ingredient container
     *
     * @param type it is one of the @{@link IngredientType}.
     * @return  it will return 0 if quantity is other than WATER else it
     *          will return the quantity of WATER in the ingredient
     *          container
     */
    @Override
    public int ingredientLevel(IngredientType type) {
        if (type == null || type != IngredientType.WATER) return 0;
        return waterContainer.quantity();
    }

    /**
     * Refill of the supported ingredient type in the ingredient container
     *
     * @param type it is one of @{@link IngredientType}.
     *             if the ingredient type is other than WATER
     *             then @{@link IncorrectIngredientTypeException} is thrown
     * @param amount quantity of the ingredient being refilled
     * @throws IncorrectIngredientTypeException
     */
    @Override
    public synchronized void refillIngredient(IngredientType type, int amount)
            throws IncorrectIngredientTypeException{
        if (type == null || type != IngredientType.WATER)
            throw new IncorrectIngredientTypeException("Refill of Ingredient Type=" + type
                    + BeverageOutputMessage.NOT_SUPPORTED  + " in " + this.getClass().getSimpleName());
        waterContainer.refill(amount);
    }

    /**
     * List ingredients running low in the ingredient container.
     * If WATER is running low in the WATER container to brew
     * HOT_WATER then WATER will be added in the list
     *
     * @return list of ingredients running low in the ingredient
     *          container
     */
    @Override
    public List<IngredientType> ingredientsRunningLow() {
        List<IngredientType> ingr = new ArrayList<>();
        if (waterContainer.quantity() < beverageRecipe.getQuantity(IngredientType.WATER)) {
            ingr.add(IngredientType.WATER);
        }
        return ingr;
    }

    /**
     * Builder pattern to build Hot milk machine to abstract out
     * multiple compulsory fields in the constructor.
     */
    public static class Builder {
        private BeverageComposition beverageComposition;
        private IngredientContainer waterContainer;
        private int outlet;

        public Builder outlet(int outlet){
            this.outlet = outlet;
            return this;
        }

        public Builder waterContainer(IngredientContainer ingredientContainer){
            this.waterContainer = ingredientContainer;
            return this;
        }

        public Builder beverageComposition(BeverageComposition beverageComposition){
            this.beverageComposition = beverageComposition;
            return this;
        }

        public HotWaterMachine build(){
            if (waterContainer == null || beverageComposition == null) throw new IllegalArgumentException("argument for "
                    + HotWaterMachine.class.getSimpleName() + " construction is not correct.");
            HotWaterMachine hotWaterMachine = new HotWaterMachine(outlet);
            hotWaterMachine.waterContainer = waterContainer;
            hotWaterMachine.beverageRecipe = beverageComposition;
            return hotWaterMachine;
        }
    }
}
