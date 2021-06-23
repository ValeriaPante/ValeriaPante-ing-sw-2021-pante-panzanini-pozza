package it.polimi.ingsw.Model.Abilities.StorageAbility;

import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;

import java.util.EnumMap;

public class WithoutStorageAbilityBehavior implements StorageAbilityBehavior{

    @Override
    public boolean isEmpty() throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }

    @Override
    public void singleAdd(Resource toBeAdded) throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }

    @Override
    public void singleRemove(Resource resource) throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }

    @Override
    public EnumMap<Resource, Integer> getCapacity() throws WrongLeaderCardType {
        throw new WrongLeaderCardType();
    }

    @Override
    public void select(Resource toSelect, int position){
        throw new WrongLeaderCardType();
    }

    @Override
    public EnumMap<Resource, Integer> content(){
        throw new WrongLeaderCardType();
    }

    public boolean contains(EnumMap<Resource, Integer> checkMap) {
        throw new WrongLeaderCardType();
    }

    public void removeSelected() {
        throw new WrongLeaderCardType();
    }

    public void select(Resource resource){
        throw new WrongLeaderCardType();
    }

    public void deselect(Resource resource) {
        throw new WrongLeaderCardType();
    }

    public EnumMap<Resource, Integer> getSelected(){
        throw new WrongLeaderCardType();
    }

    @Override
    public void deselectAll() {
        throw new WrongLeaderCardType();
    }

    @Override
    public int countAll(){
        throw new WrongLeaderCardType();
    }

    @Override
    public String toString(){
        throw new WrongLeaderCardType();
    }

    @Override
    public String toString(int number){
        throw new WrongLeaderCardType();
    }
}
