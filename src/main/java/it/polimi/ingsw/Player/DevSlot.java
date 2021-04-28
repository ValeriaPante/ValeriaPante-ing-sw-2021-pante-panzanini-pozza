package it.polimi.ingsw.Player;

import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.DevCardType;
import it.polimi.ingsw.Exceptions.*;

import java.util.*;

public class DevSlot{
    private List<DevCard> cards;
    private boolean selected;

    public DevSlot(){
        cards = new ArrayList<>();
        selected = false;
    }

    public List<DevCard> getCards(){
        List<DevCard> result = new ArrayList<>();
        int i;
        for (i = 0; i < cards.size(); i++){
            result.add(cards.get(i).clone());
        }
        return result;
    }

    public int numberOfCards(){
        return cards.size();
    }

    public boolean isEmpty(){
        return cards.isEmpty();
    }

    public DevCard topCard(){
        return cards.get(0).clone();
    }

    public void addCard(DevCard card) throws CantPutThisHere{
        if(this.isInsertable(card)) {
            cards.add(0, card.clone());
        } else {
            throw new CantPutThisHere();
        }
    }

    public boolean isInsertable(DevCard card){
        return ((this.isEmpty() && card.getType().getLevel() == 1)) || (card.getType().getLevel() == this.topCard().getType().getLevel() + 1);
    }

    public ArrayList<DevCardType> getDevCardTypeContained(){
        ArrayList<DevCardType> result = new ArrayList<>();
        for (DevCard t : cards){
            result.add(new DevCardType(t.getType().getLevel(), t.getType().getColor()));
        }
        return result;
    }

    public int totalPoints(){
        int result = 0;
        for(DevCard card: this.cards)
            result += card.getVictoryPoints();
        return result;
    }

}
