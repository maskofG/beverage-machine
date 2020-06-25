package org.commons.ingredients;
/**
 * enumerated types of ingredients to brew beverages
 *
 */
public enum IngredientType {
    WATER("water", 0),
    MILK ("milk", 1),
    TEA_LEAVES_SYRUP("tea_leaves_syrup", 2),
    GREEN_MIXTURE ("green_mixture", 3),
    GINGER_SYRUP ("ginger_syrup", 4),
    ELAICHI_SYRUP ("elaichi_syrup", 5),
    COFFEE_SYRUP ("coffee_syrup", 6),
    SUGAR_SYRUP ("sugar_syrup", 7);

    private String name;
    private int value;

    private IngredientType(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
