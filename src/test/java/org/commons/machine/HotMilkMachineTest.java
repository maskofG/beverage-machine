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

public class HotMilkMachineTest {
    private Gson gson = new Gson();
    private String inputFile = getClass().getClassLoader().getResource("input_test.json").getPath();
    private int outlet;
    private BeverageComposition hotmilkRecipe;
    private IngredientContainer milkContainer;
    private HotMilkMachine hotMilkMachine;



    @Before
    public void setup() throws IOException, BeverageTypeNotSupportedException {
        InputData inputData = gson.fromJson(new FileReader(inputFile), InputData.class);
        outlet = inputData.getOutlet();
        hotmilkRecipe = inputData.buildBeverageComposition(BeverageType.HOT_MILK);
        milkContainer = inputData.buildIngredientContainer(IngredientType.MILK);
        hotMilkMachine = new HotMilkMachine.Builder()
                .outlet(outlet).beverageRecipe(hotmilkRecipe)
                .milkContainer(milkContainer).build();
    }

    /**
     * Testing HotMilkMachine.Builder class
     */
    @Test
    public void testBuilder() {
        Exception ex = null;
        try {
            HotMilkMachine hmm = new HotMilkMachine.Builder().build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            HotMilkMachine hmm = new HotMilkMachine.Builder().outlet(2).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            HotMilkMachine hmm = new HotMilkMachine.Builder().outlet(2)
                    .beverageRecipe(hotmilkRecipe).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            HotMilkMachine hmm = new HotMilkMachine.Builder().outlet(2)
                    .milkContainer(milkContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            HotMilkMachine hmm = new HotMilkMachine.Builder().outlet(2)
                    .beverageRecipe(hotmilkRecipe).milkContainer(milkContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
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
            hotMilkMachine.brew(null);
        } catch (BeverageTypeNotSupportedException btnse) {
            ex = btnse;
        } catch (RequestedQuantityNotPresentException | RequestedQuantityNotSufficientException e) {
            throw e;
        }

        Assert.assertEquals( true, ex != null);

        ex = null;
        try {
            hotMilkMachine.brew(BeverageType.HOT_WATER);
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
        String output = hotMilkMachine.dispense(BeverageType.HOT_WATER);

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
         * dispense a cup of hot milk
         */
        String output = hotMilkMachine.dispense(BeverageType.HOT_MILK);

        /**
         * check parameters after 1 cup dispense of hot milk
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(450, milkContainer.quantity());
        Assert.assertEquals(450, hotMilkMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(true, hotMilkMachine.ingredientsRunningLow().isEmpty());

        /**
         *
         * after 9 dispense the milk level should run low
         * milk level = 450
         * each cup contains = 50, so we should be able to dispense 9 cups;
         *
         */
        boolean prepared = true;
        for(int i=0;i<9;i++) {
            output = hotMilkMachine.dispense(BeverageType.HOT_MILK);
            prepared = prepared && output.contains(BeverageOutputMessage.PREPARED);
        }

        Assert.assertEquals(true, prepared);
        Assert.assertEquals(0, hotMilkMachine.ingredientLevel(IngredientType.MILK));

        /**
         * cannot dispense since milk is not available
         */
        output = hotMilkMachine.dispense(BeverageType.HOT_MILK);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NA));

        /**
         * milk level = 30, cannot dispense since milk is not sufficient
         */
        hotMilkMachine.refillIngredient(IngredientType.MILK, 30);
        output = hotMilkMachine.dispense(BeverageType.HOT_MILK);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS));

        hotMilkMachine.refillIngredient(IngredientType.MILK, 470);

    }

    /**
     * test refill ingredient functionality
     */
    @Test
    public void testRefillIngredient(){
        Exception e = null;
        try {
            hotMilkMachine.refillIngredient(null, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);

        e = null;
        try {
            hotMilkMachine.refillIngredient(IngredientType.WATER, 20);
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
        Assert.assertEquals(500, hotMilkMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(0, hotMilkMachine.ingredientLevel(null));
        Assert.assertEquals(0, hotMilkMachine.ingredientLevel(IngredientType.WATER));
    }

    @Test
    public void testIngredientRunningLow() throws IncorrectIngredientTypeException {
        Assert.assertEquals(true, hotMilkMachine.ingredientsRunningLow().isEmpty());

        String output;
        boolean prepared = true;
        for(int i=0;i<10;i++) {
            output = hotMilkMachine.dispense(BeverageType.HOT_MILK);
            prepared = prepared && output.contains(BeverageOutputMessage.PREPARED);
        }

        List<IngredientType> ingredientTypeList = hotMilkMachine.ingredientsRunningLow();

        Assert.assertEquals(1, ingredientTypeList.size());
        Assert.assertEquals(IngredientType.MILK, ingredientTypeList.get(0));

        hotMilkMachine.refillIngredient(IngredientType.MILK, 500);
    }
}
