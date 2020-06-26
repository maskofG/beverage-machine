package org.commons.machine;

import com.google.gson.Gson;
import org.commons.ingredients.IngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.IncorrectIngredientTypeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;


/**
 *     "total_items_quantity": {
 *       "hot_water": 500,
 *       "hot_milk": 500,
 *       "ginger_syrup": 300,
 *       "sugar_syrup": 100,
 *       "tea_leaves_syrup": 100,
 *       "coffee_syrup": 300,
 *       "green_mixture": 300,
 *       "elaichi_syrup": 300
 *     },
 *      "hot_coffee": {
 *         "hot_water": 100,
 *         "hot_milk": 400,
 *         "sugar_syrup": 50,
 *         "coffee_syrup": 30
 *       }
 */
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

    /**
     * testing CoffeeMachine.Builder class
     */
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

    /**
     * Testing if machine dispenses not prepared if we provide wrong argument,
     */
    @Test
    public void testIncorrectDispense() {
        String output = coffeeMachine.dispense(BeverageType.HOT_MILK);

        Assert.assertEquals(true, output != null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.NOT_SUPPORTED));
    }


    /**
     * Testing scenarios of correct dispensing of beverage and Eventually running out
     * of ingredients and testing if machine stops dispensing beverage
     *
     */
    @Test
    public void testCorrectDispense() throws IncorrectIngredientTypeException {
        /**
         * dispense a cup of hot coffee
         */
        String output = coffeeMachine.dispense(BeverageType.HOT_COFFEE);

        /**
         * check parameters after 1 cup dispense of elaichi tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(270, coffeeSyrupContainer.quantity());
        Assert.assertEquals(400, coffeeMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(100, coffeeMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(270, coffeeMachine.ingredientLevel(IngredientType.COFFEE_SYRUP));
        Assert.assertEquals(50, coffeeMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(false, coffeeMachine.ingredientsRunningLow().isEmpty());


        /**
         * cannot dispense since milk is not sufficient
         */
        output = coffeeMachine.dispense(BeverageType.HOT_COFFEE);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS) && output.contains("hot_milk"));


        coffeeMachine.refillIngredient(IngredientType.WATER, 100);
        coffeeMachine.refillIngredient(IngredientType.MILK, 400);
        coffeeMachine.refillIngredient(IngredientType.COFFEE_SYRUP, 30);
        coffeeMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 50);
    }

    @Test
    public void testRefillIngredient(){
        Exception e = null;
        try {
            coffeeMachine.refillIngredient(null, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);

        e = null;
        try {
            coffeeMachine.refillIngredient(IngredientType.ELAICHI_SYRUP, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);
    }



    @Test
    public void testIngredientRunningLow() throws IncorrectIngredientTypeException {
        Assert.assertEquals(true, coffeeMachine.ingredientsRunningLow().isEmpty());

        /**
         * dispense a cup of hot coffee
         */
        String output = coffeeMachine.dispense(BeverageType.HOT_COFFEE);

        /**
         * check parameters after 1 cup dispense of elaichi tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(false, coffeeMachine.ingredientsRunningLow().isEmpty());


        /**
         * cannot dispense since milk is not sufficient
         */
        output = coffeeMachine.dispense(BeverageType.HOT_COFFEE);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS) && output.contains("hot_milk"));
        List<IngredientType> ingredientTypeList = coffeeMachine.ingredientsRunningLow();
        Assert.assertEquals(false, ingredientTypeList.isEmpty());
        Assert.assertEquals(1, ingredientTypeList.size());
        Assert.assertEquals(IngredientType.MILK, ingredientTypeList.get(0));


        coffeeMachine.refillIngredient(IngredientType.WATER, 100);
        coffeeMachine.refillIngredient(IngredientType.MILK, 400);
        coffeeMachine.refillIngredient(IngredientType.COFFEE_SYRUP, 30);
        coffeeMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 50);
    }
}
