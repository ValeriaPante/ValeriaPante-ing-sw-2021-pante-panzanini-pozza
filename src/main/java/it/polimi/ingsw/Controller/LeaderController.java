package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.ActionToken;
import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Decks.DevDeck;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.CantPutThisHere;
import it.polimi.ingsw.Exceptions.UnsatisfiedRequirements;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;

import java.util.*;

public class LeaderController {
    private Table table;
    private FaithTrackController faithTrackController;

    public LeaderController(){
        this.faithTrackController = FaithTrackController.getInstance();
    }

    //Leader Cards
    //--------------------------------------------------------------------------------------------

    //seleziona la carta
    public void chooseLeaderCard(LeaderCard chosenCard){
        try{
            chosenCard.select();
        } catch (IndexOutOfBoundsException e){
            //messaggio: non esiste questa carta
        }
    }

    //attiva/scarta la carta
    public void actionOnLeaderCard(Boolean discard) throws UnsatisfiedRequirements {
        LeaderCard pickedCard = null;
        for(LeaderCard card: table.turnOf().getLeaderCards())
            if(card.isSelected()){
                pickedCard = card;
                break;
            }

        if(pickedCard != null){
            if (discard){
                table.turnOf().discardLeaderCard(pickedCard);
                FaithTrackController.getInstance().movePlayerOfTurn(table, 1);
            } else if (!pickedCard.hasBeenPlayed()){
                if (!checkRequirements(pickedCard))
                    throw new UnsatisfiedRequirements();
                pickedCard.play();
            }
        }
    }

    private boolean checkRequirements(LeaderCard leaderCardForAction){
        return checkResourceReq(leaderCardForAction) && checkDevCardTypeReq(leaderCardForAction);
    }

    private boolean checkResourceReq(LeaderCard leaderCardForAction){
        // prende il contenuto che hai da tutti
        Depot allResourceOwned = new Depot();
        allResourceOwned.addEnumMap(table.turnOf().getResourcesOwned());
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
    //--------------------------------------------------------------------------------------------
}
