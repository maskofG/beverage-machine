package org.commons.machine;

import com.google.gson.Gson;
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

public class ChaiPointMachineTest {

    private Gson gson = new Gson();
    private String inputFile = getClass().getClassLoader().getResource("input_test.json").getPath();
    private int outlet;
    private BeverageComposition hotwaterRecipe;
    private BeverageComposition hotmilkRecipe;
    private BeverageComposition greenTeaRecipe;
    private BeverageComposition gingerTeaRecipe;
    private BeverageComposition elaichiTeaRecipe;
    private BeverageComposition hotCoffeeRecipe;

    private IngredientContainer waterContainer;
    private IngredientContainer milkContainer;
    private IngredientContainer greenMixtureContainer;
    private IngredientContainer teaLeavesContainer;
    private IngredientContainer elaichiSyrupContainer;
    private IngredientContainer gingerSyrupContainer;
    private IngredientContainer coffeeSyrupContainer;
    private IngredientContainer sugarSyrupContainer;
    private ChaiPointBeverageMachine    chaiPointBeverageMachine;
    private HotWaterMachine hotWaterMachine;
    private HotMilkMachine  hotMilkMachine;
    private GreenTeaMachine greenTeaMachine;
    private GingerTeaMachine gingerTeaMachine;
    private ElaichiTeaMachine elaichiTeaMachine;
    private CoffeeMachine   coffeeMachine;




    @Before
    public void setUp() throws IOException, BeverageTypeNotSupportedException {
        InputData inputData = gson.fromJson(new FileReader(inputFile), InputData.class);
        outlet = inputData.getOutlet();
        hotwaterRecipe = inputData.buildBeverageComposition(BeverageType.HOT_WATER);
        hotmilkRecipe = inputData.buildBeverageComposition(BeverageType.HOT_MILK);
        greenTeaRecipe = inputData.buildBeverageComposition(BeverageType.GREEN_TEA);
        gingerTeaRecipe = inputData.buildBeverageComposition(BeverageType.GINGER_TEA);
        elaichiTeaRecipe = inputData.buildBeverageComposition(BeverageType.ELAICHI_TEA);
        hotCoffeeRecipe = inputData.buildBeverageComposition(BeverageType.HOT_COFFEE);

        waterContainer = inputData.buildIngredientContainer(IngredientType.WATER);
        milkContainer = inputData.buildIngredientContainer(IngredientType.MILK);
        teaLeavesContainer = inputData.buildIngredientContainer(IngredientType.TEA_LEAVES_SYRUP);
        greenMixtureContainer = inputData.buildIngredientContainer(IngredientType.GREEN_MIXTURE);
        gingerSyrupContainer = inputData.buildIngredientContainer(IngredientType.GINGER_SYRUP);
        elaichiSyrupContainer = inputData.buildIngredientContainer(IngredientType.ELAICHI_SYRUP);
        sugarSyrupContainer = inputData.buildIngredientContainer(IngredientType.SUGAR_SYRUP);
        coffeeSyrupContainer = inputData.buildIngredientContainer(IngredientType.COFFEE_SYRUP);

        setUpHotWaterMachine(inputData);
        setUpHotMilkMachine(inputData);
        setUpGreenTeaMachine(inputData);
        setUpGingerTeaMachine(inputData);
        setUpElaichiTeaMachine(inputData);
        setUpCoffeeMachine(inputData);

        chaiPointBeverageMachine =
                new ChaiPointBeverageMachine.Builder()
                .outlet(outlet)
                .addMachine(hotWaterMachine)
                .addMachine(hotMilkMachine)
                .addMachine(greenTeaMachine)
                .addMachine(gingerTeaMachine)
                .addMachine(elaichiTeaMachine)
                .addMachine(coffeeMachine)
                .build();
    }

    private void setUpHotWaterMachine(InputData inputData) {
        hotWaterMachine = new HotWaterMachine.Builder()
                .outlet(outlet).beverageRecipe(hotwaterRecipe)
                .waterContainer(waterContainer).build();
    }

    private void setUpHotMilkMachine(InputData inputData) {
        hotMilkMachine = new HotMilkMachine.Builder()
                .outlet(outlet).beverageRecipe(hotmilkRecipe)
                .milkContainer(milkContainer).build();
    }

    private void setUpGreenTeaMachine(InputData inputData) {
        greenTeaMachine = new GreenTeaMachine.Builder()
                .outlet(outlet).addRecipe(greenTeaRecipe)
                .addIngredientContainer(waterContainer)
                .addIngredientContainer(greenMixtureContainer)
                .addIngredientContainer(gingerSyrupContainer)
                .addIngredientContainer(sugarSyrupContainer)
                .build();
    }

    private void setUpGingerTeaMachine(InputData inputData) {
        gingerTeaMachine = new GingerTeaMachine.Builder()
                .outlet(outlet).addRecipe(gingerTeaRecipe)
                .addIngredientContainer(waterContainer)
                .addIngredientContainer(milkContainer)
                .addIngredientContainer(teaLeavesContainer)
                .addIngredientContainer(gingerSyrupContainer)
                .addIngredientContainer(sugarSyrupContainer)
                .build();
    }

    private void setUpElaichiTeaMachine(InputData inputData) {
        elaichiTeaMachine = new ElaichiTeaMachine.Builder()
                .outlet(outlet).addRecipe(elaichiTeaRecipe)
                .addIngredientContainer(waterContainer)
                .addIngredientContainer(milkContainer)
                .addIngredientContainer(teaLeavesContainer)
                .addIngredientContainer(elaichiSyrupContainer)
                .addIngredientContainer(sugarSyrupContainer)
                .build();
    }

    private void setUpCoffeeMachine(InputData inputData) {
        coffeeMachine = new CoffeeMachine.Builder()
                .outlet(outlet).addRecipe(hotCoffeeRecipe)
                .addIngredientContainer(waterContainer)
                .addIngredientContainer(milkContainer)
                .addIngredientContainer(coffeeSyrupContainer)
                .addIngredientContainer(sugarSyrupContainer)
                .build();
    }


    /**
     * testing ChaiPointBeverageMachine.Builder class
     */
    @Test
    public void testBuilder() {
        Exception ex = null;
        ChaiPointBeverageMachine cpbm;

        try {
             cpbm = new ChaiPointBeverageMachine.Builder().build();
        } catch (IllegalArgumentException ile) {
            ex = ile;
        }
        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cpbm = new ChaiPointBeverageMachine.Builder().outlet(2).build();
        } catch (IllegalArgumentException ila){
            ex = ila;
        }
        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cpbm = new ChaiPointBeverageMachine.Builder().outlet(2).addMachine(hotWaterMachine).build();
        } catch (IllegalArgumentException ila){
            ex = ila;
        }

        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cpbm = new ChaiPointBeverageMachine.Builder().outlet(2).addMachine(hotWaterMachine)
                    .addMachine(hotMilkMachine).build();
        } catch (IllegalArgumentException ila){
            ex = ila;
        }

        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cpbm = new ChaiPointBeverageMachine.Builder().outlet(2).addMachine(hotWaterMachine)
                    .addMachine(hotMilkMachine).addMachine(greenTeaMachine).build();
        } catch (IllegalArgumentException ila){
            ex = ila;
        }

        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cpbm = new ChaiPointBeverageMachine.Builder().outlet(2).addMachine(hotWaterMachine)
                    .addMachine(hotMilkMachine).addMachine(greenTeaMachine)
                    .addMachine(gingerTeaMachine).build();
        } catch (IllegalArgumentException ila){
            ex = ila;
        }

        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cpbm = new ChaiPointBeverageMachine.Builder().outlet(2).addMachine(hotWaterMachine)
                    .addMachine(hotMilkMachine).addMachine(greenTeaMachine)
                    .addMachine(gingerTeaMachine).addMachine(elaichiTeaMachine).build();
        } catch (IllegalArgumentException ila){
            ex = ila;
        }

        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cpbm = new ChaiPointBeverageMachine.Builder().outlet(2).addMachine(hotWaterMachine)
                    .addMachine(hotMilkMachine).addMachine(greenTeaMachine)
                    .addMachine(gingerTeaMachine).addMachine(elaichiTeaMachine)
                    .addMachine(coffeeMachine).build();
        } catch (IllegalArgumentException ila){
            ex = ila;
        }

        Assert.assertEquals(true, ex == null);
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
            chaiPointBeverageMachine.brew(null);
        } catch (BeverageTypeNotSupportedException btnse) {
            ex = btnse;
        } catch (RequestedQuantityNotPresentException | RequestedQuantityNotSufficientException e) {
            throw e;
        }

        Assert.assertEquals( true, ex != null);

        ex = null;
        try {
            hotWaterMachine.brew(BeverageType.HOT_MILK);
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
        String output = chaiPointBeverageMachine.dispense(null);

        Assert.assertEquals(true, output != null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains("null"));
    }

    /**
     * Testing scenarios of correct dispensing of beverage and Eventually running out
     * of ingredients and testing if machine stops dispensing beverage
     *
     */
    @Test
    public void testCorrectDispense() throws IncorrectIngredientTypeException {
        testDispenseOfHotWater();
        testDispenseOfHotMilk();
        testDispenseOfGreenTea();
        testDispenseOfGingerTea();
        testDispenseOfElaichiTea();
        testDispenseOfHotCoffee();
    }

    private void testDispenseOfHotWater() throws IncorrectIngredientTypeException {
        /**
         * dispense a cup of hot water
         */
        String output = chaiPointBeverageMachine.dispense(BeverageType.HOT_WATER);

        /**
         * check parameters after 1 cup dispense of hot water
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(450, waterContainer.quantity());
        Assert.assertEquals(450, chaiPointBeverageMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(true, chaiPointBeverageMachine.ingredientsRunningLow().isEmpty());

        chaiPointBeverageMachine.refillIngredient(IngredientType.WATER, 50);

    }

    private void testDispenseOfHotMilk() throws IncorrectIngredientTypeException {
        /**
         * dispense a cup of hot milk
         */
        String output = chaiPointBeverageMachine.dispense(BeverageType.HOT_MILK);

        /**
         * check parameters after 1 cup dispense of hot milk
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(450, milkContainer.quantity());
        Assert.assertEquals(450, chaiPointBeverageMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(true, chaiPointBeverageMachine.ingredientsRunningLow().isEmpty());

        chaiPointBeverageMachine.refillIngredient(IngredientType.MILK, 50);;
    }

    private void testDispenseOfGreenTea() throws IncorrectIngredientTypeException {
        /**
         * dispense a cup of ginger tea
         */
        String output = chaiPointBeverageMachine.dispense(BeverageType.GREEN_TEA);

        /**
         * check parameters after 1 cup dispense of green tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(270, greenMixtureContainer.quantity());
        Assert.assertEquals(400, chaiPointBeverageMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(270, chaiPointBeverageMachine.ingredientLevel(IngredientType.GREEN_MIXTURE));
        Assert.assertEquals(270, chaiPointBeverageMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
        Assert.assertEquals(50, chaiPointBeverageMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(true, chaiPointBeverageMachine.ingredientsRunningLow().isEmpty());

        chaiPointBeverageMachine.refillIngredient(IngredientType.WATER, 100);
        chaiPointBeverageMachine.refillIngredient(IngredientType.GREEN_MIXTURE, 30);
        chaiPointBeverageMachine.refillIngredient(IngredientType.GINGER_SYRUP, 30);
        chaiPointBeverageMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 50);

    }

    private void testDispenseOfGingerTea() throws IncorrectIngredientTypeException {
        /**
         * dispense a cup of ginger tea
         */
        String output = chaiPointBeverageMachine.dispense(BeverageType.GINGER_TEA);

        /**
         * check parameters after 1 cup dispense of ginger tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(290, gingerSyrupContainer.quantity());
        Assert.assertEquals(300, chaiPointBeverageMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(400, chaiPointBeverageMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(70, chaiPointBeverageMachine.ingredientLevel(IngredientType.TEA_LEAVES_SYRUP));
        Assert.assertEquals(290, chaiPointBeverageMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
        Assert.assertEquals(90, chaiPointBeverageMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(true, chaiPointBeverageMachine.ingredientsRunningLow().isEmpty());

        chaiPointBeverageMachine.refillIngredient(IngredientType.WATER, 200);
        chaiPointBeverageMachine.refillIngredient(IngredientType.MILK, 100);
        chaiPointBeverageMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, 30);
        chaiPointBeverageMachine.refillIngredient(IngredientType.GINGER_SYRUP, 10);
        chaiPointBeverageMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 10);
    }

    private void testDispenseOfElaichiTea() throws IncorrectIngredientTypeException {
        /**
        * dispense a cup of elaichi tea
        */
        String output = chaiPointBeverageMachine.dispense(BeverageType.ELAICHI_TEA);

        /**
         * check parameters after 1 cup dispense of elaichi tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(270, elaichiSyrupContainer.quantity());
        Assert.assertEquals(300, chaiPointBeverageMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(400, chaiPointBeverageMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(70, chaiPointBeverageMachine.ingredientLevel(IngredientType.TEA_LEAVES_SYRUP));
        Assert.assertEquals(270, chaiPointBeverageMachine.ingredientLevel(IngredientType.ELAICHI_SYRUP));
        Assert.assertEquals(90, chaiPointBeverageMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(true, elaichiTeaMachine.ingredientsRunningLow().isEmpty());

        chaiPointBeverageMachine.refillIngredient(IngredientType.WATER, 200);
        chaiPointBeverageMachine.refillIngredient(IngredientType.MILK, 100);
        chaiPointBeverageMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, 30);
        chaiPointBeverageMachine.refillIngredient(IngredientType.ELAICHI_SYRUP, 30);
        chaiPointBeverageMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 10);
    }

    private void testDispenseOfHotCoffee() throws IncorrectIngredientTypeException {
        /**
         * dispense a cup of hot coffee from chaiPointMachine
         */
        String output = chaiPointBeverageMachine.dispense(BeverageType.HOT_COFFEE);

        /**
         * check parameters after 1 cup dispense of elaichi tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(270, coffeeSyrupContainer.quantity());
        Assert.assertEquals(400, chaiPointBeverageMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(100, chaiPointBeverageMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(270, chaiPointBeverageMachine.ingredientLevel(IngredientType.COFFEE_SYRUP));
        Assert.assertEquals(50, chaiPointBeverageMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(false, chaiPointBeverageMachine.ingredientsRunningLow().isEmpty());


        /**
         * cannot dispense since milk is not sufficient
         */
        output = chaiPointBeverageMachine.dispense(BeverageType.HOT_COFFEE);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS) && output.contains("hot_milk"));


        chaiPointBeverageMachine.refillIngredient(IngredientType.WATER, 100);
        chaiPointBeverageMachine.refillIngredient(IngredientType.MILK, 400);
        chaiPointBeverageMachine.refillIngredient(IngredientType.COFFEE_SYRUP, 30);
        chaiPointBeverageMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 50);
    }

    @Test
    public void testRefillIngredient(){
        Exception e = null;
        try {
            chaiPointBeverageMachine.refillIngredient(null, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);
    }

    /**
     * testing the scenario when one of the ingredients gets low
     *
     * @throws IncorrectIngredientTypeException
     */
    @Test
    public void testIngredientRunningLow() throws IncorrectIngredientTypeException {
        /**
         * dispense a cup of ginger tea
         */
        String output = chaiPointBeverageMachine.dispense(BeverageType.GREEN_TEA);

        /**
         * check parameters after 1st cup dispense of green tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(270, greenMixtureContainer.quantity());
        Assert.assertEquals(400, chaiPointBeverageMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(270, chaiPointBeverageMachine.ingredientLevel(IngredientType.GREEN_MIXTURE));
        Assert.assertEquals(270, chaiPointBeverageMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
        Assert.assertEquals(50, chaiPointBeverageMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(true, chaiPointBeverageMachine.ingredientsRunningLow().isEmpty());

        /**
         * check parameters after 2nd cup dispense of green tea
         * sugar level = 0
         */
        output = chaiPointBeverageMachine.dispense(BeverageType.GREEN_TEA);
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(240, greenMixtureContainer.quantity());
        Assert.assertEquals(300, chaiPointBeverageMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(240, chaiPointBeverageMachine.ingredientLevel(IngredientType.GREEN_MIXTURE));
        Assert.assertEquals(240, chaiPointBeverageMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
        Assert.assertEquals(0, chaiPointBeverageMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        List<IngredientType> runningLowIngrList = chaiPointBeverageMachine.ingredientsRunningLow();
        Assert.assertEquals(false, runningLowIngrList.isEmpty());
        Assert.assertEquals(1, runningLowIngrList.size());
        Assert.assertEquals(IngredientType.SUGAR_SYRUP, runningLowIngrList.get(0));

        output = chaiPointBeverageMachine.dispense(BeverageType.GREEN_TEA);
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.NOT_PREPARED)&&
                output.contains(BeverageOutputMessage.QTY_NA) &&
                output.contains(IngredientType.SUGAR_SYRUP.getFieldDescriptor()));

        chaiPointBeverageMachine.refillIngredient(IngredientType.WATER, 200);
        chaiPointBeverageMachine.refillIngredient(IngredientType.GREEN_MIXTURE, 60);
        chaiPointBeverageMachine.refillIngredient(IngredientType.GINGER_SYRUP, 60);
        chaiPointBeverageMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 100);
    }

    /**
     * testing edge cases and branch cases for ingredientLevel function
     */
    @Test
    public void testIngredientLevel(){
        Assert.assertEquals(0, chaiPointBeverageMachine.ingredientLevel(null));
        Assert.assertEquals(500, chaiPointBeverageMachine.ingredientLevel(IngredientType.MILK));
    }

}
