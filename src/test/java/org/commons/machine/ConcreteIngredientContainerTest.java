package org.commons.machine;

import org.commons.ingredients.ConcreteIngredientContainer;
import org.commons.ingredients.IngredientType;
import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;
import org.junit.Assert;
import org.junit.Test;

public class ConcreteIngredientContainerTest {

    /**
     * This method test all the apis exposed by concrete ingredients
     * @throws RequestedQuantityNotSufficientException
     * @throws RequestedQuantityNotPresentException
     */
    @Test
    public void testAPI() throws RequestedQuantityNotSufficientException,
            RequestedQuantityNotPresentException {
        ConcreteIngredientContainer cic = new ConcreteIngredientContainer(IngredientType.WATER, 20);

        Assert.assertEquals(IngredientType.WATER, cic.type());
        Assert.assertEquals(20, cic.quantity());

        cic.retrieve(10);
        Assert.assertEquals(10, cic.quantity());

        Exception ex = null;
        try {
            cic.retrieve(20);
        } catch (RequestedQuantityNotSufficientException rqns) {
            ex = rqns;
        }

        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cic.check(20);
        } catch (RequestedQuantityNotSufficientException rqns){
            ex = rqns;
        }

        Assert.assertEquals(true, ex != null);

        cic.retrieve(10);

        Assert.assertEquals(0, cic.quantity());

        try {
            ex = null;
            cic.retrieve(10);
        } catch (RequestedQuantityNotPresentException rqnp) {
            ex = rqnp;
        }

        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cic = new ConcreteIngredientContainer(null, 20);
        } catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);

        try {
            ex = null;
            cic = new ConcreteIngredientContainer(IngredientType.WATER, -20);
        } catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);
    }
}
