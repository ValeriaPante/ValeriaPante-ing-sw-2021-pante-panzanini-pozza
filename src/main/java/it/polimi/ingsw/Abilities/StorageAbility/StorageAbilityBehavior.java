package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public interface StorageAbilityBehavior{
    void singleAdd (Resource toBeAdded);
    void singleRemove (Resource toBeRemoved);
    boolean isEmpty();
    EnumMap<Resource, Integer> content();
    EnumMap<Resource, Integer> getCapacity();
    boolean contains(EnumMap<Resource, Integer> checkMap);
    void pay(EnumMap<Resource, Integer> removeMap);
}
