package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Deposit.Shelf;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Player.RealPlayer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class CardActionController extends SelectionController{

    public CardActionController(Table table, FaithTrackController ftc){
        super(table, ftc);
    }

    //ritorna true se esite una possibile conbinazione per la quale il giocatore può permettersi la checkMap
    //tiene in considerazione anche la possibilità di any in checkmap
    protected boolean isAffordableSomehow(EnumMap<Resource, Integer> checkMap) {
        Depot allResources = new Depot() {{
            addEnumMap(table.turnOf().getResourcesOwned());
        }};
        EnumMap<Resource, Integer> copy = checkMap.clone();

        if (!checkMap.containsKey(Resource.ANY)) {
            return allResources.contains(checkMap);
        }

        //qui in poi ho le any
        int anyAmount = copy.get(Resource.ANY);
        copy.remove(Resource.ANY);
        int otherResourcesAmount = 0;
        for (int value : copy.values()) {
            otherResourcesAmount += value;
        }
        return (allResources.contains(copy) && allResources.countAll() >= otherResourcesAmount + anyAmount);
    }

    //return a List<Payable> with selection on those Payable
    protected List<Payable> getPayableWithSelection(){
        RealPlayer player = this.table.turnOf();
        ArrayList<Payable> payableWithSelection = new ArrayList<>();

        for (Shelf shelf : player.getShelves()){
            if (!shelf.isEmpty() && shelf.getQuantitySelected()!=0){
                payableWithSelection.add(shelf);
            }
        }

        if (player.getStrongBox().areThereSelections()){
            payableWithSelection.add(player.getStrongBox());
        }

        for (LeaderCard leaderCard : player.getLeaderCards()){
            if (leaderCard.hasBeenPlayed()){
                try{
                    if (!leaderCard.getAbility().getSelected().isEmpty()){
                        payableWithSelection.add(leaderCard.getAbility());
                    }
                }
                catch (WeDontDoSuchThingsHere e){
                    //non è una carta con potere di storage
                }
            }
        }

        return payableWithSelection;
    }


}
