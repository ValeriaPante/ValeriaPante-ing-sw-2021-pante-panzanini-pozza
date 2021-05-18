package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Enums.PopeFavorCardState;
import it.polimi.ingsw.Network.Client.Visitor;

public class PopeFavourCardStateMessage extends WithIntMessage{

    private final PopeFavorCardState[] cards;

    public PopeFavourCardStateMessage(int playerId, PopeFavorCardState[] cards) {
        this.id = playerId;
        this.cards = cards;
    }

    public PopeFavorCardState[] getCards() {
        return cards;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
