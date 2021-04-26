package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public interface StorageAbilityBehavior{
    void singleAdd (Resource toBeAdded);
    void singleRemove (Resource toBeRemoved);
    boolean isEmpty();
    EnumMap<Resource, Integer> content();
    EnumMap<Resource, Integer> getCapacity();
    boolean contains(EnumMap<Resource, Integer> checkMap);
    void removeSelected();
    void select(Resource toSelect, int position);
    EnumMap<Resource, Integer> getSelected();
    void deselectAll();
    String toString();
    //per sapere se è il primo o il secondo
    String toString(int number);
}
