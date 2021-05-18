package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class RemoveLobbyMessage extends WithIntMessage{

    public RemoveLobbyMessage(int lobbyId) {
        this.id = lobbyId;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
