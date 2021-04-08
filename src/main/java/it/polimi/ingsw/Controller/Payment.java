package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.UnsatisfiedRequirements;
import it.polimi.ingsw.Player.RealPlayer;

import java.util.EnumMap;
import java.util.Map;

public class Payment {

    private TransactionCatalyst transactionCatalyst;
    RealPlayer player;
    InputManager inputManager;

    public Payment(RealPlayer player, EnumMap<Resource, Integer> goal) throws UnsatisfiedRequirements{
        this.player = player;
        if (this.isAffordableSomehow(goal)){
            this.transactionCatalyst = new TransactionCatalyst(goal);
            this.inputManager = InputManager.getInstance();
        }
        else{
            throw new UnsatisfiedRequirements();
        }
    }

    public void select(String selection){
        //potrebbe generare eccezione se non è nel formato corretto
        SelectResourceOutput selectResourceOutput = inputManager.selectResourcesInStorages(selection, this.player);

        EnumMap<Resource, Integer> toAdd = selectResourceOutput.getToAdd();
        EnumMap<Resource, Integer> toRemove = selectResourceOutput.getToRemove();
        Payable storage = selectResourceOutput.getStorage();

        if (toAdd.isEmpty() && toRemove.isEmpty()){
            this.transactionCatalyst.remove(selectResourceOutput.getStorage());
        }
        else{
            this.transactionCatalyst.subtract(storage, toRemove);
            this.transactionCatalyst.add(storage, toAdd);
        }
    }

    //in sostanza fa un reset ma rimane legato al player
    //utilizzato nel caso se il giocatore dopo aver fatto delle selzioni vuole cambiare
    //cosa comprare
    public void changeGoal(EnumMap<Resource, Integer> newGoal) throws UnsatisfiedRequirements {
        if (this.isAffordableSomehow(newGoal)) {
            this.transactionCatalyst = new TransactionCatalyst(newGoal);
        }
        else{
            throw new UnsatisfiedRequirements();
        }
    }

    //mi serve carire se in qualche combinazione di risorse
    //si può permettere di comprare quello che chiede
    private boolean isAffordableSomehow(EnumMap<Resource, Integer> goal){
        Depot allResources = new Depot();
        allResources.addEnumMap(this.player.getResourcesOwned());
        return allResources.contains(goal);
    }

    public Map<Payable, EnumMap<Resource, Integer>> selected(){
        return this.transactionCatalyst.getContent();
    }

    public void pay(){
        //potrebbe generare eccezione
        this.transactionCatalyst.commit();
    }


}
