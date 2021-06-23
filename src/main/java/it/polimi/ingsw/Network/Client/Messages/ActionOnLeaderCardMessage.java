package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class ActionOnLeaderCardMessage extends WithIntMessage{
    private String type = "actionOnLeaderCard";
    private final boolean discard;
    private final int cardId;

    public ActionOnLeaderCardMessage(int playerId, boolean discard, int cardId) {
        this.id = playerId;
        this.discard = discard;
        this.cardId = cardId;
    }

    public boolean isDiscard() {
        return discard;
    }

    public int getCardId() {
        return cardId;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
