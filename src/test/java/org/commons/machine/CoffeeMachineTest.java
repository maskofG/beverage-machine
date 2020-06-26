package org.commons.machine;

import com.google.gson.Gson;
import org.commons.ingredients.IngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

public class CoffeeMachineTest {
    private Gson gson = new Gson();
    private String inputFile = getClass().getClassLoader().getResource("input_test.json").getPath();
    private int outlet;
    private BeverageComposition hotCoffeeRecipe;
    private IngredientContainer waterContainer;
    private IngredientContainer milkContainer;
    private IngredientContainer coffeeSyrupContainer;
    private IngredientContainer sugarSyrupContainer;
    private CoffeeMachine       coffeeMachine;



    @Before
    public void setup() throws IOException, BeverageTypeNotSupportedException {
        InputData inputData = gson.fromJson(new FileReader(inputFile), InputData.class);
        outlet = inputData.getOutlet();
        hotCoffeeRecipe = inputData.buildBeverageComposition(BeverageType.HOT_COFFEE);
        waterContainer = inputData.buildIngredientContainer(IngredientType.WATER);
        milkContainer = inputData.buildIngredientContainer(IngredientType.MILK);
        coffeeSyrupContainer = inputData.buildIngredientContainer(IngredientType.COFFEE_SYRUP);
        sugarSyrupContainer = inputData.buildIngredientContainer(IngredientType.SUGAR_SYRUP);
        coffeeMachine = new CoffeeMachine.Builder()
                .outlet(outlet).addRecipe(hotCoffeeRecipe)
                .addIngredientContainer(waterContainer)
                .addIngredientContainer(milkContainer)
                .addIngredientContainer(coffeeSyrupContainer)
                .addIngredientContainer(sugarSyrupContainer)
                .build();
    }

    @Test
    public void testBuilder() {
        Exception ex = null;
        try {
            CoffeeMachine cm = new CoffeeMachine.Builder().build();
        } catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);
    }
}
