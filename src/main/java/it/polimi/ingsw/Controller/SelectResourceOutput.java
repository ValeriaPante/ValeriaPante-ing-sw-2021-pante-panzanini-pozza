package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;

import java.util.EnumMap;

public class SelectResourceOutput {
    private Payable storage;
    private EnumMap<Resource, Integer> toAdd;
    private EnumMap<Resource, Integer> toRemove;

    public SelectResourceOutput(Payable storage, EnumMap<Resource, Integer> toAdd, EnumMap<Resource, Integer> toRemove){
        this.storage = storage;
        this.toAdd = toAdd;
        this.toRemove = toRemove;
    }

    public Payable getStorage() {
        return this.storage;
    }
    public EnumMap<Resource, Integer> getToAdd() {
        return this.toAdd.clone();
    }
    public EnumMap<Resource, Integer> getToRemove() {
        return this.toRemove.clone();
    }
}
