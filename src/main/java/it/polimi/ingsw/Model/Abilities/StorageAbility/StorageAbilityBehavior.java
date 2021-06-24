package it.polimi.ingsw.Model.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

/**
 * Representation of the Ability Storage of LeaderCards
 */
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
    int countAll();
    String toString();
    String toString(int number);
}
