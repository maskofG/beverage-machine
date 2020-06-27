package org.commons.machine;

import org.exceptions.BeverageTypeNotSupportedException;
import org.exceptions.RequestedQuantityNotPresentException;
import org.exceptions.RequestedQuantityNotSufficientException;

import java.util.concurrent.Semaphore;

/**
 *
 * Base beverage machine = Bare metal beverage machine +
 *                         Noutlet beverage dispensing module +
 *                         plugs for connecting brewing setup
 *                         for different beverages
 *
 * Brewing setup can be plugged with multiple pluggable
 * ingredients container.
 *
 * So this bare metal Beverage Machine class implements the functionality
 * of allowing parallel dispensing of beverage for "N" people. Any more
 * than N people will have to wait until one spot is empty for dispensing
 * beverage.
 *
 * concrete class can inherit this class to implement their brewing module/setup
 * and this brewing module can be plugged with multiple ingredients container
 * depending on type of beverage we want to brew.
 * The concrete will brew a beverage and serve beverage to N people in
 * parallel.
 *
 */
public abstract class BaseBeverageMachine implements BeverageMachine{

    private Semaphore semaphore;

    public BaseBeverageMachine(int outlet) {
        if (outlet <= 0) {
            throw new IllegalArgumentException("number of outlets cannot be negative or zero in beverage machine");
        }

        semaphore = new Semaphore(outlet, true);
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
            dispenserResult.append(type.getFieldDescriptor());
            brew(type);
            dispenserResult.append(" ").append(BeverageOutputMessage.PREPARED);
        }catch (RequestedQuantityNotPresentException | RequestedQuantityNotSufficientException rqnpe){
            dispenserResult.append(" ").append(BeverageOutputMessage.NOT_PREPARED).append(" ")
                    .append("because").append(" ").append(rqnpe.getMessage());
        } catch (BeverageTypeNotSupportedException btns) {
            dispenserResult.append(" ").append(BeverageOutputMessage.NOT_PREPARED).append(" ")
                    .append("because").append(" ").append(btns.getMessage());
        } catch (Exception e){
            dispenserResult.append(" ").append(BeverageOutputMessage.NOT_PREPARED).append(" ")
                    .append("because").append(" ").append(e.getMessage());
        }finally {
            semaphore.release();
            return dispenserResult.toString();
        }
    }

    /**
     * Retrieve ingredient and brew ingredients (No exactly brewing as of now) which is
     * needed to prepare asked beverage type.
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
    public abstract void brew(BeverageType type)
            throws BeverageTypeNotSupportedException, RequestedQuantityNotPresentException,
            RequestedQuantityNotSufficientException;

}
