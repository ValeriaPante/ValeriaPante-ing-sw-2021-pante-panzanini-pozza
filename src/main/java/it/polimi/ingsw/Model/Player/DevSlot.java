package it.polimi.ingsw.Model.Player;

import it.polimi.ingsw.Model.Cards.DevCard;
import it.polimi.ingsw.Model.Cards.DevCardType;
import it.polimi.ingsw.Exceptions.*;

import java.util.*;

/**
 * Representation of Slot where the player puts his development cards
 */
public class DevSlot{
    private final List<DevCard> cards;

    /**
     * Constructor
     */
    public DevSlot(){
        cards = new ArrayList<>();
    }

    /**
     * Getter of the card contained in the slot
     * @return a list of the development cards contained in the slot
     */
    public List<DevCard> getCards(){
        List<DevCard> result = new ArrayList<>();
        int i;
        for (i = 0; i < cards.size(); i++){
            result.add(cards.get(i).clone());
        }
        return result;
    }

    /**
     * Selects the card on top of the slot
     */
    public void selectTopCard(){
        if (!this.isEmpty()){
            this.cards.get(0).select();
        }
    }

    /**
     * Getter of the number of cards contained
     * @return the number of cards contained in the slot
     */
    public int numberOfCards(){
        return cards.size();
    }

    /**
     * Checks if the slot is empty
     * @return true if the slot is empty, false otherwise
     */
    public boolean isEmpty(){
        return cards.isEmpty();
    }

    /**
     * Getter of the card on top of the slot
     * @return development card on top of the slot
     */
    public DevCard topCard(){
        if(!this.isEmpty()) return cards.get(0).clone();
        else return  null;
    }

    /**
     * Add a card on top of the slot
     * @param card development card to place
     * @throws CantPutThisHere if the card can't be placed on top of the others contained in the slot
     */
    public void addCard(DevCard card) throws CantPutThisHere{
        if(this.isInsertable(card)) {
            cards.add(0, card.clone());
        } else {
            throw new CantPutThisHere();
        }
    }

    /**
     * Checks if a development card can be placed on top of the slot
     * @param card card player wants to place
     * @return true if the card can be placed on top, false otherwise
     */
    public boolean isInsertable(DevCard card){
        return ((this.isEmpty() && card.getType().getLevel() == 1)) || (this.topCard() != null && (card.getType().getLevel() == this.topCard().getType().getLevel() + 1));
    }

    /**
     * Getter of the types (color-level) of development card contained in the slot
     * @return the list of the types contained
     */
    public ArrayList<DevCardType> getDevCardTypeContained(){
        ArrayList<DevCardType> result = new ArrayList<>();
        for (DevCard t : cards){
            result.add(new DevCardType(t.getType().getLevel(), t.getType().getColor()));
        }
        return result;
    }

    /**
     * Getter of the total points of the cards contained
     * @return the sum of the victory points of the development card contained in the slot
     */
    public int totalPoints(){
        int result = 0;
        for(DevCard card: this.cards)
            result += card.getVictoryPoints();
        return result;
    }

}
