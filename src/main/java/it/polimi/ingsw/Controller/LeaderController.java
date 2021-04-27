package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Exceptions.BrokenPlayerException;
import it.polimi.ingsw.Game.Table;

import java.util.*;

public class LeaderController extends SelectionController{
    private final FaithTrackController faithTrackController;

    public LeaderController(Table table) {
        super(table);
        this.faithTrackController = new FaithTrackController(table);
    }

    //seleziona la carta
    public void chooseLeaderCard(int id){
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        LeaderCard chosenCard = this.leaderCardFromID(id);
        if(chosenCard != null) chosenCard.select();
    }

    //attiva/scarta la carta
    public void actionOnLeaderCard(Boolean discard) {
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        LeaderCard pickedCard = null;
        for(LeaderCard card: table.turnOf().getLeaderCards())
            if(card.isSelected()){
                pickedCard = card;
                break;
            }

        if(pickedCard != null){
            if (discard){
                table.turnOf().discardLeaderCard(pickedCard);
                faithTrackController.movePlayerOfTurn(1);
            } else if (!pickedCard.hasBeenPlayed()){
                if (!checkRequirements(pickedCard))
                    table.turnOf().setErrorMessage("You don't have the requirements needed. ");
                else pickedCard.play();
            }
        }
    }

    private boolean checkRequirements(LeaderCard leaderCardForAction){
        return checkResourceReq(leaderCardForAction) && checkDevCardTypeReq(leaderCardForAction);
    }

    private boolean checkResourceReq(LeaderCard leaderCardForAction){
        // prende il contenuto che hai da tutti
        Depot allResourceOwned = new Depot();
        try {
            allResourceOwned.addEnumMap(table.turnOf().getResourcesOwned());
        } catch (BrokenPlayerException e){
            return leaderCardForAction.getResourceReq().isEmpty();
        }
        return allResourceOwned.contains(leaderCardForAction.getResourceReq());
    }

    private boolean checkDevCardTypeReq(LeaderCard leaderCardForAction){
        boolean devCardReq = true;
        if(!leaderCardForAction.getDevCardReq().isEmpty()){
            ArrayList<DevCardType> ownedDevCard = new ArrayList<>();
            for(int i = 0; i < table.turnOf().getDevSlots().length; i++)
                ownedDevCard.addAll(table.turnOf().getDevSlots()[i].getDevCardTypeContained());
            int sum;
            for (Map.Entry<DevCardType, Integer> entry : leaderCardForAction.getDevCardReq().entrySet()) {
                sum = 0;
                for (DevCardType devCardType : ownedDevCard) {
                    if ((entry.getKey().getLevel() == 0 && devCardType.getColor() == entry.getKey().getColor()) || (entry.getKey().getLevel() <= devCardType.getLevel() && devCardType.getColor() == entry.getKey().getColor()))
                        sum++;
                }
                if (sum != entry.getValue()){
                    devCardReq = false;
                    break;
                }
            }
        }
        return devCardReq;
    }
}
