package it.polimi.ingsw.Model.Deposit;

import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

/**
 * This interface is used for removing the resources selected in the object implementing it
 * and knowing if a container contains an enumMap
 */
public interface Payable {
    /**
     * Checks if the payable object contains an enumMap of resources
     * @param checkMap target enumMap
     * @return true if the payable object contains the same or more resources of the type and quantity
     * specified in the enumMap passed as a parameter
     */
    boolean contains(EnumMap<Resource, Integer> checkMap);

    /**
     * Removes all the selected resources from the payable object
     */
    void pay();
}
