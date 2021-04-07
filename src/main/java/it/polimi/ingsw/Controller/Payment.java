package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Player.RealPlayer;

import java.util.EnumMap;

public class Payment {

    private final TransactionCatalyst transactionCatalyst;
    RealPlayer player;
    InputManager inputManager;

    public Payment(RealPlayer player, EnumMap<Resource, Integer> goal){
        this.transactionCatalyst = new TransactionCatalyst(goal);
        this.player = player;
        this.inputManager = InputManager.getInstance();
    }

    public void select(String selection){
        if (selection.isEmpty()){
            //potrebbe generare eccezione
            this.transactionCatalyst.commit();
        }
        //potrebbe generare eccezione se non è nel formato corretto
        SelectResourceOutput selectResourceOutput = inputManager.selectResourcesInStorages(selection, this.player);

        EnumMap<Resource, Integer> toAdd = selectResourceOutput.getToAdd();
        EnumMap<Resource, Integer> toRemove = selectResourceOutput.getToRemove();
        Payable storage = selectResourceOutput.getStorage();

        if (toAdd.isEmpty() && toRemove.isEmpty()){
            this.transactionCatalyst.remove(selectResourceOutput.getStorage());
        }
        else{
            //non ancora implementati questi medodi
            this.transactionCatalyst.subtract(storage, toRemove);
            this.transactionCatalyst.add(storage, toAdd);
        }
    }


}
