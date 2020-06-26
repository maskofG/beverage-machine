package org.commons.machine;

import org.commons.ingredients.BeverageComposition;
import org.commons.ingredients.IngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.IncorrectIngredientTypeException;
import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

import java.util.ArrayList;
import java.util.List;

/**
 *  Hot milk beverage machine which brews cups of hot milk paralelly for
 *  outlet number of people
 *  Its works on milk as its ingredient.
 *
 *   Hot milk machine = Noutlet Base beverage machine +
 *                      hot milk brewing module +
 *                      pluggable ingredient container for milk.
 *
 */
public class HotMilkMachine extends BaseBeverageMachine {
    /**
     * recipe for the beverage. It tells quantity of milk in one brewed cup.
     */
    private BeverageComposition beverageRecipe;

    /**
     * container for storing milk
     */
    private IngredientContainer milkContainer;

    /**
     * number of outlets of the hot milk beverage machine
     * @param outlet
     */
    private HotMilkMachine(int outlet){
        super(outlet);
    }

    /**
     *  retrieve ingredients for hot milk.
     *  if type is null or not hot milk then it throws
     *  @{@link BeverageTypeNotSupportedException}
     *  method is thread-safe and consistency is maintained while retrieving
     *  ingredients parallely for different request.
     *
     * @param type can be one of the @{@link BeverageType}.
     *             But it only supports HOT_MILK and throws exception
     *             on any other value of type.
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     * @throws BeverageTypeNotSupportedException
     */
    @Override
    public synchronized void brew(BeverageType type)
            throws RequestedQuantityNotPresentException,
            RequestedQuantityNotSufficientException, BeverageTypeNotSupportedException {
        if (type == null || type != BeverageType.HOT_MILK )
            throw new BeverageTypeNotSupportedException("BeverageType="+ type + " " +
                    BeverageOutputMessage.NOT_SUPPORTED + " in " + this.getClass().getSimpleName());
        checkAvailability();
        milkContainer.retrieve(beverageRecipe.getQuantity(IngredientType.MILK));
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
        milkContainer.check(beverageRecipe.getQuantity(IngredientType.MILK));
    }

    /**
     * returns the quantity of the ingredient in the ingredient container
     *
     * @param type it is one of the @{@link IngredientType}.
     * @return  it will return 0 if quantity is other than MILK else it
     *          will return the quantity of MILK in the ingredient
     *          container
     */
    @Override
    public int ingredientLevel(IngredientType type) {
        if (type == null || type != IngredientType.MILK) return 0;

        return milkContainer.quantity();
    }

    /**
     * Refill of the supported ingredient type in the ingredient container
     *
     * @param type it is one of @{@link IngredientType}.
     *             if the ingredient type is other than MILK
     *             then @{@link IncorrectIngredientTypeException} is thrown
     * @param amount quantity of the ingredient being refilled
     * @throws IncorrectIngredientTypeException
     */
    @Override
    public void refillIngredient(IngredientType type, int amount) throws IncorrectIngredientTypeException {
        if (type == null || type != IngredientType.MILK)
            throw new IncorrectIngredientTypeException("Refill of Ingredient Type=" + type
                    + BeverageOutputMessage.NOT_SUPPORTED  + " in " + this.getClass().getSimpleName());

        milkContainer.refill(amount);
    }

    /**
     * List ingredients running low in the ingredient container.
     * If MILK is running low in the MILK container to brew
     * HOT_MILK then MILK will be added in the list
     *
     * @return list of ingredients running low in the ingredient
     *          container
     */
    @Override
    public List<IngredientType> ingredientsRunningLow() {
        List<IngredientType> ingr = new ArrayList<>();
        if (milkContainer.quantity() < beverageRecipe.getQuantity(IngredientType.MILK)) {
            ingr.add(IngredientType.MILK);
        }
        return ingr;
    }

    /**
     * Builder pattern to build Hot milk machine to abstract out
     * multiple compulsory fields in the constructor.
     */
    public static class Builder {
        private BeverageComposition beverageComposition;
        private IngredientContainer milkContainer;
        private int outlet;

        public Builder outlet(int outlet){
            this.outlet = outlet;
            return this;
        }

        public Builder milkContainer(IngredientContainer ingredientContainer){
            this.milkContainer = ingredientContainer;
            return this;
        }

        public Builder beverageComposition(BeverageComposition beverageComposition){
            this.beverageComposition = beverageComposition;
            return this;
        }

        public HotMilkMachine build(){
            if (milkContainer == null || beverageComposition == null)
                throw new IllegalArgumentException("argument for " + HotMilkMachine.class.getSimpleName() +
                        " construction is not correct.");

            HotMilkMachine hotMilkMachine = new HotMilkMachine(outlet);
            hotMilkMachine.milkContainer = milkContainer;
            hotMilkMachine.beverageRecipe = beverageComposition;
            return hotMilkMachine;
        }
    }
}
