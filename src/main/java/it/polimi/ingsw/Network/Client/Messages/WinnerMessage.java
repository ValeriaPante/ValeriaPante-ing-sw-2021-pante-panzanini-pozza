package it.polimi.ingsw.Network.Client.Messages;

import it.polimi.ingsw.Network.Client.Visitor;

public class WinnerMessage extends WithIntMessage{

    public WinnerMessage(int winnerId) {
        this.id = winnerId;
    }

    @Override
    public void visit(Visitor v){
        v.updateModel(this);
    }
}
