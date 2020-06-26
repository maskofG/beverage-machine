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
 *       "green_mixture": 300
 *     }
 *           "ginger_tea": {
 *         "hot_water": 200,
 *         "hot_milk": 100,
 *         "ginger_syrup": 10,
 *         "sugar_syrup": 10,
 *         "tea_leaves_syrup": 30
 *       }
 */
public class GingerTeaMachineTest {

    private Gson gson = new Gson();
    private String inputFile = getClass().getClassLoader().getResource("input_test.json").getPath();
    private int outlet;
    private BeverageComposition gingerTeaRecipe;
    private IngredientContainer waterContainer;
    private IngredientContainer milkContainer;
    private IngredientContainer teaLeavesContainer;
    private IngredientContainer gingerSyrupContainer;
    private IngredientContainer sugarSyrupContainer;
    private GingerTeaMachine    gingerTeaMachine;



    @Before
    public void setup() throws IOException, BeverageTypeNotSupportedException {
        InputData inputData = gson.fromJson(new FileReader(inputFile), InputData.class);
        outlet = inputData.getOutlet();
        gingerTeaRecipe = inputData.buildBeverageComposition(BeverageType.GINGER_TEA);
        waterContainer = inputData.buildIngredientContainer(IngredientType.WATER);
        milkContainer = inputData.buildIngredientContainer(IngredientType.MILK);
        teaLeavesContainer = inputData.buildIngredientContainer(IngredientType.TEA_LEAVES_SYRUP);
        gingerSyrupContainer = inputData.buildIngredientContainer(IngredientType.GINGER_SYRUP);
        sugarSyrupContainer = inputData.buildIngredientContainer(IngredientType.SUGAR_SYRUP);
        gingerTeaMachine = new GingerTeaMachine.Builder()
                .outlet(outlet).addRecipe(gingerTeaRecipe)
                .addIngredientContainer(waterContainer)
                .addIngredientContainer(milkContainer)
                .addIngredientContainer(teaLeavesContainer)
                .addIngredientContainer(gingerSyrupContainer)
                .addIngredientContainer(sugarSyrupContainer)
                .build();
    }

    /**
     * Testing GingerTeaMachine.Builder class
     */
    @Test
    public void testBuilder() {
        Exception ex = null;
        try {
            GingerTeaMachine gtm = new GingerTeaMachine.Builder().build();
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
        String output = gingerTeaMachine.dispense(BeverageType.HOT_MILK);

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
        String output = gingerTeaMachine.dispense(BeverageType.GINGER_TEA);

        /**
         * check parameters after 1 cup dispense of ginger tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(290, gingerSyrupContainer.quantity());
        Assert.assertEquals(300, gingerTeaMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(400, gingerTeaMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(70, gingerTeaMachine.ingredientLevel(IngredientType.TEA_LEAVES_SYRUP));
        Assert.assertEquals(290, gingerTeaMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
        Assert.assertEquals(90, gingerTeaMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));
        Assert.assertEquals(true, gingerTeaMachine.ingredientsRunningLow().isEmpty());

        /**
         *
         * after 1 more dispense water will become 100 and we need atleast 200 to make the
         * beverage.
         *
         */
        output = gingerTeaMachine.dispense(BeverageType.GINGER_TEA);

        Assert.assertEquals(true, output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(100, gingerTeaMachine.ingredientLevel(IngredientType.WATER));
        Assert.assertEquals(300, gingerTeaMachine.ingredientLevel(IngredientType.MILK));
        Assert.assertEquals(40, gingerTeaMachine.ingredientLevel(IngredientType.TEA_LEAVES_SYRUP));
        Assert.assertEquals(280, gingerTeaMachine.ingredientLevel(IngredientType.GINGER_SYRUP));
        Assert.assertEquals(80, gingerTeaMachine.ingredientLevel(IngredientType.SUGAR_SYRUP));

        /**
         * cannot dispense since water is not sufficient
         */
        output = gingerTeaMachine.dispense(BeverageType.GINGER_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.NOT_PREPARED) &&
                output.contains(BeverageOutputMessage.QTY_NS) && output.contains("hot_water"));


        gingerTeaMachine.refillIngredient(IngredientType.WATER, 400);
        gingerTeaMachine.refillIngredient(IngredientType.MILK, 200);
        gingerTeaMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, 60);
        gingerTeaMachine.refillIngredient(IngredientType.GINGER_SYRUP, 20);
        gingerTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 20);
    }


    @Test
    public void testRefillIngredient(){
        Exception e = null;
        try {
            gingerTeaMachine.refillIngredient(null, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);

        e = null;
        try {
            gingerTeaMachine.refillIngredient(IngredientType.ELAICHI_SYRUP, 20);
        } catch (IncorrectIngredientTypeException iite) {
            e = iite;
        }

        Assert.assertEquals(true, e != null);
    }


    @Test
    public void testIngredientRunningLow() throws IncorrectIngredientTypeException {
        /**
         * initiailly none of the ingredient is running low
         */
        Assert.assertEquals( true, gingerTeaMachine.ingredientsRunningLow().isEmpty());


        /**
         * dispense a cup of ginger tea
         */
        String output = gingerTeaMachine.dispense(BeverageType.GINGER_TEA);

        /**
         * check parameters after 1 cup dispense of ginger tea
         */
        Assert.assertEquals(true, output!=null);
        Assert.assertEquals(true, !output.isEmpty() && output.contains(BeverageOutputMessage.PREPARED));
        Assert.assertEquals(true, gingerTeaMachine.ingredientsRunningLow().isEmpty());

        /**
         *
         * after 1 more dispense water will become 100 and we need atleast 200 to make the
         * beverage.
         *
         */
        output = gingerTeaMachine.dispense(BeverageType.GINGER_TEA);
        Assert.assertEquals(true, output.contains(BeverageOutputMessage.PREPARED));
        List<IngredientType> ingredientTypeList = gingerTeaMachine.ingredientsRunningLow();
        Assert.assertEquals(false, ingredientTypeList.isEmpty());
        Assert.assertEquals(1, ingredientTypeList.size());
        Assert.assertEquals(IngredientType.WATER, ingredientTypeList.get(0));


        gingerTeaMachine.refillIngredient(IngredientType.WATER, 400);
        gingerTeaMachine.refillIngredient(IngredientType.MILK, 200);
        gingerTeaMachine.refillIngredient(IngredientType.TEA_LEAVES_SYRUP, 60);
        gingerTeaMachine.refillIngredient(IngredientType.GINGER_SYRUP, 20);
        gingerTeaMachine.refillIngredient(IngredientType.SUGAR_SYRUP, 20);
    }

}
