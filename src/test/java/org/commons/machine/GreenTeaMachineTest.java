package org.commons.machine;

import com.google.gson.Gson;
import org.commons.ingredients.ConcreteIngredientContainer;
import org.commons.ingredients.IngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.IncorrectIngredientTypeException;
import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;
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
 *       "green_mixture": 300
 *     }
 *       "green_tea": {
 *         "hot_water": 100,
 *         "ginger_syrup": 30,
 *         "sugar_syrup": 50,
 *         "green_mixture": 30
 *       }
 */
public class GreenTeaMachineTest {
    private Gson gson = new Gson();
    private String inputFile = getClass().getClassLoader().getResource("input_test.json").getPath();
    private int outlet;
    private BeverageComposition greenTeaRecipe;
    private IngredientContainer waterContainer;
    private IngredientContainer greenMixtureContainer;
    private IngredientContainer gingerSyrupContainer;
    private IngredientContainer sugarSyrupContainer;
    private GreenTeaMachine greenTeaMachine;



    @Before
    public void setup() throws IOException, BeverageTypeNotSupportedException {
        InputData inputData = gson.fromJson(new FileReader(inputFile), InputData.class);
        outlet = inputData.getOutlet();
        greenTeaRecipe = inputData.buildBeverageComposition(BeverageType.GREEN_TEA);
        waterContainer = inputData.buildIngredientContainer(IngredientType.WATER);
        greenMixtureContainer = inputData.buildIngredientContainer(IngredientType.GREEN_MIXTURE);
        gingerSyrupContainer = inputData.buildIngredientContainer(IngredientType.GINGER_SYRUP);
        sugarSyrupContainer = inputData.buildIngredientContainer(IngredientType.SUGAR_SYRUP);
        greenTeaMachine = new GreenTeaMachine.Builder()
                .outlet(outlet).addRecipe(greenTeaRecipe)
                .addIngredientContainer(waterContainer)
                .addIngredientContainer(greenMixtureContainer)
                .addIngredientContainer(gingerSyrupContainer)
                .addIngredientContainer(sugarSyrupContainer)
                .build();
    }

    /**
     * testing GreenTeaMachine.Builder class
     */
    @Test
    public void testBuilder() {
        Exception ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder().build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder()
                    .outlet(2).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder()
                    .outlet(2).addRecipe(greenTeaRecipe).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder()
                    .addRecipe(greenTeaRecipe).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder()
                    .outlet(2).addRecipe(greenTeaRecipe)
                    .addIngredientContainer(waterContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder()
                    .outlet(2).addRecipe(greenTeaRecipe)
                    .addIngredientContainer(waterContainer)
                    .addIngredientContainer(greenMixtureContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder()
                    .outlet(2).addRecipe(greenTeaRecipe)
                    .addIngredientContainer(waterContainer)
                    .addIngredientContainer(greenMixtureContainer)
                    .addIngredientContainer(gingerSyrupContainer)
                    .build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder()
                    .outlet(2).addRecipe(greenTeaRecipe)
                    .addIngredientContainer(waterContainer)
                    .addIngredientContainer(greenMixtureContainer)
                    .addIngredientContainer(gingerSyrupContainer)
                    .addIngredientContainer(sugarSyrupContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex == null);

        ex = null;
        try {
            IngredientContainer coffeeContainer = new ConcreteIngredientContainer(IngredientType.COFFEE_SYRUP, 10);
            GreenTeaMachine gtm = new GreenTeaMachine.Builder()
                    .outlet(2).addRecipe(greenTeaRecipe)
                    .addIngredientContainer(coffeeContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);
    }

    /**
     * Testing brew method of the module
     * @throws RequestedQuantityNotSufficientException
     * @throws RequestedQuantityNotPresentException
     */
    @Test
    public void testBrew() throws RequestedQuantityNotSufficientException,
            RequestedQuantityNotPresentException {
        Exception ex = null;
        try {
            greenTeaMachine.brew(null);
        } catch (BeverageTypeNotSupportedException btnse) {
            ex = btnse;
        } catch (RequestedQuantityNotPresentException | RequestedQuantityNotSufficientException e) {
            throw e;
        }

        Assert.assertEquals( true, ex != null);

        ex = null;
        try {
            greenTeaMachine.brew(BeverageType.HOT_MILK);
        } catch (BeverageTypeNotSupportedException btnse) {
            ex = btnse;
        } catch (RequestedQuantityNotPresentException | RequestedQuantityNotSufficientException e) {
            throw e;
        }

        Assert.assertEquals(true, ex != null);
    }

    /**
     * Testing if machine dispenses not prepared if we provide wrong argument,
     */
    @Test
    public void testIncorrectDispense() {
        String output = greenTeaMachine.dispense(BeverageType.HOT_WATER);

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
         * dispense a cup of ginger tea
         */
        String output = greenTeaMachine.dispense(BeverageType.GREEN_TEA);

        /**
         * check parameters after 1 cup dispense of green tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(270, greenMixtureContainer.quantity());
        Assert.assertEquals(400, greenTeaMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(270, greenTeaMachine.ingredientLevel(IngredientType.GREEN_MIXTURE));
        Assert.assertEquals(270, greenTeaMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
        Assert.assertEquals(50, greenTeaMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(true, greenTeaMachine.ingredientsRunningLow().isEmpty());

        /**
         *
         * after 1 more dispense sugar will finish
         *
         */
        output = greenTeaMachine.dispense(BeverageType.GREEN_TEA);

        Assert.assertEquals(true, output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(0, greenTeaMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(240, greenTeaMachine.ingredientLevel(IngredientType.GREEN_MIXTURE));
        Assert.assertEquals(240, greenTeaMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
        Assert.assertEquals(300, greenTeaMachine.ingredientLevel(IngredientType.WATER));

        /**
         * cannot dispense since sugar is not available
         */
        output = greenTeaMachine.dispense(BeverageType.GREEN_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NA) && output.contains("sugar_syrup"));


        greenTeaMachine.refillIngredient(IngredientType.WATER, 200);
        greenTeaMachine.refillIngredient(IngredientType.GREEN_MIXTURE, 60);
        greenTeaMachine.refillIngredient(IngredientType.GINGER_SYRUP, 60);
        greenTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 100);
    }


    /**
     * Tests the functionality of refill of ingredients
     */
    @Test
    public void testRefillIngredient(){
        Exception e = null;
        try {
            greenTeaMachine.refillIngredient(null, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);

        e = null;
        try {
            greenTeaMachine.refillIngredient(IngredientType.MILK, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);
    }

    /**
     * check ingredient level functionality
     */
    @Test
    public void testIngredientLevel(){
        Assert.assertEquals(500, greenTeaMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(0, greenTeaMachine.ingredientLevel(null));
        Assert.assertEquals(0, greenTeaMachine.ingredientLevel(IngredientType.MILK));
    }

    /**
     * test api which gives the list ingredients which are running low on
     * quantity
     * @throws IncorrectIngredientTypeException
     */
    @Test
    public void testIngredientRunningLow() throws IncorrectIngredientTypeException {
        Assert.assertEquals(true, greenTeaMachine.ingredientsRunningLow().isEmpty());

        greenTeaMachine.refillIngredient(IngredientType.WATER, 500);
        greenTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 400);
        //sugar syrup total quantity = 300 and per cup quantity is 30
        //green mixture total quantity = 300 and cup quantity is 30

        for(int i=0;i<10;i++){
            greenTeaMachine.dispense(BeverageType.GREEN_TEA);
        }

        List<IngredientType> ingredientTypeList = greenTeaMachine.ingredientsRunningLow();
        Assert.assertEquals(4, ingredientTypeList.size());
        Assert.assertEquals(true, ingredientTypeList.contains(IngredientType.WATER) &&
                ingredientTypeList.contains(IngredientType.GREEN_MIXTURE) &&
                ingredientTypeList.contains(IngredientType.GINGER_SYRUP) &&
                ingredientTypeList.contains(IngredientType.SUGAR_SYRUP));

        // if we try to dispense a cup of green tea, it will fail
        String output = greenTeaMachine.dispense(BeverageType.GREEN_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.QTY_NA) &&
                output.contains("hot_water"));

        //refill water less than minimum required limited => the message from
        //green tea machine changes
        greenTeaMachine.refillIngredient(IngredientType.WATER, 90);
        output = greenTeaMachine.dispense(BeverageType.GREEN_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS) &&
                output.contains("hot_water"));
        //bring water level back to 500 from 90
        greenTeaMachine.refillIngredient(IngredientType.WATER, 410);
        greenTeaMachine.refillIngredient(IngredientType.GREEN_MIXTURE, 300);
        greenTeaMachine.refillIngredient(IngredientType.GINGER_SYRUP, 300);
        greenTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 100);
    }
}
