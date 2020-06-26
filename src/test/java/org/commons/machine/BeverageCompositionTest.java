package org.commons.machine;

import org.commons.ingredients.IngredientType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeverageCompositionTest {

    @Test
    public void testPutAndGet(){
        BeverageComposition beverageComposition = new BeverageComposition();

        Exception ex = null;
        try {
            beverageComposition.put(IngredientType.MILK, -10);
        } catch (IllegalArgumentException ila) {
            ex = ila;
        }

        Assert.assertEquals(true, ex != null);

        ex = null;
        try {
            beverageComposition.put(null, 10);
        } catch (IllegalArgumentException ila) {
            ex = ila;
        }
        Assert.assertEquals(true, ex != null);

        beverageComposition.put(IngredientType.MILK, 10);

        Assert.assertEquals(10, beverageComposition.getQuantity(IngredientType.MILK));
    }
}
