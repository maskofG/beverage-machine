package org.commons.machine;

import org.commons.ingredients.IngredientType;
import org.exceptions.IncorrectIngredientTypeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.concurrent.Callable;


public class BaseBaverageMachineTest {
    private BaseBeverageMachine baseBeverageMachine;
    private Thread t1;
    private Thread t2;
    private Thread t3;
    @Before
    public void setup(){
        baseBeverageMachine = instantiateBaseBeverageMachine(2);
    }

    public BaseBeverageMachine instantiateBaseBeverageMachine(int outlet) {
        return new BaseBeverageMachine(outlet) {
            @Override
            public void brew(BeverageType type) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("Thread is interrupt. But it comes out of wait");
                }
            }

            @Override
            public int ingredientLevel(IngredientType type) {
                return 0;
            }

            @Override
            public void refillIngredient(IngredientType type, int amount)
                    throws IncorrectIngredientTypeException {

            }

            @Override
            public List<IngredientType> ingredientsRunningLow() {
                return null;
            }
        };
    }

    @Test
    public void testNOutlet() throws InterruptedException {
        Runnable r1 = getRunnable();
        Runnable r2 = getRunnable();
        Runnable r3 = getRunnable();
        t1 = new Thread(r1);
        t2 = new Thread(r2);
        t3 = new Thread(r3);
        t1.start();
        Thread.sleep(1000);
        t2.start();
        Thread.sleep(1000);
        t3.start();
        Thread.sleep(1000);

        Assert.assertEquals(Thread.State.WAITING, t3.getState());

        t1.interrupt();
        Thread.sleep(1000);
        Assert.assertNotEquals(Thread.State.WAITING, t3.getState());

        t2.interrupt();
        t3.interrupt();
    }

    public Runnable getRunnable() {
        Runnable rx = () -> baseBeverageMachine.dispense(BeverageType.HOT_WATER);
        return rx;
    }
    public Callable<String> getCallable() {
        Callable rx =() -> {
            return baseBeverageMachine.dispense(BeverageType.HOT_WATER);
        };
        return rx;
    }


    @Test
    public void testNegativeOutlet() {
        Exception ex = null;
        try {
            BaseBeverageMachine baseBeverageMachine = instantiateBaseBeverageMachine(-2);
        } catch (IllegalArgumentException ile) {
            ex = ile;
        }

        Assert.assertEquals(true, ex != null);
    }

}
