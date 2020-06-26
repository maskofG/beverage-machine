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

    @Test
    public void testBuilder() {
        Exception ex = null;
        try {
            ElaichiTeaMachine etm = new ElaichiTeaMachine.Builder().build();
        } catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);
    }

}
