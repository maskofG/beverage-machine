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
}
