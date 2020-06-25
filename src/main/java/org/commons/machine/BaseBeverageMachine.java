package org.commons.machine;

import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

import java.util.concurrent.Semaphore;

/**
 * Base Beverage Machine class which implements the functionality
 * of allowing brewing of beverage by "N" people in parallel. Any more
 * than N people will to wait until one spot is empty for brewing coffee.
 *
 * Class that inherits this class decides which beverages it wants to serve
 * and how it wants to implement thread-safety in case of parallel execution
 * of "N" simulataneous request by implementing retrieveBeverageItems(BeverageType)
 *
 */
public abstract class BaseBeverageMachine implements BeverageMachine{

    private Semaphore semaphore;

    public BaseBeverageMachine(int outlet) {
        if (outlet <= 0)
            throw new IllegalArgumentException("number of outlets cannot be negative or zero in beverage machine");

        semaphore = new Semaphore(outlet);
    }

    /**
     * Dispenses the coffee for outlet number of simulataneous beverages
     * It internally calls retrieve beverage Items and it is implemented by
     * concrete beverage machine instance depending on what kind of beverage its
     * serving.
     *
     * @param type is one of the BeverageType beverage
     * @return information if beverage is prepared or not with relevant information
     */
    @Override
    public String dispense(BeverageType type) {
        StringBuilder dispenserResult = new StringBuilder();
        try {
            semaphore.acquire();
            dispenserResult.append(type.name());
            retrieveBeverageItems(type);
            dispenserResult.append(" ").append(BeverageOutputMessage.PREPARED);
        }catch (RequestedQuantityNotPresentException | RequestedQuantityNotSufficientException rqnpe){
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

    /**
     * Retrieve ingredient which is needed to prepare asked beverage type.
     * If a machine doesnot support requested beverage type it can throw
     * @{@link BeverageTypeNotSupportedException}
     * if while retrieving if the machine finds it cannot prepare the beverage because one of
     * the ingredient is either insufficient or is not at all available it can throw
     * @{@link RequestedQuantityNotSufficientException} or
     * @{@link RequestQuantityNotPresentException} respectively
     *
     * @param type is one of the beverage type
     * @throws BeverageTypeNotSupportedException
     * @throws RequestedQuantityNotPresentException
     * @throws RequestedQuantityNotSufficientException
     */
    public abstract void retrieveBeverageItems(BeverageType type)
            throws BeverageTypeNotSupportedException, RequestedQuantityNotPresentException,
            RequestedQuantityNotSufficientException;

}
