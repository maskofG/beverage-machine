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
 *       "green_mixture": 300,
 *       "elaichi_syrup": 300
 *     },
 *      "elaichi_tea": {
 *         "hot_water": 200,
 *         "hot_milk": 100,
 *         "sugar_syrup": 10,
 *         "tea_leaves_syrup": 30,
 *         "elaichi_syrup": 30
 *       }
}
 */

public class ElaichiTeaMachineTest {
    private Gson gson = new Gson();
    private String inputFile = getClass().getClassLoader().getResource("input_test.json").getPath();
    private int outlet;
    private BeverageComposition elaichiTeaRecipe;
    private IngredientContainer waterContainer;
    private IngredientContainer milkContainer;
    private IngredientContainer teaLeavesContainer;
    private IngredientContainer elaichiSyrupContainer;
    private IngredientContainer sugarSyrupContainer;
    private ElaichiTeaMachine   elaichiTeaMachine;



    @Before
    public void setup() throws IOException, BeverageTypeNotSupportedException {
        InputData inputData = gson.fromJson(new FileReader(inputFile), InputData.class);
        outlet = inputData.getOutlet();
        elaichiTeaRecipe = inputData.buildBeverageComposition(BeverageType.ELAICHI_TEA);
        waterContainer = inputData.buildIngredientContainer(IngredientType.WATER);
        milkContainer = inputData.buildIngredientContainer(IngredientType.MILK);
        teaLeavesContainer = inputData.buildIngredientContainer(IngredientType.TEA_LEAVES_SYRUP);
        elaichiSyrupContainer = inputData.buildIngredientContainer(IngredientType.ELAICHI_SYRUP);
        sugarSyrupContainer = inputData.buildIngredientContainer(IngredientType.SUGAR_SYRUP);
        elaichiTeaMachine = new ElaichiTeaMachine.Builder()
                .outlet(outlet).addRecipe(elaichiTeaRecipe)
                .addIngredientContainer(waterContainer)
                .addIngredientContainer(milkContainer)
                .addIngredientContainer(teaLeavesContainer)
                .addIngredientContainer(elaichiSyrupContainer)
                .addIngredientContainer(sugarSyrupContainer)
                .build();
    }

    /**
     * testing ElaichiTeaMachine.Builder class
     */
    @Test
    public void testBuilder() {
        Exception ex = null;
        try {
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder().build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder()
                    .outlet(2).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder()
                    .outlet(2).addRecipe(elaichiTeaRecipe).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder()
                    .outlet(2).addRecipe(elaichiTeaRecipe)
                    .addIngredientContainer(waterContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder()
                    .outlet(2).addRecipe(elaichiTeaRecipe)
                    .addIngredientContainer(waterContainer)
                    .addIngredientContainer(milkContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder()
                    .outlet(2).addRecipe(elaichiTeaRecipe)
                    .addIngredientContainer(waterContainer)
                    .addIngredientContainer(milkContainer)
                    .addIngredientContainer(teaLeavesContainer)
                    .build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder()
                    .outlet(2).addRecipe(elaichiTeaRecipe)
                    .addIngredientContainer(waterContainer)
                    .addIngredientContainer(milkContainer)
                    .addIngredientContainer(teaLeavesContainer)
                    .addIngredientContainer(elaichiSyrupContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder()
                    .outlet(2).addRecipe(elaichiTeaRecipe)
                    .addIngredientContainer(waterContainer)
                    .addIngredientContainer(milkContainer)
                    .addIngredientContainer(teaLeavesContainer)
                    .addIngredientContainer(elaichiSyrupContainer)
                    .addIngredientContainer(sugarSyrupContainer).build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex == null);

        ex = null;
        try {

            IngredientContainer gingerSyrupContainer = new ConcreteIngredientContainer(IngredientType.GINGER_SYRUP, 10);
            IngredientContainer coffeeContainer = new ConcreteIngredientContainer(IngredientType.COFFEE_SYRUP, 10);
            ElaichiTeaMachine gtm = new ElaichiTeaMachine.Builder()
                    .outlet(2).addRecipe(elaichiTeaRecipe)
                    .addIngredientContainer(gingerSyrupContainer)
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
            elaichiTeaMachine.brew(null);
        } catch (BeverageTypeNotSupportedException btnse) {
            ex = btnse;
        } catch (RequestedQuantityNotPresentException | RequestedQuantityNotSufficientException e) {
            throw e;
        }

        Assert.assertEquals( true, ex != null);

        ex = null;
        try {
            elaichiTeaMachine.brew(BeverageType.HOT_MILK);
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
        String output = elaichiTeaMachine.dispense(BeverageType.HOT_MILK);

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
         * dispense a cup of elaichi tea
         */
        String output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);

        /**
         * check parameters after 1 cup dispense of elaichi tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(270, elaichiSyrupContainer.quantity());
        Assert.assertEquals(300, elaichiTeaMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(400, elaichiTeaMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(70, elaichiTeaMachine.ingredientLevel(IngredientType.TEA_LEAVES_SYRUP));
        Assert.assertEquals(270, elaichiTeaMachine.ingredientLevel(IngredientType.ELAICHI_SYRUP));
        Assert.assertEquals(90, elaichiTeaMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(true, elaichiTeaMachine.ingredientsRunningLow().isEmpty());

        /**
         *
         * after 1 more dispense water will become 100 and we need atleast 200 to make the
         * beverage.
         *
         */
        output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);

        Assert.assertEquals(true, output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(100, elaichiTeaMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(300, elaichiTeaMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(40, elaichiTeaMachine.ingredientLevel(IngredientType.TEA_LEAVES_SYRUP));
        Assert.assertEquals(240, elaichiTeaMachine.ingredientLevel(IngredientType.ELAICHI_SYRUP));
        Assert.assertEquals(80, elaichiTeaMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));

        /**
         * cannot dispense since water is not sufficient
         */
        output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS) && output.contains("hot_water"));


        elaichiTeaMachine.refillIngredient(IngredientType.WATER, 400);
        elaichiTeaMachine.refillIngredient(IngredientType.MILK, 200);
        elaichiTeaMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, 60);
        elaichiTeaMachine.refillIngredient(IngredientType.ELAICHI_SYRUP, 60);
        elaichiTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 20);
    }

    @Test
    public void testRefillIngredient(){
        Exception e = null;
        try {
            elaichiTeaMachine.refillIngredient(null, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);

        e = null;
        try {
            elaichiTeaMachine.refillIngredient(IngredientType.GREEN_MIXTURE, 20);
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
        Assert.assertEquals(500, elaichiTeaMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(0, elaichiTeaMachine.ingredientLevel(null));
        Assert.assertEquals(0, elaichiTeaMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
    }

    /**
     * test the api which gives the list of ingredient type which are running low on
     * quantity
     * @throws IncorrectIngredientTypeException
     */
    @Test
    public void testIngredientRunningLow() throws IncorrectIngredientTypeException {
        Assert.assertEquals(true, elaichiTeaMachine.ingredientsRunningLow().isEmpty());

        /**
         * dispense a cup of elaichi tea
         */
        String output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);

        /**
         * check parameters after 1 cup dispense of elaichi tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() &&
                output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(true, elaichiTeaMachine.ingredientsRunningLow().isEmpty());

        /**
         *
         * after 1 more dispense water will become 100 and we need atleast 200 to make the
         * beverage.
         *
         */
        output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.PREPARED));
        List<IngredientType> ingredientTypeList = elaichiTeaMachine.ingredientsRunningLow();
        Assert.assertEquals(1, ingredientTypeList.size());
        Assert.assertEquals(IngredientType.WATER, ingredientTypeList.get(0));


        elaichiTeaMachine.refillIngredient(IngredientType.WATER, 400);
        elaichiTeaMachine.refillIngredient(IngredientType.MILK, 200);
        elaichiTeaMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, 60);
        elaichiTeaMachine.refillIngredient(IngredientType.ELAICHI_SYRUP, 60);
        elaichiTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 20);

        //refill the ingredients such that after dispensing a fixed number of cups all
        //quantity is zero
        //total items:     water=500,milk=500,tea=100,elaichi=300,sugar=100
        //recipe for a cup-water=200,milk=100,tea=30,elaichi=30,sugar=10
        //refil ingredients so that we can get 10 cups of elaichi tea and
        //all ingredients gets empty after that.
        elaichiTeaMachine.refillIngredient(IngredientType.WATER, 1500);
        elaichiTeaMachine.refillIngredient(IngredientType.MILK, 500);
        elaichiTeaMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, 200);
        //elaichi syrup is sufficient to prepare 10 cups of elaichi tea
        //sugar syrup is also sufficient to prepare 10 cups of elaichi tea

        for(int i=0;i<10;i++) {
            output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);
            Assert.assertEquals(true, output.contains(BeverageOutputMessage.PREPARED));
        }

        ingredientTypeList = elaichiTeaMachine.ingredientsRunningLow();
        Assert.assertEquals(5, ingredientTypeList.size());
        Assert.assertEquals(true, ingredientTypeList.contains(IngredientType.WATER) &&
                ingredientTypeList.contains(IngredientType.MILK) &&
                ingredientTypeList.contains(IngredientType.TEA_LEAVES_SYRUP) &&
                ingredientTypeList.contains(IngredientType.ELAICHI_SYRUP) &&
                ingredientTypeList.contains(IngredientType.SUGAR_SYRUP));

        // if we try to dispense a cup of ginger tea, it will not
        output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.QTY_NA) &&
                output.contains("hot_water"));

        //refill water less than minimum required limited => the message from
        //ginger tea machine changes
        elaichiTeaMachine.refillIngredient(IngredientType.WATER, 90);
        output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS) &&
                output.contains("hot_water"));
        //bring water level back to 500 from 90
        elaichiTeaMachine.refillIngredient(IngredientType.WATER, 410);
        //if we again try to dispense the tea, the system will respond with
        // "ginger_tea cannot be prepared"
        output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.QTY_NA) &&
                output.contains("hot_milk"));
        elaichiTeaMachine.refillIngredient(IngredientType.MILK, 90);
        output = elaichiTeaMachine.dispense(BeverageType.ELAICHI_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS) &&
                output.contains("hot_milk"));
        //bring milk level back to 500 from 90
        elaichiTeaMachine.refillIngredient(IngredientType.MILK, 410);
        elaichiTeaMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, 100);
        elaichiTeaMachine.refillIngredient(IngredientType.ELAICHI_SYRUP, 300);
        elaichiTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 100);
    }
}
