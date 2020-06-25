package org.commons.machine;

import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.RequestQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

import java.util.concurrent.Semaphore;

public abstract class BaseBeverageMachine implements BeverageMachine{

    private Semaphore semaphore;

    public BaseBeverageMachine(int outlet) {
        if (outlet <= 0)
            throw new IllegalArgumentException("number of outlets cannot be negative or zero in beverage machine");

        semaphore = new Semaphore(outlet);
    }

    @Override
    public String dispense(BeverageType type) {
        StringBuilder dispenserResult = new StringBuilder();
        try {
            semaphore.acquire();
            dispenserResult.append(type.name());
            retrieveBeverageItems(type);
            dispenserResult.append(" ").append(BeverageOutputMessage.PREPARED);
        }catch (RequestQuantityNotPresentException | RequestedQuantityNotSufficientException rqnpe){
            dispenserResult.append(" ").append(BeverageOutputMessage.NOT_PREPARED).append(" ")
                    .append("because").append(" ").append(rqnpe.getMessage());
        } catch (BeverageTypeNotSupportedException btns) {
            dispenserResult.append(" ").append(BeverageOutputMessage.NOT_PREPARED).append(" ")
                    .append("because").append(" ").append(btns.getMessage());
        } catch (InterruptedException e) {
            dispenserResult.append(" ").append(BeverageOutputMessage.NOT_PREPARED).append(" ")
                    .append("because").append(" ").append("of some machine issue");
        } finally {
            semaphore.release();
            return dispenserResult.toString();
        }
    }

    public abstract void retrieveBeverageItems(BeverageType type)
            throws BeverageTypeNotSupportedException, RequestQuantityNotPresentException, RequestedQuantityNotSufficientException;

}
