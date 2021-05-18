package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class NewTopCardMessage extends WithIntMessage{
    private final int numberOfDeck;

    public NewTopCardMessage(int cardId, int numberOfDeck) {
        this.id = cardId;
        this.numberOfDeck = numberOfDeck;
    }

    public int getNumberOfDeck() {
        return numberOfDeck;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
