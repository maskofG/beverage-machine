package org.commons.machine;

/**
 * Type of beverages currently supported by our system
 * Enumeration optimises on compare and equals in comparison to string as
 * these are global scoped numbers with string value as one of its
 * attributes.
 *
 */
public enum BeverageType {
    HOT_WATER("hot_water", 0),
    HOT_MILK("hot_milk", 1),
    HOT_COFFEE("hot_coffee", 2),
    GREEN_TEA("green_tea", 3),
    ELAICHI_TEA("elaichi_tea", 4),
    GINGER_TEA( "ginger_tea", 5);

    private String fieldDescriptor;
    private int value;

    public String getFieldDescriptor(){
        return fieldDescriptor;
    }

    private BeverageType(String fieldDescriptor, int value){
        this.fieldDescriptor = fieldDescriptor;
        this.value = value;
    }
}
