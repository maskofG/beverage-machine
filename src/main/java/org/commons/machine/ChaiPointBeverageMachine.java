package org.commons.machine;

import org.commons.ingredients.IngredientType;
import org.exceptions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *  Chai point beverage machine which brews cups of -
 *      1. hot water
 *      2. hot milk
 *      3. green tea
 *      4. ginger tea
 *      5. elaichi tea
 *      6. hot coffee
 *  paralelly for outlet number of people
 *  Its works on following ingredient -
 *      1. water
 *      2. milk
 *      3. green mixture
 *      4. tea leaves syrup
 *      5. ginger syrup
 *      6. elaichi syrup
 *      7. coffee syrup
 *      8. sugar syrup
 *
 *  ChaiPoint Machine = hot water brewing module +
 *                  hot milk brewing module +
 *                  green tea brewing module +
 *                  ginger tea brewing module +
 *                  elaichi tea brewing module +
 *                  hot coffee brewing module
 *                  coffee brewing module +
 *                  pluggable ingredient container
 *                  for water,milk,green mixture,
 *                  leaves syrup,ginger syrup,
 *                  elaichi syrup, coffee syrup and sugar syrup.
 *
 */
public class ChaiPointBeverageMachine extends BaseBeverageMachine {

    private HotWaterMachine hotWaterMachine;
    private HotMilkMachine hotMilkMachine;
    private GreenTeaMachine greenTeaMachine;
    private GingerTeaMachine gingerTeaMachine;
    private ElaichiTeaMachine elaichiTeaMachine;
    private CoffeeMachine coffeeMachine;

    private ChaiPointBeverageMachine(int outlet){
        super(outlet);
    }

    @Override
    public synchronized void brew(BeverageType type) throws BeverageTypeNotSupportedException,
            RequestedQuantityNotPresentException, RequestedQuantityNotSufficientException {
        if (type == null)
            throw new BeverageTypeNotSupportedException("BeverageType=null "+
                    BeverageOutputMessage.NOT_SUPPORTED + " in " + this.getClass().getSimpleName());

        switch (type) {
            case HOT_WATER:     hotWaterMachine.brew(BeverageType.HOT_WATER);
                                break;
            case HOT_MILK:      hotMilkMachine.brew(BeverageType.HOT_MILK);
                                break;
            case GREEN_TEA:     greenTeaMachine.brew(BeverageType.GREEN_TEA);
                                break;
            case GINGER_TEA:    gingerTeaMachine.brew(BeverageType.GINGER_TEA);
                                break;
            case ELAICHI_TEA:   elaichiTeaMachine.brew(BeverageType.ELAICHI_TEA);
                                break;
            case HOT_COFFEE:    coffeeMachine.brew(BeverageType.HOT_COFFEE);
                                break;
            default:            throw new BeverageTypeNotSupportedException("BeverageType=null "+
                                BeverageOutputMessage.NOT_SUPPORTED + " in " + this.getClass().getSimpleName());
        }
    }

    @Override
    public int ingredientLevel(IngredientType type) {
        if (type == null ) return 0;

        int level = 0;
        switch (type) {
            case WATER:             level = hotWaterMachine.ingredientLevel(IngredientType.WATER);
                                    break;
            case MILK:              level = hotMilkMachine.ingredientLevel(IngredientType.MILK);
                                    break;
            case GREEN_MIXTURE:     level = greenTeaMachine.ingredientLevel(IngredientType.GREEN_MIXTURE);
                                    break;
            case TEA_LEAVES_SYRUP:  level = gingerTeaMachine.ingredientLevel(IngredientType.TEA_LEAVES_SYRUP);
                                    break;
            case GINGER_SYRUP:      level = gingerTeaMachine.ingredientLevel(IngredientType.GINGER_SYRUP);
                                    break;
            case ELAICHI_SYRUP:     level = elaichiTeaMachine.ingredientLevel(IngredientType.ELAICHI_SYRUP);
                                    break;
            case COFFEE_SYRUP:      level = coffeeMachine.ingredientLevel(IngredientType.COFFEE_SYRUP);
                                    break;
            case SUGAR_SYRUP:       level = gingerTeaMachine.ingredientLevel(IngredientType.SUGAR_SYRUP);
                                    break;
        }
        return level;
    }

    @Override
    public synchronized void refillIngredient(IngredientType type, int amount) throws IncorrectIngredientTypeException {
        if (type == null)
            throw new IncorrectIngredientTypeException("Refill of Ingredient Type=" + type +
                    BeverageOutputMessage.NOT_SUPPORTED  + " in " + this.getClass().getSimpleName());

        switch (type) {
            case WATER:             hotWaterMachine.refillIngredient(IngredientType.WATER, amount);
                                    break;
            case MILK:              hotMilkMachine.refillIngredient(IngredientType.MILK, amount);
                                    break;
            case GREEN_MIXTURE:     greenTeaMachine.refillIngredient(IngredientType.GREEN_MIXTURE, amount);
                                    break;
            case TEA_LEAVES_SYRUP:  gingerTeaMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, amount);
                                    break;
            case GINGER_SYRUP:      gingerTeaMachine.refillIngredient(IngredientType.GINGER_SYRUP, amount);
                                    break;
            case ELAICHI_SYRUP:     elaichiTeaMachine.refillIngredient(IngredientType.ELAICHI_SYRUP, amount);
                                    break;
            case COFFEE_SYRUP:      coffeeMachine.refillIngredient(IngredientType.COFFEE_SYRUP, amount);
                                    break;
            case SUGAR_SYRUP:       gingerTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, amount);
                                    break;
            default:                new IncorrectIngredientTypeException("Refill of Ingredient Type=" + type +
                                    BeverageOutputMessage.NOT_SUPPORTED  + " in " + this.getClass().getSimpleName());
        }
    }

    @Override
    public List<IngredientType> ingredientsRunningLow() {
        HashSet ingrSet = new HashSet();
        ingrSet.addAll(greenTeaMachine.ingredientsRunningLow());
        ingrSet.addAll(gingerTeaMachine.ingredientsRunningLow());
        ingrSet.addAll(elaichiTeaMachine.ingredientsRunningLow());
        ingrSet.addAll(coffeeMachine.ingredientsRunningLow());
        List<IngredientType> ingrList = new ArrayList<>(ingrSet);
        return ingrList;
    }

    public static class Builder {
        private int outlet;
        private HotWaterMachine hotWaterMachine;
        private HotMilkMachine hotMilkMachine;
        private GreenTeaMachine greenTeaMachine;
        private GingerTeaMachine gingerTeaMachine;
        private ElaichiTeaMachine elaichiTeaMachine;
        private CoffeeMachine coffeeMachine;

        public Builder outlet(int outlet) {
            this.outlet = outlet;
            return this;
        }

        public Builder addMachine(HotWaterMachine hotWaterMachine) {
            this.hotWaterMachine = hotWaterMachine;
            return this;
        }

        public Builder addMachine(HotMilkMachine hotMilkMachine) {
            this.hotMilkMachine = hotMilkMachine;
            return this;
        }

        public Builder addMachine(GreenTeaMachine greenTeaMachine){
            this.greenTeaMachine = greenTeaMachine;
            return this;
        }

        public Builder addMachine(GingerTeaMachine gingerTeaMachine){
            this.gingerTeaMachine = gingerTeaMachine;
            return this;
        }

        public Builder addMachine(ElaichiTeaMachine elaichiTeaMachine) {
            this.elaichiTeaMachine = elaichiTeaMachine;
            return this;
        }

        public Builder addMachine(CoffeeMachine coffeeMachine){
            this.coffeeMachine = coffeeMachine;
            return this;
        }

        public ChaiPointBeverageMachine build(){
            if (hotWaterMachine == null || hotMilkMachine == null || greenTeaMachine == null ||
                    gingerTeaMachine == null || elaichiTeaMachine == null || coffeeMachine == null)
                throw new IllegalArgumentException("argument for " + GreenTeaMachine.class.getSimpleName() +
                        " construction is not correct.");

            ChaiPointBeverageMachine chaiPointBeverageMachine = new ChaiPointBeverageMachine(outlet);
            chaiPointBeverageMachine.hotWaterMachine = hotWaterMachine;
            chaiPointBeverageMachine.hotMilkMachine = hotMilkMachine;
            chaiPointBeverageMachine.greenTeaMachine = greenTeaMachine;
            chaiPointBeverageMachine.gingerTeaMachine = gingerTeaMachine;
            chaiPointBeverageMachine.elaichiTeaMachine = elaichiTeaMachine;
            chaiPointBeverageMachine.coffeeMachine = coffeeMachine;
            return chaiPointBeverageMachine;
        }
    }
}
