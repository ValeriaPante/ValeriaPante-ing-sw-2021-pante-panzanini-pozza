package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class NewDevCardMessage extends WithIntMessage{

    private final int cardId;
    private final int numberOfSlot;

    public NewDevCardMessage(int playerId, int cardId, int numberOfSlot) {
        this.id = playerId;
        this.cardId = cardId;
        this.numberOfSlot = numberOfSlot;
    }

    public int getCardId() {
        return cardId;
    }

    public int getNumberOfSlot() {
        return numberOfSlot;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
