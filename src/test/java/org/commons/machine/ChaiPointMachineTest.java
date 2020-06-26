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
        setUpHotWaterMachine(inputData);
        setUpHotMilkMachine(inputData);
        setUpGreenTeaMachine(inputData);
        setUpGingerTeaMachine(inputData);
        setUpElaichiTeaMachine(inputData);
        setUpCoffeeMachine(inputData);

        ChaiPointBeverageMachine chaiPointBeverageMachine =
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

    private void setUpHotWaterMachine(InputData inputData) throws BeverageTypeNotSupportedException {
        hotwaterRecipe = inputData.buildBeverageComposition(BeverageType.HOT_WATER);
        waterContainer = inputData.buildIngredientContainer(IngredientType.WATER);
        hotWaterMachine = new HotWaterMachine.Builder()
                .outlet(outlet).beverageRecipe(hotwaterRecipe).waterContainer(waterContainer).build();
    }

    private void setUpHotMilkMachine(InputData inputData) throws BeverageTypeNotSupportedException {
        hotmilkRecipe = inputData.buildBeverageComposition(BeverageType.HOT_MILK);
        milkContainer = inputData.buildIngredientContainer(IngredientType.MILK);
        hotMilkMachine = new HotMilkMachine.Builder()
                .outlet(outlet).beverageRecipe(hotmilkRecipe)
                .milkContainer(milkContainer).build();
    }

    private void setUpGreenTeaMachine(InputData inputData) throws BeverageTypeNotSupportedException {
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

    private void setUpGingerTeaMachine(InputData inputData) throws BeverageTypeNotSupportedException {
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

    private void setUpElaichiTeaMachine(InputData inputData) throws BeverageTypeNotSupportedException {
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

    private void setUpCoffeeMachine(InputData inputData) throws BeverageTypeNotSupportedException {
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
    public void testXYZ(){

    }

}
