package it.polimi.ingsw.Player;

import it.polimi.ingsw.Cards.DevCard;
import it.polimi.ingsw.Cards.DevCardType;

import java.util.*;

public class DevSlot{
    private List<DevCard> cards;

    public DevSlot(){
        cards = new ArrayList<>();
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

    public void addCard(DevCard card){
        cards.add(0, card.clone());
    }

    public ArrayList<DevCardType> getDevCardTypeContained(){
        ArrayList<DevCardType> result = new ArrayList<>();
        for (DevCard t : cards){
            result.add(new DevCardType(t.getType().getLevel(), t.getType().getColor()));
        }
        return result;
    }
}
