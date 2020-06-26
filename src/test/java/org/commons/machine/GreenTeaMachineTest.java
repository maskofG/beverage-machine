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

    @Test
    public void testBuilder() {
        Exception ex = null;
        try {
            GreenTeaMachine gtm = new GreenTeaMachine.Builder().build();
        }catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);
    }
}
