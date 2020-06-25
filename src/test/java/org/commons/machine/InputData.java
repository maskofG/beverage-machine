package org.commons.machine;

import org.commons.ingredients.BeverageComposition;
import org.commons.ingredients.ConcreteIngredientContainer;
import org.commons.ingredients.IngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;


public class InputData {
    private class Machine {
        private class OutletInfo {
            private int count_n;
        }

        private class IngredientInfo {
            private int hot_water;
            private int hot_milk;
            private int ginger_syrup;
            private int sugar_syrup;
            private int tea_leaves_syrup;
            private int green_mixture;
            private int elaichi_syrup;
            private int coffee_syrup;
        }

        private class BeverageInfo {
            private int water;
            private int milk;
            private int hot_water;
            private int hot_milk;
            private int green_mixture;
            private int tea_leaves_syrup;
            private int ginger_syrup;
            private int coffe_syrup;
            private int elaichi_syrup;
            private int sugar_syrup;
        }

        private class Beverages {
            private BeverageInfo ginger_tea;
            private BeverageInfo hot_coffee;
            private BeverageInfo elaichi_tea;
            private BeverageInfo green_tea;
            private BeverageInfo hot_water;
            private BeverageInfo hot_milk;
        }

        private OutletInfo outlets;
        private IngredientInfo total_items_quantity;
        private Beverages beverages;
    }
    private Machine machine;

    public int getOutlet(){
        return machine.outlets.count_n;
    }

    public IngredientContainer buildIngredientContainer(IngredientType type) {
        Machine.IngredientInfo ingredientInfo = machine.total_items_quantity;
        ConcreteIngredientContainer ingrContainer;
        switch (type) {
            case WATER:         ingrContainer = new ConcreteIngredientContainer(IngredientType.WATER, ingredientInfo.hot_water);
                                return ingrContainer;
            case MILK:          ingrContainer = new ConcreteIngredientContainer(IngredientType.MILK, ingredientInfo.hot_milk);
                                return ingrContainer;
            case GREEN_MIXTURE: ingrContainer = new ConcreteIngredientContainer(IngredientType.GREEN_MIXTURE,
                                ingredientInfo.green_mixture);
                                return ingrContainer;
            case TEA_LEAVES_SYRUP:  ingrContainer = new ConcreteIngredientContainer(IngredientType.TEA_LEAVES_SYRUP,
                                    ingredientInfo.tea_leaves_syrup);
                                    return ingrContainer;
            case GINGER_SYRUP:  ingrContainer = new ConcreteIngredientContainer(IngredientType.GINGER_SYRUP,
                                ingredientInfo.ginger_syrup);
                                return ingrContainer;
            case ELAICHI_SYRUP: ingrContainer = new ConcreteIngredientContainer(IngredientType.ELAICHI_SYRUP,
                                ingredientInfo.elaichi_syrup);
                                return ingrContainer;
            case COFFEE_SYRUP:  ingrContainer = new ConcreteIngredientContainer(IngredientType.COFFEE_SYRUP,
                                ingredientInfo.coffee_syrup);
                                return ingrContainer;
            case SUGAR_SYRUP:   ingrContainer = new ConcreteIngredientContainer(IngredientType.SUGAR_SYRUP,
                                ingredientInfo.sugar_syrup);
                                return ingrContainer;

            default:            throw new IllegalArgumentException("incorrect ingredient type");
        }
    }

    public BeverageComposition buildBeverageComposition(BeverageType type) throws BeverageTypeNotSupportedException {
        Machine.Beverages beverages = machine.beverages;
        BeverageComposition beverageComposition;
        switch (type) {
            case HOT_WATER:     beverageComposition = new BeverageComposition();
                                beverageComposition.put(IngredientType.WATER, beverages.hot_water.water);
                                return beverageComposition;
            case HOT_MILK:      beverageComposition = new BeverageComposition();
                                beverageComposition.put(IngredientType.MILK, beverages.hot_milk.milk);
                                return beverageComposition;
            case GREEN_TEA:     beverageComposition = new BeverageComposition();
                                beverageComposition.put(IngredientType.WATER, beverages.green_tea.hot_water);
                                beverageComposition.put(IngredientType.GREEN_MIXTURE, beverages.green_tea.green_mixture);
                                beverageComposition.put(IngredientType.GINGER_SYRUP, beverages.green_tea.ginger_syrup);
                                beverageComposition.put(IngredientType.SUGAR_SYRUP, beverages.green_tea.sugar_syrup);
                                return beverageComposition;
            case GINGER_TEA:    beverageComposition = new BeverageComposition();
                                beverageComposition.put(IngredientType.WATER, beverages.ginger_tea.hot_water);
                                beverageComposition.put(IngredientType.MILK, beverages.ginger_tea.hot_milk);
                                beverageComposition.put(IngredientType.TEA_LEAVES_SYRUP, beverages.ginger_tea.tea_leaves_syrup);
                                beverageComposition.put(IngredientType.GINGER_SYRUP, beverages.ginger_tea.ginger_syrup);
                                beverageComposition.put(IngredientType.SUGAR_SYRUP, beverages.ginger_tea.sugar_syrup);
                                return beverageComposition;
            case ELAICHI_TEA:   beverageComposition = new BeverageComposition();
                                beverageComposition.put(IngredientType.WATER, beverages.elaichi_tea.hot_water);
                                beverageComposition.put(IngredientType.MILK, beverages.elaichi_tea.hot_milk);
                                beverageComposition.put(IngredientType.TEA_LEAVES_SYRUP, beverages.elaichi_tea.tea_leaves_syrup);
                                beverageComposition.put(IngredientType.ELAICHI_SYRUP, beverages.elaichi_tea.elaichi_syrup);
                                beverageComposition.put(IngredientType.SUGAR_SYRUP, beverages.elaichi_tea.sugar_syrup);
                                return beverageComposition;
            case HOT_COFFEE:    beverageComposition = new BeverageComposition();
                                beverageComposition.put(IngredientType.WATER, beverages.hot_coffee.hot_water);
                                beverageComposition.put(IngredientType.MILK, beverages.hot_coffee.hot_milk);
                                beverageComposition.put(IngredientType.COFFEE_SYRUP, beverages.hot_coffee.coffe_syrup);
                                beverageComposition.put(IngredientType.SUGAR_SYRUP, beverages.hot_coffee.sugar_syrup);
                                return beverageComposition;
            default:            throw new BeverageTypeNotSupportedException("Beverage Type=" + type +
                                " is not supported currently");

        }
    }
}
