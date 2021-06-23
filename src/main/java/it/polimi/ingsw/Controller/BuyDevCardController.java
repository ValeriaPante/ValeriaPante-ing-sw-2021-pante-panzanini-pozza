package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Model.Cards.DevCard;
import it.polimi.ingsw.Model.Cards.LeaderCard;
import it.polimi.ingsw.Model.Decks.DevDeck;
import it.polimi.ingsw.Model.Deposit.Depot;
import it.polimi.ingsw.Model.Deposit.Payable;
import it.polimi.ingsw.Enums.MacroTurnType;
import it.polimi.ingsw.Enums.MicroTurnType;
import it.polimi.ingsw.Enums.Resource;
import it.polimi.ingsw.Exceptions.CantPutThisHere;
import it.polimi.ingsw.Exceptions.GameOver;
import it.polimi.ingsw.Exceptions.WrongLeaderCardType;
import it.polimi.ingsw.Model.Deposit.StrongBox;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class BuyDevCardController extends CardActionController{
    private final ArrayList<Integer> appliedDiscounts;

    public BuyDevCardController(FaithTrackController ftc){
        super(ftc);
        appliedDiscounts = new ArrayList<>();
    }

    public void chooseDevCard(int chosenDeck){
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if(table.turnOf().getMacroTurnType() == MacroTurnType.NONE) {
            try {
                if(!table.getDevDecks()[chosenDeck - 1].isEmpty()) {
                    if (!atLeastOneDevSlotIsAvailable(table.getDevDecks()[chosenDeck - 1].getTopCard())) {
                        table.turnOf().setErrorMessage("You can't buy this card, there is no slot to contain it. ");
                    } else if(!isAffordableSomehow(table.getDevDecks()[chosenDeck - 1].getTopCard().getCost())){
                        table.turnOf().setErrorMessage("You can't buy this card, you don't have enough resources. ");
                    }else {
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
        } else table.turnOf().setErrorMessage("You can't do this action");
    }

    private boolean atLeastOneDevSlotIsAvailable(DevCard card){
        boolean result = false;
        for(int i = 0; i < table.turnOf().getDevSlots().length; i++)
            result = result || table.turnOf().getDevSlots()[i].isInsertable(card);
        return result;
    }

    private boolean thereIsASelection(){
        for(DevDeck deck: table.getDevDecks()){
            if(deck.getTopCard().isSelected())
                return true;
        }
        return false;
    }

    public void buyDevCard(){
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if (this.thereIsASelection() && table.turnOf().getMacroTurnType() == MacroTurnType.NONE){
            table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
            table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
            for(DevDeck deck: table.getDevDecks()){
                if(deck.getTopCard().isSelected()) {
                    StrongBox temp = table.turnOf().getSupportContainer();
                    temp.clear();
                    temp.addEnumMap(deck.getTopCard().getCost());
                    break;
                }
            }
        } else table.turnOf().setErrorMessage("You can't do this action");
    }

    public void applyDiscountAbility(int id){
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if (table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() &&  table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            LeaderCard card = getUsableLeaderCard(id);
            if (card == null) table.turnOf().setErrorMessage("This Leader Card doesn't exist or hasn't been played. ");
            else{
                if(appliedDiscounts.contains(id)) return;
                EnumMap<Resource, Integer> toBePaid = table.turnOf().getSupportContainer().content();
                try {
                    toBePaid.replaceAll((k, v) -> v - ((card.getAbility().getDiscount().get(k) != null) ? card.getAbility().getDiscount().get(k) : 0));
                    for(Map.Entry<Resource, Integer> entry: toBePaid.entrySet())
                        if(entry.getValue() == 0)
                            toBePaid.remove(entry.getKey());

                    table.turnOf().getSupportContainer().clear();
                    if(toBePaid.isEmpty()){
                        table.turnOf().setMicroTurnType(MicroTurnType.ANY_DECISION);
                    } else {
                        table.turnOf().getSupportContainer().addEnumMap(toBePaid);
                    }

                } catch (WrongLeaderCardType e){
                    table.turnOf().setErrorMessage("This Leader Card has not a discount ability. ");
                }
            }
        } else table.turnOf().setErrorMessage("You can't do this action");
    }

    public boolean selectionFromShelf(Resource resType, int numberOfShelf){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            selectFromShelf(resType, numberOfShelf);
            return true;
        }
        return false;
    }

    public boolean selectionFromLeaderStorage(Resource resType, int serial, int resPosition){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            selectFromLeaderStorage(resType, serial, resPosition);
            return true;
        }
        return false;
    }

    public boolean selectionFromStrongBox(Resource resType, int quantity){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            selectFromStrongBox(resType, quantity);
            return true;
        }
        return false;
    }

    public boolean deselectionFromShelf(Resource resType, int numberOfShelf){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP) {
            deselectFromShelf(resType, numberOfShelf);
            return true;
        }
        return false;
    }

    public boolean deselectionFromStrongBox(Resource resType, int quantity){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            deselectFromStrongBox(resType, quantity);
            return true;
        }
        return false;
    }

    public void paySelected(){
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            try{
                if(!this.isEnough()){
                    table.turnOf().setErrorMessage("Your selection doesn't match the cost. You selected too many resources.");
                    return;
                }
            } catch (IndexOutOfBoundsException e){
                table.turnOf().setErrorMessage("Your selection doesn't match the cost. You selected too few resources. ");
                return;
            }

            for(Payable payable: this.getPayableWithSelection())
                payable.pay();
            table.turnOf().setMicroTurnType(MicroTurnType.ANY_DECISION);
        } else table.turnOf().setErrorMessage("You can't do this action");
    }


    //controlla che le risorse selezionate siano uguali al costo della carta
    private boolean isEnough(){
        Depot temp = new Depot();
        EnumMap<Resource, Integer> tempMap = new EnumMap<>(Resource.class);
        if(table.turnOf().getStrongBox().getSelection() != null){
            temp.addEnumMap(table.turnOf().getStrongBox().getSelection());
        }
        for(int i = 0; i < table.turnOf().getShelves().length; i++){
            if(!table.turnOf().getShelves()[i].isEmpty()){
                tempMap.put(table.turnOf().getShelves()[i].getResourceType(), table.turnOf().getShelves()[i].getQuantitySelected());
                temp.addEnumMap(tempMap);
                tempMap.clear();
            }
        }
        for(int i = 0; i < table.turnOf().getLeaderCards().length; i++){
            if(table.turnOf().getLeaderCards()[i].hasBeenPlayed()){
                try {
                    temp.addEnumMap(table.turnOf().getLeaderCards()[i].getAbility().getSelected());
                } catch (WrongLeaderCardType e) {

                }
            }
        }

        tempMap = temp.removeEnumMapWhatPossible(table.turnOf().getSupportContainer().content());
        return tempMap == null;

    }

    public void chooseDevSlot(int numberOfSlot) throws GameOver {
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.ANY_DECISION){
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
                    table.turnOf().setMicroTurnType(MicroTurnType.NONE);
                    table.turnOf().setMacroTurnType(MacroTurnType.DONE);
                    appliedDiscounts.clear();
                    if(table.turnOf().getNumberOfDevCardOwned() == 7){
                        table.setLastLap();
                        throw new GameOver();
                    }
                } catch (CantPutThisHere e) {
                    table.turnOf().setErrorMessage("This Slot can't contain your card. ");
                } catch (IndexOutOfBoundsException e) {
                    table.turnOf().setErrorMessage("Wrong selection: there is not such slot. ");
                }
            }
        } else table.turnOf().setErrorMessage("You can't do this action");
    }
}
