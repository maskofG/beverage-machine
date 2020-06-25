package org.commons.machine;

import com.google.gson.Gson;
import org.commons.ingredients.BeverageComposition;
import org.commons.ingredients.IngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.IncorrectIngredientTypeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;


public class HotWaterInputDataTest {
    private Gson gson = new Gson();
    private String inputFile = getClass().getClassLoader().getResource("input_test.json").getPath();
    private int outlet;
    private BeverageComposition hotwaterRecipe;
    private IngredientContainer waterContainer;
    private HotWaterMachine hotWaterMachine;



    @Before
    public void setup() throws IOException, BeverageTypeNotSupportedException {
        InputData inputData = gson.fromJson(new FileReader(inputFile), InputData.class);
        outlet = inputData.getOutlet();
        hotwaterRecipe = inputData.buildBeverageComposition(BeverageType.HOT_WATER);
        waterContainer = inputData.buildIngredientContainer(IngredientType.WATER);
        hotWaterMachine = new HotWaterMachine.Builder()
                .outlet(outlet).beverageComposition(hotwaterRecipe).waterContainer(waterContainer).build();
    }

    @Test
    public void testIncorrectDispense(){
        String output = hotWaterMachine.dispense(BeverageType.HOT_MILK);

        Assert.assertEquals(true, output != null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains("not supported"));
    }

    /**
     * test dispensing of hot water scenarios
     *
     * @throws IncorrectIngredientTypeException
     */
    @Test
    public void testCorrectDispense() throws IncorrectIngredientTypeException {
        /**
         * dispense a cup of hot water
         */
        String output = hotWaterMachine.dispense(BeverageType.HOT_WATER);

        /**
         * check parameters after 1 cup dispense of hot water
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(450, waterContainer.quantity());
        Assert.assertEquals(450, hotWaterMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(true, hotWaterMachine.ingredientsRunningLow().isEmpty());

        /**
         *
         * after 9 dispense the water level should run low
         * water level = 450
         * each cup contains = 50, so we should be able to dispense 9 cups;
         *
         */
        boolean prepared = true;
        for(int i=0;i<9;i++) {
            output = hotWaterMachine.dispense(BeverageType.HOT_WATER);
            prepared = prepared && output.contains(BeverageOutputMessage.PREPARED);
        }

        Assert.assertEquals(true, prepared);
        Assert.assertEquals(0, hotWaterMachine.ingredientLevel(IngredientType.WATER));

        /**
         * cannot dispense since water is not available
         */
        output = hotWaterMachine.dispense(BeverageType.HOT_WATER);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NA));

        /**
         * water level = 30, cannot dispense since water is not sufficient
         */
        hotWaterMachine.refillIngredient(IngredientType.WATER, 30);
        output = hotWaterMachine.dispense(BeverageType.HOT_WATER);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS));

        hotWaterMachine.refillIngredient(IngredientType.WATER, 470);

    }

    /**
     * check refill of water
     */
    @Test
    public void testRefillIngredient(){

        Assert.assertEquals(500, hotWaterMachine.ingredientLevel(IngredientType.WATER));

        Exception ex = null;
        try {
            hotWaterMachine.refillIngredient(IngredientType.WATER, 50);
        } catch (IncorrectIngredientTypeException | IllegalArgumentException e) {
            ex = e;
        }

        Assert.assertEquals(null, ex);
        Assert.assertEquals(550, hotWaterMachine.ingredientLevel(IngredientType.WATER));

        /**
         * check if exception is throw if we refill negative value of water
         */

        try {
            hotWaterMachine.refillIngredient(IngredientType.WATER, -50);
        } catch (IncorrectIngredientTypeException | IllegalArgumentException e) {
            ex = e;
        }

        Assert.assertEquals(true, ex != null);
        Assert.assertEquals(550, hotWaterMachine.ingredientLevel(IngredientType.WATER));

        hotWaterMachine.dispense(BeverageType.HOT_WATER);
    }

    /**
     * check ingredient level functionality
     */
    @Test
    public void testIngredientLevel(){
        Assert.assertEquals(500, hotWaterMachine.ingredientLevel(IngredientType.WATER));
    }

    /**
     * check ingredient level running low
     */
    @Test
    public void testIngredientRunningLow() {
        for(int i=0;i<9;i++) {
            hotWaterMachine.dispense(BeverageType.HOT_WATER);
            Assert.assertEquals(true, hotWaterMachine.ingredientsRunningLow().isEmpty());
        }

        hotWaterMachine.dispense(BeverageType.HOT_WATER);
        Assert.assertEquals(false, hotWaterMachine.ingredientsRunningLow().isEmpty());
        Assert.assertEquals(1, hotWaterMachine.ingredientsRunningLow().size());
        Assert.assertEquals(IngredientType.WATER, hotWaterMachine.ingredientsRunningLow().get(0));
    }

}
