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

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;


/**
 * Controller that manages the process of buying a new development card
 */
public class BuyDevCardController extends CardActionController{
    private final ArrayList<Integer> appliedDiscounts;

    /**
     * Controller constructor
     * @param ftc controller that manages actions on the faith track
     */
    public BuyDevCardController(FaithTrackController ftc){
        super(ftc);
        appliedDiscounts = new ArrayList<>();
    }


    /**
     * Selects the card on top of the chosen deck if possible (if a card had been chosen before, it will be deselected);
     * sets an error message in the following cases:
     * - player has already made his turn
     * - chosen number of deck doesn't exist
     * - chosen deck is empty
     * - there is no available slot to contain the card
     * - player doesn't own the required resources to buy the card
     * @param chosenDeck number of deck from which the player chose to buy the card on top
     */
    public void chooseDevCard(int chosenDeck){
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if(table.turnOf().getMacroTurnType() == MacroTurnType.NONE) {
            try {
                if(!table.getDevDecks()[chosenDeck - 1].isEmpty()) {
                    int cardId = table.getDevDecks()[chosenDeck-1].getTopCard().getId();
                    if (!atLeastOneDevSlotIsAvailable(table.getDevDecks()[chosenDeck - 1].getTopCard())) {
                        table.turnOf().setErrorMessage("You can't buy this card, there is no slot to contain it. ", cardId);
                    } else if(!isAffordableSomehow(table.getDevDecks()[chosenDeck - 1].getTopCard().getCost())){
                        table.turnOf().setErrorMessage("You can't buy this card, you don't have enough resources. ", cardId);
                    }else {
                        for(DevDeck deck: table.getDevDecks()){
                            DevCard card = deck.getTopCard();
                            if(card != null && card.isSelected())
                                deck.selectTopCard();
                        }
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


    /**
     * Checks if the player can place the chosen card on one of his slots
     * @param card chosen card
     * @return true if he can place the card, false otherwise
     */
    private boolean atLeastOneDevSlotIsAvailable(DevCard card){
        boolean result = false;
        for(int i = 0; i < table.turnOf().getDevSlots().length; i++)
            result = result || table.turnOf().getDevSlots()[i].isInsertable(card);
        return result;
    }

    /**
     * Checks if the player had already chose a card
     * @return true if a selection had already been made, false otherwise
     */
    private boolean thereIsASelection(){
        for(DevDeck deck: table.getDevDecks()){
            try {
                if(deck.getTopCard() != null && deck.getTopCard().isSelected())
                    return true;
            } catch (IndexOutOfBoundsException ignored){

            }
        }
        return false;
    }

    /**
     * Makes effective the choice of buying the selected development card
     * sets an error in case player hasn't the previous step in the process of buying a new development card
     */
    public void buyDevCard(){
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if (this.thereIsASelection() && table.turnOf().getMacroTurnType() == MacroTurnType.NONE){
            table.turnOf().setMacroTurnType(MacroTurnType.BUY_NEW_CARD);
            table.turnOf().setMicroTurnType(MicroTurnType.SETTING_UP);
            for(DevDeck deck: table.getDevDecks()){
                if(deck.getTopCard()!= null && deck.getTopCard().isSelected()) {
                    table.updatePlayerOfTurnSupportContainer(deck.getTopCard().getCost());
                    break;
                }
            }
        } else table.turnOf().setErrorMessage("You can't do this action");
    }


    /**
     * Applies the discount of a leader card chosen by the player;
     * sets an error message in the following cases:
     * - player doesn't own that card
     * - player didn't activate the card
     * - chosen card has not a discount ability
     * - player has already made his turn
     * - player hasn't the previous step in the process of buying a new development card
     * @param id chosen leader card id
     */
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

                    table.updatePlayerOfTurnSupportContainer(toBePaid);

                    if(toBePaid.isEmpty()){
                        table.turnOf().setMicroTurnType(MicroTurnType.ANY_DECISION);
                    }
                } catch (WrongLeaderCardType e){
                    table.turnOf().setErrorMessage("This Leader Card has not a discount ability. ");
                }
            }
        } else table.turnOf().setErrorMessage("You can't do this action");
    }

    /**
     * Selects a resource from a shelf
     * @param resType type of resource to select
     * @param numberOfShelf number of shelf to select from
     * @return true if the selection was successfully made, false otherwise
     */
    public boolean selectionFromShelf(Resource resType, int numberOfShelf){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            selectFromShelf(resType, numberOfShelf);
            return true;
        }
        return false;
    }

    /**
     * Selects a resource from a leader card with storage ability
     * @param resType type of resource to select
     * @param serial id of the leader card with storage ability
     * @param resPosition position of the resource inside the storage
     * @return true if the selection was successfully made, false otherwise
     */
    public boolean selectionFromLeaderStorage(Resource resType, int serial, int resPosition){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            selectFromLeaderStorage(resType, serial, resPosition);
            return true;
        }
        return false;
    }

    /**
     * Selects a resource from the strongbox
     * @param resType type of resource to select
     * @param quantity quantity of the chosen resource to select
     * @return true if the selection was successfully made, false otherwise
     */
    public boolean selectionFromStrongBox(Resource resType, int quantity){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            selectFromStrongBox(resType, quantity);
            return true;
        }
        return false;
    }

    /**
     * Deselects a resource from a shelf
     * @param resType type of resource to deselect
     * @param numberOfShelf number of shelf to deselect from
     * @return true if the deselection was successfully made, false otherwise
     */
    public boolean deselectionFromShelf(Resource resType, int numberOfShelf){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP) {
            deselectFromShelf(resType, numberOfShelf);
            return true;
        }
        return false;
    }

    /**
     * Deselects a resource from the strongbox
     * @param resType type of resource to deselect
     * @param quantity quantity of the chosen resource to deselect
     * @return true if the deselection was successfully made, false otherwise
     */
    public boolean deselectionFromStrongBox(Resource resType, int quantity){
        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){
            deselectFromStrongBox(resType, quantity);
            return true;
        }
        return false;
    }

    /**
     * Makes effective the payment of the selected resources;
     * sets an error message int he following cases:
     * - the total selection doesn't match the cost of the card
     * - player has already made his turn
     * - player hasn't the previous step in the process of buying a new development card
     */
    public void paySelected(){
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.SETTING_UP){

            if(!this.isEnough()){
                table.turnOf().setErrorMessage("Your selection doesn't match the cost.");
                return;
            }

            for(Payable payable: this.getPayableWithSelection())
                table.payPlayerOfTurn(payable);
            table.turnOf().setMicroTurnType(MicroTurnType.ANY_DECISION);
        } else table.turnOf().setErrorMessage("You can't do this action");
    }


    /**
     * Checks if the total selection of resources to pay matches the cost of the chosen card
     * @return true if the selection matches the cost, false otherwise
     */
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
                } catch (WrongLeaderCardType ignored) {

                }
            }
        }

        EnumMap<Resource, Integer> selected = temp.content();
        if(selected == null) selected = new EnumMap<>(Resource.class);
        EnumMap<Resource, Integer> supportContainerContent = table.turnOf().getSupportContainer().content();
        if(supportContainerContent == null) supportContainerContent = new EnumMap<>(Resource.class);
        return selected.equals(supportContainerContent);

    }

    /**
     * Puts the bought card on top of the chosen slot;
     * sets an error message in the following cases:
     * - player has already made his turn
     * - player hasn't the previous step in the process of buying a new development card
     * - the chosen slot can't contain the bought card
     * - number of slot is wrong
     * @param numberOfSlot number of the slot to put the card onto
     * @throws GameOver when player bought his 7th card (in this case the game must end)
     */
    public void chooseDevSlot(int numberOfSlot) throws GameOver {
        table.turnOf().clearErrorMessage();
        table.clearBroadcastMessage();

        if(table.turnOf().getMacroTurnType() == MacroTurnType.BUY_NEW_CARD && this.thereIsASelection() && table.turnOf().getMicroTurnType() == MicroTurnType.ANY_DECISION){
            DevDeck[] devDecks = table.getDevDecks();
            int numberOfDeck = 0;
            for (numberOfDeck = 0; numberOfDeck<devDecks.length; numberOfDeck++){
                if (devDecks[numberOfDeck].getTopCard()!= null && devDecks[numberOfDeck].getTopCard().isSelected()){
                    break;
                }
            }

            if (numberOfDeck<devDecks.length){
                try {
                    table.updatePlayerOfTurnDevSlot(numberOfSlot, devDecks[numberOfDeck].getTopCard());
                    table.drawDevDeck(numberOfDeck);
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
