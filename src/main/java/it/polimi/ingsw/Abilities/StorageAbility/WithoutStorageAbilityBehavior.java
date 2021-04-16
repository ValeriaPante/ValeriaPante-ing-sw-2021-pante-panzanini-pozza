package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;

import java.util.EnumMap;

public class WithoutStorageAbilityBehavior implements StorageAbilityBehavior{

    @Override
    public boolean isEmpty() throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
    }

    @Override
    public void singleAdd(Resource toBeAdded) throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
    }

    @Override
    public void singleRemove(Resource resource) throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
    }

    @Override
    public EnumMap<Resource, Integer> getCapacity() throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
    }

    @Override
    public void select(Resource toSelect, int position){
        throw new WeDontDoSuchThingsHere();
    }

    @Override
    public EnumMap<Resource, Integer> content(){
        throw new WeDontDoSuchThingsHere();
    }

    public boolean contains(EnumMap<Resource, Integer> checkMap) {
        throw new WeDontDoSuchThingsHere();
    }

    public void removeSelected() {
        throw new WeDontDoSuchThingsHere();
    }

    public void select(Resource resource){
        throw new WeDontDoSuchThingsHere();
    }

    public void deselect(Resource resource) {
        throw new WeDontDoSuchThingsHere();
    }

    public EnumMap<Resource, Integer> getSelected(){
        throw new WeDontDoSuchThingsHere();
    }

    @Override
    public String toString(){
        throw new WeDontDoSuchThingsHere();
    }

    @Override
    public String toString(int number){
        throw new WeDontDoSuchThingsHere();
    }
}
