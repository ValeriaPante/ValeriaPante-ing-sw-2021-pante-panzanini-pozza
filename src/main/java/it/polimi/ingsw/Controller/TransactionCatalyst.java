package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;
import java.util.Map;

//this class may become a lambda since there are no attributes needed.
// My only concern is about deleting the exceptions, it may be difficult to distinguish each case

public class TransactionCatalyst {

    public boolean isAffordable(Map<Payable, EnumMap<Resource, Integer>> payingMethod) throws NullPointerException,IllegalArgumentException{
        if (payingMethod == null)
            throw new NullPointerException();

        if (payingMethod.isEmpty())
            throw new IllegalArgumentException();

        for (Map.Entry<Payable, EnumMap<Resource, Integer>> elementOfMap : payingMethod.entrySet())
            if ( !(elementOfMap.getKey().contains(elementOfMap.getValue())) )
                return false;

       return true;
    }

    public void commit(Map<Payable, EnumMap<Resource, Integer>> payingMethod) throws NullPointerException,IllegalArgumentException{
        if (payingMethod == null)
            throw new NullPointerException();

        if (payingMethod.isEmpty())
            throw new IllegalArgumentException();

        for (Map.Entry<Payable, EnumMap<Resource, Integer>> elementOfMap : payingMethod.entrySet())
            elementOfMap.getKey().pay(elementOfMap.getValue());

    }
}
