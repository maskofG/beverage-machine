package org.commons.machine;

public enum BeverageType {
    HOT_WATER("hot_water", 0),
    HOT_MILK("hot_milk", 1),
    HOT_COFFEE("hot_coffee", 2),
    GREEN_TEA("green_tea", 3),
    ELAICHI_TEA("elaichi_tea", 4),
    GINGER_TEA( "ginger_tea", 5);

    private String name;
    private int value;

    private BeverageType(String name, int value){
        this.name = name;
        this.value = value;
    }
}
