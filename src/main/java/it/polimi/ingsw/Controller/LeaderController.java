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
    private final List<String> players;

    public LeaderController(){
        this.players = new ArrayList<>();
        this.faithTrackController = FaithTrackController.getInstance();
    }

    public void addNewPlayer(String playerName){
        players.add(playerName);
    }

    public void removePlayer(String playerName){
        players.remove(playerName);
    }


    //End turn
    //--------------------------------------------------------------------------------------------
    public void endTurn(){
        if(table.isSinglePlayer()){
            table.nextTurn();
            if(anEntireLineIsEmpty()){
                table.setLastLap();
            } else {
                playActionToken(table.getLorenzo().getActionTokenDeck().draw());
                if(anEntireLineIsEmpty()) table.setLastLap();
            }
        }
        table.nextTurn();
        if((table.isLastLap()) && (table.getPlayers()[0] == table.turnOf()))
            endGame();
    }

    public void endGame(){
        //calcola il vincitore e mostra il messaggio
    }

    private void playActionToken(ActionToken token){
        switch (token.getType()){
            case TWOFP:
                faithTrackController.movePlayerOfTurn(this.table, 2);
                break;
            case RESETDECKONEFP:
                faithTrackController.movePlayerOfTurn(this.table, 1);
                table.getLorenzo().getActionTokenDeck().reset();
                break;
            case DISCARDGREEN:
                discardDevCards(0);
                break;
            case DISCARDYELLOW:
                discardDevCards(1);
                break;
            case DISCARDBLUE:
                discardDevCards(2);
                break;
            case DISCARDPURPLE:
                discardDevCards(3);
                break;
        }
    }

    private void discardDevCards(int color){
        int cardsToDiscard = 2;
        int level = 0;
        ArrayList<DevDeck> lineOfDecks = getLineOfDecks(color);
        while(cardsToDiscard > 0 && level < 3){
            if (lineOfDecks.get(level).size() > 1){
                lineOfDecks.get(level).draw();
                cardsToDiscard--;
            } else if (lineOfDecks.get(level).size() == 1) {
                lineOfDecks.get(level).draw();
                cardsToDiscard--;
                level++;
            } else {
                level++;
            }
        }
    }

    private boolean anEntireLineIsEmpty(){
        for(int i = 0; i < 4; i++){
            if (getLineOfDecks(i).isEmpty()) return true;
        }
        return false;
    }

    private ArrayList<DevDeck> getLineOfDecks(int color){
        ArrayList<DevDeck> lineOfDecks = new ArrayList<>();
        lineOfDecks.add(table.getDevDecks()[color]);
        lineOfDecks.add(table.getDevDecks()[color + 4]);
        lineOfDecks.add(table.getDevDecks()[color + 8]);
        return lineOfDecks;
    }
    //--------------------------------------------------------------------------------------------

    //Buy Dev Card
    //--------------------------------------------------------------------------------------------
    public void chooseDevCard(int chosenDeck){
        try {
            if(!table.getDevDecks()[chosenDeck - 1].isEmpty()) {
                if (atLeastOneDevSlotIsAvailable(table.getDevDecks()[chosenDeck - 1].getTopCard())){
                    //messaggio: non hai dove mettere questa carta
                } else {
                    //deseleziona un eventuale deck che era stato selezionato prima
                    for(DevDeck deck: table.getDevDecks()){
                        if(deck.getTopCard().isSelected())
                            deck.selectTopCard();
                    }
                    //seleziona deck scelto
                    table.getDevDecks()[chosenDeck - 1].selectTopCard();
                }
            } else {
                //messaggio: questo mazzetto è vuoto
            }
        } catch (IndexOutOfBoundsException e){
            //messaggio: non esiste questo mazzetto
        }
    }

    private boolean atLeastOneDevSlotIsAvailable(DevCard card){
        boolean result = false;
        for(int i = 0; i < table.turnOf().getDevSlots().length; i++)
            result = result || table.turnOf().getDevSlots()[i].isInsertable(card);
        return result;
    }

    public void buyDevCard(){
        table.turnOf().setMacroTurnType(MacroTurnType.BUYNEWCARD);
        for(DevDeck deck: table.getDevDecks()){
            if(deck.getTopCard().isSelected()) {
                table.turnOf().setToPay(deck.getTopCard().getCost());
                break;
            }
        }
    }

    public void applyDiscountAbility(LeaderCard card){

        EnumMap<Resource, Integer> toBePaid = table.turnOf().getToPay();
        try {
            for (EnumMap.Entry<Resource, Integer> entry : toBePaid.entrySet())
                toBePaid.put(entry.getKey(), entry.getValue() - ((card.getAbility().getDiscount().get(entry.getKey()) != null)? card.getAbility().getDiscount().get(entry.getKey()): 0));
            table.turnOf().setToPay(toBePaid);
        } catch (WeDontDoSuchThingsHere e){
            //messaggio: questa leadercard non ha questo potere
        }

    }

    public void selectHowToPay(){
        //seleziona la risorsa scelta
    }

    public void paySelected(){
        try{
            //per tutti i depositi, pay
        } catch(IndexOutOfBoundsException e){
            //messaggio: non hai le risorse necessare
        }

    }

    public void chooseDevSlot(int numberOfSlot){
        DevDeck chosenDeck = null;
        for(DevDeck deck: table.getDevDecks()){
            if(deck.getTopCard().isSelected()) {
                chosenDeck = deck;
                break;
            }
        }
        if(chosenDeck != null){
            try {
                table.turnOf().getDevSlots()[numberOfSlot - 1].addCard(chosenDeck.getTopCard());
                chosenDeck.selectTopCard();
                chosenDeck.draw();
                if(table.turnOf().getNumberOfDevCardOwned() == 7) table.setLastLap();
            } catch (CantPutThisHere e) {
                //messaggio: non puoi mettere questa carta qui
            } catch (IndexOutOfBoundsException e) {
                //messaggio: non esiste questo numero di slot
            }
        }
    }

    //--------------------------------------------------------------------------------------------

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

    //Transmutation: da rifare
    private void applyTransmutationAbility(LeaderCard leaderCardForAction, EnumMap<Resource, Integer> toBePlaced){
        if(toBePlaced.get(Resource.WHITE) == null || toBePlaced.get(Resource.WHITE) == 0){
            //messaggio: non hai palline bianche
        } else {
            for (EnumMap.Entry<Resource, Integer> entry : leaderCardForAction.getAbility().getWhiteInto().entrySet())
                toBePlaced.put(entry.getKey(), toBePlaced.get(entry.getKey())+ entry.getValue()*toBePlaced.get(Resource.WHITE));
            toBePlaced.remove(Resource.WHITE);
        }
    }

    private void applyTransmutationAbility(LeaderCard leaderCardForAction, EnumMap<Resource, Integer> toBePlaced, int quantity){
        for (EnumMap.Entry<Resource, Integer> entry : leaderCardForAction.getAbility().getWhiteInto().entrySet())
            toBePlaced.put(entry.getKey(), toBePlaced.get(entry.getKey())+ entry.getValue()*quantity);
    }

    private void applyTransmutationAbility(LeaderCard leaderCardForAction1, LeaderCard leaderCardForAction2, EnumMap<Resource, Integer> toBePlaced, int firstQuantity, int secondQuantity) throws IndexOutOfBoundsException{
        if(toBePlaced.get(Resource.WHITE) == null || toBePlaced.get(Resource.WHITE) == 0){
            //messaggio: non hai palline bianche
        } else {
            while(firstQuantity + secondQuantity != toBePlaced.get(Resource.WHITE)){
                System.out.println("The sum doesn't match with the number of whites you gained.");
                throw new IndexOutOfBoundsException();
            }

            applyTransmutationAbility(leaderCardForAction1, toBePlaced, firstQuantity);
            applyTransmutationAbility(leaderCardForAction2, toBePlaced, secondQuantity);
            toBePlaced.remove(Resource.WHITE);

        }
    }
}
