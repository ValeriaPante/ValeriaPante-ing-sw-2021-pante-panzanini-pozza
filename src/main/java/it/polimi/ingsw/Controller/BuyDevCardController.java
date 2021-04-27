package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.LeaderCard;
import it.polimi.ingsw.Decks.DevDeck;
import it.polimi.ingsw.Deposit.Depot;
import it.polimi.ingsw.Deposit.Payable;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.CantPutThisHere;
import it.polimi.ingsw.Exceptions.WeDontDoSuchThingsHere;
import it.polimi.ingsw.Game.Table;
import it.polimi.ingsw.Deposit.StrongBox;

import java.util.EnumMap;

public class BuyDevCardController extends CardActionController{

    public BuyDevCardController(Table table){ super(table);}

    public void chooseDevCard(int chosenDeck){
        try {
            if(!table.getDevDecks()[chosenDeck - 1].isEmpty()) {
                if (atLeastOneDevSlotIsAvailable(table.getDevDecks()[chosenDeck - 1].getTopCard())){
                    table.turnOf().setErrorMessage("You can't buy this card, there is no slot to contain it. ");
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
                table.turnOf().setErrorMessage("Wrong selection: this deck is empty. ");
            }
        } catch (IndexOutOfBoundsException e){
            table.turnOf().setErrorMessage("Wrong selection: There is no such deck. ");
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
            table.turnOf().setErrorMessage("This Leader Card has not a discount ability. ");
        }

    }

    public void paySelected(){
        try{
            if(!this.isEnough()) table.turnOf().setErrorMessage("Your selection doesn't match the cost. You selected too many resources.");
        } catch (IndexOutOfBoundsException e){
            table.turnOf().setErrorMessage("Your selection doesn't match the cost. You selected too few resources. ");
        }

        for(Payable payable: this.getPayableWithSelection())
            payable.pay();
    }


    //controlla che le risorse selezionate siano uguali al costo della carta
    private boolean isEnough(){
        Depot temp = new Depot();
        EnumMap<Resource, Integer> tempMap = new EnumMap<>(Resource.class);
        temp.addEnumMap(table.turnOf().getStrongBox().getSelection());
        for(int i = 0; i < table.turnOf().getShelves().length; i++){
            tempMap.put(table.turnOf().getShelves()[i].getResourceType(), 1);
            temp.addEnumMap(tempMap);
            tempMap.clear();
        }
        for(int i = 0; i < table.turnOf().getLeaderCards().length; i++){
            try {
                temp.addEnumMap(table.turnOf().getLeaderCards()[i].getAbility().getSelected());
            } catch (WeDontDoSuchThingsHere e) {

            }
        }

        tempMap = temp.removeEnumMapIfPossible(table.turnOf().getSupportContainer().content());
        if(tempMap.isEmpty())
            return true;
        else
            return false;

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
                table.turnOf().setErrorMessage("This Slot can't contain your card. ");
            } catch (IndexOutOfBoundsException e) {
                table.turnOf().setErrorMessage("Wrong selection: there is not such slot. ");
            }
        }
    }
}
