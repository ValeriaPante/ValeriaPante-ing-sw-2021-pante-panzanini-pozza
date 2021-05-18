package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class NewPlayerPositionMessage extends WithIntMessage{

    private final int position;

    public NewPlayerPositionMessage(int playerId, int position) {
        this.id = playerId;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public boolean visit(Visitor v){
        v.updateModel(this);
        return false;
    }
}
