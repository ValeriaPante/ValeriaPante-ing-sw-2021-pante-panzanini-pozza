package it.polimi.ingsw.Abilities.StorageAbility;

import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;

import java.util.EnumMap;

public class WithoutStorageAbilityBehavior implements StorageAbilityBehavior{

    @Override
    public boolean isEmpty() throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
        //Exception
    }

    @Override
    public void singleAdd(Resource toBeAdded) throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
    }

    @Override
    public void singleRemove(Resource resource) throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
        //Exception or empty
    }

    @Override
    public EnumMap<Resource, Integer> getCapacity() throws WeDontDoSuchThingsHere{
        throw new WeDontDoSuchThingsHere();
        //return null;
    }

    @Override
    public EnumMap<Resource, Integer> content(){
        throw new WeDontDoSuchThingsHere();
    }

    public boolean contains(EnumMap<Resource, Integer> checkMap) {
        throw new WeDontDoSuchThingsHere();
    }

    public void pay(EnumMap<Resource, Integer> removeMap) {
        throw new WeDontDoSuchThingsHere();
    }
}
