package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public interface StorageAbilityBehavior {
    void singleAdd (Resource toBeAdded);
    void singleRemove (Resource toBeRemoved);
    boolean isEmpty();
    EnumMap<Resource, Integer> getContent();
    EnumMap<Resource, Integer> getCapacity();
}
