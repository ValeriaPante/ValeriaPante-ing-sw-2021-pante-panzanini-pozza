package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class TurnOfMessage extends WithIntMessage{

    public TurnOfMessage(int playerId) {
        this.id = playerId;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
