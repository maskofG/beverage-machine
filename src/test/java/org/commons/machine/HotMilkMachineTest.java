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

//    @Test
//    private void testRefillIngredient(){
//
//    }
//
//    @Test
//    public void testIngredientLevel(){
//
//    }
//
//    @Test
//    public void testIngredientRunningLow(){
//
//    }
}
