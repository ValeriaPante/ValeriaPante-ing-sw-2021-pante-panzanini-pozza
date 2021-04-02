package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;

import java.util.EnumMap;

public class WithoutStorageAbilityBehavior implements StorageAbilityBehavior{

    public boolean isEmpty() throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
        //Exception
    }

    public void singleAdd(Resource toBeAdded) throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
    }

    public void singleRemove(Resource resource) throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
        //Exception or empty
    }

    public EnumMap<Resource, Integer> getCapacity() throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
        //return null;
    }

    public EnumMap<Resource, Integer> getContent(){
        throw new WeDontDoSuchThingsHere();
    }

}
