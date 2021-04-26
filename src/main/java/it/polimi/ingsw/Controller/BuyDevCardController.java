package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Decks.DevDeck;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.CantPutThisHere;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Deposit.StrongBox;

import java.util.EnumMap;

public class BuyDevCardController extends CardActionController{
    private Table table;

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
                StrongBox temp = table.turnOf().getSupportContainer();
                temp.clear();
                temp.addEnumMap(deck.getTopCard().getCost());
                break;
            }
        }
    }

    public void applyDiscountAbility(LeaderCard card){

        EnumMap<Resource, Integer> toBePaid = table.turnOf().getSupportContainer().content();
        try {
            for (EnumMap.Entry<Resource, Integer> entry : toBePaid.entrySet())
                toBePaid.put(entry.getKey(), entry.getValue() - ((card.getAbility().getDiscount().get(entry.getKey()) != null)? card.getAbility().getDiscount().get(entry.getKey()): 0));
            table.turnOf().getSupportContainer().clear();
            table.turnOf().getSupportContainer().addEnumMap(toBePaid);
        } catch (WeDontDoSuchThingsHere e){
            //messaggio: questa leadercard non ha questo potere
        }

    }

    public void paySelected(){
        try{
            //chiama uno dei metodi comuni
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
}
